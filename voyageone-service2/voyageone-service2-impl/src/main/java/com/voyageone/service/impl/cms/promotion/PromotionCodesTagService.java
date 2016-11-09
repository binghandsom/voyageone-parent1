package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdatePromotionProductTagParameter;
import com.voyageone.service.dao.cms.CmsBtPromotionCodesTagDao;
import com.voyageone.service.daoext.cms.CmsBtPromotionCodesTagDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionCodesTagModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/11/4.
 */
@Service
public class PromotionCodesTagService extends BaseService {

    @Autowired
    CmsBtPromotionCodesTagDaoExt cmsBtPromotionCodesTagDaoExt;

    @Autowired
    CmsBtPromotionCodesTagDao cmsBtPromotionCodesTagDao;

    @VOTransactional
    public void updatePromotionCodesTag(List<TagTreeNode> tagList, String channelId, int promotionCodesId,String userName) {
        if(tagList==null) return;
        tagList.forEach(f -> {
            if (f.getChecked() == 0) {
                //删除
                cmsBtPromotionCodesTagDaoExt.deleteByTagIdPromotionCodesId(promotionCodesId, f.getId());
            } else if (f.getChecked() == 2) {
                if (get(promotionCodesId, f.getId()) == null) {
                    //不存在 新增
                    CmsBtPromotionCodesTagModel promotionCodesTagModel=new CmsBtPromotionCodesTagModel();
                    promotionCodesTagModel.setTagName(f.getName());
                    promotionCodesTagModel.setCmsBtTagId(f.getId());
                    promotionCodesTagModel.setChannelId(channelId);
                    promotionCodesTagModel.setCmsBtPromotionCodesId(promotionCodesId);
                    promotionCodesTagModel.setCreater(userName);
                    promotionCodesTagModel.setModifier(userName);
                    promotionCodesTagModel.setCreated(new Date());
                    promotionCodesTagModel.setModified(new Date());
                    cmsBtPromotionCodesTagDao.insert(promotionCodesTagModel);
                }
            }
        });
    }
    @VOTransactional
    public  void  updatePromotionProductTag(UpdatePromotionProductTagParameter parameter,String channelId,String userName) {
        List<TagTreeNode> tagList=  parameter.getTagList().stream().map(m -> {
            TagTreeNode tagTreeNode = new TagTreeNode();
            tagTreeNode.setId(m.getTagId());
            tagTreeNode.setName(m.getTagName());
            tagTreeNode.setChecked(m.getChecked());
            return tagTreeNode;
        }).collect(Collectors.toList());
        updatePromotionCodesTag(tagList,channelId,parameter.getId(),userName);
    }
    public CmsBtPromotionCodesTagModel get(int promotionCodesId, int tagId) {
        Map<String, Object> map = new HashedMap();
        map.put("cmsBtTagId", tagId);
        map.put("cmsBtPromotionCodesId", promotionCodesId);
        return cmsBtPromotionCodesTagDao.selectOne(map);
    }
    public  int batchDeleteByCodes(List<String> codeList,int promotionId)
    {
      return   cmsBtPromotionCodesTagDaoExt.batchDeleteByCodes(codeList, promotionId);
    }
}
