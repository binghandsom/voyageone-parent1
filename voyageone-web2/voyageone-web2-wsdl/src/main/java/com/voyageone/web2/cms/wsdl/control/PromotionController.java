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
	 * @api {post} /rest/promotion/saveOrUpdate 增加或修改promotion
	 * @apiName saveOrUpdatePromotion
	 * @apiDescription 添加或修改promotion
	 * @apiGroup promotion
	 * @apiVersion 0.0.1
	 * @apiPermission 认证商户
	 * @apiParam (系统级参数) {String} token <code>api_token</code>
	 * @apiParam (应用级参数) {Integer} promotionId promotionId
	 * @apiParam (应用级参数) {String} channelId channelId
	 * @apiParam (应用级参数) {Integer} cartId cartId
	 * @apiParam (应用级参数) {Boolean} promotionStatus promotionStatus
	 * @apiParam (应用级参数) {String} promotionName promotionName
	 * @apiParam (应用级参数) {String} prePeriodStart prePeriodStart
	 * @apiParam (应用级参数) {String} prePeriodEnd prePeriodEnd
	 * @apiParam (应用级参数) {String} preSaleStart preSaleStart
	 * @apiParam (应用级参数) {String} preSaleEnd preSaleEnd
	 * @apiParam (应用级参数) {String} activityStart activityStart
	 * @apiParam (应用级参数) {String} activityEnd activityEnd
	 * @apiParam (应用级参数) {String} tejiabaoId tejiabaoId
	 * @apiParam (应用级参数) {String} promotionType promotionType
	 * @apiParam (应用级参数) {String} cartName cartName
	 * @apiParam (应用级参数) {Boolean} isActive isActive
	 * @apiParam (应用级参数) {Integer} refTagId refTagId
	 * @apiSuccess (返回字段) {String} code code
	 * @apiSuccess (返回字段) {Integer} modifiedCount modifiedCount
	 * @apiSuccess (返回字段) {Integer} matchedCount matchedCount
	 * @apiSuccess (返回字段) {Integer} insertedCount insertedCount
	 * @apiSuccess (返回字段) {Integer} removedCount removedCount
	 * @apiSuccess (返回字段) {String} message message
	 * @apiSuccess (返回字段) {String} class classpath
	 * @apiSuccessExample Success Response
	 * {
	 *  "code": "0",
	 *  "modifiedCount": 0,
	 *  "matchedCount": 0,
	 *  "message": null,
	 *  "class": "com.voyageone.web2.sdk.api.response.PromotionsPutResponse",
	 *  "insertedCount": 1,
	 *  "removedCount": 0
	 * }
	 * @apiErrorExample  错误示例
	 * {
	 *	"ApiException":{
	 *		"errCode": "500",
	 *		"errMsg": "系统内部异常",
	 *		"url": "http://api.voyageone.com/cms/500.html"
	 *	}
	 * }
	 * @apiErrorExample 错误编码
	 * 500:服务器内部错误
	 * 501:内部业务错误，请联系聚美开发人员
	 * 502:认证失败
	 * 503:参数错误，不能为空
	 * 504:结束时间UNIX时间戳不能为空，且修改后的时间必须大于系统当前时间
	 * 505:无权操作
	 */
	@RequestMapping("saveOrUpdate")
	public PromotionsPutResponse saveOrUpdate(
			@RequestBody PromotionPutRequest promotionPutRequest) {
		return promotionService.saveOrUpdate(promotionPutRequest);
	}

	/**
	 * @api {post} /promotion/selectByCondtion 条件查询Promotion
	 * @apiName selectPromotionByCondtion
	 * @apiDescription 根据条件获取promotions
	 * @apiGroup promotion
	 * @apiVersion 0.0.1
	 * @apiPermission 认证商户
	 * @apiParam (系统级参数) {String} token api_token
	 * @apiParam (应用级参数) {String} channelId channelId
	 * @apiParam (应用级参数) {Integer} promotionId promotionId.
	 * @apiParam (应用级参数) {String} promotionName promotionName.
	 * @apiParam (应用级参数) {Integer} cartId cartId.
	 * @apiParam (应用级参数) {String} promotionType promotionType.
	 * @apiParam (应用级参数) {String} createdStart createdStart.
	 * @apiParam (应用级参数) {String} createdEnd createdEnd.
	 * @apiSuccess (返回字段) {[CmsBtPromotionModel]} cmsBtPromotionModels 查询结果总数.
	 * @apiSuccess (返回字段) {Int} totalCount 查询结果总数.
	 * @apiSuccess (返回字段) {String} code 状态码.
	 * @apiSuccess (返回字段) {String} message 信息.
	 * @apiSuccess (返回字段) {String} class 返回类路径.
	 * @apiSuccessExample Success Response
	 * {
	 *		"cmsBtBusinessLogModels": [
	 *		{
	 *			"created": "2015-12-22 12:28:37.0",
	 *			"creater": "2015-12-22 12:28:37",
	 *			"modified": "2015-12-22 12:28:37.0",
	 *			"modifier": "UploadProductJob",
	 *			"seq": 9,
	 *			"channelId": "",
	 *			"catId": "1",
	 *			"cartId": 23,
	 *			"groupId": null,
	 *			"groupName": null,
	 *			"productId": 1,
	 *			"productName": "",
	 *			"promotionId": null,
	 *			"promotionName": null,
	 *			"model": "model-aa-code-1",
	 *			"code": "code-1",
	 *			"sku": "",
	 *			"errorTypeId": 1,
	 *			"errorCode": "400",
	 *			"errMsg": "参数无效;材质成分属性值错误：存在重复的材质名称：三醋酯纤维(三醋纤);",
	 *			"status": false
	 *		}
	 *		],
	 *		"code": "0",
	 *		"message": null,
	 *		"totalCount": 12,
	 *		"class": "com.voyageone.web2.sdk.api.response.BusinessLogGetResponse"
	 * }
	 * @apiErrorExample  错误示例
	 * {
	 *	"ApiException":{
	 *		"errCode": "500",
	 *		"errMsg": "系统内部异常",
	 *		"url": "http://api.voyageone.com/cms/500.html"
	 *	}
	 * }
	 * @apiErrorExample 错误编码
	 * 500:服务器内部错误
	 * 501:内部业务错误，请联系聚美开发人员
	 * 502:认证失败
	 * 503:参数错误，不能为空
	 * 504:结束时间UNIX时间戳不能为空，且修改后的时间必须大于系统当前时间
	 * 505:无权操作
	 */
	@RequestMapping("selectByCondtion")
	public PromotionsGetResponse selectByCondition(
			@RequestBody PromotionsGetRequest promotionGetRequest) {
		return promotionService.selectByCondition(promotionGetRequest);
	}

	/**
	 * @api {post} /promotion/deleteById 删除promotion
	 * @apiName deletePromotionById
	 * @apiDescription 根据id删除promotion
	 * @apiGroup promotion
	 * @apiVersion 0.0.1
	 * @apiPermission 认证商户
	 * @apiParam (系统级参数) {String} token api_token
	 * @apiParam (应用级参数) {Integer} promotionId promotionId
	 * @apiSuccess (返回字段) {String} code code
	 * @apiSuccess (返回字段) {Integer} modifiedCount modifiedCount
	 * @apiSuccess (返回字段) {Integer} matchedCount matchedCount
	 * @apiSuccess (返回字段) {Integer} insertedCount insertedCount
	 * @apiSuccess (返回字段) {Integer} removedCount removedCount
	 * @apiSuccess (返回字段) {String} message message
	 * @apiSuccess (返回字段) {String} class classpath
	 * @apiSuccessExample Success Response
	 * {
	 *  "code": "0",
	 *  "modifiedCount": 0,
	 *  "matchedCount": 0,
	 *  "message": null,
	 *  "class": "com.voyageone.web2.sdk.api.response.PromotionsPutResponse",
	 *  "insertedCount": 1,
	 *  "removedCount": 0
	 * }
	 * @apiErrorExample  错误示例
	 * {
	 *	"ApiException":{
	 *		"errCode": "500",
	 *		"errMsg": "系统内部异常",
	 *		"url": "http://api.voyageone.com/cms/500.html"
	 *	}
	 * }
	 * @apiErrorExample 错误编码
	 * 500:服务器内部错误
	 * 501:内部业务错误，请联系聚美开发人员
	 * 502:认证失败
	 * 503:参数错误，不能为空
	 * 504:结束时间UNIX时间戳不能为空，且修改后的时间必须大于系统当前时间
	 * 505:无权操作
	 */
	@RequestMapping("deleteById")
	public PromotionsPutResponse deleteById(
			@RequestBody PromotionDeleteRequest promotionDeleteRequest) {
		return promotionService.deleteById(promotionDeleteRequest);
	}

}
