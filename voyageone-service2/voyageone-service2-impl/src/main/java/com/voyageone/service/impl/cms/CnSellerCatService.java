package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.cn.CnCategoryBean;
import com.voyageone.service.dao.cms.mongo.CmsBtSellerCatDao;
import com.voyageone.service.impl.cms.sx.CnCategoryService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void  updateSellerCat(CmsBtSellerCatModel currentNode, ShopBean shopBean)
    {
        CnCategoryBean cnCategoryBean= cnCategoryService.createCnCategoryBean(currentNode.getFullCatId(), "-", currentNode.getCatName(), currentNode.getCatName(), currentNode.getUrlKey());
        boolean ret = cnCategoryService.uploadCnCategory(cnCategoryBean,false,shopBean);
        if (!ret) {
            throw new BusinessException("创建类目失败， 请再尝试一下。");
        }
    }
    public void  deleteSellerCat(CmsBtSellerCatModel currentNode, ShopBean shopBean)
    {
        CnCategoryBean cnCategoryBean= cnCategoryService.createCnCategoryBean(currentNode.getFullCatId(), "-", currentNode.getCatName(), currentNode.getCatName(), currentNode.getUrlKey());
        boolean ret = cnCategoryService.uploadCnCategory(cnCategoryBean,true,shopBean);
        if (!ret) {
            throw new BusinessException("创建类目失败， 请再尝试一下。");
        }
    }
}
