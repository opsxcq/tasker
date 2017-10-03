package sh.strm.tasker.runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient.LogsParam;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerConfig.Builder;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerExit;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.NetworkConfig;

import sh.strm.tasker.Configuration;
import sh.strm.tasker.task.DockerTask;
import sh.strm.tasker.util.Docker;

public class DockerTaskRunner extends Runner<DockerTask> {

	static Logger log = Logger.getLogger(DockerTaskRunner.class.getName());

	private DefaultDockerClient docker;
	private Docker client;

	@Autowired
	private Configuration config;

	//private Swarm swarm;
	//private boolean isSwarm;

	public DockerTaskRunner() throws Exception {
		this.docker = DefaultDockerClient.fromEnv().build();
		this.client = new Docker(this.docker);
		// Check swarm status

		//		try {
		//			this.swarm = this.docker.inspectSwarm();
		//			if (this.swarm != null && this.swarm.id() != null) {
		//				this.isSwarm = true;
		//			}
		//		} catch (Exception e) {
		//			// OK, It will show that this node isn't a docker swarm node or manager
		//		}
	}

	public TaskExecutionResult executeTask(DockerTask task) throws Exception {

		if (!this.client.hasImage(task.getImage()) || task.isAlwaysPull()) {
			this.client.pullImage(task.getImage());
		}

		String containerId = this.client.getContainerIdByName(task.getName());
		if (containerId != null) {
			if (!task.isReuseContainer()) {
				// Delete it
				this.client.removeContainer(task.getName());
				containerId = createContainer(task);
			}
		} else {
			containerId = createContainer(task);
		}

		log.info("Starting container " + containerId + " for task " + task.getName());
		docker.startContainer(containerId);

		checkContainerCreationState(containerId);

		TaskExecutionResult result = collectTaskExecutionResult(task, containerId);

		if (!task.isKeepContainerAfterExecution() && !task.isReuseContainer()) {
			log.info("Removing finished container " + containerId);
			docker.removeContainer(containerId);
		}

		return result;
	}

	private String createContainer(DockerTask task) throws Exception {
		final ContainerConfig containerConfig = configureContainer(task);
		final ContainerCreation creation = docker.createContainer(containerConfig, task.getName());
		return creation.id();
	}

	private ContainerConfig configureContainer(DockerTask task) throws Exception {
		Builder container = ContainerConfig.builder().image(task.getImage());
		com.spotify.docker.client.messages.HostConfig.Builder hostConfig = HostConfig.builder();

		configureTargetExecutable(task, container);

		configureVariables(task, container);

		configureVolumes(task, hostConfig);

		configurePorts(task, container);

		configureNetwork(task, container, hostConfig);

		// Set host config
		container.hostConfig(hostConfig.build());

		final ContainerConfig containerConfig = container.build();
		return containerConfig;
	}

	private TaskExecutionResult collectTaskExecutionResult(DockerTask task, String containerId)
			throws DockerException, InterruptedException {
		final ContainerExit exit = docker.waitContainer(containerId);
		log.info("Container " + containerId + " finished with exit code " + exit.statusCode());

		TaskExecutionResult result = new TaskExecutionResult(task);
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

		return result;
	}

	private void configureNetwork(DockerTask task, Builder container, com.spotify.docker.client.messages.HostConfig.Builder hostConfig)
			throws Exception {
		// Configure container network
		String networkName = task.getNetwork();
		if (networkName != null && !networkName.equals("")) {
			createNetworkIfDoesntExist(networkName);
			hostConfig.networkMode(networkName);
		}

		// Configure the container name to be the task name
		container.hostname(task.getName());
	}

	private void configurePorts(DockerTask task, Builder container) {
		// Port mappings
		if (task.getPorts() != null) {
			container.exposedPorts(task.getPorts());
		}
	}

	private void configureVolumes(DockerTask task, com.spotify.docker.client.messages.HostConfig.Builder hostConfig) {
		// Volumes
		if (task.getVolumes() != null) {
			for (String volume : task.getVolumes()) {
				if (volume != null) {
					hostConfig.appendBinds(volume);
				}
			}
		}
	}

	private void configureTargetExecutable(DockerTask task, Builder container) {

		// If we got a script property, ignore everything else and use this
		if (task.getScript() != null) {
			container.entrypoint("/bin/sh");
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

			container.cmd("-c", arguments.toString());
		} else {
			if (task.getEntrypoint() != null) {
				container.entrypoint(task.getEntrypoint());
			}

			if (task.getArguments() != null) {
				container.cmd(task.getArguments());
			}
		}
	}

	private void configureVariables(DockerTask task, Builder container) {
		// Environment variables
		List<String> environment = new ArrayList<String>();

		if (this.config.getGlobalEnvironment() != null) {
			environment.addAll(Arrays.asList(this.config.getGlobalEnvironment()));
		}

		if (task.getEnvironment() != null) {
			environment.addAll(Arrays.asList(task.getEnvironment()));
		}

		if (environment.size() > 0) {
			container.env(environment);
		}
	}

	private void createNetworkIfDoesntExist(String networkName) throws Exception {
		synchronized (DockerTaskRunner.class) {
			if (!this.client.hasNetwork(networkName)) {
				com.spotify.docker.client.messages.NetworkConfig.Builder networkConfig = NetworkConfig.builder();
				networkConfig.name(networkName);
				networkConfig.attachable(true);
				docker.createNetwork(networkConfig.build());
			}
		}
	}

	private void checkContainerCreationState(String containerId) throws DockerException, InterruptedException {
		String error = docker.inspectContainer(containerId).state().error();
		if (error != null && !error.equals("")) {
			throw new IllegalStateException("Container " + containerId + " creation failed:" + error);
		}
	}

}
