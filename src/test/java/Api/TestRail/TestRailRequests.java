package Api.TestRail;

import Api.TestRail.Constants.ApiUrl;
import Constants.ResValues;
import Utils.RequestUtils;

import java.util.Map;

public class TestRailRequests {
    private final String testRailUrl;
    private final String testRailLogin;
    private final String testRailPassword;

    public TestRailRequests(String testRailUrl, String testRailLogin, String testRailPassword) {
        this.testRailUrl = testRailUrl;
        this.testRailLogin = testRailLogin;
        this.testRailPassword = testRailPassword;
    }

    public String getSuites(String projectId){
        Map<ResValues, String> getSuiteResponse = RequestUtils.sendGetRequest(
                String.format("%s%s%s", testRailUrl, ApiUrl.getSuites, projectId),
                testRailLogin, testRailPassword);

        return getSuiteResponse.get(ResValues.BODY);
    }

    public String postRun(String projectId, String runParams){
        Map<ResValues, String> postRunResponse = RequestUtils.sendPostRequest(
                String.format("%s%s%s", testRailUrl, ApiUrl.addRun, projectId),
                runParams,
                testRailLogin, testRailPassword);


        return postRunResponse.get(ResValues.BODY);
    }

    public String getTestIdInRun(String runId){
        Map<ResValues, String> getTestIdResponse = RequestUtils.sendGetRequest(
                String.format("%s%s%s", testRailUrl, ApiUrl.getTests, runId),
                testRailLogin, testRailPassword);

        return getTestIdResponse.get(ResValues.BODY);
    }

    public String addResults(String trTestId, String testResultParams){
        Map<ResValues, String> addResultResponse = RequestUtils.sendPostRequest(
                String.format("%s%s%s", testRailUrl, ApiUrl.addResult, trTestId),
                testResultParams,
                testRailLogin, testRailPassword);

        return addResultResponse.get(ResValues.BODY);
    }

    public void addScreenToResult(String resultId, String screenPath){
        RequestUtils.sendMultipart(
                String.format("%s%s%s", testRailUrl, ApiUrl.addAttachmentToResult, resultId),
                screenPath,
                testRailLogin, testRailPassword);
    }


}
