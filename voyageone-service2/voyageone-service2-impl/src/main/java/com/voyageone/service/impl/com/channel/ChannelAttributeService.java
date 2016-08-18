package com.voyageone.service.impl.com.channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.ComMtValueChannelBean;
import com.voyageone.service.dao.com.ComMtValueChannelDao;
import com.voyageone.service.daoext.com.ComMtValueChannelDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.ComMtValueChannelModel;
import com.voyageone.service.model.com.PageModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/16
 */
@Service
public class ChannelAttributeService extends BaseService {
	
	@Autowired
	private ComMtValueChannelDao channelAttrDao;
	
	@Autowired
	private ComMtValueChannelDaoExt channelAttrDaoExt;

	public PageModel<ComMtValueChannelBean> searchChannelAttributeByPage(String channelId, Integer typeId,
			String langId, String name, String value, Integer pageNum, Integer pageSize) {
		PageModel<ComMtValueChannelBean> pageModel = new PageModel<ComMtValueChannelBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channelId", channelId);
		params.put("typeId", typeId);
		params.put("langId", langId);
		params.put("name", name);
		params.put("value", value);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(channelAttrDaoExt.selectChannelAttributeCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询渠道属性信息
		pageModel.setResult(channelAttrDaoExt.selectChannelAttributeByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateChannelAttribute(ComMtValueChannelModel model, String username, boolean append) {
		// 保存渠道属性信息
		boolean success = false;
		if (append) {
			// 添加渠道属性信息
			model.setCreater(username);
			model.setModifier(username);
			success = channelAttrDao.insert(model) > 0;
		} else {
			ComMtValueChannelModel channelAttr = channelAttrDao.select(model.getId());
			if (channelAttr == null) {
				throw new BusinessException("更新的渠道属性信息不存在");
			}
			model.setModifier(username);
			success = channelAttrDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存渠道属性信息失败");
		}
	}

	@VOTransactional
	public void deleteChannelAttribute(List<Integer> channelAttrIds, String username) {
		for (Integer channelAttrId : channelAttrIds) {
			ComMtValueChannelModel model = new ComMtValueChannelModel();
			model.setId(channelAttrId);
			model.setActive(false);
			model.setModifier(username);
			if (channelAttrDao.update(model) <= 0) {
				throw new BusinessException("删除渠道属性信息失败");
			}
		}
	}

}
