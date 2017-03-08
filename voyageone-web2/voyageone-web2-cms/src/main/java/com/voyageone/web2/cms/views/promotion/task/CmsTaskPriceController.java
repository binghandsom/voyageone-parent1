package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsTeJiaBaoDelMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTaskTejiabaoModel;
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
import java.util.stream.Collectors;

/**
 * Created by james.li on 2015/12/11.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.PROMOTION.TASK.PRICE.ROOT,
        method = RequestMethod.POST
)
public class CmsTaskPriceController extends CmsController {

    @Autowired
    private CmsTaskPriceService cmsTaskPriceService;

    @Autowired
    private PromotionService promotionDetail;

    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.PRICE.GET_PRICE_LIST)
    public AjaxResponse getPriceList(@RequestBody Map param) {

        int cnt = cmsTaskPriceService.getPriceListCnt(param);
        List<Map<String,Object>> resultBean = cmsTaskPriceService.getPriceList(param);
        Map<String,Object> result = new HashMap<>();
        result.put("resultData",resultBean);
        result.put("total", cnt);
        param.put("synFlg",3);
        result.put("failCnt",cmsTaskPriceService.getPriceListCnt(param));
        param.put("synFlg",1);
        result.put("pendingCnt",cmsTaskPriceService.getPriceListCnt(param));
        param.put("synFlg",0);
        result.put("stopCnt",cmsTaskPriceService.getPriceListCnt(param));
        CmsBtPromotionModel cmsBtPromotionModel = promotionDetail.getByPromotionId(Integer.parseInt(param.get("promotionId").toString()));
        result.put("isAllPromotion",cmsBtPromotionModel.getIsAllPromotion());
        // 返回用户信息
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.PRICE.UPDATE_TASK_STATUS)
    public AjaxResponse updateTaskStatus(@RequestBody CmsBtTaskTejiabaoModel param) {

        param.setModifier(getUser().getUserName());
        cmsTaskPriceService.updateTaskStatus(param);
        // 返回用户信息
        return success(null);
    }

    /**
     * 跟据一个活动把该活动下的商品从全店特价宝中删除
     * @param promotionId 活动Id
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.PRICE.DEL_ALL_PROMOTION_BY_CUSTOM_PROMOTION_ID)
    public AjaxResponse delAllPromotionByCustomPromotionId(@RequestBody Integer promotionId){

        CmsBtPromotionModel cmsBtPromotionModel = promotionDetail.getByPromotionId(promotionId);

            // 找出该活动所在的channel有没有全店特价宝的活动ID
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(cmsBtPromotionModel.getChannelId()
                    , CmsConstants.ChannelConfig.TEJIABAO_ID
                    , cmsBtPromotionModel.getCartId().toString());
            if(cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())){
                throw new BusinessException("该渠道没有配置全店特价宝");
            }else{
                CmsBtPromotionModel allPromtoion = promotionDetail.getByPromotionId(Integer.parseInt(cmsChannelConfigBean.getConfigValue1()));
                if(allPromtoion != null){
                    Map<String,Object> param = new HashMap<>();
                    param.put("promotionId", promotionId);
                    List<Map<String,Object>> items = cmsTaskPriceService.getPriceList(param);
                    List<String> numIids = items.stream()
                            .filter(item-> item.get("numIid") != null && !StringUtil.isEmpty(item.get("numIid").toString()))
                            .map(item->item.get("numIid").toString())
                            .distinct()
                            .collect(Collectors.toList());
                    CmsTeJiaBaoDelMQMessageBody cmsTeJiaBaoDelMQMessageBody = new CmsTeJiaBaoDelMQMessageBody();
                    cmsTeJiaBaoDelMQMessageBody.setChannelId(cmsBtPromotionModel.getChannelId());
                    cmsTeJiaBaoDelMQMessageBody.setCartId(cmsBtPromotionModel.getCartId());
                    cmsTeJiaBaoDelMQMessageBody.setNumIId(numIids);
                    cmsTeJiaBaoDelMQMessageBody.setTejiabaoId(Long.parseLong(allPromtoion.getTejiabaoId()));
                    cmsTeJiaBaoDelMQMessageBody.setSender(getUser().getUserName());
                    cmsMqSenderService.sendMessage(cmsTeJiaBaoDelMQMessageBody);
                }

            }
        return success(null);
    }

    /**
     * 跟据一个活动把该活动下的商品重新刷新全店特价宝的价格
     * @param promotionId 活动Id
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.PRICE.REFRESH_ALL_PROMOTION_BY_CUSTOM_PROMOTION_ID)
    public AjaxResponse refreshAllPromotionByCustomPromotionId(@RequestBody Integer promotionId){

        CmsBtPromotionModel cmsBtPromotionModel = promotionDetail.getByPromotionId(promotionId);

        // 找出该活动所在的channel有没有全店特价宝的活动ID
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(cmsBtPromotionModel.getChannelId()
                , CmsConstants.ChannelConfig.TEJIABAO_ID
                , cmsBtPromotionModel.getCartId().toString());
        if(cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())){
            throw new BusinessException("该渠道没有配置全店特价宝");
        }else{
            CmsBtPromotionModel allPromtoion = promotionDetail.getByPromotionId(Integer.parseInt(cmsChannelConfigBean.getConfigValue1()));
            if(allPromtoion != null){
                Map<String,Object> param = new HashMap<>();
                param.put("promotionId", promotionId);
                List<Map<String,Object>> items = cmsTaskPriceService.getPriceList(param);

                List<String> numIids = items.stream()
                        .filter(item-> item.get("numIid") != null && !StringUtil.isEmpty(item.get("numIid").toString()))
                        .map(item->item.get("numIid").toString())
                        .distinct()
                        .collect(Collectors.toList());
                CmsTeJiaBaoDelMQMessageBody cmsTeJiaBaoDelMQMessageBody = new CmsTeJiaBaoDelMQMessageBody();
                cmsTeJiaBaoDelMQMessageBody.setChannelId(cmsBtPromotionModel.getChannelId());
                cmsTeJiaBaoDelMQMessageBody.setCartId(cmsBtPromotionModel.getCartId());
                cmsTeJiaBaoDelMQMessageBody.setNumIId(numIids);
                cmsTeJiaBaoDelMQMessageBody.setTejiabaoId(Long.parseLong(cmsBtPromotionModel.getTejiabaoId()));
                cmsTeJiaBaoDelMQMessageBody.setSender(getUser().getUserName());
                cmsMqSenderService.sendMessage(cmsTeJiaBaoDelMQMessageBody);

                List<String> codes = items.stream()
                        .filter(item-> item.get("key") != null && !StringUtil.isEmpty(item.get("key").toString()))
                        .map(item->item.get("key").toString())
                        .distinct()
                        .collect(Collectors.toList());

                CmsBtTaskTejiabaoModel cmsBtTaskTejiabaoModel = new CmsBtTaskTejiabaoModel();
                cmsBtTaskTejiabaoModel.setPromotionId(allPromtoion.getPromotionId());
                cmsBtTaskTejiabaoModel.setTaskType(0);
                cmsBtTaskTejiabaoModel.setModifier(getUser().getUserName());
                cmsBtTaskTejiabaoModel.setSynFlg(1);
                codes.forEach(code->{
                    cmsBtTaskTejiabaoModel.setKey(code);
                    cmsTaskPriceService.updateTaskStatus(cmsBtTaskTejiabaoModel);
                });
            }
        }
        return success(null);
    }
}
