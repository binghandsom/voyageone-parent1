package com.voyageone.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voyageone.base.BaseController;
import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.UrlConstants;
import com.voyageone.cms.formbean.PageParamPromotionBean;
import com.voyageone.cms.formbean.PromotionProduct;
import com.voyageone.cms.formbean.PromotionYearList;
import com.voyageone.cms.modelbean.Promotion;
import com.voyageone.cms.modelbean.RelationPromotionProduct;
import com.voyageone.cms.service.PromotionEditService;
import com.voyageone.common.Constants;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CMS_EDIT_PROMOTION)
public class PromotionController extends BaseController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(PromotionController.class);

	@Autowired
	private PromotionEditService promotionEditService;

	@RequestMapping(value = "/doSavePromotionInfo")
	public void doSavePromotionInfo(HttpServletResponse response, @RequestBody Promotion promotion) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			logger.info(" promotion: " + JsonUtil.getJsonString(promotion));
			String effectiveDateStart = promotion.getEffectiveDateStart();
			if (effectiveDateStart != null) {
				promotion.setPromotionYear(effectiveDateStart.substring(0, 4));
				promotion.setPromotionMonth(effectiveDateStart.substring(0, 4) + effectiveDateStart.substring(5, 7));
			}
			isSuccess = promotionEditService.insertPromotion(promotion, getUser());
			responseBean.setResultInfo(promotion);
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			// 设置返回结果
			if (msgType > 0) {
				responseBean.setResult(isSuccess, msgCode, msgType);
			} else {
				responseBean.setResult(isSuccess);
			}
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}
	}

	@RequestMapping(value = "/doGetPromotionInfo")
	public void doGetPromotionInfo(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String promotionId = String.valueOf(paramMap.get("promotionId"));
			String channelId = String.valueOf(paramMap.get("channelId"));
			logger.info(" promotionId: " + promotionId + "channelId:" + channelId);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("promotionId", promotionId);
			data.put("channelId", channelId);
			Promotion promotion = promotionEditService.getPromotionInfo(data);
			isSuccess = true;
			responseBean.setResultInfo(promotion);
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			// 设置返回结果
			if (msgType > 0) {
				responseBean.setResult(isSuccess, msgCode, msgType);
			} else {
				responseBean.setResult(isSuccess);
			}
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}

	}

	@RequestMapping(value = "/doGetPromotionList")
	public void doGetPromotionList(HttpServletResponse response, @RequestBody String channelId) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {

			logger.info("channelId:" + channelId);
			List<PromotionYearList> promotionList = promotionEditService.getPromotionList(channelId);
			isSuccess = true;
			responseBean.setResultInfo(promotionList);
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			// 设置返回结果
			if (msgType > 0) {
				responseBean.setResult(isSuccess, msgCode, msgType);
			} else {
				responseBean.setResult(isSuccess);
			}
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}

	}

	@RequestMapping(value = "/doGetSubPromotionInfo")
	public void doGetSubPromotionInfo(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String promotionType = String.valueOf(paramMap.get("promotionType"));
			Map<String, Object> data = new HashMap<String, Object>();
			if (promotionType.equals(CmsConstants.PromotionType.TYPE_YEAR)) {
				String promotionYear = String.valueOf(paramMap.get("promotionId"));
				logger.info(" promotionYear: " + promotionYear);
				data.put("promotionYear", promotionYear);
				data.put("promotionMonth", null);
			}
			if (promotionType.equals(CmsConstants.PromotionType.TYPE_MONTH)) {
				String promotionMonth = String.valueOf(paramMap.get("promotionId"));
				logger.info(" promotionMonth: " + promotionMonth);
				data.put("promotionYear", null);
				data.put("promotionMonth", promotionMonth);

			}
			List<Promotion> promotion = promotionEditService.getSubPromotionList(data);
			responseBean.setResultInfo(promotion);
			isSuccess = true;
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			// 设置返回结果
			if (msgType > 0) {
				responseBean.setResult(isSuccess, msgCode, msgType);
			} else {
				responseBean.setResult(isSuccess);
			}
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}

	}

	@RequestMapping(value = "/doGetSubPromotionProductInfo")
	public void doGetSubPromotionProductInfo(HttpServletResponse response, @RequestBody DtRequest<PageParamPromotionBean> dtRequest) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			logger.info(" dtRequest: " + JsonUtil.getJsonString(dtRequest));
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("promotionId", dtRequest.getParam().getPromotionId());
			data.put("channelId", dtRequest.getParam().getChannelId());
			data.put("offset", dtRequest.getStart());
			data.put("pageCount", dtRequest.getLength());
			DtResponse<List<PromotionProduct>> promotion = promotionEditService.getPromotionProductPage(data);
			promotion.setDraw(dtRequest.getDraw());
			responseBean.setResultInfo(promotion);
			isSuccess = true;
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			// 设置返回结果
			if (msgType > 0) {
				responseBean.setResult(isSuccess, msgCode, msgType);
			} else {
				responseBean.setResult(isSuccess);
			}
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}

	}

	@RequestMapping(value = "/doUpdatePromotionInfo")
	public void doUpdatePromotionInfo(HttpServletResponse response, @RequestBody Promotion promotion) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			logger.info(" promotion: " + JsonUtil.getJsonString(promotion));
			isSuccess = promotionEditService.updatePromotion(promotion, getUser());
			responseBean.setResultInfo(isSuccess);
			
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			// 设置返回结果
			if (msgType > 0) {
				responseBean.setResult(isSuccess, msgCode, msgType);
			} else {
				responseBean.setResult(isSuccess);
			}
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}

	}

	@RequestMapping(value = "/doGetPromMonth")
	public void doGetPromMonth(HttpServletResponse response, @RequestBody String channelId) {
		resultDeal(response, promotionEditService.getPromotionMonth(channelId));
	}

	@RequestMapping(value = "/doGetPromInfo")
	public void doGetPromInfoByMonth(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {
		resultDeal(response, promotionEditService.getPromInfo(paramMap));
	}

	@RequestMapping(value = "/doUpdatePromotionDiscount")
	public void doUpdatePromotionDiscount(HttpServletResponse response, @RequestBody Map<String, Object> data) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			logger.info(" data: " + JsonUtil.getJsonString(data));
			isSuccess = promotionEditService.updateBatchPromotionProduct(data);
			responseBean.setResultInfo(isSuccess);
			
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			// 设置返回结果
			if (msgType > 0) {
				responseBean.setResult(isSuccess, msgCode, msgType);
			} else {
				responseBean.setResult(isSuccess);
			}
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doDeletePromotionProduct")
    public void doDeletePromotionProduct (HttpServletResponse response, @RequestBody Map<String, Object> data) {
    	AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			logger.info(" data: " + JsonUtil.getJsonString(data));
			isSuccess = promotionEditService.deletePromotionProduct(data);
			responseBean.setResultInfo(isSuccess);
		
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			// 设置返回结果
			if (msgType > 0) {
				responseBean.setResult(isSuccess, msgCode, msgType);
			} else {
				responseBean.setResult(isSuccess);
			}
			// 结果返回输出流
			responseBean.writeTo(request, response);
			// 输出结果出力
			logger.info(responseBean.toString());
		}
    	
    }
	@RequestMapping(value = "/doCreatePromProductRelation")
	public void doCreatePromProductRelation(HttpServletResponse response, @RequestBody Map<String, String[]> paramMap) {
		String[] promotionIdArray = paramMap.get("promotionIdArray");
		String[] productIdArray = paramMap.get("productIdArray");
		resultDeal(response, promotionEditService.createPromProductRelation(getUser(), promotionIdArray, productIdArray));
	}

	// 发送结果集合
	private synchronized void resultDeal(HttpServletResponse response, Map<String, Object> resultMap) {
		AjaxResponseBean result = new AjaxResponseBean();
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		// logger.info(result.toString());
	}
}
