package com.voyageone.web2.cms.views.group;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
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
    private ProductGroupService productGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;
    @Autowired
    private PlatformService platformService;

    private final static String searchItems = "channelId;prodId;catId;catPath;created;creater;modified;" +
            "modifier;fields;skus";

    /**
     * 获取检索页面初始化的master data数据
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取promotion list
        masterData.put("promotionList", promotionService.getPromotionsByChannelId(userInfo.getSelChannelId(), null));

        //add by holysky  新增一些页的聚美促销活动预加载
        masterData.put("jmPromotionList", cmsBtJmPromotionService.getJMActivePromotions(CartEnums.Cart.JM.getValue(), userInfo.getSelChannelId()));

        return masterData;
    }

    /**
     * 获取当前页的product列表
     */
    public Map<String, Object> getProductList(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {


        Map<String, Object> result = new HashMap<>();

        // 先取得group信息，
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"groupId\": #}");
        queryObject.setParameters(Integer.valueOf(params.get("id").toString()));
        List<CmsBtProductGroupModel> rstList = productGroupService.getList(userInfo.getSelChannelId(), queryObject);
        if (rstList == null || rstList.isEmpty()) {
            $warn("CmsGroupDetailService.getProductList 没有group数据 " + params.toString());
            throw new BusinessException("该group不存在");
        }

        CmsBtProductGroupModel grpObj = rstList.get(0);

        List<String> codeList = grpObj.getProductCodes();
        if (codeList == null || codeList.isEmpty()) {
            $warn("CmsGroupDetailService.getProductList group下没有product数据 " + params.toString());
            throw new BusinessException("该group下没有product数据");
        }

        JomgoQuery grpQueryObject = new JomgoQuery();
        grpQueryObject.setQuery("{\"common.fields.code\": {$in:#}}");
        grpQueryObject.setParameters(codeList);
        List<CmsBtProductModel> prodList = productService.getList(userInfo.getSelChannelId(), grpQueryObject);
        if (prodList == null || codeList.isEmpty()) {
            $warn("CmsGroupDetailService.getProductList 没有product数据 " + params.toString());
            throw new BusinessException("该group下对应的product数据找不到");
        }

        result.put("groupInfo", grpObj);
        result.put("productList", prodList);
        // 设置平台商品url
        result.put("productUrl", platformService.getPlatformProductUrl(String.valueOf(grpObj.getCartId())));
        return result;
    }

    /**
     * update main product.
     */
    public void updateMainProduct(Map<String, Object> params, UserSessionBean userSession) {

        Object groupIdObj = params.get("groupId");
        Object prodCodeObj = params.get("mainProductCode");

        if (groupIdObj == null){
            throw new BusinessException("group id is null !");
        } else if (prodCodeObj == null){
            throw new BusinessException("product code is null !");
        }

        productGroupService.updateMainProduct(userSession.getSelChannelId(), String.valueOf(prodCodeObj), Long.parseLong(String.valueOf(groupIdObj)), userSession.getUserName());
    }

}