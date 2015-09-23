package com.voyageone.batch.ims.service.beat;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureGetResponse;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.batch.ims.bean.BeatPicBean;
import com.voyageone.batch.ims.enums.BeatFlg;
import com.voyageone.batch.ims.enums.ImsPicCategoryType;
import com.voyageone.common.components.tmall.TbPictureService;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.voyageone.batch.ims.enums.BeatFlg.Startup;
import static java.lang.String.format;

/**
 * 价格披露定时任务。
 * 定时获取数据，拼接图片地址，下载并上传到 taobao 的图片空间。获取到图片在空间内的 url 后。刷新到 taobao 的商品信息中
 * <p>
 * Created by Jonas on 7/16/15.
 * modify by sky on 20150814
 */
@Service
public class ImsBeatUpdateService extends ImsBeatBaseService {

    @Autowired
    private TbPictureService tbPictureService;

    public void beat(BeatPicBean beatPicBean) {

        ShopBean shopBean = ShopConfigs.getShop(beatPicBean.getChannel_id(), beatPicBean.getCart_id());

        // 获取店铺专用主图目录
        String cate_tid = getCategory(shopBean, ImsPicCategoryType.Beat);

        $info("价格披露：获取图片目录 [ %s ] [ %s ]", beatPicBean.getNum_iid(), cate_tid);

        if (StringUtils.isEmpty(cate_tid)) {
            // 目录获取失败，则回退到设置状态，并设定结果。等待设置后重试
            beatPicBean.setComment(format("没有找到用于存放价格披露图片的目录。[ %s ] [ %s ] [ %s ]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), beatPicBean.getBeat_item_id()));
            beatPicBean.setBeat_flg(Startup);
            return;
        }

        // 获取主图地址
        String image_url = getImage(shopBean, cate_tid, beatPicBean);

        if (StringUtils.isEmpty(image_url)) {
            // 保存信息，理论上，上小段逻辑 flg 不会改动。等待后续重试
            return;
        }

        Boolean result = updateSchema(shopBean, beatPicBean, image_url);

        if (result == null) {
            // 如果为空表示重试。直接保存信息
            return;
        }

        // 否则保存执行结果
        beatPicBean.setBeat_flg(result ? BeatFlg.Passed : BeatFlg.Fail);

        $info("价格披露：披露完成 [ %s ] [ %s ]", beatPicBean.getNum_iid(), beatPicBean.getBeat_item_id());
    }

    private String getImage(ShopBean shopBean, String category_tid, BeatPicBean beatPicBean) {

        String title = getTitle(beatPicBean);

        clearLastImage(shopBean, title, category_tid);

        String url = getImageUrl(beatPicBean);

        $info("价格披露：图片下载地址: " + url);

        if (StringUtils.isEmpty(url)) return null;

        byte[] image;

        $info("价格披露：准备下载披露图片");

        try (InputStream inputStream = HttpUtils.getInputStream(url, null)) {

            image = IOUtils.toByteArray(inputStream);

        } catch (IOException e) {
            beatPicBean.setComment(format("价格披露：线程内下载图片出现异常 [ %s ] [ %s ]", e.getMessage(), url));
            $info("价格披露：线程内下载图片出现异常。[ %s ] [ %s ]", beatPicBean.getBeat_item_id(), e.getMessage());
            return null;
        }

        $info("价格披露：已下载 [ %s ]", image.length);

        try {
            PictureUploadResponse uploadResponse = tbPictureService.uploadPicture(shopBean, image, title, category_tid);

            if (uploadResponse.isSuccess() && StringUtils.isEmpty(uploadResponse.getSubCode())) {
                // 成功返回
                return uploadResponse.getPicture().getPicturePath();
            }

            String message = format("价格披露：上传图片失败了 [ %s ] [ %s ]", uploadResponse.getSubCode(), uploadResponse.getSubMsg());

            $info(message);
            beatPicBean.setComment(message);

            return null;

        } catch (ApiException e) {

            String message = format("价格披露：上传图片出现异常 [ %s ] [ %s ]", e.getErrMsg(), e.getLocalizedMessage());
            $info(message);
            beatPicBean.setComment(message);

            return null;
        }
    }

    private void clearLastImage(ShopBean shopBean, String title, String category_tid) {
        // 价格披露的图片，在上传之前先检查是否已有，有则删除
        // 老图检测和删除时出现的错误和异常不会影响正常逻辑执行。所以此处都忽略
        try {
            PictureGetResponse pictureGetResponse = tbPictureService.getPictures(shopBean, title, Long.valueOf(category_tid));
            List<Picture> pictures = pictureGetResponse.getPictures();
            if (pictures == null || pictures.size() < 1) return;
            Picture picture = pictures.get(0);
            tbPictureService.deletePictures(shopBean, picture.getPictureId());
        } catch (ApiException ignored) {
        }
    }

    /**
     * 获取拼接的商品价格披露图的地址
     */
    private String getImageUrl(BeatPicBean beatPicBean) {

        String price = beatPicBean.getPrice();

        if (StringUtils.isEmpty(price)) {
            // 未设置价格，回退到设置状态
            beatPicBean.setComment("没有设置价格。");
            beatPicBean.setBeat_flg(BeatFlg.Startup);
            return null;
        }

        String imageName = getImageName(beatPicBean);

        if (StringUtils.isEmpty(imageName)) return null;

        String template = beatPicBean.getTemp_url();

        return template.replace("{key}", imageName).replace("{price}", beatPicBean.getPrice());
    }

    @Override
    public String getTaskName() {
        return "ImsBeatPicJob-beat";
    }
}
