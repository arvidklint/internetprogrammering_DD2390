import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class RoboGuesser {
	public static void main(String args[]) {
		RoboGuesser roboGuesser = new RoboGuesser();
	}

	int[] guesses;
	private final String USER_AGENT = "Mozilla/5.0";

	RoboGuesser() {
		guesses = new int[100];


		for (int i = 0; i < 1; i++) {
			try {
				URL url = new URL("http://localhost:4711/index.html?guess=50");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");

				connection.setRequestProperty("User-Agent", USER_AGENT);
				// connection.setRequestProperty("Cookie", cookieName + "=" + cookieValue);



				// OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
				String cookieValue = null;
				String cookieName = null;
				String headerName = null;
				for (int j = 2; (headerName = connection.getHeaderFieldKey(j)) != null; j++) {
					if (headerName.equals("Set-Cookie")) {
						String cookie = connection.getHeaderField(j);
						System.out.println(cookie);
						cookie = cookie.substring(0, cookie.indexOf(";"));
						cookieName = cookie.substring(0, cookie.indexOf("="));
						cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
					}
				}


				int responseCode = connection.getResponseCode();
				// BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				// String line;

				// while ((line = reader.readLine()) != null) {
				// 	System.out.println(line);
				// }
				// reader.close();
			} catch (MalformedURLException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	private void running() {
		
	}

	private void sendRequest() {

	}

	private void retreiveResponse() {

	}
}