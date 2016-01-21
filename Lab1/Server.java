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
	ArrayList<Socket> sockets;
	ServerSocket serverSocket;

	ServerWorker(int _port) {
		try {
			serverSocket = new ServerSocket(_port);
			sockets = new ArrayList<Socket>();
			System.out.println("Server started, port: " + _port);
		} catch(IOException e) {
			System.err.println("Error (unable to start server)");
		}
	}

	public void run() {
		while(running) {
			try {
				Socket socket = serverSocket.accept();
				sockets.add(socket);
				ServerConnection client = new ServerConnection(socket, this);
				Thread clientThread = new Thread(client);
				clientThread.start();
				System.out.println("New connection established, source address: " + socket.getInetAddress());
			} catch (IOException e) {
				System.err.println("Error (unable to start connection)");
			}
		}
	}

	public synchronized void broadcastAll(String _message) {
		for (Socket socket : sockets) {
			try {
				PrintStream output = new PrintStream(socket.getOutputStream());
				output.println(_message);
				output.flush();
			} catch(IOException e) {
				System.err.println("Error (unable to broadcast to " + socket.getInetAddress() + "): " + e);
			}
		}
	}

	public void removeSocket(Socket _socket) {
		sockets.remove(_socket);
		System.out.println("Removed connection, source address: " + _socket.getInetAddress());
		System.out.println("Current sockets: ");
		for(Socket socket : sockets) {
			System.out.println("Source address: " + socket.getInetAddress());
		}
	}
}

class ServerConnection implements Runnable {
	boolean running = true;
	Socket socket;
	ServerWorker serverWorker;

	ServerConnection(Socket _socket, ServerWorker _serverWorker) {
		socket = _socket;
		serverWorker = _serverWorker;
	}

	public void run() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (running) {
				if (!socket.isClosed()) {
					String message = input.readLine();
					if (message.equals("@logout")) {
						removeConnection();
					} else {
						serverWorker.broadcastAll(message);
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
			serverWorker.removeSocket(socket);
			socket.shutdownOutput();
			socket.close();
			running = false;
		} catch(IOException e) {
			System.err.println("Error (unable to remove connection): " + e);
		} 
	}
}