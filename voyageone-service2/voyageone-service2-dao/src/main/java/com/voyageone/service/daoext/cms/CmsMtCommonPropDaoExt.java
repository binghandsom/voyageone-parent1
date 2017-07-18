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


}
