package com.voyageone.oms;


public interface OmsMessageConstants {

    // 消息代码

    //	订单明细删除异常
    public static final String MESSAGE_CODE_210001 = "210001";
    //	运费或自动调整运费不允许删除
    public static final String MESSAGE_CODE_210002 = "210002";
    //	订单明细追加异常
    public static final String MESSAGE_CODE_210003 = "210003";
    //	订单锁定状态更新异常
    public static final String MESSAGE_CODE_210004 = "210004";
    //	订单取消异常
    public static final String MESSAGE_CODE_210005 = "210005";

    //	订单状态更新异常
    public static final String MESSAGE_CODE_210007 = "210007";
    //	订单明细状态更新异常
    public static final String MESSAGE_CODE_210008 = "210008";
    //	订单其他属性更新异常
    public static final String MESSAGE_CODE_210009 = "210009";
    //	订单明细退货异常
    public static final String MESSAGE_CODE_210010 = "210010";
    //	订单明细取消退货异常
    public static final String MESSAGE_CODE_210011 = "210011";
    //	地址更新异常
    public static final String MESSAGE_CODE_210012 = "210012";
    //	注释更新异常
    public static final String MESSAGE_CODE_210013 = "210013";
    //	发货更新异常
    public static final String MESSAGE_CODE_210014 = "210014";
    //	该项目已被删除
    public static final String MESSAGE_CODE_210015 = "210015";
    //	订单Approve失败
    public static final String MESSAGE_CODE_210016 = "210016";
    //	拆分订单无法建立，原订单未Cancel
    public static final String MESSAGE_CODE_210017 = "210017";
    //	原始订单不存在
    public static final String MESSAGE_CODE_210018 = "210018";
    //	该订单存在Return明细，必须先Unreturn明细后，再进行订单Cancel
    public static final String MESSAGE_CODE_210019 = "210019";
    //	该项目已被Return，必须先Unreturn后，在进行订单明细Delete
    public static final String MESSAGE_CODE_210020 = "210020";
    //	Product 修正，不能删除
    public static final String MESSAGE_CODE_210021 = "210021";
    //	订单恢复异常
    public static final String MESSAGE_CODE_210022 = "210022";
    //	物品件数过多，不允许Approve
    public static final String MESSAGE_CODE_210023 = "210023";
    //	订单金额过大，不允许Approve
    public static final String MESSAGE_CODE_210024 = "210024";
    //	该客户已经存在不能更新
    public static final String MESSAGE_CODE_210025 = "210025";
    //	订单Notes保存异常
    public static final String MESSAGE_CODE_210026 = "210026";
    //	客户信息更新异常
    public static final String MESSAGE_CODE_210027 = "210027";
    //	该渠道不允许追加LineItem
    public static final String MESSAGE_CODE_210028 = "210028";
    //	该渠到不允许取消LineItem
    public static final String MESSAGE_CODE_210029 = "210029";
    //	改渠道不允许Return LineItem
    public static final String MESSAGE_CODE_210030 = "210030";
    //	当前状态不允许取消LineItem
    public static final String MESSAGE_CODE_210031 = "210031";
    //	该渠道不允许取消Order
    public static final String MESSAGE_CODE_210032 = "210032";
    //	当前状态不允许取消Order
    public static final String MESSAGE_CODE_210033 = "210033";
    //	该渠道不允许修改Order
    public static final String MESSAGE_CODE_210034 = "210034";
    //	当前状态不允许修改Order
    public static final String MESSAGE_CODE_210035 = "210035";
    //	当前状态不允许Return LineItem
    public static final String MESSAGE_CODE_210036 = "210036";
    //	当前状态不允许追加LineItem
    public static final String MESSAGE_CODE_210037 = "210037";
    //	价差订单绑定失败
    public static final String MESSAGE_CODE_210038 = "210038";
    //	该订单不存在，或该订单为子订单
    public static final String MESSAGE_CODE_210039 = "210039";
    //	客户Comment 更新异常
    public static final String MESSAGE_CODE_210040 = "210040";
    //	Invoice更新异常
    public static final String MESSAGE_CODE_210041 = "210041";
    //	InvoiceInfo更新异常
    public static final String MESSAGE_CODE_210042 = "210042";
    //	Expected < FinalGrandTotal 不允许解锁
    public static final String MESSAGE_CODE_210043 = "210043";
    //	价差订单不能绑定
    public static final String MESSAGE_CODE_210044 = "210044";
    //	未来订单不能绑定
    public static final String MESSAGE_CODE_210045 = "210045";
    //	订单退款信息取得异常
    public static final String MESSAGE_CODE_210046 = "210046";
    //	订单退款拒绝异常
    public static final String MESSAGE_CODE_210047 = "210047";
    //	淘宝API调用异常
    public static final String MESSAGE_CODE_210048 = "210048";
    //	卖家同意退货异常
    public static final String MESSAGE_CODE_210049 = "210049";
    //	退货金额与天猫不一致，不能退款。等待天猫推送。
    public static final String MESSAGE_CODE_210050 = "210050";
    //	卖家同意退款异常
    public static final String MESSAGE_CODE_210051 = "210051";
    //	卖家回填物流信息异常
    public static final String MESSAGE_CODE_210052 = "210052";
    //	审核退款单异常
    public static final String MESSAGE_CODE_210053 = "210053";
    //	上传文件失败
    public static final String MESSAGE_CODE_210054 = "210054";
    //	总退款金额与系统算出不一致，不能退款。确认客户申请退款金额是否正确。
    public static final String MESSAGE_CODE_210055 = "210055";
    //	总退款金额与天猫不一致，不能退款。等待天猫推送。
    public static final String MESSAGE_CODE_210056 = "210056";
    //	第三方订单取消失败
    public static final String MESSAGE_CODE_210057 = "210057";
    //	第三方订单已推送并未取消，取消失败
    public static final String MESSAGE_CODE_210058 = "210058";
    //	订单未推送不能申请
    public static final String MESSAGE_CODE_210059 = "210059";
    //	该项目已被Return
    public static final String MESSAGE_CODE_210060 = "210060";
    //	Surcharge项目未调整，请调整后再Approve
    public static final String MESSAGE_CODE_210061 = "210061";
    //	客户拒收更新异常
    public static final String MESSAGE_CODE_210062 = "210062";
    //	该文件已上传
    public static final String MESSAGE_CODE_210063 = "210063";
    //	Settlement关联DB保存异常
    public static final String MESSAGE_CODE_210064 = "210064";
    //	上传的文件中，没有Settlement数据
    public static final String MESSAGE_CODE_210065 = "210065";
    //	上传的Settlement文件为空
    public static final String MESSAGE_CODE_210066 = "210066";
    //	上传的Settlement文件，格式不正确
    public static final String MESSAGE_CODE_210067 = "210067";
    //	下单人不一致不能绑定
    public static final String MESSAGE_CODE_210068 = "210068";
    //	退款金额 > 订单金额，不能退款。确认申请退款金额是否正确。
    public static final String MESSAGE_CODE_210069 = "210069";
    //	订单sku个数过多，不允许Approve
    public static final String MESSAGE_CODE_210070 = "210070";
    //	汇率保存异常
    public static final String MESSAGE_CODE_210071 = "210071";

