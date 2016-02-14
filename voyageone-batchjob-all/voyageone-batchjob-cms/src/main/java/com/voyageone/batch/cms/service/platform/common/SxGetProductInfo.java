package com.voyageone.batch.cms.service.platform.common;

import com.voyageone.batch.cms.bean.platform.SxData;
import org.springframework.stereotype.Repository;

/**
 * Created by zhujiaye on 16/2/14.
 */
@Repository
public class SxGetProductInfo {
	/**
	 * 获取group的所有商品的信息
	 */
	public SxData getProductInfoByGroupId(String channelId, Long groupId) {
		// TODO: 获取数据 ……

		return new SxData();
	}

	/**
	 * 获取group的所有商品的主数据信息
	 */
	private void getProductInfo(String channelId, Integer cartId, Long groupId) {

	}

	/**
	 * 获取group的所有商品的sku信息
	 */
	private void getSkuInfo(String channelId, Integer cartId, Long groupId) {

	}

	/**
	 * 获取group的所有商品的库存信息
	 */
	public void getQtyInfo(String channelId, Integer cartId, Long groupId) {

	}

}
