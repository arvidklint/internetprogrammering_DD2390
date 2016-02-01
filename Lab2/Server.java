import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class Server{

    public static void main(String[] args) throws IOException{
		ServerSocket ss = new ServerSocket(8081);
		while(true){
			System.out.println("Waiting for connections.");
		    Socket s = ss.accept();
		    Integer randomNumber = 9000000000;
		    BufferedReader request = new BufferedReader(new InputStreamReader(s.getInputStream()));
		    PrintStream response = new PrintStream(s.getOutputStream());
		    String str = request.readLine();

		    StringTokenizer tokens = new StringTokenizer(str," ?");
		    Integer guessNumber = Integer.parseInt(tokens.nextToken());
			String requestedDocument = tokens.nextToken();
			System.out.println("Guessed number is: " + guessNumber);
			System.out.println("requestedDocument is: " + requestedDocument);


			/* HTTP HEADER*/
			response.println("HTTP/1.0 200 OK");
			response.println("Server : Guessing Game 1.0");
			if(requestedDocument.indexOf(".html") != -1)
				response.println("Content-Type: text/html");
			response.println("Set-Cookie: clientId=1; expires=Wednesday,31-Dec-2017 21:00:00 GMT");
			
			/* HTTP BODY */
			response.println();
 			File file = new File("."+requestedDocument);

		    FileInputStream fileInput = new FileInputStream(file);
		    byte[] bytes = new byte[1024];
		    while( fileInput.available() > 0){
				response.write(bytes,0,fileInput.read(bytes));
		    }

		    response.println("Your guess was: " + guessNumber);
		    response.writeBytes(responseString);
		    response.flush();
		    response.close();		 
	 
		  //   PrintStream response = new PrintStream(s.getOutputStream());
		  //   response.println("HTTP/1.0 200 OK");
		  //   response.println("Server : Slask 0.1 Beta");
		  //   if(requestedDocument.indexOf(".html") != -1)
				// response.println("Content-Type: text/html");
		  //   if(requestedDocument.indexOf(".gif") != -1)
				// response.println("Content-Type: image/gif");
		    
		  //   response.println("Set-Cookie: clientId=1; expires=Wednesday,31-Dec-2017 21:00:00 GMT");

		  //   response.println();
		  //   File f = new File("."+requestedDocument);
		  //   FileInputStream infil = new FileInputStream(f);
		  //   byte[] b = new byte[1024];
		  //   while( infil.available() > 0){
				// response.write(b,0,infil.read(b));
		  //   }
		  //  s.shutdownOutput();
		    
		}
    }
}

