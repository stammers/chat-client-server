package Framework;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


/**
 * A NotificationSource stores a List of registered Sinks and when 
 * notified it will send that Notification onto all registered Sinks.
 * Also handles unreachable/disconnected Sinks.
 * 
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class NotificationSource extends UnicastRemoteObject implements Source{

	private volatile List<Sink> sinks;
	private volatile List<Sink> unreachable;

	/**
	 * Creates a NotificationSource, and initialises the lists of sinks and 
	 * unreachable sinks.
	 * 
	 * @throws RemoteException
	 */
	public NotificationSource() throws RemoteException{
		super();
		sinks = new ArrayList<Sink>();
		unreachable = new ArrayList<Sink>();
	}

	/* (non-Javadoc)
	 * @see Source#registerSink(Sink)
	 */
	public void registerSink(Sink sink) throws RemoteException{
		//only register a sinks if it isn't already
		if(!sinks.contains(sink)){
			sinks.add(sink);
		}
	}

	/* (non-Javadoc)
	 * @see Source#notify(Notification)
	 */
	public void notify(Notification message) throws RemoteException{
		List<Sink> temp1 = new ArrayList<Sink>(sinks);

		/*
		 * Allows for the sink to be unreachable for one notify call.
		 * If it is still unreachable on the next it is removed from the list,
		 * if it is reachable it is removed from the unreachable list.
		 */
		for(Sink sink : temp1){
			try {
				sink.notify(message);
				if(unreachable.contains(sink)){
					unreachable.remove(sink);
				}
			} catch (RemoteException e) {
				if(unreachable.contains(sink)){
					sinks.remove(sink);
					unreachable.remove(sink);
				}else{
					unreachable.add(sink);
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see Source#checkConnection()
	 */
	@Override
	public boolean checkConnection() throws RemoteException {
		return true;
	}

	/* (non-Javadoc)
	 * @see Framework.Source#removeSink(Framework.Sink)
	 */
	@Override
	public void removeSink(Sink sink) throws RemoteException {
		sinks.remove(sink);
		unreachable.remove(sink);		
	}


}
