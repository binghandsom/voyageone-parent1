package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.cms.CmsConstants;
import com.voyageone.batch.cms.bean.*;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voyageone.batch.cms.service.feed.BcbgWsdlConstants.*;
import static java.util.stream.Collectors.*;

/**
 * (临时) 为更新接口提供专门服务
 * Created by Jonas on 10/20/15.
 */
@Service
public class BcbgWsdlUpdate extends BcbgWsdlBase {

    private static final String UPDATE_FLG = "status=30";

    @Override
    protected String getWhereUpdateFlg() {
        return UPDATE_FLG;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "BcbgAnalysis.Update";
    }

    protected boolean postUpdatedProduct() throws Exception {

        $info("准备处理更新商品");

        WsdlProductService service = new WsdlProductService(channel);

        // 先获取要更新的商品信息(由 getWhereUpdateFlg 决定)
        List<ProductBean> updatingProduct = getProducts();

        if (updatingProduct.size() < 1) {
            $info("没有商品需要更新");
            return false;
        }

        // 从 _full 后缀的表里取上次的数据
        List<ProductBean> newProducts = getNewProducts();
        Map<String, List<ImageBean>> codeImages = getNewImages();

        // 在这次的商品数据和上次的商品数据之间建立关联关系
        Map<Boolean, List<ProductsFeedUpdate>> feedMap = updatingProduct
                .stream()
                .map(p -> new Relation(p, newProducts, codeImages)) // 这里注意, indexOf 重写了 ProductBean 的 equals 方法
                .map(this::getWsdlParam)
                .collect(groupingBy(p -> p.getUpdatefields() != null, toList()));

        List<ProductsFeedUpdate> feedUpdates = feedMap.get(true);

        if (feedUpdates == null || feedUpdates.size() < 1) {
            int[] counts = bcbgSuperFeedDao.updateUpdatingSuccess();
            $info("没有商品需要更新. 商品更新信息 Feed [ %s ] [ %s ]", counts[0], counts[1]);
            return false;
        }

        List<ProductsFeedUpdate> noNeedUpdating = feedMap.get(false);

        if (noNeedUpdating != null && noNeedUpdating.size() > 0) {
            int[] counts = bcbgSuperFeedDao.updateFull(noNeedUpdating.stream().map(ProductsFeedUpdate::getCode).collect(toList()));
            $info("部分商品不需要更新, 执行信息 Feed [ %s ] [ %s ]", counts[0], counts[1]);
        }

        $info("已取得商品更新参数 [ %s ]", feedUpdates.size());

        List<String> updatedCodes = new ArrayList<>();

        for (ProductsFeedUpdate feedUpdate : feedUpdates) {

            WsdlProductUpdateResponse response = service.update(feedUpdate);

            ProductUpdateResponseBean productUpdateResponseBean = response.getResultInfo();

            if (response.getResult().equals("NG") || productUpdateResponseBean.getFailure().size() > 0) {

                // web services 返回系统失败
                if (response.getResult().equals("NG")) {
                    $info("更新产品处理失败，MessageCode = %s ,Message = %s", response.getMessageCode(), response.getMessage());
                    logIssue("cms 数据导入处理", "更新产品处理异常 code=" + feedUpdate.getBarcode());
                }
                // web services 返回数据失败
                else {

                    // 出错统计
                    List<ProductUpdateDetailBean> productUpdateDetailBeans = productUpdateResponseBean.getFailure();

                    String failureMessage = productUpdateDetailBeans.stream().map(ProductUpdateDetailBean::getResultMessage)
                            .collect(joining(";"));

                    $info("更新产品处理失败，" + failureMessage);
                    logIssue("cms 数据导入处理", "更新产品处理失败，" + failureMessage);
                }
            } else {
                // 记录成功的商品 Code
                updatedCodes.add(feedUpdate.getCode());
            }
        }

        // 不管更新是否成功, 最新的数据是要保存到库中
        // 如果更新不成功, 只是不更新标识位, 等待下次程序认为是强制更新
        int count0 = bcbgSuperFeedDao.deleteUpdating();
        int count1 = bcbgSuperFeedDao.selectInsertUpdated();
        int count2 = bcbgSuperFeedDao.updateFlgToUpdated(updatedCodes);
        $info("更新结束, 原数据删除 [ %s ] 补充数据 [ %s ] 标记成功数据 [ %s ]", count0, count1, count2);

        return count2 > 0;
    }

