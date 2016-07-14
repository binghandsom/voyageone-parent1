package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.CmsBtPromotion.EditCmsBtPromotionBean;
import com.voyageone.service.bean.cms.CmsBtPromotionBean;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.util.MapModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author james 2015/12/15
 * @version 2.0.0
 */
@RestController
@RequestMapping(
        value = PROMOTION.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsPromotionIndexController extends CmsController {

    @Autowired
    private CmsPromotionIndexService cmsPromotionService;
    @Autowired
    private PromotionService promotionService;

    @RequestMapping(PROMOTION.LIST.INDEX.INIT)
    public AjaxResponse init() {
        return success(cmsPromotionService.init(getUser().getSelChannelId(), getLang()));
    }
    @RequestMapping(PROMOTION.LIST.INDEX.InitByPromotionId)
    public AjaxResponse initByPromotionId(@RequestBody int PromotionId) {
        return success(cmsPromotionService.initByPromotionId(PromotionId, getUser().getSelChannelId(), getLang()));
    }
    //重构 begin
    //获取指定页数据
    @RequestMapping(PROMOTION.LIST.INDEX.GetPage)
    public AjaxResponse getPage(@RequestBody PageQueryParameters parameters) {
        parameters.put("channelId",getUser().getSelChannelId());
        return success(promotionService.getPage(parameters));
    }
    //获取数量
    @RequestMapping(PROMOTION.LIST.INDEX.GetCount)
    public AjaxResponse getCount(@RequestBody PageQueryParameters parameters) {
        parameters.put("channelId",getUser().getSelChannelId());
        return success(promotionService.getCount(parameters));
    }
    //获取编辑数据
    @RequestMapping(PROMOTION.LIST.INDEX.GetEditModel)
    public  AjaxResponse getEditModel(@RequestBody int promotionId) {
       return success(promotionService.getEditModel(promotionId));
    }
    /**
     * 保存
     */
    @RequestMapping(PROMOTION.LIST.INDEX.SaveEditModel)
    public AjaxResponse saveEditModel(@RequestBody EditCmsBtPromotionBean editModel) {
        String channelId = getUser().getSelChannelId();
        editModel.getPromotionModel().setChannelId(channelId);
        editModel.getPromotionModel().setCreater(getUser().getUserName());
        editModel.getPromotionModel().setModifier(getUser().getUserName());
        promotionService.saveEditModel(editModel);
        return success(null);
    }
    //删除
    @RequestMapping(PROMOTION.LIST.INDEX.DeleteByPromotionId)
    public AjaxResponse deleteByPromotionId(@RequestBody int promotionId ) {
     return success(promotionService.deleteByPromotionId(promotionId));
    }
    //重构 end
    @RequestMapping(PROMOTION.LIST.INDEX.GET_PROMOTION_LIST)
    public AjaxResponse queryList(@RequestBody Map<String, Object> params) {
        return success(promotionService.getPromotionsByChannelId(getUser().getSelChannelId(), params));
    }

//    @RequestMapping({PROMOTION.LIST.INDEX.INSERT_PROMOTION, PROMOTION.LIST.INDEX.UPDATE_PROMOTION})
//    public AjaxResponse insertOrUpdate(@RequestBody CmsBtPromotionBean cmsBtPromotionBean) {
//        String channelId = getUser().getSelChannelId();
//        cmsBtPromotionBean.setChannelId(channelId);
//        cmsBtPromotionBean.setCreater(getUser().getUserName());
//        cmsBtPromotionBean.setModifier(getUser().getUserName());
//        return success(cmsPromotionService.addOrUpdate(cmsBtPromotionBean));
//    }
//    @RequestMapping(PROMOTION.LIST.INDEX.DEL_PROMOTION)
//    public AjaxResponse delPromotion(@RequestBody CmsBtPromotionBean cmsBtPromotionBean) {
//        String channelId = getUser().getSelChannelId();
//        cmsBtPromotionBean.setChannelId(channelId);
//        cmsBtPromotionBean.setCreater(getUser().getUserName());
//        cmsBtPromotionBean.setModifier(getUser().getUserName());
//        return success(cmsPromotionService.delete(cmsBtPromotionBean));
//    }
    @RequestMapping(PROMOTION.LIST.INDEX.PROMOTION_EXPORT)
    public ResponseEntity<byte[]> doExport(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer promotionId, @RequestParam String promotionName)
            throws Exception {

        byte[] data = cmsPromotionService.getCodeExcelFile(promotionId, getUser().getSelChannelId());
        return genResponseEntityFromBytes(String.format("%s(%s).xlsx",promotionName , DateTimeUtil.getLocalTime(getUserTimeZone(), "yyyyMMddHHmmss") , ".xlsx"), data);
    }
}
