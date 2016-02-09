import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.Math;
import java.util.*;

public class RoboGuesser {
	public static void main(String args[]) {
		RoboGuesser roboGuesser = new RoboGuesser();
	}

	int[] guesses;
	private final String USER_AGENT = "Mozilla/5.0";

	RoboGuesser() {
		guesses = new int[100];


		for (int i = 0; i < 100; i++) {
			try {
				boolean guessing = true;
				int guess = 50;
				int total = 0;
				int lower = 0;
				int higher = 100;
				String cookieValue = null;
				String cookieName = null;
				String headerName = null;

				while(guessing) {
					URL url = new URL("http://localhost:4711/index.html?guess=" + guess);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setRequestProperty("User-Agent", USER_AGENT);

					if (cookieValue != null && cookieName != null) {
						connection.setRequestProperty("Cookie", cookieName + "=" + cookieValue);
					}
					
					for (int j = 2; (headerName = connection.getHeaderFieldKey(j)) != null; j++) {
						if (headerName.equals("Set-Cookie")) {
							String cookie = connection.getHeaderField(j);
							cookie = cookie.substring(0, cookie.indexOf(";"));
							cookieName = cookie.substring(0, cookie.indexOf("="));
							cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
						}
					}

					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String line;

					while ((line = reader.readLine()) != null) {
						if (line.toLowerCase().contains("between")) {
							StringTokenizer tokens = new StringTokenizer(line," ");
							while(!tokens.nextToken().equals("between")) { }
							lower = Integer.parseInt(tokens.nextToken());
							tokens.nextToken();
							higher = Integer.parseInt(tokens.nextToken().replace(".", ""));
						} 
						else if (line.toLowerCase().contains("badoonga")) {
							StringTokenizer tokens = new StringTokenizer(line," ");
							while(!tokens.nextToken().equals("guesses:")) { }
							guesses[i] = Integer.parseInt(tokens.nextToken().replace("</div>", ""));
							guessing = false;
							break;
						}
					}
					reader.close();


					guess = lower + (int)Math.floor((higher - lower)/2);
				}

				
			} catch (MalformedURLException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		}

		float total = 0;
		for (int i = 0; i < guesses.length; i++) {
			total += guesses[i];
		}
		System.out.println("Average number of guesses: " + total / guesses.length);
	}
}