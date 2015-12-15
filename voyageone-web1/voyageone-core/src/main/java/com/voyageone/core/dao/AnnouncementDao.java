package com.voyageone.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.core.CoreConstants;

@Repository
public class AnnouncementDao extends BaseDao {
	
	/**
	 * 获得公共公告
	 * 
	 * @param lang
	 * @param startTime
	 * @return
	 */
	public List<Map<String, String>> getPublicAnnouncements(String lang, String startTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("lang", lang);
		params.put("companyId", CoreConstants.ANNOUNCEMENT_PUBLIC);
		params.put("created", startTime);
		
		List<Map<String, String>> announcementList = selectList(Constants.DAO_NAME_SPACE_CORE + "ct_announcement_getAnnouncements", params);
		
		if (announcementList == null || announcementList.size() == 0) {
			announcementList = new ArrayList<>();
		}
		
		return announcementList;
	}
	
	/**
	 * 获得该公司的公告
	 * 
	 * @param companyId
	 * @param lang
	 * @param startTime
	 * @return
	 */
	public List<Map<String, String>> getCompanyAnnouncements(int companyId, String lang, String startTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("lang", lang);
		params.put("companyId", companyId);
		params.put("created", startTime);
		
		List<Map<String, String>> announcementList = selectList(Constants.DAO_NAME_SPACE_CORE + "ct_announcement_getAnnouncements", params);
		
		if (announcementList == null || announcementList.size() == 0) {
			announcementList = new ArrayList<>();
		}
		
		return announcementList;
	}
	
	/**
	 * 根据id获得该公告的内容
	 * 
	 * @param id
	 * @param lang
	 * @return
	 */
	public String getAnnouncementContent(int id, String lang) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("lang", lang);
		
		String announcementContent =
				selectOne(Constants.DAO_NAME_SPACE_CORE + "ct_announcement_getAnnouncementContent", params);
		
		return announcementContent == null ? "" : announcementContent;
	}
}
