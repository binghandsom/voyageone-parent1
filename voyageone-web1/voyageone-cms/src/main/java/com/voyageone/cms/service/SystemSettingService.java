package com.voyageone.cms.service;

import java.util.List;
import java.util.Map;

import com.voyageone.cms.modelbean.MtType;

public interface SystemSettingService {

	List<MtType> getCmsMtType(Map<String, Object> data);

	boolean updateMtType(MtType mtType);

	boolean insertMtType(MtType mtType);

	int getMtTypeCount();

}
