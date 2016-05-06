package com.voyageone.service.impl.cms.sx;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.*;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtSizeMapDao;
import com.voyageone.service.dao.cms.CmsMtPlatformDictDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtPlatformImagesDaoExt;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtPlatformImagesModel;
import com.voyageone.service.model.cms.CmsBtSizeMapModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.service.model.ims.ImsBtProductModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * 上新相关共通逻辑
 *
 * @author morse.lu 16/04/19
 */
@Service
public class SxProductService extends BaseService {

    /**
     * upd_flg=0,需要上传(重新上传)
     */
    private static final String UPD_FLG_ADD = "0";
    /**
     * upd_flg=1,已经上传
     */
    private static final String UPD_FLG_UPLOADED = "1";
    @Autowired
    private TbPictureService tbPictureService;
    @Autowired
    private CmsBtSxWorkloadDaoExt sxWorkloadDao;
    @Autowired
    private ImsBtProductDao imsBtProductDao;
    @Autowired
    private CmsBtSizeMapDao cmsBtSizeMapDao;
    @Autowired
    private CmsBtPlatformImagesDaoExt cmsBtPlatformImagesDaoExt;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;
    @Autowired
    private CmsMtPlatformDictDao cmsMtPlatformDictDao;

    public static String encodeImageUrl(String plainValue) {
        String endStr = "%&";
        if (!plainValue.endsWith(endStr))
            return plainValue + endStr;
        return plainValue;
    }

    /**
     * sku根据size排序<br>
     * 已知size：<br>
     * 纯数字系列，纯数字系列(cm)，服饰系列(XL,L,M,S等)，其他(N/S,O/S,OneSize)
     *
     * @param skuSourceList 排序对象
     */
    public void sortSkuInfo(List<CmsBtProductModel_Sku> skuSourceList) {

        // Map<size, sort> 为了将来可能会从DB取得设定，先做成Map
        Map<String, Integer> mapSort = new HashMap<>();
        for (SkuSort s : SkuSort.values()) {
            mapSort.put(s.getSize(), Integer.valueOf(s.getSort()));
        }

        skuSourceList.sort((a, b) -> {
            String sizeA = a.getSize();
            String sizeB = b.getSize();

            Integer sortA = getSizeSort(sizeA, mapSort);
            Integer sortB = getSizeSort(sizeB, mapSort);

            if (mapSort.get(SkuSort.DIGIT.getSize()).intValue() == sortA.intValue() && mapSort.get(SkuSort.DIGIT.getSize()).intValue() == sortB.intValue()) {
                // 纯数字系列
                return Float.valueOf(sizeA).compareTo(Float.valueOf(sizeB));
            }

            if (mapSort.get(SkuSort.DIGIT_UNITS.getSize()).intValue() == sortA.intValue() && mapSort.get(SkuSort.DIGIT_UNITS.getSize()).intValue() == sortB.intValue()) {
                // 纯数字系列(cm)
                return Float.valueOf(sizeA.substring(0, sizeA.length() - 2)).compareTo(
                        Float.valueOf(sizeB.substring(0, sizeB.length() - 2)));
            }

            return sortA.compareTo(sortB);
        });
    }

    /**
     * 取得size对应设定的sort
     *
     * @param size    size
     * @param mapSort Map<size, sort>
     * @return sort
     */
    private Integer getSizeSort(String size, Map<String, Integer> mapSort) {
        Integer sort;
        if (StringUtils.isNumeric(size)) {
            // 纯数字系列
            sort = mapSort.get(SkuSort.DIGIT.getSize());
        } else if (size.length() > 2 && size.lastIndexOf("cm") == size.length() - 2 && StringUtils.isNumeric(size.substring(0, size.length() - 2))) {
            // 纯数字系列(cm)
            // 最后两位是cm，并且去除cm后剩下的是数字
            sort = mapSort.get(SkuSort.DIGIT_UNITS.getSize());
        } else {
            sort = mapSort.get(size);
            if (sort == null) {
                sort = mapSort.get(SkuSort.OTHER.getSize());
            }
        }
        return sort;
    }

    /**
     * 回写cms_bt_sx_workload表
     *
     * @param sxWorkloadModel bean
     * @param publishStatus   status
     * @param modifier 更新者
     */
    public int updateSxWorkload(CmsBtSxWorkloadModel sxWorkloadModel, int publishStatus, String modifier) {
        CmsBtSxWorkloadModel upModel = new CmsBtSxWorkloadModel();
        BeanUtils.copyProperties(sxWorkloadModel, upModel);
        upModel.setPublishStatus(publishStatus);
        upModel.setModifier(modifier);
        return sxWorkloadDao.updateSxWorkloadModelWithModifier(upModel);
    }

