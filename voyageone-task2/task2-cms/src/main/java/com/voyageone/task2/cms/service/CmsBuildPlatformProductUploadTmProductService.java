package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.factory.SchemaWriter;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 天猫平台上新用产品相关服务
 *
 * @author desmond on 2016/5/11.
 * @version 2.0.0
 */
@Service
public class CmsBuildPlatformProductUploadTmProductService extends BaseService {

    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private TbProductService tbProductService;

    /**
     * 去天猫(天猫国际)上去寻找是否有存在这个product
     *
     * @param expressionParser ExpressionParser (包含SxData)
     * @param cmsMtPlatformCategorySchemaModel MongoDB  propsProduct取得用
     * @param cmsMtPlatformMappingModel MongoDB 平台CategoryId取得，mapping设定
     * @param shopBean ShopBean 店铺信息
     * @param modifier 更新者
     * @return 返回的天猫platformProductId列表 (如果没有找到, 返回null)
     */
    public List<String> getProductIdFromTmall(ExpressionParser expressionParser, CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel,
                                      CmsMtPlatformMappingModel cmsMtPlatformMappingModel, ShopBean shopBean, String modifier){
        // 产品id列表(返回值)
        List<String> platformProductIdList = new ArrayList<>();
        // 上新数据
        SxData sxData = expressionParser.getSxData();
        // 平台类目id
        Long platformCategoryId = Long.valueOf(cmsMtPlatformMappingModel.getPlatformCategoryId());

        // 获取匹配产品规则
        List<Field> fieldList;
        try {
            // 调用天猫API获取匹配产品规则（tmall.product.match.schema.get）
            String strXml = tbProductService.getProductMatchSchema(platformCategoryId, shopBean);
            $debug("调用天猫API获取的匹配产品规则 strXml:" + strXml);
            // 将取得的响应xml转换为List<Field>
            fieldList = SchemaReader.readXmlForList(strXml);
        } catch (ApiException ex) {
            // 调用API异常
            String errMsg = String.format("天猫获取匹配产品规则失败(调用天猫API异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), platformCategoryId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        } catch (TopSchemaException ex) {
            // 解析XML异常
            String errMsg = String.format("天猫获取匹配产品规则失败(解析XML异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), platformCategoryId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        } catch (Exception ex) {
            // 异常
            String errMsg = String.format("天猫获取匹配产品规则失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), platformCategoryId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        }

        // 根据field列表取得属性值mapping数据
        try {
            // 取得所有field对应的属性值
            // 这里不需要用返回值MAP(返回的不是全部，只是一个基本类型field子集
            // 想要使用包含复杂类型的全体field直接用第一个参数fieldList即可
            sxProductService.constructMappingPlatformProps(fieldList, cmsMtPlatformMappingModel, shopBean, expressionParser, modifier, false);
        } catch (Exception ex) {
            String errMsg = String.format("匹配天猫产品时根据field列表取得属性值mapping数据失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), platformCategoryId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        }

        // 匹配产品
        try {
            // 将取得的属性值List<Field>转换为xml
            String strPropXml = SchemaWriter.writeParamXmlString(fieldList);
            // 调用天猫API匹配product取得platformProductId数组(tmall.product.schema.match）
            String[] strProductList = tbProductService.matchProduct(platformCategoryId, strPropXml, shopBean);
            // 取得返回匹配产品ID
            if (strProductList != null && strProductList.length > 0) {
                platformProductIdList.addAll(Arrays.asList(strProductList));
            }
        } catch (ApiException ex) {
            // 调用API异常
            String errMsg = String.format("没有找到天猫平台匹配的产品(调用天猫API异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), platformCategoryId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        } catch (TopSchemaException ex) {
            // 解析XML异常
            String errMsg = String.format("没有找到天猫平台匹配的产品(解析XML异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), platformCategoryId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        } catch (Exception ex) {
            // 异常
            String errMsg = String.format("没有找到天猫平台匹配的产品！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), platformCategoryId);
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            ex.printStackTrace();
            throw new BusinessException(ex.getMessage());
        }

        return platformProductIdList;
    }

    /**
     * 在天猫平台上传商品时检查产品状态(不存在，等待审核，审核完毕)
     * 对所有search product到的产品进行遍历
     *  1. 如果发现某一个产品的产品状态是审核完毕，那么跳出循环,返回该可上传商品的平台产品id。
     *  2. 如果发现某一个产品的产品状态是未审核，那么继续循环
     *  3. 如果发现产品不存在，那么也继续循环
     *  4. 循环结束，如果有未审核的产品，那么抛出业务异常，任务结束，结束原因为：需要等待产品审核
     *  5. 循环结束，如果没有未审核的产品，返回null,后面会进入上传产品状态
     *
     * @param sxData SxData 上新数据
     * @param platformProductIdList List<String> 平台platformProductId列表
     * @param shopBean ShopBean 店铺信息
     * @return 返回的可以上传商品的platformProductId()
     */
    public String getUsefulProductId(SxData sxData, List<String> platformProductIdList, ShopBean shopBean) {
        String platformProductId = null;
        boolean wait_for_check = false; //产品是否在等待天猫审核

        for (String pid : platformProductIdList)
        {
            // 查询产品状态
            String product_status;
            try {
                product_status = getProductStatus(Long.parseLong(pid), shopBean);
            } catch (Exception ex) {
                String errMsg = String.format("调用天猫API查询产品状态失败！[PlatformProductId:%s]", pid);
                $error(errMsg);
                ex.printStackTrace();
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(ex.getMessage());
            }
            // 可以发布商品
            if ("true".equalsIgnoreCase(product_status)) {
                // 找到一个可以发布商品的产品id就退出循环
                platformProductId = pid;
                wait_for_check = false;
                break;
            }  // 没有款号
            else if (product_status == null || "".equals(product_status)){
                // 如果在循环中已经发现有产品存在但未审核，那么不会覆盖productId为null，
                // 否则循环结束后，会被认为没有找到产品，从而重新上传产品
                // 也就是说，天猫审核中的不重新上传产品
                if (wait_for_check)
                    continue;
                else
                    platformProductId = null;
            } // 等待天猫审核中
            else {
                platformProductId = pid;
                wait_for_check = true;
            }
        }

        if (wait_for_check) {
            // 如果循环完所有的平台产品id列表，没有可用的，只有天猫审核的产品，则抛出异常
            String errMsg = String.format("发现已有产品符合我们要上传的商品，但需要等待天猫审核该产品.  [PlatformProductId:%s]", platformProductId);
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }

        return  platformProductId;
    }

    private String getProductStatus(Long product_id, ShopBean shopBean) throws ApiException, TopSchemaException {

        // 调用天猫API获取产品信息获取schema(tmall.product.schema.get )
        String schema = tbProductService.getProductSchema(product_id, shopBean);
        List<Field> fields = SchemaReader.readXmlForList(schema);

        for (Field field : fields)
        {
            if (field instanceof InputField && "can_publish_item".equals(field.getId())) {
                String productStatus = ((InputField) field).getDefaultValue();
                $debug("product_id:" + product_id + ", productStatus:" + productStatus);
                return productStatus;
            }
        }

        return null;
    }

    /**
     * 上传产品到天猫(天猫国际)平台
     * 1. 从Mango数据库中查询所有产品的字段
     * 2. 对所有产品字段进行mapping填值
     * 3. 调用Tmall API上传产品，如果上传成功，进入上传商品状态
     *
     * @param expressionParser ExpressionParser (包含SxData)
     * @param cmsMtPlatformCategorySchemaModel MongoDB  propsProduct取得用
     * @param cmsMtPlatformMappingModel MongoDB 平台CategoryId取得，mapping设定
     * @param shopBean ShopBean 店铺信息
     * @param modifier 更新者
     * @return 返回的产品上传成功的天猫productId
     */
    public String uploadProduct(ExpressionParser expressionParser, CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel,
                                CmsMtPlatformMappingModel cmsMtPlatformMappingModel, ShopBean shopBean, String modifier) {
        // 上传成功返回的产品id(返回值)
        String platformProductId = "";
        // 上新数据
        SxData sxData = expressionParser.getSxData();
        // 平台类目id
        Long platformCategoryId = Long.valueOf(cmsMtPlatformMappingModel.getPlatformCategoryId());
        // 品牌code
        Long brandCode = Long.valueOf(sxData.getBrandCode());
        // 产品schema
        String productSchema = cmsMtPlatformCategorySchemaModel.getPropsProduct();
        // 匹配之后的XML格式数据
        String xmlData;

        List<Field> fieldList;
        try {
            // 调用天猫API获取产品发布规则（tmall.product.add.schema.get）
//            String strXml = tbProductService.getAddProductSchema(platformCategoryId, brandCode, shopBean);
//            $debug("调用天猫API获取的产品发布规则 strXml:" + strXml);
            $debug("productSchema:" + productSchema);
            // 将取得的产品Schema xml转换为List<Field>
            fieldList = SchemaReader.readXmlForList(productSchema);
        } catch (TopSchemaException ex) {
            // 解析XML异常
            String errMsg = String.format("将取得的产品Schema xml转换为field列表失败(解析XML异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), platformCategoryId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        }

        // 根据field列表取得属性值mapping数据
        try {
            // 取得所有field对应的属性值
            sxProductService.constructMappingPlatformProps(fieldList, cmsMtPlatformMappingModel, shopBean, expressionParser, modifier, false);
        } catch (Exception ex) {
            String errMsg = String.format("天猫新增产品时根据field列表取得属性值mapping数据失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), platformCategoryId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        }

        StringBuffer failCause = new StringBuffer();
        String addProductResult = null;
        try {
            // 将取得所有field对应的属性值的列表转成xml字符串
            xmlData = SchemaWriter.writeParamXmlString(fieldList);
            $debug("调用天猫API上传产品 xmlData:" + xmlData);
            // 调用天猫API使用Schema文件发布一个产品(tmall.product.schema.add)
            addProductResult = tbProductService.addProduct(platformCategoryId, brandCode, xmlData, shopBean, failCause);
        } catch (TopSchemaException ex) {
            // 解析XML异常
            String errMsg = String.format("将取得所有field对应的属性值的列表转成xml字符串失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), platformCategoryId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        } catch (ApiException ex) {
            // 调用API异常
            String errMsg = String.format("调用天猫API上传产品失败(调用天猫API异常)！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s] [BrandCode:%s] [FailCause:%s]",
                    sxData.getChannelId(), sxData.getCartId(), platformCategoryId, brandCode, failCause.toString());
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        } catch (Exception ex) {
            // 异常
            String errMsg = String.format("调用天猫API上传产品失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s] [BrandCode:%s] [FailCause:%s]",
                    sxData.getChannelId(), sxData.getCartId(), platformCategoryId, brandCode, failCause.toString());
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        }

        // 成功，则更新状态
        if (!StringUtils.isEmpty(addProductResult)) {
            // 将返回值xml字符串转换成field列表
            List<Field> result_fields = SchemaReader.readXmlForList(addProductResult);
            for (Field field : result_fields) {
                // 在返回值xml中取得productId
                if ("product_id".equals(field.getId()) && FieldTypeEnum.INPUT.equals(field.getType())) {
                    platformProductId = ((InputField)field).getValue();
                    $debug("调用天猫API上传产品返回的platformProductId:" + platformProductId);
                    break;
                }
            }
        } else {
            // 返回值异常
            String errMsg = String.format("调用天猫API上传产品失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s] [BrandCode:%s] [FailCause:%s]",
                    sxData.getChannelId(), sxData.getCartId(), platformCategoryId, brandCode, failCause.toString());
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }

        return platformProductId;
    }

    /**
     * 判断商品是否是达尔文体系
     *
     * @param sxData SxData 上新数据
     * @param shopBean ShopBean 店铺
     * @param platformCategoryId String 平台类目id
     * @param brandCode long 品牌
     * @return 返回的商品是否属于达尔文体系
     */
    public boolean getIsDarwin(SxData sxData, ShopBean shopBean, String platformCategoryId, String brandCode) {
        //判断商品是否是达尔文体系
        StringBuffer failCause = new StringBuffer();
        Boolean isDarwin = false;
        String strPlatformCategoryId = (platformCategoryId == null) ? "0" : platformCategoryId;
        String strBrandCode = (brandCode == null) ? "0" : brandCode;

        try {
            // 判断是否是达尔文（brandCode没有用到）
            isDarwin = tbProductService.isDarwin(Long.parseLong(strPlatformCategoryId), Long.parseLong(strBrandCode), shopBean, failCause);
            if (isDarwin == null && failCause.length() != 0) {
                if (failCause.indexOf("访问淘宝超时") == -1) {
                    String errMsg = String.format("判断商品是否是达尔文体系失败(访问淘宝超时)！[PlatformCategoryId:%s] [BrandCode:%s] [FailCause:%s]",
                            platformCategoryId, brandCode, failCause.toString());
                    $error(errMsg);
                    sxData.setErrorMessage(errMsg);
                    throw new BusinessException(errMsg);
                }
            }
        } catch (ApiException ex) {
            // 调用API异常
            String errMsg = String.format("调用天猫API判断商品是否是达尔文体系失败(调用天猫API异常)！[PlatformCategoryId:%s] [BrandCode:%s] [FailCause:%s]",
                    platformCategoryId, brandCode, failCause.toString());
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        } catch (Exception ex) {
            // 异常
            String errMsg = String.format("调用天猫API判断商品是否是达尔文体系失败！[PlatformCategoryId:%s] [BrandCode:%s] [FailCause:%s]",
                    platformCategoryId, brandCode, failCause.toString());
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        }

        return isDarwin;
    }

}
