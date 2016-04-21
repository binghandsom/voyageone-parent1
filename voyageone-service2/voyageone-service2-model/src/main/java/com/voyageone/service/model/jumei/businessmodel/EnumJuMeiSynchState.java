package com.voyageone.service.model.jumei.businessmodel;

// 0:未更新  1:待上传 2:上新更新成功 3:修改 更新成功 4:上传异常
public enum  EnumJuMeiSynchState {

    NoUpdate(0),      //白底方图
    Updating(1),
    NewSuccess(2),
    UpdateSuccess(3);
    private int id;

    private EnumJuMeiSynchState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
