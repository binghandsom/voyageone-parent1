package com.voyageone.cms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.cms.dao.SearchDao;
import com.voyageone.cms.formbean.*;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.modelbean.UserSessionBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.dao.CategoryDao;
import com.voyageone.cms.dao.ModelDao;
import com.voyageone.cms.service.CategoryEditService;
import com.voyageone.cms.service.ModelEditService;
import com.voyageone.cms.service.SetsizechartService;
import com.voyageone.cms.utils.CommonUtils;

//@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class ModelEditServiceImpl implements ModelEditService {

	private static Log logger = LogFactory.getLog(ModelEditServiceImpl.class);

	@Autowired
	private ModelDao modelDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private SearchDao searchDao;

	@Autowired
	private CategoryEditService categoryEditService;

	@Autowired
	private SetsizechartService setsizechartService;

	@Autowired
	private CommonServiceImpl commonServiceImpl;

	@Autowired
	private SimpleTransaction simpleTransactionCms;

	/**
	 * 取得model的US数据
	 */
	@Override
	public ModelUSBean doGetUSModelInfo(String modelId, String channelId, boolean isExtend) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("modelId", modelId);
		data.put("channelId", channelId);
		ModelUSBean modelBean = modelDao.doGetUSModelInfo(data);
		if (modelBean != null && isExtend) {
			// 对应的PrimaryCategory存在的场合
			if (modelBean.getIsPrimaryCategory() == 1 && modelBean.getPrimaryCategoryId() != null && modelBean.getPrimaryCategoryId() != 0) {
				// 取得PrimaryCategory的信息 把model里面是null的属性用上
				CategoryUSBean categoryUSBean = categoryEditService.getUSCategoryInfo(modelBean.getPrimaryCategoryId().toString(), channelId);
				CommonUtils.merger(modelBean, categoryUSBean);
			}
		}
		return modelBean;
	}

	/**
	 * 取得model的CN数据
	 */
	@Override
	public ModelCNBean doGetCNModelInfo(String modelId, String channelId, boolean isExtend) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("modelId", modelId);
		data.put("channelId", channelId);
		ModelCNBean modelCNBean = modelDao.doGetCNModelInfo(data);
		CategoryCNBean categoryCNBean = null;
		ModelCNBaseBean cnBaseModelInfo = null;
		Boolean isMainCategoryId = false;
		if (modelCNBean != null) {
			if (modelCNBean.getCnBaseModelInfo().getMainCategoryId() != null) {
				isMainCategoryId = true;
				modelCNBean.getCnBaseModelInfo().setMainPaerntCategoryTypeId(CmsConstants.IDType.TYPE_MODEL);
			}
			if (isExtend) {
				// 对应的PrimaryCategory存在的场合
				if (modelCNBean.getCnBaseModelInfo().getCnIsPrimaryCategory() == 1 && modelCNBean.getCnBaseModelInfo().getPrimaryCategoryId() != null
						&& modelCNBean.getCnBaseModelInfo().getPrimaryCategoryId() != 0) {
					// 取得PrimaryCategory的信息 把model里面是null的属性用上
					categoryCNBean = categoryEditService.getCNCategoryInfo(modelCNBean.getCnBaseModelInfo().getPrimaryCategoryId().toString(), channelId);
					CommonUtils.merger(modelCNBean.getCnBaseModelInfo(), categoryCNBean);
					CommonUtils.merger(modelCNBean.getJdModelInfo(), categoryCNBean);
					CommonUtils.merger(modelCNBean.getTmModelInfo(), categoryCNBean);
					cnBaseModelInfo = modelCNBean.getCnBaseModelInfo();
				}
			}
		}

		if (cnBaseModelInfo != null) {
			// MainCategoryId发生变化的场合
			if (cnBaseModelInfo.getMainCategoryId() != null && !isMainCategoryId) {
				// 设定主CategoryName
				cnBaseModelInfo.setMainCategoryName(categoryCNBean.getMainCategoryName());
				// 如果父的category信息中的MainParentCategoryId是空的场合说明
				// MainParentCategoryId就是他本身
				if (StringUtils.isEmpty(categoryCNBean.getMainParentCategoryId())) {
					cnBaseModelInfo.setMainParentCategoryId(categoryCNBean.getCategoryId() + "");
				} else {
					cnBaseModelInfo.setMainParentCategoryId(categoryCNBean.getMainParentCategoryId());
				}
				cnBaseModelInfo.setMainPaerntCategoryTypeId(CmsConstants.IDType.TYPE_CATEGORY);
			}

			// 设定主CategoryName
			if (StringUtils.isEmpty(cnBaseModelInfo.getMainCategoryName()) && cnBaseModelInfo.getMainCategoryId() != null) {

				Map<String, Object> ret = categoryDao.getMainCategoryName(cnBaseModelInfo.getMainCategoryId().toString());

				if (ret != null) {
					cnBaseModelInfo.setMainCategoryName(ret.get("mainCategoryName").toString());
				}else{
					cnBaseModelInfo.setMainCategoryId(null);
				}
			}

			// 查询SizeChartName和SizeChartUrl
			Map<String, Object> sizemap = new HashMap<String, Object>();
			sizemap.put("sizeChartId", cnBaseModelInfo.getSizeChartId());
			sizemap.put("channelId", cnBaseModelInfo.getChannelId());
			SizeChartInfo sizeChartInfo = setsizechartService.doGetSizeChart(sizemap);
			if (sizeChartInfo != null ) {
				cnBaseModelInfo.setSizeChartName(sizeChartInfo.getSizeChartName());
				cnBaseModelInfo.setSizeChartUrl(sizeChartInfo.getSizeChartImageUrl());
			}

			// 获取JD类目名
			if (StringUtils.isEmpty(modelCNBean.getJdModelInfo().getJdCategoryName()) && modelCNBean.getJdModelInfo().getJdCategoryId() != null) {
				HashMap<String, Object> datamap = new HashMap<String, Object>();
				datamap.put("id", modelCNBean.getJdModelInfo().getJdCategoryId());
				datamap.put("platformId", CmsConstants.Category.PLATFORM_JD_ID);
				Map<String, Object> ret = categoryDao.getTMJDName(datamap);
				if (ret != null) {
					cnBaseModelInfo.setMainCategoryName(ret.get("name").toString());
				}
			}

			// 获取TM类目名
			if (StringUtils.isEmpty(modelCNBean.getTmModelInfo().getTmCategoryName()) && modelCNBean.getTmModelInfo().getTmCategoryId() != null) {
				HashMap<String, Object> datamap = new HashMap<String, Object>();
				datamap.put("id", modelCNBean.getTmModelInfo().getTmCategoryId());
				datamap.put("platformId", CmsConstants.Category.PLATFORM_TM_ID);
				Map<String, Object> ret = categoryDao.getTMJDName(datamap);
				if (ret != null) {
					cnBaseModelInfo.setMainCategoryName(ret.get("name").toString());
				}
			}
		}
		return modelCNBean;
	}

	/**
	 * 取得model的CNPriceSettingInfo
	 */
	@Override
	public ModelPriceSettingBean doGetModelCNPriceSettingInfo(String modelId, String channelId, boolean isExtend) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("modelId", modelId);
		data.put("channelId", channelId);
		ModelPriceSettingBean modelPriceSetting = modelDao.doGetModelCNPriceSettingInfo(data);
		// 对应的PrimaryCategory存在的场合
		if (modelPriceSetting != null && isExtend && modelPriceSetting.getCnIsPrimaryCategory() == 1 && modelPriceSetting.getPrimaryCategoryId() != null
				&& modelPriceSetting.getPrimaryCategoryId() != 0) {
			// 取得PrimaryCategory的信息 把model里面是null的属性用上
			CategoryPriceSettingBean priceSetting = categoryEditService.doGetCategoryCNPriceSettingInfo(modelPriceSetting.getPrimaryCategoryId().toString(),
					channelId);
			CommonUtils.merger(modelPriceSetting, priceSetting);
		}
		return modelPriceSetting;
	}

	@Override
	public boolean doUpdateModelCNPriceSettingInfo(ModelPriceSettingBean modelPriceSettingBean, String modifier) throws Exception {
		ModelPriceSettingBean oldNoExtendBean = doGetModelCNPriceSettingInfo(modelPriceSettingBean.getModelId().toString(),
				modelPriceSettingBean.getChannelId(), false);
		ModelPriceSettingBean oldYesExtendBean = doGetModelCNPriceSettingInfo(modelPriceSettingBean.getModelId().toString(),
				modelPriceSettingBean.getChannelId(), true);

		List<String> tables = CommonUtils.compareBean(modelPriceSettingBean, oldYesExtendBean, oldNoExtendBean);
		// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
		List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
		simpleTransactionCms.openTransaction();
		try {
			if (tables.contains("cms_bt_cn_model_price_setting")) {
				if (notNullTables.contains("cms_bt_cn_model_price_setting")) {
					if (modelDao.doUpdateModelCNPriceSettingInfo(oldNoExtendBean) == 0) {
						modelDao.doInsertModelCNPriceSettingInfo(oldNoExtendBean);
					}
				} else {
					modelDao.doDelModelCNPriceSettingInfo(oldNoExtendBean);
				}
				modelDao.doUpdatePublishStatus(modelPriceSettingBean, "cn", null);

				// 重新计算价格
				// 查看里面是否有需要从父Category继承的
				if (modelPriceSettingBean.isExistNull()) {
					// 重新取一下继承的数据
					modelPriceSettingBean = doGetModelCNPriceSettingInfo(modelPriceSettingBean.getModelId().toString(), modelPriceSettingBean.getChannelId(),
							true);
				}
				doUpdateModelProductPriceSetting(modelPriceSettingBean, modifier);

				modelDao.doInsertModelHistoryPriceSettingInfo(modelPriceSettingBean);

			}
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		simpleTransactionCms.commit();
		return true;

	}

	/**
	 * 更新model下的所有product的价格
	 * 
	 * @param modelPriceSettingBean
	 * @throws Exception
	 */
	@Override
	public void doUpdateModelProductPriceSetting(ModelPriceSettingBean modelPriceSettingBean, String modifier) throws Exception {
		logger.info("更新priceinfo  modelId= " + modelPriceSettingBean.getModelId().toString() + " channelId= " + modelPriceSettingBean.getChannelId());
		// 取得该model下所有product的priceSetting信息
		List<ProductPriceSettingBean> productPriceSettings = modelDao.getModelProductPriceSetting(modelPriceSettingBean.getModelId().toString(),
				modelPriceSettingBean.getChannelId());
		for (ProductPriceSettingBean productPriceSettingBean : productPriceSettings) {
			if (productPriceSettingBean.isExistNull()) {
				CommonUtils.merger(productPriceSettingBean, modelPriceSettingBean);
				commonServiceImpl.doUpdateProductCNPriceInfo(productPriceSettingBean.getProductId(), productPriceSettingBean.getChannelId(),
						productPriceSettingBean, modifier);
			}
		}
	}

	/**
	 * 取得model的所对应的category的US信息
	 */
	@Override
	public List<Map<String, Object>> doGetUSCategoryList(String modelId, String channelId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("modelId", modelId);
		data.put("channelId", channelId);
		// 取出model对应的所有的category列表
		List<Map<String, Object>> relationInfos = modelDao.doGetModelRelation(data);
		// List<CategoryUSBean> USCategoryList = new
		// ArrayList<CategoryUSBean>();
		// for (Map<String, Object> relationInfo : relationInfos) {
		// // 根据categoryID取出category信息
		// CategoryUSBean category =
		// categoryEditService.getUSCategoryInfo(relationInfo.get("primaryCategoryId").toString(),
		// channelId);
		// USCategoryList.add(category);
		// }
		return modelDao.doGetModelRelation(data);
	}

	/**
	 * 取得model的所对应的category的CN信息
	 */
	@Override
	public List<Map<String, Object>> doGetCNCategoryList(String modelId, String channelId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("modelId", modelId);
		data.put("channelId", channelId);
		// // 取出model对应的所有的category列表
		// List<Map<String, Object>> relationInfos =
		// modelDao.doGetModelRelation(data);
		// List<CategoryCNBean> CategoryList = new ArrayList<CategoryCNBean>();
		// for (Map<String, Object> relationInfo : relationInfos) {
		// // 根据categoryID取出category信息
		// CategoryCNBean category =
		// categoryEditService.getCNCategoryInfo(relationInfo.get("primaryCategoryId").toString(),
		// channelId);
		// CategoryList.add(category);
		// }
		return modelDao.doGetModelRelation(data);
	}

	@Override
	public List<ModelProductUSBean> doGetUSProductList(Map<String, Object> data) throws Exception {
		List<ModelProductUSBean> ModelProductUSBean = modelDao.doGetUSProductList(data);
		return ModelProductUSBean;
	}

	@Override
	public int doGetUSProductListCnt(Map<String, Object> data) throws Exception {
		return modelDao.doGetUSProductListCount(data);
	}

	@Override
	public List<ModelProductCNBean> doGetCNProductList(Map<String, Object> data) throws Exception {
		List<ModelProductCNBean> ModelProductCNBean = modelDao.doGetCNProductList(data);
		return ModelProductCNBean;
	}

	@Override
	public int doGetCNProductListCnt(Map<String, Object> data) throws Exception {
		return modelDao.doGetCNProductListCount(data);
	}

	/**
	 * 更新CN Modelinfo
	 */
	@Override
	public boolean doUpdateCNModelInfo(ModelCNBaseBean modelCnBean) throws Exception {
		// 获取更新前的model信息
		ModelCNBaseBean oldNoExtendBean = doGetCNModelInfo(modelCnBean.getModelId().toString(), modelCnBean.getChannelId().toString(), false)
				.getCnBaseModelInfo();
		ModelCNBaseBean oldYesExtendBean = doGetCNModelInfo(modelCnBean.getModelId().toString(), modelCnBean.getChannelId().toString(), true)
				.getCnBaseModelInfo();
		simpleTransactionCms.openTransaction();
		try {
			// 检查这条记录有没有已经被其他用户修改了 比较修改时间
			if (modelDao.doUpdateCnModel(modelCnBean) > 0) {
				List<String> tables = CommonUtils.compareBean(modelCnBean, oldYesExtendBean, oldNoExtendBean);
				// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
				List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
				// 检查需要跟新那几张表
				for (String table : tables) {
					if (table.equals("cms_bt_cn_model_extend")) {
						if (notNullTables.contains(table)) {
							if (modelDao.doUpdateCnModelExtend(oldNoExtendBean) == 0) {
								modelDao.doInsertCnModelExtend(oldNoExtendBean);
							}
						} else {
							modelDao.doDelCnModelExtend(oldNoExtendBean);
						}
					}
				}
				modelDao.doUpdatePublishStatus(modelCnBean, "cn", null);
				simpleTransactionCms.commit();
			} else {
				simpleTransactionCms.rollback();
				return false;
			}
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			System.out.println(e.toString());
			throw e;
		}

		return true;
	}

	@Override
	public boolean doUpdateCNModelTmallInfo(ModelTMBean modelTMBean) throws Exception {
		ModelTMBean oldNoExtendBean = doGetCNModelInfo(modelTMBean.getModelId().toString(), modelTMBean.getChannelId().toString(), false).getTmModelInfo();
		ModelTMBean oldYesExtendBean = doGetCNModelInfo(modelTMBean.getModelId().toString(), modelTMBean.getChannelId().toString(), true).getTmModelInfo();

		List<String> tables = CommonUtils.compareBean(modelTMBean, oldYesExtendBean, oldNoExtendBean);
		// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
		List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
		try {
			simpleTransactionCms.openTransaction();
			if (tables.size() > 0) {
				String table = tables.get(0);
				if (notNullTables.contains(table)) {
					if (modelDao.doUpdateCnTMModelExtend(oldNoExtendBean) == 0) {
						modelDao.doInsertCnTMModelExtend(oldNoExtendBean);
					}
				} else {
					modelDao.doDelCnTMModelExtend(oldNoExtendBean);
				}
				modelDao.doUpdatePublishStatus(modelTMBean, "cn", 1);
			}
			simpleTransactionCms.commit();
		} catch (Exception e) {
			simpleTransactionCms.rollback();

			throw e;
		}
		return true;
	}

	@Override
	public boolean doUpdateCNModelJingDongInfo(ModelJDBean modelJDBean) throws Exception {
		ModelJDBean oldNoExtendBean = doGetCNModelInfo(modelJDBean.getModelId().toString(), modelJDBean.getChannelId().toString(), false).getJdModelInfo();
		ModelJDBean oldYesExtendBean = doGetCNModelInfo(modelJDBean.getModelId().toString(), modelJDBean.getChannelId().toString(), true).getJdModelInfo();

		List<String> tables = CommonUtils.compareBean(modelJDBean, oldYesExtendBean, oldNoExtendBean);
		// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
		List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
		try {
			simpleTransactionCms.openTransaction();
			if (tables.size() > 0) {
				String table = tables.get(0);
				if (notNullTables.contains(table)) {
					if (modelDao.doUpdateCnJDModelExtend(oldNoExtendBean) == 0) {
						modelDao.doInsertCnJDModelExtend(oldNoExtendBean);
					}
				} else {
					modelDao.doDelCnJDModelExtend(oldNoExtendBean);
				}
				modelDao.doUpdatePublishStatus(modelJDBean, "cn", 2);
			}
			simpleTransactionCms.commit();
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return true;
	}

	/**
	 * 更新US Modelinfo
	 */
	@Override
	public boolean doUpdateUSModelInfo(ModelUSBean modelUSBean) throws Exception {
		// 获取更新前的model信息
		ModelUSBean oldNoExtendBean = doGetUSModelInfo(modelUSBean.getModelId().toString(), modelUSBean.getChannelId().toString(), false);
		ModelUSBean oldYesExtendBean = doGetUSModelInfo(modelUSBean.getModelId().toString(), modelUSBean.getChannelId().toString(), true);
		simpleTransactionCms.openTransaction();
		try {
			// 检查这条记录有没有已经被其他用户修改了 比较修改时间
			if (modelDao.doUpdateUsModel(modelUSBean) > 0) {
				List<String> tables = CommonUtils.compareBean(modelUSBean, oldYesExtendBean, oldNoExtendBean);
				// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
				List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
				// 检查需要跟新那几张表
				for (String table : tables) {
					if (table.equals("cms_bt_model_extend")) {
						if (notNullTables.contains(table)) {
							if (modelDao.doUpdateUsModelExtend(oldNoExtendBean) == 0) {
								modelDao.doInsertUsModelExtend(oldNoExtendBean);
							}
						} else {
							modelDao.doDelUsModelExtend(oldNoExtendBean);
						}
					} else if (table.equals("cms_bt_google_model_extend")) {
						if (notNullTables.contains(table)) {
							if (modelDao.doUpdateUsGoogleModelExtend(oldNoExtendBean) == 0) {
								modelDao.doInsertUsGoogleModelExtend(oldNoExtendBean);
							}
						} else {
							modelDao.doDelUsGoogleModelExtend(oldNoExtendBean);
						}
					} else if (table.equals("cms_bt_pricegrabber_model_extend")) {
						if (notNullTables.contains(table)) {
							if (modelDao.doUpdateUsPricegrabberModelExtend(oldNoExtendBean) == 0) {
								modelDao.doInsertUsPricegrabberModelExtend(oldNoExtendBean);
							}
						} else {
							modelDao.doDelUsPricegrabberModelExtend(oldNoExtendBean);
						}
					} else if (table.equals("cms_bt_amazon_model_extend")) {
						if (notNullTables.contains(table)) {
							if (modelDao.doUpdateUsAmazonModelExtend(oldNoExtendBean) == 0) {
								modelDao.doInsertUsAmazonModelExtend(oldNoExtendBean);
							}
						} else {
							modelDao.doDelUsAmazonModelExtend(oldNoExtendBean);
						}
					}
				}
				modelDao.doUpdatePublishStatus(modelUSBean, "us", null);
				simpleTransactionCms.commit();
			} else {
				simpleTransactionCms.rollback();
				return false;
			}
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return true;
	}

	@Override
	public boolean doUpdateUSModelPrimaryCategory(Map<String, Object> data) throws Exception {

		simpleTransactionCms.openTransaction();
		try {
			String categoryId = data.get("categoryId").toString();
			// 先把原先的PrimaryCategory设成0
			data.put("categoryId", null);
			if ("1".equals(data.get("typeId").toString())) {
				data.put("isPrimaryCategory", '0');
			} else {
				data.put("cnIsPrimaryCategory", '0');
			}
			modelDao.doUpdateUSModelPrimaryCategory(data);

			// 再把要修改的category设成1
			data.put("categoryId", categoryId);
			if ("1".equals(data.get("typeId").toString())) {
				data.put("isPrimaryCategory", '1');
			} else {
				data.put("cnIsPrimaryCategory", '1');
			}
			data.put("typeId", null);
			modelDao.doUpdateUSModelPrimaryCategory(data);

			// 更新PublishStatus
			ModelBaseBean model = new ModelBaseBean();
			model.setModelId(Integer.valueOf(data.get("modelId").toString()));
			model.setChannelId(data.get("channelId").toString());
			modelDao.doUpdatePublishStatus(model, "us", null);
			simpleTransactionCms.commit();
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return false;
	}

	@Override
	public int doUpdateRemoveCategoryModel(Map<String, Object> data) throws Exception {
		return modelDao.doUpdateRemoveCategoryModel(data);
	}

	@Override
	public void doUpdateMainCategory(Map<String, Object> data) throws Exception {

		String modelId = (String) data.get("id");
		String channelId = (String) data.get("channelId");
		String mainCategoryId = (String) data.get("mainCategoryId");
		String tmCategoryId = (String) data.get("tmCategoryId");
		String jdCategoryId = (String) data.get("jdCategoryId");

		ModelCNBean modelCNBean = doGetCNModelInfo(modelId, channelId, true);
		modelCNBean.getCnBaseModelInfo().setMainCategoryId(mainCategoryId);
		modelCNBean.getCnBaseModelInfo().setModifier((String) data.get("modifier"));
		modelCNBean.getJdModelInfo().setJdCategoryId(jdCategoryId);
		modelCNBean.getJdModelInfo().setModifier((String) data.get("modifier"));
		modelCNBean.getTmModelInfo().setTmCategoryId(tmCategoryId);
		modelCNBean.getTmModelInfo().setModifier((String) data.get("modifier"));

		doUpdateCNModelInfo(modelCNBean.getCnBaseModelInfo());
		doUpdateCNModelJingDongInfo(modelCNBean.getJdModelInfo());
		doUpdateCNModelTmallInfo(modelCNBean.getTmModelInfo());

	}

	@Override
	public List<Map<String, Object>> doSearchCategoryByModel(Map<String, Object> requestMap) {
		List<CategoryBean> allCategory = searchDao.getAllParentCategory(requestMap.get("channelId").toString());
		// 模糊查询所有的匹配输入的model的category
		List<Map<String, Object>> result = modelDao.doSearchCategoryByModel(requestMap);
		setParentCategoryPath(result, allCategory);
		return result;
	}

	@Override
	public boolean doChangeModel(Map<String, Object> requestMap, UserSessionBean user) {
		StringBuilder productIdString = new StringBuilder();
		List<Map<String, Integer>> productIdArray = (List<Map<String, Integer>>) requestMap.get("productIds");

		productIdString.append("(");
		for (int i = 0; i < productIdArray.size(); i++) {
			if (i == productIdArray.size() - 1) {
				productIdString.append(productIdArray.get(i).get("productId"));
				productIdString.append(")");
			} else {
				productIdString.append(productIdArray.get(i).get("productId")).append(",");
			}
		}
		requestMap.put("productIdString", productIdString.toString());
		setParamBean(requestMap, user);
		simpleTransactionCms.openTransaction();
		boolean sucFlg;
		try {
			sucFlg = modelDao.doDeleteRelation(requestMap);
			if (sucFlg) {
				sucFlg = modelDao.doCreateRelation(requestMap);
			}
			simpleTransactionCms.commit();
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return sucFlg;
	}

	/**
	 * @description 设置操作所需要的所有参数
	 * @param requestMap
	 *            参数requestMap
	 * @param userSessionBean
	 *            sessionBean 获取userName
	 */
	private void setParamBean(Map<String, Object> requestMap, UserSessionBean userSessionBean) {
		// modelId
		String modelId = requestMap.get("modelId").toString();

		// channelId
		String channelId = requestMap.get("channelId").toString();

		// 产品List
		List<Map<String, Integer>> productIdArray = (List<Map<String, Integer>>) requestMap.get("productIds");

		// 获取model对应的所有的category
		List<Map<String, Map<String, Map<String, Object>>>> requestList = (List<Map<String, Map<String, Map<String, Object>>>>) requestMap.get("modelInfo");
		List<Map<String, Object>> categoryIdList;
		categoryIdList = (List<Map<String, Object>>) requestList.get(0).get("parentCategoryPath");

		// 设置建立model和product关系的参数
		List<CategoryProductCNBean> paramModelProductList = new ArrayList<>();
		// 设置建立category和product关系的参数
		List<CategoryProductCNBean> paramCategoryProductList = new ArrayList<>();
		for (Map<String, Integer> productIdMap : productIdArray) {
			CategoryProductCNBean categoryProductCNBean = new CategoryProductCNBean();
			categoryProductCNBean.setProductId(productIdMap.get("productId").toString());
			categoryProductCNBean.setModelId(modelId);
			categoryProductCNBean.setChannelId(channelId);
			categoryProductCNBean.setModifier(userSessionBean.getUserName());
			paramModelProductList.add(categoryProductCNBean);
			for (Map<String, Object> temMap : categoryIdList) {
				CategoryProductCNBean categoryProductCNBean1 = new CategoryProductCNBean();
				String caregoryId = temMap.get("categoryId").toString();
				categoryProductCNBean1.setProductId(productIdMap.get("productId").toString());
				categoryProductCNBean1.setCategoryId(caregoryId);
				categoryProductCNBean1.setChannelId(channelId);
				categoryProductCNBean1.setModifier(userSessionBean.getUserName());
				paramCategoryProductList.add(categoryProductCNBean1);
			}
		}
		requestMap.put("paramModelProductList", paramModelProductList);
		requestMap.put("paramCategoryProductList", paramCategoryProductList);
	}

	/**
	 * @description 找出parentCategoryPath
	 * @param data
	 *            根据model找出的category
	 * @param allCategory
	 *            根据找出的category找到所有的父category
	 */
	private void setParentCategoryPath(List<Map<String, Object>> data, List<CategoryBean> allCategory) {
		Map<String, Object> buffer = new HashMap<String, Object>();
		if (data != null) {
			for (Map<String, Object> item : data) {
				if (item.get("primaryCategoryId") != null) {
					if (buffer.containsKey(item.get("primaryCategoryId").toString())) {
						item.put("parentCategoryPath", buffer.get(item.get("primaryCategoryId").toString()));
					} else {
						List<CategoryBean> temp = getParentPath(allCategory, item.get("primaryCategoryId").toString());
						item.put("parentCategoryPath", temp);
						buffer.put(item.get("primaryCategoryId").toString(), temp);
					}
				}
			}
		}
	}

	private List<CategoryBean> getParentPath(List<CategoryBean> categoryBeans, String categoryId) {
		List<CategoryBean> result = new ArrayList<>();
		CategoryBean temp = null;
		for (CategoryBean categoryBean : categoryBeans) {
			if (categoryBean.getCategoryId() == Integer.parseInt(categoryId)) {
				temp = categoryBean;
				break;
			}
		}
		while (temp != null) {
			result.add(0, temp);
			temp = temp.getParent();
		}
		return result;
	}
	@Override
	public boolean saveCnModelExtend(ModelCNBaseBean modelCnBean) {
		boolean isSuccess = false;
		int updateCount = modelDao.doUpdateCnModelExtend(modelCnBean);
		if (updateCount == 0) {
			int insertCount = modelDao.doInsertCnModelExtend(modelCnBean);
			if (insertCount > 0) {
				isSuccess = true;
			}
		} else {
			isSuccess = true;
		}
		return isSuccess;

	}
}
