package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.CmsUrlConstants.BIREPORT;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchController;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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
    //@RequestMapping(value = BIREPORT.LIST.DOWNLOAD.CREATEXLSFILE)
    public ResponseEntity<byte[]> createXlsFile1()
    {


//        biRepSupport.createXlsFile();
     /*   Local local=request.getLocale();
        String[] file = new String[]{"a.txt","a,b"};
        byte[] bs = file[1].getBytes("UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", new String(file[0].getBytes("UTF-8"), "ISO8859-1"));  //解决文件名中文乱码问题
        return new ResponseEntity<byte[]>(bs, headers, HttpStatus.CREATED);
        return
        return;*/
        //String exportPath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_PATH);
        String exportPath="E:/";
        String fileName="test.xls";
       /* File pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("高级检索 文件下载任务 文件目录不存在 " + exportPath);
            throw new BusinessException("4004");
        }

        exportPath += fileName;
        pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("高级检索 文件下载任务 文件不存在 " + exportPath);
            throw new BusinessException("4004");
        }
     /*   return genResponseEntityFromFile(fileName, exportPath);*//*
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "dict.txt");
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(FileUtils.getUserDirectory()),
                headers, HttpStatus.CREATED);*/
        return null;
    }

    @RequestMapping(value = BIREPORT.LIST.DOWNLOAD.CREATEXLSFILE)
    public ResponseEntity<byte[]> createXlsFile() throws IOException {
        /*String exportPath = "D:/";
        File pathFileObj = new File(exportPath);

        if (!pathFileObj.exists()) {
            $info("高级检索 文件下载任务 文件目录不存在 " + exportPath);
            throw new BusinessException("4004");
        }

        exportPath += "hello.xlsx";
        pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("高级检索 文件下载任务 文件不存在 " + exportPath);
            throw new BusinessException("4004");
        }
       return genResponseEntityFromFile("hello.xls", exportPath);*/
        return genResponseEntityFromBytes("test.txt",biRepConsultService.createXLSFile());
    }

    @RequestMapping(value = BIREPORT.LIST.DOWNLOAD.DOWNLOADTEST)
    public void download(HttpServletResponse res) throws IOException {
        OutputStream os = res.getOutputStream();
        try {
            res.reset();
            res.setHeader("Content-Disposition", "attachment; filename=dict.txt");
            res.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(FileUtils.getUserDirectory()));
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    //    -------------------------------------------------------------------------------------------------------------------------
}
