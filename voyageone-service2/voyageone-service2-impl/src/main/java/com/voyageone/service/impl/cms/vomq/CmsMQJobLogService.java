package com.voyageone.service.impl.cms.vomq;

import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.service.IMQJobLog;
import com.voyageone.service.impl.BaseService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dell on 2016/12/26.
 */
@Service
public class CmsMQJobLogService  extends BaseService implements IMQJobLog {

    public void log(IMQMessageBody messageBody, Exception ex, Date beginDate, Date endDate) {

    }

}
