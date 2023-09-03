package tests.email.hotmail;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ListEmailsByDateRange {
    public static void main(String[] args) throws Exception {
        String accessToken = "<accessToken>";
        String startDateTime = "2023-08-16T00:00:00Z"; // Replace with your desired start date
        String endDateTime = "2023-08-15T22:59:59Z";   // Replace with your desired end date

        String apiUrl = "https://graph.microsoft.com/v1.0/users/" +
                        "<userID>" +
                        "/messages" +
                        "?$filter=receivedDateTime+ge+" + URLEncoder.encode(startDateTime, StandardCharsets.UTF_8) +
                        "+and+receivedDateTime+le+" + URLEncoder.encode(endDateTime, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            System.out.println(responseBody);
        } else {
            System.out.println("Error: " + response.statusCode());
            System.out.println("Error body: " + response.body());
        }
    }
}
