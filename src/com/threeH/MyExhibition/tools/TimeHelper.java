package com.threeH.MyExhibition.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间转换工具类.
 *
 * @author 熊波
 */
public class TimeHelper {
    /**
     * 将毫秒为单位的时间转化为为特定格式的时间字符串
     *
     * @param milliSeconds 自1970年至现在的毫秒数
     * @return 时间字符串
     */
    public static String updateMilliSecToFormatDateStr(long milliSeconds) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm");
        return mDateFormat.format(new Date(milliSeconds));
    }

    /**
     * 将毫秒为单位的时间转化为为特定格式的时间字符串
     *
     * @param milliSeconds 自1970年至现在的毫秒数
     * @param dateFormat   时间格式字符串
     * @return 时间字符串
     */
    public static String updateMilliSecToFormatDateStr(long milliSeconds, String dateFormat) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(dateFormat);
        return mDateFormat.format(new Date(milliSeconds * 1000));
    }

    /**
     * 获取当天时间
     *
     * @return 当天时间
     */
    public static String getCurrentDate() {
        return getCurrentTime("yyyy-MM-dd");
    }

    /**
     * 获取当天时间
     *
     * @return 当天时间
     */
    public static String getCurrentMinute() {
        return getCurrentTime("MM-dd HH:mm");
    }

    /**
     * 获取当天时间
     *
     * @return 当天时间
     */
    public static String getCurrentSecond() {
        Date date = new Date();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    private static String getCurrentTime(String dateFormat) {
        Date date = new Date();
        SimpleDateFormat myFormatter = new SimpleDateFormat(dateFormat);
        return myFormatter.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return 微秒数
     */
    public static long getCurrTime() {
        return System.currentTimeMillis();
    }

    /**
     * 将指定时间按照规则转化为时间字符串
     * * <p>
     * 动态列表；
     * 动态详情；
     * 对话列表；
     * 对话详细；
     * 评论列表；
     * 通知列表；
     * 下拉刷新；	1分钟之内→刚刚
     * 1小时之内→X分钟前
     * 1天之内→今天 05:23
     * 更早→ 05-23 18:54
     * （跨年的带上年份 11-05-23 18:18）
     * </p>
     *
     * @param time 自1970年至现在的毫秒数
     * @return 指定规则的时间字符串
     */
    public static String getTimeRule1(long time) {
        Calendar c1 = Calendar.getInstance(TimeZone.getDefault());
        c1.setTimeInMillis(time);
        return compare1(c1);
    }

    /**
     * 将指定时间按照规则转化为时间字符串
     * <p>
     * 动态列表；
     * 动态详情；
     * 对话列表；
     * 对话详细；
     * 评论列表；
     * 通知列表；
     * 下拉刷新；	1分钟之内→刚刚
     * 1小时之内→X分钟前
     * 1天之内→今天 05:23
     * 更早→ 05-23 18:54
     * （跨年的带上年份 11-05-23 18:18）
     * </p>
     *
     * @param time 时间，格式为yyyy-MM-dd HH:mm:ss
     * @return 指定规则的时间字符串
     */
    public static String getTimeRule1(String time) {
        if (time == null || "".equals(time.trim())) {
            return "刚刚";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(time);
            Calendar c1 = Calendar.getInstance(TimeZone.getDefault());
            c1.setTime(date);
            return compare1(c1);
        } catch (ParseException e) {
            return "刚刚";
        }
    }

    /**
     * 将指定时间按照规则转化为时间字符串
     * <p>
     * 生的；
     * 熟的；	X分钟前；
     * X小时前；
     * X天前；
     * X年前；
     * </p>
     *
     * @param time 自1970年至现在的毫秒数
     * @return 指定规则的时间字符串
     */
    public static String getTimeRule2(long time) {
        Calendar c1 = Calendar.getInstance(TimeZone.getDefault());
        c1.setTimeInMillis(time);
        return compare2(c1);
    }

    /**
     * 将指定时间按照规则转化为时间字符串
     * <p>
     * 生的；
     * 熟的；	X分钟前；
     * X小时前；
     * X天前；
     * X年前；
     * </p>
     *
     * @param time 时间字符串，格式为yyyy-MM-dd HH:mm:ss
     * @return 指定规则的时间字符串
     */
    public static String getTimeRule2(String time) {
        if (time == null || "".equals(time.trim())) {
            return "刚刚";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            return "刚刚";
        }
        Calendar c1 = Calendar.getInstance(TimeZone.getDefault());
        c1.setTime(date);
        return compare2(c1);
    }

    /**
     * 将指定时间按照规则转化为时间字符串
     *
     * @param time 自1970年至现在的纳秒数
     * @return 聊天时间条
     */
    public static String getTimeRule3(long time) {
        Calendar c1 = Calendar.getInstance(TimeZone.getDefault());
        c1.setTimeInMillis(time / 1000);
        return compare3(c1);
    }

    private static String compare3(Calendar c1) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
        Calendar c2 = Calendar.getInstance(TimeZone.getDefault());
        //如果是去年及之前，那么按照格式1显示时间
        if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR)) {
            return sdf1.format(c1.getTime());
        }
        //如果是本年，并且是 上月及之前或者本月昨日之前，那么按照格式2显示时间
        if (c1.get(Calendar.MONTH) < c2.get(Calendar.MONTH)
                || (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) < c2
                .get(Calendar.DAY_OF_MONTH) - 1)) {
            return sdf2.format(c1.getTime());
        }
        //如果本年本月昨日，显示”昨天 小时：分钟“
        if (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) - 1) {
            return "昨天 " + c1.get(Calendar.HOUR_OF_DAY) + ":" + (c1.get(Calendar.MINUTE) < 10 ? "0" + c1.get(Calendar.MINUTE) : c1.get(Calendar.MINUTE));
        }
        //如果本年本月本日，显示”小时：分钟“
        if (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
            return c1.get(Calendar.HOUR_OF_DAY) + ":" + (c1.get(Calendar.MINUTE) < 10 ? "0" + c1.get(Calendar.MINUTE) : c1.get(Calendar.MINUTE));
        }
        //七月，按照格式1显示时间
        return sdf2.format(c1.getTime());
    }

    private static String compare1(Calendar c1) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
        Calendar c2 = Calendar.getInstance(TimeZone.getDefault());
        //如果是去年及去年以前，那么按照格式1显示时间
        if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR)) {
            return sdf1.format(c1.getTime());
        }
        //如果为今年，但非本月或这是本月，但是不是在当前日，那么按照格式2显示时间
        if (c1.get(Calendar.MONTH) < c2.get(Calendar.MONTH)
                || (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) < c2
                .get(Calendar.DAY_OF_MONTH))) {
            return sdf2.format(c1.getTime());
        }
        //如果为本年本月本日，但是在前1小时之前，显示”今天 小时：分钟“
        if (c1.get(Calendar.HOUR_OF_DAY) < c2.get(Calendar.HOUR_OF_DAY) - 1) {
            return "今天 " + c1.get(Calendar.HOUR_OF_DAY) + ":" + (c1.get(Calendar.MINUTE) < 10 ? "0" + c1.get(Calendar.MINUTE) : c1.get(Calendar.MINUTE));
        }

        //如果为本年本月本日，但是是在前后一小时，如果相差一小时以内，显示” 分钟前“；如果相差一小时以外，显示”今天 小时：分钟“
        if (c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY) - 1) {
            if (c1.get(Calendar.MINUTE) > c2.get(Calendar.MINUTE)) {
                return c2.get(Calendar.MINUTE) + (60 - c1.get(Calendar.MINUTE)) + "分钟前";
            }
            return "今天 " + c1.get(Calendar.HOUR_OF_DAY) + ":" + (c1.get(Calendar.MINUTE) < 10 ? "0" + c1.get(Calendar.MINUTE) : c1.get(Calendar.MINUTE));
        }
        //如果本年本月本日本小时，在当前时间之前，那么显示" 分钟前"
        if (c1.get(Calendar.MINUTE) < c2.get(Calendar.MINUTE) - 1) {
            return c2.get(Calendar.MINUTE) - c1.get(Calendar.MINUTE) + "分钟前";
        }
        //如果本年本月本日本小时本分钟及之后，显示”刚刚“
        return "刚刚";
    }

    private static String compare2(Calendar c1) {
        // 当前时间的Calendar实例.
        Calendar c2 = Calendar.getInstance(TimeZone.getDefault());
        // 一年前
        if (c1.get(Calendar.YEAR) < (c2.get(Calendar.YEAR) - 1)) {
            return (c2.get(Calendar.YEAR) - 1 - c1.get(Calendar.YEAR)) + "年前";
        }
        // 去年
        if (c1.get(Calendar.YEAR) == (c2.get(Calendar.YEAR) - 1)) {
            return isOut7Day(c2.get(Calendar.DAY_OF_YEAR)
                    + (365 - c1.get(Calendar.DAY_OF_YEAR)));
        }
        // 今年（昨天以前）
        if (c1.get(Calendar.DAY_OF_YEAR) < (c2.get(Calendar.DAY_OF_YEAR) - 1)) {
            return isOut7Day(c2.get(Calendar.DAY_OF_YEAR)
                    - c1.get(Calendar.DAY_OF_YEAR));
        }
        // 昨天
        if (c1.get(Calendar.DAY_OF_YEAR) == (c2.get(Calendar.DAY_OF_YEAR) - 1)) {
            if (c1.get(Calendar.HOUR_OF_DAY) > c2.get(Calendar.HOUR_OF_DAY)) {
                return c2.get(Calendar.HOUR_OF_DAY)
                        + (24 - c1.get(Calendar.HOUR_OF_DAY)) + "小时前";
            }
            return "昨天";
        }
        // 今天(1小时之前)
        if (c1.get(Calendar.HOUR_OF_DAY) < (c2.get(Calendar.HOUR_OF_DAY) - 1)) {
            return (c2.get(Calendar.HOUR_OF_DAY) - c1.get(Calendar.HOUR_OF_DAY))
                    + "小时前";
        }
        // 今天(1小时之内)
        if (c1.get(Calendar.HOUR_OF_DAY) == (c2.get(Calendar.HOUR_OF_DAY) - 1)) {
            if (c1.get(Calendar.MINUTE) > c2.get(Calendar.MINUTE)) {
                return c2.get(Calendar.MINUTE) + (60 - c1.get(Calendar.MINUTE))
                        + "分钟前";
            }
            return "1小时前";
        }
        // 1小时之内
        if (c1.get(Calendar.MINUTE) < (c2.get(Calendar.MINUTE) - 1)) {
            return c2.get(Calendar.MINUTE) - c1.get(Calendar.MINUTE) + "分钟前";
        }
        // 1分钟之内
        if (c1.get(Calendar.MINUTE) == (c2.get(Calendar.MINUTE) - 1)) {
            if (c1.get(Calendar.SECOND) > c2.get(Calendar.SECOND)) {
                return "刚刚";
            }
            return "1分钟前";
        }
        return "刚刚";
    }

    public static String isOut7Day(int day) {
        if (day < 7) {
            return day + "天前";
        } else {
            return "一周前";
        }
    }


    /**
     * 判断时间是否有效
     *
     * @param push_sound_time 时间字符串，格式为”起始小时数,结束小时数“
     * @return 是否有效
     */
    public static boolean isValidatePushTime(String push_sound_time) {
        String[] time = push_sound_time.split(",");
        try {
            int start = Integer.parseInt(time[0]);
            int end = Integer.parseInt(time[1]);
            Calendar c2 = Calendar.getInstance(TimeZone.getDefault());
            int hour = c2.get(Calendar.HOUR_OF_DAY);
            if (start == end) {
                return true;
            }
            if (start <= end) {
                if (hour >= start && hour <= end) {
                    return false;
                }
                return true;
            } else {
                if (hour >= start || hour < end) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
