package com.voyageone.task2.cms.service.search;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.components.solr.bean.CommIdSearchModel;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.query.SimpleQueryCursor;
import com.voyageone.components.solr.service.CmsProductDistSearchService;
import com.voyageone.components.solr.service.CmsProductSearchService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Cms Product Data 全量导入 分销 收索服务器 Service
 *
 * @author chuanyu.liang 2016/9/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsProductTotalImportToDistSearchService extends BaseCronTaskService {

    private static final int IMPORT_DATA_TO_SEARCH_FROM_MONGO_SIZE = 1000;

    private static final String CMS_CHANNEL_ID = "928";

    @Autowired
    private ChannelService channelService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private CmsProductDistSearchService cmsProductDistSearchService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 取得所有Channel
        List<OrderChannelBean> list = channelService.getChannelListBy(null, null, -1, "1");
        if (list == null || list.isEmpty()) {
            return;
        }

        logger.info("CmsProductTotalImportToDistSearchService.onStartup start channelId:{}", CMS_CHANNEL_ID);
        importDataToSearchFromMongo(CMS_CHANNEL_ID);
        logger.info("CmsProductTotalImportToDistSearchService.onStartup end channelId:{}", CMS_CHANNEL_ID);
    }

    /**
     * 全量导入
     */
    void importDataToSearchFromMongo(String channelId) {
        long currentTime = System.currentTimeMillis();
        JongoQuery queryObject = new JongoQuery();
        //queryObject.setProjection("{'_id':1, 'channelId':1, 'common.fields.code':1, 'common.fields.model':1, 'common.skus.skuCode':1}");
        Iterator<CmsBtProductModel> it = cmsBtProductDao.selectCursor(queryObject, channelId);

        List<SolrUpdateBean> beans = new ArrayList<>();
        int index = 1;
        while (it.hasNext()) {
            CmsBtProductModel cmsBtProductModel = it.next();

            SolrUpdateBean update = cmsProductDistSearchService.createSolrBeanForNew(cmsBtProductModel, currentTime);
            if (update == null) {
                continue;
            }
            beans.add(update);
            // 更新数据
            if (index % IMPORT_DATA_TO_SEARCH_FROM_MONGO_SIZE == 0) {
                String response = cmsProductDistSearchService.saveBeans(beans);
                logger.info("CmsProductTotalImportToDistSearchService.importDataToSearchFromMongo commit count:{}; response:{}", index, response);
                cmsProductDistSearchService.commit();
                beans = new ArrayList<>();
            }
            index++;
        }
        // 更新数据
        if (!beans.isEmpty()) {
            String response = cmsProductDistSearchService.saveBeans(beans);
            logger.info("CmsProductTotalImportToDistSearchService.importDataToSearchFromMongo commit count:{}; response:{}", index, response);
            cmsProductDistSearchService.commit();
        }

        index = 1;
        List<String> removeIdList = new ArrayList<>();
        //删除数据
        SimpleQueryCursor<CommIdSearchModel> productSearchCursor = cmsProductDistSearchService.queryIdsForCursorNotLastVer(channelId, currentTime);
        //noinspection Duplicates
        while (productSearchCursor.hasNext()) {
            CommIdSearchModel model = productSearchCursor.next();
            if (model != null && model.getId() != null) {
                removeIdList.add(model.getId());
            }
            // 删除数据
            if (index % IMPORT_DATA_TO_SEARCH_FROM_MONGO_SIZE == 0) {
                cmsProductDistSearchService.deleteByIds(removeIdList);
                removeIdList = new ArrayList<>();
                cmsProductDistSearchService.commit();
            }
            index++;
        }
        // 删除数据
        if (!removeIdList.isEmpty()) {
            cmsProductDistSearchService.deleteByIds(removeIdList);
            cmsProductDistSearchService.commit();
        }
    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsProductTotalImportToDistSearchJob";
    }
}
