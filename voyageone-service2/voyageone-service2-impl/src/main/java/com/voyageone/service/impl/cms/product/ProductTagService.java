package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtTagProductLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Product Tag Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class ProductTagService extends BaseService {

//    @Autowired
//    private CmsBtTagProductLogDao tagProductLogDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /**
     * 增加商品的Tag
     */
    public void saveTagProducts(String channelId, String tagPath, List<Long> productIds, String modifier) {
        Map<Long, List<String>> productIdTagsMap = new HashMap<>();
        for (Long productId : productIds) {
            if (!productIdTagsMap.containsKey(productId)) {
                productIdTagsMap.put(productId, new ArrayList<>());
            }
            List<String> tagModelList = productIdTagsMap.get(productId);
            if (!tagModelList.contains(tagPath)) {
                tagModelList.add(tagPath);
            }
        }

        saveTagProducts(channelId, productIdTagsMap, modifier);
    }

    /**
     * save tag products
     */
    public void saveTagProducts(String channelId, Map<Long, List<String>> productIdTagsMap, String modifier) {
        if (productIdTagsMap == null || productIdTagsMap.size() == 0) {
            throw new RuntimeException("productIdTagsMap is not empty!");
        }

        if (StringUtils.isEmpty(modifier)) {
            throw new RuntimeException("modifier is not empty!");
        }

        // add tag products
        List<BulkUpdateModel> bulkProductList = new ArrayList<>();
        List<CmsBtTagProductLogModel> cmsBtTagLogModelList = new ArrayList<>();


        for (Map.Entry<Long, List<String>> entry: productIdTagsMap.entrySet()) {

            Long productId = entry.getKey();
            List<String> tagPathList = entry.getValue();
            if (tagPathList == null || tagPathList.size() == 0) {
                continue;
            }

            for (String tagPath : tagPathList) {
                if (tagPath == null || StringUtils.isEmpty(tagPath)) {
                    throw new RuntimeException("tagPath is not empty!");
                }

                Integer tagId = getTagIdByTagPath(tagPath);
                if (tagId == null) {
                    throw new RuntimeException("tagPath is incorrect, not get tagId!");
                }

                addSaveTagProducts(channelId, tagId, tagPath, modifier, productId, bulkProductList, cmsBtTagLogModelList);
            }
        }

//        if (cmsBtTagLogModelList.size() > 0) {
//            tagProductLogDao.insertCmsBtTagLogList(cmsBtTagLogModelList);
//        }

        if (bulkProductList.size() > 0) {
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkProductList, modifier, "$addToSet");
        }
    }

    /**
     * saveTagProductsBulk
     * @param channelId channel Id
     * @param tagId tagId
     * @param tag tag
     * @param modifier modifier
     * @param productId product Id
     * @param bulkList BulkUpdateModel List
     * @param cmsBtTagLogModelList cmsBtTagLogModel List
     */
    private void addSaveTagProducts(String channelId, int tagId, String tag, String modifier, Long productId,
                                     List<BulkUpdateModel> bulkList, List<CmsBtTagProductLogModel> cmsBtTagLogModelList) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("tags", tag);
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", productId);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        CmsBtTagProductLogModel tagLogModel = new CmsBtTagProductLogModel();
        tagLogModel.setTagId(tagId);
        tagLogModel.setProductId(productId);
        tagLogModel.setComment("insert");
        tagLogModel.setCreater(modifier);
        cmsBtTagLogModelList.add(tagLogModel);
    }

    /**
     * 取得tagID
     * @param tagPath tag Path
     */
    private Integer getTagIdByTagPath(String tagPath) {
        Integer result = null;
        if (tagPath != null) {
            String[] tagPathArr = tagPath.split("-");
            if (tagPathArr.length>1) {
                String tagIdStr = tagPathArr[tagPathArr.length-1];
                if (StringUtils.isDigit(tagIdStr)) {
                    result = Integer.parseInt(tagIdStr);
                }
            }
        }
        return result;
    }

    /**
     * tags deletes
     */
    public void delete(String channelId, String tagPath, List<Long> productIds, String modifier) {
        Map<Long, List<String>> productIdTagsMap = new HashMap<>();
        for (Long productId : productIds) {
            if (!productIdTagsMap.containsKey(productId)) {
                productIdTagsMap.put(productId, new ArrayList<>());
            }
            List<String> tagModelList = productIdTagsMap.get(productId);
            if (!tagModelList.contains(tagPath)) {
                tagModelList.add(tagPath);
            }
        }

        delete(channelId, productIdTagsMap, modifier);
    }

    /**
     * tags deletes
     */
    public void delete(String channelId, Map<Long, List<String>> productIdTagsMap, String modifier) {
        if (productIdTagsMap == null || productIdTagsMap.size() == 0) {
            throw new RuntimeException("productIdTagsMap is not empty!");
        }

        if (StringUtils.isEmpty(modifier)) {
            throw new RuntimeException("modifier is not empty!");
        }

        // add tag products
        List<BulkUpdateModel> bulkProductList = new ArrayList<>();
        List<CmsBtTagProductLogModel> cmsBtTagLogModelList = new ArrayList<>();

        for (Map.Entry<Long, List<String>> entry: productIdTagsMap.entrySet()) {

            Long productId = entry.getKey();
            List<String> tagPathList = entry.getValue();
            if (tagPathList == null || tagPathList.size() == 0) {
                continue;
            }

            for (String tagPath : tagPathList) {
                if (tagPath == null || StringUtils.isEmpty(tagPath)) {
                    throw new RuntimeException("tagPath is not empty!");
                }

                Integer tagId = getTagIdByTagPath(tagPath);
                if (tagId == null) {
                    throw new RuntimeException("tagPath is incorrect, not get tagId!");
                }

                addDeleteTagBulk(channelId, tagId, tagPath, modifier, productId, bulkProductList, cmsBtTagLogModelList);
            }
        }

//        if (cmsBtTagLogModelList.size() > 0) {
//            tagProductLogDao.insertCmsBtTagLogList(cmsBtTagLogModelList);
//        }

        if (bulkProductList.size() > 0) {
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkProductList, modifier, "$pull");
        }
    }

    /**
     * 批量delete tage 根据productId
     * @param channelId channel Id
     * @param tagId tagId
     * @param tag tag
     * @param modifier modifier
     * @param productId product Id
     * @param bulkList BulkUpdateModel List
     * @param cmsBtTagLogModelList cmsBtTagLogModel List
     */
    private void addDeleteTagBulk(String channelId, int tagId, String tag, String modifier, Long productId,
                List<BulkUpdateModel> bulkList, List<CmsBtTagProductLogModel> cmsBtTagLogModelList) {
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("tags", tag);
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", productId);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        CmsBtTagProductLogModel tagLogModel = new CmsBtTagProductLogModel();
        tagLogModel.setTagId(tagId);
        tagLogModel.setProductId(productId);
        tagLogModel.setComment("delete");
        tagLogModel.setCreater(modifier);
        cmsBtTagLogModelList.add(tagLogModel);
    }

}