    /**
     * 回写ims_bt_product表
     *
     * @param sxData 上新数据
     * @param updateType s:sku级别, p:product级别
     * @param modifier 更新者
     */
    public void updateImsBtProduct(SxData sxData, String updateType, String modifier) {
        // voyageone_ims.ims_bt_product表的更新, 用来给wms更新库存时候用的
        List<CmsBtProductModel> sxProductList = sxData.getProductList();
        for (CmsBtProductModel sxProduct : sxProductList) {
            String code = sxProduct.getFields().getCode();

            ImsBtProductModel imsBtProductModel = imsBtProductDao.selectImsBtProductByChannelCartCode(
                    sxData.getChannelId(),
                    sxData.getCartId(),
                    code);
            if (imsBtProductModel == null) {
                // 没找到就插入
                imsBtProductModel = new ImsBtProductModel();
                imsBtProductModel.setChannelId(sxData.getChannelId());
                imsBtProductModel.setCartId(sxData.getCartId());
                imsBtProductModel.setCode(code);
                imsBtProductModel.setNumIid(sxData.getPlatform().getNumIId());
                imsBtProductModel.setQuantityUpdateType(updateType);

                imsBtProductDao.insertImsBtProduct(imsBtProductModel, modifier);
            } else {
                // 找到了, 更新
                imsBtProductModel.setNumIid(sxData.getPlatform().getNumIId());
                imsBtProductModel.setQuantityUpdateType(updateType);

                imsBtProductDao.updateImsBtProductBySeq(imsBtProductModel, modifier);
            }
        }
    }

    /**
     * 尺码转换
     *
     * @param cmsBtSizeMapModelList 尺码对照表
     * @param originalSize   转换前size
     * @return 转后后size
     */
    public String changeSize(List<CmsBtSizeMapModel> cmsBtSizeMapModelList, String originalSize) {

        for (CmsBtSizeMapModel cmsBtSizeMapModel : cmsBtSizeMapModelList) {
            if (originalSize.equals(cmsBtSizeMapModel.getOriginalSize())) {
                return cmsBtSizeMapModel.getAdjustSize();
            }
        }

        return null;
    }

    /**
     * 上传图片到天猫图片空间
     *
     * @param channelId   渠道id
     * @param cartId      平台id
     * @param groupId     groupId
     * @param shopBean    shopBean
     * @param imageUrlSet 待上传url的list
     * @param user        更新者
     */
    public Map<String, String> uploadImage(String channelId, int cartId, String groupId, ShopBean shopBean, Set<String> imageUrlSet, String user) throws Exception {
        // Map<srcUrl, destUrl>
        Map<String, String> retUrls = new HashMap<>();

        List<CmsBtPlatformImagesModel> imageUrlModel = cmsBtPlatformImagesDaoExt.selectPlatformImagesList(channelId, cartId, groupId);

        Map<String,CmsBtPlatformImagesModel> mapImageUrl = new HashMap<>();
        for (CmsBtPlatformImagesModel model : imageUrlModel) {
            mapImageUrl.put(model.getOriginalImgUrl(), model);
        }

        List<CmsBtPlatformImagesModel> imageUrlSaveModels = new ArrayList<>();

        for (String srcUrl : imageUrlSet) {
            // TODO:未定，可能要截，可能不要
//            String decodeSrcUrl = decodeImageUrl(srcUrl);
            CmsBtPlatformImagesModel model = mapImageUrl.get(srcUrl);
            if (model != null) {
                String updFlg = model.getUpdFlg();
                if (UPD_FLG_ADD.equals(updFlg)) {
                    // upd_flg=0,需要上传(重新上传)
                    // 上传后,更新cms_bt_platform_images
                    String destUrl = "";
                    String pictureId = "";
                    Picture picture = uploadImageByUrl(srcUrl, shopBean);
                    // test用 start
//                    Picture picture = new Picture();
//                    picture.setPicturePath("456.jgp");
//                    picture.setPictureId(Long.valueOf("456"));
                    // test用 end
                    if (picture != null) {
                        destUrl = picture.getPicturePath();
                        pictureId = String.valueOf(picture.getPictureId());
                    }
                    retUrls.put(srcUrl, destUrl);

                    model.setPlatformImgUrl(destUrl);
                    model.setPlatformImgId(pictureId);
                    model.setUpdFlg(UPD_FLG_UPLOADED);

                    cmsBtPlatformImagesDaoExt.updatePlatformImagesById(model, user);
                } else if (UPD_FLG_UPLOADED.equals(updFlg)) {
                    // upd_flg=1,已经上传
                    retUrls.put(srcUrl, model.getPlatformImgUrl());
                }
            } else {
                // 无数据，需要上传
                // 上传后, 插入cms_bt_platform_images
                String destUrl = "";
                String pictureId = "";
                Picture picture = uploadImageByUrl(srcUrl, shopBean);
                // test用 start
//                Picture picture = new Picture();
//                picture.setPicturePath("123.jgp");
//                picture.setPictureId(Long.valueOf("123"));
                // test用 end
                if (picture != null) {
                    destUrl = picture.getPicturePath();
                    pictureId = String.valueOf(picture.getPictureId());
                }
                retUrls.put(srcUrl, destUrl);

                CmsBtPlatformImagesModel imageUrlInfo = new CmsBtPlatformImagesModel();
                imageUrlInfo.setCartId(cartId);
                imageUrlInfo.setChannelId(channelId);
                imageUrlInfo.setSearchId(groupId);
                imageUrlInfo.setImgName(""); // 暂定为空
                imageUrlInfo.setOriginalImgUrl(srcUrl);
                imageUrlInfo.setPlatformImgUrl(destUrl);
                imageUrlInfo.setPlatformImgId(pictureId);
                imageUrlInfo.setUpdFlg(UPD_FLG_UPLOADED);
                imageUrlInfo.setCreater(user);
                imageUrlInfo.setModifier(user);
                imageUrlSaveModels.add(imageUrlInfo);
            }
        }

        if (imageUrlSaveModels.size() > 0) {
            // insert image url
            cmsBtPlatformImagesDaoExt.insertPlatformImagesByList(imageUrlSaveModels);
        }

        return retUrls;
    }

