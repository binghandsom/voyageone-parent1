package com.voyageone.service.impl.com.user;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.service.bean.com.AdminRoleBean;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.daoext.core.AdminRoleDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-18.
 */

@Service
public class AdminRoleService extends BaseService {


    @Autowired
    AdminRoleDaoExt adminRoleDaoExt;

    public PageModel<AdminRoleBean> searchRole(String roleName, Integer roleType, String channelId,
                                               Integer active, Integer storeId, String application, Integer pageNum, Integer pageSize) {


        PageModel<AdminRoleBean> pageModel = new PageModel<>();
        // 设置查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleName", roleName);
        params.put("roleType", roleType);
        params.put("channelId", channelId);
        params.put("storeId", storeId);
        params.put("active", active);
        params.put("application", application);

        // 判断查询结果是否分页
        boolean needPage = false;
        if (pageNum != null && pageSize != null) {
            needPage = true;
            pageModel.setCount(adminRoleDaoExt.selectRoleCount(params));
            params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
        }

        List<AdminRoleBean> list = adminRoleDaoExt.selectRoleByPage(params);
        if(!needPage)
        {
            pageModel.setCount(list.size());
        }
        pageModel.setResult(list);
        return pageModel;

    }
}
