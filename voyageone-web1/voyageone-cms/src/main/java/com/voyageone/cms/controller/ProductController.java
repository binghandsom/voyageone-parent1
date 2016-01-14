package com.voyageone.cms.controller;

import com.jd.open.api.sdk.internal.util.JsonUtil;
import com.voyageone.base.BaseController;
import com.voyageone.cms.UrlConstants;
import com.voyageone.cms.formbean.*;
import com.voyageone.cms.modelbean.RelationPromotionProduct;
import com.voyageone.cms.service.ProductEditService;
import com.voyageone.common.Constants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CMS_EDIT_PRODUCT)
public class ProductController extends BaseController {
	
	@Autowired
	private ProductEditService productEditService;
	@RequestMapping(value = "/doGetUSProductInfo")
	public void doGetUSProductInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String productId = (String) requestMap.get("productId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("productId: " + productId + " channelId: " + channelId);

			ProductUSBean ret = null;

			ret = productEditService.doGetUSProductInfo(productId, channelId, true);
			isSuccess = true;
			responseBean.setResultInfo(ret);
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doGetCNProductInfo")
	public void doGetCNProductInfo(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String productId = (String) requestMap.get("productId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("productId: " + productId + " channelId: " + channelId);

			ProductCNBean ret = null;

			ret = productEditService.doGetCNProductInfo(productId, channelId, true);
			isSuccess = true;
			responseBean.setResultInfo(ret);
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doGetProductCNPriceSettingInfo")
	public void doGetProductCNPriceSettingInfo(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String productId = (String) requestMap.get("productId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("productId: " + productId + " channelId: " + channelId);

			ProductPriceSettingBean ret = new ProductPriceSettingBean();

			ret = productEditService.doGetProductCNPriceSettingInfo(productId, channelId, true);
			isSuccess = true;
			responseBean.setResultInfo(ret);
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doGetUSProductPriceInfo")
	public void doGetUSProductPriceInfo(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String productId = (String) requestMap.get("productId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("productId: " + productId + " channelId: " + channelId);

			List<ProductUSPriceInfo> ret = null;

			ret = productEditService.doGetProductUSPriceInfo(productId, channelId, null);
			isSuccess = true;
			responseBean.setResultInfo(ret);
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
			logger.debug(responseBean.toString());
		}
	}
//	@RequestMapping(value = "/doGetProductAmazonPriceInfo")
//	public void doGetProductAmazonPriceInfo(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
//		AjaxResponseBean responseBean = new AjaxResponseBean();
//
//		boolean isSuccess = false;
//		String msgCode = "";
//		int msgType = 0;
//		try {
//			String productId = (String) requestMap.get("productId");
//			String channelId = (String) requestMap.get("channelId");
//			// 输入参数出力
//			logger.info("productId: " + productId + " channelId: " + channelId);
//
//			ProductUSPriceInfo ret = null;
//
//			ret = productEditService.doGetProductUSPriceInfo(productId, channelId, CmsConstants.CartId.US_AMAZONE_CARTID);
//			isSuccess = true;
//			responseBean.setResultInfo(ret);
//		} catch (Exception e) {
//			logger.info(e);
//			msgCode = MessageConstants.MESSAGE_CODE_500001;
//			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
//		} finally {
//			// 设置返回结果
//			if (msgType > 0) {
//				responseBean.setResult(isSuccess, msgCode, msgType);
//			} else {
//				responseBean.setResult(isSuccess);
//			}
//			// 结果返回输出流
//			responseBean.writeTo(request, response);
//			// 输出结果出力
//			logger.debug(responseBean.toString());
//		}
//	}
	@RequestMapping(value = "/doGetCNProductPriceInfo")
	public void doGetCNProductlPriceInfo(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String productId = (String) requestMap.get("productId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("productId: " + productId + " channelId: " + channelId);

			List<ProductCNPriceInfo> ret = null;

			ret = productEditService.doGetProductCNPriceInfo(productId, channelId, null);
		
			isSuccess = true;
			responseBean.setResultInfo(ret);
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
			logger.debug(responseBean.toString());
		}
	}
//	@RequestMapping(value = "/doGetProductCNCartPriceInfo")
//	public void doGetProductCNCartPriceInfo(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
//		AjaxResponseBean responseBean = new AjaxResponseBean();
//
//		boolean isSuccess = false;
//		String msgCode = "";
//		int msgType = 0;
//		try {
//			String productId = (String) requestMap.get("productId");
//			String channelId = (String) requestMap.get("channelId");
//			// 输入参数出力
//			logger.info("productId: " + productId + " channelId: " + channelId);
//
//			List<ProductCNPriceInfo> ret = null;
//
//			ret = productEditService.doGetProductCNPriceInfo(productId, channelId, "");
//			isSuccess = true;
//			responseBean.setResultInfo(ret);
//		} catch (Exception e) {
//			logger.info(e);
//			msgCode = MessageConstants.MESSAGE_CODE_500001;
//			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
//		} finally {
//			// 设置返回结果
//			if (msgType > 0) {
//				responseBean.setResult(isSuccess, msgCode, msgType);
//			} else {
//				responseBean.setResult(isSuccess);
//			}
//			// 结果返回输出流
//			responseBean.writeTo(request, response);
//			// 输出结果出力
//			logger.debug(responseBean.toString());
//		}
//	}
	@RequestMapping(value = "/doGetProductImages")
	public void doGetProductImages(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String productId = (String) requestMap.get("productId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("productId: " + productId + " channelId: " + channelId);
            List<ProductImage> ret = productEditService.doGetProductImage(productId, channelId);
			
			isSuccess = true;
			responseBean.setResultInfo(ret);
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doGetProductInventory")
	public void doGetProductInventory(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String code = (String) requestMap.get("code");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("code: " + code + " channelId: " + channelId);
            List<Map<String, Object>> ret = productEditService.doGetInventory(channelId, code);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("inventoryInfo", ret);
            result.put("thirdPartnerInfo", null);
            result.put("lasterReceivedOn", null);
			isSuccess = true;
			responseBean.setResultInfo(result);
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doUpdateUSProductInfo")
	public void doUpdateUSProductInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductUSBean requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = productEditService.doUpdateUSProductInfo(requestMap);

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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doUpdateCNProductInfo")
	public void doUpdateCNProductInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductCNBaseProductInfo requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = productEditService.doUpdateCNProductInfo(requestMap);
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doUpdateCNProductTmallInfo")
	public void doUpdateCNProductTmallInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductCNTMProductInfo requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = productEditService.doUpdateCNProductTmallInfo(requestMap);
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doUpdateCNProductJingDongInfo")
	public void doUpdateCNProductJingDongInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductCNJDProductInfo requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			//requestMap.setModifier(getUser().getUserName());
			isSuccess = productEditService.doUpdateCNProductJingDongInfo(requestMap);
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = "/doUpdateProductCNPriceSettingInfo")
	public void doUpdateProductCNPriceSettingInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductPriceSettingBean requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = productEditService.doUpdateProductCNPriceSettingInfo(requestMap);
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = {"/doUpdateProductOfficialPriceInfo","/doUpdateProductUSCartPriceInfo"})
	public void doUpdateProductUSPriceInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductUSPriceInfo requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			if (request.getServletPath().lastIndexOf("/doUpdateProductOfficialPriceInfo") > -1){
				isSuccess = productEditService.doUpdateProductUSPriceInfo(requestMap,true);
			}else{
				isSuccess = productEditService.doUpdateProductUSPriceInfo(requestMap,false);
			}
			
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
			logger.debug(responseBean.toString());
		}
	}
	@RequestMapping(value = {"/doUpdateProductCNPriceInfo","/doUpdateProductCNCartPriceInfo"})
	public void doUpdateProductCNPriceInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductCNPriceInfo requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			requestMap.setModifier(getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			isSuccess = productEditService.doUpdateProductCNPriceInfo(requestMap);
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
			logger.debug(responseBean.toString());
		}
	}

	@RequestMapping(value = {"/doSetCnProductProperty"})
	public void doSetCnProductProperty(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		Map<String, Object> resMap = new HashMap<>();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			paramMap.put("modifier", getUser().getUserName());
			isSuccess = productEditService.doSetCnProductProperty(paramMap);
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			if (msgType > 0) {
				responseBean.setResult(false, msgCode, msgType);
			} else {
				resMap.put("successFlg", true);
				responseBean.setResultInfo(resMap);
				responseBean.setResult(isSuccess);
			}
			responseBean.writeTo(request, response);
			logger.debug(responseBean.toString());
		}
	}

	@RequestMapping(value = {"/doSetCnProductShare"})
	public void doSetCnProductShare(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		Map<String, Object> resMap = new HashMap<>();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			paramMap.put("modifier", getUser().getUserName());
			isSuccess = productEditService.doSetCnProductShare(paramMap, getUser());
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			if (msgType > 0) {
				responseBean.setResult(false, msgCode, msgType);
			} else {
				resMap.put("successFlg", true);
				responseBean.setResultInfo(resMap);
				responseBean.setResult(isSuccess);
			}
			responseBean.writeTo(request, response);
			logger.debug(responseBean.toString());
		}
	}

	@RequestMapping(value = {"/doGetPromotionHistory"})
	public void doGetPromotionHistroy(HttpServletResponse response,  @RequestBody DtRequest<ProductUSPriceInfo> dtRequest){
		AjaxResponseBean responseBean = new AjaxResponseBean();
		String msgCode = "";
		int msgType = 0;
		try {
			DtResponse<List<RelationPromotionProduct>> dtResponse = productEditService.doGetPromotionHistory(dtRequest);
			dtResponse.setDraw(dtRequest.getDraw());
			responseBean.setResultInfo(dtResponse);
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			if (msgType > 0) {
				responseBean.setResult(false, msgCode, msgType);
			} else {
				responseBean.setResult(true);
			}
			responseBean.writeTo(request, response);
			logger.debug(responseBean.toString());
		}
	}

	@RequestMapping(value = {"/doGetCustomInfo"})
	public void doGetCustomInfo(HttpServletResponse response, @RequestBody Map<String, Object> requestMap){
		AjaxResponseBean responseBean = new AjaxResponseBean();
		List<Map<String, Object>> resMap = new ArrayList<Map<String,Object>>();
		String msgCode = "";
		int msgType = 0;
		try {
			String productId = (String) requestMap.get("productId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("productId: " + productId + " channelId: " + channelId);
			
			resMap = productEditService.doGetCustomInfo(requestMap);
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			if (msgType > 0) {
				responseBean.setResult(false, msgCode, msgType);
			} else {
				responseBean.setResultInfo(resMap);
				responseBean.setResult(true);
			}
			responseBean.writeTo(request, response);
			logger.debug(responseBean.toString());
		}
	}

	@RequestMapping(value = {"/doUpdateCustomInfo"})
	public void doUpdateCustomInfo(HttpServletResponse response, @RequestBody List<Map<String, Object>> requestMap){
		AjaxResponseBean responseBean = new AjaxResponseBean();
		List<Map<String, Object>> resMap = new ArrayList<Map<String,Object>>();
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("参数：" + JsonUtil.toJson(requestMap));

			productEditService.doUpdateCustomInfo(requestMap, getUser());
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			if (msgType > 0) {
				responseBean.setResult(false, msgCode, msgType);
			} else {
				responseBean.setResult(true);
			}
			responseBean.writeTo(request, response);
			logger.debug(responseBean.toString());
		}
	}

	@RequestMapping(value = {"/doGetPriceHistory"})
	public void doGetPriceHistory(HttpServletResponse response,  @RequestBody DtRequest<ProductUSPriceInfo> dtRequest){
		AjaxResponseBean responseBean = new AjaxResponseBean();
		String msgCode = "";
		int msgType = 0;
		try {
			DtResponse<List<ProductUSPriceInfo>> dtResponse = productEditService.doGetPirceHistory(dtRequest);
			dtResponse.setDraw(dtRequest.getDraw());
			responseBean.setResultInfo(dtResponse);
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			if (msgType > 0) {
				responseBean.setResult(false, msgCode, msgType);
			} else {
				responseBean.setResult(true);
			}
			responseBean.writeTo(request, response);
			logger.debug(responseBean.toString());
		}
	}
	
	@RequestMapping(value = {"/doUpdateProductImg"})
	public void doUpdateProductImg(HttpServletResponse response,  @RequestBody Map<String, Object> requestMap){
		AjaxResponseBean responseBean = new AjaxResponseBean();
		List<Map<String, Object>> resMap = new ArrayList<Map<String,Object>>();
		String msgCode = "";
		int msgType = 0;
		try {
			requestMap.put("modifier", getUser().getUserName());
			logger.info("参数：" + JsonUtil.toJson(requestMap));
			
			
			productEditService.doUpdateProductImg(requestMap);
		} catch (Exception e) {
			logger.info(e);
			msgCode = MessageConstants.MESSAGE_CODE_500001;
			msgType = MessageConstants.MESSAGE_TYPE_EXCEPTION;
		} finally {
			if (msgType > 0) {
				responseBean.setResult(false, msgCode, msgType);
			} else {
				responseBean.setResult(true);
			}
			responseBean.writeTo(request, response);
			logger.debug(responseBean.toString());
		}
	}
}
