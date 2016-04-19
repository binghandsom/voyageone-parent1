package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsBtJmSkuModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtJmSkuDaoExt {
    public  CmsBtJmSkuModel getBySkuCodeChannelId(@Param("skuCode") String skuCode,@Param("channelId") String channelId);
    }
