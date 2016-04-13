package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsMtCommonPropDao;
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
    private CmsMtCommonPropDao cmsMtCommonPropDao;

    // 取得自定义显示列设置
    public List<Map<String, Object>> getCustColumns() {
        return cmsMtCommonPropDao.selectCustColumns();
    }

    public List<Map<String, Object>> getCustColumnsByUserId(int userId) {
        return cmsMtCommonPropDao.selectUserCustColumns(userId);
    }

    public int addUserCustColumn(int userId, String userName, String param1, String param2) {
        return cmsMtCommonPropDao.insertUserCustColumns(userId, userName, param1, param2);
    }

    public int saveUserCustColumn(int userId, String userName, String param1, String param2) {
        return cmsMtCommonPropDao.updateUserCustColumns(userId, userName, param1, param2);
    }

}
