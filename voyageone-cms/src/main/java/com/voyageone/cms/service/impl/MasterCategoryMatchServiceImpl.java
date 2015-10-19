package com.voyageone.cms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.cms.dao.MasterCategoryMatchDao;
import com.voyageone.cms.formbean.CmsCategoryBean;
import com.voyageone.cms.modelbean.CmsCategoryModel;
import com.voyageone.cms.service.MasterCategoryMatchService;
import com.voyageone.common.bussiness.platformInfo.dao.PlatformInfoDao;
import com.voyageone.common.bussiness.platformInfo.model.PlatformInfoModel;
import com.voyageone.common.configs.ImsCategoryConfigs;

@Service
public class MasterCategoryMatchServiceImpl implements MasterCategoryMatchService {
	
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MasterCategoryMatchService.class);

	@Autowired
	ImsCategoryConfigs imsCategoryConfigs;
	@Autowired
	MasterCategoryMatchDao masterCategoryMatchDao;
	@Autowired
	PlatformInfoDao platformInfoDao;

	/**
	 * 获取所有cms类目的信息.
	 */
	@Override
	public List<CmsCategoryModel> getCmsCategoryModelList(String channelId) {

		List<CmsCategoryModel> categoryModels = masterCategoryMatchDao.getCmsCategoryList(channelId);
		List<String> propMatchDoneList = masterCategoryMatchDao.getPropMatchCompleteCategories(channelId);

		List<CmsCategoryModel> removeList = new ArrayList<>();

		for (CmsCategoryModel model : categoryModels) {
			if (model.getMainCategoryId()>0) {
				model.setMainCategoryPath(ImsCategoryConfigs.getMtCategoryBeanById(model.getMainCategoryId()).getCategoryPath());
				if(propMatchDoneList.contains(String.valueOf(model.getMainCategoryId()))){
					model.setPropMatchStatus(1);
				}
			}
			List<CmsCategoryModel> subModels = new ArrayList<>();
			boolean isTop = true;
			for (CmsCategoryModel subModel : categoryModels) {
				if (subModel.getParentCategoryId() == model.getCategoryId()) {
					subModels.add(subModel);
				}

				if (model.getParentCategoryId() == subModel.getCategoryId()) {
					isTop = false;
				}
			}
			model.setChildren(subModels);
			if (isTop) {
				model.setParentCategoryId(0);
			} else {
				removeList.add(model);
			}
		}

		categoryModels.removeAll(removeList);

		for (CmsCategoryModel model : categoryModels) {

			model.setCmsCategoryPath(model.getEnName());

			this.buildModel(model, model.getEnName(), model.getMainCategoryPath(),model.getMainCategoryId());
		}

		return categoryModels;

	}
	
	/**
	 *
	 * @param parent
	 * @param parentPath
	 * @param parentMainPath
	 */
	private void buildModel(CmsCategoryModel parent, String parentPath, String parentMainPath,int parentMainCatId) {

		List<CmsCategoryModel> categoryModels = parent.getChildren();

		if (categoryModels != null) {

			for (CmsCategoryModel cmsCategoryModel : categoryModels) {
				String path = parentPath + " > " + cmsCategoryModel.getEnName();
				cmsCategoryModel.setCmsCategoryPath(path);
				if (cmsCategoryModel.getMainCategoryId() == 0) {
					cmsCategoryModel.setMainCategoryPath(parentMainPath);
					cmsCategoryModel.setExtendMainCategoryId(parentMainCatId);
				}
				if (cmsCategoryModel.getMainCategoryId() > 0) {
					buildModel(cmsCategoryModel, path, cmsCategoryModel.getMainCategoryPath(),cmsCategoryModel.getMainCategoryId());
				}else {
					buildModel(cmsCategoryModel, path, parentMainPath,parentMainCatId);
				}

			}
		}
	}
	
	/**
	 * 获取对应主类目的平台类目信息.
	 */
	@Override
	public List<PlatformInfoModel> getPlatformInfo(int categoryId) {
		return platformInfoDao.getPlatformInfo(categoryId);
	}

}
