package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.CmsUrlConstants.BIREPORT;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author james 2015/12/15
 * @version 2.0.0
 */
@RestController
@RequestMapping(
        value = BIREPORT.LIST.DOWNLOAD.ROOT,
        method = RequestMethod.POST
)
public class BiReportConsultController extends CmsController {
    private final BiRepConsultService biRepConsultService;

    @Autowired
    public BiReportConsultController(BiRepConsultService biRepConsultService) {
        this.biRepConsultService = biRepConsultService;
    }
    @Autowired
    private CmsAdvanceSearchService searchIndexService;

    @RequestMapping(BIREPORT.LIST.DOWNLOAD.BIREPDOWNLOAD)
    public AjaxResponse biRepDownload(@RequestBody PageQueryParameters platform) {
        return success(biRepConsultService.biRepDownload(platform));
    }
    /**
     * 创建文件下载任务，发送消息到MQ，开始批处理
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.EXPORT_PRODUCTS)
    public AjaxResponse createFile(@RequestBody Map<String, Object> params) {
        String fileName = null;
        Integer fileType = (Integer) params.get("fileType");
        Map<String, Object> resultBean = new HashMap<>();
        if (fileType == null) {
            resultBean.put("ecd", "4002");
            return success(resultBean);
        }
        //bi_report file type parameters from the front, value is null or 6
        if (fileType == 6) {
            fileName = "biRepList_";
        }
        if (fileName == null) {
            resultBean.put("ecd", "4002");
            return success(resultBean);
        }
        try {
            // 文件下载时分页查询要做特殊处理
            if (searchIndexService.getCodeExcelFile(params, getUser(), getCmsSession(), getLang())) {
                resultBean.put("ecd", "0");
                return success(resultBean);
            } else {
                resultBean.put("ecd", "4004");
                return success(resultBean);
            }
        } catch (Throwable e) {
            $error("创建文件时出错", e);
            resultBean.put("ecd", "4003");
            return success(resultBean);
        }
    }




//    -------------------------------------------------------------------------------------------------------------------------
}
