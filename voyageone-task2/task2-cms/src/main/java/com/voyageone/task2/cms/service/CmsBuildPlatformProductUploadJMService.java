package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.*;
import com.voyageone.components.jumei.bean.*;
import com.voyageone.components.jumei.reponse.*;
import com.voyageone.components.jumei.request.*;
import com.voyageone.components.jumei.service.JumeiImageFileService;
import com.voyageone.components.jumei.service.JumeiProductService;
import com.voyageone.components.jumei.service.JumeiService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtJmProductDao;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.CmsBtPlatformImagesDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.BusinessLogService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
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
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
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

    // 聚美详情,聚美实拍或聚美使用方法里面的html语法解析错误(例："span不能使用face属性","不能使用外链"等)
    private static final String INVALID_HTML_CONTENT = "109902";
//    private static final String DUPLICATE_PRODUCT_NAME = "109902";
    // 产品名称(name)在聚美已存在
    private static final String DUPLICATE_PRODUCT_DRAFT_NAME = "103087";
    // 商品自带条码（UPC_CODE）存在相同 值或UPC_CODE在聚美已存在
    private static final String DUPLICATE_SPU_BARCODE = "105106";
    // 在聚美已存在的商家编码(businessman_num)
    private static final String DUPLICATE_SKU_BUSINESSMAN_NUM = "102063";
    private static final String INVALID_PRODUCT_STATUS = "产品状态不是待审核";
    private static final Pattern special_symbol= Pattern.compile("[~@'\\[\\]\\s\".:#$%&_''‘’^]");


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

    @Autowired
    ProductService productService;

    @Autowired
    BusinessLogService businessLogService;

    @Autowired
    private JumeiHtMallService jumeiHtMallService;

    @Autowired
    private JumeiService jumeiService;

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
            List<CmsBtSxWorkloadModel> workloadList = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithChannelIdCartIdGroupBy(LIMIT, channel, CART_ID);

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

        SxData sxData =  null;

        try {

            boolean needRetry = false;

            String channelId = work.getChannelId();
            Long groupId = work.getGroupId();
            //按groupId取Product
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);

            if (sxData == null) {
                String errorMsg = String.format("取得上新用的商品数据(SxData)信息失败！请向管理员确认 [sxData=null][workloadId:%s][groupId:%s]:", work.getId(), work.getGroupId());
                $error(errorMsg);
                throw new BusinessException(errorMsg);
            }

            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                String errorMsg = sxData.getErrorMessage();
                // 取得上新数据出错时，cartId有可能没有设置
                sxData.setCartId(CART_ID);
                // 有错误的时候，直接报错
                throw new BusinessException(errorMsg);
            }

            // 增加聚美规格的默认属性设置 START
            {
                // 修改main product里的内容
                {
                    CmsBtProductModel product = sxData.getMainProduct();
                    List<BaseMongoMap<String, Object>> productJmSku = product.getPlatform(CART_ID).getSkus();
                    for (BaseMongoMap<String, Object> sku : productJmSku) {
                        if (StringUtils.isEmpty(sku.getStringAttribute("property"))) {
                            sku.setStringAttribute("property", "OTHER");
                        }
                    }
                }

                // 修改product list里的内容
                if (sxData.getProductList() != null) {
                    for (CmsBtProductModel product : sxData.getProductList()) {
                        List<BaseMongoMap<String, Object>> productJmSku = product.getPlatform(CART_ID).getSkus();
                        for (BaseMongoMap<String, Object> sku : productJmSku) {
                            if (StringUtils.isEmpty(sku.getStringAttribute("property"))) {
                                sku.setStringAttribute("property", "OTHER");
                            }
                        }
                    }
                }

                // 修改sku list里的内容
                if (sxData.getSkuList() != null) {
                    for (BaseMongoMap<String, Object> sku : sxData.getSkuList()) {
                        if (StringUtils.isEmpty(sku.getStringAttribute("property"))) {
                            sku.setStringAttribute("property", "OTHER");
                        }
                    }
                }

            }

            // 增加聚美规格的默认属性设置 END

            // 上新对象产品Code列表
            List<String> listSxCode = sxData.getProductList().stream().map(p -> p.getCommon().getFields().getCode()).collect(Collectors.toList());

            //读店铺信息
            ShopBean shop = Shops.getShop(channelId, CART_ID);
            if (shop == null) {
                $error("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, CART_ID);
                throw new Exception(String.format("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, CART_ID));
            }

            ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

            //对聚美来说所有的商品都是主商品
            CmsBtProductModel product = sxData.getMainProduct();
            String productCode = product.getCommon().getFields().getCode();
            $info("主商品[Code:%s]! ", productCode);


            CmsBtJmProductModel cmsBtJmProductModel = null;
            List<CmsBtJmSkuModel> cmsBtJmSkuModelList = new ArrayList<>();

            CmsBtProductModel_Platform_Cart jmCart = product.getPlatform(CART_ID);
            String originHashId = jmCart.getpNumIId();

            //取库存
            Map<String, Integer> skuLogicQtyMap = productService.getLogicQty(StringUtils.isNullOrBlank2(product.getOrgChannelId())? channelId :  product.getOrgChannelId(), jmCart.getSkus().stream().map(w->w.getStringAttribute("skuCode")).collect(Collectors.toList()));

            if (StringUtils.isNullOrBlank2(originHashId)) {
                //如果OriginHashId不存在，则创建新商品

                //填充cmsBtJmProductModel
                cmsBtJmProductModel = fillCmsBtJmProductModel(cmsBtJmProductModel, product);
                //填充cmsBtJmSkuModelList
                cmsBtJmSkuModelList = fillCmsBtJmSkuModelList(cmsBtJmSkuModelList, product);

                //填充JmProductBean
                JmProductBean bean = fillJmProductBean(product, expressionParser, shop, skuLogicQtyMap);

                HtProductAddRequest htProductAddRequest = new HtProductAddRequest();
                htProductAddRequest.setJmProduct(bean);
                HtProductAddResponse htProductAddResponse = jumeiHtProductService.addProductAndDeal(shop, htProductAddRequest);

                // 因为聚美API的不稳定性，商品聚美上新成功后会可能存在返回的response获取不到jm的hashId，以及spuno，
                // 如果返回的response无hashId，或者无spuno，再重新调用聚美获取商品的API拿一次聚美商品信息，做后续处理。
                // 保证上新成功，聚美的hashId和spuno能够正常回写。
                boolean jmApiErrorNoHashId = false;
                if (htProductAddResponse != null && htProductAddResponse.getIs_Success()) {
                    // 上新成功时，看聚美平台返回的response里面hashId，spuno等是否为空
                    if (StringUtil.isEmpty(htProductAddResponse.getJm_hash_id())) {
                        // jmHashId为空时
                        $info("新增聚美商品成功！但返回的response获取不到jm的hashId,后面会重新调用一次聚美获取商品的API取得" +
                                "商品信息 [ProductId:%s], [ChannelId:%s], [CartId:%s]", product.getProdId(), channelId, CART_ID);
                        jmApiErrorNoHashId = true;
                    } else {
                        // spuno为空时
                        List<HtProductAddResponse_Spu> spus = htProductAddResponse.getSpus();
                        for (CmsBtJmSkuModel jmsku : cmsBtJmSkuModelList) {
                            HtProductAddResponse_Spu spu = spus.stream().filter(w -> w.getPartner_sku_no().equals(jmsku.getSkuCode())).findFirst().get();
                            // 只要有一个spuno为空时，就需要重新取得一下聚美商品信息
                            if (StringUtils.isEmpty(spu.getJumei_spu_no())) {
                                $info("新增聚美商品成功！但返回的response获取不到jm的spuno或skuno,后面会重新调用一次聚美获取商品的API取得" +
                                        "商品信息 [ProductId:%s], [ChannelId:%s], [CartId:%s]", product.getProdId(), channelId, CART_ID);
                                jmApiErrorNoHashId = true;
                                break;
                            }
                        }
                    }
                }

                if (htProductAddResponse != null && htProductAddResponse.getIs_Success() && !jmApiErrorNoHashId) {
                    $info("新增产品成功！[ProductId:%s], [ChannelId:%s], [CartId:%s]", product.getProdId(), channelId, CART_ID);
                    // 新增产品成功
                    String jmProductId = htProductAddResponse.getJumei_Product_Id();
                    String jmHashId = htProductAddResponse.getJm_hash_id();
                    //保存jm_product_id
                    cmsBtJmProductModel.setJumeiProductId(jmProductId);
                    cmsBtJmProductModel.setOriginJmHashId(jmHashId);
                    cmsBtJmProductDao.insert(cmsBtJmProductModel);
                    $info("新增CmsBtJmProduct成功！[JM_PRODUCT_ID:%s],[ProductId:%s], [ChannelId:%s], [CartId:%s]", jmProductId, product.getProdId(), channelId, CART_ID);

                    //保存jm_sku_no, jm_spu_no
                    List<HtProductAddResponse_Spu> spus = htProductAddResponse.getSpus();
                    for (CmsBtJmSkuModel jmsku : cmsBtJmSkuModelList) {
                        HtProductAddResponse_Spu spu = spus.stream().filter(w -> w.getPartner_sku_no().equals(jmsku.getSkuCode())).findFirst().get();
                        jmsku.setJmSkuNo(spu.getJumei_sku_no());
                        jmsku.setJmSpuNo(spu.getJumei_spu_no());
                        cmsBtJmSkuDao.insert(jmsku);
                        $info("新增CmsBtJmSku成功！[JM_SPU_NO:%s], [ProductId:%s], [ChannelId:%s], [CartId:%s]", spu.getJumei_spu_no(), product.getProdId(), channelId, CART_ID);
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
                    sxData.getPlatform().setPlatformPid(jmProductId);
                    productGroupService.updateGroupsPlatformStatus(sxData.getPlatform(), listSxCode);
                    if(jmHashId.endsWith("p0"))
                    {
                        String errorMsg = String.format("聚美Hash_Id格式错误![ProductId:%s], [ChannelId:%s], [CartId:%s]:", product.getProdId(), channelId, CART_ID);
                        $error(errorMsg);
                        new BusinessException(errorMsg);
                    }

                    // added by morse.lu 2016/08/30 start
                    uploadMall(product, shop, expressionParser, null, null);
                    // added by morse.lu 2016/08/30 end
                }
                //如果上新成功之后没取到jmHashId,spuno,skuno，或者JM中已经有该商品了，则调用一次聚美获取商品的API取得商品信息，补全本地库的内容
                else if(jmApiErrorNoHashId ||
//                        htProductAddResponse.getError_code().contains(DUPLICATE_PRODUCT_NAME) ||
                        htProductAddResponse.getError_code().contains(DUPLICATE_PRODUCT_DRAFT_NAME)||
                        htProductAddResponse.getBody().contains(INVALID_PRODUCT_STATUS))
                {
                    // 上新成功但没取到jmHashId等值得时候，不需要重新上新
                    if (!jmApiErrorNoHashId) {
                        needRetry = true;
                    }

                    // 调用聚美API根据商品名称获取商品详情(/v1/htProduct/getProductByIdOrName)
                    JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductByName(shop, bean.getName() );
                    if(jmGetProductInfoRes != null)
                    {
                        originHashId = jmGetProductInfoRes.getHash_ids();
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
                        jmCart.setpProductId(jmProductId);
                        jmCart.setpNumIId(originHashId);
                        saveProductPlatform(channelId, product);


                        sxData.getPlatform().setPublishTime(DateTimeUtil.getNowTimeStamp());
                        sxData.getPlatform().setPlatformStatus(CmsConstants.PlatformStatus.InStock);
                        sxData.getPlatform().setInStockTime(DateTimeUtil.getNowTimeStamp());
                        sxData.getPlatform().setModifier(getTaskName());
                        sxData.getPlatform().setNumIId(originHashId);
                        sxData.getPlatform().setPlatformPid(jmProductId);

                        productGroupService.updateGroupsPlatformStatus(sxData.getPlatform(), listSxCode);

                        // added by morse.lu 2016/08/30 start
                        uploadMall(product, shop, expressionParser, null, null);
                        // added by morse.lu 2016/08/30 end
                    }
                    else
                    {
                        String msg = String.format("读取聚美产品信息失败！[ProductId:%s], [ChannelId:%s], [CartId:%s]", product.getProdId(), channelId, CART_ID);
                        $error(msg);
                        throw  new BusinessException(msg);
                    }
                }
                //上新失败
                else
                {
                    String errMsg = "";
                    // 如果是错误代码是"109902"(HTML解析错误)的时候
                    if (htProductAddResponse.getError_code().contains(INVALID_HTML_CONTENT)) {
                        errMsg = "Master产品详情中的简短描述,详情描述或聚美使用方法等中英文项目的HTML内容语法解析错误！";
                    }
                    String msg = String.format("聚美新增产品上新失败！%s [ProductId:%s], [Message:%s]", errMsg, product.getProdId(), htProductAddResponse.getErrorMsg());
                    $error(msg);
                    throw  new BusinessException(msg);
                }

            }
            //更新产品
            else {
                //先去聚美查一下product
                JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductById(shop, jmCart.getpProductId() );
                List<JmGetProductInfo_Spus> remoteSpus = null;
                if(jmGetProductInfoRes != null)
                {
                    remoteSpus = jmGetProductInfoRes.getSpus();
                }
                if(remoteSpus == null)
                {
                    remoteSpus = new ArrayList<>();
                }

                // 补全两个属性, 这两个属性最终会回写到数据库中 START
				for (BaseMongoMap map : product.getPlatform(CART_ID).getSkus()) {
					if (remoteSpus.stream().filter(w -> w.getBusinessman_code().equals(map.getStringAttribute("skuCode"))).count() > 0) {
						JmGetProductInfo_Spus result = remoteSpus.stream().filter(w -> w.getBusinessman_code().equals(map.getStringAttribute("skuCode"))).findFirst().get();

						if (!map.containsKey("jmSpuNo") || StringUtils.isEmpty(map.getStringAttribute("jmSpuNo"))) {
							if (!StringUtils.isEmpty(result.getSpu_no())) {
								map.put("jmSpuNo", result.getSpu_no());
							}
						}

						if (!map.containsKey("jmSkuNo") || StringUtils.isEmpty(map.getStringAttribute("jmSkuNo"))) {
							if (result.getSku_list().size() > 0 && !StringUtils.isEmpty(result.getSku_list().get(0).getSku_no())) {
								map.put("jmSkuNo", result.getSku_list().get(0).getSku_no());
							}
						}

						// 20161002 tom 增加一个补丁 START
						// 首先刚才用sku找到的这条记录, 我们认为是正确的SPU
						String barcode = product.getCommon().getSkus().stream().filter(w -> w.getSkuCode().equals(map.getStringAttribute("skuCode"))).findFirst().get().getBarcode();
						String voToBarcode = addVoToBarcode(barcode, channelId, map.getStringAttribute("skuCode"));

						// 我们判断一下这个SPU的【商品自带条码】是否符合我们的起名规则, 如果不一样, 那么我们就需要改正它
						if (!voToBarcode.equals(result.getUpc_code())) {

							// 但是如果remote里, 已经有其他SPU占用了这个SPU的【商品自带条码】的话,  那么那个就认为是错误的SPU, 需要将其变为ERROR
							long cnt = remoteSpus.stream().filter(w -> w.getUpc_code().equals(voToBarcode)).count();
							if (cnt > 0) {
								JmGetProductInfo_Spus resultError = remoteSpus.stream().filter(w -> w.getUpc_code().equals(voToBarcode)).findFirst().get();
								updateErrSpuUpcCode(shop, resultError.getSpu_no(), resultError.getUpc_code());
							}

							// 然后再把当前的这个SPU的【商品自带条码】改成正确的
							result.setUpc_code(voToBarcode);
							updateRealSpuUpcCode(shop, result.getSpu_no(), result.getUpc_code());
						}
						// 20161002 tom 增加一个补丁 END
					}
				}
				// 补全两个属性, 这两个属性最终会回写到数据库中 END
				// 20161002 tom 除了上面这段根据sku来查找, 还应该再根据我们起名规则的商品自带条码再找一遍(这就是SPU存在, 但是SKU不存在的场合) START
				for (BaseMongoMap map : product.getPlatform(CART_ID).getSkus()) {
					if (!map.containsKey("jmSpuNo") || StringUtils.isEmpty(map.getStringAttribute("jmSpuNo"))) {
						// 预想的SPU的barcode
						String barcode = product.getCommon().getSkus().stream().filter(w -> w.getSkuCode().equals(map.getStringAttribute("skuCode"))).findFirst().get().getBarcode();
						String voToBarcode = addVoToBarcode(barcode, channelId, map.getStringAttribute("skuCode"));

						if (remoteSpus.stream().filter(w -> w.getUpc_code().equals(voToBarcode)).count() > 0) {
							JmGetProductInfo_Spus result = remoteSpus.stream().filter(w -> w.getUpc_code().equals(voToBarcode)).findFirst().get();
							if (!StringUtils.isEmpty(result.getSpu_no())) {
								map.put("jmSpuNo", result.getSpu_no());
							}
						}

					}

				}
				// 20161002 tom 除了上面这段根据sku来查找, 还应该再根据我们起名规则的商品自带条码再找一遍(这就是SPU存在, 但是SKU不存在的场合) END

                // added by morse.lu 2016/09/01 start
                // 追加的skuCode列表
                List<String> addSkuList = new ArrayList<>();
                // added by morse.lu 2016/09/01 end

                //如果OriginHashId存在，则修改商品属性
                CmsBtProductModel_Field fields = product.getCommon().getFields();
                BaseMongoMap<String, Object> jmFields = jmCart.getFields();
                // delete by desmond 2016/07/08 start
//                        String brandName = fields.getBrand();
//                        String productType = fields.getProductType();
//                        String sizeType = fields.getSizeType();
                // delete by desmond 2016/07/08 end

                //查询jm_product
                CmsBtJmProductModel jmProductModel = getCmsBtJmProductModel(channelId, productCode);
                boolean needAdd = false;
                if(jmProductModel == null)
                {
                    needAdd =  true;
                }
                jmProductModel = fillCmsBtJmProductModel(jmProductModel, product);

                HtProductUpdateRequest htProductUpdateRequest = fillHtProductUpdateRequest(product, expressionParser, shop);
                HtProductUpdateResponse htProductUpdateResponse = jumeiHtProductService.update(shop, htProductUpdateRequest);

                if (htProductUpdateResponse != null && htProductUpdateResponse.getIs_Success()) {
                    $info("更新产品成功！[ProductId:%s], [ChannelId:%s], [CartId:%s]", product.getProdId(), channelId, CART_ID);

                    //回写数据库
                    if (!needAdd) {
                        cmsBtJmProductDao.update(jmProductModel);
                    }
                    else
                    {
                        jmProductModel.setJumeiProductId(jmCart.getpProductId());
                        jmProductModel.setOriginJmHashId(jmCart.getpNumIId());
                        cmsBtJmProductDao.insert(jmProductModel);
                    }

                    //查询MySQL库SPU
                    List<CmsBtJmSkuModel> skuList = getCmsBtJmSkuModels(channelId, productCode);

                    List<BaseMongoMap<String, Object>> newSkuList = jmCart.getSkus();
                    List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();
                    newSkuList = mergeSkuAttr(newSkuList, commonSkus);



                    for (BaseMongoMap<String, Object> skuMap : newSkuList) {

                        String skuCode = skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                        //旧SPU需要更新
//                        if (remoteSpus.stream().filter(w -> w.getBusinessman_code().equals(skuCode)).count() > 0) {
                        if (remoteSpus.stream().filter(w -> w.getUpc_code().equals(addVoToBarcode(skuMap.getStringAttribute("barcode"), channelId, skuCode))).count() > 0) {
//                            JmGetProductInfo_Spus oldSku = remoteSpus.stream().filter(w -> w.getBusinessman_code().equals(skuCode)).findFirst().get();
                            JmGetProductInfo_Spus oldSku = remoteSpus.stream().filter(w -> w.getUpc_code().equals(addVoToBarcode(skuMap.getStringAttribute("barcode"), channelId, skuCode))).findFirst().get();
                            String jmSpuNo = oldSku.getSpu_no();
                            HtSpuUpdateRequest htSpuUpdateRequest = new HtSpuUpdateRequest();
                            htSpuUpdateRequest.setJumei_spu_no(jmSpuNo);
                            DecimalFormat dformat = new DecimalFormat(".00");
                            String priceStr = dformat.format(Math.ceil(skuMap.getDoubleAttribute("clientMsrpPrice")));
                            htSpuUpdateRequest.setAbroad_price(Double.valueOf(priceStr));
                            htSpuUpdateRequest.setAttribute(jmFields.getStringAttribute("attribute"));
                            htSpuUpdateRequest.setProperty(skuMap.getStringAttribute("property"));
                            // update by desmond 2016/07/08 start
//                                    String sizeStr = skuMap.getStringAttribute("size");
//                                    sizeStr = getSizeFromSizeMap(sizeStr, channelId, brandName, productType, sizeType);
                            String sizeStr = skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
                            // update by desmond 2016/07/08 end
                            htSpuUpdateRequest.setSize(sizeStr);
                            htSpuUpdateRequest.setUpc_code(addVoToBarcode(skuMap.getStringAttribute("barcode"), channelId, skuCode));
//                                  htSpuUpdateRequest.setArea_code(19);//TODO

                            HtSpuUpdateResponse htSpuUpdateResponse = jumeiHtSpuService.update(shop, htSpuUpdateRequest);
                            if (htSpuUpdateResponse != null && htSpuUpdateResponse.is_Success()) {
                                $info("更新Spu成功！[ProductId:%s], [JmSpuNo:%s]", product.getProdId(), jmSpuNo);
                                //如果mysql库中有这条sku
                                if(skuList.stream().filter(w -> w.getSkuCode().equals(skuCode)).count() > 0)
                                {
                                    CmsBtJmSkuModel mySku = skuList.stream().filter(w -> w.getSkuCode().equals(skuCode)).findFirst().get();
                                    mySku.setJmSize(sizeStr);
                                    mySku.setCmsSize(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.size.name()));
                                    mySku.setMsrpUsd(new BigDecimal(skuMap.getDoubleAttribute("clientMsrpPrice")));
                                    mySku.setModifier(getTaskName());
                                    cmsBtJmSkuDao.update(mySku);
                                }
                                else
                                {
                                    //如果MySQL库中没有这条SPU,则新增一条
                                    CmsBtJmSkuModel mySku = fillNewCmsBtJmSkuModel(channelId, productCode, skuMap , sizeStr);
                                    mySku.setJmSpuNo(oldSku.getSpu_no());
                                    mySku.setJmSkuNo(oldSku.getSku_no());
                                    mySku.setModifier(getTaskName());
                                    mySku.setModified(new Date());
                                    cmsBtJmSkuDao.insert(mySku);
                                }
                            }
                            //更新Spu失败
                            else
                            {
                                String msg = String.format("更新Spu失败！[ProductId:%s], [Message:%s]", product.getProdId(), htSpuUpdateResponse.getErrorMsg());
                                $error(msg);
                                throw  new BusinessException(msg);
                            }
                            //检查Remote SPU是否有sku属性，如果没有，则添加SKU
                            if(StringUtils.isNullOrBlank2(oldSku.getSku_no()))
                            {
								addSkuList.add(skuCode);

//								//需要增加SKU
//                                HtSkuAddRequest htSkuAddRequest = new HtSkuAddRequest();
////                                htSkuAddRequest.setCustoms_product_number(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
//                                htSkuAddRequest.setCustoms_product_number(" ");
//                                htSkuAddRequest.setSale_on_this_deal("1");
//                                htSkuAddRequest.setBusinessman_num(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
//                                htSkuAddRequest.setStocks(String.valueOf(skuLogicQtyMap.get(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))));
//                                htSkuAddRequest.setDeal_price(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));
//                                htSkuAddRequest.setMarket_price(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));
//                                htSkuAddRequest.setJumei_hash_id(originHashId);
//                                htSkuAddRequest.setJumei_spu_no(oldSku.getSpu_no());
//                                HtSkuAddResponse htSkuAddResponse = jumeiHtSkuService.add(shop, htSkuAddRequest);
//                                if (htSkuAddResponse != null && htSkuAddResponse.is_Success()) {
//                                    $info("增加Sku成功！[skuCode:%s]", skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
//                                    if(skuList.stream().filter(w -> w.getSkuCode().equals(skuCode)).count() > 0)
//                                    {
//                                        CmsBtJmSkuModel mySku = skuList.stream().filter(w -> w.getSkuCode().equals(skuCode)).findFirst().get();
//                                        mySku.setJmSkuNo(htSkuAddResponse.getJumei_sku_no());
//                                        mySku.setModifier(getTaskName());
//                                        mySku.setModified(new Date());
//                                        cmsBtJmSkuDao.update(mySku);
//                                    }
//                                }
//                                //增加Sku失败
//                                else
//                                {
//                                    String msg = String.format("增加Sku失败！[ProductId:%s], [Message:%s]", product.getProdId(), htSkuAddResponse.getErrorMsg());
//                                    $error(msg);
//                                    throw  new BusinessException(msg);
//                                }
                            }
                        }
                        //新SPU需要增加
                        else {
                            HtSpuAddRequest htSpuAddRequest = new HtSpuAddRequest();
                            htSpuAddRequest.setUpc_code(addVoToBarcode(skuMap.getStringAttribute("barcode"), channelId, skuCode));
                            // update by desmond 2016/07/08 start
//                                    String sizeStr = skuMap.getStringAttribute("size");
//                                    htSpuAddRequest.setSize(getSizeFromSizeMap(sizeStr, channelId, brandName, productType, sizeType));
                            String sizeStr = skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
                            htSpuAddRequest.setSize(sizeStr);
                            // update by desmond 2016/07/08 end
                            DecimalFormat dformat = new DecimalFormat(".00");
                            String priceStr = dformat.format(Math.ceil(skuMap.getDoubleAttribute("clientMsrpPrice")));
                            htSpuAddRequest.setAbroad_price(priceStr);
                            htSpuAddRequest.setArea_code("19");//TODO
                            htSpuAddRequest.setJumei_product_id(jmCart.getpProductId());
                            htSpuAddRequest.setProperty(skuMap.getStringAttribute("property"));
                            htSpuAddRequest.setAttribute(jmFields.getStringAttribute("attribute"));
                            HtSpuAddResponse htSpuAddResponse = jumeiHtSpuService.add(shop, htSpuAddRequest);

                            if (htSpuAddResponse != null && htSpuAddResponse.is_Success()) {
                                $info("新增Spu成功！[ProductId:%s], [JmSpuNo:%s]", product.getProdId(), htSpuAddResponse.getJumei_spu_no());
                                skuMap.setStringAttribute("jmSpuNo", htSpuAddResponse.getJumei_spu_no());

                                // deleted by morse.lu 2016/09/01 start
                                // 为了聚美商城能够追加sku，暂时删除，以后聚美结构会改，还会恢复
                                // added by morse.lu 2016/09/01 start
                                // 追加成功时才加进list
                                addSkuList.add(skuCode);
                                // added by morse.lu 2016/09/01 end
//                                HtSkuAddRequest htSkuAddRequest = new HtSkuAddRequest();
////                                htSkuAddRequest.setCustoms_product_number(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
//                                htSkuAddRequest.setCustoms_product_number(" ");
//                                htSkuAddRequest.setSale_on_this_deal("1");
//                                htSkuAddRequest.setBusinessman_num(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
//                                htSkuAddRequest.setStocks(String.valueOf(skuLogicQtyMap.get(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))));
//                                htSkuAddRequest.setDeal_price(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));
//                                htSkuAddRequest.setMarket_price(skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));
//                                htSkuAddRequest.setJumei_hash_id(originHashId);
//                                htSkuAddRequest.setJumei_spu_no(htSpuAddResponse.getJumei_spu_no());
//                                HtSkuAddResponse htSkuAddResponse = jumeiHtSkuService.add(shop, htSkuAddRequest);
//                                if (htSkuAddResponse != null && htSkuAddResponse.is_Success()) {
//                                    $info("增加Sku成功！[skuCode:%s]", skuMap.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
//
//                                    CmsBtJmSkuModel cmsBtJmSkuModel = fillNewCmsBtJmSkuModel(channelId, productCode, skuMap , sizeStr);
//                                    cmsBtJmSkuModel.setJmSpuNo(htSpuAddResponse.getJumei_spu_no());
//                                    cmsBtJmSkuModel.setJmSkuNo(htSkuAddResponse.getJumei_sku_no());
//                                    cmsBtJmSkuDao.insert(cmsBtJmSkuModel);
//
//                                    skuMap.setStringAttribute("jmSkuNo", htSkuAddResponse.getJumei_sku_no());
//
//                                }
//                                //增加Sku失败
//                                else
//                                {
//                                    String msg = String.format("增加Sku失败！[ProductId:%s], [Message:%s]", product.getProdId(), htSkuAddResponse.getErrorMsg());
//                                    $error(msg);
//                                    throw  new BusinessException(msg);
//                                }
                                // deleted by morse.lu 2016/09/01 end
                            }
                            //新增Spu失败
                            else
                            {
                                String msg = String.format("新增Spu失败！[ProductId:%s], [Message:%s]", product.getProdId(), htSpuAddResponse.getErrorMsg());
                                $error(msg);
                                throw  new BusinessException(msg);
                            }
                        }
                        //更新SKU,然而在当前版本中SkuCode是不能改的。所以删掉了更新sku的逻辑
                    }

                    // 获取jm_hash_id列表（去掉cms_bt_jm_promotion表中已删除的活动(status=0)和hashId为空的记录，并根据created降序来排列）
                    List<String> jmHashIdList = cmsBtJmPromotionProductDaoExt.selectJmHashIds(channelId, productCode, DateTimeUtilBeijing.getCurrentBeiJingDate());
                    $info("已经存在的聚美Deal的size:" + jmHashIdList.size() + ",对应的productCode:" + productCode);
                    // 如果没找到活动对应的jm_hash_id，用originHashId
                    if (jmHashIdList.size() == 0)
                        jmHashIdList.add(originHashId);

                    // 取得第一条当前有效活动最晚创建的第一条jm_hash_id即可，只可能有一个jm_hash_id，不用循环了
                    String hashId = jmHashIdList.get(0);
//                    for (String hashId : jmHashIdList) {
                        $info("更新Deal的hashId:" + hashId);
                        HtDealUpdateRequest htDealUpdateRequest = fillHtDealUpdateRequest(product,hashId,expressionParser,shop);
                        HtDealUpdateResponse htDealUpdateResponse = jumeiHtDealService.update(shop, htDealUpdateRequest);
                        if (htDealUpdateResponse != null && htDealUpdateResponse.is_Success()) {
                            $info("聚美更新Deal成功！[ProductId:%s]", product.getProdId());
                        }
                        //更新Deal失败
                        else
                        {
                            String msg = String.format("聚美更新Deal失败！[ProductId:%s], [Message:%s]", product.getProdId(), htDealUpdateResponse.getErrorMsg());
                            $error(msg);
                            throw  new BusinessException(msg);
                        }
//                    }
                }
                //更新产品失败
                else
                {
                    String msg = String.format("聚美更新产品上新失败！[ProductId:%s], [Message:%s]", product.getProdId(), htProductUpdateResponse.getErrorMsg());
                    $error(msg);
                    throw  new BusinessException(msg);
                }

                //保存product到MongoDB
                saveProductPlatform(channelId, product);
                sxData.getPlatform().setPublishTime(DateTimeUtil.getNowTimeStamp());
                sxData.getPlatform().setModifier(getTaskName());

                productGroupService.updateGroupsPlatformStatus(sxData.getPlatform(), listSxCode);

                // add by desmond 2016/09/29 start
                // 批量修改deal市场价(为了后面uploadMall时不报商城市场价与团购市场价不一致的错误，即使异常也继续uploadMall)
                updateDealPriceBatch(shop, product, true, false);
                // add by desmond 2016/09/29 end

                // added by morse.lu 2016/08/30 start
                uploadMall(product, shop, expressionParser, addSkuList, skuLogicQtyMap);
                // added by morse.lu 2016/08/30 end

                // 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中不存在对应的SkuCode，则在平台上隐藏该商品编码并把库存改为0
                doHideNotExistSkuDeal(shop, originHashId, remoteSpus, product.getPlatform(CART_ID).getSkus());
                // 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中不存在对应的SkuCode，则在聚美商城上隐藏该商品编码并把库存改为0
                if (!StringUtils.isEmpty(product.getPlatform(CART_ID).getpPlatformMallId()))
                    doHideNotExistSkuMall(shop, remoteSpus, product.getPlatform(CART_ID).getSkus());
            }

            //保存workload
            if(needRetry)
            {
                //需要重试
                delayWorkload(work);
                $info("workload需要重试！[workId:%s][groupId:%s]", work.getId(), work.getGroupId());
                return;
            }

            saveWorkload(work, WORK_LOAD_SUCCESS);

            $info("保存workload成功！[workId:%s][groupId:%s]", work.getId(), work.getGroupId());

        }
        catch (ServerErrorException se) {
            //需要重试
            delayWorkload(work);
            $info("workload需要重试！[workId:%s][groupId:%s]", work.getId(), work.getGroupId());
        }
        catch (Exception e) {

            if(sxData == null)
            {
                sxData = new SxData();
                sxData.setCartId(CART_ID);
                sxData.setChannelId(work.getChannelId());
                sxData.setGroupId(work.getGroupId());
            }

            if (e instanceof BusinessException && StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setErrorMessage(e.getMessage());
            }

            //保存错误log
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isNullOrBlank2(sxData.getErrorMessage())) {
                if(StringUtils.isNullOrBlank2(e.getMessage())) {
                    sxData.setErrorMessage("聚美上新出现不可预知的错误，请跟管理员联系 " + e.getStackTrace()[0].toString());
                    $error(sxData.getErrorMessage());
                }
                else
                {
                    sxData.setErrorMessage(e.getMessage());
                }
            }
            // 上新失败后回写product表pPublishError的值("Error")和pPublishMessage(上新错误信息)
            productGroupService.updateUploadErrorStatus(sxData.getPlatform(), sxData.getErrorMessage());

            // 插入错误消息
            sxProductService.insertBusinessLog(sxData, getTaskName());
            //保存workload
            saveWorkload(work, WORK_LOAD_FAIL);

            e.printStackTrace();
            $error("workload上新失败！[workId:%s][groupId:%s]", work.getId(), work.getGroupId());
        }

    }


    /**
     * @param hashId
     * @param expressionParser
     * @param shopProp
     * @return
     * @throws Exception
     */
    private HtDealUpdateRequest fillHtDealUpdateRequest(CmsBtProductModel product, String hashId, ExpressionParser expressionParser, ShopBean shopProp) throws Exception {
        String channelId = product.getChannelId();
        BaseMongoMap<String, Object> jmFields = product.getPlatform(CART_ID).getFields();

        HtDealUpdateRequest htDealUpdateRequest = new HtDealUpdateRequest();
        htDealUpdateRequest.setJumei_hash_id(hashId);
        HtDealUpdate_DealInfo dealInfo = new HtDealUpdate_DealInfo();
        String shippingId = Codes.getCode("JUMEI", channelId);
        dealInfo.setShipping_system_id(NumberUtils.toInt(shippingId));
        dealInfo.setProduct_long_name(jmFields.getStringAttribute("productLongName"));
        dealInfo.setProduct_medium_name(jmFields.getStringAttribute("productMediumName"));
        dealInfo.setProduct_short_name(jmFields.getStringAttribute("productShortName"));
        dealInfo.setBefore_date(jmFields.getStringAttribute("beforeDate"));
        dealInfo.setSuit_people(jmFields.getStringAttribute("suitPeople"));
        dealInfo.setSpecial_explain(jmFields.getStringAttribute("specialExplain"));
        dealInfo.setSearch_meta_text_custom(jmFields.getStringAttribute("searchMetaTextCustom"));
//        dealInfo.setAttribute(jmFields.getStringAttribute("attribute"));
        dealInfo.setUser_purchase_limit(jmFields.getIntAttribute("userPurchaseLimit"));

        String jmDetailTemplate = getTemplate("聚美详情", expressionParser, shopProp);
        dealInfo.setDescription_properties(jmDetailTemplate);
        String jmProductTemplate = getTemplate("聚美实拍", expressionParser, shopProp);
        dealInfo.setDescription_images(jmProductTemplate);
        String jmUseageTemplate = getTemplate("聚美使用方法", expressionParser, shopProp);
        dealInfo.setDescription_usage(jmUseageTemplate);

        List<BaseMongoMap<String, Object>> skuList = product.getPlatform(CART_ID).getSkus();

        List<String> jmSkuNoList = skuList.stream().filter(p -> !StringUtils.isEmpty(p.getStringAttribute("jmSkuNo")))
                .map(w->w.getStringAttribute("jmSkuNo")).collect(Collectors.toList());
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


        List<BaseMongoMap<String, Object>>   jmSkus  = product.getPlatform(CART_ID).getSkus();
        List<BaseMongoMap<String, Object>>   newJmSkus =  new ArrayList<>();
        for (BaseMongoMap<String, Object> sku : jmSkus)
        {
            BaseMongoMap<String, Object> newSku = new  BaseMongoMap<String, Object>();
            newSku.setStringAttribute("skuCode", sku.getStringAttribute("skuCode"));
            newSku.setAttribute("priceMsrp", sku.getDoubleAttribute("priceMsrp"));
            newSku.setAttribute("priceRetail", sku.getDoubleAttribute("priceRetail"));
            newSku.setAttribute("priceSale", sku.getDoubleAttribute("priceSale"));
            newSku.setStringAttribute("priceChgFlg", sku.getStringAttribute("priceChgFlg"));
            newSku.setStringAttribute("priceDiffFlg", sku.getStringAttribute("priceDiffFlg"));
            newSku.setAttribute("isSale", sku.getAttribute("isSale"));
            newSku.setStringAttribute("jmSpuNo", sku.getStringAttribute("jmSpuNo"));
            newSku.setStringAttribute("jmSkuNo", sku.getStringAttribute("jmSkuNo"));
            newSku.setStringAttribute("property", sku.getStringAttribute("property"));
            newJmSkus.add(newSku);
        }

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", product.getProdId());

        rsMap.put("platforms.P" + CART_ID + ".skus", newJmSkus);
        rsMap.put("platforms.P" + CART_ID + ".pProductId", product.getPlatform(CART_ID).getpProductId());
        rsMap.put("platforms.P" + CART_ID + ".pNumIId", product.getPlatform(CART_ID).getpNumIId());


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
     * @param expressionParser
     * @param shopProp
     * @return
     * @throws Exception
     */
    private HtProductUpdateRequest fillHtProductUpdateRequest(CmsBtProductModel product,
                                                              ExpressionParser expressionParser, ShopBean shopProp) throws Exception {

        CmsBtProductModel_Field fields = product.getCommon().getFields();
        CmsBtProductModel_Platform_Cart jmCart = product.getPlatform(CART_ID);
        HtProductUpdateRequest htProductUpdateRequest = new HtProductUpdateRequest();
        String productId = jmCart.getpProductId();
        BaseMongoMap<String, Object> jmFields = jmCart.getFields();
        String productName = jmFields.getStringAttribute("productNameCn") + " " + special_symbol.matcher(fields.getCode()).replaceAll("-");
        htProductUpdateRequest.setJumei_product_name(productName);
        htProductUpdateRequest.setJumei_product_id(productId);
        HtProductUpdate_ProductInfo productInfo = new HtProductUpdate_ProductInfo();
        productInfo.setBrand_id(jmCart.getpBrandId());
        productInfo.setCategory_v3_4_id(jmCart.getpCatId());
        productInfo.setForeign_language_name(jmFields.get("productNameEn").toString());
        productInfo.setName(productName);


        //商品主图
        String picTemplate = getTemplate("聚美白底方图", expressionParser, shopProp);

        if (!StringUtils.isNullOrBlank2(picTemplate)) {
            picTemplate = picTemplate.substring(0, picTemplate.lastIndexOf(","));
            productInfo.setNormalImage(picTemplate);
        }
        htProductUpdateRequest.setUpdate_data(productInfo);


        return htProductUpdateRequest;
    }


    /**
     * 填充JmProductBean
     *
     * @param product
     * @return
     * @throws Exception
     */
    private JmProductBean fillJmProductBean(CmsBtProductModel product,ExpressionParser expressionParser, ShopBean shopProp, Map<String, Integer> skuLogicQtyMap) throws Exception {

        String channelId = product.getChannelId();
        CmsBtProductModel_Platform_Cart jmCart = product.getPlatform(CartEnums.Cart.JM);

        JmProductBean bean = new JmProductBean();

        //先从fields读，以后会从common.fields
        CmsBtProductModel_Field fields = product.getCommon().getFields();
        // delete by desmond 2016/07/08 start
//        String brandName = fields.getBrand();
//        String productType = fields.getProductType();
//        String sizeType = fields.getSizeType();
        // delete by desmond 2016/07/08 end
        String productCode = fields.getCode();

        BaseMongoMap<String, Object> jmFields = jmCart.getFields();

        bean.setProduct_spec_number(fields.getCode());
        // update by desmond 2016/09/01 start
//        bean.setCategory_v3_4_id(Integer.valueOf(jmCart.getpCatId()));
        bean.setCategory_v3_4_id(NumberUtils.toInt(jmCart.getpCatId()));
//        bean.setBrand_id(Integer.valueOf(jmCart.getpBrandId()));
        bean.setBrand_id(NumberUtils.toInt(jmCart.getpBrandId()));
        // update by desmond 2016/09/01 end
        bean.setName(jmFields.getStringAttribute("productNameCn") + " " +  special_symbol.matcher(productCode).replaceAll("-"));
        bean.setForeign_language_name(jmFields.getStringAttribute("productNameEn"));
        //白底方图
        String picTemplate = getTemplate("聚美白底方图", expressionParser, shopProp);

        if (!StringUtils.isNullOrBlank2(picTemplate)) {
            picTemplate = picTemplate.substring(0, picTemplate.lastIndexOf(","));
            bean.setNormalImage(picTemplate);
        }

        JmProductBean_DealInfo deal = new JmProductBean_DealInfo();
        deal.setPartner_deal_id(productCode + "-" + channelId + "-" + CART_ID);

        deal.setUser_purchase_limit(jmFields.getIntAttribute("userPurchaseLimit"));

        String shippingId = Codes.getCode("JUMEI", channelId);
        deal.setShipping_system_id(NumberUtils.toInt(shippingId));


        String jmDetailTemplate = getTemplate("聚美详情", expressionParser, shopProp);
        deal.setDescription_properties(jmDetailTemplate);
        String jmProductTemplate = getTemplate("聚美实拍", expressionParser, shopProp);
        deal.setDescription_images(jmProductTemplate);
        String jmUseageTemplate = getTemplate("聚美使用方法", expressionParser, shopProp);
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
        // edward 2016-07-11 时间从30分钟改成3分钟
        rightNow.add(Calendar.MINUTE, 3);
        deal.setEnd_time(rightNow.getTimeInMillis() / 1000);
        List<String> skuCodeList = product.getCommon().getSkus().stream().filter(p -> !StringUtils.isEmpty(p.getStringAttribute("skuCode")))
                .map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList());
        String skuString = Joiner.on(",").join(skuCodeList);
        deal.setPartner_sku_nos(skuString);
        deal.setRebate_ratio("1");
        bean.setDealInfo(deal);


        List<BaseMongoMap<String, Object>> jmSkus = jmCart.getSkus();
        List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();

        //合并Common和平台的Sku属性
        jmSkus = mergeSkuAttr(jmSkus, commonSkus);

        List<JmProductBean_Spus> spus = new ArrayList<>();

        for (BaseMongoMap<String, Object> jmSku : jmSkus) {
            JmProductBean_Spus spu = new JmProductBean_Spus();
            spu.setPartner_spu_no(jmSku.getStringAttribute("skuCode"));
            spu.setUpc_code(addVoToBarcode(jmSku.getStringAttribute("barcode"), channelId, jmSku.getStringAttribute("skuCode")));
            spu.setPropery(jmSku.getStringAttribute("property"));
            spu.setAttribute(jmFields.getStringAttribute("attribute"));//Code级
            // update by desmond 2016/07/08 start
//            String size = jmSku.getStringAttribute("size");
//            String  sizeStr = getSizeFromSizeMap(size, channelId, brandName, productType, sizeType);
            String sizeStr = jmSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
            // update by desmond 2016/07/08 end
            spu.setSize(sizeStr);

            spu.setAbroad_price(Math.ceil(jmSku.getDoubleAttribute("clientMsrpPrice")));
            spu.setArea_code("19"); //TODO

            JmProductBean_Spus_Sku jmSpuSku = new JmProductBean_Spus_Sku();
            jmSpuSku.setPartner_sku_no(jmSku.getStringAttribute("skuCode"));
            jmSpuSku.setSale_on_this_deal("1");
//            jmSpuSku.setCustoms_product_number(jmSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
            jmSpuSku.setCustoms_product_number(" ");
            jmSpuSku.setBusinessman_num(jmSku.getStringAttribute("skuCode"));
            Integer stock = skuLogicQtyMap.get(jmSku.getStringAttribute("skuCode"));
            jmSpuSku.setStocks(String.valueOf(stock));

            jmSpuSku.setDeal_price(jmSku.getStringAttribute("priceSale"));
            jmSpuSku.setMarket_price(jmSku.getStringAttribute("priceMsrp"));

            spu.setSkuInfo(jmSpuSku);
            spus.add(spu);
        }



        bean.setSpus(spus);


        return bean;
    }


    /**
     * 读取字典的模板，如果返回空字符串则抛出异常
     *
     * @param dictName
     * @param expressionParser
     * @param shopProp
     * @return
     * @throws Exception
     */
    private String getTemplate(String dictName, ExpressionParser expressionParser, ShopBean shopProp) throws Exception {
        String result = sxProductService.resolveDict(dictName, expressionParser, shopProp, getTaskName(), null);
        if(StringUtils.isNullOrBlank2(result))
        {
            String errorMsg = String.format("字典解析器说:解析的结果是空的! (猜测有可能是素材管理里的共通图片啥的没有一张图片成功上传到平台) [dictName:%s],[ProdId:%s]:", dictName, expressionParser.getSxData().getMainProduct().getProdId());
            throw new BusinessException(errorMsg);
        }
        return  result;
    }


    /**
     * 填充skulist
     *
     * @param list
     * @param product
     * @return
     */
    private List<CmsBtJmSkuModel> fillCmsBtJmSkuModelList(List<CmsBtJmSkuModel> list, CmsBtProductModel product)
    {
        String channelId =  product.getChannelId();
        CmsBtProductModel_Field fields =  product.getCommon().getFields();
        String productCode = fields.getCode();

        if(list == null)
        {
            list  = new ArrayList<>();
        }

        List<BaseMongoMap<String, Object>> jmSkus = product.getPlatform(CART_ID).getSkus();
        List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();

        //合并Common和平台的Sku属性
        jmSkus = mergeSkuAttr(jmSkus, commonSkus);


        for (BaseMongoMap<String, Object> jmSku : jmSkus) {
            //填写CmsBtJMSku
            // update by desmond 2016/07/08 start
//            String size = jmSku.getStringAttribute("size");
//            String  sizeStr = getSizeFromSizeMap(size, channelId, brandName, productType, sizeType);
            String  sizeStr = jmSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
            // update by desmond 2016/07/08 end
            CmsBtJmSkuModel cmsBtJmSkuModel = fillNewCmsBtJmSkuModel(channelId, productCode, jmSku, sizeStr);
            list.add(cmsBtJmSkuModel);
        }

        return list;
    }


    /**
     * 填充CmsBtJmProductModel
     *
     * @param cmsBtJmProductModel
     * @param product
     * @return
     */
    private  CmsBtJmProductModel fillCmsBtJmProductModel(CmsBtJmProductModel cmsBtJmProductModel, CmsBtProductModel product)
    {
        if(cmsBtJmProductModel == null)
        {
            cmsBtJmProductModel = new CmsBtJmProductModel();
            cmsBtJmProductModel.setCreated(new Date());
            cmsBtJmProductModel.setModifier(getTaskName());
            cmsBtJmProductModel.setCreater(getTaskName());
        }

        //填充cmsBtJmProductModel
        String channelId =  product.getChannelId();
        CmsBtProductModel_Field fields =  product.getCommon().getFields();
        String productCode = fields.getCode();
        String brandName = fields.getBrand();
        String productType = fields.getProductType();
        String sizeType = fields.getSizeType();
        BaseMongoMap<String, Object> jmFields = product.getPlatform(CART_ID).getFields();

        cmsBtJmProductModel.setChannelId(channelId);
        cmsBtJmProductModel.setProductCode(productCode);
        cmsBtJmProductModel.setOrigin(fields.getOrigin());
        cmsBtJmProductModel.setProductNameCn(jmFields.getStringAttribute("productNameCn") + " " + special_symbol.matcher(productCode).replaceAll("-"));
        cmsBtJmProductModel.setVoBrandName(brandName);                                   // VO系统里面的品牌名称
        cmsBtJmProductModel.setVoCategoryName(product.getCommon().getCatPath());
        cmsBtJmProductModel.setBrandName(product.getPlatform(CART_ID).getpBrandName());  // 聚美平台上的品牌名称
        cmsBtJmProductModel.setProductType(productType);
        cmsBtJmProductModel.setSizeType(sizeType);
        cmsBtJmProductModel.setProductDesEn(fields.getShortDesEn());
        cmsBtJmProductModel.setAttribute(jmFields.getStringAttribute("attribute"));
        cmsBtJmProductModel.setForeignLanguageName(jmFields.getStringAttribute("productNameEn"));
        cmsBtJmProductModel.setAddressOfProduce(jmFields.getStringAttribute("originCn"));
        cmsBtJmProductModel.setAvailablePeriod(jmFields.getStringAttribute("beforeDate"));
        cmsBtJmProductModel.setProductDesCn(fields.getLongDesCn());
        cmsBtJmProductModel.setApplicableCrowd(jmFields.getStringAttribute("suitPeople"));
        cmsBtJmProductModel.setSpecialnote(jmFields.getStringAttribute("specialExplain"));
        cmsBtJmProductModel.setColorEn(fields.getColor());
        cmsBtJmProductModel.setImage1(fields.getImages1().get(0).getName());
        cmsBtJmProductModel.setProductLongName(jmFields.getStringAttribute("productLongName"));
        cmsBtJmProductModel.setProductMediumName(jmFields.getStringAttribute("productMediumName"));
        cmsBtJmProductModel.setProductShortName(jmFields.getStringAttribute("productShortName"));
        cmsBtJmProductModel.setSearchMetaTextCustom(jmFields.getStringAttribute("searchMetaTextCustom"));
        cmsBtJmProductModel.setMaterialEn(fields.getMaterialEn());
        cmsBtJmProductModel.setMaterialCn(fields.getMaterialCn());

        List<BaseMongoMap<String, Object>> jmSkus = product.getPlatform(CART_ID).getSkus();
        List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();

        //合并Common和平台的Sku属性
        jmSkus = mergeSkuAttr(jmSkus, commonSkus);
        if (jmSkus.size() > 0) {
            BaseMongoMap<String, Object> jmSku = jmSkus.get(0);
            cmsBtJmProductModel.setMsrpUsd(new BigDecimal(jmSku.getStringAttribute("clientMsrpPrice")));
            cmsBtJmProductModel.setMsrpRmb(new BigDecimal(jmSku.getStringAttribute("priceMsrp")));
            cmsBtJmProductModel.setRetailPrice(new BigDecimal(jmSku.getStringAttribute("priceSale")));
            cmsBtJmProductModel.setSalePrice(new BigDecimal(jmSku.getStringAttribute("priceRetail")));
        }

        String hscode = fields.getHsCodePrivate();
        if (!StringUtils.isNullOrBlank2(hscode)) {
            String[] hscodeArray = hscode.split(",");
            cmsBtJmProductModel.setHsCode(hscodeArray[0]);
            cmsBtJmProductModel.setHsName(hscodeArray[1]);
            cmsBtJmProductModel.setHsUnit(hscodeArray[2]);
        }
        cmsBtJmProductModel.setModified(new Date());
        return cmsBtJmProductModel;
    }


    // delete by desmond 2016/07/08 start    不用做尺码转换了，直接用sizeSx
