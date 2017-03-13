package com.voyageone.task2.cms.service.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductErrorDao;
import com.voyageone.service.impl.cms.product.ProductCheckService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 每天check各个channel的product数据是否正确,主要用于检测产品的状态和sku数据
 *
 * @author edward.lin 2017/03/06
 * @version 2.15.0
 */
@Service
public class CmsCheckProductIsRightService extends BaseCronTaskService {

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsCheckProductIsRightJob";
    }

    private final Integer PAGE_SIZE = 100;

    @Autowired
    ProductService productService;
    @Autowired
    ProductCheckService productCheckService;
    @Autowired
    CmsBtProductErrorDao cmsBtProductErrorDao;

    @SuppressWarnings("unchecked")
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 处理开始前,昨天执行的结果清空
        cmsBtProductErrorDao.deleteAll();

        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        for (String channelId : channelIdList) {
            List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");

            long sumCnt = productService.countByQuery("{}", null, channelId);

            long pageCnt = sumCnt / PAGE_SIZE + (sumCnt % PAGE_SIZE > 0 ? 1 : 0);

            for (int pageNum = 1; pageNum <= pageCnt; pageNum++) {
                JongoQuery jongoQuery = new JongoQuery();
                jongoQuery.setQuery("{\"common.fields.code\": #}");
                jongoQuery.setParameters("020-332V116270");

                jongoQuery.setSkip((pageNum - 1) * PAGE_SIZE);
                jongoQuery.setLimit(PAGE_SIZE);
                List<CmsBtProductModel> cmsBtProductModels = productService.getList(channelId, jongoQuery);
                for (int i = 0; i < cmsBtProductModels.size(); i++) {
                    $info(String.format("%d/%d  _id:%s", (pageNum - 1) * PAGE_SIZE + i + 1, sumCnt, cmsBtProductModels.get(i).get_id()));
                    productCheckService.checkProductIsRight(cmsBtProductModels.get(i), cartList);
                }
            }
        }
    }
}
