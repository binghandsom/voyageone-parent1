package com.voyageone.web2.cms.views.search;

import com.voyageone.cms.service.CmsBtChannelCategoryService;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean;
import com.voyageone.web2.cms.views.promotion.CmsPromotionService;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsSearchIndexService extends BaseAppService{

    @Autowired
    private CmsPromotionService cmsPromotionService;

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Autowired
    private CmsBtChannelCategoryService cmsBtChannelCategoryService;

    private final String searchItems = "channelId;prodId;catId;catPath;created;creater;modified;" +
            "modifier;fields;groups.msrpStart;groups.msrpEnd;groups.retailPriceStart;groups:retailPriceEnd;" +
            "groups.salePriceStart;groups.salePriceEnd;groups.platforms.$;skus";

    /**
     * 获取检索页面初始化的master data数据
     * @param userInfo
     * @return
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, String language) throws IOException{

        Map<String, Object> masterData = new HashMap<>();

        // 获取product status
        masterData.put("productStatusList", TypeConfigEnums.MastType.productStatus.getList(language));

        // 获取publish status
        masterData.put("platformStatusList", TypeConfigEnums.MastType.platFormStatus.getList(language));

        // 获取label
        masterData.put("labelTypeList", TypeConfigEnums.MastType.label.getList(language));

        // 获取price type
        masterData.put("priceTypeList", TypeConfigEnums.MastType.priceType.getList(language));

        // 获取compare type
        masterData.put("compareTypeList", TypeConfigEnums.MastType.compareType.getList(language));

        // 获取brand list
        masterData.put("brandList", TypeChannel.getOptions(Constants.comMtType.BRAND, userInfo.getSelChannelId(), language));

        // 获取category list
        masterData.put("categoryList", cmsBtChannelCategoryService.getFinallyCategoriesByChannelId(userInfo.getSelChannelId()));

        // 获取promotion list
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", userInfo.getSelChannelId());
        masterData.put("promotionList", cmsPromotionService.queryByCondition(params));

        return masterData;
    }

    /**
     * 返回当前页的group列表
     * @param searchValue
     * @param userInfo
     * @param cmsSessionBean
     * @return
     */
    public ProductsGetResponse getGroupList(CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {

        ProductsGetRequest groupRequest = new ProductsGetRequest(userInfo.getSelChannelId());
        groupRequest.setPageNo(searchValue.getGroupPageNum());
        groupRequest.setPageSize(searchValue.getGroupPageSize());
        groupRequest.setQueryString(getSearchQueryForGroup(searchValue, cmsSessionBean));
        groupRequest.setFields(searchItems);

        //SDK取得Product 数据
        return voApiClient.execute(groupRequest);
    }

    /**
     * 获取当前页的product列表
     * @param searchValue
     * @param userInfo
     * @param cmsSessionBean
     * @return
     */
    public ProductsGetResponse GetProductList(CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {

        ProductsGetRequest productRequest = new ProductsGetRequest(userInfo.getSelChannelId());
        productRequest.setPageNo(searchValue.getProductPageNum());
        productRequest.setPageSize(searchValue.getProductPageSize());
        productRequest.setQueryString(getSearchQueryForProduct(searchValue, cmsSessionBean));
        productRequest.setFields(searchItems);

        //SDK取得Product 数据
        return voApiClient.execute(productRequest);
    }

    /**
     * 获取product的检索条件
     * @param searchValue
     * @param cmsSessionBean
     * @return
     */
    private String getSearchQueryForProduct (CmsSearchInfoBean searchValue, CmsSessionBean cmsSessionBean) {

        StringBuffer result = new StringBuffer();

        // 设置platform检索条件
        StringBuffer resultPlatforms = new StringBuffer();

        // 添加platform cart
        resultPlatforms.append(MongoUtils.splicingValue("cartId", cmsSessionBean.getCategoryType().get("cartId")).toString());
        resultPlatforms.append(",");

        // 获取platform status
        if (searchValue.getPlatformStatus() != null
                && searchValue.getPlatformStatus().length > 0) {
            // 获取platform status
            resultPlatforms.append(MongoUtils.splicingValue("platformStatus", searchValue.getPlatformStatus()));
            resultPlatforms.append(",");
        }

        // 获取publishTime start
        if (searchValue.getPublishTimeStart() != null ) {
            resultPlatforms.append(MongoUtils.splicingValue("publishTime", searchValue.getPublishTimeStart(), "$gte"));
            resultPlatforms.append(",");
        }

        // 获取publishTime End
        if (searchValue.getPublishTimeTo() != null) {
            resultPlatforms.append(MongoUtils.splicingValue("publishTime", searchValue.getPublishTimeTo(), "$lte"));
            resultPlatforms.append(",");
        }

        result.append(MongoUtils.splicingValue("groups.platforms"
                , "{" + resultPlatforms.toString().substring(0, resultPlatforms.toString().length() - 1) + "}"
                , "$elemMatch"));
        result.append(",");

        // 获取其他检索条件
        result.append(getSearchValueForMongo(searchValue));

        if (!StringUtils.isEmpty(result.toString())) {
            return "{" + result.toString().substring(0, result.toString().length() - 1) + "}";
        }
        else {
            return "";
        }
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     * @param searchValue
     * @param cmsSessionBean
     * @return
     */
    private String getSearchQueryForGroup (CmsSearchInfoBean searchValue, CmsSessionBean cmsSessionBean) {

        StringBuffer result = new StringBuffer();

        // 设置platform检索条件
        StringBuffer resultPlatforms = new StringBuffer();

        // 添加platform cart
        resultPlatforms.append(MongoUtils.splicingValue("cartId", cmsSessionBean.getCategoryType().get("cartId")).toString());
        resultPlatforms.append(",");

        // 获取platform status
        if (searchValue.getPlatformStatus() != null
                && searchValue.getPlatformStatus().length > 0) {
            // 获取platform status
            resultPlatforms.append(MongoUtils.splicingValue("platformStatus", searchValue.getPlatformStatus()));
            resultPlatforms.append(",");
        }

        // 获取publishTime start
        if (searchValue.getPublishTimeStart() != null ) {
            resultPlatforms.append(MongoUtils.splicingValue("publishTime", searchValue.getPublishTimeStart(), "$gte"));
            resultPlatforms.append(",");
        }

        // 获取publishTime End
        if (searchValue.getPublishTimeTo() != null) {
            resultPlatforms.append(MongoUtils.splicingValue("publishTime", searchValue.getPublishTimeTo(), "$lte"));
            resultPlatforms.append(",");
        }

        // 设置查询主商品
        resultPlatforms.append(MongoUtils.splicingValue("isMain", 1));
        resultPlatforms.append(",");

        result.append(MongoUtils.splicingValue("groups.platforms"
                , "{" + resultPlatforms.toString().substring(0, resultPlatforms.toString().length() - 1) + "}"
                , "$elemMatch"));
        result.append(",");

        // 获取其他检索条件
        result.append(getSearchValueForMongo(searchValue));

        if (!StringUtils.isEmpty(result.toString())) {
            return "{" + result.toString().substring(0, result.toString().length() - 1) + "}";
        }
        else {
            return "";
        }
    }

    /**
     * 获取其他检索条件
     * @param searchValue
     * @return
     */
    private String getSearchValueForMongo (CmsSearchInfoBean searchValue) {
        StringBuffer result = new StringBuffer();

        // 获取category Id
        if (searchValue.getCatId() != null) {
            result.append(MongoUtils.splicingValue("catId", searchValue.getCatId()));
            result.append(",");
        }

        // 获取product status
        if (searchValue.getProductStatus() != null
                && searchValue.getProductStatus().length > 0) {
            result.append(MongoUtils.splicingValue("fields.status", searchValue.getProductStatus()));
            result.append(",");
        }

        // 获取price start
        if(searchValue.getPriceType() != null
                && searchValue.getPriceStart() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getPriceType() + "Start", searchValue.getPriceStart(), "$gte"));
            result.append(",");
        }

        // 获取price end
        if (searchValue.getPriceType() != null
                && searchValue.getPriceEnd() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getPriceType() + "End", searchValue.getPriceEnd(), "$lte"));
            result.append(",");
        }

        // 获取createdTime start
        if (searchValue.getCreateTimeStart() != null) {
            result.append(MongoUtils.splicingValue("created", searchValue.getCreateTimeStart(), "$gte"));
            result.append(",");
        }

        // 获取createdTime End
        if (searchValue.getCreateTimeTo() != null) {
            result.append(MongoUtils.splicingValue("created", searchValue.getCreateTimeTo(), "$lte"));
            result.append(",");
        }

        // 获取inventory
        if (searchValue.getCompareType() != null
                && searchValue.getInventory() != null) {
            result.append(MongoUtils.splicingValue("fields.inventory", searchValue.getInventory(), searchValue.getCompareType()));
            result.append(",");
        }

        // 获取brand
        if (searchValue.getBrand() != null) {
            result.append(MongoUtils.splicingValue("fields.brand", searchValue.getBrand()));
            result.append(",");
        }

        // 获取promotion
        if (searchValue.getPromotion() != null) {
            result.append(MongoUtils.splicingValue("tags", searchValue.getPromotion()));
            result.append(",");
        }

        // 获取code list用于检索code,model,productName,longTitle
        if (searchValue.getCodeList() != null
                && searchValue.getCodeList().length > 0) {
            List<String> orSearch = new ArrayList<>();
            orSearch.add(MongoUtils.splicingValue("fields.code", searchValue.getCodeList()));
            orSearch.add(MongoUtils.splicingValue("fields.model", searchValue.getCodeList()));

            if (searchValue.getCodeList().length == 1) {
                orSearch.add(MongoUtils.splicingValue("fields.productName", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.longTitle", searchValue.getCodeList()[0], "$regex"));
            }
            result.append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
            result.append(",");
        }

        return result.toString();
    }
}
