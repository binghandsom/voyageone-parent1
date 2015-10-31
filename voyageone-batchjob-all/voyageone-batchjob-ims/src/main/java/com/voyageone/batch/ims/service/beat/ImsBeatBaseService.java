package com.voyageone.batch.ims.service.beat;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.domain.PictureCategory;
import com.taobao.api.response.*;
import com.taobao.top.schema.exception.TopSchemaException;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.ims.bean.BeatPicBean;
import com.voyageone.batch.ims.dao.ImsPicCategoryDao;
import com.voyageone.batch.ims.dao.ImsPicDao;
import com.voyageone.batch.ims.enums.ImsPicCategoryType;
import com.voyageone.batch.ims.modelbean.ImsPic;
import com.voyageone.batch.ims.modelbean.ImsPicCategory;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbItemSchema;
import com.voyageone.common.components.tmall.TbItemService;
import com.voyageone.common.components.tmall.TbPictureService;
import com.voyageone.common.components.tmall.bean.TbGetPicCategoryParam;
import com.voyageone.common.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
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
    private TbItemService tbItemService;

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

            $info("价格披露(B)：新增目录获取的 id 已插入数据库。反应行数[ %s ]", count);

            return id;
        }
    }

    private String getCategoryTid(ShopBean shopBean, ImsPicCategory category) {

        TbGetPicCategoryParam param = new TbGetPicCategoryParam();
        param.setPictureCategoryName(category.getCategory_name());

        try {
            PictureCategoryGetResponse res = tbPictureService.getCategories(shopBean, param);

            List<PictureCategory> categories = res.getPictureCategories();

            if (res.isSuccess() && categories != null && categories.size() > 0)
                return String.valueOf(categories.get(0).getPictureCategoryId());

            $info("价格披露(B)：调用接口尝试获取目录失败 [ %s ] [ %s ] [ %s ]", res.getSubCode(),
                    res.getSubMsg(), category.getCategory_name());
            logIssue("图片还原时，调用接口获取目录失败", format("[ %s ] [ %s ]", res.getSubCode(), res.getSubMsg()));

            return null;
        } catch (ApiException e) {
            $info("价格披露(B)：调用接口尝试获取目录出现错误 [ %s ] [ %s ] [ %s ] [ %s ] [ %s ]",
                    e.getLocalizedMessage(), e.getErrMsg(), shopBean.getOrder_channel_id(), shopBean.getCart_id(),
                    category.getCategory_name());
            return null;
        }
    }

    private String createCategory(ShopBean shopBean, ImsPicCategory category) {
        try {
            PictureCategoryAddResponse res = tbPictureService.addCategory(shopBean, category.getCategory_name());

            if (!res.isSuccess()) {
                $info("价格披露(B)：调用淘宝接口创建图片目录失败 [ %s ] [ %s ] [ %s ] [ %s ]",
                        res.getSubMsg(),
                        shopBean.getOrder_channel_id(), shopBean.getCart_id(), category.getCategory_name());
                logIssue(format("调用淘宝接口创建图片目录失败 [ %s ]", res.getSubMsg()), category);

                return null;
            }

            Long id = res.getPictureCategory().getPictureCategoryId();

            $info("价格披露(B)：调用图片目录新增接口成功 [ %s ]", id);

            return String.valueOf(id);

        } catch (ApiException e) {
            $info("价格披露(B)：调用淘宝接口创建图片目录失败 [ %s ] [ %s ] [ %s ]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), category.getCategory_name());
            logIssue(e, category);
            return null;
        }
    }

    protected Boolean updateSchema(ShopBean shopBean, BeatPicBean beatPicBean, Map<Integer, String> tbImageUrlMap) {
        String errorMsg;
        try {
            // 获取 taobao 的商品信息
            TbItemSchema itemSchema = tbItemService.getUpdateSchema(shopBean, beatPicBean.getNum_iid());

            $info("预计更新");
            for (Map.Entry<Integer, String> entry: tbImageUrlMap.entrySet()) {
                $info("\t%s\t%s", entry.getKey(), entry.getValue());
            }

            // 更改其主图地址
            itemSchema.setMainImage(tbImageUrlMap);

            // 将商品信息更新回 taobao
            TmallItemSchemaUpdateResponse res = tbItemService.updateFields(shopBean, itemSchema);

            if (res == null) {
                beatPicBean.setComment("更新商品信息时, 为获取到响应.");
                return false;
            }

            $info("价格披露：接口结果 [ %s ] [ %s ] [ %s ] [ %s ] [ %s ]", res.getUpdateItemResult(), res.getGmtModified(),
                    res.getMsg(), res.getSubCode(), res.getSubMsg());

            if (StringUtils.isEmpty(res.getSubCode())) return true;

            $info("价格披露：商品更新失败了。[ %s ] [ %s ] [ %s ]", beatPicBean.getNum_iid(), res.getSubCode(), res.getSubMsg());

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
            $info(errorMsg);
            switch (e1.getSubCode()) {
                // 商品已删除则返回失败。终止重试
                case "isv.invalid-permission:item-deleted":
                    return false;
            }
            return null;
        } catch (ApiException e2) {
            errorMsg = format("价格披露(B)：商品更新处理出现 api 异常。[ %s ] [ %s ]", e2.getLocalizedMessage(), e2.getErrMsg());
            beatPicBean.setComment(errorMsg);
            $info(errorMsg);
            return null;
        } catch (TopSchemaException e3) {
            errorMsg = format("价格披露(B)：处理 Schema 时出现异常。[ %s ]", e3.getLocalizedMessage());
            beatPicBean.setComment(errorMsg);
            $info(errorMsg);
            return false;
        }
    }

    protected ImsBeatImageInfo copyInfo(ImsBeatImageInfo imageInfo, int index) {

        ImsBeatImageInfo copy = new ImsBeatImageInfo();

        copy.setImage_name(imageInfo.getImage_name());
        copy.setUrl_key(imageInfo.getUrl_key());
        copy.setImage_id(index);
        copy.setCode(imageInfo.getCode());
        copy.setChannel_id(imageInfo.getChannel_id());
        copy.setImageUrl(imageInfo.getImageUrl());

        return copy;
    }


    protected final static String NO_IMAGE_FLG = "NO IMAGE";

    @Autowired
    private TbPictureService tbPictureService;

    @Autowired
    private ImsPicDao imsPicDao;

    protected String getOrgImageUrl(ImsBeatImageInfo imageInfo, List<TaskControlBean> taskControlList) {

        // 通过特殊标记处理无图位置
        if (imageInfo.isNoImage()) return NO_IMAGE_FLG;

        String title = imageInfo.getOrgTitle();

        String cate_id = imageInfo.getCategoryTid();

        BeatPicBean beatPicBean = imageInfo.getBeatInfo();

        ShopBean shopBean = imageInfo.getShop();

        ImsPic pic = imsPicDao.selectByTitle(title, cate_id);

        if (pic != null)
            return pic.getPic_url();

        Picture picture;

        try {
            PictureGetResponse res = tbPictureService.getPictures(shopBean, title, Long.valueOf(cate_id));

            List<Picture> pictures = res.getPictures();

            if (res.isSuccess() && pictures != null && pictures.size() > 0) {

                picture = pictures.get(0);

            } else {

                $info("价格披露还原：没有找到图片，准备从新上传 [ %s ] [ %s ] [ %s ] [ %s ]",
                        res.getSubMsg(), title, cate_id, shopBean.getShop_name());

                picture = uploadOrg(imageInfo, taskControlList);
            }

        } catch (ApiException e) {

            String message = format("价格披露还原：调用淘宝图片获取接口异常 [ %s ] [ %s ] [ %s ] [ %s ]",
                    e.getLocalizedMessage(), title, cate_id, shopBean.getShop_name());
            $info(message);
            beatPicBean.setComment(message);

            return null;
        }

        if (picture == null)
            return null;

        pic = new ImsPic();
        pic.setTitle(title);
        pic.setCategory_tid(cate_id);
        pic.setModifier(getTaskName());
        pic.setCreater(getTaskName());
        pic.setPic_tid(String.valueOf(picture.getPictureId()));
        pic.setPic_url(picture.getPicturePath());

        int count = imsPicDao.insert(pic);

        $info("价格披露还原：插入 pic 数据。反应行数 [ %s ]", count);

        return pic.getPic_url();
    }

    private Picture uploadOrg(ImsBeatImageInfo imageInfo, List<TaskControlBean> taskControlList) {

        String cate_id = imageInfo.getCategoryTid();

        BeatPicBean beatPicBean = imageInfo.getBeatInfo();

        ShopBean shopBean = imageInfo.getShop();

        String errorMsg;
        // 获取模板地址
        // 此处先不把 “template_url” 加入到 Name 枚举中。等待后续做主图任务时，顺带修改这里获取模板的方式
        // 所以暂时使用字符串
        TaskControlBean templateUrl = taskControlList.stream()
                .filter(c -> c.getCfg_name().equals("template_url") && c.getCfg_val1().equals(shopBean.getCart_id() + shopBean.getOrder_channel_id()))
                .findFirst()
                .orElse(null);

        // 模版配置为空
        if (templateUrl == null || StringUtils.isEmpty(templateUrl.getCfg_val2())) {
            errorMsg = format("价格披露还原：没有找到主图模板地址，当前 Cart 为：[ %s ] [ %s ]", shopBean.getCart_id(),
                    shopBean.getShop_name());
            $info(errorMsg);
            beatPicBean.setComment(errorMsg);
            return null;
        }

        // 检查模版配置是否正确
        if (!templateUrl.getCfg_val2().contains("{key}")) {
            errorMsg = format("价格披露还原：主图模板地址没有找到关键字“{key}” [ %s ]", templateUrl.getCfg_val2());
            $info(errorMsg);
            beatPicBean.setComment(errorMsg);
            return null;
        }

        String imageName = imageInfo.getImage_name();

        if (StringUtils.isEmpty(imageName)) return null;

        byte[] image;
        String image_url = templateUrl.getCfg_val2().replace("{key}", imageName);

        $info("价格披露还原：尝试下载图片：[ %s ]", image_url);

        // 尝试下载
        try (InputStream inputStream = HttpUtils.getInputStream(image_url, null)) {

            image = IOUtils.toByteArray(inputStream);

            $info("价格披露还原：已下载，长度：[ %s ]", image.length);

        } catch (IOException e) {
            errorMsg = format("价格披露还原：线程内下载图片出现异常。异常信息：%s", e.getMessage());
            beatPicBean.setComment(errorMsg);
            $info(errorMsg);
            return null;
        }

        // 尝试上传
        try {
            PictureUploadResponse res = tbPictureService.uploadPicture(shopBean, image, imageInfo.getOrgTitle(),
                    cate_id);

            if (res.getPicture() != null) return res.getPicture();
            errorMsg = "线程内上传图片失败：" + format("[ %s ] [ %s ]", res.getSubCode(), res.getSubMsg());
            beatPicBean.setComment(errorMsg);
            $info("价格披露还原：线程内上传图片失败 [ %s ] [ %s ]", res.getSubCode(), res.getSubMsg());
            return null;

        } catch (ApiException e) {
            errorMsg = format("价格披露还原：线程内上传图片出现异常。异常信息：%s", e.getMessage());
            beatPicBean.setComment(errorMsg);
            $info(errorMsg);
            return null;
        }
    }
}
