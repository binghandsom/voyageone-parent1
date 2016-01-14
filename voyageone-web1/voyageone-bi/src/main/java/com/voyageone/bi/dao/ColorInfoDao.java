package com.voyageone.bi.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.voyageone.bi.commonutils.DaoUtils;
import com.voyageone.bi.disbean.PageCmbBoxDisBean;
import com.voyageone.bi.tranbean.UserInfoBean;

@Repository
public class ColorInfoDao {
//	private static Log logger = LogFactory.getLog(BrandInfoDao.class);
	
	@Autowired
	private DaoQueryCache daoQueryCache;
	
	public List<PageCmbBoxDisBean> selectAllColor(UserInfoBean userInfoBean)  {
		String dbName = DaoUtils.getFirstDbName(userInfoBean);
		String sql = "SELECT  id,  name FROM #dbName#vm_color_define WHERE enable=1 Order by name";
		sql = sql.replace("#dbName#", dbName);
		
		List<PageCmbBoxDisBean> result = daoQueryCache.queryCache(sql);
    	return result;
	}

}
