package com.voyageone.task2.cms.usa.job;

import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.components.solr.query.SimpleQueryBean;
import com.voyageone.components.solr.service.CmsProductSearchService;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumDataAmountType;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumFeedSum;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumPlatformInfoSum;
import com.voyageone.service.dao.cms.CmsBtDataAmountDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.impl.cms.TypeChannelsService;
import com.voyageone.service.model.cms.CmsBtDataAmountModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 美国CMS2 data amount 相关Job
 *
 * @Author rex.wu
 * @Create 2017-07-11 16:08
 */
@Service
public class UsaCmsDataAmountJobService extends BaseCronTaskService {

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;
    @Autowired
    private CmsBtDataAmountDao cmsBtDataAmountDao;
    @Autowired
    private CmsProductSearchService cmsProductSearchService;
    @Autowired
    private TypeChannelsService typeChannelsService;

    /**
     * 获取子系统
     */
    @Override
    protected String getTaskName() {
        return "UsaCmsDataAmountJob";
    }

    /**
     * 获取任务名称
     */
    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        String channelId = ChannelConfigEnums.Channel.SN.getId();
        $info("UsaCmsDataAmountJob:   begin");

        // 待统计的Feed状态
        List<CmsConstants.UsaFeedStatus> feedStatusList = new ArrayList<>();
        feedStatusList.add(CmsConstants.UsaFeedStatus.New);
        feedStatusList.add(CmsConstants.UsaFeedStatus.Pending);
        feedStatusList.add(CmsConstants.UsaFeedStatus.Ready);
        // 待统计的Product平台状态
        List<CmsConstants.PlatformStatus> platformStatusList = new ArrayList<>();
        platformStatusList.add(CmsConstants.PlatformStatus.Pending);
        platformStatusList.add(CmsConstants.PlatformStatus.OnSale);
        platformStatusList.add(CmsConstants.PlatformStatus.InStock);

        // 首先汇总Feed信息, 各状态<New,Pending,Ready> items count
        List<JongoAggregate> aggregateList = new ArrayList<>();
        aggregateList.add(new JongoAggregate("{$match:{\"channelId\":#,\"status\":{$in:#}}}", channelId, feedStatusList));
        aggregateList.add(new JongoAggregate("{$group:{_id:\"$status\",total:{$sum:1}}}"));
        List<Map<String, Object>> feedItemMap = cmsBtFeedInfoDao.aggregateToMap(channelId, aggregateList);
        List<CmsConstants.UsaFeedStatus> existFeedStatusList = new ArrayList<>();
        for (Map<String, Object> map : feedItemMap) {
            String status = (String) map.get("_id");
            Integer total = (Integer) map.get("total");

            CmsConstants.UsaFeedStatus feedStatus = CmsConstants.UsaFeedStatus.valueOf(status);
            EnumFeedSum feedSum = this.getEnumFeedSum(feedStatus);

            if (feedSum == null) continue;
            existFeedStatusList.add(feedStatus);

            // insert or update
            this.saveOrUpdateDataAmount(channelId, feedSum, String.valueOf(total));
        }

        // MongoDB中没有对应状态的Feed,也要补上对应记录
        feedStatusList.removeAll(existFeedStatusList);
        if (!feedStatusList.isEmpty()) {
            for (CmsConstants.UsaFeedStatus feedStatus : feedStatusList) {
                EnumFeedSum feedSum = this.getEnumFeedSum(feedStatus);
                if (feedSum == null) continue;
                // insert or update
                this.saveOrUpdateDataAmount(channelId, feedSum, "0");
            }
        }

