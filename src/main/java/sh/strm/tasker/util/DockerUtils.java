package sh.strm.tasker.util;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.exceptions.ConflictException;

public class DockerUtils {

	/**
	 * Delete a volume from the Docker host
	 * 
	 * @param name
	 *            Volume name
	 * @throws Exception
	 */
	public static void removeVolume(String name) throws Exception {
		boolean done = false;
		for (int i = 0; i < 50 && !done; i++) {
			try {
				DefaultDockerClient docker = DefaultDockerClient.fromEnv().build();
				docker.removeVolume(name);
				done = true;
			} catch (ConflictException e) {
				Thread.sleep(1000);
			}
		}
		if (!done) {
			throw new ConflictException("Volume is in use and cannot be removed");
		}
	}

}
