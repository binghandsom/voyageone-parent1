package com.voyageone.cms.feed;

/**
 * 对 Operation 的映射。便于把完成信息转换成 json
 *
 * Created by Jonas on 9/2/15.
 */
public class OperationBean {

    private String desc;

    private boolean single;

    private String name;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Operation toEnum() {
        return Operation.valueOf(this.getName());
    }
}
