import java.io.*;
import java.net.*;
import java.util.*;

public class HttpServer {
	public static void main(String[] args) throws IOException{
		HttpServer server = new HttpServer();
	}

	ArrayList<User> users;

	HttpServer() throws IOException{
		ServerSocket ss = new ServerSocket(4711);

		users = new ArrayList<User>();

		

		while(true){
			Socket s = ss.accept();
			BufferedReader request = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String str = request.readLine();
			System.out.println(str);
			StringTokenizer tokens = new StringTokenizer(str," ?=");
			tokens.nextToken();
			String requestedDocument = tokens.nextToken();
			if (requestedDocument.equals("/")) {
				requestedDocument = "/index.html";
			}
			int guess = 0;
			if (tokens.hasMoreTokens()) {
				if (tokens.nextToken().equals("guess")) {
					guess = Integer.parseInt(tokens.nextToken());
				}
			}
			System.out.println(requestedDocument + " " + guess);

			String clientId = UUID.randomUUID().toString();

			// Go through the rest of the request
			while((str = request.readLine()) != null && str.length() > 0)  {
				System.out.println(str);
				if (str.toLowerCase().contains("cookie:")) {
					clientId = getClientId(str);
					// System.out.println("Client ID: " + clientId);
				}
			}

			User user;
			if ((user = getUser(clientId)) == null) {
				System.out.println("Could not find user, adding now");
				Random random = new Random();
				int max = 99;
				int min = 1;
				int randomNumber = random.nextInt((max - min) + 1) + min;
				user = new User(clientId, randomNumber);
				users.add(user);
			} else {
				System.out.println("Found User " + user.getID());
			}

			s.shutdownInput();


			String message;
			if (guess != 0) {
				user.addGuess();
				if (user.getRandomNumber() > guess) {
					setBoundaries(user, guess);
					message = "Too low, guess between " + user.getLower() + " and " + user.getHigher() + ". Number of guesses: " + user.getGuesses();
				} 
				else if (user.getRandomNumber() < guess) {
					setBoundaries(user, guess);
					message = "Too high, guess between " + user.getLower() + " and " + user.getHigher() + ". Number of guesses: " + user.getGuesses();
				} 
				else {
					user.setWinner(true);
					message = "Whooopa badoonga, Number of guesses: " + user.getGuesses();
				}
			}
			else {
				message = "Welcome! Guess the number between 0 and 100";
			}
	 
			PrintStream response = new PrintStream(s.getOutputStream());
			response.println("HTTP/1.0 200 OK");
			response.println("Server : Guess 0.1 Beta");
			if(requestedDocument.indexOf(".html") != -1)
				response.println("Content-Type: text/html");
			if(requestedDocument.indexOf(".gif") != -1)
				response.println("Content-Type: image/gif");
			
			response.println("Set-Cookie: clientId=" + clientId + "; expires=Wednesday,31-Dec-2017 21:00:00 GMT");

			response.println();

			try (BufferedReader br = new BufferedReader(new FileReader("." + requestedDocument))) {
				String line = null;
				while ((line = br.readLine()) != null) {
					response.println(line);
					if (line.equals("<body>")) {
						response.println("<div>" + message + "</div>");
					}
				}
			} catch (Exception e) {
				System.err.println("Error: " + e);
			}

			s.shutdownOutput();
			s.close();
		}
	}

	private void setBoundaries(User user, int _guess) {
		if (_guess > user.getRandomNumber()) {
			if (_guess < user.getHigher()) {
				user.setHigher(_guess);
			}
		}
		else if (_guess < user.getRandomNumber()) {
			if (_guess > user.getLower()) {
				user.setLower(_guess);
			}
		}
	}

	private String getClientId(String _str) {
		// System.out.println("Get client id");
		String cid = "";
		if(_str.contains("clientId")) {
			StringTokenizer tokens = new StringTokenizer(_str, " =;");
			String next = tokens.nextToken();
			while (tokens.hasMoreTokens()) {
				// go through until clientId is found
				if (tokens.nextToken().equals("clientId")) {
					cid = tokens.nextToken();
					break;
				} 
				// System.out.println(next);
			}
		} else {
			cid = UUID.randomUUID().toString();
		}
		return cid;
	}

	private User getUser(String _id) {
		for (User user: users) {
			if (user.getID().equals(_id)) {
				return user;
			}
		}
		return null;
	}
}

