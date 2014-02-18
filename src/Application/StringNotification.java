package Application;

import Framework.Notification;


/**
 * The Notification class contains a String which can be sent between machines.
 * 
 * @author Daniel
 *
 */
public class StringNotification implements Notification{

	private static final long serialVersionUID = -2424024103750531110L;
	private String message;
	
	public StringNotification(String message){
		this.message=message;
	}

	/* (non-Javadoc)
	 * @see Framework.NotificationInterface#getMessage()
	 */
	public String getMessage(){
		return message;
	}
}
