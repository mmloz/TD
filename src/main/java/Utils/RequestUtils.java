package Utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.List;

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
}
