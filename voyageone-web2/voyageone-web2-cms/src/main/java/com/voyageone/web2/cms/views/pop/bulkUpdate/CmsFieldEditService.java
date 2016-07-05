package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
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
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            updObj.setUpdate("{$set:{'common.fields." + prop_id + "':#}}");
            updObj.setUpdateParameters(hsCode);

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
        updObj.setQuery("{'productCodes':{$in:#},'channelId':#,'cartId':{$in:#},'platformActive':{$ne:#}}");
        updObj.setUpdate("{$set:{'platformActive':#,'modified':#,'modifier':#}}");

        // 设置platformActive的状态
        CmsConstants.PlatformActive statusVal = null;
        if ("1".equals(prop_id)) {
            statusVal = com.voyageone.common.CmsConstants.PlatformActive.ToOnSale;
        } else if ("0".equals(prop_id)) {
            statusVal = com.voyageone.common.CmsConstants.PlatformActive.ToInStock;
        }

        updObj.setQueryParameters(productCodes, userInfo.getSelChannelId(), cartList, statusVal);
        updObj.setUpdateParameters(statusVal, DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
        WriteResult rs = productGroupService.updateMulti(updObj, userInfo.getSelChannelId());
        $debug("批量修改属性.(商品上下架) 结果1=：" + rs.toString());

        for (Integer cartIdVal : cartList) {
            // 这里需要确认更新成功后再记录上新操作表
            JomgoQuery qurObj = new JomgoQuery();
            qurObj.setQuery("{'common.fields.code':{$in:#},'platforms.P#.status':#}");
            qurObj.setParameters(productCodes, cartIdVal, CmsConstants.ProductStatus.Approved);
            qurObj.setProjection("{'common.fields.code':1,'_id':0}");

            List<CmsBtProductModel> prodList = productService.getList(userInfo.getSelChannelId(), qurObj);
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

            if (codeList.size() > 0) {
                // 插入上新程序
                List<Integer> cartIdList = new ArrayList<>(1);
                cartIdList.add(cartIdVal);
                productService.insertSxWorkLoad(userInfo.getSelChannelId(), codeList, cartIdList, userInfo.getUserName());
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

        // 先判断是否是ready状态
        JomgoQuery queryObject = new JomgoQuery();
        StringBuilder qryStr = new StringBuilder();
        qryStr.append("{'common.fields.code':{$in:#},");
        for (Integer cartIdVal : cartList) {
            qryStr.append("$and:[{'platforms.P" + cartIdVal + ".status':{$ne:'Ready'}},{'platforms.P" + cartIdVal + ".status':{$ne:'Approved'}}],");
        }
        qryStr.deleteCharAt(qryStr.length() - 1);
        qryStr.append("}");
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
                    qryStr.append("'platforms.P" + cartIdVal + ".status':{$ne:'Ready',$ne:'Approved'},");
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
            List<Integer> updCartList = new ArrayList<>();
            for (Integer cartIdVal : cartList) {
                // 如果该产品以前就是approved,则不做处理
                if (CmsConstants.ProductStatus.Approved.name().equals(productModel.getPlatformNotNull(cartIdVal).getStatus())) {
                    break;
                }
                updCartList.add(cartIdVal);
                strList.add("'platforms.P" + cartIdVal + ".status':'Approved','platforms.P" + cartIdVal + ".pStatus':'WaitingPublish'");
            }

            if (strList.isEmpty()) {
                $debug("产品未更新 code=" + code);
                continue;
            }
            String updStr = "{$set:{";
            updStr += StringUtils.join(strList, ',');
            updStr += ",'modified':#,'modifier':#}}";
            JomgoUpdate updObj = new JomgoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setQueryParameters(code);
            updObj.setUpdate(updStr);
            updObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());

            //执行product的pStatus更新及group的publishStatus更新
            productService.updateFirstProduct(updObj, userInfo.getSelChannelId());

            updObj.setQuery("{'productCodes':#,'channelId':#,'cartId':{$in:#}}");
            updObj.setQueryParameters(code, userInfo.getSelChannelId(), updCartList);
            updObj.setUpdate("{$set:{'platformStatus':'WaitingPublish','modified':#,'modifier':#}}");
            updObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
            productGroupService.updateMulti(updObj, userInfo.getSelChannelId());

            // 这里需要确认更新成功后再记录上新操作表
            CmsBtProductModel newProduct = productService.getProductById(userInfo.getSelChannelId(), productModel.getProdId());
            // 执行product上新(针对各平台)
            List<Integer> newcartList = new ArrayList<>();
            for (Integer cartIdVal : cartList) {
                // 如果该产品以前就是approved,则不做处理
                if (!CmsConstants.ProductStatus.Approved.name().equals(productModel.getPlatformNotNull(cartIdVal).getStatus())) {
                    continue;
                }
                newcartList.add(cartIdVal);
            }
            List<String> codeList = new ArrayList<>(1);
            codeList.add(code);
            // 插入上新程序
            productService.insertSxWorkLoad(userInfo.getSelChannelId(), codeList, newcartList, userInfo.getUserName());

        }
        rsMap.put("ecd", 0);
        return rsMap;
    }

    /**
     * 根据request值获取需要更新的Field数据
     */
    private Object[] getPropValue(Map<String, Object> params) {
        try {
            Object[] field = new Object[2];
            String type = ((Map<String, Object>) params.get("property")).get("type").toString();

            switch (FieldTypeEnum.getEnum(type)) {
                case SINGLECHECK:
                    Map<String, Object> prop = (Map<String, Object>) params.get("property");
                    String prop_id = prop.get("id").toString();
                    String prop_value = ((Map<String, Object>) prop.get("value")).get("value").toString();
                    field[0] = prop_id;
                    field[1] = prop_value;
                    break;
            }
            return field;
        } catch (Exception e) {
            $error("CmsPropChangeService: ", e);
        }
        return null;
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

        String priceType = (String) params.get("priceType");
        String optionType = (String) params.get("optionType");
        BigDecimal priceValue = new BigDecimal((String) params.get("priceValue"));
        boolean isRoundUp = "1".equals((String) params.get("isRoundUp")) ? true : false;

        // 阀值
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);
        Double breakThreshold = null;
        if (cmsChannelConfigBean != null) {
            breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D + 1.0;
        }

        // 获取产品的信息
        JomgoQuery qryObj = new JomgoQuery();
        qryObj.setQuery("{'common.fields.code':{$in:#},'platforms.P" + cartId + ".skus.0':{$exists:true}}");
        qryObj.setParameters(productCodes);
        qryObj.setProjection("{'common.fields.code':1,'platforms.P" + cartId + ".skus':1,'_id':0}");

        List<JomgoUpdate> bulkList = new ArrayList<>();
        List<CmsBtProductModel> prodObjList = productService.getList(userInfo.getSelChannelId(), qryObj);
        for (CmsBtProductModel prodObj : prodObjList) {
            List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartId).getSkus();
            for (BaseMongoMap skuObj : skuList) {
                // 修改后的最终售价
                Double rs = null;
                if (StringUtils.isEmpty(priceType)) {
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
                    }
                }
                if (rs == null) {
                    $warn("setProductSalePrice: 数据错误 sku=" + skuObj.getStringAttribute("skuCode"));
                    break;
                }
                Object priceRetail = skuObj.get("priceRetail");
                if (priceRetail == null) {
                    $warn("setProductSalePrice: 数据错误 priceRetail为空 sku=" + skuObj.getStringAttribute("skuCode"));
                    break;
                }
                // 指导价
                Double result = 0D;
                if (priceRetail instanceof Double) {
                    result = (Double) priceRetail;
                } else {
                    if (!StringUtil.isEmpty(priceRetail.toString())){
                        result = new Double(priceRetail.toString());
                    } else {
                        $warn("setProductSalePrice: 数据错误 priceRetail格式错误 sku=" + skuObj.getStringAttribute("skuCode"));
                        break;
                    }
                }
                String diffFlg = "1";
                if (rs < result) {
                    diffFlg = "2";
                    $warn("setProductSalePrice: 输入数据错误 低于指导价 sku=" + skuObj.getStringAttribute("skuCode"));
                    rsMap.put("ecd", 2);
                    rsMap.put("prodCode", prodObj.getCommonNotNull().getFieldsNotNull().getCode());
                    rsMap.put("skuCode", skuObj.getStringAttribute("skuCode"));
                    rsMap.put("priceSale", rs);
                    rsMap.put("priceLimit", result);
                    return rsMap;
                } else if (rs > result) {
                    diffFlg = "3";
                }
                if (breakThreshold != null) {
                    double priceLimit = result * breakThreshold;
                    if (rs > priceLimit) {
                        $warn("setProductSalePrice: 输入数据错误 大于阀值 sku=" + skuObj.getStringAttribute("skuCode"));
                        rsMap.put("ecd", 3);
                        rsMap.put("prodCode", prodObj.getCommonNotNull().getFieldsNotNull().getCode());
                        rsMap.put("skuCode", skuObj.getStringAttribute("skuCode"));
                        rsMap.put("priceSale", rs);
                        rsMap.put("priceLimit", priceLimit);
                        return rsMap;
                    }
                }
                skuObj.setAttribute("priceDiffFlg", diffFlg);
            }

            // 更新产品的信息
            JomgoUpdate updObj = new JomgoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setUpdate("{$set:{'platforms.P" + cartId + ".skus':#,'modified':#,'modifier':#}}");
            updObj.setQueryParameters(prodObj.getCommon().getFields().getCode());
            updObj.setUpdateParameters(skuList, DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
            bulkList.add(updObj);
        }
        BulkWriteResult rs = cmsBtProductDao.bulkUpdateWithMap(userInfo.getSelChannelId(), bulkList);
        $debug("批量修改商品价格 结果=：" + rs.toString());

        // TODO--需要记录价格变更履历

        // 再查询这批商品是否可上新
        List<String> codeList = new ArrayList<>();
        qryObj.setQuery("{'common.fields.code':{$in:#},'platforms.P" + cartId + ".skus.0':{$exists:true},'platforms.P" + cartId + ".status':'Approved'}");
        qryObj.setParameters(productCodes);
        qryObj.setProjection("{'common.fields.code':1,'_id':0}");
        prodObjList = productService.getList(userInfo.getSelChannelId(), qryObj);
        for (CmsBtProductModel prodObj : prodObjList) {
            codeList.add(prodObj.getCommon().getFields().getCode());
        }

        if (codeList.size() > 0) {
            // 插入上新程序
            List<Integer> cartIdList = new ArrayList<>(1);
            cartIdList.add(cartId);
            productService.insertSxWorkLoad(userInfo.getSelChannelId(), codeList, cartIdList, userInfo.getUserName());
        }
        rsMap.put("ecd", 0);
        return rsMap;
    }

    private Double getFinalSalePrice(BigDecimal baseVal, String optionType, BigDecimal priceValue, boolean isRoundUp) {
        BigDecimal rs = null;
        if ("=".equals(optionType) || baseVal == null) {
            rs = priceValue;
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
