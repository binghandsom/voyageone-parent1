package com.voyageone.task2.cms.service.search;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.components.solr.bean.CommIdSearchModel;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.query.SimpleQueryCursor;
import com.voyageone.components.solr.service.CmsProductSearchService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Cms Product Data 全量导入收索服务器 Service
 *
 * @author chuanyu.liang 2016/9/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsProductTotalImportToSearchService extends BaseCronTaskService {

    private static final int IMPORT_DATA_TO_SEARCH_FROM_MONGO_SIZE = 1000;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private CmsProductSearchService cmsProductSearchService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 取得所有Channel
        List<OrderChannelBean> list = channelService.getChannelListBy(null, null, -1, "1");
        if (list == null || list.isEmpty()) {
            return;
        }
        List<String> cart = Arrays.asList("928");
        for (OrderChannelBean bean: list) {
            List<TypeChannelBean> typeChannelBeans = TypeChannels.getTypeListSkuCarts(bean.getOrder_channel_id(), Constants.comMtTypeChannel.SKU_CARTS_53_A,"en");
           if(typeChannelBeans.stream().anyMatch(typeChannelBean -> !cart.contains(typeChannelBean.getValue()))) {
                String channelId = bean.getOrder_channel_id();
                logger.info("CmsProductSearchImportService.onStartup start channelId:{}", channelId);
                importDataToSearchFromMongo(channelId);
                logger.info("CmsProductSearchImportService.onStartup end channelId:{}", channelId);
            }else{
                logger.info("channelId:{} 不需要做", bean.getOrder_channel_id());
            }
        }
    }
    /**
     * 全量导入
     */
    public void importDataToSearchFromMongo(String channelId) {
        long currentTime = System.currentTimeMillis();
        JongoQuery queryObject = new JongoQuery();
        Iterator<CmsBtProductModel> it = cmsBtProductDao.selectCursor(queryObject, channelId);

        List<SolrUpdateBean> beans = new ArrayList<>();
        int index = 1;
        while (it.hasNext()) {
            CmsBtProductModel cmsBtProductModel = it.next();

            SolrUpdateBean update = cmsProductSearchService.createSolrBeanForNew(cmsBtProductModel, currentTime);
            if (update == null) {
                continue;
            }
            beans.add(update);
            // 更新数据
            if (index % IMPORT_DATA_TO_SEARCH_FROM_MONGO_SIZE == 0) {
                String response = cmsProductSearchService.saveBeans(beans);
                logger.info("CmsProductSearchImportService.importDataToSearchFromMongo commit count:{}; response:{}", index, response);
                cmsProductSearchService.commit();
                beans = new ArrayList<>();
            }
            index++;
        }
        // 更新数据
        if (!beans.isEmpty()) {
            String response = cmsProductSearchService.saveBeans(beans);
            logger.info("CmsProductSearchImportService.importDataToSearchFromMongo commit count:{}; response:{}", index, response);
            cmsProductSearchService.commit();
        }

        // 因为增量job以运行 避免删除增量误删除
        index = 1;
        List<String> removeIdList = new ArrayList<>();
        //删除数据
        SimpleQueryCursor<CommIdSearchModel> productSearchCursor = cmsProductSearchService.queryIdsForCursorNotLastVer(channelId, currentTime);
        //noinspection Duplicates
        while (productSearchCursor.hasNext()) {
            CommIdSearchModel model = productSearchCursor.next();
            if (model != null && model.getId() != null) {
                removeIdList.add(model.getId());
            }
            // 删除数据
            if (index % IMPORT_DATA_TO_SEARCH_FROM_MONGO_SIZE == 0) {
                cmsProductSearchService.deleteByIds(removeIdList);
                removeIdList = new ArrayList<>();
                cmsProductSearchService.commit();
            }
            index++;
        }
        // 删除数据
        if (!removeIdList.isEmpty()) {
            cmsProductSearchService.deleteByIds(removeIdList);
            cmsProductSearchService.commit();
        }

        cmsProductSearchService.optimize();
    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsProductTotalImportToSearchJob";
    }
}
