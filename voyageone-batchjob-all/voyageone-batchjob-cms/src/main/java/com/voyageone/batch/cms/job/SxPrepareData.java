package com.voyageone.batch.cms.job;

import com.voyageone.batch.Context;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.*;

/**
 * Created by Leo on 15-12-16.
 */
public class SxPrepareData {
    public static void main(String[] args) {
        Context conext = Context.getContext();
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        conext.putAttribute("springContext", ctx);

        CmsProductService cmsProductService = ctx.getBean(CmsProductService.class);
        CmsMtCategorySchemaDao cmsMtCategorySchemaDao = ctx.getBean(CmsMtCategorySchemaDao.class);
        String catId = "5omL6KGoPuiFleihqA==";

        List<CmsBtProductModel> products = new ArrayList<>();
        products.add(createProduct(cmsMtCategorySchemaDao, "200", 1111, 1, "jewelry", catId, true));
        products.add(createProduct(cmsMtCategorySchemaDao, "200", 1111, 2, "jewelry", catId, false));
        cmsProductService.insert(products);
        System.out.println("complete");
    }

    public static CmsBtProductModel createProduct(CmsMtCategorySchemaDao cmsMtCategorySchemaDao, String channelId, long groupId, long productId, String brand, String catId, boolean isMain) {
        CmsBtProductModel product = new CmsBtProductModel(channelId);
        product.setProdId(productId);

        product.setCatId(catId);
        product.setCatPath("-100-10000-" + catId + "-");
        String code = "code-" + productId;
        CmsBtProductModel_Field fields = product.getFields();
        fields.setCode(code);
        fields.setBrand(brand);
        fields.setAttribute("productName", "Stud Earrings with Cubic Zirconia in Sterling Silver " + code);

        fields.setLongTitle("Stud Earrings with Cubic Zirconia in Sterling Silver- 长标题" + code);
        fields.setMiddleTitle("Stud Earrings with Cubic Zirconia in Sterling Silver- 中标题" + code);
        fields.setShortTitle("Stud Earrings with Cubic Zirconia in Sterling Silver- 短标题" + code);

        fields.setModel("model-aa-" + code);
        fields.setColor("Color" + code);
        fields.setOrigin("china-" + code);

        fields.setShortDesCn("Stud Earrings with Cubic Zirconia in Sterling Silver- 简短描述中文" + code);
        fields.setLongDesCn("Stud Earrings with Cubic Zirconia in Sterling Silver- 详情描述中文" + code);
        fields.setShortDesEn("Stud Earrings with Cubic Zirconia in Sterling Silver- 简短描述英语" + code);
        fields.setLongDesEn("Stud Earrings with Cubic Zirconia in Sterling Silver- 详情描述英语" + code);

        List<CmsBtProductModel_Field_Image> images = fields.getImages1();
        images.add(new CmsBtProductModel_Field_Image("DTW68F16_001"));
        images.add(new CmsBtProductModel_Field_Image("DTW68F16_001_a"));
        images.add(new CmsBtProductModel_Field_Image("DTW68F16_001_c"));
        images.add(new CmsBtProductModel_Field_Image("DTW68F16_001_b"));
        images.add(new CmsBtProductModel_Field_Image("DTW68F16_001_c"));

        fields.setSalePriceStart(500.00);
        fields.setSalePriceEnd(800.00);

        CmsBtProductModel_Group groups = product.getGroups();

        groups.setSalePriceStart(500.00);
        groups.setSalePriceEnd(800.00);

        List<CmsBtProductModel_Group_Platform> platforms = groups.getPlatforms();
        CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform();
        platform.setGroupId(groupId);
        platform.setCartId(23);
        platform.setIsMain(isMain);
        platform.setInstockTime("2015-11-18 16:19:00");
        platform.setProductStatus("InStock");
        platform.setPublishStatus("等待上新");
        platform.setComment("");
        platforms.add(platform);

        List<CmsBtProductModel_Sku> skus = product.getSkus();
        for (int i=1; i<3; i++) {
            CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
            sku.setSkuCode(code + "-sku-" + i);
            sku.setBarcode("1234567890-" + i);
            sku.setPriceSale(500.00 + i);
            List<Integer> skuCarts = new ArrayList<>();
            skuCarts.add(21);
            skuCarts.add(23);
            sku.setSkuCarts(skuCarts);
            skus.add(sku);
        }

        //build schema fields
        CmsMtCategorySchemaModel cmsMtCategorySchemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(catId);
        for (Field field : cmsMtCategorySchemaModel.getFields()) {
            if (!"brand".equals(field.getId()) && !"images1".equals(field.getId()) ) {
                Object fieldValue = constructFieldValue(field, 0);
                fields.setAttribute(field.getId(), fieldValue);
            }
        }

        return product;
    }

    private static Object constructFieldValue(Field field, int extraParam) {
        switch (field.getType()) {
            case INPUT:
                if (extraParam != 0) {
                    return field.getName() + extraParam;
                } else {
                    return field.getName();
                }
            case SINGLECHECK: {
                List<Option> options = ((SingleCheckField) field).getOptions();
                if (options.size() > 0)
                    return ((SingleCheckField) field).getOptions().get(0).getValue();
                return null;
            }
            case MULTICHECK: {
                List<Option> options = ((MultiCheckField) field).getOptions();
                List<String> stringValues = new ArrayList<>();
                int k = 0;
                for (Option option : options) {
                    stringValues.add(option.getValue());
                    if (++k == 2) {
                        break;
                    }
                }
                return stringValues;
            }
            case COMPLEX: {
                Map<String, Object> valueMap = new HashMap<>();
                for (Field subField : ((ComplexField)field).getFieldList()) {
                    valueMap.put(subField.getId(), constructFieldValue(subField, extraParam));
                }
                return valueMap;
            }
            case MULTICOMPLEX: {
                List<Map<String, Object>> valueMaps = new ArrayList<>();
                int valuesCount = 3;
                for (int i = 0; i<valuesCount; i++) {
                    Map<String, Object> valueMap = new HashMap<>();
                    for (Field subField : ((MultiComplexField)field).getFieldList()) {
                        valueMap.put(subField.getId(), constructFieldValue(subField, i+1));
                    }
                    valueMaps.add(valueMap);
                }
                return valueMaps;
            }
            default:
                return null;
        }
    }
}
