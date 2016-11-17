package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.dao.cms.CmsBtShelvesDao;
import com.voyageone.service.fields.cms.CmsBtShelvesModelActive;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesExample;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/11.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
@Service
public class CmsBtShelvesService extends BaseService {
    private final CmsBtShelvesDao cmsBtShelvesDao;

    @Autowired
    public CmsBtShelvesService(CmsBtShelvesDao cmsBtShelvesDao) {
        this.cmsBtShelvesDao = cmsBtShelvesDao;
    }

    public CmsBtShelvesModel getId(Integer id) {
        return cmsBtShelvesDao.select(id);
    }

    public int update(CmsBtShelvesModel cmsBtShelvesModel) {
        return cmsBtShelvesDao.update(cmsBtShelvesModel);
    }

    public Integer insert(CmsBtShelvesModel cmsBtShelvesModel) {
        cmsBtShelvesModel.setActive(CmsBtShelvesModelActive.ACTIVATE);
        cmsBtShelvesModel.setCreated(new Date());
        cmsBtShelvesModel.setModified(new Date());

        if (!checkName(cmsBtShelvesModel)) {
            throw new BusinessException("该货架名称已存在");
        }

        cmsBtShelvesDao.insert(cmsBtShelvesModel);
        return cmsBtShelvesModel.getId();
    }

    public List<CmsBtShelvesModel> selectList(Map map) {
        return cmsBtShelvesDao.selectList(map);
    }

    public List<CmsBtShelvesModel> selectList(CmsBtShelvesModel exampleModel) {
        return cmsBtShelvesDao.selectList(exampleModel);
    }

    public List<CmsBtShelvesModel> selectByChannelId(String channelId) {
        CmsBtShelvesModel byChannelId = new CmsBtShelvesModel();
        byChannelId.setChannelId(channelId);
        byChannelId.setActive(CmsBtShelvesModelActive.ACTIVATE);
        return selectList(byChannelId);
    }

    public boolean checkName(CmsBtShelvesModel exampleModel) {

        CmsBtShelvesExample example = new CmsBtShelvesExample();

        // where channelId = ? and cartId = ? and shelvesName = ?
        CmsBtShelvesExample.Criteria criteria = example.createCriteria()
                .andChannelIdEqualTo(exampleModel.getChannelId())
                .andCartIdEqualTo(exampleModel.getCartId())
                .andShelvesNameEqualTo(exampleModel.getShelvesName());

        Integer id = exampleModel.getId();

        // and id != ?
        if (id != null)
            criteria.andIdNotEqualTo(id);

        return cmsBtShelvesDao.countByExample(example) < 1;
    }
    public List<CmsBtShelvesModel>selectByChannelIdCart(String channelId, Integer cartId){
        Map<String, Object> params = new HashedMap();
        params.put("channelId",channelId);
        params.put("cartId",cartId);
        params.put("active", CmsBtShelvesModelActive.ACTIVATE);
        return cmsBtShelvesDao.selectList(params);
    }
}
