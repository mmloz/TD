package Utils.DataBaseUtils;

import java.sql.*;

public class DBConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/union_reporting";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public void insertTest(String testName, int statusId, String methodName, int projectId, int sessionId, String env, String browser){
        String insertTestQuery = String.format(DBQueries.INSERT_TEST, testName, statusId, methodName, projectId, null, "CURRENT_TIMESTAMP", sessionId, env, browser, null);
        executeUpdate(insertTestQuery);
    }

    public void insertLogInTest(String content, int testId){
        String insertLogQuery = String.format(DBQueries.INSERT_LOG, content, 0, testId);
        executeUpdate(insertLogQuery);
    }

    public void insertScreenInTest(String hexString, String attachmentType, int testId){
        String insertLogQuery = String.format(DBQueries.INSERT_ATTACHMENT, hexString, attachmentType, testId);
        executeUpdate(insertLogQuery);
    }

    private void executeUpdate(String query){
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)){
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
