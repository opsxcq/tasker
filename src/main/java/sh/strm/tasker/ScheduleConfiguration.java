package sh.strm.tasker;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import sh.strm.tasker.schedule.Schedule;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties()
public class ScheduleConfiguration {

	private List<Schedule> schedule;

	public List<Schedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}

	public Schedule getScheduleByName(String name) {
		return getSchedule().stream().filter(schedule -> schedule.getName().equals(name)).findFirst().orElse(null);
	}

	@Override
	public String toString() {
		return "ScheduleConfiguration [schedule=" + schedule + "]";
	}

}
