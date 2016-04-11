package com.voyageone.components.tmall.exceptions;

import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;

/**
 * 调用淘宝 api: taobao.item.update.schema.get 失败
 * <p>
 * Created by Jonas on 8/11/15.
 */
public class GetUpdateSchemaFailException extends Exception {

    private TmallItemUpdateSchemaGetResponse res;

    public GetUpdateSchemaFailException(TmallItemUpdateSchemaGetResponse res) {
        super(res.getSubMsg());
        this.res = res;
    }

    public TmallItemUpdateSchemaGetResponse getRes() {
        return res;
    }

    public String getSubMsg() {
        return res.getSubMsg();
    }

    public String getSubCode() {
        return res.getSubCode();
    }
}
