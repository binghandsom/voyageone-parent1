package com.voyageone.task2.cms.service.platform;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtPlatformActiveLogDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtPlatformActiveLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 记录上下架操作历史(新增记录)
 * @author jiangjusheng on 2016/07/11
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_PlatformActiveLogJob)
public class CmsPlatformActiveLogService extends BaseMQCmsService {

    @Autowired
    CmsBtPlatformActiveLogDao platformActiveLogDao;
    @Autowired
    ProductService productService;
    @Autowired
    ProductGroupService productGroupService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        $info("CmsPlatformActiceLogService start 参数 " + JacksonUtil.bean2Json(messageMap));
        String channelId = StringUtils.trimToNull((String) messageMap.get("channelId"));
        String prodCode = StringUtils.trimToNull((String) messageMap.get("prodCode"));
        if (channelId == null || prodCode == null
                || StringUtils.isEmpty((String) messageMap.get("activeStatus")) || StringUtils.isEmpty((String) messageMap.get("creater"))) {
            $error("CmsPlatformActiceLogService 缺少参数");
            return;
        }

        List<Integer> cartIdList = (List<Integer>) messageMap.get("cartIdList");
        if (cartIdList == null || cartIdList.isEmpty()) {
            $error("CmsPlatformActiceLogService 缺少cartid参数");
            return;
        }

        for (Integer cartId : cartIdList) {
            // 取得产品信息
            JomgoQuery queryObj = new JomgoQuery();
            queryObj.setQuery("{'common.fields.code':#,'platforms.P#':{$exists:true}}");
            queryObj.setParameters(prodCode, cartId);
            queryObj.setProjectionExt("platforms.P" + cartId + ".pStatus");
            CmsBtProductModel prodObj = productService.getProductByCondition(channelId, queryObj);
            if (prodObj == null) {
                $error("CmsPlatformActiceLogService 产品不存在(或没有platforms数据) 参数=" + messageMap.toString());
                continue;
            }

            // 取得group信息
            queryObj = new JomgoQuery();
            queryObj.setQuery("{'productCodes':#,'cartId':#}");
            queryObj.setParameters(prodCode, cartId);
            queryObj.setProjectionExt("mainProductCode", "groupId", "numIId");
            CmsBtProductGroupModel grpObj = productGroupService.getProductGroupByQuery(channelId, queryObj);
            if (grpObj == null) {
                $error("CmsPlatformActiceLogService 产品对应的group不存在 参数=" + messageMap.toString());
                continue;
            }

            CmsBtPlatformActiveLogModel model = new CmsBtPlatformActiveLogModel();
            model.setCartId(cartId);
            model.setChannelId(channelId);
            model.setProdCode(prodCode);
            model.setActiveStatus((String) messageMap.get("activeStatus"));
            CmsConstants.PlatformStatus pStatus = prodObj.getPlatformNotNull(cartId).getpStatus();
            if (pStatus != null) {
                model.setPlatformStatus(pStatus.name());
            }

            model.setComment((String) messageMap.get("comment"));
            model.setGroupId(grpObj.getGroupId());
            model.setMainProdCode(grpObj.getMainProductCode());
            model.setNumIId(grpObj.getNumIId());
            model.setResult("0");
            model.setCreater((String) messageMap.get("creater"));
            model.setCreated(DateTimeUtil.getNow());
            model.setModified("");
            model.setModifier("");

            WriteResult rs = platformActiveLogDao.insert(model);
            $debug("CmsPlatformActiceLogService 更新结果 " + rs.toString());
        }
    }

}
