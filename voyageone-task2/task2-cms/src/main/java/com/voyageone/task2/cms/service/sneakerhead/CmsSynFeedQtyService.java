package com.voyageone.task2.cms.service.sneakerhead;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.wms.GetStoreStockDetailRequest2;
import com.voyageone.web2.sdk.api.response.wms.GetStoreStockDetailData2;
import com.voyageone.web2.sdk.api.response.wms.GetStoreStockDetailResponse2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dell on 2017/7/11.
 */
@Service
public class CmsSynFeedQtyService  extends BaseCronTaskService {
    @Autowired
    protected VoApiDefaultClient voApiClient;
    @Autowired
    private FeedInfoService feedInfoService;
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        final String channelId = ChannelConfigEnums.Channel.SN.getId();


        String subChannlId = "";

        //封装查询条件
        Criteria criteria = new Criteria();
        String[] status = new String[]{"New","Pending","Ready"};
        criteria.and("status").in(status);
        JongoQuery queryObject = new JongoQuery(criteria);
        Iterator<CmsBtFeedInfoModel> feeds = feedInfoService.getCursor(channelId, queryObject);

        //封装skulist
        int index = 0;
        //获取到每一个feed
        while (feeds.hasNext()) {
            CmsBtFeedInfoModel cmsBtProductModel = feeds.next();
            if (index % 3000 == 0) {
                $info("取得cms_bt_feed表的所有code的个数为:" + index);
            }
            //封装skulist按feed分,为了求feed的总库存
            ArrayList<String> skuList = new ArrayList<>();
            //获取到当前feed对应skus
            List<CmsBtFeedInfoModel_Sku> skus = cmsBtProductModel.getSkus();
            //封装当前feed对应的skulist
            if (ListUtils.notNull(skus)){
                skus.forEach(sku -> {
                    skuList.add(sku.getSku());
                });
            }
            //调用wms远程接口,获取库存详情
            GetStoreStockDetailRequest2 getStoreStockDetailRequest2 = new GetStoreStockDetailRequest2();
            getStoreStockDetailRequest2.setChannelId(channelId);
            getStoreStockDetailRequest2.setSubChannelId(subChannlId);
            getStoreStockDetailRequest2.setSkuList(skuList);
            //获取到当前feed对应的库存信息
            GetStoreStockDetailResponse2 execute = voApiClient.execute(getStoreStockDetailRequest2);
            //初始化feed总库存
            Integer totalQty = 0;
            List<GetStoreStockDetailData2.Temp> stocks = execute.getData().getStocks();
            //获取每个sku的总库存,并进行更新
            int skuNum = 0;
            for (GetStoreStockDetailData2.Temp value:stocks) {
                Integer skuTotal = value.getBase().getTotal().get(0);
                //更新对应的sku库存
                CmsBtFeedInfoModel_Sku cmsBtFeedInfoModel_sku = skus.get(skuNum);
                cmsBtFeedInfoModel_sku.setQty(skuTotal);
                //totalQty += total;
                skuNum++;
                totalQty += skuTotal;
            }
            cmsBtProductModel.setSkus(skus);
            //更新feed对应的总库存
            cmsBtProductModel.setQty(totalQty);
            feedInfoService.updateFeedInfo(cmsBtProductModel);
            index++;
        }


    }
    @Override
    protected String getTaskName() {
        return "CmsSynFeedQtyService";
    }
    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
