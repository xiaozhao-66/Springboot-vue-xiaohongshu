/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.yanhuo.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理
 * 
 * @author Mark sunlightcs@gmail.com
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @return  返回yyyy-MM-dd格式日期
     */
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 日期解析
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回Date
     */
    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换成日期
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)){
            return null;
        }

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     * 根据周数，获取开始日期、结束日期
     * @param week  周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return  返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date 日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date 日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date 日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date 日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date 日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date 日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    /**
     * 发布了多久的时间描述：几分钟前，几小时前，几天前，几个月前，几年前
     * @param date 日期
     * @return 转化后的日期
     */
    public static String timeUtile(Date date) {
        // 拿到当前时间戳和发布时的时间戳，然后得出时间戳差
        Date curTime = new Date();
        long timeDiff = curTime.getTime() - date.getTime();
        //上面一行代码可以换成以下（兼容性的解决）

        // 单位换算
        long sec = 1000;
        long min = 60 * sec;
        long hour = min * 60;
        long day = hour * 24;
        long week = day * 7;
        long month = week * 4;
        long year = month * 12;
        DecimalFormat df = new DecimalFormat("#");
        // 计算发布时间距离当前时间的周、天、时、分
//        double exceedyear = Math.floor(timeDiff / year);
//        double exceedmonth = Math.floor(timeDiff / month);
//        double exceedWeek = Math.floor(timeDiff / week);
        double exceedDay = Math.floor(timeDiff / day);
        double exceedHour = Math.floor(timeDiff / hour);
        double exceedMin = Math.floor(timeDiff / min);
        double exceedSec = Math.floor(timeDiff / sec);

        if(exceedDay>=7){
            return format(date,DATE_PATTERN);
        }
        else if(exceedDay < 7 && exceedDay > 0){
            return df.format(exceedDay) + "天前";
        }
        else if (exceedHour < 24 && exceedHour > 0) {
            return df.format(exceedHour) + "小时前";
        } else {
            if(exceedMin<60 && exceedMin>0){
                return df.format(exceedMin) + "分钟前";
            }else {
                return "刚刚";
            }

        }
        // 最后判断时间差到底是属于哪个区间，然后return
//        if (exceedyear < 100 && exceedyear > 0) {
//            return df.format(exceedyear) + "年前";
//        } else {
//            if (exceedmonth < 12 && exceedmonth > 0) {
//                return df.format(exceedmonth) + "月前";
//            } else {
//                if (exceedWeek < 4 && exceedWeek > 0) {
//                    return df.format(exceedWeek) + "星期前";
//                } else {
//                    if (exceedDay < 7 && exceedDay > 0) {
//                        return df.format(exceedDay) + "天前";
//                    } else {
//                        if (exceedHour < 24 && exceedHour > 0) {
//                            return df.format(exceedHour) + "小时前";
//                        } else {

//                            return df.format(exceedMin) + "分钟前";
//                        }
//                    }
//                }
//            }
//   }
    }
}
