package com.voyageone.common.configs.Enums;

/**
 * Created by Jack on 9/27/2015.
 */
public class PromotionTypeEnums {
    /**
     * 对应 tm_carrier 表中存在的所有快递公司名称
     * @author jamse
     */
    public enum Type {

        /**
         * 特价宝
         */
        TEJIABAO(0),
        /**
         * 价格披露
         */
        JIAGEPILU(1);



        private Integer typeId;

        Type(Integer typeId) {
            this.typeId = typeId;
        }

        public Integer getTypeId() {
            return this.typeId;
        }


        public static Type valueOf(int typeId) {
            for (Type type : values())
                if (type.getTypeId() == typeId)
                    return type;
            return null;
        }
    }


}
