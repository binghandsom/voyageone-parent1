package com.voyageone.web2.vms.views.feed;

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
 * VmsFeedImportResultController
 * Created on 16/07/11.
 * @author jeff.duan
 * @version 1.0
 */
@RestController
@RequestMapping(
        value = VmsUrlConstants.FEED.FEED_IMPORT_RESULT.ROOT,
        method = RequestMethod.POST
)
public class VmsFeedImportResultController extends BaseController {

    @Autowired
    private VmsFeedImportResultService vmsFeedImportResultService;

    /**
     * 初始化
     *
     * @return 结果
     */
    @RequestMapping(VmsUrlConstants.FEED.FEED_IMPORT_RESULT.INIT)
    public AjaxResponse init(@RequestBody Map<String, Object> param){
        param.put("lang", this.getLang());
        // 初始化（取得检索条件信息)
        Map<String, Object> result  = vmsFeedImportResultService.init(param);
        //返回数据的类型
        return success(result);
    }

    /**
     *  检索
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(VmsUrlConstants.FEED.FEED_IMPORT_RESULT.SEARCH)
    public AjaxResponse search(@RequestBody Map<String, Object> param){
        param.put("channelId", this.getUser().getSelChannelId());
        param.put("lang", this.getLang());
        Map<String, Object>  result = vmsFeedImportResultService.search(param);
        return success(result);
    }

    /**
     *  下载Feed错误文件
     *
     * @return Feed错误文件
     */
    @RequestMapping(VmsUrlConstants.FEED.FEED_IMPORT_RESULT.DOWN_FEED_ERROR_FILE)
    public ResponseEntity downloadFeedErrorFile(@RequestParam String errorFileName) throws IOException {
        // Feed错误文件路径
        String checkResultFilePath = com.voyageone.common.configs.Properties.readValue("vms.feed.check");
        checkResultFilePath += "/" + getUser().getSelChannelId() + "/";

        try(FileInputStream file = new FileInputStream(checkResultFilePath + errorFileName)) {
            return genResponseEntityFromStream(errorFileName, file);
        } catch (FileNotFoundException ex) {
            // Lost FeedCheckErrorFile.
            throw new BusinessException("8000018");
        }
    }
}
