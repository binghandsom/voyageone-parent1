package com.voyageone.service.impl.cms.sx;

/**
 * Created by Charis on 2017/3/29.
 *
 *  更新商品平台数据时所涉及到的属性名称
 */
public enum PlatformWorkloadAttribute {

    // 商品PC描述
    DESCRIPTION("description"),

    // 商品标题
    TITLE("title"),

    // 商品主图
    ITEM_IMAGES("item_images"),

    // 店铺内分类
    SELLER_CIDS("seller_cids"),

    // 透明素材图
    WHITE_BG_IMAGE("white_bg_image"),

    // 商品卖点
    SELL_POINTS("sell_points"),

    // 商品APP描述
    WIRELESS_DESC("wireless_desc");

    String name;
    PlatformWorkloadAttribute(String name){
        this.name = name;
    }

    public String getValue() {
        return this.name;
    }

    //是否包含枚举项
    public static PlatformWorkloadAttribute get(String name){
        //所有的枚举值
        PlatformWorkloadAttribute[] platformWorkloadAttribute = values();
        //遍历查找
        for(PlatformWorkloadAttribute s : platformWorkloadAttribute){
            if(s.getValue().equals(name)){
                return s;
            }
        }

        return null;
    }
}
