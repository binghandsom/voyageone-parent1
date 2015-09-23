package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsThirdFtpDownloadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 与JCftp服务连接
 * 下载库存，产品，订单文件，解读数据放入临时库文件
 *
 * @author fred
 */
@Component("wmsThirdFtpDownloadFileJobTask")
public class WmsThirdFtpDownloadFileJob extends BaseTaskJob {
    @Autowired
    WmsThirdFtpDownloadFileService wmsThirdFtpDownloadFileService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsThirdFtpDownloadFileService;
    }

}


