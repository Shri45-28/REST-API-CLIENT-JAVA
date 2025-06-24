import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherAPIClientSimple {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter city name: ");
        String city = scanner.nextLine().trim();

        String apiKey = "95fd6774956f478f93a144517252306"; 
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                            + city + "&appid=" + apiKey + "&units=metric";

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

           
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
               
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String json = response.toString();
                String cityName = getValue(json, "\"name\":\"", "\"");
                String temperature = getValue(json, "\"temp\":", ",");
                String humidity = getValue(json, "\"humidity\":", "}");
                String description = getValue(json, "\"description\":\"", "\"");

                System.out.println("\nWeather Report for " + cityName + ":");
                System.out.println("Temperature: " + temperature + "°C");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Description: " + description);
            } else {
               
                System.out.println("HTTP Error: " + responseCode);
                System.out.println("Error retrieving weather data. Check your API key and city name.");
            }

        } catch (Exception e) {
            System.out.println("Error retrieving weather data. Please check the city name or API key.");
            e.printStackTrace();
        }
    }
 private static String getValue(String json, String keyStart, String keyEnd) {
        int start = json.indexOf(keyStart);
        if (start == -1) return "Not Found";
        start += keyStart.length();
        int end = json.indexOf(keyEnd, start);
        if (end == -1) return "Not Found";
        return json.substring(start, end);
    }


}
