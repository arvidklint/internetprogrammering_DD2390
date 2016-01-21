import java.util.*;
import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerWorker serverWorker = new ServerWorker(1337);
		Thread serverWorkerThread = new Thread(serverWorker);
		serverWorkerThread.start();
	}
}

class ServerWorker implements Runnable {
	boolean running = true;
	//ArrayList<Socket> sockets;
	ServerSocket serverSocket;
	ArrayList<ServerConnection> connections;

	ServerWorker(int _port) {
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

	public synchronized void broadcastAll(String _message, String _username, String _id) {
		for (ServerConnection connection : connections) {
			if (!connection.socket.getInetAddress().toString().equals(_id)) {
				sendMessage(connection, _message, _username);
			}
		}
	}

	public synchronized void broadcastTo(String _message, String _receivingUser, String _sourceUser) {
		for(ServerConnection connection : connections){
			if(connection.username.equals(_receivingUser)){
				System.out.println("Sending message to: " + _receivingUser);
				sendMessage(connection, _message, _sourceUser);
				break;
			}
		}
	}

	private void sendMessage(ServerConnection _connection, String _message, String _sourceUser) {
		try {
			PrintStream output = new PrintStream(_connection.socket.getOutputStream());
			output.println(_sourceUser + ": " + _message);
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

class ServerConnection implements Runnable {
	boolean running = true;
	public Socket socket;
	ServerWorker serverWorker;
	public String username = "";
	public String id = "";

	ServerConnection(Socket _socket, ServerWorker _serverWorker) {
		socket = _socket;
		username = socket.getInetAddress().toString();
		id = socket.getInetAddress().toString();
		serverWorker = _serverWorker;
	}

	public void run() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (running) {
				if (!socket.isClosed()) {
					String message = input.readLine();
					StringTokenizer tokens = new StringTokenizer(message, " ");
					
					if (tokens.hasMoreTokens()) {
						String command = tokens.nextToken();
						if (command.equals("@logout")) {
							removeConnection();
						} else if (command.equals("@username")) {
							username = tokens.nextToken();
						} else if (command.equals("@whisper")) {
							String reciever = tokens.nextToken();
							String privateMessage = tokens.nextToken("");
							serverWorker.broadcastTo(privateMessage, reciever, username);

						}
						else {
							serverWorker.broadcastAll(message, username, id);
						}
					}
				} else {
					removeConnection();
				}
			}
		} catch(IOException e) {
			System.err.println("Error (ServerConnection): " + e);
		}
	}

	private void removeConnection() {
		try{
			serverWorker.removeConnectionFromList(this);
			socket.shutdownOutput();
			socket.close();
			running = false;
		} catch(IOException e) {
			System.err.println("Error (unable to remove connection): " + e);
		} 
	}
}