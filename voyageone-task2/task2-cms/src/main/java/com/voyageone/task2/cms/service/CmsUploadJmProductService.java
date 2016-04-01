package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.JmPicBean;
import com.voyageone.task2.cms.dao.DealImportDao;
import com.voyageone.task2.cms.dao.JMUploadProductDao;
import com.voyageone.task2.cms.dao.ProductImportDao;
import com.voyageone.task2.cms.model.JmBtDealImportModel;
import com.voyageone.task2.cms.model.JmBtProductImportModel;
import com.voyageone.task2.cms.model.JmBtSkuImportModel;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.jumei.Bean.*;
import com.voyageone.common.components.jumei.Enums.JumeiImageType;
import com.voyageone.common.components.jumei.JumeiProductService;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Service
public class CmsUploadJmProductService extends BaseTaskService {
    @Autowired
    private JMUploadProductDao jmUploadProductDao;

    @Autowired
    private JumeiProductService jumeiProductService;

    @Autowired
    private DealImportDao dealImportDao;

    @Autowired
    private ProductImportDao productImportDao;

    @Autowired
    private SimpleTransaction simpleTransaction;

    private static final String IMG_HTML = "<img src=\"%s\" alt=\"\" />";

    private static final String DESCRIPTION_USAGE = "<div>%s %s <br /></div>";

    private static final String DESCRIPTION_IMAGES = "%s<br />";

    private static final Pattern special_symbol= Pattern.compile("[~@'\\[\\]\\s\".:#$%&_''‘’^]");

