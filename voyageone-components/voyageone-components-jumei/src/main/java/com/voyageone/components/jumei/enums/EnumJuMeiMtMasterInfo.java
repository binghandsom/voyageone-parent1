package com.voyageone.components.jumei.enums;

/**
 * Created by dell on 2016/4/13.
 */
public enum  EnumJuMeiMtMasterInfo {
    SpecialNote(3),     //特殊说明
    BRANDSTORY(4),      //品牌故事图
    SIZE(5),            //尺码图
    LOGISTICS(6);       //物流介绍
    private int id;
    private EnumJuMeiMtMasterInfo(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
