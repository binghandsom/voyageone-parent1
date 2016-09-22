package com.voyageone.task2.cms.service.feed.status;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.daoext.cms.CmsFeedLiveSkuDaoExt;
import com.voyageone.service.model.cms.CmsFeedLiveSkuKey;
import com.voyageone.service.model.cms.CmsFeedLiveSkuModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author james.li on 2016/9/22.
 * @version 2.0.0
 */
public abstract class FeedStatusCheckBaseService extends BaseTaskService {

    protected abstract List<CmsFeedLiveSkuModel> getSkuList() throws Exception;

    protected abstract ChannelConfigEnums.Channel getChannel();

    @Autowired
    CmsFeedLiveSkuDaoExt cmsFeedLiveSkuDaoExt;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        List<CmsFeedLiveSkuModel> skus = getSkuList();
        List<List<CmsFeedLiveSkuModel>> skuList = CommonUtil.splitList(skus, 1000);
        $info("删除channel=" + getChannel().getId() + "的数据");
        deleteData();
        $info("插入channel=" + getChannel().getId() + "的数据。共" + skus.size() + "条");
        skuList.forEach(this::insertData);
    }

    private void insertData(List<CmsFeedLiveSkuModel> skus) {
        cmsFeedLiveSkuDaoExt.insertList(skus);
    }

    private void deleteData() {
        CmsFeedLiveSkuKey key = new CmsFeedLiveSkuKey();
        key.setChannelId(getChannel().getId());
        cmsFeedLiveSkuDaoExt.delete(key);
    }

}
