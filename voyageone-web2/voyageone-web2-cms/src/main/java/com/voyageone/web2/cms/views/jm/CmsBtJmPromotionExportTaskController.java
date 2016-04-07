package com.voyageone.web2.cms.views.jm;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionExportTaskService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionExportTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
//@RestController
@RequestMapping(
        value = CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsBtJmPromotionExportTaskController extends CmsController {
    @Autowired
    private CmsBtJmPromotionExportTaskService service;
///cms/CmsBtJmPromotionExportTask/index/getByPromotionId
  //  @RequestMapping( value = "/cms/CmsBtJmPromotionExportTask/index/getByPromotionId", method = RequestMethod.POST)//CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.GET_BY_PROMOTIONID)
   // @ResponseBody
  @RequestMapping(CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.GET_BY_PROMOTIONID)
  @ResponseBody
    public AjaxResponse getByPromotionId(@RequestBody int promotionId) {
        return success(service.getByPromotionId(promotionId));
    }
    @RequestMapping("downloadExcel")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);

        CmsBtJmPromotionExportTaskModel model=service.get(Integer.parseInt(hm.get("id").toString()));
        String importPath = Properties.readValue(CmsConstants.Props.CMS_JM_EXPORT_PATH);
        String filePath = importPath+"/"+ model.getFileName().trim();//
        File excelFile = new File(filePath);
       // FileInputStream  fs=new FileInputStream(filePath);
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename="+model.getFileName().trim());
            response.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(excelFile));
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
    // @RequestMapping(value = "/cms/CmsBtJmPromotionExportTask/index/downloadExcel", method = RequestMethod.POST)
    //@ResponseBody
   @RequestMapping("downloadExcel33")
    public Object downloadExcel1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);

        CmsBtJmPromotionExportTaskModel model=service.get(Integer.parseInt(hm.get("id").toString()));
        String filePath = "/usr/JMExport/" + model.getFileName().trim();//"/Product20160324164706.xls";
        File excelFile = new File(filePath);
/* response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");        //改成输出excel文件
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName + ".xls", "utf-8"));*/
       // String fileName="";
       response.reset();
       response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
       response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(model.getFileName(), "utf-8"));


//       response.setCharacterEncoding("utf-8");
//        response.setContentType("multipart/form-data");
//        response.setHeader("Content-Disposition", "attachment;fileName="
//                + fileName);
       InputStream inputStream = new FileInputStream(excelFile);
       OutputStream os = response.getOutputStream();
        try {

            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.flush();
            os.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       finally {
            os.close();
        }
       return null;
    }

}
