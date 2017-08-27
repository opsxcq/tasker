package sh.strm.tasker.schedule;

public class ScheduleParser {

	/**
	 * Convert an `every` expression into its respective cron expression
	 * 
	 * @param expression
	 *            an Every time format expression
	 * @return a converted Cron compatible expression
	 */
	public static String expressionToCron(String expression) {
		// normalization
		String every = expression.toLowerCase().trim();

		String cron = null;
		// Singular words/cases
		if (every.equals("minute")) {
			cron = "* * * * *";
		}

		if (cron == null) {
			throw new IllegalArgumentException(expression + " isn't a valid every expression");
		}

		return cron;
	}

}
