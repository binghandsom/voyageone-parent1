package com.voyageone.service.impl.com;

import com.voyageone.common.Constants;
import com.voyageone.service.dao.com.ComMtValueChannelDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.ComMtValueChannelModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * synship.com_mt_value_channel表Service
 *
 * @author desmond.li on 2016/8/15.
 * @version 2.4.0
 */
@Service
public class ComMtValueChannelService extends BaseService {

    private final ComMtValueChannelDao comMtValueChannelDao;

    @Autowired
    public ComMtValueChannelService(ComMtValueChannelDao comMtValueChannelDao) {
        this.comMtValueChannelDao = comMtValueChannelDao;
    }

    /**
     * 将一些项目(如：brand,sizeType,productType)的初始化中英文mapping信息插入到synship.com_mt_value_channel表中
     *
     * @param intTypeId  Integer mapping类型id
     * @param channelId  String 渠道id
     * @param mappingKey String mapping key的值
     * @param mappingKey String mapping value的值
     * @param mappingKey String strLangId值
     */
    public void insertComMtValueChannelMapping(Integer intTypeId, String channelId, String mappingKey,
                                               String mappingValue, String modifier) {
        // 插入一条英文版mapping信息
        insertComMtValueChannelMappingInfo(intTypeId, channelId, mappingKey, mappingValue, Constants.LANGUAGE.EN, modifier);
        // 插入一条中文版mapping信息
        insertComMtValueChannelMappingInfo(intTypeId, channelId, mappingKey, mappingValue, Constants.LANGUAGE.CN, modifier);
    }

    /**
     * 将一些项目(如：brand,sizeType,productType)的初始化mapping信息插入到synship.com_mt_value_channel表中
     *
     * @param intTypeId  Integer mapping类型id
     * @param channelId  String 渠道id
     * @param mappingKey String mapping key的值
     * @param mappingValue String mapping value的值
     * @param strLangId String strLangId值
     */
    private void insertComMtValueChannelMappingInfo(Integer intTypeId, String channelId, String mappingKey,
                                                    String mappingValue, String strLangId, String modifier) {

        // 插入一条英文或者中文版mapping信息
        ComMtValueChannelModel brandEnValueChannelModel = new ComMtValueChannelModel();
        brandEnValueChannelModel.setTypeId(intTypeId);
        brandEnValueChannelModel.setChannelId(channelId);
        brandEnValueChannelModel.setValue(mappingKey);      // mapping key
        brandEnValueChannelModel.setName(mappingValue);
        brandEnValueChannelModel.setAddName1(mappingValue);
        brandEnValueChannelModel.setLangId(strLangId);      // "cn"或"en"
        brandEnValueChannelModel.setCreater(modifier);
        brandEnValueChannelModel.setModifier(modifier);
        comMtValueChannelDao.insert(brandEnValueChannelModel);
    }
}

