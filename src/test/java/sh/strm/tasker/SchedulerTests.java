package sh.strm.tasker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	public void testScheduleSetEvery() throws Exception {
		Schedule schedule = new Schedule();
		schedule.setEvery("minute");
		assertThat(schedule.getCron()).isEqualTo("0 * * * * *");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testScheduleSetEveryWithNull() throws Exception {
		Schedule schedule = new Schedule();
		schedule.setEvery(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyMinuteNotANumber() {
		ScheduleParser.expressionToCron("two minutes");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyMinuteDecimal() {
		ScheduleParser.expressionToCron("1.2 minutes");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyMinuteNegativeNumber() {
		ScheduleParser.expressionToCron("-10 minutes");
	}

	//////////////////////////////////////////////////////////////////////////

	@Test
	public void everyMinuteSimple() {
		Schedule schedule = conf.getScheduleByName("testEveryMinute");
		assertEquals("1 minutes", schedule.getEvery());
		assertEquals("0 * * * * *", ScheduleParser.expressionToCron(schedule.getEvery()));
	}

	@Test
	public void everyParserEveryMinuteSingular() throws Exception {
		assertEquals("0 * * * * *", ScheduleParser.expressionToCron("minute"));
	}

	@Test
	public void everyParserEveryMinuteSingularNumeric() throws Exception {
		assertEquals("0 * * * * *", ScheduleParser.expressionToCron("1 minute"));
	}

	@Test
	public void everyParserEveryMinuteNumeric() throws Exception {
		assertEquals("0 * * * * *", ScheduleParser.expressionToCron("1 minutes"));
	}

	@Test
	public void everyParserEveryFiveMinutesNumeric() throws Exception {
		assertEquals("0 */5 * * * *", ScheduleParser.expressionToCron("5 minutes"));
	}

	@Test
	public void everyParserEveryFiveMinutesNumericCase() throws Exception {
		assertEquals("0 */5 * * * *", ScheduleParser.expressionToCron("5 Minutes"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserMinuteError() throws Exception {
		ScheduleParser.expressionToCron("5 minutess");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserMinuteErrorTooMuchToken() throws Exception {
		ScheduleParser.expressionToCron("2 2 minutes");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserMinuteErrorRangeLower() throws Exception {
		ScheduleParser.expressionToCron("0 minutes");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserMinuteErrorRangeHigher() throws Exception {
		ScheduleParser.expressionToCron("60 minutes");
	}

	//////////////////////////////////////////////////////////////////////////

	@Test
	public void everyHourSimple() {
		Schedule schedule = conf.getScheduleByName("testEveryHour");
		assertEquals("hour", schedule.getEvery());
		assertEquals("0 0 * * * *", ScheduleParser.expressionToCron(schedule.getEvery()));
	}

	@Test
	public void everyParserEveryHourSingular() throws Exception {
		assertEquals("0 0 * * * *", ScheduleParser.expressionToCron("hour"));
	}

	@Test
	public void everyParserEveryHourSingularNumeric() throws Exception {
		assertEquals("0 0 * * * *", ScheduleParser.expressionToCron("1 hour"));
	}

	@Test
	public void everyParserEveryHoursNumeric() throws Exception {
		assertEquals("0 0 * * * *", ScheduleParser.expressionToCron("1 hours"));
	}

	@Test
	public void everyParserEveryTwoHourNumeric() throws Exception {
		assertEquals("0 0 */2 * * *", ScheduleParser.expressionToCron("2 hours"));
	}

	@Test
	public void everyParserEveryTwoHourNumericCase() throws Exception {
		assertEquals("0 0 */2 * * *", ScheduleParser.expressionToCron("2 Hours"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserHourError() throws Exception {
		ScheduleParser.expressionToCron("5 hourss");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserHourErrorTooMuchToken() throws Exception {
		ScheduleParser.expressionToCron("2 2 hours");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserHourErrorRangeLower() throws Exception {
		ScheduleParser.expressionToCron("0 hours");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserHourErrorRangeHigher() throws Exception {
		ScheduleParser.expressionToCron("24 hours");
	}

	//////////////////////////////////////////////////////////////////////////

	@Test
	public void everyDaySimple() {
		Schedule schedule = conf.getScheduleByName("testEveryDay");
		assertEquals("1 day", schedule.getEvery());
		assertEquals("0 0 0 * * *", ScheduleParser.expressionToCron(schedule.getEvery()));
	}

	@Test
	public void everyParserEveryDaySingular() throws Exception {
		assertEquals("0 0 0 * * *", ScheduleParser.expressionToCron("day"));
	}

	@Test
	public void everyParserEveryDaySingularNumeric() throws Exception {
		assertEquals("0 0 0 * * *", ScheduleParser.expressionToCron("1 day"));
	}

	@Test
	public void everyParserEveryDaysNumeric() throws Exception {
		assertEquals("0 0 0 * * *", ScheduleParser.expressionToCron("1 days"));
	}

	@Test
	public void everyParserEveryFiveDayNumeric() throws Exception {
		assertEquals("0 0 0 */5 * *", ScheduleParser.expressionToCron("5 days"));
	}

	@Test
	public void everyParserEveryFiveDayNumericCase() throws Exception {
		assertEquals("0 0 0 */5 * *", ScheduleParser.expressionToCron("5 Days"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserDayError() throws Exception {
		ScheduleParser.expressionToCron("5 Dayz");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserDayErrorTooMuchToken() throws Exception {
		ScheduleParser.expressionToCron("2 2 days");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserDayErrorRangeLower() throws Exception {
		ScheduleParser.expressionToCron("0 days");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserDayErrorRangeHigher() throws Exception {
		ScheduleParser.expressionToCron("32 days");
	}

	//////////////////////////////////////////////////////////////////////////

	@Test
	public void everyMonthSimple() {
		Schedule schedule = conf.getScheduleByName("testEveryMonth");
		assertEquals("month", schedule.getEvery());
		assertEquals("0 0 0 1 * *", ScheduleParser.expressionToCron(schedule.getEvery()));
	}

	@Test
	public void everyParserEveryMonthSingular() throws Exception {
		assertEquals("0 0 0 1 * *", ScheduleParser.expressionToCron("month"));
	}

	@Test
	public void everyParserEveryMonthSingularNumeric() throws Exception {
		assertEquals("0 0 0 1 * *", ScheduleParser.expressionToCron("1 month"));
	}

	@Test
	public void everyParserEveryMonthsNumeric() throws Exception {
		assertEquals("0 0 0 1 * *", ScheduleParser.expressionToCron("1 months"));
	}

	@Test
	public void everyParserEverySixMonthsNumeric() throws Exception {
		assertEquals("0 0 0 1 */6 *", ScheduleParser.expressionToCron("6 months"));
	}

	@Test
	public void everyParserEverySixMonthNumericCase() throws Exception {
		assertEquals("0 0 0 1 */6 *", ScheduleParser.expressionToCron("6 Months"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserMonthError() throws Exception {
		ScheduleParser.expressionToCron("6 Monthz");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserMonthErrorTooMuchToken() throws Exception {
		ScheduleParser.expressionToCron("2 2 months");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserMonthErrorRangeLower() throws Exception {
		ScheduleParser.expressionToCron("0 months");
	}

	@Test(expected = IllegalArgumentException.class)
	public void everyParserMonthErrorRangeHigher() throws Exception {
		ScheduleParser.expressionToCron("12 months");
	}

	//////////////////////////////////////////////////////////////////////////

	@Test
	public void everyYear() throws Exception {
		assertEquals("0 0 0 1 1 *", ScheduleParser.expressionToCron("year"));
	}

	//////////////////////////////////////////////////////////////////////////

	@Test
	public void everyWeekSunday() throws Exception {
		assertEquals("0 0 0 0 0 SUN", ScheduleParser.expressionToCron("Sunday"));
	}

	@Test

	public void everyWeekMonday() throws Exception {
		assertEquals("0 0 0 0 0 MON", ScheduleParser.expressionToCron("Monday"));
	}

	@Test
	public void everyWeekTuesday() throws Exception {
		assertEquals("0 0 0 0 0 TUE", ScheduleParser.expressionToCron("Tuesday"));
	}

	@Test
	public void everyWeekWednesday() throws Exception {
		assertEquals("0 0 0 0 0 WED", ScheduleParser.expressionToCron("Wednesday"));
	}

	@Test
	public void everyWeekThursday() throws Exception {
		assertEquals("0 0 0 0 0 THU", ScheduleParser.expressionToCron("Thursday"));
	}

	@Test
	public void everyWeekFriday() throws Exception {
		assertEquals("0 0 0 0 0 FRI", ScheduleParser.expressionToCron("Friday"));
	}

	@Test
	public void everyWeekSaturday() throws Exception {
		assertEquals("0 0 0 0 0 SAT", ScheduleParser.expressionToCron("Saturday"));
	}

}
