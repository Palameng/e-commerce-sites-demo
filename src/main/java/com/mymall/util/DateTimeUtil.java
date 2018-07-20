package com.mymall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {


    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static Date strToDate(String dateTimeStr, String formatStr){
        //制定格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        //转换
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);

        return dateTime.toDate();

    }

    public static String dateToStr(Date date, String formatStr){
        if (date == null){
            return StringUtils.EMPTY;
        }

        DateTime dateTime = new DateTime(date);

        return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dateTimeStr){
        //制定格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        //转换
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);

        return dateTime.toDate();

    }

    public static String dateToStr(Date date){
        if (date == null){
            return StringUtils.EMPTY;
        }

        DateTime dateTime = new DateTime(date);

        return dateTime.toString(STANDARD_FORMAT);
    }


//    public static void main(String[] args) {
//        String formatStr = "yyyy-MM-dd HH:mm:ss";
//        System.out.println(DateTimeUtil.dateToStr(new Date(), formatStr));
//        System.out.println(DateTimeUtil.strToDate("2018-07-20 11:11:11", formatStr));
//    }


}
