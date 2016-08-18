package com.voyageone.web2.vms.views.home;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VmsUrlConstants;
import com.voyageone.web2.vms.views.inventory.VmsInventoryFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * VmsHomeInfoController
 * Created on 16/07/11.
 * @author jeff.duan
 * @version 1.0
 */
@RestController
@RequestMapping(
        value = VmsUrlConstants.HOME.HOME_INFO.ROOT,
        method = RequestMethod.POST
)
public class VmsHomeInfoController extends BaseController {

    @Autowired
    private VmsHomeInfoService vmsHomeInfoService;


    /**
     *  初始化
     *
     * @return 统计结果
     */
    @RequestMapping(VmsUrlConstants.HOME.HOME_INFO.INIT)
    public AjaxResponse init() throws IOException {
        Map<String, Object> result = vmsHomeInfoService.init(this.getUser().getSelChannelId());
        return success(result);
    }
}
