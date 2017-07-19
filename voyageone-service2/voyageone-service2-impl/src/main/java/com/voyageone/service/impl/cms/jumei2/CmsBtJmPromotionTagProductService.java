package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdatePromotionProductTagParameter;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionTagProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionTagProductDaoExt;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionTagProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/11/7.
 */
@Service
public class CmsBtJmPromotionTagProductService {

    @Autowired
    CmsBtJmPromotionProductDao cmsBtJmPromotionProductDao;
    @Autowired
    CmsBtJmPromotionDao cmsBtJmPromotionDao;
    @Autowired
    CmsBtJmPromotionTagProductDao dao;
    @Autowired
    CmsBtJmPromotionTagProductDaoExt daoExt;
    @Autowired
    ProductService productService;

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

    @VOTransactional
    public  void  updatePromotionProductTag(UpdatePromotionProductTagParameter parameter, String channelId, String userName) {

        List<TagTreeNode> tagList = parameter.getTagList().stream().map(m -> {
            TagTreeNode tagTreeNode = new TagTreeNode();
            tagTreeNode.setId(m.getTagId());
            tagTreeNode.setName(m.getTagName());
            tagTreeNode.setChecked(m.getChecked());
            return tagTreeNode;
        }).collect(Collectors.toList());

        updateJmPromotionTagProduct(tagList, channelId, parameter.getId(), userName);

        //更新mongo product tag
        CmsBtJmPromotionProductModel codesModel = cmsBtJmPromotionProductDao.select(parameter.getId());
        codesModel.setPromotionTag(getPromotionTag(tagList, codesModel.getPromotionTag()));
        cmsBtJmPromotionProductDao.update(codesModel);
        CmsBtJmPromotionModel promotionModel = cmsBtJmPromotionDao.select(codesModel.getCmsBtJmPromotionId());
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


        CmsBtJmPromotionProductModel codesModel = cmsBtJmPromotionProductDao.select(parameter.getId());
        String promotionTag = codesModel.getPromotionTag();
        List<String> tags = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(promotionTag)) {
            tags = new ArrayList<>(Arrays.asList(promotionTag.split("\\|")));
        }
        for (TagTreeNode tag : tagList) {

            // 逐一删除cms_bt_jm_promotion_tag_product数据
            daoExt.deleteByTagIdJmPromotionProductId(parameter.getId(), tag.getId());
            // 更新cms_bt_jm_promotion_product, promotion_tag以|拼接多个tag
            if (tags.contains(tag.getName())) {
                tags.remove(tag.getName());
                // 重新设置promotionTag
                StringBuffer sb = new StringBuffer();
                int tagCount = tags.size();
                if (tagCount > 0) {
                    for (int i = 0; i < tagCount; i++) {
                        sb.append(tags.get(i));
                        if (i != tagCount - 1) {
                            sb.append("|");
                        }
                    }

                }
                CmsBtJmPromotionProductModel updateModel = new CmsBtJmPromotionProductModel();
                updateModel.setId(codesModel.getId());
                updateModel.setPromotionTag(sb.toString());
                updateModel.setModified(new Date());
                updateModel.setModifier(userName);
                cmsBtJmPromotionProductDao.update(updateModel);

            }
        }
        // 删除对应的MongoDB 中cms_bt_product_cxx中的tags

        CmsBtJmPromotionModel promotionModel = cmsBtJmPromotionDao.select(codesModel.getCmsBtJmPromotionId());
        productService.deleteCmsBtProductTags(channelId, codesModel.getProductCode(), promotionModel.getRefTagId(), tagList, userName);
    }

    public  String getPromotionTag(List<TagTreeNode> tagList,String oldPromotionTag) {
        HashSet<String> hs = new HashSet<>();

        if (!StringUtils.isEmpty(oldPromotionTag)) {

            String[] oldTagList = oldPromotionTag.split("\\|");
            for (String o : oldTagList) {
                if (!StringUtils.isEmpty(o)) {
                    hs.add(o);
                }
            }
        }
        tagList.forEach(f->{
            if(f.getChecked()==2) {
                hs.add(f.getName());
            }
            else if(f.getChecked()==0)
            {
                hs.remove(f.getName());
            }
        });
        StringBuilder sb = new StringBuilder();
        hs.stream().forEach(f -> {
            sb.append("|").append(f);
        });
        if (sb.length() > 0) {
            return sb.substring(1);
        }
        return "";
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
