package com.voyageone.service.impl.vms.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.vms.channeladvisor.*;
import com.voyageone.service.bean.vms.channeladvisor.enums.ErrorIDEnum;
import com.voyageone.service.bean.vms.channeladvisor.enums.RequestResultEnum;
import com.voyageone.service.bean.vms.channeladvisor.product.BuyableProductModel;
import com.voyageone.service.bean.vms.channeladvisor.product.BuyableProductResultModel;
import com.voyageone.service.bean.vms.channeladvisor.product.FieldModel;
import com.voyageone.service.bean.vms.channeladvisor.product.ProductGroupResultModel;
import com.voyageone.service.dao.cms.mongo.CmsBtCAdProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtCAdProductLogDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/9/12.
 * @version 2.0.0
 */
@Service
public class CAFeedProductService extends BaseService {
    @Autowired
    private CmsBtCAdProductLogDao cmsBtCAdProductLogDao;

    @Autowired
    private CmsBtCAdProductDao cmsBtCAdProductDao;

    @Autowired
    private MqSender sender;

    public List<ProductGroupResultModel> updateStatus(String channelId, List<CmsBtCAdProductModel> cmsMtCAdProudcts) {
        List<ProductGroupResultModel> productGroupResultModels = new ArrayList<>(cmsMtCAdProudcts.size());
        List<String> successSellerSKU = new ArrayList<>();
        cmsMtCAdProudcts.forEach(p -> {
            List<ErrorModel> errors = new ArrayList<>();
            ProductGroupResultModel productGroupResultModel = creadResult(channelId, p, errors);
            productGroupResultModels.add(productGroupResultModel);
            cmsBtCAdProductLogDao.insert(channelId, p);
            p.set_id(null);
            // 和之前的产品数据合并
            CmsBtCAdProductModel befCAdProudctModel = cmsBtCAdProductDao.getBySellerSku(channelId, p.getSellerSKU());
            if (befCAdProudctModel == null) {
                productGroupResultModel.getErrors().add(new ErrorModel(ErrorIDEnum.ProductNotFound));
                productGroupResultModel.setHasErrors(true);
            } else {
                for (int i = 0; i < p.getBuyableProducts().size(); i++) {
                    BuyableProductModel sku = p.getBuyableProducts().get(i);
                    boolean flg = false;
                    try {
                        for (BuyableProductModel befSku : befCAdProudctModel.getBuyableProducts()) {
                            if (sku.getSellerSKU().equalsIgnoreCase(befSku.getSellerSKU())) {
                                flg = true;
                                if (sku.getListingStatus() == null) throw new BusinessException("Status Quantity");
                                befSku.setListingStatus(sku.getListingStatus());
                                break;
                            }
                        }
                        if (!flg) {
                            productGroupResultModel.getBuyableProductResults().get(i).setHasErrors(true);
                            productGroupResultModel.getBuyableProductResults().get(i).setRequestResult(RequestResultEnum.Fail);
                            productGroupResultModel.getBuyableProductResults().get(i).setMarketPlaceItemID("");
                            productGroupResultModel.getBuyableProductResults().get(i).getErrors().add(new ErrorModel(ErrorIDEnum.ProductNotFound));
                        }
                    } catch (BusinessException e) {
                        productGroupResultModel.getBuyableProductResults().get(i).setHasErrors(true);
                        productGroupResultModel.getBuyableProductResults().get(i).setRequestResult(RequestResultEnum.Fail);
                        productGroupResultModel.getBuyableProductResults().get(i).setMarketPlaceItemID("");
                        productGroupResultModel.getBuyableProductResults().get(i).getErrors().add(new ErrorModel(ErrorIDEnum.InvalidRequest, e.getMessage()));
                    }
                }
                befCAdProudctModel.setModified(DateTimeUtil.getNowTimeStamp());
                befCAdProudctModel.setModifier("updateStatus");
                cmsBtCAdProductDao.update(channelId, befCAdProudctModel);
                successSellerSKU.add(befCAdProudctModel.getSellerSKU());
            }
            //successSellerSKU.add(p.getSellerSKU());
        });
        sendMq(channelId, successSellerSKU);
        return productGroupResultModels;
    }

