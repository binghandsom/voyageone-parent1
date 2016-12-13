package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.service.bean.cms.mt.channel.config.CmsMtChannelConfigInfo;
import com.voyageone.service.bean.cms.mt.channel.config.SaveListInfo;
import com.voyageone.service.bean.com.ChannelPermissionBean;
import com.voyageone.service.dao.cms.CmsMtChannelConfigDao;
import com.voyageone.service.daoext.cms.CmsMtChannelConfigDaoExt;
import com.voyageone.service.daoext.cms.CmsMtChannelConfigDaoExtCamel;
import com.voyageone.service.daoext.com.UserDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.cache.CommCacheControlService;
import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by rex.wu on 2016/11/7.
 */
@Service
public class CmsMtChannelConfigService extends BaseService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CmsMtChannelConfigDaoExt cmsMtChannelConfigDaoExt;
    @Autowired
    private CmsMtChannelConfigDao cmsMtChannelConfigDao;
    @Autowired
    private CommCacheControlService cacheControlService;

    public Map<String, Object> init(String channelId, String userName) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(userName)) {
            List<ChannelPermissionBean> channels = userDao.selectPermissionChannel(userName);
            resultMap.put("channels", channels);
        }
        if (StringUtils.isNotBlank(channelId)) {
            List<CmsMtChannelConfigModel> configs = cmsMtChannelConfigDaoExt.selectByChannelId(channelId);
            resultMap.put("configs", configs);
        }
        resultMap.put("channelId", channelId);
        return resultMap;
    }

    public Map<String, Object> loadByChannel(String channelId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(channelId)) {
            List<CmsMtChannelConfigModel> configs = cmsMtChannelConfigDaoExt.selectByChannelId(channelId);
            resultMap.put("configs", configs);
        }
        resultMap.put("channelId", channelId);
        return resultMap;
    }

    public void insert(CmsMtChannelConfigModel channelConfigModel) {
        String channelId = channelConfigModel.getChannelId();
        String configKey = channelConfigModel.getConfigKey();
        String configCode = channelConfigModel.getConfigCode();
        String comment = channelConfigModel.getComment();
        Integer status = channelConfigModel.getStatus();
        Integer configType = channelConfigModel.getConfigType();
        // String creater = channelConfigModel.getCreater();
        if (StringUtils.isBlank(channelId)) {
            throw new BusinessException("请先选择店铺！");
        }
        if (StringUtils.isBlank(configKey) || StringUtils.isBlank(configCode) || StringUtils.isBlank(comment)) {
            throw new BusinessException("请填写必填项!");
        }
        if (status == null) {
            throw new BusinessException("请填写状态！");
        }
        if (configType == null) {
            throw new BusinessException("请填写设置类型！");
        }
        channelConfigModel.setCreated(new Date());
        cmsMtChannelConfigDao.insert(channelConfigModel);
        this.clearRedisCache();
    }

    public CmsMtChannelConfigModel selectById(Integer id) {
        CmsMtChannelConfigModel target = null;
        if (id != null) {
            target = cmsMtChannelConfigDao.select(id);
        }
        return target;
    }

    public void edit(CmsMtChannelConfigModel channelConfigModel) {
        Integer id = channelConfigModel.getId();
        CmsMtChannelConfigModel target = null;
        if (id == null || (target = cmsMtChannelConfigDao.select(id)) == null) {
            throw new BusinessException("查询不到要修改的配置记录！");
        }
        String configKey = channelConfigModel.getConfigKey();
        String configCode = channelConfigModel.getConfigCode();
        String comment = channelConfigModel.getComment();
        Integer status = channelConfigModel.getStatus();
        Integer configType = channelConfigModel.getConfigType();
        if (StringUtils.isBlank(configKey) || StringUtils.isBlank(configCode) || StringUtils.isBlank(comment)) {
            throw new BusinessException("请填写必填项!");
        }
        if (status == null) {
            throw new BusinessException("请填写状态！");
        }
        if (configType == null) {
            throw new BusinessException("请填写设置类型！");
        }
        channelConfigModel.setModified(new Date());
        cmsMtChannelConfigDao.update(channelConfigModel);
        this.clearRedisCache();
    }

    public void delete(String channelConfigId) {
        String message = "";
        if (StringUtils.isBlank(channelConfigId)) {
            message = "请先选择要删除的记录！";
        } else {
            Integer id = Integer.parseInt(channelConfigId);
            CmsMtChannelConfigModel target = cmsMtChannelConfigDao.select(id);
            if (target == null) {
                message = "查询不到要删除的记录！";
            } else {
                cmsMtChannelConfigDao.delete(id);
                this.clearRedisCache();
            }
        }
        if (StringUtils.isNotBlank(message)) {
            throw new BusinessException(message);
        }
    }

    /**
     * 清空Redis缓存
     */
    private void clearRedisCache() {
        Set<String> cacheKeySet = cacheControlService.getCacheKeySet();
        if (CollectionUtils.isNotEmpty(cacheKeySet)) {
            cacheKeySet.forEach(subCacheKey -> cacheControlService.deleteCache(CacheKeyEnums.KeyEnum.valueOf(subCacheKey)));
        }
    }

    @Autowired
    CmsMtChannelConfigDaoExtCamel cmsMtChannelConfigDaoExtCamel;

    public List<CmsMtChannelConfigInfo> search(Map<String, Object> map) {
        return cmsMtChannelConfigDaoExtCamel.selectConfigInfoList(map);
    }

    //批量保存配置
    @VOTransactional
    public void saveList(SaveListInfo info, String channelId, String userName) {
        List<CmsMtChannelConfigInfo> listDelete = new ArrayList<>();
        List<CmsMtChannelConfigInfo> listUpdate = new ArrayList<>();
        List<CmsMtChannelConfigInfo> listAdd = new ArrayList<>();

        info.getList().forEach(f -> {
            if (f.getId() != null && f.getId() > 0) {
                if (f.getChecked()) {
                    //更新
                    saveList_update(f,userName);
                } else {
                    //删除
                    saveList_delete(f);
                }
            } else {
                if (f.getChecked()) {
                    //新增
                    saveList_add(f, channelId, userName);
                }
            }
        });
    }

    public void saveList_add(CmsMtChannelConfigInfo info, String channelId, String userName) {

        CmsMtChannelConfigModel model = new CmsMtChannelConfigModel();
        model.setChannelId(channelId);
        model.setCreated(new Date());
        model.setCreater(userName);
        model.setComment(info.getComment());
        model.setConfigCode(info.getConfigCode());
        model.setConfigKey(info.getConfigKey());
        model.setConfigType(0);
        model.setConfigValue1(info.getConfigValue1());
        model.setConfigValue2(info.getConfigValue2());
        model.setConfigValue3(info.getConfigValue3());
        model.setStatus(0);
        model.setModified(new Date());
        model.setModifier(userName);
        cmsMtChannelConfigDao.insert(model);
    }

    public void saveList_update(CmsMtChannelConfigInfo info,String userName) {
        CmsMtChannelConfigModel model = cmsMtChannelConfigDao.select(info.getId());

        model.setModifier(userName);

        model.setConfigValue1(info.getConfigValue1());

        model.setConfigValue2(info.getConfigValue2());

        model.setConfigValue3(info.getConfigValue3());

        cmsMtChannelConfigDao.update(model);
    }

    public void saveList_delete(CmsMtChannelConfigInfo info) {
        cmsMtChannelConfigDao.delete(info.getId());
    }
}
