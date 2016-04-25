package com.voyageone.service.impl.cms;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.daoext.cms.CmsMtCommonPropDaoExt;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * CommonProp Service
 *
 * @author chuanyu.liang 2016/01/28
 * @version 2.0.1
 * @since 2.0.0
 */

@Service
public class CommonPropService extends BaseService {

    @Autowired
    private CmsMtCommonPropDaoExt cmsMtCommonPropDaoExt;

    // 取得自定义显示列设置
    public List<Map<String, Object>> getCustColumns() {
        return cmsMtCommonPropDaoExt.selectCustColumns();
    }

    public List<Map<String, Object>> getCustColumnsByUserId(int userId) {
        return cmsMtCommonPropDaoExt.selectUserCustColumns(userId);
    }

    @VOTransactional
    public int addUserCustColumn(int userId, String userName, String param1, String param2) {
        return cmsMtCommonPropDaoExt.insertUserCustColumns(userId, userName, param1, param2);
    }

    @VOTransactional
    public int saveUserCustColumn(int userId, String userName, String param1, String param2) {
        return cmsMtCommonPropDaoExt.updateUserCustColumns(userId, userName, param1, param2);
    }

}
