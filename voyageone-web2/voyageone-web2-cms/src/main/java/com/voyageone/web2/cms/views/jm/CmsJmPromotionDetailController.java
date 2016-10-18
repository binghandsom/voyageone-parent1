package com.voyageone.web2.cms.views.jm;

import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.businessmodel.JMUpdateSkuWithPromotionInfo;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.InitParameter;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTimeAll;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdatePromotionProductParameter;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdatePromotionProductTagParameter;
import com.voyageone.service.bean.cms.jumei.*;
import com.voyageone.service.impl.cms.jumei.*;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionProduct3Service;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionSku3Service;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(
        value = CmsUrlConstants.JMPROMOTION.LIST.DETAIL.ROOT,
        method = RequestMethod.POST
)
public class CmsJmPromotionDetailController extends CmsController {
    private final CmsBtJmPromotionProductService serviceCmsBtJmPromotionProduct;
    private final CmsBtJmProductService cmsBtJmProductService;
    private final CmsBtJmPromotionProductService cmsBtJmPromotionProductService;
    private final CmsBtJmSkuService cmsBtJmSkuService;
    private final CmsBtJmPromotionSkuService cmsBtJmPromotionSkuService;
    private final CmsBtJmMasterBrandService cmsBtJmMasterBrandService;
    private final MqSender sender;

    //begin 2
    private final CmsBtJmPromotionProduct3Service service3;
    private final CmsBtJmPromotionSku3Service service3CmsBtJmPromotionSku;
    //end 2

    private final CmsJmPromotionService jmPromotionService;

    @Autowired
    public CmsJmPromotionDetailController(CmsBtJmPromotionProductService serviceCmsBtJmPromotionProduct,
                                          CmsBtJmPromotionSku3Service service3CmsBtJmPromotionSku,
                                          CmsBtJmPromotionSkuService cmsBtJmPromotionSkuService,
                                          CmsBtJmPromotionProductService cmsBtJmPromotionProductService,
                                          CmsBtJmPromotionProduct3Service service3,
                                          CmsBtJmProductService cmsBtJmProductService,
                                          CmsBtJmMasterBrandService cmsBtJmMasterBrandService,
                                          CmsBtJmSkuService cmsBtJmSkuService, MqSender sender,
                                          CmsJmPromotionService jmPromotionService) {
        this.serviceCmsBtJmPromotionProduct = serviceCmsBtJmPromotionProduct;
        this.service3CmsBtJmPromotionSku = service3CmsBtJmPromotionSku;
        this.cmsBtJmPromotionSkuService = cmsBtJmPromotionSkuService;
        this.cmsBtJmPromotionProductService = cmsBtJmPromotionProductService;
        this.service3 = service3;
        this.cmsBtJmProductService = cmsBtJmProductService;
        this.cmsBtJmMasterBrandService = cmsBtJmMasterBrandService;
        this.cmsBtJmSkuService = cmsBtJmSkuService;
        this.sender = sender;
        this.jmPromotionService = jmPromotionService;
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.INIT)
    public AjaxResponse init(@RequestBody InitParameter parameter) {
        return success(service3.init(parameter, getUser().getSelChannelId(), getLang()));
    }

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

    //全量延期
    ///cms/jmpromotion/detail/updateDealEndTimeAll
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdateDealEndTimeAll)
    //延迟Deal结束时间  全量
    public AjaxResponse updateDealEndTimeAll(@RequestBody ParameterUpdateDealEndTimeAll parameter) {
        //聚美专场结束时间都以59秒结尾。
        parameter.getDealEndTime().setSeconds(59);//聚美专场结束时间都以59秒结尾。
        CallResult result = service3.updateDealEndTimeAll(parameter);
        if (result.isResult()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", parameter.getPromotionId());
            sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        }
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

    //jm2 begin 批量变更价格
    //批量更新价格
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchUpdateDealPrice)
    public AjaxResponse batchUpdateDealPrice(@RequestBody BatchUpdatePriceParameterBean parameter) {
        CallResult result = service3.batchUpdateDealPrice(parameter);
        return success(result);
    }

    //批量同步价格
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchSynchPrice)
    public AjaxResponse batchSyncPrice(@RequestBody BatchSynchPriceParameter parameter) {
        service3.batchSynchPrice(parameter);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", parameter.getPromotionId());
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        CallResult result = new CallResult();
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.SynchAllPrice)
    public AjaxResponse syncAllPrice(@RequestBody int promotionId) {
        CallResult result = service3.synchAllPrice(promotionId);
        if (result.isResult()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", promotionId);
            sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        }
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

    //全量删除
    //全量再售
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.CopyDealAll)
    public AjaxResponse copyDealAll(@RequestBody int promotionId) {
        CallResult result = service3.copyDealAll(promotionId);
        if (result.isResult()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", promotionId);
            sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        }
        return success(result);
    }

    //批量删除 product  已经再售的不删
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchDeleteProduct)
    public AjaxResponse batchDeleteProduct(@RequestBody BatchDeleteProductParameter parameter) {
        service3.batchDeleteProduct(parameter);
        CallResult result = new CallResult();
        return success(result);
    }

    //全量删除
    //删除全部product  已经再售的不删
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.DeleteAllProduct)
    public AjaxResponse deleteAllProduct(@RequestBody int promotionId) {
        CallResult result = new CallResult();
        result = service3.deleteAllProduct(promotionId);
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.GetProductView)
    public AjaxResponse getProductView(@RequestBody int promotionProductId) {
        return success(service3.getProductView(promotionProductId));
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdateDealPrice)
    public AjaxResponse updateDealPrice(@RequestBody UpdateSkuDealPriceParameter parameter) {
        String userName = getUser().getUserName();
        service3CmsBtJmPromotionSku.updateDealPrice(parameter, userName);
        CallResult result = new CallResult();
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdatePromotionProduct)
    public AjaxResponse updatePromotionProduct(@RequestBody UpdatePromotionProductParameter parameter) {
        service3.updatePromotionProduct(parameter, getUser().getUserName());
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdatePromotionProductTag)
    public AjaxResponse updatePromotionProductTag(@RequestBody UpdatePromotionProductTagParameter parameter) {
        service3.updatePromotionProductTag(parameter, getUser().getUserName());
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.SelectChangeCountByPromotionId)
    public AjaxResponse selectChangeCountByPromotionId(@RequestBody long cmsBtJmPromotionProductId) {
        int count = service3.selectChangeCountByPromotionId(cmsBtJmPromotionProductId);
        return success(count);
    }


    //刷新参考价格
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.RefreshPrice)
    public  AjaxResponse refreshPrice(@RequestBody long cmsBtJmPromotionId) {

        // // TODO: 2016/10/18  加入 发消息  李俊提供
        return success(null);
    }
    //jm2 end

    @RequestMapping("getPromotionTagModules")
    public AjaxResponse getPromotionTagModules(@RequestBody int jmPromotionId) {
        return success(jmPromotionService.getPromotionTagModules(jmPromotionId));
    }
}
