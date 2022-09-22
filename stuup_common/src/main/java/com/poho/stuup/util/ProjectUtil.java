package com.poho.stuup.util;

import com.poho.common.constant.CommonConstants;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.CusTransfer;
import com.poho.stuup.custom.CusUser;
import com.poho.stuup.model.User;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 19:08 2019-06-09
 * @Modified By:
 */
public class ProjectUtil {
    /**
     * request中获取当前登录用户信息
     * @param request
     * @return
     */
    public static String obtainLoginUser(HttpServletRequest request) {
        Claims claimsUser = (Claims) request.getAttribute(CommonConstants.CLAIMS_USER);
        return claimsUser.getId();
    }

    /**
     *
     * @param request
     * @return
     */
    public static CusUser getSessionUser(HttpServletRequest request) {
        Object object = MicrovanUtil.getSessionAttribute(request, ProjectConstants.SESSION_USER);
        if (MicrovanUtil.isNotEmpty(object)) {
            return (CusUser) object;
        }
        return null;
    }

    /**
     * 下载Excel
     *
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
     *
     * @param roleIds
     * @return
     */
    public static String splitListUseComma(List roleIds) {
        if (MicrovanUtil.isNotEmpty(roleIds)) {
            Object[] ids = roleIds.toArray();
            return StringUtils.join(ids, ",");
        }
        return "";
    }

    /**
     *
     * @param user
     * @return
     */
    public static CusUser convertCusUser(User user) {
        CusUser cusUser = new CusUser();
        cusUser.setUserId(user.getOid());
        cusUser.setUserName(user.getUserName());
        cusUser.setMobile(user.getMobile());
        cusUser.setIdCard(user.getIdCard());
        cusUser.setBirthday(user.getBirthday());
        cusUser.setSex(user.getSex());
        cusUser.setDeptId(user.getDeptId());
        cusUser.setDeptName(user.getDeptName());
        cusUser.setUserType(user.getUserType());
        cusUser.setDegree(user.getDegree());
        return cusUser;
    }

    /**
     *
     * @param month
     * @return
     */
    public static String convertCHNYM(String month) {
        if (MicrovanUtil.isNotEmpty(month)) {
            StringBuffer str = new StringBuffer();
            str.append(month.replace("-", "年"));
            str.append("月");
            return str.toString();
        }
        return "";
    }

    /**
     *
     * @param userType
     * @return
     */
    public static String convertUserType(Integer userType) {
        String ut = "";
        switch (userType.intValue()) {
            case 1:
                ut = "在职在编";
                break;
            case 2:
                ut = "编外运行";
                break;
            case 3:
                ut = "行政外聘";
                break;
            default:
                ut = "";
                break;
        }
        return ut;
    }

    /**
     *
     * @param score
     * @return
     */
    public static String convertGrade(Integer score) {
        String grade = "";
        switch (score.intValue()) {
            case 1:
                grade = "A级";
                break;
            case 2:
                grade = "B级";
                break;
            case 3:
                grade = "C级";
                break;
            case 4:
                grade = "D级";
                break;
            default:
                grade = "";
                break;
        }
        return grade;
    }


    /**
     * 转换用户数据为穿梭框需要的数据
     * @param users
     * @return
     */
    public static List<CusTransfer> convertCusTransfer(List<User> users) {
        List<CusTransfer> cusTransfers = new ArrayList<>();
        if (MicrovanUtil.isNotEmpty(users)) {
            for (User user : users) {
                cusTransfers.add(new CusTransfer(user.getOid(), user.getUserName()));
            }
        }
        return cusTransfers;
    }


    /**
     *
     * @param rangeType
     * @return
     */
    public static long convertRangeTypeToRoleId(Integer rangeType) {
        long roleId;
        switch (rangeType.intValue()) {
            case 1:
                roleId = ProjectConstants.ROLE_DZLD;
                break;
            case 2:
                roleId = ProjectConstants.ROLE_FGLD;
                break;
            case 3:
                roleId = ProjectConstants.ROLE_BMFZR;
                break;
            case 4:
                roleId = ProjectConstants.ROLE_QTZC;
                break;
            case 5:
                roleId = ProjectConstants.ROLE_PTJS;
                break;
            default:
                roleId = ProjectConstants.ROLE_PTJS;
                break;
        }
        return roleId;
    }

    /**
     * 将男女转换为码值
     * @param gradeStr
     * @return
     */
    public static int convertSexStr(String gradeStr) {
        if ("男".equals(gradeStr)) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 将用户类型转换为码值
     * @param userTypeStr
     * @return
     */
    public static int convertUserTypeStr(String userTypeStr) {
        int userType;
        if ("编外运行".equals(userTypeStr)) {
            userType = 2;
        } else if ("行政外聘".equals(userTypeStr)) {
            userType = 3;
        } else {
            userType = 1;
        }
        return userType;
    }

    /**
     * 根据身份证号获取出生年月
     * @param idCard
     * @return
     */
    public static String obtainBirthdayFromIdCard(String idCard) {
        String birthday = "";
        if (MicrovanUtil.isNotEmpty(idCard)) {
            if (idCard.length() == 15 || idCard.length() == 18) {
                if(idCard.length() == 15) {
                    birthday = "19" + idCard.substring(6, 10);
                } else if(idCard.length() == 18) {
                    birthday = idCard.substring(6, 12);
                }
                String year = birthday.substring(0, 4);
                String month = birthday.substring(4, 6);
                birthday = year + "-" + month;
            }
        }
        return birthday;
    }

    /**
     *
     * @param sex
     * @return
     */
    public static int getSex(String sex) {
        int intSex = 1;
        if (sex.contains("女")) {
            intSex = 2;
        }
        return intSex;
    }

     public static int getPageNum(String pageNum){
         int page = 1;
         if (MicrovanUtil.isNotEmpty(pageNum)) {
             page = Integer.valueOf(pageNum);
         }
         return page;
     }

    public static int getPageSize(String size){
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return pageSize;
    }

    public static Integer getDictKeyByValue (Map<Integer, String > dictMap, String value){
        if(!CollectionUtils.isEmpty(dictMap) && StringUtils.isNotBlank(value)){
            for(Map.Entry<Integer, String> entry : dictMap.entrySet()){
                if(value.equals(entry.getValue())){
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static Map<Integer, String > LEVEL_DICT_MAP = new HashMap<>();
    public static Map<Integer, String > MILITARY_LEVEL_DICT_MAP = new HashMap<>();
    public static Map<Integer, String > GOOD_FLAG_DICT_MAP = new HashMap<>();
    public static Map<Integer, String > POLITICAL_LEVEL_DICT_MAP = new HashMap<>();

    static {
        //级别字典
        LEVEL_DICT_MAP.put(1, "国际");
        LEVEL_DICT_MAP.put(2, "国家级");
        LEVEL_DICT_MAP.put(3, "省级");
        LEVEL_DICT_MAP.put(4, "市级");

        //军训等级
        MILITARY_LEVEL_DICT_MAP.put(1, "合格");
        MILITARY_LEVEL_DICT_MAP.put(0, "不合格");

        //是否优秀字典
        GOOD_FLAG_DICT_MAP.put(0, "否");
        GOOD_FLAG_DICT_MAP.put(1, "是");

        //党团活动等级
        POLITICAL_LEVEL_DICT_MAP.put(1, "一级");
        POLITICAL_LEVEL_DICT_MAP.put(2, "二级");
        POLITICAL_LEVEL_DICT_MAP.put(3, "三级");

    }
}
