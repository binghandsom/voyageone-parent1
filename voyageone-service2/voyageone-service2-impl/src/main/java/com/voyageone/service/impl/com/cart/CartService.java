package com.voyageone.service.impl.com.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.dao.com.CtCartDao;
import com.voyageone.service.daoext.admin.CtCartDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.CtCartModel;
import com.voyageone.service.model.com.PageModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/11
 */
@Service("AdminCartService")
public class CartService extends BaseService {
	
	@Autowired
	private CtCartDao cartDao;
	
	@Autowired
	private CtCartDaoExt cartDaoExt;

	public List<CtCartModel> getAllCart() {
		return cartDao.selectList(Collections.emptyMap());
	}

	public List<CtCartModel> getCartByIds(List<String> cartIds) {
		List<Integer> iCartIds = new ArrayList<Integer>();
		for (String cartId : cartIds) {
			iCartIds.add(Integer.valueOf(cartId));
		}
		return cartDaoExt.selectCartByIds(iCartIds);
	}

	public PageModel<CtCartModel> searchCartByPage(Integer cartId, String cartName, String cartType,
			Integer pageNum, Integer pageSize) {
		PageModel<CtCartModel> pageModel = new PageModel<CtCartModel>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cartId", cartId);
		params.put("cartName", cartName);
		params.put("cartType", cartType);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(cartDaoExt.selectCartCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询渠道信息
		pageModel.setResult(cartDaoExt.selectCartByPage(params));
		
		return pageModel;
	}

	public List<Map<String, Object>> getAllPlatform() {
		return cartDaoExt.selectAllPlatform();
	}

	public void addOrUpdateCart(CtCartModel model, String username, boolean append) {
		CtCartModel cart = cartDao.select(model.getCartId());
		boolean success = false;
		if (append) {
			// 添加Cart信息
			if (cart != null) {
				throw new BusinessException("添加的Cart信息已存在");
			}
			model.setCreater(username);
			model.setModifier(username);
			success = cartDao.insert(model) > 0;
		} else {
			// 更新Cart信息
			if (cart == null) {
				throw new BusinessException("更新的Cart信息不存在");
			}
			model.setModifier(username);
			success = cartDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存Cart信息失败");
		}
	}

	public void deleteCart(List<Integer> cartIds, String username) {
		for (Integer cartId : cartIds) {
			CtCartModel model = new CtCartModel();
			model.setCartId(cartId);
			model.setActive(false);
			model.setModifier(username);
			if (cartDao.update(model) <= 0) {
				throw new BusinessException("删除Cart信息失败");
			}
		}
	}

}
