package com.voyageone.task2.cms.service.promotion.beat;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureGetResponse;
import com.taobao.api.response.PictureUploadResponse;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbItemService;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.task.beat.ConfigBean;
import com.voyageone.service.bean.cms.task.beat.TaskBean;
import com.voyageone.service.model.cms.CmsBtBeatInfoModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.enums.BeatFlag;
import com.voyageone.service.model.cms.enums.ImageCategoryType;
import com.voyageone.task2.base.BaseTaskService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * Created by jonasvlag on 16/3/3.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class BeatJobService extends BaseTaskService {

    @Autowired
    private CmsBeatInfoService beatInfoService;
    @Autowired
    private TbItemService tbItemService;
    @Autowired
    private ImageCategoryService imageCategoryService;
    @Autowired
    private TbPictureService tbPictureService;

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

        String thread_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.thread_count);

        String atom_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.atom_count);

        final int THREAD_COUNT = Integer.valueOf(thread_count);

        final int PRODUCT_COUNT_ON_THREAD = Integer.valueOf(atom_count);

        int limit = PRODUCT_COUNT_ON_THREAD * THREAD_COUNT;

        List<CmsBtBeatInfoModel> beatInfoModels = beatInfoService.getNeedBeatData(limit);

        $info("预定抽取数量：%s，实际抽取数量：%s", limit, beatInfoModels.size());

        // 计算需要的拆分次数
        int total = beatInfoModels.size();

        // 拆分集合并创建任务
        List<Runnable> runnableList = new ArrayList<>();

        for (int i = 0; i < total; i += PRODUCT_COUNT_ON_THREAD) {
            int end = i + PRODUCT_COUNT_ON_THREAD;

            if (end > total) end = total;

            List<CmsBtBeatInfoModel> subList = beatInfoModels.subList(i, end);

            runnableList.add(() -> {
                for (CmsBtBeatInfoModel bean : subList) {
                    bean.clearMessage();
                    Context context = null;
                    try {
                        context = new Context(bean);
                        Boolean result = context.update();
                        if (result != null) {
                            if (result)
                                setSuccess(bean);
                            else
                                setFail(bean);
                        }
                    } catch (GetUpdateSchemaFailException getUpdateSchemaFailException) {
                        String message = format("价格披露2 调用 getUpdateSchema 失败了 -> %s", getUpdateSchemaFailException.getMessage());
                        $info(message);
                        bean.setMessage(message);
                        switch (getUpdateSchemaFailException.getSubCode()) {
                            // 商品已删除则返回失败。终止重试
                            case "isv.invalid-permission:item-deleted":
                                setFail(bean);
                                break;
                            // 其他继续重试
                        }
                    } catch (TopSchemaException topSchemaException) {
                        String message = format("价格披露2 出现 TopSchemaException -> %s", topSchemaException.getMessage());
                        $info(message);
                        bean.setMessage(message);
                        // 不再重试
                        setFail(bean);
                    } catch (ApiException apiException) {
                        String message = format("价格披露2 出现 ApiException -> %s (%s) -> %s (%s)", apiException.getClass().getName(), apiException.getErrCode(), apiException.getErrMsg(), apiException.getMessage());
                        $info(message);
                        bean.setMessage(message);
                        logIssue(apiException, bean);
                        // 继续重试
                    } catch (IOException e) {
                        String message = format("价格披露2 出现 IOException -> %s -> %s", e.getClass().getName(), e.getMessage());
                        $info(message);
                        bean.setMessage(message);
                        logIssue(e, bean);
                        // 继续重试
                    }

                    bean.setModifier(getTaskName());
                    beatInfoService.saveFlagAndMessage(bean);
                }
            });
        }

        // 运行线程
        runWithThreadPool(runnableList, taskControlList);
    }

    private void setSuccess(CmsBtBeatInfoModel beatInfoModel) {
        switch (beatInfoModel.getBeatFlag()) {
            case BEATING:
                beatInfoModel.setBeatFlag(BeatFlag.SUCCESS);
                break;
            case REVERT:
                beatInfoModel.setBeatFlag(BeatFlag.RE_SUCCESS);
                break;
        }
    }

    private void setFail(CmsBtBeatInfoModel beatInfoModel) {
        switch (beatInfoModel.getBeatFlag()) {
            case BEATING:
                beatInfoModel.setBeatFlag(BeatFlag.FAIL);
                break;
            case REVERT:
                beatInfoModel.setBeatFlag(BeatFlag.RE_FAIL);
                break;
        }
    }

    private class Context {

        private ShopBean shopBean;

        private CmsMtImageCategoryModel upCategory;

        private CmsMtImageCategoryModel downCategory;

        private CmsBtBeatInfoModel beatInfoModel;

        private TaskBean taskBean;

        private ConfigBean configBean;

        private CmsBtPromotionModel promotion;

        private TbItemSchema tbItemSchema;

        private Context(CmsBtBeatInfoModel beatInfoModel) throws TopSchemaException, ApiException, GetUpdateSchemaFailException {
            this.beatInfoModel = beatInfoModel;
            this.promotion = beatInfoModel.getPromotion();
            this.shopBean = Shops.getShop(promotion.getChannelId(), promotion.getCartId());

            // TODO 测试代码
            this.shopBean.setAppKey("21008948");
            this.shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
            this.shopBean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");
            this.shopBean.setApp_url("http://gw.api.taobao.com/router/rest");

            this.tbItemSchema = tbItemService.getUpdateSchema(shopBean, beatInfoModel.getNum_iid());
            this.taskBean = new TaskBean(beatInfoModel.getTask());
            this.configBean = taskBean.getConfig();
            this.upCategory = imageCategoryService.getCategory(shopBean, ImageCategoryType.Beat);
            this.downCategory = imageCategoryService.getCategory(shopBean, ImageCategoryType.Main);
        }

        private Boolean update() throws IOException, ApiException, TopSchemaException {
            tbItemSchema.setFieldValue();
            Map<Integer, String> images = new HashMap<>();
            images.put(1, getTaobaoImageUrl(beatInfoModel.getPromotion_code().getImage_url_1()));
            if (configBean.isNeed_vimage()) {
                if (tbItemSchema.hasVerticalImage())
                    tbItemSchema.setVerticalImage(getTaobaoVerticalImageUrl());
                else {
                    String url_key2 = beatInfoModel.getPromotion_code().getImage_url_2();
                    if (!StringUtils.isEmpty(url_key2))
                        images.put(2, getTaobaoImageUrl(beatInfoModel.getPromotion_code().getImage_url_2()));
                    else {
                        // 如果不为空, 则还原和刷图有区别, 还原需要清空
                        images.put(2, beatInfoModel.getBeatFlag().equals(BeatFlag.REVERT) ? "" : images.get(1));
                    }
                }
            }
            tbItemSchema.setMainImage(images);
            TmallItemSchemaUpdateResponse updateResponse = tbItemService.updateFields(shopBean, tbItemSchema);

            if (updateResponse == null) {
                beatInfoModel.setMessage("更新商品信息时, 没获取到响应.");
                return false;
            }

            $info("调用 ItemSchemaUpdate -> [ %s ] [ %s ] [ %s ] [ %s ] [ %s ]", updateResponse.getUpdateItemResult(),
                    updateResponse.getGmtModified(), updateResponse.getMsg(),
                    updateResponse.getSubCode(), updateResponse.getSubMsg());

            if (StringUtils.isEmpty(updateResponse.getSubCode())) {
                beatInfoModel.setMessage("success");
                return true;
            }

            $info("商品更新失败了。[ %s ] [ %s ] [ %s ]", beatInfoModel.getNum_iid(), updateResponse.getSubCode(), updateResponse.getSubMsg());

            // 指定忽略这些错误，让后续任务可以重新尝试
            switch (updateResponse.getSubCode()) {
                case "isp.service-unavailable": // 服务不可用
                    return null;
            }

            beatInfoModel.setMessage(format("修改淘宝商品时失败了。[ %s ] [ %s ]", updateResponse.getSubCode(), updateResponse.getSubMsg()));
            return false;
        }

        private String getTaobaoImageUrl(String image_url_key) throws IOException, ApiException {
            String imageUrl, imageName;
            CmsMtImageCategoryModel categoryModel;
            switch (beatInfoModel.getBeatFlag()) {
                case BEATING:
                    imageUrl = configBean.getBeat_template();
                    imageName = image_url_key + ".beat.jpg";
                    categoryModel = upCategory;
                    break;
                case REVERT:
                case SUCCESS:
                    imageUrl = configBean.getRevert_template();
                    imageName = image_url_key + ".jpg";
                    categoryModel = downCategory;
                    break;
                default:
                    return null;
            }
            return getTaobaoImageUrl(formatUrl(imageUrl, image_url_key, beatInfoModel.getPromotion_code().getPromotionPrice()), imageName, categoryModel);
        }

        private String getTaobaoVerticalImageUrl() throws IOException, ApiException {
            CmsBtPromotionCodesBean code = beatInfoModel.getPromotion_code();
            String imageUrl, imageName, image_url_key = code.getImage_url_3();
            CmsMtImageCategoryModel categoryModel;
            switch (beatInfoModel.getBeatFlag()) {
                case BEATING:
                    imageUrl = configBean.getBeat_vtemplate();
                    imageName = image_url_key + ".beat.jpg";
                    categoryModel = upCategory;
                    break;
                case REVERT:
                case SUCCESS:
                    imageUrl = configBean.getRevert_vtemplate();
                    imageName = image_url_key + ".jpg";
                    categoryModel = downCategory;
                    break;
                default:
                    return null;
            }
            return getTaobaoImageUrl(formatUrl(imageUrl, image_url_key, code.getPromotionPrice()), imageName, categoryModel);
        }

        private String formatUrl(String imageUrl, String image_url_key, Double price) {
            return imageUrl.replace("{key}", image_url_key).replace("{price}", new DecimalFormat("#").format(price));
        }

        private String getTaobaoImageUrl(String imageUrl, String imageName, CmsMtImageCategoryModel categoryModel)
                throws IOException, ApiException {
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
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            PictureUploadResponse uploadResponse = tbPictureService.uploadPicture(shopBean, bytes, imageName, categoryModel.getCategory_tid());
            if (uploadResponse.isSuccess() && StringUtils.isEmpty(uploadResponse.getSubCode()))
                // 成功返回
                return uploadResponse.getPicture().getPicturePath();
            String message = format("上传主图失败：[ %s ] [ %s ]", uploadResponse.getSubCode(), uploadResponse.getSubMsg());
            $info(message);
            beatInfoModel.setMessage(message);
            return null;
        }
    }
}
