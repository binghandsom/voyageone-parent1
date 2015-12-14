package com.voyageone.web2.sdk.api.internal.util.json;

public interface JSONErrorListener {
    void start(String text);
    void error(String message, int column);
    void end();
}
