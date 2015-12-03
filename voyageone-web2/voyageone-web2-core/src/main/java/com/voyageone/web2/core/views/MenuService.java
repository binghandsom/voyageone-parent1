package com.voyageone.web2.core.views;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.dao.UserRolePropertyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Index 路径
 * Created on 12/01/15.
 *
 * @author Edward
 * @version 2.0.0
 */
@Service
public class MenuService extends BaseAppService {

    @Autowired
    private UserRolePropertyDao userRolePropertyDao;

    /**
     * 根据userId和ChannelId获取Menu列表.
     * @param userId
     * @param channelId
     * @return
     */
    public List<Map<String, Object>> getMenuList (Integer userId, String channelId) {

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("channelId", channelId);

        return userRolePropertyDao.selectUserMenu(data);
    }

    /**
     * 获取language列表.
     * @return
     */
    public List<TypeBean> getLanguageList() {
        return TypeConfigEnums.MastType.languageType.getList(Constants.LANGUAGE.EN);
    }
}
