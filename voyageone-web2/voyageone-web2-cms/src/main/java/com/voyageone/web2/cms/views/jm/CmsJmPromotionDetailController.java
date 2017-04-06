package com.voyageone.web2.cms.views.jm;

import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.businessmodel.JMPromotionProduct.UpdateRemarkParameter;
import com.voyageone.service.bean.cms.businessmodel.JMUpdateSkuWithPromotionInfo;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.*;
import com.voyageone.service.bean.cms.jumei.*;
import com.voyageone.service.bean.cms.jumei2.JmProduct;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.jumei.*;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionProduct3Service;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionSku3Service;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsJmMallPromotionPriceSyncMQMessageBody;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
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
    private final CmsBtJmPromotionProductService serviceCmsBtJmPromotionProduct;
    private final CmsBtJmProductService cmsBtJmProductService;
    private final CmsBtJmPromotionProductService cmsBtJmPromotionProductService;
    private final CmsBtJmSkuService cmsBtJmSkuService;
    private final CmsBtJmPromotionSkuService cmsBtJmPromotionSkuService;
    private final CmsBtJmMasterBrandService cmsBtJmMasterBrandService;
    private final MqSender sender;

    private final CmsMqSenderService cmsMqSenderService;

    //begin 2
    private final CmsBtJmPromotionProduct3Service service3;
    private final CmsBtJmPromotionSku3Service service3CmsBtJmPromotionSku;
    //end 2

    private final CmsJmPromotionService jmPromotionService;

    private final TagService tagService;
    private final CmsBtJmImageTemplateService jmImageTemplateService;
    @Autowired
    private CmsBtJmPromotionService btJmPromotionService;

    @Autowired
    public CmsJmPromotionDetailController(CmsBtJmPromotionProductService serviceCmsBtJmPromotionProduct,
                                          CmsBtJmPromotionSku3Service service3CmsBtJmPromotionSku,
                                          CmsBtJmPromotionSkuService cmsBtJmPromotionSkuService,
                                          CmsBtJmPromotionProductService cmsBtJmPromotionProductService,
                                          CmsBtJmPromotionProduct3Service service3,
                                          CmsBtJmProductService cmsBtJmProductService,
                                          CmsBtJmMasterBrandService cmsBtJmMasterBrandService,
                                          CmsBtJmSkuService cmsBtJmSkuService, MqSender sender,
                                          CmsJmPromotionService jmPromotionService, TagService tagService,
                                          CmsBtJmImageTemplateService jmImageTemplateService, CmsMqSenderService cmsMqSenderService) {
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
        this.tagService = tagService;
        this.jmImageTemplateService = jmImageTemplateService;
        this.cmsMqSenderService = cmsMqSenderService;
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
            service3.sendMessage(parameter.getPromotionId(), getUser().getUserName(), getUser().getSelChannelId());
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
        service3.sendMessage(promotionId, getUser().getUserName(), getUser().getSelChannelId());
        CallResult result = new CallResult();
        return success(result);
    }

    //部分商品上新
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.JmNewByProductIdListInfo)
    public AjaxResponse jmNewByProductIdListInfo(@RequestBody ProductIdListInfo parameter) {
        serviceCmsBtJmPromotionProduct.jmNewByProductIdListInfo(parameter);
        service3.sendMessage(parameter.getPromotionId(), getUser().getUserName(), getUser().getSelChannelId());

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
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchUpdateSkuDealPrice)
    public AjaxResponse batchUpdateSkuDealPrice(@RequestBody BatchUpdateSkuPriceParameterBean parameter) {
        CallResult result = service3.batchUpdateSkuDealPrice(parameter,getUser().getUserName());
        return success(result);
    }
    //批量同步商城价格
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchSynchMallPrice)
    public AjaxResponse batchSyncMallPrice(@RequestBody CmsJmMallPromotionPriceSyncMQMessageBody parameter) {
        parameter.setChannelId(getUser().getSelChannelId());
        parameter.setSender(getUser().getUserName());
        cmsMqSenderService.sendMessage(parameter);
        return success(true);
    }
    //批量同步价格
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchSynchPrice)
    public AjaxResponse batchSyncPrice(@RequestBody BatchSynchPriceParameter parameter) {
        service3.batchSynchPrice(parameter);
        service3.sendMessage(parameter.getPromotionId(), getUser().getUserName(), getUser().getSelChannelId());
        CallResult result = new CallResult();
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.SynchAllPrice)
    public AjaxResponse syncAllPrice(@RequestBody int promotionId) {
        CallResult result = service3.synchAllPrice(promotionId);
        if (result.isResult()) {
            service3.sendMessage(promotionId, getUser().getUserName(), getUser().getSelChannelId());
        }
        return success(result);
    }

    //批量再售
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchCopyDeal)
    public AjaxResponse batchCopyDeal(@RequestBody BatchCopyDealParameter parameter) {
        service3.batchCopyDeal(parameter);
        service3.sendMessage(parameter.getPromotionId(), getUser().getUserName(), getUser().getSelChannelId());
        CallResult result = new CallResult();
        return success(result);
    }

    //全量再售
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.CopyDealAll)
    public AjaxResponse copyDealAll(@RequestBody int promotionId) {
        CallResult result = service3.copyDealAll(promotionId);
        if (result.isResult()) {
            service3.sendMessage(promotionId, getUser().getUserName(), getUser().getSelChannelId());
        }
        return success(result);
    }

    //批量删除 product  已经再售的不删
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.BatchDeleteProduct)
    public AjaxResponse batchDeleteProduct(@RequestBody BatchDeleteProductParameter parameter) {
        service3.batchDeleteProduct(parameter,getUser().getSelChannelId());
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

    //修改单个商品tag
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdatePromotionProductTag)
    public AjaxResponse updatePromotionProductTag(@RequestBody UpdatePromotionProductTagParameter parameter) {
        UserSessionBean userSessionBean=getUser();
        service3.updatePromotionProductTag(parameter, userSessionBean.getSelChannelId(),userSessionBean.getUserName());
        return success(null);
    }
    //批量修改商品tag
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdatePromotionListProductTag)
    public int updatePromotionListProductTag(@RequestBody UpdateListPromotionProductTagParameter parameter) {
        UserSessionBean userSessionBean=getUser();
        return service3.updatePromotionListProductTag(parameter,userSessionBean.getSelChannelId(), userSessionBean.getUserName());
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.SelectChangeCountByPromotionId)
    public AjaxResponse selectChangeCountByPromotionId(@RequestBody long cmsBtJmPromotionProductId) {
        int count = service3.selectChangeCountByPromotionId(cmsBtJmPromotionProductId);
        return success(count);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UpdateRemark)
    public AjaxResponse updateRemark(@RequestBody UpdateRemarkParameter parameter) {
        int count = service3.updateRemark(parameter);
        return success(count);
    }

    //刷新参考价格
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.RefreshPrice)
    public  AjaxResponse refreshPrice(@RequestBody int jmPromotionId) {
        cmsBtJmPromotionSkuService.senderJMRefreshPriceMQMessage(jmPromotionId, getUser().getUserName(), getUser().getSelChannelId());
        return success(null);
    }
    //jm2 end

    /**
     * 获取专场活动所使用的各种背景模板地址
     * @since 2.8.0
     */
    @RequestMapping("getJmTemplateUrls")
    public AjaxResponse getJmTemplateUrls() {

        Map<String, Object> template = new HashMap<>();

        List<String> bayWindowTemplateUrls = jmImageTemplateService.getBayWindowTemplateUrls();
        String separatorBar = jmImageTemplateService.getSeparatorBar("");

        template.put("bayWindowTemplateUrls", bayWindowTemplateUrls);
        template.put("separatorBar", separatorBar);

        return success(template);
    }

    /**
     * 获取活动下所有的聚美模块
     * @since 2.8.0
     */
    @RequestMapping("getPromotionTagModules")
    public AjaxResponse getPromotionTagModules(@RequestBody int jmPromotionId) {
        return success(jmPromotionService.getPromotionTagModules(jmPromotionId));
    }

    /**
     * 保存所有聚美模块
     * @since 2.8.0
     */
    @RequestMapping("savePromotionTagModules")
    public AjaxResponse savePromotionTagModules(@RequestBody SavePromotionTagModules param) {
        jmPromotionService.savePromotionTagModules(param.getJmPromotionId(), param.getTagModulesList(), getUser());
        return success(true);
    }

    /**
     * 获取聚美模块下所有的商品
     * @since 2.8.0
     */
    @RequestMapping("getPromotionProducts")
    public AjaxResponse getPromotionProducts(@RequestBody int tagId) {
        return success(service3.getPromotionTagProductList(tagId));
    }

    /**
     * 按顺序保存模块下的商品
     * @since 2.8.0
     */
    @RequestMapping("saveProductSort")
    public AjaxResponse saveProductSort(@RequestBody SaveProductSort param) {
        service3.saveProductSort(tagService.getTagByTagId(param.getTagId()), param.getJmProductList(), getUser().getUserName());
        return success(true);
    }

    /**
     * 设置聚美活动各阶段的状态
     */
    @RequestMapping("setJmPromotionStepStatus")
    public AjaxResponse setJmPromotionStepStatus(@RequestBody Map<String, Object> param) {
        btJmPromotionService.setJmPromotionStepStatus((Integer) param.get("jmPromId"),
                CmsBtJmPromotionService.JmPromotionStepNameEnum.valueOf((String) param.get("stepName")),
                CmsBtJmPromotionService.JmPromotionStepStatusEnum.valueOf((String) param.get("stepStatus")),
                getUser().getUserName());
        return success(true);
    }

    private static class SaveProductSort {
        Integer tagId;
        List<JmProduct> jmProductList;

        public Integer getTagId() {
            return tagId;
        }

        public void setTagId(Integer tagId) {
            this.tagId = tagId;
        }

        public List<JmProduct> getJmProductList() {
            return jmProductList;
        }

        public void setJmProductList(List<JmProduct> jmProductList) {
            this.jmProductList = jmProductList;
        }
    }

    public static class SavePromotionTagModules {
        private int jmPromotionId;
        private List<CmsJmPromotionService.CmsJmTagModules> tagModulesList;

        public int getJmPromotionId() {
            return jmPromotionId;
        }

        public void setJmPromotionId(int jmPromotionId) {
            this.jmPromotionId = jmPromotionId;
        }

        public List<CmsJmPromotionService.CmsJmTagModules> getTagModulesList() {
            return tagModulesList;
        }

        public void setTagModulesList(List<CmsJmPromotionService.CmsJmTagModules> tagModulesList) {
            this.tagModulesList = tagModulesList;
        }
    }
}