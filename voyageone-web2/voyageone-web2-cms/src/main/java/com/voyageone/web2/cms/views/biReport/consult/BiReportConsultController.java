package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.security.service.ComUserService;
import com.voyageone.service.dao.report.BiReportDownloadTaskDao;
import com.voyageone.service.model.report.BiReportDownloadTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.CmsUrlConstants.BIREPORT;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchController;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author james 2015/12/15
 @version 2.0.0
 */

@RestController
@RequestMapping(
        value = BIREPORT.LIST.DOWNLOAD.ROOT,
        method = RequestMethod.POST
)
public class BiReportConsultController extends CmsAdvanceSearchController {

    @Autowired
    BiRepConsultService biRepConsultService;

    @Autowired
    ComUserService comUserService;


    @RequestMapping(BIREPORT.LIST.DOWNLOAD.DOWNLOADTASKLIST)
    public AjaxResponse downloadTaskList()   //@RequestBody PageQueryParameters searchInfo
    {
        Map<String, Object> mapForTask = new HashedMap();
        mapForTask.put("fileName", null);
        mapForTask.put("creatorId", getUser().getUserId());
        System.out.println("____________________"+getUser().getUserId()+"~~~~~~~~~~~~~~~~~~~~~~~~~"+getUser().getUserName());
        mapForTask.put("taskStatus", null);
        List<BiReportDownloadTaskModel> taskModelList = biRepConsultService.getDownloadTaskList(mapForTask);
        return success(taskModelList);
    }
    //获取指定页数据
    @RequestMapping(BIREPORT.LIST.DOWNLOAD.GETPAGE)
    public AjaxResponse getPage(@RequestBody PageQueryParameters parameters) {
//        parameters.put("channelId", getUser().getSelChannelId());
        parameters.put("creatorId",getUser().getUserId());
        System.out.println("getUserId"+getUser().getUserId());
        return success(biRepConsultService.getPage(parameters));
    }

    //获取数量
    @RequestMapping(BIREPORT.LIST.DOWNLOAD.GETCOUNT)
    public AjaxResponse getCount() {
//        parameters.put("channelId", getUser().getSelChannelId());
        PageQueryParameters parameters=new PageQueryParameters();
        parameters.put("creatorId",getUser().getUserId());
        System.out.println("getUserId"+getUser().getUserId() +"getUserName"+getUser().getUserName());
        return success(biRepConsultService.getCount(parameters));
    }
    /**
     * 下载文件
     * TODO-- 注意：这里没有检查文件访问权限，可能会有问题，可以下载其他人的数据文件(通过伪造文件名)
     * 　　　　　　　 也没考虑过期文件删除的问题
     */
    @RequestMapping(BIREPORT.LIST.DOWNLOAD.BIREPDOWNLOAD)
    public ResponseEntity<byte[]> downloadFile(@RequestParam String fileName,@RequestParam String exportPath ,@RequestParam Integer taskId) {
        File pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("高级检索 文件下载任务 文件目录不存在 " + exportPath);
            throw new BusinessException("4004");
        }

        exportPath += fileName;
        pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("高级检索 文件下载任务 文件不存在 " + exportPath);
            biRepConsultService.outTimeTask(taskId);
            throw new BusinessException("4004");
        }
        return genResponseEntityFromFile(fileName, exportPath);
    }
    @RequestMapping(BIREPORT.LIST.DOWNLOAD.CREATEXLSFILETASK)
    public void createFileTask(@RequestBody Map<String ,Object> map)
    {
        Integer creatorId=getUser().getUserId();
        String creatorName=getUser().getUserName();
        Date createTime=new Date();
        map.put("creatorId",creatorId);
        map.put("creatorName",creatorName);
        map.put("createTime",createTime);
        biRepConsultService.dealTheFileCreateRequest(map);

        //将值传递给biReport生成地方
        return;
    }

    @RequestMapping(BIREPORT.LIST.DOWNLOAD.INIT)
    public AjaxResponse init() {
        return success(biRepConsultService.init(getUser().getSelChannelId(), getLang()));
    }

    @RequestMapping(BIREPORT.LIST.DOWNLOAD.GET_CHANNEL_LIST)
    public AjaxResponse  getChannelsByUser() {
        List<Map<String , String>> channels = comUserService.selectChannelsByUser(getUser().getUserName());

        //目前只返回有数据的channel
        if(channels != null && channels.size() > 0)
        {
            List result = channels.stream().filter(w -> w.get("channel_id").equals("007") || w.get("channel_id").equals("010") || w.get("channel_id").equals("012")
                    || w.get("channel_id").equals("014") || w.get("channel_id").equals("017") || w.get("channel_id").equals("018") || w.get("channel_id").equals("024")
                    || w.get("channel_id").equals("030"))
                    .collect(Collectors.toList());
            return  success(result);
        }

        return success(Collections.EMPTY_LIST) ;



    }
}
