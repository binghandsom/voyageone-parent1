package com.voyageone.service.impl.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.service.dao.cms.CmsMtPlatformBrandsDao;
import com.voyageone.service.daoext.cms.CmsMtPlatformBrandsDaoExt;
import com.voyageone.service.model.cms.CmsMtPlatformBrandsModel;

/**
 * Created by Wangtd on 8/1/16.
 */
@Service
public class CmsMtPlatformBrandService {
	
	@Autowired
	private CmsMtPlatformBrandsDao cmsMtPlatformBrandsDao;
	
	@Autowired
	private CmsMtPlatformBrandsDaoExt cmsMtPlatformBrandsDaoExt;

	public void deleteBrandsByChannelIdAndCartId(String channelId, String cartId) {
		cmsMtPlatformBrandsDaoExt.deleteBrandsByChannelIdAndCartId(channelId, cartId);
	}

	public void saveList(List<CmsMtPlatformBrandsModel> brandModels) {
		for (CmsMtPlatformBrandsModel brandModel : brandModels) {
            if (brandModel.getId() == null || brandModel.getId() <= 0) {
            	cmsMtPlatformBrandsDao.update(brandModel);
            } else {
            	cmsMtPlatformBrandsDao.insert(brandModel);
            }
        }
	}

}
