package sh.strm.tasker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sh.strm.tasker.integration.docker.DockerAllTests;

@RunWith(Suite.class)
@SuiteClasses({ TaskerApplicationTests.class, DockerAllTests.class, SchedulerTests.class })
public class AllTests {

}
