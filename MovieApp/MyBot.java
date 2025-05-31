import org.jibble.pircbot.*;
import java.util.*;
public class MyBot extends PircBot {
    
    public MyBot() {
        this.setName("BessieYoungJock");
        
    }
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.contains("Show:")) {
            String[] parts = message.split(" ", 3);
            if (parts.length < 3) {
                sendMessage(channel, "Please put 'Show: <movie title> <year>");
            } else {
                try {
                    makeApi api = new makeApi();
                    String response = api.fetchMovieData(parts[1], Integer.parseInt(parts[2]));
                    if (response != null) {
                        sendMessage(channel, "Finding the movie...");
                        api.displayData(response);
                    } else {
                        sendMessage(channel, "Movie not found.");
                    }
                } catch (Exception e) {
                    sendMessage(channel, "Error retrieving movie data.");
                }
            }
        }
    }

}