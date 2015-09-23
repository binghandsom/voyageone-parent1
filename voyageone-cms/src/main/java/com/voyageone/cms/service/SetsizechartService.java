package com.voyageone.cms.service;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.voyageone.cms.formbean.BindSizeChartBean;
import com.voyageone.cms.formbean.SizeChartInfo;
import com.voyageone.cms.formbean.SizeChartModelBean;
import com.voyageone.cms.modelbean.BindSizeChart;
import com.voyageone.cms.modelbean.SizeChart;
import com.voyageone.cms.modelbean.SizeChartDetail;
import com.voyageone.common.Constants;
import com.voyageone.core.modelbean.UserSessionBean;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public interface SetsizechartService {

	public List<SizeChartInfo> doGetBindedSizeChartList(Map<String,Object> data);

	public List<SizeChartInfo> doGetOtherSizeChartList(Map<String,String> data);

	public boolean insertSizeChart(SizeChart sizeChart);

	public boolean insertBindSizeChart(BindSizeChart bindSizeChart);

	public SizeChartInfo doGetSizeChartDetailInfo(Map<String, Object> data);

	public List<BindSizeChartBean> doGetBindSizeChart(Map<String, Object> data);

	public SizeChartInfo doGetSizeChart(Map<String, Object> data);

	public boolean updateSizeChart(SizeChart sizeChart);

//	public boolean updateSizeChartDetail(SizeChartDetail sizeChartDetail);

	public boolean updateSizeChartAndSizeChartDetail(Map<String, Object> data, UserSessionBean user);

	public boolean batchUpdateSizeChartDetail(List<Map<String, Object>> sizeChartDetail);

	public boolean updateSizeChartDetail(Map<String, Object> sizeChartDetail,UserSessionBean user);

	public boolean insertSizeChartDetail(List<Map<String, Object>> sizeChartDetail);

	public String insertSizeChartAndSizeChartDetail(Map<String, Object> data, UserSessionBean user);

	public int getSizeChartCount(String channelId);

	public int getBindSizeChartCount(String channelId);

	public boolean updateModelSizeChart(Map<String, Object> data, UserSessionBean user);

	List<SizeChartModelBean> getSizeChartModelBean(String channelId);

		
}
