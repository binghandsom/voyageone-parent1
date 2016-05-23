package com.voyageone.common.components.issueLog.enums;

/**
 * 用于标识错误在何处记录
 * 对应 com_issue_log 的 error_type 字段
 * Created by Tester on 5/6/2015.
 *
 * @author Jonas
 */
public enum ErrorType {
    WSDL(1),
    BatchJob(2),
    OpenAPI(3)    ;

    private int id;

    ErrorType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ErrorType valueOf(int error_type_value) {
        switch (error_type_value) {
            case 1:
                return ErrorType.WSDL;
            case 2:
                return ErrorType.BatchJob;
            case 3:
                return ErrorType.OpenAPI;
            default:
                return null;
        }
    }
}
