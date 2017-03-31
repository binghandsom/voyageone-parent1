package com.voyageone.task2.cms.service;

import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import com.taobao.api.response.TmallItemIncrementUpdateSchemaGetResponse;
import com.taobao.api.response.TmallItemSchemaIncrementUpdateResponse;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.factory.SchemaWriter;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.PlatformMappingDeprecatedService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.enums.PlatformWorkloadAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Charis on 2017/3/23.
 */
@SuppressWarnings("ALL")
@Service
public class CmsBuildPlatformAttributeUpdateTmServcie extends BaseCronTaskService{

    // 天猫系cartID
    private final static List<String> cartList = Lists.newArrayList("20","23");
    // 天猫增量更新支持的属性 -> 对应系统任务名(workloadName)
    private final static List<String> IncrementAttrList = Lists.newArrayList(
                        PlatformWorkloadAttribute.SELLER_CIDS, PlatformWorkloadAttribute.ITEM_IMAGES,
                        PlatformWorkloadAttribute.DESCRIPTION, PlatformWorkloadAttribute.TITLE,
                        PlatformWorkloadAttribute.SELL_POINTS, PlatformWorkloadAttribute.WHITE_BG_IMAGE,
                        PlatformWorkloadAttribute.WIRELESS_DESC);

    @Autowired
    private PlatformMappingDeprecatedService platformMappingDeprecatedService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private TbProductService tbProductService;

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformAttributeUpdateTmJob";
    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        //获取Workload列表
        List<CmsBtSxWorkloadModel> groupList = new ArrayList<>();
        // 获取该任务可以运行的销售渠道
        List<String> channels = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdListCartIdList(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channels, cartList);
        if (sxWorkloadModels == null || sxWorkloadModels.size() == 0) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channels, cartList);
            return;
        }

        for(CmsBtSxWorkloadModel workloadModel : groupList) {
            doTmAttibuteUpdate(workloadModel);
        }
    }

    public void doTmAttibuteUpdate(CmsBtSxWorkloadModel work) {
        String channelId = work.getChannelId();
        int cartId = work.getCartId();
        Long groupId = work.getGroupId();
        String workloadName = work.getWorkloadName();
        SxData sxData = null;
        ShopBean shop = new ShopBean();
        // 开始时间
        long prodStartTime = System.currentTimeMillis();
        try {
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);

            if (sxData == null) {
                String errorMsg = String.format("(SxData)信息失败！[sxData=null][workloadId:%s][groupId:%s]:", work.getId(), work.getGroupId());
                $error(errorMsg);
                throw new BusinessException(errorMsg);
            }
            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                String errorMsg = sxData.getErrorMessage();
                // 有错误的时候，直接报错
                throw new BusinessException(errorMsg);
            }
            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            // 主产品取得结果判断
            if (mainProduct == null) {
                String errMsg = String.format("取得主商品信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            //读店铺信息
            shop = Shops.getShop(channelId, cartId);

            if (shop == null) {
                $error("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                throw new Exception(String.format("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId));
            }

            // 取得主产品类目对应的platform mapping数据
            CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel = platformMappingDeprecatedService.getMappingByMainCatId(channelId, cartId, mainProduct.getCommon().getCatId());
            // 表达式解析子
            ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
            CmsBtProductModel_Platform_Cart cartData = mainProduct.getPlatform(Integer.parseInt(shop.getCart_id()));
            String numIId = cartData.getpNumIId();
            if (StringUtils.isEmpty(numIId)) {
                String errorMsg = String.format("取得该平台wareId失败！[ChannelId:%s] [CartId；%s] [GroupId:%s]", channelId, cartId, groupId);
                logger.error(errorMsg);
                throw new BusinessException(errorMsg);
            }
            // 根据任务名称判断要选用天猫的增量还是全量更新
            if (IncrementAttrList.contains(workloadName)) {
                // 增量接口更新
                incrementUpdateItem(sxData, shop, workloadName, numIId, expressionParser, cmsMtPlatformMappingModel);
            } else {
                // 全量接口更新
                allUpdateItem(sxData, shop, workloadName, numIId, expressionParser, cmsMtPlatformMappingModel);
            }

        } catch(Exception ex) {
            // 取得sxData为空
            if (sxData == null) {
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(String.format("取得上新用的商品数据信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId));
            }
            // 回写workload表(失败2)
            sxProductService.updateSxWorkload(work, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
            // 回写详细错误信息表(cms_bt_business_log)
            sxProductService.insertBusinessLog(sxData, getTaskName());
            // 上新出错时状态回写操作
            sxProductService.doUploadFinalProc(shop, false, sxData, work, "", null, "", getTaskName());
            $error(String.format("天猫平台单个产品和商品新增或更新信息异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [耗时:%s]",
                    channelId, cartId, groupId, (System.currentTimeMillis() - prodStartTime)));
            return;
        }
    }

    public void incrementUpdateItem(SxData sxData, ShopBean shop, String workloadName, String numIId,
                                    ExpressionParser expressionParser, CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel) throws Exception{
        // 调用天猫增量更新商品规则API获取当前商品id对应的商品规则
        List<Field> fields = null;
        try {
            fields = getIncrementUpdateSchema(shop, numIId);
        } catch (BusinessException be) {
            sxData.setErrorMessage(be.getMessage());
            throw be;
        } catch (Exception e) {
            String errMsg = String.format("天猫增量更新商品时，调用天猫增量更新商品规则API获取当前商品id对应的商品规则失败! " + e.getMessage());
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }
        if (ListUtils.isNull(fields)) {
            String errMsg = String.format("调用天猫增量更新商品规则API获取当前商品id对应的商品规则失败! [ChannelId:%s] " +
                    "[numIId:%s] [CartId:%s] [workloadName:%s]", shop.getOrder_channel_id(), numIId, shop.getCart_id(), workloadName);
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }
        // 将需要更新的属性设入schema中
        List<Field> newFields = fields.stream().filter(p -> workloadName.equals(p.getId())).collect(Collectors.toList());
        try {
            sxProductService.constructMappingPlatformProps(newFields, cmsMtPlatformMappingModel, shop, expressionParser, getTaskName(), true);
            for (Field tmField : fields) {
                if (workloadName.equals(tmField.getId())) {
                    tmField = newFields.get(0);
                } else {
                    if (tmField.getType().toString().equalsIgnoreCase("input")) {
                        InputField field = (InputField) tmField;
                        field.setValue(field.getDefaultValue());
                    } else if (tmField.getType().toString().equalsIgnoreCase("singleCheck")) {
                        SingleCheckField field = (SingleCheckField) tmField;
                        field.setValue(field.getDefaultValue());
                    } else if (tmField.getType().toString().equalsIgnoreCase("complex")) {
                        ComplexField field = (ComplexField) tmField;
                        field.setComplexValue(field.getDefaultComplexValue());
                    } else if (tmField.getType().toString().equalsIgnoreCase("multiCheck")) {
                        MultiCheckField field = (MultiCheckField) tmField;
                        field.setValues(field.getDefaultValuesDO());
                    } else if (tmField.getType().toString().equalsIgnoreCase("multiIsnput")) {
                        MultiInputField field = (MultiInputField) tmField;
                        field.setValues(field.getDefaultValues());
                    } else if (tmField.getType().toString().equalsIgnoreCase("multiComplex")) {
                        MultiComplexField field = (MultiComplexField) tmField;
                        field.setComplexValues(field.getDefaultComplexValues());
                    }
                }
            }
        } catch (BusinessException be) {
            sxData.setErrorMessage(be.getMessage());
            throw be;
        } catch (Exception e) {
            String errMsg = String.format("天猫增量更新商品时，商品类目设值失败! " + e.getMessage());
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }

        // 调用天猫根据规则增量更新商品
        String result = null;
        try {
            result = updateItemSchemaIncrement(numIId, fields, shop, workloadName);
        } catch (BusinessException be) {
            sxData.setErrorMessage(be.getMessage());
            throw be;
        } catch (Exception e) {
            String errMsg = String.format("调用天猫根据规则增量更新商品失败! " + e.getMessage());
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }
        if (StringUtils.isEmpty(result)) {
            String errMsg = String.format("天猫增量更新商品时失败! [numIId:%s]" + numIId);
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }
        $info("success");

    }

    public List<Field> getIncrementUpdateSchema(ShopBean shop, String numIId) {

        // 获取天猫增量更新商品规则获取商品规则的schema
        String incrementUpdateSchema = null;
        TmallItemIncrementUpdateSchemaGetResponse response;

        String errMsg = "";
        try {
            // 天猫增量更新商品规则获取(tmall.item.increment.update.schema.get)
            response = tbProductService.getItemIncrementUpdateSchema(numIId, null, shop);
            if (response == null) {
                errMsg = String.format("调用天猫增量更新商品规则获取商品规则时，访问淘宝超时(response == null)");
            } else if (!response.isSuccess()
                    || !StringUtils.isEmpty(response.getErrorCode())) {
                errMsg = String.format("调用天猫增量更新商品规则获取商品规则时，出现错误 [ErrorCode:%s] [ErrMsg:%s]",
                        response.getErrorCode()+ " " + response.getSubCode(), response.getMsg() + " " + response.getSubMsg());
            } else {
                incrementUpdateSchema = response.getUpdateItemResult();
            }
            if (!StringUtils.isEmpty(errMsg)) {
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            errMsg = String.format("调用天猫增量更新商品规则获取API时出现异常 [ErrMsg:%s]", e.getMessage());
            $error(errMsg);
            throw new BusinessException(e.getMessage());
        }

        List<Field> fieldList = null;
        if (!StringUtils.isEmpty(incrementUpdateSchema)) {
            try {
                fieldList = SchemaReader.readXmlForList(incrementUpdateSchema);
            } catch (TopSchemaException e) {
                // 解析XML异常
                errMsg = String.format("天猫增量更新产品时,将取得的商品规则Schema xml转换为field列表失败(解析XML异常)！[ChannelId:%s] [CartId:%s] [NumIId:%s]",
                        shop.getOrder_channel_id(), shop.getCart_id(), numIId);
                $error(errMsg);
                e.printStackTrace();
                throw new BusinessException(e.getMessage());
            }
        }

        return fieldList;
    }

    protected String updateItemSchemaIncrement(String numIId, List<Field> itemFields, ShopBean shop,
                                               String workloadName) throws ApiException {
        if (StringUtils.isEmpty(numIId) || ListUtils.isNull(itemFields) || shop == null) return null;

        String xmlData;
        // 设置更新字段列表(除了"透明素材图"之外都要更新)
        // -----------------------------------------------------------
//        <field id="update_fields" name="更新字段列表" type="multiCheck">
//        <rules>
//        <rule name="requiredRule" value="true"/>
//        </rules>
//        <options>
//        <option displayName="商品卖点" value="sell_points"/>
//        <option displayName="商品所属的店铺类目列表" value="seller_cids"/>
//        <option displayName="商品描述" value="description"/>
//        <option displayName="透明素材图" value="white_bg_image"/>
//        <option displayName="商品标题" value="title"/>
//        <option displayName="新商品无线描述" value="wireless_desc"/>
//        </options>
//        <default-values>
//        <default-value>sell_points</default-value>
//        <default-value>seller_cids</default-value>
//        <default-value>description</default-value>
//        <default-value>white_bg_image</default-value>
//        <default-value>title</default-value>
//        <default-value>wireless_desc</default-value>
//        </default-values>
//        </field>
        // -----------------------------------------------------------
        if (itemFields.stream().filter(p -> "update_fields".equalsIgnoreCase(p.getId())).count() > 0) {
            MultiCheckField updateFields = (MultiCheckField)itemFields.stream().filter(p -> "update_fields".equalsIgnoreCase(p.getId())).findFirst().get();
            // 设置更新字段列表(除了"透明素材图"之外都要更新)
            List<Value> updateFieldsValues = new ArrayList<>();
            switch (workloadName) {
                case "sell_points":
                    updateFieldsValues.add(new Value(workloadName)); break;
                case "seller_cids":
                    updateFieldsValues.add(new Value(workloadName)); break;
                case "description":
                    updateFieldsValues.add(new Value(workloadName)); break;
                case "title":
                    updateFieldsValues.add(new Value(workloadName)); break;
                case "wireless_desc":
                    updateFieldsValues.add(new Value(workloadName)); break;

            }
            updateFields.setValues(updateFieldsValues);
        }

        try {
            xmlData = SchemaWriter.writeParamXmlString(itemFields);
        } catch (TopSchemaException e) {
            // 解析XML异常
            String errMsg = String.format("天猫增量更新产品时,将取得的商品规则field列表转换为Schema xml失败(生成XML异常)！[ChannelId:%s] [CartId:%s] [NumIId:%s]",
                    shop.getOrder_channel_id(), shop.getCart_id(), numIId);
            $error(errMsg);
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }

        $debug("numIId:" + numIId);
        $debug("xmlData:" + xmlData);

        String result = null;
        String errMsg = "";

        try {
            // 天猫根据规则增量更新商品(tmall.item.schema.increment.update)
            TmallItemSchemaIncrementUpdateResponse response = tbProductService.updateItemSchemaIncrement(numIId, xmlData, shop);
            if (response == null) {
                errMsg = String.format("调用天猫根据规则增量更新商品时，访问淘宝超时(response == null)");
            } else if (!response.isSuccess()
                    || !StringUtils.isEmpty(response.getErrorCode())) {
                errMsg = String.format("调用天猫根据规则增量更新商品时，出现错误 [ErrorCode:%s] [ErrMsg:%s]",
                        response.getErrorCode()+ " " + response.getSubCode(), response.getMsg() + " " + response.getSubMsg());
            } else {
                // 正常更新根据规则增量更新商品时
                result = response.getUpdateItemResult();
                $debug("result: " + result);
            }

            if (!StringUtils.isEmpty(errMsg)) {
                // 抛出错误消息
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            errMsg = String.format("调用天猫根据规则增量更新商品API时出现异常 [ErrMsg:%s]", e.getMessage());
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        return result;
    }


    public void allUpdateItem(SxData sxData, ShopBean shop, String workloadName, String numIId,
                              ExpressionParser expressionParser, CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel) throws Exception {
        // 获取天猫全量更新商品的规则
        String updateItemRuleSchema = getUpdateSchema(numIId, shop, sxData);
        List<Field> fields;
        try {
            fields = SchemaReader.readXmlForList(updateItemRuleSchema);
        } catch (TopSchemaException e) {
            $error(e.getMessage(), e);
            sxData.setErrorMessage("Can't convert schema to fields: " + e.getMessage());
            throw new BusinessException("Can't convert schema to fields: " + e.getMessage());
        }

        // 将需要更新的属性从规则fields中筛选出来去数据库取值
        List<Field> newFields = fields.stream().filter(p -> workloadName.equals(p.getId())).collect(Collectors.toList());
        try {
            sxProductService.constructMappingPlatformProps(newFields, cmsMtPlatformMappingModel, shop, expressionParser, getTaskName(), true);
            for (Field tmField : fields) {
                if (workloadName.equals(tmField.getId())) {
                    tmField = newFields.get(0);
                } else {
                    if (tmField.getType().toString().equalsIgnoreCase("input")) {
                        InputField field = (InputField) tmField;
                        field.setValue(field.getDefaultValue());
                    } else if (tmField.getType().toString().equalsIgnoreCase("singleCheck")) {
                        SingleCheckField field = (SingleCheckField) tmField;
                        field.setValue(field.getDefaultValue());
                    } else if (tmField.getType().toString().equalsIgnoreCase("complex")) {
                        ComplexField field = (ComplexField) tmField;
                        field.setComplexValue(field.getDefaultComplexValue());
                    } else if (tmField.getType().toString().equalsIgnoreCase("multiCheck")) {
                        MultiCheckField field = (MultiCheckField) tmField;
                        field.setValues(field.getDefaultValuesDO());
                    } else if (tmField.getType().toString().equalsIgnoreCase("multiIsnput")) {
                        MultiInputField field = (MultiInputField) tmField;
                        field.setValues(field.getDefaultValues());
                    } else if (tmField.getType().toString().equalsIgnoreCase("multiComplex")) {
                        MultiComplexField field = (MultiComplexField) tmField;
                        field.setComplexValues(field.getDefaultComplexValues());
                    }
                }
            }
        } catch (BusinessException be) {
            sxData.setErrorMessage(be.getMessage());
            throw be;
        } catch (Exception e) {
            String errMsg = String.format("天猫全量更新商品时，商品类目设值失败! " + e.getMessage());
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }

        for (int retry = 0; retry < 2; retry++) {
            // update
            try {
                $debug("updateTmallItem: [numIId:" + numIId + "]");
                numIId = updateTmallItemSchema(numIId, fields, shop);
            } catch (ApiException e) {
                if (retry == 0 &&
                        (e.getMessage().contains("isv.invalid-permission:add-xinpin") || e.getMessage().contains("isv.invalid-parameter:xinpin") || e.getMessage().contains("isv.invalid-parameter:isXinpin"))) {
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
                } else if (retry == 0 &&
                        e.getMessage().contains("您填写的 颜色分类 中不能包含该货号信息")) {
                    doReSetXml(fields);
                    continue;
                }
                sxData.setErrorMessage(e.getMessage());
                throw new BusinessException(e.getMessage());
            }
            // 如果没有商品是否为新品打标错误时，只需要做一次就跳过循环
            break;
        }
        $info("success");

    }

    public String getUpdateSchema(String numIId, ShopBean shop, SxData sxData) throws Exception{
        // 获取更新商品的规则的schema
        String errMsg = String.format("更新商品schema取得失败!商品numIID: [%s]", numIId);
        String updateItemSchema;
        try {
            TmallItemUpdateSchemaGetResponse updateItemResponse = tbProductService.doGetWareInfoItem(numIId, shop);
            if (updateItemResponse.getErrorCode() != null) {
                logger.error(updateItemResponse.getSubMsg());
                sxData.setErrorMessage(updateItemResponse.getSubMsg());
                throw new BusinessException(updateItemResponse.getSubMsg());
            }
            updateItemSchema = updateItemResponse.getUpdateItemResult();
            if (StringUtils.isEmpty(updateItemSchema)) {
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }
        } catch (TopSchemaException e) {
            $error(e.getMessage(), e);
            sxData.setErrorMessage("Can't convert schema to fields: " + e.getMessage());
            throw new BusinessException("Can't convert schema to fields: " + e.getMessage());
        } catch (ApiException e) {
            sxData.setErrorMessage(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
        return updateItemSchema;
    }


    private String updateTmallItemSchema(String numId, List<Field> itemFields, ShopBean shopBean) throws ApiException {
        String xmlData;

        try {
            xmlData = SchemaWriter.writeParamXmlString(itemFields);
        } catch (TopSchemaException e) {
//            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new BusinessException(e.getMessage());
        }
        StringBuffer failCause = new StringBuffer();
        $debug("numId:" + numId);
        $debug("xmlData:" + xmlData);
        TmallItemSchemaUpdateResponse updateItemResponse = tbProductService.updateItemByNumIId(numId, xmlData, shopBean);
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

    /**
     * 有些商品（注意： 不是有些类目， 而是不确定的随机的商品）， 颜色分类 中不能包含货号信息
     * @param itemFields
     */
    private void doReSetXml(List<Field> itemFields) {
        for (Field field : itemFields) {
            if ("prop_extend_1627207".equals(field.getId())) {
                MultiComplexField multiComplexField = (MultiComplexField)field;
                if (multiComplexField.getComplexValues() != null) {
                    for (ComplexValue v : multiComplexField.getComplexValues()) {
                        String alias_name = v.getInputFieldValue("alias_name");
                        if (!StringUtils.isEmpty(alias_name)) {
                            alias_name = alias_name.substring(0, 1) + "-" + alias_name.substring(1);
                            v.setInputFieldValue("alias_name", alias_name);
                        }
                    }
                }
                break;
            }
        }
    }

}
