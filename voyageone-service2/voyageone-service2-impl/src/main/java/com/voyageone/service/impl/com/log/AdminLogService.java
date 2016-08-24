package com.voyageone.service.impl.com.log;


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.security.dao.ComLogDao;
import com.voyageone.security.model.ComLogModel;
import com.voyageone.service.bean.com.AdminRoleBean;
import com.voyageone.service.daoext.core.AdminLogDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-23.
 */
@Service
public class AdminLogService  extends BaseService {

    @Autowired
    AdminLogDaoExt adminLogDaoExt;

    @Autowired
    ComLogDao comLogDao;


    public PageModel<ComLogModel> searchLog(Integer pageNum, Integer pageSize) {
        return  searchLog(new ComLogModel(),  pageNum,  pageSize);
    }

    public PageModel<ComLogModel> searchLog(ComLogModel params, Integer pageNum, Integer pageSize) {

        PageModel<ComLogModel> pageModel = new PageModel<>();

        // 判断查询结果是否分页
        boolean needPage = false;
        Map<String,Object> newMap = new HashMap<>();
        BeanUtils.copyProperties(params, newMap);

        if (pageNum != null && pageSize != null) {
            needPage = true;
            pageModel.setCount(adminLogDaoExt.selectCount(params));
            newMap = MySqlPageHelper.build(newMap).page(pageNum).limit(pageSize).addSort("created", Order.Direction.DESC).toMap();
        }
        else
        {
            newMap = MySqlPageHelper.build(newMap).addSort("created", Order.Direction.DESC).toMap();
        }

        List<ComLogModel> list = adminLogDaoExt.selectList(newMap);
        if (!needPage) {
            pageModel.setCount(list.size());
        }

        pageModel.setResult(list);
        return pageModel;
    }


    public ComLogModel  getLog(int id) {
        return comLogDao.select(id);
    }


}
