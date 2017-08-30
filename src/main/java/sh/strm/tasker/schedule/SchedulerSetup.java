package sh.strm.tasker.schedule;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerSetup {

	@Autowired
	private ScheduleConfiguration conf;

	private static final Logger LOG = Logger.getLogger(SchedulerSetup.class);

	@PostConstruct
	public void init() {
		LOG.info("Scheduler configuration loaded");
		for (Schedule schedule : conf.getSchedule()) {
			LOG.info(schedule);
		}

	}
}
