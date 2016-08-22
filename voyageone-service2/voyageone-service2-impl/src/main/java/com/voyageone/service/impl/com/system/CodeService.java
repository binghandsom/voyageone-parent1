package com.voyageone.service.impl.com.system;

import java.util.List;

import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmCodeKey;
import com.voyageone.service.model.com.TmCodeModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/22
 */
public class CodeService extends BaseService {

	public PageModel<TmCodeModel> searchCodeByPage(String code, String name, String des, Integer pageNum,
			Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addOrUpdateCode(TmCodeModel model, String username, boolean append) {
		// TODO Auto-generated method stub
		
	}

	public void deleteCode(List<TmCodeKey> codeKeys, String username) {
		// TODO Auto-generated method stub
		
	}

}
