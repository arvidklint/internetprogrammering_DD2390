import java.util.Scanner;
import java.net.*;

public class Client {
	PrintStream output;
	BufferedReader input;

	public static void main(String[] args) {
		String host = args[1];
		int port = args[2];
		if (args[3]) {
			String fileName = args[3];
		}

		try {
			Socket socket = new Socket(host, port);
			output = new PrintStream(socket.getOutputStream());
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			OutputThread outputThread(this);
			InputThread inputThread(this);
		} catch(IOException e) {
			System.err.println("Error: " + e);
		}
	}

	public synchronized sendMessage(String _message) {
		output.println("message");
		output.flush();
	}
}

class OutputThread implements Runnable {
	String message = "";
	Client client = null;
	String id = null;

	OutputThread(Client _client) {
		client = _client;
	}

	public void run() {
		while (running) {
			Scanner scan = new Scanner(System.in);
			message = scan.next();
			client.sendMessage(id, message);
		}
	}
}

class InputThread implements Runnable {

	InputThread() {

	}
}