package com.voyageone.web2.cms.views.jm;

import com.voyageone.common.CmsConstants;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.businessmodel.JMUpdateProductWithPromotionInfo;
import com.voyageone.service.bean.cms.businessmodel.JMUpdateSkuWithPromotionInfo;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTimeAll;
import com.voyageone.service.bean.cms.jumei.*;
import com.voyageone.service.impl.cms.jumei.*;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionProduct3Service;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionSku3Service;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(
        value = CmsUrlConstants.JMPROMOTION.LIST.DETAIL.ROOT,
        method = RequestMethod.POST
)
public class CmsJmPromotionDetailController extends CmsController {
    @Autowired
    private CmsBtJmPromotionProductService serviceCmsBtJmPromotionProduct;
    @Autowired
    private CmsBtJmProductService cmsBtJmProductService;
    @Autowired
    private CmsBtJmPromotionProductService cmsBtJmPromotionProductService;
    @Autowired
    private CmsBtJmSkuService cmsBtJmSkuService;
    @Autowired
    private CmsBtJmPromotionSkuService cmsBtJmPromotionSkuService;
    @Autowired
    private CmsBtJmProductImagesService cmsBtJmProductImagesService;
    @Autowired
    private CmsBtJmMasterPlatService cmsBtJmMasterPlatService;
    @Autowired
    private CmsBtJmCategoryService cmsBtJmCategoryService;
    @Autowired
    private CmsBtJmMasterBrandService cmsBtJmMasterBrandService;
    @Autowired
    private MqSender sender;

    //begin 2
    @Autowired
    CmsBtJmPromotionProduct3Service service3;
    @Autowired
    CmsBtJmPromotionSku3Service service3CmsBtJmPromotionSku;
    //end 2

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.GET_PROMOTION_PRODUCT_INFO_LIST_BY_WHERE)
    public AjaxResponse getPromotionProductInfoListByWhere(@RequestBody Map params) {
        return success(serviceCmsBtJmPromotionProduct.getPageByWhere(params));
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.GetPromotionProductInfoCountByWhere)
    public AjaxResponse getPromotionProductInfoCountByWhere(@RequestBody Map<String, Object> map) {
        return success(serviceCmsBtJmPromotionProduct.getCountByWhere(map));
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UPDATE)
    public AjaxResponse update(@RequestBody CmsBtJmPromotionProductModel params) {
        String channelId = getUser().getSelChannelId();
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        serviceCmsBtJmPromotionProduct.update(params);
        CallResult result = new CallResult();
        return success(result);
    }

