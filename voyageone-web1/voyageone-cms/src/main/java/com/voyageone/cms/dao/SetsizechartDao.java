package com.voyageone.cms.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.cms.formbean.BindSizeChartBean;
import com.voyageone.cms.formbean.SizeChartInfo;
import com.voyageone.cms.formbean.SizeChartModelBean;
import com.voyageone.cms.modelbean.BindSizeChart;
import com.voyageone.cms.modelbean.SizeChart;
import com.voyageone.cms.modelbean.SizeChartDetail;
import com.voyageone.common.Constants;

@Repository
public class SetsizechartDao extends CmsBaseDao {
	/**
	 * 获取BindedSizeChartList
	 * 
	 * @param data
	 * @return
	 */
	public List<SizeChartInfo> doGetBindedSizeChartList(Map<String, Object> data) {
		List<SizeChartInfo> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "get_binded_size_chartlist", data);
		if (info == null || info.size() == 0) {
			info = new ArrayList<SizeChartInfo>();
		}
		return info;
	}

	/**
	 * 获取OtherSizeChartList
	 * 
	 * @param data
	 * @return
	 */
	public List<SizeChartInfo> doGetOtherSizeChartList(Map<String, String> data) {
		List<SizeChartInfo> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "get_other_size_chart_list", data);
		if (info == null || info.size() == 0) {
			info = new ArrayList<SizeChartInfo>();
		}
		return info;
	}

	/**
	 * 获取SizeChartInfo
	 * 
	 * @param data
	 * @return
	 */
	public SizeChartInfo doGetSizeChartInfo(Map<String, Object> data) {
		SizeChartInfo info = selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_SizeChartInfo", data);
		return info;

	}

	/**
	 * 获取SizeChartDetailInfo
	 * 
	 * @param data
	 * @return
	 */
	public SizeChartInfo doGetSizeChartDetailInfo(Map<String, Object> data) {
		SizeChartInfo info = selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_SizeChartDetailInfo", data);
		return info;
	}

	/**
	 * 获取BindSizeChart信息
	 * 
	 * @param data
	 * @return
	 */
	public List<BindSizeChartBean> doGetBindSizeChart(Map<String, Object> data) {

		List<BindSizeChartBean> info = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_SizeChartBindList", data);
		return info;

	}

	/**
	 * 插入SizeChart
	 * 
	 * @param sizeChart
	 * @return
	 */
	public boolean insertSizeChart(SizeChart sizeChart) {

		boolean isSuccess = false;
		int count = insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_SizeChart", sizeChart);
		if (count > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 批量插入SizeChartDetail
	 * 
	 * @param sizeChartDetail
	 * @return
	 */
	public boolean insertSizeChartDetail(List<Map<String,Object>> sizeChartDetail) {

		boolean isSuccess = false;
		int count = insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_SizeChartDetail", sizeChartDetail);
		if (count > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 插入BindSizeChart
	 * 
	 * @param bindSizeChart
	 * @return
	 */
	public boolean insertBindSizeChart(BindSizeChart bindSizeChart) {
		boolean isSuccess = false;
		int count = insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_BindSizeChart", bindSizeChart);
		if (count > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 更新SizeChart
	 * 
	 * @param sizeChart
	 * @return
	 */
	public boolean updateSizeChart(SizeChart sizeChart) {
		boolean isSuccess = false;
		int count = update(Constants.DAO_NAME_SPACE_CMS + "cms_update_SizeChart", sizeChart);
		if (count > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 更新SizeChartDetail
	 * 
	 * @param sizeChartDetail
	 * @return
	 */
	public boolean updateSizeChartDetail(Map<String,Object> sizeChartDetail) {
		boolean isSuccess = false;
		int count = update(Constants.DAO_NAME_SPACE_CMS + "cms_update_SizeChartDetail", sizeChartDetail);
		if (count > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 批量更新SizeChartDetail
	 * 
	 * @param sizeChartDetail
	 * @return
	 */
	public boolean batchUpdateSizeChartDetail(List<Map<String, Object>> sizeChartDetail) {
		boolean isSuccess = false;
		int count = update(Constants.DAO_NAME_SPACE_CMS + "cms_batchUpdate_SizeChartDetail", sizeChartDetail);
		if (count > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}
	/**
	 * 批量删除SizeChartDetail
	 * @param data
	 * @return
	 */
	public boolean batchDeleteSizeChartDetail(String data) {
		boolean isSuccess = false;
		int count = delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_SizeChartDetail", data);
		if (count > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}
	/**
	 * 获取SizeChart条数
	 * @param channelId
	 * @return
	 */
	public int getSizeChartCount(String channelId) {
		int count = selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_SizeChartCount", channelId);
		return count ;
	}
	/**
	 * 获取BindSizeChart条数
	 * @param channelId
	 * @return
	 */
	public int getBindSizeChartCount(String channelId) {
		int count = selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_BindSizeChartCount", channelId);
		return count ;
	}
	/**
	 * 获取SizeChartModelList
	 * @param channelId
	 * @return
	 */
	public List<SizeChartModelBean> getSizeChartModelList (String channelId) {
		List<SizeChartModelBean> ret = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_SizeChartModel", channelId);
		return ret;
	}
}
