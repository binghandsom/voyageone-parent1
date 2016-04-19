package com.voyageone.components.jumei.enums;

/**
 * Created by dell on 2016/4/13.
 */
public enum  EnumJuMeiProductImageType {

        NORMAL(1),          //白底方图
        PRODUCT(2),         //商品详情图
        PARAMETER(3),       //参数图

        VERTICAL(7),        //竖图
        Special(8);         //商品定制图Product Code
        private int id;

        private EnumJuMeiProductImageType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

}
