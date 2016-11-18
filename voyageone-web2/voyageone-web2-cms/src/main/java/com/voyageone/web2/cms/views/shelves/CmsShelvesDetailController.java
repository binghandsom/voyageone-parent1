package com.voyageone.web2.cms.views.shelves;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.fields.cms.CmsBtShelvesModelActive;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by james on 2016/11/15.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.SHELVES.DETAIL.ROOT)
public class CmsShelvesDetailController extends CmsController {
    private final CmsBtShelvesService cmsBtShelvesService;
    private final CmsShelvesDetailService cmsShelvesDetailService;
    private final CmsAdvanceSearchService advanceSearchService;
    private final CmsBtShelvesProductService cmsBtShelvesProductService;

    @Autowired
    public CmsShelvesDetailController(CmsShelvesDetailService cmsShelvesDetailService, CmsBtShelvesService cmsBtShelvesService, CmsAdvanceSearchService advanceSearchService, CmsBtShelvesProductService cmsBtShelvesProductService) {
        this.cmsShelvesDetailService = cmsShelvesDetailService;
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.advanceSearchService = advanceSearchService;
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.SEARCH)
    public AjaxResponse search(@RequestBody CmsBtShelvesModel example) {
        example.setChannelId(getUser().getSelChannelId());
        example.setActive(CmsBtShelvesModelActive.ACTIVATE);

        return success(cmsBtShelvesService.selectList(example));
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.ADD_PRODUCT)
    public AjaxResponse addProduct(@RequestBody AddProduct params) {
        Integer shelvesId = params.shelvesId;
        List<String> productCodes = params.productCodes;
        Integer isSelAll = params.isSelAll;
        if (isSelAll == null) {
            isSelAll = 0;
        }
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(getUser().getSelChannelId(), getCmsSession());
        }
        if (productCodes == null || productCodes.isEmpty()) {
            throw new BusinessException("批量修改商品属性 没有code条件 params=" + params.toString());
        }
        cmsShelvesDetailService.addProducts(shelvesId, productCodes, getUser().getUserName());
        return success(true);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.GET_SHELVES_INFO)
    public AjaxResponse getShelvesInfo(@RequestBody GetShelvesInfo params) {
        List<Integer> shelvesIds = params.shelvesIds;
        Boolean isLoadPromotionPrice = params.isLoadPromotionPrice;
        if (isLoadPromotionPrice == null)
            isLoadPromotionPrice = false;
        return success(cmsShelvesDetailService.getShelvesInfo(shelvesIds, isLoadPromotionPrice));
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.CREATE_SHELVES)
    public AjaxResponse createShelves(@RequestBody CmsBtShelvesModel cmsBtShelvesModel) {
        cmsBtShelvesModel.setChannelId(getUser().getSelChannelId());
        cmsBtShelvesModel.setCreater(getUser().getUserName());
        cmsBtShelvesModel.setModifier(getUser().getUserName());

        cmsBtShelvesService.insert(cmsBtShelvesModel);
        return success(cmsBtShelvesModel);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.UPDATE_SHELVES)
    public AjaxResponse updateShelves(@RequestBody CmsBtShelvesModel cmsBtShelvesModel) {
        cmsBtShelvesModel.setModifier(getUser().getUserName());
        cmsBtShelvesModel.setModified(new Date());

        if (!cmsBtShelvesService.checkName(cmsBtShelvesModel)) {
            throw new BusinessException("该货架名称已存在");
        }

        cmsBtShelvesService.update(cmsBtShelvesModel);
        return success(cmsBtShelvesModel);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.UPDATE_PRODUCT_SORT)
    public AjaxResponse updateProductSort(@RequestBody List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {
        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductModel -> cmsBtShelvesProductModel.setModifier(getUser().getUserName()));
        cmsBtShelvesProductService.updateSort(cmsBtShelvesProductModels);
        return success(true);
    }

    @RequestMapping("removeProduct")
    public AjaxResponse removeProduct(@RequestBody CmsBtShelvesProductModel cmsBtShelvesProductModel) {
        cmsBtShelvesProductService.delete(cmsBtShelvesProductModel);
        return success(true);
    }

    @RequestMapping("clearProduct")
    public AjaxResponse clearProduct(@RequestBody CmsBtShelvesProductModel cmsBtShelvesProductModel) {
        cmsBtShelvesProductService.deleteByShelvesId(cmsBtShelvesProductModel.getShelvesId());
        return success(true);
    }

    @RequestMapping("deleteShelves")
    public AjaxResponse deleteShelves(@RequestBody CmsBtShelvesModel cmsBtShelvesModel) {
        cmsBtShelvesModel.setModifier(getUser().getUserName());
        cmsBtShelvesService.delete(cmsBtShelvesModel);
        return success(true);
    }

    private static class AddProduct {
        @JsonProperty("shelvesId") Integer shelvesId;
        @JsonProperty("productCodes") List<String> productCodes;
        @JsonProperty("isSelAll") Integer isSelAll;
    }

    private static class GetShelvesInfo {
        @JsonProperty("shelvesIds") List<Integer> shelvesIds;
        @JsonProperty("isLoadPromotionPrice") Boolean isLoadPromotionPrice;
    }
}
