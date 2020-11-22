package Api.TestRail;

import Api.TestRail.Constants.ApiUrl;
import Models.Response;
import Utils.RequestUtils;

public class TestRailRequests {
    private final RequestUtils requestUtils;
    private final String testRailUrl;
    private final String testRailLogin;
    private final String testRailPassword;

    private final String urlTemplate = "%s%s%s";

    public TestRailRequests(String testRailUrl, String testRailLogin, String testRailPassword) {
        this.testRailUrl = testRailUrl;
        this.testRailLogin = testRailLogin;
        this.testRailPassword = testRailPassword;
        this.requestUtils = new RequestUtils();
    }

    public String getSuites(String projectId){
        Response getSuiteResponse = requestUtils.sendGetRequest(
                String.format(urlTemplate, testRailUrl, ApiUrl.getSuites, projectId),
                testRailLogin, testRailPassword);

        return getSuiteResponse.getBody();
    }

    public String postRun(String projectId, String runParams){
        Response postRunResponse = requestUtils.sendPostRequest(
                String.format(urlTemplate, testRailUrl, ApiUrl.addRun, projectId),
                runParams,
                testRailLogin, testRailPassword);


        return postRunResponse.getBody();
    }

    public String getTestIdInRun(String runId){
        Response getTestIdResponse = requestUtils.sendGetRequest(
                String.format(urlTemplate, testRailUrl, ApiUrl.getTests, runId),
                testRailLogin, testRailPassword);

        return getTestIdResponse.getBody();
    }

    public String addResults(String trTestId, String testResultParams){
        Response addResultResponse = requestUtils.sendPostRequest(
                String.format(urlTemplate, testRailUrl, ApiUrl.addResult, trTestId),
                testResultParams,
                testRailLogin, testRailPassword);

        return addResultResponse.getBody();
    }

    public void addScreenToResult(String resultId, String screenPath){
        requestUtils.sendMultipart(
                String.format(urlTemplate, testRailUrl, ApiUrl.addAttachmentToResult, resultId),
                screenPath,
                testRailLogin, testRailPassword);
    }


}
