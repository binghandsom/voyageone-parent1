package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoAggregate;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean2;
import com.voyageone.web2.cms.views.channel.CmsChannelTagService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private ProductTagService productTagService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    // 查询产品信息时的缺省输出列
    public final static String searchItems = "channelId;prodId;created;creater;modified;orgChannelId;modifier;freeTags;sales;platforms;" +
            "common.fields.productNameEn;common.fields.brand;common.fields.status;common.fields.code;common.fields.images1;common.fields.images2;common.fields.images3;common.fields.images4;common.fields.quantity;common.fields.productType;common.fields.sizeType;common.fields.isMasterMain;" +
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

        // 获取自定义标签列表
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
        masterData.put("sortList", commonPropService.getSortColumns());

        // 获取category list
        masterData.put("categoryList", channelCategoryService.getAllCategoriesByChannelId(userInfo.getSelChannelId()));

        // 店铺(cart/平台)列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, language);
        // 按cart获取promotion list
        Map<String, List> promotionMap = new HashMap<>();
        param = new HashMap<>();
        for (TypeChannelBean cartBean : cartList) {
            if (CartEnums.Cart.JM.getId().equals(cartBean.getValue())) {
                // 聚美促销活动预加载
                promotionMap.put(CartEnums.Cart.JM.getId(), jmPromotionService.getJMActivePromotions(userInfo.getSelChannelId()));
            } else {
                param.put("cartId", Integer.parseInt(cartBean.getValue()));
                promotionMap.put(cartBean.getValue(), promotionService.getPromotions4AdvSearch(userInfo.getSelChannelId(), param));
            }
        }
        masterData.put("promotionMap", promotionMap);

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

        // 获取店铺列表
        masterData.put("cartList", cartList);

        return masterData;
    }

    /**
     * 统计当前查询的product件数（查询条件从画面而来）
     */
    public long countProductCodeList(CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        long rs = cmsBtProductDao.countByQuery(advSearchQueryService.getSearchQuery(searchValue, cmsSessionBean, false), userInfo.getSelChannelId());
        return rs;
    }

    /**
     * 获取当前查询的product列表（查询条件从画面而来）<br>
     * 这里为了效率考虑，不检索全部数据，只检索1000件（考虑每页最多显示100条，平均每个group最多有10个code）
     */
    public List<String> getProductCodeList(CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean, int limitSize) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(advSearchQueryService.getSearchQuery(searchValue, cmsSessionBean, false));
        queryObject.setProjection("{'common.fields.code':1,'_id':0}");
        queryObject.setSort(advSearchQueryService.setSortValue(searchValue, cmsSessionBean));
        if (searchValue.getProductPageNum() > 0) {
            queryObject.setSkip((searchValue.getProductPageNum() - 1) * searchValue.getProductPageSize());
            if (limitSize > 0) {
                queryObject.setLimit(limitSize);
            } else {
                queryObject.setLimit(searchValue.getProductPageSize());
            }
        }
        if ($isDebugEnabled()) {
            $debug(String.format("高级检索 获取当前查询的product列表 ChannelId=%s, %s", userInfo.getSelChannelId(), queryObject.toString()));
        }
        List<CmsBtProductModel> prodList = productService.getList(userInfo.getSelChannelId(), queryObject);
        if (prodList == null || prodList.isEmpty()) {
            $warn("高级检索 getProductCodeList prodList为空 查询条件=：" + queryObject.toString());
            return new ArrayList<>(0);
        }

        // 取得符合条件的产品code列表
        List<String> codeList = new ArrayList<>(prodList.size());
        for (CmsBtProductModel prodObj : prodList) {
            if (prodObj.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field field = prodObj.getCommon().getFields();
            if (field != null && field.getCode() != null) {
                codeList.add(field.getCode());
            }
        }
        return codeList;
    }

    /**
     * 获取当前查询的product code列表（查询条件从session而来）
     */
    public List<String> getProductCodeList(String channelId, CmsSessionBean cmsSessionBean) {
        CmsSearchInfoBean2 searchValue = (CmsSearchInfoBean2) cmsSessionBean.getAttribute("_adv_search_params");
        if (searchValue == null) {
            $warn("高级检索 getProductCodeList session中的查询条件为空");
            return new ArrayList<>(0);
        }
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(advSearchQueryService.getSearchQuery(searchValue, cmsSessionBean, false));
        queryObject.setProjection("{'common.fields.code':1,'_id':0}");
        if ($isDebugEnabled()) {
            $debug(String.format("高级检索 获取当前查询的product列表 (session) ChannelId=%s, %s", channelId, queryObject.toString()));
        }

        List<CmsBtProductModel> prodList = productService.getList(channelId, queryObject);
        if (prodList == null || prodList.isEmpty()) {
            $warn("高级检索 getProductCodeList prodList为空 查询条件(session)=：" + queryObject.toString());
            return new ArrayList<>(0);
        }

        // 取得符合条件的产品code列表
        List<String> codeList = new ArrayList<>(prodList.size());
        for (CmsBtProductModel prodObj : prodList) {
            if (prodObj.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field field = prodObj.getCommon().getFields();
            if (field != null && field.getCode() != null) {
                codeList.add(field.getCode());
            }
        }
        return codeList;
    }

    /**
     * 获取当前查询的product id列表（查询条件从session而来）
     */
    public List<Long> getProductIdList(String channelId, CmsSessionBean cmsSessionBean) {
        CmsSearchInfoBean2 searchValue = (CmsSearchInfoBean2) cmsSessionBean.getAttribute("_adv_search_params");
        if (searchValue == null) {
            return new ArrayList<>(0);
        }
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(advSearchQueryService.getSearchQuery(searchValue, cmsSessionBean, false));
        queryObject.setProjection("{'prodId':1,'_id':0}");

        List<CmsBtProductModel> prodList = productService.getList(channelId, queryObject);
        if (prodList == null || prodList.isEmpty()) {
            $warn("CmsSearchAdvanceService.getProductIdList prodList为空");
            return new ArrayList<>(0);
        }

        // 取得符合条件的产品code列表
        List<Long> codeList = new ArrayList<>(prodList.size());
        for (CmsBtProductModel prodObj : prodList) {
            codeList.add(prodObj.getProdId());
        }
        return codeList;
    }

    /**
     * 获取当前页的product列表
     */
    public List<CmsBtProductBean> getProductInfoList(List<String> prodCodeList, CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
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
     * 统计当前查询的group件数（查询条件从画面而来）
     * 其实是统计product表中'platforms.Pxx.mainProductCode'的个数(使用聚合查询)，这里需要确保product表和group表的数据一致
     */
    public long countGroupCodeList(CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        List<JomgoAggregate> aggrList = new ArrayList<>();
        String qry1 = advSearchQueryService.getSearchQuery(searchValue, cmsSessionBean, false);
        if (qry1 != null && qry1.length() > 0) {
            aggrList.add(new JomgoAggregate("{ $match : " + qry1 + " }"));
        }
        String gp1 = "{ $group : { _id : '$platforms.P" + searchValue.getCartId() + ".mainProductCode' } }";
        String gp2 = "{ $group : { _id : null, count: { $sum : 1 } } }";
        aggrList.add(new JomgoAggregate(gp1));
        aggrList.add(new JomgoAggregate(gp2));
        List<Map<String, Object>> rs = cmsBtProductDao.aggregateToMap(userInfo.getSelChannelId(), aggrList);
        if (rs == null || rs.isEmpty()) {
            $warn("高级检索 countGroupCodeList 统计无结果");
            return 0;
        }
        Map rsMap = rs.get(0);
        if (rsMap == null || rsMap.isEmpty()) {
            $warn("高级检索 countGroupCodeList 统计无结果");
            return 0;
        }
        int sum = (Integer) rsMap.get("count");
        return sum;
    }

    /**
     * 返回当前页的group列表，这里是分页查询
     */
    public List<String> getGroupCodeList(List<String> codeList, UserSessionBean userInfo, CmsSearchInfoBean2 searchValue, int cartId) {
        // 在group表中过滤platforms相关信息
        JomgoQuery qrpQuy = new JomgoQuery();
        qrpQuy.setQuery("{'cartId':#,'productCodes':{$in:#}}");
        qrpQuy.setParameters(cartId, codeList);
        qrpQuy.setProjection("{'_id':0,'mainProductCode':1}");
        if (searchValue.getGroupPageNum() > 0) {
            qrpQuy.setSkip((searchValue.getGroupPageNum() - 1) * searchValue.getGroupPageSize());
            qrpQuy.setLimit(searchValue.getGroupPageSize());
        }

        List<CmsBtProductGroupModel> grpList = productGroupService.getList(userInfo.getSelChannelId(), qrpQuy);
        if (grpList == null || grpList.isEmpty()) {
            $warn("高级检索 getProductCodeList grpList为空 查询条件=：" + qrpQuy.toString());
            return new ArrayList<String>(0);
        }

        // 将上面查询的结果放到一个List中,以返回code
        List<String> grpCodeList = new ArrayList<String>(grpList.size());
        for (CmsBtProductGroupModel grpObj : grpList) {
            String pCd = grpObj.getMainProductCode();
            grpCodeList.add(pCd);
        }
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

    /**
     * 向产品添加tag，同时添加该tag的所有上级tag
     */
    public void addProdTag(String channelId, Map<String, Object> params, String tagsKey, String modifier, CmsSessionBean cmsSession) {
        List<Long> prodIdList = CommonUtil.changeListType((List<Integer>) params.get("prodIdList"));
        String tagPath = StringUtils.trimToNull((String) params.get("tagPath"));
        if (tagPath == null) {
            $warn("CmsAdvanceSearchService：addProdTag 缺少参数 未选择标签");
            throw new BusinessException("缺少参数，未选择标签!");
        }

        if (prodIdList == null || prodIdList.isEmpty()) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            prodIdList = getProductIdList(channelId, cmsSession);
            if (prodIdList == null || prodIdList.isEmpty()) {
                $warn("CmsAdvanceSearchService：addProdTag 缺少参数 未选择商品");
                throw new BusinessException("缺少参数，未选择商品!");
            }
        }

        productTagService.addProdTag(channelId, tagPath, prodIdList, tagsKey, modifier);
    }
}
