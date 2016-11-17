package com.voyageone.service.impl.cms.sx;

import com.google.common.base.Joiner;
import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.rule.Rule;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.*;
import com.voyageone.common.util.baidu.translate.BaiduTranslateUtil;
import com.voyageone.components.jumei.bean.JmImageFileBean;
import com.voyageone.components.jumei.service.JumeiImageFileService;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.*;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtImageGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.dao.ims.ImsBtProductExceptDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.daoext.cms.CmsBtPlatformImagesDaoExt;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.daoext.cms.PaddingImageDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.*;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.sx.sku_field.AbstractSkuFieldBuilder;
import com.voyageone.service.impl.cms.sx.sku_field.SkuFieldBuilderService;
import com.voyageone.service.impl.cms.sx.sku_field.tmall.TmallGjSkuFieldBuilderImpl7;
import com.voyageone.service.impl.cms.sx.sku_field.tmall.TmallGjSkuFieldBuilderImpl8;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.enums.CustomMappingType;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import com.voyageone.service.model.cms.mongo.channel.*;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.ims.ImsBtProductModel;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private static final int UPD_FLG_ADD = 0;
    /**
     * upd_flg=1,已经上传
     */
    private static final int UPD_FLG_UPLOADED = 1;
    @Autowired
    private TbPictureService tbPictureService;
    @Autowired
    TbProductService tbProductService;
    @Autowired
    private SkuFieldBuilderService skuFieldBuilderService;
    @Autowired
    private BusinessLogService businessLogService;
    @Autowired
    private ConditionPropValueService conditionPropValueService;
    @Autowired
    private FeedCustomPropService customPropService;
    @Autowired
    private SizeChartService sizeChartService;
    @Autowired
    private ImageTemplateService imageTemplateService;
    @Autowired
    private TaobaoScItemService taobaoScItemService;

    @Autowired
    private CmsBtSxWorkloadDaoExt sxWorkloadDao;
    @Autowired
    private ImsBtProductDao imsBtProductDao;
    @Autowired
    private ImsBtProductExceptDao imsBtProductExceptDao;
    @Autowired
    private CmsBtImageGroupDao cmsBtImageGroupDao;
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
    @Autowired
    private PaddingImageDaoExt paddingImageDaoExt;
    @Autowired
    private CmsMtPlatformPropMappingCustomDao cmsMtPlatformPropMappingCustomDao;
    @Autowired
    private CmsMtBrandsMappingDao cmsMtBrandsMappingDao;
    @Autowired
    private WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    JumeiImageFileService jumeiImageFileService;
    @Autowired
    private CmsBtWorkloadHistoryDao cmsBtWorkloadHistoryDao;
    @Autowired
    private CmsMtPlatformPropSkuDao cmsMtPlatformPropSkuDao;
    @Autowired
    private CmsMtChannelSkuConfigDao cmsMtChannelSkuConfigDao;
    @Autowired
    private CmsBtBrandBlockService cmsBtBrandBlockService;

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
    // modified by morse.lu 2016/06/28 start
    // product表结构变化
