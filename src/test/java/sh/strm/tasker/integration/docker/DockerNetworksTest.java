package sh.strm.tasker.integration.docker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicBoolean;

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
public class DockerNetworksTest {

	@Autowired
	private TaskConfiguration conf;

	@Autowired
	private DockerTaskRunner dockerRunner;

	@Test
	public void testDockerRunContainerWithNetwork() throws Exception {
		DockerTask taskWrite = conf.getDockerTaskByName("helloWithNetwork01");

		final AtomicBoolean secondSuccess = new AtomicBoolean(false);
		Thread otherContainer = new Thread(() -> {
			DockerTask taskRead = conf.getDockerTaskByName("helloWithNetwork02");
			try {
				TaskExecutionResult resultSecond = dockerRunner.executeTask(taskRead);
				secondSuccess.set(resultSecond.isSuccessful());
			} catch (Exception e) {
				System.err.println("Error on background container: " + e.getMessage());
			}
		});
		otherContainer.start();

		Thread.sleep(5000);

		TaskExecutionResult resultFirst = dockerRunner.executeTask(taskWrite);

		assertEquals("green bar", resultFirst.getOutput());
		assertTrue(resultFirst.isSuccessful());

		otherContainer.join(60000);
		assertTrue("Check if background container was succesful", secondSuccess.get());

		DockerUtils.removeNetwork("testNetwork");
	}

	@Test
	public void testDockerNetworkParseOK() throws Exception {
		DockerTask task = new DockerTask();
		task.setNetwork("ItWillWork");
	}

	@Test
	public void testDockerNetworkParseError() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setNetwork("");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

}
