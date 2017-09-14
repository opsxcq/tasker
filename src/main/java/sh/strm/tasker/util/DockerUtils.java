package sh.strm.tasker.util;

import com.spotify.docker.client.DefaultDockerClient;

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

}
