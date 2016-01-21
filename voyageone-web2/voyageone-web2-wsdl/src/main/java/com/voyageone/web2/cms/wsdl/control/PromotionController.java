package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.voyageone.web2.cms.wsdl.service.PromotionService;
import com.voyageone.web2.sdk.api.request.PromotionDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionPutRequest;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;

/**
 * product Controller
 *
 * @author aooer 16/01/14
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = "/rest/promotion", method = RequestMethod.POST)
public class PromotionController extends BaseController {

	@Autowired
	private PromotionService promotionService;

	/**
	 * 添加或者修改
	 * 
	 * @param promotionPutRequest Request
	 * @return PromotionsPutResponse
	 */
	@RequestMapping("saveOrUpdate")
	public PromotionsPutResponse saveOrUpdate(
			@RequestBody PromotionPutRequest promotionPutRequest) {
		return promotionService.saveOrUpdate(promotionPutRequest);
	}

	/**
	 * 根据条件查询
	 * 
	 * @param promotionGetRequest Request
	 * @return PromotionsGetResponse
	 */
	@RequestMapping("selectByCondtion")
	public PromotionsGetResponse selectByCondition(
			@RequestBody PromotionsGetRequest promotionGetRequest) {
		return promotionService.selectByCondition(promotionGetRequest);
	}

	/**
	 * 删除
	 * 
	 * @param promotionDeleteRequest Request
	 * @return PromotionsPutResponse
	 */
	@RequestMapping("deleteById")
	public PromotionsPutResponse deleteById(
			@RequestBody PromotionDeleteRequest promotionDeleteRequest) {
		return promotionService.deleteById(promotionDeleteRequest);
	}

}
