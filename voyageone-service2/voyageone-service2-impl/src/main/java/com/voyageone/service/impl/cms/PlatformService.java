package com.voyageone.service.impl.cms;

import com.voyageone.common.configs.beans.PlatformBean;
import com.voyageone.common.configs.dao.PlatformDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/18 10:45
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Service
public class PlatformService {
    @Resource
    PlatformDao platformDao;

    public List<PlatformBean> getAll() {
        return platformDao.getAll();
    }
}
