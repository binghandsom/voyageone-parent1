package com.voyageone.task2.base;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.service.IMQJobLog;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
/**
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class TBaseMQCmsService<TMQMessageBody  extends IMQMessageBody> extends TBaseMQAnnoService<TMQMessageBody> {
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return getClass().getSimpleName();
    }

    //是否记录Job日志
    public boolean isMQJobLog() {
      //  cfg_name = 'run_flg'  cfg_val2=1 全量记录mq处理日志   默认只记录异常日志
        String val2 = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.run_flg, "1");
        return "1".equals(val2);
    }
    @Autowired()
    IMQJobLog logService;

    @Override
    public void startup(TMQMessageBody messageBody) throws Exception {
        Date beginDate = new Date();
        try {
            onStartup(messageBody);
            if (isMQJobLog()) {
                log(messageBody, null, beginDate, new Date());
            }
        }
        //特殊异常 抛出处理
        catch (Exception e) {
            log(messageBody, e, beginDate, new Date());
        }
    }

    private void log(IMQMessageBody messageBody, Exception ex, Date beginDate, Date endDate) {
        if (logService != null) {
            logService.log(messageBody, null, beginDate, new Date());
        }
    }
}
