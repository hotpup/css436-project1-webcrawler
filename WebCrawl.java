
// Caleb Chun
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.MalformedInputException;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawl {
    private static HashSet<String> visitedURLs = new HashSet<>();
    private static int numHops;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java WebCrawl <url> <num_hops>");
            return;
        }
        String stringUrl = args[0];
        visitedURLs.add(stringUrl);
        try {
            numHops = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("The second argument must be an integer.");
            return;
        }

        if (!search(stringUrl) && numHops > 1) {
            return;
        }
    }

    private static boolean search(String stringURL) {
        try {
            URL url = new URL(stringURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);

            int statusCode = connection.getResponseCode();
            if (statusCode >= 300 && statusCode < 400) {
                String newURL = connection.getHeaderField("Location");
                if (newURL != null) {
                    return search(newURL);
                }
            } else if (statusCode >= 400) {
                System.out.println("Error: " + statusCode + " for URL: " + stringURL);
                return false;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                int index = 0;
                while ((index = line.indexOf("<a href=\"", index)) != -1) {
                    index += 9;
                    int endIndex = line.indexOf("\"", index);
                    if (endIndex != -1) {
                        String foundURL = line.substring(index, endIndex);
                        if (!foundURL.startsWith("http://") && !foundURL.startsWith("https://")) {
                            continue;
                        }
                        if (visitedURLs.contains(foundURL)) {
                            continue;
                        }

                        visitedURLs.add(foundURL);
                        System.out.println("Visiting: " + foundURL);
                        numHops -= 1;

                        if (numHops <= -1) {
                            return false;
                        }

                        if (!search(foundURL)) {
                            reader.close();
                            connection.disconnect();
                            return false;
                        } else {
                            reader.close();
                            connection.disconnect();
                            return true;
                        }
                    }
                }
            }
            System.out.println("No new links found");
            reader.close();
            connection.disconnect();
        } catch (MalformedInputException e) {
            System.out.println("The URL is malformed: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("An I/O error occurred: " + e.getMessage());
            return false;
        }
        return true;
    }
}
