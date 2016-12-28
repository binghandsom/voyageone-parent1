package com.voyageone.components.rabbitmq.service;

import com.voyageone.components.rabbitmq.bean.IMQMessageBody;

import java.util.Date;

/**
 * Created by dell on 2016/12/26.
 */
public interface IMQJobLog {
    void log(String jobName,IMQMessageBody messageBody, Exception ex, Date beginDate, Date endDate);
}