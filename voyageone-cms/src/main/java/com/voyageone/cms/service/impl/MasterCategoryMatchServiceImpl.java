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
	public List<CmsCategoryBean> getCmsCategoryList(String channelId) {

		List<CmsCategoryModel> categoryModels = masterCategoryMatchDao.getCmsCategoryList(channelId);

		List<String> propMatchDoneList = masterCategoryMatchDao.getPropMatchCompleteCategories(channelId);

		List<CmsCategoryModel> removeList = new ArrayList<>();
		List<CmsCategoryBean> resultList = new ArrayList<>();

		for (CmsCategoryModel model : categoryModels) {
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

			CmsCategoryBean categoryBean = new CmsCategoryBean();

			BeanUtils.copyProperties(model, categoryBean);

			categoryBean.setCmsCategoryPath(model.getEnName());
			if (model.getMainCategoryId()>0) {
				categoryBean.setMainCategoryPath(
						ImsCategoryConfigs.getMtCategoryBeanById(model.getMainCategoryId()).getCategoryPath());
				if(propMatchDoneList.contains(String.valueOf(model.getMainCategoryId()))){
					categoryBean.setPropMatchStatus(1);
				}
			}
			resultList.add(categoryBean);

			this.buildViewModel(model, model.getEnName(), categoryBean.getMainCategoryPath(), resultList);
		}

		return resultList;

	}
	
	

	/**
	 * 
	 * @param parent
	 * @param parentPath
	 * @param parentMainPath
	 * @param resultList
	 */
	private void buildViewModel(CmsCategoryModel parent, String parentPath, String parentMainPath,
			List<CmsCategoryBean> resultList) {

		List<CmsCategoryModel> categoryModels = parent.getChildren();

		if (categoryModels != null) {

			for (CmsCategoryModel cmsCategoryModel : categoryModels) {
				CmsCategoryBean categoryBean = new CmsCategoryBean();
				BeanUtils.copyProperties(cmsCategoryModel, categoryBean);
				String path = parentPath + " > " + cmsCategoryModel.getEnName();
				categoryBean.setCmsCategoryPath(path);
				if (cmsCategoryModel.getMainCategoryId() == 0) {
					categoryBean.setMainCategoryPath(parentMainPath);
				} else {
					if (cmsCategoryModel.getMainCategoryId()>0) {
						categoryBean.setMainCategoryPath(ImsCategoryConfigs
								.getMtCategoryBeanById(cmsCategoryModel.getMainCategoryId()).getCategoryPath());
					}
					
				}
				resultList.add(categoryBean);
				if (cmsCategoryModel.getMainCategoryId() == -1) {
					buildViewModel(cmsCategoryModel, path, parentMainPath, resultList);
				}else {
					buildViewModel(cmsCategoryModel, path, categoryBean.getMainCategoryPath(), resultList);
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
