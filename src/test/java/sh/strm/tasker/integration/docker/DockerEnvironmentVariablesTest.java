package sh.strm.tasker.integration.docker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import sh.strm.tasker.Configuration;
import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.runner.DockerTaskRunner;
import sh.strm.tasker.runner.TaskExecutionResult;
import sh.strm.tasker.task.DockerTask;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DockerEnvironmentVariablesTest {

	@Autowired
	private TaskConfiguration conf;

	@Autowired
	private DockerTaskRunner dockerRunner;

	@Test
	public void testDockerRunContainerEnvironmentVariables() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloEnvironmentVariables");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertThat(result.getOutput()).isEqualTo("green bar");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerEnvironmentParseVariablesError() throws Exception {
		DockerTask task = new DockerTask();
		task.setEnvironment("ItWontWork");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerEnvironmentParseVariablesError2() throws Exception {
		DockerTask task = new DockerTask();
		task.setEnvironment("ItWontWork:2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerEnvironmentParseVariablesError3() throws Exception {
		DockerTask task = new DockerTask();
		task.setEnvironment("ItWontWork:");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerEnvironmentParseVariablesError4() throws Exception {
		DockerTask task = new DockerTask();
		task.setEnvironment("ItWontWork=");
	}

	//////////////////////////////////////////////////////////////////////////////////

	@Test
	public void testDockerRunContainerEnvironmentVariablesGlobal() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloEnvironmentVariablesGlobal");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertThat(result.getOutput()).isEqualTo("green bar");
	}

	@Test
	public void testDockerRunContainerEnvironmentVariablesGlobalOverride() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloEnvironmentVariablesGlobalOverride");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertThat(result.getOutput()).isEqualTo("green is the bar");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerEnvironmentParseVariablesGlobalError() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setGlobalEnvironment("ItWontWork");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerEnvironmentParseVariablesGlobalError2() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setGlobalEnvironment("ItWontWork:2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerEnvironmentParseVariablesGlobalError3() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setGlobalEnvironment("ItWontWork=");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerEnvironmentParseVariablesGlobalError4() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setGlobalEnvironment("=ItWontWork");
	}

	@Test()
	public void testDockerEnvironmentParseVariablesGlobalNull() throws Exception {
		Configuration configuration = new Configuration();
		String[] arguments = null;
		configuration.setGlobalEnvironment(arguments);
		assertThat(configuration.getGlobalEnvironment()).isNotNull();
	}
}
