package com.voyageone.cms.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SearchService {

	public List<Map<String, Object>> doSearch(Map<String, Object> data);

	public List<Map<String, Object>> doAdvanceSearch(Map<String, Object> data);

	public int doSearchCount(Map<String, Object> data);
	
	public int doAdvanceSearchCount(Map<String, Object> data);

	File doExport(Map<String, Object> data, String path, String name);

}
