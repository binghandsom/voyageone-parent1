package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.dao.cms.CmsMtDictDao;
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
public class DictManageService extends BaseService {

    @Autowired
    private CmsMtDictDao cmsMtDictDao;

    /**
     * 获取渠道所有字典的简单信息
     */
    public CmsMtDictModel getDict(CmsMtDictModel cmsMtDictModel) {
        return cmsMtDictDao.selectById(cmsMtDictModel);
    }

    public List<CmsMtDictModel> getModesByChannel(CmsDictionaryIndexBean params) {
        return cmsMtDictDao.selectByChannel(params);
    }

    public int getCountByChannel(CmsDictionaryIndexBean params) {
        return cmsMtDictDao.selectAllCount(params);
    }

    public Map<String, Object> getModesAndTotalCountByChannel(CmsDictionaryIndexBean params) {
        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("dictionaryList", getModesByChannel(params));
        resultInfo.put("dictionaryListCnt", getCountByChannel(params));
        return resultInfo;
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
            if (cmsMtDictDao.selectByName(cmsMtDictModel).size() > 1) {
                throw new BusinessException("该字典名称已经存在,请重新设定字典名称!");
            }
        }
    }

    /**
     * 添加一个字典项
     */
    public int addDict(CmsMtDictModel cmsMtDictModel) {
        // 检测新字典项数据
        checkDict(cmsMtDictModel, true);
        return cmsMtDictDao.insertDict(cmsMtDictModel);
    }

    /**
     * 删除一个字典项
     */
    public int removeDict(CmsMtDictModel cmsMtDictModel) {
        cmsMtDictDao.insertDictLog(cmsMtDictModel);
        return cmsMtDictDao.deleteDict(cmsMtDictModel);
    }

    /**
     * 更新一个字典项
     */
    public int saveDict(CmsMtDictModel cmsMtDictModel) {
        checkDict(cmsMtDictModel, false);

        CmsMtDictModel oldCmsMtDictModel = getDict(cmsMtDictModel);
        if (!oldCmsMtDictModel.getModified().equals(cmsMtDictModel.getModified())) {
            throw new BusinessException("该条数据已经被其他人更新过了,请确认!");
        }

        cmsMtDictDao.insertDictLog(oldCmsMtDictModel);
        return cmsMtDictDao.updateDict(cmsMtDictModel);
    }
}
