import java.util.*;
import java.net.*;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(1337);
		ArrayList<Socket> sockets = new ArrayList<Socket>();

		boolean running = true;
		while(running) {
			Socket socket = serverSocket.accept();
			sockets.add(socket);
			ServerConnection client = new ServerConnection(socket);
			Thread clientThread = new Thread(client);
			clientThread.start();
		}
	}

	public static broadcastAll(String _message) {
		for (Socket socket : sockets) {
			PrintStream output = new PrintStream(socket.getOutputStream());
			output.println(_message);
			output.flush();
		}
	}
}

class ServerConnection implements Runnable {
	boolean running = true;
	Socket socket;

	ServerConnection(Socket _socket) {
		socket = _socket;
	}

	public void run() {
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		while (running) {
			Server.broadcastAll(input.readline()));
		}
	}
}