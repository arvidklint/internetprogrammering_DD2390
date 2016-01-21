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

	public void broadcastAll(String _message) {
		for (Socket socket : sockets) {
			try{
				PrintStream output = new PrintStream(socket.getOutputStream());
				output.println(_message);
				output.flush();
				System.out.println("Broadcasted to all users, message: " + _message);
			} catch(IOException e) {
				System.err.println("Error (unable to broadcast to " + socket.getInetAddress() + "): " + e);
			}
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
				serverWorker.broadcastAll(input.readLine());
			}
		} catch(IOException e) {
			System.err.println("Error (ServerConnection): " + e);
		}
	}
}