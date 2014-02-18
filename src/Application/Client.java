package Application;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;


/**
 * The main client part of the application, provides a GUI for the ClientApplication class.
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class Client extends JFrame {

	private ClientApplication application = null;
	private JTextField entry;
	private String user;
	private JMenuBar menuMain;
	private JFrame popup;

	public Client(String title) {
		super(title);
	}

	/**
	 * Initialises the GUI, creating the main components and setting up listeners.
	 */
	public void init() {
		menuMain = new JMenuBar();
		JMenu connections = new JMenu("Connections");
		connections.setMnemonic('C');

		JMenuItem newServer = new JMenuItem("Connect to server");
		newServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				newServerConnect();
			}

		});
		connections.add(newServer);

		JMenuItem cleanup = new JMenuItem("Cleanup connections");
		cleanup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (application != null) {
					application.cleanupSources();
				}
			}

		});
		connections.add(cleanup);
		menuMain.add(connections);

		JPanel connectPanel = connectionWindow(true);

		//calls the application to remove the sinks from all sources
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				if(application != null){
					application.removeSink();
				}
				System.exit(0);
			}
		});
		this.setContentPane(connectPanel);
		this.setSize(500, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Creates and returns the JPanel where server details are entered
	 * to connect to. 
	 * 
	 * @param firstRun Whether this is the initial run or not.
	 * @return The created JPanel.
	 */
	private JPanel connectionWindow(final boolean firstRun) {
		JPanel connectPanel = new JPanel(new BorderLayout());
		JPanel serverInfo = new JPanel(new GridLayout(0, 2));
		JButton connect = new JButton("Connect");
		JLabel serverNameLbl = new JLabel("Server Name:");
		JLabel serverIpLbl = new JLabel("Server IP Address:");
		JLabel serverPortLbl = new JLabel("Server Port:");
		JLabel userNameLbl = new JLabel("Username:");
		final JTextField serverName = new JTextField();
		final JTextField serverIp = new JTextField();
		final JTextField serverPort = new JTextField();
		final JTextField userName = new JTextField();

		serverInfo.add(serverNameLbl);
		serverInfo.add(serverName);
		serverInfo.add(serverIpLbl);
		serverInfo.add(serverIp);
		serverInfo.add(serverPortLbl);
		serverInfo.add(serverPort);
		serverInfo.add(userNameLbl);
		serverInfo.add(userName);

		connectPanel.add(serverInfo, BorderLayout.CENTER);
		connectPanel.add(connect, BorderLayout.SOUTH);

		connect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				connectToServer(firstRun, serverName.getText(), serverIp.getText(), serverPort.getText());
				user = userName.getText();
			}
		});

		return connectPanel;
	}

	/** 
	 * Creates and returns the main JPanel containing all the chat components.
	 * Creates these components, adds all needed listeners and redirects System.out 
	 * to the created JTextArea.
	 * 
	 * @return The created JPanel.
	 */
	private JPanel mainWindow() {
		JPanel main = new JPanel(new BorderLayout());
		JPanel bottom = new JPanel(new BorderLayout());
		JTextArea display = new JTextArea();
		display.setEditable(false);
		DefaultCaret caret = (DefaultCaret) display.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollerDisplay = new JScrollPane(display);
		main.add(scrollerDisplay, BorderLayout.CENTER);
		main.add(bottom, BorderLayout.SOUTH);
		JButton send = new JButton("Send");
		entry = new JTextField();
		bottom.add(entry, BorderLayout.CENTER);
		bottom.add(send, BorderLayout.EAST);
		MessageSender sender = new MessageSender();
		send.addActionListener(sender);
		entry.addActionListener(sender);

		//Creates a printstream from a Filteredstream and redirects System.out to it.
		PrintStream aPrintStream = new PrintStream(new FilteredStream(new ByteArrayOutputStream(), display));
		System.setOut(aPrintStream);

		return main;
	}

	/**
	 * Either creates the ClientApplication and connects to a server or uses the 
	 * existing ClientApplication to connect to a server using the passed details.
	 * 
	 * @param firstRun Whether the client application should be created or not.
	 * @param name The name of the server/source.
	 * @param ip The ip address of the server.
	 * @param port THe port number the server is using.
	 */
	private void connectToServer(Boolean firstRun, String name, String ip, String port) {
		boolean created = false;
		if (firstRun) {
			created = createApplication(name, ip, port);
		} else {
			created = application.connectServer(name, ip, port);
		}

		if (created) {
			if (firstRun) {
				JPanel main = mainWindow();
				this.setContentPane(main);
				this.setJMenuBar(menuMain);
				this.revalidate();
			} else {
				popup.dispose();
			}
		} else {
			JOptionPane.showMessageDialog(this,"Couldn't connect to the given server");

		}
	}

	/**
	 * Creates a ClientApplication which will connect to a server 
	 * using the given details.
	 * 
	 * @param name The server/source name.
	 * @param ip The ip address of the server.
	 * @param port The port number the server is using.
	 * @return If the creation was successful.
	 */
	private boolean createApplication(String name, String ip, String port) {
		if (name.equals("") || ip.equals("") || port.equals("")) {
			return false;
		}
		try {
			application = new ClientApplication(name, ip, port);
		} catch (MalformedURLException e) {
			return false;
		} catch (RemoteException e) {
			return false;
		} catch (NotBoundException e) {
			return false;
		}

		return true;

	}

	/**
	 * Sends the given string message to the ClientApplication.
	 * 
	 * @param text The entered message.
	 * @return If the ClientApplication used the message successfully.
	 */
	private boolean send(String text) {
		try {
			application.userMessage(text, user);
		} catch (ConnectException e) {
			System.out.println("Message couldn't be sent to all Servers, please check your network connection");
			System.out.println("If a server has stopped use cleanup connections from the menu to remove it");
			return false;
		}
		return true;
	}

	/**
	 * Creates a popup windows to connect to another server.
	 */
	private void newServerConnect() {
		popup = new JFrame("Connect to another server");
		JPanel panel = connectionWindow(false);
		popup.setContentPane(panel);
		popup.setSize(500, 300);
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setVisible(true);

	}

	public static void main(String[] args) {
		Client client = new Client("Client");
		client.init();

	}

	/**
	 * A listener which will send the entered text, clearing teh textfield if successful.
	 * @author Daniel
	 *
	 */
	class MessageSender implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean sent = send(entry.getText());
			if (sent) {
				entry.setText("");
			}
		}

	}
}
