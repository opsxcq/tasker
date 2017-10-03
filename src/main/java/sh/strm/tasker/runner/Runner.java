package sh.strm.tasker.runner;

import java.io.IOException;

import org.apache.log4j.Logger;

import sh.strm.tasker.notify.Notifier;
import sh.strm.tasker.task.Task;

public abstract class Runner<T extends Task> {

	static Logger log = Logger.getLogger(DockerTaskRunner.class.getName());

	public TaskExecutionResult execute(T task) {
		long timeStart = System.currentTimeMillis();
		log.info("Starting the execution of the " + task.getName() + " task");
		TaskExecutionResult result = null;

		try {
			result = executeTask(task);

			for (Notifier notifier : task.getNotifiers()) {
				try {
					if (result.isSuccessful() && notifier.isNotifyOnSuccess()) {
						log.info("Triggering notifier " + notifier.getName() + " for task " + task.getName());
						notifier.trigger(result);
					} else if (!result.isSuccessful() && notifier.isNotifyOnError()) {
						log.info("Triggering notifier " + notifier.getName() + " for task " + task.getName());
						notifier.trigger(result);
					}
				} catch (Exception e) {
					log.error("Error triggering " + notifier.getName() + " notifier, error:" + e.getMessage(), e);
				}
			}

		} catch (IOException e) {
			log.error("Error running " + task.getName() + " task, IO Error connecting to docker server: " + e.getMessage());
		} catch (Exception e) {
			log.error("Error running " + task.getName() + " task, error:" + e.getMessage(), e);
		}

		long timeFinished = System.currentTimeMillis();
		log.info(buildFinishMessage(task, timeStart, timeFinished));

		// Error, create a result, notify and continue
		return result;
	}

	private String buildFinishMessage(T task, long timeStart, long timeFinished) {
		StringBuilder sb = new StringBuilder();
		sb.append("Task ");
		sb.append(task.getName());
		sb.append(" took ");
		sb.append(timeFinished - timeStart);

		return sb.toString();
	}

	public abstract TaskExecutionResult executeTask(T task) throws Exception;

}
