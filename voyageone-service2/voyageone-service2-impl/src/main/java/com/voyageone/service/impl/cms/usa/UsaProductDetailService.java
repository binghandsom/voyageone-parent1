package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.CmsMtBrandsMappingBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CommonSchemaService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by james on 2017/7/18.
 */
@Service
public class UsaProductDetailService extends BaseService {

    @Autowired
    ProductService productService;

    @Autowired
    CommonSchemaService commonSchemaService;


    public Map<String, Object> getMastProductInfo(String channelId, Long prodId) {
        Map<String, Object> result = new HashMap<>();

        // 取得产品信息
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        // 取得该商品的所在group的其他商品的图片
        List<CmsBtProductModel> cmsBtProductGroup = productService.getProductListByModel(channelId, cmsBtProduct.getCommonNotNull().getFieldsNotNull().getModel());
        List<Map<String, Object>> images = new ArrayList<>();
        cmsBtProductGroup.forEach(product -> {
            if (product != null) {
                Map<String, Object> image = new HashMap<>();
                image.put("productCode", product.getCommonNotNull().getFields().getCode());
                String imageName = "";

                if (!ListUtils.isNull(product.getCommon().getFields().getImages1()) && product.getCommon().getFields().getImages1().get(0).size() > 0) {
                    imageName = (String) product.getCommon().getFields().getImages1().get(0).get("image1");
                }
                if (StringUtil.isEmpty(imageName) && !ListUtils.isNull(product.getCommon().getFields().getImages6()) && product.getCommon().getFields().getImages6().get(0).size() > 0) {
                    imageName = (String) product.getCommon().getFields().getImages6().get(0).get("image6");
                }
                image.put("imageName", imageName);
                image.put("prodId", product.getProdId());
                image.put("qty", product.getCommon().getFields().getQuantity());
                images.add(image);
            }
        });

        List<Field> cmsMtCommonFields = commonSchemaService.getComUsSchemaModel().getFields();
        fillFieldOptions(cmsMtCommonFields, channelId, "en");
        CmsBtProductModel_Common productComm = cmsBtProduct.getCommon();

        String productType = productComm.getFields().getProductType();
        productComm.getFields().setProductType(StringUtil.isEmpty(productType) ? "" : productType.trim());
        String sizeType = productComm.getFields().getSizeType();
        productComm.getFields().setSizeType(StringUtil.isEmpty(sizeType) ? "" : sizeType.trim());

        if (productComm != null) {
            FieldUtil.setFieldsValueFromMap(cmsMtCommonFields, cmsBtProduct.getCommon().getFields());
            productComm.put("schemaFields", cmsMtCommonFields);
        }


        Map<String, Object> mastData = new HashMap<>();
        mastData.put("images", images);
        mastData.put("lock", cmsBtProduct.getLock());
        mastData.put("appSwitch", productComm.getFields().getAppSwitch());
        mastData.put("translateStatus", productComm.getFields().getTranslateStatus());

        // 获取各个平台的状态
        List<Map<String, Object>> platformList = new ArrayList<>();
        if (cmsBtProduct.getPlatforms() != null) {
            cmsBtProduct.getPlatforms().forEach((s, platformInfo) -> {
                if (platformInfo.getCartId() == null || platformInfo.getCartId() == 0) {
                    return;
                }
                Map<String, Object> platformStatus = new HashMap<String, Object>();
                platformStatus.put("cartId", platformInfo.getCartId());
                platformStatus.put("pStatus", platformInfo.getpStatus());
                platformStatus.put("status", platformInfo.getStatus());
                platformStatus.put("pPublishError", platformInfo.getpPublishError());
                platformStatus.put("pNumIId", platformInfo.getpNumIId());
                platformStatus.put("cartName", CartEnums.Cart.getValueByID(platformInfo.getCartId() + ""));
                platformStatus.put("pReallyStatus", platformInfo.getpReallyStatus());
                platformStatus.put("pIsMain", platformInfo.getpIsMain());
                platformStatus.put("pPlatformMallId", platformInfo.getpPlatformMallId());
                platformList.add(platformStatus);
            });
        }
        mastData.put("platformList", platformList);

        mastData.put("feedInfo", productService.getCustomProp(cmsBtProduct));
        mastData.put("productCustomIsDisp", cmsBtProduct.getFeed().getProductCustomIsDisp());


        result.put("productComm", productComm);
        result.put("mastData", mastData);
        return result;
    }

    /**
     * 获取产品平台信息
     *
     * @param channelId channelId
     * @param prodId    prodId
     * @param cartId    cartId
     * @return 产品平台信息
     */
    public Map<String, Object> getProductPlatform(String channelId, Long prodId, int cartId, String language) {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getUsPlatform(cartId);
        if (platformCart == null) {
            platformCart = new CmsBtProductModel_Platform_Cart();
            platformCart.setCartId(cartId);
        }

        if (platformCart.getFields() == null) platformCart.setFields(new BaseMongoMap<>());

        // added by morse.lu 2016/09/13 end
        platformCart.put("schemaFields", getSchemaFields(platformCart.getFields(), platformCart.getpCatId(), channelId, cartId, prodId, language, null, platformCart.getpBrandId(), sxProductService.generateStyleCode(cmsBtProduct, cartId)));

        return platformCart;
    }

    /**
     * 填充field选项值.
     */
    private static void fillFieldOptions(List<Field> fields, String channelId, String language) {

        for (Field field : fields) {

            if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())
                    || CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {

                FieldTypeEnum type = field.getType();

                switch (type) {
                    case LABEL:
                        break;
                    case INPUT:
                        break;
                    case SINGLECHECK:
                    case MULTICHECK:
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

                            OptionsField optionsField = (OptionsField) field;
                            optionsField.setOptions(options);
                        } else if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
                            // 获取type channel bean
                            List<TypeChannelBean> typeChannelBeanList;
                            typeChannelBeanList = TypeChannels.getTypeWithLang(field.getId(), channelId, language);

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
                            OptionsField optionsField = (OptionsField) field;
                            optionsField.setOptions(options);
                        }
                        break;
                    default:
                        break;

                }

            }
        }

    }
}
