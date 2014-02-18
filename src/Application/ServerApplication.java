package Application;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Framework.NotificationSource;

/**
 * The main class of the Server side part of the application. Contains all
 * the methods which directly interact with the Framework.
 * 
 * @author Daniel
 *
 */
public class ServerApplication {

	int port = 1099;

	/**
	 * Creates a ServerApplication which will try to create an RMI Registry and
	 * a NotificationSource and will register the source on the Registry with
	 * the given name.
	 * 
	 * @param name The given name to register the notification source with.
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	public ServerApplication(String name) throws RemoteException,
			MalformedURLException {

		Registry rmiregistry = null;
		boolean created = false;

		/*
		 * Will try to create a registry on port 1099, if it can't it will
		 * increase the port number and try again, continuing until it creates a
		 * registry or it's looped over 50 port numbers.
		 */

		while (!created) {
			try {
				rmiregistry = LocateRegistry.createRegistry(port);
				created = true;
			} catch (RemoteException e) {
				port++;
			}
			if (port > 1149) {
				System.out.println("50 ports checked");
				break;
			}
		}

		if (created && rmiregistry != null) {
			NotificationSource source = new NotificationSource();
			rmiregistry.rebind(name, source);
		} else {
			throw new RemoteException("RMI Registry couldn't be created");
		}

	}

	/**
	 * Returns the port number that was used.
	 * 
	 * @return The port number.
	 */
	public int getPort() {
		return port;
	}

}
