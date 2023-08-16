package tests.email.hotmail;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;

public class ListEmailsByDateRangeExample {
    public static void main(String[] args) throws Exception {
        String accessToken = "YOUR_ACCESS_TOKEN";
        String startDateTime = "2023-01-01T00:00:00Z"; // Replace with your desired start date
        String endDateTime = "2023-08-31T23:59:59Z";   // Replace with your desired end date

        String apiUrl = "https://graph.microsoft.com/v1.0/me/messages" +
                        "?$filter=receivedDateTime ge " + startDateTime +
                        " and receivedDateTime le " + endDateTime;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);

        // Set authorization header
        httpGet.setHeader("Authorization", "Bearer " + accessToken);

        // Execute the request
        HttpResponse response = httpclient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        // Parse the JSON response to extract email messages
        // Sample code to parse JSON and extract email messages
        System.out.println(responseBody);

        httpclient.close();
    }
}
