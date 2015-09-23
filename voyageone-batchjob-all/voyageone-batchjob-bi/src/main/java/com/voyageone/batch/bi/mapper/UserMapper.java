package com.voyageone.batch.bi.mapper;

import com.voyageone.batch.bi.bean.formbean.FormUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Kylin on 2015/6/11.
 */
@Repository
public interface UserMapper {

    //获得店铺基础信息
   List<FormUser> select_list_vm_shop_user(int iEnable);
}
