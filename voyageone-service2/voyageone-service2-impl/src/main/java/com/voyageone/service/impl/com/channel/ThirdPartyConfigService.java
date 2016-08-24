package com.voyageone.service.impl.com.channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.ComMtThirdPartyConfigBean;
import com.voyageone.service.dao.com.ComMtThirdPartyConfigDao;
import com.voyageone.service.daoext.com.ComMtThirdPartyConfigDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.ComMtThirdPartyConfigModel;
import com.voyageone.service.model.com.PageModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/16
 */
@Service
public class ThirdPartyConfigService extends BaseService {
	
	@Autowired
	private ComMtThirdPartyConfigDao thirdPartyConfigDao;
	
	@Autowired
	private ComMtThirdPartyConfigDaoExt thirdPartyConfigDaoExt;

	public PageModel<ComMtThirdPartyConfigBean> searchThirdPartyConfigByPage(String channelId, String propName,
			String propVal, Boolean active, Integer pageNum, Integer pageSize) {
		PageModel<ComMtThirdPartyConfigBean> pageModel = new PageModel<ComMtThirdPartyConfigBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("propName", propName);
		params.put("propVal", propVal);
		params.put("active", active);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(thirdPartyConfigDaoExt.selectThirdPartyConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询第三方配置信息
		pageModel.setResult(thirdPartyConfigDaoExt.selectThirdPartyConfigByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateThirdPartyConfig(ComMtThirdPartyConfigModel model, String username, boolean append) {
		boolean success = false;
		if (append) {
			// 添加第三方配置信息
			model.setCreater(username);
			model.setModifier(username);
			success = thirdPartyConfigDao.insert(model) > 0;
		} else {
			ComMtThirdPartyConfigModel thirdPartyConfig = thirdPartyConfigDao.select(model.getSeq());
			// 更新渠道信息
			if (thirdPartyConfig == null) {
				throw new BusinessException("更新的第三方配置信息不存在");
			}
			model.setModifier(username);
			success = thirdPartyConfigDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存第三方配置信息失败");
		}
	}

	@VOTransactional
	public void deleteThirdPartyConfig(List<Integer> seqIds, String username) {
		for (Integer seqId : seqIds) {
			ComMtThirdPartyConfigModel model = new ComMtThirdPartyConfigModel();
			model.setSeq(seqId);
			model.setActive(false);
			model.setModifier(username);
			if (thirdPartyConfigDao.update(model) <= 0) {
				throw new BusinessException("删除第三方配置信息失败");
			}
		}
	}

}
