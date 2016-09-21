package com.voyageone.service.impl.vms;

import com.voyageone.service.dao.vms.VmsBtClientAccessLogDao;
import com.voyageone.service.model.vms.VmsBtClientAccessLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ApiClientAccessLogService
 *
 * @author chuanyu.liang 16/9/8
 * @version 2.0.0
 */
@Service
public class ApiClientAccessLogService {

    @Autowired
    private VmsBtClientAccessLogDao vmsBtClientAccessLogDao;

    public void saveLog(VmsBtClientAccessLogModel model) {
        vmsBtClientAccessLogDao.insert(model);
    }
}
