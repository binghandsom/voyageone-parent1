package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    private static final String FIELD_SKU_CARTS = "skuCarts";

    /**
     * 获取pop画面options.
     */
    public List<CmsMtCommonPropDefModel> getPopOptions(String language, String channel_id) {

        List<CmsMtCommonPropDefModel> modelList = categorySchemaService.getALLCommonPropDef();
        List<CmsMtCommonPropDefModel> resultList = new ArrayList<>();

        for (CmsMtCommonPropDefModel model : modelList) {
            CmsMtCommonPropDefModel resModel = new CmsMtCommonPropDefModel();
            Field field = model.getField();
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
    public void setProductFields(Map<String, Object> params, UserSessionBean userInfo, int cartId) {
        Map<String, Object> prop = (Map<String, Object>) params.get("property");
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        String prop_id = prop.get("id").toString();
        if ("hsCodePrivate".equals(prop_id) || "hsCodeCrop".equals(prop_id)) {
            // 如果是税号更新，则另外处理
            // TODO -- 这里为了兼容新旧业务，同时更新了fields和common.fields，以后必须改过来
            String hsCode = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                hsCode = (String) valObj.get("value");
            }
            if (hsCode == null) {
                hsCode = "";
            }
            Map<String, Object> quyObj10 = new HashMap<String, Object>();
            Map<String, Object> quyObj11 = new HashMap<String, Object>();
            quyObj11.put("$in", productCodes);
            quyObj10.put("common.fields.code", quyObj11);
            Map<String, Object> quyObj20 = new HashMap<String, Object>();
            Map<String, Object> quyObj21 = new HashMap<String, Object>();
            quyObj21.put("$in", productCodes);
            quyObj20.put("fields.code", quyObj21);

            List<Map<String, Object>> orList = new ArrayList<>();
            Map<String, Object> quyObj = new HashMap<String, Object>();
            orList.add(quyObj10);
            orList.add(quyObj20);
            quyObj.put("$or", orList);

            Map<String, Object> updObj1 = new HashMap<String, Object>();
            Map<String, Object> updObj2 = new HashMap<String, Object>();
            updObj2.put("common.fields." + prop_id, hsCode);
            updObj2.put("fields." + prop_id, hsCode);
            updObj1.put("$set", updObj2);
            productService.updateProduct(userInfo.getSelChannelId(), quyObj, updObj1);
            return;
        }

        // 获取更新数据
        Object[] field = getPropValue(params);

        // TODO: 16/4/27 以后改成一个语句批量更新,目前没时间改 
        for (String code : productCodes) {

            // 获取产品的信息
            CmsBtProductModel productModel = productService.getProductByCode(userInfo.getSelChannelId(), code);

            // 处理如果是批量更新status,如果该产品以前就是approved,则不做处理
            if ("status".equals(prop_id)
                    && com.voyageone.common.CmsConstants.ProductStatus.Approved.name().equals(productModel.getFields().getStatus()))
                break;


            // 如果更新的是platformActive,则更新cms_bt_product_groups表
            if ("platformActive".equals(prop_id)) {
                CmsBtProductGroupModel CmsBtProductGroupModel = new CmsBtProductGroupModel();
                if (0 != cartId && 1 != cartId) CmsBtProductGroupModel.setCartId(cartId);
                CmsBtProductGroupModel.setChannelId(userInfo.getSelChannelId());

                // 只要找到对应的
                CmsBtProductGroupModel.setMainProductCode(code);

                // 设置platformActive的状态
                if(com.voyageone.common.CmsConstants.PlatformActive.ToOnSale.name().equals(field[1].toString()))
                    CmsBtProductGroupModel.setPlatformActive(com.voyageone.common.CmsConstants.PlatformActive.ToOnSale);
                else if (com.voyageone.common.CmsConstants.PlatformActive.ToInStock.name().equals(field[1].toString()))
                    CmsBtProductGroupModel.setPlatformActive(com.voyageone.common.CmsConstants.PlatformActive.ToInStock);

                CmsBtProductGroupModel.setModifier(userInfo.getUserName());

                productGroupService.updateGroupsPlatformActiveBympCode(CmsBtProductGroupModel);
            } else {
                productModel.getFields().setAttribute(field[0].toString(), field[1]);
            }

            ProductUpdateBean updateRequest = new ProductUpdateBean();
            updateRequest.setProductModel(productModel);
            updateRequest.setIsCheckModifed(false);
            updateRequest.setModifier(userInfo.getUserName());

            //执行product的carts更新
            if(updateRequest.getProductModel().getFields().getStatus().equals(CmsConstants.ProductStatus.Approved.name())) {

                // 执行carts更新
                List<CmsBtProductModel_Carts> carts = productService.getCarts(updateRequest.getProductModel().getSkus(), updateRequest.getProductModel().getCarts());
                updateRequest.getProductModel().setCarts(carts);
            }

            productService.updateProduct(userInfo.getSelChannelId(), updateRequest);



            CmsBtProductModel newProduct = productService.getProductById(userInfo.getSelChannelId(), productModel.getProdId());

            //执行product上新
            if(newProduct.getFields().getStatus().equals(CmsConstants.ProductStatus.Approved.name())) {

                // 插入上新程序
                productService.insertSxWorkLoad(userInfo.getSelChannelId(), newProduct, userInfo.getUserName());

                // TODO 插入全店特价宝
//                CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
//                cmsBtPromotionCodesBean.setProductId(newProduct.getProdId());
//                cmsBtPromotionCodesBean.setProductCode(newProduct.getFields().getCode());
//                cmsBtPromotionCodesBean.setPromotionPrice(newProduct.getFields().getPriceSaleEd());
//                cmsBtPromotionCodesBean.setPromotionId(0);
//                cmsBtPromotionCodesBean.setNumIid(oldProduct.getGroups().getNumIId());
//                cmsBtPromotionCodesBean.setChannelId(channelId);
//                cmsBtPromotionCodesBean.setCartId(23);
//                cmsBtPromotionCodesBean.setModifier(userName);
//                promotionDetailService.teJiaBaoPromotionUpdate(cmsBtPromotionCodesBean);
            }
        }
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
        if (isSelAll == 1 && (productCodes == null || productCodes.isEmpty())) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $error("没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        String prop_id = (String) params.get("putFlg");
        if (prop_id == null || prop_id.isEmpty()) {
            $error("没有设置上下架操作");
            rsMap.put("ecd", 2);
            return rsMap;
        }
        if (!"1".equals(prop_id) && !"0".equals(prop_id)) {
            $error("没有设置上下架操作");
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

        for (String code : productCodes) {
            // 获取产品的信息
            JomgoUpdate updObj = new JomgoUpdate();
            updObj.setQuery("{'productCodes':#,'channelId':#,'cartId':{$in:#},'platformActive':{$ne:#}}");
            updObj.setUpdate("{$set:{'platformActive':#,'modified':#,'modifier':#}}");
            // 设置platformActive的状态
            if ("1".equals(prop_id)) {
                updObj.setQueryParameters(code, userInfo.getSelChannelId(), cartList, com.voyageone.common.CmsConstants.PlatformActive.ToOnSale);
                updObj.setUpdateParameters(com.voyageone.common.CmsConstants.PlatformActive.ToOnSale, DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
            } else if ("0".equals(prop_id)) {
                updObj.setQueryParameters(code, userInfo.getSelChannelId(), cartList, com.voyageone.common.CmsConstants.PlatformActive.ToInStock);
                updObj.setUpdateParameters(com.voyageone.common.CmsConstants.PlatformActive.ToInStock, DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
            }

            cmsBtProductGroupDao.updateMulti(updObj, userInfo.getSelChannelId());

            // 这里需要确认更新成功后再记录上新操作表
            CmsBtProductModel newProduct = productService.getProductByCode(userInfo.getSelChannelId(), code);
            // 执行product上新
            if (newProduct.getFields().getStatus().equals(CmsConstants.ProductStatus.Approved.name())) {
                // 插入上新程序
                productService.insertSxWorkLoad(userInfo.getSelChannelId(), code, cartList, userInfo.getUserName());
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
        if (isSelAll == 1 && (productCodes == null || productCodes.isEmpty())) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $error("没有code条件 params=" + params.toString());
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
            int idx = 0;
            for (String code : productCodes) {
                if (idx < startIdx) {
                    idx ++;
                    continue;
                }
                idx ++;
                // 获取产品的信息
                CmsBtProductModel productModel = productService.getProductByCode(userInfo.getSelChannelId(), code);
                // 阈值价格 TODO-- 暂无
                double priceLimit = 999;
                List<Map<String, Object>> prodInfoList = new ArrayList<>();

                for (Integer cartIdVal : cartList) {
                    qryStr.append("'platforms.P" + cartIdVal + ".status':{$ne:'Ready',$ne:'Approved'},");
                    CmsBtProductModel_Platform_Cart ptmObj = productModel.getPlatform(cartIdVal);
                    String cartName = Carts.getCart(cartIdVal).getName();
                    List<BaseMongoMap<String, Object>> skuObjList = ptmObj.getSkus();
                    for (BaseMongoMap<String, Object> skuObj : skuObjList) {
                        double priceSale = skuObj.getDoubleAttribute("priceSale");
                        double priceRetail = skuObj.getDoubleAttribute("priceRetail");
                        Map<String, Object> priceInfo = new HashMap<>();
                        if (priceSale < priceRetail) {
                            priceInfo.put("priceRetail", priceRetail);
                        }
                        if (priceRetail > priceLimit) {
                            priceInfo.put("priceLimit", priceLimit);
                        }
                        if (priceSale < priceRetail || priceRetail > priceLimit) {
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
            List<CmsBtProductModel_Carts> carts = productModel.getCarts();

            List<String> strList = new ArrayList<>();
            List<Integer> updCartList = new ArrayList<>();
            for (Integer cartIdVal : cartList) {
                if (productModel.getCommon() == null) {
                    continue;
                }
                CmsBtProductModel_Field field = productModel.getCommon().getFields();
                if (field == null) {
                    continue;
                }
                // 如果该产品以前就是approved,则不做处理
                if (com.voyageone.common.CmsConstants.ProductStatus.Approved.name().equals(field.getStatus())) {
                    break;
                }
                updCartList.add(cartIdVal);
                strList.add("'platforms.P" + cartIdVal + ".status':'Approved','platforms.P" + cartIdVal + ".pStatus':'WaitingPublish'");

                if (carts != null && carts.size() > 0) {
                    for (CmsBtProductModel_Carts cart : carts) {
                        if (Objects.equals(cartIdVal, cart.getCartId())) {
                            cart.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
                        }
                    }
                }
            }

            if (strList.isEmpty()) {
                $debug("产品未更新 code=" + code);
                break;
            }
            String updStr = "{$set:{";
            updStr += StringUtils.join(strList, ',');
            if (carts != null && carts.size() > 0) {
                updStr += ",'carts':#";
            }
            updStr += ",'modified':#,'modifier':#}}";
            JomgoUpdate updObj = new JomgoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setQueryParameters(code);
            updObj.setUpdate(updStr);
            if (carts != null && carts.size() > 0) {
                updObj.setUpdateParameters(carts, DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
            } else {
                updObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
            }

            //执行product的pStatus更新及group的publishStatus更新
            cmsBtProductDao.updateFirst(updObj, userInfo.getSelChannelId());

            updObj.setQuery("{'productCodes':#,'channelId':#,'cartId':{$in:#}}");
            updObj.setQueryParameters(code, userInfo.getSelChannelId(), updCartList);
            updObj.setUpdate("{$set:{'platformStatus':'WaitingPublish','modified':#,'modifier':#}}");
            updObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
            cmsBtProductGroupDao.updateMulti(updObj, userInfo.getSelChannelId());

            // 这里需要确认更新成功后再记录上新操作表
            CmsBtProductModel newProduct = productService.getProductById(userInfo.getSelChannelId(), productModel.getProdId());
            // 执行product上新
            if (newProduct.getFields().getStatus().equals(CmsConstants.ProductStatus.Approved.name())) {
                // 插入上新程序
                productService.insertSxWorkLoad(userInfo.getSelChannelId(), code, cartList, userInfo.getUserName());
            }
        }
        rsMap.put("ecd", 0);
        return rsMap;
    }

    /**
     * 根据request值获取需要更新的Field数据
     */
    private Object[] getPropValue(Map<String, Object> params) {
        try {
//            CmsBtProductModel_Field field = new CmsBtProductModel_Field();
            Object[] field = new Object[2];

            String type = ((Map<String, Object>) params.get("property")).get("type").toString();
//            Field prop = FieldTypeEnum.createField(FieldTypeEnum.getEnum(type));

            switch (FieldTypeEnum.getEnum(type)) {
//                case INPUT:
//                    prop_value = (String)prop.getValue();
//                    break;
//                case MULTIINPUT:
//                    (List<Value>)prop.getValue();
//                    break;
                case SINGLECHECK:
//                    SingleCheckField prop = new SingleCheckField();
//                    BeanUtils.populate(prop, (Map<String, Object>) params.get("property"));
                    Map<String, Object> prop = (Map<String, Object>) params.get("property");
                    String prop_id = prop.get("id").toString();
                    String prop_value = ((Map<String, Object>) prop.get("value")).get("value").toString();
                    field[0] = prop_id;
                    field[1] = prop_value;
                    break;
//                case MULTICHECK:
//                    (List<Value>)prop.getValue(); List<String>
//                        field.put(prop.getId(), ((Value)prop.getValue()).getValue());
//                    break;
//                case COMPLEX:Map<String, Object>
//                    (ComplexValue)prop.getValue();
//                    break;
//                case MULTICOMPLEX:List<Map<String, Object>>
//                    (List<ComplexValue>)prop.getValue();
//                    break;
//                case LABEL:
//                    (String)prop.getValue();
//                    break;
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
        }
        return optionsField;
    }
}
