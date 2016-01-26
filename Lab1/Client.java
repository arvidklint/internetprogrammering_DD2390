import java.util.*;
import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);

		Client client = new Client(host, port);
	}

	String host;
	int port;
	Socket socket;
	OutputThread output;
	InputThread input;
	ArrayList<FileRequest> fileRequests;

	Client (String _host, int _port) {
		host = _host;
		port = _port;
		Scanner scanner = new Scanner(System.in);
		System.out.print("Username: ");
		String username = scanner.next();
		fileRequests = new ArrayList<FileRequest>();
		
		try {
			socket = new Socket(host, port);
			output = new OutputThread(this, socket, username);
			input = new InputThread(this, socket);
			Thread outputThread = new Thread(output);
			Thread inputThread = new Thread(input);
			outputThread.start();
			inputThread.start();
		} catch(IOException e) {
			System.err.println("Error: " + e);
		}
	}

	public void addFileRequest(String _user, String _filePath, int _fileSize) {
		fileRequests.add(new FileRequest(_user, _filePath, _fileSize));
		System.out.println("Added request from user: " + _user);
	}

	public boolean removeFileRequest(String _user) {
		boolean removed = false;
		for (FileRequest fileRequest : fileRequests) {
			if (fileRequest.username.equals(_user)) {
				fileRequests.remove(fileRequest);
				removed = true;
				break;
			}
		}
		if (removed) {
			System.out.println("Removed request from user: " + _user);
		}

		return removed;
	}

	public FileRequest getFileRequest(String _user) {
		for (FileRequest fileRequest : fileRequests) {
			if (fileRequest.username.equals(_user)) {
				return fileRequest;
			}
		}
		return null;
	}
}