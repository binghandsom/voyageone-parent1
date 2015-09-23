package com.voyageone.bi.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.voyageone.bi.commonutils.DaoUtils;
import com.voyageone.bi.dao.cache.CacheContext;
import com.voyageone.bi.disbean.PageCmbBoxDisBean;
import com.voyageone.bi.tranbean.UserInfoBean;

@Repository
public class CategoryInfoDao {
//	private static Log logger = LogFactory.getLog(BrandInfoDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<PageCmbBoxDisBean> selectAllCateogry(UserInfoBean userInfoBean)  {	
		String dbName = DaoUtils.getFirstDbName(userInfoBean);
		String sql = "SELECT  id,  level, is_parent,  name FROM #dbName#vm_category WHERE enable=1 Order by level,id";
		sql = sql.replace("#dbName#", dbName);
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
			disBean.setCode(rs.getString("id")+"-"+rs.getString("level")+"-"+rs.getString("is_parent"));
			disBean.setName(rs.getString("name"));
			result.add(disBean);
		}
		
		CacheContext.getInstance().addOrUpdateCache(sql, result);
    	return result;
	}
}
