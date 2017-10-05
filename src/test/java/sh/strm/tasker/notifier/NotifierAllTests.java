package sh.strm.tasker.notifier;

import java.io.IOException;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

@RunWith(Suite.class)
@SuiteClasses({ NotifierTests.class, EmailNotifierTests.class })
public class NotifierAllTests {

	public static class CustomTestYamlInitialization implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			try {
				Resource resource = applicationContext.getResource("classpath:application-notifier.yml");
				YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
				PropertySource<?> yamlTestProperties = sourceLoader.load("yamlTestProperties", resource, null);
				applicationContext.getEnvironment().getPropertySources().addFirst(yamlTestProperties);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
