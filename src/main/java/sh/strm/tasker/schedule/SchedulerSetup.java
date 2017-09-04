package sh.strm.tasker.schedule;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.runner.DockerTaskRunner;
import sh.strm.tasker.runner.TaskExecutionResult;
import sh.strm.tasker.task.DockerTask;

@Component
@EnableAsync
public class SchedulerSetup {

	@Autowired
	private ScheduleConfiguration conf;
	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private TaskConfiguration taskConfiguration;

	@Autowired
	private DockerTaskRunner dockerRunner;

	private static final Logger LOG = Logger.getLogger(SchedulerSetup.class);

	public void setConf(ScheduleConfiguration conf) {
		this.conf = conf;
	}

	@PostConstruct
	public void init() {
		LOG.info("Scheduler configuration loaded, wiring tasks and schedules");

		// Wire Task and Schedule configuration
		for (Schedule schedule : conf.getSchedule()) {
			DockerTask task = taskConfiguration.getDockerTaskByName(schedule.getTask());

			// Sanity check
			if (task == null) {
				throw new IllegalStateException("Defined task not found " + schedule.getTask() + " aborting application boot");
			}

			schedule.setRealTask(task);

		}

		for (Schedule schedule : conf.getSchedule()) {
			LOG.info(schedule);

			Trigger trigger = new CronTrigger(schedule.getCron());
			taskScheduler.schedule(() -> {
				try {
					DockerTask task = schedule.getRealTask();
					TaskExecutionResult result = dockerRunner.executeTask(task);
					// TODO: Save task execution result
					LOG.debug("Task " + task.getName() + " output");
					LOG.debug(result.getOutput());

				} catch (Exception e) {
					// TODO: Save task result in case of error
					e.printStackTrace();
				}
			}, trigger);

		}

	}
}
