package sh.strm.tasker.notifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import sh.strm.tasker.notify.EmailNotifier;
import sh.strm.tasker.runner.TaskExecutionResult;
import sh.strm.tasker.task.Task;

public class EmailTemplateTest {

	@Test
	public void testTaskName() {
		Task task = mock(Task.class);
		when(task.getName()).thenReturn("Test task");

		TaskExecutionResult result = new TaskExecutionResult(task);
		result.setOutput("green bar");
		result.markAsFinishedWithSuccess();

		EmailNotifier notifier = new EmailNotifier();
		String rendered = notifier.render("Template $task", result);

		assertThat(rendered).isEqualTo("Template Test task");
	}

	@Test
	public void testTestSuccess() {
		Task task = mock(Task.class);
		when(task.getName()).thenReturn("Test task");

		TaskExecutionResult result = new TaskExecutionResult(task);
		result.setOutput("green bar");
		result.markAsFinishedWithSuccess();

		EmailNotifier notifier = new EmailNotifier();
		String template = "#if($success)\ngreen bar\n#end";
		String rendered = notifier.render(template, result);

		assertThat(rendered).isEqualTo("green bar\n");
	}

	@Test
	public void testTestError() {
		Task task = mock(Task.class);
		when(task.getName()).thenReturn("Test task");

		TaskExecutionResult result = new TaskExecutionResult(task);
		result.setOutput("green bar");
		result.markAsFinished(1);

		EmailNotifier notifier = new EmailNotifier();
		String template = "#if($error)\ngreen bar\n#end";
		String rendered = notifier.render(template, result);

		assertThat(rendered).isEqualTo("green bar\n");
	}

	@Test
	public void testStartDate() {
		Task task = mock(Task.class);
		when(task.getName()).thenReturn("Test task");

		TaskExecutionResult result = new TaskExecutionResult(task);
		result.setOutput("green bar");
		result.markAsFinished(1);

		EmailNotifier notifier = new EmailNotifier();
		String template = "$start";
		String rendered = notifier.render(template, result);

		assertThat(rendered).isEqualTo(result.getStarted().toString());
	}

	@Test
	public void testEndDate() {
		Task task = mock(Task.class);
		when(task.getName()).thenReturn("Test task");

		TaskExecutionResult result = new TaskExecutionResult(task);
		result.setOutput("green bar");
		result.markAsFinished(1);

		EmailNotifier notifier = new EmailNotifier();
		String template = "$end";
		String rendered = notifier.render(template, result);

		assertThat(rendered).isEqualTo(result.getFinished().toString());
	}

	@Test
	public void testLog() {
		Task task = mock(Task.class);
		when(task.getName()).thenReturn("Test task");

		TaskExecutionResult result = new TaskExecutionResult(task);
		result.setOutput("green bar");
		result.markAsFinished(1);

		EmailNotifier notifier = new EmailNotifier();
		String template = "$log";
		String rendered = notifier.render(template, result);

		assertThat(rendered).isEqualTo("green bar");
	}

}
