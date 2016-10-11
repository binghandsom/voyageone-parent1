package com.voyageone.web2.vms.views.feed;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.feed.FeedCategoryTreeService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * VmsFeedSearchService
 * Created on 2016/7/11.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsFeedSearchService extends BaseViewService {

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private FeedCategoryTreeService feedCategoryTreeService;

    // 查询产品信息时的缺省输出列
    private final String searchItems = "{'category':1,'code':1,'name':1,'image':1,'skus':1}";

    // 查询产品信息时的缺省排序条件
    private final String sortItems = "{'code':1}";

    /**
     * 初始化（取得类目信息)
     * @param channelId
     * @return
     */
    public List<CmsMtFeedCategoryTreeModel> init(String channelId) {
        List<CmsMtFeedCategoryTreeModel> feedCategoryTree = feedCategoryTreeService.getFeedAllCategoryTree(channelId);
        return feedCategoryTree;
    }

    /**
     * 获取当前页的FEED信息
     * @param searchValue
     * @param userInfo
     * @return
     */
    public List<Map<String, Object>> getFeedList(Map<String, Object> searchValue, UserSessionBean userInfo) {
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery(feedInfoService.getSearchQueryForVendor(searchValue));
        // 当数据很多时，加上指定字段反而影响性能
        // queryObject.setProjection(searchItems);
        queryObject.setSort(sortItems);
        int pageNum = (Integer) searchValue.get("curr");
        int pageSize = (Integer) searchValue.get("size");
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);
        List<CmsBtFeedInfoModel> models = feedInfoService.getListForVendor(userInfo.getSelChannelId(), queryObject);
        List<Map<String, Object>> feedInfoList = editFeedInfo(models);
        return feedInfoList;
    }

    /**
     * 获取当前页的product列表Cnt
     * @param searchValue
     * @param userInfo
     * @return
     */
    public long getFeedCnt(Map<String, Object> searchValue, UserSessionBean userInfo) {
        return feedInfoService.getCntForVendor(userInfo.getSelChannelId(), searchValue);
    }

    /**
     * 编辑FeedInfo
     * @param models 编辑的CmsBtFeedInfoModel列表
     */
    private List<Map<String, Object>> editFeedInfo(List<CmsBtFeedInfoModel> models) {
        List<Map<String, Object>> feedInfoList = new ArrayList<>();
        for(CmsBtFeedInfoModel model : models) {

            Map<String, Object> feedInfoMap = new HashMap();
            feedInfoMap.put("code", model.getCode());
            feedInfoMap.put("name", model.getName());
            feedInfoMap.put("qtyTotal", model.getQty());
            List<CmsBtFeedInfoModel_Sku> skuList = model.getSkus();
            if (skuList != null && skuList.size() > 0) {
                feedInfoMap.put("skuNum", skuList.size());
            } else {
                feedInfoMap.put("skuNum", 0);
            }
            List<String> imageList = model.getImage();
            if (imageList != null && imageList.size() > 0) {
                feedInfoMap.put("image", imageList.get(0));
            } else {
                feedInfoMap.put("image", "");
            }

            String category = model.getCategory();
            String[] categoryArray = category.split("-");
            if (categoryArray.length > 0) {
                category = "";
                for (String categoryItem : categoryArray) {
                    // 不等于空的情况下，去掉首尾空格，并替换全角横杠为半角横杠，重新组装一下
                    if (!StringUtils.isEmpty(categoryItem)) {
                        category += categoryItem.trim().replaceAll("－", "-") + "/";
                    }
                }
                // 去掉最后一个分隔符[/]
                if (!StringUtils.isEmpty(category)) {
                    category = category.substring(0, category.length() - 1);
                }
                model.setCategory(category);
            }
            feedInfoMap.put("category", model.getCategory());

            // 子Sku的编辑
            List<Map<String, Object>> skuInfoList = new ArrayList<>();
            Double maxPrice = null;
            Double minPrice = null;
            if (skuList != null && skuList.size() > 0) {
                for (CmsBtFeedInfoModel_Sku skuModel : skuList) {
                    Map<String, Object> skuInfoMap = new HashMap();
                    skuInfoMap.put("sku", skuModel.getClientSku());
                    skuInfoMap.put("voPrice", skuModel.getPriceNet());
                    skuInfoMap.put("variationTheme", skuModel.getVariationTheme() == null ? "size" : skuModel.getVariationTheme().toLowerCase());
                    skuInfoMap.put("size", skuModel.getSize());
                    skuInfoMap.put("weight", skuModel.getWeightOrg() + " " + skuModel.getWeightOrgUnit());
                    skuInfoMap.put("qty", skuModel.getQty());
                    skuInfoList.add(skuInfoMap);
                    if (maxPrice == null || skuModel.getPriceNet() > maxPrice) {
                        maxPrice = skuModel.getPriceNet();
                    }
                    if (minPrice == null || skuModel.getPriceNet() < minPrice) {
                        minPrice = skuModel.getPriceNet();
                    }
                }
            }
            feedInfoMap.put("maxVoPrice", maxPrice == null ? 0.00 : maxPrice);
            feedInfoMap.put("minVoPrice", minPrice == null ? 0.00 : minPrice);
            if (skuInfoList.size() > 1) {
                feedInfoMap.put("skus", skuInfoList);
            }
            feedInfoList.add(feedInfoMap);
        }

        return feedInfoList;
    }
}
