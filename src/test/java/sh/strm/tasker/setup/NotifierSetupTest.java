package sh.strm.tasker.setup;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.notify.EmailNotifier;
import sh.strm.tasker.notify.NotifierConfiguration;
import sh.strm.tasker.notify.NotifierSetup;
import sh.strm.tasker.task.DockerTask;

public class NotifierSetupTest {

	@Test(expected = IllegalStateException.class)
	public void testSimpleSetup() {
		TaskConfiguration taskConfiguration = new TaskConfiguration();
		List<DockerTask> tasks = new ArrayList<DockerTask>();
		DockerTask task = new DockerTask();
		task.setName("test");

		tasks.add(task);
		taskConfiguration.setDocker(tasks);

		NotifierConfiguration configuration = new NotifierConfiguration();
		List<EmailNotifier> emailNotifiers = new ArrayList<EmailNotifier>();

		EmailNotifier emailNotifier = new EmailNotifier();
		emailNotifier.setTask("missing task");
		emailNotifiers.add(emailNotifier);

		configuration.setEmail(emailNotifiers);

		NotifierSetup setup = new NotifierSetup();

		setup.setTaskConfiguration(taskConfiguration);
		setup.setConfiguration(configuration);

		setup.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testSimpleSetupServerTest() {
		TaskConfiguration taskConfiguration = new TaskConfiguration();
		List<DockerTask> tasks = new ArrayList<DockerTask>();
		DockerTask task = new DockerTask();
		task.setName("test");

		tasks.add(task);
		taskConfiguration.setDocker(tasks);

		NotifierConfiguration configuration = new NotifierConfiguration();
		List<EmailNotifier> emailNotifiers = new ArrayList<EmailNotifier>();

		EmailNotifier emailNotifier = new EmailNotifier();
		emailNotifier.setTask("test");
		emailNotifier.setServer("thishostdontexist");

		emailNotifiers.add(emailNotifier);

		configuration.setEmail(emailNotifiers);

		NotifierSetup setup = new NotifierSetup();

		setup.setTaskConfiguration(taskConfiguration);
		setup.setConfiguration(configuration);

		setup.init();
	}

}
