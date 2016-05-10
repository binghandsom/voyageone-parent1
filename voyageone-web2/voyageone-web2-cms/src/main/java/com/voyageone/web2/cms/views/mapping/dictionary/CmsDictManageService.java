package com.voyageone.web2.cms.views.mapping.dictionary;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.service.bean.cms.DictionaryMasterPropBean;
import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.impl.cms.CustomWordService;
import com.voyageone.service.impl.cms.DictService;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import com.voyageone.service.model.cms.enums.DictionaryMasterProp;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 字典（表达式）的增删改查管理
 * <p>
 * Created by Jonas on 9/11/15.
 */
@Service
public class CmsDictManageService extends BaseAppService {

    @Autowired
    private DictService dictService;

    @Autowired
    private CustomWordService customWordService;

    /**
     * 获取渠道所有字典的简单信息
     */
    public CmsMtPlatformDictModel getDict(CmsMtPlatformDictModel cmsMtPlatformDictModel, UserSessionBean user) {
        cmsMtPlatformDictModel.setOrderChannelId(user.getSelChannelId());
        return dictService.getDict(cmsMtPlatformDictModel);
    }

    /**
     * 获取检索页面初始化的master data数据
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, String language) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取platform信息
        masterData.put("platformList", TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_D, language));

        return masterData;
    }

    /**
     * 获取渠道的所有定义好的字典
     *
     * @param params    dt 请求参数
     * @param channel_id 渠道
     * @return 字典集合
     */
    public Map<String, Object> dtGetAllDict(CmsDictionaryIndexBean params, String channel_id, String language) {

        params.setOrder_channel_id(channel_id);
        params.setLang(language);

        return dictService.getModesAndTotalCountByChannel(params);
    }

    /**
     * 添加一个字典项
     *
     * @param cmsMtPlatformDictModel 字典项
     * @param user           当前用户
     * @return 更新的字典项
     */
    public void addDict(CmsMtPlatformDictModel cmsMtPlatformDictModel, UserSessionBean user) {
        cmsMtPlatformDictModel.setOrderChannelId(user.getSelChannelId());
        cmsMtPlatformDictModel.setCreater(user.getUserName());
        cmsMtPlatformDictModel.setModifier(user.getUserName());

        if (dictService.addDict(cmsMtPlatformDictModel) < 1) {
            // TODO 以后所有的异常msg统一修改
            throw new BusinessException("插入一条新的字典数据失败!");
        }
    }

    /**
     * 删除一个字典项
     *
     * @param cmsMtPlatformDictModel 字典项
     * @return 更新结果
     */
    public int delDict(CmsMtPlatformDictModel cmsMtPlatformDictModel, UserSessionBean userInfo) {
        cmsMtPlatformDictModel.setOrderChannelId(userInfo.getSelChannelId());
        cmsMtPlatformDictModel.setModifier(userInfo.getUserName());
        return dictService.removeDict(cmsMtPlatformDictModel);
    }

    /**
     * 更新一个字典项
     *
     * @param cmsMtPlatformDictModel 字典项
     * @param user         当前用户
     * @return 更新结果
     */
    public int setDict(CmsMtPlatformDictModel cmsMtPlatformDictModel, UserSessionBean user) {
        cmsMtPlatformDictModel.setOrderChannelId(user.getSelChannelId());
        cmsMtPlatformDictModel.setModifier(user.getUserName());
        return dictService.saveDict(cmsMtPlatformDictModel);
    }

    /**
     * 获取可用的主数据属性
     *
     * @return 属性集合
     */
    public Map<String, Object> getMasterProps(UserSessionBean userInfo, String language, CmsSessionBean cmsSessionBean) {
        Map<String, Object> resultInfo = new HashMap<>();

        // TODO 目前是从定义死的数据取得,以后改成从数据库中取得
        // 获取主数据数据
        List<DictionaryMasterPropBean> beans = new ArrayList<>();
        for (DictionaryMasterProp prop : DictionaryMasterProp.values()) {
            beans.add(prop.toBean());
        }
        resultInfo.put("masterProps", beans);

        // 获取字典的数据
        CmsDictionaryIndexBean params = new CmsDictionaryIndexBean();
        params.setOrder_channel_id(userInfo.getSelChannelId());
        params.setLang(language);
        params.setCartId(cmsSessionBean.getPlatformType().get("cartId").toString());
        resultInfo.put("dictionaryProps", dictService.getModesByChannel(params));

        return resultInfo;
    }

    /**
     * 获取可用的自定义逻辑
     *
     * @return 逻辑集合
     */
    public Map<String, Object> getCustoms() {
        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("customs", customWordService.getModels());
        return resultInfo;
    }


}
