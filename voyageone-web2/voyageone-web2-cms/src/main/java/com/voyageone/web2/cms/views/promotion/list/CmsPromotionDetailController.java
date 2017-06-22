package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtPromotionGroupsBean;
import com.voyageone.service.bean.cms.businessmodel.CmsPromotionDetail.SaveSkuPromotionPricesParameter;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdatePromotionProductTagParameter;
import com.voyageone.service.impl.cms.promotion.PromotionCodesTagService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.promotion.PromotionSkuService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsSneakerHeadAddPromotionMQMessageBody;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james 2015/12/15
 * @version 2.0.0
 */
@RestController
@RequestMapping(
        value = PROMOTION.LIST.DETAIL.ROOT,
        method = RequestMethod.POST
)
public class CmsPromotionDetailController extends CmsController {

    @Autowired
    PromotionCodesTagService promotionCodesTagService;
    @Autowired
    private CmsPromotionDetailService cmsPromotionDetailService;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @Autowired
    private PromotionSkuService promotionSkuService;
    @Autowired
    private  CmsMqSenderService cmsMqSenderService;
    @Autowired
    private PromotionService promotionService;

    @RequestMapping(PROMOTION.LIST.DETAIL.GET_PROMOTION_GROUP)
    public AjaxResponse getPromotionGroup(@RequestBody Map<String, Object> params) {
        int cartId = Integer.parseInt(getCmsSession().getPlatformType().get("cartId").toString());
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);

