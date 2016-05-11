package com.voyageone.ims.enums;

/**
 * 主数据，属性类型
 */
public enum MasterPropTypeEnum {

    INPUT(1),

    SINGLECHECK(2),

    MULTICHECK(3),

    LABEL(4),

    COMPLEX(5),

    MULTICOMPLEX(6),

    MULTIINPUT(7);

    private int value;

    public int getValue() {
        return value;
    }

    MasterPropTypeEnum(int value) {
        this.value = value;
    }

    public static int getValue(MasterPropTypeEnum masterType) {
        return masterType.value;
    }

    public static MasterPropTypeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return INPUT;
            case 2:
                return SINGLECHECK;
            case 3:
                return MULTICHECK;
            case 4:
                return LABEL;
            case 5:
                return COMPLEX;
            case 6:
                return MULTICOMPLEX;
            case 7:
                return MULTIINPUT;
            default:
                return null;
        }
    }

    /**
     * 获取中文描述
     */
    public String getName() {
        switch (this) {
            case INPUT:
                return "文本";
            case SINGLECHECK:
                return "单选";
            case MULTICHECK:
                return "多选";
            case LABEL:
                return "标签";
            case COMPLEX:
                return "组合属性";
            case MULTICOMPLEX:
                return "组合属性组";
            case MULTIINPUT:
                return "文本组";
            default:
                return null;
        }
    }
}

