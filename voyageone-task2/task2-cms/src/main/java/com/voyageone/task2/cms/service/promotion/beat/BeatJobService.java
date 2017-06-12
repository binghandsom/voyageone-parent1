package com.voyageone.task2.cms.service.promotion.beat;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureGetResponse;
import com.taobao.api.response.PictureUploadResponse;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbItemService;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.components.tmall.service.TbSimpleItemService;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.task.beat.ConfigBean;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.enums.ImageCategoryType;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;
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
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.String.format;

/**
 * Created by jonasvlag on 16/3/3.
 *
 * @version 2.6.0
 * @since 2.0.0
 */
@Service
public class BeatJobService extends BaseCronTaskService {

    /**
     * 用于表示错误位置。用来区分判断是否需要发送错误报告。
     * 为 0 表示无错误。
     * 为 1 表示空值错误。
     * 为 2 表示值类型错误。
     */
    private static int lastErrorTarget = 0;

    static int getLastErrorTarget() {
        return lastErrorTarget;
    }

    private final CmsBeatInfoService beatInfoService;

    private final TbItemService tbItemService;

    private final TbSimpleItemService tbSimpleItemService;

    private final ImageCategoryService imageCategoryService;

    private final TbPictureService tbPictureService;

    @Autowired
    public BeatJobService(CmsBeatInfoService beatInfoService, TbItemService tbItemService,
                          TbSimpleItemService tbSimpleItemService, TbPictureService tbPictureService,
                          ImageCategoryService imageCategoryService) {
        this.beatInfoService = beatInfoService;
        this.tbItemService = tbItemService;
        this.tbSimpleItemService = tbSimpleItemService;
        this.tbPictureService = tbPictureService;
        this.imageCategoryService = imageCategoryService;
    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBeatJob2";
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

        List<CmsBtBeatInfoBean> beatInfoModels = beatInfoService.getNeedBeatData(LIMIT);

        if (beatInfoModels.isEmpty()) {
            $info("没有需要进行处理的价格披露任务");
            return;
        }

        $info("预定抽取数量：%s，实际抽取数量：%s", LIMIT, beatInfoModels.size());

        // 计算需要的拆分次数
        int total = beatInfoModels.size();

        // 拆分集合并创建任务
        List<Runnable> runnableList = new ArrayList<>();

        for (int i = 0; i < total; i += PRODUCT_COUNT_ON_THREAD) {
            int end = i + PRODUCT_COUNT_ON_THREAD;

            if (end > total) end = total;

            List<CmsBtBeatInfoBean> subList = beatInfoModels.subList(i, end);

            runnableList.add(() -> {
                for (CmsBtBeatInfoBean bean : subList) {

                    // 每次任务前, 重置信息
                    bean.clearMessage();
                    Context context;

                    try {
                        // 创建上下文数据
                        context = new Context(bean);
                        // 调用接口刷图
                        Boolean result = context.update();

                        if (result != null) {
                            // 不为空, 则标记任务结果
                            if (result)
                                succeed(bean);
                            else
                                fail(bean);
                        }
                        // result 为空, 表示下一次重试

                    } catch (BreakBeatJobException breakBeatJobException) {

                        bean.setMessage(breakBeatJobException.getMessage());
                        Throwable innerException = breakBeatJobException.getCause();
                        if (innerException != null) {
                            String innerMessage = innerException.getMessage();
                            if (StringUtils.isEmpty(innerMessage))
                                innerMessage = "未知的内部异常";
                            bean.setMessage(innerMessage);
                        }
                        fail(bean);

                    } catch (GetUpdateSchemaFailException | TopSchemaException | ApiException exception) {

                        // 对已知可能出现的异常, 提供相对明确的信息格式化
                        // 并且, 这种异常并不发送错误报告

                        String message = exception.getMessage();

                        if (exception instanceof GetUpdateSchemaFailException) {
                            GetUpdateSchemaFailException getUpdateSchemaFailException = (GetUpdateSchemaFailException) exception;
                            String subCode = getUpdateSchemaFailException.getSubCode();
                            if (!StringUtils.isEmpty(subCode))
                                message += "(" + subCode + ")";
                        } else if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            message = format("%s (%s) -> %s (%s)",
                                    apiException.getClass().getName(),
                                    apiException.getErrCode(),
                                    apiException.getErrMsg(),
                                    apiException.getMessage());
                        }

                        $error(format("价格披露2 出现 %s 异常 -> %s", exception.getClass().getName(), message), exception);

                        bean.setMessage(format("出现 %s 异常: %s", getGoodName(exception), message));
                        fail(bean);

                    } catch (Exception exception) {

                        $error("价格披露2 出现异常", exception);

                        // 对未知异常发送错误报告
                        logIssue(exception, bean.getId());

                        bean.setMessage(format("出现 %s 异常: %s", getGoodName(exception), exception.getMessage()));
                        fail(bean);
                    }

                    $info(bean.getProductCode() + " 图片替换完成");

                    bean.setModifier(getTaskName());

                    try {
                        beatInfoService.saveFlagAndMessage(bean);
                    } catch (Exception e) {
                        $error("卧槽，这里也报错", e);
                    }

                    $info(bean.getProductCode() + " 数据也 save 好了");
                }
            });
        }

