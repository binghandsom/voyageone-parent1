package com.voyageone.cms.service;


import java.util.List;
import java.util.Map;

import com.voyageone.cms.formbean.ModelCNBaseBean;
import com.voyageone.cms.formbean.ModelUSBean;
import com.voyageone.common.Constants;

import com.voyageone.core.modelbean.UserSessionBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.voyageone.cms.formbean.CategoryUSBean;
import com.voyageone.cms.formbean.CategoryCNBean;
import com.voyageone.cms.formbean.ModelCNBean;
import com.voyageone.cms.formbean.ModelJDBean;
import com.voyageone.cms.formbean.ModelPriceSettingBean;
import com.voyageone.cms.formbean.ModelProductCNBean;
import com.voyageone.cms.formbean.ModelProductUSBean;
import com.voyageone.cms.formbean.ModelTMBean;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public interface ModelEditService {

	
	public ModelUSBean doGetUSModelInfo(String modelId, String channelId, boolean isExtend) throws Exception;
	
	public ModelCNBean doGetCNModelInfo(String modelId, String channelId, boolean isExtend) throws Exception;
	
	public ModelPriceSettingBean doGetModelCNPriceSettingInfo(String modelId, String channelId, boolean isExtend) throws Exception;
	
	public boolean doUpdateModelCNPriceSettingInfo(ModelPriceSettingBean modelPriceSettingBean, String modifier) throws Exception;
	
	public List<Map<String, Object>> doGetUSCategoryList(String modelId, String channelId) throws Exception;

	public List<Map<String, Object>> doGetCNCategoryList(String modelId, String channelId) throws Exception;
	
	public List<ModelProductUSBean> doGetUSProductList(Map<String, Object> data) throws Exception;
	
	public int doGetUSProductListCnt(Map<String, Object> data) throws Exception;
	
	public boolean doUpdateCNModelInfo(ModelCNBaseBean modelCnBean) throws Exception;
	
	public boolean doUpdateUSModelInfo(ModelUSBean modelUSBean) throws Exception;

	public boolean doUpdateUSModelPrimaryCategory(Map<String, Object> data) throws Exception;

	public int doUpdateRemoveCategoryModel(Map<String, Object> data) throws Exception;

	public boolean doUpdateCNModelJingDongInfo(ModelJDBean modelJDBean) throws Exception;

	public boolean doUpdateCNModelTmallInfo(ModelTMBean modelTMBean) throws Exception;

	public List<ModelProductCNBean> doGetCNProductList(Map<String, Object> data) throws Exception;

	public void doUpdateMainCategory(Map<String, Object> data) throws Exception;

	public void doUpdateModelProductPriceSetting(ModelPriceSettingBean modelPriceSettingBean, String modifier) throws Exception;

	public int doGetCNProductListCnt(Map<String, Object> data) throws Exception;

	List<Map<String, Object>> doSearchCategoryByModel(Map<String, Object> requestMap);

	boolean doChangeModel(Map<String, Object> requestMap, UserSessionBean user);

	boolean saveCnModelExtend(ModelCNBaseBean modelCnBean);
}