    private List<ProductBean> getNewProducts() {

        $info("准备批量获取上次记录的 Product ( Full )");

        ProductBean productColumns = getProductColumns();

        // 无条件,获取所有
        // 依据前序逻辑,这里每次任务只会执行一次.所以无需担心
        String where = Constants.EmptyString;

        List<ProductBean> productBeans = superFeedDao.selectSuperfeedProduct(
                String.format("%s GROUP BY %s", where, grouping_product),
                productColumns,
                String.format("%s JOIN %s ON %s", table_feed, table_style_full, on_product));

        $info("取得上次记录的 Product ( Full ) [ %s ] 个", productBeans.size());

        for (ProductBean productBean : productBeans) {
            // 这里处理 UrlKey 字段, 否则后续无法和正常的商品信息比对
            productBean.setUrl_key(clearSpecialSymbol(productBean.getUrl_key()));

            calePrice(productBean);
        }

        return productBeans;
    }

    private Map<String, List<ImageBean>> getNewImages() {

        $info("准备批量获取上次记录的 Image ( Full )");

        // 拼装 full 表获取的专用条件
        String where = String.format("WHERE %s", getWhereUpdateFlg());

        // 逻辑设定上, getLastImags 将和 getLastProducts 一样, 一次 Job 只查询一次. 数据的处理移交 Java 端执行
        // 所以吧必须的两列内容按固定格式取出
        String code = Feed.getVal1(channel, FeedEnums.Name.item_code);
        String images = Feed.getVal1(channel, FeedEnums.Name.images);

        // 通过组装的 SQL 查询这次需要更新的商品,其上次的图片信息
        List<String> imageArrs = superFeedDao.selectAllfeedImage(
                where,
                String.format("DISTINCT CONCAT(%s,'<>',%s)", code, images), // 这里定义固定格式
                String.format("%s JOIN %s ON %s", table_feed, table_style_full, on_product)); // 表部分

        return imageArrs
                .stream()
                .map(str -> str.split("<>"))
                .collect(
                        toMap(arr -> arr[0], arr -> getImageBeans(arr[1])));
    }

    private List<ImageBean> getImageBeans(String imagePathsStr) {

        List<ImageBean> imageBeans = new ArrayList<>();

        String separator = Feed.getVal1(channel, FeedEnums.Name.image_split);

        String[] imagePaths = imagePathsStr.split(separator);

        for (String imagePath : imagePaths) {

            ImageBean imageBean = new ImageBean();

            imageBean.setImage_type("1");
            imageBean.setImage_url(imagePath);
            imageBean.setImage_name(imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.lastIndexOf(".")));
            imageBean.setDisplay_order("0");

            imageBeans.add(imageBean);
        }

