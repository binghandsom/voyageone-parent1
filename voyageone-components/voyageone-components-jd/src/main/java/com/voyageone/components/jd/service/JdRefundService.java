package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.afsservice.AfsServiceProcessFacade.AfsRefundInfoOut;
import com.jd.open.api.sdk.domain.refundapply.RefundApplySoaService.RefundApplyVo;
import com.jd.open.api.sdk.request.afsservice.AfsserviceRefundinfoGetRequest;
import com.jd.open.api.sdk.request.refundapply.PopAfsSoaRefundapplyQueryByIdRequest;
import com.jd.open.api.sdk.request.refundapply.PopAfsSoaRefundapplyQueryPageListRequest;
import com.jd.open.api.sdk.response.afsservice.AfsserviceRefundinfoGetResponse;
import com.jd.open.api.sdk.response.refundapply.PopAfsSoaRefundapplyQueryByIdResponse;
import com.jd.open.api.sdk.response.refundapply.PopAfsSoaRefundapplyQueryPageListResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jd.JdBase;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdRefundService extends JdBase {
	
	private static final String C_JD_RETURN_SUCCESS = "0";
	
	/**
	 * 京东退款审核单列表查询
	 */
	public List<RefundApplyVo> doJDSearchRefundOrders(ShopBean shop, String ids, String startdate, String enddate, Long status) {
		List<RefundApplyVo> jdRefundList = new ArrayList<RefundApplyVo>();

		// 当前页:第一页
		int intPageNow = 1;
		
		// 出错次数
		int intApiErrorCount = 0;

		while (true) {
			try {				
				// 参数设置
				PopAfsSoaRefundapplyQueryPageListRequest request=new PopAfsSoaRefundapplyQueryPageListRequest();
				if(ids != null){
					request.setIds(ids);
				}
				if(status != null){
					request.setStatus(status);
				}
				if(startdate != null){
					// 开始时间
					request.setApplyTimeStart(startdate);
				}
				if(enddate != null){
					// 会被检索到的订单的状态
					request.setApplyTimeEnd(enddate);
				}
//				request.setOrderId("9550443614");
				
//				request.setCheckTimeStart(startdate);
//				
//				request.setCheckTimeEnd(enddate);
				// 当前页
				request.setPageIndex(intPageNow);
				// 每页件数(最大50件)
				request.setPageSize(50);

				PopAfsSoaRefundapplyQueryPageListResponse response=reqApi(shop, request);
				
				if (C_JD_RETURN_SUCCESS.equals(response.getCode())) {
					// 京东返回正常的场合
					// 看一下是否有数据，如果列表长度为0，那就不用再继续做了
					if (response.getQueryResult().getResult().isEmpty()) {
						break;
					}

					// 遍历一下返回的订单列表
					for (RefundApplyVo refundSearchInfo : response.getQueryResult().getResult()) {
	                	jdRefundList.add(refundSearchInfo);
					}
					
					// 因为本次调用API成功了，所以出错次数清零
					intApiErrorCount = 0;
					
					logger.info("success:doSearchRefundOrders:count:" + response.getQueryResult().getResult().size());

					// 做下一页
					intPageNow++;

				} else {
					logger.info("error:doSearchRefundOrders:error1:page:" + intPageNow + ":errorCount:" + intApiErrorCount);
					// 出错了，再做一遍试试看
					intApiErrorCount++;
					break;
				}
					
			} catch (Exception e) {
				logger.info("error:doSearchRefundOrders:channel:error2:page:" + intPageNow + ":errorCount:" + intApiErrorCount);
				// 异常了，再做一遍试试看
				break;
			}
		}
		
		return jdRefundList;
	}
	
	/**
	 * 京东根据Id查询退款审核单
	 */
	public RefundApplyVo doJDRefundDetail(ShopBean shop, String id) {
		PopAfsSoaRefundapplyQueryByIdRequest request=new PopAfsSoaRefundapplyQueryByIdRequest(); 
		request.setId(Long.parseLong(id));
		try {
			PopAfsSoaRefundapplyQueryByIdResponse response=reqApi(shop, request);
			if (C_JD_RETURN_SUCCESS.equals(response.getCode())) {
				// 京东返回正常的场合
				// 看一下是否有数据，如果列表长度为0，那就不用再继续做了
				if (!response.getQueryResult().getResult().isEmpty()) {
					return response.getQueryResult().getResult().get(0);
				}
			}
		} catch (JdException e) {
			logger.info("error:doRefundDetail:id:" + id);
			return null;
		} 
		return null;
	}
	
	/**
	 * 京东根据服务单号取得退款信息
	 */
	public AfsRefundInfoOut doJDGetRefundInfo(ShopBean shop, String serviceId){
		AfsserviceRefundinfoGetRequest request=new AfsserviceRefundinfoGetRequest();
		request.setAfsServiceId(Integer.parseInt(serviceId));
		try {
			AfsserviceRefundinfoGetResponse response=reqApi(shop, request);
			if (C_JD_RETURN_SUCCESS.equals(response.getCode())) {
				// 京东返回正常的场合
				return response.getPublicResultObject().getAfsRefundInfoOut();
			}
		} catch (JdException e) {
			logger.info("error:doJDGetRefundInfo:serviceId:" + serviceId);
			return null;
		}
		return null;
	}
}
