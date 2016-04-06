package com.voyageone.web2.cms.views.mapping.dictionary;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.bean.DictionaryMasterPropBean;
import com.voyageone.cms.enums.DictionaryMasterProp;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.impl.cms.CustomWordService;
import com.voyageone.service.impl.cms.DictManageService;
import com.voyageone.service.model.cms.CmsMtDictModel;
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
    private DictManageService dictManageService;

    @Autowired
    private CustomWordService customWordService;

    /**
     * 获取渠道所有字典的简单信息
     */
    public CmsMtDictModel getDict(CmsMtDictModel cmsMtDictModel, UserSessionBean user) {
        cmsMtDictModel.setOrder_channel_id(user.getSelChannelId());
        return dictManageService.getDict(cmsMtDictModel);
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

        return dictManageService.getModesAndTotalCountByChannel(params);
    }

    /**
     * 添加一个字典项
     *
     * @param cmsMtDictModel 字典项
     * @param user           当前用户
     * @return 更新的字典项
     */
    public void addDict(CmsMtDictModel cmsMtDictModel, UserSessionBean user) {
        cmsMtDictModel.setOrder_channel_id(user.getSelChannelId());
        cmsMtDictModel.setCreater(user.getUserName());
        cmsMtDictModel.setModifier(user.getUserName());

        if (dictManageService.addDict(cmsMtDictModel) < 1) {
            // TODO 以后所有的异常msg统一修改
            throw new BusinessException("插入一条新的字典数据失败!");
        }
    }

    /**
     * 删除一个字典项
     *
     * @param cmsMtDictModel 字典项
     * @return 更新结果
     */
    public int delDict(CmsMtDictModel cmsMtDictModel, UserSessionBean userInfo) {
        cmsMtDictModel.setOrder_channel_id(userInfo.getSelChannelId());
        cmsMtDictModel.setModifier(userInfo.getUserName());
        return dictManageService.removeDict(cmsMtDictModel);
    }

    /**
     * 更新一个字典项
     *
     * @param cmsMtDictModel 字典项
     * @param user         当前用户
     * @return 更新结果
     */
    public int setDict(CmsMtDictModel cmsMtDictModel, UserSessionBean user) {
        cmsMtDictModel.setOrder_channel_id(user.getSelChannelId());
        cmsMtDictModel.setModifier(user.getUserName());
        return dictManageService.saveDict(cmsMtDictModel);
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
        params.setCart_id(cmsSessionBean.getPlatformType().get("cartId").toString());
        resultInfo.put("dictionaryProps", dictManageService.getModesByChannel(params));

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
