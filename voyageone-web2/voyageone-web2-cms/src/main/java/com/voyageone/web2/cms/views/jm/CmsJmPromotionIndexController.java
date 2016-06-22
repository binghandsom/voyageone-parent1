package com.voyageone.web2.cms.views.jm;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotion3Service;
import com.voyageone.service.impl.cms.jumei2.JmBtDealImportService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(
        value = CmsUrlConstants.JMPROMOTION.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsJmPromotionIndexController extends CmsController {
    @Autowired
    private CmsBtJmPromotionService service;
    @Autowired
    private CmsBtJmPromotion3Service service3;

    @RequestMapping(CmsUrlConstants.PROMOTION.LIST.INDEX.INIT)
    public AjaxResponse init() {
        return success(service.init());
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GET_LIST_BY_WHERE)
    public AjaxResponse getListByWhere(@RequestBody Map params) {
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        return success(service.getListByWhere(params));
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GetEditModel)
    public AjaxResponse getEditModel(@RequestBody int id) {
        return success(service.getEditModel(id));
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.SaveModel)
    public AjaxResponse saveModel(@RequestBody CmsBtJmPromotionSaveBean parameter) {
        String channelId = getUser().getSelChannelId();
        String userName = getUser().getUserName();
        parameter.getModel().setChannelId(channelId);
        return success(service.saveModel(parameter, userName, channelId));
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GET)
    public Object get(@RequestBody int id) {//@RequestParam("id")
        return success(service.select(id));
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.DELETE)
    public Object delete(@RequestBody int id) {
        service.delete(id);
        return success(0);
    }

    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GetTagListByPromotionId)
    public AjaxResponse getTagListByPromotionId(@RequestBody int promotionId) {
        return success(service3.getTagListByPromotionId(promotionId));
    }

    @Autowired
    JmBtDealImportService serviceJmBtDealImport;

    //"/cms/jmpromotion/index/importJM"
    @RequestMapping(value = CmsUrlConstants.JMPROMOTION.LIST.INDEX.ImportJM, method = RequestMethod.GET)
    public Object importChannel(@RequestParam("channelId") String channelId) {
        // return success(service3.getTagListByPromotionId(promotionId));
        return serviceJmBtDealImport.importJM(channelId);
        // return "true";
    }
}
