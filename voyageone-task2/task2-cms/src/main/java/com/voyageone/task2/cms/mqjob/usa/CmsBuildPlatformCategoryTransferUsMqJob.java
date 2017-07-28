package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductTopDao;
import com.voyageone.service.impl.cms.SellerCatService;
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
import java.util.List;

/**
 * Created by Charis on 2017/7/25.
 */
@Service
@RabbitListener()
public class CmsBuildPlatformCategoryTransferUsMqJob extends TBaseMQCmsService<CmsCategoryReceiveMQMessageBody> {

    private static final String symbol = "-";

    @Autowired
    private SellerCatService sellerCatService;

    @Autowired
    private CmsBtProductTopDao cmsBtProductTopDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    @Override
    public void onStartup(CmsCategoryReceiveMQMessageBody messageBody) throws Exception {

        String channelId = messageBody.getChannelId();
        String cartId = messageBody.getCartId();
        List<String> fullCatIds = messageBody.getFullCatIds(); // "-"分割

        List<CmsBtSellerCatModel> sellerCatModels = sellerCatService.getSellerCatsByChannelCart(channelId, Integer.parseInt(cartId), true);
        for (String fullCatId : fullCatIds) {
            TransferUsCategoryModel categoryModel = new TransferUsCategoryModel();

            String[] fullCatIdArrs = fullCatId.split(symbol);
            String catId = fullCatId;
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
                codes.addAll(cmsBtProductDao.selectListCodeBySellerCat(channelId, Integer.parseInt(cartId), catId, null, null, null, true)); // 普通code排序
            } else {
                if (ListUtils.notNull(topModel.getProductCodeList())) {
                    codes.addAll(topModel.getProductCodeList()); // 置顶列表
                }
                codes.addAll(cmsBtProductDao.selectListCodeBySellerCat(channelId, Integer.parseInt(cartId), catId, topModel.getSortColumnName(), topModel.getSortType(), topModel.getProductCodeList(), true)); // 普通code排序

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
}
