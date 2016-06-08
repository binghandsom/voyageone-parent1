package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean2;
import com.voyageone.web2.cms.views.channel.CmsChannelTagService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class CmsAdvanceSearchService extends BaseAppService {

    @Autowired
    private PromotionService promotionService;
    @Autowired
    private ChannelCategoryService channelCategoryService;
    @Autowired
    private CommonPropService commonPropService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private FeedCustomPropService feedCustomPropService;
    @Autowired
    private CmsChannelTagService cmsChannelTagService;
    @Resource
    private CmsBtJmPromotionService jmPromotionService;
    @Autowired
    private CmsAdvSearchQueryService advSearchQueryService;

    // 查询产品信息时的缺省输出列
    public final static String searchItems = "channelId;prodId;catId;catPath;created;creater;modified;orgChannelId;modifier;carts;skus;freeTags;sales;" +
            "common.fields.longTitle;common.fields.productNameEn;common.fields.brand;common.fields.status;common.fields.code;common.fields.images1;common.fields.images2;common.fields.images3;common.fields.images4;common.fields.quantity;common.fields.productType;common.fields.sizeType;common.fields.isMasterMain;" +
            "common.fields.priceSaleSt;common.fields.priceSaleEd;common.fields.priceRetailSt;common.fields.priceRetailEd;common.fields.priceMsrpSt;common.fields.priceMsrpEd;common.fields.hsCodeCrop;common.fields.hsCodePrivate;";

    /**
     * 获取检索页面初始化的master data数据
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, CmsSessionBean cmsSession, String language) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取product status
        masterData.put("productStatusList", TypeConfigEnums.MastType.productStatus.getList(language));

        // 获取publish status
        masterData.put("platformStatusList", TypeConfigEnums.MastType.platFormStatus.getList(language));

        // 获取label
        Map<String, Object> param = new HashMap<>(2);
        param.put("channelId", userInfo.getSelChannelId());
        param.put("tagTypeSelectValue", "4");
        masterData.put("freetagList", cmsChannelTagService.getTagInfoList(param));

        // 获取price type
        masterData.put("priceTypeList", TypeConfigEnums.MastType.priceType.getList(language));

        // 获取compare type
        masterData.put("compareTypeList", TypeConfigEnums.MastType.compareType.getList(language));

        // 获取brand list
        masterData.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, userInfo.getSelChannelId(), language));

        // 获取sort list
        masterData.put("sortList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.SORT_ATTRIBUTES_61, userInfo.getSelChannelId(), language));

        // 获取category list
        masterData.put("categoryList", channelCategoryService.getAllCategoriesByChannelId(userInfo.getSelChannelId()));

        // 获取promotion list
        masterData.put("promotionList", promotionService.getPromotionsByChannelId(userInfo.getSelChannelId(), null));

        //add by holysky  新增一些页的聚美促销活动预加载
        masterData.put("jmPromotionList", jmPromotionService.getJMActivePromotions(userInfo.getSelChannelId()));

        // 获取自定义查询用的属性
        masterData.put("custAttsList", cmsSession.getAttribute("_adv_search_props_custAttsQueryList"));

        //标签type
        masterData.put("tagTypeList", Types.getTypeList(74, language));

        // 设置按销量排序的选择列表
        masterData.put("salesTypeList", advSearchQueryService.getSalesTypeList(userInfo.getSelChannelId(), language, null));

        // 判断是否是minimall用户
        boolean isMiniMall = userInfo.getSelChannelId().equals(ChannelConfigEnums.Channel.VOYAGEONE.getId());
        masterData.put("isminimall", isMiniMall ? 1 : 0);
        if (isMiniMall) {
            masterData.put("channelList", Channels.getUsJoiChannelList());
        }

        //获取店铺列表
        masterData.put("cartList",TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, language));

        return masterData;
    }

    /**
     * 获取当前页的product列表
     */
    public List<String> getProductCodeList(CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(advSearchQueryService.getSearchQuery(searchValue, cmsSessionBean, false));
        queryObject.setProjection("{'common.fields.code':1,'_id':0}");
        queryObject.setSort(advSearchQueryService.setSortValue(searchValue, cmsSessionBean));
        List<CmsBtProductModel> prodList = productService.getList(userInfo.getSelChannelId(), queryObject);
        if (prodList == null || prodList.isEmpty()) {
            $warn("CmsSearchAdvanceService.getProductCodeList prodList为空");
            return new ArrayList<>(0);
        }

        // 取得符合条件的产品code列表
        List<String> codeList = new ArrayList<>(prodList.size());
        for (CmsBtProductModel prodObj : prodList) {
            CmsBtProductModel_Field field = prodObj.getCommon().getFields();
            if (field != null && field.getCode() != null) {
                codeList.add(field.getCode());
            }
        }
        return codeList;
    }

    /**
     * 获取当前页的product列表
     */
    public List<CmsBtProductBean> getProductInfoList(List<String> prodCodeList
            , CmsSearchInfoBean2 searchValue
            , UserSessionBean userInfo
            , CmsSessionBean cmsSessionBean) {
        // 最后再获取本页实际产品信息
        JomgoQuery queryObject = new JomgoQuery();
        String[] codeArr = new String[prodCodeList.size()];
        codeArr = prodCodeList.toArray(codeArr);
        queryObject.setQuery("{" + MongoUtils.splicingValue("common.fields.code", codeArr, "$in") + "}");

        String plusStr = (String) cmsSessionBean.getAttribute("_adv_search_props_searchItems");
        if (plusStr == null) {
            plusStr = "";
        }
        String projStr = queryObject.buildProjection(searchItems.concat(plusStr).split(";"));
        queryObject.setProjection(projStr);
        queryObject.setSort(advSearchQueryService.setSortValue(searchValue, cmsSessionBean));

        List<CmsBtProductBean> prodInfoList = productService.getBeanList(userInfo.getSelChannelId(), queryObject);
        if (prodInfoList == null || prodInfoList.isEmpty()) {
            $warn("CmsSearchAdvanceService.getProductInfoList prodInfoList");
            return new ArrayList<>(0);
        }
        return prodInfoList;
    }

    /**
     * 检查翻译状态
     */
    public void checkProcStatus(List<CmsBtProductBean> productList, String lang) {
        if (productList == null || productList.isEmpty()) {
            return;
        }
        List<TypeBean> transStatusList = TypeConfigEnums.MastType.translationStatus.getList(lang);
        Map<String, String> transStatusMap = new HashMap<>(transStatusList.size());
        for (TypeBean beanObj : transStatusList) {
            transStatusMap.put(beanObj.getValue(), beanObj.getName());
        }
//        List<TypeBean> lockStatusList = TypeConfigEnums.MastType.procLockStatus.getList(lang);
//        Map<String, String> lockStatusMap = new HashMap<>(lockStatusList.size());
//        for (TypeBean beanObj : lockStatusList) {
//            lockStatusMap.put(beanObj.getValue(), beanObj.getName());
//        }

        for (CmsBtProductModel prodObj : productList) {
            CmsBtProductModel_Field fieldsObj = prodObj.getCommon().getFields();
            if (fieldsObj != null) {
                String stsFlg = org.apache.commons.lang3.StringUtils.trimToNull(fieldsObj.getTranslateStatus());
                if (stsFlg != null) {
                    String stsValueStr = transStatusMap.get(stsFlg);
                    if (stsValueStr == null) {
                        fieldsObj.setTranslateStatus("");
                    } else {
                        fieldsObj.setTranslateStatus(stsValueStr);
                    }
                } else {
                    fieldsObj.setTranslateStatus("");
                }

//                stsFlg = org.apache.commons.lang3.StringUtils.trimToNull(prodObj.getLock());
//                if (stsFlg != null) {
//                    String stsValueStr = lockStatusMap.get(stsFlg);
//                    if (stsValueStr == null) {
//                        prodObj.setLock("");
//                    } else {
//                        prodObj.setLock(stsValueStr);
//                    }
//                } else {
//                    prodObj.setLock("");
//                }
            }
        }
    }

    /**
     * 返回当前页的group列表
     */
    public List<String> getGroupCodeList(List<String> codeList, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        String[] codeArr = new String[codeList.size()];
        codeArr = codeList.toArray(codeArr);
        StringBuilder resultPlatforms = new StringBuilder();
        resultPlatforms.append(MongoUtils.splicingValue("cartId", Integer.valueOf(cmsSessionBean.getPlatformType().get("cartId").toString())));
        resultPlatforms.append(",");
        resultPlatforms.append(MongoUtils.splicingValue("productCodes", codeArr, "$in"));

        // 在group表中过滤platforms相关信息
        JomgoQuery qrpQuy = new JomgoQuery();
        qrpQuy.setQuery("{" + resultPlatforms.toString() + "}");
        qrpQuy.setProjection("{'_id':0,'mainProductCode':1}");

        List<CmsBtProductGroupModel> grpList = productGroupService.getList(userInfo.getSelChannelId(), qrpQuy);
        if (grpList == null || grpList.isEmpty()) {
            $warn("CmsSearchAdvanceService.getProductCodeList grpList");
            return new ArrayList<String>(0);
        }

        // 将上面查询的结果放到一个临时map中,以过滤重复code
        Map<String, String> codeList2 = new HashMap<String, String>();
        for (CmsBtProductGroupModel grpObj : grpList) {
            String pCd = grpObj.getMainProductCode();
            codeList2.put(pCd, pCd);
        }

        List<String> grpCodeList = new ArrayList<String>(codeList2.size());
        codeList2.keySet().forEach(grpCodeList::add);
        return grpCodeList;
    }

    /**
     * 根据类目路径查询已翻译的属性信息
     */
    public List<Map<String, Object>> selectAttrs(String channelId, String catPath) {
        return feedCustomPropService.getFeedCustomPropAttrs(channelId, catPath);
    }

    /**
     * 取得自定义显示列设置
     */
    public List<Map<String, Object>> getCustColumns() {
        return commonPropService.getCustColumns();
    }

}
