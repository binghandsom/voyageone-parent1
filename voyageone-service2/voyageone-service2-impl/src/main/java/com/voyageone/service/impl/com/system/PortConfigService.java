package com.voyageone.service.impl.com.system;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.dao.com.TmPortConfigDao;
import com.voyageone.service.daoext.com.TmPortConfigDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmPortConfigModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/23
 */
@Service
public class PortConfigService extends BaseService {
	
	@Autowired
	private TmPortConfigDao portConfigDao;

	@Autowired
	private TmPortConfigDaoExt portConfigDaoExt;

	public List<TmPortConfigModel> getAllPort() {
		return portConfigDao.selectList(Collections.emptyMap());
	}

	public PageModel<TmPortConfigModel> searchPortConfigByPage(String port, String cfgName, String cfgVal,
			Integer pageNum, Integer pageSize) {
		PageModel<TmPortConfigModel> pageModel = new PageModel<TmPortConfigModel>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("port", port);
		params.put("cfgName", cfgName);
		params.put("cfgVal", cfgVal);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(portConfigDaoExt.selectPortConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询港口信息
		pageModel.setResult(portConfigDaoExt.selectPortConfigByPage(params));
		
		return pageModel;
	}

	public void addOrUpdatePortConfig(TmPortConfigModel model, boolean append) {
		boolean success = false;
		if (append) {
			// 添加港口信息
			success = portConfigDao.insert(model) > 0;
		} else {
			// 更新港口信息
			TmPortConfigModel portConfig = portConfigDao.select(model.getSeq());
			if (portConfig == null) {
				throw new BusinessException("更新的港口信息不存在");
			}
			success = portConfigDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存港口信息失败");
		}
	}

	@VOTransactional
	public void deletePortConfig(List<Integer> portIds) {
		for (Integer portId : portIds) {
			if (portConfigDao.delete(portId) <= 0) {
				throw new BusinessException("删除港口信息失败");
			}
		}
	}

}
