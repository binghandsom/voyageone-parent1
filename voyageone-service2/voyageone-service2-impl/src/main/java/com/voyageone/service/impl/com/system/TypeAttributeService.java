package com.voyageone.service.impl.com.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.ComMtValueBean;
import com.voyageone.service.dao.com.ComMtValueDao;
import com.voyageone.service.daoext.com.ComMtValueDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.ComMtValueModel;
import com.voyageone.service.bean.com.PaginationResultBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/19
 */
@Service
public class TypeAttributeService extends BaseService {
	
	@Autowired
	private ComMtValueDao typeAttrDao;

	@Autowired
	private ComMtValueDaoExt typeAttrDaoExt;

	public PaginationResultBean<ComMtValueBean> searchTypeAttributeByPage(Integer typeId, String langId, String name, String value,
																		  Boolean active, Integer pageNum, Integer pageSize) {
		PaginationResultBean<ComMtValueBean> paginationResultBean = new PaginationResultBean<ComMtValueBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("typeId", typeId);
		params.put("langId", langId);
		params.put("name", name);
		params.put("value", value);
		params.put("active", active);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			paginationResultBean.setCount(typeAttrDaoExt.selectTypeAttributeCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询类型配置信息
		paginationResultBean.setResult(typeAttrDaoExt.selectTypeAttributeByPage(params));
		
		return paginationResultBean;
	}

	public void addOrUpdateTypeAttribute(ComMtValueModel model, String username, boolean append) {
		boolean success = false;
		if (append) {
			// 添加类型属性信息
			model.setCreater(username);
			model.setModifier(username);
			success = typeAttrDao.insert(model) > 0;
		} else {
			// 更新类型属性信息
			ComMtValueModel typeAttr = typeAttrDao.select(model.getId());
			if (typeAttr == null) {
				throw new BusinessException("更新的类型属性信息不存在");
			}
			model.setModifier(username);
			success = typeAttrDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存类型属性信息失败");
		}
	}

	@VOTransactional
	public void deleteTypeAttribute(List<Integer> typeAttrIds, String username) {
		for (Integer typeAttrId : typeAttrIds) {
			ComMtValueModel model = new ComMtValueModel();
			model.setId(typeAttrId);
			model.setActive(false);
			model.setModifier(username);
			if (typeAttrDao.update(model) <= 0) {
				throw new BusinessException("删除类型属性信息失败");
			}
		}
	}

}