//    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UPDATEDEAlPRICE)
//    public AjaxResponse updateDealPrice(@RequestBody Map<String, Object> map) {
//        int id = Integer.parseInt((String) map.get("id"));
//        BigDecimal dealPrice = new BigDecimal(map.get("dealPrice").toString());
//        CmsBtJmPromotionProductModel model = serviceCmsBtJmPromotionProduct.select(id);
//        model.setDealPrice(dealPrice);
//        model.setModifier(getUser().getUserName());
//        serviceCmsBtJmPromotionProduct.update(model);
//        CallResult result = new CallResult();
//        return success(result);
//    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.DELETE)
    public AjaxResponse delete(@RequestBody int id) {
        serviceCmsBtJmPromotionProduct.delete(id);
        CallResult result = new CallResult();
        return success(result);
    }

    //全部删除
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.DELETEBYPPROMOTIONID)
    public AjaxResponse deleteByPromotionId(@RequestBody int promotionId) {
        serviceCmsBtJmPromotionProduct.deleteByPromotionId(promotionId);
        CallResult result = new CallResult();
        return success(result);
    }

    //部分商品删除
    ///cms/jmpromotion/detail/deleteByProductIdList
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.DELETEBYPRODUCTIDLIST)
    public AjaxResponse deleteByProductIdList(@RequestBody ProductIdListInfo parameter) {
        serviceCmsBtJmPromotionProduct.deleteByProductIdList(parameter);
        CallResult result = new CallResult();
        return success(result);
    }


    ///cms/jmpromotion/detail/updateDealEndTimeAll
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdateDealEndTimeAll)
    //延迟Deal结束时间  全量
    public int updateDealEndTimeAll(@RequestBody ParameterUpdateDealEndTimeAll parameter) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", parameter.getPromotionId());
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdateDealEndTimeJob, map);
        return serviceCmsBtJmPromotionProduct.updateDealEndTimeAll(parameter);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.GET_PRODUCT_DETAIL)
    // 获取产品详细信息
    public AjaxResponse getProductDetail(@RequestBody Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        map.put("channelId", getUser().getSelChannelId());

        Map<String, Object> imageParam = new HashMap<>();
        imageParam.putAll(map);
        imageParam.put("imageType", 1);
        imageParam.put("imageIndex", 1);

        result.put("productImage", cmsBtJmProductImagesService.selectOne(imageParam));
        result.put("productInfo", cmsBtJmProductService.selectOne(map));
        CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel = cmsBtJmPromotionProductService.selectOne(map);
        result.put("productPromotionInfo", cmsBtJmPromotionProductModel);
        List<CmsBtJmSkuModel> skuList = cmsBtJmSkuService.selectList(map);

        Map<String, Object> promotionSkuParam = new HashMap<>();
        // promotionSkuParam.put("cmsBtJmProductId", cmsBtJmPromotionProductModel.getCmsBtJmProductId());
        List<CmsBtJmPromotionSkuModel> promotionSkuList = cmsBtJmPromotionSkuService.selectList(promotionSkuParam);
        result.put("skuList", cmsBtJmSkuService.selectSkuList(skuList, promotionSkuList));

        return success(result);
    }

    /**
     * 更新产品信息
     *
     * @param productInfo
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UPDATE_PRODUCT_DETAIL)
    public AjaxResponse updateProductDetail(@RequestBody CmsBtJmProductModel productInfo) {
        return success(cmsBtJmProductService.update(productInfo));
    }

    /**
     * 更新产品信息及活动产品信息
     *
     * @param request
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UPDATE_PROMOTION_PRODUCT_DETAIL)
    public AjaxResponse updatePromotionProductDetail(@RequestBody JMUpdateProductWithPromotionInfo request) {

        Map<String, Object> result = new HashMap<>();

        result.put("updateProductResult", cmsBtJmProductService.update(request.getCmsBtJmProductModel()));
        result.put("updatePromotionProductResult", cmsBtJmPromotionProductService.update(request.getCmsBtJmPromotionProductModel()));

        return success(result);
    }

    //所有未上新商品上新
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.JmNewUpdateAll)
    public AjaxResponse jmNewUpdateAll(@RequestBody int promotionId) {
        serviceCmsBtJmPromotionProduct.jmNewUpdateAll(promotionId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", promotionId);
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        CallResult result = new CallResult();
        return success(result);
    }

    //部分商品上新
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.JmNewByProductIdListInfo)
    public AjaxResponse jmNewByProductIdListInfo(@RequestBody ProductIdListInfo parameter) {
        serviceCmsBtJmPromotionProduct.jmNewByProductIdListInfo(parameter);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", parameter.getPromotionId());
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        CallResult result = new CallResult();
        return success(result);
    }

    /**
     * 更新产品信息
     *
     * @param request
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UPDATE_SKU_DETAIL)
    public AjaxResponse updateSkuDetail(@RequestBody JMUpdateSkuWithPromotionInfo request) {

        JMUpdateSkuWithPromotionInfo result = new JMUpdateSkuWithPromotionInfo();

        cmsBtJmSkuService.update(request.getCmsBtJmSkuModel());
        int promotionSkuId = cmsBtJmPromotionSkuService.updateWithDiscount(request.getCmsBtJmPromotionSkuModel(), getUser().getSelChannelId(), getUser().getUserName());

        result.setCmsBtJmSkuModel(cmsBtJmSkuService.select(request.getCmsBtJmSkuModel().getId()));
        result.setCmsBtJmPromotionSkuModel(cmsBtJmPromotionSkuService.select(promotionSkuId));

        return success(result);
    }

    /**
     * 更新产品信息
     *
     * @param request
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.DELETE_PROMOTION_SKU)
    public AjaxResponse deletePromotionSku(@RequestBody CmsBtJmPromotionSkuModel request) {
        return success(cmsBtJmPromotionSkuService.delete(request.getId()));
    }

    /**
     * 获取产品详情页的master数据
     *
     * @return
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.GET_PRODUCT_MASTER_DATA)
    public AjaxResponse getProductMasterData() {

        Map<String, Object> result = new HashMap<>();
        result.put("categoryList", cmsBtJmCategoryService.selectAll());
        result.put("brandList", cmsBtJmMasterBrandService.selectAll());
        result.put("priceUnitList", cmsBtJmMasterPlatService.selectListByCode(CmsConstants.JmMasterPlatCode.PRICE_UNIT));
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdateJM)
    public AjaxResponse updateJM(@RequestBody int promotionProductId) throws Exception {
//        BatchSynchPriceParameter parameter=new BatchSynchPriceParameter();
//        service3.batchSynchPrice(parameter);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("id", parameter.getPromotionId());
//        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
//        CallResult result = new CallResult();
        return success(null);
    }

    //jm2 begin
    //批量更新价格
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchUpdateDealPrice)
    public AjaxResponse batchUpdateDealPrice(@RequestBody BatchUpdatePriceParameterBean parameter) {
        service3.batchUpdateDealPrice(parameter);
        return success(null);
    }

    //批量同步价格
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchSynchPrice)
    public AjaxResponse batchSynchPrice(@RequestBody BatchSynchPriceParameter parameter) {
        service3.batchSynchPrice(parameter);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", parameter.getPromotionId());
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        CallResult result = new CallResult();
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.SynchAllPrice)
    public AjaxResponse synchAllPrice(@RequestBody int promotionId) {
        service3.synchAllPrice(promotionId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", promotionId);
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        CallResult result = new CallResult();
        return success(result);
    }

    //批量再售
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchCopyDeal)
    public AjaxResponse batchCopyDeal(@RequestBody BatchCopyDealParameter parameter) {
        service3.batchCopyDeal(parameter);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", parameter.getPromotionId());
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        CallResult result = new CallResult();
        return success(result);
    }

    //全部再售
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.CopyDealAll)
    public AjaxResponse copyDealAll(@RequestBody int promotionId) {
        service3.copyDealAll(promotionId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", promotionId);
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        CallResult result = new CallResult();
        return success(result);
    }

    //批量删除 product  已经再售的不删
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchDeleteProduct)
    public AjaxResponse batchDeleteProduct(@RequestBody BatchDeleteProductParameter parameter) {
        service3.batchDeleteProduct(parameter);
        CallResult result = new CallResult();
        return success(result);
    }

    //删除全部product  已经再售的不删
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.DeleteAllProduct)
    public AjaxResponse deleteAllProduct(@RequestBody int promotionId) {
        service3.deleteAllProduct(promotionId);
        CallResult result = new CallResult();
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.GetProductView)
    public  AjaxResponse getProductView(@RequestBody int promotionProductId) {
        return success(service3.getProductView(promotionProductId));
    }
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdateDealPrice)
    public AjaxResponse updateDealPrice(@RequestBody UpdateSkuDealPriceParameter parameter) {
        String userName = getUser().getUserName();
        service3CmsBtJmPromotionSku.updateDealPrice(parameter, userName);
        CallResult result = new CallResult();
        return success(result);
    }
    //jm2 end
}
