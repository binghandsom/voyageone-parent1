package com.voyageone.web2.cms.views.group;

import com.voyageone.common.util.MongoUtils;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.promotion.CmsPromotionService;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 16/1/14
 */
@Service
public class CmsGroupListService {

    @Autowired
    private CmsPromotionService cmsPromotionService;

    @Autowired
    protected VoApiDefaultClient voApiClient;

    private final String searchItems = "channelId;prodId;catId;catPath;created;creater;modified;" +
            "modifier;fields;groups.msrpStart;groups.msrpEnd;groups.retailPriceStart;groups:retailPriceEnd;" +
            "groups.salePriceStart;groups.salePriceEnd;groups.platforms.$;skus";

    private final String searchProductIds = "channelId;prodId;fields.code";

    /**
     * 获取检索页面初始化的master data数据
     * @param userInfo
     * @return
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, String language) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取promotion list
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", userInfo.getSelChannelId());
        masterData.put("promotionList", cmsPromotionService.queryByCondition(params));

        return masterData;
    }

    /**
     * 获取当前页的product列表
     * @param params
     * @param userInfo
     * @param cmsSessionBean
     * @return
     */
    public ProductsGetResponse GetProductList(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {

        ProductsGetRequest productRequest = new ProductsGetRequest(userInfo.getSelChannelId());
        productRequest.setPageNo(Integer.valueOf(params.get("pageNum").toString()));
        productRequest.setPageSize(Integer.valueOf(params.get("pageSize").toString()));
        productRequest.setQueryString(getSearchValue(params, cmsSessionBean));
        productRequest.setFields(searchItems);

        //SDK取得Product 数据
        return voApiClient.execute(productRequest);
    }

    /**
     * 获取该Group下面所有Id列表
     * @param params
     * @param userInfo
     * @param cmsSessionBean
     * @return
     */
    public ProductsGetResponse GetProductIdList(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {

        ProductsGetRequest productIdsRequest = new ProductsGetRequest(userInfo.getSelChannelId());
        productIdsRequest.setQueryString(getSearchValue(params, cmsSessionBean));
        productIdsRequest.setFields(searchProductIds);

        //SDK取得Product 数据
        return voApiClient.execute(productIdsRequest);
    }

    /**
     * 获取group的检索条件
     * @param params
     * @param cmsSessionBean
     * @return
     */
    private String getSearchValue (Map<String, Object> params, CmsSessionBean cmsSessionBean) {

        StringBuffer result = new StringBuffer();

        // 设置platform检索条件
        StringBuffer resultPlatforms = new StringBuffer();

        // 添加platform cart
        resultPlatforms.append(MongoUtils.splicingValue("cartId", cmsSessionBean.getCategoryType().get("cartId")).toString());
        resultPlatforms.append(",");

        // 添加platform id
        resultPlatforms.append(MongoUtils.splicingValue("groupId", Long.valueOf(params.get("id").toString())));
        resultPlatforms.append(",");

        result.append(MongoUtils.splicingValue("groups.platforms"
                , "{" + resultPlatforms.toString().substring(0, resultPlatforms.toString().length() - 1) + "}"
                , "$elemMatch"));
        result.append(",");

        return "{" + result.toString() + "}";
    }

}