package com.voyageone.bi.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.voyageone.bi.dao.cache.CacheContext;
import com.voyageone.bi.disbean.PageCmbBoxDisBean;

@Repository
public class DaoQueryCache {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<PageCmbBoxDisBean> queryCache (String sql) {
		String key = sql;
		@SuppressWarnings("unchecked")
		List<PageCmbBoxDisBean> result= (List<PageCmbBoxDisBean>) CacheContext.getInstance().get(key);
        if (result != null) {
            return result;
        }
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		result = new ArrayList<PageCmbBoxDisBean>();
		while (rs.next()) {
			PageCmbBoxDisBean disBean = new PageCmbBoxDisBean();
			disBean.setCode(rs.getString("id"));
			disBean.setName(rs.getString("name"));
			result.add(disBean);
		}
		
		CacheContext.getInstance().addOrUpdateCache(sql, result);
		return result;
	}
}
