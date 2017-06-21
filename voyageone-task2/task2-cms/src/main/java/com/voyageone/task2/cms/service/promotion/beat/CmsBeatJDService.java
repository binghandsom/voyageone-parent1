package com.voyageone.task2.cms.service.promotion.beat;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.ImageReadService.Image;
import com.jd.open.api.sdk.response.imgzone.ImgzonePictureUploadResponse;
import com.jd.open.api.sdk.response.ware.ImageReadFindImagesByWareIdResponse;
import com.jd.open.api.sdk.response.ware.SkuReadFindSkuByIdResponse;
import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureGetResponse;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jd.service.JdImgzoneService;
import com.voyageone.components.jd.service.JdWareNewService;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.bean.cms.task.beat.ConfigBean;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.enums.ImageCategoryType;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.CmsMtImageCategoryModel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

/**
 * Created by james on 2017/6/20.
 */
@Service
public class CmsBeatJDService extends BaseCronTaskService {

    @Autowired
    private CmsBeatInfoService beatInfoService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JdWareNewService jdWareNewService;

    @Autowired
    private ImageCategoryService imageCategoryService;

    @Autowired
    private JdImgzoneService jdImgzoneService;


    @Override
    protected String getTaskName() {
        return "CmsBeatJDJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 如果任意配置为空, 或不是数字, 就尝试报错。报错在成功之前, 只会发送一次
        int[] config = tryGetConfig(taskControlList);

        // 如果返回的配置为空, 则放弃执行此次任务
        if (config == null)
            return;

        final int THREAD_COUNT = config[0];
        final int PRODUCT_COUNT_ON_THREAD = config[1];
        final int LIMIT = PRODUCT_COUNT_ON_THREAD * THREAD_COUNT;

        List<CmsBtBeatInfoBean> beatInfoModels = beatInfoService.getNeedBeatData(LIMIT, Arrays.asList(26, 24));

        if (beatInfoModels.isEmpty()) {
            $info("没有需要进行处理的价格披露任务");
            return;
        }

        $info("预定抽取数量：%s，实际抽取数量：%s", LIMIT, beatInfoModels.size());
    }


    public void beatMain(CmsBtBeatInfoBean cmsBtBeatInfo) {
        ConfigBean configBean;
        ShopBean shopBean = Shops.getShop(cmsBtBeatInfo.getTask().getChannelId(), cmsBtBeatInfo.getTask().getCartId());
        shopBean.setShop_name("Sneakerhead国际旗舰店");
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
        shopBean.setSessionKey("614a5873-f72e-4efc-9208-c0c5db4e07ac");

        configBean = JacksonUtil.json2Bean(cmsBtBeatInfo.getTask().getConfig(), ConfigBean.class);

        try {
            CmsMtImageCategoryModel cmsMtImageCategoryModel = imageCategoryService.getCategory(shopBean, ImageCategoryType.Beat);
            if (cmsMtImageCategoryModel == null) throw new BusinessException("平台的图片空间取得失败");
            if (cmsBtBeatInfo.getSynFlag() == BeatFlag.BEATING.getFlag()) {
                String jdImageUrl = getJdImageUrl(shopBean, configBean.getBeat_template(), cmsBtBeatInfo.getImageName(), cmsMtImageCategoryModel, cmsBtBeatInfo.getPrice());
                $info("jdImageUrl " + jdImageUrl);
                Image image = getJDImage(shopBean, cmsBtBeatInfo.getTask().getChannelId(), cmsBtBeatInfo.getTask().getCartId(), cmsBtBeatInfo.getProductCode(), cmsBtBeatInfo.getNumIid());
                jdWareNewService.imageWriteUpdate(shopBean, cmsBtBeatInfo.getNumIid(), Collections.singletonList(image.getColorId()), Collections.singletonList(jdImageUrl), Collections.singletonList("1"));
            }
        } catch (Exception e) {
            if(cmsBtBeatInfo.getSynFlag() == BeatFlag.REVERT.getFlag()){
                cmsBtBeatInfo.setSynFlag(BeatFlag.RE_FAIL);
            }else{
                cmsBtBeatInfo.setSynFlag(BeatFlag.FAIL);
            }
            cmsBtBeatInfo.setMessage(e.getMessage());
        }
    }

