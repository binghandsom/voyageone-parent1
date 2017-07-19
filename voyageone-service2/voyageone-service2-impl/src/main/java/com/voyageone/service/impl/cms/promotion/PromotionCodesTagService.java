package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdateListPromotionProductTagParameter;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdatePromotionProductTagParameter;
import com.voyageone.service.dao.cms.CmsBtPromotionCodesDao;
import com.voyageone.service.dao.cms.CmsBtPromotionCodesTagDao;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.daoext.cms.CmsBtPromotionCodesTagDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtPromotionCodesModel;
import com.voyageone.service.model.cms.CmsBtPromotionCodesTagModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
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
    CmsBtPromotionCodesDao cmsBtPromotionCodesDao;

    @Autowired
    CmsBtPromotionDao cmsBtPromotionDao;

    @Autowired
    CmsBtPromotionCodesTagDao cmsBtPromotionCodesTagDao;

    @Autowired
    ProductService productService;

    @VOTransactional
    public void updatePromotionCodesTag(List<TagTreeNode> tagList, String channelId, int promotionCodesId, String userName) {
        if (tagList == null) return;
        tagList.forEach(f -> {
            if (f.getChecked() == 0) {
                //删除
                cmsBtPromotionCodesTagDaoExt.deleteByTagIdPromotionCodesId(promotionCodesId, f.getId());
            } else if (f.getChecked() == 2) {
                if (get(promotionCodesId, f.getId()) == null) {
                    //不存在 新增
                    CmsBtPromotionCodesTagModel promotionCodesTagModel = new CmsBtPromotionCodesTagModel();
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
    public void updatePromotionProductTag(UpdatePromotionProductTagParameter parameter, String channelId, String userName) {
        List<TagTreeNode> tagList = parameter.getTagList().stream().map(m -> {
            TagTreeNode tagTreeNode = new TagTreeNode();
            tagTreeNode.setId(m.getTagId());
            tagTreeNode.setName(m.getTagName());
            tagTreeNode.setChecked(m.getChecked());
            return tagTreeNode;
        }).collect(Collectors.toList());

        updatePromotionCodesTag(tagList, channelId, parameter.getId(), userName);

        //更新mongo product tag
        CmsBtPromotionCodesModel codesModel = cmsBtPromotionCodesDao.select(parameter.getId());
        CmsBtPromotionModel promotionModel = cmsBtPromotionDao.select(codesModel.getPromotionId());
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, codesModel.getProductCode());
        productService.updateCmsBtProductTags(channelId, cmsBtProductModel, promotionModel.getRefTagId(), tagList, userName);
    }

    @VOTransactional
    public void deletePromotionProductTag(UpdatePromotionProductTagParameter parameter, String channelId, String userName) {
        List<TagTreeNode> tagList = parameter.getTagList().stream().map(m -> {
            TagTreeNode tagTreeNode = new TagTreeNode();
            tagTreeNode.setId(m.getTagId());
            tagTreeNode.setName(m.getTagName());
            tagTreeNode.setChecked(m.getChecked());
            return tagTreeNode;
        }).collect(Collectors.toList());

        for (TagTreeNode tag : tagList) {
            // 逐一删除cms_bt_promotion_codes_tag数据
            cmsBtPromotionCodesTagDaoExt.deleteByTagIdPromotionCodesId(parameter.getId(), tag.getId());
        }
        // 删除对应的MongoDB 中cms_bt_product_cxx中的tags
        CmsBtPromotionCodesModel codesModel = cmsBtPromotionCodesDao.select(parameter.getId());
        CmsBtPromotionModel promotionModel = cmsBtPromotionDao.select(codesModel.getPromotionId());
        productService.deleteCmsBtProductTags(channelId, codesModel.getProductCode(), promotionModel.getRefTagId(), tagList, userName);
    }

    @VOTransactional
    public void updatePromotionListProductTag(UpdateListPromotionProductTagParameter parameter, String channelId, String userName) {

        if (parameter.getListPromotionProductId() == null || parameter.getListPromotionProductId().size() == 0) {
            return;
        }
        UpdatePromotionProductTagParameter parameterProductTag = new UpdatePromotionProductTagParameter();
        parameterProductTag.setTagList(parameter.getTagList());

        parameter.getListPromotionProductId().forEach(id -> {
            parameterProductTag.setId(id);
            if (parameter.getActionType() == 0) {
                updatePromotionProductTag(parameterProductTag, channelId, userName);
            } else {
                deletePromotionProductTag(parameterProductTag, channelId, userName);
            }
        });
    }


    public CmsBtPromotionCodesTagModel get(int promotionCodesId, int tagId) {
        Map<String, Object> map = new HashedMap();
        map.put("cmsBtTagId", tagId);
        map.put("cmsBtPromotionCodesId", promotionCodesId);
        return cmsBtPromotionCodesTagDao.selectOne(map);
    }

    public int batchDeleteByCodes(List<String> codeList, int promotionId) {
        return cmsBtPromotionCodesTagDaoExt.batchDeleteByCodes(codeList, promotionId);
    }

    //批量删除活动商品的tag
    @VOTransactional
    public int deleteListByPromotionId_Codes(String channelId, int promotionId, List<String> codes, int refTagId) {
        int result = cmsBtPromotionCodesTagDaoExt.batchDeleteByCodes(codes, promotionId);
        productService.removeTagByCodes(channelId, codes, refTagId);
        return result;
    }

    @VOTransactional
    public  void   addTag(String channelId,int promotionCodesId, CmsBtTagModel tag,String userName)
    {
//        if (get(promotionCodesId,tag.getId()) == null) {
//            //不存在 新增
//            CmsBtPromotionCodesTagModel promotionCodesTagModel = new CmsBtPromotionCodesTagModel();
//            promotionCodesTagModel.setTagName(tag.getTagName());
//            promotionCodesTagModel.setCmsBtTagId(tag.getId());
//            promotionCodesTagModel.setChannelId(channelId);
//            promotionCodesTagModel.setCmsBtPromotionCodesId(promotionCodesId);
//            promotionCodesTagModel.setCreater(userName);
//            promotionCodesTagModel.setModifier(userName);
//            promotionCodesTagModel.setCreated(new Date());
//            promotionCodesTagModel.setModified(new Date());
//            cmsBtPromotionCodesTagDao.insert(promotionCodesTagModel);
//        }
//        CmsBtTagModel tag = searchTag(tags, code.getTag());
//        if (tag != null) {
//            //cmsBtPromotionCodeModel1.setTagId(tag.getId());
//            // //// votodo: 2016/11/10  待实现
//            promotionCodesTagService.addTag(code.getChannelId(), codesId, tag, code.getModifier());
//        }
    }

}
