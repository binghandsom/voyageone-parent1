package com.voyageone.web2.cms.views.jmpromotion;

import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionDownloadImageZipService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionExportTask3Service;
import com.voyageone.web2.base.ajax.AjaxResponse;
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
    public ResponseEntity downloadSpecialImageZip(@RequestParam Integer promotionId) {
        String strZipName ="11";
        byte[] data=cmsBtJmPromotionDownloadImageZipService.selectSpecialImagesList(promotionId);
        return genResponseEntityFromBytes(strZipName,data);
    }
    /**
     * 下载商品主图包
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.DOWNLOAD_WARES_IMAGE_ZIP)
    public ResponseEntity downloadWaresImageZip(@RequestParam Integer promotionId) {
        String strZipName ="111";
        byte[] data=cmsBtJmPromotionDownloadImageZipService.selectWaresImageList(promotionId);
        return genResponseEntityFromBytes(strZipName,data);
    }
}
