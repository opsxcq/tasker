package sh.strm.tasker.notify;

import sh.strm.tasker.runner.TaskExecutionResult;
import sh.strm.tasker.task.Task;

public abstract class Notifier {

	private String name;
	private String task;
	private Task realTask;

	private String when;

	private boolean notifyOnError;
	private boolean notifyOnSuccess;

	public Notifier() {
		this.notifyOnError = true;
		this.notifyOnSuccess = true;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getTask() {
		return task;
	}

	public void setRealTask(Task realTask) {
		this.realTask = realTask;
	}

	public Task getRealTask() {
		return realTask;
	}

	public void setWhen(String when) {
		String value = when.toLowerCase().trim();
		if (value.equals("always")) {
			this.notifyOnError = true;
			this.notifyOnSuccess = true;
		} else if (value.equals("on-success")) {
			this.notifyOnError = false;
			this.notifyOnSuccess = true;
		} else if (value.equals("on-error")) {
			this.notifyOnError = true;
			this.notifyOnSuccess = false;
		} else {
			throw new IllegalArgumentException("Invalid parameter value: " + when);
		}
		this.when = value;
	}

	public String getWhen() {
		return when;
	}

	public boolean isNotifyOnError() {
		return notifyOnError;
	}

	public boolean isNotifyOnSuccess() {
		return notifyOnSuccess;
	}

	public abstract void trigger(TaskExecutionResult result);

}
