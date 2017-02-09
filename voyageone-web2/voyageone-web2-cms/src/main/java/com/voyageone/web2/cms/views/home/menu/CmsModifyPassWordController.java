package com.voyageone.web2.cms.views.home.menu;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.security.service.ComUserService;
import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.impl.com.user.AdminUserService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Created by gjl on 2017/1/5.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.HOME.MODIFY_PASS_WORD.ROOT, method = {RequestMethod.POST})
public class CmsModifyPassWordController  extends CmsController {

    @Autowired
    private ComUserService comUserService ;
    @Autowired
    private AdminUserService adminUserService ;
    /**
     * 更改密码保存
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.SAVE)
    public AjaxResponse saveList(@RequestBody Map password) {
        UserSessionBean user = getUser();
        String oldPassword = (String) password.get("oldPassword");
        String newPassword = (String) password.get("newPassword");
        try {
            return success(comUserService.changePass(user.getUserName(),oldPassword,newPassword,user.getUserName()));
        } catch (Exception e) {
            throw new BusinessException("原始密码错误");
        }
    }
}
