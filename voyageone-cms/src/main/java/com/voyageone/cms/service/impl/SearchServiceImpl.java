package com.voyageone.cms.service.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.omg.CORBA.portable.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.dao.SearchDao;
import com.voyageone.cms.formbean.CategoryBean;
import com.voyageone.cms.service.SearchService;
import com.voyageone.cms.utils.CSVUtils;
import com.voyageone.cms.utils.CommonUtils;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.StringUtils;


@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchDao searchDao;

	/**
	 * 检索
	 */
	@Override
	public List<Map<String, Object>> doSearch(Map<String, Object> data) {

		List<CategoryBean> allCategory = searchDao.getAllParentCategory(data.get("channelId").toString());
		List<Map<String, Object>> result = searchDao.doSearch(data);
		int type = (Integer) data.get("type");
		if (type == CmsConstants.IDType.TYPE_CATEGORY || type == CmsConstants.IDType.TYPE_ALL) {
			// 找出parentCategoryPath
			setParentCategoryPath(result, allCategory);
		}

		if (type == CmsConstants.IDType.TYPE_MODEL || type == CmsConstants.IDType.TYPE_ALL) {
			setParentCategoryPath(result, allCategory);
		}
		if (type == CmsConstants.IDType.TYPE_PRODUCT || type == CmsConstants.IDType.TYPE_ALL) {
			setParentCategoryPath(result, allCategory);
		}
		return result;

	}

	@Override
	public int doSearchCount(Map<String, Object> data) {

		return searchDao.doSearchCnt(data);

	}

	@Override
	public List<Map<String, Object>> doAdvanceSearch(Map<String, Object> data) {
		List<Map<String, Object>> ret;
		ret = searchDao.doAdvanceSearch(data);
		if (ret == null) {
			return new ArrayList<Map<String, Object>>();
		}
		return ret;
	}

	@Override
	public int doAdvanceSearchCount(Map<String, Object> data) {

		return searchDao.doAdvanceSearchCnt(data);

	}

	/**
	 * 找出parentCategoryPath
	 * 
	 * @param data
	 * @param allCategory
	 * @param buffer
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

	public List<CategoryBean> getParentPath(List<CategoryBean> categoryBeans, String categoryId) {

		List<CategoryBean> result = new ArrayList<CategoryBean>();
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
	public File doExport(Map<String, Object> data, String outPutPath, String name) {
		boolean isSuccess = false;
		//获取列表
		List<Object> head = getColumn();
		List<Map<String, Object>> ret = searchDao.doAdvanceSearch(data);
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for (Map<String, Object> obj : ret) {
			List<Object> list = new ArrayList<Object>();
			for (Object column : getColumn()) {
				list.add(obj.get(column));
			}
			dataList.add(list);
		}
		File result = CSVUtils.createCSVFile(head, dataList, outPutPath, name);
		// List<Map<String, Object>> ret =doAdvanceSearch (data);
		return result;

	}

	@Override
	public byte[] doExport(Map<String, Object> data) throws IOException {
		boolean isSuccess = false;
		StringBuffer stringBuffer=new StringBuffer();
		//获取列表
		List<Object> head = getColumn();
		stringBuffer.append(CSVUtils.writeRow(head)).append("\n");
		List<Map<String, Object>> ret = searchDao.doAdvanceSearch(data);
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for (Map<String, Object> obj : ret) {
			List<Object> list = new ArrayList<Object>();
			for (Object column : getColumn()) {
				list.add(obj.get(column));
			}
			stringBuffer.append(CSVUtils.writeRow(list)).append("\n");
		}
		byte[] bom = {(byte)0xef,(byte)0xbb,(byte)0xbf};

		return ArrayUtils.addAll(bom, stringBuffer.toString().getBytes("UTF-8"));

	}
/**
 * 取展示的列,等选择列完成可以从表里面取
 * @return
 */
	public List<Object> getColumn() {
		List<Object> column = new ArrayList<Object>();
		column.add("code");
		column.add("cnName");
		column.add("quantity");
		column.add("cnDisplayOrder");
		column.add("productType");
		column.add("brand");
		column.add("cnSizeType");
		column.add("isApprovedDescription");
		column.add("isEffective");
		column.add("colorMap");
		column.add("cnColor");
		column.add("madeInCountry");
		column.add("materialFabric1");
		column.add("materialFabric2");
		column.add("materialFabric3");
		column.add("cnLongDescription");
		column.add("cnShortDescription");
		column.add("urlKey");
		column.add("created");
		column.add("modified");
		column.add("referenceMsrp");
		column.add("referencePrice");
		column.add("firstSalePrice");
		column.add("cnPrice");
		column.add("effectivePrice");
		column.add("cnPriceRmb");
		column.add("cnPriceAdjustmentRmb");
		column.add("cnPriceFinalRmb");
		column.add("cnIsOnSale");
		column.add("cnIsApproved");
		column.add("cnPublishStatus");
		column.add("cnNumIid");
		column.add("cnPublishFaildComment");
		column.add("cnOfficialPriceAdjustmentRmb");
		column.add("cnOfficialPriceFinalRmb");
		column.add("cnOfficialFreeShippingType");
		column.add("cnOfficialIsOnSale");
		column.add("cnOfficialIsApproved");
		column.add("cnOfficialPrePublishDateTime");
		column.add("cnOfficialPublishStatus");
		column.add("cnOfficialNumIid");
		column.add("cnOfficialPublishFaildComment");
		column.add("tbPriceAdjustmentRmb");
		column.add("tbPriceFinalRmb");
		column.add("tbFreeShippingType");
		column.add("tbIsOnSale");
		column.add("tbIsApproved");
		column.add("tbPrePublishDateTime");
		column.add("tbPublishStatus");
		column.add("tbNumIid");
		column.add("tbPublishFaildComment");
		column.add("tmPriceAdjustmentRmb");
		column.add("tmPriceFinalRmb");
		column.add("tmFreeShippingType");
		column.add("tmIsOnSale");
		column.add("tmIsApproved");
		column.add("tmPrePublishDateTime");
		column.add("tmPublishStatus");
		column.add("tmNumIid");
		column.add("tmPublishFaildComment");
		column.add("tgPriceAdjustmentRmb");
		column.add("tgPriceFinalRmb");
		column.add("tgFreeShippingType");
		column.add("tgIsOnSale");
		column.add("tgIsApproved");
		column.add("tgPrePublishDateTime");
		column.add("tgPublishStatus");
		column.add("tgNumIid");
		column.add("tgPublishFaildComment");
		column.add("jdPriceAdjustmentRmb");
		column.add("jdPriceFinalRmb");
		column.add("jdFreeShippingType");
		column.add("jdIsApproved");
		column.add("jdPrePublishDateTime");
		column.add("jdPublishStatus");
		column.add("jdNumIid");
		column.add("jdPublishFaildComment");
		column.add("jgPriceAdjustmentRmb");
		return column;

	}

}
