import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.MalformedInputException;

public class WebCrawl {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Webcrawler <url> <num_hops>");
            return;
        }
        String stringUrl = args[0];
        int numHops;
        try {
            numHops = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return;
        }
        try {

            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int statusCode = connection.getResponseCode();
            System.out.println(statusCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            connection.disconnect();
        } catch (MalformedInputException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}