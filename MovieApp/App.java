import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jibble.pircbot.*;

public class App
{
	public static void main(String[] args) throws Exception{
		Scanner keybrd = new Scanner(System.in);
		 // Now start our bot up.
       MyBot bot = new MyBot();
        
        
        bot.setVerbose(true);
        bot.connect("irc.freenode.net"); //tells it where to connect to - this is the same as the web interface I linked in the last slide
        bot.joinChannel("#testChannel"); // Name of channel is you want to connect to - in this case it’s called “#testChannel” 
 //this is the default message it will send when your pircbot first goes live 
        bot.sendMessage("#testChannel", "Hey! Enter any message and I’ll respond!");
       
//this is just for me to use and test the API
		do {
			try {
				makeApi obj = new makeApi();
				System.out.print("Hello there, we give out great information about movies and such\n"+
				"Please type in the name of the movie and the year released for more information about the movies\n");
				String feedback = obj.fetchMovieData(keybrd.nextLine(), keybrd.nextInt());
				keybrd.nextLine();
				if(feedback != null)
				obj.displayData(feedback);
				else
					System.out.println("Movie is not in our database please put in another movie.");
				System.out.println("Would you like to play again(Yes/No)");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}while(keybrd.nextLine().equalsIgnoreCase("yes"));
		
		keybrd.close();
		bot.disconnect();
	}
   
}

class makeApi{
	private static String apiUrl = "http://www.omdbapi.com/?i=tt3896198&apikey=";
	private String apiKey;
	makeApi(){
	apiKey = "2a98062d";
	}
	makeApi(String tempKey){;
	apiKey = tempKey;
	apiUrl = apiUrl + "?api="+tempKey+"&\r\n";
	}
	 String fetchMovieData(String movieTitle, int date) throws Exception {
		 apiUrl += apiKey + "&t=" + movieTitle.replace(" ", "%20") + "&y=" + date;
	        URL url = new URL(apiUrl);

	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(5000); 
	        conn.setReadTimeout(5000);

	        int responseCode = conn.getResponseCode();
	        if (responseCode == 200) { 
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder response = new StringBuilder();
	            String input;
	            while ((input = reader.readLine()) != null) {
	                response.append(input);
	            }
	            reader.close();
	            return response.toString();
	        } else {
	            System.out.println("Error: " + responseCode);
	            return null;
	        }
	    }
	  void displayData(String response) {
		 JsonObject obj = JsonParser.parseString(response).getAsJsonObject();
		 if (obj.has("Response") && obj.get("Response").getAsString().equals("True")) {
			 String title = obj.get("Title").getAsString();
			 String ageRate = obj.get("Rated").getAsString();
			 String dateReleased = obj.get("Released").getAsString();
			 String runtime = obj.get("Runtime").getAsString();
			 String genre = obj.get("Genre").getAsString();
			 String actors = obj.get("Actors").getAsString();
			 String plot = obj.get("Plot").getAsString();
			 
			 System.out.printf("Title: %s\n Release Date: %s\n Rating: %s\n ", title, dateReleased, ageRate);
			 System.out.printf("Runtime: %s\n Genre: %s\n Actors: %s\n Plot: %s\n", runtime, genre, actors, plot);
		 }
		 else {
			 System.out.println("Error "+ obj.get("Error").getAsString()); 
		 }
	 }
}


