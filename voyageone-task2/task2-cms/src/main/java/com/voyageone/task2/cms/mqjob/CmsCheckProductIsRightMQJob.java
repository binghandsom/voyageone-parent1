package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.dao.cms.mongo.CmsBtProductErrorDao;
import com.voyageone.service.impl.cms.product.ProductCheckService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsCheckProductIsRightMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by james on 2017/3/9.
 */
@Service
@VOSubRabbitListener
public class CmsCheckProductIsRightMQJob extends TBaseMQCmsSubService<CmsCheckProductIsRightMQMessageBody> {

    private final Integer PAGE_SIZE = 100;
    @Autowired
    ProductService productService;
    @Autowired
    ProductCheckService productCheckService;
    @Autowired
    CmsBtProductErrorDao cmsBtProductErrorDao;

    @Override
    public void onStartup(CmsCheckProductIsRightMQMessageBody messageBody) throws Exception {
        // 处理开始前,昨天执行的结果清空
        cmsBtProductErrorDao.deleteAll();

        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(messageBody.getChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");

        String productCode = messageBody.getCode();
        String channelId = messageBody.getChannelId();
        long sumCnt;
        if (!StringUtils.isEmpty(productCode))
            sumCnt = productService.countByQuery("{\"common.fields.code\": \"" + productCode + "\"}", null, channelId);
        else
            sumCnt = productService.countByQuery("{}", null, channelId);

        long pageCnt = sumCnt / PAGE_SIZE + (sumCnt % PAGE_SIZE > 0 ? 1 : 0);

        for (int pageNum = 1; pageNum <= pageCnt; pageNum++) {
            JongoQuery jongoQuery = new JongoQuery();
            jongoQuery.setSkip((pageNum - 1) * PAGE_SIZE);
            jongoQuery.setLimit(PAGE_SIZE);
            if (!StringUtils.isEmpty(productCode)) {
                jongoQuery.setQuery("{\"common.fields.code\": #}");
                jongoQuery.setParameters(productCode);
            }
            List<CmsBtProductModel> cmsBtProductModels = productService.getList(channelId, jongoQuery);
            for (int i = 0; i < cmsBtProductModels.size(); i++) {
                $info(String.format("%d/%d  _id:%s", (pageNum - 1) * PAGE_SIZE + i + 1, sumCnt, cmsBtProductModels.get(i).get_id()));
                productCheckService.checkProductIsRight(cmsBtProductModels.get(i), cartList, getTaskName());
            }
        }
//        }

    }
}
