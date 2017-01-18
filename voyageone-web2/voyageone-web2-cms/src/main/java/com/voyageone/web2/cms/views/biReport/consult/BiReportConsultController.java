package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.CmsUrlConstants.BIREPORT;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchController;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class BiReportConsultController extends CmsAdvanceSearchController {
    private final BiRepConsultService biRepConsultService;
    private final BiRepSupport biRepSupport;
    @Autowired
    public BiReportConsultController(BiRepConsultService biRepConsultService,BiRepSupport biRepSupport) {
        this.biRepConsultService = biRepConsultService;
        this.biRepSupport=biRepSupport;
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
            if (searchIndexService.getCodeExcelFile(params, getUser(),null , getLang())) {//getCmsSession()
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

    @RequestMapping(value = BIREPORT.LIST.DOWNLOAD.CREATEXLSFILE)
    public ResponseEntity<byte[]> createXlsFile(@RequestParam Map<String, Object> params) {
        String nameCn=null;
        String staDate=null;
        String endDate=null;
        if((String)params.get("nameCn")!=null &&(String)params.get("nameCn")!="")
        {

            nameCn=(String)params.get("nameCn");
            System.out.println(nameCn);
        }
        if((String)params.get("staDate")!=null &&(String)params.get("staDate")!="")
        {
            staDate=(String)params.get("staDate");
            System.out.println(staDate);
        }
        if((String)params.get("endDate")!=null &&(String)params.get("endDate")!="")
        {
            endDate=(String)params.get("endDate");
            System.out.println(endDate);
        }
        Map mapForSelect=new HashMap<String,Object>();
        mapForSelect.put("nameCn",nameCn);
        mapForSelect.put("staDate",staDate);
        mapForSelect.put("endDate",endDate);

        return genResponseEntityFromBytes("shopsale.xlsx",biRepConsultService.createXLSFile(mapForSelect));
    }


}
