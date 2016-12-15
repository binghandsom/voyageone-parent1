package com.voyageone.service.impl.com.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.ComMtTrackingInfoConfigBean;
import com.voyageone.service.dao.com.ComMtTrackingInfoConfigDao;
import com.voyageone.service.daoext.com.ComMtTrackingInfoConfigDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.ComMtTrackingInfoConfigModel;
import com.voyageone.service.bean.com.PaginationResultBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/22
 */
@Service
public class CartTrackingService extends BaseService {
	
	@Autowired
	private ComMtTrackingInfoConfigDao cartTrackingDao;
	
	@Autowired
	private ComMtTrackingInfoConfigDaoExt cartTrackingDaoExt;
	
	public List<ComMtTrackingInfoConfigBean> searchCartTrackingByKeys(String channelId, Integer cartId) {
		return searchCartTrackingByPage(channelId, cartId, null, null, null, null, null).getResult();
	}

	public PaginationResultBean<ComMtTrackingInfoConfigBean> searchCartTrackingByPage(String channelId, Integer cartId,
																					  String trackingStatus, String location, Boolean active, Integer pageNum, Integer pageSize) {
		PaginationResultBean<ComMtTrackingInfoConfigBean> paginationResultBean = new PaginationResultBean<ComMtTrackingInfoConfigBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("cartId", cartId);
		params.put("trackingStatus", trackingStatus);
		params.put("location", location);
		params.put("active", active);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			paginationResultBean.setCount(cartTrackingDaoExt.selectCartTrackingCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询Catr物流信息
		paginationResultBean.setResult(cartTrackingDaoExt.selectCartTrackingByPage(params));
		
		return paginationResultBean;
	}

	public void addOrUpdateCartTracking(ComMtTrackingInfoConfigModel model, String username, boolean append) {
		boolean success = false;
		if (append) {
			// 添加Cart物流信息
			model.setCreater(username);
			model.setModifier(username);
			success = cartTrackingDao.insert(model) > 0;
		} else {
			// 更新Cart物流信息
			ComMtTrackingInfoConfigModel cartTracking = cartTrackingDao.select(model.getSeq());
			if (cartTracking == null) {
				throw new BusinessException("更新的Cart物流信息不存在");
			}
			model.setModifier(username);
			success = cartTrackingDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存Cart物流信息失败");
		}
	}

	@VOTransactional
	public void deleteCartTracking(List<Integer> cartTrackingIds, String username) {
		for (Integer cartTrackingId : cartTrackingIds) {
			ComMtTrackingInfoConfigModel model = new ComMtTrackingInfoConfigModel();
			model.setSeq(cartTrackingId);
			model.setActive(false);
			model.setModifier(username);
			if (cartTrackingDao.update(model) <= 0) {
				throw new BusinessException("删除Cart物流信息失败");
			}
		}
	}

}