        // 运行线程
        runWithThreadPool(runnableList, taskControlList);
    }

    /**
     * 优化异常名称的显示
     * <p>如 java.lang.NullPointerException 将转换为 NullPointer</p>
     */
    private String getGoodName(Exception exception) {
        String exceptionName = exception.getClass().getSimpleName();
        return exceptionName.replace("Exception", "");
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

        if (errorTarget > 0)
            // 如果有错误
            if (errorTarget != lastErrorTarget) {
                // 并且该错误不是上次发送的错误。则这次发送该错误
                // 并重置错误位置标识
                lastErrorTarget = errorTarget;
                throw new BusinessException(errorMessage);
            } else {
                // 否则, 之前无错误, 或被发送过, 则这次的检查将被忽略
                return null;
            }
        else
            // 如果没错误
            // 则这次不会发送, 并重置标识
            lastErrorTarget = errorTarget;

        return new int[]{Integer.valueOf(thread_count), Integer.valueOf(atom_count)};
    }

    private void succeed(CmsBtBeatInfoBean beatInfoModel) {
        switch (beatInfoModel.getSynFlagEnum()) {
            case BEATING:
                beatInfoModel.setSynFlag(BeatFlag.SUCCESS);
                break;
            case SUCCESS:
            case REVERT:
                beatInfoModel.setSynFlag(BeatFlag.RE_SUCCESS);
                break;
        }
    }

    private void fail(CmsBtBeatInfoBean beatInfoModel) {
        switch (beatInfoModel.getSynFlagEnum()) {
            case BEATING:
                beatInfoModel.setSynFlag(BeatFlag.FAIL);
                break;
            case SUCCESS:
            case REVERT:
                beatInfoModel.setSynFlag(BeatFlag.RE_FAIL);
                break;
        }
    }

    private class Context {

        private ShopBean shopBean;

        private CmsMtImageCategoryModel upCategory;

        private CmsMtImageCategoryModel downCategory;

        private CmsBtBeatInfoBean beatInfoBean;

        private ConfigBean configBean;

        private TbItemSchema tbItemSchema;

        private CmsMtImageCategoryModel getUpCategory() {
            if (upCategory == null)
                upCategory = imageCategoryService.getCategory(shopBean, ImageCategoryType.Beat);
            return upCategory;
        }

        private CmsMtImageCategoryModel getDownCategory() {
            if (downCategory == null)
                downCategory = imageCategoryService.getCategory(shopBean, ImageCategoryType.Main);
            return downCategory;
        }

        private TbItemSchema getTbItemSchema() throws TopSchemaException, ApiException, GetUpdateSchemaFailException {
            if (tbItemSchema == null) {
                if (isSimpleCart())
                    tbItemSchema = tbSimpleItemService.getSimpleItem(shopBean, beatInfoBean.getNumIid());
                else
                    tbItemSchema = tbItemService.getUpdateSchema(shopBean, beatInfoBean.getNumIid());
            }
            return tbItemSchema;
        }

        /**
         * 获取是否是同购店
         *
         * @return 是否是同购店
         * @since 2.6.0
         */
        private boolean isSimpleCart() {

            CartEnums.Cart cart = CartEnums.Cart.getValueByID(shopBean.getCart_id());

            return CartEnums.Cart.isSimple(cart);
        }

        private Context(CmsBtBeatInfoBean beatInfoBean) {

            CmsBtPromotionModel promotion = beatInfoBean.getPromotion();

            this.beatInfoBean = beatInfoBean;
            this.shopBean = Shops.getShop(promotion.getChannelId(), promotion.getCartId());
            this.configBean = JacksonUtil.json2Bean(beatInfoBean.getTask().getConfig(), ConfigBean.class);
        }

        private InputStream getImageStream(String templateUrl, String imageName, boolean withPrice) {

            // 检查当前图片状态
            ImageStatus currentStatus = beatInfoBean.getImageStatusEnum();

            // 如果没有, 则默认为 None
            if (currentStatus == null)
                currentStatus = ImageStatus.None;

            // 如果是 Error, 放弃执行
            if (currentStatus == ImageStatus.Error)
                throw new BreakBeatJobException("取图失败");

            // 此处预想逻辑是异步处理图片。此时使用同步所以, 只要不是 Error 状态。都直接请求取图
            // 所以也就是只要不是 Error 其他状态都直接取图

            Double promotionPrice = beatInfoBean.getPromotion_code().getPromotionPrice();
            try {
                String imageUrl;
                if (withPrice)
                    imageUrl = templateUrl.replace("{key}", imageName).replace("{price}", new DecimalFormat("#.##").format(promotionPrice));
                else
                    imageUrl = templateUrl.replace("{key}", imageName);

                URL url = new URL(imageUrl);
                return url.openStream();
            } catch (Exception e) {
                beatInfoBean.setImageStatus(ImageStatus.Error);
                $info("取图失败, 发生异常", e);
                throw new BreakBeatJobException("取图失败, 发生异常", e);
            }
        }

        private String getTaobaoImageUrl(String imageUrl, String imageName, CmsMtImageCategoryModel categoryModel, boolean withPrice)
                throws IOException, ApiException {

            Objects.requireNonNull(categoryModel, "图片目录未配置");

            // 首先尝试获取之前存在图片
            // 如果之前使用过, 则直接删除, 如果没有或者出现了任何错误, 都无视。
            // 直接继续上图
            try {
                PictureGetResponse pictureGetResponse = tbPictureService.getPictures(shopBean,
                        imageName, Long.valueOf(categoryModel.getCategory_tid()));

                List<Picture> pictures = pictureGetResponse.getPictures();

                if (pictures != null && !pictures.isEmpty()) {
                    Picture picture = pictures.get(0);
                    tbPictureService.deletePictures(shopBean, picture.getPictureId());
                }
            } catch (ApiException ignored) {
            }

            $info("尝试下载并上传：[ %s ] -> [ %s ] -> [ %s ]", imageUrl, imageName, categoryModel.getCategory_name());

            InputStream inputStream = getImageStream(imageUrl, imageName, withPrice);

            byte[] bytes = IOUtils.toByteArray(inputStream);

            PictureUploadResponse uploadResponse = tbPictureService.uploadPicture(shopBean, bytes, imageName, categoryModel.getCategory_tid());

            if (uploadResponse.isSuccess() && StringUtils.isEmpty(uploadResponse.getSubCode()))
                // 成功返回
                return uploadResponse.getPicture().getPicturePath();

            String message = format("上传主图失败：[ %s ] [ %s ]", uploadResponse.getSubCode(), uploadResponse.getSubMsg());

            throw new BreakBeatJobException(message);
        }

        Boolean update() throws IOException, ApiException, TopSchemaException, GetUpdateSchemaFailException {

            TbItemSchema itemSchema = getTbItemSchema();

            // 还原覆盖 Schema 的原值
            getTbItemSchema().setFieldValueWithDefault();

            // 创建结果 Map, 最终该 Map 将更新到 Schema 上
            Map<Integer, String> images = new HashMap<>();

            // 更新第一张图片
            images.put(1, getTaobaoImageUrl(beatInfoBean.getPromotion_code().getImage_url_1()));

            if (configBean.isNeed_vimage()) {
                // 如果需要更新竖图
                if (itemSchema.hasVerticalImage())
                    // 并且 Schema 有专门的竖图字段
                    // 那么就用专门的竖图来更新这个竖图字段
                    itemSchema.setVerticalImage(getTaobaoVerticalImageUrl());
                else {
                    // 木有专门的竖图字段怎么办
                    // 没事~ 就刷商品的第二张图, 貌似第二张图在手机端就是竖图。这是 IT 运营组说的
                    String url_key2 = beatInfoBean.getPromotion_code().getImage_url_2();
                    if (!StringUtils.isEmpty(url_key2))
                        // 提供了第二张图的图片 Key。就用这个刷
                        images.put(2, getTaobaoImageUrl(beatInfoBean.getPromotion_code().getImage_url_2()));
                    else {
                        // 如果不为空, 则还原和刷图有区别, 还原需要清空
                        // 如果第二张图为空, 那么刷披露图, 就复制第一张图就可以了, 不需要再折腾了
                        // 如果是要还原, 那么这个商品本身是没有第二张图的, 也就是要清空。所以设置为空字符
                        images.put(2, beatInfoBean.getSynFlagEnum().equals(BeatFlag.REVERT) ? "" : images.get(1));
                    }
                }
            }
            // 保存到 Schema
            itemSchema.setMainImage(images);

            if (tbItemSchema.isSimple()) {
                String result = tbSimpleItemService.updateSimpleItem(shopBean, itemSchema);

                if (!StringUtils.isEmpty(result) && result.startsWith("ERROR:")) {
                    beatInfoBean.setMessage(result);
                    return false;
                }

                return true;
            } else {
                TmallItemSchemaUpdateResponse updateResponse = tbItemService.updateFields(shopBean, itemSchema);

                if (updateResponse == null) {
                    beatInfoBean.setMessage("更新商品信息时, 没获取到响应.");
                    return false;
                }

                $info("调用 ItemSchemaUpdate -> [ %s ] [ %s ] [ %s ] [ %s ] [ %s ]", updateResponse.getUpdateItemResult(),
                        updateResponse.getGmtModified(), updateResponse.getMsg(),
                        updateResponse.getSubCode(), updateResponse.getSubMsg());

                if (StringUtils.isEmpty(updateResponse.getSubCode())) {
                    beatInfoBean.setMessage("success");
                    return true;
                }

                $info("商品更新失败了。[ %s ] [ %s ] [ %s ]", beatInfoBean.getNumIid(), updateResponse.getSubCode(), updateResponse.getSubMsg());

                // 指定忽略这些错误，让后续任务可以重新尝试
                switch (updateResponse.getSubCode()) {
                    case "isp.service-unavailable": // 服务不可用
                        return null;
                }

                beatInfoBean.setMessage(format("修改淘宝商品时失败了。[ %s ] [ %s ]", updateResponse.getSubCode(), updateResponse.getSubMsg()));
                return false;
            }
        }

        private String getTaobaoImageUrl(String image_url_key) throws IOException, ApiException {

            String templateUrl;
            boolean withPrice;
            CmsMtImageCategoryModel categoryModel;

            switch (beatInfoBean.getSynFlagEnum()) {
                case BEATING:
                    templateUrl = configBean.getBeat_template();
                    categoryModel = getUpCategory();
                    withPrice = true;
                    break;
                case REVERT:
                case SUCCESS:
                    templateUrl = configBean.getRevert_template();
                    categoryModel = getDownCategory();
                    withPrice = false;
                    break;
                default:
                    return null;
            }
            return getTaobaoImageUrl(templateUrl, image_url_key, categoryModel, withPrice);
        }

        private String getTaobaoVerticalImageUrl() throws IOException, ApiException {

            String templateUrl;
            CmsMtImageCategoryModel categoryModel;
            boolean withPrice;

            CmsBtPromotionCodesBean code = beatInfoBean.getPromotion_code();

            String image_url_key = code.getImage_url_3();

            // 如果没有指定竖图
            // 那么使用第一张图作为竖图
            if (StringUtils.isEmpty(image_url_key))
                image_url_key = code.getImage_url_1();

            switch (beatInfoBean.getSynFlagEnum()) {
                case BEATING:
                    templateUrl = configBean.getBeat_vtemplate();
                    categoryModel = getUpCategory();
                    withPrice = true;
                    break;
                case REVERT:
                case SUCCESS:
                    templateUrl = configBean.getRevert_vtemplate();
                    categoryModel = getDownCategory();
                    withPrice = false;
                    break;
                default:
                    return null;
            }
            return getTaobaoImageUrl(templateUrl, image_url_key, categoryModel, withPrice);
        }
    }
}
