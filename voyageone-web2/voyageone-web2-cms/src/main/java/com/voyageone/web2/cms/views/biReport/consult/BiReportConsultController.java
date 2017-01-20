package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.service.dao.report.BiReportDownloadTaskDao;
import com.voyageone.service.model.report.BiReportDownloadTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.CmsUrlConstants.BIREPORT;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchController;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    @Autowired
    public BiReportConsultController(BiRepConsultService biRepConsultService) {
        this.biRepConsultService = biRepConsultService;
    }
    @Autowired
    private CmsAdvanceSearchService searchIndexService;
    @Autowired
    private BiReportDownloadTaskDao biReportDownloadTaskDao;

    @RequestMapping(BIREPORT.LIST.DOWNLOAD.BIREPDOWNLOAD)
    public AjaxResponse biRepDownload(@RequestBody PageQueryParameters platform) {
        return success(biRepConsultService.biRepDownload(platform));
    }
    @RequestMapping(BIREPORT.LIST.DOWNLOAD.DOWNLOADTASKLIST)
    public AjaxResponse downloadTaskList()   //@RequestBody PageQueryParameters searchInfo
    {
        Map<String,Object> mapForTask=new HashedMap();
        mapForTask.put("fileName",null);
        mapForTask.put("creatorId",1);
        mapForTask.put("taskStatus",null);
        List<BiReportDownloadTaskModel> taskModelList=biRepConsultService.getDownloadTaskList(mapForTask);
        return success(taskModelList);
    }
    /**
     * 创建文件下载任务
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
    public AjaxResponse createXlsFile(@RequestParam Map<String, Object> params) {
        Map<String, Object> resultBean = new HashMap<>();
        Map<String,Map> mapForPara=getParamMap(params);
        Map<String,Object> mapForTask=mapForPara.get("mapForTask");
        Map<String,Object> mapForSelect=mapForPara.get("mapForSelect");
        String fileName=(String)mapForSelect.get("fileName");

        CreateXlsThread createXlsThread=new CreateXlsThread(fileName,mapForTask,mapForSelect,biRepConsultService,biReportDownloadTaskDao);
        Thread t=new Thread(createXlsThread);
        t.start();
      /*  }*/

        return null;
    }
    @RequestMapping("")
    public void createDownloadTask(@RequestParam Map<String,Object> parameters)
    {
        int fileType=1;
        String fileName=biRepConsultService.getName(parameters,fileType);
        int taskStatus=1;

    }

    /**
     * 获取查询channel数据库中的参数，用来查询用于创建excel数据
     * @param params
     * @return
     */
    public Map<String,Map> getParamMap(Map<String,Object> params)
    {
        //获取mapforSelelct,用于从数据库中查找创建excel文件的数据
        Integer fileType=(Integer)params.get("fileType");
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

        Map<String,Object> mapForSelect=new HashMap();
        mapForSelect.put("nameCn",nameCn);
        mapForSelect.put("staDate",staDate);
        mapForSelect.put("endDate",endDate);

        String fileName=biRepConsultService.getName(mapForSelect,2);
        mapForSelect.put("fileName",fileName);
        Map<String ,Object> mapForTask=new HashedMap();
        mapForTask.put("fileName",fileName);
        mapForTask.put("creatorId",1);

        Map<String,Map> totalParameters=new HashedMap();
        totalParameters.put("mapForTask",mapForTask);
        totalParameters.put("mapForSelect",mapForSelect);

        return totalParameters;
    }
}
