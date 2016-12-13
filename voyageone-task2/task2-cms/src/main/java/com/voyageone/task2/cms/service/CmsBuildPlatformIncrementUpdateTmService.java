package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.taobao.api.response.TmallItemIncrementUpdateSchemaGetResponse;
import com.taobao.api.response.TmallItemSchemaIncrementUpdateResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.factory.SchemaWriter;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.PlatformMappingDeprecatedService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 天猫根据规则增量更新商品服务
 * 只做天猫平台增量更新更新操作，不做新增操作；如果该产品本身没有上新到天猫平台就什么就不会做
 *
 * @author desmond on 2016/10/31.
 * @version 2.8.0
 */
@Service
public class CmsBuildPlatformIncrementUpdateTmService extends BaseCronTaskService {

    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private TbProductService tbProductService;
    @Autowired
    private PlatformMappingDeprecatedService platformMappingDeprecatedService;
    @Autowired
    private ProductGroupService productGroupService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformIncrementUpdateTmJob";
    }

    /**
     * 天猫平台增量更新商品处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

    }

    /**
     * 天猫根据规则增量更新产品上新处理
     *
     * @param productCode 产品code
     * @param channelId   渠道id
     * @param cartId      平台id
     */
    public String doIncrementUpdateProduct(String productCode, String channelId, int cartId) {
        // 当前groupid(用于取得产品信息)
        Long groupId = null;
        // 商品id
        String numIId = "";
        // 表达式解析子
        ExpressionParser expressionParser = null;
        // 上新数据
        SxData sxData = null;
        // 平台Mapping信息
        CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel;;

        // 天猫商品增量更新处理
        try {
            // 获取店铺信息
            ShopBean shopProp = Shops.getShop(channelId, cartId);
            if (shopProp == null) {
                String errMsg = String.format("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                $error(errMsg);
                return errMsg;
            }

            // 根据传入的productCode取得groupId
            CmsBtProductGroupModel cartGroupModel = productGroupService.selectProductGroupByCode(channelId, productCode, cartId);
            if (cartGroupModel == null) {
                String errMsg = String.format("根据产品code取得Group信息失败！[ChannelId:%s] [ProductCode:%s] [CartId:%s]", channelId, groupId, cartId);
                $error(errMsg);
                return errMsg;
            }
            // 从group信息中取得商品id
            numIId = cartGroupModel.getNumIId();
            if (StringUtils.isEmpty(numIId)) {
                String errMsg = String.format("天猫根据规则增量更新商品服务只支持更新上新过的产品，当前对象产品code对应的Group信息" +
                        "没找到天猫平台商品id(numIId=空),请上新成功之后再使用增量更新. [ChannelId:%s] [ProductCode:%s] [CartId:%s]", channelId, groupId, cartId);
                $error(errMsg);
                return errMsg;
            }

            // 取得当前产品的groupId
            groupId = cartGroupModel.getGroupId();
            // 上新用的商品数据信息取得
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId, true);
            if (sxData == null) {
                // 异常的时候去做这段逻辑
                throw new BusinessException("SxData取得失败!");
            }
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                // 取得上新数据出错时，cartId有可能没有设置
                sxData.setCartId(cartId);
                // 有错误的时候，直接报错
                throw new BusinessException(sxData.getErrorMessage());
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

            // 属性值准备
            // 取得主产品类目对应的platform mapping数据
            cmsMtPlatformMappingModel = platformMappingDeprecatedService.getMappingByMainCatId(channelId, cartId, mainProduct.getCommon().getCatId());

            // 调用天猫增量更新商品规则API获取当前商品id对应的商品规则
            List<Field> fields = null;
            try {
                fields = getIncrementUpdateSchema(shopProp, numIId);
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
                        "[ProductCode:%s] [CartId:%s] [NumIId:%s]", channelId, groupId, cartId, numIId);
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            // 表达式解析子
            expressionParser = new ExpressionParser(sxProductService, sxData);

            try {
                sxProductService.constructMappingPlatformProps(fields, cmsMtPlatformMappingModel, shopProp, expressionParser, getTaskName(), true);
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
                result = updateItemSchemaIncrement(numIId, fields, shopProp);
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


        } catch (Exception ex) {
            // 取得sxData为空
            if (sxData == null) {
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(String.format("天猫增量更新商品时,取得上新用的商品数据信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId));
            }

            // 上传产品失败，后面商品也不用上传，直接回写workload表   (失败2)
            String errMsg = String.format("天猫增量更新商品时异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [%s]",
                    channelId, cartId, groupId, ex.getMessage());
            $error(errMsg);
            ex.printStackTrace();
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setErrorMessage(errMsg);
            }
            return sxData.getErrorMessage();
        }

        // 正常结束
        $info(String.format("天猫单个商品增量更新成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [ProductCode:%s] [itemId:%s]",
                channelId, cartId, groupId, productCode, numIId));
        return null; // 正常结束返回null
    }

    /**
     * 调用天猫增量更新商品规则获取商品规则
     *
     * @param shop     店铺信息
     * @param numIId   商品id
     */
    protected List<Field> getIncrementUpdateSchema(ShopBean shop, String numIId) {

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
                // 正常取得天猫增量更新商品规则获取商品规则时
                incrementUpdateSchema = response.getUpdateItemResult();
            }

            if (!StringUtils.isEmpty(errMsg)) {
                // 抛出错误消息
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

    /**
     * 调用天猫根据规则增量更新商品
     *
     * @param shop     店铺信息
     * @param numIId   商品id
     */
    protected String updateItemSchemaIncrement(String numIId, List<Field> itemFields, ShopBean shop) throws ApiException {
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
            updateFieldsValues.add(new Value("sell_points"));   // 商品卖点
            updateFieldsValues.add(new Value("seller_cids"));   // 商品所属的店铺类目列表
            updateFieldsValues.add(new Value("description"));   // 商品描述
            updateFieldsValues.add(new Value("title"));         // 商品标题
            updateFieldsValues.add(new Value("wireless_desc")); // 新商品无线描述
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

}
