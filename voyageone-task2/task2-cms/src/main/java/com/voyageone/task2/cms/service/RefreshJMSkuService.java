package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.JumeiHtSkuService;
import com.voyageone.components.jumei.JumeiHtSpuService;
import com.voyageone.components.jumei.bean.JmGetProductInfoRes;
import com.voyageone.components.jumei.bean.JmGetProductInfo_Spus;
import com.voyageone.components.jumei.bean.JmGetProductInfo_Spus_Sku;
import com.voyageone.components.jumei.bean.StockSyncReq;
import com.voyageone.components.jumei.reponse.HtSkuUpdateResponse;
import com.voyageone.components.jumei.reponse.HtSpuUpdateResponse;
import com.voyageone.components.jumei.request.HtSkuUpdateRequest;
import com.voyageone.components.jumei.request.HtSpuUpdateRequest;
import com.voyageone.components.jumei.service.JumeiProductService;
import com.voyageone.components.jumei.service.JumeiService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan Shi on 2016/7/19.
 */
@Service
public class RefreshJMSkuService extends BaseTaskService {


    @Autowired
    JumeiProductService jumeiProductService;

    @Autowired
    JumeiHtSkuService jumeiHtSkuService;

    @Autowired
    JumeiHtSpuService jumeiHtSpuService;

    @Autowired
    JumeiService jumeiService;


    public List<String> getErrorSkuCode(ShopBean shop, CmsBtProductModel product) throws Exception {
        List<String> result = new ArrayList<>();

        Long productId = product.getProdId();

        $info("product:%s", product.getProdId());

        String jmProductId = product.getPlatform(27).getpProductId();
        $info("jmProductId:%s", jmProductId);


        String hashId = product.getPlatform(27).getpNumIId();
        $info("hashId:%s", hashId);
        if (StringUtils.isNullOrBlank2(hashId)) {
            throw new BusinessException("Bad hashId");
        }

        JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductById(shop, jmProductId);

        Thread.sleep(1000);
        List<JmGetProductInfo_Spus> spus = jmGetProductInfoRes.getSpus();

        for (JmGetProductInfo_Spus spu : spus) {
            $info("spu.getSpu_no():%s", spu.getSpu_no());
            $info("spu.getUpc_code():%s", spu.getUpc_code());

            String jmSpuNo = spu.getSpu_no();

            List<JmGetProductInfo_Spus_Sku> skus = spu.getSku_list();
            for (JmGetProductInfo_Spus_Sku sku : skus) {
                String skuCode = sku.getBusinessman_code();
                String jmSkuNo = sku.getSku_no();
                if (skuCode.startsWith("ERROR_")) {
                    $info("skuCode:%s",skuCode);

                    String line  = productId + "," + jmProductId + "," + jmSpuNo + "," + jmSkuNo + ","+skuCode;
                    result.add(line);
                }
            }

        }
        return result;
    }


