package com.voyageone.batch.ims.service.beat;

import com.taobao.api.ApiException;
import com.taobao.api.domain.PictureCategory;
import com.taobao.api.response.PictureCategoryAddResponse;
import com.taobao.api.response.PictureCategoryGetResponse;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.ims.bean.BeatPicBean;
import com.voyageone.batch.ims.dao.ImsBeatPicDao;
import com.voyageone.batch.ims.dao.ImsPicCategoryDao;
import com.voyageone.batch.ims.enums.ImsPicCategoryType;
import com.voyageone.batch.ims.modelbean.ImsPicCategory;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbItemSchema;
import com.voyageone.common.components.tmall.TbItemService;
import com.voyageone.common.components.tmall.TbPictureService;
import com.voyageone.common.components.tmall.bean.TbGetPicCategoryParam;
import com.voyageone.common.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * 价格披露的通用部分
 * <p>
 * Created by Jonas on 8/20/15.
 */
public abstract class ImsBeatBaseService extends BaseTaskService {

    @Autowired
    private ImsBeatPicDao imsBeatPicDao;

    @Autowired
    private TbItemService tbItemService;

    @Autowired
    private TbPictureService tbPictureService;

    @Autowired
    private ImsPicCategoryDao imsPicCategoryDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.IMS;
    }

    @Override
    public abstract String getTaskName();

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
    }

    protected String getCategory(ShopBean shopBean, ImsPicCategoryType type) {

        if (shopBean == null)
            return null;

        // 检查是否有已经缓存
        // - 如果缓存了，则查询
        // - 如果目录 taobao 上实际不存在，则创建一个，并更新会数据库
        // - 如果表内查询不到，则返回错误内容

        ImsPicCategory category = imsPicCategoryDao.select(shopBean, type);

        if (category == null)
            return null;

        // 没有先尝试获取
        // 再尝试创建
        if (!StringUtils.isEmpty(category.getCategory_tid()))
            return category.getCategory_tid();

        if (StringUtils.isEmpty(category.getCategory_name()))
            return null;
        else {
            String id = getCategoryTid(shopBean, category);

            // 获取失败则尝试创建
            if (id == null)
                id = createCategory(shopBean, category);

            if (StringUtils.isEmpty(id)) return null;

            category.setCategory_tid(id);
            category.setModifier(getTaskName());
            int count = imsPicCategoryDao.updateTid(category);

            logger.info(format("价格披露(B)：新增目录获取的 id 已插入数据库。反应行数[ %s ]", count));

            return id;
        }
    }

    protected List<ImsBeatImageInfo> getTbImageUrl(BeatPicBean beatPicBean) {
        // 现根据位置获取 CMS 的图片信息
        List<ImsBeatImageInfo> imageInfoList = imsBeatPicDao.getImageInfo(beatPicBean);

        String[] strings = beatPicBean.getTargets().split(",");

        // 已重写了 ImsBeatImageInfo 的比较方法,所以可以用 contains
        Arrays.stream(strings).map(Integer::valueOf)
                .filter(i -> !imageInfoList.contains(i))
                .forEach(i -> {
                    ImsBeatImageInfo imageInfo = new ImsBeatImageInfo();

                    imageInfo.setChannel_id(beatPicBean.getChannel_id());
                    imageInfo.setBeatInfo(beatPicBean);
                    imageInfo.setCode(beatPicBean.getCode());
                    imageInfo.setImage_id(i);
                    imageInfo.setUrl_key(beatPicBean.getUrl_key());

                    imageInfoList.add(imageInfo);
                });

        return imageInfoList;
    }

    private String getCategoryTid(ShopBean shopBean, ImsPicCategory category) {

        TbGetPicCategoryParam param = new TbGetPicCategoryParam();
        param.setPictureCategoryName(category.getCategory_name());

        try {
            PictureCategoryGetResponse res = tbPictureService.getCategories(shopBean, param);

            List<PictureCategory> categories = res.getPictureCategories();

            if (res.isSuccess() && categories != null && categories.size() > 0)
                return String.valueOf(categories.get(0).getPictureCategoryId());

            logger.info(format("价格披露(B)：调用接口尝试获取目录失败 [ %s ] [ %s ] [ %s ]", res.getSubCode(),
                    res.getSubMsg(), category.getCategory_name()));
            logIssue("图片还原时，调用接口获取目录失败", format("[ %s ] [ %s ]", res.getSubCode(), res.getSubMsg()));

            return null;
        } catch (ApiException e) {
            logger.info(format("价格披露(B)：调用接口尝试获取目录出现错误 [ %s ] [ %s ] [ %s ] [ %s ] [ %s ]",
                    e.getLocalizedMessage(), e.getErrMsg(), shopBean.getOrder_channel_id(), shopBean.getCart_id(),
                    category.getCategory_name()));
            return null;
        }
    }

    private String createCategory(ShopBean shopBean, ImsPicCategory category) {
        try {
            PictureCategoryAddResponse res = tbPictureService.addCategory(shopBean, category.getCategory_name());

            if (!res.isSuccess()) {
                logger.info(format("价格披露(B)：调用淘宝接口创建图片目录失败 [ %s ] [ %s ] [ %s ] [ %s ]",
                        res.getSubMsg(),
                        shopBean.getOrder_channel_id(), shopBean.getCart_id(), category.getCategory_name()));
                logIssue(format("调用淘宝接口创建图片目录失败 [ %s ]", res.getSubMsg()), category);

                return null;
            }

            Long id = res.getPictureCategory().getPictureCategoryId();

            logger.info(format("价格披露(B)：调用图片目录新增接口成功 [ %s ]", id));

            return String.valueOf(id);

        } catch (ApiException e) {
            logger.info(format("价格披露(B)：调用淘宝接口创建图片目录失败 [ %s ] [ %s ] [ %s ]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), category.getCategory_name()));
            logIssue(e, category);
            return null;
        }
    }

    protected Boolean updateSchema(ShopBean shopBean, BeatPicBean beatPicBean, Map<Integer, String> tbImageUrlMap) {
        String errorMsg;
        try {
            // 获取 taobao 的商品信息
            TbItemSchema itemSchema = tbItemService.getUpdateSchema(shopBean, beatPicBean.getNum_iid());

            // 更改其主图地址
            itemSchema.setMainImage(tbImageUrlMap);

            // 将商品信息更新回 taobao
            TmallItemSchemaUpdateResponse res = tbItemService.updateFields(shopBean, itemSchema);

            if (StringUtils.isEmpty(res.getSubCode())) return true;

            $info("价格披露(B)：商品更新失败了。[ %s ] [ %s ] [ %s ]", beatPicBean.getNum_iid(), res.getSubCode(), res.getSubMsg());

            // 指定忽略这些错误，让后续任务可以重新尝试
            switch (res.getSubCode()) {
                case "isp.service-unavailable": // 服务不可用
                    return null;
            }

            beatPicBean.setComment(format("修改淘宝商品时失败了。[ %s ] [ %s ]", res.getSubCode(), res.getSubMsg()));
            return false;

        } catch (GetUpdateSchemaFailException e1) {
            errorMsg = format("价格披露(B)：调用 getUpdateSchema 失败了。[ %s ] [ %s ]", e1.getSubCode(), e1.getSubMsg());
            beatPicBean.setComment(errorMsg);
            logger.info(errorMsg);
            switch (e1.getSubCode()) {
                // 商品已删除则返回失败。终止重试
                case "isv.invalid-permission:item-deleted":
                    return false;
            }
            return null;
        } catch (ApiException e2) {
            errorMsg = format("价格披露(B)：商品更新处理出现 api 异常。[ %s ] [ %s ]", e2.getLocalizedMessage(), e2.getErrMsg());
            beatPicBean.setComment(errorMsg);
            logger.info(errorMsg);
            return null;
        } catch (TopSchemaException e3) {
            errorMsg = format("价格披露(B)：处理 Schema 时出现异常。[ %s ]", e3.getLocalizedMessage());
            beatPicBean.setComment(errorMsg);
            logger.info(errorMsg);
            return false;
        }
    }
}
