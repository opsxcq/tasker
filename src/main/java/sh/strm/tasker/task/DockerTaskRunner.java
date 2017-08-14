package sh.strm.tasker.task;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient.LogsParam;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerConfig.Builder;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerExit;

public class DockerTaskRunner extends Runner {

	private DefaultDockerClient docker;

	public DockerTaskRunner() throws Exception {
		this.docker = DefaultDockerClient.fromEnv().build();
	}

	public String executeTask(DockerTask task) throws Exception {

		Builder container = ContainerConfig.builder().image(task.getImage());

		if (task.getEntrypoint() != null) {
			container = container.entrypoint(task.getEntrypoint());
		}

		if (task.getArguments() != null) {
			container = container.cmd(task.getArguments());
		}

		final ContainerConfig containerConfig = container.build();

		final ContainerCreation creation = docker.createContainer(containerConfig);
		String containerId = creation.id();

		docker.startContainer(containerId);

		final ContainerExit exit = docker.waitContainer(containerId);

		// TODO : Do some descente exception treatment
		if (exit.statusCode() != 0) {

		}

		String logs = docker.logs(containerId, LogsParam.stdout(), LogsParam.stderr()).readFully();

		// Remove the last new line if needed
		if (logs.endsWith("\n")) {
			logs = logs.substring(0, logs.length() - 1);
		}

		return logs;
	}

	@Override
	public boolean executeTask(Task task) {
		// TODO Auto-generated method stub
		return false;
	}

}
