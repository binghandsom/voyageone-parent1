package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.vms.channeladvisor.product.FieldModel;
import com.voyageone.service.dao.cms.mongo.CmsBtCAdProductDao;
import com.voyageone.service.dao.vms.VmsBtClientInventoryDao;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProductModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.vms.VmsBtClientInventoryModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/9/13.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_CA_Feed_Analysis)
public class CaAnalysisService extends BaseMQCmsService {

    @Autowired
    private CmsBtCAdProductDao cmsBtCAdProductDao;

    @Autowired
    private FeedToCmsService feedToCmsService;

    @Autowired
    private VmsBtClientInventoryDao vmsBtClientInventoryDao;

    @Autowired
    private MqSender sender;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        String channelId = messageMap.get("channelId").toString();
        List<String> sellerSKUs = (List<String>) messageMap.get("sellerSKUs");
        List<CmsBtCAdProductModel> feedList = cmsBtCAdProductDao.getProduct(channelId, sellerSKUs);
        feedList.forEach(cmsBtCAdProudctModel -> {
            List<CmsBtFeedInfoModel> products = convertToFeedInfo(channelId, cmsBtCAdProudctModel);
            if (products != null && products.size() > 0) {
                Map response = feedToCmsService.updateProduct(channelId, products, getTaskName());
            }
            updateVmsInventory(channelId, cmsBtCAdProudctModel);
        });

    }

    private List<CmsBtFeedInfoModel> convertToFeedInfo(String channelId, CmsBtCAdProductModel feed) {

        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        feed.getBuyableProducts().forEach(sku -> {
            CmsBtFeedInfoModel cmsBtFeedInfoModel = new CmsBtFeedInfoModel();
            cmsBtFeedInfoModel.setChannelId(channelId);
            cmsBtFeedInfoModel.setModel(feed.getSellerSKU());
            cmsBtFeedInfoModel.setBrand(getFieldValueByName(feed.getFields(), "brand"));
            cmsBtFeedInfoModel.setCategory(getFieldValueByName(feed.getFields(), "category").replaceAll("-", "－").replaceAll(" : ", "-"));
            cmsBtFeedInfoModel.setColor(getFieldValueByName(sku.getFields(), "color"));
            cmsBtFeedInfoModel.setCode(feed.getSellerSKU() + (StringUtil.isEmpty(cmsBtFeedInfoModel.getColor()) ? "" : "-" + cmsBtFeedInfoModel.getColor()));
            cmsBtFeedInfoModel.setLongDescription(getFieldValueByName(feed.getFields(), "description"));
            cmsBtFeedInfoModel.setName(getFieldValueByName(feed.getFields(), "title"));
            cmsBtFeedInfoModel.setImage(getImages(feed.getFields()));

            List<CmsBtFeedInfoModel_Sku> skus = new ArrayList<CmsBtFeedInfoModel_Sku>();
            CmsBtFeedInfoModel_Sku feedSku = new CmsBtFeedInfoModel_Sku();
            Double price = 0.0;
            if (!StringUtil.isEmpty(getFieldValueByName(sku.getFields(), "price"))) {
                price = Double.parseDouble(getFieldValueByName(sku.getFields(), "price"));
            }
            feedSku.setPriceNet(price);
            feedSku.setPriceMsrp(price);
            feedSku.setPriceClientRetail(0.0);
            feedSku.setPriceClientMsrp(0.0);
            feedSku.setPriceCurrent(price);
            feedSku.setClientSku(sku.getSellerSKU());
            feedSku.setQty(sku.getQuantity());
            feedSku.setSize(getFieldValueByName(sku.getFields(), "size"));
            feedSku.setSku(channelId + "-" + sku.getSellerSKU());
            feedSku.setImage(getImages(feed.getFields()));
            skus.add(feedSku);
            cmsBtFeedInfoModel.setSkus(skus);

            List<FieldModel> temp = JacksonUtil.jsonToBeanList(JacksonUtil.bean2Json(feed.getFields()), FieldModel.class);
            temp.addAll(sku.getFields());
            cmsBtFeedInfoModel.setAttribute(getAtt(temp));
            if (codeMap.containsKey(cmsBtFeedInfoModel.getCode())) {
                CmsBtFeedInfoModel beforeFeed = codeMap.get(cmsBtFeedInfoModel.getCode());
                beforeFeed.getSkus().addAll(cmsBtFeedInfoModel.getSkus());
                beforeFeed.getImage().addAll(cmsBtFeedInfoModel.getImage());
                beforeFeed.setImage(beforeFeed.getImage().stream().distinct().collect(Collectors.toList()));
                beforeFeed.setAttribute(BaseAnalysisService.attributeMerge(beforeFeed.getAttribute(), cmsBtFeedInfoModel.getAttribute()));
            } else {
                modelBeans.add(cmsBtFeedInfoModel);
                codeMap.put(cmsBtFeedInfoModel.getCode(), cmsBtFeedInfoModel);
            }
        });
        return modelBeans;

    }


    private String getFieldValueByName(List<FieldModel> fields, String name) {
        FieldModel field = fields.stream().filter(fieldModel -> fieldModel.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (field != null) {
            return field.getValue();
        }
        return "";
    }

    private List<String> getImages(List<FieldModel> fields) {
        List<String> images = new ArrayList<>();
        for (int i = 1; i < 20; i++) {

            String image = getFieldValueByName(fields, "productgroupimageurl" + i);
            if (!StringUtil.isEmpty(image)) {
                images.add(image);
            } else {
                break;
            }
        }
        return images;
    }

    private Map<String, List<String>> getAtt(List<FieldModel> fields) {

        Map<String, List<String>> attribute = new HashMap<>();
        fields.forEach(fieldModel -> {
            if (!StringUtil.isEmpty(fieldModel.getValue())) {
                if (attribute.containsKey(fieldModel.getName())) {
                    List<String> temp = attribute.get(fieldModel.getName());
                    temp.add(fieldModel.getValue());
                    attribute.put(fieldModel.getName(), temp);
                } else {
                    List<String> values = new ArrayList<>();
                    values.add(fieldModel.getValue());
                    attribute.put(fieldModel.getName(), values);
                }

            }
        });
        return attribute;
    }

    private void updateVmsInventory(String channelId, CmsBtCAdProductModel cmsBtCAdProduct) {
        List<String> skulist = new ArrayList<>();
        cmsBtCAdProduct.getBuyableProducts().forEach(buyableProductModel -> {
            skulist.add(buyableProductModel.getSellerSKU());
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("orderChannelId", channelId);
            param.put("sellerSku", buyableProductModel.getSellerSKU());
            VmsBtClientInventoryModel vmsBtClientInventoryModel = vmsBtClientInventoryDao.selectOne(param);
            if (vmsBtClientInventoryModel != null) {
                vmsBtClientInventoryModel.setQty(buyableProductModel.getQuantity());
                vmsBtClientInventoryModel.setStatus(buyableProductModel.getListingStatus().toString());
                vmsBtClientInventoryModel.setModifier(getTaskName());
                vmsBtClientInventoryModel.setModified(DateTimeUtil.getDate());
                vmsBtClientInventoryDao.update(vmsBtClientInventoryModel);
            } else {
                vmsBtClientInventoryModel = new VmsBtClientInventoryModel();
                vmsBtClientInventoryModel.setSellerSku(buyableProductModel.getSellerSKU());
                vmsBtClientInventoryModel.setQty(buyableProductModel.getQuantity());
                vmsBtClientInventoryModel.setStatus(buyableProductModel.getListingStatus().toString());
                vmsBtClientInventoryModel.setOrderChannelId(channelId);
                vmsBtClientInventoryModel.setCreater(getTaskName());
                vmsBtClientInventoryModel.setCreated(DateTimeUtil.getDate());
                vmsBtClientInventoryModel.setModifier(getTaskName());
                vmsBtClientInventoryModel.setModified(DateTimeUtil.getDate());
                vmsBtClientInventoryDao.insert(vmsBtClientInventoryModel);
            }
        });

        if(skulist.size() > 0){
            Map<String,Object> data = new HashMap<>();
            data.put("order_channel_id",channelId);
            data.put("skulist",skulist);
            sender.sendMessage(CmsMqRoutingKey.CMS_BATCH_CA_Update_Quantity, data);
        }
    }

}
