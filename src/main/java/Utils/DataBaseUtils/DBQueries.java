package Utils.DataBaseUtils;

public class DBQueries {
    public static final String INSERT_TEST = "INSERT INTO union_reporting.test" +
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
            "%s, " +
            "%s, " +
            "'%s', " +
            "'%s', " +
            "%s" +
            ");";

    public static final String INSERT_LOG = "INSERT INTO log(content, is_exception, test_id) VALUES ('%s', %s, %s);";

    public static final String INSERT_ATTACHMENT = "INSERT INTO attachment(content, content_type, test_id) VALUES (0x%s, %s, %s);";

    private DBQueries(){}
}
