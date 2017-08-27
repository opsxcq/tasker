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
	
	//////////////////////////////////////////////////////////////////////////

	@Test
	public void everyMinuteSimple() {
		Schedule schedule = conf.getScheduleByName("testEveryMinute");
		assertEquals("1 minutes", schedule.getEvery());
		assertEquals("* * * * *", ScheduleParser.expressionToCron(schedule.getEvery()));
	}

	@Test
	public void everyParserEveryMinuteSingular() throws Exception {
		assertEquals("* * * * *", ScheduleParser.expressionToCron("minute"));	
	}
	
	@Test
	public void everyParserEveryMinuteSingularNumeric() throws Exception {
		assertEquals("* * * * *", ScheduleParser.expressionToCron("1 minute"));	
	}
	
	@Test
	public void everyParserEveryMinuteNumeric() throws Exception {
		assertEquals("* * * * *", ScheduleParser.expressionToCron("1 minutes"));	
	}
	
	@Test
	public void everyParserEveryFiveMinutesNumeric() throws Exception {
		assertEquals("*/5 * * * *", ScheduleParser.expressionToCron("5 minutes"));	
	}
	
	@Test
	public void everyParserMinuteError() throws Exception {
		try {
			ScheduleParser.expressionToCron("5 minutess");
			fail();
		} catch(IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////////

	@Test
	public void everyHourSimple() {
		Schedule schedule = conf.getScheduleByName("testEveryHour");
		assertEquals("hour", schedule.getEvery());
		assertEquals("0 * * * *", ScheduleParser.expressionToCron(schedule.getEvery()));
	}

	@Test
	public void everyParserEveryHourSingular() throws Exception {
		assertEquals("0 * * * *", ScheduleParser.expressionToCron("hour"));	
	}
	
	@Test
	public void everyParserEveryHourSingularNumeric() throws Exception {
		assertEquals("0 * * * *", ScheduleParser.expressionToCron("1 hour"));	
	}
	
	@Test
	public void everyParserEveryHoursNumeric() throws Exception {
		assertEquals("0 * * * *", ScheduleParser.expressionToCron("1 hours"));	
	}
	
	@Test
	public void everyParserEveryFiveHourNumeric() throws Exception {
		assertEquals("0 */2 * * *", ScheduleParser.expressionToCron("2 hours"));	
	}
	
	@Test
	public void everyParserHourError() throws Exception {
		try {
			ScheduleParser.expressionToCron("5 hourss");
			fail();
		} catch(IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}
	
	
	
}
