package com.voyageone.service.daoext.cms;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 平台品牌数据访问扩展
 * @author Wangtd 2016/08/01
 * @since 2.3.0
 */
@Repository
public interface CmsMtPlatformBrandsDaoExt {

	/** 
	 * 删除指定渠道和店铺的品牌数据
	 */
	void deleteBrandsByChannelIdAndCartId(@Param("channelId") String channelId, @Param("cartId") String cartId);

}
