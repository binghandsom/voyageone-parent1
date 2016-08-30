package com.voyageone.task2.cms.service.product;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.bi.BiVtSalesProductExt;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从bi基础数据表取得产品的bi信息，并保存 (浏览量 访客数 加购件数 收藏人数)
 *
 * @author jiangjusheng on 2016/08/30
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_AdvSearch_GetBIDataJob)
public class CmsProcductBIDataService extends BaseMQCmsService {

    @Autowired
    private BiVtSalesProductExt biDataDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    // 每次查询取得的最大件数
    private final static int PAGE_LIMIT = 1000;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        $info("CmsProcductBIDataService start... 参数" + JacksonUtil.bean2Json(messageMap));
        String channelId = StringUtils.trimToNull((String) messageMap.get("channelId"));
        Integer cartId = (Integer) messageMap.get("cartId");
        if (channelId == null || cartId == null) {
            $error("CmsProcductBIDataService 缺少参数");
            return;
        }

        Map<String, Object> sqlParams = new HashMap<>(6);
        Date lastDay = DateTimeUtil.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1);
        int lastDayVal = NumberUtils.toInt(DateTimeUtil.format(lastDay, DateTimeUtil.DATE_TIME_FORMAT_3));
        sqlParams.put("endDate", lastDayVal);
        sqlParams.put("channelId", channelId);
        sqlParams.put("cartId", cartId);
        sqlParams.put("oLimit", PAGE_LIMIT);

        BulkJongoUpdateList bulkUpdList = new BulkJongoUpdateList(PAGE_LIMIT, cmsBtProductDao, channelId);
        long oIdx = 0;
        List<Map<String, Object>> biData = null;
        BulkWriteResult rs = null;

        // 先统计昨天的数据
        sqlParams.put("staDate", lastDayVal);
        setBiData(bulkUpdList, sqlParams, 1);

        // 再统计最近7天的数据
        lastDay = DateTimeUtil.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -7);
        int staDayVal = NumberUtils.toInt(DateTimeUtil.format(lastDay, DateTimeUtil.DATE_TIME_FORMAT_3));
        sqlParams.put("staDate", staDayVal);
        setBiData(bulkUpdList, sqlParams, 7);

        // 再统计最近30天的数据
        lastDay = DateTimeUtil.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -30);
        staDayVal = NumberUtils.toInt(DateTimeUtil.format(lastDay, DateTimeUtil.DATE_TIME_FORMAT_3));
        sqlParams.put("staDate", staDayVal);
        setBiData(bulkUpdList, sqlParams, 30);

        $info("CmsProcductBIDataService 结束");
    }

    /*
     * 查询并保存BI数据
     */
    private void setBiData(BulkJongoUpdateList bulkUpdList, Map<String, Object> sqlParams, int opeType) {
        long oIdx = 0;
        List<Map<String, Object>> biData = null;
        BulkWriteResult rs = null;
        Integer cartId = (Integer) sqlParams.get("cartId");

        do {
            sqlParams.put("oIdx", oIdx * PAGE_LIMIT);
            biData = biDataDao.selectList(sqlParams);
            if (biData == null || biData.isEmpty()) {
                // 没有销量数据
                $warn("CmsProcductBIDataService 本店铺无BI数据 sqlParams=" + sqlParams.toString());
                break;
            }
            oIdx ++;

            for (Map orderObj : biData) {
                String numIid = (String) orderObj.get("num_iid");

                JongoUpdate updObj = new JongoUpdate();
                updObj.setQuery("{'platforms.P#.pNumIId':#,'platforms.P#.status':'Approved'}");
                updObj.setQueryParameters(cartId, numIid, cartId);
                updObj.setUpdate("{$set:{'bi.sum#.pv.cartId#':#,'bi.sum#.uv.cartId#':#, 'bi.sum#.gwc.cartId#':#,'bi.sum#.scs.cartId#':#, 'modified':#,'modifier':#}}");
                updObj.setUpdateParameters(opeType, cartId, orderObj.get("pv"), opeType, cartId, orderObj.get("uv"), opeType, cartId, orderObj.get("cartNums"), opeType, cartId, orderObj.get("collNums"), DateTimeUtil.getNowTimeStamp(), MqRoutingKey.CMS_TASK_AdvSearch_GetBIDataJob);
                // 批量更新
                rs = bulkUpdList.addBulkJongo(updObj);
                if (rs != null) {
                    $debug(String.format("更新product sqlParams=%s 执行结果1=%s", sqlParams.toString(), rs.toString()));
                }
            }
        } while (biData.size() == PAGE_LIMIT);

        rs = bulkUpdList.execute();
        if (rs != null) {
            $debug(String.format("更新product sqlParams=%s 执行结果2=%s", sqlParams.toString(), rs.toString()));
        }
    }
}
