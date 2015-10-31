package com.voyageone.batch.ims.service.beat;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.ims.bean.BeatPicBean;
import com.voyageone.batch.ims.dao.ImsBeatPicDao;
import com.voyageone.batch.ims.enums.BeatFlg;
import com.voyageone.batch.ims.enums.ImsPicCategoryType;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 价格披露图片还原处理
 * <p>
 * Created by Jonas on 7/30/15
 * modify by sky on 20150814
 */
@Service
public class ImsBeatRevertService extends ImsBeatBaseService {

    @Autowired
    private ImsBeatPicDao imsBeatPicDao;

    public void revert(List<TaskControlBean> taskControlList, BeatPicBean beatPicBean) {

        // 获取目标店铺
        ShopBean shopBean = ShopConfigs.getShop(beatPicBean.getChannel_id(), beatPicBean.getCart_id());

        // 获取店铺专用主图目录
        String cate_tid = getCategory(shopBean, ImsPicCategoryType.Main);

        $info("价格披露还原：获取图片目录 [ %s ] [ %s ]", beatPicBean.getNum_iid(), cate_tid);

        if (StringUtils.isEmpty(cate_tid)) {
            beatPicBean.setComment("价格披露还原：获取图片目录失败");
            return;
        }

        // 获取主图地址
        // 逻辑同 update(上传) 一样, 内部会通过 flg 判断如何生成图片的标识名称
        // 实际主图的下载上传不同于 update. 已处理过的商品主图会存储在相应的数据库中, 可重用.
        Map<Integer, String> tbImageUrlMap = getOrgImageUrl(shopBean, beatPicBean, cate_tid, taskControlList);

        if (tbImageUrlMap == null) {
            // 这里之前的方法会记录为何获取不到 url 的原因。所以此处失败不输出 log
            beatPicBean.setBeat_flg(getResultFlg(beatPicBean, false));
            return;
        }

        Boolean flg = updateSchema(shopBean, beatPicBean, tbImageUrlMap);

        if (flg == null) return;

        beatPicBean.setBeat_flg(getResultFlg(beatPicBean, flg));

        $info("价格披露还原：完成 [ %s ] [ %s ]", beatPicBean.getNum_iid(), beatPicBean.getCode());
    }

    private Map<Integer, String> getOrgImageUrl(ShopBean shopBean, BeatPicBean beatPicBean, String category_tid, List<TaskControlBean> taskControlList) {
        // 现根据位置获取 CMS 的图片信息
        List<ImsBeatImageInfo> imageInfoList = imsBeatPicDao.getImageInfo(beatPicBean);

        if (imageInfoList == null) return null;

        Map<Integer, String> tbImageUrlMap = new HashMap<>();

        // 为顺延的图片设定 NO IMAGE
        if (beatPicBean.isExtended()) appendExtended(imageInfoList, beatPicBean);

        for (ImsBeatImageInfo imageInfo: imageInfoList) {

            // 补全信息
            imageInfo.setBeatInfo(beatPicBean);
            imageInfo.setCategoryTid(category_tid);
            imageInfo.setShop(shopBean);

            String tbImageUrl = getOrgImageUrl(imageInfo, taskControlList);

            if (StringUtils.isEmpty(tbImageUrl)) return null;
            else if (tbImageUrl.equals(NO_IMAGE_FLG))
                // 转换 FLG 为 null, 后续接口调用内部会继续进一步判断
                tbImageUrlMap.put(imageInfo.getImage_id(), null);
            else
                tbImageUrlMap.put(imageInfo.getImage_id(), tbImageUrl);
        }

        return tbImageUrlMap;
    }

    private void appendExtended(List<ImsBeatImageInfo> imageInfoList, BeatPicBean beatPicBean) {

        String targets = beatPicBean.getTargets();

        ImsBeatImageInfo first = imageInfoList.get(0);

        int size = imageInfoList.size();

        for (String target: targets.split(",")) {

            int index = Integer.valueOf(target);

            // 为顺延产生的多余图片,设定 NO IMAGE
            if (index > size) {
                ImsBeatImageInfo imageInfo = copyInfo(first, index);
                imageInfo.setNoImage(true);
                imageInfoList.add(imageInfo);
            }
        }
    }

    private BeatFlg getResultFlg(BeatPicBean beatPicBean, Boolean result) {
        switch (beatPicBean.getBeat_flg()) {
            case Passed:
                return result ? BeatFlg.Revert : BeatFlg.RevertFail;
            case Cancel:
                return result ? BeatFlg.Canceled : BeatFlg.CancelFail;
        }
        return null;
    }

    @Override
    public String getTaskName() {
        return "ImsBeatPicJob-revert";
    }
}
