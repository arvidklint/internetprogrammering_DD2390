import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

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