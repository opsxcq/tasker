package sh.strm.tasker.notify;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import sh.strm.tasker.TaskConfiguration;
import sh.strm.tasker.task.DockerTask;

@Component
@EnableAsync
public class NotifierSetup {

	@Autowired
	private TaskConfiguration taskConfiguration;

	@Autowired
	private NotifierConfiguration conf;

	private static final Logger log = Logger.getLogger(NotifierSetup.class);

	@PostConstruct
	public void init() {
		log.info("Notifier configuration loaded, wiring tasks and notifiers");

		for (Notifier notifier : conf.getNotifiers()) {
			log.info(notifier);

			DockerTask task = taskConfiguration.getDockerTaskByName(notifier.getTask());

			// Sanity check
			if (task == null) {
				throw new IllegalStateException("Defined task not found " + notifier.getTask() + " aborting application boot");
			}

			String notifierName = notifier.getName();
			if (notifierName == null) {
				notifierName = "anonymous (" + notifier.getClass().getSimpleName() + ")";
			}
			log.info("Wiring task " + task.getName() + " to " + notifierName);

			notifier.setRealTask(task);
			task.addNotifier(notifier);

			if (notifier instanceof EmailNotifier) {
				EmailNotifier emailNotifier = (EmailNotifier) notifier;
				if (emailNotifier.isValidateServerOnConfigurationLoad()) {
					log.info("Validating " + notifier.getName() + " notifier e-mail server connection");
					try {
						emailNotifier.testConnection();
					} catch (MessagingException e) {
						log.error("Connection problems for notifier" + notifier.getName() + ", error: " + e.getMessage());
						throw new IllegalArgumentException("Invalid notifier configuration for " + notifier.getName(), e);
					}
				}

			}

		}

	}
}
