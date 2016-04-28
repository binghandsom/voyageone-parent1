package com.voyageone.service.daoext.cms;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtJmProductDaoExt {
    public CmsBtJmProductModel getByProductCodeChannelId(@Param("productCode") String productCode,@Param("channelId") String channelId);
}
