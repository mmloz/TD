package Api;

import Api.Constants.UnionUrlParams;
import Utils.RequestUtils;
import Utils.UrlBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnionApiRequests {
    private final RequestUtils requestUtils = new RequestUtils();
    private final UrlBuilder urlBuilder = new UrlBuilder();

    public String getToken(String variant){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(UnionUrlParams.VARIANT, variant));

        String token = null;
        try {
            token = requestUtils.postWithParams(urlBuilder.getTokenUrl(), params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }
}
