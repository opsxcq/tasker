package sh.strm.tasker.notifier;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ NotifierTests.class, EmailNotifierTests.class })
public class NotifierAllTests {

}
