package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.JmBtApiLogModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JmBtApiLogDaoExt {
    public List<JmBtApiLogModel>  getPage();
    }
