package com.voyageone.service.impl.vms.order;

import com.voyageone.service.dao.vms.VmsBtOrderDetailDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * service about product's status
 * Created by vantis on 16-7-6.
 */
@Service
public class VmsOrderDetailService extends BaseService {

    @Autowired
    VmsBtOrderDetailDao vmsBtOrderDetailDao;

}
