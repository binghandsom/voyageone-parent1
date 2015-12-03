package com.voyageone.core.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public interface AnnouncementService {

	public List<Map<String, String>> getPublicAnnouncements(HttpServletRequest request);
	
	public List<Map<String, String>> getCompanyAnnouncements(HttpServletRequest request);
	
	public String getAnnouncementContent(HttpServletRequest request, int id);
}
