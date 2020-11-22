package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtils {
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss.S";

    public static boolean isDateSortedByDesc(List<String> dates) {
        List<Date> datesList = new ArrayList<>();

        for (String test : dates){
            Date date = null;
            try {
                date = new SimpleDateFormat(dateFormat).parse(test);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datesList.add(date);
        }

        for (int i = 0; i < datesList.size() - 1; i++){
            if (datesList.get(i).compareTo(datesList.get(i + 1)) < 0) return false;
        }

        return true;
    }
}
