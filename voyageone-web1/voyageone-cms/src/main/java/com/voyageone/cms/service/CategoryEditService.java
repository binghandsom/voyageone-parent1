package com.voyageone.cms.service;

import com.voyageone.cms.formbean.CategoryBean;
import com.voyageone.cms.formbean.CategoryListBean;
import com.voyageone.cms.formbean.CategoryProductCNBean;
import com.voyageone.cms.formbean.CategoryProductUSBean;
import com.voyageone.cms.formbean.CategoryUSBean;
import com.voyageone.cms.formbean.CategoryCNBean;
import com.voyageone.cms.formbean.CategoryModelBean;
import com.voyageone.cms.formbean.CategoryPriceSettingBean;
import com.voyageone.cms.modelbean.AmazonCategoryExtend;
import com.voyageone.cms.modelbean.CategoryExtend;
import com.voyageone.cms.modelbean.CnCategoryExtend;
import com.voyageone.cms.modelbean.CnCategoryPrice;
import com.voyageone.cms.modelbean.CnTMCategoryExtend;
import com.voyageone.cms.modelbean.GoogleCategoryExtend;
import com.voyageone.cms.modelbean.JdCategoryExtend;
import com.voyageone.cms.modelbean.PricegrabberCategoryExtend;
import com.voyageone.common.Constants;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public interface CategoryEditService {

	public CategoryUSBean getUSCategoryInfo(String categoryId, String channelId) throws Exception;

	public CategoryCNBean getCNCategoryInfo(String categoryId, String channelId) throws Exception;

	public CategoryPriceSettingBean doGetCategoryCNPriceSettingInfo(String categoryId, String channelId) throws Exception;

	public List<CategoryCNBean> doGetCNSubCategoryList(String categoryId, String channelId);

	public List<CategoryUSBean> getUSSubCategoryList(String categoryId, String channelId);

	public List<CategoryModelBean> getUSCategoryModelList(HashMap<String, Object> data);

	public List<CategoryModelBean> getCNCategoryModelList(HashMap<String, Object> data);

	public DtResponse<List<CategoryProductUSBean>> getCategoryUSProductList(HashMap<String, Object> data) throws Exception;

	public DtResponse<List<CategoryProductCNBean>> getCategoryCNProductList(HashMap<String, Object> data) throws Exception;

	public boolean updateUSCategoryInfo(CategoryUSBean inCategoryBean, UserSessionBean user) throws Exception;

	public boolean updateUSCategoryAmazonInfo(AmazonCategoryExtend amazoneCategoryExtend, UserSessionBean user) throws Exception;

	public boolean updateUSCategoryGoogleInfo(GoogleCategoryExtend googleCategoryExtendm, UserSessionBean user) throws Exception;

	public boolean updateUSCategoryPricegrabber(PricegrabberCategoryExtend pricegrabberCategoryExtend, UserSessionBean user) throws Exception;

	public boolean updateCNCategoryInfo(CategoryCNBean categoryBean, UserSessionBean user) throws Exception;

	public boolean updateCategoryCNPriceSettingInfo(CategoryPriceSettingBean priceSettingBean, UserSessionBean user) throws Exception;

	public List<CategoryBean> doGetCategoryList(String channelId);

	public boolean updateUSCategoryExtendInfo(CategoryExtend categoryExtend,UserSessionBean user) throws Exception;

	public boolean updatePublishCategory(int categoryId, String channelId, boolean isPublished, UserSessionBean user, String type);

	public boolean updateCnCategoryExtend(CnCategoryExtend cnCategoryExtend, UserSessionBean user) throws Exception;

	public boolean updateCnCategoryPrice(CnCategoryPrice price, UserSessionBean user) throws Exception;

	public boolean updateJdCategoryExtend(JdCategoryExtend jdCategoryExtend, UserSessionBean user) throws Exception;

	public boolean updateCnTMCategoryExtend(CnTMCategoryExtend cnTMCategoryExtend, UserSessionBean user) throws Exception;

	public boolean updateMainCategoryId(HashMap<String, Object> data, UserSessionBean user) throws Exception;

	public List<CategoryListBean> getCategoryList(String categoryId, String channelId) throws Exception;

	public Map<String, Object> getModelInfo(String modelId, String categoryId, String channelId) throws Exception;




}
