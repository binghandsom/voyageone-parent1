package com.voyageone.service.daoext.jumei;
import com.voyageone.service.model.jumei.CmsBtJmProductModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CmsBtJmProductDaoExt {
    public CmsBtJmProductModel getByProductCodeChannelId(@Param("channelId") String channelId, @Param("productCode") String productCode);
}
