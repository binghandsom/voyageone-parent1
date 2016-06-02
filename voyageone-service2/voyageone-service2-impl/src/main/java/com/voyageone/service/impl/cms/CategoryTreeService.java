package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.service.dao.cms.mongo.CmsMtCategoryTreeDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryTreeService extends BaseService {

    @Autowired
    private CmsMtCategoryTreeDao cmsMtCategoryTreeDao;

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;

//    /**
//     * 生成并插入cms_mt_category_tree数据（cartId ）
//     */
//    public boolean createCmsMtCategoryTreeFromPlatform(int cartId) {
//        return createCmsMtCategoryTreeFromPlatform(cartId, false);
//    }
//    public boolean createCmsMtCategoryTreeFromPlatform(int cartId, boolean isUpdate) {
//        List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModels = platformCategoryDao.selectPlatformCategoriesByCartId(cartId);
//        for (CmsMtPlatformCategoryTreeModel platformCategoryTreeModel : platformCategoryTreeModels) {
//            CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTreeModel, true);
//            CmsMtCategoryTreeModel dbModel = cmsMtCategoryTreeDao.selectByCatId(cmsMtCategoryTreeModel.getCatId());
//            if (dbModel != null) {
//                if (isUpdate) {
//                    cmsMtCategoryTreeDao.delete(dbModel);
//                    cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
//                }
//            } else {
//                cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 生成并插入cms_mt_category_tree数据（channelId cartId ）
//     */
//    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId) {
//        return createCmsMtCategoryTreeFromPlatform(channelId, cartId, false);
//    }
//
//    /**
//     * 生成并插入cms_mt_category_tree数据（channelId cartId isUpdate）
//     */
//    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId, boolean isUpdate) {
//        List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModels = platformCategoryDao.selectByChannel_CartId(channelId, cartId);
//        for (CmsMtPlatformCategoryTreeModel platformCategoryTreeModel : platformCategoryTreeModels) {
//            CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTreeModel, true);
//            CmsMtCategoryTreeModel dbModel = cmsMtCategoryTreeDao.selectByCatId(cmsMtCategoryTreeModel.getCatId());
//            if (dbModel != null) {
//                if (isUpdate) {
//                    cmsMtCategoryTreeDao.delete(dbModel);
//                    cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
//                }
//            } else {
//                cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
//            }
//        }
//        return true;
//    }
//
//
//    /**
//     * 生成并插入cms_mt_category_tree数据（channelId cartId categoryId ）
//     */
//    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId, String categoryId) {
//        return createCmsMtCategoryTreeFromPlatform(channelId, cartId, categoryId, false);
//    }
//    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId, String categoryId, boolean isUpdate) {
//        CmsMtPlatformCategoryTreeModel platformCategoryTreeModel = platformCategoryDao.selectByChannel_CartId_CatId(channelId, cartId, categoryId);
//        CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTreeModel, true);
//        CmsMtCategoryTreeModel dbModel = cmsMtCategoryTreeDao.selectByCatId(cmsMtCategoryTreeModel.getCatId());
//        if (dbModel != null) {
//            if (isUpdate) {
//                cmsMtCategoryTreeDao.delete(dbModel);
//                cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
//            }
//        } else {
//            cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
//        }
//        return true;
//    }
//
//
//    /**
//     * 生成CmsMtCategoryTreeModel数据
//     */
//    private CmsMtCategoryTreeModel createCategoryTreeModel(CmsMtPlatformCategoryTreeModel platformCategoryTreeModel, boolean isRoot) {
//        CmsMtCategoryTreeModel result = null;
//        if (platformCategoryTreeModel != null) {
//            result = new CmsMtCategoryTreeModel();
//            String catPath = platformCategoryTreeModel.getCatPath();
//            String catId = StringUtils.generCatId(catPath);
//            result.setCatId(catId);
//            result.setCatName(platformCategoryTreeModel.getCatName());
//            result.setCatPath(catPath);
//            int isParent = 0;
//            List<CmsMtPlatformCategoryTreeModel> pChildren = platformCategoryTreeModel.getChildren();
//            if (pChildren != null && pChildren.size()>0) {
//                isParent = 1;
//            }
//            result.setIsParent(isParent);
//            List<CmsMtCategoryTreeModel> children = createCategoryTreeModel(pChildren);
//            for (CmsMtCategoryTreeModel child : children) {
//                child.setParentCatId(result.getCatId());
//            }
//            result.setChildren(children);
//            if (!isRoot) {
//                result.setCreatedStr(null);
//                result.setCreater(null);
//                result.setModifiedStr(null);
//                result.setModifier(null);
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 生成CmsMtCategoryTreeModel List 数据
//     */
//    private List<CmsMtCategoryTreeModel> createCategoryTreeModel(List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeList) {
//        List<CmsMtCategoryTreeModel> result = new ArrayList<>();
//        if (platformCategoryTreeList != null) {
//            for (CmsMtPlatformCategoryTreeModel platformCategoryTree : platformCategoryTreeList) {
//                CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTree, false);
//                result.add(cmsMtCategoryTreeModel);
//            }
//        }
//        return result;
//    }

    /**
     * 对一个channelid下的类目追加一个Category
     *
     * @param categoryPath  类目
     */
    public void addCategory(String categoryPath, String modifier) {
        List<String> categorys = Arrays.asList(categoryPath.split(">"));
        // 取得一级类目树

        CmsMtCategoryTreeModel categoryTree = getFeedCategoryByCatId(MD5.getMD5(categorys.get(0)));

        if (categoryTree == null) {
            categoryTree = new CmsMtCategoryTreeModel();
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
                cmsMtCategoryTreeDao.update(categoryTree);
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
            cmsMtCategoryTreeDao.update(categoryTree);
        }
    }

    /**
     * 追加子类目
     */
    private boolean addSubCategory(CmsMtCategoryTreeModel tree, String category) {
        boolean chgFlg = false;
        String[] c = category.split(">");
        String temp = c[0] + ">";
        CmsMtCategoryTreeModel befNode = tree;
        for (int i = 1; i < c.length; i++) {
            temp += c[i];
            CmsMtCategoryTreeModel node = findCategory(tree, temp);
            if (node == null) {
                node = new CmsMtCategoryTreeModel();
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
     * @return CmsMtCategoryTreeModel
     */
    public CmsMtCategoryTreeModel getFeedCategoryByCatId(String catId) {
        return cmsMtCategoryTreeDao.selectByCatId(catId);
    }

    /**
     * 取得一级类目列表（主数据）
     *
     * @return CmsMtCategoryTreeModel
     */
    public List<CmsMtCategoryTreeModel> getFstLvlMasterCategory() {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("{'catId':1,'catName':1,'catPath':1,'isParent':1}");
        queryObject.setSort("{'catName':1}");
        return cmsMtCategoryTreeDao.select(queryObject);
    }

    /**
     * 取得一级类目列表（所有主数据）
     *
     * @return CmsMtCategoryTreeModel
     */
    public List<CmsMtCategoryTreeModel> getMasterCategory() {
        return cmsMtCategoryTreeDao.select(new JomgoQuery());
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtCategoryTreeModel findCategory(CmsMtCategoryTreeModel tree, String catPath) {
        for (CmsMtCategoryTreeModel CmsMtCategoryTreeModel : tree.getChildren()) {
            if (CmsMtCategoryTreeModel.getCatPath().equalsIgnoreCase(catPath)) {
                return CmsMtCategoryTreeModel;
            }
            if (CmsMtCategoryTreeModel.getChildren().size() > 0) {
                CmsMtCategoryTreeModel category = findCategory(CmsMtCategoryTreeModel, catPath);
                if (category != null) return category;
            }
        }
        return null;
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtCategoryTreeModel findCategorySingleSku(CmsMtCategoryTreeModel tree, String catPath, List result) {
        for (CmsMtCategoryTreeModel CmsMtCategoryTreeModel : tree.getChildren()) {
            if (CmsMtCategoryTreeModel.getCatPath().equalsIgnoreCase(catPath)) {
                if ("1".equals(CmsMtCategoryTreeModel.getSingleSku())) {
                    result.add("1");
                }
                return CmsMtCategoryTreeModel;
            }
            if (CmsMtCategoryTreeModel.getChildren().size() > 0) {
                CmsMtCategoryTreeModel category = findCategorySingleSku(CmsMtCategoryTreeModel, catPath, result);
                if (category != null) {
                    if ("1".equals(CmsMtCategoryTreeModel.getSingleSku())) {
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
    public List<CmsMtCategoryTreeModel> findCategoryListByCatId(String rootCatId, int catLevel, String catId) {
        CmsMtCategoryTreeModel treeModel = cmsMtCategoryTreeDao.selectByCatId(rootCatId == null ? catId : rootCatId);
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
    public CmsMtCategoryTreeModel findCategoryByCatId(CmsMtCategoryTreeModel tree, String catId) {
        if (tree == null) {
            return null;
        }
        for (CmsMtCategoryTreeModel catTreeModel : tree.getChildren()) {
            if (catTreeModel.getCatId().equalsIgnoreCase(catId)) {
                return catTreeModel;
            }
            if (catTreeModel.getChildren().size() > 0) {
                CmsMtCategoryTreeModel category = findCategoryByCatId(catTreeModel, catId);
                if (category != null) return category;
            }
        }
        return null;
    }
}
