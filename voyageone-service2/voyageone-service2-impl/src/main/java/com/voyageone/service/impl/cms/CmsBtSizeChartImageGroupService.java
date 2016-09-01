package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtSizeChartImageGroupDao;
import com.voyageone.service.daoext.cms.CmsBtSizeChartImageGroupDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtSizeChartImageGroupModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/8/25.
 */
@Service
public class CmsBtSizeChartImageGroupService extends BaseService {
    @Autowired
    CmsBtSizeChartImageGroupDao dao;
    @Autowired
    CmsBtSizeChartImageGroupDaoExt daoExt;

    public List<CmsBtSizeChartImageGroupModel> getList(String channelId) {
        Map<String, Object> map = new HashedMap();
        map.put("channelId", channelId);
        return dao.selectList(map);
    }

    public CmsBtSizeChartImageGroupModel get(String channelId, int cartId, int cmsBtSizeChartId, long cmsBtImageGroupId) {
        Map<String, Object> map = new HashedMap();
        map.put("cmsBtSizeChartId", cmsBtSizeChartId);
        map.put("cmsBtImageGroupId", cmsBtImageGroupId);
        map.put("channelId", channelId);
        map.put("cartId", cartId);
        return dao.selectOne(map);
    }

    public void save(String channelId, int cartId, int cmsBtSizeChartId, long cmsBtImageGroupId, String userName) {
        CmsBtSizeChartImageGroupModel model = get(channelId, cartId, cmsBtSizeChartId, cmsBtImageGroupId);
        if (model == null) {// 不存在新增
            model = new CmsBtSizeChartImageGroupModel();
            model.setChannelId(channelId);
            model.setCartId(cartId);
            model.setCmsBtImageGroupId(cmsBtImageGroupId);
            model.setCmsBtSizeChartId(cmsBtSizeChartId);
            model.setCreater(userName);
            model.setCreated(new Date());
            model.setModified(new Date());
            model.setModifier(userName);
            dao.insert(model);
        }
    }

    public int deleteByCmsBtSizeChartId(String channelId, int cmsBtSizeChartId) {
        return daoExt.deleteByCmsBtSizeChartId(channelId, cmsBtSizeChartId);
    }

    public int deleteByCmsBtImageGroupId(String channelId, long cmsBtImageGroupId) {
        return daoExt.deleteByCmsBtImageGroupId(channelId, cmsBtImageGroupId);
    }

    public List<CmsBtSizeChartImageGroupModel> getListByCmsBtSizeChartId(String channelId, int cmsBtSizeChartId) {
        Map<String, Object> map = new HashedMap();
        map.put("channelId", channelId);
        map.put("cmsBtSizeChartId", cmsBtSizeChartId);
        return dao.selectList(map);
    }
}
