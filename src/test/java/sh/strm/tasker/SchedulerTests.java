package sh.strm.tasker;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import sh.strm.tasker.schedule.Schedule;
import sh.strm.tasker.schedule.ScheduleParser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerTests {

	@Autowired
	private ScheduleConfiguration conf;

	@Test
	public void contextLoads() {
		assertNotNull(conf);
	}

	@Test
	public void everyMinuteSimple() {
		Schedule schedule = conf.getScheduleByName("testEveryMinute01");
		assertEquals("1 minutes", schedule.getEvery());
		assertEquals("* * * * *", ScheduleParser.expressionToCron(schedule.getEvery()));
	}

}
