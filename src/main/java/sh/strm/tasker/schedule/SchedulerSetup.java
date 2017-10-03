package sh.strm.tasker.schedule;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import sh.strm.tasker.ScheduleConfiguration;
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

	private static final Logger log = Logger.getLogger(SchedulerSetup.class);

	public void setConf(ScheduleConfiguration conf) {
		this.conf = conf;
	}

	@PostConstruct
	public void init() {
		log.info("Scheduler configuration loaded, wiring tasks and schedules");

		// Wire Task and Schedule configuration
		for (Schedule schedule : conf.getSchedule()) {
			DockerTask task = taskConfiguration.getDockerTaskByName(schedule.getTask());

			// Sanity check
			if (task == null) {
				throw new IllegalStateException("Defined task not found " + schedule.getTask() + " aborting application boot");
			}

			String scheduleName = schedule.getName();
			if (scheduleName == null) {
				scheduleName = "anonymous (" + schedule.getCron() + ")";
			}
			log.info("Wiring task " + task.getName() + " to " + scheduleName);

			schedule.setRealTask(task);

		}

		for (Schedule schedule : conf.getSchedule()) {
			log.info(schedule);

			Trigger trigger = new CronTrigger(schedule.getCron());
			taskScheduler.schedule(() -> {
				try {
					DockerTask task = schedule.getRealTask();
					TaskExecutionResult result = dockerRunner.execute(task);
					// TODO: Save task execution result
					if (result != null) {
						log.debug("Task " + task.getName() + " output");
						log.debug(result.getOutput());
					} else {
						log.debug("Task " + task.getName() + " suffered an unexpected error and Tasker was unable"
								+ "to determine it's result, please check it");
					}

				} catch (Exception e) {
					// TODO: Save task result in case of error
					e.printStackTrace();
				}
			}, trigger);

		}

	}
}
