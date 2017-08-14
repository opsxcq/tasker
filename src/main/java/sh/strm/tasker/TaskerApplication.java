package sh.strm.tasker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.spotify.docker.client.DefaultDockerClient;

import sh.strm.tasker.runner.DockerTaskRunner;

@SpringBootApplication
public class TaskerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TaskerApplication.class, args);
	}
	
	@Bean
	public DockerTaskRunner getDefaultDockerClient() throws Exception{
		return new DockerTaskRunner();
	}
}
