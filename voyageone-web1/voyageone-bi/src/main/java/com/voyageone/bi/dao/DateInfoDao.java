package com.voyageone.bi.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.voyageone.bi.bean.DateBean;
import com.voyageone.bi.disbean.ConditionBean.DateType;

@Repository
public class DateInfoDao {
	private static Log logger = LogFactory.getLog(DateInfoDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//cache
	private Map<String, List<DateBean>> dataCache = new HashMap<String, List<DateBean>>();
	
	/**
	 * cacheALLDate
	 */
	public void cacheALLDate() {		
		String sql = "SELECT id,code,type,year,month,month_id,month_code,day FROM vm_date v WHERE enable=1 ORDER BY id;";
		logger.info("cacheALLDate sql:" + sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);

		List<DateBean> dayList = new ArrayList<DateBean>(); 
		Set<String> daySet = new HashSet<String>(); 
		List<DateBean> monthList  = new ArrayList<DateBean>(); 
		Set<String> monthSet = new HashSet<String>(); 
		List<DateBean> yearList  = new ArrayList<DateBean>(); 
		Set<String> yearSet = new HashSet<String>(); 
		while (rs.next()) {
			DateBean bean = new DateBean();
			bean.id = rs.getInt("id");
			bean.code = rs.getString("code");
			bean.type = DateType.Day;
			bean.year = rs.getInt("year");
			bean.month = rs.getInt("month");
			bean.month_id = rs.getInt("month_id");
			bean.month_code = rs.getString("month_code");
			bean.day = rs.getInt("day");
			
			// day
			String strId = String.valueOf(bean.id);
			if (!daySet.contains(strId)) {
				daySet.add(strId);
				dayList.add(bean);
			}
			
			//month
			String strMonthId = String.valueOf(bean.month_id);
			if (!monthSet.contains(strMonthId)) {
				monthSet.add(strMonthId);
				bean = new DateBean();
				bean.id = rs.getInt("month_id");
				bean.code = rs.getString("month_code");
				bean.type = DateType.Month;
				bean.year = rs.getInt("year");
				bean.month = rs.getInt("month");
				bean.month_id = rs.getInt("month_id");
				bean.month_code = rs.getString("month_code");
				monthList.add(bean);
			}
			
			//Year
			String strYear = String.valueOf(bean.year);
			if (!yearSet.contains(strYear)) {
				yearSet.add(strYear);
				bean = new DateBean();
				bean.id = rs.getInt("year");
				bean.code = rs.getString("year");
				bean.type = DateType.Year;
				bean.year = rs.getInt("year");
				yearList.add(bean);
			}
			
			dataCache.put(DateType.Day.toString(), dayList);
			dataCache.put(DateType.Month.toString(), monthList);
			dataCache.put(DateType.Year.toString(), yearList);
    	}
	}
	
	/**
	 * getDataList
	 * @param dateType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DateBean> getDataList(DateType dateType, int startDate, int endDate) {
		List<DateBean> result = new ArrayList<DateBean>();
		List<DateBean> dataList = dataCache.get(dateType.toString());
		if (dataList == null) {
			cacheALLDate();
			dataList = dataCache.get(dateType.toString());
		}
		for (DateBean bean : dataList) {
			if (startDate<=bean.id && endDate>=bean.id ) {
				result.add(bean);
			}
		}
		return result;
	}
}
