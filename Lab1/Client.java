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

class FileRequest {
	String username;
	String filePath;
	int fileSize;

	FileRequest(String _username, String _filePath, int _fileSize) {
		username = _username;
		filePath = _filePath;
		fileSize = _fileSize;
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
						String filePath = tokens.nextToken();
						File tempFile = new File(filePath);
						int size = (int)tempFile.length();
						System.out.println("File size: " + size + " bytes");
						client.addFileRequest(receiver, filePath, size);
						sendMessage("@file " + receiver + " " + filePath + " " + size);
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
								String filePath = tokens.nextToken();
								int size = Integer.parseInt(tokens.nextToken());

								client.addFileRequest(sourceUser, filePath, size);
								System.out.println(sourceUser + " wants to send you a file: " + filePath + "(" + size / 1000 + " kb). Answer with '@fileanswer " + sourceUser + " yes/no': ");
							} 
							else if (command.equals("@filesocketopen")) {
								String sourceUser = tokens.nextToken();
								String sourceIP = tokens.nextToken();
								int port = Integer.parseInt(tokens.nextToken());
								System.out.println(sourceIP + " " + port);
								FileRequest fileRequest = client.getFileRequest(sourceUser);
								if(client.removeFileRequest(sourceUser)) {
									FileReceiver fileReceiver = new FileReceiver(sourceIP, port, fileRequest.filePath, fileRequest.fileSize);
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
									System.out.println(receiver + " answered yes to request to send file.");
									FileRequest fileRequest = client.getFileRequest(receiver); // Check if the filerequest exist
									if (client.removeFileRequest(receiver)) {
										System.out.println("Initialising filesender");
										String ip = socket.getLocalAddress().toString().substring(1);
										int port = 1338;
										FileSender fileSender = new FileSender(ip, port, fileRequest.filePath);
										Thread fileSenderThread = new Thread(fileSender);
										fileSenderThread.start();
										sendMessage("@filesocketopen " + receiver + " " + ip + " " + port);
									}
								} else {
									client.removeFileRequest(receiver);
									System.out.println("File transfer has been rejected.");
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
	String filePath;
	Socket socket;
	ServerSocket serverSocket;
	FileInputStream fileInputStream;
	BufferedInputStream bufferedInputStream;
	OutputStream outputStream;

	FileSender(String _myIP, int _port, String _filePath) {
		myIP = _myIP;
		port = _port;
		filePath = _filePath;
		try {
			serverSocket = new ServerSocket(port);
			System.err.println("initialized serversocket for file transfer (port: " + port + ")");
		} catch(IOException e) {
			System.err.println("Filesender could not initialize serverSocket");
		}
	}

	public void run() {
		try {
			System.out.println("Waiting for connection.");
			socket = serverSocket.accept();
			System.out.println("filesocket connected: " + socket.getInetAddress());
			File file = new File(filePath);
			byte [] bytes  = new byte [(int)file.length()];
			System.out.println("File transfer to " + socket.getInetAddress() + " is initialised.");
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			// bufferedInputStream.read(bytes, 0, bytes.length);
			outputStream = socket.getOutputStream();
			// outputStream.write(bytes, 0, bytes.length);
			int count;
			while((count = bufferedInputStream.read(bytes)) > 0) {
				outputStream.write(bytes, 0, count);
			}
			outputStream.flush();
			System.out.println("File transfer to " + socket.getInetAddress() + " is completed.");
		} catch(IOException e) {
			System.err.println("Filesender could not accept incoming socket request");
		} finally {
			try {
				if (outputStream != null) outputStream.close();
				if (bufferedInputStream != null) bufferedInputStream.close();
				if (socket != null) socket.close();
				if (serverSocket != null) serverSocket.close();
			} catch(IOException e) {
				System.err.println("Could not close shit.");
			}
		}
	}
}

class FileReceiver implements Runnable {
	String sourceIP;
	int port;
	int fileSize;
	String filePath;
	Socket socket;
	InputStream inputStream = null;
	FileOutputStream fileOutputStream = null;
	BufferedOutputStream bufferedOutputStream = null;

	FileReceiver(String _sourceIP, int _port, String _filePath, int _fileSize) {
		sourceIP = _sourceIP;
		port = _port;
		fileSize = _fileSize;
		filePath = _filePath;
	}

	public void run() {
		try {
			socket = new Socket(sourceIP, port);
			System.out.println("File connection is open. ");
			byte[] bytes = new byte[fileSize];
			inputStream = socket.getInputStream();
			fileOutputStream = new FileOutputStream(filePath);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			// inputStream.read(bytes, 0, bytes.length);
			// int current = bytesRead;

			// while (bytesRead > -1) {
			// 	bytesRead = inputStream.read(bytes, current, bytes.length - current);
			// 	if (bytesRead >= 0) {
			// 		current += bytesRead;
			// 	}
			// }


			// bufferedOutputStream.write(bytes, 0, bytes.length);
			int count;
			while ((count = inputStream.read(bytes)) > 0) {
				bufferedOutputStream.write(bytes, 0, count);
			}
			bufferedOutputStream.flush();
			System.out.println("File transfer complete! Good job!");

		} catch(Exception e) {
			System.err.println("Filereceiver could not open socket: " + e);
		} finally {
			try {
				if (fileOutputStream != null) fileOutputStream.close();
				if (bufferedOutputStream != null) bufferedOutputStream.close();
				if (socket != null) socket.close();
			} catch(IOException e) {
				System.err.println("Could not close shit.");
			}
		}
	}
}