//    /**
//     * 取size
//     *
//     * @param sizeStr
//     * @param channelId
//     * @param brandName
//     * @param productType
//     * @param sizeType
//     * @return
//     */
//    private String getSizeFromSizeMap(String sizeStr, String channelId, String brandName, String productType, String sizeType) {
//        if (!StringUtils.isNullOrBlank2(sizeStr)) {
//            Map<String, String> sizeMap = sxProductService.getSizeMap(channelId, brandName, productType, sizeType);
//            String changedSize = sizeMap.get(sizeStr);
//            if (changedSize != null) {
//                return changedSize;
//            } else {
//                return sizeStr;
//            }
//        } else {
//            return "NO SIZE";
//        }
//    }
    // delete by desmond 2016/07/08 end


    /**
     * 新建CmsBtJmSkuModel
     *
     * @param channelId
     * @param productCode
     * @param jmSku
     * @return
     */
    private CmsBtJmSkuModel fillNewCmsBtJmSkuModel(String channelId, String productCode, BaseMongoMap<String, Object> jmSku, String sizeStr ) {
        CmsBtJmSkuModel cmsBtJmSkuModel = new CmsBtJmSkuModel();
        cmsBtJmSkuModel.setChannelId(channelId);
        cmsBtJmSkuModel.setProductCode(productCode);
        cmsBtJmSkuModel.setSkuCode(jmSku.getStringAttribute("skuCode"));
        cmsBtJmSkuModel.setUpc(jmSku.getStringAttribute("barcode"));
        cmsBtJmSkuModel.setCmsSize(jmSku.getStringAttribute("size"));
        cmsBtJmSkuModel.setFormat(jmSku.getStringAttribute("property"));
        cmsBtJmSkuModel.setJmSize(sizeStr);


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
                // delete by desmond 2016/08/23 start
                // 应该是以分平台下面sku的价格优先，不要用common下的价格覆盖正确的价格
//                jmSku.put("priceMsrp", CommonSku.getPriceMsrp());
//                jmSku.put("priceRetail", CommonSku.getPriceRetail());
                // delete by desmond 2016/08/23 end
                jmSku.put("clientMsrpPrice", CommonSku.getClientMsrpPrice());
                jmSku.put("clientRetailPrice", CommonSku.getClientRetailPrice());
                jmSku.put(CmsBtProductConstants.Platform_SKU_COM.size.name(), CommonSku.getSize());
                // add by desmond 2016/07/08 start
                jmSku.put("sizeNick", CommonSku.getSizeNick());
                jmSku.put(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name(), CommonSku.getSizeSx());
                // add by desmond 2016/07/08 end
            }
        }

        return jmSkus;
    }

    /**
     * 取得商品自带条码
     * 在barcode的后面拼接vo+channelId+skuCode的前50位字符
     *
     * @param barcode String barCode
     * @param channelId String 渠道id
     * @param skuCode String skuCode
     * @return String 商品自带条码
     */
    private String addVoToBarcode(String barcode, String channelId, String skuCode) {
        if (StringUtils.isEmpty(barcode))
            return "";

        String result = barcode + "vo" + channelId + skuCode;

        return result.substring(0, result.length() >= 50 ? 50 : result.length());
    }

    /**
     * 上到聚美商城去
     *
     * @param product
     * @param shopBean
     * @param addSkuList 追加的skuCode列表
     * @param skuLogicQtyMap 库存
     */
    public void uploadMall(CmsBtProductModel product, ShopBean shopBean, ExpressionParser expressionParser, List<String> addSkuList, Map<String, Integer> skuLogicQtyMap) throws Exception {
        String mallId = product.getPlatform(CART_ID).getpPlatformMallId(); // 聚美Mall Id.
        if (StringUtils.isEmpty(mallId)) {
            // 新增
            StringBuffer sb = new StringBuffer("");
            mallId = jumeiHtMallService.addMall(shopBean, product.getPlatform(CART_ID).getpNumIId(), sb);

            if (StringUtils.isEmpty(mallId) || sb.length() > 0) {
                if (!StringUtils.isEmpty(mallId)) {
                    // add成功并生成了mallId,只是有别的错误，也回写mallId
                    updateMallId(product, mallId);
                } else {
                    // 上传失败
                    throw new BusinessException("添加商品到聚美商城失败!" + sb.toString());
                }
            } else {
                // 成功，回写mallId
                updateMallId(product, mallId);
            }

			{
				// 更新价格
				List<HtMallSkuPriceUpdateInfo> updateData = new ArrayList<>();
				List<BaseMongoMap<String, Object>> skuList = product.getPlatform(CART_ID).getSkus();
				for (BaseMongoMap<String, Object> sku : skuList) {
					String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
					if (ListUtils.isNull(addSkuList) || !addSkuList.contains(skuCode)) {
						// 当jmSkuNo不为空时,才加到updateData中，否则批量修改商城商品价格[MALL]时会报100002：jumei_sku_no,参数错误
						if (!StringUtils.isEmpty(sku.getStringAttribute("jmSkuNo"))) {
							// 不是新追加的
							HtMallSkuPriceUpdateInfo skuInfo = new HtMallSkuPriceUpdateInfo();
							skuInfo.setJumei_sku_no(sku.getStringAttribute("jmSkuNo"));
							skuInfo.setMarket_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));
							skuInfo.setMall_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));
							updateData.add(skuInfo);
						}
					}
				}

				if (!updateData.isEmpty()) {
					StringBuffer sbPrice = new StringBuffer("");
					boolean isSuccess = jumeiHtMallService.updateMallSkuPrice(shopBean, updateData, sbPrice);
					if (!isSuccess) {
						// TODO 临时修改:
						// TODO 目前是先做成: 只要错误信息里有"不存在关系售卖数据"这几个字, 就认为是正常的不报错
						// TODO 之后应该改成: 必须是全部错误都是"不存在关系售卖数据"的场合, 才认为是正常的不报错
						// TODO 最终应该是让聚美提供API, 进行关联
						if (!StringUtils.isEmpty(sbPrice.toString()) && !sbPrice.toString().contains("不存在关系售卖数据")) {
							// 价格更新失败throw出去
							throw new BusinessException("聚美商城的商品价格更新失败!" + sbPrice.toString());
						}
					}
				}
			}

		} else {
			{
				// 更新价格
				List<HtMallSkuPriceUpdateInfo> updateData = new ArrayList<>();
				List<BaseMongoMap<String, Object>> skuList = product.getPlatform(CART_ID).getSkus();
				for (BaseMongoMap<String, Object> sku : skuList) {
					String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
					if (ListUtils.isNull(addSkuList) || !addSkuList.contains(skuCode)) {
						// 当jmSkuNo不为空时,才加到updateData中，否则批量修改商城商品价格[MALL]时会报100002：jumei_sku_no,参数错误
						if (!StringUtils.isEmpty(sku.getStringAttribute("jmSkuNo"))) {
							// 不是新追加的
							HtMallSkuPriceUpdateInfo skuInfo = new HtMallSkuPriceUpdateInfo();
							skuInfo.setJumei_sku_no(sku.getStringAttribute("jmSkuNo"));
							skuInfo.setMarket_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));
							skuInfo.setMall_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));
							updateData.add(skuInfo);
						}
					}
				}

				if (!updateData.isEmpty()) {
					StringBuffer sbPrice = new StringBuffer("");
					boolean isSuccess = jumeiHtMallService.updateMallSkuPrice(shopBean, updateData, sbPrice);
					if (!isSuccess) {
						// TODO 临时修改:
						// TODO 目前是先做成: 只要错误信息里有"不存在关系售卖数据"这几个字, 就认为是正常的不报错
						// TODO 之后应该改成: 必须是全部错误都是"不存在关系售卖数据"的场合, 才认为是正常的不报错
						// TODO 最终应该是让聚美提供API, 进行关联
						if (!StringUtils.isEmpty(sbPrice.toString()) && !sbPrice.toString().contains("不存在关系售卖数据")) {
							// 价格更新失败throw出去
							throw new BusinessException("聚美商城的商品价格更新失败!" + sbPrice.toString());
						}
					}
				}
			}

			// 变更
            BaseMongoMap<String, Object> jmFields = product.getPlatform(CART_ID).getFields();

            HtMallUpdateInfo mallUpdateInfo = new HtMallUpdateInfo();
            mallUpdateInfo.setJumeiMallId(mallId);
            HtMallUpdateInfo.UpdateDataInfo updateDataInfo = mallUpdateInfo.getUpdateDataInfo();
            updateDataInfo.setShipping_system_id(NumberUtils.toInt(Codes.getCode("JUMEI", product.getChannelId())));
            updateDataInfo.setProduct_long_name(jmFields.getStringAttribute("productLongName"));
            updateDataInfo.setProduct_medium_name(jmFields.getStringAttribute("productMediumName"));
            updateDataInfo.setProduct_short_name(jmFields.getStringAttribute("productShortName"));
            updateDataInfo.setBefore_date(jmFields.getStringAttribute("beforeDate"));
            updateDataInfo.setSuit_people(jmFields.getStringAttribute("suitPeople"));
            updateDataInfo.setSpecial_explain(jmFields.getStringAttribute("specialExplain"));
            updateDataInfo.setSearch_meta_text_custom(jmFields.getStringAttribute("searchMetaTextCustom"));
            updateDataInfo.setDescription_properties(getTemplate("聚美详情", expressionParser, shopBean));
            updateDataInfo.setDescription_usage(getTemplate("聚美使用方法", expressionParser, shopBean));
            updateDataInfo.setDescription_images(getTemplate("聚美实拍", expressionParser, shopBean));

            StringBuffer sb = new StringBuffer("");
            boolean isSuccess = jumeiHtMallService.updateMall(shopBean, mallUpdateInfo, sb);
            if (!isSuccess) {
                // 上传失败
                throw new BusinessException("聚美商城的商品更新失败!" + sb.toString());
            }

            // 追加sku
            if (ListUtils.notNull(addSkuList)) {
                List<BaseMongoMap<String, Object>> skuList = product.getPlatform(CART_ID).getSkus();
                List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();
                skuList = mergeSkuAttr(skuList, commonSkus);
                for (BaseMongoMap<String, Object> sku : skuList) {
                    String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                    if (addSkuList.contains(skuCode)) {
                        HtMallSkuAddInfo mallSkuAddInfo = new HtMallSkuAddInfo();
                        mallSkuAddInfo.setJumeiSpuNo(sku.getStringAttribute("jmSpuNo"));
                        HtMallSkuAddInfo.SkuInfo skuInfo = mallSkuAddInfo.getSkuInfo();
//                        skuInfo.setCustoms_product_number(" "); // 发货仓库为保税区仓库时，此处必填, 现在暂时不用设置
//						skuInfo.setCustoms_product_number(skuCode);
                        skuInfo.setBusinessman_num(skuCode);
                        Integer stock = skuLogicQtyMap.get(skuCode);
                        if (stock == null) {
                            stock = 0;
                        }

                        // 聚美mall sku 不能追加库存为0的sku, 所以如果库存为0的场合, 跳过不追加
                        if (stock == 0) {
                        	continue;
						}

                        skuInfo.setStocks(stock);
                        skuInfo.setMall_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));
                        skuInfo.setMarket_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));

                        sb.setLength(0);
                        String jumeiSkuNo = jumeiHtMallService.addMallSku(shopBean, mallSkuAddInfo, sb);
                        if (StringUtils.isEmpty(jumeiSkuNo) || sb.length() > 0) {
                            // 价格更新失败throw出去
                            throw new BusinessException("聚美商城追加sku失败!" + sb.toString());
                        }

                        // 回写
                        String sizeStr = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
                        CmsBtJmSkuModel cmsBtJmSkuModel = fillNewCmsBtJmSkuModel(product.getChannelId(), product.getCommon().getFields().getCode(), sku, sizeStr);
                        cmsBtJmSkuModel.setJmSpuNo(sku.getStringAttribute("jmSpuNo"));
                        cmsBtJmSkuModel.setJmSkuNo(jumeiSkuNo);
                        cmsBtJmSkuDao.insert(cmsBtJmSkuModel);

                        sku.setStringAttribute("jmSkuNo", jumeiSkuNo);
                        saveProductPlatform(product.getChannelId(), product);
                    }
                }
            }
        }
    }

    /**
     * 回写Mall Id 到product表和group表
     * @param product
     * @param mallId 聚美Mall Id
     */
    protected void updateMallId(CmsBtProductModel product, String mallId) {
        String channelId = product.getChannelId();
        String code = product.getCommon().getFields().getCode();

        JongoUpdate updateProductQuery = new JongoUpdate();
        updateProductQuery.setQuery("{\"common.fields.code\": #}");
        updateProductQuery.setQueryParameters(code);

        // 上到聚美商城是默认在售的，所以只要成功上传到聚美商城，就把状态回写为OnSale
        updateProductQuery.setUpdate("{$set:{" +
                "\"platforms.P"+ CART_ID +".pPlatformMallId\": #," +
                "\"platforms.P"+ CART_ID +".pStatus\": #," +
                "\"platforms.P"+ CART_ID +".pReallyStatus\": #" +
                "}}");
        updateProductQuery.setUpdateParameters(mallId, CmsConstants.PlatformStatus.OnSale, CmsConstants.PlatformStatus.OnSale);

        cmsBtProductDao.updateFirst(updateProductQuery, channelId);


        JongoUpdate updateGroupQuery = new JongoUpdate();
        updateGroupQuery.setQuery("{\"cartId\": #, \"productCodes\": #}");
        updateGroupQuery.setQueryParameters(CART_ID, code);

        updateGroupQuery.setUpdate("{$set:{" +
                "\"platformMallId\": #," +
                "\"platformStatus\": #" +
                "}}");
        updateGroupQuery.setUpdateParameters(mallId, CmsConstants.PlatformStatus.OnSale);

        cmsBtProductGroupDao.updateFirst(updateGroupQuery, channelId);
    }

    /**
     * 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中不存在对应的SkuCode，则在平台上隐藏该商品编码并把库存改为0
     *
     * @param shop 店铺信息
     * @param originHashId 聚美hash Id
     * @param remoteSpus 平台上取得的spu列表
     * @param jmSkus DB中的sku列表
     */
    protected void doHideNotExistSkuDeal(ShopBean shop, String originHashId,
                                                     List<JmGetProductInfo_Spus> remoteSpus,
                                                     List<BaseMongoMap<String, Object>> jmSkus) throws Exception{
        if (ListUtils.isNull(remoteSpus)) return;

        for (JmGetProductInfo_Spus spu : remoteSpus) {
            // 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中不存在对应的SkuCode
            if (isNotExistBusinessmanCode(spu, jmSkus)) {
                // 把Deal的库存修改成0
				if (!StringUtils.isEmpty(spu.getBusinessman_code())) {
					String stockSyncResponse = updateStockNum(shop, spu.getBusinessman_code(), "0");
					$info("[skuCode:%s]同步库存:%s", spu.getBusinessman_code(), stockSyncResponse);
				}

                // 修改聚美SKU商家商品编码(skuCode) 头部+“ERROR_”（已有“ERROR_”的不追加）
				if (!StringUtils.isEmpty(spu.getSku_no())) {
					updateErrSkuBusinessmanNum(shop, originHashId, spu.getBusinessman_code(), spu.getSku_no());
				}

                // 修改聚美SKU商品自带条码(barCode/upcCode) 头部+“ERROR_”（已有“ERROR_”的不追加）
				if (!StringUtils.isEmpty(spu.getBusinessman_code())) {
					updateErrSpuUpcCode(shop, spu.getSpu_no(), spu.getUpc_code());
				}

                // 将聚美SKU状态（最新Deal）改为隐藏(is_enable=0)
				if (!StringUtils.isEmpty(spu.getSku_no())) {
					updateSkuIsEnableDeal(shop, originHashId, spu.getSku_no(), "0");
				}
            } else if (isNotSaleBusinessmanCode(spu, jmSkus)) {
                // 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中存在对应的SkuCode,但isSale=false(不在该平台卖了)
                // 只下架该sku，不修改商家商品编码(skuCode)和聚美SKU商家商品编码(skuCode)
                // 把Deal的库存修改成0
				if (!StringUtils.isEmpty(spu.getBusinessman_code())) {
					String stockSyncResponse = updateStockNum(shop, spu.getBusinessman_code(), "0");
					$info("[skuCode:%s]同步库存:%s", spu.getBusinessman_code(), stockSyncResponse);
				}

                // 将聚美SKU状态（最新Deal）改为隐藏(is_enable="0")
				if (!StringUtils.isEmpty(spu.getSku_no())) {
					updateSkuIsEnableDeal(shop, originHashId, spu.getSku_no(), "0");
				}
            } else {
                // 将聚美SKU状态（最新Deal）改为显示(is_enable="1")  // 每个正常的都改一下显示太花时间了，这次先注掉，好像没有取得isEnable的API
//                updateSkuIsEnableDeal(shop, originHashId, spu.getSku_no(), "1");
            }
        }
    }

    /**
     * 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中不存在对应的SkuCode，则将聚美商城上隐藏该商品编码
     *
     * @param shop 店铺信息
     * @param remoteSpus 平台上取得的spu列表
     * @param jmSkus DB中的sku列表
     */
    protected void doHideNotExistSkuMall(ShopBean shop,
                                         List<JmGetProductInfo_Spus> remoteSpus,
                                         List<BaseMongoMap<String, Object>> jmSkus) throws Exception{
        if (ListUtils.isNull(remoteSpus)) return;

        for (JmGetProductInfo_Spus spu : remoteSpus) {
            StringBuffer failCause = new StringBuffer("");
            // 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中不存在对应的SkuCode
            if (isNotExistBusinessmanCode(spu, jmSkus)) {
                // 将聚美SKU状态（商城）改为隐藏(is_enable=disabled)
				if (!StringUtils.isEmpty(spu.getSku_no())) {
					updateSkuIsEnableMall(shop, spu.getSku_no(), "disabled", failCause);
				}
            } else if (isNotSaleBusinessmanCode(spu, jmSkus)) {
                // 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中存在对应的SkuCode,但isSale=false(不在该平台卖了)
                // 只下架该sku，不修改商家商品编码(skuCode)和聚美SKU商家商品编码(skuCode)
                // 将聚美SKU状态（商城）改为隐藏(is_enable=disabled)
				if (!StringUtils.isEmpty(spu.getSku_no())) {
					updateSkuIsEnableMall(shop, spu.getSku_no(), "disabled", failCause);
				}
            } else {
                // 将聚美SKU状态（商城）改为显示(is_enable=enabled)  // 每个正常的都改一下显示太花时间了，这次先注掉，好像没有取得isEnable的API
//                updateSkuIsEnableMall(shop, spu.getSku_no(), "enabled", failCause);
            }
            if (failCause.toString().length() > 0)
                $info("隐藏聚美商城sku失败! [skuCode:%s] [failCause=%s]", spu.getBusinessman_code(), failCause.toString());
        }
    }

    /**
     * 聚美平台上取得的remoteSpu的skuCode是否在cms本地的mongoDB中不存在 (true:不存在 false:存在)
     */
    protected boolean isNotExistBusinessmanCode(JmGetProductInfo_Spus remoteSpu, List<BaseMongoMap<String, Object>> jmSkus) {
        if (remoteSpu == null) return false;

        if (ListUtils.isNull(jmSkus)) return true;

        return jmSkus.stream().filter(sku -> remoteSpu.getBusinessman_code().equals(sku.getStringAttribute("skuCode"))).count() == 0;
    }

    /**
     * 聚美平台上取得的remoteSpu的skuCode是否在cms本地的mongoDB中不在当前平台销售(isSale=false)
     */
    protected boolean isNotSaleBusinessmanCode(JmGetProductInfo_Spus remoteSpu, List<BaseMongoMap<String, Object>> jmSkus) {
        if (remoteSpu == null) return false;

        if (ListUtils.isNull(jmSkus)) return false;

        return jmSkus.stream().filter(sku ->
            remoteSpu.getBusinessman_code().equals(sku.getStringAttribute("skuCode"))
                    && !Boolean.parseBoolean(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.isSale.name()))
        ).count() > 0;
    }

    /**
     * 聚美Deal SKU库存同步
     */
    protected String updateStockNum(ShopBean shop, String businessmanCode, String stockNum) {
        if (!StringUtils.isEmpty(businessmanCode) && businessmanCode.startsWith("ERROR_")) {
            return "当前SKU无需同步库存";
        }

        StockSyncReq stockSyncReq = new StockSyncReq();
        stockSyncReq.setBusinessman_code(businessmanCode);
        stockSyncReq.setEnable_num(stockNum);

        String strResult;
        try {
            strResult = jumeiService.stockSync(shop, stockSyncReq);
        } catch (Exception e) {
            $error(String.format("聚美上新修改聚美Deal SKU库存同步(清空不存在sku的库存) 调用聚美API失败 channelId=%s, " +
                    "cartId=%s msg=%s", shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()), e);
            throw new BusinessException("聚美上新修改聚美Deal SKU库存同步(清空不存在sku的库存)失败！");
        }
        return strResult;
    }

    /**
     * 修改聚美上下架Deal关联的Sku(上下架)
     * 聚美平台上商品如果只剩下一个skuCode,下架时会报异常（每个商品至少要有一个sku），这里直接抛出异常，另外处理
     */
    protected void updateSkuIsEnableDeal(ShopBean shop, String jumeiHashId, String jumeiSkuNo, String isEnable) throws Exception {

        if (!"0".equals(isEnable) && !"1".equals(isEnable)) {
            String errMsg = String.format("聚美上下架Deal关联的Sku方法(updateSkuIsEnableDeal)的isEnable(0/1)参数不对 [isEnable:%s]", isEnable);
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        HtDealUpdateSkuIsEnableRequest request = new HtDealUpdateSkuIsEnableRequest();
        request.setJumei_hash_id(jumeiHashId);
        request.setJumei_sku_no(jumeiSkuNo);
        request.setIs_enable(isEnable);

        try {
            HtDealUpdateSkuIsEnableResponse response = jumeiHtDealService.updateSkuIsEnable(shop, request);
            if (response != null) {
                $info("聚美上新修改聚美Deal关联的Sku上下架 " + response.getBody());
                if (!response.is_Success()) {
                    // 100013  : is_enable参数和数据库中参数一致，没有发生改变  <-这个不算异常
                    if (!StringUtils.isEmpty(response.getError_code())
                            && !"100013".equals(response.getError_code())) {
//                        throw new BusinessException("聚美上新修改聚美Deal关联的Sku上下架失败! msg=%s", response.getErrorMsg());
                        $warn("聚美上新修改聚美Deal关联的Sku上下架失败! msg=%s", response.getErrorMsg());
                    }
                }
            }
        } catch (Exception e) {
            $error(String.format("聚美上新修改聚美Deal关联的Sku上下架 调用聚美API失败 channelId=%s, " +
                    "cartId=%s msg=%s", shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()), e);
            throw new BusinessException("聚美上新修改聚美Deal关联的Sku上下架失败！");
        }
    }

    /**
     * 聚美Mall 编辑商城的sku  htSku/updateSkuForMall  聚美商城上下架
     * @param status 是否启用，enabled-是，disabled-否
     */
    protected boolean updateSkuIsEnableMall(ShopBean shop, String jumeiSkuNo, String status, StringBuffer failCause) throws Exception{

        boolean result = false;

        if (!"enabled".equals(status) && !"disabled".equals(status)) {
            String errMsg = String.format("聚美上下架Deal关联的Sku方法(updateSkuIsEnableMall)的status(enabled/disabled)" +
                    "参数不对 [isEnable:%s]", status);
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        try {
            result = jumeiHtMallService.updateSkuForMall(shop, jumeiSkuNo, status, null, null, failCause);
            // delete by morse.lu 2016/10/08 start
            // 出错也继续做下去
//            if (!result) {
//                throw new BusinessException("聚美上新修改聚美Sku商家商品编码(skuCode) 头部+\"ERROR_\"失败！");
//            }
            // delete by morse.lu 2016/10/08 end
        } catch (Exception e) {
            $error(String.format("聚美上新修改聚美Mall 编辑商城的sku是否启用(上下架) 调用聚美API失败 channelId=%s, " +
                    "cartId=%s msg=%s", shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()), e);
            throw new BusinessException("聚美上新修改聚美Mall 编辑商城的sku是否启用(上下架)失败！");
        }

        return result;
    }

    /**
     * 修改聚美聚美SKU商家商品编码和聚美SKU条形码 头部+“ERROR_”（已有“ERROR_”的不追加）
     */
    protected void updateErrSkuBusinessmanNum(ShopBean shop, String jumeiHashId, String oldSkuCode, String jumeiSkuNo) throws Exception{

        HtSkuUpdateRequest htSkuUpdateRequest = new HtSkuUpdateRequest();
        htSkuUpdateRequest.setJumei_sku_no(jumeiSkuNo);
        htSkuUpdateRequest.setJumei_hash_id(jumeiHashId);

        // 商家商品编码(skuCode)
        if (!oldSkuCode.startsWith("ERROR_")) {
            htSkuUpdateRequest.setBusinessman_num("ERROR_" + oldSkuCode);
        } else {
            return;
        }

        htSkuUpdateRequest.setCustoms_product_number(" ");

        try {
            HtSkuUpdateResponse response = jumeiHtSkuService.update(shop, htSkuUpdateRequest);
            if (response != null) {
                $info("聚美上新修改聚美Sku商家商品编码(skuCode) 头部+\"ERROR_\" " + response.getBody());
            }
        } catch (Exception e) {
            $error(String.format("聚美上新修改聚美Sku商家商品编码(skuCode) 头部+\"ERROR_\" 调用聚美API失败 channelId=%s, " +
                    "cartId=%s msg=%s", shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()), e);
            throw new BusinessException("聚美上新修改聚美Sku商家商品编码(skuCode) 头部+\"ERROR_\"失败！");
        }
    }

    /**
     * 修改聚美Spu商品自带条码(barCode) 头部+“ERROR_”（已有“ERROR_”的不追加）
     */
    protected void updateErrSpuUpcCode(ShopBean shop, String jumeiSpuNo, String oldUpcCode) throws Exception {

        HtSpuUpdateRequest htSpuUpdateRequest = new HtSpuUpdateRequest();
        htSpuUpdateRequest.setJumei_spu_no(jumeiSpuNo);

        // 商品自带条码(barCode)
        if (!oldUpcCode.startsWith("ERROR_")) {
            htSpuUpdateRequest.setUpc_code("ERROR_" + oldUpcCode);
        } else {
            return;
        }

        try {
            HtSpuUpdateResponse response = jumeiHtSpuService.update(shop, htSpuUpdateRequest);
            if (response != null) {
                $info("聚美上新修改聚美Spu商品自带条码(barCode) 头部+\"ERROR_\" " + response.getBody());
            }
        } catch (Exception e) {
            $error(String.format("聚美上新修改聚美Spu商品自带条码(barCode) 头部+\"ERROR_\" 调用聚美API失败 channelId=%s, " +
                    "cartId=%s msg=%s", shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()), e);
            throw new BusinessException("聚美上新修改聚美Spu商品自带条码(barCode) 头部+\"ERROR_\"失败！");
        }
    }

	/**
	 * 修改聚美Spu商品自带条码(barCode)
	 */
	protected void updateRealSpuUpcCode(ShopBean shop, String jumeiSpuNo, String newUpcCode) throws Exception {

		HtSpuUpdateRequest htSpuUpdateRequest = new HtSpuUpdateRequest();
		htSpuUpdateRequest.setJumei_spu_no(jumeiSpuNo);

		htSpuUpdateRequest.setUpc_code(newUpcCode);

		try {
			HtSpuUpdateResponse response = jumeiHtSpuService.update(shop, htSpuUpdateRequest);
			if (response != null) {
				$info("聚美上新修改聚美Spu商品自带条码(barCode) " + response.getBody());
			}
		} catch (Exception e) {
			$error(String.format("聚美上新修改聚美Spu商品自带条码(barCode) 调用聚美API失败 channelId=%s, " +
					"cartId=%s msg=%s", shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()), e);
			throw new BusinessException("聚美上新修改聚美Spu商品自带条码(barCode) 失败！");
		}
	}

    /**
     * 批量更新deal价格(不用逐个deal去修或团购价了)
     *
     * @param product CmsBtProductModel
     * @param isUpdateMarketPrice 是否批量修改市场价
     * @param isUpdateDealPrice 是否批量修改团购价
     */
    protected void updateDealPriceBatch(ShopBean shop, CmsBtProductModel product, boolean isUpdateMarketPrice, boolean isUpdateDealPrice) {

        if (product == null) return;
        // 产品code
        String productCode = product.getCommon().getFields().getCode();

        List<BaseMongoMap<String, Object>> skuList = product.getPlatform(CART_ID).getSkus();
        if (ListUtils.isNull(skuList)) return;

        // 聚美HashId
        String jmHashId = product.getPlatform(CART_ID).getpNumIId();

        List<HtDeal_UpdateDealPriceBatch_UpdateData> updateDataList = new ArrayList<>();
        for (BaseMongoMap<String, Object> sku : skuList) {
            // 如果该sku的聚美Sku_no为空,则跳过
            if (StringUtils.isEmpty(sku.getStringAttribute("jmSkuNo"))) continue;

            HtDeal_UpdateDealPriceBatch_UpdateData skuUpdate = new HtDeal_UpdateDealPriceBatch_UpdateData();
            // 聚美Deal唯一值(jmHashId)
            skuUpdate.setJumei_hash_id(jmHashId);
            // 聚美Sku_no
            skuUpdate.setJumei_sku_no(sku.getStringAttribute("jmSkuNo"));
            // 市场价(市场价和团购价不能同时为空,市场价必须大于等于团购价)
            if (isUpdateMarketPrice)
                skuUpdate.setMarket_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));
            // 团购价(市场价和团购价不能同时为空,团购价至少大于15元)
            if (isUpdateDealPrice)
                skuUpdate.setDeal_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));

            updateDataList.add(skuUpdate);
        }

        if (ListUtils.isNull(updateDataList)) return;

        HtDealUpdateDealPriceBatchRequest request = new HtDealUpdateDealPriceBatchRequest();
        request.setUpdate_data(updateDataList);

        // 修改对象价格
        String strUpdatePrice = "";
        if (isUpdateMarketPrice && !isUpdateDealPrice) {
            strUpdatePrice += "(市场价)";
        } if (!isUpdateMarketPrice && isUpdateDealPrice) {
            strUpdatePrice += "(团购价)";
        } if (isUpdateMarketPrice && isUpdateDealPrice) {
            strUpdatePrice += "(市场价和团购价)";
        }

        HtDealUpdateDealPriceBatchResponse response;
        try {
            // 调用批量更新deal价格API(/v1/htDeal/updateDealPriceBatch)
            response = jumeiHtDealService.updateDealPriceBatch(shop, request);
            if (response != null && response.is_Success()) {
                $info("批量更新deal价格%s成功! [ProductCode=%s], [JumeiHashId=%s]", strUpdatePrice, productCode, jmHashId);
            } else {
                throw new BusinessException(response.getErrorMsg());
            }
        } catch (Exception e) {
            // 即使批量修改deal价格失败，也继续做后面的uploadMall
            $error("批量更新deal价格%s失败! [ProductCode=%s], [JumeiHashId=%s], [Error=%s]", strUpdatePrice, productCode, jmHashId, e.getMessage());
        }
    }

}

