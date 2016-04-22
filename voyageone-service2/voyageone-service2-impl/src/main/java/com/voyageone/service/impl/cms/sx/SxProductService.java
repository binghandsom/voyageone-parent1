package com.voyageone.service.impl.cms.sx;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtPlatformImagesDao;
import com.voyageone.service.dao.cms.CmsBtSizeMapDao;
import com.voyageone.service.dao.cms.CmsBtSxWorkloadDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPlatformImagesModel;
import com.voyageone.service.model.cms.CmsBtSizeMapModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
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

    @Autowired
    private TbPictureService tbPictureService;

    @Autowired
    private CmsBtSxWorkloadDao sxWorkloadDao;

    @Autowired
    private ImsBtProductDao imsBtProductDao;

    @Autowired
    private CmsBtSizeMapDao cmsBtSizeMapDao;

    @Autowired
    private CmsBtPlatformImagesDao cmsBtPlatformImagesDao;

    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /** upd_flg=0,需要上传(重新上传) */
    private static final String UPD_FLG_ADD ="0";
    /** upd_flg=1,已经上传 */
    private static final String UPD_FLG_UPLOADED ="1";

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
     * @param sizeMapGroupId 尺码对照表id
     * @param originalSize   转换前size
     * @return 转后后size
     */
    public String changeSize(int sizeMapGroupId, String originalSize) {
        // cms_bt_size_map
        CmsBtSizeMapModel result = cmsBtSizeMapDao.selectSizeMap(sizeMapGroupId, originalSize);
        if (result != null) {
            return result.getAdjustSize();
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

        List<CmsBtPlatformImagesModel> imageUrlModel = cmsBtPlatformImagesDao.selectPlatformImagesList(channelId, cartId, groupId);

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

                    cmsBtPlatformImagesDao.updatePlatformImagesById(model, user);
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
            cmsBtPlatformImagesDao.insertPlatformImagesByList(imageUrlSaveModels);
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

    private String decodeImageUrl(String encodedValue) {
        return encodedValue.substring(0, encodedValue.length() - 2);
    }

    /**
     * 上新用的商品数据取得
     *
     * @param channelId channelId
     * @param groupId groupId
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
        for (CmsBtProductModel productModel: productModelList) {
            if (mainProductCode.equals(productModel.getFields().getCode())) {
                // 主商品
                sxData.setMainProduct(productModel);
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
}
