package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsMtMasterInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtMasterInfoDaoExt {
    public CmsMtMasterInfoModel getByKey(@Param("platformId") int platformId, @Param("channelId") String channelId, @Param("brandName") String brandName, @Param("productType") String productType, @Param("dataType") int dataType);
    public CmsMtMasterInfoModel getByKeySizeType(@Param("platformId") int platformId, @Param("channelId") String channelId, @Param("brandName") String brandName, @Param("productType") String productType, @Param("dataType") int dataType, String sizeType);
}
