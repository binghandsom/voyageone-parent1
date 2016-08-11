package com.voyageone.service.impl.admin.cart;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.service.dao.admin.CtCartDao;
import com.voyageone.service.daoext.admin.CtCartDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.admin.CtCartModel;

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

	public List<CtCartModel> getCartByIds(List<Integer> cartIds) {
		return cartDaoExt.selectCartByIds(cartIds);
	}

}
