package com.voyageone.web2.cms.views.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsMtCategoryTreeAllBean;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedSetCategoryMQMessageBody;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author JiangJusheng
 * @version 2.0.0, 2016/04/06
 */
@RestController
@RequestMapping( value = CmsUrlConstants.SEARCH.FEED.ROOT )
public class CmsFeedSearchController extends CmsController {

    @Autowired
    private CmsFeedSearchService searchService;

    @Autowired
    private CmsBtExportTaskService cmsBtExportTaskService;

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    CmsMqSenderService cmsMqSenderService;

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
    public AjaxResponse init(@RequestBody String channelId) throws Exception {
        CmsSessionBean cmsSession = getCmsSession();
        UserSessionBean userInfo = getUser();
        return success(searchService.getMasterData(userInfo, channelId, cmsSession, getLang()));
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

    /**
     * @api {post} /cms/search/feed/updateFeedStatus 2.3 批量更新FEED状态信息
     * @apiName updateFeedStatus
     * @apiDescription 批量更新FEED状态信息
     * @apiGroup search
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String[]} selList 已选择的feed list
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  批量更新FEED状态信息
     * @apiExample 使用表
     *  使用mongo:cms_bt_feed_info_cxxx表
     * @apiSampleRequest off
     */
    @RequestMapping(CmsUrlConstants.SEARCH.FEED.UPDATE)
    public AjaxResponse updateFeedStatus(@RequestBody Map params) {

        int cnt = 0;
        Boolean isAll = (Boolean) params.get("isAll");
        Integer status = (Integer) params.get("status");
        Map<String, Object> searchValue = (Map<String, Object>) params.get("searchInfo");
        String channelId = getUser().getSelChannelId();
        if(searchValue != null){
            channelId = searchValue.get("orgChaId")==null?getUser().getSelChannelId():searchValue.get("orgChaId").toString();
        }
        if(isAll == null || !isAll) {
            List selList = (List) params.get("selList");
            if (selList == null || selList.isEmpty()) {
                throw new BusinessException("请至少选择一个Feed.");
            }
            searchService.updateFeedStatus(selList,status, getUser(),channelId);
        }else{


            searchService.updateFeedStatus(searchValue, status, getUser(),channelId);
        }
        // 返回结果信息
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.SEARCH.FEED.EXPORT)
    public  AjaxResponse export(@RequestBody CmsBtExportTaskModel params) {
        params.setChannelId(getUser().getSelChannelId());
        params.setModifier(getUser().getUserName());
        params.setCreater(getUser().getUserName());
        params.setCreated(new Date());
        params.setTaskType(CmsBtExportTaskService.FEED);
        params.setStatus(0);
        return success(searchService.export(getUser().getSelChannelId(), params, getUser().getUserName()));
    }

    @RequestMapping(CmsUrlConstants.SEARCH.FEED.EXPORTSEARCH)
    public AjaxResponse exportSearch(@RequestBody Map<String,Object> params){
        Map<String, Object> resultBean = new HashMap<String, Object>();
        UserSessionBean userInfo = getUser();
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        String channelId = params.get("orgChaId") == null ? userInfo.getSelChannelId() : params.get("orgChaId").toString();
        resultBean.put("exportList", cmsBtExportTaskService.getExportTaskByUser(channelId, CmsBtExportTaskService.FEED, userInfo.getUserName(), (pageNum - 1) * pageSize, pageSize));
        resultBean.put("exportListTotal", cmsBtExportTaskService.getExportTaskByUserCnt(channelId, CmsBtExportTaskService.FEED, userInfo.getUserName()));

        // 返回feed信息
        return success(resultBean);
    }

    @RequestMapping(CmsUrlConstants.SEARCH.FEED.DOWNLOAD)
    public ResponseEntity<byte[]> download(@RequestParam String fileName){
        return genResponseEntityFromFile(fileName, CmsBtExportTaskService.savePath + fileName);
    }

    // 单商品设主类目
    @RequestMapping(CmsUrlConstants.SEARCH.FEED.UPDATE_MAIN_CATEGORY)
    public AjaxResponse updateMainCategory(@RequestBody MainCategoryBean params){
        return success(feedInfoService.updateMainCategory(getUser().getSelChannelId(), params.code, params.mainCategoryInfo, getUser().getUserName()));
    }

    // 批量设主类目
    @RequestMapping(CmsUrlConstants.SEARCH.FEED.BATCH_UPDATE_MAIN_CATEGORY)
    public AjaxResponse batchUpdateMainCategory(@RequestBody batchUpdateCategoryBean batchUpdateCategory){

        int cnt = 0;
        Boolean isAll = batchUpdateCategory.isAll;
        Map<String, Object> searchValue = batchUpdateCategory.searchInfo;
        String channelId = getUser().getSelChannelId();
        if(searchValue != null){
            channelId = searchValue.get("orgChaId")==null?getUser().getSelChannelId():searchValue.get("orgChaId").toString();
        }
        List<String> selList;
        if(isAll == null || !isAll) {
            selList = batchUpdateCategory.selList.stream().map(item->item.get("code")).collect(Collectors.toList());
            if (selList == null || selList.isEmpty()) {
                throw new BusinessException("请至少选择一个Feed.");
            }
            sendBatchUpdateMainCategory(channelId, selList, batchUpdateCategory.mainCategoryInfo,getUser().getUserName());
        }else{
            long feedListTotal = searchService.getFeedCnt(searchValue, getUser());
            Long pageCnt = feedListTotal / 100 + (feedListTotal % 100 > 0 ? 1 : 0);
            searchValue.put("pageSize",100);
            for(long i=0;i<pageCnt;i++){
                searchValue.put("pageNum",i+1);
                List<CmsBtFeedInfoModel> feedList = searchService.getFeedList(searchValue, getUser());
                if(ListUtils.notNull(feedList)){
                    selList = feedList.stream().map(CmsBtFeedInfoModel::getCode).collect(Collectors.toList());
                    sendBatchUpdateMainCategory(channelId, selList, batchUpdateCategory.mainCategoryInfo,getUser().getUserName());
                }
            }
        }
        // 返回结果信息
        return success(null);
    }

    private void sendBatchUpdateMainCategory(String channelId, List<String> codes, CmsMtCategoryTreeAllBean cmsMtCategoryTreeAllBean, String sender){
        CmsFeedSetCategoryMQMessageBody cmsFeedSetCategoryMQMessageBody = new CmsFeedSetCategoryMQMessageBody();
        cmsFeedSetCategoryMQMessageBody.setChannelId(channelId);
        cmsFeedSetCategoryMQMessageBody.setMainCategoryInfo(cmsMtCategoryTreeAllBean);
        cmsFeedSetCategoryMQMessageBody.setCodeList(codes);
        cmsFeedSetCategoryMQMessageBody.setSender(sender);
        cmsMqSenderService.sendMessage(cmsFeedSetCategoryMQMessageBody);

    }
    private static class MainCategoryBean {
        @JsonProperty("code")
        String code;
        @JsonProperty("mainCategoryInfo")
        CmsMtCategoryTreeAllBean mainCategoryInfo;
    }

    private static class batchUpdateCategoryBean{
        @JsonProperty("searchInfo")
        Map<String, Object> searchInfo;
        @JsonProperty("isAll")
        Boolean isAll;
        @JsonProperty("selList")
        List<Map<String, String>> selList;
        @JsonProperty("mainCategoryInfo")
        CmsMtCategoryTreeAllBean mainCategoryInfo;

    }
}
