package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.service.dao.cms.mongo.CmsMtCategoryTreeAllDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryTreeAllService extends BaseService {

    @Autowired
    private CmsMtCategoryTreeAllDao cmsMtCategoryTreeAllDao;

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;
    /**
     * 对一个channelid下的类目追加一个Category
     *
     * @param categoryPath  类目
     */
    public void addCategory(String categoryPath, String modifier) {
        List<String> categorys = Arrays.asList(categoryPath.split(">"));
        // 取得一级类目树

        CmsMtCategoryTreeAllModel categoryTree = getFeedCategoryByCatId(MD5.getMD5(categorys.get(0)));

        if (categoryTree == null) {
            categoryTree = new CmsMtCategoryTreeAllModel();
            categoryTree.setCreated(DateTimeUtil.getNow());
            categoryTree.setCreater(modifier);
            categoryTree.setCatPath(categorys.get(0));
            categoryTree.setCatName(categorys.get(0));
            categoryTree.setCatId(MD5.getMD5(categorys.get(0)));
            categoryTree.setParentCatId("0");
            categoryTree.setIsParent(categorys.size() > 0 ? 1 : 0);
            categoryTree.setChildren(new ArrayList<>());
            if(categorys.size() == 1){
                categoryTree.setModified(DateTimeUtil.getNow());
                categoryTree.setModifier(modifier);
                cmsMtCategoryTreeAllDao.update(categoryTree);
                return;
            }
        }else{
            if(findCategory(categoryTree,categoryPath) != null){
                return;
            }
        }

//        if (addCategory(categoryTree, category) != null) {
//            return;
//        }
        if(addSubCategory(categoryTree, categoryPath)){
            categoryTree.setModified(DateTimeUtil.getNow());
            categoryTree.setModifier(modifier);
            cmsMtCategoryTreeAllDao.update(categoryTree);
        }
    }

    /**
     * 追加子类目
     */
    private boolean addSubCategory(CmsMtCategoryTreeAllModel tree, String category) {
        boolean chgFlg = false;
        String[] c = category.split(">");
        String temp = c[0] + ">";
        CmsMtCategoryTreeAllModel befNode = tree;
        for (int i = 1; i < c.length; i++) {
            temp += c[i];
            CmsMtCategoryTreeAllModel node = findCategory(tree, temp);
            if (node == null) {
                node = new CmsMtCategoryTreeAllModel();
                node.setModifier(null);
                node.setModified(null);
                node.setCreater(null);
                node.setCreated(null);
                node.setCatPath(temp);
                node.setCatName(c[i]);
                node.setCatId(MD5.getMD5(temp));
                node.setParentCatId(befNode == null ? "0" : befNode.getCatId());
                node.setIsParent(i < c.length - 1 ? 1 : 0);
                node.setChildren(new ArrayList<>());
                if(befNode.getChildren() == null) befNode.setChildren(new ArrayList<>());
                befNode.getChildren().add(node);
                befNode = node;
                chgFlg = true;
            } else {
                befNode = node;
            }
            temp += ">";
        }
        return chgFlg;
    }

    /**
     * 根据一级类目获取一级类目下所有的类目
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public CmsMtCategoryTreeAllModel getFeedCategoryByCatId(String catId) {
        return cmsMtCategoryTreeAllDao.selectByCatId(catId);
    }

    /**
     * 获取类目名称对应的类目对象
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public CmsMtCategoryTreeAllModel getFeedCategoryByCatPath(String catPath) {
        List<CmsMtCategoryTreeAllModel> categoryTreeList = getMasterCategory();
       for (CmsMtCategoryTreeAllModel categoryTree : categoryTreeList) {
           CmsMtCategoryTreeAllModel model = findCategory(categoryTree, catPath);
           if (model != null) {
               return  model;
           }
       }
        return  null;
    }

    /**
     * 取得一级类目列表（主数据）
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public List<CmsMtCategoryTreeAllModel> getFstLvlMasterCategory() {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("{'catId':1,'catName':1,'catPath':1,'isParent':1}");
        queryObject.setSort("{'catName':1}");
        return cmsMtCategoryTreeAllDao.select(queryObject);
    }

    /**
     * 取得一级类目列表（所有主数据）
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public List<CmsMtCategoryTreeAllModel> getMasterCategory() {
        return cmsMtCategoryTreeAllDao.select(new JomgoQuery());
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtCategoryTreeAllModel findCategory(CmsMtCategoryTreeAllModel tree, String catPath) {
        for (CmsMtCategoryTreeAllModel CmsMtCategoryTreeAllModel : tree.getChildren()) {
            if (CmsMtCategoryTreeAllModel.getCatPath().equalsIgnoreCase(catPath)) {
                return CmsMtCategoryTreeAllModel;
            }
            if (CmsMtCategoryTreeAllModel.getChildren().size() > 0) {
                CmsMtCategoryTreeAllModel category = findCategory(CmsMtCategoryTreeAllModel, catPath);
                if (category != null) return category;
            }
        }
        return null;
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtCategoryTreeAllModel findCategorySingleSku(CmsMtCategoryTreeAllModel tree, String catPath, List result) {
        for (CmsMtCategoryTreeAllModel CmsMtCategoryTreeAllModel : tree.getChildren()) {
            if (CmsMtCategoryTreeAllModel.getCatPath().equalsIgnoreCase(catPath)) {
                if ("1".equals(CmsMtCategoryTreeAllModel.getSingleSku())) {
                    result.add("1");
                }
                return CmsMtCategoryTreeAllModel;
            }
            if (CmsMtCategoryTreeAllModel.getChildren().size() > 0) {
                CmsMtCategoryTreeAllModel category = findCategorySingleSku(CmsMtCategoryTreeAllModel, catPath, result);
                if (category != null) {
                    if ("1".equals(CmsMtCategoryTreeAllModel.getSingleSku())) {
                        result.add("1");
                    }
                    return category;
                }
            }
        }
        return null;
    }

    /**
     * 根据category从tree中找到节点
     */
    public List<CmsMtCategoryTreeAllModel> findCategoryListByCatId(String rootCatId, int catLevel, String catId) {
        CmsMtCategoryTreeAllModel treeModel = cmsMtCategoryTreeAllDao.selectByCatId(rootCatId == null ? catId : rootCatId);
        if (catLevel > 0) {
            treeModel = findCategoryByCatId(treeModel, catId);
        }
        if (treeModel == null) {
            return new ArrayList<>(0);
        }
        return treeModel.getChildren();
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtCategoryTreeAllModel findCategoryByCatId(CmsMtCategoryTreeAllModel tree, String catId) {
        if (tree == null) {
            return null;
        }
        for (CmsMtCategoryTreeAllModel catTreeModel : tree.getChildren()) {
            if (catTreeModel.getCatId().equalsIgnoreCase(catId)) {
                return catTreeModel;
            }
            if (catTreeModel.getChildren().size() > 0) {
                CmsMtCategoryTreeAllModel category = findCategoryByCatId(catTreeModel, catId);
                if (category != null) return category;
            }
        }
        return null;
    }
}