    public String changeSkuCode(ShopBean shop, CmsBtProductModel product) throws Exception {

        $info("product:%s", product.getProdId());

        String jmProductId = product.getPlatform(27).getpProductId();
        $info("jmProductId:%s", jmProductId);
        String hashId = product.getPlatform(27).getpNumIId();
        $info("hashId:%s", hashId);
        if (StringUtils.isNullOrBlank2(hashId)) {
            throw new BusinessException("Bad hashId");
        }

        JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductById(shop, jmProductId);
        Thread.sleep(1000);
        List<JmGetProductInfo_Spus> spus = jmGetProductInfoRes.getSpus();
        List<BaseMongoMap<String, Object>> localSkus = product.getPlatform(27).getSkus();

        for (JmGetProductInfo_Spus spu : spus) {
            //仅仅在JM后台有这个spu, 在本地数据库没有了
            $info("spu.getSpu_no():%s", spu.getSpu_no());
            if (localSkus.stream().filter(w -> spu.getSpu_no().equals(w.getStringAttribute("jmSpuNo"))).count() == 0) {
                HtSpuUpdateRequest htSpuUpdateRequest = new HtSpuUpdateRequest();
                htSpuUpdateRequest.setJumei_spu_no(spu.getSpu_no());

                if (!spu.getUpc_code().startsWith("ERROR_")) {
                    htSpuUpdateRequest.setUpc_code("ERROR_" + spu.getUpc_code());
                } else {
                    htSpuUpdateRequest.setUpc_code(spu.getUpc_code());
                }


                HtSpuUpdateResponse htSpuUpdateResponse = jumeiHtSpuService.update(shop, htSpuUpdateRequest);
                Thread.sleep(1000);
                if (htSpuUpdateResponse != null && htSpuUpdateResponse.is_Success()) {
                    $info("更新Spu成功！[JmSpuNo:%s]", spu.getSpu_no());
                }
                //更新Spu失败
                else {
                    String msg = String.format("更新Spu失败！[JmSpuNo:%s]", spu.getSpu_no());
                    $error(msg);
                    throw new BusinessException(msg);
                }
            }

            List<JmGetProductInfo_Spus_Sku> skus = spu.getSku_list();
            for (JmGetProductInfo_Spus_Sku sku : skus) {

                //仅仅在JM后台有这个sku, 在本地数据库没有了
                $info("sku.getSku_no():%s", spu.getSpu_no());
                if (localSkus.stream().filter(w -> sku.getSku_no().equals(w.getStringAttribute("jmSkuNo"))).count() == 0) {
                    HtSkuUpdateRequest htSkuUpdateRequest = new HtSkuUpdateRequest();
                    htSkuUpdateRequest.setJumei_sku_no(sku.getSku_no());
                    htSkuUpdateRequest.setJumei_hash_id(hashId);
                    String oldSkuCode = sku.getBusinessman_code();
                    //先修改库存为0
                    StockSyncReq stockSyncReq = new StockSyncReq();
                    stockSyncReq.setBusinessman_code(oldSkuCode);
                    stockSyncReq.setEnable_num("0");
                    String stockSyncResponse = jumeiService.stockSync(shop, stockSyncReq);
                    $info("同步库存:%s", stockSyncResponse);
                    Thread.sleep(1000);


                    if (!oldSkuCode.startsWith("ERROR_")) {
                        htSkuUpdateRequest.setBusinessman_num("ERROR_" + oldSkuCode);
                    } else {
                        htSkuUpdateRequest.setBusinessman_num(oldSkuCode);
                    }

                    htSkuUpdateRequest.setCustoms_product_number(" ");

                    HtSkuUpdateResponse htSkuUpdateResponse = jumeiHtSkuService.update(shop, htSkuUpdateRequest);
                    Thread.sleep(1000);
                    if (htSkuUpdateResponse != null && htSkuUpdateResponse.is_Success()) {
                        $info("更新Sku成功！[skuCode:%s]", sku.getSku_no());
                    }
                    //更新Sku失败
                    else {
                        String msg = String.format("更新Sku失败！[skuCode:%s]", sku.getSku_no());
                        $error(msg);
                        throw new BusinessException(msg);
                    }
                }
            }
        }


        for (JmGetProductInfo_Spus spu : spus) {
            //仅仅在JM后台有这个spu, 在本地数据库没有了
            HtSpuUpdateRequest htSpuUpdateRequest = new HtSpuUpdateRequest();
            if (localSkus.stream().filter(w -> spu.getSpu_no().equals(w.getStringAttribute("jmSpuNo"))).count() > 0) {

                BaseMongoMap<String, Object> skuMap = localSkus.stream().filter(w -> w.getStringAttribute("jmSpuNo").equals(spu.getSpu_no())).findFirst().get();
                htSpuUpdateRequest.setJumei_spu_no(spu.getSpu_no());
                String skuCode = skuMap.getStringAttribute("skuCode");
                String barCode = product.getCommon().getSkus().stream().filter(w -> w.getSkuCode().equals(skuCode)).findFirst().get().getBarcode();
                htSpuUpdateRequest.setUpc_code(addVoToBarcode(barCode, "023"));


                HtSpuUpdateResponse htSpuUpdateResponse = jumeiHtSpuService.update(shop, htSpuUpdateRequest);
                Thread.sleep(1000);
                if (htSpuUpdateResponse != null && htSpuUpdateResponse.is_Success()) {
                    $info("更新Spu成功！[JmSpuNo:%s]", spu.getSpu_no());
                }
                //更新Spu失败
                else {
                    String msg = String.format("更新Spu失败！[JmSpuNo:%s]", spu.getSpu_no());
                    $error(msg);
                    throw new BusinessException(msg);
                }
            }


            List<JmGetProductInfo_Spus_Sku> skus = spu.getSku_list();
            for (JmGetProductInfo_Spus_Sku sku : skus) {
                HtSkuUpdateRequest htSkuUpdateRequest = new HtSkuUpdateRequest();
                htSkuUpdateRequest.setJumei_sku_no(sku.getSku_no());
                htSkuUpdateRequest.setJumei_hash_id(hashId);

                if (localSkus.stream().filter(w -> sku.getSku_no().equals(w.getStringAttribute("jmSkuNo"))).count() > 0) {


                    BaseMongoMap<String, Object> localSku = localSkus.stream().filter(w -> w.getStringAttribute("jmSkuNo").equals(sku.getSku_no())).findFirst().get();
                    htSkuUpdateRequest.setBusinessman_num(localSku.getStringAttribute("skuCode"));

                    htSkuUpdateRequest.setCustoms_product_number(" ");
                    HtSkuUpdateResponse htSkuUpdateResponse = jumeiHtSkuService.update(shop, htSkuUpdateRequest);
                    Thread.sleep(1000);

                    if (htSkuUpdateResponse != null && htSkuUpdateResponse.is_Success()) {
                        $info("更新Sku成功！[skuCode:%s]", sku.getSku_no());
                    }
                    //更新Sku失败
                    else {
                        String msg = String.format("更新Sku失败！[skuCode:%s]", sku.getSku_no());
                        $error(msg);
                        throw new BusinessException(msg);
                    }
                }
            }
        }
        $info("Success hashId:%s", hashId);

        return hashId;
    }

    private String addVoToBarcode(String barcode, String channelId) {
        if (StringUtils.isEmpty(barcode))
            return "";
        return barcode + "vo" + channelId;
    }


    @Override
    public SubSystem getSubSystem() {
        return null;
    }

    @Override
    public String getTaskName() {
        return null;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

    }
}
