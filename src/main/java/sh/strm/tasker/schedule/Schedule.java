package sh.strm.tasker.schedule;

public class Schedule {
	
	private String name;

	private String every;
	private String cron;
	private String task;

	public Schedule() {
		this.name = "unamed";
	}

	public String getEvery() {
		return every;
	}

	public void setEvery(String every) {
		this.every = every;
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

	
	
}
