package com.voyageone.batch.cms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.dao.JMUploadProductDao;
import com.voyageone.batch.cms.model.JmBtDealImportModel;
import com.voyageone.batch.cms.model.JmBtProductImportModel;
import com.voyageone.batch.cms.model.JmBtSkuImportModel;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.jumei.Bean.JmProductBean;
import com.voyageone.common.components.jumei.Bean.JmProductBean_DealInfo;
import com.voyageone.common.components.jumei.Bean.JmProductBean_Spus;
import com.voyageone.common.components.jumei.Bean.JmProductBean_Spus_Sku;
import com.voyageone.common.components.jumei.JumeiProductService;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ShopConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.IDN;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Service
public class CmsUploadJmPicService extends BaseTaskService {

    @Autowired
    private JMUploadProductDao jmUploadProductDao;

    @Autowired
    private JumeiProductService jumeiProductService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsUploadJmPicJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        ShopBean shopBean = ShopConfigs.getShop("001", CartEnums.Cart.JM.getId());

    }

    private List<JmBtProductImportModel> getNotUploadProduct(Integer count) {
        return jmUploadProductDao.getNotUploadProduct(count);
    }


    public void uploadProduct(JmBtProductImportModel jmBtProductImport, ShopBean shopBean) throws Exception {

        // 取得上新的数据
        JmProductBean jmProductBean = selfBeanToJmBean(jmBtProductImport);
        // 上新
        jumeiProductService.productNewUpload(shopBean,jmProductBean);

        jmBtProductImport.setJumeiProductId(jmProductBean.getJumei_product_id());
        jmBtProductImport.getJmBtDealImportModel().setJumeiHashId(jmProductBean.getDealInfo().getJumei_hash_id());

    }


    /**
     * 把我们自己定义的product结构转成JM上新的结构
     * @param jmBtProductImport 数据查来的bean
     * @return JM上新的bean
     * @throws Exception
     */
    private JmProductBean selfBeanToJmBean(JmBtProductImportModel jmBtProductImport) throws Exception {
        String partner_sku_nos = "";
        JmProductBean jmProductBean = new JmProductBean();

        jmProductBean.setName(jmBtProductImport.getProductName());
        jmProductBean.setProduct_spec_number(jmBtProductImport.getProductCode());
        jmProductBean.setCategory_v3_4_id(jmBtProductImport.getCategoryLv4Id());
        jmProductBean.setBrand_id(jmBtProductImport.getBrandId());
        jmProductBean.setForeign_language_name(jmBtProductImport.getForeignLanguageName());
        // Todo

        // sku
        List<JmProductBean_Spus> spus = new ArrayList<>();
        for (JmBtSkuImportModel jmBtSkuImportModel : jmBtProductImport.getSkuImportModelList()) {
            JmProductBean_Spus spu = new JmProductBean_Spus();
            spu.setPartner_spu_no(jmBtSkuImportModel.getSku());
            spu.setUpc_code(jmBtSkuImportModel.getUpcCode());
            spu.setPropery("OTHER");
            spu.setSize(jmBtSkuImportModel.getSize());
            spu.setAttribute(jmBtSkuImportModel.getAttribute());
            spu.setAbroad_price(jmBtSkuImportModel.getAbroadPrice().toString());
            // todo 价格单位
//            spu.setArea_code();

            JmProductBean_Spus_Sku sku = new JmProductBean_Spus_Sku();
            sku.setPartner_sku_no(jmBtSkuImportModel.getSku());
            sku.setSale_on_this_deal("1");
            sku.setBusinessman_num(jmBtSkuImportModel.getSku());
            sku.setStocks("0");
            sku.setDeal_price(jmBtSkuImportModel.getDealPrice().toString());
            sku.setMarket_price(jmBtSkuImportModel.getMarketPrice().toString());
            spu.setSkuInfo(sku);
            spus.add(spu);

            partner_sku_nos += jmBtSkuImportModel.getSku() + ",";
        }

        //dealinfo
        JmBtDealImportModel jmBtDealImportModel = jmBtProductImport.getJmBtDealImportModel();
        JmProductBean_DealInfo jmProductBean_DealInfo = new JmProductBean_DealInfo();
        jmProductBean_DealInfo.setPartner_deal_id(jmBtDealImportModel.getPartnerDealId());
        jmProductBean_DealInfo.setStart_time(getTime(jmBtDealImportModel.getStartTime()));
        jmProductBean_DealInfo.setEnd_time(getTime(jmBtDealImportModel.getEndTime()));
        jmProductBean_DealInfo.setUser_purchase_limit(jmBtDealImportModel.getUserPurchaseLimit());
        // todo 仓库
        jmProductBean_DealInfo.setShipping_system_id(jmBtDealImportModel.getShippingSystemId());
        jmProductBean_DealInfo.setProduct_long_name(jmBtDealImportModel.getProductLongName());
        jmProductBean_DealInfo.setProduct_medium_name(jmBtDealImportModel.getProductMediumName());
        jmProductBean_DealInfo.setProduct_short_name(jmBtDealImportModel.getProductShortName());
        jmProductBean_DealInfo.setAddress_of_produce(jmBtDealImportModel.getAddressOfProduce());
        jmProductBean_DealInfo.setBefore_date("无");
        jmProductBean_DealInfo.setSuit_people("时尚潮流人士");
        jmProductBean_DealInfo.setSearch_meta_text_custom(jmBtDealImportModel.getSearchMetaTextCustom());

        // todo 特殊说明
//        jmProductBean_DealInfo.setSpecial_explain();

//        jmProductBean_DealInfo.setDescription_properties();
//        jmProductBean_DealInfo.setDescription_usage();
//        jmProductBean_DealInfo.setDescription_images();

        jmProductBean_DealInfo.setPartner_sku_nos(partner_sku_nos.substring(0, partner_sku_nos.length() - 2));

        jmProductBean.setDealInfo(jmProductBean_DealInfo);
        return jmProductBean;
    }

    public static Long getTime(String user_time) throws Exception {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;


        d = sdf.parse(user_time);
        long l = d.getTime();

        return l;
    }
}
