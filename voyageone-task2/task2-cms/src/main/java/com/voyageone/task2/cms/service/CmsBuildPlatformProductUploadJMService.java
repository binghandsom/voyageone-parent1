package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.JumeiHtProductService;
import com.voyageone.components.jumei.JumeiHtSkuService;
import com.voyageone.components.jumei.JumeiHtSpuService;
import com.voyageone.components.jumei.bean.*;
import com.voyageone.components.jumei.reponse.*;
import com.voyageone.components.jumei.request.*;
import com.voyageone.components.jumei.service.JumeiImageFileService;
import com.voyageone.components.jumei.service.JumeiProductService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtJmProductDao;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.CmsBtPlatformImagesDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ethan Shi on 2016/6/8.
 *
 * @author Ethan Shi
 * @version 2.1.0
 */
@Service
public class CmsBuildPlatformProductUploadJMService extends BaseTaskService {

    public static final int LIMIT = 100;
    public static final int WORK_LOAD_FAIL = 2;
    public static final int WORK_LOAD_SUCCESS = 1;
    private static final int CART_ID = CartEnums.Cart.JM.getValue();

    private static final String DUPLICATE_PRODUCT_NAME = "109902";
    private static final String DUPLICATE_PRODUCT_DRAFT_NAME = "103087";
    private static final String DUPLICATE_SPU_BARCODE = "105106";
    private static final String DUPLICATE_SKU_BUSINESSMAN_NUM = "102063";


    @Autowired
    CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;

    @Autowired
    CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Autowired
    CmsBtJmProductDaoExt cmsBtJmProductDaoExt;

    @Autowired
    CmsBtJmPromotionProductDaoExt cmsBtJmPromotionProductDaoExt;

    @Autowired
    JumeiHtProductService jumeiHtProductService;

    @Autowired
    JumeiHtSpuService jumeiHtSpuService;

    @Autowired
    JumeiHtSkuService jumeiHtSkuService;

    @Autowired
    JumeiHtDealService jumeiHtDealService;

    @Autowired
    SxProductService sxProductService;

    @Autowired
    CmsBtJmProductDao cmsBtJmProductDao;

    @Autowired
    CmsBtJmSkuDao cmsBtJmSkuDao;

    @Autowired
    JumeiImageFileService jumeiImageFileService;

    @Autowired
    CmsBtPlatformImagesDao cmsBtPlatformImagesDao;

    @Autowired
    ProductGroupService productGroupService;

    @Autowired
    JumeiProductService jumeiProductService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadJMJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        //获取Workload列表
        List<CmsBtSxWorkloadModel> groupList = new ArrayList<>();

