package com.voyageone.service.bean.cms.businessmodel;

// 0:未更新  1:待上传 2:上新更新成功 3:修改 更新成功 4:上传异常
public enum EnumJuMeiUpdateState {

    NoUpdate(0),      //白底方图
    Updating(1),//待更新
    UpdateSuccess(2),
    Error(3);
    private int id;

    private EnumJuMeiUpdateState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
