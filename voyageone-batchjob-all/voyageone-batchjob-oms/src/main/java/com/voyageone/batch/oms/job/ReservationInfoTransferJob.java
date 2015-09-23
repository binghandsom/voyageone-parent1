package com.voyageone.batch.oms.job;


import java.util.List;

import javax.mail.MessagingException;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.oms.service.ReservationInfoTransferService;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.batch.oms.job]  
 * @ClassName    [ReservationInfoTransferJob]   
 * @Description  [定时任务调用JOB]   
 * @Author       [sky]   
 * @CreateDate   [20150415]   
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
public class ReservationInfoTransferJob extends BaseTaskJob {

    @Autowired
    ReservationInfoTransferService reservationInfoTransferSevice;

    @Override
    protected BaseTaskService getTaskService() {
        return reservationInfoTransferSevice;
    }

}