    private Picture uploadImageByUrl(String url, ShopBean shopBean) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int TIMEOUT_TIME = 10*1000;
        int waitTime = 0;
        int retry_times = 0;
        int max_retry_times = 3;
        InputStream is;
        do {
            try {
                URL imgUrl = new URL(url);
                is = imgUrl.openStream();
                byte[] byte_buf = new byte[1024];
                int readBytes = 0;

                while (true) {
                    while (is.available() >= 0) {
                        readBytes = is.read(byte_buf, 0, 1024);
                        if (readBytes < 0)
                            break;
                        $debug("read " + readBytes + " bytes");
                        waitTime = 0;
                        baos.write(byte_buf, 0, readBytes);
                    }
                    if (readBytes < 0)
                        break;

                    Thread.sleep(1000);
                    waitTime += 1000;

                    if (waitTime >= TIMEOUT_TIME) {
                        $error("fail to download image:" + url);
                        return null;
                    }
                }
                is.close();
            } catch (Exception e) {
                $error("exception when upload image", e);
                if ("Connection reset".equals(e.getMessage())) {
                    if (++retry_times < max_retry_times)
                        continue;
                }
                throw new BusinessException(String.format("Fail to upload image[%s]: %s", url, e.getMessage()));
            }
            break;
        } while (true);

        $info("read complete, begin to upload image");

        //上传到天猫
        Picture picture;
        String pictureUrl = null;
        try {
            $info("upload image, wait Tmall response...");
            PictureUploadResponse pictureUploadResponse = tbPictureService.uploadPicture(shopBean, baos.toByteArray(), "image_title", "0");
            $info("response comes");
            if (pictureUploadResponse == null) {
                String failCause = "上传图片到天猫时，超时, tmall response为空";
                $error(failCause);
                throw new BusinessException(failCause);
            } else if (pictureUploadResponse.getErrorCode() != null) {
                String failCause = "上传图片到天猫时，错误:" + pictureUploadResponse.getErrorCode() + ", " + pictureUploadResponse.getMsg();
                $error(failCause);
                $error("上传图片到天猫时，sub错误:" + pictureUploadResponse.getSubCode() + ", " + pictureUploadResponse.getSubMsg());
                throw new BusinessException(failCause);
            }
            picture = pictureUploadResponse.getPicture();
            if (picture != null)
                pictureUrl = picture.getPicturePath();
        } catch (ApiException e) {
            String failCause = "上传图片到天猫国际时出错！ msg:" + e.getMessage();
            $error("errCode: " + e.getErrCode());
            $error("errMsg: " + e.getErrMsg());
            throw new BusinessException(failCause);
        }
        $info(String.format("Success to upload image[%s -> %s]", url, pictureUrl));

