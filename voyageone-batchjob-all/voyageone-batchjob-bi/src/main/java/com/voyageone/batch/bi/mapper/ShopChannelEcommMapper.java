package com.voyageone.batch.bi.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.voyageone.batch.bi.bean.modelbean.ShopChannelEcommBean;

/**
 * Created by Kylin on 2015/6/11.
 */
@Repository
public interface ShopChannelEcommMapper {

    //获得店铺基础信息
   List<ShopChannelEcommBean> select_list_vm_shop_driver(int iEnable);
}
