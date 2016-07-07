package com.voyageone.web2.vms.views.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VendorUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/5/5.
 */
@RestController
@RequestMapping(
        value = VendorUrlConstants.FEED.FEED_FILE_IMPORT.ROOT,
        method = RequestMethod.POST
)
public class VmsFeedFileUploadController extends BaseController {

    @Autowired
    private VmsFeedFileUploadService vmsFeedFileUploadService;


    /**
     *  下载Feed文件模板
     *
     * @return Feed文件模板
     */
    @RequestMapping(VendorUrlConstants.FEED.FEED_FILE_IMPORT.DOWNLOAD_SAMPLE_FEED_FILE)
    public ResponseEntity downSampleFeedFile() {
        // Feed文件模板的路径
        String sampleFilePath = com.voyageone.common.configs.Properties.readValue("vms.feed.sample.file");
        FileInputStream file = null;
        try {
            file = new FileInputStream(sampleFilePath);
        } catch (FileNotFoundException e) {
        }
        // 文件模板不存在的情况
        if (file == null) {
            throw new BusinessException("7000069");
        }
        return genResponseEntityFromStream("feed_file_sample.csv", file);
    }


    /**
     *  保存FeedFile文件
     *
     * @param param 客户端参数
     * @param file 导入文件
     * @return 结果
     */
    @RequestMapping(VendorUrlConstants.FEED.FEED_FILE_IMPORT.UPLOAD_FEED_FILE)
    public AjaxResponse saveUploadFeedFile(@RequestParam Map<String, Object> param, @RequestParam MultipartFile file) {
        vmsFeedFileUploadService.saveFeedFile(getUser().getSelChannelId(), getUser().getUserName(), file);
        return success(null);
    }
}
