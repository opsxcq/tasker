package sh.strm.tasker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sh.strm.tasker.integration.docker.DockerAllTests;
import sh.strm.tasker.notifier.NotifierAllTests;
import sh.strm.tasker.setup.SetupAllTests;

@RunWith(Suite.class)
@SuiteClasses({ TaskerApplicationTests.class, DockerAllTests.class, SchedulerTests.class, NotifierAllTests.class, SetupAllTests.class })
public class AllTests {

}
