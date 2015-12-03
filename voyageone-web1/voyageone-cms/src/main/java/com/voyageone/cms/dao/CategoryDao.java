package com.voyageone.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.formbean.CategoryBean;
import com.voyageone.cms.formbean.CategoryListBean;
import com.voyageone.cms.formbean.CategoryProductCNBean;
import com.voyageone.cms.formbean.CategoryProductUSBean;
import com.voyageone.cms.formbean.CategoryUSBean;
import com.voyageone.cms.formbean.CategoryCNBean;
import com.voyageone.cms.formbean.CategoryModelBean;
import com.voyageone.cms.formbean.CategoryPriceSettingBean;
import com.voyageone.cms.modelbean.*;
import com.voyageone.common.Constants;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CategoryDao extends CmsBaseDao {
	/**
	 * 获取CNCategoryInfo信息
	 * 
	 * @param data
	 * @return
	 */
	public CategoryCNBean getCNCategoryInfo(HashMap<String, Object> data) {
		CategoryCNBean ret = null;
		List<CategoryCNBean> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_select_get_cn_categoryinfo", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}

	/**
	 * 获取CNSubCategoryList信息
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<CategoryCNBean> doGetCNSubCategoryList(HashMap<String, Object> data) {

		List<CategoryCNBean> ret = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_cn_sub_categoryList", data);
		return ret;
	}

	/**
	 * 获取CategoryCNPriceSettingInfo信息
	 * 
	 * @param data
	 * @return
	 */
	public CategoryPriceSettingBean doGetCategoryCNPriceSettingInfo(HashMap<String, Object> data) {
		CategoryPriceSettingBean ret = null;
		List<CategoryPriceSettingBean> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_cn_category_price_setting", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}

		return ret;
	}

	/**
	 * 获取USCategoryInfo
	 * 
	 * @param categoryId
	 * @param channelId
	 * @return
	 */
	public CategoryUSBean getUSCategoryInfo(HashMap<String, Object> data) throws Exception {

		CategoryUSBean ret = null;
		List<CategoryUSBean> info = null;
		info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_select_get_us_categoryinfo", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}

		return ret;
	}

	/**
	 * 获取USSubCategoryList信息
	 * 
	 * @param categoryId
	 * @param channelId
	 * @return
	 */
	public List<CategoryUSBean> getUSSubCategoryList(HashMap<String, Object> data) {
		List<CategoryUSBean> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_subCategory", data);
		return info;
	}

	/**
	 * 获取USCategory下的Model信息
	 * 
	 * @param data
	 * @return
	 */
	public List<CategoryModelBean> getUSCategoryModelList(HashMap<String, Object> data) {
		List<CategoryModelBean> result = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_USCategoryModelList", data);
		return result;

	}

	/**
	 * 获取CNCategory下的Model信息
	 * 
	 * @param data
	 * @return
	 */
	public List<CategoryModelBean> getCNCategoryModelList(HashMap<String, Object> data) {
		List<CategoryModelBean> result = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CNCategoryModelList", data);
		return result;

	}

	/**
	 * 获取USCategory下的Product信息
	 * 
	 * @param data
	 * @return
	 */
	public List<CategoryProductUSBean> getUSCategoryProductList(HashMap<String, Object> data) {
		List<CategoryProductUSBean> result = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_USCategoryProductList", data);
		return result;
	}

	/**
	 * 获取Category下的CNProduct信息
	 * 
	 * @param data
	 * @return
	 */
	public List<CategoryProductCNBean> getCategoryProductCNList(HashMap<String, Object> data) {
		List<CategoryProductCNBean> result = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CNCategoryProductList", data);
		return result;

	}

	/**
	 * 获取MainCategoryName
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getMainCategoryName(String id) {
		Map<String, Object> ret = null;
		List<Map<String, Object>> info = null;
		info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_MainCategoryName", id);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}

	/**
	 * 获取TM JD CategoryName
	 * 
	 * @param data
	 * @return
	 */
	public Map<String, Object> getTMJDName(HashMap<String, Object> data) {
		Map<String, Object> ret = null;
		List<Map<String, Object>> info = null;
		info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_TMCategoryName", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}

	/**
	 * 更新CategoryInfo
	 * 
	 * @param cagtegory
	 * @return
	 */
	public boolean updateCategoryInfo(Category cagtegory) {
		boolean isSuccess = false;
		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_CategoryInfo", cagtegory);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 更新CategoryExtendinfo
	 * 
	 * @param cagtegoryextend
	 * @return
	 */
	public boolean updateCategoryExtendInfo(CategoryExtend cagtegoryextend) {
		boolean isSuccess = false;

		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_CategoryExtendInfo", cagtegoryextend);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 更新PublishCategory
	 * 
	 * @param publishCategory
	 * @return
	 */
	public boolean updatePublicCategory(PublishCategory publishCategory) {
		boolean isSuccess = false;
		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_PublishCategory", publishCategory);

		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 对CategoryExtend进行插入操作
	 * 
	 * @param cagtegoryextend
	 * @return
	 */
	public boolean insertCategoryExtend(CategoryExtend cagtegoryextend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_CategoryExtend", cagtegoryextend);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 对PublishCategory进行插入操作
	 * 
	 * @param publishCategory
	 * @return
	 */
	public boolean insertPublicCategory(PublishCategory publishCategory) {

		boolean isSuccess = false;
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_PublishCategory", publishCategory);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 获取publishCategory的信息
	 * 
	 * @param data
	 * @return
	 */
	public PublishCategory getPublishCategory(HashMap<String, Object> data) {
		PublishCategory publishCategory = null;
		List<PublishCategory> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_PublishCategory", data);
		if (info != null && info.size() > 0) {
			publishCategory = info.get(0);
		}
		return publishCategory;
	}

	/**
	 * 获取CategoryExtend的信息
	 * 
	 * @param data
	 * @return
	 */
	public CategoryExtend getCategoryExtend(HashMap<String, Object> data) {
		CategoryExtend categoryExtend = null;
		List<CategoryExtend> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CategoryExtend", data);
		if (info != null && info.size() > 0) {
			categoryExtend = info.get(0);
		}
		return categoryExtend;
	}

	/**
	 * 删除CategoryExtend信息
	 * 
	 * @param data
	 * @return
	 */
	public boolean deleteCategoryExtend(HashMap<String, Object> data) {
		boolean isSuccess = false;
		int result = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_CategoryExtend", data);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 获取GoogleCategoryExtend的信息
	 * 
	 * @param data
	 * @return
	 */
	public GoogleCategoryExtend getGoogleCategoryExtend(HashMap<String, Object> data) {
		GoogleCategoryExtend googleCategoryExtend = null;
		List<GoogleCategoryExtend> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_GoogleCategoryExtend", data);
		if (info != null && info.size() > 0) {
			googleCategoryExtend = info.get(0);
		}

		return googleCategoryExtend;

	}

	/**
	 * 添加GoogleCategoryExtend信息
	 * 
	 * @param amazoneCategoryExtend
	 * @return
	 */
	public boolean insertGoogleCategoryExtend(GoogleCategoryExtend googleCategoryExtend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_GoogleCategoryExtend", googleCategoryExtend);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 更新GoogleCategoryExtend信息
	 * 
	 * @param amazoneCategoryExtend
	 * @return
	 */
	public boolean updateGoogleCategoryExtend(GoogleCategoryExtend googleCategoryExtend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_GoogleCategoryExtend", googleCategoryExtend);

		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 删除GoogleCategoryExtend信息
	 * 
	 * @param data
	 * @return
	 */
	public boolean deleteGoogleCategoryExtend(HashMap<String, Object> data) {
		boolean isSuccess = false;
		int result = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_GoogleCategoryExtend", data);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 获取AmazonCategoryExtend的信息
	 * 
	 * @param data
	 * @return
	 */
	public AmazonCategoryExtend getAmazonCategoryExtend(HashMap<String, Object> data) {
		AmazonCategoryExtend amazoneCategory = null;
		List<AmazonCategoryExtend> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_AmazonCategoryExtend", data);
		if (info != null && info.size() > 0) {
			amazoneCategory = info.get(0);
		}

		return amazoneCategory;

	}

	/**
	 * 添加AmazonCategoryExtend信息
	 * 
	 * @param amazoneCategoryExtend
	 * @return
	 */
	public boolean insertAmazonCategoryExtend(AmazonCategoryExtend amazoneCategoryExtend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_AmazonCategoryExtend", amazoneCategoryExtend);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 更新AmazonCategoryExtend信息
	 * 
	 * @param amazoneCategoryExtend
	 * @return
	 */
	public boolean updateAmazonCategoryExtend(AmazonCategoryExtend amazoneCategoryExtend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_AmazonCategoryExtend", amazoneCategoryExtend);

		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 删除AmazonCategoryExtend信息
	 * 
	 * @param data
	 * @return
	 */
	public boolean deleteAmazonCategoryExtend(HashMap<String, Object> data) {
		boolean isSuccess = false;
		int result = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_AmazonCategoryExtend", data);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 获取AmazonCategoryExtend的信息
	 * 
	 * @param data
	 * @return
	 */
	public PricegrabberCategoryExtend getpricegrabberCategoryExtend(HashMap<String, Object> data) {
		PricegrabberCategoryExtend getpricegrabberCategoryExtend = null;
		List<PricegrabberCategoryExtend> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_PricegrabberCategory", data);
		if (info != null && info.size() > 0) {
			getpricegrabberCategoryExtend = info.get(0);
		}

		return getpricegrabberCategoryExtend;

	}

	/**
	 * 添加AmazonCategoryExtend信息
	 * 
	 * @param amazoneCategoryExtend
	 * @return
	 */
	public boolean insertPricegrabberCategoryExtend(PricegrabberCategoryExtend pricegrabberCategoryExtend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_PricegrabberCategory", pricegrabberCategoryExtend);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 更新AmazonCategoryExtend信息
	 * 
	 * @param amazoneCategoryExtend
	 * @return
	 */
	public boolean updatePricegrabberCategoryExtend(PricegrabberCategoryExtend pricegrabberCategoryExtend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_PricegrabberCategory", pricegrabberCategoryExtend);

		if (result > 0) {
			isSuccess = true;
		}

		return isSuccess;
	}

	/**
	 * 删除AmazonCategoryExtend信息
	 * 
	 * @param data
	 * @return
	 */
	public boolean deletePricegrabberCategoryExtend(HashMap<String, Object> data) {
		boolean isSuccess = false;
		int result = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_PricegrabberCategory", data);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 更新CnCategory信息
	 * 
	 * @param cnCategory
	 * @return
	 */
	public boolean updateCnCategory(CnCategory cnCategory) {

		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_CnCategory", cnCategory);
		boolean isSuccess = false;
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 更新CnCategoryExtend的信息
	 * 
	 * @param cnCategoryExtend
	 * @return
	 */
	public boolean updateCnCategoryExtend(CnCategoryExtend cnCategoryExtend) {

		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_CnCategoryExtend", cnCategoryExtend);
		boolean isSuccess = false;
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 获取CnCategoryExtend信息
	 * 
	 * @param data
	 * @return
	 */
	public CnCategoryExtend getCnCategoryExtend(HashMap<String, Object> data) {
		CnCategoryExtend cnCategoryExtend = null;
		List<CnCategoryExtend> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CnCategoryExtend", data);
		if (info != null && info.size() > 0) {
			cnCategoryExtend = info.get(0);
		}

		return cnCategoryExtend;

	}

	/**
	 * 删除
	 * 
	 * @param data
	 * @return
	 */
	public boolean deleteCnCategoryExtend(HashMap<String, Object> data) {
		boolean isSuccess = false;
		int result = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_CnCategoryExtend", data);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	public boolean insertCnCategoryExtend(CnCategoryExtend cnCategoryExtend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_CnCategoryExtend", cnCategoryExtend);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 删除CnCategoryPriceSetting信息
	 * 
	 * @param data
	 * @return
	 */
	public boolean deleteCnCategoryPriceSetting(HashMap<String, Object> data) {
		int result = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_CnCategoryPriceSetting", data);
		boolean isSuccess = false;
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 插入CnCategoryPrice信息
	 * 
	 * @param cnCategoryPriceSetting
	 * @return
	 */
	public boolean insertCnCategoryPriceSetting(CnCategoryPrice CnCategoryPrice) {
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_CnCategoryPriceSetting", CnCategoryPrice);
		boolean isSuccess = false;
		if (result > 0) {
			isSuccess = true;
		}

		return isSuccess;

	}

	/**
	 * 更新CnCategoryPrice信息
	 * 
	 * @param CnCategoryPrice
	 * @return
	 */
	public boolean updateCnCategoryPriceSetting(CnCategoryPrice CnCategoryPrice) {
		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_CnCategoryPriceSetting", CnCategoryPrice);
		boolean isSuccess = false;
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 获取CnCategoryPrice信息
	 * 
	 * @param data
	 * @return
	 */
	public CnCategoryPrice getCnCategoryPrice(HashMap<String, Object> data) {
		CnCategoryPrice cnCategoryPrice = null;
		List<CnCategoryPrice> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CnCategoryPrice", data);
		if (info != null && info.size() > 0) {
			cnCategoryPrice = info.get(0);
		}

		return cnCategoryPrice;

	}

	/**
	 * 插入HistoryPriceSetting信息
	 * 
	 * @param historyPriceSetting
	 * @return
	 */
	public boolean insertHistoryPriceSetting(HistoryPriceSetting historyPriceSetting) {
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_HistoryPriceSetting", historyPriceSetting);
		boolean isSuccess = false;
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 更新JdCategoryExtend的信息
	 * 
	 * @param cnCategoryExtend
	 * @return
	 */
	public boolean updateJdCategoryExtend(JdCategoryExtend jdCategoryExtend) {

		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_JdCategoryExtend", jdCategoryExtend);
		boolean isSuccess = false;
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 获取JdCategoryExtend信息
	 * 
	 * @param data
	 * @return
	 */
	public JdCategoryExtend getJdCategoryExtend(HashMap<String, Object> data) {
		JdCategoryExtend jdCategoryExtend = null;
		List<JdCategoryExtend> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_JdCategoryExtend", data);
		if (info != null && info.size() > 0) {
			jdCategoryExtend = info.get(0);
		}

		return jdCategoryExtend;

	}

	/**
	 * 删除JdCategoryExtend
	 * 
	 * @param data
	 * @return
	 */
	public boolean deleteJdCategoryExtend(HashMap<String, Object> data) {
		boolean isSuccess = false;
		int result = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_JdCategoryExtend", data);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 插入JdCategoryExtend信息
	 * 
	 * @param cnTMCategoryExtend
	 * @return
	 */
	public boolean insertJdCategoryExtend(JdCategoryExtend jdCategoryExtend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_JdCategoryExtend", jdCategoryExtend);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 更新CnTMCategoryExtend的信息
	 * 
	 * @param cnCategoryExtend
	 * @return
	 */
	public boolean updateCnTMCategoryExtend(CnTMCategoryExtend cnTMCategoryExtend) {

		int result = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_CnTMCategoryExtend", cnTMCategoryExtend);
		boolean isSuccess = false;
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 获取CnCategoryExtend信息
	 * 
	 * @param data
	 * @return
	 */
	public CnTMCategoryExtend getCnTMCategoryExtend(HashMap<String, Object> data) {
		CnTMCategoryExtend cnTMCategoryExtend = null;
		List<CnTMCategoryExtend> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CnTMCategoryExtend", data);
		if (info != null && info.size() > 0) {
			cnTMCategoryExtend = info.get(0);
		}

		return cnTMCategoryExtend;

	}

	/**
	 * 删除CnCategoryExtend
	 * 
	 * @param data
	 * @return
	 */
	public boolean deleteCnTMCategoryExtend(HashMap<String, Object> data) {
		boolean isSuccess = false;
		int result = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_CnTMCategoryExtend", data);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 插入CnCategoryExtend信息
	 * 
	 * @param cnTMCategoryExtend
	 * @return
	 */
	public boolean insertCnTMCategoryExtend(CnTMCategoryExtend cnTMCategoryExtend) {
		boolean isSuccess = false;
		int result = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_CnTMCategoryExtend", cnTMCategoryExtend);
		if (result > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	public CategoryListBean getCategoryList(HashMap<String, Object> data) {

		CategoryListBean categoryListBean = null;
		List<CategoryListBean> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CategoryList", data);
		if (info != null && info.size() > 0) {
			categoryListBean = info.get(0);
		}
		return categoryListBean;
	}

	public Map<String, Object> getModelInfo(HashMap<String, Object> data) {

		Map<String, Object> ret = new HashMap<String, Object>();
		List<Map<String, Object>> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_ModelInfo", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}

		return ret;
	}

	public List<Map<String, Object>> getModelPriceSetting(Map<String, Object> data) {
		List<Map<String, Object>> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "get_sub_model_price_setting", data);
		return info;
	}

	/**
	 * 获取CategoryUSProduct的数量
	 * 
	 * @param data
	 * @return
	 */
	public int getCategoryUSProductCount(Map<String, Object> data) {
		int count = selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_USCategoryProductListCount", data);
		return count;
	}
	public boolean updateImsProduct (Map<String, Object> data) {
		boolean isSuccess = false;
		int count = update(Constants.DAO_NAME_SPACE_CMS + "cms_update_ImsBtProduct", data);
		if(count > 0) {
		   isSuccess = true;
		}
		return isSuccess;
	}
}
