package com.voyageone.components.rabbitmq.exception;



public class MQMessageRuleException extends Exception {
    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MQMessageRuleException() {
    }

    public MQMessageRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQMessageRuleException(String message) {
        super(message);
    }
    public MQMessageRuleException(String code,String message) {
        super(message);
        this.setCode(code);
    }
}
