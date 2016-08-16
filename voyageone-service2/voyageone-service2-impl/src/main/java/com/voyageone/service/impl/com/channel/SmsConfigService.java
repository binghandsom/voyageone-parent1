package com.voyageone.service.impl.com.channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.TmSmsConfigBean;
import com.voyageone.service.dao.com.TmSmsConfigDao;
import com.voyageone.service.daoext.admin.TmSmsConfigDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmSmsConfigModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/15
 */
@Service
public class SmsConfigService extends BaseService {
	
	@Autowired
	private TmSmsConfigDao smsConfigDao;
	
	@Autowired
	private TmSmsConfigDaoExt smsConfigDaoExt;

	public PageModel<TmSmsConfigBean> searchSmsConfigByPage(String channelId, String smsType, String content,
			String smsCode, Integer pageNum, Integer pageSize) {
		PageModel<TmSmsConfigBean> pageModel = new PageModel<TmSmsConfigBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("smsType", smsType);
		params.put("content", content);
		params.put("smsCode", smsCode);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(smsConfigDaoExt.selectSmsConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询短信配置信息
		pageModel.setResult(smsConfigDaoExt.selectSmsConfigByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateSmsConfig(TmSmsConfigModel model, String username, boolean append) {
		// 保存渠道信息
		boolean success = false;
		if (append) {
			// 添加短信配置信息
			model.setCreater(username);
			model.setModifier(username);
			success = smsConfigDao.insert(model) > 0;
		} else {
			// 检索短信配置信息
			TmSmsConfigModel smsConfig = smsConfigDao.select(model.getSeq());
			// 更新渠道信息
			if (smsConfig == null) {
				throw new BusinessException("更新的短信配置信息不存在");
			}
			model.setModifier(username);
			success = smsConfigDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存短信配置信息失败");
		}
	}

	@VOTransactional
	public void deleteSmsConfig(List<Integer> seqIds, String username) {
		for (Integer seqId : seqIds) {
			TmSmsConfigModel smsConfig = new TmSmsConfigModel();
			smsConfig.setSeq(seqId);
			smsConfig.setDelFlg("0");
			smsConfig.setModifier(username);
			if (smsConfigDao.update(smsConfig) <= 0) {
				throw new BusinessException("删除短信配置信息失败");
			}
		}
	}
	
}
