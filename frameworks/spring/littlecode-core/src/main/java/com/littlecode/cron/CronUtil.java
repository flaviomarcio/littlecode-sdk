package com.littlecode.cron;


import com.littlecode.parsers.PrimitiveUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CronUtil {

    public static final String CRON_FORMAT = "%s %s %s %s %s %s";//"0 0 * * * *"

    private String expression;
    private LocalDateTime temporal;


//Parse the given crontab expression string into a CronExpression. The string has six single space-separated time and date fields:
//┌───────────── second (0-59)
//│ ┌───────────── minute (0 - 59)
//│ │ ┌───────────── hour (0 - 23)
//│ │ │ ┌───────────── day of the month (1 - 31)
//│ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
//│ │ │ │ │ ┌───────────── day of the week (0 - 7)
//│ │ │ │ │ │          (0 or 7 is Sunday, or MON-SUN)
//│ │ │ │ │ │
//* * * * * *

    //Example expressions:
//
//"0 0 * * * *" = the top of every hour of every day.
//"*/10 * * * * *" = every ten seconds.
//"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
//"0 0 6,19 * * *" = 6:00 AM and 7:00 PM every day.
//"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
//"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
//"0 0 0 25 12 ?" = every Christmas Day at midnight
//"0 0 0 L * *" = last day of the month at midnight
//"0 0 0 L-3 * *" = third-to-last day of the month at midnight
//"0 0 0 1W * *" = first weekday of the month at midnight
//"0 0 0 LW * *" = last weekday of the month at midnight
//"0 0 0 * * 5L" = last Friday of the month at midnight
//"0 0 0 * * THUL" = last Thursday of the month at midnight
//"0 0 0 ? * 5#2" = the second Friday in the month at midnight
//"0 0 0 ? * MON#1" = the first Monday in the month at midnight

    private static String check(String val) {
        return (val != null && val.trim().isEmpty()) ? "*" : val;
    }

    public static String ofCron(String seconds, String minutes, String hours, String days, String months, String dayOfWeek) {
        var expression = String.format(CronUtil.CRON_FORMAT,
                check(seconds),
                check(minutes),
                check(hours),
                check(days),
                check(months),
                check(dayOfWeek)
        );
        if (CronExpression.isValidExpression(expression)) {
            var cronExpression = CronExpression.parse(expression);
            return cronExpression.toString();
        }
        return "";
    }

    public static String ofCron(String seconds, String minutes, String hours, String days) {
        return CronUtil.ofCron(seconds, minutes, hours, days, "", "");
    }

    public static String ofCron(String seconds, String minutes, String hours) {
        return CronUtil.ofCron(seconds, minutes, hours, "", "", "");
    }

    public static String ofCron(String seconds, String minutes) {
        return CronUtil.ofCron(seconds, minutes, "", "", "", "");
    }

    public static String ofCron(String seconds) {
        return CronUtil.ofCron(seconds, "", "", "", "", "");
    }

    public static String ofCron(int seconds, int minutes, int hours, int days, int months, int dayOfWeek) {
        seconds = Math.max(seconds, 0);
        seconds = Math.min(seconds, 59);

        minutes = Math.max(minutes, 0);
        minutes = Math.min(minutes, 59);

        hours = Math.max(hours, 0);
        hours = Math.min(hours, 23);

        days = Math.max(days, 1);
        days = Math.min(days, 31);

        months = Math.max(months, 1);
        months = Math.min(months, 12);

        dayOfWeek = Math.max(dayOfWeek, 0);
        dayOfWeek = Math.min(dayOfWeek, 7);

        return ofCron(
                PrimitiveUtil.toString(seconds),
                PrimitiveUtil.toString(minutes),
                PrimitiveUtil.toString(hours),
                PrimitiveUtil.toString(days),
                PrimitiveUtil.toString(months),
                PrimitiveUtil.toString(dayOfWeek)
        );
    }

    public static String ofCron(int seconds, int minutes, int hours, int days) {
        return CronUtil.ofCron(seconds, minutes, hours, days, 0, 0);
    }

    public static String ofCron(int seconds, int minutes, int hours) {
        return CronUtil.ofCron(seconds, minutes, hours, 0, 0, 0);
    }

    public static String ofCron(int seconds, int minutes) {
        return CronUtil.ofCron(seconds, minutes, 0, 0, 0, 0);
    }

    public static String ofCron(int seconds) {
        return CronUtil.ofCron(seconds, 0, 0, 0, 0, 0);
    }

    public static String everySecond(int second) {
        return CronUtil.ofCron(second, 0, 0, 0, 0, 0);
    }

    public static String everySecond() {
        return CronUtil.ofCron(1, 0, 0, 0, 0, 0);
    }

    public static String everyMinute(int minutes) {
        return CronUtil.ofCron(0, minutes, 0, 0, 0, 0);
    }

    public static String everyMinute() {
        return CronUtil.ofCron(0, 1, 0, 0, 0, 0);
    }

    public static String everyDay(int days) {
        return CronUtil.ofCron(0, 0, days, 0, 0, 0);
    }

    public static String everyDay() {
        return CronUtil.ofCron(0, 0, 1, 0, 0, 0);
    }

    public LocalDateTime next() {
        if (CronExpression.isValidExpression(this.expression) && this.temporal != null) {
            var cron = CronExpression.parse(this.expression);
            var temporal = cron.next(this.temporal);
            if (temporal != null)
                return LocalDateTime.of(temporal.toLocalDate(), temporal.toLocalTime());
        }
        return null;
    }
}
