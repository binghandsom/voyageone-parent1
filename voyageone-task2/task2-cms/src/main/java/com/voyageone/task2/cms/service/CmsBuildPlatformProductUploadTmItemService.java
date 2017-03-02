package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.taobao.api.response.TmallItemSchemaAddResponse;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.factory.SchemaWriter;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.bean.ItemSchema;
import com.voyageone.components.tmall.service.TbCategoryService;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by morse.lu on 2016/5/11.
 */
@Service
public class CmsBuildPlatformProductUploadTmItemService extends BaseService {

    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private TbProductService tbProductService;
    @Autowired
    private TbCategoryService tbCategoryService;

    /**
     * TM上新商品
     *
     * @param expressionParser ExpressionParser (包含SxData)
     * @param platformProductId 平台产品id. 新建的场合：上传产品API调用后返回的产品id. 更新的场合：ProductGroup里platformPid（sxData.getPlatform().getPlatformPid()）
     * @param cmsMtPlatformCategorySchemaModel MongoDB  propsItem取得用
     * @param cmsMtPlatformMappingModel MongoDB 平台CategoryId取得，mapping设定
     * @param shopBean 店铺
     * @param modifier 更新者
     * @return numIId
     * @throws Exception
     */
    public String uploadItem(ExpressionParser expressionParser, String platformProductId, CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel, CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel, ShopBean shopBean, String modifier) throws Exception {
        SxData sxData = expressionParser.getSxData();
        String numIId = sxData.getPlatform().getNumIId();
//        Long categoryCode = Long.valueOf(cmsMtPlatformMappingModel.getPlatformCategoryId());
        Long categoryCode = Long.parseLong(sxData.getMainProduct().getPlatform(sxData.getCartId()).getpCatId());
        // added by morse.lu 2016/07/14 start
        // 无产品只有商品
        if (StringUtils.isEmpty(platformProductId)) {
            platformProductId = "0";
        }
        // added by morse.lu 2016/07/14 end

        // 20170206 tom 直接用天猫上最新的（这样就可以拿到最新的产品规格， 之后的版本会改进效率） START
//        String itemSchema = cmsMtPlatformCategorySchemaModel.getPropsItem();
        String itemSchema ="";
        try {
            String strCatId = expressionParser.getSxData().getMainProduct().getPlatform(expressionParser.getSxData().getCartId()).getpCatId();
            Long catId = Long.parseLong(strCatId);
            ItemSchema schema = tbCategoryService.getTbItemAddSchema(shopBean, catId, Long.parseLong(platformProductId));
            if (schema != null && schema.getResult() == 0) {
                itemSchema = schema.getItemResult();
            }
        } catch (Exception e) {

        }
        // 20170206 tom 直接用天猫上最新的（这样就可以拿到最新的产品规格， 之后的版本会改进效率） END

        // deleted by morse.lu 2016/10/16 start
        // 这段又不需要了- -！ 因为允许改类目了
        // added by morse.lu 2016/08/16 start
        if (!StringUtils.isEmpty(numIId)) {
            // 更新的话，实时去取
            // 获取更新商品的规则的schema
            String errMsg = String.format("更新商品schema取得失败!商品编号[%s]", numIId);
            try {
                TmallItemUpdateSchemaGetResponse updateItemResponse = tbProductService.doGetWareInfoItem(numIId, shopBean);
                if (updateItemResponse.getErrorCode() != null) {
                    logger.error(updateItemResponse.getSubMsg());
                    sxData.setErrorMessage(updateItemResponse.getSubMsg());
                    throw new BusinessException(updateItemResponse.getSubMsg());
                }
                String updateItemSchema = updateItemResponse.getUpdateItemResult();
                if (StringUtils.isEmpty(updateItemSchema)) {
                    sxData.setErrorMessage(errMsg);
                    throw new BusinessException(errMsg);
                }
                sxData.setUpdateItemFields(SchemaReader.readXmlForMap(updateItemSchema));
            } catch (TopSchemaException e) {
                $error(e.getMessage(), e);
                sxData.setErrorMessage("Can't convert schema to fields: " + e.getMessage());
                throw new BusinessException("Can't convert schema to fields: " + e.getMessage());
            } catch (ApiException e) {
                sxData.setErrorMessage(e.getMessage());
                throw new BusinessException(e.getMessage());
            }
        }
        // added by morse.lu 2016/08/16 end
        // deleted by morse.lu 2016/10/16 end
        $debug("itemSchema:" + itemSchema);

        List<Field> fields;
        try {
            fields = SchemaReader.readXmlForList(itemSchema);
        } catch (TopSchemaException e) {
            $error(e.getMessage(), e);
            sxData.setErrorMessage("Can't convert schema to fields: " + e.getMessage());
            throw new BusinessException("Can't convert schema to fields: " + e.getMessage());
        }

        try {
            sxProductService.constructMappingPlatformProps(fields, cmsMtPlatformMappingModel, shopBean, expressionParser, modifier, true);
        } catch (BusinessException be) {
            sxData.setErrorMessage(be.getMessage());
            throw be;
        } catch (Exception e) {
            $error("商品类目设值失败! " + e.getMessage());
            sxData.setErrorMessage(e.getMessage());
            throw new BusinessException(e.getMessage());
        }

        // 如果添加或更新天猫商品时，报商品是否为新品打标错误的时候，不要报出错误，把新品->非新品再试一次
        for (int retry = 0; retry < 2; retry++) {
            if (StringUtils.isEmpty(numIId)) {
                // add
                try {
                    $debug("addTmallItem: [productCode:" + platformProductId + ", categoryCode:" + categoryCode + "]");
                    numIId = addTmallItem(categoryCode, platformProductId, fields, shopBean);
                } catch (ApiException e) {
//                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                    // 当前用户拥有该类目下没有发布新品权限时,返回错误码:isv.invalid-permission:xinpin (参考：taobao.item.update (更新商品信息))
                    // 如果不是一口价全新的宝贝被设置为新品,返回错误码：isv.invalid-parameter:xinpin   (参考：taobao.item.update (更新商品信息))
                    // 如果没有上传上传一张吊牌图片，会返回错误码：isv.invalid-parameter:isXinpin 发布新品，必须上传一张吊牌图片,但正常情况下天猫上新
                    // 都会上传吊牌图的(如果没有吊牌图会上传一张白色背景图)，所以如果出现必须上传吊牌图的错误，就抛出错误
                    if (retry == 0
                            && (e.getMessage().contains("isv.invalid-permission:add-xinpin") || e.getMessage().contains("isv.invalid-parameter:xinpin") || e.getMessage().contains("isv.invalid-parameter:isXinpin"))) {
                        // 把field列表中的"是否新品"从"是(true)"->"否(false)",再做一次新增商品
                        sxProductService.setFieldValue(fields, "is_xinpin", "false");
                        continue;
                    }
                    sxData.setErrorMessage(e.getMessage());
                    throw new BusinessException(e.getMessage());
                } catch (BusinessException e) {
                    if (retry == 0
                            && (e.getMessage().contains("isv.invalid-permission:add-xinpin") || e.getMessage().contains("isv.invalid-parameter:xinpin") || e.getMessage().contains("isv.invalid-parameter:isXinpin"))) {
                        // 把field列表中的"是否新品"从"是(true)"->"否(false)",再做一次新增商品
                        sxProductService.setFieldValue(fields, "is_xinpin", "false");
                        continue;
                    }
                    sxData.setErrorMessage(e.getMessage());
                    throw new BusinessException(e.getMessage());
                }
            } else {
                // update
                try {
                    $debug("updateTmallItem: [productCode:" + platformProductId + ", categoryCode:" + categoryCode + ", numIId:" + numIId + "]");
                    numIId = updateTmallItem(platformProductId, numIId, categoryCode, fields, shopBean);
                } catch (ApiException e) {
//                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                    if (retry == 0
                            && (e.getMessage().contains("isv.invalid-permission:add-xinpin") || e.getMessage().contains("isv.invalid-parameter:xinpin") || e.getMessage().contains("isv.invalid-parameter:isXinpin"))) {
                        // 把field列表中的"是否新品"从"是(true)"->"否(false)",再做一次更新商品
                        sxProductService.setFieldValue(fields, "is_xinpin", "false");
                        continue;
                    }
                    sxData.setErrorMessage(e.getMessage());
                    throw new BusinessException(e.getMessage());
                } catch (BusinessException e) {
                    if (retry == 0
                            && (e.getMessage().contains("isv.invalid-permission:add-xinpin") || e.getMessage().contains("isv.invalid-parameter:xinpin") || e.getMessage().contains("isv.invalid-parameter:isXinpin"))) {
                        // 把field列表中的"是否新品"从"是(true)"->"否(false)",再做一次更新商品
                        sxProductService.setFieldValue(fields, "is_xinpin", "false");
                        continue;
                    }
                    sxData.setErrorMessage(e.getMessage());
                    throw new BusinessException(e.getMessage());
                }
            }

            // 如果没有商品是否为新品打标错误时，只需要做一次就跳过循环
            break;
        }

