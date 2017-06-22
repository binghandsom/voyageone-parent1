package com.voyageone.service.model.cms.enums;

/**
 * 销售渠道种类
 *
 * @author Edward, 2016/02/04.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum CartType {

    TMALL("TMall", "天猫", "TM", 20, 1),

    TAOBAO("Taobao", "淘宝", "TB", 21, 1),

    OFFLINE("Off Line", "线下订单", "OF", 22, 0),

    TMALLG("TMallG", "天猫国际", "TG", 23, 1),

    JINGDONG("JingDong", "京东", "JD", 24, 2),

    JINGDONGG("JingDongG", "京东国际", "JG", 26, 2),

    JUMEI("JuMei", "聚美优品", "JM", 27, 4),

    USJOI_JGJ("US Joi JGJ", "US Joi匠心界(JG)", "JGJ", 28, 2),

    USJOI_JGY("US Joi JGY", "US Joi悦境(JG)", "JGY", 29, 2),

    USJOI_JGT("US Joi JGT", "US Joi测试(TG)", "JGY", 98, 1),

    TMALL_MINIMALL("Tmall MiniMall", "Tmall MiniMall(JG)", "JGY", 30, 1),

    KAOLA("KAOLA", "KAOLA", "KL", 34, 8),

    USJOI_JGJ_928("USJOI匠心界", "USJOI匠心界", "USJGJ", 928, 0),

    USJOI_JGY_929("USJOI匠心界", "USJOI匠心界", "USJGY", 929, 0),

    USJOI_JGT_998("USJOI匠心界", "USJOI测试", "USJGT", 998, 0),

    MASTER("Master", "主数据", "MT", 0, 0),

    FEED("Feed", "品牌方数据", "TH", 1, 0);

    private String name;

    private String cnName;

    private String shortName;

    private Integer cartId;

    private Integer platformId;

    CartType(String name, String cnName, String shortName, Integer cartId, Integer platformId) {
        this.name = name;
        this.cnName = cnName;
        this.shortName = shortName;
        this.cartId = cartId;
        this.platformId = platformId;
    }

    /**
     * 根据cartId返回对应CartType
     */
    public static CartType getCartById (Integer cartId) {
        for (CartType c : CartType.values()) {
            if (c.getCartId().intValue() == cartId.intValue()) {
                return c;
            }
        }
        return null;
    }

    public static String getCartNameById (Integer cartId, String languageId) {
        for (CartType c : CartType.values()) {
            if (c.getCartId().intValue() == cartId.intValue()) {
                if (languageId != null) {
                    switch (languageId) {
                        case "en":
                            return c.name;
                        case "cn":
                            return c.cnName;
                        default:
                            return c.name;
                    }
                } else {
                    return c.name;
                }
            }
        }
        return null;
    }

    public static Integer getPlatformIdById(Integer cartId)
    {
        for (CartType c : CartType.values()) {
            if (c.getCartId().intValue() == cartId.intValue()) {
                return c.getPlatformId();
            }
        }
        return  0;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }
}
