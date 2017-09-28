package sh.strm.tasker.task;

public class DockerTask extends Task {

	private String image;

	private String entrypoint;
	private String[] arguments;

	private String[] environment;
	private String[] volumes;
	private String[] ports;
	private String network;

	private boolean scriptStrict;
	private String[] script;

	private boolean keepContainerAfterExecution;
	private boolean alwaysPull;
	private boolean reuseContainer;

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

	public String[] getVolumes() {
		return volumes;
	}

	public void setVolumes(String... volumes) {
		if (volumes != null) {
			for (String volume : volumes) {
				if (volume != null) {
					if (!volume.contains(":")) {
						throw new IllegalArgumentException("Volumes must follow the 'volume1:volume2' format mapping");
					}
					String split[] = volume.split(":");
					if (split.length != 2 || split[0] == null || split[0].length() == 0 || split[1] == null || split[1].length() == 0) {
						throw new IllegalArgumentException("Volumes must follow the 'volume1:volume2' format mapping");
					}
				}
			}
		}

		this.volumes = volumes;
	}

	public String[] getPorts() {
		return ports;
	}

	public void setPorts(String... ports) {
		if (ports != null) {
			for (String port : ports) {
				if (port != null) {
					if (!port.contains(":")) {
						throw new IllegalArgumentException("Port mapping must follow the 'hostPort:containerPort' format mapping");
					}
					String split[] = port.split(":");
					if (split.length != 2 || split[0] == null || split[0].length() == 0 || split[1] == null || split[1].length() == 0) {
						throw new IllegalArgumentException("Port mapping must follow the 'hostPort:containerPort' format mapping");
					}
					try {
						int external = Integer.parseInt(split[0]);
						int internal = Integer.parseInt(split[1]);

						if (external <= 0 || external > 0xFFFF || internal <= 0 || internal > 0xFFFF) {
							throw new IllegalArgumentException("Port mapping must follow the 'hostPort:containerPort' format mapping, and only use valid port numbers");
						}
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Port mapping must follow the 'hostPort:containerPort' format mapping, and only use valid port numbers");
					}
				}
			}
		}
		this.ports = ports;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		if (network != null) {
			if ("".equals(network)) {
				throw new IllegalArgumentException("Invalid network name");
			}
		}
		this.network = network;
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

	public void setKeepContainerAfterExecution(boolean keepContainerAfterExecution) {
		this.keepContainerAfterExecution = keepContainerAfterExecution;
	}

	public boolean isKeepContainerAfterExecution() {
		return keepContainerAfterExecution;
	}

	public boolean isAlwaysPull() {
		return alwaysPull;
	}

	public void setAlwaysPull(boolean alwaysPull) {
		this.alwaysPull = alwaysPull;
	}

	public boolean isReuseContainer() {
		return reuseContainer;
	}

	public void setReuseContainer(boolean reuseContainer) {
		this.reuseContainer = reuseContainer;
	}

}
