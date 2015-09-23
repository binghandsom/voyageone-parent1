package com.voyageone.core.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.voyageone.common.Constants;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.voyageone.core.CoreConstants;
import com.voyageone.core.dao.AnnouncementDao;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.service.AnnouncementService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

	@Autowired
	private AnnouncementDao announcementDao;
	
	/**
	 * 获得公共的公告标题
	 * 
	 * @return
	 */
	public List<Map<String, String>> getPublicAnnouncements(HttpServletRequest request) {
		// 获得语言标志
		String lang = (String)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_LANG);
		// 开始时间 TODO 先固定写死
		String startTime = DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(-2), 8,DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		
		return announcementDao.getPublicAnnouncements(lang, startTime);
	};
	
	/**
	 * 获得公司的公告标题
	 * 
	 * @param companyId
	 * @return
	 */
	public List<Map<String, String>> getCompanyAnnouncements(HttpServletRequest request) {
		// 取得用户信息session
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		// 设置当前用户选择的公司
		int companyId = user.getSelectCompany().getCompanyId();
		// 获得语言标志
		String lang = (String)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_LANG);
		// 开始时间 先固定写死
		String startTime = DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(-2),8, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		
		return announcementDao.getCompanyAnnouncements(companyId, lang, startTime);
	};
	
	/**
	 * 获得公告内容
	 * 
	 * @param id
	 * @return
	 */
	public String getAnnouncementContent(HttpServletRequest request, int id) {
		// 获得语言标志
		String lang = (String)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_LANG);
		
		return announcementDao.getAnnouncementContent(id, lang);
	};
}
