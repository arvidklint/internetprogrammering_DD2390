import java.util.*;
import java.net.*;
import java.io.*;

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