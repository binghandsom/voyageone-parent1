package com.voyageone.bi.dao.sqlolap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;

import com.voyageone.bi.commonutils.SpringBeanFactoryUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;

@Repository
public class SqlOlapDaoSupport {
	//private static Log logger = LogFactory.getLog(SqlOlapDaoSupport.class);
	

	private JdbcTemplate jdbcTemplate_Stores = null;
	
	private JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate_Stores == null) {
			jdbcTemplate_Stores = (JdbcTemplate)SpringBeanFactoryUtils.getBeanByName("jdbcTemplate_Stores");
		}
		return jdbcTemplate_Stores;
	}
	
	/**
	 * queryWithBean
	 * @param queryStr
	 * @return
	 */
	public ChartGridDisBean queryWithBean(String queryStr) {
		ChartGridDisBean result = new ChartGridDisBean();
		try {
			JdbcTemplate jdbcTemplate = getJdbcTemplate();
			SqlRowSet rs = jdbcTemplate.queryForRowSet(queryStr);
			SqlRowSetMetaData metaData = rs.getMetaData();
			if (rs.next()) {
				for (int i=0; i<metaData.getColumnCount(); i++) {
					String columnName = metaData.getColumnLabel(i);
					SqlOlapSalesUtils.setColumnValue(columnName, rs, result);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}
	
	/**
	 * queryWithBeanList
	 * @param queryStr
	 * @return
	 */
	public List<ChartGridDisBean> queryWithBeanList(String queryStr) {
		List<ChartGridDisBean> result = null;
		try {
			JdbcTemplate jdbcTemplate = getJdbcTemplate();
			SqlRowSet rs = jdbcTemplate.queryForRowSet(queryStr);
			result = getDisBeanList(rs);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}
	
	/**
	 * getDisBeanList
	 * @param rs
	 * @return
	 */
	private List<ChartGridDisBean> getDisBeanList(SqlRowSet rs) {
		List<ChartGridDisBean> result = new ArrayList<ChartGridDisBean>();
		SqlRowSetMetaData metaData = rs.getMetaData();
		while(rs.next()) {
			ChartGridDisBean chartGridDisBean = new ChartGridDisBean();
			for (int i=0; i<metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnLabel(i+1);
				SqlOlapSalesUtils.setColumnValue(columnName, rs, chartGridDisBean);
			}
			result.add(chartGridDisBean);
		}
		return result;
	}

}
