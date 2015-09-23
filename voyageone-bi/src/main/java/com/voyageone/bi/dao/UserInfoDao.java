package com.voyageone.bi.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.voyageone.bi.bean.UserMenuBean;
import com.voyageone.bi.commonutils.MD5Util;
import com.voyageone.bi.disbean.MemberLoginDisBean;
import com.voyageone.bi.tranbean.UserChannelBean;
import com.voyageone.bi.tranbean.UserInfoBean;
import com.voyageone.bi.tranbean.UserShopBean;

@Repository
public class UserInfoDao {
	private static Log logger = LogFactory.getLog(UserInfoDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public UserInfoBean selectUserById(MemberLoginDisBean bean) throws Exception {		
		return selectUserById(bean.getUsername(), bean.getPassword());
	}
	
	public UserInfoBean selectUserById(String uid, String pwd) throws Exception {		
		String sql = "SELECT  uid,  email,  nick,  company,  phone FROM vm_user WHERE uid=? and pwd=? and enable=1";
		Object[] param = { uid, MD5Util.MD5(pwd) };
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, param);

		final UserInfoBean ret = new UserInfoBean();
		if (rs.next()) {
			ret.setUid(rs.getString("uid"));
			ret.setEmail(rs.getString("email"));
			// ret.setn(rs.getString("nick"));
			ret.setCompany(rs.getString("company"));
			ret.setPhone(rs.getString("phone"));
    	}
    	return ret;
	}

	
	// 密码更新更新
	public int updateUserPassword(String uid, String pwd){
		
		String sql = "UPDATE vm_user SET pwd=? WHERE uid=?";
		Object[] param = { MD5Util.MD5(pwd), uid };
		int result = jdbcTemplate.update(sql, param);
	
		if(result >= 1){
			logger.info("UpdateShipment Success.");
		}else{
			logger.error("UpdateShipment Failed.");
		}
		return result;
	}
	
	/**
	 * selectUserShopById
	 * @param userID
	 * @return
	 */
	public List<UserShopBean> selectUserShopById(String userID) {		
    	String sql = "SELECT s.id, s.code, "
    			+ "s.name, s.name_en, s.name_cn, s.channel_id, s.ecomm_id "
    			+ "FROM vm_user_shop us "
    			+ "inner join vm_user u on us.user_id=u.id and u.enable=1 "
    			+ "inner join vm_shop s on us.shop_id=s.id and s.enable=1 "
    			+ "where u.uid = ?";
    	
		Object[] param ={userID};
    	SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, param);
    	
    	List<UserShopBean> ret = new ArrayList<UserShopBean>();
    	
    	while (rs.next()) {
    		UserShopBean bean = new UserShopBean();
    		bean.setId(rs.getInt("id"));
    		bean.setCode(rs.getString("code"));
    		bean.setName(rs.getString("name_cn"));
    		bean.setName_en(rs.getString("name_en"));
    		bean.setChannel_id(rs.getString("channel_id"));
    		bean.setEcomm_id(rs.getString("ecomm_id"));
    		ret.add(bean);
    	}
    	
