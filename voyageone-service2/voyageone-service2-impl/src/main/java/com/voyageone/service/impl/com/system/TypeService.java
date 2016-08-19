package com.voyageone.service.impl.com.system;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.dao.com.ComMtTypeDao;
import com.voyageone.service.daoext.com.ComMtTypeDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.ComMtTypeModel;
import com.voyageone.service.model.com.PageModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/17
 */
@Service
public class TypeService extends BaseService {
	
	@Autowired
	private ComMtTypeDao typeDao;
	
	@Autowired
	private ComMtTypeDaoExt typeDaoExt;
	
	public List<ComMtTypeModel> getAllType() {
		return typeDao.selectList(Collections.emptyMap());
	}

	public PageModel<ComMtTypeModel> searchTypeByPage(Integer id, String name, String comment, Integer pageNum,
			Integer pageSize) {
		PageModel<ComMtTypeModel> pageModel = new PageModel<ComMtTypeModel>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("name", name);
		params.put("comment", comment);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(typeDaoExt.selectTypeCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询类型信息
		pageModel.setResult(typeDaoExt.selectTypeByPage(params));
		
		return pageModel;
	}

	public ComMtTypeModel addOrUpdateType(ComMtTypeModel model, String username, boolean append) {
		// 保存类型信息
		boolean success = false;
		if (append) {
			// 添加类型信息
			model.setCreater(username);
			model.setModifier(username);
			success = typeDao.insert(model) > 0;
		} else {
			// 更新类型信息
			ComMtTypeModel typeInfo = typeDao.select(model.getId());
			if (typeInfo == null) {
				throw new BusinessException("更新的类型信息不存在");
			}
			model.setModifier(username);
			success = typeDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存类型信息失败");
		}
		
		return model;
	}

	public void deleteType(List<Integer> typeIds, String username) {
		for (Integer typeId : typeIds) {
			ComMtTypeModel model = new ComMtTypeModel();
			model.setId(typeId);
//			model.setActive(false);
			model.setModifier(username);
			if (typeDao.update(model) <= 0) {
				throw new BusinessException("保存类型信息失败");
			}
		}
	}

}
