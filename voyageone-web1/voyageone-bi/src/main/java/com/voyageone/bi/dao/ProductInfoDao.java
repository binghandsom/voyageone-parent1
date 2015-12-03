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
public class ProductInfoDao {
//	private static Log logger = LogFactory.getLog(BrandInfoDao.class);
	
	public static String cacheKey = "AllProductIDAndCode";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<PageCmbBoxDisBean> selectAllProductIDAndCode(String dbName)  {		
		String sql = "SELECT  id, code FROM  #dbName#.vm_product WHERE enable=1 Order by code";
		sql = sql.replace("#dbName#", dbName);
		
    	String key = cacheKey;
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
			disBean.setName(rs.getString("code"));
			result.add(disBean);
		}
		
		CacheContext.getInstance().addOrUpdateCache(sql, result);
    	return result;
	}
	
	public List<PageCmbBoxDisBean> selectProductIDAndCodeByQuery(String query, int topCount, UserInfoBean userInfoBean)  {
		List<PageCmbBoxDisBean> result = new ArrayList<PageCmbBoxDisBean>();
		if (query != null && query.length()>0) {
			String querystr= query.trim();
			String dbName = DaoUtils.getFirstDbName(userInfoBean);
			List<PageCmbBoxDisBean> allProduct = selectAllProductIDAndCode(dbName);
			for (PageCmbBoxDisBean bean:allProduct) {
				String name = bean.getName().toLowerCase();
				if (name.startsWith(querystr.toLowerCase())) {
					result.add(bean);
					if (result.size()>=topCount) {
						break;
					}
				}
			}
		}
		return result;
	}

}
