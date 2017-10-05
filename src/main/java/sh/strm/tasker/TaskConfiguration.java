package sh.strm.tasker;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import sh.strm.tasker.task.DockerTask;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "tasks")
public class TaskConfiguration {

	private List<DockerTask> docker;

	public void setDocker(List<DockerTask> docker) {
		// If some name is removed (namesCount != total size), it means that we had a duplicated name 
		long namesCount = docker.stream().//
				map(task -> task.getName()).//
				distinct().count();
		if (namesCount != docker.size()) {
			throw new IllegalArgumentException("Task names must be unique");
		}

		this.docker = docker;
	}

	public List<DockerTask> getDocker() {
		return docker;
	}

	public List<DockerTask> getTasks() {
		return this.docker;
	}

	public DockerTask getDockerTaskByName(String name) {
		return getDocker().stream().filter(task -> task.getName().equals(name)).findFirst().orElse(null);
	}

}