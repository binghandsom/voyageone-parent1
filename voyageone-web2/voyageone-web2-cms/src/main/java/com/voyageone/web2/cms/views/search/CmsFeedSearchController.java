package com.voyageone.web2.cms.views.search;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author JiangJusheng
 * @version 2.0.0, 2016/04/06
 */
@RestController
@RequestMapping( value = CmsUrlConstants.SEARCH.FEED.ROOT,  method = RequestMethod.POST )
public class CmsFeedSearchController extends CmsController {

    @Autowired
    private CmsFeedSearchService searchService;

    /**
     * @api {post} /cms/search/feed/init 2.1 初始化FEED检索画面时,获取master数据
     * @apiName init
     * @apiDescription 初始化FEED检索画面时,获取master数据
     * @apiGroup search
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object[]} categoryList 产品类目一览，没有数据时返回空数组（@see com.voyageone.cms.service.model.CmsMtCategoryTreeModel）
     * @apiSuccess (应用级返回字段) {Object[]} brandList 产品品牌一览，没有数据时返回空数组（@see com.voyageone.common.configs.beans.TypeChannelBean）
     * @apiSuccessExample 成功响应查询请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "categoryList": [ {"feed_prop_original":"a_b_c", "feed_prop_translation":"yourname" }...],
     *   "brandList": [ {"propId":"a_b_c", "propName":"yourname" }...]
     *  }
     * }
     * @apiExample  业务说明
     *  初始化FEED检索画面时,获取master数据,包括如下:
     *    产品类目一览，产品品牌一览
     * @apiExample 使用表
     *  使用mongo:cms_mt_category_tree表，Synship:com_mt_value_channel表
     * @apiSampleRequest off
     */
    @RequestMapping(CmsUrlConstants.SEARCH.FEED.INIT)
    public AjaxResponse init() throws Exception {
        CmsSessionBean cmsSession = getCmsSession();
        UserSessionBean userInfo = getUser();
        return success(searchService.getMasterData(userInfo, cmsSession, getLang()));
    }

    /**
     * @api {post} /cms/search/feed/search 2.2 检索FEED信息
     * @apiName search
     * @apiDescription 检索FEED信息
     * @apiGroup search
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {Integer} pageNum 当前查询页数，0为第一页
     * @apiParam (应用级参数) {Integer} pageSize 每页显示数据条数
     * @apiParam (应用级参数) {String} category 类目名称
     * @apiParam (应用级参数) {String} name 产品名称
     * @apiParam (应用级参数) {String} fuzzySearch 模糊查询项目(code,name,model,short_description,long_description)
     * @apiParam (应用级参数) {String} priceType 查询的价格类型(price_current,price_msrp,price_net,price_client_retail,price_client_msrp)
     * @apiParam (应用级参数) {String} priceValueSta 查询的价格区间下限
     * @apiParam (应用级参数) {String} priceValueEnd 查询的价格区间上限
     * @apiParam (应用级参数) {String} createTimeSta 产品创建时间区间下限
     * @apiParam (应用级参数) {String} createTimeEnd 产品创建时间区间上限
     * @apiParam (应用级参数) {String} updateTimeSta 产品更新时间区间下限
     * @apiParam (应用级参数) {String} updateTimeEnd 产品更新时间区间上限
     * @apiParam (应用级参数) {String} brand 产品品牌
     * @apiParam (应用级参数) {String} color 产品颜色
     * @apiParam (应用级参数) {String} productType 产品类型
     * @apiParam (应用级参数) {String} sizeType 产品尺寸类型
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object[]} feedList FEED信息，没有数据时返回空数组
     * @apiSuccess (应用级返回字段) {Integer} feedListTotal 符合查询条件的FEED信息总数
     * @apiSuccessExample 成功响应查询请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "feedList": [ {"feed_prop_original":"a_b_c", "feed_prop_translation":"yourname" }...],
     *   "feedListTotal": 31
     *  }
     * }
     * @apiExample  业务说明
     *  根据指定条件检索FEED信息
     * @apiExample 使用表
     *  使用mongo:cms_bt_feed_info_cxxx表
     * @apiSampleRequest off
     */
    @RequestMapping(CmsUrlConstants.SEARCH.FEED.SEARCH)
    public AjaxResponse search(@RequestBody Map params) {
        Map<String, Object> resultBean = new HashMap<String, Object>();
        UserSessionBean userInfo = getUser();

        // 获取feed列表
        List<CmsBtFeedInfoModel> feedList = searchService.getFeedList(params, userInfo);
        resultBean.put("feedList", feedList);
        long feedListTotal = searchService.getFeedCnt(params, userInfo);
        resultBean.put("feedListTotal", feedListTotal);

        // 返回feed信息
        return success(resultBean);
    }

}
