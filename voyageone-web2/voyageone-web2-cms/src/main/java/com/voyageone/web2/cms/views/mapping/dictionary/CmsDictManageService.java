package com.voyageone.web2.cms.views.mapping.dictionary;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.bean.DictionaryMasterPropBean;
import com.voyageone.cms.enums.DictionaryMasterProp;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.dao.cms.CmsMtCustomWordDao;
import com.voyageone.service.dao.cms.CmsMtDictDao;
import com.voyageone.service.model.cms.CmsMtDictModel;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.core.dao.ChannelShopDao;
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
public class CmsDictManageService {

    @Autowired
    private CmsMtDictDao cmsMtDictDao;

    @Autowired
    protected ChannelShopDao channelShopDao;

    @Autowired
    protected CmsMtCustomWordDao customWordDao;

    /**
     * 获取检索页面初始化的master data数据
     * @param userInfo
     * @return
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, String language) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取platform信息
        masterData.put("platformList", TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_D , language));

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

        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("dictionaryList", cmsMtDictDao.selectByChannel(params));
        resultInfo.put("dictionaryListCnt", cmsMtDictDao.selectAllCount(params));

        return resultInfo;
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

        // 检测新字典项数据
        checkDict(cmsMtDictModel);

        cmsMtDictModel.setCreater(user.getUserName());
        cmsMtDictModel.setModifier(user.getUserName());

        if (cmsMtDictDao.insertDict(cmsMtDictModel) < 1)
            // TODO 以后所有的异常msg统一修改
            throw new BusinessException("插入一条新的字典数据失败!");
    }

    /**
     * 删除一个字典项
     *
     * @param cmsMtDictModel 字典项
     * @return 更新结果
     */
    public int delDict(CmsMtDictModel cmsMtDictModel, UserSessionBean userInfo) {
        //插入备份
        cmsMtDictModel.setModifier(userInfo.getUserName());
        cmsMtDictDao.insertDictLog(cmsMtDictModel);
        return cmsMtDictDao.deleteDict(cmsMtDictModel);
    }

    /**
     * 更新一个字典项
     *
     * @param cmsMtDictModel 字典项
     * @param user         当前用户
     * @return 更新结果
     */
    public int setDict(CmsMtDictModel cmsMtDictModel, UserSessionBean user) {
        checkDict(cmsMtDictModel);

        cmsMtDictModel.setModifier(user.getUserName());
        //插入备份
        CmsMtDictModel oldCmsMtDictModel = getDict(cmsMtDictModel, user);
        if (!oldCmsMtDictModel.getModified().equals(cmsMtDictModel.getModified()))
            throw new BusinessException("该条数据已经被其他人更新过了,请确认!");

        cmsMtDictDao.insertDictLog(oldCmsMtDictModel);
        return cmsMtDictDao.updateDict(cmsMtDictModel);
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
        resultInfo.put("dictionaryProps", cmsMtDictDao.selectByChannel(params));

        return resultInfo;
    }

    /**
     * 获取可用的自定义逻辑
     *
     * @return 逻辑集合
     */
    public Map<String, Object> getCustoms() {
        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("customs", customWordDao.selectWithParam());
        return resultInfo;
    }

    /**
     * 获取渠道所有字典的简单信息
     *
     * @param cmsMtDictModel
     * @param userInfo
     * @return
     */
    public CmsMtDictModel getDict(CmsMtDictModel cmsMtDictModel, UserSessionBean userInfo) {
        cmsMtDictModel.setOrder_channel_id(userInfo.getSelChannelId());
        return cmsMtDictDao.selectById(cmsMtDictModel);
    }

    /**
     * 检测现有数据是否符合
     * @param cmsMtDictModel
     */
    private void checkDict(CmsMtDictModel cmsMtDictModel) {

        if (StringUtils.isEmpty(cmsMtDictModel.getName()))
            throw new BusinessException("字典名称不存在!");

        if (StringUtils.isEmpty(cmsMtDictModel.getValue()))
            throw new BusinessException("字典定义内容不存在!");

        if (cmsMtDictDao.selectByName(cmsMtDictModel).size() > 1)
            throw new BusinessException("该字典名称已经存在,请重新设定字典名称!");
    }
}
