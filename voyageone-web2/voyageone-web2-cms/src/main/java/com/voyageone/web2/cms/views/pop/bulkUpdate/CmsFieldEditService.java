package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Carts;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

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
