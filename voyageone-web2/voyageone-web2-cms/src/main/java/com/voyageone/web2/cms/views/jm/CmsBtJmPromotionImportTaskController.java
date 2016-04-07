package com.voyageone.web2.cms.views.jm;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.help.DateHelp;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionImportTaskService;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionProductService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionExportTaskModel;
import com.voyageone.service.model.jumei.CmsBtJmPromotionImportTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping(
        value = CmsUrlConstants.CmsBtJmPromotionImportTask.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsBtJmPromotionImportTaskController extends CmsController {
    @Autowired
    private CmsBtJmPromotionImportTaskService service;

    @RequestMapping(CmsUrlConstants.CmsBtJmPromotionImportTask.LIST.INDEX.GET_BY_PROMOTIONID)
    public AjaxResponse getByPromotionId(@RequestBody int promotionId) {
        return success(service.getByPromotionId(promotionId));
    }
    @RequestMapping("downloadExcel")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);
        CmsBtJmPromotionImportTaskModel model = service.get(Integer.parseInt(hm.get("id").toString()));
        String importPath = Properties.readValue(CmsConstants.Props.CMS_JM_IMPORT_PATH);
        String fileName = model.getFileName().trim();
        String path = importPath + "/" + fileName;//"/Product20160324164706.xls";
        FileUtils.downloadFile(response, fileName, path);
    }
    @RequestMapping("upload")
    public AjaxResponse upload(HttpServletRequest request, @RequestParam int promotionId) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<String> listFileName=new ArrayList<>();
        String path = Properties.readValue(CmsConstants.Props.CMS_JM_IMPORT_PATH);
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if(multipartResolver.isMultipart(request)){
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while(iter.hasNext()){
                //记录上传过程起始时的时间，用来计算上传时间
                int pre = (int) System.currentTimeMillis();
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if(file != null) {
                    //取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (myFileName.trim() != "") {
                        System.out.println(myFileName);
                        //重命名上传后的文件名
                        String fileName = "demoUpload" + file.getOriginalFilename();
                        String timerstr = DateHelp.DateToString(new Date(), "yyyyMMddHHmmssSSS");
                        //定义上传路径
                        String filepath = path + "/" + timerstr + fileName;
                        File localFile = new File(filepath);
                        file.transferTo(localFile);
                        listFileName.add(timerstr + fileName);
                    }
                }
                //记录上传该文件后的时间
                int finaltime = (int) System.currentTimeMillis();
                System.out.println(finaltime - pre);
            }
        }
        Map<String, List<String>> reponse=null;// = cmsPromotionDetailService.uploadPromotion(input, promotionId, getUser().getUserName());
        // 返回用户信息
        return success(reponse);
    }
}
