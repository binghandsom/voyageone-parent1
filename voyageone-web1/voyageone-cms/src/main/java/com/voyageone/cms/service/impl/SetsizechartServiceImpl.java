package com.voyageone.cms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voyageone.cms.dao.SetsizechartDao;
import com.voyageone.cms.formbean.BindSizeChartBean;
import com.voyageone.cms.formbean.CategoryBean;
import com.voyageone.cms.formbean.ModelCNBaseBean;
import com.voyageone.cms.formbean.SizeChartInfo;
import com.voyageone.cms.formbean.SizeChartModelBean;
import com.voyageone.cms.modelbean.BindSizeChart;
import com.voyageone.cms.modelbean.CnModelPrice;
import com.voyageone.cms.modelbean.SizeChart;
import com.voyageone.cms.modelbean.SizeChartDetail;
import com.voyageone.cms.service.ModelEditService;
import com.voyageone.cms.service.SetsizechartService;
import com.voyageone.common.Constants;
import com.voyageone.core.modelbean.UserSessionBean;

/**
 * 
 * Title: SetsizechartServiceImpl Description:尺码设定
 * 
 * @author: eric
 * @date:2015年9月6日
 *
 */
// @Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class SetsizechartServiceImpl implements SetsizechartService {

	@Autowired
	private SetsizechartDao setsizechartDao;
	@Autowired
	private ModelEditService modelService;

	/**
	 * 获取BindedSizeChartList
	 */
	@Override
	public List<SizeChartInfo> doGetBindedSizeChartList(Map<String, Object> data) {
		return setsizechartDao.doGetBindedSizeChartList(data);
	}

	/**
	 * 获取OtherSizeChartList
	 */
	@Override
	public List<SizeChartInfo> doGetOtherSizeChartList(Map<String, String> data) {

		List<SizeChartInfo> ret = setsizechartDao.doGetOtherSizeChartList(data);
		return ret;
	}

	@Override
	public SizeChartInfo doGetSizeChart(Map<String, Object> data) {

		SizeChartInfo sizeChartInfo = setsizechartDao.doGetSizeChartInfo(data);
		return sizeChartInfo;
	}

	/**
	 * 获取SizeChartDetail
	 */
	@Override
	public SizeChartInfo doGetSizeChartDetailInfo(Map<String, Object> data) {
		SizeChartInfo sizeChartInfo = setsizechartDao.doGetSizeChartDetailInfo(data);
		return sizeChartInfo;
	}

	@Override
	public List<BindSizeChartBean> doGetBindSizeChart(Map<String, Object> data) {
		List<BindSizeChartBean> bindSizeChartList = setsizechartDao.doGetBindSizeChart(data);
		return bindSizeChartList;
	}

	/**
	 * 更新SizeChartAndSizeChartDetail
	 */
	@Transactional("transactionManagerCms")
	@Override
	public String  insertSizeChartAndSizeChartDetail(Map<String, Object> data, UserSessionBean user) {
		boolean isSuccess = false;
		SizeChart sizeChart = new SizeChart();
		sizeChart.setSizeChartName(String.valueOf(data.get("sizeChartName")));
		sizeChart.setChannelId(String.valueOf(data.get("channelId")));
		sizeChart.setSizeChartModelId(Integer.parseInt(String.valueOf(data.get("sizeChartModelId"))));
		sizeChart.setCreater(user.getUserName());
		sizeChart.setModifier(user.getUserName());
		isSuccess = insertSizeChart(sizeChart);
		if (isSuccess) {
			List<Map<String, Object>> sizeList = (List<Map<String, Object>>) data.get("sizeList");
			for (Map<String, Object> map : sizeList) {
				map.put("sizeChartId", sizeChart.getSizeChartId());
				map.put("creater", user.getUserName());
				map.put("modifier", user.getUserName());
			}
			isSuccess = insertSizeChartDetail(sizeList);
		}
		if(isSuccess) {
			return String.valueOf(sizeChart.getSizeChartId());
		} else {
			return null;
		}

	}

	/**
	 * 插入SizeChart
	 */
	@Override
	public boolean insertSizeChart(SizeChart sizeChart) {
		boolean isSuccess = false;
		isSuccess = setsizechartDao.insertSizeChart(sizeChart);
		return isSuccess;
	}

	/**
	 * 插入SizeChartDetail
	 * 
	 * @param sizeChartDetail
	 * @return
	 */
	@Override
	public boolean insertSizeChartDetail(List<Map<String, Object>> sizeChartDetail) {
		boolean isSuccess = false;
		isSuccess = setsizechartDao.insertSizeChartDetail(sizeChartDetail);
		return isSuccess;
	}

	/**
	 * 插入BindSizeChart
	 * 
	 * @param bindSizeChart
	 * @return
	 */
	@Override
	public boolean insertBindSizeChart(BindSizeChart bindSizeChart) {

		boolean isSuccess = false;
		isSuccess = setsizechartDao.insertBindSizeChart(bindSizeChart);
		return isSuccess;

	}

	@Transactional("transactionManagerCms")
	@Override
	public boolean updateSizeChartAndSizeChartDetail(Map<String, Object> data, UserSessionBean user) {
		boolean isSuccess = false;
		SizeChart sizeChart = new SizeChart();
		sizeChart.setSizeChartId(Integer.parseInt((String) data.get("sizeChartId")));
		sizeChart.setSizeChartName(String.valueOf(data.get("sizeChartName")));
		sizeChart.setChannelId(String.valueOf(data.get("channelId")));
		// sizeChart.setSizeChartModelId(Integer.parseInt(String.valueOf(data.get("sizeChartModelId"))));
		// sizeChart.setSizeChartImageUrl(String.valueOf(data.get("sizeChartImageUrl")));
		sizeChart.setModifier(user.getUserName());
		isSuccess = updateSizeChart(sizeChart);
		if (isSuccess) {
			List<Map<String, Object>> sizeList = (List<Map<String, Object>>) data.get("sizeList");
			String sizeChartId = (String) data.get("sizeChartId");
			isSuccess = batchDeleteSizeChartDetail(sizeChartId);
			for (Map<String, Object> map : sizeList) {
				map.put("creater", user.getUserName());
				map.put("modifier", user.getUserName());
			}
			isSuccess = insertSizeChartDetail(sizeList);
		}
		return isSuccess;
	}

	/**
	 * 更新SizeChart
	 * 
	 * @param sizeChart
	 * @return
	 */
	@Override
	public boolean updateSizeChart(SizeChart sizeChart) {
		boolean isSuccess = false;
		isSuccess = setsizechartDao.updateSizeChart(sizeChart);
		return isSuccess;
	}

	/**
	 * 更新sizeChartDetail
	 * 
	 * @param sizeChartDetail
	 * @return
	 */
	@Override
	public boolean updateSizeChartDetail(Map<String, Object> sizeChartDetail, UserSessionBean user) {
		boolean isSuccess = false;
		sizeChartDetail.put("creater", user.getUserName());
		isSuccess = setsizechartDao.updateSizeChartDetail(sizeChartDetail);
		return isSuccess;
	}

	/**
	 * 批量更新sizeChartDetail
	 * 
	 * @param sizeChartDetail
	 * @return
	 */
	@Override
	public boolean batchUpdateSizeChartDetail(List<Map<String, Object>> sizeChartDetail) {
		boolean isSuccess = false;
		isSuccess = setsizechartDao.batchUpdateSizeChartDetail(sizeChartDetail);
		return isSuccess;
	}

	public boolean batchDeleteSizeChartDetail(String data) {
		boolean isSuccess = false;
		isSuccess = setsizechartDao.batchDeleteSizeChartDetail(data);
		return isSuccess;
	}

	/**
	 * 获取SizeChartCount
	 * 
	 * @param channelId
	 * @return
	 */
	@Override
	public int getSizeChartCount(String channelId) {
		int count = setsizechartDao.getSizeChartCount(channelId);
		return count;

	}

	/**
	 * 获取BindSizeChartCount
	 * 
	 * @param channelId
	 * @return
	 */
	@Override
	public int getBindSizeChartCount(String channelId) {
		int count = setsizechartDao.getBindSizeChartCount(channelId);
		return count;

	}

	/**
	 * 更新ModelSize
	 */
	@Transactional("transactionManagerCms")
	@Override
	public boolean updateModelSizeChart(Map<String, Object> data, UserSessionBean user) {
		boolean isSuccess = false;
		ModelCNBaseBean modelCnBean = new ModelCNBaseBean();
		modelCnBean.setModelId(Integer.parseInt(String.valueOf(data.get("modelId"))));
		modelCnBean.setChannelId(String.valueOf(data.get("channelId")));
		modelCnBean.setSizeChartId(Integer.parseInt(String.valueOf(data.get("sizeChartId"))));
		modelCnBean.setModifier(user.getUserName());
		isSuccess = modelService.saveCnModelExtend(modelCnBean);
		List<BindSizeChartBean> ret = setsizechartDao.doGetBindSizeChart(data);
		BindSizeChart bindSizeChart = new BindSizeChart();
		bindSizeChart.setBrandId(String.valueOf(data.get("brandId")));
		bindSizeChart.setChannelId(String.valueOf(data.get("channelId")));
		bindSizeChart.setProductTypeId(String.valueOf(data.get("productTypeId")));
		bindSizeChart.setSizeTypeId(String.valueOf(data.get("sizeTypeId")));
		bindSizeChart.setSizeChartId(String.valueOf(data.get("sizeChartId")));
		bindSizeChart.setCreater(user.getUserName());
		bindSizeChart.setModifier(user.getUserName());
		if (ret == null || (ret != null && ret.size() == 0)) {

			if (isSuccess) {
				isSuccess = insertBindSizeChart(bindSizeChart);
			}
		} else {
			BindSizeChartBean selectBean = ret.get(0);
			if (!(selectBean.getBrandId().equals(String.valueOf(data.get("brandId")))
					&& selectBean.getSizeChartId().equals(String.valueOf(data.get("sizeChartId")))
					&& selectBean.getProductTypeId().equals(String.valueOf(data.get("productTypeId")))
					&& selectBean.getSizeTypeId().equals(String.valueOf(data.get("sizeTypeId"))) && selectBean.getChannelId().equals(
					String.valueOf(data.get("channelId"))))) {
				if (isSuccess) {
					isSuccess = insertBindSizeChart(bindSizeChart);
				}
			}

		}
		return isSuccess;

	}
	/**
	 * 获取SizeChartModelList
	 * @param channelId
	 * @return
	 */
	@Override
	public List<SizeChartModelBean> getSizeChartModelBean (String channelId) {
		return setsizechartDao.getSizeChartModelList(channelId);	
	}
}
