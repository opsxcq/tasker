package sh.strm.tasker.integration.docker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
		assertEquals("green bar", result.getOutput());
	}

	@Test
	public void testDockerEnvironmentParseVariablesError() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setEnvironment("ItWontWork");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerEnvironmentParseVariablesError2() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setEnvironment("ItWontWork:2");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerEnvironmentParseVariablesError3() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setEnvironment("ItWontWork:");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerEnvironmentParseVariablesError4() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setEnvironment("ItWontWork=");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	//////////////////////////////////////////////////////////////////////////////////

	@Test
	public void testDockerRunContainerEnvironmentVariablesGlobal() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloEnvironmentVariablesGlobal");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertEquals("green bar", result.getOutput());
	}

	@Test
	public void testDockerRunContainerEnvironmentVariablesGlobalOverride() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloEnvironmentVariablesGlobalOverride");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertEquals("green is the bar", result.getOutput());
	}

	@Test
	public void testDockerEnvironmentParseVariablesGlobalError() throws Exception {
		try {
			Configuration configuration = new Configuration();
			configuration.setGlobalEnvironment("ItWontWork");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerEnvironmentParseVariablesGlobalError2() throws Exception {
		try {
			Configuration configuration = new Configuration();
			configuration.setGlobalEnvironment("ItWontWork:2");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerEnvironmentParseVariablesGlobalError3() throws Exception {
		try {
			Configuration configuration = new Configuration();
			configuration.setGlobalEnvironment("ItWontWork=");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}
}
