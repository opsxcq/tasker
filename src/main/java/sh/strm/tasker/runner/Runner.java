package sh.strm.tasker.runner;

import sh.strm.tasker.task.Task;

public abstract class Runner<T extends Task> {

	public abstract TaskExecutionResult executeTask(T task) throws Exception;
	
}
