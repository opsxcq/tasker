package sh.strm.tasker.setup;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.task.DockerTask;
import sh.strm.tasker.task.TaskSetup;

public class TaskSetupTest {

	@Test(expected = IllegalStateException.class)
	public void testSimpleSetup() {
		TaskConfiguration configuration = new TaskConfiguration();
		List<DockerTask> tasks = new ArrayList<DockerTask>();
		DockerTask task = new DockerTask();

		tasks.add(task);
		configuration.setDocker(tasks);

		TaskSetup setup = new TaskSetup();
		setup.setConfiguration(configuration);
		setup.init();
	}

}
