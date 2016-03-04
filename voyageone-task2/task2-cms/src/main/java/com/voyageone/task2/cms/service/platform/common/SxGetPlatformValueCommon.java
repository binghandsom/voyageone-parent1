package com.voyageone.task2.cms.service.platform.common;

import com.voyageone.task2.cms.dao.BrandMapDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by zhujiaye on 16/2/15.
 */
@Repository
public class SxGetPlatformValueCommon {
	@Autowired
	private BrandMapDao brandMapDao;

	/**
	 * 根据主数据的品牌, 获取platform的brand
	 * @param channelId channel id
	 * @param cartId cart id
	 * @param brand brand
	 * @return platform的brand
	 */
	public String getBrand(String channelId, int cartId, String brand) {
		return brandMapDao.cmsBrandToPlatformBrand(channelId, cartId, brand);
	}

}