        // =========================================================================================
        // =================================统计美国各平台Product信息===================================
        // =========================================================================================
        List<TypeChannelBean> cartsBeanList = typeChannelsService.getOnlyUsPlatformTypeList(channelId, "en");
        if (CollectionUtils.isNotEmpty(cartsBeanList)) {
            for (TypeChannelBean usaCart : cartsBeanList) {
                /*List<JongoAggregate> productAggregateList = new ArrayList<>();
                productAggregateList.add(new JongoAggregate(
                        String.format("{$match:{\"channelId\":#,\"usPlatforms.P%s\":{$exists:true},\"usPlatforms.P%s.status\":{$exists:true}}}", usaCart.getValue(), usaCart.getValue()),
                        channelId));
                productAggregateList.add(new JongoAggregate(String.format("{$group:{_id:\"$usPlatforms.P%s.status\",total:{$sum:1}}}", usaCart.getValue())));
                List<Map<String, Object>> productItemsMap = cmsBtProductDao.aggregateToMap(channelId, productAggregateList);
                for (Map<String, Object> map : productItemsMap) {
                    String pStatus = (String) map.get("_id");
                    Integer pStatusCount = (Integer) map.get("total");

                    System.out.println(String.format("平台%s状态: %s, 总条数: %s", usaCart.getName(), pStatus, pStatusCount));

                    CmsConstants.ProductStatus productStatus = CmsConstants.ProductStatus.valueOf(pStatus);
                    // 找不到平台状态枚举值则直接跳过
                    if (productStatus == null) {
                        continue;
                    }

                    // insert or update 各平台各状态商品的统计数
                    CmsBtDataAmountModel platformAmountQueryModel = new CmsBtDataAmountModel();
                    platformAmountQueryModel.setChannelId(channelId);
                    platformAmountQueryModel.setCartId(Integer.valueOf(usaCart.getValue()));
                    platformAmountQueryModel.setAmountName(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getAmountName(), pStatus));

                    CmsBtDataAmountModel platformAmountModel = cmsBtDataAmountDao.selectOne(platformAmountQueryModel);
                    if (platformAmountModel == null) {
                        platformAmountModel = new CmsBtDataAmountModel();
                        platformAmountModel.setChannelId(channelId);
                        platformAmountModel.setCartId(Integer.valueOf(usaCart.getValue()));
                        platformAmountModel.setAmountName(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getAmountName(), pStatus));
                        platformAmountModel.setAmountVal(String.valueOf(pStatusCount));
                        platformAmountModel.setComment(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getComment(), usaCart.getName()));
                        platformAmountModel.setDataAmountTypeId(EnumDataAmountType.UsaPlatformSum.getId());
                        platformAmountModel.setCreater(getTaskName());
                        platformAmountModel.setCreated(new Date());
                        cmsBtDataAmountDao.insert(platformAmountModel);
                    } else {
                        CmsBtDataAmountModel updateModel = new CmsBtDataAmountModel();
                        updateModel.setId(platformAmountModel.getId());
                        updateModel.setAmountName(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getAmountName(), pStatus));
                        updateModel.setAmountVal(String.valueOf(pStatusCount));
                        updateModel.setComment(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getComment(), usaCart.getName()));
                        updateModel.setModifier(getTaskName());
                        updateModel.setModified(new Date());
                        cmsBtDataAmountDao.update(updateModel);
                    }
                }*/

                // 统计各状态Product数目
                for (CmsConstants.PlatformStatus platformStatus : platformStatusList) {
                    Criteria criteria = new Criteria("productChannel").is(channelId);
                    criteria = criteria.and("P" + usaCart.getValue() + "_pStatus").is(platformStatus.name());
                    long total = cmsProductSearchService.countByQuery(new SimpleQueryBean(criteria));

                    // insert or update 各平台各状态商品的统计数
                    CmsBtDataAmountModel platformAmountQueryModel = new CmsBtDataAmountModel();
                    platformAmountQueryModel.setChannelId(channelId);
                    platformAmountQueryModel.setCartId(Integer.valueOf(usaCart.getValue()));
                    platformAmountQueryModel.setAmountName(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getAmountName(), platformStatus.name()));

                    CmsBtDataAmountModel platformAmountModel = cmsBtDataAmountDao.selectOne(platformAmountQueryModel);
                    if (platformAmountModel == null) {
                        platformAmountModel = new CmsBtDataAmountModel();
                        platformAmountModel.setChannelId(channelId);
                        platformAmountModel.setCartId(Integer.valueOf(usaCart.getValue()));
                        platformAmountModel.setAmountName(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getAmountName(), platformStatus.name()));
                        platformAmountModel.setAmountVal(String.valueOf(total));
                        platformAmountModel.setComment(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getComment(), usaCart.getName()));
                        platformAmountModel.setDataAmountTypeId(EnumDataAmountType.UsaPlatformSum.getId());
                        platformAmountModel.setLinkUrl(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getLinkUrl());
                        platformAmountModel.setLinkParameter(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getLinkParameter(), usaCart.getValue(), platformStatus.name()));
                        platformAmountModel.setCreater(getTaskName());
                        platformAmountModel.setCreated(new Date());
                        cmsBtDataAmountDao.insert(platformAmountModel);
                    } else {
                        CmsBtDataAmountModel updateModel = new CmsBtDataAmountModel();
                        updateModel.setId(platformAmountModel.getId());
                        updateModel.setAmountName(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getAmountName(), platformStatus.name()));
                        updateModel.setAmountVal(String.valueOf(total));
                        updateModel.setComment(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getComment(), usaCart.getName()));
                        updateModel.setLinkUrl(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getLinkUrl());
                        updateModel.setLinkParameter(String.format(EnumPlatformInfoSum.USA_CMS_PLATFORMS_AMOUNT.getLinkParameter(), usaCart.getValue(), platformStatus.name()));
                        updateModel.setModifier(getTaskName());
                        updateModel.setModified(new Date());
                        cmsBtDataAmountDao.update(updateModel);
                    }
                }
            }
        }

        $info("UsaCmsDataAmountJob:   end");

    }

    private EnumFeedSum getEnumFeedSum(CmsConstants.UsaFeedStatus feedStatus) {
        EnumFeedSum feedSum = null;
        switch (feedStatus) {
            case New:
                feedSum = EnumFeedSum.USA_CMS_FEED_STATUS_NEW;
                break;
            case Pending:
                feedSum = EnumFeedSum.USA_CMS_FEED_STATUS_PENDING;
                break;
            case Ready:
                feedSum = EnumFeedSum.USA_CMS_FEED_STATUS_READY;
                break;
            default:
                break;
        }
        return feedSum;
    }

    /**
     * Insert or Update Feed统计记录
     *
     * @param channelId 渠道ID
     * @param feedSum   分类
     * @param amountVal 统计值
     */
    private void saveOrUpdateDataAmount(String channelId, EnumFeedSum feedSum, String amountVal) {
        CmsBtDataAmountModel dataAmountModel = new CmsBtDataAmountModel();
        CmsBtDataAmountModel queryModel = new CmsBtDataAmountModel();
        queryModel.setChannelId(channelId);
        queryModel.setAmountName(feedSum.getAmountName());
        CmsBtDataAmountModel targetModel = cmsBtDataAmountDao.selectOne(queryModel);
        if (targetModel == null) {
            dataAmountModel.setChannelId(channelId);
            dataAmountModel.setAmountName(feedSum.getAmountName());
            dataAmountModel.setAmountVal(amountVal);
            dataAmountModel.setComment(feedSum.getComment());
            dataAmountModel.setCartId(0);
            dataAmountModel.setDataAmountTypeId(EnumDataAmountType.UsaFeedSum.getId());
            dataAmountModel.setLinkUrl(feedSum.getLinkUrl());
            dataAmountModel.setLinkParameter(feedSum.getLinkParameter());
            dataAmountModel.setCreated(new Date());
            dataAmountModel.setCreater(getTaskName());
            cmsBtDataAmountDao.insert(dataAmountModel);
        } else {
            dataAmountModel.setId(targetModel.getId());
            dataAmountModel.setAmountVal(amountVal);
            dataAmountModel.setComment(feedSum.getComment());
            dataAmountModel.setLinkUrl(feedSum.getLinkUrl());
            dataAmountModel.setLinkParameter(feedSum.getLinkParameter());
            dataAmountModel.setModified(new Date());
            dataAmountModel.setModifier(getTaskName());
            cmsBtDataAmountDao.update(dataAmountModel);
        }
    }

}