        return numIId;
    }

    private String addTmallItem(Long categoryCode, String productCode, List<Field> itemFields, ShopBean shopBean) throws ApiException {
        String xmlData;

        try {
            xmlData = SchemaWriter.writeParamXmlString(itemFields);
        } catch (TopSchemaException e) {
//            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new BusinessException(e.getMessage());
        }
        StringBuffer failCause = new StringBuffer();
        $debug("tmall category code:" + categoryCode);
        $debug("xmlData:" + xmlData);
        TmallItemSchemaAddResponse addItemResponse = tbProductService.addItem(categoryCode, productCode, xmlData, shopBean);
        String numId;
        if (addItemResponse == null) {
            failCause.append("Tmall return null response when add item");
            $error(failCause + ", request:" + xmlData);
            throw new BusinessException(failCause.toString());
        } else if (addItemResponse.getErrorCode() != null) {
            $debug("errorCode:" + addItemResponse.getErrorCode());
            if (addItemResponse.getSubCode().contains("IC_CHECKSTEP_ALREADY_EXISTS_SAME_SPU")) {
                String subMsg = addItemResponse.getSubMsg();
                numId = getNumIdFromSubMsg(subMsg, failCause);
                if (failCause.length() == 0 && numId != null && !"".equals(numId)) {
                    $debug(String.format("find numId(%s) has been uploaded before", numId));
                    return numId;
                }
            } else {
                failCause.append(addItemResponse.getSubCode());
                failCause.append(addItemResponse.getSubMsg());
                if (addItemResponse.getSubMsg() != null && addItemResponse.getSubMsg().contains("类目必选属性未填写")) {
                    failCause.append(", 如果属性是产品属性的话， 可能需要去商家后台去纠错：https://product.tmall.com/product/spu_detail.htm?spu_id=" + productCode);
                }
            }

            //天猫系统服务异常
            if (failCause.indexOf("天猫商品服务异常") != -1
                    || failCause.indexOf("访问淘宝超时") != -1) {
                $debug("此处应该是下次启动任务仍需处理的错误--->" + failCause.toString());
                throw new BusinessException(failCause.toString());
            }
            else {
                throw new BusinessException(failCause.toString());
            }
        } else {
            numId = addItemResponse.getAddItemResult();
            $debug("numId: " + numId);
            return numId;
        }
    }

    private String updateTmallItem(String productId, String numId, Long categoryCode, List<Field> itemFields, ShopBean shopBean) throws ApiException {
        String xmlData;

        try {
            xmlData = SchemaWriter.writeParamXmlString(itemFields);
        } catch (TopSchemaException e) {
//            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new BusinessException(e.getMessage());
        }
        StringBuffer failCause = new StringBuffer();
        $debug("tmall category code:" + categoryCode);
        $debug("numId:" + numId);
        $debug("xmlData:" + xmlData);
        TmallItemSchemaUpdateResponse updateItemResponse = tbProductService.updateItem(productId, numId, categoryCode, xmlData, shopBean);
        if (updateItemResponse == null) {
            failCause.append("Tmall return null response when update item");
            $error(failCause + ", request:" + xmlData);
            throw new BusinessException(failCause.toString());
        } else if (updateItemResponse.getErrorCode() != null) {
            $debug("errorCode:" + updateItemResponse.getErrorCode());
            String subMsg = updateItemResponse.getSubMsg();
            numId = getNumIdFromSubMsg(subMsg, failCause);
            if (numId != null && !"".equals(numId)) {
                $debug(String.format("find numId(%s) has been uploaded before", numId));
                return numId;
            }
            failCause.append(updateItemResponse.getSubCode());
            if (updateItemResponse.getSubMsg() != null && updateItemResponse.getSubMsg().contains("类目必选属性未填写")) {
                failCause.append(", 如果属性是产品属性的话， 可能需要去商家后台去纠错：https://product.tmall.com/product/spu_detail.htm?spu_id=" + productId);
            }
            //天猫系统服务异常
            if (failCause.indexOf("天猫商品服务异常") != -1
                    || failCause.indexOf("访问淘宝超时") != -1) {
                $debug("此处应该是下次启动任务仍需处理的错误--->" + failCause.toString());
                throw new BusinessException(failCause.toString());
            }
            throw new BusinessException(failCause.toString());
        } else {
            numId = updateItemResponse.getUpdateItemResult();
            $debug("numId: " + numId);
            return numId;
        }
    }

    private String getNumIdFromSubMsg(String subMsg, StringBuffer failCause) {
        //pattern: "您已发布过同类宝贝，不允许重复发布；已发布的商品ID列表为：521369504454"
        //pattern: 您已发布过相同类目(腰带/皮带/腰链)，品牌(BCBG)，货号(FLTBC155)的宝贝，不允许重复发布；已发布的商品ID列表为：524127233288,上架的数量必须大于0
        String numId = null;
        Pattern pattern = Pattern.compile("您已发布过.*已发布的商品ID列表为：\\d+");
        Matcher matcher = pattern.matcher(subMsg);
        if (matcher.find()) {
            String matchString = subMsg.substring(matcher.start(), matcher.end());
            Pattern numIdPattern = Pattern.compile("\\d+");
            Matcher numIdMatcher = numIdPattern.matcher(matchString);
            while (numIdMatcher.find()) {
                String matchInter = matchString.substring(numIdMatcher.start(), numIdMatcher.end());
                if (matchInter.length() == 12) {
                    numId = matchInter;
                    break;
                }
            }
            if (numId == null){
                failCause.append(subMsg);
                return null;
            }

            if (matcher.start() != 0) {
                failCause.append(subMsg.substring(0, matcher.start()));
            }

            if (matcher.end() != subMsg.length()) {
                failCause.append(subMsg.substring(matcher.end() + 1));
            }
        } else {
            failCause.append(subMsg);
        }
        return numId;
    }

}
