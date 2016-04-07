package com.voyageone.task2.cms.job;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.service.dao.cms.mongo.CmsMtCategorySchemaDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.Context;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-12-16.
 */
public class SxPrepareData {
    public static void main(String[] args) {
        Context conext = Context.getContext();
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        conext.putAttribute("springContext", ctx);

        ProductService productService = ctx.getBean(ProductService.class);
        CmsMtCategorySchemaDao cmsMtCategorySchemaDao = ctx.getBean(CmsMtCategorySchemaDao.class);
        //珠宝/钻石/翡翠/黄金>天然珍珠（新）>胸饰
        //String catId= "54+g5a6dL+mSu+efsy/nv6Hnv6Av6buE6YeRPuWkqeeEtuePjeePoO+8iOaWsO+8iT7og7jppbA=";

        //珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰   sku template 1
        String catId = "2d0e4b1c769c3d21ce8925621deb72b2";

        //女装/女士精品>背心吊带  sku template 3
//        String catId = "5aWz6KOFL+Wls+Wjq+eyvuWTgT7og4zlv4PlkIrluKY=";

        List<CmsBtProductModel> products = new ArrayList<>();
        products.add(createProduct(cmsMtCategorySchemaDao, "200", 1111, 1, "jewelry", catId, true));
        products.add(createProduct(cmsMtCategorySchemaDao, "200", 1111, 2, "jewelry", catId, false));
        productService.insert(products);
        System.out.println("complete");
    }

    public static CmsBtProductModel createProduct(CmsMtCategorySchemaDao cmsMtCategorySchemaDao, String channelId, long groupId, long productId, String brand, String catId, boolean isMain) {
        CmsBtProductModel product = new CmsBtProductModel(channelId);
        product.setProdId(productId);

        product.setCatId(catId);
        product.setCatPath("-100-10000-" + catId + "-");
        String code = "code-" + productId;
//        String code = "code-beixindiaodai-" + productId;
        CmsBtProductModel_Field fields = product.getFields();
        fields.setCode(code);
        fields.setBrand(brand);
        fields.setAttribute("productName", "Stud Zirconia in Sterling Silver " + code);

        fields.setLongTitle("Stud Earrings with Cubic Silver- 长标题" + code);
        fields.setMiddleTitle("Stud Earrings with Cubic Silver- 中标题" + code);
        fields.setShortTitle("Stud Earrings with Cubic Silver- 短标题" + code);

        fields.setModel("model-aa-" + code);
        fields.setColor("Color" + code);
        fields.setOrigin("china-" + code);

        fields.setShortDesCn("Stud Earrings with in Sterling Silver- 简短描述中文" + code);
        fields.setLongDesCn("Stud Earrings with Sterling Silver- 详情描述中文" + code);
        fields.setShortDesEn("Stud Earrings with in Sterling Silver- 简短描述英语" + code);
        fields.setLongDesEn("Stud Earrings with Sterling Silver- 详情描述英语" + code);

        List<CmsBtProductModel_Field_Image> images = fields.getImages1();
        images.add(new CmsBtProductModel_Field_Image("image1", "DTW68F16_001"));
        images.add(new CmsBtProductModel_Field_Image("image1", "DTW68F16_001_a"));
        images.add(new CmsBtProductModel_Field_Image("image1", "DTW68F16_001_c"));
        images.add(new CmsBtProductModel_Field_Image("image1", "DTW68F16_001_b"));
        images.add(new CmsBtProductModel_Field_Image("image1", "DTW68F16_001_c"));

        fields.setPriceSaleSt(500.00);
        fields.setPriceSaleEd(800.00);

        CmsBtProductModel_Group groups = product.getGroups();

        groups.setSalePriceStart(500.00);
        groups.setSalePriceEnd(800.00);

        List<CmsBtProductModel_Group_Platform> platforms = groups.getPlatforms();
        CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform();
        platform.setGroupId(groupId);
        platform.setCartId(23);
        platform.setIsMain(isMain);
        platforms.add(platform);
        platform.setPlatformActive(CmsConstants.PlatformActive.Instock);

        List<CmsBtProductModel_Sku> skus = product.getSkus();
        for (int i=1; i<3; i++) {
            CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
            sku.setSkuCode(code + "-sku-" + i);
            sku.setPriceSale(500.00 + i);
            List<Integer> skuCarts = new ArrayList<>();
            skuCarts.add(21);
            skuCarts.add(23);
            sku.setSkuCarts(skuCarts);
            sku.setAttribute("sku_MarketTime", "2015-05-14");

            if (i%3 == 0) {
                sku.setAttribute("std_size_prop_20509_-1", "L");
            }
            if (i%3 == 1) {
                sku.setAttribute("std_size_prop_20509_-1", "S");
            }
            if (i%3 == 2) {
                sku.setAttribute("std_size_prop_20509_-1", "M");
            }
            skus.add(sku);
        }

        //build schema fields
        CmsMtCategorySchemaModel cmsMtCategorySchemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(catId);
        for (Field field : cmsMtCategorySchemaModel.getFields()) {
            if ("brand".equals(field.getId()) || "images1".equals(field.getId()) ) {
                continue;
            } else {
                Object fieldValue = constructFieldValue(field, 0);
                if (fieldValue != null)
                    fields.setAttribute(field.getId(), fieldValue);
            }
        }

        return product;
    }

