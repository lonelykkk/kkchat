package com.kkk.entity.constants;

import com.kkk.entity.enums.UserContactTypeEnum;

public class Constants {
    public static final String ZERO_STR = "0";

    public static final Integer ZERO = 0;

    public static final Integer ONE = 1;

    public static final Integer LENGTH_10 = 10;
    public static final Integer LENGTH_11 = 11;
    public static final Integer LENGTH_20 = 20;

    public static final Integer LENGTH_30 = 30;

    public static final String SESSION_KEY = "session_key";

    public static final String FILE_FOLDER_FILE = "/file/";

    public static final String FILE_FOLDER_TEMP = "/temp/";

    public static final String FILE_FOLDER_TEMP_2 = "temp";

    public static final String FILE_FOLDER_IMAGE = "images/";

    public static final String FILE_FOLDER_AVATAR_NAME = "avatar/";

    public static final String CHECK_CODE_KEY = "check_code_key";

    public static final String IMAGE_SUFFIX = ".png";

    public static final String COVER_IMAGE_SUFFIX = "_cover.png";

    public static final String[] IMAGE_SUFFIX_LIST = new String[]{".jpeg", ".jpg", ".png", ".gif", ".bmp", ".webp"};

    public static final String[] VIDEO_SUFFIX_LIST = new String[]{".mp4", ".avi", ".rmvb", ".mkv", ".mov"};

    public static final Long FILE_SIZE_MB = 1024 * 1024L;

    /**
     * redis key 相关
     */

    /**
     * 过期时间 1分钟
     */
    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 60;


    public static final Integer REDIS_KEY_EXPIRES_HEART_BEAT = 6;

    /**
     * 过期时间 1天
     */
    public static final Integer REDIS_KEY_EXPIRES_DAY = REDIS_KEY_EXPIRES_ONE_MIN * 60 * 24;


    public static final Integer REDIS_KEY_TOKEN_EXPIRES = REDIS_KEY_EXPIRES_DAY * 2;


    public static final String REDIS_KEY_CHECK_CODE = "kkk:checkcode:";
    public static final String REDIS_KEY_WS_TOKEN = "kkk:ws:token:";

    public static final String REDIS_KEY_WS_TOKEN_USERID = "kkk:ws:token:userid";

    public static final String REDIS_KEY_WS_USER_HEART_BEAT = "kkk:ws:user:heartbeat";

    public static final String REDIS_KEY_WS_ON_LINE_USER = "kkk:ws:online:";

    //用户联系人列表
    public static final String REDIS_KEY_USER_CONTACT = "kkk:ws:user:contact:";

    //用户参与的会话列表
    public static final String REDIS_KEY_USER_SESSION = "kkk:ws:user:session:";

    public static final Long MILLISECOND_3DAYS_AGO = 3 * 24 * 60 * 60 * 1000L;

    public static final String ROBOT_UID = UserContactTypeEnum.USER.getPrefix() + "robot";

    //系统设置
    public static final String REDIS_KEY_SYS_SETTING = "kkk:syssetting:";

    public static final String APP_UPDATE_FOLDER = "/app/";

    public static final String APP_NAME = "EasyChatSetup.";
    public static final String APP_EXE_SUFFIX = ".exe";

    //正则
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$";

    //申请信息模板
    public static final String APPLY_INFO_TEMPLATE = "我是%s";

    //自己退群
    public static final String out_group_TEMPLATE_self = "%s退出了群聊";

    //被管理员踢群
    public static final String out_group_TEMPLATE = "%s被管理员移出了群聊";

}
