package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsMtMasterInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtMasterInfoDaoExt {
    public CmsMtMasterInfoModel selectByKey(@Param("platformId") int platformId, @Param("channelId") String channelId, @Param("brandName") String brandName, @Param("productType") String productType, @Param("dataType") int dataType);
    public CmsMtMasterInfoModel selectByKeySizeType(@Param("platformId") int platformId, @Param("channelId") String channelId, @Param("brandName") String brandName, @Param("productType") String productType, @Param("dataType") int dataType, @Param("sizeType") String sizeType);
    public  Integer selectCountByWhere(Map<String, Object> map);
    public  List selectListByWhere(Map<String, Object> map);
    public List<CmsMtMasterInfoModel> selectListJMNewImageInfo(Map<String, Object> map);
}
