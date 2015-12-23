package com.voyageone.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.TypeRef;
import com.mongodb.WriteResult;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.dao.CmsBtFeedProductImageDao;
import com.voyageone.cms.service.dao.mongodb.CmsBtFeedInfoDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtFeedCategoryTreeDao;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import com.voyageone.cms.service.model.CmsBtFeedProductImageModel;
import com.voyageone.cms.service.model.CmsFeedCategoryModel;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
public class FeedToCmsService {

    @Autowired
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    private CmsBtFeedProductImageDao cmsBtFeedProductImageDao;

    private String modifier;

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
    public List<CmsFeedCategoryModel> getFinallyCategories(String channelId) {

        CmsMtFeedCategoryTreeModel category = cmsMtFeedCategoryTreeDao.selectFeedCategory(channelId);

        return JsonPath.parse(category.getCategoryTree()).read("$..child[?(@.isChild == 1)]", new TypeRef<List<CmsFeedCategoryModel>>() {
        });
    }

    /**
     * 更新code信息如果不code不存在会新建
     *
     * @param products 产品列表
     * @return response
     */
    public Map updateProduct(String channelId, List<CmsBtFeedInfoModel> products, String modifier) {
        this.modifier = modifier;
        List<String> existCategory = new ArrayList<>();
        List<CmsBtFeedInfoModel> failProduct = new ArrayList<>();
        List<CmsBtFeedInfoModel> succeedProduct = new ArrayList<>();
        Map<String, Map<String, List<String>>> attributeMtDatas = new HashMap<>();
        for (CmsBtFeedInfoModel product : products) {
            try {

                String category = product.getCategory();

                // 判断是否追加一个新的类目
                if (!existCategory.contains(category)) {
                    addCategory(channelId, category);
                    existCategory.add(category);
                }
                List<String> imageUrls;
                imageUrls = product.getImage();

                // 把Image中的Path删除只保留文件名
                product.setImage(product.getImage().stream().map(image -> image.substring(image.lastIndexOf("/") + 1)).collect(toList()));
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
                }
                product.setModified(DateTimeUtil.getNow());
                product.setModifier(this.modifier);
                product.setUpdFlg(0);
                cmsBtFeedInfoDao.update(product);

                List<CmsBtFeedProductImageModel> imageModels = new ArrayList<>();
                imageUrls.forEach(s -> imageModels.add(new CmsBtFeedProductImageModel(channelId, s, this.modifier)));
                cmsBtFeedProductImageDao.updateImagebyUrl(imageModels);

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
                failProduct.add(product);
            }
        }

        // 更新类目中属性
        for (String key : attributeMtDatas.keySet()) {
            updateFeedCategoryAttribute(channelId, attributeMtDatas.get(key), key);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("succeed", succeedProduct);
        response.put("fail", failProduct);
        return response;
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
    private CmsFeedCategoryModel findCategory(List<CmsFeedCategoryModel> tree, String cat) {

        ReadContext ctx = JsonPath.parse(tree);

        List<CmsFeedCategoryModel> child = ctx.read("$..child[?(@.path == '" + cat.replace("'", "\\\'") + "')]");

        if (child.size() == 0) {
            child = ctx.read("$..*[?(@.path == '" + cat.replace("'", "\\\'") + "')]");
        }

        return child == null || child.size() == 0 ? null : child.get(0);
    }

    /**
     * 追加一个类目
     */
    private List<CmsFeedCategoryModel> addCategory(List<CmsFeedCategoryModel> tree, String category) {
        String[] c = category.split("-");
        String temp = "";
        CmsFeedCategoryModel befNode = null;
        for (int i = 0; i < c.length; i++) {
            temp += c[i];
            CmsFeedCategoryModel node = findCategory(tree, temp);
            if (node == null) {
                CmsFeedCategoryModel newNode = new CmsFeedCategoryModel();
                newNode.setName(c[i]);
                newNode.setCid(temp);
                newNode.setPath(temp);
                newNode.setChild(new ArrayList<>());

                if (i == c.length - 1) newNode.setIsChild(1);

                if (befNode == null) {
                    tree.add(newNode);
                } else {
                    befNode.getChild().add(newNode);
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
        CmsFeedCategoryModel node = findCategory(categoryTree.getCategoryTree(), category);

        if (node == null)
            throw new BusinessException(null, String.format("can`t find any category by \"%s\"", category));

        if (node.getAttribute() == null) {
            node.setAttribute(attribute);
            cmsMtFeedCategoryTreeDao.update(categoryTree);
            return;
        }

        Map<String, List<String>> oldAtt = node.getAttribute();

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
    }

    /**
     * 将类目和其子类目全部转化为扁平的数据流
     *
     * @param feedCategoryTreeModel Feed 类目树
     * @return 扁平化后的类目数据
     */
    public Stream<CmsFeedCategoryModel> flatten(CmsMtFeedCategoryTreeModel feedCategoryTreeModel) {

        return feedCategoryTreeModel.getCategoryTree().stream().flatMap(this::flatten);
    }

    /**
     * 将类目和其子类目全部转化为扁平的数据流
     *
     * @param feedCategoryModels 多个类目
     * @return 扁平化后的类目数据
     */
    public Stream<CmsFeedCategoryModel> flatten(List<CmsFeedCategoryModel> feedCategoryModels) {

        return feedCategoryModels.stream().flatMap(this::flatten);
    }

    /**
     * 将类目和其子类目全部转化为扁平的数据流
     *
     * @param feedCategoryModel 某 Feed 类目
     * @return 扁平化后的类目数据
     */
    public Stream<CmsFeedCategoryModel> flatten(CmsFeedCategoryModel feedCategoryModel) {

        Stream<CmsFeedCategoryModel> feedCategoryModelStream = Stream.of(feedCategoryModel);

        List<CmsFeedCategoryModel> children = feedCategoryModel.getChild();

        if (children != null && children.size() > 0)
            feedCategoryModelStream = Stream.concat(feedCategoryModelStream, children.stream().flatMap(this::flatten));

        return feedCategoryModelStream;
    }

    public WriteResult save(CmsMtFeedCategoryTreeModel treeModel) {

        return cmsMtFeedCategoryTreeDao.update(treeModel);
    }


}
