package com.voyageone.service.daoext.cms;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JMProductDealBean;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtDealImportModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtProductModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtSkuModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  JmBtDealImportDaoExt {
    List<JMProductDealBean> selectListProductDealByChannelId(String channelId);

    JmBtDealImportModel selectJmBtDealImportModel(@Param("channelId") String channelId, @Param("dealId") String dealId, @Param("productCode") String productCode);

    JmBtProductModel selectJmBtProductModel(@Param("channelId") String channelId, @Param("dealId") String dealId, @Param("productCode") String productCode);

    JmBtSkuModel selectJmBtSkuModel(@Param("channelId") String channelId, @Param("dealId") String dealId, @Param("productCode") String productCode);
}
