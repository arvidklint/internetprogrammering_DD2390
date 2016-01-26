import java.util.*;
import java.net.*;
import java.io.*;

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