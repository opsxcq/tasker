package sh.strm.tasker.integration.docker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.runner.DockerTaskRunner;
import sh.strm.tasker.runner.TaskExecutionResult;
import sh.strm.tasker.task.DockerTask;
import sh.strm.tasker.util.Docker;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DockerTaskRunnerTest {

	@Autowired
	private TaskConfiguration conf;

	@Autowired
	private DockerTaskRunner dockerRunner;

	@Autowired
	private Docker client;

	@Test
	public void testDockerRunContainerAlwaysPull() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloAlwaysPull");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertTrue(task.isAlwaysPull());
		assertEquals("green bar", result.getOutput());
	}

	@Test
	public void testDockerRunContainer() throws Exception {
		DockerTask task = conf.getDockerTaskByName("hello");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertEquals("green bar", result.getOutput());
	}

	@Test
	public void testDockerRunContainerOnlyEntrypoint() throws Exception {
		DockerTask task = conf.getDockerTaskByName("helloOnlyEntrypoint");
		TaskExecutionResult result = dockerRunner.executeTask(task);
		assertEquals("root", result.getOutput());
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

	@Test(expected = IllegalArgumentException.class)
	public void testDontAcceptDuplicatedTaskName() {
		DockerTask task1 = new DockerTask();
		task1.setName("task01");

		DockerTask task2 = new DockerTask();
		task2.setName("task02");

		TaskConfiguration config = new TaskConfiguration();

		config.setDocker(Arrays.asList(new DockerTask[] { task1, task2 }));

		// Must fail to add this;
		config.setDocker(Arrays.asList(new DockerTask[] { task1, task2, task2 }));
	}

	@Test
	public void testContainerReuse() throws Exception {
		// Just some cleanup before the test
		client.removeContainer("helloReuseContainer");

		DockerTask task = conf.getDockerTaskByName("helloReuseContainer");
		assertTrue(task.isReuseContainer());

		TaskExecutionResult resultFirst = dockerRunner.executeTask(task);
		assertEquals("", resultFirst.getOutput());

		TaskExecutionResult resultSecond = dockerRunner.executeTask(task);
		assertEquals("green bar", resultSecond.getOutput());

		assertTrue("Remove container", client.removeContainer("helloReuseContainer"));
	}

	@Test
	public void testContainerRemovalIfExists() throws Exception {
		// Test if container exists
		client.removeContainer("helloRemoveExistingContainer");

		DockerTask task = conf.getDockerTaskByName("helloRemoveExistingContainer");
		assertTrue(task.isKeepContainerAfterExecution());

		TaskExecutionResult resultFirst = dockerRunner.executeTask(task);
		assertEquals("green bar", resultFirst.getOutput());

		TaskExecutionResult resultSecond = dockerRunner.executeTask(task);
		assertEquals("green bar", resultSecond.getOutput());

		assertTrue("Remove container", client.removeContainer("helloRemoveExistingContainer"));
	}

	@Test(expected = IllegalStateException.class)
	public void testInvalidDockerEntrypointOrScript() throws Exception {
		DockerTask task = new DockerTask();
		task.check();
	}

}
