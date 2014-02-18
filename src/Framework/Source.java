package Framework;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * The interface for NotificationSource which can be accessed Remotely.
 * 
 * @author Daniel
 *
 */
public interface Source extends Remote{

	/**
	 * Registers a given remote Sink with the current Source.
	 * 
	 * @param sink The given Sink reference.
	 * @throws RemoteException
	 */
	public void registerSink(Sink sink) throws RemoteException;

	/**
	 * Notifies all registered Sinks with the given Notification.
	 * 
	 * @param message The given Notification.
	 * @throws RemoteException
	 */
	public void notify(Notification message) throws RemoteException;

	/**
	 * Allows the remote connection to a Source to be checked.
	 * 
	 * @return A value so the connection can be checked.
	 * @throws RemoteException
	 */
	public boolean checkConnection() throws RemoteException;
	
	/**
	 * Removes a given Sink from the lists of registered, doesn't check if Sink
	 * is registered to begin with.
	 * 
	 * @param sink The given Sink to remove.
	 * @throws RemoteException
	 */
	public void removeSink(Sink sink) throws RemoteException;

}
