package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.daoext.cms.CmsMtDictDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtDictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典（表达式）的增删改查管理
 * <p>
 * Created by Jonas on 9/11/15.
 */
@Service
public class DictService extends BaseService {

    @Autowired
    private CmsMtDictDaoExt cmsMtDictDaoExt;

    /**
     * 获取渠道所有字典的简单信息
     */
    public CmsMtDictModel getDict(CmsMtDictModel cmsMtDictModel) {
        return cmsMtDictDaoExt.selectById(cmsMtDictModel);
    }

    public List<CmsMtDictModel> getModesByChannel(CmsDictionaryIndexBean params) {
        return cmsMtDictDaoExt.selectByChannel(params);
    }

    public int getCountByChannel(CmsDictionaryIndexBean params) {
        return cmsMtDictDaoExt.selectAllCount(params);
    }

    public Map<String, Object> getModesAndTotalCountByChannel(CmsDictionaryIndexBean params) {
        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("dictionaryList", getModesByChannel(params));
        resultInfo.put("dictionaryListCnt", getCountByChannel(params));
        return resultInfo;
    }

    public List<CmsMtDictModel> getModesByChannelCartId(String order_channel_id, int cartId) {
        return cmsMtDictDaoExt.selectByChannelCartId(order_channel_id, cartId);
    }

    /**
     * 检测现有数据是否符合
     * @param cmsMtDictModel CmsMtDictModel
     */
    private void checkDict(CmsMtDictModel cmsMtDictModel, boolean isNameCheck) {

        if (StringUtils.isEmpty(cmsMtDictModel.getName()))
            throw new BusinessException("字典名称不存在!");

        if (StringUtils.isEmpty(cmsMtDictModel.getValue()))
            throw new BusinessException("字典定义内容不存在!");

        if (isNameCheck) {
            if (cmsMtDictDaoExt.selectByName(cmsMtDictModel).size() > 1) {
                throw new BusinessException("该字典名称已经存在,请重新设定字典名称!");
            }
        }
    }

    /**
     * 添加一个字典项
     */
    @VOTransactional
    public int addDict(CmsMtDictModel cmsMtDictModel) {
        // 检测新字典项数据
        checkDict(cmsMtDictModel, true);
        return cmsMtDictDaoExt.insertDict(cmsMtDictModel);
    }

    /**
     * 删除一个字典项
     */
    @VOTransactional
    public int removeDict(CmsMtDictModel cmsMtDictModel) {
        cmsMtDictDaoExt.insertDictLog(cmsMtDictModel);
        return cmsMtDictDaoExt.deleteDict(cmsMtDictModel);
    }

    /**
     * 更新一个字典项
     */
    @VOTransactional
    public int saveDict(CmsMtDictModel cmsMtDictModel) {
        checkDict(cmsMtDictModel, false);

        CmsMtDictModel oldCmsMtDictModel = getDict(cmsMtDictModel);
        if (!oldCmsMtDictModel.getModified().equals(cmsMtDictModel.getModified())) {
            throw new BusinessException("该条数据已经被其他人更新过了,请确认!");
        }

        cmsMtDictDaoExt.insertDictLog(oldCmsMtDictModel);
        return cmsMtDictDaoExt.updateDict(cmsMtDictModel);
    }
}
