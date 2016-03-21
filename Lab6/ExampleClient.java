import java.io.*; 
import java.net.*; 
import javax.net.ssl.*;

public class Client{
	public static void main(String[] args){
		SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault(); 
		
		System.out.println("(sf) Supported Ciphers:");
		for(int i = 0; i < sf.getSupportedCipherSuites().length; i++){
			System.out.println(sf.getSupportedCipherSuites()[i]);
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(sf); 
		SSLSocket s = null;
		System.out.println("HTTPS Connection setDefault");
		System.out.println("SSLSocket 's' instantiated!");
		try{
			System.out.println("sockets being created");
			s = (SSLSocket)sf.createSocket("my.nada.kth.se",1234);
			System.out.println("socket is created");
		}
		catch(MalformedURLException e){ 
			System.out.println(e.getMessage());
		}
		catch(IOException e){
			System.out.println(e.getMessage()); 
		}

		System.out.println("(s) Supported Ciphers:");
		for(int i = 0; i < s.getSupportedCipherSuites().length; i++) {
			System.out.println(s.getSupportedCipherSuites()[i]);
		}

		String[] cipher = {"SSL_DH_anon_WITH_RC4_128_MD5"}; 
		s.setEnabledCipherSuites(cipher);
		System.out.println("(s) Enabled Cipher:");
		for(int i = 0; i < s.getEnabledCipherSuites().length; i++){
			System.out.println(s.getEnabledCipherSuites()[i]);
		}

		PrintWriter outFile = null;
		try{
			outFile = new PrintWriter(s.getOutputStream());
		}
		catch(IOException e){ 
			System.out.println(e.getMessage());	
		}
		outFile.println("Hello"); 
		outFile.close();
	}
}