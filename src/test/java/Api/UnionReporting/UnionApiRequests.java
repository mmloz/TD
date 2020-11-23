package Api.UnionReporting;

import Api.UnionReporting.Constants.UnionUrlParams;
import Utils.JsonUtils;
import Utils.RequestUtils;
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
            token = requestUtils.sendPostRequest(urlBuilder.getTokenUrl(), params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }

    public String getJsonTests(String projectId){
        List<NameValuePair> getJsonParams = new ArrayList<>();
        getJsonParams.add(new BasicNameValuePair(UnionUrlParams.PROJECT_ID, projectId));

        String getTestsResponse = null;
        do {
            try {
                getTestsResponse = requestUtils.sendPostRequest(urlBuilder.getTestsListJsonUrl(), getJsonParams);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!JsonUtils.isJson(getTestsResponse));

        return getTestsResponse;
    }
}
