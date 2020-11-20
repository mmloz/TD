package Utils;

import Constants.ResValues;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestUtils {
    private final String defCharset = "UTF-8";

    public String postWithParams(String url, List<NameValuePair> params) throws IOException
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        CloseableHttpResponse response = null;
        String body;

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity, defCharset);
        } finally
        {
            if (response != null) response.close();
        }

        return body;
    }

    public static Map<ResValues, String> sendPostRequest(String website, String values, String username, String password) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(website))
                .header("Content-Type", "application/json")
                .header("Authorization", basicAuth(username, password))
                .header("Accept","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(values))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return getResponseMap(response);
    }

    public static Map<ResValues, String> sendGetRequest(String website, String username, String password) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(website))
                .header("Content-Type", "application/json")
                .header("Authorization", basicAuth(username, password))
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getResponseMap(response);
    }

    public static String sendMultipart(String website, String filePath, String username, String password) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(website);
        String authentication = basicAuth(username, password);
        File f = new File(filePath);

        uploadFile.addHeader("Authorization", authentication);
        uploadFile.addHeader("Attachment", f.getName());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addBinaryBody("attachment", f);
        //uploadFile.setEntity(builder.build());

        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();

        try {
            return EntityUtils.toString(responseEntity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<ResValues, String> getResponseMap(HttpResponse<String> response){
        Map<ResValues, String> result = new HashMap<>();
        result.put(ResValues.CODE, String.valueOf(response.statusCode()));
        result.put(ResValues.BODY, response.body());

        return result;
    }

    private static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
