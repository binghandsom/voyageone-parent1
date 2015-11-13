package com.voyageone.cms.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.common.Constants;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.CmsMsgConstants;
import com.voyageone.cms.controller.CategoryController;
import com.voyageone.cms.dao.CategoryDao;
import com.voyageone.cms.dao.CommonDao;
import com.voyageone.cms.dao.ProductDao;
import com.voyageone.cms.dao.SetsizechartDao;
import com.voyageone.cms.formbean.CategoryBean;
import com.voyageone.cms.formbean.CategoryCNBean;
import com.voyageone.cms.formbean.CategoryListBean;
import com.voyageone.cms.formbean.CategoryProductCNBean;
import com.voyageone.cms.formbean.CategoryProductUSBean;
import com.voyageone.cms.formbean.CategoryUSBean;
import com.voyageone.cms.formbean.CategoryModelBean;
import com.voyageone.cms.formbean.CategoryPriceSettingBean;
import com.voyageone.cms.formbean.ModelCNBean;
import com.voyageone.cms.formbean.ModelPriceSettingBean;
import com.voyageone.cms.formbean.ModelUSBean;
import com.voyageone.cms.formbean.SizeChartInfo;
import com.voyageone.cms.modelbean.AmazonCategoryExtend;
import com.voyageone.cms.modelbean.Category;
import com.voyageone.cms.modelbean.CategoryExtend;
import com.voyageone.cms.modelbean.CnCategory;
import com.voyageone.cms.modelbean.CnCategoryExtend;
import com.voyageone.cms.modelbean.CnCategoryPrice;
import com.voyageone.cms.modelbean.CnTMCategoryExtend;
import com.voyageone.cms.modelbean.GoogleCategoryExtend;
import com.voyageone.cms.modelbean.HistoryPriceSetting;
import com.voyageone.cms.modelbean.JdCategoryExtend;
import com.voyageone.cms.modelbean.PricegrabberCategoryExtend;
import com.voyageone.cms.modelbean.PublishCategory;
import com.voyageone.cms.service.CategoryEditService;
import com.voyageone.cms.service.CommonService;
import com.voyageone.cms.service.ModelEditService;
import com.voyageone.cms.service.ProductEditService;
import com.voyageone.cms.utils.CommonUtils;
import com.voyageone.cms.utils.DaoHandler;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class CategoryEditServiceImpl implements CategoryEditService {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(CategoryEditServiceImpl.class);

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private SetsizechartDao setsizechartDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ModelEditService modelEditService;

	@Autowired
	private CommonDao commonDao;
	@Autowired
	private CommonServiceImpl commonServiceImpl;

	public List<CategoryBean> doGetCategoryList(String channelId) {
		return commonDao.getCategoryMenuList(channelId);
	}

	/**
	 * 取得PriceSetting表的信息
	 */
	public CategoryPriceSettingBean doGetCategoryCNPriceSettingInfo(String categoryId, String channelId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", categoryId);
		data.put("channelId", channelId);
		DaoHandler daoHandler = new DaoHandler<CategoryDao, CategoryPriceSettingBean>(categoryDao, "doGetCategoryCNPriceSettingInfo", data, "");
		Map<String, CategoryPriceSettingBean> resultMap = daoHandler.getCategoryInfoByGroup();
		CategoryPriceSettingBean resultBean = null;
		if (resultMap != null) {
			resultBean = resultMap.get("resultBean");
		}
		return resultBean;
	}

	/**
	 * 取得CN的CategoryInfo信息
	 * 
	 * @param categoryId
	 * @param channelId
	 */
	@Override
	public CategoryCNBean getCNCategoryInfo(String categoryId, String channelId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", categoryId);
		data.put("channelId", channelId);
		DaoHandler daoHandler = new DaoHandler<CategoryDao, CategoryCNBean>(categoryDao, "getCNCategoryInfo", data, "mainCategoryId");
		Map<String, CategoryCNBean> resultMap = (Map<String, CategoryCNBean>) daoHandler.getCategoryInfoByGroup();
		CategoryCNBean fieldBean = null;
		CategoryCNBean resultBean = null;
		if (resultMap != null) {
			fieldBean = resultMap.get("fieldBean");
			resultBean = resultMap.get("resultBean");
			if (resultBean != null) {
				// 获取取得mainCategoryId值所对应的CategoryId,填入resultVBean
				if (fieldBean != null) {
					// 如果mainCategoryId值所对应的CategoryId是本身的CategoryID就对MainParentCategoryId填入null
					if (!StringUtils.null2Space(String.valueOf(fieldBean.getCategoryId())).equals(
							StringUtils.null2Space(String.valueOf(resultBean.getCategoryId())))) {
						resultBean.setMainParentCategoryId(String.valueOf(fieldBean.getCategoryId()));

					}
					resultBean.setMainParentCategoryTypeId(CmsConstants.Category.MAIN_CATEGORY_TYPE);
				}

				// 获取mainCategoryName
				String mainCategoryId = resultBean.getMainCategoryId();
				Map<String, Object> mainCategory = categoryDao.getMainCategoryName(mainCategoryId);
				if (mainCategory != null && !mainCategory.isEmpty()) {
					resultBean.setMainCategoryName(mainCategory.get("mainCategoryName").toString());
				}

				// 获取TmCategoryName
				String tmCategoryId = resultBean.getTmCategoryId();
				String jdCategoryId = resultBean.getJdCategoryId();
				HashMap<String, Object> tmMap = new HashMap<String, Object>();
				tmMap.put("id", tmCategoryId);
				tmMap.put("platformId", CmsConstants.Category.PLATFORM_TM_ID);
				Map<String, Object> tmCategoryMap = categoryDao.getTMJDName(tmMap);
				if (tmCategoryMap != null && !tmCategoryMap.isEmpty()) {
					resultBean.setTmCategoryName(tmCategoryMap.get("name").toString());
				}

				// 获取JdCategoryName
				HashMap<String, Object> jdMap = new HashMap<String, Object>();
				jdMap.put("id", jdCategoryId);
				jdMap.put("platformId", CmsConstants.Category.PLATFORM_JD_ID);
				Map<String, Object> jdCategoryMap = categoryDao.getTMJDName(jdMap);
				if (jdCategoryMap != null && !jdCategoryMap.isEmpty()) {
					resultBean.setJdCategoryName(jdCategoryMap.get("name").toString());
				}
				// 获取SizeChartName、SizeChartUrl
				String sizeChartId = resultBean.getSizeChartId();
				Map<String, Object> sizemap = new HashMap<String, Object>();
				sizemap.put("sizeChartId", sizeChartId);
				sizemap.put("channelId", resultBean.getChannelId());
				SizeChartInfo sizeChartInfo = setsizechartDao.doGetSizeChartInfo(sizemap);
				if (sizeChartInfo != null ) {
					resultBean.setSizeChartName(sizeChartInfo.getSizeChartName());
					resultBean.setSizeChartUrl(sizeChartInfo.getSizeChartImageUrl());
				}

			}
		}
		return resultBean;

	}

	/**
	 * 获取US的Category信息
	 * 
	 * @throws Exception
	 */
	public CategoryUSBean getUSCategoryInfo(String categoryId, String channelId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", categoryId);
		data.put("channelId", channelId);

		DaoHandler daoHandler = new DaoHandler<CategoryDao, CategoryUSBean>(categoryDao, "getUSCategoryInfo", data, "");
		Map<String, CategoryUSBean> resultMap = daoHandler.getCategoryInfoByGroup();
		CategoryUSBean resultBean = null;
		if (resultMap != null) {
			resultBean = resultMap.get("resultBean");
		}
		return resultBean;

	}

	/**
	 * 获取USSubCategory
	 */
	@Override
	public List<CategoryUSBean> getUSSubCategoryList(String categoryId, String channelId) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("categoryId", categoryId);
		data.put("channelId", channelId);
		List<CategoryUSBean> list = categoryDao.getUSSubCategoryList(data);
		return list;

	}

	/**
	 * 获取该category下的子分类
	 * 
	 * @param categoryId
	 */
	public List<CategoryCNBean> doGetCNSubCategoryList(String categoryId, String channelId) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("categoryId", categoryId);
		data.put("channelId", channelId);
		List<CategoryCNBean> ret = categoryDao.doGetCNSubCategoryList(data);
		return ret;
	}

	/**
	 * 获取CategoryUSModelList信息
	 * 
	 * @param data
	 * @return
	 */
	@Override
	public List<CategoryModelBean> getUSCategoryModelList(HashMap<String, Object> data) {
		List<CategoryModelBean> result = categoryDao.getUSCategoryModelList(data);
		return result;
	}

	/**
	 * 获取CategoryCNModelList信息
	 * 
	 * @param data
	 * @return
	 */
	@Override
	public List<CategoryModelBean> getCNCategoryModelList(HashMap<String, Object> data) {
		List<CategoryModelBean> result = categoryDao.getCNCategoryModelList(data);
		return result;
	}

	/**
	 * 获取Category下的USProduct信息
	 * 
	 * @throws Exception
	 */
	@Override
	public DtResponse<List<CategoryProductUSBean>> getCategoryUSProductList(HashMap<String, Object> data) throws Exception {
		List<CategoryProductUSBean> result = categoryDao.getUSCategoryProductList(data);
//		if (result != null && result.size() > 0) {
//			for (CategoryProductUSBean categoryProductUSBean : result) {
//				ModelUSBean modelUSBean = modelEditService.doGetUSModelInfo(categoryProductUSBean.getModelId(), categoryProductUSBean.getChannelId(), true);
//				if (modelUSBean != null) {
//					CommonUtils.merger(categoryProductUSBean, modelUSBean);
//				}
//			}
//		}
		DtResponse<List<CategoryProductUSBean>> dtResponse = new DtResponse<List<CategoryProductUSBean>>();
		dtResponse.setData(result);
		int count = categoryDao.getCategoryUSProductCount(data);
		dtResponse.setRecordsFiltered(count);
		dtResponse.setRecordsTotal(count);

		return dtResponse;
	}

	/**
	 * 获取Category下的分页CNProduct信息
	 * 
	 * @throws Exception
	 */
	@Override
	public DtResponse<List<CategoryProductCNBean>> getCategoryCNProductList(HashMap<String, Object> data) throws Exception {

		List<CategoryProductCNBean> result = categoryDao.getCategoryProductCNList(data);
//		for (CategoryProductCNBean categoryProductCNBean : result) {
//			ModelCNBean modelCNBean = modelEditService.doGetCNModelInfo(categoryProductCNBean.getModelId(), categoryProductCNBean.getChannelId(), true);
//			if (modelCNBean != null) {
//				CommonUtils.merger(categoryProductCNBean, modelCNBean.getCnBaseModelInfo());
//			}
//		}
		DtResponse<List<CategoryProductCNBean>> dtResponse = new DtResponse<List<CategoryProductCNBean>>();
		dtResponse.setData(result);
		int count = categoryDao.getCategoryUSProductCount(data);
		dtResponse.setRecordsFiltered(count);
		dtResponse.setRecordsTotal(count);
		return dtResponse;
	}

	/**
	 * 获取Category的父节点List
	 * 
	 * @param categoryId
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CategoryListBean> getCategoryList(String categoryId, String channelId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", categoryId);
		data.put("channelId", channelId);
		DaoHandler daoHandler = new DaoHandler<CategoryDao, CategoryPriceSettingBean>(categoryDao, "getCategoryList", data, "");
		List<CategoryListBean> resultList = daoHandler.getCategoryGroup();
		return resultList;

	}

	/**
	 * 获取Product下的Model以及Category
	 * 
	 * @param modelId
	 * @param categoryId
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getModelInfo(String modelId, String categoryId, String channelId) throws Exception {
		List<CategoryListBean> categoryList = getCategoryList(categoryId, channelId);
		Collections.reverse(categoryList);
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("modelId", modelId);
		data.put("channelId", channelId);
		Map<String, Object> modelInfo = categoryDao.getModelInfo(data);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("categoryList", categoryList);
		resultMap.put("modelInfo", modelInfo);
		return resultMap;
	}

	/**
	 * 更新USCategory信息
	 * 
	 * @throws Exception
	 */
	@Transactional("transactionManagerCms")
	@Override
	public boolean updateUSCategoryInfo(CategoryUSBean inCategoryBean, UserSessionBean user) throws Exception {

		boolean isSuccess = true;
		Category category = new Category();
		if (inCategoryBean != null) {
			String channelId = inCategoryBean.getChannelId();
			int categoryId = inCategoryBean.getCategoryId();
			CategoryUSBean categoryBean = getUSCategoryInfo(String.valueOf(categoryId), channelId);
			// 对Category数据进行封装
			CommonUtils.copyBean(inCategoryBean, category);
			category.setCategoryId(categoryId);
			category.setChannelId(channelId);
			category.setCreater(user.getUserName());
			category.setModifier(user.getUserName());
			isSuccess = updateUSCategory(category);
			if (isSuccess) {
				CategoryExtend categoryExtend = new CategoryExtend();
				categoryExtend.setCategoryId(categoryId);
				categoryExtend.setChannelId(channelId);
				CommonUtils.packingBean(categoryExtend, inCategoryBean, categoryBean);
				isSuccess = updateUSCategoryExtendInfo(categoryExtend, user);
			}
			if (isSuccess) {
				AmazonCategoryExtend amazoneCategoryExtend = new AmazonCategoryExtend();
				amazoneCategoryExtend.setCategoryId(categoryId);
				amazoneCategoryExtend.setChannelId(channelId);
				CommonUtils.packingBean(amazoneCategoryExtend, inCategoryBean, categoryBean);
				isSuccess = updateUSCategoryAmazonInfo(amazoneCategoryExtend, user);
			}
			if (isSuccess) {
				GoogleCategoryExtend googleCategoryExtend = new GoogleCategoryExtend();
				googleCategoryExtend.setCategoryId(categoryId);
				googleCategoryExtend.setChannelId(channelId);
				CommonUtils.packingBean(googleCategoryExtend, inCategoryBean, categoryBean);
				isSuccess = updateUSCategoryGoogleInfo(googleCategoryExtend, user);
			}
			if (isSuccess) {
				PricegrabberCategoryExtend pricegrabberCategoryExtend = new PricegrabberCategoryExtend();
				CommonUtils.packingBean(pricegrabberCategoryExtend, inCategoryBean, categoryBean);
				pricegrabberCategoryExtend.setCategoryId(categoryId);
				pricegrabberCategoryExtend.setChannelId(channelId);
				isSuccess = updateUSCategoryPricegrabber(pricegrabberCategoryExtend, user);
			}
			if (isSuccess) {
				boolean isPublished = inCategoryBean.isPublished();
				isSuccess = updatePublishCategory(categoryId, channelId, isPublished, user, CmsConstants.CountryType.TYPE_US);
			}
		}
		return isSuccess;

	}

	/**
	 * 更新USCategory信息
	 * 
	 * @param category
	 * @return
	 */
	public boolean updateUSCategory(Category category) {
		boolean isSuccess = true;
		// 对category表进行更新
		isSuccess = categoryDao.updateCategoryInfo(category);
		return isSuccess;

	}

	/**
	 * 更新CategoryExtend信息
	 * 
	 * @throws Exception
	 */
	@Transactional
	@Override
	public boolean updateUSCategoryExtendInfo(CategoryExtend categoryExtend, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		// 对categoryExtend进行数据封装
		String channelId = categoryExtend.getChannelId();
		int categoryId = categoryExtend.getCategoryId();
		categoryExtend.setCreater(user.getUserName());
		categoryExtend.setModifier(user.getUserName());
		// 封装查询条件
		HashMap<String, Object> data = getQueryData(categoryId, channelId);

		CategoryExtend catagoryextendselect = categoryDao.getCategoryExtend(data);
		if (catagoryextendselect != null) {
			// 如果都为空进行categoryExtend该条信息的删除操作
			if (CommonUtils.isNullBean(categoryExtend)) {
				isSuccess = categoryDao.deleteCategoryExtend(data);
			} else {
				// 查询到categoryId对应的CategoryExtend数据不为空，进行更新操作
				isSuccess = categoryDao.updateCategoryExtendInfo(categoryExtend);

			}
		} else {
			// 查询到categoryId对应的CategoryExtend数据为空，并且seoCanonical、seoKeywords、seoDescription、seoTitle有一条不为空就增加一条数据
			if (!CommonUtils.isNullBean(categoryExtend)) {
				isSuccess = categoryDao.insertCategoryExtend(categoryExtend);
			}
		}

		return isSuccess;

	}

	/**
	 * 更新Amazon信息
	 * 
	 * @throws Exception
	 */
	@Transactional
	@Override
	public boolean updateUSCategoryAmazonInfo(AmazonCategoryExtend amazoneCategoryExtend, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		String channelId = amazoneCategoryExtend.getChannelId();
		int categoryId = amazoneCategoryExtend.getCategoryId();

		// 封装查询条件
		HashMap<String, Object> data = getQueryData(categoryId, channelId);
		amazoneCategoryExtend.setCreater(user.getUserName());
		amazoneCategoryExtend.setModifier(user.getUserName());
		// 查询AmazonCategoryExtend信息
		AmazonCategoryExtend amazoneCategoryExtendselect = categoryDao.getAmazonCategoryExtend(data);
		// 如果amazoneCategoryExtendselect不为空，AmazonBrowseTreeId的数据为空进行删除操作，不为空进行更新操作
		if (amazoneCategoryExtendselect != null) {
			if (CommonUtils.isNullBean(amazoneCategoryExtend)) {
				isSuccess = categoryDao.deleteAmazonCategoryExtend(data);
			} else {
				isSuccess = categoryDao.updateAmazonCategoryExtend(amazoneCategoryExtend);
			}
		} else {
			// 如果为空，AmazonBrowseTreeId不为空，进行插入操作
			if (!CommonUtils.isNullBean(amazoneCategoryExtend)) {
				isSuccess = categoryDao.insertAmazonCategoryExtend(amazoneCategoryExtend);
			}
		}

		return isSuccess;

	}

	/**
	 * 更新GoogleInfo信息
	 * 
	 * @return
	 */
	@Transactional
	@Override
	public boolean updateUSCategoryGoogleInfo(GoogleCategoryExtend googleCategoryExtend, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		try {

			// 封装查询条件
			HashMap<String, Object> data = getQueryData(googleCategoryExtend.getCategoryId(), googleCategoryExtend.getChannelId());
			googleCategoryExtend.setCreater(user.getUserName());
			googleCategoryExtend.setModifier(user.getUserName());
			// 查询AmazonCategoryExtend信息
			GoogleCategoryExtend googleCategoryExtendselect = categoryDao.getGoogleCategoryExtend(data);
			// 如果amazoneCategoryExtendselect不为空，AmazonBrowseTreeId的数据为空进行删除操作，不为空进行更新操作
			if (googleCategoryExtendselect != null) {
				if (CommonUtils.isNullBean(googleCategoryExtend)) {
					isSuccess = categoryDao.deleteGoogleCategoryExtend(data);
				} else {
					isSuccess = categoryDao.updateGoogleCategoryExtend(googleCategoryExtend);
				}
			} else {
				// 如果为空，AmazonBrowseTreeId不为空，进行插入操作
				if (!CommonUtils.isNullBean(googleCategoryExtend)) {
					isSuccess = categoryDao.insertGoogleCategoryExtend(googleCategoryExtend);
				}
			}
		} catch (Exception e) {
			isSuccess = false;

		}

		return isSuccess;
	}

	/**
	 * 更新CategoryPricegrabber信息
	 */
	@Transactional
	@Override
	public boolean updateUSCategoryPricegrabber(PricegrabberCategoryExtend pricegrabberCategoryExtend, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		try {
			// 封装查询条件
			HashMap<String, Object> data = getQueryData(pricegrabberCategoryExtend.getCategoryId(), pricegrabberCategoryExtend.getChannelId());

			pricegrabberCategoryExtend.setCreater(user.getUserName());
			pricegrabberCategoryExtend.setModifier(user.getUserName());
			// 查询PricegrabberCategoryExtend信息
			PricegrabberCategoryExtend pricegrabberCategoryExtendselect = categoryDao.getpricegrabberCategoryExtend(data);
			// 如果pricegrabberCategoryExtendselect不为空，PricegrabberCategoryId的数据为空进行删除操作，不为空进行更新操作
			if (pricegrabberCategoryExtendselect != null) {
				if (CommonUtils.isNullBean(pricegrabberCategoryExtend)) {
					isSuccess = categoryDao.deletePricegrabberCategoryExtend(data);
				} else {
					isSuccess = categoryDao.updatePricegrabberCategoryExtend(pricegrabberCategoryExtend);
				}
			} else {
				// 如果为空，PricegrabberCategoryId不为空，进行插入操作
				if (!CommonUtils.isNullBean(pricegrabberCategoryExtend)) {
					isSuccess = categoryDao.insertPricegrabberCategoryExtend(pricegrabberCategoryExtend);
				}
			}
		} catch (Exception e) {
			isSuccess = false;

		}

		return isSuccess;

	}

	/**
	 * 更新PublishCategory信息
	 */
	@Transactional
	@Override
	public boolean updatePublishCategory(int categoryId, String channelId, boolean isPublished, UserSessionBean user, String type) {
		boolean isSuccess = true;
		try {

			HashMap<String, Object> data = getQueryData(categoryId, channelId);

			PublishCategory publishCategoryselect = categoryDao.getPublishCategory(data);
			PublishCategory publishCategory = new PublishCategory();
			// 对PublishCategory进行数据封装
			publishCategory.setCategoryId(categoryId);
			publishCategory.setChannelId(channelId);
			publishCategory.setCreater(user.getUserName());
			publishCategory.setModifier(user.getUserName());
			publishCategory.setPublishStatus(CmsConstants.Category.CATEGORY_PUBLISHSTATUS);
			publishCategory.setIsPublished(CmsConstants.Category.CATEGORY_ISPUBLISHED_NO);
			if (type.equals(CmsConstants.CountryType.TYPE_US)) {
				publishCategory.setCartId(CmsConstants.CartId.US_OFFICIAL_CARTID);
			} else {
				publishCategory.setCartId(CmsConstants.CartId.CN_OFFICIAL_CARTID);
			}
			if (isPublished) {
				if (publishCategoryselect != null) {

					isSuccess = categoryDao.updatePublicCategory(publishCategory);
				} else {
					isSuccess = categoryDao.insertPublicCategory(publishCategory);
				}

			} else {
				if (publishCategoryselect != null) {

					isSuccess = categoryDao.updatePublicCategory(publishCategory);
				}
			}

		} catch (Exception e) {
			isSuccess = false;
			logger.info(e);
		}

		return isSuccess;

	}

	/**
	 * 对CategoryCN进行更新
	 */
	@Transactional("transactionManagerCms")
	@Override
	public boolean updateCNCategoryInfo(CategoryCNBean inCategoryBean, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		CnCategory cnCategory = new CnCategory();
		CnCategoryExtend cnCategoryExtend = new CnCategoryExtend();
		String channelId = inCategoryBean.getChannelId();
		int categoryId = inCategoryBean.getCategoryId();
		CategoryCNBean categoryBean = getCNCategoryInfo(String.valueOf(categoryId), channelId);

		// 对Category数据进行封装
		cnCategory.setCategoryId(categoryId);
		cnCategory.setChannelId(channelId);
		cnCategory.setCreater(user.getUserName());
		cnCategory.setModifier(user.getUserName());
		CommonUtils.copyBean(inCategoryBean, cnCategory);
		// 对category表进行更新
		isSuccess = updateCnCategory(cnCategory);
		// 对categoryExtend进行数据封装
		cnCategoryExtend.setCategoryId(categoryId);
		cnCategoryExtend.setChannelId(channelId);
		cnCategoryExtend.setCreater(user.getUserName());
		cnCategoryExtend.setModifier(user.getUserName());
		CommonUtils.packingBean(cnCategoryExtend, inCategoryBean, categoryBean);
		HashMap<String, Object> data = getQueryData(categoryId, channelId);
		if (isSuccess) {
			isSuccess = updateCnCategoryExtend(cnCategoryExtend, user);
		}
		if (isSuccess) {
			boolean isPublished = inCategoryBean.isCnIsPublished();
			isSuccess = updatePublishCategory(categoryId, channelId, isPublished, user, CmsConstants.CountryType.TYPE_CN);
		}
		return isSuccess;

	}

	/**
	 * 更新CnCategory
	 * 
	 * @param cnCategory
	 * @return
	 */
	public boolean updateCnCategory(CnCategory cnCategory) {
		boolean isSuccess = true;
		// 对category表进行更新
		isSuccess = categoryDao.updateCnCategory(cnCategory);
		return isSuccess;

	}

	/**
	 * 更新CnCategoryExtend
	 */
	@Override
	public boolean updateCnCategoryExtend(CnCategoryExtend cnCategoryExtend, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		HashMap<String, Object> data = getQueryData(cnCategoryExtend.getCategoryId(), cnCategoryExtend.getChannelId());
		CnCategoryExtend catagoryextendselect = categoryDao.getCnCategoryExtend(data);
		if (catagoryextendselect != null) {
			// 如果都为空进行cncategoryExtend该条信息的删除操作
			if (CommonUtils.isNullBean(cnCategoryExtend)) {
				isSuccess = categoryDao.deleteCnCategoryExtend(data);
			} else {
				// 查询到categoryId对应的cnCategoryExtend数据不为空，进行更新操作
				cnCategoryExtend.setModifier(user.getUserName());
				isSuccess = categoryDao.updateCnCategoryExtend(cnCategoryExtend);
			}
		} else {
			// 查询到categoryId对应的cnCategoryExtend数据为空，并且seoCanonical、seoKeywords、seoDescription、seoTitle有一条不为空就增加一条数据
			if (!CommonUtils.isNullBean(cnCategoryExtend)) {
				cnCategoryExtend.setCreater(user.getUserName());
				isSuccess = categoryDao.insertCnCategoryExtend(cnCategoryExtend);
			}
		}
		return isSuccess;
	}

	/**
	 * 更新JdCategoryExtend
	 */
	@Override
	public boolean updateJdCategoryExtend(JdCategoryExtend jdCategoryExtend, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		HashMap<String, Object> data = getQueryData(jdCategoryExtend.getCategoryId(), jdCategoryExtend.getChannelId());
		jdCategoryExtend.setCreater(user.getUserName());
		jdCategoryExtend.setModifier(user.getUserName());
		JdCategoryExtend jdCategoryExtendselect = categoryDao.getJdCategoryExtend(data);
		if (jdCategoryExtendselect != null) {
			// 如果都为空进行jdCategoryExtend该条信息的删除操作
			if (CommonUtils.isNullBean(jdCategoryExtend)) {
				isSuccess = categoryDao.deleteJdCategoryExtend(data);
			} else {
				// 查询到categoryId对应的jdCategoryExtend数据不为空，进行更新操作
				isSuccess = categoryDao.updateJdCategoryExtend(jdCategoryExtend);
			}
		} else {
			// 查询到categoryId对应的jdCategoryExtend数据为空
			if (!CommonUtils.isNullBean(jdCategoryExtend)) {
				isSuccess = categoryDao.insertJdCategoryExtend(jdCategoryExtend);
			}
		}
		return isSuccess;
	}

	/**
	 * 更新CnTMCategoryExtend
	 */
	@Override
	public boolean updateCnTMCategoryExtend(CnTMCategoryExtend cnTMCategoryExtend, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		HashMap<String, Object> data = getQueryData(cnTMCategoryExtend.getCategoryId(), cnTMCategoryExtend.getChannelId());
		cnTMCategoryExtend.setCreater(user.getUserName());
		cnTMCategoryExtend.setModifier(user.getUserName());
		CnTMCategoryExtend cnTMCategoryExtendselect = categoryDao.getCnTMCategoryExtend(data);
		if (cnTMCategoryExtendselect != null) {
			// 如果都为空进行cnTMCategoryExtend该条信息的删除操作
			if (CommonUtils.isNullBean(cnTMCategoryExtend)) {
				isSuccess = categoryDao.deleteCnTMCategoryExtend(data);
			} else {
				// 查询到categoryId对应的cnTMCategoryExtend数据不为空，进行更新操作
				isSuccess = categoryDao.updateCnTMCategoryExtend(cnTMCategoryExtend);
			}
		} else {
			// 查询到categoryId对应的cnTMCategoryExtend数据为空，并且seoCanonical、seoKeywords、seoDescription、seoTitle有一条不为空就增加一条数据
			if (!CommonUtils.isNullBean(cnTMCategoryExtend)) {
				isSuccess = categoryDao.insertCnTMCategoryExtend(cnTMCategoryExtend);
			}
		}
		return isSuccess;
	}

	/**
	 * 更新PriceSettingBean的信息
	 * 
	 * @throws Exception
	 */
	@Transactional("transactionManagerCms")
	@Override
	public boolean updateCategoryCNPriceSettingInfo(CategoryPriceSettingBean priceSettingBean, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		int categoryId = priceSettingBean.getCategoryId();
		String channelId = priceSettingBean.getChannelId();
		CategoryPriceSettingBean priceSettingBeanSelect = doGetCategoryCNPriceSettingInfo(String.valueOf(categoryId), channelId);
		CnCategoryPrice resultBean = new CnCategoryPrice();
		resultBean.setCategoryId(categoryId);
		resultBean.setChannelId(channelId);
		resultBean.setCreater(user.getUserName());
		resultBean.setModifier(user.getUserName());
		CommonUtils.packingBean(resultBean, priceSettingBean, priceSettingBeanSelect);
		isSuccess = updateCnCategoryPrice(resultBean, user);
		if (isSuccess) {
			HistoryPriceSetting historyPriceSetting = new HistoryPriceSetting();
			historyPriceSetting.setCategoryModelProductId(categoryId);
			historyPriceSetting.setPriceSettingType(CmsConstants.Category.HISTORY_PRICE_SETTING_TYPE_CATEGORY);
			historyPriceSetting.setCreater(user.getUserName());
			historyPriceSetting.setModifier(user.getUserName());
			historyPriceSetting.setComment(priceSettingBean.getComment());
			CommonUtils.copyBean(priceSettingBean, historyPriceSetting);
			historyPriceSetting.setChannelId(channelId);
			if (historyPriceSetting.getPricingFactor() != null) {
				historyPriceSetting.setPricingFactor(historyPriceSetting.getPricingFactor().equals("") ? null : historyPriceSetting.getPricingFactor());
			}
			isSuccess = addHistoryPriceSetting(historyPriceSetting);

		}

		// 更新category下的所有的product的价格
		Map<String, Object> data = new HashMap<String, Object>();
		// 获取该category下的所有子的category
		List<Integer> subCategroyList = getSubCategoryId(priceSettingBean.getCategoryId(), priceSettingBean.getChannelId());
		subCategroyList.add(priceSettingBean.getCategoryId());
		data.put("subCategroyList", subCategroyList);
		data.put("channelId", priceSettingBean.getChannelId());
		// 获取该category下的所有子category所对应的model的PriceSetting信息
		List<Map<String, Object>> subCategoryModelPriceSettings = categoryDao.getModelPriceSetting(data);
		// 循环所有的子category
		for (Map<String, Object> map : subCategoryModelPriceSettings) {
			// 循环子category 下的所有的model
			List<ModelPriceSettingBean> modelPriceSettingList = (List<ModelPriceSettingBean>) map.get("modelList");
			for (ModelPriceSettingBean modelPriceSettingBean : modelPriceSettingList) {
				// model是从category继承的场合
				if (modelPriceSettingBean.isExistNull()) {
					// 价格重新计算
					ModelPriceSettingBean priceSetting = modelEditService.doGetModelCNPriceSettingInfo(modelPriceSettingBean.getModelId().toString(),
							channelId, true);
					modelEditService.doUpdateModelProductPriceSetting(priceSetting, user.getUserName());
				}
			}
		}
		return isSuccess;

	}

	@Override
	public boolean updateCnCategoryPrice(CnCategoryPrice price, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		HashMap<String, Object> data = getQueryData(price.getCategoryId(), price.getChannelId());
		if (price.getPricingFactor() != null) {
			price.setPricingFactor(price.getPricingFactor().equals("") ? null : price.getPricingFactor());
		}
		CnCategoryPrice cnCategoryPriceselect = categoryDao.getCnCategoryPrice(data);
		if (cnCategoryPriceselect != null) {
			if (CommonUtils.isNullBean(price)) {
				isSuccess = categoryDao.deleteCnCategoryPriceSetting(data);
			} else {
				isSuccess = categoryDao.updateCnCategoryPriceSetting(price);
			}
		} else {
			if (!CommonUtils.isNullBean(price)) {
				isSuccess = categoryDao.insertCnCategoryPriceSetting(price);
			}
		}
		return isSuccess;
	}

	/**
	 * 插入HistoryPriceSetting
	 * 
	 * @param historyPriceSetting
	 * @return
	 */
	public boolean addHistoryPriceSetting(HistoryPriceSetting historyPriceSetting) {
		boolean isSuccess = true;
		isSuccess = categoryDao.insertHistoryPriceSetting(historyPriceSetting);
		return isSuccess;
	}

	@Transactional("transactionManagerCms")
	@Override
	public boolean updateMainCategoryId(HashMap<String, Object> data, UserSessionBean user) throws Exception {
		boolean isSuccess = true;
		Integer categoryId = (Integer) data.get("id");
		String channelId = String.valueOf(data.get("channelId"));
		
		// 更新CategoryExtend
		CategoryExtend categoryExtend = new CategoryExtend();
		categoryExtend.setCategoryId(categoryId);
		categoryExtend.setChannelId(channelId);
		if(data.get("mainCategoryId") != null) {
			categoryExtend.setMainCategoryId(String.valueOf(data.get("mainCategoryId")));
		}
		isSuccess = updateUSCategoryExtendInfo(categoryExtend, user);
		if (isSuccess) {
			// 更新 CnCategoryExtend
			CnCategoryExtend cnCategoryExtend = new CnCategoryExtend();
			cnCategoryExtend.setCategoryId(categoryId);
			cnCategoryExtend.setChannelId(channelId);
			if(data.get("mainCategoryId") != null) {
			  cnCategoryExtend.setMainCategoryId(String.valueOf(data.get("mainCategoryId")));
			}
			isSuccess = updateCnCategoryExtend(cnCategoryExtend, user);
		}
		if (isSuccess) {
			// 更新CnTMCategoryExtend
			CnTMCategoryExtend cnTMCategoryExtend = new CnTMCategoryExtend();
			cnTMCategoryExtend.setCategoryId(categoryId);
			cnTMCategoryExtend.setChannelId(channelId);
			if(data.get("tmCategoryId")!=null){
				cnTMCategoryExtend.setTmCategoryId(Integer.parseInt((String)data.get("tmCategoryId")));
			}
			
			isSuccess = updateCnTMCategoryExtend(cnTMCategoryExtend, user);
		}
		if (isSuccess) {
			// 更新JdCategoryExtend
			JdCategoryExtend jdCategoryExtend = new JdCategoryExtend();
			jdCategoryExtend.setCategoryId(categoryId);
			jdCategoryExtend.setChannelId(channelId);
			if(data.get("jdCategoryId")!=null){
				jdCategoryExtend.setJdCategoryId(Integer.parseInt((String)data.get("jdCategoryId")));
			}
			isSuccess = updateJdCategoryExtend(jdCategoryExtend, user);
		}
        if(isSuccess) {
           Map<String,Object> param = new HashMap<String, Object>();
           param.put("categoryId", categoryId);
           param.put("channelId", channelId);
           isSuccess = updateImsProduct(param);           
        }
		return isSuccess;

	}

	public HashMap<String, Object> getQueryData(int categoryId, String channelId) {
		// 封装查询条件
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("categoryId", categoryId);
		data.put("channelId", channelId);
		return data;
	}

	/**
	 * 获取指定Categoryid下的子子孙孙的categoryid
	 * 
	 * @param categoryId
	 * @param channelId
	 * @return
	 */
	private List<Integer> getSubCategoryId(Integer categoryId, String channelId) {
		List<CategoryBean> categorys = commonDao.getSubCategoryMenuList(channelId, categoryId.toString());
		List<Integer> subCategroyList = new ArrayList<Integer>();
		categroyTreeToList(categorys, subCategroyList);
		return subCategroyList;

	}

	/**
	 * 把树状结构的categorys 转成list
	 * 
	 * @param categorys
	 * @param subCategroyList
	 */
	private void categroyTreeToList(List<CategoryBean> categorys, List<Integer> subCategroyList) {
		for (CategoryBean categoryBean : categorys) {
			subCategroyList.add(categoryBean.getCategoryId());
			if (categoryBean.getChildren().size() > 0) {
				categroyTreeToList(categoryBean.getChildren(), subCategroyList);
			}
		}
	}

	public int getCategoryUSProductCount(Map<String, Object> data) {
		int count = categoryDao.getCategoryUSProductCount(data);
		return count;

	}
	/**
	 * 插入ImsProduct
	 * @param data
	 * @return
	 */
	public boolean updateImsProduct (Map<String,Object> data) {
		boolean isSuccess = categoryDao.updateImsProduct(data);
		return isSuccess;
	
	}
}
