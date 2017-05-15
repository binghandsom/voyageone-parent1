package com.voyageone.service.impl.cms.product.search;

import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.dao.cms.mongo.CmsBtSearchItemDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.model.cms.mongo.search.CmsBtSearchItemModel;
import com.voyageone.service.model.cms.mongo.search.CmsBtSearchItemModel_SearchItem;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 检索项Service
 *
 * @Author rex.wu
 * @Create 2017-05-12 13:15
 */
@Service
public class CmsBtSearchItemService extends BaseService {

    @Autowired
    private CmsBtSearchItemDao cmsBtSearchItemDao;
    @Autowired
    private CommonPropService commonPropService;
    @Autowired
    private CmsAdvSearchQueryService advSearchQueryService;


    /**
     * 高级检索查询/下载时解析条件，并把条件不空的检索项名称记录下来
     * @param userId 用户ID
     * @param channelId 渠道
     * @param jongoQuery 最终生成的Mongo查询条件
     */
    public void analysisSearchItems(int userId, String userName, String channelId, CmsSearchInfoBean2 searchInfo, JongoQuery jongoQuery) {

        if (searchInfo != null) {

            try {
                if (jongoQuery == null) {
                    jongoQuery = advSearchQueryService.getSearchQuery(searchInfo, channelId);
                }
                CmsBtSearchItemModel searchItemModel = new CmsBtSearchItemModel();
                searchItemModel.setCreater(userName);
                // 渠道
                searchItemModel.setChannelId(channelId);

                List<CmsBtSearchItemModel_SearchItem> searchItems = null;
                if (jongoQuery != null && CollectionUtils.isNotEmpty(searchItems = this.getMongoModelField(jongoQuery.getQueryMap()))) {
                    searchItemModel.setSearchItems(searchItems);
                }

                // 排序条件
                List<String> sortItems = new ArrayList<>();
                if (StringUtils.isNotBlank(searchInfo.getSortOneName())) {
                    sortItems.add(searchInfo.getSortOneName());
                }
                if (StringUtils.isNotBlank(searchInfo.getSortTwoName())) {
                    sortItems.add(searchInfo.getSortTwoName());
                }
                if (StringUtils.isNotBlank(searchInfo.getSortThreeName())) {
                    sortItems.add(searchInfo.getSortThreeName());
                }
                if (!sortItems.isEmpty()) {
                    searchItemModel.setSortItems(sortItems);
                }

                if (CollectionUtils.isNotEmpty(searchItemModel.getSearchItems()) && CollectionUtils.isNotEmpty(searchItemModel.getSearchItems())) {
                    cmsBtSearchItemDao.insert(searchItemModel);
                }

                // 自定义列：检索时从synship.ct_user_config取出用户设置
                // 自定义列暂不考虑了
                /*Map<String, Object> colMap2 = commonPropService.getCustColumnsByUserId(userId, "cms_prod_cust_col_salestype");
                colMap2 = commonPropService.getCustColumnsByUserId(userId, "cms_prod_cust_col_bidata");
                colMap2 = commonPropService.getCustColumnsByUserId(userId, "cms_prod_cust_col");
                colMap2 = commonPropService.getCustColumnsByUserId(userId, "cms_prod_cust_col_platform");*/
            } catch (Exception e) {
                // 异常记录，不影响高级检索或下载的业务
                $error(String.format("(%s)高级检索或下载时分析查询条件并记录出错了", userName), e);
            }
        }
    }

    public List<CmsBtSearchItemModel_SearchItem> getMongoModelField(Map queryMap) {
        List<CmsBtSearchItemModel_SearchItem> searchItems = new ArrayList<>();

        for (Object entryObj : queryMap.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry) entryObj;

            if (entry.getKey().startsWith("$")) {
                List<DBObject> values = (List<DBObject>) entry.getValue();
                if (CollectionUtils.isNotEmpty(values)){
                    for (DBObject dbObject : values) {
                        searchItems.addAll(getMongoModelField(dbObject.toMap()));
                    }
                }
            } else {
                searchItems.add(new CmsBtSearchItemModel_SearchItem(entry.getKey(), entry.getValue().toString().replace("\\", "")));
            }
        }
        return searchItems;
    }

}
