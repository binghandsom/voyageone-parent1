package com.voyageone.cms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;

@Repository
public class PopUpMtPropSetValueDao extends BaseDao{
	
	public List<String> getDictContents(String channelId){
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channel_id", channelId);

		return super.selectList("get_dict_contents", parmMap);
	}

}
