package com.voyageone.service.impl.cms;

import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.beans.PlatformBean;
import com.voyageone.common.configs.dao.PlatformDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/18 10:45
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Service
public class PlatformService extends BaseService {
    @Resource
    PlatformDao platformDao;

    public List<PlatformBean> getAll() {
        return platformDao.getAll();
    }

    /**
     * 返回平台的商品访问url
     * @param cartId
     * @return
     */
    public String getPlatformProductUrl (String cartId) {

        // 取得CMS中默认的显示用模板ID
        String commonTemplateId = Codes.getCodeName("PLATFORM_PRODUCT_URL", cartId);
        if (commonTemplateId == null && !"0".equals(cartId) && !"1".equals(cartId))
            return "";
        return commonTemplateId;
    }
}
