package com.voyageone.service.impl.com.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmPortConfigModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/23
 */
@Service
public class PortConfigService extends BaseService {

	public PageModel<TmPortConfigModel> searchPortConfigByPage(String port, String cfgName, String cfgVal,
			Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addOrUpdatePortConfig(TmPortConfigModel model, boolean append) {
		// TODO Auto-generated method stub
		
	}

	public void deletePortConfig(List<Integer> portIds) {
		// TODO Auto-generated method stub
		
	}

}
