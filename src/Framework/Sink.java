package Framework;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * The interface for NotificationSink, extends Remote so that it can be 
 * accessed remotely.
 * 
 * @author Daniel
 *
 */
public interface Sink extends Remote{

	/**
	 * Notifies the Sink with the given Notification.
	 * 
	 * @param message The given Notification message.
	 * @throws RemoteException
	 */
	public void notify(Notification message) throws RemoteException;
}
