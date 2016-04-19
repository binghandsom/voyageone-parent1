package com.voyageone.service.impl.cms.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedAttributesModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * feed数据导入CMS中 给各个店铺feed解析程序调用插入mongoDB的接口
 *
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-12.
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class FeedToCmsService extends BaseService {

    @Autowired
    private FeedCategoryTreeService feedCategoryTreeService;

    @Autowired
    private FeedInfoLogService feedInfoLogService;

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private CmsMtChannelValuesService cmsMtChannelValuesService;

    @Autowired
    private FeedCategoryAttributeService feedCategoryAttributeService;

//    public static final String URL_FORMAT = "[~@.' '#$%&*_''/‘’^\\()]";
//    private final Pattern special_symbol = Pattern.compile(URL_FORMAT);


    /**
     * 获取该channel下所有的叶子类目
     *
     * @param channelId 渠道ID
     * @return 所有的叶子类目
     */
    public List<CmsMtFeedCategoryModel> getFinallyCategories(String channelId) {

//        CmsMtFeedCategoryTreeModel category = cmsMtFeedCategoryTreeDao.selectFeedCategory(channelId);
//
//        return JsonPath.parse(category.getCategoryTree()).read("$..child[?(@.isChild == 1)]", new TypeRef<List<CmsMtFeedCategoryModel>>() {
//        });
        return null;
    }

    public boolean chkCategoryPathValid(String categoryPath) {
        if (categoryPath.length() == categoryPath.lastIndexOf("-") + 1) {
            return false;
        }
        String category[] = categoryPath.split("-");
        return !StringUtil.isEmpty(category[category.length - 1]);
    }

    /**
     * 更新code信息如果不code不存在会新建
     *
     * @param products 产品列表
     */
    @VOTransactional
    public Map<String, List<CmsBtFeedInfoModel>> updateProduct(String channelId, List<CmsBtFeedInfoModel> products, String modifier) {
        List<String> existCategory = new ArrayList<>();
        List<CmsBtFeedInfoModel> failProduct = new ArrayList<>();
        List<CmsBtFeedInfoModel> succeedProduct = new ArrayList<>();

        //0:brand 1:sizeType 2:productType
        Set<String> brandList = new HashSet<>();
        Set<String> sizeTypeList = new HashSet<>();
        Set<String> productTypeList = new HashSet<>();


        Map<String, Map<String, List<String>>> attributeMtDatas = new HashMap<>();
        for (CmsBtFeedInfoModel product : products) {
            try {

                product.setModified(DateTimeUtil.getNow());
                product.setModifier(modifier);
                product.setUpdFlg(0);

                String category = product.getCategory();
                if (!chkCategoryPathValid(category)) {
                    throw new BusinessException("category 不合法：" + category);
                }

                // 写log表
                feedInfoLogService.insertCmsBtFeedInfoLog(product);
                // 判断是否追加一个新的类目
                if (!existCategory.contains(category)) {
                    feedCategoryTreeService.addCategory(channelId, category, modifier);
                    existCategory.add(category);
                }

                // 以下图片处理在生成主数据是再处理 feed导入不做处理
//                List<String> imageUrls;
//                imageUrls = product.getImage();
//
//                // 把Image中的Path删除只保留文件名
//                if ("010".equalsIgnoreCase(channelId) || "012".equalsIgnoreCase(channelId)) {
//                    product.setImage(product.getImage().stream().map(image -> image.substring(image.lastIndexOf("/") + 1, image.lastIndexOf("."))).collect(toList()));
//                } else {
//                    int i = 1;
//                    List<String> images = new ArrayList<>();
//                    for (String  image :product.getImage()){
//                        if("015".equalsIgnoreCase(channelId)){
//                            images.add(special_symbol.matcher(product.getCode()).replaceAll(Constants.EmptyString) + "-" + i);
//                        }else{
//                            images.add(channelId + "-" + special_symbol.matcher(product.getCode()).replaceAll(Constants.EmptyString) + "-" + i);
//                        }
//                        i++;
//                    }
//                    product.setImage(images);
//                }
                CmsBtFeedInfoModel befproduct = feedInfoService.getProductByCode(channelId, product.getCode());
                if (befproduct != null) {
                    product.set_id(befproduct.get_id());
                    //把之前的sku（新的product中没有的sku）保存到新的product的sku中
                    befproduct.getSkus().forEach(skuModel -> {
                        if (!product.getSkus().contains(skuModel)) {
                            product.getSkus().add(skuModel);
                        }
                    });
                    product.setCreated(befproduct.getCreated());
                    product.setCreater(befproduct.getCreater());
                    product.setAttribute(attributeMerge(product.getAttribute(), befproduct.getAttribute()));
                    if (befproduct.getUpdFlg() == 2) {
                        product.setUpdFlg(2);
                    } else {
                        product.setUpdFlg(0);
                    }
                }
                feedInfoService.updateFeedInfo(product);

                brandList.add(product.getBrand());
                sizeTypeList.add(product.getSizeType());
                productTypeList.add(product.getProductType());

                //// 以下图片处理在生成主数据是再处理 feed导入不做处理
//                List<CmsBtFeedProductImageModel> imageModels = new ArrayList<>();
//
//                int i = 1;
//                for (String  image :imageUrls){
//                    CmsBtFeedProductImageModel cmsBtFeedProductImageModel =  new CmsBtFeedProductImageModel(channelId,product.getCode(), image, i, this.modifier);
//                    imageModels.add(cmsBtFeedProductImageModel);
//                    i++;
//                }
//                cmsBtFeedProductImageDao.insertImagebyUrl(imageModels);

                Map<String, List<String>> attributeMtData;
                if (attributeMtDatas.get(category) == null) {
                    attributeMtData = new HashMap<>();
                    attributeMtDatas.put(category, attributeMtData);
                } else {
                    attributeMtData = attributeMtDatas.get(category);
                }
                attributeMtDataMake(attributeMtData, product);
                succeedProduct.add(product);
            } catch (Exception e) {
                e.printStackTrace();
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                $error(e.getMessage(), e);
                failProduct.add(product);
            }
        }

        // 更新类目中属性
        for (String key : attributeMtDatas.keySet()) {
            updateFeedCategoryAttribute(channelId, attributeMtDatas.get(key), key);
        }

        //0:brand 1:sizeType 2:productType
        insertCmsMtChannelValues(channelId,brandList, 0, modifier);
        insertCmsMtChannelValues(channelId,sizeTypeList, 1, modifier);
        insertCmsMtChannelValues(channelId,productTypeList, 2, modifier);

        Map<String, List<CmsBtFeedInfoModel>> response = new HashMap<>();
        response.put("succeed", succeedProduct);
        response.put("fail", failProduct);
        return response;
    }

    private Map<String, List<String>> attributeMerge(Map<String, List<String>> attribute1, Map<String, List<String>> attribute2) {

        for (String key : attribute1.keySet()) {
            if (attribute2.containsKey(key)) {
                attribute2.put(key, Stream.concat(attribute1.get(key).stream(), attribute2.get(key).stream())
                        .map(String::trim)
                        .distinct()
                        .collect(toList()));
            } else {
                attribute2.put(key, attribute1.get(key));
            }
        }
        return attribute2;
    }

    /**
     * 把一个code下的属性抽出存放到类目中
     *
     * @param attributeMtData 属性
     * @param product         产品
     */
    private void attributeMtDataMake(Map<String, List<String>> attributeMtData, CmsBtFeedInfoModel product) {
        Map<String, List<String>> map = product.getAttribute();
        if (map == null) return;
        for (String key : map.keySet()) {
            if (attributeMtData.containsKey(key)) {
                List<String> value = attributeMtData.get(key);
                value.addAll(map.get(key));
                attributeMtData.put(key, value.stream().distinct().collect(toList()));
            } else {
                List<String> value = new ArrayList<>();
                value.addAll(map.get(key));
                attributeMtData.put(key, value);
            }
        }
    }

    /**
     * 属性的基本数据保存到类目中
     *
     * @param channelId 渠道
     * @param attribute 属性
     * @param category  类目
     */
    private void updateFeedCategoryAttribute(String channelId, Map<String, List<String>> attribute, String category) {

        String catId = MD5.getMD5(category);
        CmsMtFeedAttributesModel cmsBtFeedCategoryAttribute = feedCategoryAttributeService.getCategoryAttributeByCatId(channelId, catId);
        if (cmsBtFeedCategoryAttribute == null) {
            cmsBtFeedCategoryAttribute = new CmsMtFeedAttributesModel();
            cmsBtFeedCategoryAttribute.setChannelId(channelId);
            cmsBtFeedCategoryAttribute.setCatId(catId);
            cmsBtFeedCategoryAttribute.setCatPath(category);
            cmsBtFeedCategoryAttribute.setAttribute(new HashMap<>());
        }


        Map<String, List<String>> oldAtt = cmsBtFeedCategoryAttribute.getAttribute();

        for (String key : attribute.keySet()) {
            if (oldAtt.containsKey(key)) {
                oldAtt.put(key, Stream.concat(attribute.get(key).stream(), oldAtt.get(key).stream())
                        .map(String::trim)
                        .distinct()
                        .collect(toList()));
            } else {
                oldAtt.put(key, attribute.get(key));
            }
        }
        feedCategoryAttributeService.updateAttributes(cmsBtFeedCategoryAttribute);
    }

    private void insertCmsMtChannelValues(String channelId, Set<String> values, int type,String modifier) {
        CmsMtChannelValuesModel cmsMtChannelValuesModel = new CmsMtChannelValuesModel();
        cmsMtChannelValuesModel.setChannelId(channelId);
        cmsMtChannelValuesModel.setType(type);
        cmsMtChannelValuesModel.setModifier(modifier);
        cmsMtChannelValuesModel.setCreater(modifier);
        cmsMtChannelValuesModel.setCreated(DateTimeUtil.getNow());
        values.forEach(s -> {
            cmsMtChannelValuesModel.setKey(s);
            cmsMtChannelValuesModel.setValue(s);
            cmsMtChannelValuesService.insertCmsMtChannelValues(cmsMtChannelValuesModel);
        });

    }
}
