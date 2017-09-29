package sh.strm.tasker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import sh.strm.tasker.runner.DockerTaskRunner;
import sh.strm.tasker.util.Docker;

@SpringBootApplication
public class TaskerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskerApplication.class, args);
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}

	@Bean
	public DockerTaskRunner getDefaultDockerClient() throws Exception {
		return new DockerTaskRunner();
	}

	@Bean
	public Docker defaultClient() throws Exception {
		return new Docker();
	}

}
