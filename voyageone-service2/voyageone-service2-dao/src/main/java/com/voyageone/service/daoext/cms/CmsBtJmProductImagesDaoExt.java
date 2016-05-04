package com.voyageone.service.daoext.cms;
import com.voyageone.service.model.cms.CmsBtJmProductImagesModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CmsBtJmProductImagesDaoExt {
    public CmsBtJmProductImagesModel getByKey(@Param("channelId") String channelId, @Param("productCode") String productCode, @Param("imageType") int imageType, @Param("imageIndex") int imageIndex);
    public List<CmsBtJmProductImagesModel> getListByPromotionId(int promotionId);
}
