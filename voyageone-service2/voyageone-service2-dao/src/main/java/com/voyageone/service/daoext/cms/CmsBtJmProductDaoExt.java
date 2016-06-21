package com.voyageone.service.daoext.cms;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtJmProductDaoExt {
    public CmsBtJmProductModel selectByProductCodeChannelId(@Param("productCode") String productCode, @Param("channelId") String channelId);

    public List<CmsBtJmProductModel> selectByProductCodeListChannelId(@Param("productCodes") List<String> productCodes, @Param("channelId") String channelId);
}
