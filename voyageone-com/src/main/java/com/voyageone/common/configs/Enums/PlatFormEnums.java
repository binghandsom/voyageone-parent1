package com.voyageone.common.configs.Enums;

/**
 * Created by Jack on 4/14/2017.
 */

public class PlatFormEnums {
    /**
     * 对应 com_mt_type 表中存在的所有配置名称
     */
    public enum PlatForm {
        /**
         * 共通
         */
        COM("99"),
        /**
         * 线下平台
         */
        OF("0"),
        /**
         * 淘宝
         */
        TM("1"),
        /**
         * 京东
         */
        JD("2"),
        /**
         * 独立域名
         */
        CN("3"),
        /**
         * 聚美
         */
        JM("4"),
        /**
         * 分销
         */
        DT("6"),
        /**
         * 新独立域名
         */
        CNN("7"),

        /**
         * 网易平台
         */
        NTES("8");

        private String id;

        PlatForm(String id) {
            this.id = id;
        }

        public static PlatFormEnums.PlatForm getValueByID(String id) {
            switch (id) {
                case "99":
                    return COM;
                case "0":
                    return OF;
                case "1":
                    return TM;
                case "2":
                    return JD;
                case "3":
                    return CN;
                case "4":
                    return JM;
                case "6":
                    return DT;
                case "7":
                    return CNN;
                default:
                    return null;
            }
        }

        public String getId() {
            return id;
        }
    }
}
