package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.dao.cms.CmsBtJmPromotionTagProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionTagProductDaoExt;
import com.voyageone.service.model.cms.CmsBtJmPromotionTagProductModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Created by dell on 2016/11/7.
 */
@Service
public class CmsBtJmPromotionTagProductService {

    @Autowired
    CmsBtJmPromotionTagProductDao dao;
    @Autowired
    CmsBtJmPromotionTagProductDaoExt daoExt;

    @VOTransactional
    public void updateJmPromotionTagProduct(List<TagTreeNode> tagList, String channelId, int jmPromotionProductId, String userName) {
        if (tagList == null) return;
        tagList.forEach(f -> {
            if (f.getChecked() == 0) {
                //删除
                daoExt.deleteByTagIdJmPromotionProductId(jmPromotionProductId, f.getId());
            } else if (f.getChecked() == 2) {
                if (get(jmPromotionProductId, f.getId()) == null) {
                    //不存在 新增
                    CmsBtJmPromotionTagProductModel model = new CmsBtJmPromotionTagProductModel();
                    model.setTagName(f.getName());
                    model.setCmsBtTagId(f.getId());
                    model.setChannelId(channelId);
                    model.setCmsBtJmPromotionProductId(jmPromotionProductId);
                    model.setCreater(userName);
                    model.setModifier(userName);
                    model.setCreated(new Date());
                    model.setModified(new Date());
                    dao.insert(model);
                }
            }
        });
    }
    public CmsBtJmPromotionTagProductModel get(int jmPromotionProductId, int tagId) {
        Map<String, Object> map = new HashedMap();
        map.put("cmsBtTagId", tagId);
        map.put("cmsBtJmPromotionProductId", jmPromotionProductId);
        return dao.selectOne(map);
    }
    public  int batchDeleteByCodes(List<String> codeList,int promotionId)
    {
        return   daoExt.batchDeleteByCodes(codeList, promotionId);
    }
}
