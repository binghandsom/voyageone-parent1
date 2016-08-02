package com.voyageone.service.daoext.cms;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Wangtd on 8/1/16.
 */
@Repository
public interface CmsMtPlatformBrandsDaoExt {

	/** 
	 * 删除指定渠道和店铺的品牌数据
	 */
	void deleteBrandsByChannelIdAndCartId(@Param("channelId") String channelId, @Param("cartId") String cartId);

}
