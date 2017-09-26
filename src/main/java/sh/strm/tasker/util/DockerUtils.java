package sh.strm.tasker.util;

import com.spotify.docker.client.DefaultDockerClient;
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

	public static void listNetworks() throws Exception {
		DefaultDockerClient docker = DefaultDockerClient.fromEnv().build();
		for (Network network : docker.listNetworks()) {
			System.out.println("-------------NETWORK=" + network.name());
		}
		for (Network network : docker.listNetworks()) {
			System.out.println(network);
		}
	}

}