        // 获取该任务可以运行的销售渠道
        List<String> channels = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);


        for (String channel : channels) {
            List<CmsBtSxWorkloadModel> workloadList = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithChannelIdCartId(LIMIT, channel, CART_ID);

            if (groupList.size() > LIMIT) {
                break;
            }
            if (workloadList != null) {
                groupList.addAll(workloadList);
            }
        }

        if (groupList.size() == 0) {
            $error("上新任务表中没有该平台对应的任务列表信息！[CartId:%s]", CART_ID);
            return;
        }

        $info("共读取[%d]条上新任务！[CartId:%s]", groupList.size(), CART_ID);

        for (CmsBtSxWorkloadModel work : groupList) {
            updateProduct(work);
        }
    }

    /**
     * 上新
     *
     * @param work
     * @throws Exception
     */
    public void updateProduct(CmsBtSxWorkloadModel work) throws Exception {

        try {

            boolean needRetry = false;

            String channelId = work.getChannelId();
            Long groupId = work.getGroupId();
            //按groupId取Product
            SxData sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);


            if (sxData != null) {

                try {
                    //读店铺信息
                    ShopBean shop = Shops.getShop(channelId, CART_ID);
                    if (shop == null) {
                        $error("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, CART_ID);
                        throw new Exception(String.format("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, CART_ID));
                    }

                    ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

                    //对聚美来说所有的商品都是主商品
                    CmsBtProductModel product = sxData.getMainProduct();
                    String productCode = product.getFields().getCode();
                    $info("主商品[Code:%s]! ", productCode);

                    CmsBtJmProductModel cmsBtJmProductModel = new CmsBtJmProductModel();
                    List<CmsBtJmSkuModel> cmsBtJmSkuModelList = new ArrayList<>();

                    CmsBtProductModel_Platform_Cart jmCart = product.getPlatform(CART_ID);
                    String originHashId = jmCart.getpNumIId();
                    if (StringUtils.isNullOrBlank2(originHashId)) {
                        //如果OriginHashId不存在，则创建新商品

                        //填充JmProductBean
                        JmProductBean bean = fillJmProductBean(channelId, product, cmsBtJmProductModel, cmsBtJmSkuModelList, expressionParser, shop, groupId);
                        HtProductAddRequest htProductAddRequest = new HtProductAddRequest();
                        htProductAddRequest.setJmProduct(bean);
                        HtProductAddResponse htProductAddResponse = jumeiHtProductService.addProductAndDeal(shop, htProductAddRequest);

                        if (htProductAddResponse != null && htProductAddResponse.getIs_Success()) {
                            $info("新增产品成功！[ProductId:%s], [ChannelId:%s], [CartId:%s]", product.getProdId(), channelId, CART_ID);
                            // 新增产品成功
                            String jmProductId = htProductAddResponse.getJumei_Product_Id();
                            String jmHashId = htProductAddResponse.getJm_hash_id();
                            //保存jm_product_id
                            cmsBtJmProductModel.setJumeiProductId(jmProductId);
                            cmsBtJmProductModel.setOriginJmHashId(jmHashId);
                            cmsBtJmProductDao.insert(cmsBtJmProductModel);
                            $info("保存jm_product_id成功！[JM_PRODUCT_ID:%s],[ProductId:%s], [ChannelId:%s], [CartId:%s]", jmProductId, product.getProdId(), channelId, CART_ID);

                            //保存jm_sku_no, jm_spu_no
                            List<HtProductAddResponse_Spu> spus = htProductAddResponse.getSpus();
                            for (CmsBtJmSkuModel jmsku : cmsBtJmSkuModelList) {
                                HtProductAddResponse_Spu spu = spus.stream().filter(w -> w.getPartner_sku_no().equals(jmsku.getSkuCode())).findFirst().get();
                                jmsku.setJmSkuNo(spu.getJumei_sku_no());
                                jmsku.setJmSpuNo(spu.getJumei_spu_no());
                                cmsBtJmSkuDao.insert(jmsku);
                                $info("保存聚美SKU成功！[JM_SPU_NO:%s], [ProductId:%s], [ChannelId:%s], [CartId:%s]", spu.getJumei_spu_no(), product.getProdId(), channelId, CART_ID);
                            }

                            List<BaseMongoMap<String, Object>> productJmSku = jmCart.getSkus();
                            for (BaseMongoMap<String, Object> sku : productJmSku) {
                                HtProductAddResponse_Spu spu = spus.stream().filter(w -> w.getPartner_sku_no().equals(sku.getStringAttribute("skuCode"))).findFirst().get();
                                sku.setStringAttribute("jmSpuNo", spu.getJumei_spu_no());
                                sku.setStringAttribute("jmSkuNo", spu.getJumei_sku_no());
                            }


                            //保存product到MongoDB
                            jmCart.setpProductId(jmProductId);
                            jmCart.setpNumIId(jmHashId);
                            saveProductPlatform(channelId, product);
                            //保存group到MongoDB
                            sxData.getPlatform().setPublishTime(DateTimeUtil.getNowTimeStamp());
                            sxData.getPlatform().setPlatformStatus(CmsConstants.PlatformStatus.InStock);
                            sxData.getPlatform().setInStockTime(DateTimeUtil.getNowTimeStamp());
                            sxData.getPlatform().setModifier(getTaskName());
                            sxData.getPlatform().setNumIId(jmHashId);
                            productGroupService.updateGroupsPlatformStatus(sxData.getPlatform());
                        }
                        //如果JM中已经有该商品了，则读取商品信息，补全本地库的内容
                        else if(htProductAddResponse.getError_code().contains(DUPLICATE_PRODUCT_NAME) ||
                                htProductAddResponse.getError_code().contains(DUPLICATE_PRODUCT_DRAFT_NAME))
                        {
                            JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductByName(shop, bean.getName() );
                            if(jmGetProductInfoRes != null)
                            {
                                originHashId = jmGetProductInfoRes.getHash_ids();
                                jmGetProductInfoRes.getSpus();
                                String jmProductId = jmGetProductInfoRes.getProduct_id();



                                //查找Product,并保存到数据库
                                CmsBtJmProductModel  productModel = getCmsBtJmProductModel(channelId, productCode);
                                if(productModel == null)
                                {
                                    cmsBtJmProductModel.setOriginJmHashId(originHashId);
                                    cmsBtJmProductModel.setJumeiProductId(jmProductId);
                                    cmsBtJmProductDao.insert(cmsBtJmProductModel);
                                    //保存jm_product_id
                                    $info("保存jm_product_id成功！[JM_PRODUCT_ID:%s],[ProductId:%s], [ChannelId:%s], [CartId:%s]", jmProductId, product.getProdId(), channelId, CART_ID);
                                }
                                else {
                                    productModel.setOriginJmHashId(originHashId);
                                    productModel.setJumeiProductId(jmProductId);
                                    cmsBtJmProductDao.update(productModel);
                                    //保存jm_product_id
                                    $info("保存jm_product_id成功！[JM_PRODUCT_ID:%s],[ProductId:%s], [ChannelId:%s], [CartId:%s]", jmProductId, product.getProdId(), channelId, CART_ID);
                                }


                                List<JmGetProductInfo_Spus> spus = jmGetProductInfoRes.getSpus();
                                //查询SPU
                                List<CmsBtJmSkuModel> skuList = getCmsBtJmSkuModels(channelId, productCode);
                                for (CmsBtJmSkuModel jmsku : skuList) {
                                    if ( spus.stream().filter(w -> w.getBusinessman_code().equals(jmsku.getSkuCode())).count() >0) {
                                        JmGetProductInfo_Spus spu = spus.stream().filter(w -> w.getBusinessman_code().equals(jmsku.getSkuCode())).findFirst().get();
                                        jmsku.setJmSkuNo(spu.getSku_no());
                                        jmsku.setJmSpuNo(spu.getSpu_no());
                                        cmsBtJmSkuDao.update(jmsku);
                                        $info("保存聚美SKU成功！[JM_SPU_NO:%s], [ProductId:%s], [ChannelId:%s], [CartId:%s]", spu.getSpu_no(), product.getProdId(), channelId, CART_ID);
                                    }
                                }

                                //保存jm_sku_no, jm_spu_no
                                List<BaseMongoMap<String, Object>> productJmSku = jmCart.getSkus();
                                for (BaseMongoMap<String, Object> sku : productJmSku) {
                                    if ( spus.stream().filter(w -> w.getBusinessman_code().equals(sku.getStringAttribute("skuCode"))).count() >0)
                                    {
                                        JmGetProductInfo_Spus spu = spus.stream().filter(w -> w.getBusinessman_code().equals(sku.getStringAttribute("skuCode"))).findFirst().get();
                                        sku.setStringAttribute("jmSpuNo", spu.getSpu_no());
                                        sku.setStringAttribute("jmSkuNo", spu.getSku_no());
                                    }
                                }
                                jmCart.setpProductId(jmGetProductInfoRes.getProduct_id());
                                jmCart.setpNumIId(originHashId);
                                saveProductPlatform(channelId, product);

                                sxData.getPlatform().setNumIId(originHashId);
                                sxData.getPlatform().setPublishTime(DateTimeUtil.getNowTimeStamp());
                                sxData.getPlatform().setModifier(getTaskName());
                                productGroupService.updateGroupsPlatformStatus(sxData.getPlatform());

                            }
                            else
                            {
                                $error("读取产品失败！[ProductId:%s], [ChannelId:%s], [CartId:%s]\", product.getProdId(), channelId, CART_ID");
                                throw  new BusinessException("读取产品失败！[ProductId:%s], [ChannelId:%s], [CartId:%s]\", product.getProdId(), channelId, CART_ID");
                            }
                            needRetry = true;

                        }
                    } else {
                        //如果OriginHashId存在，则修改商品属性
                        CmsBtProductModel_Field fields = product.getFields();
                        BaseMongoMap<String, Object> jmFields = jmCart.getFields();
                        String brandName = fields.getBrand();
                        String productType = fields.getProductType();
                        String sizeType = fields.getSizeType();

                        //查询jm_product
                        CmsBtJmProductModel jmProductModel = getCmsBtJmProductModel(channelId, productCode);
                        HtProductUpdateRequest htProductUpdateRequest = fillHtProductUpdateRequest(product, jmProductModel, expressionParser, shop);
                        HtProductUpdateResponse htProductUpdateResponse = jumeiHtProductService.update(shop, htProductUpdateRequest);

                        if (htProductUpdateResponse != null && htProductUpdateResponse.getIs_Success()) {
                            $info("更新产品成功！[ProductId:%s], [ChannelId:%s], [CartId:%s]", product.getProdId(), channelId, CART_ID);

                            //回写数据库
                            if (jmProductModel != null) {
                                cmsBtJmProductDao.update(jmProductModel);
                            }

                            //查询SPU
                            List<CmsBtJmSkuModel> skuList = getCmsBtJmSkuModels(channelId, productCode);

                            List<BaseMongoMap<String, Object>> newSkuList = jmCart.getSkus();
                            List<CmsBtProductModel_Sku> commonSkus = product.getSkus();
                            newSkuList = mergeSkuAttr(newSkuList, commonSkus);

                            for (BaseMongoMap<String, Object> skuMap : newSkuList) {

                                String skuCode = skuMap.getStringAttribute("skuCode");
                                //旧SPU需要更新
                                if (skuList.stream().filter(w -> w.getSkuCode().equals(skuCode)).count() > 0) {
                                    CmsBtJmSkuModel oldSku = skuList.stream().filter(w -> w.getSkuCode().equals(skuCode)).findFirst().get();
                                    String jmSpuNo = oldSku.getJmSpuNo();
                                    HtSpuUpdateRequest htSpuUpdateRequest = new HtSpuUpdateRequest();
                                    htSpuUpdateRequest.setJumei_spu_no(jmSpuNo);
                                    htSpuUpdateRequest.setAbroad_price(skuMap.getDoubleAttribute("clientMsrpPrice"));
                                    htSpuUpdateRequest.setAttribute(jmFields.getStringAttribute("attribute"));
                                    htSpuUpdateRequest.setProperty(skuMap.getStringAttribute("property"));
                                    String sizeStr = skuMap.getStringAttribute("size");
                                    sizeStr = getSizeFromSizeMap(sizeStr, channelId, brandName, productType, sizeType);
                                    htSpuUpdateRequest.setSize(sizeStr);
//                                  htSpuUpdateRequest.setArea_code(19);//TODO

                                    HtSpuUpdateResponse htSpuUpdateResponse = jumeiHtSpuService.update(shop, htSpuUpdateRequest);
                                    if (htSpuUpdateResponse != null && htSpuUpdateResponse.is_Success()) {
                                        $info("更新Spu成功！[ProductId:%s], [JmSpuNo:%s]", product.getProdId(), jmSpuNo);
                                        oldSku.setJmSize(sizeStr);
                                        oldSku.setMsrpUsd(new BigDecimal(skuMap.getDoubleAttribute("clientMsrpPrice")));
                                        cmsBtJmSkuDao.update(oldSku);
                                    }

                                }
                                //新SPU需要增加
                                else {
                                    HtSpuAddRequest htSpuAddRequest = new HtSpuAddRequest();
                                    htSpuAddRequest.setUpc_code(skuMap.getStringAttribute("barcode"));
                                    String sizeStr = skuMap.getStringAttribute("size");
                                    htSpuAddRequest.setSize(getSizeFromSizeMap(sizeStr, channelId, brandName, productType, sizeType));

                                    int i = skuMap.getIntAttribute("clientMsrpPrice");

                                    htSpuAddRequest.setAbroad_price(skuMap.getDoubleAttribute("clientMsrpPrice"));
                                    htSpuAddRequest.setArea_code("19");//TODO
                                    htSpuAddRequest.setJumei_product_id(jmCart.getpProductId());

                                    htSpuAddRequest.setProperty(skuMap.getStringAttribute("property"));
                                    htSpuAddRequest.setAttribute(jmFields.getStringAttribute("attribute"));
                                    HtSpuAddResponse htSpuAddResponse = jumeiHtSpuService.add(shop, htSpuAddRequest);

                                    if (htSpuAddResponse != null && htSpuAddResponse.is_Success()) {
                                        $info("新增Spu成功！[ProductId:%s], [JmSpuNo:%s]", product.getProdId(), htSpuAddResponse.getJumei_spu_no());
                                        skuMap.setStringAttribute("jmSpuNo", htSpuAddResponse.getJumei_spu_no());


                                        HtSkuAddRequest htSkuAddRequest = new HtSkuAddRequest();
                                        htSkuAddRequest.setCustoms_product_number(skuMap.getStringAttribute("skuCode"));
                                        htSkuAddRequest.setSale_on_this_deal("1");
                                        htSkuAddRequest.setBusinessman_num(skuMap.getStringAttribute("skuCode"));
                                        htSkuAddRequest.setStocks("1");
                                        htSkuAddRequest.setDeal_price(skuMap.getStringAttribute("priceSale"));
                                        htSkuAddRequest.setMarket_price(skuMap.getStringAttribute("priceMsrp"));
                                        htSkuAddRequest.setJumei_hash_id(originHashId);
                                        htSkuAddRequest.setJumei_spu_no(htSpuAddResponse.getJumei_spu_no());
                                        HtSkuAddResponse htSkuAddResponse = jumeiHtSkuService.add(shop, htSkuAddRequest);
                                        if (htSkuAddResponse != null && htSkuAddResponse.is_Success()) {
                                            $info("增加Sku成功！[skuCode:%s]", skuMap.getStringAttribute("skuCode"));

                                            CmsBtJmSkuModel cmsBtJmSkuModel = fillNewCmsBtJmSkuModel(channelId, productCode, skuMap);
                                            cmsBtJmSkuModel.setJmSpuNo(htSpuAddResponse.getJumei_spu_no());
                                            cmsBtJmSkuModel.setJmSkuNo(htSkuAddResponse.getJumei_sku_no());
                                            cmsBtJmSkuDao.insert(cmsBtJmSkuModel);

                                            skuMap.setStringAttribute("jmSkuNo", htSkuAddResponse.getJumei_sku_no());
                                        }
                                    }
                                }
                            }

                            //获取jm_hash_id列表
                            List<String> jmHashIdList = cmsBtJmPromotionProductDaoExt.selectJmHashIds(productCode, channelId);
                            jmHashIdList.add(originHashId);

                            //重新读一次
                            skuList = getCmsBtJmSkuModels(channelId, productCode);

                            for (String hashId : jmHashIdList) {
                                HtDealUpdateRequest htDealUpdateRequest = fillHtDealUpdateRequest(channelId, jmFields, skuList, hashId, expressionParser, shop);
                                HtDealUpdateResponse htDealUpdateResponse = jumeiHtDealService.update(shop, htDealUpdateRequest);
                                if (htDealUpdateResponse != null && htDealUpdateResponse.is_Success()) {
                                    $info("更新Deal成功！[ProductId:%s]", product.getProdId());
                                }
                            }
                        }

                        //保存product到MongoDB
                        saveProductPlatform(channelId, product);

                        sxData.getPlatform().setPublishTime(DateTimeUtil.getNowTimeStamp());
                        sxData.getPlatform().setModifier(getTaskName());
                        productGroupService.updateGroupsPlatformStatus(sxData.getPlatform());
                    }

                    //保存workload
                    if(needRetry)
                    {
                        //需要重试
                        delayWorkload(work);
                        $info("保存workload成功！[workId:%s]", work.getId());
                    }
                    else {
                        saveWorkload(work, WORK_LOAD_SUCCESS);
                        $info("保存workload成功！[workId:%s]", work.getId());
                    }
                } catch (Exception e) {
                    //保存错误log
                    // 如果上新数据中的errorMessage为空
                    if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                        sxData.setErrorMessage(e.getMessage());
                    }
                    sxProductService.insertBusinessLog(sxData, getTaskName());
                    throw e;
                }
            }
        }
        catch (ServerErrorException se) {
            //需要重试
            delayWorkload(work);
        }
        catch (Exception e) {
            //保存workload
            saveWorkload(work, WORK_LOAD_FAIL);
            $error("workload上新失败！[workId:%s]", work.getId());

        }
    }


    /**
     * @param channelId
     * @param jmFields
     * @param skuList
     * @param hashId
     * @param expressionParser
     * @param shopProp
     * @return
     * @throws Exception
     */
    private HtDealUpdateRequest fillHtDealUpdateRequest(String channelId, BaseMongoMap<String, Object> jmFields,
                                                        List<CmsBtJmSkuModel> skuList, String hashId, ExpressionParser expressionParser, ShopBean shopProp) throws Exception {
        HtDealUpdateRequest htDealUpdateRequest = new HtDealUpdateRequest();
        htDealUpdateRequest.setJumei_hash_id(hashId);
        HtDealUpdate_DealInfo dealInfo = new HtDealUpdate_DealInfo();
        String shippingId = Codes.getCode("JUMEI", channelId);
        dealInfo.setShipping_system_id(Integer.valueOf(shippingId));
        dealInfo.setProduct_long_name(jmFields.getStringAttribute("productLongName"));
        dealInfo.setProduct_medium_name(jmFields.getStringAttribute("productMediumName"));
        dealInfo.setProduct_short_name(jmFields.getStringAttribute("productShortName"));
        dealInfo.setBefore_date(jmFields.getStringAttribute("beforeDate"));
        dealInfo.setSuit_people(jmFields.getStringAttribute("suitPeople"));
        dealInfo.setSpecial_explain(jmFields.getStringAttribute("specialExplain"));
        dealInfo.setSearch_meta_text_custom(jmFields.getStringAttribute("searchMetaTextCustom"));
        dealInfo.setAttribute(jmFields.getStringAttribute("attribute"));

        String jmDetailTemplate = sxProductService.resolveDict("聚美详情", expressionParser, shopProp, getTaskName(), null);
        dealInfo.setDescription_properties(jmDetailTemplate);
        String jmProductTemplate = sxProductService.resolveDict("聚美实拍", expressionParser, shopProp, getTaskName(), null);
        dealInfo.setDescription_images(jmProductTemplate);
        String jmUseageTemplate = sxProductService.resolveDict("聚美使用方法", expressionParser, shopProp, getTaskName(), null);
        dealInfo.setDescription_usage(jmUseageTemplate);

        List<String> jmSkuNoList = skuList.stream().map(CmsBtJmSkuModel::getJmSkuNo).collect(Collectors.toList());
        dealInfo.setJumei_sku_no(Joiner.on(",").join(jmSkuNoList));
        htDealUpdateRequest.setUpdate_data(dealInfo);
        return htDealUpdateRequest;
    }

    /**
     * @param work
     * @param result
     */
    private void saveWorkload(CmsBtSxWorkloadModel work, int result) {
        work.setPublishStatus(result);
        work.setModifier(getTaskName());
        cmsBtSxWorkloadDaoExt.updatePublishStatus(work);
    }


    private void delayWorkload(CmsBtSxWorkloadModel work) {
        Date modifyTime = work.getModified();
        Calendar calendar = Calendar.getInstance() ;
        calendar.setTime(modifyTime);
        calendar.add(Calendar.MINUTE, -10);
        work.setCreated(calendar.getTime());
        work.setModifier(getTaskName());
        cmsBtSxWorkloadDaoExt.delayWorkload(work);
    }




    /**
     * @param channelId
     * @param productCode
     * @param jmHashId
     */
    private void saveGroupNumIId(String channelId, String productCode, String jmHashId) {
        CmsBtProductGroupModel group = cmsBtProductGroupDao.selectOneWithQuery("{\"mainProductCode\": \"" + productCode + "\"," + "\"cartId\":" + CART_ID + "}", channelId);
        if (group != null) {
            group.setNumIId(jmHashId);
            group.setPublishTime(DateTimeUtil.getNow());
            group.setModifier(getTaskName());
            cmsBtProductGroupDao.update(group);
            $info("保存productGroup成功！[GroupId:%s], [ChannelId:%s], [CartId:%s]", group.getGroupId(), channelId, CART_ID);
        }
    }

    /**
     * @param channelId
     * @param product
     */
    private void saveProductPlatform(String channelId, CmsBtProductModel product) {
        Map<String, Object> rsMap = new HashMap<>();

        rsMap.put("platforms.P" + CART_ID + ".pProductId", product.getPlatform(CART_ID).getpProductId());
        rsMap.put("platforms.P" + CART_ID + ".pNumIId", product.getPlatform(CART_ID).getpNumIId());
        List<BaseMongoMap<String, Object>>   jmSkus  = product.getPlatform(CART_ID).getSkus();
        List<BaseMongoMap<String, Object>>   newJmSkus =  new ArrayList<>();
        for (BaseMongoMap<String, Object> sku : jmSkus)
        {
            BaseMongoMap<String, Object> newSku = new  BaseMongoMap<String, Object>();
            newSku.setStringAttribute("skuCode", sku.getStringAttribute("skuCode"));
            newSku.setStringAttribute("priceMsrp", sku.getDoubleAttribute("priceMsrp"));
            newSku.setStringAttribute("priceRetail", sku.getDoubleAttribute("priceRetail"));
            newSku.setStringAttribute("priceSale", sku.getDoubleAttribute("priceSale"));
            newSku.setStringAttribute("priceChgFlg", sku.getStringAttribute("priceChgFlg"));
            newSku.setStringAttribute("jmSpuNo", sku.getStringAttribute("jmSpuNo"));
            newSku.setStringAttribute("jmSkuNo", sku.getStringAttribute("jmSkuNo"));
            newSku.setStringAttribute("property", sku.getStringAttribute("property"));
            newJmSkus.add(newSku);
        }

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", product.getProdId());

        rsMap.put("platforms.P" + CART_ID + ".skus", newJmSkus);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        cmsBtProductDao.update(channelId, queryMap, updateMap);
        $info("保存product成功！[ProductId:%s], [ChannelId:%s], [CartId:%s]", product.getProdId(), channelId, CART_ID);
    }

    /**
     * 查询CmsBtJmSkuModel
     *
     * @param channelId
     * @param productCode
     * @return
     */
    private List<CmsBtJmSkuModel> getCmsBtJmSkuModels(String channelId, String productCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("productCode", productCode);
        map.put("channelId", channelId);

        List<CmsBtJmSkuModel> skuList = cmsBtJmSkuDao.selectList(map);
        if (skuList == null) {
            skuList = new ArrayList<>();
        }
        return skuList;
    }

    /**
     * 查询getCmsBtJmProductModel
     *
     * @param channelId
     * @param productCode
     * @return
     */
    private CmsBtJmProductModel getCmsBtJmProductModel(String channelId, String productCode) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("productCode", productCode);
        productMap.put("channelId", channelId);

        CmsBtJmProductModel model = cmsBtJmProductDao.selectOne(productMap);

        return model;
    }


    /**
     * 填充HtProductUpdateRequest
     *
     * @param product
     * @param cmsBtJmProductModel
     * @param expressionParser
     * @param shopProp
     * @return
     * @throws Exception
     */
    private HtProductUpdateRequest fillHtProductUpdateRequest(CmsBtProductModel product, CmsBtJmProductModel cmsBtJmProductModel,
                                                              ExpressionParser expressionParser, ShopBean shopProp) throws Exception {

        CmsBtProductModel_Field fields = product.getFields();
        CmsBtProductModel_Platform_Cart jmCart = product.getPlatform(CART_ID);
        HtProductUpdateRequest htProductUpdateRequest = new HtProductUpdateRequest();
        String productId = jmCart.getpProductId();
        BaseMongoMap<String, Object> jmFields = jmCart.getFields();
        String productName = jmFields.getStringAttribute("productNameCn") + " " + fields.getCode();
        htProductUpdateRequest.setJumei_product_name(productName);
        htProductUpdateRequest.setJumei_product_id(productId);
        HtProductUpdate_ProductInfo productInfo = new HtProductUpdate_ProductInfo();
        productInfo.setBrand_id(jmCart.getpBrandId());
        productInfo.setCategory_v3_4_id(jmCart.getpCatId());
        productInfo.setForeign_language_name(jmFields.get("productNameEn").toString());
        productInfo.setName(productName);


        //商品主图
        String picTemplate = sxProductService.resolveDict("聚美白底方图", expressionParser, shopProp, getTaskName(), null);

        if (!StringUtils.isNullOrBlank2(picTemplate)) {
            picTemplate = picTemplate.substring(0, picTemplate.lastIndexOf(","));
            productInfo.setNormalImage(picTemplate);
        }
        htProductUpdateRequest.setUpdate_data(productInfo);

        if (cmsBtJmProductModel != null) {
            cmsBtJmProductModel.setProductNameCn(productName);
            cmsBtJmProductModel.setForeignLanguageName(jmFields.get("productNameEn").toString());
            cmsBtJmProductModel.setImage1(fields.getImages1().get(0).getName());
            cmsBtJmProductModel.setModified(DateTimeUtil.getDate());
            cmsBtJmProductModel.setModifier(getTaskName());
            cmsBtJmProductModel.setProductLongName(jmFields.getStringAttribute("productLongName"));
            cmsBtJmProductModel.setProductMediumName(jmFields.getStringAttribute("productMediumName"));
            cmsBtJmProductModel.setProductShortName(jmFields.getStringAttribute("productShortName"));
            cmsBtJmProductModel.setAvailablePeriod(jmFields.getStringAttribute("beforeDate"));
            cmsBtJmProductModel.setApplicableCrowd(jmFields.getStringAttribute("suitPeople"));
            cmsBtJmProductModel.setSpecialnote(jmFields.getStringAttribute("specialExplain"));
            cmsBtJmProductModel.setSearchMetaTextCustom(jmFields.getStringAttribute("searchMetaTextCustom"));
        }


        return htProductUpdateRequest;
    }


    /**
     * 填充JmProductBean
     *
     * @param channelId
     * @param product
     * @param cmsBtJmProductModel
     * @param cmsBtJmSkuModelList
     * @return
     * @throws Exception
     */
    private JmProductBean fillJmProductBean(String channelId, CmsBtProductModel product, CmsBtJmProductModel cmsBtJmProductModel,
                                            List<CmsBtJmSkuModel> cmsBtJmSkuModelList, ExpressionParser expressionParser, ShopBean shopProp, Long groupId) throws Exception {

        CmsBtProductModel_Platform_Cart jmCart = product.getPlatform(CartEnums.Cart.JM);

        JmProductBean bean = new JmProductBean();

        //先从fields读，以后会从common.fields
        CmsBtProductModel_Field fields = product.getFields();
        String brandName = fields.getBrand();
        String productType = fields.getProductType();
        String sizeType = fields.getSizeType();
        String productCode = fields.getCode();

        BaseMongoMap<String, Object> jmFields = jmCart.getFields();

        bean.setProduct_spec_number(fields.getCode());
        bean.setCategory_v3_4_id(Integer.valueOf(jmCart.getpCatId()));
        bean.setBrand_id(Integer.valueOf(jmCart.getpBrandId()));
        bean.setName(jmFields.getStringAttribute("productNameCn") + " " + productCode);
        bean.setForeign_language_name(jmFields.getStringAttribute("productNameEn"));
        //白底方图
        String picTemplate = sxProductService.resolveDict("聚美白底方图", expressionParser, shopProp, getTaskName(), null);
        if (!StringUtils.isNullOrBlank2(picTemplate)) {
            picTemplate = picTemplate.substring(0, picTemplate.lastIndexOf(","));
            bean.setNormalImage(picTemplate);
        }

        JmProductBean_DealInfo deal = new JmProductBean_DealInfo();
        deal.setPartner_deal_id(productCode + "-" + channelId + "-" + CART_ID);

        deal.setUser_purchase_limit(jmFields.getIntAttribute("userPurchaseLimit"));

        String shippingId = Codes.getCode("JUMEI", channelId);
        deal.setShipping_system_id(Integer.valueOf(shippingId));


        String jmDetailTemplate = sxProductService.resolveDict("聚美详情", expressionParser, shopProp, getTaskName(), null);
        deal.setDescription_properties(jmDetailTemplate);
        String jmProductTemplate = sxProductService.resolveDict("聚美实拍", expressionParser, shopProp, getTaskName(), null);
        deal.setDescription_images(jmProductTemplate);
        String jmUseageTemplate = sxProductService.resolveDict("聚美使用方法", expressionParser, shopProp, getTaskName(), null);
        deal.setDescription_usage(jmUseageTemplate);

        deal.setProduct_long_name(jmFields.getStringAttribute("productLongName"));
        deal.setProduct_medium_name(jmFields.getStringAttribute("productMediumName"));
        deal.setProduct_short_name(jmFields.getStringAttribute("productShortName"));
        deal.setBefore_date(jmFields.getStringAttribute("beforeDate"));
        deal.setSuit_people(jmFields.getStringAttribute("suitPeople"));
        deal.setSpecial_explain(jmFields.getStringAttribute("specialExplain"));
        deal.setSearch_meta_text_custom(jmFields.getStringAttribute("searchMetaTextCustom"));
        deal.setAddress_of_produce(jmFields.getStringAttribute("originCn"));
        deal.setStart_time(System.currentTimeMillis() / 1000);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MINUTE, 30);
        deal.setEnd_time(rightNow.getTimeInMillis() / 1000);
        List<String> skuCodeList = product.getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList());
        String skuString = Joiner.on(",").join(skuCodeList);
        deal.setPartner_sku_nos(skuString);
        deal.setRebate_ratio("1");
        bean.setDealInfo(deal);


        List<BaseMongoMap<String, Object>> jmSkus = jmCart.getSkus();
        List<CmsBtProductModel_Sku> commonSkus = product.getSkus();

        //合并Common和平台的Sku属性
        jmSkus = mergeSkuAttr(jmSkus, commonSkus);

        List<JmProductBean_Spus> spus = new ArrayList<>();
        for (BaseMongoMap<String, Object> jmSku : jmSkus) {
            JmProductBean_Spus spu = new JmProductBean_Spus();
            spu.setPartner_spu_no(jmSku.getStringAttribute("skuCode"));
            spu.setUpc_code(jmSku.getStringAttribute("barcode"));
            spu.setPropery(jmSku.getStringAttribute("property"));
            spu.setAttribute(jmFields.getStringAttribute("attribute"));//Code级
            String sizeStr = jmSku.getStringAttribute("size");

            spu.setSize(getSizeFromSizeMap(sizeStr, channelId, brandName, productType, sizeType));

            spu.setAbroad_price(jmSku.getDoubleAttribute("priceSale"));
            spu.setArea_code("19"); //TODO

            JmProductBean_Spus_Sku jmSpuSku = new JmProductBean_Spus_Sku();
            jmSpuSku.setPartner_sku_no(jmSku.getStringAttribute("skuCode"));
            jmSpuSku.setSale_on_this_deal("1");
            jmSpuSku.setBusinessman_num(jmSku.getStringAttribute("skuCode"));
            jmSpuSku.setStocks("1"); //TODO
            jmSpuSku.setDeal_price(jmSku.getStringAttribute("priceSale"));
            jmSpuSku.setMarket_price(jmSku.getStringAttribute("priceMsrp"));

            spu.setSkuInfo(jmSpuSku);
            spus.add(spu);

            //填写CmsBtJMSku
            CmsBtJmSkuModel cmsBtJmSkuModel = fillNewCmsBtJmSkuModel(channelId, productCode, jmSku);
            cmsBtJmSkuModelList.add(cmsBtJmSkuModel);

        }
        bean.setSpus(spus);


        //填充cmsBtJmProductModel
        cmsBtJmProductModel.setChannelId(channelId);
        cmsBtJmProductModel.setProductCode(productCode);
        cmsBtJmProductModel.setOrigin(deal.getAddress_of_produce());
        cmsBtJmProductModel.setProductNameCn(bean.getName());
        cmsBtJmProductModel.setVoBrandName("");//TODO
        cmsBtJmProductModel.setVoCategoryName("");//TODO
        cmsBtJmProductModel.setBrandName(brandName);
        cmsBtJmProductModel.setProductType(productType);
        cmsBtJmProductModel.setSizeType(sizeType);
        cmsBtJmProductModel.setProductDesEn(fields.getShortDesEn());
        cmsBtJmProductModel.setAttribute(jmFields.getStringAttribute("attribute"));
        cmsBtJmProductModel.setForeignLanguageName(bean.getForeign_language_name());
        cmsBtJmProductModel.setAddressOfProduce(deal.getAddress_of_produce());
        cmsBtJmProductModel.setAvailablePeriod(deal.getBefore_date());
        cmsBtJmProductModel.setProductDesCn(fields.getLongDesCn());
        cmsBtJmProductModel.setApplicableCrowd(deal.getSuit_people());
        cmsBtJmProductModel.setSpecialnote(deal.getSpecial_explain());
        cmsBtJmProductModel.setColorEn(fields.getColor());
        cmsBtJmProductModel.setImage1(fields.getImages1().get(0).getName());
        cmsBtJmProductModel.setProductLongName(deal.getProduct_long_name());
        cmsBtJmProductModel.setProductMediumName(deal.getProduct_medium_name());
        cmsBtJmProductModel.setProductShortName(deal.getProduct_short_name());
        cmsBtJmProductModel.setSearchMetaTextCustom(deal.getSearch_meta_text_custom());
        cmsBtJmProductModel.setMaterialEn(fields.getMaterialEn());
        cmsBtJmProductModel.setMaterialCn(fields.getMaterialCn());

        if (cmsBtJmSkuModelList.size() > 0) {
            cmsBtJmProductModel.setMsrpUsd(cmsBtJmSkuModelList.get(0).getMsrpUsd());
            cmsBtJmProductModel.setMsrpRmb(cmsBtJmSkuModelList.get(0).getMsrpRmb());
            cmsBtJmProductModel.setRetailPrice(cmsBtJmSkuModelList.get(0).getRetailPrice());
            cmsBtJmProductModel.setSalePrice(cmsBtJmSkuModelList.get(0).getSalePrice());
        }
        String hscode = fields.getHsCodePrivate();
        if (!StringUtils.isNullOrBlank2(hscode)) {
            String[] hscodeArray = hscode.split(",");
            cmsBtJmProductModel.setHsCode(hscodeArray[0]);
            cmsBtJmProductModel.setHsName(hscodeArray[1]);
            cmsBtJmProductModel.setHsUnit(hscodeArray[2]);
        }


        cmsBtJmProductModel.setModified(DateTimeUtil.getDate());
        cmsBtJmProductModel.setCreated(DateTimeUtil.getDate());
        cmsBtJmProductModel.setModifier(getTaskName());
        cmsBtJmProductModel.setCreater(getTaskName());

        return bean;
    }


    /**
     * 取size
     *
     * @param sizeStr
     * @param channelId
     * @param brandName
     * @param productType
     * @param sizeType
     * @return
     */
    private String getSizeFromSizeMap(String sizeStr, String channelId, String brandName, String productType, String sizeType) {
        if (!StringUtils.isNullOrBlank2(sizeStr)) {
            Map<String, String> sizeMap = sxProductService.getSizeMap(channelId, brandName, productType, sizeType);
            String changedSize = sizeMap.get(sizeStr);
            if (changedSize != null) {
                return changedSize;
            } else {
                return sizeStr;
            }
        } else {
            return "NO SIZE";
        }
    }


    /**
     * 新建CmsBtJmSkuModel
     *
     * @param channelId
     * @param productCode
     * @param jmSku
     * @return
     */
    private CmsBtJmSkuModel fillNewCmsBtJmSkuModel(String channelId, String productCode, BaseMongoMap<String, Object> jmSku) {
        CmsBtJmSkuModel cmsBtJmSkuModel = new CmsBtJmSkuModel();
        cmsBtJmSkuModel.setChannelId(channelId);
        cmsBtJmSkuModel.setProductCode(productCode);
        cmsBtJmSkuModel.setSkuCode(jmSku.getStringAttribute("skuCode"));
        cmsBtJmSkuModel.setUpc(jmSku.getStringAttribute("barcode"));
        cmsBtJmSkuModel.setCmsSize("");//TODO
        cmsBtJmSkuModel.setFormat("");//TODO
        cmsBtJmSkuModel.setJmSize(jmSku.getStringAttribute("size"));

        cmsBtJmSkuModel.setMsrpUsd(new BigDecimal(jmSku.getStringAttribute("clientMsrpPrice")));
        cmsBtJmSkuModel.setMsrpRmb(new BigDecimal(jmSku.getStringAttribute("priceMsrp")));
        cmsBtJmSkuModel.setSalePrice(new BigDecimal(jmSku.getStringAttribute("priceSale")));
        cmsBtJmSkuModel.setRetailPrice(new BigDecimal(jmSku.getStringAttribute("priceRetail")));


        cmsBtJmSkuModel.setModified(DateTimeUtil.getDate());
        cmsBtJmSkuModel.setCreated(DateTimeUtil.getDate());
        cmsBtJmSkuModel.setModifier(getTaskName());
        cmsBtJmSkuModel.setCreater(getTaskName());
        return cmsBtJmSkuModel;
    }


    /**
     * 合并平台fields和common sku属性
     *
     * @param jmSkus
     * @param commonSkus
     * @return
     */
    private List<BaseMongoMap<String, Object>> mergeSkuAttr(List<BaseMongoMap<String, Object>> jmSkus, List<CmsBtProductModel_Sku> commonSkus) {

        for (BaseMongoMap<String, Object> jmSku : jmSkus) {
            String code = jmSku.getStringAttribute("skuCode");

            if (commonSkus.stream().filter(w -> w.getSkuCode().equals(code)).count() > 0) {
                CmsBtProductModel_Sku CommonSku = commonSkus.stream().filter(w -> w.getSkuCode().equals(code)).findFirst().get();
                jmSku.put("barcode", CommonSku.getBarcode());
                jmSku.put("priceMsrp", CommonSku.getPriceMsrp());
                jmSku.put("priceRetail", CommonSku.getPriceRetail());
                jmSku.put("clientMsrpPrice", CommonSku.getClientMsrpPrice());
                jmSku.put("clientRetailPrice", CommonSku.getClientRetailPrice());
                jmSku.put("size", CommonSku.getSize());
            }
        }

        return jmSkus;
    }
}
