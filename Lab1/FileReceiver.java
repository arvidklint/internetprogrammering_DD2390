import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

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