package com.apps.freeroadingdriver.utils;

import com.apps.freeroadingdriver.constants.CommonMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by craterzone on 8/11/16.
 */

public class DateFormatter {

    private final static long SEC_IN_A_DAY = 24 * 60 * 60;

    public static String getCurrentDate(String formate){
        SimpleDateFormat sdf = new SimpleDateFormat(formate, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date( Calendar.getInstance().getTimeInMillis());
        return sdf.format(date);
    }

    public static String getDateFromSecs(long secs , String formate){
        SimpleDateFormat sdf = new SimpleDateFormat(formate, Locale.US);
        Date date = new Date( secs * 1000 );
        return sdf.format(date);
    }

    public static String getDateFromTimestamp(long timestamp , String formate){
        SimpleDateFormat sdf = new SimpleDateFormat(formate, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date( timestamp );
        return sdf.format(date);
    }

    public static int convertMillisToHrs(long millis){
        return (int)millis/(1000*3600);
    }

    public static long getEpochDateSec(){
        return ((Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis() /1000)/SEC_IN_A_DAY) * SEC_IN_A_DAY ;
    }

    public static int getEpochSecOfDay(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND);
    }

    public static String secToTime(int sec) {
        int minute = sec / 60;
        if (minute >= 60) {
            int hour = minute / 60;
            minute %= 60;
         }
        String currentMin= CommonMethods.getCurrentTime().split(":")[1];
        String currentHrs=CommonMethods.getCurrentTime().split(":")[0];
        minute= Integer.parseInt(currentMin)+minute;
        if (minute >= 60) {
            currentHrs=""+(Integer.parseInt(currentHrs)+ (minute / 60));
            minute %= 60;
         }
        return  currentHrs+":"+(minute<10 ? "0"+minute:minute);
    }

}
