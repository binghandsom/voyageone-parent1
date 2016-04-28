package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsMtMasterInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtMasterInfoDaoExt {
    public CmsMtMasterInfoModel getByKey(@Param("platformId") int platformId, @Param("channelId") String channelId, @Param("brandName") String brandName, @Param("productType") String productType, @Param("dataType") int dataType);
    public CmsMtMasterInfoModel getByKeySizeType(@Param("platformId") int platformId, @Param("channelId") String channelId, @Param("brandName") String brandName, @Param("productType") String productType, @Param("dataType") int dataType, @Param("sizeType") String sizeType);
    public  Integer getCountByWhere(Map<String, Object> map);
    public  List getListByWhere(Map<String, Object> map);
    public List<CmsMtMasterInfoModel> getListJMNewImageInfo(Map<String, Object> map);
}
