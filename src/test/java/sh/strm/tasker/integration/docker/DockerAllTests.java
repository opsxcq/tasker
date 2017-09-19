package sh.strm.tasker.integration.docker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DockerEnvironmentVariablesTest.class, DockerPortsTest.class, DockerTaskRunnerTest.class, DockerVolumesTest.class })
public class DockerAllTests {

}
