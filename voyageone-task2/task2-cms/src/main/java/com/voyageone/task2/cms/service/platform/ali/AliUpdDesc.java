package com.voyageone.task2.cms.service.platform.ali;

import com.voyageone.task2.cms.bean.platform.SxData;
import com.voyageone.task2.cms.bean.platform.SxWorkLoadBean;
import com.voyageone.task2.cms.service.platform.PlatformMethod;
import com.voyageone.task2.cms.service.platform.PlatformMethodInterface;
import com.voyageone.task2.cms.service.platform.common.SxGetProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by zhujiaye on 16/2/14.
 */
@Repository
public class AliUpdDesc extends PlatformMethod implements PlatformMethodInterface {
	@Autowired
	private SxGetProductInfo sxGetProductInfo;

	@Override
	public void doJob(SxWorkLoadBean workLoadBean) {
//		// 获取product表中, 指定group的所有product的信息
//		SxData sxData = sxGetProductInfo.getProductInfoByGroupId(workLoadBean.getChannelId(), workLoadBean.getGroupId());
//
//		// 数据准备
//
//		// 更新描述信息
//
//		// 记录完成: 正常结束
//		doComplete(workLoadBean, 1);

	}
}
