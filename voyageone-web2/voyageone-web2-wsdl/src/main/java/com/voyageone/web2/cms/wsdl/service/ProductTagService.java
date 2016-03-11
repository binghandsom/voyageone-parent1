package com.voyageone.web2.cms.wsdl.service;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.service.dao.cms.CmsBtTagProductLogDao;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.service.model.cms.CmsBtTagProductLogModel;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.ProductSkusGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsTagDeleteRequest;
import com.voyageone.web2.sdk.api.request.ProductsTagPutRequest;
import com.voyageone.web2.sdk.api.response.ProductsTagDeleteResponse;
import com.voyageone.web2.sdk.api.response.ProductsTagPutResponse;
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
 * @since. 2.0.0
 */
@Service
public class ProductTagService extends BaseService {

    @Autowired
    private CmsBtTagProductLogDao tagProductLogDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /**
     * ProductSkusGetRequest - > ProductsGetRequest
     * @param inputRequest ProductSkusGetRequest
     * @return ProductsGetRequest
     */
    private ProductsGetRequest createProductsRequest(ProductSkusGetRequest inputRequest) {
        if (inputRequest == null) {
            return null;
        }
        ProductsGetRequest result = new ProductsGetRequest();
        result.setFields(inputRequest.getFields());
        result.addField("skus");


        result.setSorts(inputRequest.getSorts());
        result.setPageNo(inputRequest.getPageNo());
        result.setPageSize(inputRequest.getPageSize());

        result.setChannelId(inputRequest.getChannelId());
        result.setProductIds(inputRequest.getProductIds());
        result.setProductCodes(inputRequest.getProductCodes());

        return result;
    }

    /**
     * save tag products
     * @param request ProductsTagPutRequest
     * @return ProductsTagPutResponse
     */
    public ProductsTagPutResponse saveTagProducts(ProductsTagPutRequest request) {
        ProductsTagPutResponse result = new ProductsTagPutResponse();
        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        List<BulkUpdateModel> bulkInsertList = new ArrayList<>();
        List<BulkUpdateModel> bulkUpdateList = new ArrayList<>();

        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
        Map<Long, List<String>> productIdTagsMap = request.getProductIdTagPathsMap();
        if (productIdTagsMap == null || productIdTagsMap.size() == 0) {
            throw new ApiException(codeEnum.getErrorCode(), "productIdTagsMap is not empty!");
        }

        String modifier  = request.getModifier();
        if (StringUtils.isEmpty(modifier)) {
            throw new ApiException(codeEnum.getErrorCode(), "modifier is not empty!");
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
                    throw new ApiException(codeEnum.getErrorCode(), "tagPath is not empty!");
                }

                Integer tagId = getTagIdByTagPath(tagPath);
                if (tagId == null) {
                    throw new ApiException(codeEnum.getErrorCode(), "tagPath is incorrect, not get tagId!");
                }

                addSaveTagProducts(channelId, tagId, tagPath, modifier, productId, bulkProductList, cmsBtTagLogModelList);
            }
        }

        if (cmsBtTagLogModelList.size() > 0) {
            tagProductLogDao.insertCmsBtTagLogList(cmsBtTagLogModelList);
        }

        if (bulkProductList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkProductList, modifier, "$addToSet");
            setResultCount(result, bulkWriteResult);
        }

        return result;
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
        tagLogModel.setComment("insert");
        tagLogModel.setCreater(modifier);
        cmsBtTagLogModelList.add(tagLogModel);
    }

    /**
     * tags deletes
     * @param request ProductsTagDeleteRequest
     * @return ProductsTagDeleteResponse
     */
    public ProductsTagDeleteResponse delete(ProductsTagDeleteRequest request) {
        ProductsTagDeleteResponse result = new ProductsTagDeleteResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
        Map<Long, List<String>> productIdTagsMap = request.getProductIdTagPathsMap();
        if (productIdTagsMap == null || productIdTagsMap.size() == 0) {
            throw new ApiException(codeEnum.getErrorCode(), "productIdTagsMap is not empty!");
        }

        String modifier  = request.getModifier();
        if (StringUtils.isEmpty(modifier)) {
            throw new ApiException(codeEnum.getErrorCode(), "modifier is not empty!");
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
                    throw new ApiException(codeEnum.getErrorCode(), "tagPath is not empty!");
                }

                Integer tagId = getTagIdByTagPath(tagPath);
                if (tagId == null) {
                    throw new ApiException(codeEnum.getErrorCode(), "tagPath is incorrect, not get tagId!");
                }

                addDeleteTagBulk(channelId, tagId, tagPath, modifier, productId, bulkProductList, cmsBtTagLogModelList);
            }
        }

        if (cmsBtTagLogModelList.size() > 0) {
            tagProductLogDao.insertCmsBtTagLogList(cmsBtTagLogModelList);
        }

        if (bulkProductList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkProductList, modifier, "$pull");
            setResultCount(result, bulkWriteResult);
        }

        return result;
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

}
