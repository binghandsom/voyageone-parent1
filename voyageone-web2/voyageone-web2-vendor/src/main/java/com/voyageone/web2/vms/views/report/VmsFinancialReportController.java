package com.voyageone.web2.vms.views.report;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * VmsFinancialReportController
 * Created on 16/07/11.
 * @author jeff.duan
 * @version 1.0
 */
@RestController
@RequestMapping(
        value = VmsUrlConstants.REPORT.FINANCIAL_REPORT.ROOT,
        method = RequestMethod.POST
)
public class VmsFinancialReportController extends BaseController {

    @Autowired
    private VmsFinancialReportService vmsFinancialReportService;


    /**
     * 初始化
     *
     * @return 结果
     */
    @RequestMapping(VmsUrlConstants.REPORT.FINANCIAL_REPORT.INIT)
    public AjaxResponse init(){
        // 初始化（取得检索条件信息)
        Map<String, Object> result  = vmsFinancialReportService.init();
        //返回数据的类型
        return success(result);
    }

    /**
     *  检索
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(VmsUrlConstants.REPORT.FINANCIAL_REPORT.SEARCH)
    public AjaxResponse search(@RequestBody Map<String, Object> param){
        Map<String, Object>  result = vmsFinancialReportService.search(param, this.getUser(), this.getLang());
        return success(result);
    }

    /**
     *  承认财务报表
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(VmsUrlConstants.REPORT.FINANCIAL_REPORT.CONFIRM)
    public AjaxResponse confirm(@RequestBody Map<String, Object> param){
        vmsFinancialReportService.confirm(param, this.getUser());
        return success(null);
    }

    /**
     *  下载财务报表文件
     *
     * @return 财务报表文件
     */
    @RequestMapping(VmsUrlConstants.REPORT.FINANCIAL_REPORT.DOWNLOAD_FINANCIAL_REPORT)
    public ResponseEntity downloadFinancialReport(@RequestParam String reportFileName) throws IOException {
        // 财务报表文件路径
        String reportFilePath = com.voyageone.common.configs.Properties.readValue("vms.report");
        reportFilePath += "/" + getUser().getSelChannelId() + "/";

        try(FileInputStream file = new FileInputStream(reportFilePath + reportFileName)) {
            return genResponseEntityFromStream(reportFileName, file);
        } catch (FileNotFoundException ex) {
            // Lost FinancialReportFile.
            throw new BusinessException("8000029");
        }
    }
}
