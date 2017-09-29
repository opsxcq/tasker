package sh.strm.tasker.util;

import java.util.Objects;

import org.apache.log4j.Logger;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import sh.strm.tasker.runner.DockerTaskRunner;

public class Docker {

	static Logger log = Logger.getLogger(DockerTaskRunner.class.getName());

	private DefaultDockerClient client;

	public Docker() throws DockerCertificateException {
		this.client = DefaultDockerClient.fromEnv().build();
	}

	public Docker(DefaultDockerClient client) throws DockerCertificateException {
		this.client = client;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Check if there is an image locally that has the name (tag)
	 * 
	 * @param name
	 *            Image name or tag
	 * @return true if exists
	 * @throws Exception
	 */
	public boolean hasImage(String name) throws Exception {
		return getImageIdByName(name) != null;
	}

	/**
	 * Get the image id of by it's name
	 * 
	 * @param name
	 *            Image name
	 * @return image id
	 * @throws Exception
	 */
	public String getImageIdByName(String name) throws Exception {
		String id = this.client.listImages().stream().//
				filter(image -> image.repoTags() != null && image.repoTags().contains(name)).//
				findFirst().map(image -> image.id()).orElse(null);

		return id;
	}

	public void pullImage(String name) throws Exception {
		log.info("Pulling image " + name);
		this.client.pull(name, (message) -> {
			if (message.progressDetail() != null) {
				Long current = message.progressDetail().current();
				Long total = message.progressDetail().total();
				log.info("Pulling image " + name + " " + current + " / " + total);
			} else {
				log.info(message);
			}
		});
	}

	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Check the existence of a volume with the given name
	 * 
	 * @param name
	 *            volume name to check
	 * @return if volume exists, return true, else, false
	 * @throws Exception
	 */
	public boolean hasVolume(String name) throws Exception {
		return getVolumeIdByName(name) != null;
	}

	/**
	 * Get a volume by it's name. Actually volumes id are their names
	 * 
	 * @param name
	 *            Volume name
	 * @return Volume name if it exists
	 * @throws Exception
	 */
	public String getVolumeIdByName(String name) throws Exception {
		String id = this.client.listVolumes().volumes().stream().//
				filter(Objects::nonNull).//
				filter(volume -> volume.name().equals(name)).//
				findFirst().map(volume -> volume.name()).orElse(null);
		return id;
	}

	/**
	 * Delete a volume from the Docker host
	 * 
	 * @param name
	 *            Volume name
	 * @throws Exception
	 */
	public boolean removeVolume(String name) throws Exception {
		String id = getVolumeIdByName(name);
		if (id != null) {
			this.client.removeVolume(name);
		}
		return id != null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Check if a given network exists
	 * 
	 * @param name
	 *            Network name
	 * @return if network exists, return true, else, false
	 * @throws Exception
	 */
	public boolean hasNetwork(String name) throws Exception {
		return getNetworkIdByName(name) != null;
	}

	/**
	 * Get network id by its name
	 * 
	 * @param name
	 *            Network name
	 * @return network id
	 * @throws Exception
	 */
	public String getNetworkIdByName(String name) throws Exception {
		String id = this.client.listNetworks().stream().//
				filter(Objects::nonNull).//
				filter(network -> network.name().contains(name)).//
				findFirst().map(network -> network.id()).orElse(null);
		return id;
	}

	/**
	 * Delete a network from the Docker host
	 * 
	 * @param name
	 *            Network name
	 * @throws Exception
	 */
	public boolean removeNetwork(String name) throws Exception {
		String id = getNetworkIdByName(name);
		if (id != null) {
			this.client.removeNetwork(id);
		}
		return id != null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Check if a given container exists
	 * 
	 * @param name
	 *            Container name
	 * @return if container exists, return true, else, false
	 * @throws Exception
	 */
	public boolean hasContainer(String name) throws Exception {
		return getContainerIdByName(name) != null;
	}

	public String getContainerIdByName(String name) throws DockerException, InterruptedException, Exception {
		// Container names via api start with a slash /, so if the name doesn't have one, we add
		String search;
		if (name != null && !name.startsWith("/")) {
			search = "/" + name;
		} else {
			search = name;
		}

		String id = this.client.listContainers(ListContainersParam.allContainers()).stream().//
				filter(Objects::nonNull).//
				filter(container -> container.names().contains(search)).//
				findFirst().map(container -> container.id()).orElse(null);
		return id;
	}

	public boolean removeContainer(String name) throws DockerCertificateException, DockerException, Exception {
		String id = getContainerIdByName(name);
		if (id != null) {
			this.client.removeContainer(id);
		}
		return id != null;
	}

}
