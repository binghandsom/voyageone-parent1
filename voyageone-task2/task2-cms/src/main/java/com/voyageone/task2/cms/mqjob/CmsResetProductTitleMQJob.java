package com.voyageone.task2.cms.mqjob;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsResetProductTitleMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 重新设置商品Title MQJob
 *
 * @Author rex.wu
 * @Create 2017-06-13 11:14
 */
@Service
@RabbitListener
public class CmsResetProductTitleMQJob extends TBaseMQCmsService<CmsResetProductTitleMQMessageBody> {

    @Autowired
    private ProductService productService;
    @Autowired
    private SxProductService sxProductService;

    /**
     * MqJobService需要实现此方法
     *
     * @param messageBody Mq消息Map
     */
    @Override
    public void onStartup(CmsResetProductTitleMQMessageBody messageBody) throws Exception {

        $info(String.format("重设置商品标题MQ内容: %s", JacksonUtil.bean2Json(messageBody)));

        String channelId = messageBody.getChannelId();
        List<String> codes = messageBody.getCodes();
        String username = messageBody.getSender();
        if (StringUtils.isBlank(username)) {
            username = "CmsResetProductTitleMQJob";
        }

        if (StringUtils.isNotBlank(channelId) && CollectionUtils.isNotEmpty(codes)) {

            // 失败信息
            List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

            for (String code : codes) {
                CmsBtProductModel productModel = productService.getProductByCode(channelId, code);
                if (productModel == null) {
                    CmsBtOperationLogModel_Msg failOne = new CmsBtOperationLogModel_Msg();
                    failOne.setSkuCode(code);
                    failOne.setMsg(String.format("%s渠道下找不到该商品", channelId));
                    failList.add(failOne);
                    continue;
                }

                CmsBtProductModel_Field fields = productModel.getCommon().getFields();

                String brand = fields.getBrand(); // 商品Brand
                // 商品品牌为空，跳过
                if (StringUtils.isBlank(brand)) {
                    CmsBtOperationLogModel_Msg failOne = new CmsBtOperationLogModel_Msg();
                    failOne.setSkuCode(code);
                    failOne.setMsg("该商品Brand为空");
                    failList.add(failOne);
                    continue;
                }

                // 根据商品品牌查询com_mt_value_channel记录，查询不到则跳过
                TypeChannelBean brandTypeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.BRAND_41, channelId, brand, "cn");
                if (brandTypeChannelBean == null) {
                    CmsBtOperationLogModel_Msg failOne = new CmsBtOperationLogModel_Msg();
                    failOne.setSkuCode(code);
                    failOne.setMsg(String.format("根据Brand(%s)在synship.com_mt_value_channel查询不到记录", brand));
                    failList.add(failOne);
                    continue;
                }

                // 原来[brand]要替换成[brand 品牌]
                String replaceContent = brandTypeChannelBean.getName();
                String replaceContentWithoutBlank = replaceContent.replaceAll(" ", "");

                HashMap<String, Object> updateMap = new HashMap<>();
                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("channelId", channelId);
                queryMap.put("common.fields.code", fields.getCode());

                boolean isMasterFlag = false; // Master产品名称中文->是否修改了
                boolean isJmFlag = false;     // 聚美产品名 / 聚美长标题 / 聚美中标题->是否修改了
                boolean isUsjoiFlag = false;  // 官网同构标题->是否修改了
                boolean isJgjFlag = false;    // 匠心界标题->是否修改了

                // Master产品名称中文
                String originalTitleCn = fields.getOriginalTitleCn();
                if (StringUtils.isNotBlank(originalTitleCn)
                        && !originalTitleCn.contains(replaceContent)
                        && originalTitleCn.contains(brand) && !removeBlank(originalTitleCn).contains(replaceContentWithoutBlank)) {

                    String newTitle = originalTitleCn.replace(brand, replaceContent);

                    updateMap.put("common.fields.originalTitleCn", newTitle);

                    if("928".equals(channelId) || "024".equals(channelId)){
                        updateMap.put("common.catConf", "1");
                    }

                    isMasterFlag = true;
                }

                // 聚美产品名 / 聚美长标题 / 聚美中标题
                CmsBtProductModel_Platform_Cart jmCart = productModel.getPlatform(CartEnums.Cart.JM);
                if (jmCart != null && jmCart.getFields() != null) {
                    String productNameCn = jmCart.getFields().getStringAttribute("productNameCn");
                    String productLongName = jmCart.getFields().getStringAttribute("productLongName");
                    String productMediumName = jmCart.getFields().getStringAttribute("productMediumName");

                    if (StringUtils.isNotBlank(productNameCn)
                            && !productNameCn.contains(replaceContent)
                            && productNameCn.contains(brand) && !removeBlank(productNameCn).contains(replaceContentWithoutBlank)) {

                        updateMap.put(String.format("platforms.P%s.fields.productNameCn", CartEnums.Cart.JM.getId()), productNameCn.replace(brand, replaceContent));
                        isJmFlag = true;
                    }

                    if (StringUtils.isNotBlank(productLongName)
                            && !productLongName.contains(replaceContent)
                            && productLongName.contains(brand) && !removeBlank(productLongName).contains(replaceContentWithoutBlank)) {

                        updateMap.put(String.format("platforms.P%s.fields.productLongName", CartEnums.Cart.JM.getId()), productLongName.replace(brand, replaceContent));
                        isJmFlag = true;
                    }

                    if (StringUtils.isNotBlank(productMediumName)
                            && !productMediumName.contains(replaceContent)
                            && productMediumName.contains(brand) && !removeBlank(productMediumName).contains(replaceContentWithoutBlank)) {

                        updateMap.put(String.format("platforms.P%s.fields.productMediumName", CartEnums.Cart.JM.getId()), productMediumName.replace(brand, replaceContent));
                        isJmFlag = true;
                    }
                }

                // 官网同购标题
                CmsBtProductModel_Platform_Cart usjoiCart = productModel.getPlatform(CartEnums.Cart.LTT);
                String title = null;
                if (usjoiCart != null && usjoiCart.getFields() != null
                        && StringUtils.isNotBlank(title = usjoiCart.getFields().getStringAttribute("title"))
                        && !title.contains(replaceContent)
                        && title.contains(brand) && !removeBlank(title).contains(replaceContentWithoutBlank)) {

                    updateMap.put(String.format("platforms.P%s.fields.title", CartEnums.Cart.LTT.getId()), title.replace(brand, replaceContent));
                    isUsjoiFlag = true;
                }

                // 匠心界标题
                CmsBtProductModel_Platform_Cart jgjCart = productModel.getPlatform(CartEnums.Cart.JGJ);
                String productTitle = null;
                if (jgjCart != null && jgjCart.getFields() != null
                        && StringUtils.isNotBlank(productTitle = jgjCart.getFields().getStringAttribute("productTitle"))
                        && !productTitle.contains(replaceContent)
                        && productTitle.contains(brand) && !removeBlank(productTitle).contains(replaceContentWithoutBlank)) {

                    updateMap.put(String.format("platforms.P%s.fields.productTitle", CartEnums.Cart.JGJ.getId()), productTitle.replace(brand, replaceContent));
                    isJgjFlag = true;
                }

                if (!updateMap.isEmpty()) {
                    try {
                        BulkWriteResult writeResult = productService.bulkUpdateWithMap(channelId, Collections.singletonList(createBulkUpdateModel(updateMap, queryMap)), username, "$set");
                        $info(String.format("(%s)重设置产品标题(channelId=%s, code=%s)，结果：%s", username, channelId, code, JacksonUtil.bean2Json(writeResult)));

                        if (isMasterFlag) {
                            // 产品如有平台状态Approved，则重新上新
                            for (CmsBtProductModel_Platform_Cart platformCart : productModel.getPlatforms().values()) {
                                Integer cartId = platformCart.getCartId();
                                if (cartId > 19 && cartId < 900 && CmsConstants.ProductStatus.Approved.name().equals(platformCart.getStatus())) {
                                    $debug(String.format("(%s)重设置Master产品名称中文(channel=%s, code=%s)，平台(cartId=%d)Approved需重新上新", username, channelId, code, cartId));
                                    sxProductService.insertPlatformWorkload(channelId, cartId, PlatformWorkloadAttribute.TITLE, Collections.singletonList(code), username);
                                }
                            }
                        } else {
                            // 修改的平台如果是Approved状态，则重新上新
                            if (isJmFlag && CmsConstants.ProductStatus.Approved.name().equals(jmCart.getStatus())) {
                                $debug(String.format("(%s)重设置聚美产品名 / 聚美长标题 / 聚美中标题(channel=%s, code=%s)，平台(cartId=%s)Approved需重新上新", username, channelId, code, CartEnums.Cart.JM.getId()));
                                sxProductService.insertPlatformWorkload(channelId, Integer.valueOf(CartEnums.Cart.JM.getId()), PlatformWorkloadAttribute.TITLE, Collections.singletonList(code), username);
                            }

                            if (isUsjoiFlag && CmsConstants.ProductStatus.Approved.name().equals(usjoiCart.getStatus())) {
                                $debug(String.format("(%s)重设置官网同构标题(channel=%s, code=%s)，平台(cartId=%s)Approved需重新上新", username, channelId, code, CartEnums.Cart.LTT.getId()));
                                sxProductService.insertPlatformWorkload(channelId, Integer.valueOf(CartEnums.Cart.LTT.getId()), PlatformWorkloadAttribute.TITLE, Collections.singletonList(code), username);
                            }

                            if (isJgjFlag && CmsConstants.ProductStatus.Approved.name().equals(jgjCart.getStatus())) {
                                $debug(String.format("(%s)重设置匠心界标题(channel=%s, code=%s)，平台(cartId=%s)Approved需重新上新", username, channelId, code, CartEnums.Cart.JGJ.getId()));
                                sxProductService.insertPlatformWorkload(channelId, Integer.valueOf(CartEnums.Cart.JGJ.getId()), PlatformWorkloadAttribute.TITLE, Collections.singletonList(code), username);
                            }
                        }
                    } catch (Exception e) {
                        $error(String.format("(%s)重设置产品Title(channelId=%s, code=%s)错误", username, channelId, code), e);
                        CmsBtOperationLogModel_Msg exceptionOne = new CmsBtOperationLogModel_Msg();
                        exceptionOne.setSkuCode(code);
                        exceptionOne.setMsg("高级检索批量修改产品标题错误");
                        failList.add(exceptionOne);
                    }

                }
            }

            if (failList.isEmpty()) {
                cmsSuccessLog(messageBody, String.format("(%s)重设置产品标题OK", username));
            } else {
                cmsSuccessIncludeFailLog(messageBody, String.format("(%s)重设置产品标题部分失败", username), failList);
            }
        }

    }


    private BulkUpdateModel createBulkUpdateModel(HashMap<String, Object> updateMap, HashMap<String, Object> queryMap) {
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        return model;
    }

    private String removeBlank (String targetString) {
        return targetString == null ? null : targetString.replaceAll(" ", "");
    }
}