    private static Vector<JmBtProductImportModel> succeedProduct = new Vector<>();

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsUploadJmProductJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        int threadPoolCnt = 5;
        int limit = 100;
        List<String> channels = new ArrayList<>();
        for (TaskControlBean taskControlBean : taskControlList) {
            if ("thread_count".equalsIgnoreCase(taskControlBean.getCfg_name())) {
                threadPoolCnt = Integer.parseInt(taskControlBean.getCfg_val1());
            } else if ("Limit".equalsIgnoreCase(taskControlBean.getCfg_name())) {
                limit = Integer.parseInt(taskControlBean.getCfg_val1());
            } else if("order_channel_id".equalsIgnoreCase(taskControlBean.getCfg_name())){
                $info(taskControlBean.getCfg_val1());
                channels.add(taskControlBean.getCfg_val1());
            }
        }
        List<JmBtProductImportModel> jmBtProductImports = getNotUploadProduct(limit,channels);
        $info("---------------cnt:" + jmBtProductImports.size());
        ShopBean shopBean = Shops.getShop(ChannelConfigEnums.Channel.SN.getId(), CartEnums.Cart.JM.getId());
//        shopBean.setAppKey("131");
//        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
//        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
//        shopBean.setApp_url("http://openapi.ext.jumei.com/");
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);

        for (JmBtProductImportModel product : jmBtProductImports) {
            executor.execute(() -> uploadProduct(product, shopBean));
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
//        succeedProduct.forEach(jmBtProductImportModel -> updateFlg(jmBtProductImportModel));
//        succeedProduct.clear();
    }

    private List<JmBtProductImportModel> getNotUploadProduct(Integer count,List<String> channelIds) throws IOException {
        Map<String, Object> param = new HashMap<>();
        param.put("count", count);
        param.put("channels", channelIds);
        $info("param:" + JacksonUtil.bean2Json(param));
        return jmUploadProductDao.getNotUploadProduct(param);
    }


    public void uploadProduct(JmBtProductImportModel jmBtProductImport, ShopBean shopBean) {

        try {
            if(jmBtProductImport.getSkuImportModelList() == null || jmBtProductImport.getSkuImportModelList().size() == 0){
                throw new BusinessException("没有找到SKU数据 code：" + jmBtProductImport.getProductCode());
            }
            $info(jmBtProductImport.getChannelId() + "|" + jmBtProductImport.getProductCode() + " 聚美上新开始");
            // 取得上新的数据
            JmProductBean jmProductBean = selfBeanToJmBean(jmBtProductImport);

            setImages(jmBtProductImport, jmProductBean);
            // 上新
            try{
                jumeiProductService.productNewUpload(shopBean, jmProductBean);
                jmBtProductImport.setJumeiProductId(jmProductBean.getJumei_product_id());
                jmBtProductImport.getJmBtDealImportModel().setJumeiHashId(jmProductBean.getDealInfo().getJumei_hash_id());
                copyJumeiSkuInfo(jmBtProductImport,jmProductBean);
            }catch (BusinessException e){
                if(e.getMessage().indexOf("103087")>=0) {
                    $info("聚美上新失败 调用取得聚美产品信息接口");
                    JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductByName(shopBean, jmProductBean.getName());
                    jmBtProductImport.setJumeiProductId(jmGetProductInfoRes.getProduct_id());
                    jmBtProductImport.getJmBtDealImportModel().setJumeiHashId(jmGetProductInfoRes.getHash_ids());
                    copyJumeiSkuInfo(jmBtProductImport, jmGetProductInfoRes);
                }else{
                    throw e;
                }
            }

            jmBtProductImport.getJmBtDealImportModel().setSynFlg(1);
            jmBtProductImport.getJmBtDealImportModel().setModifier(getTaskName());
//            succeedProduct.add(jmBtProductImport);
            updateFlg(jmBtProductImport);
            $info(jmBtProductImport.getChannelId() + "|" + jmBtProductImport.getProductCode() + " 聚美上新结束");
        } catch (Exception e) {
            $error(e);
            issueLog.log(e, ErrorType.BatchJob, getSubSystem(), jmBtProductImport.getChannelId() + "  " + jmBtProductImport.getProductCode());

            if(e.getMessage().contains("产品审核失败") || e.getMessage().contains("jumei_hash_id不正确")){
                jmBtProductImport.setSynFlg("1");
            }else{
                jmBtProductImport.setSynFlg("3");
            }
            jmBtProductImport.setUploadErrorInfo(CommonUtil.getMessages(e));
            jmBtProductImport.setModifier(getTaskName());
            productImportDao.updateProductImportInfo(jmBtProductImport);
        }
    }

    private void copyJumeiSkuInfo(JmBtProductImportModel jmBtProductImportModel,JmProductBean jmProduct){

        for(JmBtSkuImportModel skuImportModel : jmBtProductImportModel.getSkuImportModelList()){
            for(JmProductBean_Spus spu:jmProduct.getSpus()){
                if(skuImportModel.getSku().equalsIgnoreCase(spu.getPartner_spu_no())){
                    skuImportModel.setJumeiSpuNo(spu.getJumei_spu_no());
                    skuImportModel.setJumeiSkuNo(spu.getSkuInfo().getJumei_sku_no());
                    break;
                }
            }
        }

    }
    private void copyJumeiSkuInfo(JmBtProductImportModel jmBtProductImportModel,JmGetProductInfoRes jmGetProductInfoRes){

        for(JmBtSkuImportModel skuImportModel : jmBtProductImportModel.getSkuImportModelList()){
            for(JmGetProductInfo_Spus spu:jmGetProductInfoRes.getSpus()){
                if(skuImportModel.getSku().equalsIgnoreCase(spu.getSku_list().get(0).getBusinessman_code())){
                    skuImportModel.setJumeiSpuNo(spu.getSpu_no());
                    skuImportModel.setJumeiSkuNo(spu.getSku_list().get(0).getSku_no());
                    break;
                }
            }
        }

    }

    private void setImages(JmBtProductImportModel jmBtProductImport, JmProductBean jmProductBean) {
        Map<Integer, List<JmPicBean>> imagesMap = jmUploadProductDao.selectImageByCode(jmBtProductImport.getChannelId(), jmBtProductImport.getProductCode(), jmBtProductImport.getBrandName(), jmBtProductImport.getSizeType());

        StringBuffer stringBuffer = new StringBuffer();
        List<JmPicBean> pics = imagesMap.get(JumeiImageType.NORMAL.getId());
        //白底方图
        if (pics != null) {
            for (JmPicBean jmPicBean : pics) {
                if (stringBuffer.length() != 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(jmPicBean.getJmUrl());
            }
            ;
        } else {
            throw new BusinessException("白底方图不存在");
        }
        jmProductBean.setNormalImage(stringBuffer.toString());

        // 竖图
        stringBuffer = new StringBuffer();
        pics = imagesMap.get(JumeiImageType.VERTICAL.getId());
        if (pics != null) {
            for (JmPicBean jmPicBean : pics) {
                if (stringBuffer.length() != 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(jmPicBean.getJmUrl());
            }
        }
        if(stringBuffer.length() > 0){
            jmProductBean.setVerticalImage(stringBuffer.toString());
        }


        // 品牌图
        stringBuffer = new StringBuffer();
        pics = imagesMap.get(JumeiImageType.BRANDSTORY.getId());
        if (pics != null) {
            for (JmPicBean jmPicBean : pics) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getJmUrl()));
            }
        } else {
            throw new BusinessException("品牌图不存在");
        }
        jmProductBean.getDealInfo().setDescription_properties(stringBuffer.toString());


        //参数图
        stringBuffer = new StringBuffer();
        pics = imagesMap.get(JumeiImageType.PARAMETER.getId());
        if (pics != null) {
            for (JmPicBean jmPicBean : pics) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getJmUrl()));
            }
        }
        //尺码表
        pics = imagesMap.get(JumeiImageType.SIZE.getId());
        if (pics != null) {
            for (JmPicBean jmPicBean : pics) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getJmUrl()));
            }
        } else {
            if(!jmBtProductImport.getSizeType().equalsIgnoreCase("One Size") && !jmBtProductImport.getSizeType().equalsIgnoreCase("OneSize")){
                throw new BusinessException("尺码图不存在");
            }
        }
        jmProductBean.getDealInfo().setDescription_usage(String.format(DESCRIPTION_USAGE, jmBtProductImport.getProductDes(), stringBuffer.toString()));

        // 产品详细
        stringBuffer = new StringBuffer();
        pics = imagesMap.get(JumeiImageType.PRODUCT.getId());
        if (pics != null) {
            for (JmPicBean jmPicBean : pics) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getJmUrl()));
            }
        } else {
            throw new BusinessException("产品图不存在");
        }
        pics = imagesMap.get(JumeiImageType.LOGISTICS.getId());
        boolean logisticeBrand = false;
        if (pics != null) {
            // 先找出和品牌有关的物流图
            for (JmPicBean jmPicBean : pics) {
                if(jmBtProductImport.getBrandName().equalsIgnoreCase(jmPicBean.getImageTypeExtend())){
                    stringBuffer.append(String.format(IMG_HTML, jmPicBean.getJmUrl()));
                    logisticeBrand = true;
                }
            }
            //如果没有找到和品牌有关的物流图就用默认的
            if(logisticeBrand == false){
                for (JmPicBean jmPicBean : pics) {
                    stringBuffer.append(String.format(IMG_HTML, jmPicBean.getJmUrl()));
                }
            }
        } else {
            throw new BusinessException("物流图不存在");
        }
        jmProductBean.getDealInfo().setDescription_images(String.format(DESCRIPTION_IMAGES, stringBuffer.toString()));

    }

    /**
     * 更新数据库
     *
     * @param jmBtProductImport product数据
     */
    private void updateFlg(JmBtProductImportModel jmBtProductImport) {
        simpleTransaction.openTransaction();
        try {
            // 把product数据从import表中移动到 product表中
            jmBtProductImport.setModifier(getTaskName());
            if (jmUploadProductDao.updateJMProduct(jmBtProductImport) == 0) {
                jmBtProductImport.setCreater(getTaskName());
                jmUploadProductDao.insertJMProduct(jmBtProductImport);
            }

            // sku删除 重新查
            jmUploadProductDao.delJMProductSkuByCode(jmBtProductImport.getChannelId(), jmBtProductImport.getProductCode());
            jmUploadProductDao.insertJMProductSkuList(jmBtProductImport.getSkuImportModelList(), getTaskName());

            // 把import表中的flg设为
            jmBtProductImport.setSynFlg("2");
            jmBtProductImport.setUploadErrorInfo("");
            productImportDao.updateProductImportInfo(jmBtProductImport);

            // 回写JumeiHashId
            dealImportDao.updateDealImportInfo(jmBtProductImport.getJmBtDealImportModel());

        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 把我们自己定义的product结构转成JM上新的结构
     *
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
        jmProductBean.setSpus(spus);
        for (JmBtSkuImportModel jmBtSkuImportModel : jmBtProductImport.getSkuImportModelList()) {
            JmProductBean_Spus spu = new JmProductBean_Spus();
            spu.setPartner_spu_no(jmBtSkuImportModel.getSku());
            spu.setUpc_code(jmBtSkuImportModel.getUpcCode());
            spu.setPropery("OTHER");
            spu.setSize(jmBtSkuImportModel.getSize());
            spu.setAttribute(jmBtProductImport.getAttribute());
            spu.setAbroad_price(jmBtSkuImportModel.getAbroadPrice());
            // todo 价格单位
            spu.setArea_code("19");

            JmProductBean_Spus_Sku sku = new JmProductBean_Spus_Sku();
            sku.setPartner_sku_no(jmBtSkuImportModel.getSku());
            sku.setSale_on_this_deal("1");
            sku.setBusinessman_num(jmBtSkuImportModel.getSku());
            sku.setStocks("1");
            sku.setDeal_price(jmBtSkuImportModel.getDealPrice().toString());
            sku.setMarket_price(jmBtSkuImportModel.getMarketPrice().toString());
            if(jmBtSkuImportModel.getDealPrice() < 1 || jmBtSkuImportModel.getMarketPrice() < 1){
                throw new BusinessException("价格为0");
            }
            spu.setSkuInfo(sku);
            spus.add(spu);

            partner_sku_nos += jmBtSkuImportModel.getSku() + ",";
        }

        //dealinfo
        JmBtDealImportModel jmBtDealImportModel = jmBtProductImport.getJmBtDealImportModel();
        JmProductBean_DealInfo jmProductBean_DealInfo = new JmProductBean_DealInfo();
        jmProductBean_DealInfo.setPartner_deal_id(jmBtDealImportModel.getProductCode() + "-" + jmBtDealImportModel.getDealId());
        jmProductBean_DealInfo.setStart_time(getTime(jmBtDealImportModel.getStartTime()));
        jmProductBean_DealInfo.setEnd_time(getTime(jmBtDealImportModel.getEndTime()));
        jmProductBean_DealInfo.setUser_purchase_limit(jmBtDealImportModel.getUserPurchaseLimit());
        jmProductBean_DealInfo.setShipping_system_id(jmBtDealImportModel.getShippingSystemId());
        jmProductBean_DealInfo.setProduct_long_name(jmBtDealImportModel.getProductLongName());
        jmProductBean_DealInfo.setProduct_medium_name(jmBtDealImportModel.getProductMediumName());
        jmProductBean_DealInfo.setProduct_short_name(jmBtDealImportModel.getProductShortName());
        jmProductBean_DealInfo.setAddress_of_produce(jmBtProductImport.getAddressOfProduce());
        jmProductBean_DealInfo.setBefore_date("无");
        jmProductBean_DealInfo.setSuit_people("时尚潮流人士");
        jmProductBean_DealInfo.setSearch_meta_text_custom(jmBtDealImportModel.getSearchMetaTextCustom());

        // 特殊说明
        jmProductBean_DealInfo.setSpecial_explain(jmBtProductImport.getSpecialNote());

        jmProductBean_DealInfo.setPartner_sku_nos(partner_sku_nos.substring(0, partner_sku_nos.length() - 1));

        jmProductBean.setDealInfo(jmProductBean_DealInfo);
        return jmProductBean;
    }

    public static Long getTime(String user_time) throws Exception {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;


        d = sdf.parse(user_time);
        long l = d.getTime()/1000-8*3600;

        return l;
    }
}
