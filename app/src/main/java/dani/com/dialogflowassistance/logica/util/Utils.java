package dani.com.dialogflowassistance.logica.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatDateTime(Date time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(time);
    }
}
