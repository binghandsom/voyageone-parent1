package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtShelvesProductHistoryModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/21.
 */
@Repository
public interface CmsBtShelvesProductHistoryDaoExt {

    void batchInsert(@Param("shelvesId") Integer shelvesId, @Param("productCodes") List<String> productCodes, @Param("status") Integer status, @Param("modifier") String modifier);
}
