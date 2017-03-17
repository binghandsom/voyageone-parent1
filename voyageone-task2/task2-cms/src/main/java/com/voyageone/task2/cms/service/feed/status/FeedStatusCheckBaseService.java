package com.voyageone.task2.cms.service.feed.status;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.daoext.cms.CmsFeedLiveSkuDaoExt;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.feed.FeedSaleService;
import com.voyageone.service.model.cms.CmsFeedLiveSkuKey;
import com.voyageone.service.model.cms.CmsFeedLiveSkuModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/9/22.
 * @version 2.0.0
 */
public abstract class FeedStatusCheckBaseService extends BaseCronTaskService {

    private static final Integer pageSize = 200;

    protected abstract List<CmsFeedLiveSkuModel> getSkuList() throws Exception;

    protected abstract ChannelConfigEnums.Channel getChannel();

    @Autowired
    private CmsFeedLiveSkuDaoExt cmsFeedLiveSkuDaoExt;

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private FeedSaleService feedSaleService;

    private static Set<String> notSale;

    private static Set<String> sale;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return getClass().getSimpleName();
    }

    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        notSale = Collections.synchronizedSet(new HashSet<String>());

        sale = Collections.synchronizedSet(new HashSet<String>());

        List<CmsFeedLiveSkuModel> skus = getSkuList();
        if(ListUtils.isNull(skus)){
            $info("没有数据");
            return;
        }
        List<List<CmsFeedLiveSkuModel>> skuList = CommonUtil.splitList(skus, 1000);
        $info("删除channel=" + getChannel().getId() + "的数据");
        deleteData();
        $info("插入channel=" + getChannel().getId() + "的数据。共" + skus.size() + "条");
        skuList.forEach(this::insertData);
        skus.clear();
        skuList.clear();

        long cnt = feedInfoService.getCnt(getChannel().getId(), new HashMap<>());
        long pageCnt = cnt / pageSize + (cnt % pageSize == 0 ? 0 : 1);
        JongoQuery queryObject = new JongoQuery();
        for (int pageNum = 1; pageNum <= pageCnt; pageNum++) {
            queryObject.setSkip((pageNum - 1) * pageSize);
            queryObject.setLimit(pageSize);
            List<CmsBtFeedInfoModel> cmsBtFeedInfoModels = feedInfoService.getList(getChannel().getId(), queryObject);
            cmsBtFeedInfoModels.forEach(this::checkSkuStatus);
        }
        String saleList = sale.stream().map(Object::toString).collect(Collectors.joining(","));
        $info(String.format(" notSale -> sale 共%d个[%s]",sale.size(),saleList));

        saleList = notSale.stream().map(Object::toString).collect(Collectors.joining(","));
        $info(String.format(" sale -> not sale 共%d个[%s]",notSale.size(),saleList));
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
            CmsFeedLiveSkuModel cmsFeedLiveSku;
            if(getChannel().getId().equals("018")) {
                cmsFeedLiveSku = selectOne(sku.getSku());
            }else{
                cmsFeedLiveSku = selectOne(sku.getClientSku());
            }
            if (sku.getIsSale() == 0 && cmsFeedLiveSku != null) {
                $info(getChannel().getId()+ " " + sku.getSku() + " notSale -> sale");
                feedSaleService.sale(getChannel().getId(),sku.getClientSku(),cmsBtFeedInfoModel.getQty());
                sale.add(sku.getClientSku());
            } else if(sku.getIsSale() == 1 && cmsFeedLiveSku == null) {
                $info(getChannel().getId()+ " " + sku.getSku() + " sale ->notSale");
                feedSaleService.notSale(getChannel().getId(),sku.getClientSku());
                notSale.add(sku.getClientSku());
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
