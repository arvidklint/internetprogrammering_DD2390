import java.io.*;
import javax.net.ssl.*;


public class Server {
  static final int port = 1234;
  SSLServerSocket sslServerSocket;

  public static void main(String[] args) {
    try {
      SSLServerSocketFactory sslServfact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
      sslServerSocket = (SSLServerSocket)sslSrvFact.createServerSocket(port);

      SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();
      OutputStream out = sslSocket.getOutputStream();
      InputStream in = sslSocket.getInputStream();
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
