package com.voyageone.service.impl.cms.feed;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.TypeRef;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.CmsBtFeedProductImageDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsMtFeedCategoryTreeDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtFeedProductImageModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * feed数据导入CMS中
 *
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-12.
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class FeedToCmsService extends BaseService {

    @Autowired
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    private CmsBtFeedProductImageDao cmsBtFeedProductImageDao;

    private String modifier;

    public static final String URL_FORMAT = "[~@.' '#$%&*_''/‘’^\\()]";
    private final Pattern special_symbol = Pattern.compile(URL_FORMAT);

    /**
     * 获取feed类目
     *
     * @param channelId 渠道ID
     * @return CmsMtFeedCategoryTreeModel
     */
    public CmsMtFeedCategoryTreeModel getFeedCategory(String channelId) {
        CmsMtFeedCategoryTreeModel category = cmsMtFeedCategoryTreeDao.selectFeedCategory(channelId);
        if (category == null) {
            category = new CmsMtFeedCategoryTreeModel();
            category.setChannelId(channelId);
            category.setCategoryTree(new ArrayList<>());
            category.setCreater(modifier);
        }
        return category;
    }

    /**
     * 获取该channel下所有的叶子类目
     *
     * @param channelId 渠道ID
     * @return 所有的叶子类目
     */
    public List<CmsMtFeedCategoryModel> getFinallyCategories(String channelId) {

        CmsMtFeedCategoryTreeModel category = cmsMtFeedCategoryTreeDao.selectFeedCategory(channelId);

        return JsonPath.parse(category.getCategoryTree()).read("$..child[?(@.isChild == 1)]", new TypeRef<List<CmsMtFeedCategoryModel>>() {
        });
    }

    public boolean chkCategoryPathValid(String categoryPath){
        if(categoryPath.length() == categoryPath.lastIndexOf("-")+1){
            return false;
        }
        String category[] = categoryPath.split("-");
        return !StringUtil.isEmpty(category[category.length-1]);
    }

    /**
     * 更新code信息如果不code不存在会新建
     *
     * @param products 产品列表
     */
    @VOTransactional
    public Map<String, List<CmsBtFeedInfoModel>> updateProduct(String channelId, List<CmsBtFeedInfoModel> products, String modifier) {
        this.modifier = modifier;
        List<String> existCategory = new ArrayList<>();
        List<CmsBtFeedInfoModel> failProduct = new ArrayList<>();
        List<CmsBtFeedInfoModel> succeedProduct = new ArrayList<>();
        Map<String, Map<String, List<String>>> attributeMtDatas = new HashMap<>();
        for (CmsBtFeedInfoModel product : products) {
            try {

                String category = product.getCategory();
                if(!chkCategoryPathValid(category)){
                    throw new BusinessException("category 不合法："+category );
                }
                // 判断是否追加一个新的类目
                if (!existCategory.contains(category)) {
                    addCategory(channelId, category);
                    existCategory.add(category);
                }
                List<String> imageUrls;
                imageUrls = product.getImage();

                // 把Image中的Path删除只保留文件名
                if("010".equalsIgnoreCase(channelId) || "012".equalsIgnoreCase(channelId)){
                    product.setImage(product.getImage().stream().map(image -> image.substring(image.lastIndexOf("/") + 1, image.lastIndexOf("."))).collect(toList()));
                }else{
                    int i = 1;
                    List<String> images = new ArrayList<>();
                    for (String  image :product.getImage()){
                        if("015".equalsIgnoreCase(channelId)){
                            images.add(special_symbol.matcher(product.getCode()).replaceAll(Constants.EmptyString) + "-" + i);
                        }else{
                            images.add(channelId + "-" + special_symbol.matcher(product.getCode()).replaceAll(Constants.EmptyString) + "-" + i);
                        }
                        i++;
                    }
                    product.setImage(images);
                }
                CmsBtFeedInfoModel befproduct = cmsBtFeedInfoDao.selectProductByCode(channelId, product.getCode());
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
                }


                product.setModified(DateTimeUtil.getNow());
                product.setModifier(this.modifier);
                product.setUpdFlg(0);
                cmsBtFeedInfoDao.update(product);

                List<CmsBtFeedProductImageModel> imageModels = new ArrayList<>();

                int i = 1;
                for (String  image :imageUrls){
                    CmsBtFeedProductImageModel cmsBtFeedProductImageModel =  new CmsBtFeedProductImageModel(channelId,product.getCode(), image, i, this.modifier);
                    imageModels.add(cmsBtFeedProductImageModel);
                    i++;
                }
                cmsBtFeedProductImageDao.insertImagebyUrl(imageModels);

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
                $error(e.getMessage(), e);
                failProduct.add(product);
            }
        }

        // 更新类目中属性
        for (String key : attributeMtDatas.keySet()) {
            updateFeedCategoryAttribute(channelId, attributeMtDatas.get(key), key);
        }

        Map<String, List<CmsBtFeedInfoModel>> response = new HashMap<>();
        response.put("succeed", succeedProduct);
        response.put("fail", failProduct);
        return response;
    }

    private Map<String,List<String>> attributeMerge(Map<String,List<String>> attribute1, Map<String,List<String>> attribute2){

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
     * 设定feed类目
     */
    private void setFeedCategory(CmsMtFeedCategoryTreeModel tree) {
        cmsMtFeedCategoryTreeDao.update(tree);
    }

    /**
     * 根据category从tree中找到节点
     */
    private Map<String, Object> findCategory(List<Map<String, Object>> tree, String cat) {

        ReadContext ctx = JsonPath.parse(tree);

        List<Map<String, Object>> child = ctx.read("$..child[?(@.path == '" + cat.replace("'", "\\\'") + "')]");

        if (child.size() == 0) {
            child = ctx.read("$..*[?(@.path == '" + cat.replace("'", "\\\'") + "')]");
        }


        return child == null || child.size() == 0 ? null : child.get(0);
    }

    /**
     * 追加一个类目
     */
    private List<Map<String, Object>> addCategory(List<Map<String, Object>> tree, String category) {
        String[] c = category.split("-");
        String temp = "";
        Map<String, Object> befNode = null;
        for (int i = 0; i < c.length; i++) {
            temp += c[i];
            Map<String, Object> node = findCategory(tree, temp);
            if (node == null) {
                Map<String, Object> newNode = new HashMap<>();
                newNode.put("name", c[i]);
                newNode.put("cid", temp);
                newNode.put("path", temp);
                newNode.put("child", new ArrayList<>());
                if (i == c.length - 1) newNode.put("isChild", 1);
                if (befNode == null) {
                    tree.add(newNode);
                } else {
                    ((List<Map>) (befNode.get("child"))).add(newNode);
                }
                befNode = newNode;
            } else {
                befNode = node;
            }
            temp += "-";
        }
        return tree;
    }

    /**
     * 对一个channelid下的类目追加一个Category
     *
     * @param channelId 渠道
     * @param category  类目
     */
    private void addCategory(String channelId, String category) {
        CmsMtFeedCategoryTreeModel categoryTree = getFeedCategory(channelId);
        if (findCategory(categoryTree.getCategoryTree(), category) != null) {
            return;
        }
        categoryTree.setCategoryTree(addCategory(categoryTree.getCategoryTree(), category));
        categoryTree.setModified(DateTimeUtil.getNow());
        categoryTree.setModifier(modifier);
        setFeedCategory(categoryTree);
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

        CmsMtFeedCategoryTreeModel categoryTree = cmsMtFeedCategoryTreeDao.selectFeedCategory(channelId);
        Map<String, Object> node = findCategory(categoryTree.getCategoryTree(), category);

        if (node == null)
            throw new BusinessException(null, String.format("can`t find any category by \"%s\"", category));

        if (node.get("attribute") == null) {
            node.put("attribute", attribute);
            cmsMtFeedCategoryTreeDao.update(categoryTree);
            return;
        }

        Map<String, List<String>> oldAtt = (Map<String, List<String>>) node.get("attribute");

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
        cmsMtFeedCategoryTreeDao.update(categoryTree);
    }
}
