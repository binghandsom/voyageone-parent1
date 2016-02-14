package com.voyageone.batch.cms.service.platform.ali;

import com.voyageone.batch.cms.bean.platform.SxData;
import com.voyageone.batch.cms.bean.platform.SxWorkLoadBean;
import com.voyageone.batch.cms.service.platform.PlatformMethod;
import com.voyageone.batch.cms.service.platform.PlatformMethodInterface;
import com.voyageone.batch.cms.service.platform.common.SxGetProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by zhujiaye on 16/2/14.
 */
@Repository
public class AliSx extends PlatformMethod implements PlatformMethodInterface {
	@Autowired SxGetProductInfo sxGetProductInfo;
	@Autowired AliSxProduct aliSxProduct;

	@Override
	public void doJob(SxWorkLoadBean workLoadBean) {
		// 获取product表中, 指定group的所有product的信息
		SxData sxData = sxGetProductInfo.getProductInfoByGroupId(workLoadBean.getChannelId(), workLoadBean.getGroupId());

		// 确认product是否已上传
//		aliSxProduct.xxx
		// 上传product - 数据准备
		// 上传product

		// 确认item是否已上传
		// 上传item - 数据准备
		// 上传item之前再取得一下库存
		// 上传item

		// 记录完成: 正常结束
		doComplete(workLoadBean, 1);

	}
}
