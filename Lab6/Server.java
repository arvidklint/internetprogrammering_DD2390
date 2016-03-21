import java.io.*;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;

public class Server {
  static final int port = 1234;

  public static void main(String[] args) {
    SSLServerSocket sslServerSocket;

    try {
      SSLServerSocketFactory sslServFact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
      sslServerSocket = (SSLServerSocket)sslServFact.createServerSocket(port);

      System.out.println("Waiting for connection.");
      SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();
      System.out.println("Connection established?...");
      PrintStream out = new PrintStream(sslSocket.getOutputStream());

      out.println("HTTP/1.0 200 OK");
      out.println("Server : SSLServer 0.1 Beta");
      out.println("Content-Type: text/html");
      out.println();
      out.println("<h1>Hello World!</h1>");
      out.flush();
      out.close();

      sslSocket.close();
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
