package com.voyageone.web2.cms.views.jmpromotion;

import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionDownloadImageZipService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionExportTask3Service;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by gjl on 2016/10/18.
 */
@RestController
public class JmPromotionDownloadImageZipController extends CmsController {

    @Autowired
    private CmsBtJmPromotionDownloadImageZipService cmsBtJmPromotionDownloadImageZipService;
    /**
     * 下载专场图片包
     */
    public ResponseEntity downloadSpecialImageZip(@RequestBody Map<String, Object> params) {
        Integer promotionId = (Integer) params.get("promotionId");
        String strZipName ="";
        byte[] data=cmsBtJmPromotionDownloadImageZipService.selectSpecialImagesList(promotionId,strZipName);
        return genResponseEntityFromBytes(strZipName,data);
    }
    /**
     * 下载商品主图包
     */
    public ResponseEntity downloadWaresImageZip(@RequestBody Map<String, Object> params) {
        Integer promotionId = (Integer) params.get("promotionId");
        String strZipName ="";
        byte[] data=cmsBtJmPromotionDownloadImageZipService.selectWaresImageList(promotionId,strZipName);
        return genResponseEntityFromBytes(strZipName,data);
    }
}
