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
    // selType=1:  自定义搜索条件用
    // selType=2:  设置自定义显示列弹出画面用
    // selType=3:  排序条件
    public List<Map<String, Object>> getCustColumns(int selType) {
        return cmsMtCommonPropDaoExt.selectCustColumns(selType);
    }

    public List<Map<String, Object>> getCustColumnsByUserId(int userId) {
        return cmsMtCommonPropDaoExt.selectUserCustColumns(userId);
    }

    // 取得用户自定义显示列设置
    public List<Map<String, Object>> getUserCustColumnsSalesType(int userId) {
        return cmsMtCommonPropDaoExt.selectUserCustColumnsSalesType(userId);
    }

    @VOTransactional
    public int addUserCustColumn(int userId, String userName, String param1, String param2) {
        return cmsMtCommonPropDaoExt.insertUserCustColumns(userId, userName, param1, param2);
    }

    @VOTransactional
    public int saveUserCustColumn(int userId, String userName, String param1, String param2) {
        return cmsMtCommonPropDaoExt.updateUserCustColumns(userId, userName, param1, param2);
    }

    @VOTransactional
    public int addUserCustColumnsSalesType(int userId, String userName, String param1) {
        return cmsMtCommonPropDaoExt.insertUserCustColumnsSalesType(userId, userName, param1);
    }

    @VOTransactional
    public int saveUserCustColumnsSalesType(int userId, String userName, String param1) {
        return cmsMtCommonPropDaoExt.updateUserCustColumnsSalesType(userId, userName, param1);
    }

}
