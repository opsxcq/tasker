package sh.strm.tasker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sh.strm.tasker.integration.DockerTaskRunnerTest;

@RunWith(Suite.class)
@SuiteClasses({ TaskerApplicationTests.class, DockerTaskRunnerTest.class })
public class AllTests {

}
