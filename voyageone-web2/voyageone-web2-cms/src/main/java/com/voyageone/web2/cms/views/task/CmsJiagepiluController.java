package com.voyageone.web2.cms.views.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.service.bean.cms.task.beat.SearchTaskJiagepiluBean;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.TaskService;
import com.voyageone.service.impl.cms.task.JiagepiluService;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.beat.ReqParam;
import com.voyageone.web2.cms.bean.task.AddJiagepiluProductRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 价格披露控制器
 *
 * @Author rex.wu
 * @Create 2017-06-21 10:45
 */
@RestController
@RequestMapping(value = CmsUrlConstants.TASK.JIAGEPILU.ROOT)
public class CmsJiagepiluController extends BaseController {

    @Autowired
    private JiagepiluService jiagepiluService;
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = CmsUrlConstants.TASK.JIAGEPILU.GET_TASK_MODEL)
    public AjaxResponse getJiagepiluTaskModel(@RequestBody Map<String, Object> requestParams) {

        Integer taskId = (Integer) requestParams.get("taskId");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("task", jiagepiluService.getJiagepiluTaskModel(taskId));
        resultMap.put("beatFlags", BeatFlag.values());
        resultMap.put("imageStatuses", ImageStatus.values());
        return success(resultMap);
    }

    @RequestMapping(value = CmsUrlConstants.TASK.JIAGEPILU.GET_IMPORT_INFO_LIST)
    public AjaxResponse jiagepiluTaskDetail(@RequestBody Map<String, Object> requestParams) {
        Integer taskId = (Integer) requestParams.get("taskId");
        return success(jiagepiluService.getImportInfoList(taskId));
    }

    /**
     * 下载商品导入模板文件
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.DOWNLOAD_IMPORT_TEMPLATE)
    public ResponseEntity<byte[]> downloadImportTemplate() {

        String filePath = Properties.readValue(CmsProperty.Props.CMS_JIAGEPILU_IMPORT_TEMPLATE_PATH);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new BusinessException(String.format("上传模板文件(%s)不存在", filePath));
        }

        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            return genResponseEntityFromBytes("template.xlsx", bytes);
        } catch (IOException e) {
            throw new BusinessException("下载导入模板文件出错了");
        }

    }

    /**
     * 导入价格披露任务商品
     *
     * @param taskId 价格披露TaskId
     * @param file   导入Excel文件
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.IMPORT)
    public AjaxResponse importBeat(@RequestParam Integer taskId, @RequestParam MultipartFile file) {

        jiagepiluService.importProduct(taskId, file, getUser().getUserName());
        return success("");
    }


    /**
     * 搜索价格披露任务商品
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.SEARCH)
    public AjaxResponse getImportInfoList(@RequestBody SearchTaskJiagepiluBean searchBean) {
        return success(jiagepiluService.search(searchBean));
    }

    /**
     * 下载价格披露任务导入错误文件
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.DOWNLOAD_IMPORT_ERROR)
    public ResponseEntity<byte[]> downloadImportError(@RequestParam String fileName) {
        String exportPath = Properties.readValue(CmsProperty.Props.CMS_JIAGEPILU_IMPORT_ERROR_PATH);
        File pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("价格披露任务导入错误文件目录不存在" + exportPath);
            throw new BusinessException("4004");
        }

        exportPath += fileName;
        pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("价格披露任务导入错误文件不存在" + exportPath);
            throw new BusinessException("4004");
        }
        return genResponseEntityFromFile(fileName, exportPath);
    }

    /**
     * 添加或编辑价格披露任务商品
     *
     * @param request 请求参数
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.ADD_JIAGEPILU_PRODUCT)
    public AjaxResponse addJiagepiluProduct(@RequestBody AddJiagepiluProductRequest request) {
        return success(jiagepiluService.addJiagepiluProduct(request.getId(), request.getTaskId(),
                request.getNumIid(), request.getProductCode(), request.getPrice(), getUser().getUserName()));
    }

    /**
     * 操作(启动/停止/还原)价格披露任务产品(单品或者全品)
     *
     * @param param 请求参数
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.OPERATE_PRODUCT)
    public AjaxResponse operateProduct(@RequestBody ReqParam param) {
        return success(jiagepiluService.operateProduct(param.getBeat_id(), param.getTask_id(), param.getFlag(), param.getForce(), getUser().getUserName()));
    }

    /**
     * 失败或者还原的商品重启动
     * @param param
     * @return
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.REBEATING)
    public AjaxResponse reBeating(@RequestBody ReqParam param) {
        return success(jiagepiluService.reBeating(param.getTask_id(), param.getFlag(), getUser().getUserName()));
    }

    /**
     * 获取要编辑的商品
     * @param param
     * @return
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.GET_EDIT_PRODUCT)
    public AjaxResponse getEditProduct(@RequestBody ReqParam param) {
        return success(jiagepiluService.getProductById(param.getBeat_id()));
    }

    /**
     * 获取价格披露任务可选的平台(天猫系和京东系)
     * @return
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.GET_JIAGEPILU_CARTS)
    public AjaxResponse getJiagepiluCarts() {
        List<CartBean> carts = new ArrayList<>();
        List<CartBean> cartBeans = Carts.getAllCartList();
        for (CartBean cartBean : cartBeans) {
            if ("1".equalsIgnoreCase(cartBean.getPlatform_id()) || "2".equalsIgnoreCase(cartBean.getPlatform_id())) {
                carts.add(cartBean);
            }
        }
        return success(carts);
    }

    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.DOWNLOAD)
    public ResponseEntity<byte[]> download(@RequestParam int taskId) {

        CmsBtTasksModel task = taskService.getTaskById(taskId);

        String filename = String.format("%s.xls", task.getTaskName());

        return genResponseEntityFromBytes(filename, jiagepiluService.download(taskId));
    }

    /**
     * 获取价格披露商品状态分类统计数
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.GET_SUMMARY)
    public AjaxResponse getSummary(@RequestBody ReqParam param) {
        return success(jiagepiluService.getTaskSummary(param.getTask_id()));
    }

    /**
     * 删除价格披露任务
     */
    @RequestMapping(CmsUrlConstants.TASK.JIAGEPILU.DELETE_JIAGEPILU_TASK)
    public AjaxResponse deleteJiagepiluTask(@RequestBody ReqParam param) {
        return success(jiagepiluService.deleteJiagepiluTask(param.getTask_id(), getUser().getUserName()));
    }

}
