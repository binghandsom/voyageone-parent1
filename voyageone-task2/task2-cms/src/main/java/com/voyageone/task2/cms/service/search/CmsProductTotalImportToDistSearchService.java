package com.voyageone.task2.cms.service.search;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.components.solr.bean.CmsProductDistSearchModel;
import com.voyageone.components.solr.bean.CommIdSearchModel;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.query.SimpleQueryCursor;
import com.voyageone.components.solr.service.CmsBrandCatsDistSearchService;
import com.voyageone.components.solr.service.CmsProductDistSearchService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private CmsBrandCatsDistSearchService cmsBrandCatsDistSearchService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 取得所有Channel
        List<OrderChannelBean> list = channelService.getChannelListBy(null, null, -1, "1");
        if (list == null || list.isEmpty()) {
            return;
        }

        logger.info("CmsProductTotalImportToDistSearchService.onStartup start channelId:{}", CMS_CHANNEL_ID);
        Map<String, Integer> brandCatsCntSumMap = new HashMap<>();
        // import data to search from mongo
        importDataToSearchFromMongo(CMS_CHANNEL_ID, brandCatsCntSumMap);
        // import data to brand_cats cnt Sum
        importDataToBrandCatsCntSum(brandCatsCntSumMap);

        logger.info("CmsProductTotalImportToDistSearchService.onStartup end channelId:{}", CMS_CHANNEL_ID);
    }

    /**
     * 全量导入
     */
    void importDataToSearchFromMongo(String channelId, Map<String, Integer> brandCatsCntSumMap) {
        long currentTime = System.currentTimeMillis();
        JongoQuery queryObject = new JongoQuery();
        //queryObject.setProjection("{'_id':1, 'channelId':1, 'common.fields.code':1, 'common.fields.model':1, 'common.skus.skuCode':1}");
        Iterator<CmsBtProductModel> it = cmsBtProductDao.selectCursor(queryObject, channelId);

        List<SolrUpdateBean> beans = new ArrayList<>();
        int index = 1;
        while (it.hasNext()) {
            CmsBtProductModel cmsBtProductModel = it.next();
            // create CmsProductDistSearchModel
            CmsProductDistSearchModel model = cmsProductDistSearchService.createSolrSearchModelForNew(cmsBtProductModel, currentTime);
            // brandCatsCnt Sum
            brandCatsCntSum(model, brandCatsCntSumMap);
            // create SolrUpdateBean
            SolrUpdateBean update = cmsProductDistSearchService.createSolrBean(model, cmsBtProductModel.get_id(), true);
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

    private void brandCatsCntSum(CmsProductDistSearchModel model, Map<String, Integer> brandCatsCntSumMap) {
        if (model != null && model.getBrandCats() != null && !model.getBrandCats().isEmpty()) {
            for (String keyWord: model.getBrandCats()) {
                if (brandCatsCntSumMap.containsKey(keyWord)) {
                    brandCatsCntSumMap.put(keyWord, brandCatsCntSumMap.get(keyWord) + 1);
                } else {
                    brandCatsCntSumMap.put(keyWord, 1);
                }
            }
        }
    }

    private void importDataToBrandCatsCntSum(Map<String, Integer> brandCatsCntSumMap) {
        for (Map.Entry<String, Integer> entry : brandCatsCntSumMap.entrySet()) {
            System.out.println(entry.getKey() + ":" +  entry.getValue());
        }
        long currentTime = System.currentTimeMillis();
        if (!brandCatsCntSumMap.isEmpty()) {
            //noinspection
            List<SolrUpdateBean> beans = cmsBrandCatsDistSearchService.createSolrBeanForNew(brandCatsCntSumMap, CMS_CHANNEL_ID, currentTime);
            if (!beans.isEmpty()) {
                String response = cmsBrandCatsDistSearchService.saveBeans(beans);
                logger.info("CmsProductTotalImportToDistSearchService.importDataToBrandCatsCntSum commit count:{}; response:{}", brandCatsCntSumMap.size(), response);
                cmsBrandCatsDistSearchService.commit();
                brandCatsCntSumMap.clear();

                List<String> removeIdList = new ArrayList<>();
                //删除数据
                SimpleQueryCursor<CommIdSearchModel> searchCursor = cmsBrandCatsDistSearchService.queryIdsForCursorNotLastVer(CMS_CHANNEL_ID, currentTime);
                //noinspection Duplicates
                while (searchCursor.hasNext()) {
                    CommIdSearchModel model = searchCursor.next();
                    if (model != null && model.getId() != null) {
                        removeIdList.add(model.getId());
                    }
                }
                // 删除数据
                if (!removeIdList.isEmpty()) {
                    cmsBrandCatsDistSearchService.deleteByIds(removeIdList);
                    cmsBrandCatsDistSearchService.commit();
                }
            }
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
