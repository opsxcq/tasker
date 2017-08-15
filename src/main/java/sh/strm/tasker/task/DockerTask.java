package sh.strm.tasker.task;

public class DockerTask extends Task {

	private String image;
	private String[] environment;

	private String entrypoint;
	private String[] arguments;

	private String[] script;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String[] getEnvironment() {
		return environment;
	}

	public void setEnvironment(String[] environment) {
		this.environment = environment;
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

	public String[] getScript() {
		return script;
	}

	public void setScript(String[] script) {
		this.script = script;
	}

}
