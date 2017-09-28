package sh.strm.tasker.util;

import java.util.Objects;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Network;

public class DockerUtils {

	/**
	 * Delete a volume from the Docker host
	 * 
	 * @param name
	 *            Volume name
	 * @throws Exception
	 */
	public static void removeVolume(String name) throws Exception {
		DefaultDockerClient docker = DefaultDockerClient.fromEnv().build();
		docker.removeVolume(name);
	}

	/**
	 * Delete a network from the Docker host
	 * 
	 * @param name
	 *            Network name
	 * @throws Exception
	 */
	public static void removeNetwork(String name) throws Exception {
		DefaultDockerClient docker = DefaultDockerClient.fromEnv().build();
		for (Network network : docker.listNetworks()) {
			if (name.equals(network.name())) {
				docker.removeNetwork(network.id());
			}
		}
	}

	public static boolean removeContainer(String name) throws DockerCertificateException, DockerException, Exception {
		DefaultDockerClient docker = DefaultDockerClient.fromEnv().build();

		// Container names via api start with a slash /, so if the name doesn't have one, we add
		String search;
		if (name != null && !name.startsWith("/")) {
			search = "/" + name;
		} else {
			search = name;
		}
		String id = docker.listContainers(ListContainersParam.allContainers()).stream().//
				filter(Objects::nonNull).//
				filter(container -> container.names().contains(search)).//
				findFirst().map(container -> container.id()).orElse(null);

		if (id != null) {
			docker.removeContainer(id);
		}
		return id != null;
	}

}
