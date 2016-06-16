package com.voyageone.task2.cms.service.platform.uj;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.product.ProductPriceBean;
import com.voyageone.service.bean.cms.product.ProductSkuPriceBean;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

/**
 * @author james.li on 2016/4/6.
 * @version 2.0.0
 */
@Service
public class UploadToUSJoiService extends BaseTaskService{

    @Autowired
    ProductGroupService productGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private MongoSequenceService commSequenceMongoService;

    @Autowired
    private CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsUploadProductToUSJoiJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        for(OrderChannelBean channelBean:Channels.getUsJoiChannelList()) {
            List<CmsBtSxWorkloadModel> cmsBtSxWorkloadModels = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithCartId(100,  Integer.parseInt(channelBean.getOrder_channel_id()));
            cmsBtSxWorkloadModels.forEach(this::upload);
        }
    }

    public void upload(CmsBtSxWorkloadModel sxWorkLoadBean) {
        String usJoiChannelId = sxWorkLoadBean.getCartId().toString();
        try {
            $info(String.format("channelId:%s  groupId:%d  复制到%s 开始",sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId(),usJoiChannelId));
            List<CmsBtProductModel> productModels = productService.getProductByGroupId(sxWorkLoadBean.getChannelId(), new Long(sxWorkLoadBean.getGroupId()), false);
            $info("productModels"+productModels.size());
            //从group中过滤出需要上的usjoi的产品
            productModels = getUSjoiProductModel(productModels,sxWorkLoadBean.getCartId());
            if(productModels.size() == 0){
                throw new BusinessException("没有找到需要上新的SKU");
            }else{
                $info("有"+productModels.size()+"个产品要复制");
            }


            for (CmsBtProductModel productModel : productModels) {
                productModel.set_id(null);

                final List<Integer> cartIds;
                OrderChannelBean usJoiBean = Channels.getChannel(usJoiChannelId);
                if(usJoiBean != null && !StringUtil.isEmpty(usJoiBean.getCart_ids())){
                    cartIds = Arrays.asList(usJoiBean.getCart_ids().split(",")).stream().map(Integer::parseInt).collect(toList());
                }else{
                    cartIds = new ArrayList<>();
                }

                CmsBtProductModel pr = productService.getProductByCode(usJoiChannelId, productModel.getFields().getCode());
                if (pr == null) {
                    productModel.setChannelId(usJoiChannelId);
                    productModel.setOrgChannelId(sxWorkLoadBean.getChannelId());
                    productModel.setCarts(new ArrayList<>());
                    creatGroup(productModel, usJoiChannelId);

                    List<ProductPriceBean> productPrices = new ArrayList<>();
                    List<ProductSkuPriceBean> skuPriceBeans = new ArrayList<>();

                    // 根据com_mt_us_joi_config表给sku 设cartId

                    productModel.getSkus().forEach(sku -> {
                        ProductSkuPriceBean skuPriceBean = new ProductSkuPriceBean();

                        skuPriceBean.setSkuCode(sku.getSkuCode());

                        skuPriceBean.setClientMsrpPrice(sku.getClientMsrpPrice());
                        sku.setClientMsrpPrice(null);

                        skuPriceBean.setClientNetPrice(sku.getClientNetPrice());
                        sku.setClientNetPrice(null);

                        skuPriceBean.setClientRetailPrice(sku.getClientRetailPrice());
                        sku.setClientRetailPrice(null);

                        skuPriceBean.setPriceMsrp(sku.getPriceMsrp());
                        sku.setPriceMsrp(null);

                        skuPriceBean.setPriceRetail(sku.getPriceRetail());
                        sku.setPriceRetail(null);

                        skuPriceBean.setPriceSale(sku.getPriceSale());
                        sku.setPriceSale(null);

                        skuPriceBeans.add(skuPriceBean);
                        sku.setSkuCarts(cartIds);
                    });

                    productModel.setProdId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PROD_ID));
                    productService.createProduct(usJoiChannelId, productModel, sxWorkLoadBean.getModifier());

                    ProductPriceBean priceBean = new ProductPriceBean();
                    priceBean.setProductId(productModel.getProdId());

                    priceBean.setSkuPrices(skuPriceBeans);
                    productPrices.add(priceBean);
                    productSkuService.updatePrices(usJoiChannelId, productPrices, sxWorkLoadBean.getModifier());
                } else {
                    List<ProductPriceBean> productPrices = new ArrayList<>();
                    List<ProductSkuPriceBean> skuPriceBeans = new ArrayList<>();
                    for(CmsBtProductModel_Sku sku:productModel.getSkus()){
                        CmsBtProductModel_Sku oldSku = pr.getSku(sku.getSkuCode());
                        if(oldSku == null){
                            ProductSkuPriceBean skuPriceBean = new ProductSkuPriceBean();

                            skuPriceBean.setSkuCode(sku.getSkuCode());

                            skuPriceBean.setClientMsrpPrice(sku.getClientMsrpPrice());
                            sku.setClientMsrpPrice(null);

                            skuPriceBean.setClientNetPrice(sku.getClientNetPrice());
                            sku.setClientNetPrice(null);

                            skuPriceBean.setClientRetailPrice(sku.getClientRetailPrice());
                            sku.setClientRetailPrice(null);

                            skuPriceBean.setPriceMsrp(sku.getPriceMsrp());
                            sku.setPriceMsrp(null);

                            skuPriceBean.setPriceRetail(sku.getPriceRetail());
                            sku.setPriceRetail(null);

                            skuPriceBean.setPriceSale(sku.getPriceSale());
                            sku.setPriceSale(null);

                            skuPriceBeans.add(skuPriceBean);
                            sku.setSkuCarts(cartIds);
                            pr.getSkus().add(sku);
                        }else{
                            if(oldSku.getPriceMsrp().compareTo(sku.getPriceMsrp()) != 0
                                    || oldSku.getPriceRetail().compareTo(sku.getPriceRetail()) != 0
                                    || oldSku.getPriceSale().compareTo(sku.getPriceSale()) != 0){
                                ProductSkuPriceBean skuPriceBean = new ProductSkuPriceBean();

                                skuPriceBean.setSkuCode(sku.getSkuCode());

                                skuPriceBean.setClientMsrpPrice(sku.getClientMsrpPrice());

                                skuPriceBean.setClientNetPrice(sku.getClientNetPrice());

                                skuPriceBean.setClientRetailPrice(sku.getClientRetailPrice());

                                skuPriceBean.setPriceMsrp(sku.getPriceMsrp());

                                skuPriceBean.setPriceRetail(sku.getPriceRetail());

                                skuPriceBean.setPriceSale(sku.getPriceSale());

                                skuPriceBeans.add(skuPriceBean);
                            }
                        }
                    }

                    if(skuPriceBeans.size() > 0) {
                        ProductUpdateBean requestModel = new ProductUpdateBean();
                        requestModel.setProductModel(pr);
                        requestModel.setModifier(sxWorkLoadBean.getModifier());
                        requestModel.setIsCheckModifed(false); // 不做最新修改时间ｃｈｅｃｋ
                        productService.updateProduct(usJoiChannelId, requestModel);

                        ProductPriceBean priceBean = new ProductPriceBean();
                        priceBean.setProductId(pr.getProdId());
                        priceBean.setSkuPrices(skuPriceBeans);
                        productPrices.add(priceBean);
                        productSkuService.updatePrices(usJoiChannelId, productPrices, sxWorkLoadBean.getModifier());
                    }
                }
            }
            sxWorkLoadBean.setPublishStatus(1);
            cmsBtSxWorkloadDaoExt.updateSxWorkloadModel(sxWorkLoadBean);
            $info(String.format("channelId:%s  groupId:%d  复制到%s JOI 结束", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId(),usJoiChannelId));
        }catch (Exception e){
            sxWorkLoadBean.setPublishStatus(2);
            cmsBtSxWorkloadDaoExt.updateSxWorkloadModel(sxWorkLoadBean);
            $info(String.format("channelId:%s  groupId:%d  复制到%s JOI 异常", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId(),usJoiChannelId));
            e.printStackTrace();
            issueLog.log(e, ErrorType.BatchJob,SubSystem.CMS);
            throw e;
        }
    }

    /**
     * 找出需要上到minmall的产品和sku
     * @param productModels 产品列表
     * @return 产品列表
     */
    private List<CmsBtProductModel> getUSjoiProductModel(List<CmsBtProductModel> productModels,Integer cartId) {

        List<CmsBtProductModel> usJoiProductModes = new ArrayList<>();

        // 找出approved 并且 sku的carts里包含 28（usjoi的cartid）
        productModels.stream().filter(productModel -> "Approved".equalsIgnoreCase(productModel.getFields().getStatus())).forEach(productModel -> {
            List<CmsBtProductModel_Sku> skus = productModel.getSkus().stream()
                    .filter(cmsBtProductModel_sku -> cmsBtProductModel_sku.getSkuCarts().contains(cartId))
                    .collect(toList());
            if (skus.size() > 0) {
                skus.forEach(cmsBtProductModel_sku -> cmsBtProductModel_sku.setSkuCarts(null));
                productModel.setSkus(skus);
                usJoiProductModes.add(productModel);
            }
        });
        return usJoiProductModes;

    }

    /**
     * 根据model, 到product表中去查找, 看看这家店里, 是否有相同的model已经存在
     * 如果已经存在, 返回 找到了的那个group id
     * 如果不存在, 返回 -1
     *
     * @param channelId channel id
     * @param modelCode 品牌方给的model
     * @param cartId    cart id
     * @return group id
     */
    private CmsBtProductGroupModel getGroupIdByFeedModel(String channelId, String modelCode, String cartId) {
        // 先去看看是否有存在的了
        CmsBtProductGroupModel groupObj = productGroupService.selectProductGroupByModelCodeAndCartId(channelId, modelCode, cartId);
        return groupObj;
    }

    private void creatGroup(CmsBtProductModel cmsBtProductModel,String usJoiChannel) {
//            // 价格区间设置 ( -> 调用顾步春的api自动会去设置,这里不需要设置了)

        // 获取当前channel, 有多少个platform
        List<TypeChannelBean> typeChannelBeanList = TypeChannels.getTypeListSkuCarts(usJoiChannel, "D", "en"); // 取得展示用数据
        if (typeChannelBeanList == null) {
            return;
        }


        // 循环一下
        for (TypeChannelBean shop : typeChannelBeanList) {

            // 获取group id
            CmsBtProductGroupModel platform = getGroupIdByFeedModel(cmsBtProductModel.getChannelId(), cmsBtProductModel.getFields().getModel(), shop.getValue());

            // group id
            // 看看同一个model里是否已经有数据在cms里存在的
            //   如果已经有存在的话: 直接用哪个group id
            //   如果没有的话: 取一个最大的 + 1
            if (platform == null) {
            // 创建一个platform
                platform = new CmsBtProductGroupModel();
                // cart id
                platform.setCartId(Integer.parseInt(shop.getValue()));
                // 获取唯一编号
                platform.setGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));

                platform.setChannelId(cmsBtProductModel.getChannelId());
                platform.setMainProductCode(cmsBtProductModel.getFields().getCode());
                platform.setProductCodes(Arrays.asList(cmsBtProductModel.getFields().getCode()));

                platform.setPriceMsrpSt(cmsBtProductModel.getFields().getPriceMsrpSt());
                platform.setPriceMsrpEd(cmsBtProductModel.getFields().getPriceMsrpEd());
                platform.setPriceRetailSt(cmsBtProductModel.getFields().getPriceRetailSt());
                platform.setPriceRetailEd(cmsBtProductModel.getFields().getPriceRetailEd());
                platform.setPriceSaleSt(cmsBtProductModel.getFields().getPriceSaleSt());
                platform.setPriceSaleEd(cmsBtProductModel.getFields().getPriceSaleEd());
                // num iid
                platform.setNumIId(""); // 因为没有上新, 所以不会有值

                // display order
                platform.setDisplayOrder(0); // TODO: 不重要且有影响效率的可能, 有空再设置

                // platform status:发布状态: 未上新 // Synship.com_mt_type : id = 45
                platform.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
                // platform active:上新的动作: 暂时默认所有店铺是放到:仓库中
                platform.setPlatformActive(CmsConstants.PlatformActive.ToInStock);

                // qty
                platform.setQty(0); // 初始为0, 之后会有库存同步程序把这个地方的值设为正确的值的

                cmsBtProductGroupDao.insert(platform);
            } else {
                platform.getProductCodes().add(cmsBtProductModel.getFields().getCode());

                if(platform.getPriceMsrpSt() == null || platform.getPriceMsrpSt().compareTo(cmsBtProductModel.getFields().getPriceMsrpSt()) > 0){
                    platform.setPriceMsrpSt(cmsBtProductModel.getFields().getPriceMsrpSt());
                }
                if(platform.getPriceMsrpEd() == null || platform.getPriceMsrpEd().compareTo(cmsBtProductModel.getFields().getPriceMsrpEd()) < 0){
                    platform.setPriceMsrpEd(cmsBtProductModel.getFields().getPriceMsrpEd());
                }

                if(platform.getPriceRetailSt() == null || platform.getPriceRetailSt().compareTo(cmsBtProductModel.getFields().getPriceRetailSt()) > 0){
                    platform.setPriceRetailSt(cmsBtProductModel.getFields().getPriceRetailSt());
                }
                if(platform.getPriceRetailEd() == null || platform.getPriceRetailEd().compareTo(cmsBtProductModel.getFields().getPriceRetailEd()) < 0){
                    platform.setPriceRetailEd(cmsBtProductModel.getFields().getPriceRetailEd());
                }

                if(platform.getPriceSaleSt() == null || platform.getPriceSaleSt().compareTo(cmsBtProductModel.getFields().getPriceSaleSt()) > 0){
                    platform.setPriceSaleSt(cmsBtProductModel.getFields().getPriceSaleSt());
                }
                if(platform.getPriceSaleEd() == null || platform.getPriceSaleEd().compareTo(cmsBtProductModel.getFields().getPriceSaleEd()) < 0){
                    platform.setPriceSaleEd(cmsBtProductModel.getFields().getPriceSaleEd());
                }

                cmsBtProductGroupDao.update(platform);
                // is Main
                // TODO 修改设置isMain属性
//                platform.setIsMain(false);
            }
        }

    }

}
