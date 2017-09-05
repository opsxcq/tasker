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
			this.cron = ScheduleParser.expressionToCron(every);
		} else {
			this.cron = null;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cron == null) ? 0 : cron.hashCode());
		result = prime * result + ((every == null) ? 0 : every.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((realTask == null) ? 0 : realTask.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Schedule other = (Schedule) obj;
		if (cron == null) {
			if (other.cron != null)
				return false;
		} else if (!cron.equals(other.cron))
			return false;
		if (every == null) {
			if (other.every != null)
				return false;
		} else if (!every.equals(other.every))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (realTask == null) {
			if (other.realTask != null)
				return false;
		} else if (!realTask.equals(other.realTask))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Schedule [name=" + name + ", every=" + every + ", cron=" + cron + ", task=" + task + ", realTask=" + realTask + "]";
	}

}
