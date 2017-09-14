package sh.strm.tasker.runner;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient.LogsParam;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerConfig.Builder;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerExit;
import com.spotify.docker.client.messages.HostConfig;

import sh.strm.tasker.task.DockerTask;

public class DockerTaskRunner extends Runner<DockerTask> {

	static Logger log = Logger.getLogger(DockerTaskRunner.class.getName());

	private DefaultDockerClient docker;

	public DockerTaskRunner() throws Exception {
		this.docker = DefaultDockerClient.fromEnv().build();
	}

	public TaskExecutionResult executeTask(DockerTask task) throws Exception {
		long timeStart = System.currentTimeMillis();
		log.info("Starting the execution of the " + task.getName() + " task");

		TaskExecutionResult result = new TaskExecutionResult(task);

		Builder container = ContainerConfig.builder().image(task.getImage());
		com.spotify.docker.client.messages.HostConfig.Builder hostConfig = HostConfig.builder();

		if (task.getEntrypoint() != null) {
			container = container.entrypoint(task.getEntrypoint());
		}

		if (task.getArguments() != null) {
			container = container.cmd(task.getArguments());
		}

		// If we got a script property, ignore everything else and use this
		if (task.getScript() != null) {
			container = container.entrypoint("/bin/sh");
			// TODO : Check if the container has /bin/sh executable

			// For every script line, create a monster script to pass to our shell
			StringBuilder arguments = new StringBuilder();
			if (task.isScriptStrict()) {
				Arrays.asList(task.getScript()).stream().forEach(line -> arguments.append(line).append("&&"));
				// Remove the last && because it doesn't compile in BASH
				arguments.setLength(arguments.length() - 2);
			} else {
				Arrays.asList(task.getScript()).stream().forEach(line -> arguments.append(line).append(";"));
			}

			container = container.cmd("-c", arguments.toString());
		}

		// Environment variables
		if (task.getEnvironment() != null) {
			container.env(task.getEnvironment());
		}

		// Volumes
		if (task.getVolumes() != null) {
			for (String volume : task.getVolumes()) {
				if (volume != null) {
					hostConfig = hostConfig.appendBinds(volume);
				}
			}
		}

		// Set host config
		container.hostConfig(hostConfig.build());

		final ContainerConfig containerConfig = container.build();

		final ContainerCreation creation = docker.createContainer(containerConfig);
		String containerId = creation.id();

		log.info("Starting container " + containerId + " for task " + task.getName());
		docker.startContainer(containerId);
		final ContainerExit exit = docker.waitContainer(containerId);

		log.info("Container " + containerId + " finished with exit code " + exit.statusCode());

		// TODO : Do some descent exception treatment
		if (exit.statusCode() != 0) {
			result.markAsFinished(exit.statusCode());
		} else {
			result.markAsFinishedWithSuccess();
		}

		log.info("Collecting container logs for " + containerId);

		String logs = docker.logs(containerId, LogsParam.stdout(), LogsParam.stderr()).readFully();

		// Remove the last new line if needed
		if (logs.endsWith("\n")) {
			logs = logs.substring(0, logs.length() - 1);
		}

		result.setOutput(logs);

		long timeFinished = System.currentTimeMillis();

		log.info("Task " + task.getName() + " finished in " + (timeFinished - timeStart));

		if (!task.isKeepContainerAfterExecution()) {
			log.info("Removing finished container " + containerId);
			docker.removeContainer(containerId);
		}

		return result;
	}

}
