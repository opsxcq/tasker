package sh.strm.tasker.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.runner.DockerTaskRunner;
import sh.strm.tasker.runner.TaskExecutionResult;
import sh.strm.tasker.task.DockerTask;
import sh.strm.tasker.util.DockerUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DockerTaskRunnerTest {

	@Autowired
	private TaskConfiguration conf;

	@Autowired
	private DockerTaskRunner dockerRunner;

	@Test
	public void testDockerRunContainer() throws Exception {
		DockerTask task = conf.getDockerTaskByName("hello");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertEquals("green bar", result.getOutput());
	}

	@Test
	public void testDockerRunContainerScript() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloScript");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertEquals("green bar\ngreen barbar", result.getOutput());
	}

	@Test
	public void testDockerRunContainerScriptPipe() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloScriptPipe");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertEquals("green bar\ngreen barbar", result.getOutput());
	}

	@Test
	public void testDockerRunContainerScriptStrict() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloScriptStrict");
		assertTrue(task.isScriptStrict());
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertEquals("green bar", result.getOutput());
	}

	//////////////////////////////////////////////////////////////////////////////////

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

	//////////////////////////////////////////////////////////////////////////////////

	@Test
	public void testDockerRunContainerWithVolume() throws Exception {
		DockerTask taskWrite = conf.getDockerTaskByName("helloWithVolume01");

		// Set expected variable to be saved in a file in a shared volume
		String expected = "green bar " + Math.random();
		taskWrite.setEnvironment("expected=" + expected);

		TaskExecutionResult resultFirst = dockerRunner.executeTask(taskWrite);

		assertTrue(resultFirst.isSuccessful());

		DockerTask taskRead = conf.getDockerTaskByName("helloWithVolume02");

		TaskExecutionResult resultSecond = dockerRunner.executeTask(taskRead);
		assertEquals(expected, resultSecond.getOutput());

		DockerUtils.removeVolume("testVolume");
	}

	@Test
	public void testDockerVolumeParseError() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setVolumes("ItWontWork");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerVolumeParseError2() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setVolumes("ItWontWork=2");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

}
