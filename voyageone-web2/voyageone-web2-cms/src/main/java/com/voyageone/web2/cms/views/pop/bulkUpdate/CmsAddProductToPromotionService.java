package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.cms.businessmodel.CmsAdvanceSearch.AddProductToPromotionParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAdvanceSearch.TagTreeNode;
import com.voyageone.service.daoext.cms.CmsBtPromotionDaoExtCamel;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@Service
public class CmsAddProductToPromotionService extends BaseViewService {

    @Autowired
    private TagService tagService;
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

    /**
     * 数据页面初始化(有产品信息)
     */
    public Map init(AddProductToPromotionParameter params, String channelId, CmsSessionBean cmsSession) {
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
        List<TagTreeNode> listTagTreeNode = new ArrayList<>();
        List<CmsBtPromotionModel> list = cmsBtPromotionDaoExtCamel.selectAddPromotionList(channelId, cartId);
        list.forEach(m -> listTagTreeNode.add(getPromotionTagTreeNode(m)) );

        data.put("listTreeNode", list);
        return data;
    }
    TagTreeNode getPromotionTagTreeNode(CmsBtPromotionModel model) {
        TagTreeNode tagTreeNode = new TagTreeNode();
        tagTreeNode.setId(model.getId());
        tagTreeNode.setName(model.getPromotionName());
        return tagTreeNode;
    }
}
