package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchUpdateProductTitleMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;

/**
 * 高级检索批量修改商品Title消息Job
 *
 * @Author rex.wu
 * @Create 2017-05-23 14:44
 */
public class CmsBatchUpdateProductTitleMQJob extends TBaseMQCmsSubService<CmsBatchUpdateProductTitleMQMessageBody> {
    @Override
    public void onStartup(CmsBatchUpdateProductTitleMQMessageBody messageBody) throws Exception {

    }
}
