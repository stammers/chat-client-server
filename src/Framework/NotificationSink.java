package Framework;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * A NotificationSink can be registered with a NotificationSource and can 
 * then be notified by that Source with Notifications.
 * 
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class NotificationSink extends UnicastRemoteObject implements Sink{

	public NotificationSink() throws RemoteException{
		super();
	}

	/* (non-Javadoc)
	 * @see Framework.Sink#notify(Framework.Notification)
	 */
	@Override
	public void notify(Notification message) throws RemoteException {
		System.out.println(message.getMessage());
	}
	
}
