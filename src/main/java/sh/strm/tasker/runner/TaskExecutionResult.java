package sh.strm.tasker.runner;

import java.util.Date;

import sh.strm.tasker.task.Task;

public class TaskExecutionResult {

	// Null means this task still running
	private Integer exitCode;

	// Combination of stdout + stderr
	private String output;

	private Task task;

	// Internal controls
	private Date started;
	private Date finished;

	public TaskExecutionResult(Task task) {
		this.started = new Date();
		this.task = task;
	}

	public Integer getExitCode() {
		return exitCode;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public Date getStarted() {
		return started;
	}

	public Date getFinished() {
		return finished;
	}

	public Task getTask() {
		return task;
	}

	public void markAsFinishedWithSuccess() {
		this.markAsFinished(0);
	}

	public void markAsFinished(int exitCode) {
		this.finished = new Date();
		this.exitCode = exitCode;
	}

	public boolean isSuccessful() {
		return this.exitCode == 0;
	}

}
