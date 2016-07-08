package com.voyageone.service.model.cms.enums;

/**
 * 销售渠道种类
 *
 * @author Edward, 2016/02/04.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum CartType {

    TMALL("TMall", "天猫", "TM", 22),

    TAOBAO("Taobao", "淘宝", "TB", 20),

    OFFLINE("Off Line", "线下订单", "OF", 22),

    TMALLG("TMallG", "天猫国际", "TG", 23),

    JINGDONG("JingDong", "京东", "JD", 24),

    JINGDONGG("JingDongG", "京东国际", "JG", 26),

    JUMEI("JuMei", "聚美优品", "JM", 27),

    USJOI_JGJ("US Joi JGJ", "US Joi匠心界(JG)", "JGJ", 28),

    USJOI_JGY("US Joi JGY", "US Joi悦境(JG)", "JGY", 29),

    TMALL_MINIMALL("Tmall MiniMall", "Tmall MiniMall(JG)", "JGY", 30),

    USJOI_JGJ_928("USJOI匠心界", "USJOI匠心界", "JGJ", 928),

    USJOI_JGY_929("USJOI匠心界", "USJOI匠心界", "JGY", 929),

    MASTER("Master", "主数据", "MT", 0),

    FEED("Feed", "品牌方数据", "TH", 1);

    private String name;

    private String cnName;

    private String shortName;

    private Integer cartId;

    CartType(String name, String cnName, String shortName, Integer cartId) {
        this.name = name;
        this.cnName = cnName;
        this.shortName = shortName;
        this.cartId = cartId;
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
}
