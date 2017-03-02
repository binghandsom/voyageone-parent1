package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.factory.SchemaWriter;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.rule.Rule;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.bean.cms.product.SxData.SxDarwinSkuProps;
import com.voyageone.service.dao.cms.CmsMtPlatformPropMappingCustomDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsMtPlatformPropMappingCustomModel;
import com.voyageone.service.model.cms.enums.CustomMappingType;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    private CmsMtPlatformPropMappingCustomDao cmsMtPlatformPropMappingCustomDao;

    private static final String CSPU_FIELD_ID_MATCH = "cspu_list";

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
                                              CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel, ShopBean shopBean, String modifier){
        // 产品id列表(返回值)
        List<String> platformProductIdList = new ArrayList<>();
        // 上新数据
        SxData sxData = expressionParser.getSxData();
        // 平台类目id
//        Long platformCategoryId = Long.valueOf(cmsMtPlatformMappingModel.getPlatformCategoryId());
        Long platformCategoryId = Long.parseLong(sxData.getMainProduct().getPlatform(sxData.getCartId()).getpCatId());

        // 获取匹配产品规则
        List<Field> fieldList;
        try {
            // 调用天猫API获取匹配产品规则（tmall.product.match.schema.get）
            String strXml = tbProductService.getProductMatchSchema(platformCategoryId, shopBean);
            $debug("调用天猫API获取的匹配产品规则 strXml:" + strXml);
            // added by morse.lu 2016/06/06 start
            if (StringUtils.isEmpty(strXml)) {
                // ★★★只有Schema取不到才可以返回null，用于外部判断是不是Schema取不到★★★
                return null;
            }
            // added by morse.lu 2016/06/06 end
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
        } catch (BusinessException ex) {
            sxData.setErrorMessage(ex.getMessage());
            throw ex;
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
        String platformErrorMsg = null; // 平台审核失败的信息

        // added by morse.lu 2016/08/08 start
        // barCode对应的是否更新
        Map<String, SxDarwinSkuProps> mapBarcodeProps = new HashMap<>();
        Map<String, SxDarwinSkuProps> mapDarwinSkuProps = sxData.getMapDarwinSkuProps();
        if (mapDarwinSkuProps != null) {
            mapDarwinSkuProps.forEach((sku, props)-> mapBarcodeProps.put(props.getBarcode(), props));
        }
        // added by morse.lu 2016/08/08 end

        for (String pid : platformProductIdList)
        {
            // 查询产品状态
            String[] product_status;
            try {
                // modified by morse.lu 2016/08/08 start
//                product_status = getProductStatus(Long.parseLong(pid), shopBean);
                product_status = getProductStatus(Long.parseLong(pid), shopBean, mapBarcodeProps);
                // modified by morse.lu 2016/08/08 end
            } catch (Exception ex) {
                String errMsg = String.format("调用天猫API查询产品状态失败！[PlatformProductId:%s]", pid);
                $error(errMsg);
                ex.printStackTrace();
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(ex.getMessage());
            }
            // 可以发布商品
            if ("true".equalsIgnoreCase(product_status[0])) {
                // 找到一个可以发布商品的产品id就退出循环
                platformProductId = pid;
                wait_for_check = false;
                platformErrorMsg = product_status[1]; // 可能部分规格错误
                break;
            }  // 没有款号
            // modified by morse.lu 2016/08/04 start
            // 这段逻辑改一下，感觉有点问题，但具体状态是什么样不确定，以后可能还会改
//            else if (product_status == null || "".equals(product_status)){
//                // 如果在循环中已经发现有产品存在但未审核，那么不会覆盖productId为null，
//                // 否则循环结束后，会被认为没有找到产品，从而重新上传产品
//                // 也就是说，天猫审核中的不重新上传产品
//                if (wait_for_check)
//                    continue;
//                else
//                    platformProductId = null;
//            } // 等待天猫审核中
//            else {
//                platformProductId = pid;
//                wait_for_check = true;
//            }
            else if ("false".equalsIgnoreCase(product_status[0])) {
                // 审核失败 ?
                if (!wait_for_check) {
                    platformProductId = pid;
                    if (StringUtils.isEmpty(product_status[1])) {
                        product_status[1] = "错误原因取得失败!";
                    }
                    platformErrorMsg = product_status[1];
                }
            } else {
                // 审核中或未审核?
                platformProductId = pid;
                wait_for_check = true;
                platformErrorMsg = null;
            }
            // modified by morse.lu 2016/08/04 end
        }

        if (wait_for_check) {
            // 如果循环完所有的平台产品id列表，没有可用的，只有天猫审核的产品，则抛出异常
            String errMsg = String.format("发现已有产品符合我们要上传的商品，但需要等待天猫审核该产品.  [PlatformProductId:%s]", platformProductId);
//            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }

        if (!StringUtils.isEmpty(platformErrorMsg)) {
            // 审核失败
            String errMsg = String.format("产品[PlatformProductId:%s]审核失败!错误原因是:%s", platformProductId, platformErrorMsg);
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(errMsg);
        }

        return  platformProductId;
    }

    private String[] getProductStatus(Long product_id, ShopBean shopBean, Map<String, SxDarwinSkuProps> mapBarcodeProps) throws ApiException, TopSchemaException {

        // added by morse.lu 2016/08/08 start
        String[] status = new String[]{"", ""}; // status[0] 状态 status[1] error的场合error信息
        String[] errorKeys = {"不完整", "不规范", "不正确", "错误", "需", "未", "不一致", "缺少", "审核中", "待审核", "不清"}; // 以后视情况追加修正
        List<String> listBarcode = new ArrayList<>();
        // added by morse.lu 2016/08/08 end
        // 调用天猫API获取产品信息获取schema(tmall.product.schema.get )
        String schema = tbProductService.getProductSchema(product_id, shopBean);
        List<Field> fields = SchemaReader.readXmlForList(schema);
        boolean hasAllowUpdate = false;

        for (Field field : fields)
        {
            if (field instanceof InputField && "can_publish_item".equals(field.getId())) {
                String productStatus = ((InputField) field).getDefaultValue();
                $debug("product_id:" + product_id + ", productStatus:" + productStatus);
                status[0] = productStatus;
            }
            // added by morse.lu 2016/08/08 start
            if (CSPU_FIELD_ID_MATCH.equals(field.getId())) {
                // 产品规格
                MultiComplexField cspuListField = (MultiComplexField) field;

                // 20170124 过滤重复删除的规格的处理 tom START
                // key【条形码】； value【状态 + 半角冒号 + 规格编号】
                Map<String, String> mapTempCspu = new HashMap<>();
                // 预处理
                for (Field subField : cspuListField.getFields()) {
                    if (subField.getType() != FieldTypeEnum.COMPLEX) {
                        continue;
                    }
                    ComplexField cspuField = (ComplexField) subField;
                    Map<String, Field> mapFields = cspuField.getFieldMap();

                    // 规格编号
                    String tmpId = ((InputField)mapFields.get("id")).getDefaultValue();
                    // 状态
                    String tmpStatus = ((InputField)mapFields.get("status")).getDefaultValue();
                    // 条形码
                    String tmpBarcode = ((InputField)mapFields.get("barcode")).getDefaultValue().split(":")[1];

                    // 判断条码是否已经存在
                    if (mapTempCspu.containsKey(tmpBarcode)) {
                        // 判断当前的规格的状态， 如果是【被删除】以外的场合， 说明当前的规格才是对的规格
                        if (!"被删除".equals(tmpStatus)) {
                            mapTempCspu.put(tmpBarcode, tmpStatus + ":" + tmpId);
                        }
                    } else {
                        // 如果之前还没添加过， 那就添加一下
                        mapTempCspu.put(tmpBarcode, tmpStatus + ":" + tmpId);
                    }
                }
                // 20170124 过滤重复删除的规格的处理 tom END

                for (Field subField : cspuListField.getFields()) {
                    if (subField.getType() != FieldTypeEnum.COMPLEX) {
                        continue;
                    }
                    ComplexField cspuField = (ComplexField) subField;
                    Map<String, Field> mapFields = cspuField.getFieldMap();

                    // 20170124 过滤重复删除的规格的处理 tom START
                    // 规格编号
                    String tmpId = ((InputField)mapFields.get("id")).getDefaultValue();
                    // 状态
                    String tmpStatus = ((InputField)mapFields.get("status")).getDefaultValue();
                    // 条形码
                    String tmpBarcode = ((InputField)mapFields.get("barcode")).getDefaultValue().split(":")[1];

                    // 判断当前商品是否是被删除的商品
                    if ("被删除".equals(tmpStatus)) {
                        // 根据条形码， 找到预处理mapTempCspu里的那条记录
                        String tmpMapVal = mapTempCspu.get(tmpBarcode);
                        String[] split = tmpMapVal.split(":"); // 【状态 + 半角冒号 + 规格编号】
                        if (!"被删除".equals(split[0])) { // 看看当前的barcode有正常的记录
                            if (!tmpId.equals(split[1])) { // 看看正常的记录是不是自己， 如果不是自己就跳过
                                // 跳过
                                continue;
                            }
                        }
                    }
                    // 20170124 过滤重复删除的规格的处理 tom END

                    String[] barcodeXml = ((InputField) mapFields.get("barcode")).getDefaultValue().split(":"); // 3:090891203253  1:3607342551800 冒号前面不知道是什么
                    if (barcodeXml.length != 2) {
                        throw new BusinessException("天猫条形码属性值保存方式变更!要修正代码重新解析!");
                    }

                    String barCode = barcodeXml[1];
                    // 20170124 过滤重复删除的规格的处理 tom START
//                    if (listBarcode.contains(barCode)) {
//                        throw new BusinessException(String.format("本产品的规格,天猫条形码[%s]有重复!请先在天猫后台修正正确的条形码,再Approve!", barCode));
//                    }
//                    listBarcode.add(barCode);

                    // 上面这几行， 改一下逻辑， 即使有重复的， 也要把所有的重复的错误信息显示出来， 因为不知道哪个是最新的
                    // 当时如果相同的barcode里， 有非错误的（"被删除"以外的状态）的话， 那就不会走到这里的
                    if (!listBarcode.contains(barCode)) {
                        listBarcode.add(barCode);
                    }
                    // 20170124 过滤重复删除的规格的处理 tom END

                    SxDarwinSkuProps sxDarwinSkuProps = mapBarcodeProps.get(barCode);
                    if (sxDarwinSkuProps != null) {
                        // 是这次要上的barcode
                        sxDarwinSkuProps.setCspuId(((InputField) mapFields.get("id")).getDefaultValue()); // 规格编号
                        // deleted by morse.lu 2016/10/27 start
                        // 即使允许更新也要判断先原先是否有错(sxDarwinSkuProps.setErr(true)要执行)，为了之后代码来判断
//                        if (!sxDarwinSkuProps.isAllowUpdate()) {
//                            // 不允许更新，那么有错，就需要把错报出来
                        // deleted by morse.lu 2016/10/27 end
                            if (cspuField.getRules() != null) {
                                boolean isFirstErr = true;
                                for (Rule rule : cspuField.getRules()) {
                                    if ("tipRule".equals(rule.getName())) {
                                        String info = StringUtils.null2Space2(rule.getValue());
                                        boolean isErrorMsg = false;
                                        for (String errorKey : errorKeys) {
                                            if (info.indexOf(errorKey) >= 0) {
                                                isErrorMsg = true;
                                                break;
                                            }
                                        }

                                        if (isErrorMsg) {
                                            if (isFirstErr) {
                                                sxDarwinSkuProps.setErr(true);
                                                isFirstErr = false;
                                                status[1] = status[1] + "条形码[" + barCode + "]:";
                                            }
                                            status[1] = status[1] + info + ";";
                                        }
                                    }
                                }
                            }
                            // added by morse.lu 2016/10/27 start
                        if (sxDarwinSkuProps.isAllowUpdate()) {
                            // 允许更新
                            hasAllowUpdate = true;
                            // added by morse.lu 2016/10/27 start
                        }
                    }
                }
            }
            // added by morse.lu 2016/08/08 end
        }

        // added by morse.lu 2016/10/27 start
        if (hasAllowUpdate) {
            // 允许更新话，说明IT审核过错误已经修正了，在允许更新表里添加了，那么直接把状态改成true
            status[0] = "true";
            status[1] = "";
        }
        // added by morse.lu 2016/10/27 start

        return status;
    }

    /**
     * 上传产品到天猫(天猫国际)平台（增加）
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
    public String addTmallProduct(ExpressionParser expressionParser, CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel,
                                  CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel, ShopBean shopBean, String modifier) {
        // 上传成功返回的产品id(返回值)
        String platformProductId = "";
        // 上新数据
        SxData sxData = expressionParser.getSxData();
        // 平台类目id
//        Long platformCategoryId = Long.valueOf(cmsMtPlatformMappingModel.getPlatformCategoryId());
        Long platformCategoryId = Long.parseLong(sxData.getMainProduct().getPlatform(sxData.getCartId()).getpCatId());
        // 品牌code
        Long brandCode = Long.valueOf(sxData.getBrandCode());
        // 产品schema
        String productSchema = cmsMtPlatformCategorySchemaModel.getPropsProduct();
        // 匹配之后的XML格式数据
        String xmlData;

        List<Field> fieldList;
        try {
            try {
                // 调用天猫API获取产品发布规则（tmall.product.add.schema.get）
                String strXml = tbProductService.getAddProductSchema(platformCategoryId, brandCode, shopBean);
                if (!StringUtils.isEmpty(strXml)) {
                    productSchema = strXml;
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
//            $debug("调用天猫API获取的产品发布规则 strXml:" + strXml);
            $debug("productSchema:" + productSchema);
            // added by morse.lu 2016/06/06 start
            if (StringUtils.isEmpty(productSchema)) {
                // ★★★只有Schema取不到才可以返回null，可用于外部判断是不是Schema取不到★★★
                return null;
            }
            // added by morse.lu 2016/06/06 end
            // 将取得的产品Schema xml转换为List<Field>
            fieldList = SchemaReader.readXmlForList(productSchema);
        } catch (TopSchemaException ex) {
            // 解析XML异常
            String errMsg = String.format("新增产品时,将取得的产品Schema xml转换为field列表失败(解析XML异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
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
        } catch (BusinessException be) {
            sxData.setErrorMessage(be.getMessage());
            throw be;
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
            $debug("调用天猫API新增产品 xmlData:" + xmlData);
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
     * 上传产品到天猫(天猫国际)平台（更新）
     * sxData.isUpdateProductFlg() == true 才会调用此方法
     * 达尔文是特例，因为允许不更新产品信息，但是更新规格
     *
     * @param expressionParser ExpressionParser (包含SxData)
     * @param platformProductId  天猫productId
     * @param cmsMtPlatformMappingModel MongoDB 平台CategoryId取得，mapping设定
     * @param shopBean ShopBean 店铺信息
     * @param modifier 更新者
     */
    public void updateTmallProduct(ExpressionParser expressionParser, String platformProductId, CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel, ShopBean shopBean, String modifier) {
        // 上新数据
        SxData sxData = expressionParser.getSxData();
        StringBuffer failCause = new StringBuffer();

        // 获取更新产品的规则的schema
        String updateProductSchema;
        try {
            updateProductSchema = tbProductService.getProductUpdateSchema(Long.parseLong(platformProductId), shopBean, failCause);
            if (StringUtils.isEmpty(updateProductSchema)) {
                sxData.setErrorMessage(failCause.toString());
                throw new BusinessException(failCause.toString());
            }
        } catch (ApiException e) {
            sxData.setErrorMessage(e.getMessage());
            throw new BusinessException(e.getMessage());
        }

        List<Field> fieldList;
        try {
            fieldList = SchemaReader.readXmlForList(updateProductSchema);
        } catch (TopSchemaException e) {
            // 解析XML异常
            String errMsg = String.format("更新产品时,将取得的产品Schema xml转换为field列表失败(解析XML异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), platformProductId);
            $error(errMsg);
            e.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(e.getMessage());
        }

        // added by morse.lu 2016/08/09 start
        List<Field> cspuListField = new ArrayList<>();
        if (sxData.isDarwin()) {
            // 达尔文
            Map<String, Object> searchParam = new HashMap<>();
            searchParam.put("cartId", sxData.getCartId());
            searchParam.put("mapping_type", CustomMappingType.CSPU.value());
            List<CmsMtPlatformPropMappingCustomModel> cmsMtPlatformPropMappingCustomModels = cmsMtPlatformPropMappingCustomDao.selectList(searchParam);
            List<String> listCspuFieldId = cmsMtPlatformPropMappingCustomModels.stream().map(CmsMtPlatformPropMappingCustomModel::getPlatformPropId).collect(Collectors.toList());
            for (Field field : fieldList) {
                if (listCspuFieldId.contains(field.getId())) {
                    // 产品规格
                    cspuListField.add(field);
                } else{
                    if (!sxData.isUpdateProductFlg()) {
                        // 不更新产品，即只更新产品规则(达尔文特有，其余的产品不更新的话，不会调用本方法)
                        // 把所有天猫取得的值(defaultValue)拷进value
                        try {
                            setFieldDefaultValues(field);
                        } catch (Exception e) {
                            String errMsg = StringUtils.format("达尔文产品属性值取得失败!field_id[%s],fieldName[%s]", field.getId(), field.getName());
                            sxData.setErrorMessage(errMsg);
                            throw new BusinessException(errMsg);
                        }
                    }
                }
            }
        }
        // added by morse.lu 2016/08/09 end

        // 根据field列表取得属性值mapping数据
        try {
            // 取得所有field对应的属性值
            if (sxData.isUpdateProductFlg()) {
                sxProductService.constructMappingPlatformProps(fieldList, cmsMtPlatformMappingModel, shopBean, expressionParser, modifier, false);
            } else {
                // 不更新产品只更新产品规则的话，属性值直接从天猫取得(达尔文特有，其余的产品不更新的话，不会调用本方法)
                // 只把产品规格的field传进去设值
                sxProductService.constructMappingPlatformProps(cspuListField, cmsMtPlatformMappingModel, shopBean, expressionParser, modifier, false);
            }
        } catch (BusinessException be) {
            sxData.setErrorMessage(be.getMessage());
            throw be;
        } catch (Exception ex) {
            String errMsg = String.format("更新产品时,根据field列表取得属性值mapping数据失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), platformProductId);
            $error(errMsg);
            ex.printStackTrace();
            sxData.setErrorMessage(errMsg);
            throw new BusinessException(ex.getMessage());
        }

        // 更新产品
        try {
            // 将取得所有field对应的属性值的列表转成xml字符串
            String xmlData = SchemaWriter.writeParamXmlString(fieldList);
            $debug("调用天猫API更新产品 xmlData:" + xmlData);

            failCause.setLength(0);
            String result = tbProductService.updateProduct(Long.parseLong(platformProductId), xmlData, shopBean, failCause);
            if (StringUtils.isEmpty(result)) {
                sxData.setErrorMessage("请登录后台检查产品信息：https://product.tmall.com/product/spu_detail.htm?spu_id=" + platformProductId + "， 错误信息是：" + failCause.toString());
                throw new BusinessException(failCause.toString());
            }
        } catch (TopSchemaException | ApiException e) {
            sxData.setErrorMessage(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
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

    /**
     * 达尔文产品上新
     *
     * @param expressionParser ExpressionParser (包含SxData)
     * @param cmsMtPlatformCategorySchemaModel MongoDB  propsProduct取得用
     * @param dbProductId group表里平台产品id
     * @param shopBean ShopBean 店铺信息
     * @param modifier 更新者
     * @return 返回的产品能否上新商品了
     */
    public boolean sxDarwinProduct(ExpressionParser expressionParser, CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel, String dbProductId, ShopBean shopBean, String modifier) {
        boolean canSxDarwinItem = false; // 没有抛错(包括审核状态检查)，并且产品(或规格)没有追加或更新(这样会进入审核,无法上新商品)，才会返回true
        SxData sxData = expressionParser.getSxData();
        // 产品schema
        String productSchema = cmsMtPlatformCategorySchemaModel.getPropsProduct();
//        // 画面上的产品规格
//        List<Map<String, Object>> masterWordEvaluationContexts = (List<Map<String, Object>>) sxData.getMainProduct().getPlatform(sxData.getCartId()).getFields().get(CSPU_FIELD_ID);
        String platformProductId = "";

        if (StringUtils.isEmpty(dbProductId)) {
            // 新增产品到平台
            // 匹配平台产品Id列表
            // productGroup表中platformPid为空的时候，调用天猫API查找产品platformPid
            List<String> platformProductIdList  = getProductIdFromTmall(expressionParser, cmsMtPlatformCategorySchemaModel, null, shopBean, modifier);
            if (platformProductIdList == null) {
                throw new BusinessException("达尔文产品获取的匹配规则失败!");
            }

            if (platformProductIdList.isEmpty()) {
                // 没有找到产品，需要新增
                // added by morse.lu 2016/11/25 start
                if (ChannelConfigEnums.Channel.SN.getId().equals(sxData.getChannelId())) {
                    throw new BusinessException("Sneakerhead不能创建达尔文产品!");
                }
                // added by morse.lu 2016/11/25 end
//                if (ListUtils.isNull(masterWordEvaluationContexts)) {
//                    // 规格没有填
//                    throw new BusinessException("这是新增的达尔文产品,所有规格都要填!");
//                } else {
                    platformProductId = addTmallProduct(expressionParser, cmsMtPlatformCategorySchemaModel, null, shopBean, modifier);
                    if (StringUtils.isEmpty(platformProductId)) {
                        // schema没取到
                        throw new BusinessException("上新达尔文产品失败,可能是产品schema没有取到!");
                    }
//                }
             } else {
                // 对于找到的pid进行更新
                // 判断产品状态，取得正确的pid
                platformProductId = getUsefulProductId(sxData, platformProductIdList, shopBean);
                if (judgeCspuNeedUpdate(sxData)) {
                    // 要更新
                    // added by morse.lu 2016/11/25 start
                    if (ChannelConfigEnums.Channel.SN.getId().equals(sxData.getChannelId())) {
                        throw new BusinessException("Sneakerhead不能更新达尔文产品!");
                    }
                    // added by morse.lu 2016/11/25 end
                    updateTmallProduct(expressionParser, platformProductId, null, shopBean, modifier);
                 } else {
                    // 不要更新
                    canSxDarwinItem = true;
                }
            }

            if (!StringUtils.isEmpty(platformProductId)) {
                // 回写SxData和ProductGroup表中的platformPid
                sxData.getPlatform().setPlatformPid(platformProductId); // 平台产品id
                sxData.getPlatform().setModifier(modifier); // 更新者
                // 更新ProductGroup表
                productGroupService.update(sxData.getPlatform());
            }
        } else {
            // 更新产品
            // 判断产品状态，查看是否有审核中，或者审核失败的
            platformProductId = getUsefulProductId(sxData, new ArrayList<String>(){{this.add(dbProductId);}}, shopBean);
            if (judgeCspuNeedUpdate(sxData)) {
                // 要更新
                // added by morse.lu 2016/11/25 start
                if (ChannelConfigEnums.Channel.SN.getId().equals(sxData.getChannelId())) {
                    throw new BusinessException("Sneakerhead不能更新达尔文产品!");
                }
                // added by morse.lu 2016/11/25 end
                updateTmallProduct(expressionParser, platformProductId, null, shopBean, modifier);
            } else {
                // 不要更新
                canSxDarwinItem = true;
            }
        }

        return canSxDarwinItem;
    }

    /**
     * 判读产品是否需要更新
     * sxData.isUpdateProductFlg() || 有要更新或新增的规格
     *
     * @param sxData
     * @return
     */
    private boolean judgeCspuNeedUpdate(SxData sxData) {
        boolean needUpdate = false;
        // deleted by morse.lu 2016/10/13 start
        // 达尔文产品不去判断"产品是否允许更新表"，只判断"规格是否允许更新表"
//        if (sxData.isUpdateProductFlg()) {
//            needUpdate = true;
//        } else {
        // deleted by morse.lu 2016/10/13 end
            Map<String, SxDarwinSkuProps> mapDarwinSkuProps = sxData.getMapDarwinSkuProps();
            for (SxDarwinSkuProps skuProps : mapDarwinSkuProps.values()) {
                if (skuProps.isAllowUpdate() || StringUtils.isEmpty(skuProps.getCspuId())) {
                    // 允许更新 或者 增加规格
                    needUpdate = true;
                    break;
                }
            }
//        }

        return needUpdate;
    }

    /**
     * 把field里的默认值，设值进value
     *
     * @param field
     * @throws Exception
     */
    private void setFieldDefaultValues(Field field) throws Exception {

        switch (field.getType()) {
            case INPUT: {
                InputField inputField = (InputField) field;
                inputField.setValue(inputField.getDefaultValue());
                break;
            }
            case MULTIINPUT: {
                MultiInputField multiInputField = (MultiInputField) field;
                multiInputField.setValues(multiInputField.getDefaultValues());
                break;
            }
            case LABEL:
                break;
            case SINGLECHECK: {
                SingleCheckField singleCheckField = (SingleCheckField) field;
                singleCheckField.setValue(singleCheckField.getDefaultValue());
                break;
            }
            case MULTICHECK: {
                MultiCheckField multiCheckField = (MultiCheckField) field;
                List<Value> values = new ArrayList<>();
                multiCheckField.getDefaultValues().forEach(val -> values.add(new Value(val)));
                multiCheckField.setValues(values);
                break;
            }
            case COMPLEX: {
                ComplexField complexField = (ComplexField) field;
                ComplexValue complexValue = new ComplexValue();
                complexField.setComplexValue(complexValue);

                ComplexValue complexDefaultValue = complexField.getDefaultComplexValue();
                if (complexDefaultValue != null) {
                    for (String fieldId : complexDefaultValue.getFieldKeySet()) {
                        // 克隆的这个方法不能copy出value，先直接用DefaultField吧
//                        Field valueField = deepCloneField(complexDefaultValue.getValueField(fieldId));
//                        setFieldDefaultValues(valueField);
                        Field valueField = complexDefaultValue.getValueField(fieldId);
                        complexValue.put(valueField);
                    }
                }
                break;
            }
            case MULTICOMPLEX: {
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<ComplexValue> multiComplexValues = new ArrayList<>();
                multiComplexField.setComplexValues(multiComplexValues);

                List<ComplexValue> multiComplexDefaultValues = multiComplexField.getDefaultComplexValues();
                if (multiComplexDefaultValues != null) {
                    for (ComplexValue item : multiComplexDefaultValues) {
                        ComplexValue complexValue = new ComplexValue();
                        multiComplexValues.add(complexValue);

                        for (String fieldId : item.getFieldKeySet()) {
                            // 克隆的这个方法不能copy出value，先直接用DefaultField吧
//                            Field valueField = deepCloneField(item.getValueField(fieldId));
//                            setFieldDefaultValues(valueField);
                            Field valueField = item.getValueField(fieldId);
                            complexValue.put(valueField);
                        }
                    }
                }
                break;
            }
        }
    }
}
