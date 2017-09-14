package sh.strm.tasker.task;

public class DockerTask extends Task {

	private String image;

	private String entrypoint;
	private String[] arguments;

	private String[] environment;

	private boolean scriptStrict;
	private String[] script;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getEntrypoint() {
		return entrypoint;
	}

	public void setEntrypoint(String entrypoint) {
		this.entrypoint = entrypoint;
	}

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public String[] getEnvironment() {
		return environment;
	}

	public void setEnvironment(String... environment) {
		if (environment != null && environment.length > 0) {
			for (String element : environment) {
				if (element != null) {
					if (!element.contains("=")) {
						throw new IllegalArgumentException("Environment variables must follow the 'Variable=Value' format");
					}
					String split[] = element.split("=");
					// It must be at least 2 tokens, why ? Variables can have '=' too, so we won't want to mess with that.
					// Also verifies if both tokens contain something
					if (split.length < 2 || split[0] == null || split[0].length() == 0 || split[1] == null || split[1].length() == 0) {
						throw new IllegalArgumentException("Environment variables must follow the 'Variable=Value' format");
					}
				}
			}
		}
		this.environment = environment;
	}

	public String[] getScript() {
		return script;
	}

	public void setScript(String[] script) {
		this.script = script;
	}

	public boolean isScriptStrict() {
		return scriptStrict;
	}

	public void setScriptStrict(boolean scriptStrict) {
		this.scriptStrict = scriptStrict;
	}

}
