package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JMProductDealBean;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtDealImportModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtProductModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtSkuModel;
import com.voyageone.service.dao.cms.CmsBtJmProductDao;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.daoext.cms.JmBtDealImportDaoExt;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class JmBtDealImportService {
    @Autowired
    JmBtDealImportDaoExt daoExtJmBtDealImport;
    @Autowired
    CmsBtJmProductDao daoCmsBtJmProduct;
    @Autowired
    CmsBtJmSkuDao daoCmsBtJmSku;
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
        if(modelJmBtProduct==null)//不存在 sql可以查询出来  不用处理
        {
            System.out.println("ChannelId:"+productDeal.getChannelId()+" DealId:"+productDeal.getDealId()+"  code:"+productDeal.getProductCode());
            return;
        }
        CmsBtJmProductModel modelCmsBtJmProduct = new CmsBtJmProductModel();
        modelCmsBtJmProduct.setAddressOfProduce(modelJmBtProduct.getAddressOfProduce());
        modelCmsBtJmProduct.setApplicableCrowd("");
        modelCmsBtJmProduct.setAttribute(modelJmBtProduct.getAttribute());
        modelCmsBtJmProduct.setAvailablePeriod("");
        modelCmsBtJmProduct.setBrandName(modelJmBtProduct.getBrandName());
        modelCmsBtJmProduct.setChannelId(modelJmBtProduct.getChannelId());
        modelCmsBtJmProduct.setColorEn("");
        modelCmsBtJmProduct.setForeignLanguageName(modelJmBtProduct.getForeignLanguageName());
        modelCmsBtJmProduct.setHsCode(modelJmBtProduct.getHsCode());
        modelCmsBtJmProduct.setHsName(modelJmBtProduct.getHsName());
        modelCmsBtJmProduct.setHsUnit(modelJmBtProduct.getHsUnit());
        modelCmsBtJmProduct.setImage1("");
        modelCmsBtJmProduct.setJumeiProductId(modelJmBtProduct.getJumeiProductId());
        modelCmsBtJmProduct.setMaterialCn("");
        modelCmsBtJmProduct.setMaterialEn("");
        modelCmsBtJmProduct.setMsrpRmb(new BigDecimal(0));
        modelCmsBtJmProduct.setMsrpUsd(new BigDecimal(0));
        modelCmsBtJmProduct.setOrigin("");
        modelCmsBtJmProduct.setOriginJmHashId(modelJmBtDealImport.getJumeiHashId());
        modelCmsBtJmProduct.setProductCode(modelJmBtProduct.getProductCode());
        modelCmsBtJmProduct.setProductDesCn(modelJmBtProduct.getProductDes());
        modelCmsBtJmProduct.setProductDesEn("");
        modelCmsBtJmProduct.setJumeiProductId(modelJmBtProduct.getJumeiProductId());
        modelCmsBtJmProduct.setProductLongName(modelJmBtDealImport.getProductLongName());
        modelCmsBtJmProduct.setProductMediumName(modelJmBtDealImport.getProductMediumName());
        modelCmsBtJmProduct.setProductNameCn(modelJmBtProduct.getProductName());
        modelCmsBtJmProduct.setProductShortName(modelJmBtDealImport.getProductShortName());
        modelCmsBtJmProduct.setProductType("");
        modelCmsBtJmProduct.setRetailPrice(new BigDecimal(0));
        modelCmsBtJmProduct.setSalePrice(new BigDecimal(0));
        modelCmsBtJmProduct.setSizeType(modelJmBtProduct.getSizeType());
        modelCmsBtJmProduct.setSearchMetaTextCustom(modelJmBtDealImport.getSearchMetaTextCustom());
        modelCmsBtJmProduct.setSpecialnote(modelJmBtProduct.getSpecialNote());
        modelCmsBtJmProduct.setVoBrandName(modelJmBtProduct.getBrandName());
        modelCmsBtJmProduct.setCreater("system");
        modelCmsBtJmProduct.setCreated(new Date());
        modelCmsBtJmProduct.setModifier("system");
        modelCmsBtJmProduct.setModified(new Date());

        CmsBtJmSkuModel modelCmsBtJmSku = new CmsBtJmSkuModel();
        modelCmsBtJmSku.setChannelId(modelJmBtSku.getChannelId());
        modelCmsBtJmSku.setCmsSize("");
        modelCmsBtJmSku.setFormat("");
        modelCmsBtJmSku.setJmSize(modelJmBtSku.getSize());
        modelCmsBtJmSku.setJmSkuNo(modelJmBtSku.getJumeiSkuNo());
        modelCmsBtJmSku.setJmSpuNo(modelJmBtSku.getJumeiSpuNo());
        modelCmsBtJmSku.setProductCode(modelJmBtSku.getProductCode());
        modelCmsBtJmSku.setSkuCode(modelJmBtSku.getSku());
        modelCmsBtJmSku.setMsrpRmb(new BigDecimal(0));
        modelCmsBtJmSku.setMsrpUsd(new BigDecimal(0));
        modelCmsBtJmSku.setRetailPrice(new BigDecimal(0));
        modelCmsBtJmSku.setSalePrice(new BigDecimal(0));
        modelCmsBtJmSku.setUpc(modelJmBtSku.getUpcCode());
        modelCmsBtJmSku.setModifier("system");
        modelCmsBtJmSku.setModified(new Date());
        modelCmsBtJmSku.setCreated(new Date());
        modelCmsBtJmSku.setCreater("system");
         try {
             daoCmsBtJmProduct.insert(modelCmsBtJmProduct);
             daoCmsBtJmSku.insert(modelCmsBtJmSku);
         }
         catch (org.springframework.dao.DuplicateKeyException ex)//重复 不处理
         {

         }
    }
}
