package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.TbBase;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Jerry on 2015-06-03.
 */
@Component
public class TbRefundService extends TbBase {


    /**
     * 查询退款留言/凭证列表
     * @param shop 店铺信息
     * @param refundId 退款编号
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param fields 需返回的字段列表
     * @param refundPhase 退款阶段
     * @return 退款信息
     */
    public RefundMessagesGetResponse getRefundMessages(ShopBean shop, long refundId, long pageNo, long pageSize, String fields, String refundPhase) throws ApiException {
        
		//获取淘宝API连接
		//TaobaoClient client = getDefaultTaobaoClient(shop);

		RefundMessagesGetRequest req = new RefundMessagesGetRequest();
		req.setFields(fields);
		req.setRefundId(refundId);
		req.setPageNo(pageNo);
		req.setPageSize(pageSize);
		req.setRefundPhase(refundPhase);

//    		response = client.execute(req , shop.getSessionKey());
		return reqTaobaoApi(shop,req);
    }

    /**
     * 卖家拒绝退款
     * @param shop 店铺信息
     * @param refundId 退款编号
     * @param message 拒绝退款说明
     * @param fileLocation 拒绝退款时的退款凭证
     * @param refundPhase 退款阶段
     * @param refundVersion 退款版本号
     * @return 卖家拒绝退款结果
     */
    public RefundRefuseResponse doRefundRefuse(ShopBean shop, long refundId, String message, String fileLocation, String refundPhase, long refundVersion) throws ApiException {
    	
		//获取淘宝API连接
		//TaobaoClient client = getDefaultTaobaoClient(shop);

		RefundRefuseRequest req = new RefundRefuseRequest();
		req.setRefundId(refundId);
		req.setRefuseMessage(message);
		FileItem fItem = new FileItem(new File(fileLocation));
		req.setRefuseProof(fItem);
		req.setRefundPhase(refundPhase);
		req.setRefundVersion(refundVersion);
    		
//    		response = client.execute(req , shop.getSessionKey());
		return reqTaobaoApi(shop, req);
    }

    /**
     * 单笔退款详情
     * @param shop　店铺信息
	 * @param fields 需要返回的字段
     * @param refundId 退款编号
     * @return 退款详情结果
     */
    public RefundGetResponse getRefundInfo(ShopBean shop, String fields, long refundId) throws ApiException {

		//TaobaoClient client = getDefaultTaobaoClient(shop);
		RefundGetRequest req=new RefundGetRequest();
		//	订单交易状态,退款状态,货物状态,退款版本号,买家是否需要退货,退还金额(退还给买家的金额),退款原因,退款说明
//    		req.setFields("refund_id,order_status,status,good_status,refund_version,has_good_return,refund_fee,refund_phase,operation_contraint,reason,desc ");
		req.setFields(fields);
		req.setRefundId(refundId);

//		response = client.execute(req , shop.getSessionKey());
		return reqTaobaoApi(shop, req);
    }
    
    /**
     * 卖家同意退货
     * @param shop 店铺信息
     * @param refundId 退款编号
     * @param remark 卖家退货留言
     * @param refundPhase 退款阶段
     * @param refundVersion 退款版本号
     * @param sellerAddressId 卖家收货地址编号
     * @return 卖家同意退货结果
     */
    public RpReturngoodsAgreeResponse doReturnGoodsAgree(ShopBean shop, long refundId, String remark, String refundPhase, long refundVersion, long sellerAddressId) throws ApiException {
    	
		//TaobaoClient client = getDefaultTaobaoClient(shop);

		RpReturngoodsAgreeRequest req = new RpReturngoodsAgreeRequest();
		req.setRefundId(refundId);
		req.setName("");
		req.setAddress("");
		req.setPost("");
		req.setTel("");
		req.setMobile("");
		req.setRemark(remark);
		req.setRefundPhase(refundPhase);
		req.setRefundVersion(refundVersion);
		req.setSellerAddressId(sellerAddressId);

//		response = client.execute(req , shop.getSessionKey());
		return reqTaobaoApi(shop, req);
    }

    /**
     * 卖家拒绝退货
     * @param shop 店铺信息
     * @param refundId 退款编号
     * @param refundPhase 退款阶段
     * @param refundVersion 退款版本号
     * @param fileLocation 上传文件地址
     * @return 卖家拒绝退货结果
     */
    public RpReturngoodsRefuseResponse doReturnGoodsRefuse(ShopBean shop, long refundId, String refundPhase, long refundVersion, String fileLocation) throws ApiException {
    	
		//TaobaoClient client = getDefaultTaobaoClient(shop);

		RpReturngoodsRefuseRequest req = new RpReturngoodsRefuseRequest();
		req.setRefundId(refundId);
		req.setRefundPhase(refundPhase);
		req.setRefundVersion(refundVersion);
		FileItem fItem = new FileItem(new File(fileLocation));
		req.setRefuseProof(fItem);

//		response = client.execute(req , shop.getSessionKey());
		return reqTaobaoApi(shop, req);
    }
    
    /**
     * 卖家回填物流信息
     * @param shop 店铺信息
     * @param refundId 退款编号
     * @param refundPhase 退款阶段
     * @param logisticsWaybillNo 物流公司运单号
     * @param logisticsCompanyCode 物流公司编号
     * @return 卖家回填物流信息结果
     */
    public RpReturngoodsRefillResponse doReturnGoodsRefill(ShopBean shop, long refundId, String refundPhase, String logisticsWaybillNo, String logisticsCompanyCode) throws ApiException {
    	
   		//TaobaoClient client = getDefaultTaobaoClient(shop);
    		
		RpReturngoodsRefillRequest req = new RpReturngoodsRefillRequest();
		req.setRefundId(refundId);
		req.setRefundPhase(refundPhase);
		req.setLogisticsWaybillNo(logisticsWaybillNo);
		req.setLogisticsCompanyCode(logisticsCompanyCode);

//		response = client.execute(req , shop.getSessionKey());
		return reqTaobaoApi(shop, req);
    }
    
    /**
     * 卖家回填物流信息
     * @param shop 店铺信息
     * @param refundId 退款编号
     * @param operator 审核人姓名
     * @param refundPhase 退款阶段
     * @param refundVersion 退款最后更新时间
     * @param result 审核是否可用于批量退款，可选值：true（审核通过），false（审核不通过或反审核）
     * @param mesage 审核留言
     * @return 卖家回填物流信息结果
     */
    public RpRefundReviewResponse doRefundReview(ShopBean shop, long refundId, String operator, String refundPhase, long refundVersion, boolean result, String mesage) throws ApiException {

		//TaobaoClient client = getDefaultTaobaoClient(shop);

		RpRefundReviewRequest req=new RpRefundReviewRequest();
		req.setRefundId(refundId);
		req.setOperator(operator);
		req.setRefundPhase(refundPhase);
		req.setRefundVersion(refundVersion);
		req.setResult(result);
		req.setMessage(mesage);

//		response = client.execute(req , shop.getSessionKey());
		return reqTaobaoApi(shop, req);
    }
}
