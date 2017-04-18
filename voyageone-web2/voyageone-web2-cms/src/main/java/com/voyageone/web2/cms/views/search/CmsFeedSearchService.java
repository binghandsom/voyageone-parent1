package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.tools.common.CmsMasterBrandMappingService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.FeedExportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.channel.CmsFeedCustPropService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author JiangJusheng
 * @version 2.0.0, 2016/04/06
 */
@Service
public class CmsFeedSearchService extends BaseViewService {

    // 查询产品信息时的缺省输出列
    private final String searchItems = "{'category':1,'code':1,'name':1,'model':1,'color':1,'origin':1,'brand':1,'image':1,'productType':1,'sizeType':1,'shortDescription':1,'longDescription':1,'skus':1,'attribute':1,'updFlg':1,'qty':1,'updMessage':1,'created':1,'modified':1,'lastReceivedOn':1}";
    // 查询产品信息时的缺省排序条件
    private final String sortItems = "{'category':1,'code':1}";
    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private CmsFeedCustPropService cmsFeedCustPropService;

    //@Autowired
    //private MqSender sender;
    @Autowired
    private CmsBtExportTaskService cmsBtExportTaskService;
    @Autowired
    private CmsMasterBrandMappingService cmsMasterBrandMappingService;
    @Autowired
    private CmsMtChannelValuesService cmsMtChannelValuesService;
    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    /**
     * 获取检索页面初始化的master data数据
     *
     * @param userInfo
     * @return
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, String channelId, CmsSessionBean cmsSession, String language) throws IOException {
        Map<String, Object> masterData = new HashMap<>();

        if(StringUtil.isEmpty(channelId)) channelId = userInfo.getSelChannelId();

        // 获取compare type
        masterData.put("compareTypeList", TypeConfigEnums.MastType.compareType.getList(language));


        // 获取brand list
        masterData.put("brandList", cmsMtChannelValuesService.getCmsMtChannelValuesListByChannelIdType(channelId, CmsMtChannelValuesService.BRAND));

//        masterData.put("masterBrandList",cmsMasterBrandMappingService.getMasterBrandListByChannelId(channelId));
        // 获取category list
        List<CmsMtFeedCategoryTreeModel> feedCatList = cmsFeedCustPropService.getCategoryList(channelId);
        if (!feedCatList.isEmpty()) {
            feedCatList.remove(0);
        }
        List<Integer> delFlgList = new ArrayList<Integer>();

        for (int i = 0, leng = feedCatList.size(); i < leng; i++) {
            if (feedCatList.get(i).getIsParent() == 1) {
                // 非子节点
                delFlgList.add(i);
            }
        }
        for (int leng = delFlgList.size(), i = leng - 1; i >= 0; i--) {
            feedCatList.remove(delFlgList.get(i).intValue());
        }
        masterData.put("sortList", TypeConfigEnums.MastType.feedSearchSort.getList(language));
        masterData.put("categoryList", feedCatList);
        masterData.put("productType", cmsMtChannelValuesService.getCmsMtChannelValuesListByChannelIdType(channelId, CmsMtChannelValuesService.PRODUCT_TYPE));
        masterData.put("sizeType", cmsMtChannelValuesService.getCmsMtChannelValuesListByChannelIdType(channelId, CmsMtChannelValuesService.SIZE_TYPE));

        // 判断是否是minimall/usjoi用户
        boolean isMiniMall = Channels.isUsJoi(userInfo.getSelChannelId());
        masterData.put("isminimall", isMiniMall ? 1 : 0);
        if (isMiniMall) {
            List<TypeChannelBean> typeChannelBeenList = TypeChannels.getTypeChannelBeansByTypeValueLang(Constants.comMtTypeChannel.SKU_CARTS_53, userInfo.getSelChannelId(), "cn");
            if (typeChannelBeenList == null || typeChannelBeenList.isEmpty()) {
                $warn("高级检索:getMasterData 未取得供应商列表(Synship.com_mt_value_channel表中未配置) channelid=" + userInfo.getSelChannelId());
            } else {
                List<OrderChannelBean> channelBeanList = new ArrayList<>();
                for (TypeChannelBean typeBean : typeChannelBeenList) {
                    OrderChannelBean channelBean = Channels.getChannel(typeBean.getChannel_id());
                    if (channelBean != null) {
                        channelBeanList.add(channelBean);
                    } else {
                        $warn("高级检索:getMasterData 取得供应商列表 channel不存在 channelid=" + typeBean.getChannel_id());
                    }
                }
                if (channelBeanList.isEmpty()) {
                    $warn("高级检索:getMasterData 取得供应商列表 channel不存在 " + channelBeanList.toString());
                } else {
                    masterData.put("channelList", channelBeanList);
                }
            }
        }
        return masterData;
    }

    /**
     * 获取当前页的product列表Cnt
     *
     * @param searchValue
     * @param userInfo
     * @return
     */
    public long getFeedCnt(Map<String, Object> searchValue, UserSessionBean userInfo) {
        String channelId = searchValue.get("orgChaId") == null ? userInfo.getSelChannelId() : searchValue.get("orgChaId").toString();
        return feedInfoService.getCnt(channelId, searchValue);
    }

