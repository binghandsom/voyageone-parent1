package com.voyageone.web2.cms.views.shelves;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.fields.cms.CmsBtShelvesModelActive;
import com.voyageone.service.fields.cms.CmsBtShelvesProductHistoryModelStatus;
import com.voyageone.service.fields.cms.CmsBtTagModelTagType;
import com.voyageone.service.impl.cms.CmsBtShelvesProductHistoryService;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsShelvesImageUploadMQMessageBody;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ProductTagService productTagService;
    private final CmsBtShelvesProductHistoryService cmsBtShelvesProductHistoryService;
    private final TagService tagService;

    private final CmsMqSenderService cmsMqSenderService;

    @Autowired
    public CmsShelvesDetailController(CmsShelvesDetailService cmsShelvesDetailService,
                                      CmsBtShelvesService cmsBtShelvesService,
                                      CmsAdvanceSearchService advanceSearchService,
                                      CmsBtShelvesProductService cmsBtShelvesProductService,
                                      TagService tagService, CmsMqSenderService cmsMqSenderService, ProductTagService productTagService, CmsBtShelvesProductHistoryService cmsBtShelvesProductHistoryService) {
        this.cmsShelvesDetailService = cmsShelvesDetailService;
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.advanceSearchService = advanceSearchService;
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.tagService = tagService;
        this.cmsMqSenderService = cmsMqSenderService;
        this.productTagService = productTagService;
        this.cmsBtShelvesProductHistoryService = cmsBtShelvesProductHistoryService;
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
            productCodes = advanceSearchService.getProductCodeList(getUser().getSelChannelId(), params.searchInfo);
        }
        if (productCodes == null || productCodes.isEmpty()) {
            throw new BusinessException("批量修改商品属性 没有code条件 params=" + params.toString());
        }
        cmsShelvesDetailService.addProducts(shelvesId, productCodes, getUser().getUserName());
        cmsBtShelvesProductHistoryService.batchInsert(shelvesId, productCodes, CmsBtShelvesProductHistoryModelStatus.ON_LINE, getUser().getUserName());
        return success(true);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.GET_SHELVES_INFO)
    public AjaxResponse getShelvesInfo(@RequestBody GetShelvesInfo params) {
        List<Integer> shelvesIds = params.shelvesIds;
        Boolean isLoadPromotionPrice = params.isLoadPromotionPrice;
        if (isLoadPromotionPrice == null)
            isLoadPromotionPrice = false;
        return success(cmsShelvesDetailService.getShelvesInfo(shelvesIds, isLoadPromotionPrice, getUser().getUserName()));
    }

    @RequestMapping(value = CmsUrlConstants.SHELVES.DETAIL.EXPORT_APP_IMAGE, method = RequestMethod.GET)
    public HttpEntity<byte[]> doExportAppImage(@RequestParam Integer shelvesId) throws Exception {

        byte[] data = cmsShelvesDetailService.exportAppImage(shelvesId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(data.length);

        return new HttpEntity<>(data, headers);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.CREATE_SHELVES)
    public AjaxResponse createShelves(@RequestBody CmsBtShelvesModel cmsBtShelvesModel) {
        cmsBtShelvesModel.setChannelId(getUser().getSelChannelId());
        cmsBtShelvesModel.setCreater(getUser().getUserName());
        cmsBtShelvesModel.setModifier(getUser().getUserName());
        cmsBtShelvesService.insert(cmsBtShelvesModel);

        // 新建tag表
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setChannelId(getUser().getSelChannelId());
        cmsBtTagModel.setTagName(cmsBtShelvesModel.getShelvesName());
        cmsBtTagModel.setTagPath("0");
        cmsBtTagModel.setTagPathName("-" + cmsBtShelvesModel.getShelvesName() + "-");
        cmsBtTagModel.setParentTagId(0);
        cmsBtTagModel.setTagType(CmsBtTagModelTagType.shelves);
        cmsBtTagModel.setCreater(getUser().getUserName());
        cmsBtTagModel.setModifier(getUser().getUserName());
        cmsBtTagModel.setActive(1);
        tagService.insertCmsBtTagAndUpdateTagPath(cmsBtTagModel, true);

        cmsBtShelvesModel.setRefTagId(cmsBtTagModel.getId());
        cmsBtShelvesService.update(cmsBtShelvesModel);
        return success(cmsBtShelvesModel);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.UPDATE_SHELVES)
    public AjaxResponse updateShelves(@RequestBody CmsBtShelvesModel cmsBtShelvesModel) {
        cmsBtShelvesModel.setModifier(getUser().getUserName());
        cmsBtShelvesModel.setModified(new Date());
        cmsBtShelvesModel.setChannelId(getUser().getSelChannelId());

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

        CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(cmsBtShelvesProductModel.getShelvesId());
        if (cmsBtShelvesModel != null && cmsBtShelvesModel.getRefTagId() != null && cmsBtShelvesModel.getRefTagId() > 0) {
            productTagService.deleteByCodes(getUser().getSelChannelId(), "-" + cmsBtShelvesModel.getRefTagId() + "-", Collections.singletonList(cmsBtShelvesProductModel.getProductCode()), "tags");
            cmsBtShelvesProductHistoryService.batchInsert(cmsBtShelvesModel.getId(), Collections.singletonList(cmsBtShelvesProductModel.getProductCode()), CmsBtShelvesProductHistoryModelStatus.OFF_LINE, getUser().getUserName());
        }
        return success(true);
    }

    @RequestMapping("clearProduct")
    public AjaxResponse clearProduct(@RequestBody CmsBtShelvesProductModel cmsBtShelvesProductModel) {
        // 删除产品数据了tagId
        List<CmsBtShelvesProductModel> products = cmsBtShelvesProductService.getByShelvesId(cmsBtShelvesProductModel.getShelvesId());
        List<String> productCodes = products.stream().map(CmsBtShelvesProductModel::getProductCode).collect(Collectors.toList());
        CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(cmsBtShelvesProductModel.getShelvesId());
        if (cmsBtShelvesModel != null && cmsBtShelvesModel.getRefTagId() != null && cmsBtShelvesModel.getRefTagId() > 0) {
            productTagService.deleteByCodes(getUser().getSelChannelId(), "-" + cmsBtShelvesModel.getRefTagId() + "-", productCodes, "tags");
            cmsBtShelvesProductHistoryService.batchInsert(cmsBtShelvesModel.getId(), productCodes, CmsBtShelvesProductHistoryModelStatus.OFF_LINE, getUser().getUserName());
        }

        cmsBtShelvesProductService.deleteByShelvesId(cmsBtShelvesProductModel.getShelvesId());
        return success(true);
    }

    @RequestMapping("deleteShelves")
    public AjaxResponse deleteShelves(@RequestBody CmsBtShelvesModel cmsBtShelvesModel) {
        cmsBtShelvesModel.setModifier(getUser().getUserName());
        cmsBtShelvesService.delete(cmsBtShelvesModel);
        return success(true);
    }

    @RequestMapping("releaseImage")
    public AjaxResponse releaseImage(@RequestBody Integer shelvesId) {
        CmsShelvesImageUploadMQMessageBody param = new CmsShelvesImageUploadMQMessageBody();
        param.setShelvesId(shelvesId);
        param.setSender(getUser().getUserName());
        try {
            cmsMqSenderService.sendMessage(param);
            return success(true);
        } catch (BusinessException e) {
            $error(e);
            e.printStackTrace();
            return success(false);
        }
    }

    /**
     * 获取货架HTML代码
     *
     * @param params shelvesId：货架ID
     *               preview: 1 或者 0， 1：单品模板图片URL根据单品模板拼接的html代码获取，0单品图片URL直接拿货架商品的第三方平台图片地址
     */
    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.GET_SHELVES_HTML)
    public AjaxResponse getShelvesHtml(@RequestBody getShelvesHtml params) {
        Boolean preview = params.preview == null ? false : params.preview;
        String html = cmsBtShelvesService.generateHtml(params.shelvesId, preview);
        return success(html);
    }

    private static class getShelvesHtml {
        @JsonProperty("shelvesId")
        Integer shelvesId;
        @JsonProperty("preview")
        Boolean preview;
    }

    private static class AddProduct {
        @JsonProperty("shelvesId")
        Integer shelvesId;
        @JsonProperty("productCodes")
        List<String> productCodes;
        @JsonProperty("isSelAll")
        Integer isSelAll;
        @JsonProperty("searchInfo")
        CmsSearchInfoBean2 searchInfo;


    }

    private static class GetShelvesInfo {
        @JsonProperty("shelvesIds")
        List<Integer> shelvesIds;
        @JsonProperty("isLoadPromotionPrice")
        Boolean isLoadPromotionPrice;
    }


}
