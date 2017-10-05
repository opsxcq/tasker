package sh.strm.tasker.setup;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sh.strm.tasker.ScheduleConfiguration;
import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.schedule.Schedule;
import sh.strm.tasker.schedule.SchedulerSetup;
import sh.strm.tasker.task.DockerTask;

public class SchedulerSetupTest {

	@Test(expected = IllegalStateException.class)
	public void testSimpleSetup() {
		TaskConfiguration taskConfiguration = new TaskConfiguration();
		List<DockerTask> tasks = new ArrayList<DockerTask>();
		DockerTask task = new DockerTask();
		task.setName("test");

		tasks.add(task);
		taskConfiguration.setDocker(tasks);

		ScheduleConfiguration configuration = new ScheduleConfiguration();
		List<Schedule> schedules = new ArrayList<Schedule>();

		Schedule schedule = new Schedule();
		schedule.setEvery("minute");
		schedule.setTask(null);
		schedules.add(schedule);

		configuration.setSchedule(schedules);

		SchedulerSetup setup = new SchedulerSetup();

		setup.setTaskConfiguration(taskConfiguration);
		setup.setConfiguration(configuration);

		setup.init();
	}

}