    	return ret;
	}

	/**
	 * selectUserShopById
	 * @param userID
	 * @return
	 */
	public List<UserChannelBean> selectUserChannelById(String userID) {
		String sql = "SELECT s.id, s.code, "
				+ "s.name, s.name_en, s.name_cn"
				+ " FROM vm_user_shop us "
				+ "inner join vm_user u on us.user_id=u.id and u.enable=1 "
				+ "INNER JOIN vm_channel s ON us.channel_id = s.id and s.enable=1 "
				+ "where u.uid = ?"
				+ "GROUP BY s.id";

		Object[] param ={userID};
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, param);

		List<UserChannelBean> ret = new ArrayList<UserChannelBean>();

		while (rs.next()) {
			UserChannelBean bean = new UserChannelBean();
			bean.setId(rs.getInt("id"));
			bean.setCode(rs.getString("code"));
			bean.setName(rs.getString("name"));
			bean.setName_en(rs.getString("name_en"));
			ret.add(bean);
		}

		return ret;
	}
	
	
	/**
	 * selectUserShopById
	 * @param userID
	 * @return
	 */
	public List<UserShopBean> selectUserShopIdById(String userID) {		
    	String sql = "SELECT s.id, s.code "
    			+ "FROM vm_user_shop us "
    			+ "inner join vm_user u on us.user_id=u.id and u.enable=1 "
    			+ "inner join vm_shop s on us.shop_id=s.id and s.enable=1 "
    			+ "where u.uid = ?";
    	
		Object[] param ={userID};
    	SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, param);
    	
    	List<UserShopBean> ret = new ArrayList<UserShopBean>();
    	
    	while (rs.next()) {
    		UserShopBean bean = new UserShopBean();
    		bean.setId(rs.getInt("code"));
    		bean.setCode(rs.getString("code"));
//    		bean.setName(rs.getString("name"));
//    		bean.setName_en(rs.getString("name_en"));
//    		bean.setChannel_id(rs.getString("channel_id"));
//    		bean.setEcomm_id(rs.getString("ecomm_id"));
    		ret.add(bean);
    	}
    	
    	return ret;
	}
	
	/**
	 * selectUserShopById
	 * @param userID
	 * @return
	 */
	public List<String> selectUserChannelDBByUserId(String userID) {		
    	String sql = "SELECT distinct channel_db.channel_id channel_id,channel_db.db_name dbname "
    			+ "FROM vm_user_shop us "
    			+ "inner join vm_user u on us.user_id=u.id and u.enable=1 "
    			+ "inner join vm_shop s on us.shop_id=s.id and s.enable=1 "
    			+ "inner join vm_channel_db channel_db on channel_db.channel_id=s.channel_id "
    			+ "where u.uid = ? ";
    	
		Object[] param ={userID};
    	SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, param);
    	
    	List<String> ret = new ArrayList<String>();
    	
    	while (rs.next()) {
    		ret.add(rs.getString("dbname"));
    	}
    	return ret;
	}
	
	/**
	 * selectUserShopById
	 * @param userID
	 * @return
	 */
	public Map<String, List<UserMenuBean>> selectUserMenuByUserId(String userID) {		
    	String sql = "SELECT "
    			+ "menu.code code, "
				+ "menu.name name, "
				+ "menu.pname pname, "
				+ "menu.link link, "
				+ "u_menu.report_size1 report_size1, "
				+ "u_menu.report_size2 report_size2, "
		    	+ "u_menu.report_size3 report_size3, "
		    	+ "u_menu.report_size4 report_size4 "
				+ "FROM vm_user usr "
				+ "INNER JOIN vm_user_menu u_menu ON usr.id=u_menu.user_id "
				+ "INNER JOIN vm_menu menu ON menu.id=u_menu.menu_id AND menu.enable=1 "
				+ "WHERE usr.uid = ? "
				+ "ORDER BY code ";
    	
		Object[] param ={userID};
    	SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, param);
    	
    	Map<String, List<UserMenuBean>> ret = new LinkedHashMap<String, List<UserMenuBean>>();
    	while (rs.next()) {
    		UserMenuBean bean = new UserMenuBean();
    		bean.setCode(rs.getString("code"));
    		bean.setName(rs.getString("name"));
    		bean.setPname(rs.getString("pname"));
    		bean.setLink(rs.getString("link"));
    		String reportSize = rs.getString("report_size1") ;
    		reportSize = reportSize + "," + rs.getString("report_size2") ;
    		reportSize = reportSize + "," + rs.getString("report_size3") ;
    		reportSize = reportSize + "," + rs.getString("report_size4") ;
    		bean.setReportSize(reportSize);
    		if (!ret.containsKey(bean.getPname())) {
    			ret.put(bean.getPname(), new ArrayList<UserMenuBean>());
    		}
    		List<UserMenuBean> menuList = ret.get(bean.getPname());
    		menuList.add(bean);
    		
    	}
    	
    	return ret;
	}
	
	

}
