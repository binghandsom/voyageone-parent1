package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(PROMOTION.LIST.INDEX.INIT)
    public AjaxResponse init() {
        return success(cmsPromotionService.init(getUser().getSelChannelId(), getLang()));
    }

    @RequestMapping(PROMOTION.LIST.INDEX.GET_PROMOTION_LIST)
    public AjaxResponse queryList(@RequestBody Map<String, Object> params) {
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        return success(cmsPromotionService.queryByCondition(params));
    }

    @RequestMapping({PROMOTION.LIST.INDEX.INSERT_PROMOTION, PROMOTION.LIST.INDEX.UPDATE_PROMOTION})
    public AjaxResponse insertOrUpdate(@RequestBody CmsBtPromotionModel cmsBtPromotionModel) {
        String channelId = getUser().getSelChannelId();
        cmsBtPromotionModel.setChannelId(channelId);
        cmsBtPromotionModel.setCreater(getUser().getUserName());
        cmsBtPromotionModel.setModifier(getUser().getUserName());
        return success(cmsPromotionService.addOrUpdate(cmsBtPromotionModel));
    }

    @RequestMapping(PROMOTION.LIST.INDEX.DEL_PROMOTION)
    public AjaxResponse delPromotion(@RequestBody CmsBtPromotionModel cmsBtPromotionModel) {
        String channelId = getUser().getSelChannelId();
        cmsBtPromotionModel.setChannelId(channelId);
        cmsBtPromotionModel.setCreater(getUser().getUserName());
        cmsBtPromotionModel.setModifier(getUser().getUserName());
        return success(cmsPromotionService.delete(cmsBtPromotionModel));
    }


    @RequestMapping(PROMOTION.LIST.INDEX.PROMOTION_EXPORT)
    public ResponseEntity<byte[]> doExport(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer promotionId, @RequestParam String promotionName)
            throws Exception {

        byte[] data = cmsPromotionService.getCodeExcelFile(promotionId, getUser().getSelChannelId());
        return genResponseEntityFromBytes(String.format("%s(%s).xlsx",promotionName , DateTimeUtil.getLocalTime(getUserTimeZone(), "yyyyMMddHHmmss") , ".xlsx"), data);

    }
}
