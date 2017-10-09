package sh.strm.tasker.notify;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import sh.strm.tasker.runner.TaskExecutionResult;

public class EmailNotifier extends Notifier {

	private JavaMailSenderImpl mailSender;

	// Connection related fields
	private String server;
	private int port;
	private String username;
	private String password;
	private String protocol;
	boolean starttls;
	boolean debug;

	boolean validateServerOnConfigurationLoad;

	// Message related fields
	private String subject;
	private String sender;
	private List<String> recipients;
	private String content;

	private String template;

	public EmailNotifier() {
		super();
		this.mailSender = new JavaMailSenderImpl();

		this.setServer("localhost");
		this.setPort(587);

		this.setProtocol("smtp");

		this.setStarttls(true);
		this.setDebug(false);

		this.validateServerOnConfigurationLoad = true;
	}

	public void setServer(String server) {
		this.mailSender.setHost(server);
		this.server = server;
	}

	public String getServer() {
		return server;
	}

	public void setPort(int port) {
		if (port < 1 || port > 0xFFFF) {
			throw new IllegalArgumentException(port + " isn't a valid TCP port number.");
		}
		this.mailSender.setPort(port);
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setUsername(String username) {
		this.mailSender.setUsername(username);
		Properties props = this.mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");

		if (this.sender == null) {
			this.sender = username;
		}

		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.mailSender.setPassword(password);
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setProtocol(String protocol) {
		Properties props = this.mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");

		this.protocol = protocol;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setStarttls(boolean starttls) {
		Properties props = this.mailSender.getJavaMailProperties();
		props.put("mail.smtp.starttls.enable", Boolean.toString(starttls));
		this.starttls = starttls;
	}

	public boolean isStarttls() {
		return starttls;
	}

	public void setDebug(boolean debug) {
		Properties props = this.mailSender.getJavaMailProperties();
		props.put("mail.debug", Boolean.toString(debug));
		this.debug = debug;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setValidateServerOnConfigurationLoad(boolean validateServerOnConfigurationLoad) {
		this.validateServerOnConfigurationLoad = validateServerOnConfigurationLoad;
	}

	public boolean isValidateServerOnConfigurationLoad() {
		return validateServerOnConfigurationLoad;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setSender(String sender) {
		checkValidEmailAddress(sender);
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	public void setRecipients(List<String> recipients) {
		if (recipients != null) {
			for (String recipient : recipients) {
				checkValidEmailAddress(recipient);
			}
		} else {
			throw new IllegalArgumentException("Recipient addresses cannot be empty");
		}
		this.recipients = recipients;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	public void testConnection() throws MessagingException {
		this.mailSender.testConnection();
	}

	private void checkValidEmailAddress(String email) {
		try {
			InternetAddress emailAddress = new InternetAddress(email);
			emailAddress.validate();
		} catch (AddressException ex) {
			throw new IllegalArgumentException(email + " isn't a valid e-mail address");
		}
	}

	public String render(String templates, TaskExecutionResult result) {
		StringWriter writer = new StringWriter();
		StringReader reader = new StringReader(templates);
		Context context = new VelocityContext();

		context.put("success", result.isSuccessful());
		context.put("error", !result.isSuccessful());
		context.put("log", result.getOutput());
		context.put("task", result.getTask().getName());

		context.put("start", result.getStarted());
		context.put("end", result.getFinished());

		Velocity.evaluate(context, writer, "template", reader);

		return writer.toString();
	}

	@Override
	public void trigger(TaskExecutionResult result) {
		if (this.template != null) {
			this.content = this.render(this.template, result);
		}

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(this.sender);

		String[] recipients = new String[this.recipients.size()];
		this.recipients.toArray(recipients);
		message.setTo(recipients);

		message.setSubject(this.subject);
		message.setText(this.content);
		mailSender.send(message);
	}

}