    // 更新价格和库存
    public List<ProductGroupResultModel> updateQuantityPrice(String channelId, List<CmsBtCAdProductModel> cmsMtCAdProudcts) {
        List<ProductGroupResultModel> productGroupResultModels = new ArrayList<>(cmsMtCAdProudcts.size());
        List<String> successSellerSKU = new ArrayList<>();
        cmsMtCAdProudcts.forEach(p -> {
            List<ErrorModel> errors = new ArrayList<>();
            ProductGroupResultModel productGroupResultModel = creadResult(channelId, p, errors);
            productGroupResultModels.add(productGroupResultModel);
            cmsBtCAdProductLogDao.insert(channelId, p);
            p.set_id(null);
            // 和之前的产品数据合并
            CmsBtCAdProductModel befCAdProudctModel = cmsBtCAdProductDao.getBySellerSku(channelId, p.getSellerSKU());
            if (befCAdProudctModel == null) {
                productGroupResultModel.getErrors().add(new ErrorModel(ErrorIDEnum.ProductNotFound));
                productGroupResultModel.setHasErrors(true);
            } else {
                for (int i = 0; i < p.getBuyableProducts().size(); i++) {
                    BuyableProductModel sku = p.getBuyableProducts().get(i);
                    boolean flg = false;
                    try {
                        for (BuyableProductModel befSku : befCAdProudctModel.getBuyableProducts()) {
                            if (sku.getSellerSKU().equalsIgnoreCase(befSku.getSellerSKU())) {
                                flg = true;
                                if (sku.getQuantity() == null) throw new BusinessException("Invalid Quantity");
                                Double price = 0.0;
                                if (sku.price() != null) {
                                    price = Double.parseDouble(sku.price().getValue());
                                }
                                if (price.compareTo(0.0) == 0) throw new BusinessException("Invalid Price");
                                befSku.setQuantity(sku.getQuantity());
                                befSku.setListingStatus(sku.getListingStatus());
                                FieldModel priceField = befSku.price();
                                priceField.setValue(sku.price().getValue());
                                break;
                            }
                        }
                        if (!flg) {
                            productGroupResultModel.getBuyableProductResults().get(i).setHasErrors(true);
                            productGroupResultModel.getBuyableProductResults().get(i).setRequestResult(RequestResultEnum.Fail);
                            productGroupResultModel.getBuyableProductResults().get(i).setMarketPlaceItemID("");
                            productGroupResultModel.getBuyableProductResults().get(i).getErrors().add(new ErrorModel(ErrorIDEnum.ProductNotFound));
                        }
                    } catch (BusinessException e) {
                        productGroupResultModel.getBuyableProductResults().get(i).setHasErrors(true);
                        productGroupResultModel.getBuyableProductResults().get(i).setRequestResult(RequestResultEnum.Fail);
                        productGroupResultModel.getBuyableProductResults().get(i).setMarketPlaceItemID("");
                        productGroupResultModel.getBuyableProductResults().get(i).getErrors().add(new ErrorModel(ErrorIDEnum.InvalidRequest, e.getMessage()));
                    }
                }
                befCAdProudctModel.setModified(DateTimeUtil.getNowTimeStamp());
                befCAdProudctModel.setModifier("updateQuantityPrice");
                cmsBtCAdProductDao.update(channelId, befCAdProudctModel);
                successSellerSKU.add(befCAdProudctModel.getSellerSKU());
            }
        });
        sendMq(channelId, successSellerSKU);
        return productGroupResultModels;
    }

