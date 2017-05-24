package com.voyageone.service.daoext.cms;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtJmProductDaoExt {
    CmsBtJmProductModel selectByProductCodeChannelId(@Param("productCode") String productCode, @Param("orgChannelId") String channelId);

    List<CmsBtJmProductModel> selectByProductCodeListChannelId(@Param("productCodes") List<String> productCodes, @Param("orgChannelId") String channelId);

    Boolean existsCode(@Param("productCode") String productCode, @Param("orgChannelId") String channelId);
}
