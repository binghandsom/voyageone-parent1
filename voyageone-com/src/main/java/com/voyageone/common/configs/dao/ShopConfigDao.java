package com.voyageone.common.configs.dao;


import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.ShopConfigBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jack on 4/17/2015.
 */
@Repository
public class ShopConfigDao extends BaseDao  {

	/**
	 * 获取所有店铺配置数据
	 */
    public List<ShopConfigBean> getAll() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "tm_channel_shop_config_getAll");
    }
}
