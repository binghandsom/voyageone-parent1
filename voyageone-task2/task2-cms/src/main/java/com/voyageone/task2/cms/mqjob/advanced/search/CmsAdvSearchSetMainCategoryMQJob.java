package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.product.ProductMainCategoryService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchSetMainCategoryMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 高级检索文件导出job
 *
 * @Author rex
 * @Create 2016-12-30 15:25
 */
@Service
@RabbitListener()
public class CmsAdvSearchSetMainCategoryMQJob extends TBaseMQCmsService<CmsBatchSetMainCategoryMQMessageBody> {

    @Autowired
    private ProductMainCategoryService productMainCategoryService;

    @Override
    public void onStartup(CmsBatchSetMainCategoryMQMessageBody messageBody) {

        super.count = messageBody.getProductCodes().size();
        List<CmsBtOperationLogModel_Msg> failList = productMainCategoryService.setMainCategory(messageBody);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getProductCodes().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
