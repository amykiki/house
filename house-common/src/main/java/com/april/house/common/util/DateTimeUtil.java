package com.april.house.common.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String dateToString(Date date, String formatStr) {
        if (date == null) {
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date).toString(STANDARD_FORMAT);
    }

    public static Date strToDate(String dateTimeStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        return dateTimeFormatter.parseDateTime(dateTimeStr).toDate();
    }

    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        return dateTimeFormatter.parseDateTime(dateTimeStr).toDate();
    }
}
