package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchPartApprovalMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by james on 2017/4/21.
 * 批量部分字段上新
 */
@Service
@VOSubRabbitListener
public class CmsAdvSearchPartApprovalMQJob extends TBaseMQCmsSubService<AdvSearchPartApprovalMQMessageBody> {
    private final ProductService productService;

    private final SxProductService sxProductService;

    @Autowired
    public CmsAdvSearchPartApprovalMQJob(ProductService productService, SxProductService sxProductService) {
        this.productService = productService;
        this.sxProductService = sxProductService;
    }

    @Override
    public void onStartup(AdvSearchPartApprovalMQMessageBody messageBody) throws Exception {
        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        for(String code : messageBody.getCodeList()){
            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(messageBody.getChannelId(), code);
            CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getPlatform(messageBody.getCartId());
            if(platform == null || StringUtil.isEmpty(platform.getpNumIId())){
                CmsBtOperationLogModel_Msg fail = new CmsBtOperationLogModel_Msg();
                fail.setSkuCode(code);
                fail.setMsg("numIId 为空 请先上新");
                failList.add(fail);
            }else{
                messageBody.getPlatformWorkloadAttributes().forEach(workload->{
                    PlatformWorkloadAttribute platformWorkloadAttribute = PlatformWorkloadAttribute.get(workload);
                    if(platformWorkloadAttribute != null){
                        sxProductService.insertPlatformWorkload(messageBody.getChannelId(), messageBody.getCartId(), platformWorkloadAttribute, Collections.singletonList(code), messageBody.getSender());
                    }
                });
            }
        }
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getCodeList().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
