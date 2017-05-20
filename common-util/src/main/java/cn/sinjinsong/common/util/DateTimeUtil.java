package cn.sinjinsong.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SinjinSong on 2017/4/23.
 */
public class DateTimeUtil {
    private static Map<String, Integer> months = new HashMap<>();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static {
        months.put("January", 1);
        months.put("February", 2);
        months.put("March", 3);
        months.put("April", 4);
        months.put("May", 5);
        months.put("June", 6);
        months.put("July", 7);
        months.put("August", 8);
        months.put("September", 9);
        months.put("October", 10);
        months.put("November", 11);
        months.put("December", 12);
    }
    
    public static LocalDateTime toLocalDateTime(Long date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault());
    }

    public static LocalDate toLocalDate(Long date) {
        return Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(String date) {
        StringBuilder sb = new StringBuilder();
        String[] slices = date.split(" ");
        sb.append(slices[2]);
        sb.append("-");
        sb.append(months.get(slices[1]));
        sb.append("-");
        sb.append(slices[0]);
        try {
            return toLocalDateTime(sdf.parse(sb.toString()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public static long toLong(LocalDateTime localDateTime){
        return Timestamp.valueOf(localDateTime).getTime();
    }

}
