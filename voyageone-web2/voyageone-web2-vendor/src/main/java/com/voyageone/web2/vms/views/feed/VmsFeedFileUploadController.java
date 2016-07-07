package com.voyageone.web2.vms.views.feed;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VendorUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     *  保存FeedFile信息
     *
     * @param param 客户端参数
     * @param file 导入文件
     * @return 结果
     */
    @RequestMapping(VendorUrlConstants.FEED.FEED_FILE_IMPORT.UPLOAD_FEED_FILE)
    public AjaxResponse saveUploadImage(@RequestParam Map<String, Object> param, @RequestParam MultipartFile file) {
        return success(null);
    }
}
