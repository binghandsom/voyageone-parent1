package com.voyageone.service.daoext.cms;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/7/12.
 */
@Repository
public interface CmsBtPromotionDaoExtCamel {
    List selectPage(Map<String, Object> param);

    long selectCount(Map<String, Object> param);
}
