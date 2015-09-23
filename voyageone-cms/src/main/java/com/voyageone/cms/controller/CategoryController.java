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
import com.voyageone.cms.UrlConstants;
import com.voyageone.cms.formbean.CategoryProductCNBean;
import com.voyageone.cms.formbean.CategoryBean;
import com.voyageone.cms.formbean.CategoryProductUSBean;
import com.voyageone.cms.formbean.CategoryUSBean;
import com.voyageone.cms.formbean.CategoryCNBean;
import com.voyageone.cms.formbean.CategoryModelBean;
import com.voyageone.cms.formbean.CategoryPriceSettingBean;
import com.voyageone.cms.formbean.PageParamBean;
import com.voyageone.cms.service.CategoryEditService;
import com.voyageone.common.Constants;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;


/**
 * Category
 * 
 * @author eric
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CMS_EDIT_CATEGORY)
public class CategoryController extends BaseController {

	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(CategoryController.class);

	@Autowired
	private CategoryEditService categoryEditService;

	@RequestMapping(value = "/doGetCategoryList")
	public void doGetCategoryList(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info(" channelId: " + channelId);

			List<CategoryBean> ret;

			ret = categoryEditService.doGetCategoryList(channelId);
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
			logger.info(responseBean.toString());
		}
	}

	@RequestMapping(value = "/doGetCNCategoryInfo")
	public void doGetCNCategoryInfo(HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String categoryId = (String) requestMap.get("categoryId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("categoryId: " + categoryId + " channelId: " + channelId);

			CategoryCNBean ret;

			ret = categoryEditService.getCNCategoryInfo(categoryId, channelId);
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
			logger.info(responseBean.toString());
		}
	}

	@RequestMapping(value = "/doGetCNSubCategoryList")
	public void doGetCNSubCategoryList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String categoryId = (String) requestMap.get("categoryId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("categoryId: " + categoryId + " channelId: " + channelId);
			List<CategoryCNBean> ret = categoryEditService.doGetCNSubCategoryList(categoryId, channelId);
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
			logger.info(responseBean.toString());
		}
	}

	@RequestMapping(value = "/doGetCategoryCNPriceSettingInfo")
	public void doGetCategoryCNPriceSettingInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String categoryId = (String) requestMap.get("categoryId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("categoryId: " + categoryId + " channelId: " + channelId);

			CategoryPriceSettingBean ret = categoryEditService.doGetCategoryCNPriceSettingInfo(categoryId, channelId);
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
			logger.info(responseBean.toString());
		}
	}

	/**
	 * 获取USCategoryInfo
	 * 
	 * @param request
	 * @param response
	 * @param paramMap
	 */
	@RequestMapping(value = "/doGetUSCategoryInfo")
	public void doGetUSCategoryInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String categoryId = (String) requestMap.get("categoryId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("categoryId: " + categoryId + " channelId: " + channelId);

			CategoryUSBean ret = categoryEditService.getUSCategoryInfo(categoryId, channelId);
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
			logger.info(responseBean.toString());
		}

	}

	@RequestMapping(value = "/doGetUSSubCategoryList")
	public void doGetUSSubCategory(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String categoryId = (String) requestMap.get("categoryId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("categoryId: " + categoryId + " channelId: " + channelId);

			List<CategoryUSBean> ret = categoryEditService.getUSSubCategoryList(categoryId, channelId);
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
			logger.info(responseBean.toString());
		}
	}

	@RequestMapping(value = "/doUpdateUSCategoryInfo")
	public void doUpdateUSCategoryInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody CategoryUSBean inCategoryBean) {

		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {

			// 输入参数出力
			logger.info("inCategoryBean: " + inCategoryBean);

			isSuccess = categoryEditService.updateUSCategoryInfo(inCategoryBean, getUser());

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

	@RequestMapping(value = "/doUpdateCNCategoryInfo")
	public void doUpdateCNCategoryInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody CategoryCNBean inCategoryBean) {

		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {

			// 输入参数出力
			logger.info("inCategoryBean: " + inCategoryBean);

			isSuccess = categoryEditService.updateCNCategoryInfo(inCategoryBean, getUser());

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

	@RequestMapping(value = "/doUpdateCategoryCNPriceSettingInfo")
	public void doUpdateCNPriceSettingInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody CategoryPriceSettingBean inCategoryPriceBean) {

		AjaxResponseBean responseBean = new AjaxResponseBean();
		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {

			// 输入参数出力
			logger.info("inCategoryPriceBean: " + JsonUtil.getJsonString(inCategoryPriceBean));

			isSuccess = categoryEditService.updateCategoryCNPriceSettingInfo(inCategoryPriceBean, getUser());

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

	/**
	 * 获取USCategoryModelList信息
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doGetUSModelList")
	public void doGetUSModelList(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			
			// 输入参数出力
			logger.info("param：" + JsonUtil.getJsonString(requestMap));
			List<CategoryModelBean> ret = null;
			ret = categoryEditService.getUSCategoryModelList(requestMap);
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
			logger.info(responseBean.toString());
		}
	}

	/**
	 * 获取CNCategoryModelList信息
	 * 
	 * @param request
	 * @param response
	 * @param requestMap
	 */
	@RequestMapping(value = "/doGetCNModelList")
	public void doGetCNModelList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestMap) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			String categoryId = (String) requestMap.get("categoryId");
			String channelId = (String) requestMap.get("channelId");
			// 输入参数出力
			logger.info("categoryId: " + categoryId + " channelId: " + channelId);

			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("categoryId", categoryId);
			data.put("channelId", channelId);
			List<CategoryModelBean> ret = null;
			ret = categoryEditService.getCNCategoryModelList(data);
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
			logger.info(responseBean.toString());
		}
	}

	@RequestMapping(value = "/doGetUSProductList")
	public void doGetUSProductList(HttpServletRequest request, HttpServletResponse response, @RequestBody DtRequest<PageParamBean> dtRequest) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			// 输入参数出力
			logger.info("dtRequest: " + JsonUtil.getJsonString(dtRequest));

			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("categoryId",dtRequest.getParam().getCategoryId());
			data.put("channelId", dtRequest.getParam().getChannelId());
			data.put("offset", dtRequest.getStart());
			data.put("pageCount", dtRequest.getLength());
			DtResponse<List<CategoryProductUSBean>> dtResponse = categoryEditService.getCategoryUSProductList(data);
			dtResponse.setDraw(dtRequest.getDraw());
			isSuccess = true;
			responseBean.setResultInfo(dtResponse);
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

	@RequestMapping(value = "/doGetCNProductList")
	public void doGetCNProductList(HttpServletRequest request, HttpServletResponse response, @RequestBody DtRequest<PageParamBean> dtRequest) {
		AjaxResponseBean responseBean = new AjaxResponseBean();

		boolean isSuccess = false;
		String msgCode = "";
		int msgType = 0;
		try {
			
			// 输入参数出力
			logger.info("dtRequest: "+ JsonUtil.getJsonString(dtRequest));

			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("categoryId", dtRequest.getParam().getCategoryId());
			data.put("channelId", dtRequest.getParam().getChannelId());
			data.put("offset", dtRequest.getStart());
			data.put("pageCount", dtRequest.getLength());
			DtResponse<List<CategoryProductCNBean>> dtResponse = categoryEditService.getCategoryCNProductList(data);
			dtResponse.setDraw(dtRequest.getDraw());
			isSuccess = true;
			responseBean.setResultInfo(dtResponse);
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

}