    // 创建更新产品
    public String updateProduct(String channelId, List<CmsBtCAdProductModel> cmsMtCAdProudcts) {
        List<ProductGroupResultModel> productGroupResultModels = new ArrayList<>(cmsMtCAdProudcts.size());
        List<String> successSellerSKU = new ArrayList<>();
        cmsMtCAdProudcts.forEach(p -> {
            List<ErrorModel> errors = requiredProductCheck(p);
            ProductGroupResultModel productGroupResultModel = creadResult(channelId, p, errors);
            productGroupResultModels.add(productGroupResultModel);
            cmsBtCAdProductLogDao.insert(channelId, p);
            p.set_id(null);
            // 和之前的产品数据合并
            if (errors.size() == 0) {
                CmsBtCAdProductModel befCAdProudctModel = cmsBtCAdProductDao.getBySellerSku(channelId, p.getSellerSKU());
                if (befCAdProudctModel == null) {
                    p.setCreated(DateTimeUtil.getNowTimeStamp());
                    p.setCreater("updateProduct");
                    p.setModified(DateTimeUtil.getNowTimeStamp());
                    p.setModifier("updateProduct");
                    cmsBtCAdProductDao.insert(channelId, p);
                    successSellerSKU.add(p.getSellerSKU());
                } else {
                    p.set_id(befCAdProudctModel.get_id());
                    for (BuyableProductModel befSku : befCAdProudctModel.getBuyableProducts()) {
                        boolean flg = false;
                        for (BuyableProductModel sku : p.getBuyableProducts()) {
                            if (sku.getSellerSKU().equalsIgnoreCase(befSku.getSellerSKU())) {
                                flg = true;
                                break;
                            }
                        }
                        if (!flg) {
                            p.getBuyableProducts().add(befSku);
                        }
                    }
                    p.setModified(DateTimeUtil.getNowTimeStamp());
                    p.setModifier("updateProduct");
                    cmsBtCAdProductDao.update(channelId, p);
                    successSellerSKU.add(p.getSellerSKU());
                }
            }
        });
        sendMq(channelId, successSellerSKU);
        return JacksonUtil.bean2Json(productGroupResultModels);
    }

    private List<ErrorModel> requiredProductCheck(CmsBtCAdProductModel cmsBtCAdProductModel) {
        List<ErrorModel> errors = new ArrayList<>();
        //ProductGroupResultModel productGroupResultModel = new ProductGroupResultModel();
        if (StringUtil.isEmpty(cmsBtCAdProductModel.getSellerSKU())) {
            ErrorModel error = new ErrorModel(ErrorIDEnum.InvalidSellerID);
            errors.add(error);
        }
        cmsBtCAdProductModel.getBuyableProducts().forEach(buyableProductModel -> {
            if (StringUtil.isEmpty(buyableProductModel.getSellerSKU())) {
                ErrorModel error = new ErrorModel(ErrorIDEnum.InvalidSellerID);
                errors.add(error);
            }

            List<FieldModel> priceField = buyableProductModel.getFields().stream().filter(fieldModel -> "price".equalsIgnoreCase(fieldModel.getName())).collect(Collectors.toList());
            Double price = 0.0;
            if (priceField != null && priceField.size() > 0) {
                price = Double.parseDouble(priceField.get(0).getValue());
            }
            if (price.intValue() == 0) {
                ErrorModel error = new ErrorModel(ErrorIDEnum.ProductNotFound);
                errors.add(error);
            }
        });
        return errors;
    }

    private ProductGroupResultModel creadResult(String channelId, CmsBtCAdProductModel cmsBtCAdProductModel, List<ErrorModel> errors) {
        ProductGroupResultModel productGroupResultModel = new ProductGroupResultModel();
        List<BuyableProductResultModel> buyableProductResultModels = new ArrayList<>();
        cmsBtCAdProductModel.getBuyableProducts().forEach(buyableProductModel -> {
            BuyableProductResultModel buyableProductResultModel = new BuyableProductResultModel();
            buyableProductResultModel.setSellerSKU(buyableProductModel.getSellerSKU());
            if (errors.size() == 0) {
                buyableProductResultModel.setMarketPlaceItemID("channelId-" + buyableProductModel.getSellerSKU());
                buyableProductResultModel.setRequestResult(RequestResultEnum.Success);
            } else {
                buyableProductResultModel.setRequestResult(RequestResultEnum.Fail);
            }
            buyableProductResultModels.add(buyableProductResultModel);
        });
        productGroupResultModel.setSellerSKU(cmsBtCAdProductModel.getSellerSKU());
        productGroupResultModel.setErrors(errors);
        productGroupResultModel.setHasErrors(errors.size() > 0);
        productGroupResultModel.setBuyableProductResults(buyableProductResultModels);
        return productGroupResultModel;
    }

    private void sendMq(String channelId, List<String> sellerSKUs) {
        if (sellerSKUs != null && sellerSKUs.size() > 0) {
            Map<String, Object> message = new HashMap<>();
            message.put("channelId", "996");
            message.put("sellerSKUs", sellerSKUs);
            sender.sendMessage(MqRoutingKey.CMS_BATCH_CA_Feed_Analysis, message);
        }
    }
}
