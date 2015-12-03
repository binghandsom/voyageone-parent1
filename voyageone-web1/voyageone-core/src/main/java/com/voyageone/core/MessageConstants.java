package com.voyageone.core;

public interface MessageConstants {
    // 消息类型：输入参数校验失败
    int MESSAGE_TYPE_VALIDATE = 1;
    // 消息类型：业务异常
    int MESSAGE_TYPE_BUSSINESS_EXCEPTION = 2;
    // 消息类型：session过期
    int MESSAGE_TYPE_SESSION_TIMEOUT = 3;
    // 消息类型：弹出框（URL无权限或token不一致等）
    int MESSAGE_TYPE_DIALOG = 4;
    // 消息类型：不可恢复异常（系统异常、其他）
    int MESSAGE_TYPE_EXCEPTION = 5;

    // 消息代码
    String MESSAGE_CODE_100001 = "100001";

    String MESSAGE_CODE_200001 = "200001";
    String MESSAGE_CODE_200002 = "200002";
    String MESSAGE_CODE_200003 = "200003";
    String MESSAGE_CODE_200004 = "200004";

    String MESSAGE_CODE_200010 = "200010";

    String MESSAGE_CODE_300001 = "300001";

    String MESSAGE_CODE_400001 = "400001";
    String MESSAGE_CODE_400002 = "400002";
    String MESSAGE_CODE_400003 = "400003";
    String MESSAGE_CODE_400004 = "400004";
    String MESSAGE_CODE_400005 = "400005";
    String MESSAGE_CODE_400006 = "400006";
    String MESSAGE_CODE_400007 = "400007";

    String MESSAGE_CODE_500001 = "500001";
    String MESSAGE_CODE_500002 = "500002";
    String MESSAGE_CODE_500003 = "500003";
    String MESSAGE_CODE_500004 = "500004";
    String MESSAGE_CODE_500005 = "500005";

    /**
     * 共通MSG定义
     */
    final class ComMsg {

        public static String INPUT_REQUIRE = "1000001";

        public static String UPDATE_BY_OTHER = "1000002";

        public static String SYSTEM_ERR = "1000003";

        public static String NOT_FOUND_CHANNEL = "1000004";

        public static String INPUT_FORMAT = "1000005";

        public static String DATA_NOT_FOUND = "1000006";

        public static String TIME_OUT = "1000007";

        public static String NO_AUTHORIZE = "1000008";

        public static String NOT_EXISTS = "1000009";

        public static String ALREADY_EXISTS = "1000010";

    }
}
