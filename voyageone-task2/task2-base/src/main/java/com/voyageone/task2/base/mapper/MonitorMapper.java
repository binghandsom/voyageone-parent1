package com.voyageone.task2.base.mapper;

import com.voyageone.task2.base.modelbean.MonitorBean;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorMapper {
    MonitorBean getSlaveStatus();
}
