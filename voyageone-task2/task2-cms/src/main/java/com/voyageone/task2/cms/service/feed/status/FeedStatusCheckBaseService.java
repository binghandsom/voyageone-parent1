package com.voyageone.task2.cms.service.feed.status;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.daoext.cms.CmsFeedLiveSkuDaoExt;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.feed.FeedSaleService;
import com.voyageone.service.model.cms.CmsFeedLiveSkuKey;
import com.voyageone.service.model.cms.CmsFeedLiveSkuModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author james.li on 2016/9/22.
 * @version 2.0.0
 */
public abstract class FeedStatusCheckBaseService extends BaseTaskService {

    protected abstract List<CmsFeedLiveSkuModel> getSkuList() throws Exception;

    protected abstract ChannelConfigEnums.Channel getChannel();

    @Autowired
    CmsFeedLiveSkuDaoExt cmsFeedLiveSkuDaoExt;

    @Autowired
    FeedInfoService feedInfoService;

    @Autowired
    FeedSaleService feedSaleService;

    final Integer pageSize = 200;

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

        long cnt = feedInfoService.getCnt(getChannel().getId(), new HashMap<>());
        long pageCnt = cnt / pageSize + (cnt % pageSize == 0 ? 0 : 1);
        JongoQuery queryObject = new JongoQuery();
        for (int pageNum = 1; pageNum <= pageCnt; pageNum++) {
            $info("导出第" + pageNum + "页");
            queryObject.setSkip((pageNum - 1) * pageSize);
            queryObject.setLimit(pageSize);
            List<CmsBtFeedInfoModel> cmsBtFeedInfoModels = feedInfoService.getList(getChannel().getId(), queryObject);
            cmsBtFeedInfoModels.forEach(this::checkSkuStatus);
        }
    }

    private void insertData(List<CmsFeedLiveSkuModel> skus) {
        cmsFeedLiveSkuDaoExt.insertList(skus);
    }

    private void deleteData() {
        CmsFeedLiveSkuKey key = new CmsFeedLiveSkuKey();
        key.setChannelId(getChannel().getId());
        cmsFeedLiveSkuDaoExt.delete(key);
    }

    private void checkSkuStatus(CmsBtFeedInfoModel cmsBtFeedInfoModel) {
        cmsBtFeedInfoModel.getSkus().forEach(sku -> {
            CmsFeedLiveSkuModel cmsFeedLiveSku = selectOne(sku.getSku());
            if (sku.getIsSale() == 0 && cmsFeedLiveSku != null) {
                $info(getChannel().getId()+ " " + sku.getClientSku() + " notSale -> sale");
                feedSaleService.sale(getChannel().getId(),sku.getClientSku(),cmsBtFeedInfoModel.getQty());
            } else if(sku.getIsSale() == 1 && cmsFeedLiveSku == null) {
                $info(getChannel().getId()+ " " + sku.getSku() + " sale ->notSale");
                feedSaleService.notSale(getChannel().getId(),sku.getClientSku());
            }
        });
    }

    private CmsFeedLiveSkuModel selectOne(String sku){
        Map<String,Object> param = new HashMap<>();
        param.put("channelId",getChannel().getId());
        param.put("sku",sku);
        return cmsFeedLiveSkuDaoExt.selectOne(param);
    }

}
