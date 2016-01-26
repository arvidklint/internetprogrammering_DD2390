import java.util.*;
import java.net.*;
import java.io.*;

public class Server implements Runnable{
	public static void main(String[] args) throws IOException {
		Server server = new Server(1337);
		Thread serverThread = new Thread(server);
		serverThread.start();
	}

	boolean running = true;
	ServerSocket serverSocket;
	ArrayList<ServerConnection> connections;

	Server(int _port) {
		try {
			serverSocket = new ServerSocket(_port);
			connections = new ArrayList<ServerConnection>();
			System.out.println("Server started, port: " + _port);
		} catch(IOException e) {
			System.err.println("Error (unable to start server)");
		}
	}

	public void run() {
		while(running) {
			try {
				Socket socket = serverSocket.accept();
				//sockets.add(socket);
				ServerConnection client = new ServerConnection(socket, this);
				Thread clientThread = new Thread(client);
				connections.add(client);
				clientThread.start();
				System.out.println("New connection established, source address: " + socket.getInetAddress());
			} catch (IOException e) {
				System.err.println("Error (unable to start connection)");
			}
		}
	}

	public synchronized void broadcastAll(String _message, String _id) {
		for (ServerConnection connection : connections) {
			if (!connection.socket.getInetAddress().toString().equals(_id)) {
				System.out.println("Broadcasting message to all users.");
				sendMessage(connection, _message);
			}
		}
	}

	public synchronized void broadcastTo(String _message, String _receivingUser) {
		for(ServerConnection connection : connections){
			if(connection.username.equals(_receivingUser)){
				System.out.println("Sending personal message to: " + _receivingUser);
				sendMessage(connection, _message);
				break;
			}
		}
	}

	private void sendMessage(ServerConnection _connection, String _message) {
		try {
			PrintStream output = new PrintStream(_connection.socket.getOutputStream());
			output.println(_message);
			output.flush();
		}catch(IOException e) {
			System.err.println("Error (unable to broadcast to " + _connection.socket.getInetAddress() + "): " + e);
		}
	}

	public synchronized void removeConnectionFromList(ServerConnection _connection) {
		connections.remove(_connection);
		System.out.println("Removed connection, source address: " + _connection.socket.getInetAddress());
		System.out.println("Current sockets: ");
		for(ServerConnection connection : connections) {
			System.out.println("Source address: " + connection.socket.getInetAddress());
		}
	}
}