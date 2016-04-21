package com.voyageone.web2.cms.views.group;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 16/1/14
 */
@Service
public class CmsGroupDetailService extends BaseAppService {

    @Autowired
    private CmsPromotionIndexService cmsPromotionService;

    @Autowired
    protected ProductGroupService productGroupService;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private ProductService productService;

    private final static String searchItems = "channelId;prodId;catId;catPath;created;creater;modified;" +
            "modifier;fields;skus";

    private final static String searchProductIds = "channelId;prodId;fields.code";

    /**
     * 获取检索页面初始化的master data数据
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
     */
    public List<CmsBtProductModel> getProductList(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchValue(params, cmsSessionBean));
        queryObject.setProjection(searchItems.split(";"));
        int pageNum = Integer.valueOf(params.get("pageNum").toString());
        int pageSize = Integer.valueOf(params.get("pageSize").toString());
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);

        return productService.getList(userInfo.getSelChannelId(), queryObject);
    }

    /**
     * 获取当前页的product列表
     */
    public long getProductCnt(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        String queryStr = getSearchValue(params, cmsSessionBean);
        return productService.getCnt(userInfo.getSelChannelId(), queryStr);
    }

    /**
     * 获取该Group下面所有Id列表
     */
    public List<CmsBtProductModel> getProductIdList(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(String.format("{ \"groupId\":%d}", Long.valueOf((String) params.get("id"))));
        CmsBtProductGroupModel grpObj = cmsBtProductGroupDao.selectOneWithQuery(queryObject, userInfo.getSelChannelId());
        if (grpObj == null) {
            $warn("CmsGroupDetailService.getProductIdList 没有group信息 " + params.toString());
            return new ArrayList<>(0);
        }

        List<String> codeList = grpObj.getProductCodes();
        if (codeList == null || codeList.isEmpty()) {
            $warn("CmsGroupDetailService.getProductIdList 没有product code list信息 " + params.toString());
            return new ArrayList<>(0);
        }

        String[] codeArr = new String[codeList.size()];
        codeArr = codeList.toArray(codeArr);
        queryObject.setQuery("{" + MongoUtils.splicingValue("fields.code", codeArr, "$in") + "}");
        queryObject.setProjection(searchProductIds.split(";"));
        return productService.getList(userInfo.getSelChannelId(), queryObject);
    }

    /**
     * 获取group的检索条件
     */
    private String getSearchValue (Map<String, Object> params, CmsSessionBean cmsSessionBean) {
        StringBuilder result = new StringBuilder();

        // 添加platform cart
        result.append(MongoUtils.splicingValue("cartId", Integer.valueOf(cmsSessionBean.getPlatformType().get("cartId").toString())));
        result.append(",");

        // 添加platform id
        result.append(MongoUtils.splicingValue("groupId", Long.valueOf(params.get("id").toString())));

        return "{" + result.toString() + "}";
    }

    /**
     * update main product.
     */
    public Map<String, Object> updateMainProduct(Map<String, Object> params, UserSessionBean userSession) {

        //参数验证.
        String errMsg =null;

        Object groupIdObj = params.get("groupId");

        Object prodIdObj = params.get("prodId");

        if (groupIdObj == null){
            errMsg = "group id is null !";
        }else if (prodIdObj == null){
            errMsg = "product id is null !";
        }

        if (errMsg != null){
            throw new BusinessException(errMsg);
        }

        Long groupId = Long.parseLong(String.valueOf(groupIdObj));
        String channelId = userSession.getSelChannelId();
        Long productId = Long.parseLong(String.valueOf(prodIdObj));

        int modifiedCount = productGroupService.updateMainProduct(channelId, productId, groupId, userSession.getUserName());

        Map<String,Object> updateResult = new HashMap<>();
        updateResult.put("result", modifiedCount);

        return updateResult;
    }

}