package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CommonPropActionDefBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtCommonPropModel;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/12/7.
 */
@Repository
public class CmsMtCommonPropDaoExt extends ServiceBaseDao {
    public List<CmsMtCommonPropModel> selectCommonProp() {
        return selectList("select_cms_mt_common_prop_all");
    }

    public List<CommonPropActionDefBean> selectActionModelList() {
        return selectList("select_cms_mt_common_prop_actionDefAll");
    }

    // 取得自定义显示列设置
    public List<Map<String, Object>> selectCustColumns(int selType) {
        return selectList("select_cms_mt_common_prop_cust_cols", parameters("selType", selType));
    }

    // 取得用户自定义显示列设置
    public Map<String, Object> selectUserCustColumns(int userId, String cfgName) {
        return selectOne("select_synship_ct_user_config_cust_cols", parameters("userId", userId, "cfgName", cfgName));
    }

    // 增加用户自定义显示列设置
    public int insertUserCustColumns(int userId, String userName, String cfgName, String param1, String param2) {
        return insert("insert_synship_ct_user_config_cust_cols", parameters("userId", userId, "userName", userName, "cfgName", cfgName, "cfg_val1", param1, "cfg_val2", param2));
    }

    // 保存用户自定义显示列设置
    public int updateUserCustColumns(int userId, String userName, String cfgName, String param1, String param2) {
        return update("update_synship_ct_user_config_cust_cols", parameters("userId", userId, "userName", userName, "cfgName", cfgName, "cfg_val1", param1, "cfg_val2", param2));
    }

    /**
     * 删除用户自定列配置
     *
     * @param userId  用户ID
     * @param cfgName 配置名称
     */
    public int deleteUserCustColumns(int userId, String cfgName) {
        return delete("delete_synship_ct_user_config_cust_cols", parameters("userId", userId, "cfgName", cfgName));
    }

    /**
     * 根据自定义列配置获取多配置项
     *
     * @param userId  用户ID
     * @param cfgName 配置名称
     */
    public List<Map<String, Object>> selectMultiUserCustColumns(int userId, String cfgName) {
        return selectList("select_multi_synship_ct_user_config_cust_cols", parameters("userId", userId, "cfgName", cfgName));
    }

    /**
     * 获取美国CMS自定义列Platform Sale所有平台(所有用户勾选)及其日期取件
     *
     * @param cfgName 配置'usa_cms_cust_col_platform_sale'
     * @return KV:cfg_val2 和 cfg_val1
     */
    public List<Map<String, Object>> getUsaPlatformSaleCarts(String cfgName) {
        return selectList("select_synship_ct_user_config_platform_sale_carts", parameters("cfgName", cfgName));
    }

    /**
     * 把美国CMS PlatformSale所有的日期取值统一设置为一致的时间
     *
     * @param cfgName  usa_cms_cust_col_platform_sale
     * @param cfgVal1  {"beginTime":"2017-08-01","endTime":"2017-08-02"}格式
     * @param modifier 更新人
     */
    public int updateUsaPlatformSaleTime(String cfgName, String cfgVal1, String modifier) {
        return update("update_synship_ct_user_config_platform_sale_time", parameters("cfgName", cfgName, "cfgVal1", cfgVal1, "modifier", modifier));
    }
}
