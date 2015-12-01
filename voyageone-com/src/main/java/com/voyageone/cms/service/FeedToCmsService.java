package com.voyageone.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.cms.service.dao.CmsBtFeedProductImageDao;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import com.voyageone.cms.service.dao.mongodb.CmsMtFeedCategoryTreeDao;
import com.voyageone.cms.service.dao.mongodb.CmsBtFeedInfoDao;
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

    /**
     * 获取feed类目
     *
     * @param channelId
     * @return
     */
    public List<Map> getFeedCategory(String channelId) {
        CmsMtFeedCategoryTreeModel category = feedCategoryDao.selectFeedCategory(channelId);
        if (category == null) {
            category = new CmsMtFeedCategoryTreeModel();
            category.setChannelId(channelId);
            category.setCategoryTree(new ArrayList<>());
        }
        return category.getCategoryTree();
    }

    /**
     * 设定feed类目
     *
     * @param channelId
     * @param tree
     */
    public void setFeedCategory(String channelId, List<Map> tree) {
        feedCategoryDao.updateFeedCategory(channelId, tree);
    }

    /**
     * 根据category从tree中找到节点
     *
     * @param tree
     * @param cat
     * @return
     */
    public Map findCategory(List<Map> tree, String cat) {
        Object jsonObj = JsonPath.parse(tree).json();
        List<Map> child = JsonPath.read(jsonObj, "$..child[?(@.category == '" + cat.replace("'", "\\\'") + "')]");
        if (child.size() == 0) {
            child = JsonPath.read(jsonObj, "$..*[?(@.category == '" + cat.replace("'", "\\\'") + "')]");
        }
        return child == null || child.size() == 0 ? null : child.get(0);
    }

    /**
     * 追加一个类目
     *
     * @param tree
     * @param category
     */
    public List<Map> addCategory(List<Map> tree, String category) {
        if (findCategory(tree, category) != null) {
            return tree;
        }
        String[] c = category.split("-");
        String temp = "";
        Map befNode = null;
        for (int i = 0; i < c.length; i++) {
            temp += c[i];
            Map node = findCategory(tree, temp);
            if (node == null) {
                Map newNode = new HashMap<>();
                newNode.put("category", temp);
                newNode.put("child", new ArrayList<>());
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
     * @param channelId
     * @param category
     */
    public void addCategory(String channelId, String category) {
        List<Map> tree = getFeedCategory(channelId);
        tree = addCategory(tree,category);
        setFeedCategory(channelId, tree);
    }

    /**
     * 更新code信息如果不code不存在会新建
     *
     * @param products
     * @return
     */
    public Map updateProduct(String channelId, List<CmsBtFeedInfoModel> products) {

        List<String> existCategory = new ArrayList<>();
        List<CmsBtFeedInfoModel> failProduct = new ArrayList<>();
        List<CmsBtFeedInfoModel> succeedProduct = new ArrayList<>();
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
                }
                feedProductDao.updateProduct(product);
                cmsBtFeedProductImageDao.updateImagebyUrl(channelId, imageUrls);
                succeedProduct.add(product);
            }catch (Exception e){
                failProduct.add(product);
            }
        }
        Map response = new HashMap<>();
        response.put("succeed",succeedProduct);
        response.put("fail", failProduct);
        return response;
    }
}
