package sh.strm.tasker.notifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.notifier.NotifierAllTests.CustomTestYamlInitialization;
import sh.strm.tasker.notify.Notifier;
import sh.strm.tasker.runner.DockerTaskRunner;
import sh.strm.tasker.runner.TaskExecutionResult;
import sh.strm.tasker.task.DockerTask;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = CustomTestYamlInitialization.class)
public class NotifierTests {

	@Autowired
	private TaskConfiguration conf;

	@Autowired
	private DockerTaskRunner dockerRunner;

	@Test
	public void testNotifyCallTrigger() throws Exception {
		checkTriggerCall("helloNotify", "green bar", "always", true);
	}

	@Test
	public void testNotifyAlways() {
		checkTriggerCall("helloNotifyAlways", "green bar", "always", true);
		checkTriggerCall("helloNotifyAlwaysError", null, "always", true);
	}

	@Test
	public void testNotifyOnError() {
		checkTriggerCall("helloNotifyOnError", "green bar", "on-error", false);
		checkTriggerCall("helloNotifyOnErrorError", null, "on-error", true);
	}

	@Test
	public void testNotifyOnSuccess() {
		checkTriggerCall("helloNotifyOnSuccess", "green bar", "on-success", true);
		checkTriggerCall("helloNotifyOnSuccessError", null, "on-success", false);
	}

	private void checkTriggerCall(String taskName, String expectedOutput, String when, boolean shouldCall) {
		DockerTask task = conf.getDockerTaskByName(taskName);

		assertThat(task.getNotifiers().size()).isEqualTo(0);
		Notifier notifier = spy(Notifier.class);
		notifier.setWhen(when);
		task.addNotifier(notifier);
		assertThat(task.getNotifiers().size()).isEqualTo(1);

		TaskExecutionResult result = dockerRunner.execute(task);
		if (expectedOutput != null) {
			assertThat(result.getOutput()).isEqualTo(expectedOutput);
			assertThat(result.isSuccessful()).isTrue();
		} else {
			assertThat(result.isSuccessful()).isFalse();
		}

		ArgumentCaptor<TaskExecutionResult> captor = ArgumentCaptor.forClass(TaskExecutionResult.class);
		if (shouldCall) {
			verify(notifier, times(1)).trigger(captor.capture());
			assertThat(result).isEqualTo(captor.getValue());
		} else {
			verify(notifier, times(0)).trigger(captor.capture());
		}

		task.cleanNotifiers();
	}

	// Parse tests

	@Test
	public void testNotifierParseWhenAlways() throws Exception {
		Notifier notifier = spy(Notifier.class);
		notifier.setWhen("always");
		assertThat(notifier.isNotifyOnSuccess()).isTrue();
		assertThat(notifier.isNotifyOnError()).isTrue();
	}

	@Test
	public void testNotifierParseWhenOnError() throws Exception {
		Notifier notifier = spy(Notifier.class);
		notifier.setWhen("on-error");
		assertThat(notifier.isNotifyOnSuccess()).isFalse();
		assertThat(notifier.isNotifyOnError()).isTrue();
	}

	@Test
	public void testNotifierParseWhenOnSuccess() throws Exception {
		Notifier notifier = spy(Notifier.class);
		notifier.setWhen("on-success");
		assertThat(notifier.isNotifyOnSuccess()).isTrue();
		assertThat(notifier.isNotifyOnError()).isFalse();
	}

	@Test
	public void testNotifierParseWhenWithSpacesAndCase() throws Exception {
		Notifier notifier = spy(Notifier.class);
		notifier.setWhen(" on-Success ");
		assertThat(notifier.isNotifyOnSuccess()).isTrue();
		assertThat(notifier.isNotifyOnError()).isFalse();
	}

	@Test
	public void testNotifierParseWhenInvalid() throws Exception {
		try {
			Notifier notifier = spy(Notifier.class);
			notifier.setWhen(" it is invalid and should not be accepted ");
			fail();
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

}
