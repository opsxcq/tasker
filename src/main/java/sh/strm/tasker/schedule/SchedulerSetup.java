package sh.strm.tasker.schedule;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class SchedulerSetup {

	@Autowired
	private ScheduleConfiguration conf;
	@Autowired
	private TaskScheduler taskScheduler;

	private static final Logger LOG = Logger.getLogger(SchedulerSetup.class);

	public void setConf(ScheduleConfiguration conf) {
		this.conf = conf;
	}

	@PostConstruct
	public void init() {
		LOG.info("Scheduler configuration loaded");
		for (Schedule schedule : conf.getSchedule()) {
			LOG.info(schedule);

			Trigger trigger = new CronTrigger(schedule.getCron());
			taskScheduler.schedule(null, trigger);

		}

	}
}