//    public void sortSkuInfo(List<CmsBtProductModel_Sku> skuSourceList) {
    public void sortSkuInfo(List<? extends BaseMongoMap<String, Object>> skuSourceList) {
        // modified by morse.lu 2016/06/28 end

        // Map<size, sort> 为了将来可能会从DB取得设定，先做成Map
        Map<String, Integer> mapSort = new HashMap<>();
        for (SkuSort s : SkuSort.values()) {
            mapSort.put(s.getSize(), s.getSort());
        }

        skuSourceList.sort((a, b) -> {
            // modified by morse.lu 2016/06/28 start
//            String sizeA = a.getSize();
//            String sizeB = b.getSize();
            // modified by morse.lu 2016/07/08 start
            // 改成根据sizeSx来排序啦
//            String sizeA = StringUtils.null2Space(a.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.size.name()));
//            String sizeB = StringUtils.null2Space(b.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.size.name()));
            String sizeA = StringUtils.null2Space(a.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()));
            String sizeB = StringUtils.null2Space(b.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()));
            // modified by morse.lu 2016/07/08 end
            // modified by morse.lu 2016/06/28 end

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
     * 根据skuCode进行排序
     *
     * @param listSku 要排序的list
     * @param sortKey skuCode的list，用于排序
     */
    public void sortListBySkuCode(List<BaseMongoMap<String, Object>> listSku, List<String> sortKey) {
        listSku.sort((a, b) -> {
            int aIndex = sortKey.indexOf(a.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
            int bIndex = sortKey.indexOf(b.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));

            if (aIndex == -1) aIndex = 99999;
            if (bIndex == -1) bIndex = 99999;

            if (aIndex < bIndex) return -1;
            else if (aIndex == bIndex) return 0;
            else return 1;
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
//        CmsBtSxWorkloadModel upModel = new CmsBtSxWorkloadModel();
//        BeanUtils.copyProperties(sxWorkloadModel, upModel);
//        upModel.setPublishStatus(publishStatus);
//        upModel.setModifier(modifier);
//        return sxWorkloadDao.updateSxWorkloadModelWithModifier(upModel);

        if (sxWorkloadModel == null) return 0;
        sxWorkloadModel.setPublishStatus(publishStatus);
        sxWorkloadModel.setModifier(modifier);
        return sxWorkloadDao.updatePublishStatus(sxWorkloadModel);
    }

    /**
     * 回写product group表中的numIId和platformStatus(Onsale/InStock)
     *
     * @param sxData SxData 上新数据
     * @param numIId String 商品id
     * @param modifier 更新者
     */
    public void updateProductGroupNumIIdStatus(SxData sxData, String numIId, String modifier) {
        // 上新成功后回写product group表中的numIId和platformStatus
        // 回写商品id(wareId->numIId)
        sxData.getPlatform().setNumIId(numIId);
        // 设置PublishTime
        sxData.getPlatform().setPublishTime(DateTimeUtil.getNowTimeStamp());
        // platformActive平台上新状态类型(ToOnSale/ToInStock)
        if (CmsConstants.PlatformActive.ToOnSale.equals(sxData.getPlatform().getPlatformActive())) {
            // platformActive是(ToOnSale)时，把platformStatus更新成"OnSale"
            sxData.getPlatform().setPlatformStatus(CmsConstants.PlatformStatus.OnSale);
        } else {
            // platformActive是(ToInStock)时，把platformStatus更新成"InStock"(默认)
            sxData.getPlatform().setPlatformStatus(CmsConstants.PlatformStatus.InStock);
        }
        // 更新者
        sxData.getPlatform().setModifier(modifier);

        // 更新ProductGroup表(更新该model对应的所有(包括product表)和上新有关的状态信息)
        productGroupService.updateGroupsPlatformStatus(sxData.getPlatform(), sxData.getProductList().stream().map(p -> p.getCommon().getFields().getCode()).collect(Collectors.toList()));
    }

    /**
     * 出错的时候将错误信息回写到cms_bt_business_log表
     *
     * @param sxData 上新数据
     * @param modifier 更新者
     */
    public void insertBusinessLog(SxData sxData, String modifier) {
        CmsBtBusinessLogModel businessLogModel = new CmsBtBusinessLogModel();
        CmsBtProductModel mainProduct = sxData.getMainProduct();
        CmsBtProductGroupModel productGroup = sxData.getPlatform();

        // 渠道id
        businessLogModel.setChannelId(sxData.getChannelId());
        // 类目id
        if (mainProduct != null) businessLogModel.setCatId(mainProduct.getPlatform(sxData.getCartId()).getpCatId());
        // 平台id
        businessLogModel.setCartId(sxData.getCartId());
        // Group id
        businessLogModel.setGroupId(String.valueOf(sxData.getGroupId()));
        // 主商品的product_id
        if (mainProduct != null) businessLogModel.setProductId(String.valueOf(mainProduct.getProdId()));
        // code，没有code就不要设置
        if (productGroup != null) businessLogModel.setCode(productGroup.getMainProductCode());
        // 错误类型(1:上新错误)
        businessLogModel.setErrorTypeId(1);
        // 错误code
        businessLogModel.setErrorCode(sxData.getErrorCode());
        // 详细错误信息
        businessLogModel.setErrorMsg(sxData.getErrorMessage());
        // 状态(0:未处理 1:已处理)
        businessLogModel.setStatus(0);
        // 创建者
        businessLogModel.setCreater(modifier);
        // 更新者
        businessLogModel.setModifier(modifier);

        businessLogService.insertBusinessLog(businessLogModel);
    }

    /**
     * cms_bt_business_log对应的以前的错误清一下,即把status更新成1:已解决
     *
     * @param sxData 上新数据
     * @param modifier 更新者
     */
    public void clearBusinessLog(SxData sxData, String modifier) {
        CmsBtProductGroupModel productGroup = sxData.getPlatform();

        // code，没有code就不要设置
        String mainCode = "";
        if (productGroup != null) mainCode = productGroup.getMainProductCode();

        int effectCnt = businessLogService.updateFinishStatusByCondition(sxData.getChannelId(), sxData.getCartId(), Long.toString(sxData.getGroupId()),
                null, mainCode, modifier);
        $debug("cms_bt_business_log表以前的错误信息逻辑删除件数：%d件 [ChannelId:%s] [CatId:%s] [GroupId:%s]",
                effectCnt, sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId());
    }

    /**
     * cms_bt_business_log对应的以前的错误清一下,即把status更新成1:已解决
     *
     * @param channelId String 渠道id
     * @param cartId Integer 平台id
     * @param groupId Long GroupId
     * @param model String feed model
     * @param code String 产品code
     * @param modifier String 更新者
     */
    public void clearBusinessLog(String channelId, Integer cartId, Long groupId,
                                 String model, String code, String modifier) {
        // 逻辑删除cms_bt_business_log表以前的错误信息
        int effectCnt = businessLogService.updateFinishStatusByCondition(channelId, cartId,
                LongUtils.toString(groupId), model, code, modifier);
        $debug("cms_bt_business_log表以前的错误信息逻辑删除件数：%d件 [ChannelId:%s] [CatId:%d] [GroupId:%s] [Code:%s]",
                effectCnt, channelId, cartId, LongUtils.toString(groupId), code);
    }

    /**
     * 回写ims_bt_product表
     *
     * @param sxData 上新数据
     * @param modifier 更新者
     */
    public void updateImsBtProduct(SxData sxData, String modifier) {
        // s:sku级别, p:product级别
        String updateType = sxData.isHasSku() ? "s" : "p";

        // voyageone_ims.ims_bt_product表的更新, 用来给wms更新库存时候用的
        List<CmsBtProductModel> sxProductList = sxData.getProductList();
        for (CmsBtProductModel sxProduct : sxProductList) {
            // modified by morse.lu 2016/06/24 start
//            String code = sxProduct.getFields().getCode();
            String code = sxProduct.getCommon().getFields().getCode();
            // modified by morse.lu 2016/06/24 end

            ImsBtProductModel imsBtProductModel = imsBtProductDao.selectImsBtProductByChannelCartCode(
                    sxData.getMainProduct().getOrgChannelId(),   // ims表要用OrgChannelId
                    sxData.getCartId(),
                    code);
            if (imsBtProductModel == null) {
                // 没找到就插入
                imsBtProductModel = new ImsBtProductModel();
                imsBtProductModel.setChannelId(sxData.getMainProduct().getOrgChannelId()); // ims表要用OrgChannelId
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

    // delete by morse.lu 2016/06/14 start
    // 设计变更，mysql -> mongo , 新方法getSizeMap
//    /**
//     * 尺码转换
//     *
//     * @param cmsBtSizeMapModelList 尺码对照表
//     * @param originalSize   转换前size
//     * @return 转后后size
//     */
//    public String changeSize(List<CmsBtSizeMapModel> cmsBtSizeMapModelList, String originalSize) {
//
//        for (CmsBtSizeMapModel cmsBtSizeMapModel : cmsBtSizeMapModelList) {
//            if (originalSize.equals(cmsBtSizeMapModel.getOriginalSize())) {
//                return cmsBtSizeMapModel.getAdjustSize();
//            }
//        }
//
//        return null;
//    }
    // delete by morse.lu 2016/06/14 end

    /**
     * 上传图片到天猫(或聚美)图片空间
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
                int updFlg = model.getUpdFlg();
                if (UPD_FLG_ADD == updFlg) {
                    // upd_flg=0,需要上传(重新上传)
                    // 上传后,更新cms_bt_platform_images
                    String destUrl = "";
                    String pictureId = "";

                    // update by desmond 2016/07/13 start 上传图片出错时不抛出异常
                    try {
                        if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
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
                        } else if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JM.getId())) {
                            String picture = uploadImageByUrl_JM(srcUrl, shopBean);
                            if (!StringUtils.isEmpty(picture)) {
                                destUrl = picture;
                            }
                        }
                    } catch (Exception e) {
                        // 上传图片出错时不抛出异常，具体的错误信息在上传图片方法里面已经输出了，这里不做任何处理
                    }
                    // update by desmond 2016/07/13 end
                    // 将变换前后的图片url加到map中，即使平台图片url(destUrl)为空也追加，外面要根据空来做删除处理
                    retUrls.put(srcUrl, destUrl);
                    // update by desmond 2016/07/13 start  增加destUrl非空的判断
                    if (!StringUtils.isEmpty(destUrl)) {
                        model.setPlatformImgUrl(destUrl);
                        model.setPlatformImgId(pictureId);
                        model.setUpdFlg(UPD_FLG_UPLOADED);

                        cmsBtPlatformImagesDaoExt.updatePlatformImagesById(model, user);
                    }
                    // update by desmond 2016/07/13 end
                } else if (UPD_FLG_UPLOADED == updFlg) {
                    // upd_flg=1,已经上传
                    retUrls.put(srcUrl, model.getPlatformImgUrl());
                }
            } else {
                // 无数据，需要上传
                // 上传后, 插入cms_bt_platform_images
                String destUrl = "";
                String pictureId = "";
                // update by desmond 2016/07/13 start 上传图片出错时不抛出异常
                try {
                    if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
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
                    } else if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JM.getId())) {
                        String picture = uploadImageByUrl_JM(srcUrl, shopBean);
                        if (!StringUtils.isEmpty(picture)) {
                            destUrl = picture;
                        }
                    }
                } catch (Exception e) {
                    // 上传图片出错时不抛出异常，具体的错误信息在上传图片方法里面已经输出了,这里不做任何处理
                }
                // update by desmond 2016/07/13 end
                // 将变换前后的图片url加到map中，即使平台图片url(destUrl)为空也追加，外面要根据空来做删除处理
                retUrls.put(srcUrl, destUrl);
                // update by desmond 2016/07/13 start  增加destUrl非空的判断
                if (!StringUtils.isEmpty(destUrl)) {
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
                // update by desmond 2016/07/13 end
            }
        }

        if (!imageUrlSaveModels.isEmpty()) {
            // insert image url
            cmsBtPlatformImagesDaoExt.insertPlatformImagesByList(imageUrlSaveModels);
        }

        return retUrls;
    }

    public Picture uploadImageByUrl(String url, ShopBean shopBean) throws Exception {
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

//    private String getImageName(String picUrl) throws MalformedURLException {
//        URL url = new URL(picUrl);
//        String path = url.getPath();
//        String[] urlStr = path.split("/");
//        String filename = urlStr[urlStr.length -1 ];
//        return filename;
//    }
    
    
    public String uploadImageByUrl_JM(String picUrl, ShopBean shopBean) throws Exception {

        // 图片流
        InputStream inputStream = null;

        try {
            // 读取图片
            inputStream = getImgInputStream(picUrl, 3);
        } catch (Exception e) {
            // 即使scene7上URL对应的图片不存在也不要报异常，直接返回空字符串
            String errMsg = "通过scene7上聚美图片URL取得对应的图片流失败 [picUrl:" + picUrl + "]";
            $error(errMsg);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
            throw new BusinessException(errMsg);
        }

        try {
            //上传图片
            JmImageFileBean fileBean = new JmImageFileBean();
            //用UUID命名
            fileBean.setImgName(UUID.randomUUID().toString());
            fileBean.setInputStream(inputStream);
            fileBean.setNeedReplace(false);
            fileBean.setDirName(shopBean.getOrder_channel_id());
            fileBean.setExtName("jpg");
            return jumeiImageFileService.imageFileUpload(shopBean, fileBean);
        } catch (Exception e) {
            // 图片上传到聚美平台失败
            String errMsg = "上传图片到聚美平台失败 [errMsg:" + e.getMessage() + "]";
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

    }




    public String decodeImageUrl(String encodedValue) {
        return encodedValue.substring(0, encodedValue.length() - 2);
    }

    /**
     * 上新用的商品数据取得
     * 对象外的code（不会set进productList）：
     *     1：status不是Approved
     *     2：code下没有允许在该平台上上架的sku（skus.carts里不包含本次上新的cartId，以后改成platform.skus.isSale=false，这些sku不会set进skuList）
     *     3：lock = 1
     *
     * @param channelId channelId
     * @param groupId   groupId
     * @return SxData
     */
    public SxData getSxProductDataByGroupId(String channelId, Long groupId) {

        SxData sxData = new SxData();
        sxData.setChannelId(channelId);
        sxData.setGroupId(groupId);

        // 获取group信息
        CmsBtProductGroupModel grpModel = cmsBtProductGroupDao.selectOneWithQuery("{'groupId':" + groupId + "}", channelId);
        if (grpModel == null) {
            // update by desmond 2016/07/12 start
//            return null;
            String errMsg = "取得上新数据(SxData)失败! 没找到对应的group数据(groupId=" + groupId + ")";
            $error(errMsg);
            sxData.setErrorMessage(errMsg);
            return sxData;
            // update by desmond 2016/07/12 end
        }

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
        // modified by morse.lu 2016/06/13 start
//        List<CmsBtProductModel_Sku> skuList = new ArrayList<>(); // 该group下，所有允许在该平台上上架的sku
        List<BaseMongoMap<String, Object>> skuList = new ArrayList<>(); // 该group下，所有允许在该平台上上架的sku
        // modified by morse.lu 2016/06/13 end
        List<CmsBtProductModel> productModelList = cmsBtProductDao.select("{" + MongoUtils.splicingValue("common.fields.code", codeArr, "$in") + "}", channelId);
        List<CmsBtProductModel> removeProductList = new ArrayList<>(); // product删除对象(如果该product下没有允许在该平台上上架的sku，删除)
        for (CmsBtProductModel productModel : productModelList) {
            // modified by morse.lu 2016/06/28 start
            // product表结构变化
//            if (mainProductCode.equals(productModel.getFields().getCode())) {
            if (mainProductCode.equals(productModel.getCommon().getFields().getCode())) {
                // modified by morse.lu 2016/06/28 end
                // 主商品
                sxData.setMainProduct(productModel);
                // modified by morse.lu 2016/06/07 start
//                CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode(channelId, productModel.getFields().getCode());
                String orgChannelId = productModel.getOrgChannelId(); // feed信息要从org里获取
                // modified by morse.lu 2016/06/28 start
                // product表结构变化
//                String prodOrgCode = productModel.getFields().getOriginalCode(); // 有可能会有原始code
//                if (prodOrgCode == null) prodOrgCode = productModel.getFields().getCode();
                String prodOrgCode = productModel.getCommon().getFields().getOriginalCode(); // 有可能会有原始code
                if (prodOrgCode == null) prodOrgCode = productModel.getCommon().getFields().getCode();
                // modified by morse.lu 2016/06/28 end
                CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode(orgChannelId, prodOrgCode);
                // Add by desmond 2016/06/12 start
                if (feedInfo == null) {
                    // 该商品对应的feed信息不存在时，暂时的做法就是跳过当前记录， 这个group就不上了
                    String errMsg = "取得上新数据(SxData)失败! 该商品对应的feed信息不存在(OriginalCode/Code=" + prodOrgCode + ")";
                    $error(errMsg);
                    sxData.setErrorMessage(errMsg);
                    break;
                }
                // Add by desmond 2016/06/12 end
                // modified by morse.lu 2016/06/07 end
                sxData.setCmsBtFeedInfoModel(feedInfo);

                Map<String, Object> searchParam = new HashMap<>();
                searchParam.put("channelId", channelId);
                searchParam.put("cartId", cartId);
                searchParam.put("cmsBrand", productModel.getCommon().getFields().getBrand());
                CmsMtBrandsMappingModel cmsMtBrandsMappingModel = cmsMtBrandsMappingDao.selectOne(searchParam);
                if (cmsMtBrandsMappingModel != null) {
                    sxData.setBrandCode(cmsMtBrandsMappingModel.getBrandId());
                }
            }

            // 20160606 tom 增加对feed属性(feed.customIds, feed.customIdsCn)的排序 START
            CmsBtProductModel mainProductModel = sxData.getMainProduct();
            if (mainProductModel != null) {
                List<String> customIdsOld = mainProductModel.getFeed().getCustomIds();
                List<String> customIdsCnOld = mainProductModel.getFeed().getCustomIdsCn();

                if (customIdsOld != null && !customIdsOld.isEmpty() && customIdsCnOld != null && !customIdsCnOld.isEmpty()) {
                    // 获取排序顺序
//                    customPropService.doInit(channelId);
                    String feedCatPath = sxData.getCmsBtFeedInfoModel().getCategory();
                    if (feedCatPath == null) feedCatPath = "";
                    List<FeedCustomPropWithValueBean> feedCustomPropList = customPropService.getPropList(channelId, feedCatPath);

                    // 重新排序
                    List<String> customIdsNew = new ArrayList<>();
                    List<String> customIdsCnNew = new ArrayList<>();
                    for (FeedCustomPropWithValueBean feedCustomPropWithValueBean : feedCustomPropList) {
                        String customIdsSort = feedCustomPropWithValueBean.getFeed_prop_original();

                        for (int i = 0; i < customIdsOld.size(); i++) {
                            if (customIdsSort.equals(customIdsOld.get(i))) {
                                // 设置到新的里
                                customIdsNew.add(customIdsOld.get(i));
                                customIdsCnNew.add(customIdsCnOld.get(i));

                                // 删掉一下, 用来小小地提升下速度
                                customIdsOld.remove(i);
                                customIdsCnOld.remove(i);
                                break;
                            }
                        }
                    }

                    // 设置回去
                    mainProductModel.getFeed().setCustomIds(customIdsNew);
                    mainProductModel.getFeed().setCustomIdsCn(customIdsCnNew);
                }
            }
            // 20160606 tom 增加对feed属性(feed.customIds, feed.customIdsCn)的排序 END

            // 取得status判断是否已经Approved（智能上新模式的场合， 无需approve）（但是后面锁住的判断， sku是否售卖的判断仍然是要的）
            CmsBtProductModel_Platform_Cart productPlatformCart = productModel.getPlatform(cartId);
            if (productPlatformCart == null) {
                continue;
            }
            if (!isSmartSx(channelId, cartId) && !CmsConstants.ProductStatus.Approved.name().equals(productPlatformCart.getStatus())) {
                removeProductList.add(productModel);
                continue;
            }
            // 2016/06/12 add desmond START
            if (!StringUtils.isEmpty(productModel.getLock()) && "1".equals(productModel.getLock())) {
                removeProductList.add(productModel);
                continue;
            }
            // 2016/06/12 add desmond END
            // 2016/09/13 add desmond START
            // 看一下feed的品牌，master的品牌和platform的品牌这3个地方的品牌是否在品牌黑名单中，只有有一个在黑名单中，该产品就是上新对象外
            // 官网同购的场合（cart是30， 31）， platform的品牌是不存在的
            if (cmsBtBrandBlockService.isBlocked(channelId, cartId,
                    sxData.getCmsBtFeedInfoModel() == null ? "" : sxData.getCmsBtFeedInfoModel().getBrand(),
                    (productModel.getCommon() == null || productModel.getCommon().getFields() == null) ? "" : productModel.getCommon().getFields().getBrand(),
                    productPlatformCart == null ? "" : productPlatformCart.getpBrandId())) {
                removeProductList.add(productModel);
                continue;
            }
            // 2016/09/13 add desmond END

            // modified by morse.lu 2016/06/15 start
            // TODO:{}1中的这段暂时不要，临时用{}2，以后恢复
            {
                // {}1
//                // added by morse.lu 2016/06/13 start
//                if (CartEnums.Cart.TM.getId().equals(cartId.toString())
//                        || CartEnums.Cart.TB.getId().equals(cartId.toString())
//                        || CartEnums.Cart.TG.getId().equals(cartId.toString())) {
//                    // 天猫(淘宝)平台的时候，从外面的skus那里取得（天猫以后也会变成else分支那样的结构）
//                    // added by morse.lu 2016/06/13 end
//                    List<CmsBtProductModel_Sku> productModelSku = productModel.getSkus();
//                    List<CmsBtProductModel_Sku> skus = new ArrayList<>(); // 该product下，允许在该平台上上架的sku
//                    productModelSku.forEach(sku -> {
//                        if (sku.getSkuCarts().contains(cartId)) {
//                            skus.add(sku);
//                        }
//                    });
//
//                    if (skus.size() > 0) {
//                        productModel.setSkus(skus);
//                        skuList.addAll(skus);
//                    } else {
//                        // 该product下没有允许在该平台上上架的sku
//                        removeProductList.add(productModel);
//                    }
//                    // added by morse.lu 2016/06/13 start
//                } else {
                    // 天猫以外平台的时候（天猫以后也会变成这样的结构）
                    // added by morse.lu 2016/06/15 start
                    Map<String, BaseMongoMap<String, Object>> mapProductModelSku = new HashMap<>();
                    // modified by morse.lu 2016/06/24 start
//                    List<CmsBtProductModel_Sku> productModelSku = productModel.getSkus();
                    List<CmsBtProductModel_Sku> productModelSku = productModel.getCommon().getSkus();
                    // modified by morse.lu 2016/06/24 end
                    productModelSku.forEach(sku -> {
                        CmsBtProductModel_Sku copySku = new CmsBtProductModel_Sku();
                        copySku.putAll(sku);
//                        BeanUtils.copyProperties(sku, copySku);
                        mapProductModelSku.put(sku.getSkuCode(), copySku);
                    });
                    // added by morse.lu 2016/06/15 end
                    List<BaseMongoMap<String, Object>> productPlatformSku = productModel.getPlatform(cartId).getSkus();
                    List<BaseMongoMap<String, Object>> skus = new ArrayList<>(); // 该product下，允许在该平台上上架的sku
                    List<String> listSkuCode = new ArrayList<>();
                    // 分平台下面的skuCode未在共通sku(common.skus)下找到对应sku的code列表
                    List<String> notFoundSkuCodes = new ArrayList<>();
                    if (productPlatformSku != null) {
                        productPlatformSku.forEach(sku -> {
                            // 聚美以外的平台需要看PXX.skus.isSale是否等于true(该sku是否在当前平台销售),聚美不用过滤掉isSale=false的sku(聚美上新的时候false时会把它更新成隐藏)
                            if ((!CartEnums.Cart.JM.getId().equals(cartId.toString()) && Boolean.parseBoolean(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.isSale.name())))
                                    || CartEnums.Cart.JM.getId().equals(cartId.toString())) {
                                // modified by morse.lu 2016/06/15 start
//                            skus.add(sku);
                                // 外面skus的共通属性 + 从各个平台下面的skus(platform.skus)那里取得的属性
                                // 以防万一，如果各个平台下面的skus，有和外面skus共通属性一样的属性，那么是去取各个平台下面的skus属性，即把外面的值覆盖
                                BaseMongoMap<String, Object> mapSku = mapProductModelSku.get(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
                                // update by desmond 2016/08/03 start
                                if (mapSku != null && mapSku.size() > 0) {
                                    mapSku.putAll(sku); // 外面skus是共通属性 + 从各个平台下面的skus
                                    skus.add(mapSku);
                                    // modified by morse.lu 2016/06/15 end
                                    listSkuCode.add(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
                                } else {
                                    // 各个平台下面的skuCode没有在共通skus里面找到对应的sku的时候
                                    notFoundSkuCodes.add(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
                                }
                                // update by desmond 2016/08/03 end
                            }
                        });
                    }
                    // add by desmond 2016/08/03 start
                    // 如果分平台下面的skuCode没找到对应的共通skuCode则报错
                    if (ListUtils.notNull(notFoundSkuCodes)) {
                        String strNotFoundSkus = notFoundSkuCodes.stream().collect(Collectors.joining(","));
                        // 该商品对应的feed信息不存在时，暂时的做法就是跳过当前记录， 这个group就不上了
                        String errorMsg = "取得上新数据(SxData)失败! 分平台sku件数与共通sku件数不一致，有些分平台skuCode没有找到" +
                                "对应的共通sku (ProductCode:"+ productModel.getCommon().getFields().getCode() + " 未找到的分平台skuCode:" + strNotFoundSkus + ")";
                        $error(errorMsg);
                        sxData.setErrorMessage(errorMsg);
                        break;
                    }
                    // add by desmond 2016/08/03 end

                    if (!skus.isEmpty()) {
//                        productModel.getPlatform(cartId).setSkus(skus); // 只留下允许在该平台上上架的sku，且属性为：外面skus的共通属性 + 从各个平台下面的skus的属性
                        Iterator<BaseMongoMap<String, Object>> iterator1 = productPlatformSku.iterator();
                        while (iterator1.hasNext()) {
                            BaseMongoMap<String, Object> platSku = iterator1.next();
                            if (!listSkuCode.contains(platSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))) {
                                iterator1.remove();
                            }
                        }
                        Iterator<CmsBtProductModel_Sku> iterator2 = productModelSku.iterator();
                        while (iterator2.hasNext()) {
                            CmsBtProductModel_Sku comSku = iterator2.next();
                            if (!listSkuCode.contains(comSku.getSkuCode())) {
                                iterator2.remove();
                            }
                        }
                        skuList.addAll(skus);
                    } else {
                        // 该product下没有允许在该平台上上架的sku
                        removeProductList.add(productModel);
                    }
//                }
                // added by morse.lu 2016/06/13 end
            }
//            {
//                // {}2
//                // 暂时sku不判断是不是能在该平台上上架，且都从外面的skus里取
//                skuList.addAll(productModel.getSkus());
//            }
            // modified by morse.lu 2016/06/15 end
        }

        // Add by desmond 2016/06/12 start
        if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
            // 有错误(例如feed信息不存在)的时候，直接返回
            return sxData;
        }
        // Add by desmond 2016/06/12 end

        removeProductList.forEach(productModelList::remove);
        // added by morse.lu 2016/06/12 start
        if (productModelList.isEmpty()) {
            // 没有对象
            // update by desmond 2016/07/12 start
//            return null;
            String errorMsg = "取得上新数据(SxData)失败! 在产品表中没找到groupId(" + groupId + ")" +
                    "下面有效的产品，请确保产品未被锁定,已完成审批且品牌不在黑名单中.";
            $error(errorMsg);
            sxData.setErrorMessage(errorMsg);
            return sxData;
            // update by desmond 2016/07/12 end
        }
        // added by morse.lu 2016/06/12 end

        // 20161020 tom 判断是否是隔离库存的商品 START
        List<String> strSqlSkuList = new ArrayList<>();
        for (int i = 0; i < skuList.size(); i++) {
            strSqlSkuList.add(skuList.get(i).getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
        }
        int cnt = imsBtProductExceptDao.selectImsBtProductExceptByChannelCartSku(channelId, cartId, strSqlSkuList);
        if (cnt > 0) {
            String errorMsg = "取得上新数据(SxData)失败! 库存已经被隔离， 不能执行上新程序。groupId(" + groupId + "), main code(" + mainProductCode + ")";
            $error(errorMsg);
            sxData.setErrorMessage(errorMsg);
            return sxData;
        }
        // 20161020 tom 判断是否是隔离库存的商品 END

        // 20160707 tom 将上新用的size全部整理好, 放到sizeSx里, 并排序 START
        // 取得尺码转换信息
        Integer sizeChartId = null;
        if (!StringUtils.isEmpty(sxData.getMainProduct().getCommon().getFields().getSizeChart())) {
            sizeChartId = Integer.parseInt(sxData.getMainProduct().getCommon().getFields().getSizeChart());
        }
        Map<String, String> sizeMap = getSizeMap(
                channelId,
//                sxData.getMainProduct().getOrgChannelId(),
                sxData.getMainProduct().getCommon().getFields().getBrand(),
                sxData.getMainProduct().getCommon().getFields().getProductType(),
                sxData.getMainProduct().getCommon().getFields().getSizeType(),
                sizeChartId
        );

        // 20160805 这段有问题, 不要了 tom START
//        // 将skuList转成map用于sizeNick的方便检索， 将来sizeNike放到common里的话， 这段就不要了 START
//        Map<String, String> mapSizeNick = new HashMap<>();
//        for (BaseMongoMap<String, Object> sku : skuList) {
//            mapSizeNick.put(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()), sku.getStringAttribute("sizeNick"));
//        }
//        // 将skuList转成map用于sizeNick的方便检索， 将来sizeNike放到common里的话， 这段就不要了 END
//
//        // 防止同一个group里, 不同的product的sku的size使用了不同的sizeNick
//        Map<String, String> sizeSxMap = new HashMap<>();
//        // 优先mainProduct里的sizeNick
//        for (CmsBtProductModel_Sku sku : sxData.getMainProduct().getCommon().getSkus()) {
//            String sizeNick = mapSizeNick.get(sku.getSkuCode());
//            if (!StringUtils.isEmpty(sizeNick)) {
//                sizeSxMap.put(sku.getSize(), sizeNick);
//            }
//        }
//        // 然后把productList里的也一样的做一遍
//        for (CmsBtProductModel productModel : productModelList) {
//            // 遍历每个product里的sku
//            for (CmsBtProductModel_Sku sku : productModel.getCommon().getSkus()) {
//                // 已经设置过的size就不用再设置了
//                if (!sizeSxMap.containsKey(sku.getSize())) {
//                    String sizeNick = mapSizeNick.get(sku.getSkuCode());
//                    if (!StringUtils.isEmpty(sizeNick)) {
//                        sizeSxMap.put(sku.getSize(), sizeNick);
//                    }
//                }
//            }
//        }
        // 20160805 这段有问题, 不要了 tom END

        // 处理一下skuList的所有sizeSx尺码
        // modified by morse.lu 2016/07/08 start
        // 强转不了哦
//        for (BaseMongoMap<String, Object> skuMap : skuList) {
//			CmsBtProductModel_Sku sku = (CmsBtProductModel_Sku)skuMap;
//
//			if (sizeSxMap.containsKey(sku.getSize())) {
//                // 直接用Nick
//                sku.setSizeSx(sizeSxMap.get(sku.getSize()));
//			} else {
//                // 用size的尺码, 并转换
//                if (sizeMap.containsKey(sku.getSize())) {
//                    sku.setSizeSx(sizeMap.get(sku.getSize()));
//                } else {
//                    sku.setSizeSx(sku.getSize());
//                }
//                sizeSxMap.put(sku.getSize(), sku.getSizeSx());
//			}
//		}

        Map<String, String> sizeSxMap = new HashMap<>();
        for (BaseMongoMap<String, Object> sku : skuList) {
            String size = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.size.name());
            String sizeNick = sku.getStringAttribute("sizeNick");
            if (!StringUtils.isEmpty(sizeNick)) {
                // 直接用Nick
                sku.setStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name(), sizeNick);
			} else {
                // 用size的尺码, 并转换
                if (sizeMap.containsKey(size)) {
                    sku.setStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name(), sizeMap.get(size));
                } else {
                    sku.setStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name(), size);
                }
//                sizeSxMap.put(size, sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()));
			}
            sizeSxMap.put(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()), sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()));
        }
        // modified by morse.lu 2016/07/08 end

        // 把整理好的skuList里的sizeSx, 回写到mainProduct里的sizeSx 和 productList里的sizeSx里
        for (CmsBtProductModel_Sku sku : sxData.getMainProduct().getCommon().getSkus()) {
//            sku.setSizeSx(sizeSxMap.get(sku.getSize()));
            sku.setSizeSx(sizeSxMap.get(sku.getSkuCode()));
        }
        for (CmsBtProductModel productModel : productModelList) {
            for (CmsBtProductModel_Sku sku : productModel.getCommon().getSkus()) {
//                sku.setSizeSx(sizeSxMap.get(sku.getSize()));
                sku.setSizeSx(sizeSxMap.get(sku.getSkuCode()));
            }
        }

        // add by desmond 2016/20/26 start
        // 判断每个Product中是否有重复的sizeSx(有2个以上的sku用同一个sizeSx),如果有的话，上传到天猫/京东等平台会会报"销售属性重复"的错误，
        // 在这里直接报出异常，让运营到CMS画面去改尺码
        // sizeSx(设置优先顺序为CMS中的 容量/尺码 > platform size > size)
        StringBuilder duplicatedSizeSxErrMsg = new StringBuilder();
        for (CmsBtProductModel productModel : productModelList) {
            List<String> duplicatedSizeSxList = new ArrayList<>();
            for (CmsBtProductModel_Sku sku : productModel.getCommon().getSkus()) {
                String currentSizeSx = sku.getSizeSx();
                // 如果已经知道是有重复的sku就跳过
                if (duplicatedSizeSxList.contains(currentSizeSx)) {
                    continue;
                }

                // 如果当前sku.sizeSx在整个产品的skus中有重复的，则加入到重复duplicatedSizeSxList中
                if (productModel.getCommon().getSkus().stream().filter(s -> currentSizeSx.equals(s.getSizeSx())).count() > 1) {
                    duplicatedSizeSxList.add(currentSizeSx);
                }
            }

            // 如果有重复sizeSx的时候，报出异常，后面的上新不做了
            if (ListUtils.notNull(duplicatedSizeSxList)) {
                // Joiner如果对象列表中有null值的话，就会报Preconditions.checkNotNull的nullpointexception
                duplicatedSizeSxErrMsg.append( String.format("产品(%s)的多个sku的上新用尺码sizeSx(CMS系统中的" +
                        "\"容量/尺码\">\"platform size\">\"size\")都是重复值(\"%s\"), 同一个产品中" +
                        "如果多个sku使用同一个上新用尺码时，上新时会报\"销售属性重复\"的错误,请修改成不同的尺码值; ",
                        productModel.getCommon().getFields().getCode(), Joiner.on(",").join(duplicatedSizeSxList)));
            }
        }
        // 如果有重复的上新用尺码值，则报错,后面的上新不做了
        if (!StringUtils.isEmpty(duplicatedSizeSxErrMsg.toString())) {
            $error(duplicatedSizeSxErrMsg.toString());
            sxData.setErrorMessage(duplicatedSizeSxErrMsg.toString());
            return sxData;
        }
        // add by desmond 2016/20/26 end

        // 排序 (根据SizeSx来进行排序)
        for (CmsBtProductModel productModel : productModelList) {
            sortSkuInfo(productModel.getCommon().getSkus());
            sortListBySkuCode(productModel.getPlatform(sxData.getCartId()).getSkus(),
                    productModel.getCommon().getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList()));
        }
        // skuList也排序一下
        sortSkuInfo(skuList);

        // 20160707 tom 将上新用的size全部整理好, 放到sizeSx里, 并排序 END

        sxData.setProductList(productModelList);
        sxData.setSkuList(skuList);

        return sxData;
    }

    /**
     * 根据code获取SxData
     * 仅仅为了做成一个SxData类型返回，用于各种别的需要SxData的逻辑
     * 所以，尽量 不做任何上新check，不做任何多余处理，
     * 入参的平台id也只用于检索group表且允许group信息没有，而各个平台值可能不同，因此
     *      SxData里的skuList是空，没必要再去塞一遍
     *      sizeSx也不设值了，所以sku根据size排序也不做了
     *
     * @param needGetGroupCodes 是否获取group下所有code信息，如果false不获取的话，那么主商品code认为是入参的code，SxData里的值也只有入参的code的信息
     */
    public SxData getSxDataByCodeWithoutCheck(String channelId, int cartId, String code, boolean needGetGroupCodes) {
        SxData sxData = new SxData();
        sxData.setChannelId(channelId);
        sxData.setCartId(cartId);

        // 获取group信息
        CmsBtProductGroupModel grpModel = cmsBtProductGroupDao.selectOneWithQuery("{\"productCodes\": \"" + code + "\"," + "\"cartId\":" + cartId + "}", channelId);
        if (grpModel == null) {
            String errMsg = "取得上新数据(SxData)失败!没找到对应的group数据!";
            $warn(errMsg);
            sxData.setGroupId(-1L); // 可能报错需要用到，就塞个-1，不要让它为空
            if (needGetGroupCodes) {
                sxData.setErrorMessage(errMsg);
                return sxData;
            }
        } else {
            sxData.setGroupId(grpModel.getGroupId());
            sxData.setPlatform(grpModel);
        }

        // 主商品code
        String mainProductCode = code;
        if (needGetGroupCodes) {
            mainProductCode = grpModel.getMainProductCode();
        }

        // 该group下的所有code
        List<String> productCodeList;
        if (needGetGroupCodes) {
            productCodeList = grpModel.getProductCodes();
        } else {
            productCodeList = new ArrayList<>();
            productCodeList.add(code);
        }
        String[] codeArr = new String[productCodeList.size()];
        codeArr = productCodeList.toArray(codeArr);

        // 通过上面取得的code，得到对应的产品信息，以及sku信息
        List<CmsBtProductModel> productModelList = cmsBtProductDao.select("{" + MongoUtils.splicingValue("common.fields.code", codeArr, "$in") + "}", channelId);
        if (ListUtils.isNull(productModelList)) {
            String errMsg = "取得上新数据(SxData)失败!没找到对应的code数据!";
            $warn(errMsg);
            sxData.setErrorMessage(errMsg);
            return sxData;
        }
        sxData.setProductList(productModelList);

        for (CmsBtProductModel productModel : productModelList) {
            if (mainProductCode.equals(productModel.getCommon().getFields().getCode())) {
                // 主商品
                sxData.setMainProduct(productModel);

                Map<String, Object> searchParam = new HashMap<>();
                searchParam.put("channelId", channelId);
                searchParam.put("cartId", cartId);
                searchParam.put("cmsBrand", productModel.getCommon().getFields().getBrand());
                CmsMtBrandsMappingModel cmsMtBrandsMappingModel = cmsMtBrandsMappingDao.selectOne(searchParam);
                if (cmsMtBrandsMappingModel != null) {
                    sxData.setBrandCode(cmsMtBrandsMappingModel.getBrandId());
                }

                String orgChannelId = productModel.getOrgChannelId(); // feed信息要从org里获取
                String prodOrgCode = productModel.getCommon().getFields().getOriginalCode(); // 有可能会有原始code
                if (prodOrgCode == null) prodOrgCode = productModel.getCommon().getFields().getCode();
                CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode(orgChannelId, prodOrgCode);
                if (feedInfo == null) {
                    // 该商品对应的feed信息不存在，很多地方需要用到feed信息，所以错误信息先塞进sxData里面
                    String errMsg = "取得上新数据(SxData)失败! 该商品对应的feed信息不存在(OriginalCode/Code=" + prodOrgCode + ")";
                    $error(errMsg);
                    sxData.setErrorMessage(errMsg);
                } else {
                    sxData.setCmsBtFeedInfoModel(feedInfo);
                }
            }
        }

        return sxData;
    }

    /**
     * 是否为智能上新（智能上新的场合， 无需approve， 只需要sx_workload表里有这条记录就会上新）
     * （但是被锁住的记录， 不想上新的sku， 那些内容仍然会被剔除了， 只不过无需approve而已）
     * @param channelId channel id
     * @param cartId cart id
     * @return 是否为智能上新
     */
    public boolean isSmartSx(String channelId, int cartId) {
        // 目前只支持京东系的上新
        if (!CartEnums.Cart.isJdSeries(CartEnums.Cart.getValueByID(String.valueOf(cartId)))) { return false; }

        // 获取当前channel的配置
        CmsChannelConfigBean sxSmartConfig = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.SX_SMART, String.valueOf(cartId));

        String sxSmart = null;
        if (sxSmartConfig != null) {
            sxSmart = org.apache.commons.lang3.StringUtils.trimToNull(sxSmartConfig.getConfigValue1());
        }

        // 如果没有配置， 那就不做智能上新。 如果不为1， 那么就不做智能上新
        if (StringUtils.isEmpty(sxSmart) || !"1".equals(sxSmart)) {
            return false;
        }

        return true;
    }

    /**
     * 新版上新schema设值
     * Step1:custom值
     * Step2:Mapping
     * Step3:schema的上记Step1,2以外的全部field
     *
     * @param fields List<Field> 直接把值set进这个fields对象
     * @param cmsMtPlatformMappingModel CmsMtPlatformMappingDeprecatedModel
     * @param shopBean ShopBean
     * @param expressionParser ExpressionParser
     * @param user 上传图片用
     * @param isItem true：商品 false：产品
     * @return Map<field_id mt里转换后的值> （只包含叶子节点，即只包含简单类型，对于复杂类型，也只把复杂类型里的简单类型值put进Map，只为了外部可以不用再循环取值，只需要根据已知的field_id，取得转换后的值）
     * @throws Exception
     */
    public Map<String, Field> constructMappingPlatformProps(List<Field> fields, CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel, ShopBean shopBean, ExpressionParser expressionParser, String user, boolean isItem) throws Exception {
        Map<String, Field> retMap = null;
        SxData sxData = expressionParser.getSxData();

        Map<String, Field> fieldsMap = new HashMap<>();
        for (Field field : fields) {
            fieldsMap.put(field.getId(), field);
        }

        // 特殊字段Map<CartId, Map<propId, 对应mapping项目或者处理(未定)>>
        //Map<Integer, Map<String, Object>> mapSpAll = new HashMap<>();

//        Map<String, Object> mapSp = mapSpAll.get(shopBean.getCart_id());
        Map<String, Object> mapSp = new HashMap<>();

        // Step1:custom
        // 暂时除sku的Mapping改成不Mapping以外的逻辑都不修正 morse.lu 2016-06-24
        Map<CustomMappingType, List<Field>> mappingTypePropsMap = getCustomPlatformProps(fieldsMap, expressionParser, mapSp, isItem);
        if (!mappingTypePropsMap.isEmpty()) {
            // 所有sku取得
            List<String> skus = new ArrayList<>();
            for (CmsBtProductModel productModel : sxData.getProductList()) {
                skus.addAll(productModel.getCommon().getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList()));
            }
            // wms逻辑库存取得
            List<WmsBtInventoryCenterLogicModel> skuInventoryList = wmsBtInventoryCenterLogicDao.selectItemDetailBySkuList(sxData.getChannelId(), skus);
            Map<String, Integer> skuInventoryMap = new HashMap<>();
            for (WmsBtInventoryCenterLogicModel model : skuInventoryList) {
                skuInventoryMap.put(model.getSku(), model.getQtyChina());
            }

            Map<String, Field> resolveField = constructCustomPlatformProps(mappingTypePropsMap, expressionParser, cmsMtPlatformMappingModel, skuInventoryMap, shopBean, user);
            if (!resolveField.isEmpty()) {
                retMap = new HashMap<>();
                retMap.putAll(resolveField);
            }
        }

        // Step2:Mapping
        // 改成循环Mapping
        if (cmsMtPlatformMappingModel != null) {
            List<MappingBean> propMapings = cmsMtPlatformMappingModel.getProps();

            for (MappingBean mappingBean : propMapings) {
                // modified by morse.lu 2016/06/24 start
//            mapProp.put(mappingBean.getPlatformPropId(), mappingBean);
                Field field = fieldsMap.get(mappingBean.getPlatformPropId());
                if (field == null) {
                    continue;
                }
                mapSp.put(field.getId(), field);
                // deleted by morse.lu 2016/07/04 start
                // hscode不做Mapping了，写死从个人税号里去取
//            if ("hscode".equals(field.getId())) {
//                // HS海关代码
//                if (!sxData.isHasSku()) {
//                    RuleExpression ruleExpression = ((SimpleMappingBean)mappingBean).getExpression();
//                    String propValue = expressionParser.parse(ruleExpression, shopBean, user, null); // "0410004300, 戒指 ,对" 或者  "0410004300, 戒指 ,只"
//                    ((InputField) field).setValue(propValue.split(",")[0]);
//                    retMap.put(field.getId(), field);
//                }
//                continue;
//            }
                // deleted by morse.lu 2016/07/04 end
                Map<String, Field> resolveField = resolveMapping(mappingBean, field, shopBean, expressionParser, user);
                if (resolveField != null) {
                    if (retMap == null) {
                        retMap = new HashMap<>();
                    }
                    retMap.putAll(resolveField);
                }
                // modified by morse.lu 2016/06/24 end
            }
        }

        // Step3:schema的上记Step1,2以外的全部field
        for (Field field : fields) {
            if (mapSp.containsKey(field.getId())) {
                // 特殊字段 + Mapping字段
                continue;
            } else {
                // 直接取product表的fields的值
                // added by morse.lu 2016/07/04 start
                // hscode不做Mapping了，写死从个人税号里去取
                if ("hscode".equals(field.getId())) {
                    // HS海关代码
                    if (!sxData.isHasSku()) {
                        String propValue = sxData.getMainProduct().getCommon().getFields().getHsCodePrivate(); // "0410004300, 戒指 ,对" 或者  "0410004300, 戒指 ,只"
                        ((InputField) field).setValue(propValue.split(",")[0]);
                        retMap.put(field.getId(), field);
                    }
                    continue;
                }
                // added by morse.lu 2016/07/04 end
                // modified by morse.lu 2016/06/24 start
//                MappingBean mappingBean = mapProp.get(field.getId());
//                if (mappingBean == null) {
//                    continue;
//                }
//                Map<String, Field> resolveField = resolveMapping(mappingBean, field, shopBean, expressionParser, user);
                Map<String, Field> resolveField = resolveMappingFromProductField(field, shopBean, expressionParser, user);
                // modified by morse.lu 2016/06/24 end
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

	/**
     * [ 预判断 ] 设置京东属性 - [价格][价位]
     * 注意: 这里不是设置真正的价格, 而是设置价格区间用的
     * @param field 字段的内容
     * @return 返回的字段
     */
    public boolean resolveJdPriceSection_before(ShopBean shopBean, Field field) {
        String strRex1 = "\\s*\\d+-\\d+\\s*元*";
        String strRex2 = "\\s*\\d+\\s*元*以上";
        String strRex3 = "其它";
        String strRex4 = "不限";

        // 如果不是京东京东国际的话, 返回false
        if (!shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JD.getId())) {
            return false;
        }

        // 属性名字必须是指定内容
        if (!"价格".equals(field.getName())
                && !"价位".equals(field.getName())
                ) {
            return false;
        }

        // 判断类型
        if (field.getType() != FieldTypeEnum.SINGLECHECK) {
            return false;
        }

        // 遍历所有可选项, 判断是否符合规则
        SingleCheckField singleCheckField = (SingleCheckField) field;
        for (Option option : singleCheckField.getOptions()) {
            // 判断一下当前可选项属于哪类
            boolean blnError = false;
            String optionDisplayName = option.getDisplayName();

            {
                Pattern pattern = Pattern.compile(strRex1, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(optionDisplayName);
                if (matcher.find()) {
                    blnError = true;
                }
            }

            if (!blnError) {
                Pattern pattern = Pattern.compile(strRex2, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(optionDisplayName);
                if (matcher.find()) {
                    blnError = true;
                }
            }

            if (!blnError) {
                if (optionDisplayName.equals(strRex3) ||
                        optionDisplayName.equals(strRex4)) {
                    blnError = true;
                }
            }

            if (!blnError) {
                return false;
            }

        }

        return true;
    }

    /**
     * 设置京东属性 - [价格][价位]
     * 注意: 这里不是设置真正的价格, 而是设置价格区间用的
     * @param field 字段的内容
     * @param sxData 商品信息之类的
     * @return 返回的字段
     */
    public Map<String, Field> resolveJdPriceSection(Field field, SxData sxData) {
        Map<String, Field> retMap = new HashMap<>();

        // 使用最大的那个价格进行判断
        Double jdPrice = sxData.getMaxPrice();

        // 遍历所有可选项进行判断
        SingleCheckField singleCheckField = (SingleCheckField) field;
        for (Option option : singleCheckField.getOptions()) {
            // 判断一下当前可选项属于哪类
            String optionDisplayName = option.getDisplayName();
            if (optionDisplayName.contains("-")) {
                // 专门处理这类的内容: 100-199元
                optionDisplayName = optionDisplayName.replace("元", "");
                optionDisplayName = optionDisplayName.trim();

                String[] splitOption = optionDisplayName.split("-");
                splitOption[0] = splitOption[0].trim();
                splitOption[1] = splitOption[1].trim();

                Double minPrice = Double.parseDouble(splitOption[0]);
                Double maxPrice = Double.parseDouble(splitOption[1]);

                if (Double.compare(minPrice, jdPrice) > 0) {
                    // 不符合
                    continue;
                }
                if (Double.compare(maxPrice, jdPrice) < 0) {
                    // 不符合
                    continue;
                }

                // 符合的场合
                singleCheckField.setValue(option.getValue());
                retMap.put(field.getId(), singleCheckField);

                break;
            } else if (optionDisplayName.contains("以上")) {
                // 专门处理这类的内容: 500以上
                // 专门处理这类的内容: 500元以上
                optionDisplayName = optionDisplayName.replace("以上", "");
                optionDisplayName = optionDisplayName.replace("元", "");
                optionDisplayName = optionDisplayName.trim();

                Double minPrice = Double.parseDouble(optionDisplayName);
                if (Double.compare(minPrice, jdPrice) > 0) {
                    // 不符合
                    continue;
                }

                // 符合的场合
                singleCheckField.setValue(option.getValue());
                retMap.put(field.getId(), singleCheckField);

                break;
            } else {
                // 按理说没有其他的场合了, 如果有的话就有问题了, 这个在预处理里就应该判断掉
            }
        }

        return retMap;
    }

    /**
     * [ 预判断 ] 设置京东属性 - [品牌]
     * 注意: 通过这里统一设置品牌属性，这样运营就不用再设置品牌信息了
     * @param shopBean ShopBean 店铺信息
     * @param field Field 字段的内容
     * @return 是否是品牌属性
     */
    public boolean resolveJdBrandSection_before(ShopBean shopBean, Field field) {

        // 如果不是京东京东国际的话, 返回false
        if (!shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JD.getId())) {
            return false;
        }

        // 属性名字必须是指定内容
        if (!"品牌".equals(field.getName())) {
            return false;
        }

        // 判断类型
        if (field.getType() != FieldTypeEnum.SINGLECHECK) {
            return false;
        }

        return true;
    }

    /**
     * 设置京东属性 - [品牌]
     * 注意: 这里不是设置真正的价格, 而是设置价格区间用的
     * @param field 字段的内容
     * @param sxData 商品信息之类的
     * @return 返回的字段
     */
    public Map<String, Field> resolveJdBrandSection(Field field, SxData sxData) {
        Map<String, Field> retMap = new HashMap<>();

        SingleCheckField singleCheckField = (SingleCheckField) field;
        // 取得上新数据中设置的品牌code直接设置
        singleCheckField.setValue(sxData.getBrandCode());
        retMap.put(field.getId(), singleCheckField);

        return retMap;
    }

    public Map<String, Field> resolveMapping(MappingBean mappingBean, Field field, ShopBean shopBean, ExpressionParser expressionParser, String user) throws Exception {
        Map<String, Field> retMap = null;

        if (MappingBean.MAPPING_SIMPLE.equals(mappingBean.getMappingType())) {
            retMap = new HashMap<>();
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
            retMap = new HashMap<>();
            CmsBtProductModel mainProduct = expressionParser.getSxData().getMainProduct();
            ComplexMappingBean complexMappingBean = (ComplexMappingBean) mappingBean;
            if (field.getType() == FieldTypeEnum.COMPLEX) {
                // modified by morse.lu 2016/07/01 start
//                Map<String, Object> masterWordEvaluationContext = (Map<String, Object>) mainProduct.getCommon().getFields().get(complexMappingBean.getMasterPropId());
                Map<String, Object> masterWordEvaluationContext;
                try {
                    // 从各自平台fields里去取
                    masterWordEvaluationContext = (Map<String, Object>) mainProduct.getPlatform(expressionParser.getSxData().getCartId()).getFields().get(complexMappingBean.getMasterPropId());
                    if (masterWordEvaluationContext == null) {
                        // 各自平台fields里取不到，从common的fields里去取
                        masterWordEvaluationContext = (Map<String, Object>) mainProduct.getCommon().getFields().get(complexMappingBean.getMasterPropId());
                    }
                } catch (ClassCastException ex) {
                    $error(String.format("类型不正确,[field_id=%s]Complex类型,但product的fields里对应的值不是Map!", complexMappingBean.getMasterPropId()));
                    masterWordEvaluationContext = null;
                }
                // modified by morse.lu 2016/07/01 end

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
                // modified by morse.lu 2016/07/01 start
//                List<Map<String, Object>> masterWordEvaluationContexts = (List<Map<String, Object>>) mainProduct.getCommon().getFields().get(complexMappingBean.getMasterPropId());
                List<Map<String, Object>> masterWordEvaluationContexts = null;
                try {
                    // 没有设过一层，或者当前层没有对应的值，那么从各自平台fields里去取
                    masterWordEvaluationContexts = (List<Map<String, Object>>) mainProduct.getPlatform(expressionParser.getSxData().getCartId()).getFields().get(complexMappingBean.getMasterPropId());
                    if (masterWordEvaluationContexts == null) {
                        // 各自平台fields里取不到，从common的fields里去取
                        masterWordEvaluationContexts = (List<Map<String, Object>>) mainProduct.getCommon().getFields().get(complexMappingBean.getMasterPropId());
                    }
                } catch (ClassCastException ex) {
                    $error(String.format("类型不正确,[field_id=%s]MultiComplex类型,但product的fields里对应的值不是List!", complexMappingBean.getMasterPropId()));
                    masterWordEvaluationContexts = null;
                }
                // modified by morse.lu 2016/07/01 end

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
            retMap = new HashMap<>();
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

    /**
     * 新版上新Field设值，不再根据Mapping，直接从product表platform下fields里取值
     * 做一个假的MASTER类型的WordParse的方式去处理
     */
    public Map<String, Field> resolveMappingFromProductField(Field field, ShopBean shopBean, ExpressionParser expressionParser, String user) throws Exception {
        Map<String, Field> retMap = null;
        SxData sxData = expressionParser.getSxData();

        switch (field.getType()) {
            case INPUT: {
                retMap = new HashMap<>();
                String expressionValue = getProductValueByMasterMapping(field, shopBean, expressionParser, user);
                if (null == expressionValue) {
                    return null;
                }

                InputField inputField = (InputField) field;
                inputField.setValue(expressionValue);
                retMap.put(field.getId(), inputField);
                break;
            }
            case SINGLECHECK: {
                retMap = new HashMap<>();
                String expressionValue = getProductValueByMasterMapping(field, shopBean, expressionParser, user);
                if (null == expressionValue) {
                    return null;
                }

                SingleCheckField singleCheckField = (SingleCheckField) field;
                singleCheckField.setValue(expressionValue);
                retMap.put(field.getId(), singleCheckField);
                break;
            }
            case MULTIINPUT:
                break;
            case MULTICHECK: {
                retMap = new HashMap<>();
                String expressionValue = getProductValueByMasterMapping(field, shopBean, expressionParser, user);
                if (null == expressionValue) {
                    return null;
                }

                String[] valueArrays = ExpressionParser.decodeString(expressionValue);

                MultiCheckField multiCheckField = (MultiCheckField)field;
                for (String val : valueArrays) {
                    multiCheckField.addValue(val);
                }
                retMap.put(field.getId(), multiCheckField);

                break;
            }
            case COMPLEX: {
                retMap = new HashMap<>();
                // modified by morse.lu 2016/07/13 start
                // 把field_id中的【.】替换成【->】
//                String fieldId = field.getId();
                String fieldId = StringUtil.replaceDot(field.getId());
                // modified by morse.lu 2016/07/13 end
                Map<String, Object> masterWordEvaluationContext;
                try {
                    masterWordEvaluationContext = expressionParser.getLastMasterPropContext();
                    if (masterWordEvaluationContext != null) {
                        // 看看有没有上一层，有的话先从这层去取值
                        masterWordEvaluationContext = (Map<String, Object>) masterWordEvaluationContext.get(fieldId);
                    }
                    if (masterWordEvaluationContext == null) {
                        // 没有设过一层，或者当前层没有对应的值，那么从各自平台fields里去取
                        masterWordEvaluationContext = (Map<String, Object>) sxData.getMainProduct().getPlatform(sxData.getCartId()).getFields().get(fieldId);
                        if (masterWordEvaluationContext == null) {
                            // 各自平台fields里取不到，从common的fields里去取
                            masterWordEvaluationContext = (Map<String, Object>) sxData.getMainProduct().getCommon().getFields().get(fieldId);
                        }
                    }
                } catch (ClassCastException ex) {
                    $error(String.format("类型不正确,[field_id=%s]Complex类型,但product的fields里对应的值不是Map!", fieldId));
                    masterWordEvaluationContext = null;
                }

                if (masterWordEvaluationContext != null) {
                    // 找到的话，设置这一层Map
                    expressionParser.pushMasterPropContext(masterWordEvaluationContext);
                }

                ComplexField complexField = (ComplexField) field;
                ComplexValue complexValue = new ComplexValue();
                complexField.setComplexValue(complexValue);

                for (Field subField : complexField.getFields()) {
                    Field valueField = deepCloneField(subField);
                    Map<String, Field> res = resolveMappingFromProductField(valueField, shopBean, expressionParser, user);
                    if (res != null) {
                        retMap.putAll(res);
                    }
                    complexValue.put(valueField);
                }

                if (masterWordEvaluationContext != null) {
                    // 这一层结束
                    expressionParser.popMasterPropContext();
                }
                break;
            }
            case MULTICOMPLEX: {
                retMap = new HashMap<>();
                // modified by morse.lu 2016/07/13 start
                // 把field_id中的【.】替换成【->】
//                String fieldId = field.getId();
                String fieldId = StringUtil.replaceDot(field.getId());
                // modified by morse.lu 2016/07/13 end
                List<Map<String, Object>> masterWordEvaluationContexts = null;
                try {
                    Map<String, Object> masterWordEvaluationContext = expressionParser.getLastMasterPropContext();
                    if (masterWordEvaluationContext != null) {
                        // 看看有没有上一层，有的话先从这层去取值
                        masterWordEvaluationContexts = (List<Map<String, Object>>) masterWordEvaluationContext.get(fieldId);
                    }
                    if (masterWordEvaluationContexts == null) {
                        // 没有设过一层，或者当前层没有对应的值，那么从各自平台fields里去取
                        masterWordEvaluationContexts = (List<Map<String, Object>>) sxData.getMainProduct().getPlatform(sxData.getCartId()).getFields().get(fieldId);
                        if (masterWordEvaluationContexts == null) {
                            // 各自平台fields里取不到，从common的fields里去取
                            masterWordEvaluationContexts = (List<Map<String, Object>>) sxData.getMainProduct().getCommon().getFields().get(fieldId);
                        }
                    }
                } catch (ClassCastException ex) {
                    $error(String.format("类型不正确,[field_id=%s]MultiComplex类型,但product的fields里对应的值不是List!", fieldId));
                    masterWordEvaluationContexts = null;
                }

                if (masterWordEvaluationContexts == null) {
                    $error("No value found for MultiComplex field: " + field.getId());
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

                    for (Field subField : multiComplexField.getFields()) {
                        Field valueField = deepCloneField(subField);
                        Map<String, Field> res = resolveMappingFromProductField(valueField, shopBean, expressionParser, user);
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
                break;
            }
            case LABEL:
                break;
            default:
                return null;
        }

        return retMap;
    }

    /**
     * 取product表platform下fields里的数据
     * 直接create一个Master的RuleExpression的方式去做
     */
    public String getProductValueByMasterMapping(Field field, ShopBean shopBean, ExpressionParser expressionParser, String user) throws Exception {
        // modified by morse.lu 2016/10/18 start
//        RuleExpression rule = new RuleExpression();
//        // modified by morse.lu 2016/07/13 start
//        // 把field_id中的【.】替换成【->】
////        MasterWord masterWord = new MasterWord(field.getId());
//        MasterWord masterWord = new MasterWord(StringUtil.replaceDot(field.getId()));
//        // modified by morse.lu 2016/07/13 end
//        rule.addRuleWord(masterWord);
//        return expressionParser.parse(rule, shopBean, user, null);
        return getProductValueByMasterMapping(StringUtil.replaceDot(field.getId()), shopBean, expressionParser, user);
        // modified by morse.lu 2016/10/18 end
    }

    /**
     * 取product表platform下fields里的数据
     * 直接create一个Master的RuleExpression的方式去做
     */
    public String getProductValueByMasterMapping(String field_id, ShopBean shopBean, ExpressionParser expressionParser, String user) throws Exception {
        RuleExpression rule = new RuleExpression();
        MasterWord masterWord = new MasterWord(field_id);
        rule.addRuleWord(masterWord);
        return expressionParser.parse(rule, shopBean, user, null);
    }


    private Field deepCloneField(Field field) throws Exception {
        try {
            return SchemaReader.elementToField(field.toElement());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }


    /**
     * 特殊属性取得
     *
     * @param fieldsMap Map
     * @param expressionParser ExpressionParser
     * @param mapSp 特殊属性Map
     * @param isItem true：商品 false：产品
     * @return Map
     * @throws Exception
     */
    private Map<CustomMappingType, List<Field>> getCustomPlatformProps(Map<String, Field> fieldsMap, ExpressionParser expressionParser, Map<String, Object> mapSp, boolean isItem) throws Exception {
        SxData sxData = expressionParser.getSxData();

        //第一步，先从cms_mt_platform_prop_mapping从查找，该属性是否在范围，如果在，那么采用特殊处理
        List<CmsMtPlatformPropMappingCustomModel> cmsMtPlatformPropMappingCustomModels = cmsMtPlatformPropMappingCustomDao.selectList(new HashMap<String, Object>(){{put("cartId", sxData.getCartId());}});

        Map<CustomMappingType, List<Field>> mappingTypePropsMap = new HashMap<>();

        for (CmsMtPlatformPropMappingCustomModel model : cmsMtPlatformPropMappingCustomModels) {
            // add by morse.lu 2016/05/24 start
            if (!isItem && CustomMappingType.valueOf(model.getMappingType()) == CustomMappingType.SKU_INFO) {
                // 不是商品，是产品
                continue;
            }
            // add by morse.lu 2016/05/24 end
            Field field = fieldsMap.get(model.getPlatformPropId());
            if (field != null) {
                List<Field> mappingPlatformPropBeans = mappingTypePropsMap.get(CustomMappingType.valueOf(model.getMappingType()));
                if (mappingPlatformPropBeans == null) {
                    mappingPlatformPropBeans = new ArrayList<>();
                    mappingTypePropsMap.put(CustomMappingType.valueOf(model.getMappingType()), mappingPlatformPropBeans);
                }
                mappingPlatformPropBeans.add(field);
                mapSp.put(model.getPlatformPropId(), field);
            }
        }

        return mappingTypePropsMap;
    }

    /**
     * 特殊属性设值
     *
     * @param mappingTypePropsMap
     * @param expressionParser
     * @param cmsMtPlatformMappingModel
     * @param skuInventoryMap sku对应逻辑库存
     * @throws Exception
     */
    private Map<String, Field> constructCustomPlatformProps(Map<CustomMappingType, List<Field>> mappingTypePropsMap, ExpressionParser expressionParser, CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
        Map<String, Field> retMap = new HashMap<>();

        SxData sxData = expressionParser.getSxData();
        CmsBtProductModel mainSxProduct = sxData.getMainProduct();

        //品牌
        for (Map.Entry<CustomMappingType, List<Field>> entry : mappingTypePropsMap.entrySet()) {
            CustomMappingType customMappingType = entry.getKey();
            List<Field> processFields = entry.getValue();
            switch (customMappingType) {
                case BRAND_INFO: {
                    String brandCode = sxData.getBrandCode();
                    Field field = processFields.get(0);

                    if (field.getType() != FieldTypeEnum.SINGLECHECK) {
                        $error("tmall's brand field(" + field.getId() + ") must be singleCheck");
                    } else {
                        SingleCheckField singleCheckField = (SingleCheckField) field;
                        singleCheckField.setValue(brandCode);
                        retMap.put(field.getId(), singleCheckField);
                    }
                    break;
                }
                case SKU_INFO: {
                    int cartId = sxData.getCartId();

                    sxData.setHasSku(true);

                    String errorLog = "平台类目id是:" + sxData.getMainProduct().getPlatform(cartId).getpCatId() + ". groupId:" + sxData.getGroupId();

                    List<Field> allSkuFields = new ArrayList<>();
                    recursiveGetFields(processFields, allSkuFields);
                    AbstractSkuFieldBuilder skuFieldService = skuFieldBuilderService.getSkuFieldBuilder(cartId, allSkuFields);
                    if (skuFieldService == null) {
                        sxData.setErrorMessage("No sku builder find." + errorLog);
                        throw new BusinessException("No sku builder find." + errorLog);
                    }

                    skuFieldService.setCodeImageTemplate(resolveDict("属性图片模板",expressionParser,shopBean, user, null));

                    try {
                        List<Field> skuInfoFields = skuFieldService.buildSkuInfoField(allSkuFields, expressionParser, cmsMtPlatformMappingModel, skuInventoryMap, shopBean, user);
                        skuInfoFields.forEach(field -> retMap.put(field.getId(), field)); // TODO：暂时只存放最大的field（即sku，颜色扩展，size扩展）以后再改
                        // added by morse.lu 2016/07/18 start
                    } catch (BusinessException e) {
                        sxData.setErrorMessage(e.getMessage());
                        throw new BusinessException(e.getMessage());
                        // added by morse.lu 2016/07/18 end
                    } catch (Exception e) {
                        $warn(e.getMessage());
                        sxData.setErrorMessage("Can't build SkuInfoField." + errorLog);
                        throw new BusinessException("Can't build SkuInfoField." + errorLog);
                    }
                    break;
                }
                case DARWIN_SKU: {
                    int cartId = sxData.getCartId();

                    sxData.setHasSku(true);

                    String errorLog = "平台类目id是:" + sxData.getMainProduct().getPlatform(cartId).getpCatId() + ". groupId:" + sxData.getGroupId();

                    AbstractSkuFieldBuilder skuFieldService = new TmallGjSkuFieldBuilderImpl7();
                    skuFieldService.setDao(cmsMtPlatformPropSkuDao, cmsMtChannelSkuConfigDao);

                    List<Field> allSkuFields = new ArrayList<>();
                    recursiveGetFields(processFields, allSkuFields);

                    skuFieldService.setCodeImageTemplate(resolveDict("属性图片模板",expressionParser,shopBean, user, null));

                    try {
                        List<Field> skuInfoFields = skuFieldService.buildSkuInfoField(allSkuFields, expressionParser, cmsMtPlatformMappingModel, skuInventoryMap, shopBean, user);
                        skuInfoFields.forEach(field -> retMap.put(field.getId(), field));
                    } catch (BusinessException e) {
                        sxData.setErrorMessage(e.getMessage());
                        throw new BusinessException(e.getMessage());
                    } catch (Exception e) {
                        $warn(e.getMessage());
                        sxData.setErrorMessage("Can't build darwin_sku Field." + errorLog);
                        throw new BusinessException("Can't build darwin_sku Field." + errorLog);
                    }
                    break;
                }
                case IS_XINPIN:
                {
                    // 商品是否为新品
                    int cartId = sxData.getCartId();
                    if (processFields == null || processFields.size() != 1) {
                        throw new BusinessException("is_xinpin's platformProps must have only one prop!");
                    }

                    Field field = processFields.get(0);
                    if (field.getType() != FieldTypeEnum.SINGLECHECK) {
                        $error("is_xinpin's field(" + field.getId() + ") must be singleCheck");
                    } else {
                        SingleCheckField isXinpinField = (SingleCheckField) field;
                        // 商品是否为新品。只有在当前类目开通新品,并且当前用户拥有该类目下发布新品权限时才能设置is_xinpin为true，
                        // 否则设置true后会返回错误码:isv.invalid-permission:add-xinpin。
                        // 同时只有一口价全新的宝贝才能设置为新品，否则会返回错误码：isv.invalid-parameter:xinpin。
                        // 不设置该参数值或设置为false效果一致。
                        // 新品判断逻辑(第一次上新的时候，设为新品;更新的时候，如果当前时间距离首次上新时间<=60天时，设为新品，否则设为非新品)
                        if (isXinPin(mainSxProduct, cartId)) {
                            isXinpinField.setValue("true");
                        } else {
                            isXinpinField.setValue("false");
                        }
                        retMap.put(isXinpinField.getId(), isXinpinField);
                    }
                    break;
                }
                case PRICE_SECTION:
                {
                    if (processFields == null || processFields.size() != 1) {
                        throw new BusinessException("price_section's platformProps must have only one prop!");
                    }

                    Field field = processFields.get(0);
                    if (field.getType() != FieldTypeEnum.SINGLECHECK) {
                        $error("price_section's field(" + field.getId() + ") must be singleCheck");
                    } else {
                        SingleCheckField priceField = (SingleCheckField) field;
                        PriceSectionBuilder priceSectionBuilder = PriceSectionBuilder.createPriceSectionBuilder(priceField.getOptions());
                        double usePrice = sxData.getPlatform().getPriceSaleSt();

                        String priceSectionValue = priceSectionBuilder.getPriceOptionValue(usePrice);
                        priceField.setValue(priceSectionValue);
                        retMap.put(field.getId(), priceField);
                    }
                    break;
                }
                case TMALL_SERVICE_VERSION:
                {
                    if (processFields == null || processFields.size() != 1) {
                        throw new BusinessException("tmall service version's platformProps must have only one prop!");
                    }

                    Field field = processFields.get(0);
                    if (field.getType() != FieldTypeEnum.INPUT) {
                        $error("tmall service version's field(" + field.getId() + ") must be input");
                    } else {
                        InputField inputField = (InputField) field;
                        inputField.setValue("11100");
                        retMap.put(field.getId(), inputField);
                    }
                    break;
                }
                case TMALL_STYLE_CODE:
                {
                    if (processFields == null) {
                        throw new BusinessException("tmall style code's platformProps does not get!");
                    }
                    if (processFields.size() == 1) {
                        Field field = processFields.get(0);
                        if (field.getType() != FieldTypeEnum.INPUT) {
                            $error("tmall style code's field(" + field.getId() + ") must be input");
                        } else {
                            InputField inputField = (InputField) field;
                            String styleCode = sxData.getStyleCode();
                            if (StringUtils.isEmpty(styleCode)) {
                                styleCode = generateStyleCode(sxData);
                            }
                            inputField.setValue(styleCode);
                            $debug("tmall style code[" + field.getId() + "]: " + field.getValue());
                            retMap.put(field.getId(), inputField);
                        }
                    } else if (processFields.size() == 2) {
                        for (Field processField : processFields) {
                            if (processField.getType() == FieldTypeEnum.SINGLECHECK) {
                                // prop_13021751（货号）值设为-1(表示其他）
                                SingleCheckField field = (SingleCheckField) processField;
                                field.setValue("-1");
                                retMap.put(processField.getId(), field);
                            } else {
                                // in_prop_13021751其他的货号值填货号
                                if (processField.getType() != FieldTypeEnum.INPUT) {
                                    $error("tmall style code's field(" + processField.getId() + ") must be input");
                                } else {
                                    InputField field = (InputField) processField;
                                    String styleCode = sxData.getStyleCode();
                                    if (StringUtils.isEmpty(styleCode)) {
                                        styleCode = generateStyleCode(sxData);
                                    }
                                    field.setValue(styleCode);
                                    retMap.put(processField.getId(), field);
                                }
                            }
                        }
                    } else {
                        throw new BusinessException("tmall style code's platformProps must have only one or two props!");
                    }
                    break;
                }
                case TMALL_ITEM_QUANTITY:
                {
                    if (processFields == null || processFields.size() != 1) {
                        throw new BusinessException("tmall item quantity's platformProps must have only one prop!");
                    }

                    Field field = processFields.get(0);
                    if (field.getType() != FieldTypeEnum.INPUT) {
                        $error("tmall item quantity's field(" + field.getId() + ") must be input");
                    } else {
                        InputField processField = (InputField) field;
                        //初始值先设为0，等到库存更新之后，重新更新他的值
                        int totalInventory = 0;
                        for (Map.Entry<String, Integer> skuInventoryEntry : skuInventoryMap.entrySet()) {
                            totalInventory += skuInventoryEntry.getValue();
                        }
                        processField.setValue(String.valueOf(totalInventory));
                        retMap.put(field.getId(), processField);
                    }
                    break;
                }
                case TMALL_ITEM_PRICE:
                {
                    if (processFields == null || processFields.size() != 1) {
                        throw new BusinessException("tmall item price's platformProps must have only one prop!");
                    }
                    Field field = processFields.get(0);
                    if (field.getType() != FieldTypeEnum.INPUT) {
                        $error("tmall item price's field(" + field.getId() + ") must be input");
                    } else {
                        InputField itemPriceField = (InputField) field;
                        double itemPrice = calcItemPrice(sxData.getProductList(), skuInventoryMap, sxData.getChannelId(), sxData.getCartId());
                        itemPriceField.setValue(String.valueOf(itemPrice));
                        retMap.put(field.getId(), itemPriceField);
                    }
                    break;
                }
                case TMALL_XINGHAO:
                {
                    if (processFields == null || processFields.size() != 2) {
                        throw new BusinessException("tmall item xinghao's platformProps must have two props!");
                    }

                    for (Field processField : processFields) {
                        if (processField.getType() == FieldTypeEnum.SINGLECHECK) {
                            // modified by morse.lu 2016/07/06 start
//                            SingleCheckField field = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
                            SingleCheckField field = (SingleCheckField) processField;
                            // modified by morse.lu 2016/07/06 end
                            //prop_1626510（型号）值设为-1(表示其他）
                            field.setValue("-1");
                            retMap.put(processField.getId(), field);
                        } else {
                            //其他的型号值填货号
                            // modified by morse.lu 2016/07/06 start
//                            InputField field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                            InputField field = (InputField) processField;
                            // modified by morse.lu 2016/07/06 end
                            String styleCode = sxData.getStyleCode();
                            if (StringUtils.isEmpty(styleCode)) {
                                styleCode = generateStyleCode(sxData);
                            }
                            field.setValue(styleCode);
                            retMap.put(processField.getId(), field);
                        }
                    }
                    break;
                }
                case TMALL_OUT_ID: {
                    if (processFields == null || processFields.size() != 1) {
                        throw new BusinessException("tmall item outId's platformProps must have one prop!");
                    }
                    boolean hasSku = false;
                    for (CustomMappingType customMappingIter : mappingTypePropsMap.keySet()) {
                        if (customMappingIter == CustomMappingType.SKU_INFO) {
                            hasSku = true;
                            break;
                        }
                    }
                    if (hasSku) {
                        $info("已经有sku属性，忽略商品外部编码");
                        continue;
                    }

                    Field field = processFields.get(0);
                    if (field.getType() != FieldTypeEnum.INPUT) {
                        $error("tmall item outId's field(" + field.getId() + ") must be input");
                    } else {
                        InputField inputField = (InputField) field;
                        List<CmsBtProductModel> processProducts = sxData.getProductList();
                        if (processProducts.size() != 1) {
                            String errorCause = "包含商品外部编码的类目必须只有一个code";
                            $error(errorCause);
                            throw new BusinessException(errorCause);
                        }
                        CmsBtProductModel sxProduct = processProducts.get(0);
                        List<CmsBtProductModel_Sku> cmsBtProductModelSkus = sxProduct.getCommon().getSkus();
                        if (cmsBtProductModelSkus.size() != 1) {
                            String errorCause = "包含商品外部编码的类目必须只有一个sku";
                            $error(errorCause);
                            throw new BusinessException(errorCause);
                        }
                        inputField.setValue(cmsBtProductModelSkus.get(0).getSkuCode());
                        retMap.put(field.getId(), inputField);
                    }
                    break;
                }
                case TMALL_SHOP_CATEGORY:
                {
                    if (processFields == null || processFields.size() != 1) {
                        throw new BusinessException("tmall item shop_category's platformProps must have one prop!");
                    }

                    Field field = processFields.get(0);
                    String platformPropId = field.getId();
//                    List<CmsMtChannelConditionConfigModel> conditionPropValueModels = conditionPropValueService.get(sxData.getChannelId(), platformPropId);
//
//                    //优先使用条件表达式
//                    if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
//                        if (field.getType() != FieldTypeEnum.MULTICHECK) {
//                            $error("tmall item shop_category's field(" + field.getId() + ") must be MultiCheckField");
//                        } else {
//                            MultiCheckField multiCheckField = (MultiCheckField) field;
//                            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
//                            for (CmsMtChannelConditionConfigModel conditionPropValueModel : conditionPropValueModels) {
//                                String conditionExpressionStr = conditionPropValueModel.getConditionExpression();
//                                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
//                                String propValue = expressionParser.parse(conditionExpression, shopBean, user, null);
//                                if (propValue != null) {
//                                     multiCheckField.addValue(propValue);
//                                }
//                            }
//                            retMap.put(platformPropId, multiCheckField);
//                        }
//                    } else {
//                        final String sellerCategoryPropId = "seller_cids";
//                        String numIId = sxData.getPlatform().getNumIId();
//                        if (!StringUtils.isEmpty(numIId)) {
                            // 更新
                            // modified by morse.lu 2016/06/21 start
                            // 改成从表里取cid
//                            try {
//                                TmallItemUpdateSchemaGetResponse response = tbProductService.doGetWareInfoItem(numIId, shopBean);
//                                String strXml = response.getUpdateItemResult();
//                                // 读入的属性列表
//                                List<Field> fieldList = SchemaReader.readXmlForList(strXml);
//                                List<String> defaultValues = null;
//                                for (Field fd : fieldList) {
//                                    if (sellerCategoryPropId.equals(fd.getId())) {
//                                        MultiCheckField multiCheckField = (MultiCheckField) fd;
//                                        defaultValues = multiCheckField.getDefaultValues();
//                                        break;
//                                    }
//                                }
//                                if (defaultValues != null) {
//                                    MultiCheckField multiCheckField = (MultiCheckField) FieldTypeEnum.createField(FieldTypeEnum.MULTICHECK);
//                                    multiCheckField.setId(sellerCategoryPropId);
//                                    for (String defaultValue : defaultValues) {
//                                        multiCheckField.addValue(defaultValue);
//                                    }
//
//                                    retMap.put(sellerCategoryPropId, multiCheckField);
//                                }
//                            } catch (TopSchemaException | ApiException e) {
//                                $error(e.getMessage(), e);
//                            }
                            List<CmsBtProductModel_SellerCat> defaultValues = mainSxProduct.getPlatform(sxData.getCartId()).getSellerCats();
                            if (defaultValues != null && !defaultValues.isEmpty()) {
                                // modified by morse.lu 2016/07/06 start
//                                MultiCheckField multiCheckField = (MultiCheckField) FieldTypeEnum.createField(FieldTypeEnum.MULTICHECK);
                                MultiCheckField multiCheckField = (MultiCheckField) field;
                                // modified by morse.lu 2016/07/06 end
//                                multiCheckField.setId(sellerCategoryPropId);
                                for (CmsBtProductModel_SellerCat defaultValue : defaultValues) {
                                    multiCheckField.addValue(defaultValue.getcId());
                                }
                                retMap.put(platformPropId, multiCheckField);
                            }
                            // modified by morse.lu 2016/06/21 end
//                        }
//                    }
                    break;
                }
                case ITEM_STATUS: {
                    if (processFields == null || processFields.size() != 1) {
                        throw new BusinessException("tmall item status's platformProps must have one prop!");
                    }

                    Field field = processFields.get(0);
                    if (field.getType() != FieldTypeEnum.SINGLECHECK) {
                        $error("tmall's status field(" + field.getId() + ") must be singleCheck");
                    } else {
                        SingleCheckField singleCheckField = (SingleCheckField) field;

                        CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
                        if (platformActive == CmsConstants.PlatformActive.ToOnSale) {
                            singleCheckField.setValue("0");
                        } else if (platformActive == CmsConstants.PlatformActive.ToInStock) {
                            singleCheckField.setValue("2");
                        } else {
                            throw new BusinessException("PlatformActive must be Onsale or Instock, but now it is " + platformActive);
                        }
                        retMap.put(field.getId(), singleCheckField);
                    }
                    break;
                }
                // added by morse.lu 2016/06/29 start
                case ITEM_DESCRIPTION: {
                    Field field = processFields.get(0);
                    setDescriptionFieldValue(field, expressionParser, shopBean, user);
                    retMap.put(field.getId(), field);
                    break;
                }
                case ITEM_WIRELESS_DESCRIPTION: {
                    Field field = processFields.get(0);
                    setWirelessDescriptionFieldValue(field, expressionParser, shopBean, user);
                    retMap.put(field.getId(), field);
                    break;
                }
                case IMAGE: {
                    for (Field field : processFields) {
                        setImageFieldValue(field, expressionParser, shopBean, user);
                        retMap.put(field.getId(), field);
                    }
                    break;
                }
                case FREIGHT: {
                    RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
                    for (Field field : processFields) {
                        String platformPropId = field.getId();
                        List<CmsMtChannelConditionConfigModel> conditionPropValueModels = conditionPropValueService.get(sxData.getChannelId(), platformPropId);
                        String propValue = null;

                        // 优先使用设定好的
                        if (ListUtils.notNull(conditionPropValueModels)) {
                            // 暂时只有singleCheck和input,所以找到第一条就好
                            for (CmsMtChannelConditionConfigModel conditionPropValueModel : conditionPropValueModels) {
                                String conditionExpressionStr = conditionPropValueModel.getConditionExpression();
                                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                                propValue = expressionParser.parse(conditionExpression, shopBean, user, null);
                                if (!StringUtils.isEmpty(propValue)) {
                                    break;
                                }
                            }
                        } else {
                            // 画面填了啥就是啥
                            propValue = getProductValueByMasterMapping(field, shopBean, expressionParser, user);
                        }

                        if (!StringUtils.isEmpty(propValue)) {
                            // 暂时只有singleCheck和input，以后要是有别的再说
                            if (field.getType() == FieldTypeEnum.INPUT) {
                                ((InputField) field).setValue(propValue);
                            } else if (field.getType() == FieldTypeEnum.SINGLECHECK) {
                                ((SingleCheckField) field).setValue(propValue);
                            }
                            retMap.put(field.getId(), field);
                        }
                    }
                    break;
                }
                // added by morse.lu 2016/06/29 end
                // added by morse.lu 2016/08/10 start
                case CSPU: {
                    int cartId = sxData.getCartId();
                    String errorLog = "平台类目id是:" + sxData.getMainProduct().getPlatform(cartId).getpCatId() + ". groupId:" + sxData.getGroupId();
                    AbstractSkuFieldBuilder skuFieldService = new TmallGjSkuFieldBuilderImpl8();
                    skuFieldService.setDao(cmsMtPlatformPropSkuDao, cmsMtChannelSkuConfigDao);

                    List<Field> allSkuFields = new ArrayList<>();
                    recursiveGetFields(processFields, allSkuFields);

                    // deleted by morse.lu 2016/08/25 start
                    // 暂时画面写死已经上传到平台的url完整路径，所以先注了这段代码，以后改回成画面有上传按钮，只填写图片名，再恢复这段代码
//                    String imageTemplate = resolveDict("资质图片模板",expressionParser,shopBean, user, null);
//                    if (StringUtils.isEmpty(imageTemplate)) {
//                        String err = "达尔文产品没有设值资质图片模板字典!";
//                        sxData.setErrorMessage(err);
//                        throw new BusinessException(err);
//                    }
//                    skuFieldService.setCodeImageTemplate(imageTemplate);
                    // deleted by morse.lu 2016/08/25 end

                    try {
                        List<Field> skuInfoFields = skuFieldService.buildSkuInfoField(allSkuFields, expressionParser, cmsMtPlatformMappingModel, skuInventoryMap, shopBean, user);
                        skuInfoFields.forEach(field -> retMap.put(field.getId(), field));
                    } catch (BusinessException e) {
                        sxData.setErrorMessage(e.getMessage());
                        throw new BusinessException(e.getMessage());
                    } catch (Exception e) {
                        $warn(e.getMessage());
                        sxData.setErrorMessage("Can't build cspu Field." + errorLog);
                        throw new BusinessException("Can't build cspu Field." + errorLog);
                    }
                    break;
                }
                // added by morse.lu 2016/08/10 end
                // added by morse.lu 2016/08/18 start
                case PRODUCT_ID: {
                    if (processFields == null || processFields.size() != 1) {
                        throw new BusinessException("tmall item sc_product_id's platformProps must have one prop!");
                    }

                    boolean hasSku = false;
                    for (CustomMappingType customMappingIter : mappingTypePropsMap.keySet()) {
                        if (customMappingIter == CustomMappingType.SKU_INFO) {
                            hasSku = true;
                            break;
                        }
                    }
                    if (hasSku) {
                        $info("已经有sku属性，忽略外部货品id");
                        continue;
                    }

                    Field field = processFields.get(0);
                    if (field.getType() != FieldTypeEnum.INPUT) {
                        $error("tmall item sc_product_id's field(" + field.getId() + ") must be input");
                    } else {
                        InputField inputField = (InputField) field;
                        // modified by morse.lu 2016/10/17 start
//                        String value = inputField.getDefaultValue();
//                        if (!StringUtils.isEmpty(value)) {
//                            inputField.setValue(value);
//                        }
                        // 外部填写的时候，只有一个code，一个sku，这个check在商品外部编码(outer_id)的逻辑里有了，这里就不再做了
                        String skuCode = mainSxProduct.getCommon().getSkus().get(0).getSkuCode();
                        String scProductId = updateTmScProductId(shopBean,
                                            skuCode,
                                            getProductValueByMasterMapping("title", shopBean, expressionParser, user),
                                            skuInventoryMap.get(skuCode) != null ? Integer.toString(skuInventoryMap.get(skuCode)) : "0"
                        );
                        inputField.setValue(scProductId);
                        // modified by morse.lu 2016/10/17 end

                        retMap.put(field.getId(), inputField);
                    }

                    break;
                }
                // added by morse.lu 2016/08/18 end
            }
        }

        return retMap;
    }

    /**
     * 商品描述设值
     * 只填写内容这一属性
     *
     * @param field 商品描述的field
     */
    private void setDescriptionFieldValue(Field field, ExpressionParser expressionParser,  ShopBean shopBean, String user) throws Exception {
        // 详情页描述 (以后可能会根据不同商品信息，取不同的[详情页描述])
        // modified by morse.lu 2016/08/16 start
        // 画面上有指定的用指定的，没有就还是用原来的"详情页描述"
//        String descriptionValue = resolveDict("详情页描述", expressionParser, shopBean, user, null);
        SxData sxData = expressionParser.getSxData();
        String descriptionValue;
        RuleExpression ruleDetails = new RuleExpression();
        MasterWord masterWord = new MasterWord("details");
        ruleDetails.addRuleWord(masterWord);
        String details = expressionParser.parse(ruleDetails, shopBean, user, null);
        if (!StringUtils.isEmpty(details)) {
            descriptionValue = resolveDict(details, expressionParser, shopBean, user, null);
            if (StringUtils.isEmpty(descriptionValue)) {
                String errorMsg = String.format("详情页描述[%s]在dict表里未设定!", details);
                sxData.setErrorMessage(errorMsg);
                throw new BusinessException(errorMsg);
            }
        } else {
            descriptionValue = resolveDict("详情页描述", expressionParser, shopBean, user, null);
        }
        // modified by morse.lu 2016/08/16 end
        // 详情页描述-空白
        String descriptionBlankValue = resolveDict("详情页描述-空白内容", expressionParser, shopBean, user, null);

        String errorMsg = String.format("类目[%s]的商品描述field_id或结构或类型发生变化啦!", sxData.getMainProduct().getCommon().getCatPath());

        if (field.getType() == FieldTypeEnum.INPUT) {
            InputField inputField = (InputField) field;
            inputField.setValue(descriptionValue);
        } else if (field.getType() == FieldTypeEnum.COMPLEX) {
            ComplexField complexField = (ComplexField) field;
            ComplexValue complexValue = new ComplexValue();
            complexField.setComplexValue(complexValue);

            boolean isFirst = true;
            boolean isFirstReq = true; // 第一个必填属性,填[详情页描述],不是的话填[详情页描述-空白]
            Field fieldDef = null;
            for (Field subField : complexField.getFields()) {
                // 商品参数,商品展示,视频推介等
                if (subField.getType() == FieldTypeEnum.COMPLEX) {
                    ComplexField subComplexField = (ComplexField) subField;
                    boolean hasContent = false; // 是否有内容这个Field,没有的话说明id或者结构发生变化啦
                    for (Field contentField : subComplexField.getFields()) {
                        if (contentField.getType() == FieldTypeEnum.INPUT  && contentField.getId().indexOf("content") > 0) {
                            // 内容属性,把[详情页描述]填进去
                            hasContent = true;
                            boolean isRequest = false;
                            for (Rule rule : contentField.getRules()) {
                                if ("requiredRule".equalsIgnoreCase(rule.getName()) && Boolean.parseBoolean(rule.getValue())) {
                                    isRequest = true;
                                    break;
                                }
                            }

                            if (isRequest || isFirst) {
                                // 必须，或者第一次(用于默认项)
                                isFirst = false;
                                Field valueSubField = deepCloneField(subField);
                                complexValue.put(valueSubField);
                                ComplexField valueSubComplexField = (ComplexField) valueSubField;
                                ComplexValue subComplexValue = new ComplexValue();
                                valueSubComplexField.setComplexValue(subComplexValue);
                                Field valueContentField = deepCloneField(contentField);
                                subComplexValue.put(valueContentField);
                                fieldDef = valueContentField;

                                if (isFirstReq && isRequest) {
                                    isFirstReq = false;
                                    ((InputField) valueContentField).setValue(descriptionValue);
                                } else {
                                    ((InputField) valueContentField).setValue(descriptionBlankValue);
                                }
                            }
                            break;
                        }
                    }
                    if (!hasContent) {
                        sxData.setErrorMessage(errorMsg);
                        throw new BusinessException(errorMsg);
                    }
                } else if (subField.getType() == FieldTypeEnum.MULTICOMPLEX) {
                    // 目前只有一个叫自定义模块的，暂时不做
                    continue;
                } else {
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }
            }

            if (isFirstReq) {
                // 没有必须的,放在第一个属性里
                ((InputField) fieldDef).setValue(descriptionValue);
            }
        } else {
            sxData.setErrorMessage(errorMsg);
            throw new BusinessException(errorMsg);
        }
    }

    /**
     * 无线描述设值
     *
     * @param field 无线描述的field
     */
    private void setWirelessDescriptionFieldValue(Field field, ExpressionParser expressionParser,  ShopBean shopBean, String user) throws Exception {
        // common里的appSwitch,如果是1，那么就启用字典中配置好的天猫无线端模板,如果是0或未设定，那么天猫关于无线端的所有字段都设置为不启用
        Integer appSwitch = expressionParser.getSxData().getMainProduct().getCommon().getFields().getAppSwitch();
        if (appSwitch == null || appSwitch.intValue() != 1) {
            return;
        }

        // 无线描述 (以后可能会根据不同商品信息，取不同的[无线描述])
        // modified by morse.lu 2016/08/16 start
        // 画面上有指定的用指定的，没有就还是用原来的"详情页描述"
//        String descriptionValue = resolveDict("无线描述", expressionParser, shopBean, user, null);
        SxData sxData = expressionParser.getSxData();
        String descriptionValue;
        RuleExpression ruleDetails = new RuleExpression();
        MasterWord masterWord = new MasterWord("wirelessDetails");
        ruleDetails.addRuleWord(masterWord);
        String details = expressionParser.parse(ruleDetails, shopBean, user, null);
        if (!StringUtils.isEmpty(details)) {
            descriptionValue = resolveDict(details, expressionParser, shopBean, user, null);
            if (StringUtils.isEmpty(descriptionValue)) {
                String errorMsg = String.format("无线描述[%s]在dict表里未设定!", details);
                sxData.setErrorMessage(errorMsg);
                throw new BusinessException(errorMsg);
            }
        } else {
            descriptionValue = resolveDict("无线描述", expressionParser, shopBean, user, null);
        }
        // modified by morse.lu 2016/08/16 end
        if (StringUtils.isEmpty(descriptionValue)) {
            // 字典表里未设定，先什么都不做吧
            return;
        }
        Map<String, Object> mapValue = JacksonUtil.jsonToMap(descriptionValue);

        // 开始设值
        setWirelessDescriptionFieldValueWithLoop(field, mapValue, expressionParser.getSxData());
    }

    /**
     * 循环无线描述field进行设值
     */
    private void setWirelessDescriptionFieldValueWithLoop(Field field, Map<String, Object> mapValue, SxData sxData) throws Exception {
        String errorMsg = String.format("类目[%s]的无线描述field_id或结构或类型发生变化啦!", sxData.getMainProduct().getCommon().getCatPath());
        if (!mapValue.containsKey(field.getId())) {
            $warn(errorMsg);
            return;
//            sxData.setErrorMessage(errorMsg);
//            throw new BusinessException(errorMsg);
        }

        Object objVal = mapValue.get(field.getId());
        if (objVal == null) {
            return;
        }

        switch (field.getType()) {
            case INPUT:
                if (objVal instanceof String || objVal instanceof Number || objVal instanceof Boolean) {
                    ((InputField) field).setValue(String.valueOf(objVal));;
                } else {
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }

                break;
            case SINGLECHECK:
                if (objVal instanceof String || objVal instanceof Number || objVal instanceof Boolean) {
                    ((SingleCheckField) field).setValue(String.valueOf(objVal));
                } else {
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }

                break;
            case MULTIINPUT:
                break;
            case MULTICHECK:
                if (objVal instanceof List) {
                    MultiCheckField multiCheckField = (MultiCheckField) field;
                    for (Object val : (List) objVal) {
                        multiCheckField.addValue(String.valueOf(val));
                    }
                } else {
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }

                break;
            case COMPLEX: {
                if (objVal instanceof Map) {
                    ComplexField complexField = (ComplexField) field;
                    ComplexValue complexValue = new ComplexValue();
                    complexField.setComplexValue(complexValue);

                    for (Field subField : complexField.getFields()) {
                        Field valueField = deepCloneField(subField);
                        setWirelessDescriptionFieldValueWithLoop(valueField, (Map) objVal, sxData);
                        complexValue.put(valueField);
                    }
                } else {
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }

                break;
            }
            case MULTICOMPLEX:
                if (objVal instanceof List) {
                    MultiComplexField multiComplexField = (MultiComplexField) field;
                    List<ComplexValue> complexValues = new ArrayList<>();
                    multiComplexField.setComplexValues(complexValues);

                    for (Object val : (List) objVal) {
                        ComplexValue complexValue = new ComplexValue();
                        complexValues.add(complexValue);

                        if (val instanceof Map) {
                            for (Field subField : multiComplexField.getFields()) {
                                Field valueField = deepCloneField(subField);
                                setWirelessDescriptionFieldValueWithLoop(valueField, (Map) val, sxData);
                                complexValue.put(valueField);
                            }
                        } else {
                            sxData.setErrorMessage(errorMsg);
                            throw new BusinessException(errorMsg);
                        }
                    }
                } else {
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }

                break;
            case LABEL:
                break;
            default:
                return;
        }
    }

    /**
     * 图片属性设值
     */
    private void setImageFieldValue(Field field, ExpressionParser expressionParser, ShopBean shopBean, String user) throws Exception {
        SxData sxData = expressionParser.getSxData();
        String errorMsg = String.format("类目[%s]的图片[field_id=%s]的类型发生变化啦!", sxData.getMainProduct().getCommon().getCatPath(), field.getId());
        boolean hasSetting = false;

        if (field.getType() == FieldTypeEnum.COMPLEX) {
            ComplexField complexField = (ComplexField) field;
            ComplexValue complexValue = new ComplexValue();
            complexField.setComplexValue(complexValue);

            List<Field> subfields = complexField.getFields();

            for (CustomMappingType.ImageProp imageProp : CustomMappingType.ImageProp.values()) {
                if (imageProp.getPropId().equals(field.getId())) {
                    hasSetting = true;
                    for (int index = 1; index <= subfields.size(); index++) {
                        Field valueField = deepCloneField(subfields.get(index - 1));
                        if (valueField.getType() == FieldTypeEnum.INPUT) {
                            String url = resolveDict(imageProp.getBaseDictName() + index, expressionParser, shopBean, user, null);
                            if (StringUtils.isEmpty(url)) {
                                // 如果代码未设定，那么直接用field.name去取着试试看
                                url = resolveDict(valueField.getName(), expressionParser, shopBean, user, null);
                            }
                            complexValue.put(valueField);
                            ((InputField) valueField).setValue(url);
                        } else {
                            sxData.setErrorMessage(errorMsg);
                            throw new BusinessException(errorMsg);
                        }
                    }
                    break;
                }
            }
        } else if (field.getType() == FieldTypeEnum.INPUT) {
            for (CustomMappingType.ImageProp imageProp : CustomMappingType.ImageProp.values()) {
                if (imageProp.getPropId().equals(field.getId())) {
                    hasSetting = true;
                    // 第一张图
                    String url = resolveDict(imageProp.getBaseDictName() + "1", expressionParser, shopBean, user, null);
                    ((InputField) field).setValue(url);
                    break;
                }
            }
        } else {
            sxData.setErrorMessage(errorMsg);
            throw new BusinessException(errorMsg);
        }

        if (!hasSetting) {
            $warn("有未设定的图片!");
        }
    }

    private void recursiveGetFields(List<Field> fields, List<Field> resultFields) {
        for (Field field : fields) {
            switch (field.getType()) {
                case COMPLEX:
                    recursiveGetFields(((ComplexField)field).getFields(), resultFields);
                    resultFields.add(field);
                    break;
                case MULTICOMPLEX:
                    recursiveGetFields(((MultiComplexField)field).getFields(), resultFields);
                    resultFields.add(field);
                    break;
                default:
                    resultFields.add(field);
            }
        }
    }

    /**
     * 生成款号（copy TmallProductSerive的此方法）
     * 1. 如果不是达尔文体系，那么使用model作为款号直接返回
     * 2. 如果是达尔文体系，暂时不做
     * @param sxData SxData
     * @throws Exception
     */
    public String generateStyleCode(SxData sxData) throws Exception {
        boolean isDarwin = sxData.isDarwin();
        if (!isDarwin) {
            // 不是达尔文
            // modified by morse.lu 2016/10/09 start
            // 画面上可以填了,没填的话还是用model
//            String styleCode = sxData.getMainProduct().getCommon().getFields().getModel();
            String styleCode = (String) sxData.getMainProduct().getPlatform(sxData.getCartId()).getFields().get("productModel");
            if (StringUtils.isEmpty(styleCode)) {
                styleCode = sxData.getMainProduct().getCommon().getFields().getModel();
            }
            // modified by morse.lu 2016/10/09 end
            // test用 start
//            styleCode = "test." + styleCode;
            // test用 end
            sxData.setStyleCode(styleCode);
            return styleCode;
        } else {
            // 是达尔文
            // TODO:暂时throw出去
            String errMsg = "是达尔文体系，上新逻辑未做成!";
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
    }

    /**
     * 更新天猫货品id(关联商品)
     */
    public String updateTmScProductId(ShopBean shopBean, String skuCode, String title, String qty) {
        if (StringUtils.isEmpty(taobaoScItemService.doCheckNeedSetScItem(shopBean))) {
            // 不要关联商品
            return null;
        }
        try {
            String scProductId = taobaoScItemService.doCreateScItem(shopBean, skuCode, title, qty);
			// 临时忽略检查
//            if (StringUtils.isEmpty(scProductId)) {
//                throw new BusinessException(String.format("自动设置天猫商品全链路库存管理时,发生不明异常!skuCode:%s", skuCode));
//            }
            return scProductId;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException(String.format("自动设置天猫商品全链路库存管理时,发生异常!skuCode:%s" + e.getMessage(), skuCode));
        }
    }

    /**
     * 价格的计算方法为：
     *  计算最高价格，库存为0的sku不参与计算
     *  如果所有sku库存都为0， 第一个的价格作为商品价格
     */
    private double calcItemPrice(List<CmsBtProductModel> productlList, Map<String, Integer> skuInventoryMap, String channelId, int cartId) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下 tom START
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId
                , CmsConstants.ChannelConfig.PRICE
                , String.valueOf(cartId) + CmsConstants.ChannelConfig.PRICE_SX_PRICE);

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return 0d;
        } else {
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return 0d;
            }
        }
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下 tom END

        Double resultPrice = 0d, onePrice = 0d;
        List<Double> skuPriceList = new ArrayList<>();
        for (CmsBtProductModel productModel : productlList) {
            //// TODO: 16/6/30 edward 因为不知道怎么修改所以请上新组修改
            if (!productModel.getPlatform(cartId).getStatus().equals(CmsConstants.ProductStatus.Approved.name())) {
                continue;
            }
            // modified by morse.lu 2016/06/28 start
            // product表结构变化
//            for (CmsBtProductModel_Sku cmsBtProductModelSku : productModel.getSkus()) {
            for (BaseMongoMap<String, Object> cmsBtProductModelSku : productModel.getPlatform(cartId).getSkus()) {
                // modified by morse.lu 2016/06/28 end
                int skuQuantity = 0;
                // modified by morse.lu 2016/06/28 start
//                Integer skuQuantityInteger = skuInventoryMap.get(cmsBtProductModelSku.getSkuCode());
                Integer skuQuantityInteger = skuInventoryMap.get(cmsBtProductModelSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
                // modified by morse.lu 2016/06/28 end
                if (skuQuantityInteger != null) {
                    skuQuantity = skuQuantityInteger;
                }
                double skuPrice = 0;
                try {
                    skuPrice = Double.valueOf(cmsBtProductModelSku.getAttribute(sxPricePropName).toString());
                } catch (Exception e) {
                    // modified by morse.lu 2016/06/28 start
//                    $warn("No price for sku " + cmsBtProductModelSku.getSkuCode());
                    $warn("No price for sku " + cmsBtProductModelSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
                    // modified by morse.lu 2016/06/28 end
                }
                if (onePrice - 0d == 0) {
                    onePrice = skuPrice;
                }
                if (skuQuantity > 0)  {
                    skuPriceList.add(skuPrice);
                }
            }
        }

        for (double skuPrice : skuPriceList) {
            resultPrice = Double.max(resultPrice, skuPrice);
        }
        if (resultPrice - 0d == 0) {
            resultPrice = onePrice;
        }

        return resultPrice;
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

    public String searchDictList(String channelId, int cartId, String paddingPropName, int imageIndex) {
        return paddingImageDaoExt.selectByCriteria(channelId, cartId, paddingPropName, imageIndex);
    }

    /**
     *
     * 优先顺：Brand>ProductType>SizeType
     *   具体场景
     *    1）Product：Asics Shoe Men
     *    尺码组表：
     *    No	Brand	ProductType	SizeType
     *    1	    All  	All     	All
     *    结果：找到默认设置1
     *
     *    2）Product：Asics Shoe Men
     *    尺码组表：
     *    No	Brand	    ProductType	  SizeType
     *    1	    Asics       All           All
     *    2	    Asics	    Shoe          All
     *    3	    Asics	    Shoe     	  Men
     *    4	    Asics,nike	Shoe	      Men
     *    结果：找到两条符合的记录->认为找不到，报错，要求运营修改设定
     *
     *   3）Product：Asics Shoe Men
     *   尺码组表：
     *    No	Brand	    ProductType	  SizeType
     *    1	    Asics       All           All
     *    2	    Asics	    Shoe          All
     *    3	    Asics,nike	Shoe	      Men
     *   结果：第3条符合的条件最多->找到3
     *
     *   4）Product：Asics Shoe Men
     *   尺码组表：
     *   No	Brand	ProductType	SizeType
     *   1	Asics   All         All
     *   2	Asics	Shoe        All
     *   结果：第2条符合的条件最多->找到2
     *
     *   5）Product：Asics Shoe Men
     *   尺码组表：
     *   No	Brand	ProductType	SizeType
     *   1	Asics	All      	Men
     *   2	Asics	Shoe        All
     *   结果：根据优先顺序，选择2号规则
     *
     * @param imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图
     * @param viewType 1:PC端 2：APP端
     * @param brandName product.fields.brand
     * @param productType product.fields.productType
     * @param sizeType product.fields.sizeType
     * @param getOriUrl true:取原图url false:取上传后平台图url
     */
    public List<String> getImageUrls(String channelId, int cartId, int imageType, int viewType, String brandName, String productType, String sizeType, boolean getOriUrl) throws Exception {
        List<String> listUrls = new ArrayList<>();
        Map<Integer, List<CmsBtImageGroupModel>> matchMap = new HashMap<>(); // Map<完全匹配key的位置，List>
        for (int index = 0; index < 1 << 3; index++) {
            // 初期化
            // 这一版的key是3个：brandName, productType, sizeType
            matchMap.put(index, new ArrayList<>());
        }
        List<Integer> sortKey = new ArrayList<>(); // key的sort
        {
            // TODO 初期化设值不够好看，暂时没想到好方法，以后想到再改
            // 完全匹配:入参brandName为nike，检索结果也是nike。 不完全匹配：入参brandName为nike，检索结果是All
            // 这里第一位是brandName，完全匹配为1，不完全匹配为0
            // 这里第二位是productType，完全匹配为1，不完全匹配为0
            // 这里第三位是sizeType，完全匹配为1，不完全匹配为0
            // 按完全匹配数量+优先顺：Brand>ProductType>SizeType，进行下面顺序设定
            sortKey.add(7); // 111
            sortKey.add(6); // 110
            sortKey.add(5); // 101
            sortKey.add(3); // 011
            sortKey.add(4); // 100
            sortKey.add(2); // 010
            sortKey.add(1); // 001
            sortKey.add(0); // 000
        }

        String paramBrandName = brandName;
        String paramProductType = productType;
        String paramSizeType = sizeType;
        if (StringUtils.isEmpty(brandName)) {
            paramBrandName = CmsBtImageGroupDao.VALUE_ALL;
        }
        if (StringUtils.isEmpty(productType)) {
            paramProductType = CmsBtImageGroupDao.VALUE_ALL;
        }
        if (StringUtils.isEmpty(sizeType)) {
            paramSizeType = CmsBtImageGroupDao.VALUE_ALL;
        }

        List<CmsBtImageGroupModel> modelsAll = cmsBtImageGroupDao.selectListByKeysWithAll(channelId, cartId, imageType, viewType, paramBrandName, paramProductType, paramSizeType, 1);
        for (CmsBtImageGroupModel model : modelsAll) {
            // 这里第一位是，第二位是productType，第三位是sizeType，按顺序判断
            String matchVal = "";
            if (model.getBrandName().contains(paramBrandName)) {
                matchVal += "1";
            } else {
                matchVal += "0";
            }
            if (model.getProductType().contains(paramProductType)) {
                matchVal += "1";
            } else {
                matchVal += "0";
            }
            if (model.getSizeType().contains(paramSizeType)) {
                matchVal += "1";
            } else {
                matchVal += "0";
            }
            matchMap.get(Integer.parseInt(matchVal, 2)).add(model);
        }

        for (Integer key : sortKey) {
            List<CmsBtImageGroupModel> matchModels =  matchMap.get(key);
            if (matchModels.size() > 1) {
                throw new BusinessException("共通图片表找到两条以上符合的记录,请修正设定!" +
                        "channelId= " + channelId +
                        ",cartId= " + cartId +
                        ",imageType= " + imageType +
                        ",viewType= "+ viewType +
                        ",BrandName= " + paramBrandName +
                        ",ProductType= " + paramProductType +
                        ",SizeType=" + paramSizeType);
            }
            if (matchModels.size() == 1) {
                $info("找到image_group记录!");
                if (matchModels.get(0).getImage() == null || matchModels.get(0).getImage().size() == 0) {
                    throw new BusinessException("共通图片表找到的图片类型对应的图片数为0,请确保至少上传1张图片！" +
                            "channelId= " + channelId +
                            ",cartId= " + cartId +
                            ",imageType= " + imageType +
                            ",viewType= "+ viewType +
                            ",BrandName= " + paramBrandName +
                            ",ProductType= " + paramProductType +
                            ",SizeType=" + paramSizeType);
                }
                for (CmsBtImageGroupModel_Image imageInfo : matchModels.get(0).getImage()) {
                    if (getOriUrl) {
                        // 取原始图url
                        String url = imageInfo.getOriginUrl();
                        if (!StringUtils.isEmpty(url)) {
                            listUrls.add(url);
                        }
                    } else {
                        // 取平台图片url
                        String url = imageInfo.getPlatformUrl();
                        if (!StringUtils.isEmpty(url)) {
                            listUrls.add(url);
                        }
                    }
                }
                break;
            }
        }

        return listUrls;
    }

    /**
     * 从cms_bt_size_chart(mongo)取得尺码对照数据，取得逻辑与getImageUrls相同
     * 如果有sizeChartId则用sizeCharId去查，没有的话用brandName,productType,sizeType去查
     *
     * @param brandName product.fields.brand
     * @param productType product.fields.productType
     * @param sizeType product.fields.sizeType
     * @param sizeChartId product.fields.sizeChartId
     * @return Map<originalSize, adjustSize>
     */
    public Map<String, String> getSizeMap(String channelId, String brandName, String productType, String sizeType, Integer sizeChartId) {

        if (sizeChartId == null || sizeChartId == 0) {
            // 根据品牌，产品分类，尺码分类来查找尺码表信息
            return getSizeMap(channelId, brandName, productType, sizeType);
        } else {
            // 根据sizeChartId来查找尺码表信息
            Map<String, String> sizeMap = new HashMap<>();
            CmsBtSizeChartModel sizeChartModel = sizeChartService.getCmsBtSizeChartModel(sizeChartId, channelId);
            if (sizeChartModel != null && ListUtils.notNull(sizeChartModel.getSizeMap())) {
                for (CmsBtSizeChartModelSizeMap sizeInfo : sizeChartModel.getSizeMap()) {
                    if (sizeMap.containsKey(sizeInfo.getOriginalSize())) {
                        // 这一版暂时不允许有原始尺码一样的，以后会支持
                        throw new BusinessException("根据sizeChartId在尺码对照表找到一条符合的记录,但有相同的原始尺码,请修正尺码表设定!" +
                                "channelId= "    + channelId   +
                                ",sizeChartId= " + sizeChartId +
                                "OriginalSize="  + sizeInfo.getOriginalSize());
                    }
                    sizeMap.put(sizeInfo.getOriginalSize(), sizeInfo.getAdjustSize());
                }
            }

            return sizeMap;
        }

    }

    /**
     * 从cms_bt_size_chart(mongo)取得尺码对照数据，取得逻辑与getImageUrls相同
     *
     * @param brandName product.fields.brand
     * @param productType product.fields.productType
     * @param sizeType product.fields.sizeType
     * @return Map<originalSize, adjustSize>
     */
    public Map<String, String> getSizeMap(String channelId, String brandName, String productType, String sizeType) {
        Map<String, String> sizeMap = new HashMap<>();
        Map<Integer, List<CmsBtSizeChartModel>> matchMap = new HashMap<>(); // Map<完全匹配key的位置，List>
        for (int index = 0; index < 1 << 3; index++) {
            // 初期化
            // 这一版的key是3个：brandName, productType, sizeType
            matchMap.put(index, new ArrayList<>());
        }
        List<Integer> sortKey = new ArrayList<>(); // key的sort
        {
            // TODO 初期化设值不够好看，暂时没想到好方法，以后想到再改
            sortKey.add(7); // 111
            sortKey.add(6); // 110
            sortKey.add(5); // 101
            sortKey.add(3); // 011
            sortKey.add(4); // 100
            sortKey.add(2); // 010
            sortKey.add(1); // 001
            sortKey.add(0); // 000
        }

        String paramBrandName = brandName;
        String paramProductType = productType;
        String paramSizeType = sizeType;
        List<String> brandNameList = new ArrayList<>();
        List<String> productTypeList = new ArrayList<>();
        List<String> sizeTypeList = new ArrayList<>();
        if (StringUtils.isEmpty(brandName)) {
            paramBrandName = SizeChartService.VALUE_ALL;
        } else {
            brandNameList.add(brandName);
        }
        if (StringUtils.isEmpty(productType)) {
            paramProductType = SizeChartService.VALUE_ALL;
        } else {
            productTypeList.add(productType);
        }
        if (StringUtils.isEmpty(sizeType)) {
            paramSizeType = SizeChartService.VALUE_ALL;
        } else {
            sizeTypeList.add(sizeType);
        }

        List<CmsBtSizeChartModel> modelsAll = sizeChartService.getSizeChartSearch(channelId, null, null, null, null, brandNameList, productTypeList, sizeTypeList, 1, 0);
        for (CmsBtSizeChartModel model : modelsAll) {
            String matchVal = "";
            if (model.getBrandName().contains(paramBrandName)) {
                matchVal += "1";
            } else {
                matchVal += "0";
            }
            if (model.getProductType().contains(paramProductType)) {
                matchVal += "1";
            } else {
                matchVal += "0";
            }
            if (model.getSizeType().contains(paramSizeType)) {
                matchVal += "1";
            } else {
                matchVal += "0";
            }
            matchMap.get(Integer.parseInt(matchVal, 2)).add(model);
        }

        for (Integer key : sortKey) {
            List<CmsBtSizeChartModel> matchModels =  matchMap.get(key);
            if (matchModels.size() > 1) {
                throw new BusinessException("尺码对照表找到两条以上符合的记录,请修正设定!" +
                        "channelId= " + channelId +
                        ",BrandName= " + paramBrandName +
                        ",ProductType= " + paramProductType +
                        ",SizeType=" + paramSizeType);
            }
            if (matchModels.size() == 1) {
                $info("找到size_chart记录!");
                for (CmsBtSizeChartModelSizeMap sizeInfo : matchModels.get(0).getSizeMap()) {
                    // added by morse.lu start 2016/06/16 start
                    if (sizeMap.containsKey(sizeInfo.getOriginalSize())) {
                        // 这一版暂时不允许有原始尺码一样的，以后会支持
                        throw new BusinessException("尺码对照表找到一条符合的记录,但有相同的原始尺码,请修正设定!" +
                                "channelId= " + channelId +
                                ",BrandName= " + paramBrandName +
                                ",ProductType= " + paramProductType +
                                ",SizeType=" + paramSizeType +
                                "OriginalSize=" + sizeInfo.getOriginalSize());
                    }
                    // added by morse.lu start 2016/06/16 end
                    sizeMap.put(sizeInfo.getOriginalSize(), sizeInfo.getAdjustSize());
                }
                break;
            }
        }

        return sizeMap;
    }

    /**
     * 指定尺码进行转换，尺码表找不到，用原始尺码，找到，用转换后尺码
     *
     * @param listOriSize 需要转换的原始尺码list
     * @return Map<originalSize, adjustSize>
     */
    public Map<String, String> getSizeMapWithOriSize(String channelId, String brandName, String productType, String sizeType, List<String> listOriSize) {
        Map<String, String> retSizeMap = new HashMap<>();

        Map<String, String> sizeMapFromDB = getSizeMap(channelId, brandName, productType, sizeType);
        listOriSize.forEach(oriSize -> {
            String adjSize = sizeMapFromDB.get(oriSize);
            if (StringUtils.isEmpty(adjSize)) {
                retSizeMap.put(oriSize, oriSize);
            } else {
                retSizeMap.put(oriSize, adjSize);
            }
        });

        return retSizeMap;
    }

    /**
     * 从cms_bt_image_template(mongo)取得图片url，取得逻辑与getImageUrls相同
     *
     * @param channelId
     * @param cartId
     * @param imageTemplateType 1:商品图片模版 2:产品图片模版 3：详情细节模版 4：参数模版 5:自定义图片模版 6:商品包装模版
     * @param viewType 1:PC端 2：APP端
     * @param brandName product.fields.brand
     * @param productType product.fields.productType
     * @param sizeType product.fields.sizeType
     * @param imageParam 参数
     * @return url
     */
    public String getImageTemplate(String channelId, int cartId, int imageTemplateType, int viewType, String brandName, String productType, String sizeType, String... imageParam) throws Exception {
        Map<Integer, List<CmsBtImageTemplateModel>> matchMap = new HashMap<>(); // Map<完全匹配key的位置，List>
        for (int index = 0; index < 1 << 3; index++) {
            // 初期化
            // 这一版的key是3个：brandName, productType, sizeType
            matchMap.put(index, new ArrayList<>());
        }
        List<Integer> sortKey = new ArrayList<>(); // key的sort
        {
            // TODO 初期化设值不够好看，暂时没想到好方法，以后想到再改
            sortKey.add(7); // 111
            sortKey.add(6); // 110
            sortKey.add(5); // 101
            sortKey.add(3); // 011
            sortKey.add(4); // 100
            sortKey.add(2); // 010
            sortKey.add(1); // 001
            sortKey.add(0); // 000
        }

        String paramBrandName = brandName;
        String paramProductType = productType;
        String paramSizeType = sizeType;
        List<String> brandNameList = new ArrayList<>();
        List<String> productTypeList = new ArrayList<>();
        List<String> sizeTypeList = new ArrayList<>();
        if (StringUtils.isEmpty(brandName)) {
            paramBrandName = SizeChartService.VALUE_ALL;
        } else {
            brandNameList.add(brandName);
        }
        if (StringUtils.isEmpty(productType)) {
            paramProductType = SizeChartService.VALUE_ALL;
        } else {
            productTypeList.add(productType);
        }
        if (StringUtils.isEmpty(sizeType)) {
            paramSizeType = SizeChartService.VALUE_ALL;
        } else {
            sizeTypeList.add(sizeType);
        }

        List<CmsBtImageTemplateModel> modelsAll = imageTemplateService.getCmsBtImageTemplateModelList(channelId, cartId, imageTemplateType, viewType, brandNameList, productTypeList, sizeTypeList);
        for (CmsBtImageTemplateModel model : modelsAll) {
            String matchVal = "";
            if (model.getBrandName().contains(paramBrandName)) {
                matchVal += "1";
            } else {
                matchVal += "0";
            }
            if (model.getProductType().contains(paramProductType)) {
                matchVal += "1";
            } else {
                matchVal += "0";
            }
            if (model.getSizeType().contains(paramSizeType)) {
                matchVal += "1";
            } else {
                matchVal += "0";
            }
            matchMap.get(Integer.parseInt(matchVal, 2)).add(model);
        }

        String retUrl = null;
        for (Integer key : sortKey) {
            List<CmsBtImageTemplateModel> matchModels =  matchMap.get(key);
            if (matchModels.size() > 1) {
                throw new BusinessException("图片模板表找到两条以上符合的记录,请修正设定!" +
                        "channelId= " + channelId +
                        ",BrandName= " + paramBrandName +
                        ",ProductType= " + paramProductType +
                        ",SizeType=" + paramSizeType);
            }
            if (matchModels.size() == 1) {
                $info("找到image_template记录!");
                retUrl = String.format(matchModels.get(0).getImageTemplateContent(), imageParam);
                break;
            }
        }

        return retUrl;
    }

    // 20160513 tom 图片服务器切换 START
    public String getImageByTemplateId(String channelId, String imageTemplateId, String... imageParam) throws Exception {
        return null;
//        ImageCreateGetRequest request = new ImageCreateGetRequest();
//        request.setChannelId(channelId);
//        request.setTemplateId(Integer.parseInt(imageTemplateId));
//        request.setFile(imageTemplateId + "_" + imageParam[0]); // 模板id + "_" + 第一个参数(一般是图片名)
//        request.setVParam(imageParam);
//        try {
//            ImageCreateGetResponse response = imageCreateService.getImage(request);
//            return imageCreateService.getOssHttpURL(response.getResultData().getFilePath());
//        } catch (Exception e) {
//            throw new BusinessException("图片取得失败! 模板id:" + imageTemplateId + ", 图片名:" + imageParam[0]);
//        }
    }
    // 20160513 tom 图片服务器切换 END

    public List<CmsBtProductModel_Field_Image> getProductImages(CmsBtProductModel product, CmsBtProductConstants.FieldImageType imageType) {
        // 如果是PRODUCT，先看看image6有没有值，只要image6有一条，那么都从image6里取,否则还是去取image1
//        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBeanNoCode(product.getChannelId(), CmsConstants.ChannelConfig.PRODUCT_IMAGE_RULE);
        List<CmsBtProductModel_Field_Image> productImages;
        if (CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE == imageType) {
            // modified by morse.lu 2016/06/27 start
            // 表结构变化，改从common下的fields里去取
//            productImages = product.getFields().getImages(CmsBtProductConstants.FieldImageType.CUSTOM_PRODUCT_IMAGE);
            productImages = product.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.CUSTOM_PRODUCT_IMAGE);
            // modified by morse.lu 2016/06/27 end
            if (productImages == null || productImages.isEmpty() || StringUtils.isEmpty(productImages.get(0).getName())) {
                // modified by morse.lu 2016/06/27 start
                // 表结构变化，改从common下的fields里去取
//                productImages = product.getFields().getImages(imageType);
                productImages = product.getCommon().getFields().getImages(imageType);
                // modified by morse.lu 2016/06/27 end
            }
        } else {
            // modified by morse.lu 2016/06/27 start
            // 表结构变化，改从common下的fields里去取
//            productImages = product.getFields().getImages(imageType);
            productImages = product.getCommon().getFields().getImages(imageType);
            // modified by morse.lu 2016/06/27 end
        }
        return productImages;
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

    /**
     * 设置天猫之外的平台Schema中该类目的各个Field里具体属性的值
     * 天猫之外的平台不需要用platform_mapping表信息来取得平台类目Schema的各个Field属性值，直接product.P29.fields取得
     *
     * @param fields List<Field> 直接把值set进这个fields对象
     * @param shopBean ShopBean
     * @param expressionParser ExpressionParser
	 * @param blnForceSmartSx 是否强制使用智能上新
     * @return 设好值的FieldId和Field
     * @throws Exception
     */
    public Map<String, Field> constructPlatformProps(List<Field> fields, ShopBean shopBean,
                                                     ExpressionParser expressionParser,
													   boolean blnForceSmartSx) throws Exception {
        // 返回用Map<field_id, Field>
        Map<String, Field> retMap = null;
        SxData sxData = expressionParser.getSxData();

//        Map<String, Field> fieldsMap = new HashMap<>();
//        for (Field field : fields) {
//            fieldsMap.put(field.getId(), field);
//        }

        // TODO:特殊字段处理
        // 特殊字段Map<CartId, Map<propId, 对应mapping项目或者处理(未定)>>
        //Map<Integer, Map<String, Object>> mapSpAll = new HashMap<>();

        // 取得当前平台对应的特殊字段处理（目前mapSpAll为空，所以不会取到值）
//        Map<String, Object> mapSp = mapSpAll.get(shopBean.getCart_id());
        Map<String, Object> mapSp = new HashMap<>();

		// 是否智能上新
		boolean blnIsSmartSx = isSmartSx(sxData.getChannelId(), sxData.getCartId());
		if (blnIsSmartSx && blnForceSmartSx) {
			// 当前店铺允许智能上新， 并且要求当前商品智能上新 的场合
            blnIsSmartSx = true;
		} else {
            blnIsSmartSx = false;
        }

        for(Field field : fields) {
            if (mapSp.containsKey(field.getId())) {
                // 特殊字段

            } else if (resolveJdPriceSection_before(shopBean, field)) {
                // 设置京东属性 - [价格][价位]
                // mainProduct中不用设置价格价位Field的值，它是在这里根据maxJdPrice自动计算属性哪个价格区间，并把区间值设置到Field中
                Map<String, Field> resolveField = resolveJdPriceSection(field, sxData);
                if (resolveField != null) {
                    if (retMap == null) {
                        retMap = new HashMap<>();
                    }
                    retMap.putAll(resolveField);
                }
            } else if (resolveJdBrandSection_before(shopBean, field)) {
                // 设置京东属性 - [品牌]（运营不用再设置这个属性了）
                Map<String, Field> resolveField = resolveJdBrandSection(field, expressionParser.getSxData());
                if (resolveField != null) {
                    if (retMap == null) {
                        retMap = new HashMap<>();
                    }
                    retMap.putAll(resolveField);
                }
            } else {
                // 除了价格价位之外，其余的FieldId对应的值都在这里设定
                // 根据FieldId取得mainProduct中对应的属性值,设置到返回的Field中
                Map<String, Field> resolveField = resolveFieldMapping(field, sxData);
                if (resolveField != null) {
                    if (retMap == null) {
                        retMap = new HashMap<>();
                    }
                    retMap.putAll(resolveField);
                } else {
					if (blnIsSmartSx) {
						Map<String, Field> resolveField_smart  = getValueBySmartCore(field, sxData);
						if (resolveField_smart != null) {
							if (retMap == null) {
								retMap = new HashMap<>();
							}
							retMap.putAll(resolveField_smart);
						}
					}
                }
            }
        }

        return retMap;
    }

    private Map<String, Field> getValueBySmartCore(Field field, SxData sxData) {
        Map<String, Field> retMap = new HashMap<>();

        // 目前只做必填项
        if (field.getRules() == null || field.getRuleByName("requiredRule") == null || !field.getRuleByName("requiredRule").getValue().equals("true")) {
            return null;
        }

        // 具体逻辑
        switch (field.getType()) {
            case INPUT:
                break;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;

                // 先看看有没有默认值
                String val = singleCheckField.getDefaultValue();

                // 看看所有候选项里是否有比较模糊的选项
                if (StringUtils.isEmpty(val)) {
                    for (Option option : singleCheckField.getOptions()) {
                        if (option.getDisplayName().equals("其他") ||
                                option.getDisplayName().equals("其它") ||
                                option.getDisplayName().equals("混合") ||
                                option.getDisplayName().equals("通用")
                                ) {
                            val = option.getValue();
                            break;
                        }
                    }
                }

                // 还没有的话， 就用第一个候选项
                if (StringUtils.isEmpty(val)) {
                    if (singleCheckField.getOptions() != null && singleCheckField.getOptions().size() > 0) {
                        val = singleCheckField.getOptions().get(0).getValue();
                    }
                }

				if (!StringUtils.isEmpty(val)) {
					singleCheckField.setValue(val);
					retMap.put(field.getId(), singleCheckField);
				}
                break;
            case MULTIINPUT:
                break;
            case MULTICHECK:
                break;
            case COMPLEX:
                break;
            case MULTICOMPLEX:
                break;
            case LABEL:
                break;
            default:
                return null;
        }

        return retMap;
    }

    /**
     * 天猫以外的平台取得Product中FieldId对应的属性值(参考SxProductService的resolveMapping()方法)
     * 天猫之外的平台不需要用platform_mapping表信息来取得平台类目Schema的各个Field属性值，直接product.P29.fields取得
     *
     * @param field Field    平台schema表中的propsItem里面的Field
     * @param sxData SxData  上新数据
     * @return 设好值的FieldId和Field
     */
    public Map<String, Field> resolveFieldMapping(Field field, SxData sxData) throws Exception {
        Map<String, Field> retMap = new HashMap<>();

        // MASTER文法解析子（解析并取得主产品的属性值）
        Object objfieldItemValue = null;
        String strfieldItemValue = "";
        // 只支持MASTER类型Field,目前只发现SingleCheck(MultiCheck也是MASTER),没有发现Input(TextWordParser)类型
        if (!StringUtils.isEmpty(field.getId())) {
            objfieldItemValue = getPropValue(sxData.getMainProduct().getPlatform(sxData.getCartId()).getFields(), field.getId());
        }

        // 取得值为null不设置，空字符串的时候还是要设置（可能是更新时特意把某个属性的值改为空）
        if (null == objfieldItemValue) {
            return null;
        }

        if (objfieldItemValue instanceof String) {
            strfieldItemValue = String.valueOf(objfieldItemValue);
        } else if (objfieldItemValue instanceof List) {
            if (((List) objfieldItemValue).isEmpty()) {
                // 检查一下, 如果没有值的话, 后面的也不用做了
                return null;
            }
            List<String> plainPropValues = (List<String>) objfieldItemValue;
            strfieldItemValue = ExpressionParser.encodeStringArray(plainPropValues); // 用"~~"分隔
        } else {
            $error("Master value must be String or String[]");
            return null;
        }

        switch (field.getType()) {
            case INPUT:
                InputField inputField = (InputField) field;
                inputField.setValue(strfieldItemValue);
                retMap.put(field.getId(), inputField);
                break;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;
                singleCheckField.setValue(strfieldItemValue);
                retMap.put(field.getId(), singleCheckField);
                break;
            case MULTIINPUT:
                break;
            case MULTICHECK:
                String[] valueArrays = ExpressionParser.decodeString(strfieldItemValue); // 解析"~~"分隔的字符串

                MultiCheckField multiCheckField = (MultiCheckField)field;
                for (String val : valueArrays) {
                    multiCheckField.addValue(val);
                }
                retMap.put(field.getId(), multiCheckField);
                break;
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

        return retMap;
    }

    /**
     * 取得Product中FieldId对应的属性值(Copy from MasterWordParser.java)
     *
     * @param evaluationContext Map<String, Object>  Product里面的PXX平台下面的fields
     * @param propName true：商品 false：产品
     * @return Map （只包含叶子节点，即只包含简单类型，对于复杂类型，也只把复杂类型里的简单类型值put进Map，
     *                                       只为了外部可以不用再循环取值，只需要根据已知的field_id，取得转换后的值）
     */
    public Object getPropValue(Map<String, Object> evaluationContext, String propName) {
        char separator = '.';
        if (evaluationContext == null) {
            return null;
        }
        int separatorPos = propName.indexOf(separator);
        if (separatorPos == -1) {
            return evaluationContext.get(propName);
        }
        String firstPropName = propName.substring(0, separatorPos);
        String leftPropName = propName.substring(separatorPos + 1);
        return getPropValue((Map<String, Object>) evaluationContext.get(firstPropName), leftPropName);
    }


    /**
     * 获取网络图片流，遇错重试
     *
     * @param url   imgUrl
     * @param retry retrycount
     * @return inputStream / throw Exception
     */
    public static InputStream getImgInputStream(String url, int retry) throws BusinessException {
        if (--retry > 0) {
            try {
                return HttpUtils.getInputStream(url, null);
            } catch (Exception e) {
                getImgInputStream(url, retry);
            }
        }
        throw new BusinessException("通过URL取得图片失败. url:" + url);
    }

    /**
     * 上新成功或出错时状态回写操作
     * 上新成功时回写group, product,ims_bt_product(只限天猫和京东)表状态并记录履历
     * 上新失败时回写product表并将错误信息写入cms_bt_business_log表
     *
     * @param shopProp ShopBean 店铺信息
     * @param uploadStatus boolean 上新结果(成功:true,失败:false)
     * @param sxData SxData 上新数据
     * @param workload CmsBtSxWorkloadModel WorkLoad信息
     * @param numIId String 商品id
     * @param platformStatus CmsConstants.PlatformStatus (Onsale/InStock) US JOI不用填
     */
    public void doUploadFinalProc(ShopBean shopProp, boolean uploadStatus, SxData sxData, CmsBtSxWorkloadModel workload,
                                  String numIId, CmsConstants.PlatformStatus platformStatus,
                                  String platformPid, String modifier) {
        doUploadFinalProc(shopProp, uploadStatus, sxData, workload, numIId, platformStatus, platformPid, modifier, null);
    }

    /**
     * 上新成功或出错时状态回写操作
     * 上新成功时回写group, product,ims_bt_product(只限天猫和京东)表状态并记录履历
     * 上新失败时回写product表并将错误信息写入cms_bt_business_log表
     *
     * @param shopProp ShopBean 店铺信息
     * @param uploadStatus boolean 上新结果(成功:true,失败:false)
     * @param sxData SxData 上新数据
     * @param workload CmsBtSxWorkloadModel WorkLoad信息
     * @param numIId String 商品id
     * @param platformStatus CmsConstants.PlatformStatus (Onsale/InStock) US JOI不用填
     * @param pCatInfoMap 平台类目信息(包含pCatId, pCatPath)
     */
    public void doUploadFinalProc(ShopBean shopProp, boolean uploadStatus, SxData sxData, CmsBtSxWorkloadModel workload,
                                  String numIId, CmsConstants.PlatformStatus platformStatus,
                                  String platformPid, String modifier, Map<String, String> pCatInfoMap) {

        // 取得变更前的product group表数据
        CmsBtProductGroupModel beforeProductGroup = productGroupService.getProductGroupByGroupId(sxData.getChannelId(),
                sxData.getGroupId());
        if (beforeProductGroup == null) {
            $error("回写上新结果状态失败！没找到更新前的产品group表数据 [ProductCode:%s] [GroupId:%s]",
                    sxData.getChannelId(), sxData.getPlatform().getGroupId());
            return;
        }

        // 上新成功时
        if (uploadStatus) {
            // 设置共通属性
            sxData.getPlatform().setNumIId(numIId);
            // 一般店铺上新时(更新商品失败时，不更新platformStatus)
            if (platformStatus != null) {
                sxData.getPlatform().setPlatformStatus(platformStatus);
            }
            if (!StringUtils.isEmpty(platformPid)) {
                sxData.getPlatform().setPlatformPid(platformPid);
            }
            sxData.getPlatform().setModifier(modifier);

            // 第一次上新的时候
            if (StringUtils.isEmpty(beforeProductGroup.getPublishTime())) {
                sxData.getPlatform().setPublishTime(DateTimeUtil.getNowTimeStamp());
            }

            // 第一次变成inStock的时候(""->"InStock")，设置InStockTime
            if (StringUtils.isEmpty(beforeProductGroup.getInStockTime())
                    && CmsConstants.PlatformStatus.InStock.equals(sxData.getPlatform().getPlatformStatus())) {
                sxData.getPlatform().setInStockTime(DateTimeUtil.getNowTimeStamp());
            }

            // 第一次变成OnSale的时候(""->"OnSale")，设置OnStockTime
            if (StringUtils.isEmpty(beforeProductGroup.getOnSaleTime())
                    && CmsConstants.PlatformStatus.OnSale.equals(sxData.getPlatform().getPlatformStatus())) {
                sxData.getPlatform().setOnSaleTime(DateTimeUtil.getNowTimeStamp());
            }

            // 上新对象产品Code列表
            List<String> listSxCode = sxData.getProductList().stream().map(p -> p.getCommon().getFields().getCode()).collect(Collectors.toList());
            // 一般店铺上新成功后回写productGroup及product表的状态
            productGroupService.updateGroupsPlatformStatus(sxData.getPlatform(), listSxCode, pCatInfoMap);

            // 如果是天猫，京东平台的场合，回写ims_bt_product表(numIId)
            if (PlatFormEnums.PlatForm.TM.getId().equals(shopProp.getPlatform_id())
                    || PlatFormEnums.PlatForm.JD.getId().equals(shopProp.getPlatform_id())) {
                this.updateImsBtProduct(sxData, modifier);
            }

            // 写入workload履历表
            this.insertSxWorkloadHistory(beforeProductGroup, sxData, workload);

            // 回写workload表   (为了知道字段是哪个画面更新的，上新程序不更新workload表的modifier)
            this.updateSxWorkload(workload, CmsConstants.SxWorkloadPublishStatusNum.okNum,
                    StringUtils.isEmpty(workload.getModifier()) ? modifier : workload.getModifier());

        } else {
            // 上新失败后回写product表pPublishError的值("Error")
            productGroupService.updateUploadErrorStatus(sxData.getPlatform(), sxData.getErrorMessage());

            // 出错的时候将错误信息回写到cms_bt_business_log表
            this.insertBusinessLog(sxData, modifier);

            // 回写workload表   (为了知道字段是哪个画面更新的，上新程序不更新workload表的modifier)
            this.updateSxWorkload(workload, CmsConstants.SxWorkloadPublishStatusNum.errorNum,
                    StringUtils.isEmpty(workload.getModifier()) ? modifier : workload.getModifier());
        }
    }

    /**
     * 插入履历到cms_bt_sx_workload_history表
     *
     * @param before CmsBtProductGroupModel 变更前
     * @param sxData SxData sxData 上新数据
     * @param workload CmsBtSxWorkloadModel 上新workload
     */
    public int insertSxWorkloadHistory(CmsBtProductGroupModel before, SxData sxData, CmsBtSxWorkloadModel workload) {
        CmsBtWorkloadHistoryModel insModel = new CmsBtWorkloadHistoryModel();

        insModel.setChannelId(sxData.getChannelId());
        insModel.setCartId(sxData.getCartId());
        if (sxData.getGroupId() != null) {
            insModel.setGroupId(workload.getGroupId().intValue());
        }
        if (!sxData.getPlatform().getProductCodes().isEmpty()) {
            insModel.setProductCodes(sxData.getPlatform().getProductCodes().stream().collect(Collectors.joining(",")));
        }
        insModel.setNumiid(sxData.getPlatform().getNumIId() == null ? "" : sxData.getPlatform().getNumIId());
        insModel.setProcName(sxData.getPlatform().getModifier());
        insModel.setProcModifier(workload.getModifier());
        // 处理内容
        StringBuilder sbProcContent = new StringBuilder();
        String beforePlatformStatus = before.getPlatformStatus() == null ? "null" : before.getPlatformStatus().name();
        String afterPlatformStatus = sxData.getPlatform().getPlatformStatus() == null ? "null" : sxData.getPlatform().getPlatformStatus().name();
        if (!beforePlatformStatus.equals(afterPlatformStatus)) {
            sbProcContent.append("[PlatformStatus:'");
            sbProcContent.append(beforePlatformStatus);
            sbProcContent.append("'->'");
            sbProcContent.append(afterPlatformStatus);
            sbProcContent.append("']");
        }
        String beforePlatformPid = StringUtils.isEmpty(before.getPlatformPid()) ? "null" : before.getPlatformPid();
        String afterPlatformPid = StringUtils.isEmpty(sxData.getPlatform().getPlatformPid()) ? "null" : sxData.getPlatform().getPlatformPid();
        if (!beforePlatformPid.equals(afterPlatformPid)) {
            sbProcContent.append("[PlatformPid:'");
            sbProcContent.append(afterPlatformPid);
            sbProcContent.append("'->'");
            sbProcContent.append(afterPlatformPid);
            sbProcContent.append("']");
        }
        List<CmsBtProductModel> errProducts = sxData.getProductList()
                .stream()
                .filter(p -> p.getPlatform(sxData.getCartId()) != null && "Error".equals(p.getPlatform(sxData.getCartId()).getpPublishError()))
                .collect(Collectors.toList());
        if (ListUtils.notNull(errProducts)) {
            sbProcContent.append("[pPublishError:'Error'->'']");
        }
        insModel.setProcContent(StringUtils.isEmpty(sbProcContent.toString()) ?
                "[PlatformStatus,PlatformPid或pPublishError等关键状态信息没有变化]" : sbProcContent.toString());

        return cmsBtWorkloadHistoryDao.insert(insModel);
    }

    /**
     * 插入上新表的唯一一个正式的统一入口 (单个产品的场合)
     * @param productModel  产品数据
     * @param modifier      修改者
     */
    public void insertSxWorkLoad(CmsBtProductModel productModel, String modifier) {
        productModel.getPlatforms().forEach( (cartId, platform) -> {
            if (CmsConstants.ProductStatus.Approved.name().equals(platform.getStatus())
                    && (StringUtils.isEmpty(productModel.getLock()) || "0".equals(productModel.getLock()))) {
                insertSxWorkLoad(productModel.getChannelId(), productModel.getCommon().getFields().getCode(), platform.getCartId(), modifier, isSmartSx(productModel.getChannelId(), platform.getCartId()));
            }
        });
    }
    /**
     * 插入上新表的唯一一个正式的统一入口 (单个产品的场合)
     * @param productModel  产品数据
     * @param modifier      修改者
     * @param blnSmartSx 是否是智能上新
     */
    public void insertSxWorkLoad(CmsBtProductModel productModel, String modifier, boolean blnSmartSx) {
        productModel.getPlatforms().forEach( (cartId, platform) -> {
            if (CmsConstants.ProductStatus.Approved.name().equals(platform.getStatus())
                    && (StringUtils.isEmpty(productModel.getLock()) || "0".equals(productModel.getLock()))) {
                insertSxWorkLoad(productModel.getChannelId(), productModel.getCommon().getFields().getCode(), platform.getCartId(), modifier, blnSmartSx);
            }
        });
    }

    /**
     * 插入上新表的唯一一个正式的统一入口 (单个产品指定平台的场合)
     * @param productModel  产品数据
     * @param modifier      修改者
     */
    public void insertSxWorkLoad(CmsBtProductModel productModel, List<String> cartIdList, String modifier) {
        // 输入参数检查
        if (productModel == null || ListUtils.isNull(cartIdList)) {
            $warn("insertSxWorkLoad(单个产品指定平台的场合) 参数不对");
            return;
        }

        productModel.getPlatforms().forEach( (cartId, platform) -> {
            // 指定平台，已批准且未锁定时插入指定平台的workload上新
            if (cartIdList.contains(StringUtils.toString(platform.getCartId()))
                    && CmsConstants.ProductStatus.Approved.name().equals(platform.getStatus())
                    && (StringUtils.isEmpty(productModel.getLock()) || "0".equals(productModel.getLock()))) {
                insertSxWorkLoad(productModel.getChannelId(), productModel.getCommon().getFields().getCode(), platform.getCartId(), modifier, isSmartSx(productModel.getChannelId(), platform.getCartId()));
            }
        });
    }
    /**
     * 插入上新表的唯一一个正式的统一入口 (单个产品指定平台的场合)
     * @param productModel  产品数据
     * @param modifier      修改者
     * @param blnSmartSx 是否是智能上新
     */
    public void insertSxWorkLoad(CmsBtProductModel productModel, List<String> cartIdList, String modifier, boolean blnSmartSx) {
        // 输入参数检查
        if (productModel == null || ListUtils.isNull(cartIdList)) {
            $warn("insertSxWorkLoad(单个产品指定平台的场合) 参数不对");
            return;
        }

        productModel.getPlatforms().forEach( (cartId, platform) -> {
            // 指定平台，已批准且未锁定时插入指定平台的workload上新
            if (cartIdList.contains(StringUtils.toString(platform.getCartId()))
                    && CmsConstants.ProductStatus.Approved.name().equals(platform.getStatus())
                    && (StringUtils.isEmpty(productModel.getLock()) || "0".equals(productModel.getLock()))) {
                insertSxWorkLoad(productModel.getChannelId(), productModel.getCommon().getFields().getCode(), platform.getCartId(), modifier, blnSmartSx);
            }
        });
    }

    /**
     * 插入上新表的唯一一个正式的统一入口 (单个code的场合)
     * @param channelId channel id
     * @param code code
     * @param cartId cartId (如果指定cartId, 那么就只插入指定cart的数据, 如果传入null, 那么就是默认全渠道) (会自动去除没有勾选不能上新的渠道)
     * @param modifier 修改者
     */
    public void insertSxWorkLoad(String channelId, String code, Integer cartId, String modifier) {
        insertSxWorkLoad(channelId, code, cartId, modifier, isSmartSx(channelId, cartId));
    }
    /**
     * 插入上新表的唯一一个正式的统一入口 (单个code的场合)
     * @param channelId channel id
     * @param code code
     * @param cartId cartId (如果指定cartId, 那么就只插入指定cart的数据, 如果传入null, 那么就是默认全渠道) (会自动去除没有勾选不能上新的渠道)
     * @param modifier 修改者
     * @param blnSmartSx 是否是智能上新
     */
    public void insertSxWorkLoad(String channelId, String code, Integer cartId, String modifier, boolean blnSmartSx) {
        List<String> codeList = new ArrayList<>();
        codeList.add(code);
        insertSxWorkLoad(channelId, codeList, cartId, modifier, blnSmartSx);
    }

    /**
     * 插入上新表的唯一一个正式的统一入口 (批量code的场合)
     * @param channelId channel id
     * @param codeList code列表, 允许重复(重复会自动合并)
     * @param cartId cartId (如果指定cartId, 那么就只插入指定cart的数据, 如果传入null, 那么就是默认全渠道) (会自动去除没有勾选不能上新的渠道)
     * @param modifier 修改者
     */
    public void insertSxWorkLoad(String channelId, List<String> codeList, Integer cartId, String modifier) {
        insertSxWorkLoad(channelId, codeList, cartId, modifier, isSmartSx(channelId, cartId));
    }
    /**
     * 插入上新表的唯一一个正式的统一入口 (批量code的场合)
     * @param channelId channel id
     * @param codeList code列表, 允许重复(重复会自动合并)
     * @param cartId cartId (如果指定cartId, 那么就只插入指定cart的数据, 如果传入null, 那么就是默认全渠道) (会自动去除没有勾选不能上新的渠道)
     * @param modifier 修改者
     * @param blnSmartSx 是否是智能上新
     */
    public void insertSxWorkLoad(String channelId, List<String> codeList, Integer cartId, String modifier, boolean blnSmartSx) {
        // 输入参数检查
        if (StringUtils.isEmpty(channelId) || codeList == null || codeList.isEmpty() || StringUtils.isEmpty(modifier)) {
            $warn("insertSxWorkLoad 缺少参数");
            return;
        }

        // 准备插入workload表的数据
        List<CmsBtSxWorkloadModel> modelList = new ArrayList<>();
        // 已处理过的group(防止同一个group多次被插入)
        List<Long> groupWorkList = new ArrayList<>();

        for (String prodCode : codeList) {
            // 根据商品code获取其所有group信息(所有平台)
            List<CmsBtProductGroupModel> groups = cmsBtProductGroupDao.select("{\"productCodes\": \"" + prodCode + "\"}", channelId);
            for (CmsBtProductGroupModel group : groups) {
                if (groupWorkList.contains(group.getGroupId())) {
                    // 如果已经处理过了, 那么就跳过
                    continue;
                } else {
                    groupWorkList.add(group.getGroupId());
                }

                // 如果cart是0或者1的话, 直接就跳过, 肯定不用上新的.
                if (group.getCartId() < CmsConstants.ACTIVE_CARTID_MIN) {
                    continue;
                }

                if (cartId != null && cartId.intValue() != group.getCartId().intValue()) {
                    // 指定了cartId, 并且指定的cartId并不是现在正在处理的group的场合, 跳过
                    continue;
                }

                // 20160707 tom 加速, 不再做检查 START
//                // 根据groupId获取group的上新信息
//                SxData sxData = getSxProductDataByGroupId(channelId, group.getGroupId());
//
//                // 判断是否需要上新
//                if (sxData == null) {
//                    continue;
//                }
//                if (sxData.getProductList().size() == 0) {
//                    continue;
//                }
//                if (sxData.getSkuList().size() == 0) {
//                    continue;
//                }
                // 20160707 tom 加速, 不再做检查 END

                // 加入等待上新列表
                CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
                model.setChannelId(channelId);
                model.setCartId(group.getCartId());
                model.setGroupId(group.getGroupId());
                if (blnSmartSx) {
                    // 智能上新是3
                    model.setPublishStatus(3);
                } else {
                    // 普通上新是0
                    model.setPublishStatus(0);
                }
                model.setModifier(modifier);
                model.setModified(DateTimeUtil.getDate());
                model.setCreater(modifier);
                model.setCreated(DateTimeUtil.getDate());
                modelList.add(model);

            }

        }

        // 插入上新表
        int iCnt = 0;
        if (!modelList.isEmpty()) {
            // 避免一下子插入数据太多, 分批插入
            List<CmsBtSxWorkloadModel> modelListFaster = new ArrayList<>();

            for (int i = 0; i < modelList.size(); i++) {
                modelListFaster.add(modelList.get(i));

                if (i % 301 == 0 ) {
                    // 插入一次数据库
                    iCnt += sxWorkloadDao.insertSxWorkloadModels(modelListFaster);

                    // 初始化一下
                    modelListFaster = new ArrayList<>();
                }
            }

            if (modelListFaster.size() > 0) {
                // 最后插入一次数据库
                iCnt += sxWorkloadDao.insertSxWorkloadModels(modelListFaster);
            }

            // 逻辑删除cms_bt_business_log中以前的错误,即把status更新成1:已解决
            modelList.forEach(p -> {
                clearBusinessLog(p.getChannelId(), p.getCartId(), p.getGroupId(), null, null, p.getModifier());
            });
        }
        $debug("insertSxWorkLoad 新增SxWorkload结果 " + iCnt);
    }

    public FeedCustomPropService getCustomPropService() {
        return customPropService;
    }

    /**
     * 百度翻译
     *
     * @param transBaiduOrg 要翻译的内容
     * @return
     */
    public List<String> transBaidu(List<String> transBaiduOrg) {
        List<String> transBaiduCn = null;
        if (transBaiduOrg.size() > 0) {
            try {
                transBaiduCn = BaiduTranslateUtil.translate(transBaiduOrg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return transBaiduCn;
    }

    /**
     * 判断产品是否为新品
     * 第一次上新的时候，设为新品;更新的时候，如果当前时间距离首次上新时间<=60天时，设为新品;否则设为非新品
     *
     * @param mainSxProduct 上新主产品
     * @return boolean 是否为新品
     */
    public boolean isXinPin(CmsBtProductModel mainSxProduct, int cartId) {
        if (mainSxProduct == null) return false;

        String pNumIId =  mainSxProduct.getPlatformNotNull(cartId).getpPublishTime();
        String pPublishTime = mainSxProduct.getPlatformNotNull(cartId).getpPublishTime();
        Date pPublishTimedate = DateTimeUtil.parseToGmt(pPublishTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
        Date currentTimeDate = DateTimeUtil.getDate();
        // 新品判断逻辑(第一次上新的时候，设为新品;更新的时候，如果当前时间距离首次上新时间<=60天时，设为新品，否则设为非新品)
        if ((StringUtils.isEmpty(pNumIId) && StringUtils.isEmpty(pPublishTime))
                || (!StringUtils.isEmpty(pPublishTime) && DateTimeUtil.diffDays(currentTimeDate, pPublishTimedate) <= 60)) {
            return true;
        }

        return false;
    }

    /**
     * 设置对象Field列表中指定Field的单个值
     *
     * @param fields     对象Field列表
     * @param fieldId    需要设置值的对象FieldId
     * @param fieldValue 需要设置Field值
     */
    public void setFieldValue(List<Field> fields, String fieldId, String fieldValue) {
        if (ListUtils.isNull(fields) || StringUtils.isEmpty(fieldId)) return;

        List<String> fieldValues = new ArrayList<>();
        fieldValues.add(fieldValue);

        this.setFieldValues(fields, fieldId, fieldValues);
    }

    /**
     * 设置对象Field列表中指定Field的多个值
     *
     * @param fields     对象Field列表
     * @param fieldId    需要设置值的对象FieldId
     * @param fieldValues 需要设置Field值列表
     */
    public void setFieldValues(List<Field> fields, String fieldId, List<String> fieldValues) {
        if (ListUtils.isNull(fields) || StringUtils.isEmpty(fieldId)) return;

        if (fields.stream().filter(p -> fieldId.equalsIgnoreCase(p.getId())).count() > 0) {
            Field updateField = fields.stream().filter(p -> fieldId.equalsIgnoreCase(p.getId())).findFirst().get();
            this.setFieldValues(updateField, fieldValues);
        }
    }

    /**
     * 设置对象Field列表中指定Field的单个值
     *
     * @param field     对象Field
     * @param fieldValue Field值
     */
    public void setFieldValue(Field field, String fieldValue) {
        List<String> fieldValues = new ArrayList<>();
        fieldValues.add(fieldValue);

        this.setFieldValues(field, fieldValues);
    }

    /**
     * 设置对象Field列表中指定Field的多个值
     *
     * @param field     对象Field
     * @param fieldValues Field值列表
     */
    public void setFieldValues(Field field, List<String> fieldValues) {
        if (field == null || ListUtils.isNull(fieldValues)) return;

        switch (field.getType()) {
            case INPUT: {
                InputField inputField = (InputField) field;
                inputField.setValue(fieldValues.get(0));
                break;
            }
            case SINGLECHECK: {
                SingleCheckField singleCheckField = (SingleCheckField) field;
                singleCheckField.setValue(fieldValues.get(0));
                break;
            }
            case MULTIINPUT:
                break;
            case MULTICHECK: {
                MultiCheckField multiCheckField = (MultiCheckField) field;
                // 先把以前的值清空，再加新的值
                List<Value> values = new ArrayList<>();
                multiCheckField.setValues(values);
                for (String value : fieldValues) {
                    multiCheckField.addValue(value);
                }
                break;
            }
            case COMPLEX:
                break;
            case MULTICOMPLEX:
                break;
            case LABEL:
                break;
            default:
                $error("复杂类型的属性:" + field.getType() + "不能使用setFieldValues来设值");
        }
    }
}
