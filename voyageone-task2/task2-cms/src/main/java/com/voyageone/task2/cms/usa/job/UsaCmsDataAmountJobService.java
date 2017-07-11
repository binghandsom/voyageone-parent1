package com.voyageone.task2.cms.usa.job;

import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumDataAmountType;
import com.voyageone.service.bean.cms.CmsBtDataAmount.EnumFeedSum;
import com.voyageone.service.dao.cms.CmsBtDataAmountDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.model.cms.CmsBtDataAmountModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;

import org.jongo.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;
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

        // 首先汇总Feed信息, 各状态<New,Pending,Ready> items count
        List<JongoAggregate> aggregateList = new ArrayList<>();
        aggregateList.add(new JongoAggregate("{$match:{\"channelId\":#,\"status\":{$in:#}}}", channelId, feedStatusList));
        aggregateList.add(new JongoAggregate("{$group:{_id:\"$status\",total:{$sum:1}}}"));
        List<Map<String, Object>> feedItemMap = cmsBtFeedInfoDao.aggregateToMap(channelId, aggregateList);
        for (Map<String, Object> map : feedItemMap) {
            String status = (String) map.get("_id");
            Integer total = (Integer) map.get("total");

            CmsConstants.UsaFeedStatus feedStatus = CmsConstants.UsaFeedStatus.valueOf(status);
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

            if (feedSum == null) continue;

            // insert or update
            CmsBtDataAmountModel dataAmountModel = new CmsBtDataAmountModel();
            CmsBtDataAmountModel queryModel = new CmsBtDataAmountModel();
            queryModel.setChannelId(channelId);
            queryModel.setAmountName(feedSum.getAmountName());
            CmsBtDataAmountModel targetModel = cmsBtDataAmountDao.selectOne(queryModel);
            if (targetModel == null) {
                dataAmountModel.setChannelId(channelId);
                dataAmountModel.setAmountName(feedSum.getAmountName());
                dataAmountModel.setAmountVal(String.valueOf(total));
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
                dataAmountModel.setAmountVal(String.valueOf(total));
                dataAmountModel.setComment(feedSum.getComment());
                dataAmountModel.setLinkUrl(feedSum.getLinkUrl());
                dataAmountModel.setLinkParameter(feedSum.getLinkParameter());
                dataAmountModel.setModified(new Date());
                dataAmountModel.setModifier(getTaskName());
                cmsBtDataAmountDao.update(dataAmountModel);
            }
        }


        $info("UsaCmsDataAmountJob:   end");

    }
}
