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

class ServerConnection implements Runnable {
	boolean running = true;
	public Socket socket;
	Server server;
	public String username = "";
	public String id = "";

	ServerConnection(Socket _socket, Server _server) {
		socket = _socket;
		username = socket.getInetAddress().toString();
		id = socket.getInetAddress().toString();
		server = _server;
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
							String receiver = tokens.nextToken();
							String privateMessage = tokens.nextToken("").trim();
							server.broadcastTo(username + " whispers: " + privateMessage, receiver);
						} else if (command.equals("@file")) {
							String receiver = tokens.nextToken();
							server.broadcastTo(command + " " + username, receiver);
							System.out.println("File request sent to " + receiver);
						} else if(command.equals("@fileanswer")){
							String receiver = tokens.nextToken();
							String answer = tokens.nextToken();
							System.out.println("Fileanswer has been received. The answer to " + receiver + " is: " + answer);
							server.broadcastTo(command + " " + username + " " + answer, receiver);
						} else if(command.equals("@filesocketopen")){
							String receiver = tokens.nextToken();
							String sourceIP = tokens.nextToken();
							String sourcePort = tokens.nextToken();
							System.out.println("A socket has been opened. " + sourceIP + " and " + sourcePort + " is sent to: " + receiver);
							server.broadcastTo(command + " " + username + " " + sourceIP + " " + sourcePort, receiver);
						}
						else {
							server.broadcastAll(username + ": " + message, id);
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
			server.removeConnectionFromList(this);
			socket.shutdownOutput();
			socket.close();
			running = false;
		} catch(IOException e) {
			System.err.println("Error (unable to remove connection): " + e);
		} 
	}
}