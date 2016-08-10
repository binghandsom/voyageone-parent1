package com.voyageone.service.impl.admin.channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.service.dao.admin.TmOrderChannelDao;
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
	
	public List<TmOrderChannelModel> searchAllChannel() {
		return searchChannel(null, null, null, null);
	}
	
	public List<TmOrderChannelModel> searchChannel(String channelId, String name, String fullName, Integer isUsjoi) {
		return searchChannel(channelId, name, fullName, isUsjoi, 0, 0).getResult();
		
	}
	
	public PageModel<TmOrderChannelModel> searchChannel(String channelId, String name, String fullName, Integer isUsjoi,
			int pageNum, int pageSize) {
		PageModel<TmOrderChannelModel> pageModel = new PageModel<TmOrderChannelModel>();
		
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channelId", channelId);
		params.put("name", name);
		params.put("fullName", fullName);
		params.put("isUsjoi", isUsjoi);
		
		// 判断查询结果是否分页
		if (pageNum > 0 && pageSize > 0) {
			pageModel.setCount(channelDao.selectCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		
		// 查询渠道信息
		pageModel.setResult(channelDao.selectList(params));
		
		return pageModel;
	}

}
