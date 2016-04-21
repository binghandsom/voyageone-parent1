package com.voyageone.components.jumei.enums;

/**
 * @author james.li on 2016/1/28.
 * @version 2.0.0
 */
public enum JumeiImageType {
    NORMAL(1),          //白底方图
    PRODUCT(2),         //商品详情图
    PARAMETER(3),       //参数图
    BRANDSTORY(4),      //品牌故事图
    SIZE(5),            //尺码图
    LOGISTICS(6),       //物流介绍
    VERTICAL(7),        //竖图
    Special(8);         //商品定制图Product Code
    private int id;

    private JumeiImageType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
