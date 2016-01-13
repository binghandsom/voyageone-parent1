package com.voyageone.core.service;

import java.util.List;

import com.voyageone.core.modelbean.MasterInfoBean;


/**
 * OMS master info service
 * 
 * @author jacky
 *
 */
public interface MasterInfoService {

	/**
	 * 根据分类Id获得master信息
	 * 
	 * @param type
	 * @return
	 */
	public List<MasterInfoBean> getMasterInfoFromId(int type, boolean showBlank);
}
