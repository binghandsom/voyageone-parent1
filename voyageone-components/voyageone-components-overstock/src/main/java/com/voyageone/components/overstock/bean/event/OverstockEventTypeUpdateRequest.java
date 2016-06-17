package com.voyageone.components.overstock.bean.event;

import com.overstock.mp.mpc.externalclient.model.EventStatusType;

/**
 * @author aooer 2016/6/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OverstockEventTypeUpdateRequest {

    private String eventId;

    private EventStatusType eventStatusType=EventStatusType.ACKNOWLEDGED;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventStatusType getEventStatusType() {
        return eventStatusType;
    }

    public void setEventStatusType(EventStatusType eventStatusType) {
        this.eventStatusType = eventStatusType;
    }
}
