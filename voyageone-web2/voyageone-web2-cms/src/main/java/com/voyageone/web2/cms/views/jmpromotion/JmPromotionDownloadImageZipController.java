package com.voyageone.web2.cms.views.jmpromotion;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionDownloadImageZipService;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Created by gjl on 2016/10/18.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.JMPROMOTION.Images.ROOT, method = RequestMethod.POST)
public class JmPromotionDownloadImageZipController extends CmsController {

    @Autowired
    private CmsBtJmPromotionDownloadImageZipService cmsBtJmPromotionDownloadImageZipService;
    /**
     * 下载专场图片包
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.DOWNLOAD_SPECIAL_IMAGE_ZIP)
    public ResponseEntity downloadSpecialImageZip(@RequestParam Integer jmPromotionId,@RequestParam String promotionName) {
        byte[] data=cmsBtJmPromotionDownloadImageZipService.selectSpecialImagesList(jmPromotionId);
        return genResponseEntityFromBytes(String.format("%s(%s).zip",promotionName , DateTimeUtil.getLocalTime(getUserTimeZone(), "yyyyMMddHHmmss") , ".zip"), data);
    }
    /**
     * 下载商品主图包
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.DOWNLOAD_WARES_IMAGE_ZIP)
    public ResponseEntity downloadWaresImageZip(@RequestParam Integer jmPromotionId,@RequestParam String promotionName) {
        byte[] data=cmsBtJmPromotionDownloadImageZipService.selectWaresImageList(jmPromotionId);
        return genResponseEntityFromBytes(String.format("%s(%s).zip",promotionName , DateTimeUtil.getLocalTime(getUserTimeZone(), "yyyyMMddHHmmss") , ".zip"), data);
    }
}
