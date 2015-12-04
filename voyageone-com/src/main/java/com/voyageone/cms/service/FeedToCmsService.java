package com.voyageone.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.cms.service.dao.CmsBtFeedProductImageDao;
import com.voyageone.cms.service.model.CmsBtFeedProductImageModel;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import com.voyageone.cms.service.dao.mongodb.CmsMtFeedCategoryTreeDao;
import com.voyageone.cms.service.dao.mongodb.CmsBtFeedInfoDao;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by james.li on 2015/11/26.
 */
@Service
public class FeedToCmsService {
    @Autowired
    private CmsMtFeedCategoryTreeDao feedCategoryDao;

    @Autowired
    private CmsBtFeedInfoDao feedProductDao;

    @Autowired
    private CmsBtFeedProductImageDao cmsBtFeedProductImageDao;

    private String modifier;

    /**
     * 获取feed类目
     *
     * @param channelId
     * @return
     */
    public CmsMtFeedCategoryTreeModel getFeedCategory(String channelId) {
        CmsMtFeedCategoryTreeModel category = feedCategoryDao.selectFeedCategory(channelId);
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
     * @param channelId
     * @return
     */
    public List<Map> getFinallyCategories(String channelId){

        CmsMtFeedCategoryTreeModel category = getFeedCategory(channelId);
        Object jsonObj = JsonPath.parse(category.getCategoryTree()).json();
        List<Map> child = JsonPath.read(jsonObj, "$..child[?(@.isChild == 1)]");
        return child;
    }
    /**
     * 设定feed类目
     *
     * @param tree
     */
    private void setFeedCategory(CmsMtFeedCategoryTreeModel tree) {
        feedCategoryDao.update(tree);
    }

    /**
     * 根据category从tree中找到节点
     *
     * @param tree
     * @param cat
     * @return
     */
    private Map findCategory(List<Map> tree, String cat) {
        Object jsonObj = JsonPath.parse(tree).json();
        List<Map> child = JsonPath.read(jsonObj, "$..child[?(@.path == '" + cat.replace("'", "\\\'") + "')]");
        if (child.size() == 0) {
            child = JsonPath.read(jsonObj, "$..*[?(@.path == '" + cat.replace("'", "\\\'") + "')]");
        }
        return child == null || child.size() == 0 ? null : child.get(0);
    }

    /**
     * 追加一个类目
     *
     * @param tree
     * @param category
     */
    private List<Map> addCategory(List<Map> tree, String category) {
        String[] c = category.split("-");
        String temp = "";
        Map befNode = null;
        for (int i = 0; i < c.length; i++) {
            temp += c[i];
            Map node = findCategory(tree, temp);
            if (node == null) {
                Map newNode = new HashMap<>();
                newNode.put("name",c[i]);
                newNode.put("cid", temp);
                newNode.put("path", temp);
                newNode.put("child", new ArrayList<>());
                if (i == c.length - 1) {
                    newNode.put("isChild", 1);
                }
                if (befNode == null) {
                    tree.add(newNode);
                } else {
                    ((List<Map>) befNode.get("child")).add(newNode);
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
     * @param channelId
     * @param category
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
     * 更新code信息如果不code不存在会新建
     *
     * @param products
     * @return
     */
    public Map updateProduct(String channelId, List<CmsBtFeedInfoModel> products, String modifier) {
        this.modifier = modifier;
        List<String> existCategory = new ArrayList<>();
        List<CmsBtFeedInfoModel> failProduct = new ArrayList<>();
        List<CmsBtFeedInfoModel> succeedProduct = new ArrayList<>();
        Map attributeMtDatas = new HashMap<>();
        for (CmsBtFeedInfoModel product : products) {
            try {

                String category = product.getCategory();

                // 判断是否追加一个新的类目
                if (existCategory.contains(category) == false) {
                    addCategory(channelId, category);
                    existCategory.add(category);
                }
                List<String> imageUrls = new ArrayList<>();
                imageUrls = product.getImage();

                // 把Image中的Path删除只保留文件名
                product.setImage(product.getImage().stream().map(image -> image.substring(image.lastIndexOf("/") + 1)).collect(toList()));
                CmsBtFeedInfoModel befproduct = feedProductDao.selectProductByCode(channelId, product.getCode());
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
                }
                product.setModified(DateTimeUtil.getNow());
                product.setModifier(this.modifier);
                product.setUpdFlg(0);
                feedProductDao.update(product);

                List<CmsBtFeedProductImageModel> imageModels = new ArrayList<>();
                imageUrls.forEach(s -> {
                    imageModels.add(new CmsBtFeedProductImageModel(channelId, s, this.modifier));
                });
                cmsBtFeedProductImageDao.updateImagebyUrl(imageModels);

                Map attributeMtData;
                if (attributeMtDatas.get(category) == null) {
                    attributeMtData = new HashMap<>();
                    attributeMtDatas.put(category, attributeMtData);
                } else {
                    attributeMtData = (Map) attributeMtDatas.get(category);
                }
                attributeMtDataMake(attributeMtData, product);
                succeedProduct.add(product);
            } catch (Exception e) {
                e.printStackTrace();
                failProduct.add(product);
            }
        }

        // 更新类目中属性
        for (Object key : attributeMtDatas.keySet()) {
            updateFeedCategoryAttribute(channelId, (Map) attributeMtDatas.get(key), key.toString());
        }

        Map response = new HashMap<>();
        response.put("succeed", succeedProduct);
        response.put("fail", failProduct);
        return response;
    }

    /**
     * 把一个code下的属性抽出存放到类目中
     *
     * @param attributeMtData
     * @param product
     */
    private void attributeMtDataMake(Map attributeMtData, CmsBtFeedInfoModel product) {
        Map map = product.getAttribute();
        if (map == null) return;
        for (Object key : map.keySet()) {
            if (attributeMtData.containsKey(key)) {
                List<String> value = (List<String>) attributeMtData.get(key);
                value.addAll((List<String>)map.get(key));
                attributeMtData.put(key, value.stream().distinct().collect(toList()));
            } else {
                List<String> value = new ArrayList<String>();
                value.addAll((List<String>)map.get(key));
                attributeMtData.put(key, value);
            }
        }
    }

    /**
     * 属性的基本数据保存到类目中
     *
     * @param channelId
     * @param attribute
     * @param category
     */
    private void updateFeedCategoryAttribute(String channelId, Map attribute, String category) {

        CmsMtFeedCategoryTreeModel categorytree = feedCategoryDao.selectFeedCategory(channelId);
        List<Map> tree = categorytree.getCategoryTree();
        Map node = findCategory(tree, category);
        if (node.get("attribute") == null) {
            node.put("attribute", attribute);
        } else {
            Map oldAtt = (Map) node.get("attribute");
            for (Object key : attribute.keySet()) {
                if (oldAtt.containsKey(key)) {
                    List<String> value = (List<String>) attribute.get(key);
                    ((List<String>) oldAtt.get(key)).addAll(value);
                    oldAtt.put(key, ((List<String>) oldAtt.get(key)).stream().map(s -> s.trim()).distinct().collect(toList()));
                } else {
                    oldAtt.put(key, attribute.get(key));
                }
            }
        }
        feedCategoryDao.update(categorytree);
    }

}
