package sh.strm.tasker;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "config")
public class Configuration {

	private String[] globalEnvironment;

	public Configuration() {
	}

	public String[] getGlobalEnvironment() {
		return globalEnvironment;
	}

	public void setGlobalEnvironment(String... environment) {
		if (environment != null) {
			for (String variable : environment) {
				if (variable != null) {
					String split[] = variable.split("=");
					// It must be at least 2 tokens, why ? Variables can have '=' too, so we won't want to mess with that.
					// Also verifies if both tokens contain something
					if (split.length < 2 || split[0].length() == 0 || split[1].length() == 0) {
						throw new IllegalArgumentException("Environment variables must follow the 'Variable=Value' format");
					}
				}
			}
			this.globalEnvironment = environment;
		} else {
			this.globalEnvironment = new String[0];
		}
	}

}
