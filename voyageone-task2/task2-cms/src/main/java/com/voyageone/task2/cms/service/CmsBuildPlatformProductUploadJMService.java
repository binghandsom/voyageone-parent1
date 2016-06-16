package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
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
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtJmProductDao;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.sx.SxProductService;
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
    private static final String IMG_HTML = "<img src=\"%s\" alt=\"\" />";
    private static final int CART_ID = CartEnums.Cart.JM.getValue();
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
                        JmProductBean bean = fillJmProductBean(channelId, product, cmsBtJmProductModel, cmsBtJmSkuModelList);
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

                            //保存product到MongoDB
                            saveProductNumIId(channelId, product, jmProductId, jmHashId);
                            //保存group到MongoDB
                            saveGroupNumIId(channelId, productCode, jmHashId);
                        }
                    } else {
                        //如果OriginHashId存在，则修改商品属性
                        CmsBtProductModel_Field fields = product.getFields();
                        BaseMongoMap<String, Object> jmFields = jmCart.getFields();

                        //查询jm_product
                        CmsBtJmProductModel jmProductModel = getCmsBtJmProductModel(channelId, productCode);
                        HtProductUpdateRequest htProductUpdateRequest = fillHtProductUpdateRequest(product, jmProductModel);
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
                                    htSpuUpdateRequest.setAbroad_price(skuMap.getDoubleAttribute("priceSale"));
//                            htSpuUpdateRequest.setAttribute(skuMap.getStringAttribute("attribute"));
//                            htSpuUpdateRequest.setPropery(skuMap.getStringAttribute("property"));
//                            htSpuUpdateRequest.setProperty("OTHER"); //TODO
                                    htSpuUpdateRequest.setSize(skuMap.getStringAttribute("size"));
//                            htSpuUpdateRequest.setArea_code(19);//TODO

                                    HtSpuUpdateResponse htSpuUpdateResponse = jumeiHtSpuService.update(shop, htSpuUpdateRequest);
                                    if (htSpuUpdateResponse != null && htSpuUpdateResponse.is_Success()) {
                                        $info("更新Spu成功！[ProductId:%s], [JmSpuNo:%s]", product.getProdId(), jmSpuNo);
                                        oldSku.setJmSize(skuMap.getStringAttribute("size"));
                                        oldSku.setSalePrice(new BigDecimal(skuMap.getStringAttribute("priceSale")));
                                        cmsBtJmSkuDao.update(oldSku);

                                    }

                                }
                                //新SPU需要增加
                                else {
                                    HtSpuAddRequest htSpuAddRequest = new HtSpuAddRequest();
                                    htSpuAddRequest.setUpc_code(skuMap.getStringAttribute("barcode"));
                                    htSpuAddRequest.setProperty("OTHER"); //TODO
                                    htSpuAddRequest.setSize(skuMap.getStringAttribute("size"));
                                    htSpuAddRequest.setAbroad_price(skuMap.getStringAttribute("priceSale"));
                                    htSpuAddRequest.setArea_code("19");//TODO
                                    htSpuAddRequest.setJumei_product_id(jmCart.getpProductId());
//                            htSpuAddRequest.setAttribute(skuMap.getStringAttribute("attribute"));

                                    HtSpuAddResponse htSpuAddResponse = jumeiHtSpuService.add(shop, htSpuAddRequest);
                                    if (htSpuAddResponse != null && htSpuAddResponse.is_Success()) {
                                        $info("新增Spu成功！[ProductId:%s], [JmSpuNo:%s]", product.getProdId(), htSpuAddResponse.getJumei_spu_no());
                                        CmsBtJmSkuModel cmsBtJmSkuModel = fillNewCmsBtJmSkuModel(channelId, productCode, skuMap);
                                        cmsBtJmSkuModel.setJmSpuNo(htSpuAddResponse.getJumei_spu_no());
                                        cmsBtJmSkuDao.insert(cmsBtJmSkuModel);

                                    }
                                }

                            }

                            //获取jm_hash_id列表
                            List<String> jmHashIdList = cmsBtJmPromotionProductDaoExt.selectJmHashIds(productCode, channelId);
                            jmHashIdList.add(originHashId);

                            skuList = getCmsBtJmSkuModels(channelId, productCode);

                            for (String hashId : jmHashIdList) {
                                for (BaseMongoMap<String, Object> skuMap : newSkuList) {
                                    String skuCode = skuMap.getStringAttribute("skuCode");
                                    if (skuList.stream().filter(w -> w.getSkuCode().equals(skuCode)).count() > 0) {
                                        CmsBtJmSkuModel model = skuList.stream().filter(w -> w.getSkuCode().equals(skuCode)).findFirst().get();
                                        //需要增加sku
                                        if (StringUtils.isNullOrBlank2(model.getJmSkuNo())) {
                                            HtSkuAddRequest htSkuAddRequest = new HtSkuAddRequest();
                                            htSkuAddRequest.setCustoms_product_number(skuMap.getStringAttribute("skuCode"));
                                            htSkuAddRequest.setSale_on_this_deal("1");
                                            htSkuAddRequest.setBusinessman_num(skuMap.getStringAttribute("skuCode"));
                                            htSkuAddRequest.setStocks("0");
                                            htSkuAddRequest.setDeal_price(skuMap.getStringAttribute("priceMsrp"));
                                            htSkuAddRequest.setMarket_price(skuMap.getStringAttribute("priceSale"));
                                            htSkuAddRequest.setJumei_hash_id(hashId);
                                            htSkuAddRequest.setJumei_spu_no(model.getJmSpuNo());
                                            HtSkuAddResponse htSkuAddResponse = jumeiHtSkuService.add(shop, htSkuAddRequest);
                                            if (htSkuAddResponse != null && htSkuAddResponse.is_Success()) {
                                                $info("增加Sku成功！[skuCode:%s]", skuMap.getStringAttribute("skuCode"));
                                                model.setModifier(getTaskName());
                                                model.setModified(new Date());
                                                model.setJmSkuNo(htSkuAddResponse.getJumei_sku_no());
                                                cmsBtJmSkuDao.update(model);
                                            }
                                        }
                                        //需要修改SKU,然而Sku没什么能改的
                                    }
                                }

                                HtDealUpdateRequest htDealUpdateRequest = fillHtDealUpdateRequest(channelId, fields, jmFields, skuList, hashId);
                                HtDealUpdateResponse htDealUpdateResponse = jumeiHtDealService.update(shop, htDealUpdateRequest);
                                if (htDealUpdateResponse != null && htDealUpdateResponse.is_Success()) {
                                    $info("更新Deal成功！[ProductId:%s]", product.getProdId());

                                    //回写数据库
                                    jmProductModel.setProductLongName(jmFields.getStringAttribute("productLongName"));
                                    jmProductModel.setProductMediumName(jmFields.getStringAttribute("productMediumName"));
                                    jmProductModel.setProductShortName(jmFields.getStringAttribute("productShortName"));
                                    jmProductModel.setAvailablePeriod(jmFields.getStringAttribute("beforeDate"));
                                    jmProductModel.setApplicableCrowd(jmFields.getStringAttribute("suitPeople"));
                                    jmProductModel.setSpecialnote(jmFields.getStringAttribute("specialExplain"));
                                    jmProductModel.setSearchMetaTextCustom(jmFields.getStringAttribute("searchMetaTextCustom"));
                                    if (jmProductModel != null) {
                                        cmsBtJmProductDao.update(jmProductModel);
                                    }
                                }
                            }
                        }
                    }

                    //保存workload
                    saveWorkload(work, WORK_LOAD_SUCCESS);
                    $info("保存workload成功！[workId:%s]", work.getId());
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
        catch (Exception e)
        {
            //保存workload
            saveWorkload(work, WORK_LOAD_FAIL);
            $info("保存workload成功！[workId:%s]", work.getId());
        }
    }


    /**
     *
     * @param channelId
     * @param fields
     * @param jmFields
     * @param skuList
     * @param hashId
     * @return
     * @throws Exception
     */
    private HtDealUpdateRequest fillHtDealUpdateRequest(String channelId, CmsBtProductModel_Field fields, BaseMongoMap<String, Object> jmFields, List<CmsBtJmSkuModel> skuList, String hashId) throws Exception {
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

        String brandName = fields.getBrand();
        String productType = fields.getProductType();
        String sizeType = fields.getSizeType();
        //品牌图
        List<String> brandPicUrls = sxProductService.getImageUrls(channelId, CART_ID, 3, 1, brandName, productType, sizeType, false);
        StringBuffer sb = new StringBuffer();

        for (String brandPic : brandPicUrls) {
            sb.append(String.format(IMG_HTML, brandPic));
        }

        dealInfo.setDescription_properties(sb.toString());


        sb.setLength(0);
        List<String> logiPicUrls = sxProductService.getImageUrls(channelId, CART_ID, 4, 1, brandName, productType, sizeType, false);
        for (String logiPic : logiPicUrls) {
            sb.append(String.format(IMG_HTML, logiPic));
        }
        //物流图

        dealInfo.setDescription_images(sb.toString());


        sb.setLength(0);
        List<String> sizePicUrls = sxProductService.getImageUrls(channelId, CART_ID, 2, 1, brandName, productType, sizeType, false);
        for (String sizePic : sizePicUrls) {
            sb.append(String.format(IMG_HTML, sizePic));
        }

        dealInfo.setDescription_usage(sb.toString());

        htDealUpdateRequest.setUpdate_data(dealInfo);

        List<String> jmSkuNoList = skuList.stream().map(CmsBtJmSkuModel::getJmSkuNo).collect(Collectors.toList());
        dealInfo.setJumei_sku_no(Joiner.on(",").join(jmSkuNoList));
        return htDealUpdateRequest;
    }

    /**
     *
     * @param work
     * @param result
     */
    private void saveWorkload(CmsBtSxWorkloadModel work, int result) {
        work.setPublishStatus(result);
        work.setModifier(getTaskName());
        cmsBtSxWorkloadDaoExt.updatePublishStatus(work);
    }


    /**
     *
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
     *
     * @param channelId
     * @param product
     * @param jmProductId
     * @param jmHashId
     */
    private void saveProductNumIId(String channelId, CmsBtProductModel product, String jmProductId, String jmHashId) {
        product.getPlatform(CART_ID).setpNumIId(jmHashId);
        product.getPlatform(CART_ID).setpProductId(jmProductId);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", product.getProdId());
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("platforms.P" + CART_ID + ".pNumIId", jmHashId);
        rsMap.put("platforms.P" + CART_ID + ".pProductId", jmProductId);

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
     * @return
     * @throws Exception
     */
    private HtProductUpdateRequest fillHtProductUpdateRequest(CmsBtProductModel product, CmsBtJmProductModel cmsBtJmProductModel) throws Exception {
        String channelId = product.getChannelId();

        CmsBtProductModel_Field fields = product.getFields();
        CmsBtProductModel_Platform_Cart jmCart = product.getPlatform(CART_ID);
        HtProductUpdateRequest htProductUpdateRequest = new HtProductUpdateRequest();
        String productId = jmCart.getpProductId();
        BaseMongoMap<String, Object> jmFields = jmCart.getFields();
        String productName = jmFields.getStringAttribute("productNameCn");
        htProductUpdateRequest.setJumei_product_name(productName);
        htProductUpdateRequest.setJumei_product_id(productId);
        HtProductUpdate_ProductInfo productInfo = new HtProductUpdate_ProductInfo();
        productInfo.setBrand_id(jmCart.getpBrandId());
        productInfo.setCategory_v3_4_id(jmCart.getpCatId());
        productInfo.setForeign_language_name(jmFields.get("productNameEn").toString());
        productInfo.setName(productName);

        //商品主图
        String productCode = fields.getCode();
        String brandName = fields.getBrand();
        String productType = fields.getProductType();
        String sizeType = fields.getSizeType();
        List<String> mainPicUrls = sxProductService.getImageUrls(channelId, CART_ID, 1, 1, brandName, productType, sizeType, false);
        String mainPicUrlStr = Joiner.on(",").join(mainPicUrls);
        productInfo.setNormalImage(mainPicUrlStr);
        htProductUpdateRequest.setUpdate_data(productInfo);


        if (cmsBtJmProductModel != null) {
            cmsBtJmProductModel.setProductNameCn(productName);
            cmsBtJmProductModel.setForeignLanguageName(jmFields.get("productNameEn").toString());
//            cmsBtJmProductModel.setImage1(mainPicUrlStr);
            cmsBtJmProductModel.setModified(DateTimeUtil.getDate());
            cmsBtJmProductModel.setModifier(getTaskName());
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
    private JmProductBean fillJmProductBean(String channelId, CmsBtProductModel product, CmsBtJmProductModel cmsBtJmProductModel, List<CmsBtJmSkuModel> cmsBtJmSkuModelList) throws Exception {

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
        bean.setName(jmFields.getStringAttribute("productNameCn"));
        bean.setForeign_language_name(jmFields.getStringAttribute("productNameEn"));
        //商品主图
        List<String> mianPicUrls = sxProductService.getImageUrls(channelId, CART_ID, 1, 1, brandName, productType, sizeType, false);
        String mainPicUrlStr = Joiner.on(",").join(mianPicUrls);
        bean.setNormalImage(mainPicUrlStr);


        JmProductBean_DealInfo deal = new JmProductBean_DealInfo();
        deal.setPartner_deal_id(productCode + "-" + channelId + "-" + CART_ID);

        deal.setUser_purchase_limit(0);

        String shippingId = Codes.getCode("JUMEI", channelId);
        deal.setShipping_system_id(Integer.valueOf(shippingId));


        //品牌图
        List<String> brandPicUrls = sxProductService.getImageUrls(channelId, CART_ID, 3, 1, brandName, productType, sizeType, false);
        StringBuffer sb = new StringBuffer();

        for (String brandPic : brandPicUrls) {
            sb.append(String.format(IMG_HTML, brandPic));
        }

        deal.setDescription_properties(sb.toString());


        sb.setLength(0);
        List<String> logiPicUrls = sxProductService.getImageUrls(channelId, CART_ID, 4, 1, brandName, productType, sizeType, false);
        for (String logiPic : logiPicUrls) {
            sb.append(String.format(IMG_HTML, logiPic));
        }
        //物流图

        deal.setDescription_images(sb.toString());


        sb.setLength(0);
        List<String> sizePicUrls = sxProductService.getImageUrls(channelId, CART_ID, 2, 1, brandName, productType, sizeType, false);
        for (String sizePic : sizePicUrls) {
            sb.append(String.format(IMG_HTML, sizePic));
        }

        deal.setDescription_usage(sb.toString());


        deal.setProduct_long_name(jmFields.getStringAttribute("productLongName"));
        deal.setProduct_medium_name(jmFields.getStringAttribute("productMediumName"));
        deal.setProduct_short_name(jmFields.getStringAttribute("productShortName"));
        deal.setBefore_date(jmFields.getStringAttribute("beforeDate"));
        deal.setSuit_people(jmFields.getStringAttribute("suitPeople"));
        deal.setSpecial_explain(jmFields.getStringAttribute("specialExplain"));
        deal.setSearch_meta_text_custom(jmFields.getStringAttribute("searchMetaTextCustom"));
        deal.setAddress_of_produce(jmFields.getStringAttribute("originCn"));
        deal.setStart_time(System.currentTimeMillis()/1000);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MINUTE, 30);
        deal.setEnd_time(rightNow.getTimeInMillis()/1000);
        List<String> skuCodeList = product.getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList());
        String skuString = Joiner.on(",").join(skuCodeList);
        deal.setPartner_sku_nos(skuString);
        deal.setRebate_ratio("10");
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
            spu.setPropery("OTHER");
//            spu.setPropery(jmSku.getStringAttribute("property"));
//            spu.setAttribute(jmSku.getStringAttribute("attribute"));//TODO
//            spu.setSize(jmSku.getStringAttribute("size")); //TODO

            String sizeStr =jmSku.getStringAttribute("size");

            if(!StringUtils.isNullOrBlank2(sizeStr)) {
                Map sizeMap = sxProductService.getSizeMap(channelId, brandName, productType, sizeType);
                if(sizeMap != null)
                {
                    String changedSize = (String)sizeMap.get(sizeStr);
                    spu.setSize(changedSize);
                }
                else
                {
                    spu.setSize(sizeStr);
                }
            }
            else
            {
                spu.setSize("NO SIZE");
            }



            spu.setAbroad_price(jmSku.getDoubleAttribute("priceSale"));
            spu.setArea_code("19"); //TODO

            JmProductBean_Spus_Sku jmSpuSku = new JmProductBean_Spus_Sku();
            jmSpuSku.setPartner_sku_no(jmSku.getStringAttribute("skuCode"));
            jmSpuSku.setSale_on_this_deal("1");
            jmSpuSku.setBusinessman_num(jmSku.getStringAttribute("skuCode"));
            jmSpuSku.setStocks("0");
            jmSpuSku.setDeal_price(jmSku.getStringAttribute("priceMsrp"));
            jmSpuSku.setMarket_price(jmSku.getStringAttribute("priceSale"));

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
        cmsBtJmProductModel.setAttribute("");//TODO
        cmsBtJmProductModel.setForeignLanguageName(bean.getForeign_language_name());
        cmsBtJmProductModel.setAddressOfProduce(deal.getAddress_of_produce());
        cmsBtJmProductModel.setAvailablePeriod(deal.getBefore_date());
        cmsBtJmProductModel.setProductDesCn(fields.getShortDesCn());
        cmsBtJmProductModel.setApplicableCrowd(deal.getSuit_people());
        cmsBtJmProductModel.setSpecialnote(deal.getSpecial_explain());
        cmsBtJmProductModel.setColorEn(fields.getColor());
        cmsBtJmProductModel.setImage1("");//TODO
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
