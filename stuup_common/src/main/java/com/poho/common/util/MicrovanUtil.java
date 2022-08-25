package com.poho.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wupeng on 2017/3/3.
 */
public class MicrovanUtil {
    /**
     *
     * @param request
     * @param key
     * @return
     */
    public static Object getSessionAttribute(HttpServletRequest request, String key) {
        Object object = null;
        try {
            object = request.getSession(false).getAttribute(key);
        } catch (Exception e) {
        }
        return object;
    }

    /**
     *
     * @param response
     * @param result
     */
    public static void response(HttpServletResponse response, String result) {
        try {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前请求BaseBath
     * @param request
     * @return
     */
    public static String obtainBasePath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
    }

    /**
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象是否非空
     *
     * @param obj 对象
     * @return {@code true}: 非空<br>{@code false}: 空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 将集合转为String数组
     * @param list
     * @param <T>
     * @return
     */
    public static <T> String[] toArray(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }

        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = String.valueOf(list.get(i));
        }

        return result;
    }

    /**
     * list去重复
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> removeRepeat(List<T> list) {
        if (isEmpty(list)) {
            return list;
        }

        List<T> result = new ArrayList<T>();
        for (T e : list) {
            if (!result.contains(e)) {
                result.add(e);
            }
        }

        return result;
    }

    /**
     * 将字符串转化为日期格式
     * @param format
     * @param dateString
     * @return
     */
    public static Date formatDate(String format, String dateString) {
        try {
            if (!"".equals(dateString)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.parse(dateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将日期转换为字符串
     * @param format
     * @param date
     * @return
     */
    public static String formatDateToStr(String format, Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 为指定日期增加指定的年数
     * @param date
     * @param year
     * @return
     */
    public static Date addSomeYear(Date date, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
     * 为指定日期增加或减少指定的月数
     * @param date
     * @param month
     * @return
     */
    public static Date addSomeMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 为指定日期增加或减少指定的天数
     * @param date
     * @param day
     * @return
     */
    public static Date addSomeDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * 在时间加上指定的分钟数
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date addSomeMinute(Date date, int minute) {
        return new Date(date.getTime() + minute * 60 * 1000);
    }

    /**
     * 获取当前年份
     * @return
     */
    public static int currentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 判断是否是正整数
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (!"".equals(str) && str != null) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (isNum.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据毫秒数转换为 *天*时*分*秒的格式
     *
     * @param millisecond
     * @return
     */
    public static String formatMillisecond(long millisecond) {
        // 计算出相差天数
        double days = Math.floor(millisecond / (24 * 3600 * 1000));

        // 计算出小时数，计算天数后剩余的毫秒数
        long leave1 = millisecond % (24 * 3600 * 1000);
        double hours = Math.floor(leave1 / (3600 * 1000));

        // 计算相差分钟数，计算小时数后剩余的毫秒数
        long leave2 = leave1 % (3600 * 1000);
        double minutes = Math.floor(leave2 / (60 * 1000));

        // 计算相差秒数，计算分钟数后剩余的毫秒数
        long leave3 = leave2 % (60 * 1000);
        double seconds = Math.round(leave3 / 1000);

        StringBuffer sBuffer = new StringBuffer("");
        if (days > 0) {
            sBuffer.append(MicrovanUtil.subZeroAndDot(String.valueOf(days))).append("天");
        }
        if (hours > 0) {
            sBuffer.append(MicrovanUtil.subZeroAndDot(String.valueOf(hours))).append("小时");
        }
        if (minutes > 0) {
            sBuffer.append(MicrovanUtil.subZeroAndDot(String.valueOf(minutes))).append("分钟");
        }
        if (seconds > 0) {
            sBuffer.append(MicrovanUtil.subZeroAndDot(String.valueOf(seconds))).append("秒");
        }
        if ("".equals(sBuffer.toString())) {
            return "小于1秒";
        }
        return sBuffer.toString();
    }

    /**
     * 友好显示时间
     * @param time 要格式化的时间戳
     * @return 格式化后的时间戳
     */
    public static String friendlyShowTime(long time) {
        long nowTime = System.currentTimeMillis();
        String txt = "";
        // 时间差（毫秒）
        long t = nowTime - time;
        if (t <= 0) {
            txt = "刚刚";
        } else if (t < 60 * 1000) {
            // 一分钟内
            txt = t / 1000 + "秒前";
        } else if (t < 60 * 60 * 1000) {
            // 一小时内
            String s = subZeroAndDot(String.valueOf(Math.floor(t / (60 * 1000))));
            txt = s + "分钟前";
        } else if (t < (long)60 * 60 * 24 * 1000) {
            // 一天内
            String s = subZeroAndDot(String.valueOf(Math.floor(t / (60 * 60 * 1000))));
            txt = s + "小时前";
        } else if (t < (long)60 * 60 * 24 * 3 * 1000) {
            // 昨天和前天
            String date = formatDateToStr("HH:mm", new Date(time));
            txt = Math.floor(time / ((long)60 * 60 * 24 * 1000)) == 1 ? "昨天" + date : "前天 " + date;
        } else if (t < (long)60 * 60 * 24 * 30 * 1000) {
            // 一个月内
            txt = formatDateToStr("MM月dd日 HH:mm", new Date(time));
        } else if (t < (long)60 * 60 * 24 * 365 * 1000) {
            // 一年内
            txt = formatDateToStr("MM月dd日", new Date(time));
        } else {
            // 一年以前
            txt = formatDateToStr("yyyy年MM月dd日", new Date(time));
        }
        return txt;
    }

    /**
     * 删除文件功能
     *
     * @param f 文件路径
     */
    public static void deleteFile(String f) {
        File file = new File(f);
        // 是否存在
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 上传文件判断大小
     * @param distFilePath
     * @return
     */
    public static long getFileSize(String distFilePath) {
        File distFile = new File(distFilePath);
        if (distFile.isFile()) {
            return distFile.length();
        } else if (distFile.isDirectory()) {
            return FileUtils.sizeOfDirectory(distFile);
        }
        return -1L;
    }

    /**
     * @param message
     * @return
     */
    public static String sendHtmlWriter(HttpServletResponse response, String message) {
        try {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out;
            out = response.getWriter();
            out.print(message);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param message
     * @return
     */
    public static String sendJsonWriter(HttpServletResponse response, String message) {
        try {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out;
            out = response.getWriter();
            out.print(message);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public static String returnSixDecimal(int a, int b) {
        float num = (float) a / b;
        DecimalFormat df = new DecimalFormat("0.000000");
        df.setGroupingUsed(false);
        String str = df.format(num);
        return str;
    }

    /**
     * 调用Http接口返回Json结果
     *
     * @param httpUrl
     * @return
     */
    public static JSONObject returnHttpJson(String httpUrl) {
        StringBuilder json = new StringBuilder();
        try {
            URL reqURL = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) reqURL.openConnection();

            // 取得该连接的输入流，以读取响应内容
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = bufferedReader.readLine()) != null) {
                json.append(inputLine);
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSON.parseObject(json.toString());
        return jsonObject;
    }

    /**
     * 获取去掉横线的UUID
     *
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("\\-", "");
    }

    /**
     * 根据经纬度，获取两点间的距离(单位：米)
     *
     * @param aLng
     * @param aLat
     * @param bLng
     * @param bLat
     * @return
     */
    public static double distanceByTwoPoint(double aLng, double aLat, double bLng, double bLat) {
        double radALat = aLat * Math.PI / 180;
        double radBLat = bLat * Math.PI / 180;
        double a = radALat - radBLat;
        double b = aLng * Math.PI / 180 - bLng * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radALat) * Math.cos(radBLat) * Math.pow(Math.sin(b / 2), 2)));
        // 取WGS84标准参考椭球中的地球长半径(单位:m)
        distance = distance * 6378137.0;
        // distance = Math.round(distance * 10000) / 10000;
        return distance;
    }

    /**
     * 保留一位小数
     *
     * @param num
     * @return
     */
    public static double convertOneDecimal(double num) {
        DecimalFormat df = new DecimalFormat(".#");
        df.setGroupingUsed(false);
        String numStr = df.format(num);
        return Double.valueOf(numStr).doubleValue();
    }

    /**
     * 保留两位小数
     *
     * @param n
     * @return
     */
    public static double convertDouble(double n) {
        BigDecimal b = new BigDecimal(n);
        double r = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return r;
    }

    /**
     * 将米转换为公里保留一位小数
     *
     * @param distance
     * @return
     */
    public static double covertDistanceKm(double distance) {
        DecimalFormat df = new DecimalFormat(".#");
        df.setGroupingUsed(false);
        String disStr = df.format(distance / 1000);
        return Double.valueOf(disStr).doubleValue();
    }

    /**
     * 根据距离返回两位小数千米或米的字符串
     *
     * @param distance
     * @return
     */
    public static String convertDistanceShow(double distance) {
        DecimalFormat df = new DecimalFormat(".##");
        df.setGroupingUsed(false);
        String str = df.format(distance) + "米";
        if (distance > 1000) {
            str = df.format(distance / 1000) + "千米";
        }
        return str;
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        // "***.***.***.***".length() = 15
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 生成订单编号
     *
     * @return
     */
    public static String getOrderIdByUUID(int machineId) {
        // 最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        // 负数处理
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        // 0代表前面填充0
        // d代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    /**
     * 获取指定位数的随机数（首位不为0）
     *
     * @param length
     * @return
     */
    public static String getRandomStr(int length) {
        char[] chars1 = "123456789".toCharArray();
        char[] chars2 = "1234567890".toCharArray();
        Random random = new Random();
        StringBuilder builder = new StringBuilder(20);
        for (int i = 0; i < 6; i++) {
            if (i == 0) {
                builder.append(chars1[random.nextInt(chars1.length)]);
            } else {
                builder.append(chars2[random.nextInt(chars2.length)]);
            }
        }
        return builder.toString();
    }


    /**
     * 将大于10000的数格式化为1.*万形式
     *
     * @param num
     * @return
     */
    public static String formatNum(int num) {
        String numStr = "";
        if (num > 10000) {
            DecimalFormat df = new DecimalFormat("0.0");
            df.setGroupingUsed(false);
            numStr = df.format((double) num / 10000);
            if (numStr.indexOf(".") > 0) {
                numStr = numStr.replaceAll("0+?$", "");// 去掉多余的0
                numStr = numStr.replaceAll("[.]$", "");// 如最后一位是.则去掉
            }
            numStr = numStr + "万";
        } else {
            numStr = num + "";
        }
        return numStr;
    }

    /**
     * 剔除html中超链接
     *
     * @param html
     * @return
     */
    public static String removeLink(String html) {
        // "</?(A|a)(\n|.)*?>"
        // "</?(A|a)[^>]*>
        Pattern p = Pattern.compile("</?(A|a)[^>]*>");
        Matcher m = p.matcher(html);
        html = m.replaceAll("");
        StringBuffer h = new StringBuffer(html);
        h.append("<style type=\"text/css\">img{max-width:auto;}</style>");
        return h.toString();
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public static String convertPer(int a, int b) {
        if (b == 0) {
            return "100%";
        }
        double percent = (double) a / b;
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setGroupingUsed(false);
        nt.setMinimumFractionDigits(0);
        return nt.format(percent);
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public static String convertMulTwoPoint(int a, double b) {
        if (a == 0 || b == 0) {
            return "0";
        } else {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
            numberFormat.setMaximumFractionDigits(2);
            return numberFormat.format(a * b);
        }
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public static String convertDivideTwoPoint(int a, int b) {
        if (a == 0 || b == 0) {
            return "0";
        } else {
            double percent = (double) a / b;
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
            numberFormat.setMaximumFractionDigits(2);
            return numberFormat.format(percent);
        }
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public static double convertDivideTwoPoint(double a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        } else {
            double percent = a / b;
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
            numberFormat.setMaximumFractionDigits(2);
            return Double.parseDouble(numberFormat.format(percent));
        }
    }

    /**
     * 把手机号中间四位替换为星号
     *
     * @param phone
     * @return
     */
    public static String replacePhoneStar(String phone) {
        if (!MicrovanUtil.isEmpty(phone) && phone.length() == 11) {
            phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return phone;
    }

    /**
     * 比较两个时间的大小
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean compareTime(String a, String b) {
        int aInt = Integer.valueOf(a.replace(":", "")).intValue();
        int bInt = Integer.valueOf(b.replace(":", "")).intValue();
        if (aInt >= bInt) {
            return true;
        }
        return false;
    }

    /**
     * 获取两个日期之间的日期
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 日期集合
     */
    public static List<Date> getBetweenDates(Date start, Date end) {
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        result.add(start);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    /**
     * 获取某个日期的每分钟时间
     *
     * @param date
     * @return
     */
    public static List<Date> getDayMinute(Date date) {
        Calendar tt = Calendar.getInstance();
        tt.setTime(date);
        Calendar t2 = Calendar.getInstance();
        t2.setTime(date);
        t2.add(Calendar.DAY_OF_MONTH, 1);
        List<Date> dateList = new ArrayList<>();
        for (; tt.compareTo(t2) < 0; tt.add(Calendar.MINUTE, 1)) {
            dateList.add(tt.getTime());
        }
        return dateList;
    }

    /**
     * 获取两个时间指定间隔内的时间
     *
     * @param d1
     * @param d2
     * @param n
     * @return
     */
    public static List<Date> getMinute(Date d1, Date d2, int n) {
        Calendar tt = Calendar.getInstance();
        tt.setTime(d1);
        Calendar t2 = Calendar.getInstance();
        t2.setTime(d2);
        List<Date> dateList = new ArrayList<Date>();
        for (; tt.compareTo(t2) <= 0; tt.add(Calendar.MINUTE, n)) {
            dateList.add(tt.getTime());
        }
        return dateList;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 判断对象的所有属性是否都为空
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static boolean checkObjFieldAllNull(Object obj) {
        boolean isAllNull = true;
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.get(obj) != null) {
                    isAllNull = false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return isAllNull;
    }

    /**
     * 将ISO8859-1编码转换为UTF8
     * @param str
     * @return
     */
    public static String convertUTF8Str(String str) {
        String string = "";
        if (!MicrovanUtil.isEmpty(str)) {
            try {
                string = new String(str.getBytes("ISO8859-1"),"UTF-8");
            }
            catch (UnsupportedEncodingException e) {

            }
        }
        return string;
    }

    /**
     * 格式化星期
     * @param week
     * @return
     */
    public static Object formatWeekStr(int week) {
        String weekStr = "";
        switch (week) {
            case 1:
                weekStr = "周日";
                break;
            case 2:
                weekStr = "周一";
                break;
            case 3:
                weekStr = "周二";
                break;
            case 4:
                weekStr = "周三";
                break;
            case 5:
                weekStr = "周四";
                break;
            case 6:
                weekStr = "周五";
                break;
            case 7:
                weekStr = "周六";
                break;
            default:
                weekStr = "未知";
                break;
        }
        return weekStr;
    }

    /**
     *
     * @param callback
     * @param json
     * @return
     */
    public static String packJsonp(String callback, String json) {
        if (json == null) {
            json = "";
        }
        if (callback == null || callback.isEmpty()) {
            return json;
        }
        return callback + "&&" + callback + '(' + json + ')';
    }

    /**
     * 拼接url
     * @param url
     * @param name
     * @param val
     * @return
     */
    public static StringBuffer appendUrl(StringBuffer url, String name, Object val) {
        if (url.toString().contains("?")) {
            url.append("&").append(name).append("=").append(val);
        }
        else {
            url.append("?").append(name).append("=").append(val);
        }
        return url;
    }

    /**
     * JAVA获得0-9范围的随机数
     *
     * @param length 随机数长度
     * @return String
     */
    public static String getRandomNumChar(int length) {
        char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(10)]);
        }
        return buffer.toString();
    }

    /**
     * 下载文件
     * @param response
     * @param in
     * @param out
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    public static void downLoadFile(HttpServletResponse response, BufferedInputStream in, BufferedOutputStream out, String filePath, String fileName) throws IOException {
        try {
            File f = new File(filePath);
            response.setContentType("application/*");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Length", String.valueOf(f.length()));
            in = new BufferedInputStream(new FileInputStream(f));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    /**
     *
     * @return
     */
    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 创建目录
     * @param path
     */
    public static void createFolder(String path) {
        File folder = new File(path);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
    }

    /**
     * 将日期格式化为只有年月日的日期
     * @param date
     * @return
     */
    public static Date formatDateYMD(Date date) {
        String nowStr = MicrovanUtil.formatDateToStr("yyyy-MM-dd", date);
        return MicrovanUtil.formatDate("yyyy-MM-dd", nowStr);
    }

    /**
     * 计算两个日期相差天数
     * @param before
     * @param after
     * @return
     */
    public static int dayOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (int) ((afterTime - beforeTime) / (1000 * 60 * 60 * 24));
    }
}