    // 获取该code在京东的主图信息
    public Image getJDImage(ShopBean shopBean, String channelId, Integer cartId, String code, Long numIId) {
        CmsBtProductModel cmsBtProduct = productService.getProductByCode(channelId, code);
        if (cmsBtProduct == null) {
            throw new BusinessException("code在cms中不存在");
        }

        CmsBtProductModel_Platform_Cart platform = cmsBtProduct.getPlatform(cartId);
        if (platform == null) {
            throw new BusinessException("code在cms中platform数据不存在");
        }

        // 找到该code中其中一个skuID
        String jdSkuId = platform.getSkus()
                .stream()
                .filter(item -> !StringUtil.isEmpty(item.getStringAttribute("jdSkuId")))
                .map(item -> item.getStringAttribute("jdSkuId"))
                .findFirst().orElse("");

        if (StringUtil.isEmpty(jdSkuId)) {
            throw new BusinessException("code在jdSkuId不存在");
        }

        // 根据numiid 取得下面所有的图片
        ImageReadFindImagesByWareIdResponse imageReadFindImagesByWareIdResponse = jdWareNewService.getImageByWareId(shopBean, numIId);
        if (imageReadFindImagesByWareIdResponse == null)
            throw new BusinessException("numIId:" + numIId + "  没有取得该平台的图片信息");

        SkuReadFindSkuByIdResponse skuReadFindSkuByIdResponse = jdWareNewService.skuReadFindSkuById(shopBean, Long.parseLong(jdSkuId), "outerId,logo");
        if (skuReadFindSkuByIdResponse == null) throw new BusinessException("jdSkuId: " + jdSkuId + " 没有取得sku详情");

        Image image = imageReadFindImagesByWareIdResponse.getImages().stream().filter(item -> !item.getColorId().equals("0000000000")).filter(item -> item.getImgUrl().equals(skuReadFindSkuByIdResponse.getSku().getLogo())).findFirst().orElse(null);
        return image;
    }

    private int[] tryGetConfig(List<TaskControlBean> taskControlList) {

        String thread_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.thread_count);

        String atom_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.atom_count);

        String errorMessage = null;
        int errorTarget = 0;

        // 计算 errorMessage
        if (StringUtils.isAnyEmpty(thread_count, atom_count)) {
            errorMessage = "价格披露的线程配置为空";
            errorTarget = 1;
        } else if (!StringUtils.isNumeric(thread_count) || !StringUtils.isNumeric(atom_count)) {
            errorMessage = "价格披露的线程配置不是数字";
            errorTarget = 2;
        }

        return new int[]{Integer.valueOf(thread_count), Integer.valueOf(atom_count)};
    }

    private String getJdImageUrl(ShopBean shopBean, String templateUrl, String imageName, CmsMtImageCategoryModel categoryModel, Double promotionPrice)
            throws IOException, ApiException, JdException {

        Objects.requireNonNull(categoryModel, "图片目录未配置");

        String imageUrl;
        if (promotionPrice != null)
            imageUrl = templateUrl.replace("{key}", imageName).replace("{price}", new DecimalFormat("#.##").format(promotionPrice));
        else
            imageUrl = templateUrl.replace("{key}", imageName);

        $info("尝试下载并上传：[ %s ] -> [ %s ] -> [ %s ]", imageUrl, imageName, categoryModel.getCategory_name());

        URL url = new URL(imageUrl);
        InputStream inputStream = url.openStream();

        byte[] bytes = IOUtils.toByteArray(inputStream);

        ImgzonePictureUploadResponse uploadResponse = jdImgzoneService.uploadPicture("COMMONIMG", imageUrl, shopBean, bytes, categoryModel.getCategory_tid(), "");

        if (uploadResponse != null && !StringUtil.isEmpty(uploadResponse.getPictureUrl()))
            // 成功返回
            return uploadResponse.getPictureUrl();
        String message = format("上传主图失败：[ %d ] [ %s ]", uploadResponse.getCode(), uploadResponse.getDesc());
        throw new BusinessException(message);
    }

    private InputStream getImageStream(String templateUrl, String imageName, Double promotionPrice) throws IOException {

        String imageUrl;
        if (promotionPrice != null)
            imageUrl = templateUrl.replace("{key}", imageName).replace("{price}", new DecimalFormat("#.##").format(promotionPrice));
        else
            imageUrl = templateUrl.replace("{key}", imageName);

        URL url = new URL(imageUrl);
        return url.openStream();
    }
}
