package com.voyageone.service.impl.com.log;


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.service.dao.user.ComLogDao;
import com.voyageone.service.model.user.ComLogModel;
import com.voyageone.service.daoext.core.AdminLogDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.bean.com.PaginationResultBean;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-23.
 */
@Service
public class AdminLogService extends BaseService {

    @Autowired
    AdminLogDaoExt adminLogDaoExt;

    @Autowired
    ComLogDao comLogDao;


    public PaginationResultBean<ComLogModel> searchLog(Integer pageNum, Integer pageSize) {
        return searchLog(new ComLogModel(), null, null, pageNum, pageSize);
    }

    public PaginationResultBean<ComLogModel> searchLog(ComLogModel params, Long startTime, Long endTime, Integer pageNum, Integer pageSize) {

        PaginationResultBean<ComLogModel> paginationResultBean = new PaginationResultBean<>();


        // 判断查询结果是否分页
        boolean needPage = false;
        Map beanMap = new BeanMap(params);
        Map<String, Object> newMap = new HashMap<>();

        Date start = null;
        Date end = null;

        if (startTime != null) {
            start = new Date(startTime);
        }

        if (endTime != null) {
            end = new Date(endTime);
        }

        newMap.put("startTime", start);
        newMap.put("endTime", end);
        newMap.putAll(beanMap);

        if (pageNum != null && pageSize != null) {
            needPage = true;
            paginationResultBean.setCount(adminLogDaoExt.selectCount(newMap));
            newMap = MySqlPageHelper.build(newMap).page(pageNum).limit(pageSize).addSort("created", Order.Direction.DESC).toMap();
        } else {
            newMap = MySqlPageHelper.build(newMap).addSort("created", Order.Direction.DESC).toMap();
        }


        List<ComLogModel> list = adminLogDaoExt.selectList(newMap);
        if (!needPage) {
            paginationResultBean.setCount(list.size());
        }

        paginationResultBean.setResult(list);
        return paginationResultBean;
    }


    public ComLogModel getLog(int id) {
        return comLogDao.select(id);
    }


}
