package com.voyageone.batch.ims;

import com.taobao.top.schema.enums.FieldTypeEnum;

public final class ImsConstants {

    /**
     * 第三方平台的属性类型
     */
    public final static class PlatformPropType {
        public final static int C_ERROR = 0; // 实际天猫是不存在 ERROR 的， 这里是为了万一有其他的可能性而加的
        public final static int C_INPUT = 1;
        public final static int C_SINGLE_CHECK = 2;
        public final static int C_MULTI_CHECK = 3;
        public final static int C_LABEL = 4;
        public final static int C_COMPLEX = 5;
        public final static int C_MULTI_COMPLEX = 6;

        /**
         * 根据类型名称，返回值
         *
         * @param typeName 类型名称
         * @return 类型值
         */
        public static int getValueByName(FieldTypeEnum typeName) {
            if (FieldTypeEnum.INPUT.compareTo(typeName) == 0) {
                return C_INPUT;
            } else if (FieldTypeEnum.SINGLECHECK.compareTo(typeName) == 0) {
                return C_SINGLE_CHECK;
            } else if (FieldTypeEnum.MULTICHECK.compareTo(typeName) == 0) {
                return C_MULTI_CHECK;
            } else if (FieldTypeEnum.LABEL.compareTo(typeName) == 0) {
                return C_LABEL;
            } else if (FieldTypeEnum.COMPLEX.compareTo(typeName) == 0) {
                return C_COMPLEX;
            } else if (FieldTypeEnum.MULTICOMPLEX.compareTo(typeName) == 0) {
                return C_MULTI_COMPLEX;
            } else {
                return C_ERROR;
            }
        }

        /**
         * 根据类型值，返回类型名称
         *
         * @param typeValue 类型值
         * @return 类型名称
         */
        public static FieldTypeEnum getNameByValue(int typeValue) {
            if (C_INPUT == typeValue) {
                return FieldTypeEnum.INPUT;
            } else if (C_SINGLE_CHECK == typeValue) {
                return FieldTypeEnum.SINGLECHECK;
            } else if (C_MULTI_CHECK == typeValue) {
                return FieldTypeEnum.MULTICHECK;
            } else if (C_LABEL == typeValue) {
                return FieldTypeEnum.LABEL;
            } else if (C_COMPLEX == typeValue) {
                return FieldTypeEnum.COMPLEX;
            } else if (C_MULTI_COMPLEX == typeValue) {
                return FieldTypeEnum.MULTICOMPLEX;
            } else {
                return null;
            }
        }
    }

    public final static String C_PROP_PATH_SPLIT_CHAR = ">";

}
