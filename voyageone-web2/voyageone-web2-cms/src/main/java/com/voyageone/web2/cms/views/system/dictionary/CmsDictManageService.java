package com.voyageone.web2.cms.views.system.dictionary;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.bean.DictionaryMasterPropBean;
import com.voyageone.cms.enums.DictionaryMasterProp;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.bean.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.web2.cms.dao.CmsMtDictDao;
import com.voyageone.web2.cms.model.CmsMtDictModel;
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

    /**
     * 获取检索页面初始化的master data数据
     * @param userInfo
     * @return
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取platform信息
        masterData.put("categoryTypeList", channelShopDao.selectChannelShop(userInfo.getSelChannelId()));

        return masterData;
    }

    /**
     * 获取渠道的所有定义好的字典
     *
     * @param params    dt 请求参数
     * @param channel_id 渠道
     * @return 字典集合
     */
    public Map<String, Object> dtGetAllDict(CmsDictionaryIndexBean params, String channel_id) {

        params.setOrder_channel_id(channel_id);

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
        // TODO 目前默认值是0,以后再修改
        cmsMtDictModel.setCart_id("0");

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
     * @param dictWordBean 字典项
     * @param user         当前用户
     * @return 更新结果
     */
//    public int setDict(DictWordBean dictWordBean, UserSessionBean user) {
//        checkDict(dictWordBean);
//
//        dictWordBean.setModifier(user.getUserName());
//        //插入备份
//        dictDao.insertDictLog(dictWordBean);
//        return dictDao.updateDict(dictWordBean);
//    }

    /**
     * 获取可用的 CMS 属性
     *
     * @return CmsModelEnum 集合
     */
//    public List<CmsDictionaryEnum.CmsModelEnum> getCmsValues() {
//        return Arrays.asList(CmsDictionaryEnum.CmsModelEnum.values());
//    }

    /**
     * 获取可用的主数据属性
     *
     * @return 属性集合
     */
    public Map<String, Object> getMasterProps(UserSessionBean userInfo) {
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
        resultInfo.put("dictionaryProps", cmsMtDictDao.selectByChannel(params));

        return resultInfo;
    }

    /**
     * 获取可用的自定义逻辑
     *
     * @return 逻辑集合
     */
//    public List<CustomWord> getCustomWords() {
//        return customWordDao.selectWithParam();
//    }

    /**
     * 获取渠道所有字典的简单信息
     *
     * @param channel_id 当前所选渠道
     * @return 字典集合
     */
//    public List<DictWordBean> getDictList(String channel_id) {
//        return dictDao.selectSimpleDict(channel_id);
//    }

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
