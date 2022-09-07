package com.poho.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by wupeng on 2017/3/21.
 */
public class TeachEvaUtil {
    private final static Logger logger = LoggerFactory.getLogger(TeachEvaUtil.class);



    /**
     * 根据导入的学生状态文字转换为具体存储的数字
     *
     * @param status
     * @return
     */
    public static Integer convertStudentStatus(String status) {
        int sta = 1;
        if ("学籍变更".equals(status)) {
            sta = 2;
        }
        if ("毕业".equals(status)) {
            sta = 0;
        }
        return sta;
    }
    /**
     * 将身份证号转化为生日
     * @param idCard
     * @return
     */
    public static String convertBirthdayFromIdcard(String idCard) {
        String birthday = "";
        if (!MicrovanUtil.isEmpty(idCard) && idCard.length() == 18) {
            birthday = idCard.substring(6, 14);
        }
        return birthday;
    }

    /**
     * 下载Excel
     * @param response
     * @param in
     * @param out
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    public static void downLoadExcel(HttpServletResponse response, BufferedInputStream in, BufferedOutputStream out, String filePath, String fileName) throws IOException {
        try {
            File f = new File(filePath);
            response.setContentType("application/x-excel");
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

    /**
     * 转换参与排考标志
     * @param join
     * @return
     */
    public static Integer convertJoinExam(String join) {
        Integer joinExam = 1;
        if ("否".equals(join)) {
            joinExam = 2;
        }
        return joinExam;
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


}
