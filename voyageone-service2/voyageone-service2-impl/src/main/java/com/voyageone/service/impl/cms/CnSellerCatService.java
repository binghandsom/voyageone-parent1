package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.cnn.service.CnnCatalogService;
import com.voyageone.service.bean.cms.cn.CnCategoryBean;
import com.voyageone.service.dao.cms.mongo.CmsBtSellerCatDao;
import com.voyageone.service.impl.cms.sx.CnCategoryService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.sun.tools.javac.jvm.ByteCodes.ret;

/**
 * Created by dell on 2016/9/23.
 */
@Service
public class CnSellerCatService {
    @Autowired
    MongoSequenceService commSequenceMongoService;
    @Autowired
    private CmsBtSellerCatDao cmsBtSellerCatDao;

    @Autowired
    private CnnCatalogService cnnCatalogService;
    @Autowired
    CnCategoryService cnCategoryService;

    public String addSellerCat(String channelId, String parentCId, String catName, ShopBean shopBean) {
        String catId = Long.toString(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_CnShopCategory_ID));
        String catFullId = "";
        if (!StringUtils.isEmpty(parentCId)) {
            CmsBtSellerCatModel parentCurrentNode = cmsBtSellerCatDao.selectByCatId(channelId, parentCId);
            if (parentCurrentNode != null) {
                catFullId = parentCurrentNode.getFullCatId();
            }
        }
        if (StringUtils.isEmpty(catFullId)) {
            catFullId = catId;
        } else {
            catFullId = catFullId + "-" + catId;
        }
        CnCategoryBean cnCategoryBean = cnCategoryService.createCnCategoryBean(catFullId, "-", catName, catName, null);
        boolean ret = cnCategoryService.uploadCnCategory(cnCategoryBean, false, shopBean);
        if (!ret) {
            throw new BusinessException("创建类目失败， 请再尝试一下。");
        }

        return catId;
    }

    /**
     * 重载：新增时设置index为当前层级的最后一位，附带urlKey
     * @param channelId
     * @param parentCId
     * @param catName
     * @param urlKey
     * @param shopBean
     * @param index
     * @return
     */
//    public Map<String, String> addSellerCat(String channelId, String parentCId, String catName, String urlKey, ShopBean shopBean, int index) {
//        String catId = Long.toString(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_CnShopCategory_ID));
//        String catFullId = "";
//        if (!StringUtils.isEmpty(parentCId)) {
//            CmsBtSellerCatModel parentCurrentNode = cmsBtSellerCatDao.selectByCatId(channelId, parentCId);
//            if (parentCurrentNode != null) {
//                catFullId = parentCurrentNode.getFullCatId();
//            }
//        }
//        if (StringUtils.isEmpty(catFullId)) {
//            catFullId = catId;
//        } else {
//            catFullId = catFullId + "-" + catId;
//        }
//        CnCategoryBean cnCategoryBean = cnCategoryService.createCnCategoryBean(catFullId, "-", catName, catName, urlKey);
//        cnCategoryBean.setDisplayOrder(index);
//        boolean ret = cnCategoryService.uploadCnCategory(cnCategoryBean, false, shopBean);
//        if (!ret) {
//            throw new BusinessException("创建类目失败， 请再尝试一下。");
//        }
//        Map<String, String> resultMap = new HashMap<String, String>();
//        resultMap.put("catId", catId);
//        resultMap.put("urlKey", cnCategoryBean.getUrlKey());
//        return resultMap;
//    }

    /**
     * 重载上面的方法，独立官网修改单个类目的名称后，调用单个独立官网的类目更新接口时，需要传入该类目的index给类目
     * @param currentNode
     * @param shopBean
     * @param index
     */
//    public void  updateSellerCat(CmsBtSellerCatModel currentNode, ShopBean shopBean, int index)
//    {
//        CnCategoryBean cnCategoryBean= cnCategoryService.createCnCategoryBean(currentNode.getFullCatId(), "-", currentNode.getCatName(), currentNode.getCatName(), currentNode.getUrlKey());
//        cnCategoryBean.setDisplayOrder(index);
//        boolean ret = cnCategoryService.uploadCnCategory(cnCategoryBean,false,shopBean);
//        if (!ret) {
//            throw new BusinessException("创建类目失败， 请再尝试一下。");
//        }
//    }

//    public void  deleteSellerCat(CmsBtSellerCatModel currentNode, ShopBean shopBean)
//    {
//        CnCategoryBean cnCategoryBean= cnCategoryService.createCnCategoryBean(currentNode.getFullCatId(), "-", currentNode.getCatName(), currentNode.getCatName(), currentNode.getUrlKey());
//        boolean ret = cnCategoryService.uploadCnCategory(cnCategoryBean,true,shopBean);
//        if (!ret) {
//            throw new BusinessException("创建类目失败， 请再尝试一下。");
//        }
//    }
    public Map<String, String> addSellerCat(String channelId, String parentCId, String catName, String urlKey, ShopBean shopBean, int index) {
        String catId = Long.toString(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_CnShopCategory_ID));
        try {
            cnnCatalogService.addCatalog(shopBean, catName,catId,parentCId);
        }
        catch (Exception e) {
            throw new BusinessException("创建类目失败， 请再尝试一下。" + e.getMessage());
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("catId", catId);
        resultMap.put("urlKey", "");
        return resultMap;
    }

    public void  updateSellerCat(CmsBtSellerCatModel currentNode, ShopBean shopBean, int index)
    {
        try {
            cnnCatalogService.updateCatalog(shopBean, currentNode.getCatId(),currentNode.getCatName());
        } catch (Exception e) {
            throw new BusinessException("更新类目失败， 请再尝试一下。" + e.getMessage());
        }
    }

    public void  deleteSellerCat(CmsBtSellerCatModel currentNode, ShopBean shopBean)
    {
        try {
            cnnCatalogService.deleteCatalog(shopBean, currentNode.getCatId());
        }catch (Exception e) {
            throw new BusinessException("删除类目失败， 请再尝试一下。" + e.getMessage());
        }
    }
}
