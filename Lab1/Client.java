import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

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
	ArrayList<String> fileRequests;

	Client (String _host, int _port) {
		host = _host;
		port = _port;
		Scanner scanner = new Scanner(System.in);
		System.out.print("Username: ");
		String username = scanner.next();
		fileRequests = new ArrayList<String>();
		
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

	public void addFileRequest(String _user) {
		fileRequests.add(_user);
		System.out.println("Added request from user: " + _user);
	}

	public boolean removeFileRequest(String _user) {
		boolean removed = false;
		for (String fileRequest : fileRequests) {
			if (fileRequest.equals(_user)) {
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

	public String getFileRequest(String _user) {
		for (String fileRequest : fileRequests) {
			if (fileRequest.equals(_user)) {
				return fileRequest;
			}
		}
		return null;
	}
}

class OutputThread implements Runnable {
	Boolean running = true;
	String message = "";
	String username = "";
	String id = null;
	Socket socket = null;
	PrintStream output;
   	BufferedReader userInput;
   	Client client;

	OutputThread(Client _client, Socket _socket, String _username) {
		client = _client;
		socket = _socket;
		username = _username;
		try{
			output = new PrintStream(socket.getOutputStream());
			sendMessage("@username " + username);
		}catch(IOException e){
			System.err.println("Error in OutputThread: " + e);
		}
	}

	public void run() {
		while (running) {
			userInput = new BufferedReader(new InputStreamReader(System.in));
			try{
				message = userInput.readLine();
				StringTokenizer tokens = new StringTokenizer(message, " ");
				if (tokens.hasMoreTokens()) {
					String command = tokens.nextToken();
						

					/*
						The receiver of a file will use these commands
					*/
					if (command.equals("@fileanswer")) {
						String sourceUser = tokens.nextToken();
						String answer = tokens.nextToken();
						if (answer.equals("yes")) {
							sendMessage("@fileanswer " + sourceUser + " " + answer);
						} else if (answer.equals("no")) {
							if (client.removeFileRequest(sourceUser)) {
								sendMessage("@fileanswer " + sourceUser + " " + answer);
							} else {
								System.err.println("Could not remove the filerequest (probably due to wrong username)");
							}
						} else {
							System.err.println("Unrecognized command for file request answer!");
						}
					} 






					/*
						The sender of a file will use these commands
					*/
					else if (command.equals("@file")) {
						String receiver = tokens.nextToken();
						client.addFileRequest(receiver);
						sendMessage("@file " + receiver);
					}
					



					else {
						sendMessage(message);				
					}
				}


				if(message.equals("@logout")){
					shutdown();
				}
			}catch(IOException e){
				System.err.println("Error in userInput: " + e);
			}
		}
	}

	public synchronized void shutdown() {
		try{
			socket.shutdownOutput();
			socket.close();
			running = false;
		}catch(IOException e){
			System.err.println("Error, unable to shut down: " + e);
		}
	}

	public synchronized void sendMessage(String message) {
		output.println(message);
		output.flush();
	}
}

class InputThread implements Runnable {
	Boolean running = true;
	Socket socket = null;
	BufferedReader input;
	String message = "";
	Client client;
	PrintStream output;

	InputThread(Client _client, Socket _socket) {
		client = _client;
		socket = _socket;
		try{
			output = new PrintStream(socket.getOutputStream());
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(IOException e){
			System.err.println("Error in InputThread: " + e);
		}
	}

	public void run(){
		while(running){
			if(!socket.isClosed()){
				try{
					message = input.readLine();
					if(message != null){
						StringTokenizer tokens = new StringTokenizer(message, " ");
						if (tokens.hasMoreTokens()) {
							String command = tokens.nextToken();

							/*
								The receiver of a file will receive these commands
							*/
							if (command.equals("@file")) {
								String sourceUser = tokens.nextToken();
								client.addFileRequest(sourceUser);
								System.out.println(sourceUser + " wants to send you a file. Answer with '@fileanswer " + sourceUser + " yes/no': ");
							} 
							else if (command.equals("@filesocketopen")) {
								String sourceUser = tokens.nextToken();
								String sourceIP = tokens.nextToken();
								int port = Integer.parseInt(tokens.nextToken());
								if(client.removeFileRequest(sourceUser)) {
									FileReceiver fileReceiver = new FileReceiver(sourceIP, port);
									Thread fileReceiverThread = new Thread(fileReceiver);
									fileReceiverThread.start();
								}
							}

							/*
								The sender of a file will receive these commands
							*/
							else if (command.equals("@fileanswer")) {
								String receiver = tokens.nextToken();
								String answer = tokens.nextToken();
								if (answer.equals("yes")) {
									String fileRequest = client.getFileRequest(receiver); // Check if the filerequest exist
									if (fileRequest != null) {
										String ip = socket.getLocalAddress().toString().substring(1);
										int port = 1338;
										FileSender fileSender = new FileSender(ip, port);
										Thread fileSenderThread = new Thread(fileSender);
										fileSenderThread.start();
										sendMessage("@filesocketopen " + receiver + " " + ip + " " + port);
									}
								} else {

								}
							}



							else {
								System.out.println(message);
							}
						}
					}
				}catch(IOException e){
					System.out.println(e.getMessage());
				}
			}else{
				running = false;
			}
		}
	}

	public synchronized void sendMessage(String message) {
		output.println(message);
		output.flush();
	}
}

class FileSender implements Runnable {
	String myIP;
	int port;
	Socket socket;
	ServerSocket serverSocket;

	FileSender(String _myIP, int _port) {
		myIP = _myIP;
		try {
			serverSocket = new ServerSocket(port);
		} catch(IOException e) {
			System.err.println("Filesender could not initialize serverSocket");
		}
	}

	public void run() {
		try {
			socket = serverSocket.accept();
			System.out.println("filesocket connected: " + socket.getInetAddress());
		} catch(IOException e) {
			System.err.println("Filesender could not accept incoming socket request");
		}
	}
}

class FileReceiver implements Runnable {
	String sourceIP;
	int port;
	Socket socket;

	FileReceiver(String _sourceIP, int _port) {
		sourceIP = _sourceIP;
		port = _port;
	}

	public void run() {
		try {
			socket = new Socket(sourceIP, port);
		} catch(Exception e) {
			System.err.println("Filereceiver could not open socket: " + e);
		}
	}
}