    private static Object constructFieldValue(Field field, int extraParam) {
        switch (field.getType()) {
            case INPUT: {
                switch (field.getId()) {
                    case "market_price": {
                        return "700";
                    }
                    case "in_prop_1665536": {
                        return "abcdefg";
                    }
                    case "sell_point_0":
                        return "sp0";
                    case "sell_point_1":
                        return "sp1";
                    case "sell_point_2":
                        return "sp2";
                    case "sell_point_3":
                        return "sp3";
                    case "sell_point_4": {
                        return "sp4";
                    }
                    case "locality_life->expirydate->severalDays":
                        return "2";
                    case "prov":
                    case "city":
                        return "上海";
                    case "diaopai_pic":
                        return "https://img.alicdn.com/imgextra/i1/2183719539/TB2_Q4EgVXXXXccXpXXXXXXXXXX_!!2183719539.jpg";
                    case "description":
                        return "商品详情页描述";
                    case "item_size":
                        return "100";
                    case "item_weight":
                        return "20";
                    case "short_title":
                        return "无线短标题";
                    case "start_time":
                        return "2015-06-05 16:22:39";
                    case "postage_id":
                        return "1364418490";//PA
                    default: {
                        /*
                        if (extraParam != 0) {
                            return field.getName() + extraParam;
                        } else {
                            return field.getName();
                        }
                        */
                    }
                }
                return null;
            }
            case SINGLECHECK: {
                switch (field.getId()) {
                    case "prop_1665536": {
                        return "-1";
                    }
                    case "locality_life->expirydate": {
                        return "3";
                    }
                    case "delivery_way": {
                        return "2";
                    }
                    case "material_prop_name":
                        return "山羊皮";
                    default:
                    {
                        List<Option> options = ((SingleCheckField) field).getOptions();
                        if (options.size() > 0) {
                            List<Option> optionsList = ((SingleCheckField) field).getOptions();
                            String value = optionsList.get(0).getValue();
                            if ("-1".equals(value) && optionsList.size() > 1) {
                                value = ((SingleCheckField) field).getOptions().get(1).getValue();
                            }
                            return value;
                        }
                    }
                }
                return null;
            }
            case MULTICHECK: {
                List<Option> options = ((MultiCheckField) field).getOptions();
                List<String> stringValues = new ArrayList<>();

                if ("delivery_way".equals(field.getId())) {
                    stringValues.add("2");
                    return stringValues;
                }
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
                for (Field subField : ((ComplexField)field).getFields()) {
                    valueMap.put(subField.getId(), constructFieldValue(subField, extraParam));
                }
                return valueMap;
            }
            case MULTICOMPLEX: {
                List<Map<String, Object>> valueMaps = new ArrayList<>();
                int valuesCount = 1;
                for (int i = 0; i<valuesCount; i++) {
                    Map<String, Object> valueMap = new HashMap<>();
                    for (Field subField : ((MultiComplexField)field).getFields()) {
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
