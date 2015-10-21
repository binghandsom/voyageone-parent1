package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao extends BaseDao {


	public Integer doAdvanceSearchCnt(Map<String ,Object> data){
		return (Integer)(selectOne(Constants.DAO_NAME_SPACE_CMS + "advance_search_count", data));
	}

	public Integer doCategoryUnMappingCnt(String channelId){
		return (Integer)(selectOne(Constants.DAO_NAME_SPACE_CMS + "category_unmapping_count", channelId));
	}
	public Integer doCategoryPropertyUnMappingCnt(String channelId){
		return (Integer)(selectOne(Constants.DAO_NAME_SPACE_CMS + "category_property_unmapping_count", channelId));
	}
}
