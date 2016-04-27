package com.voyageone.web2.cms.views.group;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
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
    protected ProductGroupService productGroupService;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private PromotionService promotionService;

    private final static String searchItems = "channelId;prodId;catId;catPath;created;creater;modified;" +
            "modifier;fields;skus";

    private final static String searchProductIds = "channelId;prodId;fields.code";

    /**
     * 获取检索页面初始化的master data数据
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取promotion list
        masterData.put("promotionList", promotionService.getPromotionsByChannelId(userInfo.getSelChannelId()));

        return masterData;
    }

    /**
     * 获取当前页的product列表
     */
    public List<CmsBtProductModel> getProductList(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        // 先取得group信息，
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchValue(params, cmsSessionBean));
        List<CmsBtProductGroupModel> rstList = cmsBtProductGroupDao.select(queryObject, userInfo.getSelChannelId());
        if (rstList == null || rstList.isEmpty()) {
            $warn("CmsGroupDetailService.getProductList 没有group数据 " + params.toString());
            return new ArrayList<>(0);
        }
        CmsBtProductGroupModel grpObj = rstList.get(0);
        List<String> codeList = grpObj.getProductCodes();
        if (codeList == null || codeList.isEmpty()) {
            $warn("CmsGroupDetailService.getProductList group下没有product数据 " + params.toString());
            return new ArrayList<>(0);
        }
        String[] codeArr = new String[codeList.size()];
        codeArr = codeList.toArray(codeArr);

        JomgoQuery grpQueryObject = new JomgoQuery();
        grpQueryObject.setQuery("{" + MongoUtils.splicingValue("fields.code", codeArr, "$in") + "}");
        grpQueryObject.setProjection(searchItems.split(";"));

        List<CmsBtProductModel> prodList = productService.getList(userInfo.getSelChannelId(), grpQueryObject);
        if (prodList == null || codeList.isEmpty()) {
            $warn("CmsGroupDetailService.getProductList 没有product数据 " + params.toString());
            return new ArrayList<>(0);
        }
        for (CmsBtProductModel prodObj : prodList) {
            // 从group表合并platforms信息
            prodObj.setGroups(grpObj);
        }
        return prodList;
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