        return imageBeans;
    }

    /**
     * 通过关联关系,组装更新参数
     */
    private ProductsFeedUpdate getWsdlParam(Relation relation) {

        ProductsFeedUpdate feedUpdate = new ProductsFeedUpdate();

        feedUpdate.setChannel_id(channel.getId());
        feedUpdate.setCode(relation.updating.getP_code());
        feedUpdate.setProduct_url_key(relation.updating.getUrl_key());

        Map<String, String> updateFields = relation.getUpdateFields();

        if (updateFields == null || updateFields.size() < 1)
            return feedUpdate;

        feedUpdate.setUpdatefields(updateFields);

        return feedUpdate;
    }

    /**
     * 自用的关系辅助类
     */
    private class Relation {
        /**
         * 当前准备更新的商品信息
         */
        private ProductBean updating;

        /**
         * 上次记录的商品信息
         */
        private ProductBean newProduct;

        public Relation(ProductBean updating, List<ProductBean> newProductList, Map<String, List<ImageBean>> codeImages) {

            this.updating = updating;

            if (newProductList.size() < 1) return;

            // 这里因为 ProductBean 的 equals 重写, 可以通过 indexOf 查询到上次的数据
            int index = newProductList.indexOf(updating);
            if (index < 0) return;
            this.newProduct = newProductList.get(index);
            // 使用 Code 关联查询上次的图片信息
            this.newProduct.setImages(codeImages.get(this.newProduct.getP_code()));
        }

        /**
         * 获取需要更新的字段
         */
        public Map<String, String> getUpdateFields() {
            // 如果 full 里为 30, 临时表无数据. 则使用当前的数据进行更新.
            // 这种情况视为人为的手动处理.
            return newProduct == null ? getCurrFields() : getNewestFields();
        }

        private Map<String, String> getCurrFields() {
            Map<String, String> updateFields = new HashMap<>();
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_MSRP, updating.getP_msrp());
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_PRICE, updating.getPs_price());
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE, updating.getCps_cn_price());
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_RMB, updating.getCps_cn_price_rmb());
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_LONG_DESCRIPTION, updating.getPe_long_description());

            String strSyncFlg = Feed.getVal1(channel, FeedEnums.Name.sync_final_rmb);
            if (Boolean.valueOf(strSyncFlg)) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_FINAL_RMB, updating.getCps_cn_price_final_rmb());
            }

            String separator = CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_SPLIT;
            List<ImageBean> imageBeanList = updating.getImages();
            if (imageBeanList != null) {
                String images = imageBeanList.stream().map(ImageBean::getImage_url).filter(i -> i != null).collect(joining(separator));
                if (!StringUtils.isEmpty(images))
                    updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_URL, images);
            }

            return updateFields;
        }

        private Map<String, String> getNewestFields() {

            Map<String, String> updateFields = new HashMap<>();

            if (!updating.getP_msrp().equals(newProduct.getP_msrp())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_MSRP, newProduct.getP_msrp());
            }

            if (!updating.getPs_price().equals(newProduct.getPs_price())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_PRICE, newProduct.getPs_price());
            }

            if (!updating.getCps_cn_price().equals(newProduct.getCps_cn_price())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE, newProduct.getCps_cn_price());
            }

            if (!updating.getCps_cn_price_rmb().equals(newProduct.getCps_cn_price_rmb())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_RMB, newProduct.getCps_cn_price_rmb());
            }

            if (!updating.getPe_long_description().equals(newProduct.getPe_long_description())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_LONG_DESCRIPTION, newProduct.getPe_long_description());
            }

            String strSyncFlg = Feed.getVal1(channel, FeedEnums.Name.sync_final_rmb);
            if (Boolean.valueOf(strSyncFlg) && !updating.getCps_cn_price_final_rmb().equals(newProduct.getCps_cn_price_final_rmb())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_FINAL_RMB, newProduct.getCps_cn_price_final_rmb());
            }

            String separator = CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_SPLIT;

            String images = "";

            List<ImageBean> imageBeanList = updating.getImages();

            if (imageBeanList != null)
                images = imageBeanList.stream().map(ImageBean::getImage_url).filter(i -> i != null).collect(joining(separator));

            String newImages = null;

            List<ImageBean> lastImageList = newProduct.getImages();

            if (lastImageList != null)
                newImages = lastImageList.stream().map(ImageBean::getImage_url).filter(i -> i != null).collect(joining(separator));

            if (!images.equals(newImages)) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_URL, newImages);
            }

            return updateFields;
        }
    }
}
