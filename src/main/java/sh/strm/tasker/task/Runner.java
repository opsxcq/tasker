package sh.strm.tasker.task;

import sh.strm.tasker.runner.TaskExecutionResult;

public abstract class Runner<T extends Task> {

	public abstract TaskExecutionResult executeTask(T task) throws Exception;
	
}
