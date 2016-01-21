import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Client {

	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		if (args.length > 2) {
			String fileName = args[2];
		}

		try {
			Socket socket = new Socket(host, port);
			OutputThread output = new OutputThread(socket);
			InputThread input = new InputThread(socket);
			Thread outputThread = new Thread(output);
			Thread inputThread = new Thread(input);
			outputThread.start();
			inputThread.start();
		} catch(IOException e) {
			System.err.println("Error: " + e);
		}
	}
}

class OutputThread implements Runnable {
	Boolean running = true;
	String message = "";
	String id = null;
	Socket socket = null;
	PrintStream output;
   	BufferedReader userInput;


	OutputThread(Socket _socket) {
		socket = _socket;
		try{
			output = new PrintStream(socket.getOutputStream());
		}catch(IOException e){
			System.err.println("Error in OutputThread: " + e);
		}
	}

	public void run() {
		while (running) {
			userInput = new BufferedReader(new InputStreamReader(System.in));
			try{
				message = userInput.readLine();
				sendMessage(message);

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
	String msg = "";

	InputThread(Socket _socket) {
		socket = _socket;
		try{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(IOException e){
			System.err.println("Error in InputThread: " + e);
		}
	}

	public void run(){
		while(running){
			if(!socket.isClosed()){
				try{
					msg = input.readLine();
					if(msg != null){
						System.out.println(msg);
					}
				}catch(IOException e){
					System.out.println(e.getMessage());
				}
			}else{
				running = false;
			}
		}
	}
}