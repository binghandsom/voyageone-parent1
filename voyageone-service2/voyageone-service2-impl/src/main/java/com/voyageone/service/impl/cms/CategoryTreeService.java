package com.voyageone.service.impl.cms;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsMtCategoryTreeDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryTreeService extends BaseService {

    @Autowired
    private CmsMtCategoryTreeDao cmsMtCategoryTreeDao;

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;

    /**
     * 生成并插入cms_mt_category_tree数据（cartId ）
     */
    public boolean createCmsMtCategoryTreeFromPlatform(int cartId) {
        return createCmsMtCategoryTreeFromPlatform(cartId, false);
    }
    public boolean createCmsMtCategoryTreeFromPlatform(int cartId, boolean isUpdate) {
        List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModels = platformCategoryDao.selectPlatformCategoriesByCartId(cartId);
        for (CmsMtPlatformCategoryTreeModel platformCategoryTreeModel : platformCategoryTreeModels) {
            CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTreeModel, true);
            CmsMtCategoryTreeModel dbModel = cmsMtCategoryTreeDao.selectByCatId(cmsMtCategoryTreeModel.getCatId());
            if (dbModel != null) {
                if (isUpdate) {
                    cmsMtCategoryTreeDao.delete(dbModel);
                    cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
                }
            } else {
                cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
            }
        }
        return true;
    }

    /**
     * 生成并插入cms_mt_category_tree数据（channelId cartId ）
     */
    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId) {
        return createCmsMtCategoryTreeFromPlatform(channelId, cartId, false);
    }

    /**
     * 生成并插入cms_mt_category_tree数据（channelId cartId isUpdate）
     */
    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId, boolean isUpdate) {
        List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModels = platformCategoryDao.selectByChannel_CartId(channelId, cartId);
        for (CmsMtPlatformCategoryTreeModel platformCategoryTreeModel : platformCategoryTreeModels) {
            CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTreeModel, true);
            CmsMtCategoryTreeModel dbModel = cmsMtCategoryTreeDao.selectByCatId(cmsMtCategoryTreeModel.getCatId());
            if (dbModel != null) {
                if (isUpdate) {
                    cmsMtCategoryTreeDao.delete(dbModel);
                    cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
                }
            } else {
                cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
            }
        }
        return true;
    }


    /**
     * 生成并插入cms_mt_category_tree数据（channelId cartId categoryId ）
     */
    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId, String categoryId) {
        return createCmsMtCategoryTreeFromPlatform(channelId, cartId, categoryId, false);
    }
    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId, String categoryId, boolean isUpdate) {
        CmsMtPlatformCategoryTreeModel platformCategoryTreeModel = platformCategoryDao.selectByChannel_CartId_CatId(channelId, cartId, categoryId);
        CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTreeModel, true);
        CmsMtCategoryTreeModel dbModel = cmsMtCategoryTreeDao.selectByCatId(cmsMtCategoryTreeModel.getCatId());
        if (dbModel != null) {
            if (isUpdate) {
                cmsMtCategoryTreeDao.delete(dbModel);
                cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
            }
        } else {
            cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
        }
        return true;
    }


    /**
     * 生成CmsMtCategoryTreeModel数据
     */
    private CmsMtCategoryTreeModel createCategoryTreeModel(CmsMtPlatformCategoryTreeModel platformCategoryTreeModel, boolean isRoot) {
        CmsMtCategoryTreeModel result = null;
        if (platformCategoryTreeModel != null) {
            result = new CmsMtCategoryTreeModel();
            String catPath = platformCategoryTreeModel.getCatPath();
            String catId = StringUtils.generCatId(catPath);
            result.setCatId(catId);
            result.setCatName(platformCategoryTreeModel.getCatName());
            result.setCatPath(catPath);
            int isParent = 0;
            List<CmsMtPlatformCategoryTreeModel> pChildren = platformCategoryTreeModel.getChildren();
            if (pChildren != null && pChildren.size()>0) {
                isParent = 1;
            }
            result.setIsParent(isParent);
            List<CmsMtCategoryTreeModel> children = createCategoryTreeModel(pChildren);
            for (CmsMtCategoryTreeModel child : children) {
                child.setParentCatId(result.getCatId());
            }
            result.setChildren(children);
            if (!isRoot) {
                result.setCreated(null);
                result.setCreater(null);
                result.setModified(null);
                result.setModifier(null);
            }
        }
        return result;
    }

    /**
     * 生成CmsMtCategoryTreeModel List 数据
     */
    private List<CmsMtCategoryTreeModel> createCategoryTreeModel(List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeList) {
        List<CmsMtCategoryTreeModel> result = new ArrayList<>();
        if (platformCategoryTreeList != null) {
            for (CmsMtPlatformCategoryTreeModel platformCategoryTree : platformCategoryTreeList) {
                CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTree, false);
                result.add(cmsMtCategoryTreeModel);
            }
        }
        return result;
    }

}
