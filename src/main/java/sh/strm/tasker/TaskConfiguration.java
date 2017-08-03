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
		this.docker = docker;
	}

	public List<DockerTask> getDocker() {
		return docker;
	}

	public List<DockerTask> getTasks() {
		return this.docker;
	}

	@Override
	public String toString() {
		return "TaskerConfiguration [docker=" + docker + "]";
	}

}