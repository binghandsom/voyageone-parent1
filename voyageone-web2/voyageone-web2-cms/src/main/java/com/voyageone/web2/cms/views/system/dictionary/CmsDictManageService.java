package com.voyageone.web2.cms.views.system.dictionary;

import com.voyageone.web2.cms.dao.CmsMtDictDao;
import com.voyageone.web2.cms.model.CmsMtDictModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.core.dao.ChannelShopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
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
    public Map<String, Object> dtGetAllDict(Map params, String channel_id) {

        params.put("channel_id", channel_id);

        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("dictionaryList", cmsMtDictDao.selectByChannel(params));
        resultInfo.put("dictionaryListCnt", cmsMtDictDao.selectAllCount(params));

        return resultInfo;
    }

    /**
     * 添加一个字典项
     *
     * @param dictWordBean 字典项
     * @param user         当前用户
     * @return 更新的字典项
     */
//    public DictWordBean addDict(DictWordBean dictWordBean, UserSessionBean user) {
//
//        dictWordBean.setOrder_channel_id(user.getSelChannelId());
//
//        checkDict(dictWordBean);
//
//        dictWordBean.setCreater(user.getUserName());
//        dictWordBean.setModifier(user.getUserName());
//
//        if (dictDao.insertDict(dictWordBean) < 1)
//            throw new BusinessException(UPDATE_BY_OTHER);
//
//        List<DictWordBean> byName = dictDao.selectByName(dictWordBean);
//        return byName.get(0);
//    }

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
     * 获取可用的 CMS 属性
     *
     * @return CmsModelEnum 集合
     */
//    public List<CmsFieldEnum.CmsModelEnum> getCmsValues() {
//        return Arrays.asList(CmsFieldEnum.CmsModelEnum.values());
//    }

    /**
     * 获取可用的主数据属性
     *
     * @return 属性集合
     */
//    public List<DictMasterPropBean> getMasterProps() {
//
//        List<DictMasterPropBean> beans = new ArrayList<>();
//
//        for (DictMasterProp prop : DictMasterProp.values()) {
//            beans.add(prop.toBean());
//        }
//
//        return beans;
//    }

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

//    private void checkDict(DictWordBean dictWordBean) {
//
//        Channel channel = Channel.valueOfId(dictWordBean.getOrder_channel_id());
//
//        if (channel == null)
//            throw new BusinessException(NOT_FOUND_CHANNEL);
//
//        if (StringUtils.isEmpty(dictWordBean.getName()))
//            throw new BusinessException(NO_NAME);
//
//        if (StringUtils.isEmpty(dictWordBean.getValue()))
//            throw new BusinessException(NO_EXPRESSION);
//
//        if (dictDao.selectByName(dictWordBean).size() > 1)
//            throw new BusinessException(DUP_NAME);
//    }
}
