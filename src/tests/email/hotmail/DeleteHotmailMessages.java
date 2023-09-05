package tests.email.hotmail;

//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpDelete;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.apache.http.HttpResponse;
//import org.apache.http.Header;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.message.BasicHeader;

public class DeleteHotmailMessages {
    public static void main(String[] args) throws Exception {
        String accessToken = "YOUR_ACCESS_TOKEN";
        String messageId = "MESSAGE_ID_TO_DELETE";

        String endpoint = "https://graph.microsoft.com/v1.0/me/messages/" + messageId;

/*
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(endpoint);

        // Set authorization header
        httpDelete.setHeader("Authorization", "Bearer " + accessToken);

        // Execute the request
        HttpResponse response = httpclient.execute(httpDelete);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 204) {
            System.out.println("Email deleted successfully.");
        } else {
            System.out.println("Error deleting email. Status code: " + statusCode);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Response body: " + responseBody);
        }

        httpclient.close();
*/
    }
}
