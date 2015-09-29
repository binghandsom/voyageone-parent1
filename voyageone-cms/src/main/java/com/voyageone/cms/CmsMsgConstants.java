package com.voyageone.cms;

public final class CmsMsgConstants {

    public final static class CategoryMsg {

        public final static String GET_CATEGORY_INFO_ERROR = "4000001";
    }

    /**
     * 主数据属性，值匹配画面使用的专用 Message Code
     */
    public interface FeedPropMappingMsg {
        /**
         * 没有传递正确的参数（一般由程序给出的不会出现此问题，当人为改动 js 数据时使用）
         */
        String NO_PARAM = "4000002";
        /**
         * 提交的匹配值类型不正确
         */
        String ERR_MAPPING_TYPE = "4000003";
        /**
         * 提交的条件格式不正确
         */
        String ERR_CONDITION_FORMAT = "4000004";
        /**
         * 不能为 FEED 类型的匹配设定条件
         * ～暂时不用～
         */
        String CONDITION_FOR_FEED = "4000005";
        /**
         * 没有提交任何匹配值
         */
        String NO_MAPPING_VALUE = "4000006";
    }

    public interface DictManage {

        /**
         * 名称不能为空
         */
        String NO_NAME = "名称不能为空";

        /**
         * 没有找到字典的具体定义
         */
        String NO_EXPRESSION = "没有找到字典的具体定义";

        /**
         * 名称重复
         */
        String DUP_NAME = "名称重复";
    }
}
