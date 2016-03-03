package com.voyageone.batch.core.mapper;

import com.voyageone.batch.core.modelbean.MonitorBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitorMapper {
    MonitorBean getSlaveStatus();
}
