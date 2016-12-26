package com.voyageone.task2.base;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.service.IMQJobLog;
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

    @Autowired(required=false)
    IMQJobLog logService;

    @Override
    public void startup(TMQMessageBody messageBody) throws Exception {

        Date beginDate = new Date();

        try {
            onStartup(messageBody);
        }
        //特殊异常 抛出处理
        catch (Exception e) {
            Date endDate = new Date();
            e.getMessage();
            e.getStackTrace();
            if(logService!=null) {
                logService.log(messageBody, e, beginDate, new Date());
            }
            //e.toString();
            /*
  1.基类异常处理
  2. cfg_name = 'run_flg'  cfg_val2=1 全量记录mq处理日志   默认只记录异常日志
  * */
        }
    }
}
