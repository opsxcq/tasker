package sh.strm.tasker.integration.docker;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import sh.strm.tasker.integration.docker.DockerAllTests.CustomTestYamlInitialization;
import sh.strm.tasker.task.DockerTask;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = CustomTestYamlInitialization.class)
public class DockerPortsTest {

	@Test
	public void testDockerPortsParseError() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setPorts("ItWontWork");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerPortseParseError2() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setPorts("ItWontWork=2");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerPortsParseError3() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setPorts("ItWontWork:");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerPortsParseErrorOutOfRange1() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setPorts("80:100000");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerPortsParseErrorOutOfRange2() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setPorts("100000:80");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerPortsParseErrorOutOfRange3() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setPorts("0:80");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerPortsParseErrorOutOfRange4() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setPorts("80:0");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerPortsParseErrorOutOfRange5() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setPorts("80:-20");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testDockerPortsParseErrorOutOfRange6() throws Exception {
		try {
			DockerTask task = new DockerTask();
			task.setPorts("80:-20");
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

}
