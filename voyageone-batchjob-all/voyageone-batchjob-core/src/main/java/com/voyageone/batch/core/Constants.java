package com.voyageone.batch.core;

public interface Constants {

    // 错误
    String MAIL_ERROR = "执行时出现错误";

    String SCOPE_PROTOTYPE = "prototype";

    // exception消息分隔符
    String EXCEPTION_MESSAGE_PREFIX = "; cause is ";
    // exception消息描述最大值
    int EXCEPTION_MESSAGE_LENGTH = 200;

    // 字符串分割符(解析)
    String SPLIT_CHAR_RESOLVE = "\\^@\\^";
    // 左括号
    String LEFT_BRACKET_CHAR = "(";
    // 右括号
    String RIGHT_BRACKET_CHAR = ")";
    // 逗号
    String COMMA_CHAR = ",";
    // 撇号
    String APOSTROPHE_CHAR = "'";
    // 订单默认时间补充
    String DEFAULT_TIME_ADD_STR = " 00:00:00";
    // 订单默认日期补充
    String DEFAULT_DATE_ADD_STR = "1899-12-30 ";
    // 零
    String ZERO_CHAR = "0";
    // 1
    String ONE_CHAR = "1";
    // 没有折扣
    String DISCOUNT_NO = "1";
    // 正常折扣
    String DISCOUNT_NORMAL = "2";
    // 空字符串
    String EMPTY_STR = "";
    // 空格
    String BLANK_STR = " ";
    // [empty]字符串
    String EMPTY_SELF_STR = "empty";
    // NULL
    String NULL_STR = "NULL";
    // $
    String DOLLAR_CHAR_RESOLVE = "\\$";
    // now()
    String NOW_MYSQL = "now()";
    // 正常订单item 价差price-difference-001
    boolean ADJUSTMENT_0 = false;
    // Surcharge Discount Shipping
    boolean ADJUSTMENT_1 = true;

    String DISCOUNT_STR = "Discount";
    int DISCOUNT_TYPE = 1;
    String SURCHARGE_STR = "Surcharge";
    int SURCHARGE_TYPE = 2;
    String SHIPPING_STR = "Shipping";
    int SHIPPING_TYPE = 3;

    String NOTES_SYSTEM = "0";
    String NOTES_MANUAL = "1";

    String ALIBABA_STATUS_TRADE_SUCCESS = "TradeSuccess";
    String ALIBABA_STATUS_TRADE_CLOSE = "TradeClose";
    String ALIBABA_STATUS_REFUND_SUCCESS = "RefundSuccess";
    String ALIBABA_STATUS_REFUND_CLOSED = "RefundClosed";
    String ALIBABA_STATUS_REFUND_CREATED = "RefundCreated";
    String ALIBABA_STATUS_REFUND_SELLER_AGREE_AGREEMENT = "RefundSellerAgreeAgreement";
    String ALIBABA_STATUS_REFUND_SELLER_REFUSE_AGREEMENT = "RefundSellerRefuseAgreement";
    String ALIBABA_STATUS_REFUND_BUYER_MODIFY_AGREEMENT = "RefundBuyerModifyAgreement";
    
    String JD_STATUS_LOCKED = "LOCKED";

    String ORDER_STATUS_IN_PROCESSING = "In Processing";
    String ORDER_STATUS_APPROVED = "Approved";
    String ORDER_STATUS_CONFIRMED = "Confirmed";
    String ORDER_STATUS_RETURNED = "Returned";
    String ORDER_STATUS_CANCELED = "Canceled";
    String ORDER_STATUS_SHIPPED = "Shipped";
    String ORDER_STATUS_RETURN_UNSUCCESS_CLOSED = "Return Unsuccess Closed";
    String ORDER_STATUS_RETURN_REQUESTED = "Return Requested";
    String ORDER_STATUS_RETURN_APPROVED = "Return Approved";
    String ORDER_STATUS_RETURN_REFUSED = "Return Refused";

    // 字符串分割符(添加)
    String SPLIT_CHAR_ADD = "^@^";

    String ZERO_FLOAT_2 = "0.00";

    String ORDER_TYPE_ORIGINAL = "Original";
    String ORDER_TYPE_SPLIT = "Split";
    String ORDER_TYPE_PRESENT = "Present";
    String ORDER_TYPE_EXCHANGE = "Exchange";
    String ORDER_TYPE_PRICE_DIFFERENCE = "Price Difference";
    /**
     * E邮件的格式
     */
    String EMAIL_STYLE_STRING = "<style>"
            + "body{font-family:'微软雅黑',sans-serif}"
            + "table,th,td{border:1px solid silver;border-collapse:collapse;font-size:15px}"
            + "th,td{padding:3px 5px}"
            + "th{background:gray;color:white;border:1px solid #514e3a}"
            + "</style>";

    /**
     * IssueLog Mail格式
     */
    final class ISSUE_LOG_MAIL_FORMATE {
        public final static String SUBJECT = "异常终止任务列表（%s）";

        public final static String TABLE =
                "<div><span>%s</span>" +
                        "<table><tr>" +
                        "<th rowspan='2'>LogID</th>" +
                        "<th>ErrorType</th>" +
                        "<th>SubSystem</th>" +
                        "<th>ErrorLocation</th>" +
                        "<th>Description</th>" +
                        "<th>DataTime(GMT)</th>" +
                        "<th>DataTime(China)</th>" +
                        "</tr><tr><th colspan='6'>Description_Add</th>"+
                        "</tr>%s</table></div>";

        public final static String HEAD = "有%d个任务异常终止：";

        public static final String ROW =
                "<tr>" +
                        "<td rowspan='2'>%s</td>" +
                        "<td>%s</td>" +
                        "<td>%s</td>" +
                        "<td>%s</td>" +
                        "<td>%s</td>" +
                        "<td>%s</td>" +
                        "<td>%s</td>" +
                        "</tr>" +
                "<tr><td colspan='6'>%s</td></tr>";

    }

    /**
     * SynshipWebService 返回结果
     */
    final class SynshipWebService {

        public final static String RESULT_OK = "OK";
        public final static String RESULT_ERR = "ERR";

        public static final String ERR_CODE_RESERVATIONID_NOT_EXIST = "1007";
    }
}
