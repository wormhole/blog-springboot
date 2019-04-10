package net.stackoverflow.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author 凉衫薄
 */
public class DateUtils {

    /**
     * 获取日期格式路径
     *
     * @return 返回日期格式路径
     */
    public static String getDatePath() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
        String datePath = sdf.format(date);
        return datePath;
    }

    /**
     * 格式化日期(只格式化日期，不格式化时间)
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 格式化日期(格式化日期和时间)
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
