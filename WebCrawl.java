import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebCrawl {
    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: java Webcrawler <url> <num_hops>");
            return;
        }
        String stringUrl = args[0];
        int numHops = args[1];
        
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        int statusCode = connection.getResponseCode();
        System.out.println(statusCode);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        

    }
}