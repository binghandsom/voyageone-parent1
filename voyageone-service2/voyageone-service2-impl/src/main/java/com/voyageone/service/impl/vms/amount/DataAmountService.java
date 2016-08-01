package com.voyageone.service.impl.vms.amount;

import com.voyageone.service.dao.vms.VmsBtDataAmountDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.vms.VmsBtDataAmountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataAmountService
 *
 * @author jeff.duan 16/7/7
 * @version 1.0.0
 */
@Service
public class DataAmountService extends BaseService {

    @Autowired
    private VmsBtDataAmountDao vmsBtDataAmountDao;

    /**
     * 取得统计数据
     * @param channelId 渠道id
     * @return 统计数据
     */
    public List<VmsBtDataAmountModel> getDataAmountInfo(String  channelId) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        return vmsBtDataAmountDao.selectList(param);

    }
}
