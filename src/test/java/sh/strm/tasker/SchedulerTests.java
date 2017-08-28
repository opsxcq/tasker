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
	public void everyParserEveryFiveMinutesNumericCase() throws Exception {
		assertEquals("*/5 * * * *", ScheduleParser.expressionToCron("5 Minutes"));
	}

	@Test
	public void everyParserMinuteError() throws Exception {
		try {
			ScheduleParser.expressionToCron("5 minutess");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}

	@Test
	public void everyParserMinuteErrorTooMuchToken() throws Exception {
		try {
			ScheduleParser.expressionToCron("2 2 minutes");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}

	@Test
	public void everyParserMinuteErrorRangeLower() throws Exception {
		try {
			ScheduleParser.expressionToCron("0 minutes");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}

	@Test
	public void everyParserMinuteErrorRangeHigher() throws Exception {
		try {
			ScheduleParser.expressionToCron("60 minutes");
			fail();
		} catch (IllegalArgumentException e) {
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
	public void everyParserEveryTwoHourNumeric() throws Exception {
		assertEquals("0 */2 * * *", ScheduleParser.expressionToCron("2 hours"));
	}

	@Test
	public void everyParserEveryTwoHourNumericCase() throws Exception {
		assertEquals("0 */2 * * *", ScheduleParser.expressionToCron("2 Hours"));
	}

	@Test
	public void everyParserHourError() throws Exception {
		try {
			ScheduleParser.expressionToCron("5 hourss");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}
	
	@Test
	public void everyParserHourErrorTooMuchToken() throws Exception {
		try {
			ScheduleParser.expressionToCron("2 2 hours");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}

	@Test
	public void everyParserHourErrorRangeLower() throws Exception {
		try {
			ScheduleParser.expressionToCron("0 hours");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}

	@Test
	public void everyParserHourErrorRangeHigher() throws Exception {
		try {
			ScheduleParser.expressionToCron("24 hours");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}

	//////////////////////////////////////////////////////////////////////////

	@Test
	public void everyDaySimple() {
		Schedule schedule = conf.getScheduleByName("testEveryDay");
		assertEquals("1 day", schedule.getEvery());
		assertEquals("0 0 * * *", ScheduleParser.expressionToCron(schedule.getEvery()));
	}

	@Test
	public void everyParserEveryDaySingular() throws Exception {
		assertEquals("0 0 * * *", ScheduleParser.expressionToCron("day"));
	}

	@Test
	public void everyParserEveryDaySingularNumeric() throws Exception {
		assertEquals("0 0 * * *", ScheduleParser.expressionToCron("1 day"));
	}

	@Test
	public void everyParserEveryDaysNumeric() throws Exception {
		assertEquals("0 0 * * *", ScheduleParser.expressionToCron("1 days"));
	}

	@Test
	public void everyParserEveryFiveDayNumeric() throws Exception {
		assertEquals("0 0 */5 * *", ScheduleParser.expressionToCron("5 days"));
	}

	@Test
	public void everyParserEveryFiveDayNumericCase() throws Exception {
		assertEquals("0 0 */5 * *", ScheduleParser.expressionToCron("5 Days"));
	}

	@Test
	public void everyParserDayError() throws Exception {
		try {
			ScheduleParser.expressionToCron("5 Dayz");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}
	
	@Test
	public void everyParserDayErrorTooMuchToken() throws Exception {
		try {
			ScheduleParser.expressionToCron("2 2 days");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}

	@Test
	public void everyParserDayErrorRangeLower() throws Exception {
		try {
			ScheduleParser.expressionToCron("0 days");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}

	@Test
	public void everyParserDayErrorRangeHigher() throws Exception {
		try {
			ScheduleParser.expressionToCron("32 days");
			fail();
		} catch (IllegalArgumentException e) {
			// OK, this is expected, go on
		}
	}

}
