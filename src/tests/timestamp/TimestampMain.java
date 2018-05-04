package tests.timestamp;

import java.util.Calendar;
import java.util.Date;

public class TimestampMain {
    public static void main(String[] args) {
        Date date = new Date();
        date.setTime(date.getTime() + 1524520563*1000);
        System.out.println(date);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 1524520563);
        cal.set(Calendar.MILLISECOND, 0);
        System.out.println(cal.getTime());
    }
}
