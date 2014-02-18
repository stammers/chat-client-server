package Framework;

import java.io.Serializable;

public interface Notification extends Serializable {

	/**
	 * Returns the message stored in the Notification.
	 * 
	 * @return The stored message.
	 */
	public String getMessage();
}
