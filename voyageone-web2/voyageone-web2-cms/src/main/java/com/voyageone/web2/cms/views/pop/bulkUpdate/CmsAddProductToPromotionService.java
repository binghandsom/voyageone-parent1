package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.PromotionDetailAddBean;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.AddProductSaveParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.InitParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.daoext.cms.CmsBtPromotionDaoExtCamel;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotion3Service;
import com.voyageone.service.impl.cms.promotion.JMPromotionDetailService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@Service
public class CmsAddProductToPromotionService extends BaseViewService {

    @Autowired
    CmsBtPromotionDaoExtCamel cmsBtPromotionDaoExtCamel;
    @Autowired
    TagService tagService;
    @Autowired
    CmsBtJmPromotion3Service cmsBtJmPromotion3Service;
    @Autowired
    JMPromotionDetailService jmPromotionDetailService;
    @Autowired
    private CmsPromotionIndexService cmsPromotionService;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @Autowired
    private CmsAdvanceSearchService advanceSearchService;

    public void save(AddProductSaveParameter parameter, String channelId, String userName, CmsSessionBean cmsSession) {

        if (parameter.getCartId() == 0) {
            $warn("addToPromotion cartId为空 params=" + JacksonUtil.bean2Json(parameter));
            throw new BusinessException("未选择平台");
        }
        parameter.getListTagTreeNode().forEach(f -> save(f, parameter, userName, cmsSession));

    }

    void save(TagTreeNode tagTreeNode, AddProductSaveParameter parameter, String userName, CmsSessionBean cmsSession) {
        if (tagTreeNode.getChecked() == 2) {
            //状态变化的tag
            List<TagTreeNode> tagList = tagTreeNode.getChildren().stream().filter(p -> p.getChecked() != p.getOldChecked()).collect(Collectors.toList());
            if (tagList.size() > 0) {
                //商品加入活动        tag  checked: 0:删除 商品tag    2 加入商品tag
                if (parameter.getCartId() == 27) {
                    //聚美
                    addToJmPromotion(tagTreeNode.getId(), tagTreeNode.getChildren(), parameter, userName, cmsSession);
                } else {
                    addToPromotion(tagTreeNode.getId(), tagTreeNode.getChildren(), parameter, userName, cmsSession);
                }
            }
        } else if (tagTreeNode.getChecked() != tagTreeNode.getOldChecked()) {
            if (tagTreeNode.getChecked() == 0) {
                // 活动 商品从活动中删除      删除商品tag
                if (parameter.getCartId() == 27) {
                    //聚美
                    deleteFromJmPromotion(tagTreeNode.getId(), parameter);
                } else {
                    deleteFromPromotion(tagTreeNode.getId(), parameter);
                }
            }
        }
    }

    void addToPromotion(int promotionId, List<TagTreeNode> tagList, AddProductSaveParameter parameter, String modifier, CmsSessionBean cmsSession) {
        // 获取promotion信息
        CmsBtPromotionModel promotion = cmsPromotionService.queryById(promotionId);
        if (promotion == null) {
            $warn("addToPromotion promotionId不存在 promotionId=" + promotionId);
            throw new BusinessException("promotionId不存在：" + promotionId);
        }
        List<Long> productIds;
        if (parameter.getIsSelAll() == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productIds = advanceSearchService.getProductIdList(promotion.getChannelId(), cmsSession);
        } else {
            productIds = parameter.getIdList();
        }
        if (productIds == null || productIds.isEmpty()) {
            $warn("addToPromotion 未选择商品 params=" + JacksonUtil.bean2Json(parameter));
            throw new BusinessException("未选择商品");
        }
        productIds.forEach(item -> {
            PromotionDetailAddBean request = new PromotionDetailAddBean();
            request.setModifier(modifier);
            request.setChannelId(promotion.getChannelId());
            request.setOrgChannelId(promotion.getChannelId());
            request.setCartId(parameter.getCartId());
            request.setProductId(item);
            request.setRefTagId(promotion.getRefTagId());
            request.setPromotionId(promotionId);
            request.setTagList(tagList);
            request.setAddProductSaveParameter(parameter);
            if (promotionDetailService.check_addPromotionDetail(request)) {
                promotionDetailService.addPromotionDetail(request, true);
            }
        });
    }

