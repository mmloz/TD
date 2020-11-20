package Api.TestRail;

import Api.TestRail.Constants.RequestParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class TestRailUtils {
    public static String createRun(String runName, String suiteId, String trCaseId){
        Map run = new HashMap();
        run.put(RequestParams.name, runName);
        run.put(RequestParams.suiteId, suiteId);
        run.put(RequestParams.includeAll, false);
        run.put(RequestParams.caseIds, new String[]{String.valueOf(trCaseId)});

        try {
            return new ObjectMapper().writeValueAsString(run);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createTestResult(String caseStatus){
        Map testResult = new HashMap();
        testResult.put(RequestParams.statusId, caseStatus);
        try {
            return new ObjectMapper().writeValueAsString(testResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
