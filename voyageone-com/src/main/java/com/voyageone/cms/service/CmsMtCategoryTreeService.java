package com.voyageone.cms.service;

import com.mongodb.WriteResult;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategoryTreeDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategoryDao;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtPlatformCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CmsMtCategoryTreeService {

    @Autowired
    private CmsMtCategoryTreeDao cmsMtCategoryTreeDao;

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;

    /**
     * 生成并插入cms_mt_category_tree数据（channelId cartId ）
     */
    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId) {
        boolean result = true;
        List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModels = platformCategoryDao.selectByChannel_CartId(channelId, cartId);
        for (CmsMtPlatformCategoryTreeModel platformCategoryTreeModel : platformCategoryTreeModels) {
            CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTreeModel, true);
            CmsMtCategoryTreeModel dbModel = cmsMtCategoryTreeDao.selectByCatId(cmsMtCategoryTreeModel.getCatId());
            if (dbModel != null) {
                cmsMtCategoryTreeDao.delete(dbModel);
            }
            WriteResult writeResult = cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
            if (writeResult.getLastError().getInt("ok") != 1) {
                result = false;
            }
        }
        return result;
    }

    /**
     * 生成并插入cms_mt_category_tree数据（channelId cartId categoryId ）
     */
    public boolean createCmsMtCategoryTreeFromPlatform(String channelId, int cartId, String categoryId) {
        CmsMtPlatformCategoryTreeModel platformCategoryTreeModel = platformCategoryDao.selectByChannel_CartId_CatId(channelId, cartId, categoryId);
        CmsMtCategoryTreeModel cmsMtCategoryTreeModel = createCategoryTreeModel(platformCategoryTreeModel, true);
        CmsMtCategoryTreeModel dbModel = cmsMtCategoryTreeDao.selectByCatId(cmsMtCategoryTreeModel.getCatId());
        if (dbModel != null) {
            cmsMtCategoryTreeDao.delete(dbModel);
        }
        WriteResult writeResult = cmsMtCategoryTreeDao.insert(cmsMtCategoryTreeModel);
        if(writeResult.getLastError().getInt("ok") != 1) {
            return true;
        }
        return false;
    }


    /**
     * 生成CmsMtCategoryTreeModel数据
     */
    private CmsMtCategoryTreeModel createCategoryTreeModel(CmsMtPlatformCategoryTreeModel platformCategoryTreeModel, boolean isRoot) {
        CmsMtCategoryTreeModel result = null;
        if (platformCategoryTreeModel != null) {
            result = new CmsMtCategoryTreeModel();
            result.setCatId(platformCategoryTreeModel.getCatId());
            String catName = platformCategoryTreeModel.getCatName();
//            if (catName != null && catName.indexOf("/")>=0) {
//                String[] catNameArr = platformCategoryTreeModel.getCatName().split("/");
//                if (catNameArr.length>1) {
//                    catName = catNameArr[catNameArr.length-1];
//                }
//            }
            result.setCatName(catName);
            result.setCatPath(platformCategoryTreeModel.getCatPath());
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