    @Autowired
    PromotionService promotionService;

    void addToJmPromotion(int promotionId, List<TagTreeNode> tagList, AddProductSaveParameter parameter, String modifier, CmsSessionBean cmsSession) {
        // 获取promotion信息
        CmsBtJmPromotionModel promotion = cmsBtJmPromotion3Service.get(promotionId);
        if (promotion == null) {
            $warn("addToPromotion promotionId不存在 promotionId=" + promotionId);
            throw new BusinessException("promotionId不存在：" + promotionId);
        }
        List<Long> productIds;
        if (parameter.getIsSelAll() == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productIds = advanceSearchService.getProductIdList(promotion.getChannelId(), cmsSession);
        } else {
            productIds = parameter.getIdList();
        }
        if (productIds == null || productIds.isEmpty()) {
            $warn("addToPromotion 未选择商品 params=" + JacksonUtil.bean2Json(parameter));
            throw new BusinessException("未选择商品");
        }
        productIds.forEach(item -> {
            PromotionDetailAddBean request = new PromotionDetailAddBean();
            request.setModifier(modifier);
            request.setChannelId(promotion.getChannelId());
            request.setOrgChannelId(promotion.getChannelId());
            request.setCartId(parameter.getCartId());
            request.setProductId(item);
            request.setPromotionId(promotionId);
            request.setTagList(tagList);
            request.setAddProductSaveParameter(parameter);
            if (promotionDetailService.check_addPromotionDetail(request)) {
                jmPromotionDetailService.addPromotionDetail(request, promotion, modifier);

                CmsBtPromotionModel cmsBtPromotionModel = promotionService.getCmsBtPromotionModelByJmPromotionId(promotionId);
                request.setPromotionId(cmsBtPromotionModel.getId());
                promotionDetailService.addPromotionDetail(request, true);
            }
        });
    }

    void deleteFromPromotion(int promotionId, AddProductSaveParameter parameter) {
        CmsBtPromotionModel promotion = cmsPromotionService.queryById(promotionId);
        Date activityStart = DateTimeUtil.parse(promotion.getActivityStart(), "yyyy-MM-dd");
        //活动开始后不允许删除

        if (DateTimeUtilBeijing.toLocalTime(activityStart) > new Date().getTime()) {
            //活动未开始
            promotionDetailService.deleteFromPromotion(promotion, parameter);
        }
    }

    void deleteFromJmPromotion(int promotionId, AddProductSaveParameter parameter) {
        CmsBtJmPromotionModel promotion = cmsBtJmPromotion3Service.get(promotionId);
        //活动内已经再售的商品 不允许删除 聚美平台已完成活动上传的商品将不做删除操作,
        jmPromotionDetailService.deleteFromPromotion(promotion, parameter);
    }

    /**
     * 页面初始化
     */
    public Map init(InitParameter params, String channelId, CmsSessionBean cmsSession) {
        int cartId = params.getCartId();
        if (cartId == 0) {
            $warn("CmsAddProductToPromotionService.init cartI==0 " + params.toString());
            throw new BusinessException("未选择平台");
        }
        int isSelAll = params.getIsSelAll();
        //产品code
        List<String> codeList;
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            codeList = advanceSearchService.getProductCodeList(channelId, cmsSession);
        } else {
            codeList = params.getCodeList();
        }
        if (codeList == null || codeList.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            throw new BusinessException("未选择商品");
        }
        if (params.getCartId() == 27) {
            //聚美
            return jmPromotionDetailService.init(params, channelId, codeList);
        }
        //聚美 京东
        return promotionDetailService.init(params, channelId, codeList);
    }
    //获取活动的节点数据

}
