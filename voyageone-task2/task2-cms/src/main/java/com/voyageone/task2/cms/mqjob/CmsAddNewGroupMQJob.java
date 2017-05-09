package com.voyageone.task2.cms.mqjob;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsAddNewGroupMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by james on 2017/3/9.
 */
@Service
@VOSubRabbitListener
public class CmsAddNewGroupMQJob extends TBaseMQCmsSubService<CmsAddNewGroupMQMessageBody> {

    private final Integer PAGE_SIZE = 100;
    @Autowired
    ProductService productService;
    @Autowired
    ProductGroupService productGroupService;
    @Autowired
    CmsBtProductGroupDao cmsBtProductGroupDao;

    @Override
    public void onStartup(CmsAddNewGroupMQMessageBody messageBody) throws Exception {

        String productCode = messageBody.getCode();
        String channelId = messageBody.getChannelId();
        Integer cartId = messageBody.getCartId();
        Boolean isSingle = messageBody.getIsSingle();

        long sumCnt;
        if (!StringUtils.isEmpty(productCode))
            sumCnt = productService.countByQuery("{\"common.fields.code\": \"" + productCode + "\"}", null, channelId);
        else
            sumCnt = productService.countByQuery("{}", null, channelId);

        long pageCnt = sumCnt / PAGE_SIZE + (sumCnt % PAGE_SIZE > 0 ? 1 : 0);
        BulkJongoUpdateList grpBulkList = new BulkJongoUpdateList(1000, cmsBtProductGroupDao, channelId);

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
                CmsBtProductModel product = cmsBtProductModels.get(i);

                String code = product.getCommon().getFields().getCode();
                String mainProductCode = product.getPlatform(cartId).getMainProductCode();

                // 判断该code对应的group是否存在
                JongoQuery query1 = new JongoQuery();
                query1.setQuery("{\"productCodes\": #, \"cartId\": #}");
                query1.setParameters(code, cartId);
                CmsBtProductGroupModel mainGroupCode1 = productGroupService.getProductGroupByQuery(channelId, query1);

                // 如果不存在, 获取该code对应的mainProductCode的group
                if (mainGroupCode1 == null) {
                    JongoQuery query2 = new JongoQuery();
                    query2.setQuery("{\"mainProductCode\": #, \"cartId\": #}");
                    query2.setParameters(mainProductCode, cartId);
                    CmsBtProductGroupModel mainGroupCode2 = productGroupService.getProductGroupByQuery(channelId, query2);

                    // 该code对应的mainProductCode的group存在,但是productCodes中无该code
                    if (mainGroupCode2 != null && !mainGroupCode2.getProductCodes().contains(code)) {

                        // 将该code加入到该group中
                        JongoUpdate grpUpdObj = new JongoUpdate();
                        grpUpdObj.setQuery("{\"mainProductCode\": #, \"cartId\": #}");
                        grpUpdObj.setQueryParameters(mainProductCode, cartId);
                        grpUpdObj.setUpdate("{$addToSet: {\"productCodes\": #}}");
                        grpUpdObj.setUpdateParameters(code);

                        BulkWriteResult rs = grpBulkList.addBulkJongo(grpUpdObj);
                        if (rs != null) {
                            $debug(String.format("商品(group表) channelId=%s 执行结果=%s", channelId, rs.toString()));
                        }
                    }
                    // 新建一个group
                    else if (mainGroupCode2 == null) {
                        CmsBtProductGroupModel group = productGroupService.createNewGroup(channelId, cartId, mainProductCode, isSingle);
                        group.getProductCodes().add(code);
                        group.setCreater("backDoorController");
                        group.setModifier("backDoorController");
                        cmsBtProductGroupDao.insert(group);
                    }
                }
            }

            BulkWriteResult rs = grpBulkList.execute();
            if (rs != null) {
                $debug(String.format("商品(group表) channelId=%s 结果=%s", channelId, rs.toString()));
            }
        }

    }
}
