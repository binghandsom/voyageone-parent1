package com.voyageone.task2.core.mapper;

import com.voyageone.task2.core.modelbean.MonitorBean;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorMapper {
    MonitorBean getSlaveStatus();
}
