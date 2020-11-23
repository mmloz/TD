package Utils;

import Models.Response;
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
import java.util.List;

public class RequestUtils {
    private String defCharset = "UTF-8";
    private String contentType = "application/json";

    public RequestUtils(){}

    public RequestUtils(String defCharset, String contentType) {
        this.defCharset = defCharset;
        this.contentType = contentType;
    }

    public String sendPostRequest(String url, List<NameValuePair> params) throws IOException
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

    public Response sendPostRequest(String url, String params, String username, String password) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", contentType)
                .header("Authorization", basicAuth(username, password))
                .header("Accept", contentType)
                .POST(HttpRequest.BodyPublishers.ofString(params))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getResponse(response);
    }

    public Response sendGetRequest(String website, String username, String password) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(website))
                .header("Content-Type", contentType)
                .header("Authorization", basicAuth(username, password))
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getResponse(response);
    }

    public String sendMultipart(String website, String filePath, String username, String password) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(website);
        String authentication = basicAuth(username, password);
        File f = new File(filePath);

        uploadFile.addHeader("Authorization", authentication);
        uploadFile.addHeader("Attachment", f.getName());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addBinaryBody("attachment", f);

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
            return EntityUtils.toString(responseEntity, defCharset);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Response getResponse(HttpResponse<String> response){
        return Response.builder()
                .code(String.valueOf(response.statusCode()))
                .body(response.body())
                .build();
    }

    private String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
