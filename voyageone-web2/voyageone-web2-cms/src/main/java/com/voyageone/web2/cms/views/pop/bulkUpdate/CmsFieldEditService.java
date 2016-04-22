package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if (CmsConstants.optionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())
                    || CmsConstants.optionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
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
        String prop_id = prop.get("id").toString();
        List<Long> prodIdList = CommonUtil.changeListType((ArrayList<Integer>) params.get("productIds"));

        for(Long productId : prodIdList) {

            Object[] field = getPropValue(params);
            // 获取产品的信息
            CmsBtProductModel productModel = productService.getProductById(userInfo.getSelChannelId(), productId);

            // TODO 批量更新操作重置approved->ready这步暂时不执行,因为运营如果批量操作再批量approved,工作量比较大,以后有需要时再放开
            // 更新状态以外的属性时,check产品状态如果为Approved,则将产品状态设置成Ready
//            if (!"status".equals(prop_id) && CmsConstants.productStatus.APPROVED.equals(productModel.getFields().getStatus()))
//                productModel.getFields().setStatus(CmsConstants.productStatus.READY);

            // 处理如果是批量更新status,如果该产品以前就是approved,则不做处理
            if ("status".equals(prop_id)
                    && CmsConstants.productStatus.APPROVED.equals(productModel.getFields().getStatus()))
                break;

            if ("platformActive".equals(prop_id)) {
                Map platform = new HashMap();
                if ("Onsale".equals(field[1].toString())) {
                    platform.put("platformActive", com.voyageone.common.CmsConstants.PlatformActive.Onsale.name());
                } else if ("Instock".equals(field[1].toString())) {
                    platform.put("platformActive", com.voyageone.common.CmsConstants.PlatformActive.Instock.name());
                }

                // 更新group
                productGroupService.saveGroups(userInfo.getSelChannelId(), productModel.getFields().getCode(), cartId, platform);
            } else {
                productModel.getFields().setAttribute(field[0].toString(), field[1]);
            }

            ProductUpdateBean updateRequest = new ProductUpdateBean();
            updateRequest.setProductModel(productModel);
            updateRequest.setIsCheckModifed(false);
            updateRequest.setModifier(userInfo.getUserName());

            productService.updateProduct(userInfo.getSelChannelId(), updateRequest);
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
        if (CmsConstants.optionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())) {
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
        } else if (CmsConstants.optionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
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
