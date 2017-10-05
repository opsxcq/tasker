package sh.strm.tasker.schedule;

import sh.strm.tasker.task.DockerTask;

public class Schedule {

	private String name;

	private String every;
	private String cron;
	private String task;

	private DockerTask realTask;

	public Schedule() {
		this.name = "unamed";
	}

	public String getEvery() {
		return every;
	}

	public void setEvery(String every) {
		this.every = every;

		if (every != null) {
			// Convert `every` to a `cron` expression
			this.setCron(ScheduleParser.expressionToCron(every));
		} else {
			throw new IllegalArgumentException("Null isn't a valid every expression");
		}
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRealTask(DockerTask realTask) {
		this.realTask = realTask;
	}

	public DockerTask getRealTask() {
		return realTask;
	}

	@Override
	public String toString() {
		return "Schedule [name=" + name + ", every=" + every + ", cron=" + cron + ", task=" + task + ", realTask=" + realTask + "]";
	}

}
