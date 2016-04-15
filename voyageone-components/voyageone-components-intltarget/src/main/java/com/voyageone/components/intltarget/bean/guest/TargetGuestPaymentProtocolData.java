package com.voyageone.components.intltarget.bean.guest;

import java.util.List;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetGuestPaymentProtocolData {

    private List<TargetGuestPaymentProtocolMetaData> protocolData;

    public List<TargetGuestPaymentProtocolMetaData> getProtocolData() {
        return protocolData;
    }

    public void setProtocolData(List<TargetGuestPaymentProtocolMetaData> protocolData) {
        this.protocolData = protocolData;
    }
}
