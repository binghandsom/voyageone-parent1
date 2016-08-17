package com.voyageone.service.impl.com.channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.TmCarrierChannelBean;
import com.voyageone.service.dao.com.TmCarrierChannelDao;
import com.voyageone.service.daoext.com.TmCarrierChannelDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmCarrierChannelKey;
import com.voyageone.service.model.com.TmCarrierChannelModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/17
 */
@Service
public class CarrierConfigService extends BaseService {
	
	@Autowired
	private TmCarrierChannelDao carrierChannelDao;
	
	@Autowired
	private TmCarrierChannelDaoExt carrierChannelDaoExt;

	public PageModel<TmCarrierChannelBean> searchCarrierConfigByPage(String channelId, String carrier,
			String useKd100Flg, Integer pageNum, Integer pageSize) {
		PageModel<TmCarrierChannelBean> pageModel = new PageModel<TmCarrierChannelBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("carrier", carrier);
		params.put("useKd100Flg", useKd100Flg);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(carrierChannelDaoExt.searchCarrierConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询渠道属性信息
		pageModel.setResult(carrierChannelDaoExt.searchCarrierConfigByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateCarrierConfig(TmCarrierChannelModel model, String username, boolean append) {
		// 检索快递信息
		TmCarrierChannelKey carrierKey = new TmCarrierChannelKey();
		carrierKey.setOrderChannelId(model.getOrderChannelId());
		carrierKey.setCarrier(model.getCarrier());
		TmCarrierChannelModel carrier = carrierChannelDao.select(carrierKey);
		
		// 保存快递信息
		boolean success = false;
		if (append) {
			// 添加快递信息
			if (carrier != null) {
				throw new BusinessException("添加的快递信息已存在");
			}
			model.setCreater(username);
			model.setModifier(username);
			success = carrierChannelDao.insert(model) > 0;
		} else {
			// 更新快递信息
			if (carrier == null) {
				throw new BusinessException("更新的快递信息已存在");
			}
			model.setModifier(username);
			success = carrierChannelDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存快递信息失败");
		}
	}

	@VOTransactional
	public void deleteCarrierConfig(List<TmCarrierChannelKey> carrierKeys, String username) {
		for (TmCarrierChannelKey carrierKey : carrierKeys) {
			TmCarrierChannelModel model = new TmCarrierChannelModel();
			model.setOrderChannelId(carrierKey.getOrderChannelId());
			model.setCarrier(carrierKey.getCarrier());
			model.setActive(false);
			model.setModifier(username);
			if (carrierChannelDao.update(model) <= 0) {
				throw new BusinessException("删除快递信息失败");
			}
		}
	}

	public List<Map<String, Object>> getAllCarrier() {
		return carrierChannelDaoExt.selectAllCarrier();
	}

}
