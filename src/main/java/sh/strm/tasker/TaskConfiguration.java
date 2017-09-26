package sh.strm.tasker;

import java.util.HashSet;
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
		if (docker != null) {
			HashSet<String> names = new HashSet<String>();
			for (DockerTask task : docker) {
				if (task != null) {
					if (names.contains(task.getName())) {
						throw new IllegalArgumentException("Task names must be unique");
					}
					names.add(task.getName());
				}
			}
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

	@Override
	public String toString() {
		return "TaskConfiguration [docker=" + docker + "]";
	}

}