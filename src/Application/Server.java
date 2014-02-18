package Application;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The main Server part of the application, provides a GUI for the ServerApplication.
 * 
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class Server extends JFrame {
	private ServerApplication serverApp;

	public Server(String title) {
		super(title);
	}

	/**
	 * Initialises the GUI, creating all the components and adding listeners
	 * where needed.
	 */
	public void init() {
		JPanel screen = new JPanel(new BorderLayout());
		JPanel main = new JPanel(new GridLayout(0, 2));
		screen.add(main, BorderLayout.CENTER);
		JLabel serverNameLbl = new JLabel("Server name: ");
		final JTextField serverName = new JTextField();
		main.add(serverNameLbl);
		main.add(serverName);

		JButton start = new JButton("Create Server");
		screen.add(start, BorderLayout.SOUTH);
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean created = createServer(serverName.getText());
				if (created) {
					mainScreenCreate(serverName.getText());
				}
			}

		});

		this.setContentPane(screen);
		this.setSize(300, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Creates a ServerApplication, passing it the given name.
	 * 
	 * @param name The given name.
	 * @return If the ServerApplication creation was successful.
	 */
	public boolean createServer(String name) {
		if (name.equals("")) {
			return false;
		}
		try {
			serverApp = new ServerApplication(name);
		} catch (RemoteException e) {
			return false;
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	/**
	 * Creates the main display screen of the Server, showing details needed for a 
	 * Client to connect.
	 * 
	 * @param name The Server name.
	 */
	private void mainScreenCreate(String name) {
		JPanel main = new JPanel(new BorderLayout());
		JLabel message = new JLabel("Server Running");
		main.add(message, BorderLayout.NORTH);

		JPanel details = new JPanel(new GridLayout(0, 1));
		JLabel serverName = new JLabel("Server Name: " + name);
		InetAddress addr = null;
		String ipAddress;
		try {
			addr = InetAddress.getLocalHost();
			ipAddress = addr.getHostAddress();
		} catch (UnknownHostException e) {
			ipAddress = "Error loading IP Address";
		}

		JLabel serverAddress = new JLabel("Server Ip Address: " + ipAddress);

		JLabel serverPort = new JLabel("Server Port: " + serverApp.getPort());
		details.add(serverName);
		details.add(serverAddress);
		details.add(serverPort);
		main.add(details, BorderLayout.CENTER);

		this.setContentPane(main);
		this.revalidate();
	}

	public static void main(String[] args) {
		Server server = new Server("Server");
		server.init();

	}

}
