package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsThirdFileDataProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 将下载的JCftp库存文件，进行数据处理
 *
 * @author sky
 * @create 20150714
 */
@Component("wmsThirdFileDataProcessingJobTask")
public class WmsThirdFileDataProcessingJob extends BaseTaskJob{

    @Autowired
    WmsThirdFileDataProcessingService wmsThirdFileDataProcessingService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsThirdFileDataProcessingService;
    }
}
