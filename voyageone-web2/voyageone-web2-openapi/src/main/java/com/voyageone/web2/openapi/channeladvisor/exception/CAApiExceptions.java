package com.voyageone.web2.openapi.channeladvisor.exception;

import java.util.List;

/**
 * @author aooer 2016/12/27.
 */
public final class CAApiExceptions extends CAApiException {

    private List<CAApiException> caApiExceptions;

    public List<CAApiException> getCaApiExceptions() {
        return caApiExceptions;
    }

    public void setCaApiExceptions(List<CAApiException> caApiExceptions) {
        this.caApiExceptions = caApiExceptions;
    }

    public CAApiExceptions(List<CAApiException> caApiExceptions) {
        this.caApiExceptions = caApiExceptions;
    }
}
