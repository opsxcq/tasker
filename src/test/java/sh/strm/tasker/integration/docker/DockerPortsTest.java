package sh.strm.tasker.integration.docker;

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
	public void testDockerPorts() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("80:80");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseError() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("ItWontWork");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortseParseError2() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("ItWontWork=2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseError3() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("ItWontWork:");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseError4() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("ItWontWork:80");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseError5() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("80:ItWontWork");
	}

	@Test
	public void testDockerPortsNull() throws Exception {
		DockerTask task = new DockerTask();

		String[] ports = null;
		task.setPorts(ports);
	}

	@Test
	public void testDockerPortsNullArray() throws Exception {
		DockerTask task = new DockerTask();

		String[] ports = { null };
		task.setPorts(ports);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseErrorOutOfRange1() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("80:100000");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseErrorOutOfRange2() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("100000:80");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseErrorOutOfRange3() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("0:80");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseErrorOutOfRange4() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("80:0");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseErrorOutOfRange5() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("80:-20");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPortsParseErrorOutOfRange6() throws Exception {
		DockerTask task = new DockerTask();
		task.setPorts("80:-20");
	}

}
