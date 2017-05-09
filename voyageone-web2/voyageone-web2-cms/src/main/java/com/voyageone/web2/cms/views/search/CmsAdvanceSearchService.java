package com.voyageone.web2.cms.views.search;

import com.google.gson.Gson;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchQueryService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsProductFreeTagsUpdateService;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.channel.CmsChannelTagService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsAdvanceSearchService extends BaseViewService {

    @Autowired
    CmsProductFreeTagsUpdateService cmsProductFreeTagsUpdateService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private CommonPropService commonPropService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsChannelTagService cmsChannelTagService;
    @Resource
    private CmsBtJmPromotionService jmPromotionService;
    @Autowired
    private CmsAdvSearchQueryService advSearchQueryService;
    @Autowired
    private CmsAdvSearchOtherService advSearchOtherService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsMqSenderService cmsMqSenderService;
    @Autowired
    private CmsBtExportTaskService cmsBtExportTaskService;
    @Autowired
    private CmsBtShelvesService cmsBtShelvesService;
    @Autowired
    private SxProductService sxProductService;

    /**
     * 获取检索页面初始化的master data数据
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, CmsSessionBean cmsSession, String language) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取product status
        masterData.put("productStatusList", TypeConfigEnums.MastType.productStatus.getList(language));

        // 获取publish status
        masterData.put("platformStatusList", TypeConfigEnums.MastType.platformStatus.getList(language));

        // 获取自定义标签列表
        Map<String, Object> param = new HashMap<>(2);
        param.put("channelId", userInfo.getSelChannelId());
        param.put("tagTypeSelectValue", "4");
        masterData.put("freetagList", cmsChannelTagService.getTagInfoByChannelId(param));

        // 获取price type
        masterData.put("priceTypeList", TypeConfigEnums.MastType.priceType.getList(language));

        // 获取compare type
        masterData.put("compareTypeList", TypeConfigEnums.MastType.compareType.getList(language));

        // 获取brand list
        masterData.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, userInfo.getSelChannelId(), language));

        // 取得产品类型
        masterData.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, userInfo.getSelChannelId(), language));
        // 取得尺寸类型
        masterData.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, userInfo.getSelChannelId(), language));

        // 取得销量类型
        List<Map<String, String>> salesTypeList = advSearchOtherService.getSalesTypeList(userInfo.getSelChannelId(), language, null);
        List<Map<String, String>> allSortList = new ArrayList<>(salesTypeList);
        // 获取sort list
        List<Map<String, Object>> sortList = commonPropService.getCustColumns(3);
        List<Map<String, String>> biDataList = advSearchOtherService.getBiDataList(userInfo.getSelChannelId(), language, null);
        allSortList.addAll(biDataList);
        for (Map<String, String> sortData : allSortList) {
            Map<String, Object> keySumMap = new HashMap<>();
            keySumMap.put("propId", sortData.get("value"));
            keySumMap.put("propName", sortData.get("name"));
            sortList.add(keySumMap);
        }
        masterData.put("sortList", sortList);

        // 店铺(cart/平台)列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, language);
        // 按cart获取promotion list，只加载有效的活动(活动期内/未关闭/有标签)
        Map<String, List> promotionMap = new HashMap<>();
        Map<String, List> shelvesMap = new HashMap<>();
        Map<String, String> confirmPrice = new HashMap<>();
        param = new HashMap<>();
        for (TypeChannelBean cartBean : cartList) {
            if (CartEnums.Cart.JM.getId().equals(cartBean.getValue())) {
                // 聚美促销活动预加载
                promotionMap.put(CartEnums.Cart.JM.getId(), jmPromotionService.getJMActivePromotions(CartEnums.Cart.JM.getValue(), userInfo.getSelChannelId()));
            } else {
                param.put("cartId", Integer.parseInt(cartBean.getValue()));
                promotionMap.put(cartBean.getValue(), promotionService.getPromotions4AdvSearch(userInfo.getSelChannelId(), param));
            }

            shelvesMap.put(cartBean.getValue(),cmsBtShelvesService.selectByChannelIdCart(userInfo.getSelChannelId(), Integer.parseInt(cartBean.getValue())));

            // 是否是使用价格公式
            CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanWithDefault(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.PRICE_CALCULATOR, cartBean.getValue());
            if (priceCalculatorConfig == null) {
                priceCalculatorConfig = new CmsChannelConfigBean(CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA, "0", "0");
            }
            confirmPrice.put(cartBean.getValue(), priceCalculatorConfig.getConfigValue2());
        }
        masterData.put("promotionMap", promotionMap);

        masterData.put("shelvesMap", shelvesMap);

        masterData.put("confirmPrice", confirmPrice);

        // 获取自定义查询用的属性
        masterData.put("custAttsList", cmsSession.getAttribute("_adv_search_props_custAttsQueryList"));

        //标签type
        masterData.put("tagTypeList", Types.getTypeList(TypeConfigEnums.MastType.tagType.getId(), language));

        // 翻译状态
        masterData.put("transStatusList", Types.getTypeList(TypeConfigEnums.MastType.translationStatus.getId(), language));

        // 设置按销量排序的选择列表
        masterData.put("salesTypeList", salesTypeList);

        // 设置BI数据显示的选择列表
        masterData.put("biDataList", biDataList);

        // 判断是否是minimall/usjoi用户
        boolean isMiniMall = Channels.isUsJoi(userInfo.getSelChannelId());
        masterData.put("isminimall", isMiniMall ? 1 : 0);
        if (isMiniMall) {
            List<TypeChannelBean> typeChannelBeenList = TypeChannels.getTypeChannelBeansByTypeValueLang(Constants.comMtTypeChannel.SKU_CARTS_53, userInfo.getSelChannelId(), "cn");
            if (typeChannelBeenList == null || typeChannelBeenList.isEmpty()) {
                $warn("高级检索:getMasterData 未取得供应商列表(Synship.com_mt_value_channel表中未配置) channelid=" + userInfo.getSelChannelId());
            } else {
                List<OrderChannelBean> channelBeanList = new ArrayList<>();
                for (TypeChannelBean typeBean : typeChannelBeenList) {
                    OrderChannelBean channelBean = Channels.getChannel(typeBean.getChannel_id());
                    if (channelBean != null) {
                        channelBeanList.add(channelBean);
                    } else {
                        $warn("高级检索:getMasterData 取得供应商列表 channel不存在 channelid=" + typeBean.getChannel_id());
                    }
                }
                if (channelBeanList.isEmpty()) {
                    $warn("高级检索:getMasterData 取得供应商列表 channel不存在 " + channelBeanList.toString());
                } else {
                    masterData.put("channelList", channelBeanList);
                }
            }
        }

        // 获取店铺列表
        masterData.put("cartList", cartList);

        // 是否自动最终售价同步指导价格
        List<CmsChannelConfigBean> autoPriceCfg = CmsChannelConfigs.getConfigBeans(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_SALE, "");
//        String autoApprovePrice = "0"; // 缺省不自动同步
//        if (autoPriceCfg != null && "1".equals(autoPriceCfg.getConfigValue1())) {
//             autoApprovePrice = "1"; // 自动同步
//        }
//        ;
        if (!ListUtils.isNull(autoPriceCfg)) {
            masterData.put("autoApprovePrice", autoPriceCfg.stream().collect(Collectors.toMap(CmsChannelConfigBean::getConfigCode, o -> o)));
        } else {
            masterData.put("autoApprovePrice", new HashMap<>());
        }

        // 是否是使用价格公式
        CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanNoCode(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.PRICE_CALCULATOR);
//        String isPriceFormula = "0";
        if (priceCalculatorConfig == null) {
            priceCalculatorConfig = new CmsChannelConfigBean(CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA, "0", "0");
        }
        masterData.put("isPriceFormula", priceCalculatorConfig);

        // 取得渠道的通用配置，动态按钮或配置可以直接在此外添加。
        masterData.put("channelConfig", getChannelConfig(userInfo.getSelChannelId(), cartList, language));

        return masterData;
    }

    /**
     * 取得平台店铺的配置（目前控制智能上新的显示与隐藏）
     */
    public Map<String, Object> getChannelConfig(String channelId, Integer cartId, String langId) {
        TypeChannelBean channelConfig = new TypeChannelBean();
        channelConfig.setValue(String.valueOf(cartId));
        return getChannelConfig(channelId, Arrays.asList(channelConfig), langId);
    }

    /**
     * 取得平台店铺的配置（目前控制智能上新的显示与隐藏）
     */
    public Map<String, Object> getChannelConfig(String channelId, List<TypeChannelBean> cartList, String langId) {
        // 取得渠道的通用配置，动态按钮或配置可以直接在此外添加。
        Map<String, Object> configMap = new HashMap<>();
        if (CollectionUtils.isEmpty(cartList)) {
            configMap.put("publishEnabledChannels", "");
        } else {
            List<String> publishEnabledChannels = new ArrayList<>();
            cartList.forEach(cart -> {
                if (sxProductService.isSmartSx(channelId, Integer.valueOf(cart.getValue()))) {
                    publishEnabledChannels.add(cart.getValue());
                }
            });
            configMap.put("publishEnabledChannels", publishEnabledChannels);
        }

        return configMap;
    }

    /**
     * 统计当前查询的product件数（查询条件从画面而来）
     */
    public long countProductCodeList(CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        JongoQuery queryObject = advSearchQueryService.getSearchQuery(searchValue, userInfo.getSelChannelId());
        return productService.countByQuery(queryObject.getQuery(), queryObject.getParameters(), userInfo.getSelChannelId());
    }

    public List<String> getProductCodeList(String channelId, Map<String,Object> searchInfo) {
        CmsSearchInfoBean2 cmsSearchInfoBean2 = new CmsSearchInfoBean2();
        if(searchInfo == null) throw new BusinessException("检索条件不能为null");
        cmsSearchInfoBean2 = JacksonUtil.json2Bean(JacksonUtil.bean2Json(searchInfo),CmsSearchInfoBean2.class);
        return getProductCodeList(channelId, cmsSearchInfoBean2);
    }

    public List<String> getProductCodeList(String channelId, CmsSearchInfoBean2 searchValue) {
        if (searchValue == null) {
            $warn("高级检索 getProductCodeList session中的查询条件为空");
            return new ArrayList<>(0);
        }
        JongoQuery queryObject = advSearchQueryService.getSearchQuery(searchValue, channelId);
        queryObject.setProjection("{'common.fields.code':1,'_id':0}");
        if ($isDebugEnabled()) {
            $debug(String.format("高级检索 获取当前查询的product列表 (session) ChannelId=%s, %s", channelId, queryObject.toString()));
        }

        List<CmsBtProductModel> prodObjList = productService.getList(channelId, queryObject);
        if (prodObjList == null || prodObjList.isEmpty()) {
            $warn("高级检索 getProductCodeList prodObjList为空 查询条件(session)=：" + queryObject.toString());
            return new ArrayList<>(0);
        }

        // 取得符合条件的产品code列表
        List<String> codeList = prodObjList.stream().map(prodObj -> prodObj.getCommonNotNull().getFieldsNotNull().getCode()).filter(prodCode -> (prodCode != null && !prodCode.isEmpty())).collect(Collectors.toList());
        return codeList;
    }

    /**
     * 获取当前查询的product id列表（查询条件从session而来）
     */
    public List<Long> getProductIdList(String channelId, Map<String,Object> searchInfo) {
        CmsSearchInfoBean2 searchValue = new CmsSearchInfoBean2();
        BeanUtils.copyProperties(searchInfo,searchValue);
        if (searchValue == null) {
            $warn("高级检索 getProductIdList session中的查询条件为空");
            return new ArrayList<>(0);
        }
        JongoQuery queryObject = advSearchQueryService.getSearchQuery(searchValue, channelId);
        queryObject.setProjection("{'prodId':1,'_id':0}");
        if ($isDebugEnabled()) {
            $debug(String.format("高级检索 获取当前查询的product id列表 (session) ChannelId=%s, %s", channelId, queryObject.toString()));
        }

        List<CmsBtProductModel> prodList = productService.getList(channelId, queryObject);
        if (prodList == null || prodList.isEmpty()) {
            $warn("高级检索 getProductIdList prodList为空 查询条件(session)=：" + queryObject.toString());
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
     * 获取当前页的product列表信息
     */
    public List<CmsBtProductBean> getProductInfoList(List<String> prodCodeList, CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        if (prodCodeList == null || prodCodeList.isEmpty()) {
            $warn("CmsAdvanceSearchService.getProductInfoList prodCodeList为空");
            return new ArrayList<>(0);
        }
        // 最后再获取本页实际产品信息
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{'common.fields.code':{$in:#}}");
        queryObject.setParameters(prodCodeList);

        String plusStr = (String) cmsSessionBean.getAttribute("_adv_search_props_searchItems");
        if (plusStr == null) {
            plusStr = "";
        }
        queryObject.setProjectionExt(CmsAdvSearchQueryService.searchItems.concat(plusStr).split(";"));
        queryObject.setSort(advSearchQueryService.getSortValue(searchValue));

        List<CmsBtProductBean> prodInfoList = productService.getBeanList(userInfo.getSelChannelId(), queryObject);
        if (prodInfoList == null || prodInfoList.isEmpty()) {
            $warn("CmsAdvanceSearchService.getProductInfoList 检索无结果 param list=" + prodCodeList.toString());
            return new ArrayList<>(0);
        }
        return prodInfoList;
    }

    /**
     * 检查翻译状态/设置状态，由数值转换为文字描述,以及金额格式转换(这里只针对自定义显示列中的项目)
     */
    public void checkProcStatus(List<CmsBtProductBean> productList, String lang) {
        if (productList == null || productList.isEmpty()) {
            $warn("CmsAdvanceSearchService.checkProcStatus productList为空");
            return;
        }
        List<TypeBean> transStatusList = TypeConfigEnums.MastType.translationStatus.getList(lang);
        Map<String, String> transStatusMap = transStatusList.stream().collect(Collectors.toMap((p) -> p.getValue(), (p) -> p.getName()));

        List<TypeBean> catStsList = TypeConfigEnums.MastType.categoryStatus.getList(lang);
        Map<String, String> catStsMap = catStsList.stream().collect(Collectors.toMap((p) -> p.getValue(), (p) -> p.getName()));

        List<TypeBean> hsStsList = TypeConfigEnums.MastType.hsCodeStatus.getList(lang);
        Map<String, String> hsStsMap = hsStsList.stream().collect(Collectors.toMap((p) -> p.getValue(), (p) -> p.getName()));

        for (CmsBtProductModel prodObj : productList) {
            CmsBtProductModel_Field fieldsObj = prodObj.getCommon().getFields();
            if (fieldsObj != null) {
                String stsFlg = StringUtils.trimToNull(fieldsObj.getTranslateStatus());
                if (stsFlg != null) {
                    String stsValueStr = StringUtils.trimToEmpty(transStatusMap.get(stsFlg));
                    fieldsObj.setTranslateStatus(stsValueStr);
                } else {
                    fieldsObj.setTranslateStatus("");
                }

                stsFlg = StringUtils.trimToNull(fieldsObj.getCategoryStatus());
                if (stsFlg != null) {
                    String stsValueStr = StringUtils.trimToEmpty(catStsMap.get(stsFlg));
                    fieldsObj.setCategoryStatus(stsValueStr);
                } else {
                    fieldsObj.setCategoryStatus("");
                }

                stsFlg = StringUtils.trimToNull(fieldsObj.getHsCodeStatus());
                if (stsFlg != null) {
                    String stsValueStr = StringUtils.trimToEmpty(hsStsMap.get(stsFlg));
                    fieldsObj.setHsCodeStatus(stsValueStr);
                } else {
                    fieldsObj.setHsCodeStatus("");
                }
            }
        }
    }

    /**
     * 统计当前查询的group件数（查询条件从画面而来）
     * 其实是统计product表中'platforms.Pxx.mainProductCode'的个数(使用聚合查询)，这里需要确保product表和group表的数据一致
     */
    public long countGroupCodeList(CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        List<JongoAggregate> aggrList = new ArrayList<>();
        String qry1 = cmsBtProductDao.getQueryStr(advSearchQueryService.getSearchQuery(searchValue, userInfo.getSelChannelId()));
        if (qry1 != null && qry1.length() > 0) {
            aggrList.add(new JongoAggregate("{ $match : " + qry1 + " }"));
        }
        String gp1 = "{ $group : { _id : '$platforms.P" + searchValue.getCartId() + ".mainProductCode' } }";
        String gp2 = "{ $group : { _id : null, count: { $sum : 1 } } }";
        aggrList.add(new JongoAggregate(gp1));
        aggrList.add(new JongoAggregate(gp2));
        List<Map<String, Object>> rs = productService.aggregateToMap(userInfo.getSelChannelId(), aggrList);
        if (rs == null || rs.isEmpty()) {
            $warn("高级检索 countGroupCodeList Aggregate统计无结果");
            return 0;
        }
        Map rsMap = rs.get(0);
        if (rsMap == null || rsMap.isEmpty()) {
            $warn("高级检索 countGroupCodeList 统计查询无结果");
            return 0;
        }
        return (Integer) rsMap.get("count");
    }

    /**
     * 设置产品free tag，同时添加该tag的所有上级tag
     */
    public void setProdFreeTagMQ(String channelId, Map<String, Object> params, String modifier, CmsSessionBean cmsSession) {
        List<String> tagPathList = (List<String>) params.get("tagPathList");
        if (tagPathList == null || tagPathList.isEmpty()) {
            $info("CmsAdvanceSearchService：setProdFreeTag 未选择标签,将清空所有自由标签");
        }

        List<String> orgDispTagList = null;
        if (params.get("orgDispTagList") != null) {
            orgDispTagList = (List<String>) params.get("orgDispTagList");
        }

        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }
        List<String> prodCodeList;
        if (isSelAll == 1) {
            CmsSearchInfoBean2 searchValue = new CmsSearchInfoBean2();
            BeanUtils.copyProperties((Map<String, Object>) params.get("searchInfo"),searchValue);
            cmsProductFreeTagsUpdateService.sendMessage(channelId, searchValue, tagPathList, orgDispTagList, modifier);
        } else {
            prodCodeList = (List<String>) params.get("prodIdList");
            cmsProductFreeTagsUpdateService.sendMessage(channelId, prodCodeList, tagPathList, orgDispTagList, modifier);
        }
    }

    /**
     * 获取数据文件内容,什么数据，从前端来的吗？
     */
    public boolean getCodeExcelFile(Map<String, Object> searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean, String language) {
        // 创建文件下载任务
        if (cmsBtExportTaskService.checkExportTaskByUser(userInfo.getSelChannelId(), CmsBtExportTaskService.ADV_SEARCH, userInfo.getUserName()) == 0) {
            HashMap<String, String> channelIdMap = new HashMap<>();
            List<OrderChannelBean> orderChannelList = Channels.getChannelList();
            if (orderChannelList.size() > 0) {
                for (OrderChannelBean bean : orderChannelList) {
                    if (StringUtils.isEmpty(bean.getOrder_channel_id()) || StringUtils.isEmpty(bean.getFull_name()))
                        continue;
                    channelIdMap.put(bean.getOrder_channel_id(), bean.getFull_name());
                }
            }

            CmsBtExportTaskModel taskModel = new CmsBtExportTaskModel();
            taskModel.setStatus(0);
            taskModel.setChannelId(userInfo.getSelChannelId());
            taskModel.setTaskType(CmsBtExportTaskService.ADV_SEARCH);
            // 目前此值只是保存仅供查看，批处理中不用此参数，而是直接用传过去的"searchValue"
            taskModel.setParameter(JacksonUtil.bean2Json(searchValue));
            taskModel.setCreater(userInfo.getUserName());
            taskModel.setCreated(new Date());
            taskModel.setModifier(userInfo.getUserName());
            taskModel.setModified(new Date());
            int rs = cmsBtExportTaskService.add(taskModel);
            $debug("高级检索 创建文件下载任务 结果=%d", rs);

            // 发送MQ消息
            searchValue.put("_channleId", userInfo.getSelChannelId());
            searchValue.put("_userName", userInfo.getUserName());
            searchValue.put("_language", language);
            searchValue.put("_taskId", taskModel.getId());

            Map<String, Object> sessionBean = new HashMap<>();
            sessionBean.put("_adv_search_props_searchItems", cmsSessionBean.getAttribute("_adv_search_props_searchItems"));
            sessionBean.put("_adv_search_customProps", cmsSessionBean.getAttribute("_adv_search_customProps"));
            sessionBean.put("_adv_search_commonProps", cmsSessionBean.getAttribute("_adv_search_commonProps"));
            sessionBean.put("_adv_search_selSalesType", cmsSessionBean.getAttribute("_adv_search_selSalesType"));
            sessionBean.put("_adv_search_selBiDataList", cmsSessionBean.getAttribute("_adv_search_selBiDataList"));
            sessionBean.put("_adv_search_selPlatformDataList", cmsSessionBean.getAttribute("_adv_search_selPlatformDataList"));
            searchValue.put("_sessionBean", sessionBean);

            AdvSearchExportMQMessageBody advSearchExportMQMessageBody = new AdvSearchExportMQMessageBody();
            advSearchExportMQMessageBody.setChannelId(userInfo.getSelChannelId());
            advSearchExportMQMessageBody.setCmsBtExportTaskId(taskModel.getId());
            advSearchExportMQMessageBody.setSearchValue(searchValue);
            advSearchExportMQMessageBody.setChannelIdMap(channelIdMap);
            advSearchExportMQMessageBody.setSender(userInfo.getUserName());

            Gson gson = new Gson();
            String strAdvSearchExportMQMessageBody = gson.toJson(advSearchExportMQMessageBody);
            System.out.print("###############################");
            System.out.println(strAdvSearchExportMQMessageBody);
            System.out.print("###############################");

            cmsMqSenderService.sendMessage(advSearchExportMQMessageBody);
            return true;
        } else {
            return false;
        }
    }
}
