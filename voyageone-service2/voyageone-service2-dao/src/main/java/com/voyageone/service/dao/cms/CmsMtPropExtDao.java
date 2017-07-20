package com.voyageone.service.dao.cms;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * USA-CMS 自定义列Common Attributes 扩展Dao
 *
 * @Author rex.wu
 * @Create 2017-07-20 10:38
 */
@Repository
public interface CmsMtPropExtDao {

    List<Map<String,String>> selectByType(int type);

}
