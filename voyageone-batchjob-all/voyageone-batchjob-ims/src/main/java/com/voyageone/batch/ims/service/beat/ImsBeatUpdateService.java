package com.voyageone.batch.ims.service.beat;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureGetResponse;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.ims.bean.BeatPicBean;
import com.voyageone.batch.ims.dao.ImsBeatPicDao;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voyageone.batch.ims.enums.BeatFlg.*;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

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
    private ImsBeatPicDao imsBeatPicDao;

    @Autowired
    private TbPictureService tbPictureService;

    @Override
    public String getTaskName() {
        return "ImsBeatPicJob-beat";
    }

    public void beat(List<TaskControlBean> taskControlList, BeatPicBean beatPicBean) {

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
        Map<Integer, String> tbImageUrlMap = getTbImageUrl(shopBean, cate_tid, beatPicBean, taskControlList);

        if (tbImageUrlMap == null) {
            // 直接退出
            // 保存信息，理论上，上小段逻辑 flg 不会改动。等待后续重试
            return;
        }

        Boolean result = updateSchema(shopBean, beatPicBean, tbImageUrlMap);

        if (result == null) {
            // 如果为空表示重试。直接保存信息
            return;
        }

        // 否则保存执行结果
        beatPicBean.setBeat_flg(result ? Passed : Fail);

        $info("价格披露：披露完成 [ %s ] [ %s ]", beatPicBean.getNum_iid(), beatPicBean.getBeat_item_id());
    }

    private Map<Integer, String> getTbImageUrl(ShopBean shopBean, String category_tid, BeatPicBean beatPicBean, List<TaskControlBean> taskControlList) {

        // 现根据位置获取 CMS 的图片信息
        List<ImsBeatImageInfo> imageInfoList = imsBeatPicDao.getImageInfo(beatPicBean);

        List<Integer> indexList = Arrays.stream(beatPicBean.getTargets().split(",")).map(Integer::valueOf).collect(toList());

        Map<Integer, String> tbImageUrlMap = new HashMap<>();

        for (ImsBeatImageInfo imageInfo: imageInfoList) {
            // 补全信息
            imageInfo.setBeatInfo(beatPicBean);
            imageInfo.setCategoryTid(category_tid);
            imageInfo.setShop(shopBean);

            String tbImageUrl;

            if (beatPicBean.isExtended()) {

                if (imageInfo.getImage_id() == 1) {
                    tbImageUrl = getTbImageUrl(imageInfo);
                } else {
                    tbImageUrl = getOrgImageUrl(imageInfo, taskControlList);
                }

            } else {
                tbImageUrl = getTbImageUrl(imageInfo);
            }

            if (StringUtils.isEmpty(tbImageUrl)) return null;

            // 如果是 Repeat 的话, 把第一个放置到各个位置上即可
            // 所以最后直接 return 结束
            if (beatPicBean.isRepeat()) {
                for (Integer i: indexList) {
                    tbImageUrlMap.put(i, tbImageUrl);
                }
                return tbImageUrlMap;
            }

            imageInfo.setImageUrl(tbImageUrl);
        }

        if (beatPicBean.isExtended()) {
            resetForExtended(imageInfoList, beatPicBean);
        }

        for (int i = 0; i < imageInfoList.size() && i < 5; i++) {
            tbImageUrlMap.put(i + 1, imageInfoList.get(i).getImageUrl());
        }

        return tbImageUrlMap;
    }

    private void resetForExtended(List<ImsBeatImageInfo> imageInfoList, BeatPicBean beatPicBean) {

        // 在这些位置进行顺延插入
        String targets = beatPicBean.getTargets();

        ImsBeatImageInfo first = imageInfoList.get(0);

        int size = imageInfoList.size();

        for (String target: targets.split(",")) {

            int index = Integer.valueOf(target);

            if (index == 1) continue;

            if (index > size) {
                // 如果目标位置超过原有图片的数量, 说明这个位置本来没图, 所以这里直接追加就可以
                imageInfoList.add(copyInfo(first, index));
            } else {
                // 相反如果不超过, 则将需要重复的图片插入到指定的位置即可
                imageInfoList.add(index - 1, copyInfo(first, index));
            }
        }
    }

    /**
     * 根据完整的图片信息,处理获取该图片在淘宝上的路径
     */
    private String getTbImageUrl(ImsBeatImageInfo imageInfo) {

        BeatPicBean beatPicBean = imageInfo.getBeatInfo();

        // 清除该图片在淘宝上的文件
        clearLastImage(imageInfo);

        String url = formatImageUrl(imageInfo);
        if (StringUtils.isEmpty(url)) return null;

        $info("价格披露：图片下载地址: " + url);

        byte[] image;

        $info("价格披露：准备下载披露图片");

        try (InputStream inputStream = HttpUtils.getInputStream(url, null)) {

            image = IOUtils.toByteArray(inputStream);

        } catch (IOException e) {
            beatPicBean.setComment(format("价格披露：线程内下载图片出现异常 [ %s ]", e.getMessage()));
            beatPicBean.setBeat_flg(Fail);
            $info("价格披露：线程内下载图片出现异常。[ %s ] [ %s ]", beatPicBean.getBeat_item_id(), e.getMessage());
            return null;
        }

        $info("价格披露：已下载 [ %s ]", image.length);

        try {
            PictureUploadResponse uploadResponse = tbPictureService.uploadPicture(imageInfo.getShop(), image,
                    imageInfo.getBeatTitle(), imageInfo.getCategoryTid());

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

    private void clearLastImage(ImsBeatImageInfo imageInfo) {
        // 价格披露的图片，在上传之前先检查是否已有，有则删除
        // 老图检测和删除时出现的错误和异常不会影响正常逻辑执行。所以此处都忽略
        try {

            ShopBean shopBean = imageInfo.getShop();

            PictureGetResponse pictureGetResponse = tbPictureService.getPictures(shopBean,
                    imageInfo.getBeatTitle(), Long.valueOf(imageInfo.getCategoryTid()));

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
    private String formatImageUrl(ImsBeatImageInfo imageInfo) {

        BeatPicBean beatPicBean = imageInfo.getBeatInfo();

        String price = beatPicBean.getPrice();

        if (StringUtils.isEmpty(price)) {
            // 未设置价格，回退到设置状态
            beatPicBean.setComment("没有设置价格。");
            beatPicBean.setBeat_flg(Startup);
            return null;
        }

        // 获取到"商品图片"的标识名称
        // 该名称由 CMS 提供, 且固定在 CMS 和 Adobe 的服务器上.
        String imageName = imageInfo.getImage_name();

        if (StringUtils.isEmpty(imageName)) {
            $info("没有获取到 ImageName [ %s, %s, %s ]", imageInfo.getCode(), imageInfo.getUrl_key(), imageInfo.getImage_id());
            beatPicBean.setComment(format("没有获取到 ImageName [ %s ]", imageInfo.getImage_id()));
            beatPicBean.setBeat_flg(Fail);
            return null;
        }

        String template = beatPicBean.getTemp_url();

        return template.replace("{key}", imageName).replace("{price}", beatPicBean.getPrice());
    }
}