        int cnt = cmsPromotionDetailService.getPromotionModelListCnt(params);
        List<Map<String,Object>> resultBean = cmsPromotionDetailService.getPromotionGroup(params, cartId);
        Map<String,Object> result = new HashMap<>();
        result.put("resultData",resultBean);
        result.put("total",cnt);
        // 返回用户信息
        return success(result);
    }

    @RequestMapping(PROMOTION.LIST.DETAIL.GetPromotionSkuList)
    public AjaxResponse GetPromotionSkuList(@RequestBody Map<String, Object> params) {
        Object data= promotionSkuService.getListByWhere(params);
        return success(data);
    }

    @RequestMapping(PROMOTION.LIST.DETAIL.SaveSkuPromotionPrices)
    public AjaxResponse saveSkuPromotionPrices(@RequestBody SaveSkuPromotionPricesParameter parameter) {
        promotionSkuService.saveSkuPromotionPrices(parameter,getUser().getUserName());
        promotionService.sendPromotionMq(parameter.getPromotionId(), false, getUser().getUserName());
        return success(null);
    }

    @RequestMapping(PROMOTION.LIST.DETAIL.GET_PROMOTION_CODE)
    public AjaxResponse getPromotionCode(@RequestBody Map<String, Object> params) {
        int cartId = Integer.parseInt(getCmsSession().getPlatformType().get("cartId").toString());
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        int cnt = cmsPromotionDetailService.getPromotionCodeListCnt(params);
        List<CmsBtPromotionCodesBean> resultBean = cmsPromotionDetailService.getPromotionCode(params, cartId);
        Map<String,Object> result = new HashMap<>();
        result.put("resultData",resultBean);
        result.put("total",cnt);
        // 返回用户信息
        return success(result);
    }

    //修改tag
    @RequestMapping(PROMOTION.LIST.DETAIL.UpdatePromotionProductTag)
    public  AjaxResponse  updatePromotionProductTag(@RequestBody UpdatePromotionProductTagParameter parameter) {
        UserSessionBean userSessionBean = getUser();
        promotionCodesTagService.updatePromotionProductTag(parameter, userSessionBean.getSelChannelId(), userSessionBean.getUserName());

       return success(null);
    }
        @RequestMapping(PROMOTION.LIST.DETAIL.GET_PROMOTION_SKU)
    public AjaxResponse getPromotionSku(@RequestBody Map<String, Object> params) {

        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);

        int cnt = cmsPromotionDetailService.getPromotionSkuListCnt(params);
        List<Map<String,Object>> resultBean = cmsPromotionDetailService.getPromotionSku(params);
        Map<String,Object> result = new HashMap<>();
        result.put("resultData",resultBean);
        result.put("total",cnt);
        // 返回用户信息
        return success(result);
    }

    @RequestMapping(PROMOTION.LIST.DETAIL.GET_PROMOTION_UPLOAD)
    public AjaxResponse uploadPromotion(HttpServletRequest request, @RequestParam int promotionId) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        // 获得输入流：
        InputStream input = file.getInputStream();

        Map<String, List<String>> reponse = cmsPromotionDetailService.uploadPromotion(input, promotionId, getUser().getUserName());

        // 返回用户信息
        return success(reponse);
    }

    @RequestMapping(PROMOTION.LIST.DETAIL.TE_JIA_BAO_INIT)
    public AjaxResponse tejiabaoInit(@RequestBody int promotionId) throws Exception {

        promotionDetailService.teJiaBaoInit(promotionId, getUser().getSelChannelId(), getUser().getUserName());
        // 返回用户信息
        return success(null);
    }
    @RequestMapping(PROMOTION.LIST.DETAIL.UPDATE_PROMOTION_PRODUCT)
    public AjaxResponse updatePromotionProduct(@RequestBody CmsBtPromotionCodesBean params) {

        cmsPromotionDetailService.updatePromotionProduct(params, getUser().getUserName());
        // 返回用户信息
        return success(null);
    }

    @RequestMapping(PROMOTION.LIST.DETAIL.DEL_PROMOTION_MODEL)
    public AjaxResponse delPromotionModel(@RequestBody List<CmsBtPromotionGroupsBean> params) {

        cmsPromotionDetailService.delPromotionModel(params, getUser().getSelChannelId(), getUser().getUserName());
        // 返回用户信息
        return success(null);
    }

    @RequestMapping(PROMOTION.LIST.DETAIL.DEL_PROMOTION_CODE)
    public AjaxResponse delPromotionCode(@RequestBody List<CmsBtPromotionCodesBean> params) {

        cmsPromotionDetailService.delPromotionCode(params, getUser().getSelChannelId(), getUser().getUserName());
        // 返回用户信息
        return success(null);
    }

    @RequestMapping(PROMOTION.LIST.DETAIL.TMALL_JUHUASUAN_EXPORT)
    public ResponseEntity<byte[]> doTMallJuHuaSuanExport(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer promotionId, @RequestParam String promotionName)
            throws Exception {
        byte[] data = cmsPromotionDetailService.getTMallJuHuaSuanExport(promotionId, getUser().getSelChannelId());
        return genResponseEntityFromBytes(String.format("%s(聚划算)-%s.xls", promotionName, DateTimeUtil.getLocalTime(getUserTimeZone(), "MMddHHmmss"), ".xlsx"), data);
    }
    @RequestMapping(PROMOTION.LIST.DETAIL.TMALL_PROMOTION_EXPORT)
    public ResponseEntity<byte[]> doTMallPromotionExport(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer promotionId, @RequestParam String promotionName)
            throws Exception {
        byte[] data = cmsPromotionDetailService.getTMallPromotionExport(promotionId, getUser().getSelChannelId());
        return genResponseEntityFromBytes(String.format("%s(官方活动(A类))-%s.xls", promotionName, DateTimeUtil.getLocalTime(getUserTimeZone(), "MMddHHmmss"), ".xlsx"), data);
    }

    @RequestMapping("addPromotionByGroup")
    public AjaxResponse addPromotionByGroup(@RequestBody Integer promotionId) {

        CmsSneakerHeadAddPromotionMQMessageBody cmsSneakerHeadAddPromotionMQMessageBody = new CmsSneakerHeadAddPromotionMQMessageBody();
        cmsSneakerHeadAddPromotionMQMessageBody.setPromotionId(promotionId);
        cmsSneakerHeadAddPromotionMQMessageBody.setChannelId(getUser().getSelChannelId());
        cmsSneakerHeadAddPromotionMQMessageBody.setSender(getUser().getUserName());
        cmsMqSenderService.sendMessage(cmsSneakerHeadAddPromotionMQMessageBody);
        return success(true);
    }
}
