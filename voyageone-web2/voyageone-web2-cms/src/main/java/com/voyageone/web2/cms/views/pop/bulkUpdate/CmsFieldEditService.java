package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJomgoUpdateList;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
@Service
public class CmsFieldEditService extends BaseAppService {

    @Autowired
    private CategorySchemaService categorySchemaService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsAdvanceSearchService advanceSearchService;
    @Autowired
    private SizeChartService sizeChartService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    private static final String FIELD_SKU_CARTS = "skuCarts";

    /**
     * 获取pop画面options.
     */
    public List<CmsMtCommonPropDefModel> getPopOptions(String language, String channel_id) {
        List<CmsMtCommonPropDefModel> modelList = categorySchemaService.getALLCommonPropDef();
        List<CmsMtCommonPropDefModel> resultList = new ArrayList<>();

        for (CmsMtCommonPropDefModel model : modelList) {
            Field field = model.getField();
            if (field.getIsDisplay() != 1) {
                continue;
            }
            CmsMtCommonPropDefModel resModel = new CmsMtCommonPropDefModel();
            if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())
                    || CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
                OptionsField optionsField = getOptions(field, language, channel_id);
                resModel.setField(optionsField);
            } else {
                resModel.setField(field);
            }
            resultList.add(resModel);
        }
        return resultList;
    }

    /**
     * 批量修改属性.
     */
    public Map<String, Object> setProductFields(Map<String, Object> params, UserSessionBean userInfo, int cartId, CmsSessionBean cmsSession) {
        Map<String, Object> rsMap = new HashMap<>();
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }
        List<String> productCodes = null;
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        } else {
            productCodes = (ArrayList<String>) params.get("productIds");
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        Map<String, Object> prop = (Map<String, Object>) params.get("property");
        String prop_id = prop.get("id").toString();
        if ("hsCodePrivate".equals(prop_id) || "hsCodeCrop".equals(prop_id)) {
            // 税号更新
            String hsCode = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                hsCode = (String) valObj.get("value");
            }
            if (hsCode == null || hsCode.isEmpty()) {
                $warn("没有设置变更项目 params=" + params.toString());
                rsMap.put("ecd", 2);
                return rsMap;
            }

            JomgoUpdate updObj = new JomgoUpdate();
            updObj.setQuery("{'common.fields.code':{$in:#}}");
            updObj.setQueryParameters(productCodes);
            updObj.setUpdate("{$set:{'common.fields." + prop_id + "':#,'common.fields.hsCodeStatus':'1','common.fields.hsCodeSetter':#,'common.fields.hsCodeSetTime':#}}");
            updObj.setUpdateParameters(hsCode, userInfo.getUserName(), DateTimeUtil.getNow());

            WriteResult rs = productService.updateMulti(updObj, userInfo.getSelChannelId());
            $debug("批量更新结果 " + rs.toString());
            rsMap.put("ecd", 0);
            return rsMap;
        }

        rsMap.put("ecd", 0);
        return rsMap;
    }

    /**
     * 批量修改属性.(商品上下架)
     */
    public Map<String, Object> setProductOnOff(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        Map<String, Object> rsMap = new HashMap<>();
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        String prop_id = (String) params.get("putFlg");
        if (prop_id == null || prop_id.isEmpty()) {
            $warn("没有设置上下架操作");
            rsMap.put("ecd", 2);
            return rsMap;
        }
        if (!"1".equals(prop_id) && !"0".equals(prop_id)) {
            $warn("没有设置上下架操作");
            rsMap.put("ecd", 2);
            return rsMap;
        }

        Integer cartId = (Integer) params.get("cartId");
        List<Integer> cartList = null;
        if (cartId == null || cartId == 0) {
            // 表示全平台更新
            // 店铺(cart/平台)列表
            List<TypeChannelBean> cartTypeList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            cartList = cartTypeList.stream().map((cartType) -> NumberUtils.toInt(cartType.getValue())).collect(Collectors.toList());
        } else {
            cartList = new ArrayList<>(1);
            cartList.add(cartId);
        }

        // 更新产品的信息
        JomgoUpdate updObj = new JomgoUpdate();
        updObj.setQuery("{'productCodes':{$in:#},'channelId':#,'cartId':{$in:#}}");
        updObj.setUpdate("{$set:{'platformActive':#,'modified':#,'modifier':#}}");

        // 设置platformActive的状态
        CmsConstants.PlatformActive statusVal = null;
        if ("1".equals(prop_id)) {
            statusVal = com.voyageone.common.CmsConstants.PlatformActive.ToOnSale;
        } else if ("0".equals(prop_id)) {
            statusVal = com.voyageone.common.CmsConstants.PlatformActive.ToInStock;
        }

        updObj.setQueryParameters(productCodes, userInfo.getSelChannelId(), cartList);
        updObj.setUpdateParameters(statusVal, DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
        WriteResult rs = productGroupService.updateMulti(updObj, userInfo.getSelChannelId());
        $debug("批量修改属性.(商品上下架) 结果1=：" + rs.toString());

        for (Integer cartIdVal : cartList) {
            if (productCodes.size() > 0) {
                // 插入上新程序
                $debug("批量修改属性 (商品上下架) 开始记入SxWorkLoad表");
                long sta = System.currentTimeMillis();
                sxProductService.insertSxWorkLoad(userInfo.getSelChannelId(), productCodes, cartIdVal, userInfo.getUserName());
                $debug("批量修改属性 (商品上下架) 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));
            }
        }

        rsMap.put("ecd", 0);
        return rsMap;
    }

    /**
     * 批量修改属性(商品审批)
     */
    public Map<String, Object> setProductApproval(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        Map<String, Object> rsMap = new HashMap<>();
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        Integer cartId = (Integer) params.get("cartId");
        List<Integer> cartList = null;
        if (cartId == null || cartId == 0) {
            // 表示全平台更新
            // 店铺(cart/平台)列表
            List<TypeChannelBean> cartTypeList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            cartList = cartTypeList.stream().map((cartType) -> NumberUtils.toInt(cartType.getValue())).collect(Collectors.toList());
        } else {
            cartList = new ArrayList<>(1);
            cartList.add(cartId);
        }

        // 先判断是否是ready状态（minimall店铺不验证）
        List<Integer> newcartList = new ArrayList<>();
        for (Integer cartIdVal : cartList) {
            TypeChannelBean cartType = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, userInfo.getSelChannelId(), cartIdVal.toString(), "en");
            if (!"3".equals(cartType.getCartType())) {
                newcartList.add(cartIdVal);
            }
        }
        if (newcartList.size() > 0) {
            JomgoQuery queryObject = new JomgoQuery();
            StringBuilder qryStr = new StringBuilder();
            qryStr.append("{'common.fields.code':{$in:#},$or:[");
            for (Integer cartIdVal : newcartList) {
                qryStr.append("{'platforms.P" + cartIdVal + ".status':{$nin:['Ready','Approved']}},");
            }
            qryStr.deleteCharAt(qryStr.length() - 1);
            qryStr.append("]}");
            queryObject.setQuery(qryStr.toString());
            queryObject.setParameters(productCodes);
            queryObject.setProjection("{'common.fields.code':1,'_id':0}");

            List<CmsBtProductModel> prodList = productService.getList(userInfo.getSelChannelId(), queryObject);
            if (prodList != null && prodList.size() > 0) {
                // 存在未ready状态
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
                rsMap.put("ecd", 2);
                rsMap.put("codeList", codeList);
                return rsMap;
            }
        }

        // 检查商品价格 notChkPrice=1时表示忽略价格问题
        Integer notChkPriceFlg = (Integer) params.get("notChkPrice");
        if (notChkPriceFlg == null) {
            notChkPriceFlg = 0;
        }
        if (notChkPriceFlg == 0) {
            Integer startIdx = (Integer) params.get("startIdx");
            if (startIdx == null) {
                startIdx = 0;
            }

            // 阀值
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);
            Double breakThreshold = null;
            if (cmsChannelConfigBean != null) {
                breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D + 1.0;
            }

            int idx = 0;
            for (String code : productCodes) {
                if (idx < startIdx) {
                    idx ++;
                    continue;
                }
                idx ++;
                // 获取产品的信息
                CmsBtProductModel productModel = productService.getProductByCode(userInfo.getSelChannelId(), code);
                List<Map<String, Object>> prodInfoList = new ArrayList<>();
                double priceLimit = 0;

                for (Integer cartIdVal : cartList) {
                    CmsBtProductModel_Platform_Cart ptmObj = productModel.getPlatform(cartIdVal);
                    if (ptmObj == null) {
                        continue;
                    }
                    String cartName = Carts.getCart(cartIdVal).getName();
                    List<BaseMongoMap<String, Object>> skuObjList = ptmObj.getSkus();
                    if (skuObjList == null) {
                        continue;
                    }
                    for (BaseMongoMap<String, Object> skuObj : skuObjList) {
                        double priceSale = skuObj.getDoubleAttribute("priceSale");
                        double priceRetail = skuObj.getDoubleAttribute("priceRetail");
                        Map<String, Object> priceInfo = new HashMap<>();
                        if (priceSale < priceRetail) {
                            priceInfo.put("priceRetail", priceRetail);
                        }
                        if (breakThreshold != null) {
                            priceLimit = priceRetail * breakThreshold;
                        }
                        if (breakThreshold != null && priceSale > priceLimit) {
                            priceInfo.put("priceLimit", priceLimit);
                        }
                        if (priceSale < priceRetail || (breakThreshold != null && priceSale > priceLimit)) {
                            priceInfo.put("priceSale", priceSale);
                            priceInfo.put("skuCode", skuObj.get("skuCode"));
                            priceInfo.put("cartName", cartName);
                            prodInfoList.add(priceInfo);
                        }
                    }
                }
                if (prodInfoList.size() > 0) {
                    rsMap.put("startIdx", idx);
                    rsMap.put("code", code);
                    rsMap.put("infoList", prodInfoList);
                    break;
                }
            }
            if (rsMap.size() > 0) {
                rsMap.put("ecd", 3);
                return rsMap;
            }
        }

        BulkJomgoUpdateList prodBulkList = new BulkJomgoUpdateList(1000, cmsBtProductDao, userInfo.getSelChannelId());
        BulkJomgoUpdateList grpBulkList = new BulkJomgoUpdateList(1000, cmsBtProductGroupDao, userInfo.getSelChannelId());
        List<String> newProdCodeList = new ArrayList<>();
        for (String code : productCodes) {
            // 获取产品的信息
            CmsBtProductModel productModel = productService.getProductByCode(userInfo.getSelChannelId(), code);
            if (productModel.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field field = productModel.getCommon().getFields();
            if (field == null) {
                continue;
            }

            List<String> strList = new ArrayList<>();
            for (Integer cartIdVal : cartList) {
                // 如果该产品以前就是approved,则不更新pStatus
                String prodStatus = productModel.getPlatformNotNull(cartIdVal).getStatus();
                TypeChannelBean cartType = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, userInfo.getSelChannelId(), cartIdVal.toString(), "en");
                if ("3".equals(cartType.getCartType())) {
                    strList.add("'platforms.P" + cartIdVal + ".status':'Approved','platforms.P" + cartIdVal + ".pStatus':'WaitingPublish'");
                } else {
                    if (CmsConstants.ProductStatus.Ready.name().equals(prodStatus)) {
                        strList.add("'platforms.P" + cartIdVal + ".status':'Approved','platforms.P" + cartIdVal + ".pStatus':'WaitingPublish'");
                    } else if (CmsConstants.ProductStatus.Approved.name().equals(prodStatus)) {
                        strList.add("'platforms.P" + cartIdVal + ".status':'Approved'");
                    }
                }
            }

            if (strList.isEmpty()) {
                $debug("产品未更新 code=" + code);
                continue;
            }
            newProdCodeList.add(code);
            String updStr = "{$set:{";
            updStr += StringUtils.join(strList, ',');
            updStr += ",'modified':#,'modifier':#}}";
            // 更新product表的status及pStatus
            JomgoUpdate updObj = new JomgoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setQueryParameters(code);
            updObj.setUpdate(updStr);
            updObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());

            BulkWriteResult rs = prodBulkList.addBulkJomgo(updObj);
            if (rs != null) {
                $debug(String.format("商品审批(product表) channelId=%s 执行结果=%s", userInfo.getSelChannelId(), rs.toString()));
            }

            // 更新group表的platformStatus
            JomgoUpdate grpUpdObj = new JomgoUpdate();
            grpUpdObj.setQuery("{'productCodes':#,'channelId':#,'cartId':{$in:#},'platformStatus':{$in:[null,'']}}");
            grpUpdObj.setQueryParameters(code, userInfo.getSelChannelId(), cartList);
            grpUpdObj.setUpdate("{$set:{'platformStatus':'WaitingPublish','modified':#,'modifier':#}}");
            grpUpdObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());

            rs = grpBulkList.addBulkJomgo(grpUpdObj);
            if (rs != null) {
                $debug(String.format("商品审批(group表) channelId=%s 执行结果=%s", userInfo.getSelChannelId(), rs.toString()));
            }
        }

        BulkWriteResult rs = prodBulkList.execute();
        if (rs != null) {
            $debug(String.format("商品审批(product表) channelId=%s 结果=%s", userInfo.getSelChannelId(), rs.toString()));
        }
        rs = grpBulkList.execute();
        if (rs != null) {
            $debug(String.format("商品审批(group表) channelId=%s 结果=%s", userInfo.getSelChannelId(), rs.toString()));
        }

        // 插入上新程序
        for (Integer cartIdVal : cartList) {
            $debug("批量修改属性 (商品审批) 开始记入SxWorkLoad表");
            long sta = System.currentTimeMillis();
            sxProductService.insertSxWorkLoad(userInfo.getSelChannelId(), newProdCodeList, cartIdVal, userInfo.getUserName());
            $debug("批量修改属性 (商品审批) 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));
        }

        rsMap.put("ecd", 0);
        return rsMap;
    }

    /**
     * 返回OptionField数据.
     */
    private OptionsField getOptions (Field field, String language, String channelId) {
        OptionsField optionsField = (OptionsField) field;
        if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())) {
            List<TypeBean> typeBeanList = Types.getTypeList(field.getId(), language);

            // 替换成field需要的样式
            List<Option> options = new ArrayList<>();
            for (TypeBean typeBean : typeBeanList) {
                Option opt = new Option();
                opt.setDisplayName(typeBean.getName());
                opt.setValue(typeBean.getValue());
                options.add(opt);
            }
            optionsField.setOptions(options);
        } else if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
            // 获取type channel bean
            List<TypeChannelBean> typeChannelBeanList;
            if (FIELD_SKU_CARTS.equals(field.getId())) {
                typeChannelBeanList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language);
            } else {
                typeChannelBeanList = TypeChannels.getTypeWithLang(field.getId(), channelId, language);
            }

            // 替换成field需要的样式
            List<Option> options = new ArrayList<>();
            if (typeChannelBeanList != null) {
                for (TypeChannelBean typeChannelBean : typeChannelBeanList) {
                    Option opt = new Option();
                    opt.setDisplayName(typeChannelBean.getName());
                    opt.setValue(typeChannelBean.getValue());
                    options.add(opt);
                }
            }
            optionsField.setOptions(options);
        } else if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_SIZE_CHART.equals(field.getDataSource())) {
            JomgoQuery queryObject = new JomgoQuery();
            //取得收索的条件
            queryObject.setQuery("{\"channelId\": #, \"finish\": \"1\"}");
            queryObject.setParameters(channelId);
            queryObject.setSort("{sizeChartId:-1}");
            //返回数据的类型
            List<CmsBtSizeChartModel> sizeCharList = sizeChartService.getSizeCharts(queryObject);
            List<Option> options = new ArrayList<>();
            for (CmsBtSizeChartModel sizeChart : sizeCharList) {
                Option opt = new Option();
                opt.setDisplayName(sizeChart.getSizeChartName());
                opt.setValue(String.valueOf(sizeChart.getSizeChartId()));
                options.add(opt);
            }
            optionsField.setOptions(options);
        }
        return optionsField;
    }

    /**
     * 批量修改属性.(修改商品最终售价)
     */
    public Map<String, Object> setProductSalePrice(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        $debug("批量修改商品价格 开始处理");
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        Map<String, Object> rsMap = new HashMap<>();
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        Integer cartId = (Integer) params.get("cartId");
        if (cartId == null || cartId == 0) {
            $warn("没有cartId条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        String priceType = StringUtils.trimToNull((String) params.get("priceType"));
        String optionType = StringUtils.trimToNull((String) params.get("optionType"));
        String priceValue = StringUtils.trimToNull((String) params.get("priceValue"));
        boolean isRoundUp = "1".equals((String) params.get("isRoundUp")) ? true : false;

        // 阀值
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);
        double breakThreshold = 0;
        if (cmsChannelConfigBean != null) {
            breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D;
        }

        // 获取产品的信息
        JomgoQuery qryObj = new JomgoQuery();
        qryObj.setQuery("{'common.fields.code':{$in:#},'platforms.P" + cartId + ".skus.0':{$exists:true}}");
        qryObj.setParameters(productCodes);
        qryObj.setProjection("{'common.fields.code':1,'prodId':1,'common.skus.skuCode':1,'common.skus.clientMsrpPrice':1,'common.skus.clientRetailPrice':1,'common.skus.clientNetPrice':1,'platforms.P" + cartId + ".skus':1,'_id':0}");

        List<CmsBtPriceLogModel> priceLogList = new ArrayList<CmsBtPriceLogModel>();
        String skuCode = null;
        List<String> skuCodeList = new ArrayList<>();
        BulkJomgoUpdateList bulkList = new BulkJomgoUpdateList(1000, cmsBtProductDao, userInfo.getSelChannelId());
        List<CmsBtProductModel> prodObjList = productService.getList(userInfo.getSelChannelId(), qryObj);
        $debug("批量修改商品价格 开始批量处理");
        for (CmsBtProductModel prodObj : prodObjList) {
            List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartId).getSkus();
            String prodCode = prodObj.getCommonNotNull().getFieldsNotNull().getCode();
            for (BaseMongoMap skuObj : skuList) {
                // 修改后的最终售价
                Double rs = null;
                if (StringUtils.isEmpty(priceType)) {
                    // 使用固定值
                    if (priceValue == null) {
                        $warn(String.format("setProductSalePrice: 没有填写金额 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        rsMap.put("ecd", 7);
                        return rsMap;
                    }
                    rs = getFinalSalePrice(null, optionType, priceValue, isRoundUp);
                    if (rs != null) {
                        skuObj.setAttribute("priceSale", rs);
                    }
                } else {
                    Object basePrice = skuObj.getAttribute(priceType);
                    if (basePrice != null) {
                        BigDecimal baseVal = new BigDecimal(basePrice.toString());
                        rs = getFinalSalePrice(baseVal, optionType, priceValue, isRoundUp);
                        if (rs != null) {
                            skuObj.setAttribute("priceSale", rs);
                        }
                    } else {
                        $warn(String.format("setProductSalePrice: 缺少数据 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        rsMap.put("ecd", 9);
                        rsMap.put("prodCode", prodCode);
                        rsMap.put("skuCode", skuCode);
                        rsMap.put("priceType", priceType);
                        return rsMap;
                    }
                }
                skuCode = skuObj.getStringAttribute("skuCode");
                if (rs == null) {
                    $warn(String.format("setProductSalePrice: 数据错误 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                    rsMap.put("ecd", 8);
                    rsMap.put("prodCode", prodCode);
                    rsMap.put("skuCode", skuCode);
                    return rsMap;
                }
                Object priceRetail = skuObj.get("priceRetail");
                if (priceRetail == null) {
                    $warn(String.format("setProductSalePrice: 缺少数据 priceRetail为空 code=%s, sku=%s", prodCode, skuCode));
                    rsMap.put("ecd", 9);
                    rsMap.put("prodCode", prodCode);
                    rsMap.put("skuCode", skuCode);
                    rsMap.put("priceType", "priceRetail");
                    return rsMap;
                }
                // 指导价
                Double result = 0D;
                if (priceRetail instanceof Double) {
                    result = (Double) priceRetail;
                } else {
                    if (!StringUtil.isEmpty(priceRetail.toString())){
                        result = new Double(priceRetail.toString());
                    } else {
                        $warn(String.format("setProductSalePrice: 数据错误 priceRetail格式错误 code=%s, sku=%s", prodCode, skuCode));
                        rsMap.put("ecd", 9);
                        rsMap.put("prodCode", prodCode);
                        rsMap.put("skuCode", skuCode);
                        rsMap.put("priceType", "priceRetail");
                        return rsMap;
                    }
                }
                String diffFlg = productSkuService.getPriceDiffFlg(breakThreshold, rs, result);
                if ("2".equals(diffFlg) || "5".equals(diffFlg)) {
                    $warn(String.format("setProductSalePrice: 输入数据错误 低于指导价 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                    rsMap.put("ecd", 2);
                    rsMap.put("prodCode", prodCode);
                    rsMap.put("skuCode", skuCode);
                    rsMap.put("priceSale", rs);
                    rsMap.put("priceLimit", result);
                    return rsMap;
                } else if ("4".equals(diffFlg)) {
                    $warn(String.format("setProductSalePrice: 输入数据错误 大于阈值 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                    rsMap.put("ecd", 3);
                    rsMap.put("prodCode", prodCode);
                    rsMap.put("skuCode", skuCode);
                    rsMap.put("priceSale", rs);
                    rsMap.put("priceLimit", result * (breakThreshold + 1));
                    return rsMap;
                }
                skuCodeList.add(skuCode);
                skuObj.setAttribute("priceDiffFlg", diffFlg);

                CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
                cmsBtPriceLogModel.setChannelId(userInfo.getSelChannelId());
                cmsBtPriceLogModel.setProductId(prodObj.getProdId().intValue());
                cmsBtPriceLogModel.setCode(prodCode);
                cmsBtPriceLogModel.setCartId(cartId);
                cmsBtPriceLogModel.setSku(skuCode);
                cmsBtPriceLogModel.setSalePrice(rs.toString());
                cmsBtPriceLogModel.setMsrpPrice(com.voyageone.common.util.StringUtils.toString(skuObj.getStringAttribute("priceMsrp")));
                cmsBtPriceLogModel.setRetailPrice(result.toString());
                CmsBtProductModel_Sku comSku = prodObj.getCommonNotNull().getSku(skuCode);
                if (comSku == null) {
                    cmsBtPriceLogModel.setClientMsrpPrice("0");
                    cmsBtPriceLogModel.setClientRetailPrice("0");
                    cmsBtPriceLogModel.setClientNetPrice("0");
                } else {
                    cmsBtPriceLogModel.setClientMsrpPrice(com.voyageone.common.util.StringUtils.toString(comSku.getClientMsrpPrice()));
                    cmsBtPriceLogModel.setClientRetailPrice(com.voyageone.common.util.StringUtils.toString(comSku.getClientRetailPrice()));
                    cmsBtPriceLogModel.setClientNetPrice(com.voyageone.common.util.StringUtils.toString(comSku.getClientNetPrice()));
                }
                cmsBtPriceLogModel.setComment("高级检索批量更新");
                cmsBtPriceLogModel.setCreated(new Date());
                cmsBtPriceLogModel.setCreater(userInfo.getUserName());
                cmsBtPriceLogModel.setModified(new Date());
                cmsBtPriceLogModel.setModifier(userInfo.getUserName());
                priceLogList.add(cmsBtPriceLogModel);
            }

            // 更新产品的信息
            JomgoUpdate updObj = new JomgoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setUpdate("{$set:{'platforms.P" + cartId + ".skus':#,'modified':#,'modifier':#}}");
            updObj.setQueryParameters(prodObj.getCommon().getFields().getCode());
            updObj.setUpdateParameters(skuList, DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
            BulkWriteResult rs = bulkList.addBulkJomgo(updObj);
            if (rs != null) {
                $debug(String.format("批量修改商品价格 channelId=%s 执行结果=%s", userInfo.getSelChannelId(), rs.toString()));
            }
        }
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $debug(String.format("批量修改商品价格 channelId=%s 结果=%s", userInfo.getSelChannelId(), rs.toString()));
        }

        // 需要记录价格变更履历
        $debug("批量修改商品价格 开始记入价格变更履历");
        long sta = System.currentTimeMillis();
        int cnt = cmsBtPriceLogService.insertCmsBtPriceLogList(priceLogList);
        $debug("批量修改商品价格 记入价格变更履历结束 结果=" + cnt + " 耗时" + (System.currentTimeMillis() - sta));

        // 插入上新程序
        $debug("批量修改商品价格 开始记入SxWorkLoad表");
        sta = System.currentTimeMillis();
        sxProductService.insertSxWorkLoad(userInfo.getSelChannelId(), productCodes, cartId, userInfo.getUserName());
        $debug("批量修改商品价格 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));

        rsMap.put("ecd", 0);
        return rsMap;
    }

    private Double getFinalSalePrice(BigDecimal baseVal, String optionType, String priceValueStr, boolean isRoundUp) {
        BigDecimal priceValue = null;
        if (priceValueStr != null) {
            priceValue = new BigDecimal(priceValueStr);
        }
        BigDecimal rs = null;
        if ("=".equals(optionType)) {
            if (baseVal == null) {
                rs = priceValue;
            } else {
                rs = baseVal;
            }
        } else if ("+".equals(optionType)) {
            rs = baseVal.add(priceValue);
        } else if ("-".equals(optionType)) {
            rs = baseVal.subtract(priceValue);
        } else if ("*".equals(optionType)) {
            rs = baseVal.multiply(priceValue);
        } else if ("/".equals(optionType)) {
            rs = baseVal.divide(priceValue, 2, BigDecimal.ROUND_CEILING);
        }
        if (rs == null) {
            return null;
        } else {
            if (isRoundUp) {
                return rs.setScale(0, BigDecimal.ROUND_CEILING).doubleValue();
            } else {
                return rs.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
            }
        }
    }
}
