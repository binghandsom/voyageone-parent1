package com.voyageone.common.configs.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.configs.beans.StoreConfigBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jack on 4/17/2015.
 */
@Repository
public class StoreConfigDao extends BaseDao {

    /**
     * 获取所有仓库数据
     */
    public List<StoreBean> getAll() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "wms_mt_store_getAll");
    }

    /**
     * 根据所有仓库配置数据
     */
    public List<StoreConfigBean> getAllConfigs() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "ct_store_config_getAll");
    }
}
