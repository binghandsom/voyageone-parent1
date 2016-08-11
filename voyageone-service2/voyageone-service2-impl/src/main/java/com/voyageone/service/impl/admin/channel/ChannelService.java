package com.voyageone.service.impl.admin.channel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.dao.admin.TmOrderChannelDao;
import com.voyageone.service.daoext.admin.TmOrderChannelDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.TmOrderChannelModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/10
 */
@Service("AdminChannelService")
public class ChannelService extends BaseService {
	
	@Autowired
	private TmOrderChannelDao channelDao;
	
	@Autowired
	private TmOrderChannelDaoExt channelDaoExt;
	
	public List<TmOrderChannelModel> getAllChannel() {
		return channelDao.selectList(Collections.emptyMap());
	}
	
	public List<TmOrderChannelModel> searchChannel(String channelId, String channelName, Integer isUsjoi) {
		return searchChannel(channelId, channelName, isUsjoi, 0, 0).getResult();
	}
	
	public PageModel<TmOrderChannelModel> searchChannel(String channelId, String channelName, Integer isUsjoi,
			int pageNum, int pageSize) {
		PageModel<TmOrderChannelModel> pageModel = new PageModel<TmOrderChannelModel>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("channelName", channelName);
		params.put("isUsjoi", isUsjoi);
		
		// 判断查询结果是否分页
		if (pageNum > 0 && pageSize > 0) {
			pageModel.setCount(channelDaoExt.selectChannelCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询渠道信息
		pageModel.setResult(channelDaoExt.selectChannelList(params));
		
		return pageModel;
	}

	public List<Map<String, Object>> getAllCompany() {
		return channelDaoExt.selectAllCompany();
	}

	public boolean addOrUpdateChannel(TmOrderChannelModel model) {
		// 验证渠道ID唯一性
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", model.getOrderChannelId());
		if (channelDao.selectOne(params) != null) {
			throw new BusinessException("渠道ID[" + model.getOrderChannelId() + "]已存在！");
		}
		
		// 保存渠道信息，并返回保存结果。
		return channelDao.insert(model) > 0;
	}

}
