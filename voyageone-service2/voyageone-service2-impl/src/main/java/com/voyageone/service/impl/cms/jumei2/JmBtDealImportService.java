package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JMProductDealBean;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtDealImportModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtProductModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtSkuModel;
import com.voyageone.service.daoext.cms.JmBtDealImportDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JmBtDealImportService {
    @Autowired
    JmBtDealImportDaoExt daoExtJmBtDealImport;

    public void importJM(String channelId) {
        List<JMProductDealBean> listJMProductDealBean = daoExtJmBtDealImport.selectListProductDealByChannelId(channelId);
        for (JMProductDealBean productDeal : listJMProductDealBean) {
            importProduct(productDeal);
        }
    }

    private void importProduct(JMProductDealBean productDeal) {
        JmBtDealImportModel modelJmBtDealImport = daoExtJmBtDealImport.selectJmBtDealImportModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());

        JmBtProductModel modelJmBtProduct = daoExtJmBtDealImport.selectJmBtProductModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());

        JmBtSkuModel modelJmBtSku = daoExtJmBtDealImport.selectJmBtSkuModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());
    }
}
