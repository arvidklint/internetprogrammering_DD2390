import java.util.*;
import java.net.*;
import java.io.*;

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