package com.voyageone.task2.cms.service.platform.ali;

import com.voyageone.task2.cms.bean.platform.SxData;
import com.voyageone.task2.cms.bean.platform.SxWorkLoadBean;
import com.voyageone.task2.cms.service.platform.PlatformMethod;
import com.voyageone.task2.cms.service.platform.PlatformMethodInterface;
import com.voyageone.task2.cms.service.platform.common.SxGetProductInfo;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by zhujiaye on 16/2/14.
 */
@Repository
public class AliSx extends PlatformMethod implements PlatformMethodInterface {
	@Autowired
	private SxGetProductInfo sxGetProductInfo;
	@Autowired
	private AliSxProduct aliSxProduct;

	@Override
	public void doJob(SxWorkLoadBean workLoadBean) {
		// 获取product表中, 指定group的所有product的信息
//		SxData sxData = sxGetProductInfo.getProductInfoByGroupId(workLoadBean.getChannelId(), workLoadBean.getGroupId());
//
//		// 确认product是否已上传
//		if (StringUtils.isEmpty(sxData.getPlatformProductId())) {
//			// 去天猫(天猫国际)上去寻找是否有存在这个product
//			Long productId = aliSxProduct.getProductIdFromTmall(sxData);
//		}
//
//		// 上传product - 数据准备
//		// 上传product
//
//		// 确认item是否已上传
//		// 上传item - 数据准备
//		// 上传item之前再取得一下库存
//		// 上传item
//
//		// 记录完成: 正常结束
//		doComplete(workLoadBean, 1);

	}
}
