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

    // 取得用户自定义显示列设置
    public Map<String, Object> getCustColumnsByUserId(int userId, String cfgName) {
        return cmsMtCommonPropDaoExt.selectUserCustColumns(userId, cfgName);
    }

    @VOTransactional
    public int addUserCustColumn(int userId, String userName, String cfgName, String param1, String param2) {
        return cmsMtCommonPropDaoExt.insertUserCustColumns(userId, userName, cfgName, param1, param2);
    }

    @VOTransactional
    public int saveUserCustColumn(int userId, String userName, String cfgName, String param1, String param2) {
        return cmsMtCommonPropDaoExt.updateUserCustColumns(userId, userName, cfgName, param1, param2);
    }

    /**
     * 删除用户自定义列配置
     *
     * @param userId  用户ID
     * @param cfgName 配置名称
     */
    public int deleteUserCustColumns(int userId, String cfgName) {
        return cmsMtCommonPropDaoExt.deleteUserCustColumns(userId, cfgName);
    }

    public List<Map<String, Object>> getMultiCustColumnsByUserId(int userId, String cfgName) {
        return cmsMtCommonPropDaoExt.selectMultiUserCustColumns(userId, cfgName);
    }

    /**
     * 获取美国CMS自定义列Platform Sale所有平台(所有用户勾选)及其日期取件
     *
     * @param cfgName 配置'usa_cms_cust_col_platform_sale'
     * @return KV:cfg_val2 和 cfg_val1
     */
    public List<Map<String, Object>> getUsaPlatformSaleCarts(String cfgName) {
        return cmsMtCommonPropDaoExt.getUsaPlatformSaleCarts(cfgName);
    }

    /**
     * 把美国CMS PlatformSale所有的日期取值统一设置为一致的时间
     *
     * @param cfgName  usa_cms_cust_col_platform_sale
     * @param cfgVal1  {"beginTime":"2017-08-01","endTime":"2017-08-02"}格式
     * @param modifier 更新人
     */
    public int updateUsaPlatformSaleTime(String cfgName, String cfgVal1, String modifier) {
        return cmsMtCommonPropDaoExt.updateUsaPlatformSaleTime(cfgName, cfgVal1, modifier);
    }
}
