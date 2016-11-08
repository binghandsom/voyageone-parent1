package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.PromotionDetailAddBean;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.InitParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.AddProductSaveParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.bean.cms.businessmodel.CmsBtTag.TagCodeCountInfo;
import com.voyageone.service.daoext.cms.CmsBtPromotionDaoExtCamel;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.promotion.JMPromotionDetailService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@Service
public class CmsAddProductToPromotionService extends BaseViewService {

    @Autowired
    private CmsPromotionIndexService cmsPromotionService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductTagService productTagService;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @Autowired
    private CmsAdvanceSearchService advanceSearchService;
    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private CmsBtBrandBlockService brandBlockService;
    @Autowired
    CmsBtPromotionDaoExtCamel cmsBtPromotionDaoExtCamel;
    @Autowired
    TagService tagService;

    public  void  save(AddProductSaveParameter parameter, String channelId, String userName, CmsSessionBean cmsSession) {

        if (parameter.getCartId() == 0) {
            $warn("addToPromotion cartId为空 params=" + JacksonUtil.bean2Json(parameter));
            throw new BusinessException("未选择平台");
        }
        parameter.getListTagTreeNode().forEach(f->save(f,parameter,userName,cmsSession));

    }
     void  save(TagTreeNode tagTreeNode, AddProductSaveParameter parameter, String userName, CmsSessionBean cmsSession) {
        if (tagTreeNode.getChecked() == 2) {
            //状态变化的tag
            List<TagTreeNode> tagList = tagTreeNode.getChildren().stream().filter(p -> p.getChecked() != p.getOldChecked()).collect(Collectors.toList());
            if (tagList.size() > 0) {
                //商品加入活动        tag  checked: 0:删除 商品tag    2 加入商品tag
                addToPromotion(tagTreeNode.getId(),tagList,parameter,userName,cmsSession);
            }
        } else if (tagTreeNode.getChecked() != tagTreeNode.getOldChecked()) {
            if (tagTreeNode.getChecked() == 0) {
                // 活动 商品从活动中删除      删除商品tag
                deleteFromPromotion(tagTreeNode.getId(),parameter);
            }
        }
    }

    void  addToPromotion(int promotionId, List<TagTreeNode> tagList, AddProductSaveParameter parameter, String modifier, CmsSessionBean cmsSession) {
        // 获取promotion信息
        CmsBtPromotionModel promotion = cmsPromotionService.queryById(promotionId);
        if (promotion == null) {
            $warn("addToPromotion promotionId不存在 promotionId=" + promotionId);
            throw new BusinessException("promotionId不存在：" + promotionId);
        }
        List<Long> productIds = null;
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

        // 检查品牌黑名单
        Iterator<Long> iter = productIds.iterator();
        while (iter.hasNext()) {
            Long prodId = iter.next();
            CmsBtProductModel prodObj = productService.getProductById(promotion.getChannelId(), prodId);
            if (prodObj == null) {
                $warn("addToPromotion CmsBtProductModel不存在 channelId=%s, prodId=%d", promotion.getChannelId(), prodId);
                iter.remove();
                continue;
            }
            String prodCode = StringUtils.trimToNull(prodObj.getCommonNotNull().getFieldsNotNull().getCode());
            if (prodCode == null) {
                $warn("addToPromotion CmsBtProductModel不存在(没有code) channelId=%s, prodId=%d", promotion.getChannelId(), prodId);
                iter.remove();
                continue;
            }
            // 取得feed 品牌
            CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode(promotion.getChannelId(), prodCode);
            String feedBrand = null;
            if (cmsBtFeedInfoModel == null) {
                $warn("addToPromotion CmsBtFeedInfoModel channelId=%s, code=%s", promotion.getChannelId(), prodCode);
            } else {
                feedBrand = cmsBtFeedInfoModel.getBrand();
            }
            String masterBrand = prodObj.getCommonNotNull().getFieldsNotNull().getBrand();
            String platBrand = prodObj.getPlatformNotNull(parameter.getCartId()).getpBrandId();
            if (brandBlockService.isBlocked(promotion.getChannelId(), parameter.getCartId(), feedBrand, masterBrand, platBrand)) {
                $warn("addToPromotion 该品牌为黑名单 channelId=%s, cartId=%d, code=%s, feed brand=%s, master brand=%s, platform brand=%s", promotion.getChannelId(), parameter.getCartId(), prodCode, feedBrand, masterBrand, platBrand);
                iter.remove();
                continue;
            }
        }
        if (productIds.isEmpty()) {
            $info("addToPromotion：没有商品需要加入活动");
            return;
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
            promotionDetailService.addPromotionDetail(request, true);
        });
    }

    void  deleteFromPromotion(  int promotionId,AddProductSaveParameter parameter) {
        CmsBtPromotionModel promotion = cmsPromotionService.queryById(promotionId);
        Date activityStart = DateTimeUtil.parse(promotion.getActivityStart(),"yyyy-MM-dd");
        if (DateTimeUtilBeijing.toLocalTime(activityStart) > new Date().getTime()) {
            //活动已开始
            promotionDetailService.deleteFromPromotion(promotion, parameter);
        }
    }

    @Autowired
    JMPromotionDetailService jmPromotionDetailService;
    /**
     * 页面初始化
     */
    public Map init(InitParameter params, String channelId, CmsSessionBean cmsSession) {
        int cartId = params.getCartId();
        if (cartId == 0) {
            $warn("CmsAddProductToPromotionService.init cartI==0 " + params.toString());
            throw new BusinessException("未选择平台");
        }
        Map<String, Object> data = new HashMap<>();
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
        if(params.getCartId()==27)
        {
            return jmPromotionDetailService.init(params,channelId,codeList);
        }
        return promotionDetailService.init(params, channelId, codeList);
    }
    //获取活动的节点数据

}
