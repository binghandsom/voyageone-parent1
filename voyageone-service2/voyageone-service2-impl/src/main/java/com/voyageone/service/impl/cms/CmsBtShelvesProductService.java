package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtShelvesProductDao;
import com.voyageone.service.daoext.cms.CmsBtShelvesProductDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/11.
 */
@Service
public class CmsBtShelvesProductService extends BaseService {

    private final CmsBtShelvesProductDao cmsBtShelvesProductDao;

    private final CmsBtShelvesProductDaoExt cmsBtShelvesProductDaoExt;

    @Autowired
    public CmsBtShelvesProductService(CmsBtShelvesProductDao cmsBtShelvesProductDao, CmsBtShelvesProductDaoExt cmsBtShelvesProductDaoExt) {
        this.cmsBtShelvesProductDao = cmsBtShelvesProductDao;
        this.cmsBtShelvesProductDaoExt = cmsBtShelvesProductDaoExt;
    }

    public List<CmsBtShelvesProductModel> getByShelvesId(Integer shelvesId){
        return cmsBtShelvesProductDaoExt.selectByShelvesId(shelvesId);
    }

    public CmsBtShelvesProductModel getByShelvesIdProductCode(Integer shelvesId, String code){
        Map map = new HashedMap();
        map.put("shelvesId",shelvesId);
        map.put("productCode",code);
        return cmsBtShelvesProductDao.selectOne(map);
    }
    public int update(CmsBtShelvesProductModel cmsBtShelvesProductModel){
        return cmsBtShelvesProductDao.update(cmsBtShelvesProductModel);
    }

    public Integer insert(CmsBtShelvesProductModel cmsBtShelvesProductModel){
        cmsBtShelvesProductDao.insert(cmsBtShelvesProductModel);
        return cmsBtShelvesProductModel.getId();
    }

    /**
     * 更新货架顺序
     */
    public void updateSort(List<CmsBtShelvesProductModel> cmsBtShelvesProductModels){
        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductDaoExt::updateSort);
    }

    /**
     * 更新平台状态和库存
     */
    public void updatePlatformStatus(List<CmsBtShelvesProductModel> cmsBtShelvesProductModels){
        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductDaoExt::updatePlatformStatus);
    }

    /**
     * 更新平台图片
     */
    public void updatePlatformImage(CmsBtShelvesProductModel cmsBtShelvesProductModels){
        cmsBtShelvesProductDaoExt.updatePlatformImage(cmsBtShelvesProductModels);
    }
}
