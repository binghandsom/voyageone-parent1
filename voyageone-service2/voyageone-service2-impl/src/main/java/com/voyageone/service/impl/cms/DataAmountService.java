package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtDataAmountDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtDataAmountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 16/4/28
 */
@Service
public class DataAmountService extends BaseService {

    @Autowired
    private CmsBtDataAmountDao cmsBtDataAmountDao;

    /**
     * 插入统计数据表
     * @param channelId
     * @param amountName
     * @param amountVal
     * @param comment
     * @param modifier
     * @return
     */
    public int updateWithInsert(String channelId, String amountName, String amountVal, String comment, String modifier) {
        boolean upFlg = true;
        Map<String, Object> bean = new HashMap<>();
        bean.put("channelId", channelId);
        bean.put("amountName", amountName);
        CmsBtDataAmountModel result = cmsBtDataAmountDao.selectOne(bean);
        if (result == null) {
            result = new CmsBtDataAmountModel();
            result.setChannelId(channelId);
            result.setAmountName(amountName);
            result.setAmountVal(amountVal);
            result.setComment(comment);
            result.setCreater(modifier);
            result.setModifier(modifier);
            upFlg = false;
        } else {
            result.setAmountVal(amountVal);
            result.setComment(comment);
            result.setModifier(modifier);
        }

        if(upFlg)
            return cmsBtDataAmountDao.update(result);
        else {
            return cmsBtDataAmountDao.insert(result);
        }

    }
}
