package Application;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Framework.NotificationSink;
import Framework.Source;

/**
 * The main class of the client, contains all the methods which directly work
 * with the Framework.
 * 
 * @author Daniel
 *
 */
public class ClientApplication {

	private List<Source> sources;
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private NotificationSink sink;

	/**
	 * Creates a ClientApplication and gets a Source from a remote RMI Registry
	 * using the provided details. Also creates a NotificationSink and registers
	 * it to the found Source.
	 * 
	 * @param name The name of the registered Source.
	 * @param ip The ip address of the machine running the registry.
	 * @param port The port number the registry is using.
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public ClientApplication(String name, String ip, String port) throws MalformedURLException, RemoteException, NotBoundException {
		sources = new ArrayList<Source>();
		Source source = (Source) Naming.lookup("rmi://" + ip + ":" + port + "/" + name);
		sources.add(source);
		createSink();
		registerSink(source);
	}

	/**
	 * Constructs a Notification using the passed Strings and tries to notify
	 * the Source with it.
	 * 
	 * @param message The String passed from the Client GUI.
	 * @param user The current Client username.
	 * @throws ConnectException
	 */
	public void userMessage(String message, String user) throws ConnectException {
		Date date = new Date();
		String time = dateFormat.format(date);
		String fullMessage = user + ": " + message + " (" + time + ")";
		StringNotification notify = new StringNotification(fullMessage);

		int errors = 0;

		// no connected sources so error should be shown.
		if (sources.isEmpty()) {
			errors++;
		}
		for (Source source : sources) {
			try {
				source.notify(notify);
			} catch (RemoteException e) {
				errors++;
			}
		}
		if (errors > 0) {
			throw new ConnectException("One of the connected servers couldn't be reached");
		}
	}

	/**
	 * Creates a NotificationSink.
	 * 
	 * @throws RemoteException
	 */
	private void createSink() throws RemoteException {
		sink = new NotificationSink();
	}

	/**
	 * Registers the global Sink with the passed Source.
	 * 
	 * @param source The remote Source to register the Sink with.
	 * @throws RemoteException
	 */
	private void registerSink(Source source) throws RemoteException {
		source.registerSink(sink);
	}

	/**
	 * Tries to get a reference to the remote Source via the rmi interface
	 * using the provided details.
	 * 
	 * @param name The name the remote Source is registered with.
	 * @param ip The ip address of the machine running the registry.
	 * @param port The port number the registry is using.
	 * @return If the Source lookup was successful.
	 */
	public boolean connectServer(String name, String ip, String port) {
		Source source = null;
		try {
			source = (Source) Naming.lookup("rmi://" + ip + ":" + port + "/" + name);
			sources.add(source);
		} catch (MalformedURLException e) {
			return false;
		} catch (RemoteException e) {
			return false;
		} catch (NotBoundException e) {
			return false;
		}
		try {
			registerSink(source);
		} catch (RemoteException e) {
			return false;
		}
		return true;
	}

	/**
	 * Loops over all the remote Sources stored and checks if they are still
	 * available, if not they are removed.
	 */
	public void cleanupSources() {
		List<Source> temp = new ArrayList<Source>(sources);
		for (Source source : temp) {
			boolean alive;
			try {
				alive = source.checkConnection();
			} catch (RemoteException e) {
				alive = false;
			}
			if (!alive) {
				sources.remove(source);
			}
		}

	}
	
	/**
	 * Loops over all the remote Sources stored and tries to remove the 
	 * Sink from them.
	 */
	public void removeSink(){
		for (Source source : sources){
			try {
				source.removeSink(sink);
			}catch (RemoteException e){}
		}
	}
}