    /**
     * 获取当前页的FEED信息
     *
     * @param searchValue
     * @param userInfo
     * @return
     */
    public List<CmsBtFeedInfoModel> getFeedList(Map<String, Object> searchValue, UserSessionBean userInfo) {
        String channelId = searchValue.get("orgChaId") == null ? userInfo.getSelChannelId() : searchValue.get("orgChaId").toString();
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery(feedInfoService.getSearchQuery(channelId, searchValue));
        queryObject.setProjection(searchItems);
        queryObject.setSort(setSortValue(searchValue));
        int pageNum = (Integer) searchValue.get("pageNum");
        int pageSize = (Integer) searchValue.get("pageSize");
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);
        return feedInfoService.getList(channelId, queryObject);
    }


    // 批量更新FEED状态信息
    public void updateFeedStatus(List<Map> params, Integer status, UserSessionBean userInfo, String channelId) {
        List<String> codeList = new ArrayList<>(params.size());
        params.forEach(para -> codeList.add((String) para.get("code")));
        Map<String, Object> paraMap1 = new HashMap<>(1);
        paraMap1.put("$in", codeList);
        HashMap<String, Object> paraMap2 = new HashMap<>();
        paraMap2.put("code", paraMap1);

        Map<String, Object> paraStatusMap = new HashMap<>(1);
        if (status == CmsConstants.FeedUpdFlgStatus.Pending) {
            paraStatusMap.put("$nin", new ArrayList<>(Arrays.asList(CmsConstants.FeedUpdFlgStatus.FeedErr, CmsConstants.FeedUpdFlgStatus.FeedBlackList)));
        } else if (status == CmsConstants.FeedUpdFlgStatus.NotIMport) {
            paraStatusMap.put("$nin", new ArrayList<>(Arrays.asList(CmsConstants.FeedUpdFlgStatus.NotIMport, CmsConstants.FeedUpdFlgStatus.Succeed, CmsConstants.FeedUpdFlgStatus.FeedErr, CmsConstants.FeedUpdFlgStatus.FeedBlackList)));
        }
        paraMap2.put("updFlg", paraStatusMap);


        HashMap<String, Object> valueMap = new HashMap<>(1);
        valueMap.put("updFlg", status);
        valueMap.put("modified", DateTimeUtil.getNowTimeStamp());
        valueMap.put("modifier", userInfo.getUserName());

        feedInfoService.updateFeedInfo(channelId == null ? userInfo.getSelChannelId() : channelId, paraMap2, valueMap);
    }

    public void updateFeedStatus(Map<String, Object> searchValue, Integer status, UserSessionBean userInfo, String channelId) {

        List<Integer> searchStatus = null;
        if (searchValue.get("status") != null) {
            searchStatus = (List<Integer>) searchValue.get("status");
        } else {
            if (status == CmsConstants.FeedUpdFlgStatus.Pending) {
                searchValue.put("ninStatus", new ArrayList<>(Arrays.asList(CmsConstants.FeedUpdFlgStatus.FeedErr, CmsConstants.FeedUpdFlgStatus.FeedBlackList)));
            } else if (status == CmsConstants.FeedUpdFlgStatus.NotIMport) {
                searchValue.put("ninStatus", new ArrayList<>(Arrays.asList(CmsConstants.FeedUpdFlgStatus.NotIMport, CmsConstants.FeedUpdFlgStatus.Succeed, CmsConstants.FeedUpdFlgStatus.FeedErr, CmsConstants.FeedUpdFlgStatus.FeedBlackList)));
            }
        }
        if (status == CmsConstants.FeedUpdFlgStatus.Pending) {
            if (searchStatus != null && searchStatus.contains(CmsConstants.FeedUpdFlgStatus.FeedErr)) {
                throw new BusinessException("Feed数据异常错误的数据是不能导入主数据的，请重新选择状态");
            }
            if (searchStatus != null && searchStatus.contains(CmsConstants.FeedUpdFlgStatus.FeedBlackList)) {
                throw new BusinessException("Feed品牌黑免单的数据是不能导入主数据的，请重新选择状态");
            }
        } else if (status == CmsConstants.FeedUpdFlgStatus.NotIMport) {
            if (searchStatus != null && searchStatus.contains(CmsConstants.FeedUpdFlgStatus.Succeed)) {
                throw new BusinessException("导入成功是不能设为不导入的，请重新选择状态");
            }
            if (searchStatus != null && searchStatus.contains(CmsConstants.FeedUpdFlgStatus.FeedErr)) {
                throw new BusinessException("Feed数据异常错误的数据是不能设为不导入的，请重新选择状态");
            }
            if (searchStatus != null && searchStatus.contains(CmsConstants.FeedUpdFlgStatus.FeedBlackList)) {
                throw new BusinessException("Feed品牌黑免单的数据是不能设为不导入的，请重新选择状态");
            }
        }
        if(channelId == null) channelId = userInfo.getSelChannelId();
        String searchQuery = feedInfoService.getSearchQuery(channelId, searchValue);
        feedInfoService.updateAllUpdFlg(channelId, searchQuery, status, userInfo.getUserName());
    }

    /**
     * 获取排序规则
     */
    public String setSortValue(Map<String, Object> searchValue) {
        StringBuilder result = new StringBuilder();

        // 获取排序字段1
        if (searchValue.get("sortOneName") != null) {
            if (!StringUtil.isEmpty(searchValue.get("sortOneName").toString())) {
                if (searchValue.get("sortOneType") == null || StringUtil.isEmpty(searchValue.get("sortOneType").toString())) {
                    searchValue.put("sortOneType", -1);
                }
                result.append(MongoUtils.splicingValue("" + searchValue.get("sortOneName"), Integer.valueOf(searchValue.get("sortOneType").toString())));
                result.append(",");
            }
        }

        // 获取排序字段2
        if (searchValue.get("sortTwoName") != null) {
            if (!StringUtil.isEmpty(searchValue.get("sortTwoName").toString())) {
                if (searchValue.get("sortTwoType") == null || StringUtil.isEmpty(searchValue.get("sortTwoType").toString())) {
                    searchValue.put("sortTwoType", -1);
                }
                result.append(MongoUtils.splicingValue("" + searchValue.get("sortTwoName"), Integer.valueOf(searchValue.get("sortTwoType").toString())));
                result.append(",");
            }
        }

        // 获取排序字段3
        if (searchValue.get("sortThreeName") != null) {
            if (!StringUtil.isEmpty(searchValue.get("sortThreeName").toString())) {
                if (searchValue.get("sortThreeType") == null || StringUtil.isEmpty(searchValue.get("sortThreeType").toString())) {
                    searchValue.put("sortThreeType", -1);
                }
                result.append(MongoUtils.splicingValue("" + searchValue.get("sortThreeName"), Integer.valueOf(searchValue.get("sortThreeType").toString())));
                result.append(",");
            }
        }
        return result.toString().length() > 0 ? "{" + result.toString().substring(0, result.toString().length() - 1) + "}" : sortItems;
    }

    public CmsBtExportTaskModel export(String channelId, CmsBtExportTaskModel cmsBtExportTaskModel, String userName) {
        if (cmsBtExportTaskService.checkExportTaskByUser(channelId, CmsBtExportTaskService.FEED, userName) == 0) {
            cmsBtExportTaskService.add(cmsBtExportTaskModel);

            FeedExportMQMessageBody feedExportMQMessageBody = new FeedExportMQMessageBody();
            feedExportMQMessageBody.setChannelId(channelId);
            feedExportMQMessageBody.setCmsBtExportTaskId(cmsBtExportTaskModel.getId());
            feedExportMQMessageBody.setSender(userName);
            cmsMqSenderService.sendMessage(feedExportMQMessageBody);
            return cmsBtExportTaskModel;
        } else {
            throw new BusinessException("你已经有一个任务还没有执行完毕。请稍后再导出");
        }
    }
}
