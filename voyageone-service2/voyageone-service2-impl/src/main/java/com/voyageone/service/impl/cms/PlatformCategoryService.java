package com.voyageone.service.impl.cms;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.WriteResult;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.CmsMtPlatformProductIdListDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtPlatformProductIdListModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author liang 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PlatformCategoryService extends BaseService {

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;

    @Autowired
    private CmsMtPlatformCategorySchemaDao platformCategorySchemaDao;

    @Autowired
    private CmsMtPlatformProductIdListDao platformCategoryProductIdListDao;

    public List<CmsMtPlatformCategoryTreeModel> getPlatformCategories(String channelId, Integer cartId) {
        return platformCategoryDao.selectByChannel_CartId(channelId, cartId);
    }

    public CmsMtPlatformCategorySchemaModel getPlatformCatSchema(String catId, int cartId) {
        return platformCategorySchemaDao.selectPlatformCatSchemaModel(catId, cartId);
    }

    /**
     * 创建各渠道的平台类目层次关系.
     *
     * @param platformCatModelList List<CmsMtPlatformCategoryTreeModel>    平台类目树列表
     * @param cartId               String            平台ID
     * @param channelId            String            渠道ID
     * @param taskName             String            任务名
     * @return List<CmsMtPlatformCategoryTreeModel>    平台类目层次树列表
     */
    public List<CmsMtPlatformCategoryTreeModel> buildPlatformCatTrees(List<CmsMtPlatformCategoryTreeModel> platformCatModelList, String cartId, String channelId, String taskName) {
        // 设置类目层次关系.
        List<CmsMtPlatformCategoryTreeModel> assistPlatformCatList = new ArrayList<>(platformCatModelList);

        List<CmsMtPlatformCategoryTreeModel> removePlatformCatList = new ArrayList<>();

        for (CmsMtPlatformCategoryTreeModel platformCat : platformCatModelList) {
            List<CmsMtPlatformCategoryTreeModel> subPlatformCatgories = new ArrayList<>();
            for (Iterator assIterator = assistPlatformCatList.iterator(); assIterator.hasNext(); ) {

                CmsMtPlatformCategoryTreeModel subPlatformCatItem = (CmsMtPlatformCategoryTreeModel) assIterator.next();
                if (subPlatformCatItem.getParentCatId().equals(platformCat.getCatId())) {
                    subPlatformCatgories.add(subPlatformCatItem);
                    assIterator.remove();
                }
            }
            platformCat.setChildren(subPlatformCatgories);
            if (!"0".equals(platformCat.getParentCatId())) {
                //将所有非顶层类目的引用添加到待删除列表
                removePlatformCatList.add(platformCat);
            } else {
                //设置顶层类目的信息
                platformCat.setChannelId(channelId);
                platformCat.setCartId(Integer.parseInt(cartId));
                platformCat.setCreater(taskName);
                platformCat.setCreated(DateTimeUtil.getNow());
                platformCat.setModifier(taskName);
                platformCat.setModified(DateTimeUtil.getNow());
            }
            platformCat.setChannelId(channelId);
        }

        // 删除掉所有非顶层类目引用,只留下最顶层类目
        platformCatModelList.removeAll(removePlatformCatList);

        return platformCatModelList;
    }

    /**
     * 更新MangoDB类目数据.
     *
     * @param savePlatformCatModels List<CmsMtPlatformCategoryTreeModel> 类目MODEL
     * @param cartId                String            平台ID
     * @param channelId             String         渠道ID
     */
    public void setMangoDBPlatformCatTrees(List<CmsMtPlatformCategoryTreeModel> savePlatformCatModels, String cartId, String channelId) {

        if (savePlatformCatModels != null && savePlatformCatModels.size() > 0) {
            // 删除原有类目信息
            WriteResult delCatRes = platformCategoryDao.deletePlatformCategories(Integer.valueOf(cartId), channelId);
            $info("批量删除类目 CART_ID 为：" + cartId + "  channel id: " + channelId + " 的数据为: " + delCatRes.getN() + "条...");

            $info("保存最新的类目信息: " + savePlatformCatModels.size() + "条记录。");
            // 保存最新的类目信息
            platformCategoryDao.insertWithList(savePlatformCatModels);
        }
    }

    /**
     * 取得类目属性叶子节点数据
     *
     * @param cartId int            平台ID
     * @return List<CmsMtPlatformCategoryTreeModel>    平台类目层次树列表
     */
    public List<CmsMtPlatformCategoryTreeModel> getCmsMtPlatformCategoryTreeModelLeafList(int cartId) {
        List<CmsMtPlatformCategoryTreeModel> allLeaves = new ArrayList<>();
        List<Map> allCategoryLeavesMap = new ArrayList<>();
        Set<String> savedList = new HashSet<>();

        // 从MangoDB中取得cartId对应的类目信息记录
        List<CmsMtPlatformCategoryTreeModel> categoryTrees = this.platformCategoryDao.selectPlatformCategoriesByCartId(cartId);

        // 从MangoDB中取得cartId对应的类目信息叶子记录
        for (CmsMtPlatformCategoryTreeModel root : categoryTrees) {
            Object jsonObj = JsonPath.parse(root.toString()).json();
            List<Map> leavesMap = JsonPath.read(jsonObj, "$..children[?(@.isParent == 0)]");
            allCategoryLeavesMap.addAll(leavesMap);
        }

        // 去掉取得的类目信息叶子记录中catId一样的重复记录
        for (Map leafMap : allCategoryLeavesMap) {
            CmsMtPlatformCategoryTreeModel leafObj = new CmsMtPlatformCategoryTreeModel();
            try {
                BeanUtils.populate(leafObj, leafMap);
            } catch (Exception ex) {
                $error("去掉取得的类目信息叶子记录中的重复记录失败 " + "cart_id:" + cartId);
                throw new BusinessException("去掉取得的类目信息叶子记录中的重复记录失败 " + ex.getMessage());
            }

            String key = leafObj.getCatId();

            // 叶子类目并且不重复
            if (!savedList.contains(key)) {
                savedList.add(key);
                leafObj.setCartId(cartId);
                allLeaves.add(leafObj);
            }
        }

        return allLeaves;
    }

    /**
     * 删除类目属性schema数据
     */
    public int deletePlatformCategorySchemaByCartId(int cartId) {
        WriteResult delResult = platformCategorySchemaDao.deletePlatformCategorySchemaByCartId(cartId);
        return delResult.getN();
    }

    /**
     * 插入类目属性schema数据
     */
    public int insertPlatformCategorySchema(CmsMtPlatformCategorySchemaModel schemaModel) {
        WriteResult insResult = platformCategorySchemaDao.insert(schemaModel);
        return insResult.getN();
    }

    /**
     * 获取指定active状态的平台产品ID列表
     */
    public List<CmsMtPlatformProductIdListModel> getPlatformProductIdList(int active) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("active", active);

        return platformCategoryProductIdListDao.selectList(paramsMap);
    }


    /**
     * 取得一级类目列表（平台数据）
     *
     * @return CmsMtCategoryTreeModel
     */
    public List<CmsMtPlatformCategoryTreeModel> getFstLvlCategory(String channelId, int cartId) {
        return platformCategoryDao.selectByChannel_CartId(channelId, cartId);
    }

    /**
     * 根据category从tree中找到节点
     */
    public List<CmsMtPlatformCategoryTreeModel> findCategoryListByCatId(String channelId, int cartId, String rootCatId, int catLevel, String catId) {
        CmsMtPlatformCategoryTreeModel treeModel = platformCategoryDao.selectByChannel_CartId_CatId(channelId, cartId, rootCatId == null ? catId : rootCatId);
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
    public CmsMtPlatformCategoryTreeModel findCategoryByCatId(CmsMtPlatformCategoryTreeModel tree, String catId) {
        if (tree == null) {
            return null;
        }
        for (CmsMtPlatformCategoryTreeModel catTreeModel : tree.getChildren()) {
            if (catTreeModel.getCatId().equalsIgnoreCase(catId)) {
                return catTreeModel;
            }
            if (catTreeModel.getChildren().size() > 0) {
                CmsMtPlatformCategoryTreeModel category = findCategoryByCatId(catTreeModel, catId);
                if (category != null) return category;
            }
        }
        return null;
    }

    /**
     * 获取类目名称对应的类目对象
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public CmsMtPlatformCategoryTreeModel getCategoryByCatPath(String catPath, int cartId) {
        List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeList = getPlatformCategory(cartId);
        for (CmsMtPlatformCategoryTreeModel platformCategoryTree : platformCategoryTreeList) {
            CmsMtPlatformCategoryTreeModel model = findCategory(platformCategoryTree, catPath);
            if (model != null) {
                return model;
            }
        }
        return null;
    }

    /**
     * 取得一级类目列表（所有平台数据）
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public List<CmsMtPlatformCategoryTreeModel> getPlatformCategory(int cartId) {
        return platformCategoryDao.selectAll(cartId);
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtPlatformCategoryTreeModel findCategory(CmsMtPlatformCategoryTreeModel tree, String catPath) {
        for (CmsMtPlatformCategoryTreeModel cmsMtPlatformCategoryTreeModel : tree.getChildren()) {
            if (cmsMtPlatformCategoryTreeModel.getCatPath().equalsIgnoreCase(catPath)) {
                return cmsMtPlatformCategoryTreeModel;
            }
            if (cmsMtPlatformCategoryTreeModel.getChildren().size() > 0) {
                CmsMtPlatformCategoryTreeModel platformCategory = findCategory(cmsMtPlatformCategoryTreeModel, catPath);
                if (platformCategory != null) return platformCategory;
            }
        }
        return null;
    }
}
