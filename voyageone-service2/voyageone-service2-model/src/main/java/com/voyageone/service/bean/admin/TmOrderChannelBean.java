package com.voyageone.service.bean.admin;

import java.util.List;

import com.voyageone.service.model.admin.CtCartModel;
import com.voyageone.service.model.admin.TmOrderChannelModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/12
 */
public class TmOrderChannelBean extends TmOrderChannelModel {
	
	private List<CtCartModel> carts;

	public List<CtCartModel> getCarts() {
		return carts;
	}

	public void setCarts(List<CtCartModel> carts) {
		this.carts = carts;
	}

}
