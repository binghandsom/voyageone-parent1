package com.voyageone.batch.bi.mapper;

import com.voyageone.batch.bi.bean.modelbean.ShopBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Kylin on 2015/6/10.
 * vt_sales_shop
 */
@Repository
public interface ShopMapper {

    List<ShopBean> select_list_vm_shop(int iEnable);
}
