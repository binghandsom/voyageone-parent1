package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtProductStatusHistoryModel_Mysql;
import com.voyageone.service.model.util.MapModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author sunpeitao
 * @version 2.3.0
 * @since 2.3.0
 */
@Repository
public interface CmsBtProductStatusHistoryDaoExt {

    void insertList(@Param("list") List<CmsBtProductStatusHistoryModel_Mysql> list);

    List<MapModel> selectPage(Map<String, Object> param);

    List<MapModel> selectPage4Transfer(Map<String, Object> param);

    long selectCount(Map<String, Object> param);
}
