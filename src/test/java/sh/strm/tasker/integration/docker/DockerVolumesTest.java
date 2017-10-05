package sh.strm.tasker.integration.docker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.integration.docker.DockerAllTests.CustomTestYamlInitialization;
import sh.strm.tasker.runner.DockerTaskRunner;
import sh.strm.tasker.runner.TaskExecutionResult;
import sh.strm.tasker.task.DockerTask;
import sh.strm.tasker.util.Docker;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = CustomTestYamlInitialization.class)
public class DockerVolumesTest {

	@Autowired
	private TaskConfiguration conf;

	@Autowired
	private DockerTaskRunner dockerRunner;

	@Autowired
	private Docker client;

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

		client.removeVolume("testVolume");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerVolumeParseError() throws Exception {
		DockerTask task = new DockerTask();
		task.setVolumes("ItWontWork");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerVolumeParseError2() throws Exception {
		DockerTask task = new DockerTask();
		task.setVolumes("ItWontWork=2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerVolumeParseError3() throws Exception {
		DockerTask task = new DockerTask();
		task.setVolumes("ItWontWork:");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerVolumeParseError4() throws Exception {
		DockerTask task = new DockerTask();
		task.setVolumes(":ItWontWork");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerVolumeParseError5() throws Exception {
		DockerTask task = new DockerTask();
		task.setVolumes(":ItWontWork:");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerVolumeParseError6() throws Exception {
		DockerTask task = new DockerTask();
		task.setVolumes("ItWontWork=");
	}

	@Test
	public void testDockerVolumeNull() throws Exception {
		DockerTask task = new DockerTask();
		String[] volumes = null;
		task.setVolumes(volumes);
	}

	@Test
	public void testDockerVolumeNullArray() throws Exception {
		DockerTask task = new DockerTask();
		String[] volumes = { null };
		task.setVolumes(volumes);
	}
}
