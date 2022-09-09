package com.poho.stuup.constant;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 19:08 2019-06-09
 * @Modified By:
 */
public class ProjectConstants {

    public static String PROJECT_PIC = "pic";
    public static String PROJECT_PDF = "pdf";
    public static String PROJECT_TEMP = "temp";
    public static String PROJECT_COMMON = "common";

    /**
     * 短信验证码有效时间15分钟
     */
    public static long MSG_VALID_TIME = 15 * 60 * 1000;

    /**
     * 放入Session的管理员信息Key值
     */
    public static String SESSION_USER = "stuupSesAdmin";

    /**
     * 用户默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 团队标识
     */
    public static final String TEAM_SIGN = "poho";

    /**
     * 新接口URL
     */
    public static final String API_URL_NEW = "http://192.168.20.224:7081/api/vocational_school";


    /**
     * 系统默认角色：党政领导
     */
    public static long ROLE_DZLD = 1;
    /**
     * 系统默认角色：分管领导
     */
    public static long ROLE_FGLD = 2;

    /**
     * 系统默认角色：部门负责人
     */
    public static long ROLE_BMFZR = 3;
    /**
     * 系统默认角色：其他中层
     */
    public static long ROLE_QTZC = 4;
    /**
     * 系统默认角色：普通教师
     */
    public static long ROLE_PTJS = 5;
    /**
     * 系统默认角色：业务管理员
     */
    public static long ROLE_YWGLY = 6;
    /**
     * 系统默认角色：系统管理员
     */
    public static long ROLE_XTGLY = 7;
    /**
     * 系统默认角色：人力资源处处长
     */
    public static long ROLE_RLZYCCZ = 8;

    /**
     * 系统默认角色：学生
     */
    public static long ROLE_STU = 9;

    /**
     * 用户状态
     */
    public static int USER_STATE_COMMON = 1;
    public static int USER_STATE_DISABLE = 2;



    /**
     * 教师类型
     * 1在职在编
     * 2编外运行
     * 3行政外聘
     */
    public static int TEACHER_TYPE_ZZZB = 1;
    public static int TEACHER_TYPE_BWYX = 2;
    public static int TEACHER_TYPE_XZWP = 3;

    /**
     * 用户类型
     * 1 学生
     * 2 教师
     */
    public static int USER_TYPE_STU = 1;
    public static int USER_TYPE_TEACHER = 2;

    /**
     * 1党政领导
     * 2分管领导
     * 3部门负责人
     * 4其他中层
     * 5普通员工
     * 6所有中层
     */
    public static int RANGE_TYPE_DZLD = 1; //党政领导
    public static int RANGE_TYPE_FGLD = 2; //分管领导
    public static int RANGE_TYPE_BMFZR = 3; //部门负责人
    public static int RANGE_TYPE_QTZC= 4; //其他中层
    public static int RANGE_TYPE_PTYG= 5; //普通员工
    public static int RANGE_TYPE_SYZC= 6; //所有中层


    /**
     * 系统参数：同步开关
     */
    public static final String PARAM_SYNC_SWITCH = "sync_switch";
}
