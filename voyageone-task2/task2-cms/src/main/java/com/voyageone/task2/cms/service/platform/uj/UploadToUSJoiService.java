package com.voyageone.task2.cms.service.platform.uj;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.product.ProductPriceBean;
import com.voyageone.service.bean.cms.product.ProductSkuPriceBean;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.CmsBtSxWorkloadDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author james.li on 2016/4/6.
 * @version 2.0.0
 */
@Service
public class UploadToUSJoiService extends BaseTaskService{

    @Autowired
    private ProductService productService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private MongoSequenceService commSequenceMongoService;

    @Autowired
    private CmsBtSxWorkloadDao cmsBtSxWorkloadDao;

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

        List<CmsBtSxWorkloadModel> cmsBtSxWorkloadModels = cmsBtSxWorkloadDao.selectSxWorkloadModelWithCartId(100, Integer.parseInt(CartEnums.Cart.TI.getId()));
        cmsBtSxWorkloadModels.forEach(this::upload);

    }

    public void upload(CmsBtSxWorkloadModel sxWorkLoadBean) {

        try {
            $info(String.format("channelId:%s  groupId:%d  复制到US JOI 开始",sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId()));
            List<CmsBtProductModel> productModels = productService.getProductByGroupId(sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId());

            //从group中过滤出需要上的usjoi的产品
            productModels = getUSjoiProductModel(productModels);
            if(productModels.size() == 0){
                throw new BusinessException("没有找到需要上新的SKU");
            }else{
                $info("有"+productModels.size()+"个产品要复制");
            }

            for (CmsBtProductModel productModel : productModels) {
                productModel.set_id(null);

                CmsBtProductModel pr = cmsBtProductDao.selectProductByCode(ChannelConfigEnums.Channel.VOYAGEONE.getId(), productModel.getFields().getCode());
                if (pr == null) {
                    creatGroup(productModel);
                    productModel.setChannelId(ChannelConfigEnums.Channel.VOYAGEONE.getId());
                    productModel.setOrgChannelId(sxWorkLoadBean.getChannelId());

                    List<ProductPriceBean> productPrices = new ArrayList<>();
                    List<ProductSkuPriceBean> skuPriceBeans = new ArrayList<>();

                    // 根据com_mt_us_joi_config表给sku 设cartId
                    final List<Integer> cartIds;
                    OrderChannelBean usJoiBean = Channels.getChannel(productModel.getOrgChannelId());
                    if(usJoiBean != null && !StringUtil.isEmpty(usJoiBean.getCart_ids())){
                        cartIds = Arrays.asList(usJoiBean.getCart_ids().split(",")).stream().map(Integer::parseInt).collect(toList());
                    }else{
                        cartIds = new ArrayList<>();
                    }
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
                    productService.createProduct(ChannelConfigEnums.Channel.VOYAGEONE.getId(), productModel, sxWorkLoadBean.getModifier());

                    ProductPriceBean priceBean = new ProductPriceBean();
                    priceBean.setProductId(productModel.getProdId());


                    priceBean.setSkuPrices(skuPriceBeans);
                    productPrices.add(priceBean);
                    productSkuService.updatePrices(ChannelConfigEnums.Channel.VOYAGEONE.getId(), productPrices, sxWorkLoadBean.getModifier());
                } else {
                    productModel.setProdId(pr.getProdId());
                    productModel.setGroups(pr.getGroups());
                    ProductUpdateBean requestModel = new ProductUpdateBean();
                    requestModel.setProductModel(productModel);
                    requestModel.setModifier(sxWorkLoadBean.getModifier());
                    requestModel.setIsCheckModifed(false); // 不做最新修改时间ｃｈｅｃｋ
                    productService.updateProduct(ChannelConfigEnums.Channel.VOYAGEONE.getId(), requestModel);
                }
            }
            sxWorkLoadBean.setPublishStatus(1);
            cmsBtSxWorkloadDao.updateSxWorkloadModel(sxWorkLoadBean);
            $info(String.format("channelId:%s  groupId:%d  复制到US JOI 结束", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId()));
        }catch (Exception e){
            sxWorkLoadBean.setPublishStatus(2);
            cmsBtSxWorkloadDao.updateSxWorkloadModel(sxWorkLoadBean);
            $info(String.format("channelId:%s  groupId:%d  复制到US JOI 异常", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId()));
            throw e;
        }

    }


    /**
     * 找出需要上到minmall的产品和sku
     * @param productModels 产品列表
     * @return 产品列表
     */
    private List<CmsBtProductModel> getUSjoiProductModel(List<CmsBtProductModel> productModels) {

        List<CmsBtProductModel> usJoiProductModes = new ArrayList<>();

        // 找出approved 并且 sku的carts里包含 28（usjoi的cartid）
        productModels.stream().filter(productModel -> "Approved".equalsIgnoreCase(productModel.getFields().getStatus())).forEach(productModel -> {
            List<CmsBtProductModel_Sku> skus = productModel.getSkus().stream()
                    .filter(cmsBtProductModel_sku -> cmsBtProductModel_sku.getSkuCarts().contains(Integer.parseInt(CartEnums.Cart.TI.getId())))
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
    private long getGroupIdByFeedModel(String channelId, String modelCode, String cartId) {

        // 先去看看是否有存在的了
        CmsBtProductModel product = cmsBtProductDao.selectProductGroupByModelCodeAndCartId(channelId, modelCode, cartId);

        if (product == null
                || product.getGroups() == null
                || product.getGroups().getPlatforms() == null
                || product.getGroups().getPlatforms().size() == 0) {
            return -1;
        }

        // 看看是否能找到
        for (CmsBtProductModel_Group_Platform platform : product.getGroups().getPlatforms()) {
            if (platform.getCartId() == Integer.parseInt(cartId)) {
                return platform.getGroupId();
            }
        }

        // 找到product但是找不到指定cart, 也认为是找不到 (按理说是不会跑到这里的)
        return -1;
    }

    private void creatGroup(CmsBtProductModel productModel) {
        CmsBtProductModel_Group group = new CmsBtProductModel_Group();

//            // 价格区间设置 ( -> 调用顾步春的api自动会去设置,这里不需要设置了)

        // 获取当前channel, 有多少个platform
        List<TypeChannelBean> typeChannelBeanList = TypeChannels.getTypeListSkuCarts(ChannelConfigEnums.Channel.VOYAGEONE.getId(), "D", "en"); // 取得展示用数据
        if (typeChannelBeanList == null) {
            return;
        }

        List<CmsBtProductModel_Group_Platform> platformList = new ArrayList<>();
        // 循环一下
        for (TypeChannelBean shop : typeChannelBeanList) {
            // 创建一个platform
            CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform();

            // cart id
            platform.setCartId(Integer.parseInt(shop.getValue()));

            // 获取group id
            long groupId;
            groupId = getGroupIdByFeedModel(ChannelConfigEnums.Channel.VOYAGEONE.getId(), productModel.getFields().getModel(), shop.getValue());

            // group id
            // 看看同一个model里是否已经有数据在cms里存在的
            //   如果已经有存在的话: 直接用哪个group id
            //   如果没有的话: 取一个最大的 + 1
            if (groupId == -1) {
                // 获取唯一编号
                platform.setGroupId(
                        commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID)
                );

                // is Main
                platform.setIsMain(true);
            } else {
                platform.setGroupId(groupId);

                // is Main
                platform.setIsMain(false);
            }

            // num iid
            platform.setNumIId(""); // 因为没有上新, 所以不会有值

            // display order
            platform.setDisplayOrder(0); // TODO: 不重要且有影响效率的可能, 有空再设置

            // platform status:发布状态: 未上新 // Synship.com_mt_type : id = 45
            platform.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
            // platform active:上新的动作: 暂时默认所有店铺是放到:仓库中
            platform.setPlatformActive(CmsConstants.PlatformActive.Instock);

            // qty
            platform.setQty(0); // 初始为0, 之后会有库存同步程序把这个地方的值设为正确的值的

            platformList.add(platform);
        }
        group.setPlatforms(platformList);

        productModel.setGroups(group);
    }


}
