package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.excel.ExcelException;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionExportTask3Service;
import com.voyageone.service.impl.cms.vomessage.body.JmExportMQMessageBody;
import com.voyageone.task2.base.TBaseMQCmsService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * JmBtPromotion ImportJob Service  聚美导出文件生成
 *
 * @author peitao 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener()
public  class CmsJmPromotionExportMQJob extends TBaseMQCmsService<JmExportMQMessageBody> {
//前缀:Cms 后缀:MQJob
    @Autowired
    private CmsBtJmPromotionExportTask3Service service;

/*
  1.基类异常处理
  2. cfg_name = 'run_flg'  cfg_val2=1 全量记录mq处理日志   默认只记录异常日志
  3.
  * */
    @Override
    public void onStartup(JmExportMQMessageBody messageBody) throws Exception {
        $info("JmBtPromotionExportJobService", "begin");
        $debug("JmBtPromotionExportJobService收到消息：" + JacksonUtil.bean2Json(messageBody));

        // 获取Mq的配置信息
        TaskControlBean taskControlBean = getTaskControlBean(taskControlList, "cms.jm.export.path");
        if (taskControlBean == null) {
            // todo 需要插入MQ/JOB处理结果表
            $error("JmBtPromotionExportJobService", "请配置cms.jm.export.path");
            return;
        }

        String exportPath = taskControlBean.getCfg_val1();
        FileUtils.mkdirPath(exportPath);
        int id = messageBody.getJmBtPromotionExportTaskId();

        // 生成excel文件
        service.export(id, exportPath);
        // todo 插入
        $info("JmBtPromotionExportJobService", "end");
    }
}