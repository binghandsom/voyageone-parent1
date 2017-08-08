package com.voyageone.task2.cms.mqjob.usa;

import com.google.common.collect.Lists;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductTopDao;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.search.product.CmsProductSearchQueryService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsCategoryReceiveMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsCategoryTransferToUsMQMessageBody;
import com.voyageone.service.model.cms.TransferUsCategoryModel;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductTopModel;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Charis on 2017/7/25.
 */
@Service
@RabbitListener()
public class CmsBuildPlatformCategoryTransferUsMqJob extends TBaseMQCmsService<CmsCategoryReceiveMQMessageBody> {

    private static final String symbol = "-";


    @Autowired
    private CmsProductSearchQueryService cmsProductSearchQueryService;
    @Autowired
    private SellerCatService sellerCatService;

    @Autowired
    private CmsBtProductTopDao cmsBtProductTopDao;

    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    @Override
    public void onStartup(CmsCategoryReceiveMQMessageBody messageBody) throws Exception {

        String channelId = messageBody.getChannelId();
        String cartId = messageBody.getCartId();
        List<String> fullCatIds = messageBody.getFullCatIds(); // "-"分割

        List<CmsBtSellerCatModel> sellerCatModels = sellerCatService.getSellerCatsByChannelCart(channelId, Integer.parseInt(cartId), true);

        if (ListUtils.isNull(fullCatIds)) {
            // 空的话就是全类目
            fullCatIds = new ArrayList<>();
            for (CmsBtSellerCatModel root : sellerCatModels) {
                findChildrenCatIds(root, fullCatIds);
            }
        }

        for (String fullCatId : fullCatIds) {
            TransferUsCategoryModel categoryModel = new TransferUsCategoryModel();

            String catId = fullCatId;

            // 如果该类目是1级类目,自动批接上父类catId:1(Root Catalog), 2(Default Category)
            String[] fullCatIdArrs = ("1-2-" + fullCatId).split(symbol);
            String parentId = "0";
            if (fullCatIdArrs.length > 0) {
                catId = fullCatIdArrs[fullCatIdArrs.length - 1];
                parentId = fullCatIdArrs[fullCatIdArrs.length - 2];
            }
            CmsBtSellerCatModel sellerCatModel = sellerCatService.getCurrentNode(sellerCatModels, catId);
            // 填充categoryModel
            categoryModel.setChannelId(channelId);
            categoryModel.setStoreId(cartId);
            categoryModel.setChannelId(catId);
            categoryModel.setParentId("0".equals(parentId) ? null : parentId);
            categoryModel.setCategoryPath(fullCatId);
            if (sellerCatModel != null) {
                categoryModel.setName(sellerCatModel.getCatName());
                categoryModel.setUrlKey(sellerCatModel.getUrlKey());
                categoryModel.setHeaderTitle(sellerCatModel.getCatName());
                if (sellerCatModel.getMapping() != null) {
                    categoryModel.setPublished((Boolean) sellerCatModel.getMapping().get("isPublished"));
                    categoryModel.setSeoTitle(String.valueOf(sellerCatModel.getMapping().get("seoTitle")));
                    categoryModel.setSeoKeywords(String.valueOf(sellerCatModel.getMapping().get("seoKeywords")));
                    categoryModel.setSeoCanonical(String.valueOf(sellerCatModel.getMapping().get("seoCanonical")));
                    categoryModel.setSeoDescription(String.valueOf(sellerCatModel.getMapping().get("seoDescription")));
                    categoryModel.setVisibleOnMenu((Boolean) sellerCatModel.getMapping().get("isVisibleOnMenu"));
                    categoryModel.setEnableFilter((Boolean) sellerCatModel.getMapping().get("isEnableFilter"));
                }
            }
            // 获取类目下产品code列表
            List<String> codes = new ArrayList<>();
            CmsBtProductTopModel topModel = cmsBtProductTopDao.selectBySellerCatId(catId, channelId, Integer.parseInt(messageBody.getCartId()));
            if (topModel == null) {
                codes.addAll(selectListCodeBySellerCatWithSolr(channelId, Integer.parseInt(cartId), catId, null, null, Collections.emptyList())); // 普通code排序
            } else {
                if (ListUtils.notNull(topModel.getProductCodeList())) {
                    codes.addAll(topModel.getProductCodeList()); // 置顶列表
                }
                codes.addAll(selectListCodeBySellerCatWithSolr(channelId, Integer.parseInt(cartId), catId, topModel.getSortColumnName(), topModel.getSortType(), topModel.getProductCodeList())); // 普通code排序

            }

            categoryModel.setProductCodes(codes);
            // 获取当前类目的排序值
            int catIndex = sellerCatService.indexOfCurrentCat(sellerCatModels, parentId, catId);
            categoryModel.setDisplayOrder(catIndex);

            // mq推送CategoryModel
            if (categoryModel != null) {
                CmsCategoryTransferToUsMQMessageBody body = new CmsCategoryTransferToUsMQMessageBody();
                body.setChannelId(channelId);
                body.setCategoryModel(categoryModel);
                body.setSender(getTaskName());
                cmsMqSenderService.sendMessage(body);
            }
        }
    }

    /**
     * 找出所有子节点
     */
    private void findChildrenCatIds(CmsBtSellerCatModel root, List<String> fullCatIds) {
        for (CmsBtSellerCatModel model : root.getChildren()) {
            fullCatIds.add(model.getFullCatId());
            findChildrenCatIds(model, fullCatIds);
        }
    }

    private List<String> selectListCodeBySellerCatWithSolr(String channelId, int cartId, String catId, String sortKey, Integer sortType, List<String> expCodes) {
        List<String> ret = new ArrayList<>();

        CmsSearchInfoBean2 params = new CmsSearchInfoBean2();
        params.setCartId(cartId);
        params.setCidValue(Lists.newArrayList(catId));
        params.setShopCatType(1);

        while (true) {
            int pageNum = 1;
            int pageSize = 100;

            params.setProductPageNum(pageNum);
            params.setProductPageSize(pageSize);
            if (StringUtil.isEmpty(sortKey)) {
                params.setSortOneName("created");
                params.setSortOneType("-1");
            } else {
                params.setSortOneName(sortKey);
                params.setSortOneType(String.valueOf(sortType));
            }

            CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(params, channelId, null, null);
            List<String> searchResCodes = cmsProductCodeListBean.getProductCodeList();
            if (ListUtils.notNull(searchResCodes)) {
                ret.addAll(searchResCodes.stream().filter(item -> !expCodes.contains(item)).collect(Collectors.toList()));
            }

            if (ListUtils.isNull(searchResCodes) || searchResCodes.size() < pageSize) {
                break;
            }
        }

        return ret;
    }
}
