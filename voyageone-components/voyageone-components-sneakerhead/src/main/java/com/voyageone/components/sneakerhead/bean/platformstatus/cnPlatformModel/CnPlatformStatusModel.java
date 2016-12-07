package com.voyageone.components.sneakerhead.bean.platformstatus.cnPlatformModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vantis on 2016/11/24.
 * 闲舟江流夕照晚 =。=
 */
public class CnPlatformStatusModel extends BaseCnPlatformStatusModel {
    private Integer cartId;
    private List<MqqCnPlatformStatusModel> mqq = new ArrayList<>();

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<MqqCnPlatformStatusModel> getMqq() {
        return mqq;
    }

    public void setMqq(List<MqqCnPlatformStatusModel> mqq) {
        this.mqq = mqq;
    }
}
