package Utils;

import Api.TestRail.Constants.RequestParams;
import Api.UnionReporting.Constants.UnionJsonKeys;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static boolean isJson(String source){
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(source);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<String> getTestName(String source){
        ObjectMapper mapper = new ObjectMapper();

        JsonNode arrayOfTests = null;
        try {
            arrayOfTests = mapper.readTree(source);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> tests = new ArrayList<>();

        for (int i = 0; i < arrayOfTests.size(); i++){
            tests.add(arrayOfTests.get(i).get(UnionJsonKeys.NAME).asText());
        }

        return tests;
    }

    public static String getId(String source){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode object = null;
        try {
            object = mapper.readTree(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object.get(RequestParams.id).toString();
    }

    public static String getId(String source, int pos){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode object = null;
        try {
            object = mapper.readTree(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object.get(pos).get(RequestParams.id).toString();
    }
}
