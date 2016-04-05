package com.voyageone.web2.sdk.api;

/**
 * 公用常量类。
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.0
 * @since 2.0.0
 */
public class VoApiConstants {
    /** TOP默认时间格式 **/
//    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
//
//    /** TOP Date默认时区 **/
//    public static final String DATE_TIMEZONE = "GMT+8";
//
//    /** UTF-8字符集 **/
//    public static final String CHARSET_UTF8 = "UTF-8";
//
//    /** GBK字符集 **/
    public static final String CHARSET_GBK = "GBK";

    /** TOP JSON 应格式 */
    public static final String FORMAT_JSON = "json";
    /** TOP XML 应格式 */
    public static final String FORMAT_XML = "xml";

    /** MD5签名方式 */
    public static final String SIGN_METHOD_MD5 = "md5";
    /** HMAC签名方式 */
    public static final String SIGN_METHOD_HMAC = "hmac";

    /** SDK版本号 */
    public static final String SDK_VERSION = "voyageone-web2-sdk-2.0.0";

//    /** 响应编码 */
//    public static final String ACCEPT_ENCODING = "Accept-Encoding";
//    public static final String CONTENT_ENCODING = "Content-Encoding";
//    public static final String CONTENT_ENCODING_GZIP = "gzip";

//    /** 返回的错误码 */
//    public static final String ERROR_RESPONSE = "error_response";
//    public static final String ERROR_CODE = "code";
//    public static final String ERROR_MSG = "msg";
//    public static final String ERROR_SUB_CODE = "sub_code";
//    public static final String ERROR_SUB_MSG = "sub_msg";


    public enum VoApiErrorCodeEnum {
        ERROR_CODE_70000("70000", "Unknow Error!"),
        ERROR_CODE_70001("70001", "body is null."),
        ERROR_CODE_70002("70002", "code is null"),
        ERROR_CODE_70003("70003", "channelId not found"),
        ERROR_CODE_70004("70004", "not string or number type"),
        ERROR_CODE_70005("70005", "check error1"),
        ERROR_CODE_70006("70006", "check error2"),
        ERROR_CODE_70007("70007", "requtest param not found"),

        ERROR_CODE_70008("70008", "parent tag not found"),
        ERROR_CODE_70009("70009", "tag name is exist"),
        ERROR_CODE_70010("70010", "tag is not allowed to delete in use"),

        ERROR_CODE_70011("70011", "product has been update, not update!"),
        ;

        private final String errorCode;
        private final String errorMsg;

        private VoApiErrorCodeEnum(String errorCode, String errorMsg) {
            this.errorCode = errorCode;
            this.errorMsg = errorMsg;
        }

        public String getErrorCode() {
            return this.errorCode;
        }

        public String getErrorMsg() {
            return this.errorMsg;
        }
    }
}
