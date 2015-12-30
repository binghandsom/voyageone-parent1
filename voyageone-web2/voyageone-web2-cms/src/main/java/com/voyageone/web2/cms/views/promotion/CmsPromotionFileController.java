package com.voyageone.web2.cms.views.promotion;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION;
import com.voyageone.web2.cms.model.CmsBtPromotionCodeModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2015/12/21.
 */
@RestController
//@RequestMapping(
//        value = PROMOTION.DETAIL.ROOT,
//        method = RequestMethod.POST
//)
@RequestMapping(
//        value = PROMOTION.FILE.ROOT,
        method = RequestMethod.GET
)
public class CmsPromotionFileController extends CmsController {

    @Autowired
    private CmsPromotionFileService cmsPromotionFileService;

//    @RequestMapping(PROMOTION.FILE.GET_CODE_FILE)
    public ResponseEntity<byte[]> getCodeFile(@RequestBody Map params) throws IOException, InvalidFormatException {

        byte[] fileCode = cmsPromotionFileService.getCodeExcelFile(params);

        String cartId = (String)params.get("cartId");
        String channelId = (String)params.get("channelId");
        String fileName = String.format("[PRICE-DOC][%s][IMS-PRICE-BEAT][%s].xlsx", cartId, DateTimeUtil.getLocalTime(getUserTimeZone()));

        return genResponseEntityFromBytes(fileName, fileCode);
    }
}
