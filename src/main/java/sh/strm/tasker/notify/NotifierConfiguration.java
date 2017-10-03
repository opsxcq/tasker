package sh.strm.tasker.notify;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "notify")
public class NotifierConfiguration {

	private List<EmailNotifier> email;

	public List<EmailNotifier> getEmail() {
		return email;
	}

	public void setEmail(List<EmailNotifier> email) {
		this.email = email;
	}

	public List<Notifier> getNotifiers() {
		ArrayList<Notifier> notifiers = new ArrayList<Notifier>();

		notifiers.addAll(email);

		return notifiers;
	}

}
