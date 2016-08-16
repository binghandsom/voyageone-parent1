package com.voyageone.service.impl.admin.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.admin.ComMtThirdPartyConfigBean;
import com.voyageone.service.dao.admin.ComMtThirdPartyConfigDao;
import com.voyageone.service.daoext.admin.ComMtThirdPartyConfigDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.admin.ComMtThirdPartyConfigModel;
import com.voyageone.service.model.admin.PageModel;

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

	public PageModel<ComMtThirdPartyConfigBean> searchThirdPartyConfigByPage(String channelId, Integer pageNum,
			String propVal, Integer pageNum2, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
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

}
