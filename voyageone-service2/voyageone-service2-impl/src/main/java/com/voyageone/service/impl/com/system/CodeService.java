package com.voyageone.service.impl.com.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.dao.com.TmCodeDao;
import com.voyageone.service.daoext.com.TmCodeDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmCodeKey;
import com.voyageone.service.model.com.TmCodeModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/22
 */
@Service
public class CodeService extends BaseService {
	
	@Autowired
	private TmCodeDao codeDao;
	
	@Autowired
	private TmCodeDaoExt codeDaoExt;

	public PageModel<TmCodeModel> searchCodeByPage(String id, String code, String name, String des, Boolean active,
			Integer pageNum, Integer pageSize) {
		PageModel<TmCodeModel> pageModel = new PageModel<TmCodeModel>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("code", code);
		params.put("name", name);
		params.put("des", des);
		params.put("active", active);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(codeDaoExt.selectCodeCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询Code信息
		pageModel.setResult(codeDaoExt.selectCodeByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateCode(TmCodeModel model, String username, boolean append) {
		TmCodeModel code = codeDao.select(model);
		boolean success = false;
		if (append) {
			// 添加Code信息
			if (code != null) {
				throw new BusinessException("添加的Code信息已存在");
			}
			model.setCreater(username);
			model.setModifier(username);
			success = codeDao.insert(model) > 0;
		} else {
			// 更新Code信息
			if (code == null) {
				throw new BusinessException("添加的Code信息不存在");
			}
			model.setModifier(username);
			success = codeDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存Code信息失败");
		}
	}

	@VOTransactional
	public void deleteCode(List<TmCodeKey> codeKeys, String username) {
		for (TmCodeKey codeKey : codeKeys) {
			TmCodeModel model = new TmCodeModel();
			BeanUtils.copyProperties(codeKey, model);
			model.setActive(false);
			model.setModifier(username);
			if (codeDao.update(model) <= 0) {
				throw new BusinessException("删除Code信息失败");
			}
		}
	}

}