    public static final class MessageContent {
        // 单笔退款详情  调用异常
        public static final String REFUND_MESSAGES_GET_ERROR = "taobao.refund.messages.get errorCode = %s，msg = %s，subMsg = %s";
        // 单笔退款详情  调用异常
        public static final String REFUND_GET_ERROR = "taobao.refund.get errorCode = %s，msg = %s，subMsg = %s";
        // 卖家拒绝退款 调用异常
        public static final String REFUND_REFUSE_ERROR = "taobao.refund.refuse errorCode = %s，msg = %s，subMsg = %s";
        // 卖家同意退款 调用异常
        public static final String REFUND_AGREE_ERROR = "taobao.rp.refunds.agree errorCode = %s，msg = %s，subMsg = %s";
        // 卖家同意退货 调用异常
        public static final String RETURN_GOODS_AGREE_ERROR = "taobao.rp.returngoods.agree errorCode errorCode = %s，msg = %s，subMsg = %s";

        // 卖家同意退款 其他信息
        public static final String REFUND_AGREE_OTHER_INFO = "淘宝返回 ：code = %s, message = %s";

        // 天猫退款成功，OMS更新失败
        public static final String REFUND_AGREE_SUCCESS_OMSDB_UPDATE_ERROR = "天猫退款成功，OMS更新失败，请联系IT处理。（退款编号 ：%s）";

        // 订单不存在
        public static final String ORDER_IS_NOT_EXIST = "Source order id = %s is not exist.";

        // CA请求失败
        //	SubmitOrderRefund
        public static final String CA_CANCEL_ORDER_REQ_ERROR = "CA SubmitOrderRefund request failure. MessageCode = %s Message = %s";
        //	GetOrderList
        public static final String CA_GET_ORDER_INFO_REQ_ERROR = "CA GetOrderList request failure. MessageCode = %s Message = %s";
        //	GetOrderList    CheckoutStatusCode 取得
        public static final String CA_GET_ORDER_INFO_REQ_CHECKOUTSTATUS = "CA GetOrderList request CheckoutStatus = %s";

        // Pay number not exists
        public static final String PAYNUMBER_IS_NOT_EXIST = "Pay number = %s is not exist.";

    }

}