        return picture;
    }

    public String decodeImageUrl(String encodedValue) {
        return encodedValue.substring(0, encodedValue.length() - 2);
    }

    /**
     * 上新用的商品数据取得
     *
     * @param channelId channelId
     * @param groupId   groupId
     * @return SxData
     */
    public SxData getSxProductDataByGroupId(String channelId, Long groupId) {
        // 获取group信息
        CmsBtProductGroupModel grpModel = cmsBtProductGroupDao.selectOneWithQuery("{'groupId':" + groupId + "}", channelId);
        if (grpModel == null) {
            return null;
        }

        SxData sxData = new SxData();
        sxData.setChannelId(channelId);
        sxData.setGroupId(groupId);
        sxData.setPlatform(grpModel);

        // 该group下的主商品code
        String mainProductCode = grpModel.getMainProductCode();

        // 该group对应的cartId
        Integer cartId = grpModel.getCartId();
        sxData.setCartId(cartId);

        // 该group下的所有code
        List<String> productCodeList = grpModel.getProductCodes();
        String[] codeArr = new String[productCodeList.size()];
        codeArr = productCodeList.toArray(codeArr);

        // 通过上面取得的code，得到对应的产品信息，以及sku信息
        List<CmsBtProductModel_Sku> skuList = new ArrayList<>(); // 该group下，所有允许在该平台上上架的sku
        List<CmsBtProductModel> productModelList = cmsBtProductDao.select("{" + MongoUtils.splicingValue("fields.code", codeArr, "$in") + "}", channelId);
        List<CmsBtProductModel> removeProductList = new ArrayList<>(); // product删除对象(如果该product下没有允许在该平台上上架的sku，删除)
        for (CmsBtProductModel productModel : productModelList) {
            if (mainProductCode.equals(productModel.getFields().getCode())) {
                // 主商品
                sxData.setMainProduct(productModel);
                CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode(channelId, productModel.getFields().getCode());
                sxData.setCmsBtFeedInfoModel(feedInfo);
            }

            List<CmsBtProductModel_Sku> productModelSku = productModel.getSkus();
            List<CmsBtProductModel_Sku> skus = new ArrayList<>(); // 该product下，允许在该平台上上架的sku
            productModelSku.forEach(sku -> {
                if (sku.getSkuCarts().contains(cartId)) {
                    skus.add(sku);
                }
            });

            if (skus.size() > 0) {
                productModel.setSkus(skus);
                skuList.addAll(skus);
            } else {
                // 该product下没有允许在该平台上上架的sku
                removeProductList.add(productModel);
            }
        }

        removeProductList.forEach(product -> productModelList.remove(product));

        sxData.setProductList(productModelList);
        sxData.setSkuList(skuList);

        return sxData;
    }

    /**
     * mapping
     *
     * @param fields List<Field>
     * @param cmsMtPlatformMappingModel
     * @param shopBean
     * @param expressionParser
     * @param user 上传图片用
     * @return Map<field_id, mt里转换后的值>
     * @throws Exception
     */
    public Map<String, Field> constructMappingPlatformProps(List<Field> fields, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, ShopBean shopBean, ExpressionParser expressionParser, String user) throws Exception {
        Map<String, Field> retMap = null;

        // TODO:特殊字段处理
        // 特殊字段Map<CartId, Map<propId, 对应mapping项目或者处理(未定)>>
        Map<Integer, Map<String, Object>> mapSpAll = new HashMap<>();

//        Map<String, Object> mapSp = mapSpAll.get(shopBean.getCart_id());
        Map<String, Object> mapSp = new HashMap<>();

        Map<String, MappingBean> mapProp = new HashMap<>();
        List<MappingBean> propMapings = cmsMtPlatformMappingModel.getProps();
        for (MappingBean mappingBean : propMapings) {
            mapProp.put(mappingBean.getPlatformPropId(), mappingBean);
        }

        for(Field field : fields) {
            if (mapSp.containsKey(field.getId())) {
                // TODO:特殊字段处理
            } else {
                MappingBean mappingBean = mapProp.get(field.getId());
                if (mappingBean == null) {
                    continue;
                }
                Map<String, Field> resolveField = resolveMapping(mappingBean, field, shopBean, expressionParser, user);
                if (resolveField != null) {
                    if (retMap == null) {
                        retMap = new HashMap<>();
                    }
                    retMap.putAll(resolveField);
                }
            }
        }

        return retMap;
    }

    public Map<String, Field> resolveMapping(MappingBean mappingBean, Field field, ShopBean shopBean, ExpressionParser expressionParser, String user) throws Exception {
        Map<String, Field> retMap = null;

        if (MappingBean.MAPPING_SIMPLE.equals(mappingBean.getMappingType())) {
            retMap = new HashMap();
            SimpleMappingBean simpleMappingBean = (SimpleMappingBean) mappingBean;
            String expressionValue = expressionParser.parse(simpleMappingBean.getExpression(), shopBean, user, null);
            if (null == expressionValue) {
                return null;
            }

            switch (field.getType()) {
                case INPUT: {
                    InputField inputField = (InputField) field;
                    inputField.setValue(expressionValue);
                    retMap.put(field.getId(), inputField);
//                    ((InputField) field).setValue(expressionValue);
                    break;
                }
                case SINGLECHECK: {
                    SingleCheckField singleCheckField = (SingleCheckField) field;
                    singleCheckField.setValue(expressionValue);
                    retMap.put(field.getId(), singleCheckField);
//                    ((SingleCheckField) field).setValue(expressionValue);
                    break;
                }
                case MULTIINPUT:
                    break;
                case MULTICHECK: {
                    String[] valueArrays = ExpressionParser.decodeString(expressionValue);

                    MultiCheckField multiCheckField = (MultiCheckField)field;
                    for (String val : valueArrays) {
                        multiCheckField.addValue(val);
                    }
                    retMap.put(field.getId(), multiCheckField);

//                    retMap.put(field.getId(), Arrays.asList(valueArrays));
//                    for (String value : valueArrays) {
//                        ((MultiCheckField) field).addValue(value);
//                    }
                    break;
                }
                case COMPLEX:
                    break;
                case MULTICOMPLEX:
                    break;
                case LABEL:
                    break;
                default:
                    $error("复杂类型的属性:" + field.getType() + "不能使用MAPPING_SINGLE来作为匹配类型");
                    return null;
            }
        } else if (MappingBean.MAPPING_COMPLEX.equals(mappingBean.getMappingType())) {
            // Complex 会包含【一组】由多个简单类型或复杂类型组成的一组类型
            // 他的子属性可以包含任意类型， 包括Complex类型也可以
            retMap = new HashMap();
            CmsBtProductModel mainProduct = expressionParser.getSxData().getMainProduct();
            ComplexMappingBean complexMappingBean = (ComplexMappingBean) mappingBean;
            if (field.getType() == FieldTypeEnum.COMPLEX) {
                Map<String, Object> masterWordEvaluationContext = (Map<String, Object>) mainProduct.getFields().get(complexMappingBean.getMasterPropId());
                if (masterWordEvaluationContext != null) {
                    expressionParser.pushMasterPropContext(masterWordEvaluationContext);
                }

                ComplexField complexField = (ComplexField) field;
                ComplexValue complexValue = new ComplexValue();
                complexField.setComplexValue(complexValue);

                for (MappingBean subMappingBean : complexMappingBean.getSubMappings()) {
                    String platformPropId = subMappingBean.getPlatformPropId();
                    Map<String, Field> schemaFieldsMap = complexField.getFieldMap();

                    Field schemaField = schemaFieldsMap.get(platformPropId);
                    if (schemaField == null) {
                        $warn("有" + platformPropId + "的mapping关系却没有该属性");
                        $warn("跳过属性" + platformPropId);
                        continue;
                    }
                    Field valueField = deepCloneField(schemaField);
                    Map<String, Field> res = resolveMapping(subMappingBean, valueField, shopBean, expressionParser, user);
                    if (res != null) {
                        retMap.putAll(res);
                    }
                    complexValue.put(valueField);
                }
                if (masterWordEvaluationContext != null) {
                    expressionParser.popMasterPropContext();
                }
            } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
                List<Map<String, Object>> masterWordEvaluationContexts = (List<Map<String, Object>>) mainProduct.getFields().get(complexMappingBean.getMasterPropId());

                if (masterWordEvaluationContexts == null || masterWordEvaluationContexts.isEmpty()) {
                    $info("No value found for MultiComplex field: " + field.getId());
                    return null;
                }

                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<ComplexValue> complexValues = new ArrayList<>();
                multiComplexField.setComplexValues(complexValues);

                int index = 0;
                for (Map<String, Object> masterWordEvaluationContext : masterWordEvaluationContexts) {
                    expressionParser.pushMasterPropContext(masterWordEvaluationContext);
                    ComplexValue complexValue = new ComplexValue();
                    complexValues.add(complexValue);

                    for (MappingBean subMappingBean : complexMappingBean.getSubMappings()) {
                        String platformPropId = subMappingBean.getPlatformPropId();
                        Map<String, Field> schemaFieldsMap = multiComplexField.getFieldMap();

                        Field schemaField = schemaFieldsMap.get(platformPropId);
                        Field valueField = deepCloneField(schemaField);
                        Map<String, Field> res = resolveMapping(subMappingBean, valueField, shopBean, expressionParser, user);

                        if (res != null) {
                            for (Map.Entry<String, Field> entry : res.entrySet()) {
                                retMap.put(field.getId() + "_" + String.valueOf(index) + "_" + entry.getKey(), entry.getValue());
                            }
                        }
                        complexValue.put(valueField);
                    }
                    expressionParser.popMasterPropContext();
                    index++;
                }
            } else {
                $error("Unexpected field type: " + field.getType());
                return null;
            }
        } else if (MappingBean.MAPPING_MULTICOMPLEX_CUSTOM.equals(mappingBean.getMappingType())) {
            // MultiComplex 会包含【多组】由多个简单类型或复杂类型组成的一组组类型
            // 他的子属性可以包含任意类型， 包括Complex类型也可以
            retMap = new HashMap();
            MultiComplexCustomMappingBean multiComplexCustomMappingBean = (MultiComplexCustomMappingBean) mappingBean;
            MultiComplexField multiComplexField = (MultiComplexField) field;
            List<ComplexValue> complexValues = new ArrayList<>();
            for (MultiComplexCustomMappingValue multiComplexCustomMappingValue : multiComplexCustomMappingBean.getValues()) {
                ComplexValue complexValue = new ComplexValue();
                for (MappingBean subMapping : multiComplexCustomMappingValue.getSubMappings()) {
                    String platformPropId = subMapping.getPlatformPropId();
                    Map<String, Field> schemaFieldsMap = multiComplexField.getFieldMap();

                    Field schemaField = schemaFieldsMap.get(platformPropId);
                    Field valueField = deepCloneField(schemaField);
                    Map<String, Field> res = resolveMapping(subMapping, valueField, shopBean, expressionParser, user);
                    if (res != null) {
                        retMap.putAll(res);
                    }
                    complexValue.put(valueField);
                }
                complexValues.add(complexValue);
            }
            multiComplexField.setComplexValues(complexValues);
        }

        return retMap;
    }

    private Field deepCloneField(Field field) throws Exception {
        try {
            return SchemaReader.elementToField(field.toElement());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 根据字典名字解析
     */
    public String resolveDict(String dictName, ExpressionParser expressionParser, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        RuleExpression ruleExpression = new RuleExpression();
        ruleExpression.addRuleWord(new DictWord(dictName));
        return expressionParser.parse(ruleExpression, shopBean, user, extParameter);
    }

    public List<CmsMtPlatformDictModel> searchDictList(Map<String, Object> map) {
        return cmsMtPlatformDictDao.selectList(map);
    }

    private enum SkuSort {
        DIGIT("digit", 1), // 纯数字系列
        DIGIT_UNITS("digitUnits", 2), // 纯数字系列(cm)
        XXX("XXX", 3), // XXX
        XXS("XXS", 4), // XXS
        XS("XS", 5), // XS
        XS_S("XS/S", 6), // XS/S
        XSS("XSS", 7), // XSS
        S("S", 8), // S
        S_M("S/M", 9), // S/M
        M("M", 10), // M

        M_L("M/L", 11), // M/L
        L("L", 12), // L
        XL("XL", 13), // XL
        XXL("XXL", 14), // XXL
        N_S("N/S", 15), // N/S
        O_S("O/S", 16), // O/S
        ONE_SIZE("OneSize", 17), // OneSize

        OTHER("Other", 18), // 以外
        ;

        private final String size;
        private final int sort;

        private SkuSort(String size, int sort) {
            this.size = size;
            this.sort = sort;
        }

        private String getSize() {
            return this.size;
        }

        private int getSort() {
            return this.sort;
        }
    }

}
