package Utils;

import java.sql.*;

public class DBConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/union_reporting";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public void insertTest(String testName, int statusId, String methodName, int projectId,
                           int sessionId, String env, String browser){
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)){
            Statement statement = connection.createStatement();

            String insertQueryTemplate = "INSERT INTO union_reporting.test" +
                    "(name, " +
                    "status_id, " +
                    "method_name, " +
                    "project_id, " +
                    "session_id, " +
                    "start_time, " +
                    "end_time, " +
                    "env, " +
                    "browser, " +
                    "author_id)" +
                    "VALUES " +
                    "(" +
                    "'%s', " +
                    "%s, " +
                    "'%s', " +
                    "%s, " +
                    "%s, " +
                    "CURRENT_TIMESTAMP, " +
                    "null, " +
                    "'%s', " +
                    "'%s', " +
                    "null" +
                    ");";

            String insertQuery = String.format(insertQueryTemplate, testName, statusId, methodName, projectId, sessionId, env, browser);
            statement.executeUpdate(insertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertLogInTest(String content, int isException, int testId){
        String queryTemplate = "INSERT INTO log(content, is_exception, test_id) VALUES ('%s', %s, %s);";
        String insertLogQuery = String.format(queryTemplate, content, isException, testId);

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)){
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertLogQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertScreenInTest(String hexString, int testId){
        String queryTemplate = "INSERT INTO attachment(content, content_type, test_id) VALUES (0x%s, 'image/png', %s);";
        String insertLogQuery = String.format(queryTemplate, hexString, testId);

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)){
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertLogQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
