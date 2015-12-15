package com.voyageone.bi.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelInfoDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * selectUserShopById
	 * @param userID
	 * @return
	 */
	public List<String> selectChannelDBList() {		
    	String sql = "SELECT db_name FROM vm_channel_db WHERE db_name is not null AND db_name<>'' GROUP BY db_name";
    	
    	SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
    	List<String> ret = new ArrayList<String>();
    	while (rs.next()) {
    		ret.add(rs.getString("db_name"));
    	}
    	return ret;
	}
}
