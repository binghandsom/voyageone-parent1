package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.cms.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.task2.cms.bean.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * (临时) 为更新接口提供专门服务
 * Created by Jonas on 10/20/15.
 */
@Service
public class SearsWsdlUpdate extends SearsWsdlBase {

    private static final String UPDATE_FLG = "update_flg = 2";

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

    class Context extends SearsWsdlBase.ContextBase {

        protected Context(ChannelConfigEnums.Channel channel) {
            super(channel);
        }

        protected void postUpdatedProduct() throws Exception {

            $info("准备处理更新商品");

            WsdlProductService service = new WsdlProductService(channel);

            // 先获取要更新的商品信息(由 getWhereUpdateFlg 决定)
            List<ProductBean> updatingProduct = getProducts();

            if (updatingProduct.size() < 1) {
                $info("没有商品需要更新");
                return;
            }

            // 从 _full 后缀的表里取上次的数据
            List<ProductBean> lastProduct = getLastProducts();
            Map<String, List<ImageBean>> codeImages = getLastImages();

            // 在这次的商品数据和上次的商品数据之间建立关联关系
            List<ProductsFeedUpdate> feedUpdates = updatingProduct
                    .stream()
                    .map(p -> new Relation(p, lastProduct, codeImages)) // 这里注意, indexOf 重写了 ProductBean 的 equals 方法
                    .map(this::getWsdlParam)
                    .filter(p -> p != null)
                    .collect(toList());

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
                        StringBuilder failureMessage = new StringBuilder();

                        // 出错统计
                        List<ProductUpdateDetailBean> productUpdateDetailBeans = productUpdateResponseBean.getFailure();

                        for (int b = 0; b < productUpdateDetailBeans.size(); b++) {
                            failureMessage.append("Message(").append(b).append(")=").append(productUpdateDetailBeans.get(b).getResultMessage()).append(";");
                        }

                        $info("更新产品处理失败，" + failureMessage);

                        logIssue("cms 数据导入处理", "更新产品处理失败，" + failureMessage);
                    }
                } else {
                    // 记录成功的商品 Code
                    updatedCodes.add(feedUpdate.getCode());
                }
            }

            // 返回删除数量和插入数量,理论上应该相同
            updateFullByCode(updatedCodes);

            $info("已完成商品更新, 更新的商品数量 Feed [ %s ]", updatedCodes.size());
        }

        private List<ProductBean> getLastProducts() {

            $info("准备批量获取上次记录的 Product ( Full )");

            ProductBean productColumns = getProductColumns();

            // 无条件,获取所有
            // 依据前序逻辑,这里每次任务只会执行一次.所以无需担心
            String where = Constants.EmptyString;

            List<ProductBean> productBeans = superFeedDao.selectSuperfeedProduct(
                    String.format("%s %s", where, Feeds.getVal1(channel, FeedEnums.Name.product_sql_ending)),
                    productColumns,
                    String.format("%s_full %s", productTable, productJoin));

            $info("取得上次记录的 Product ( Full ) [ %s ] 个", productBeans.size());

            for (ProductBean productBean : productBeans) {
                // 这里处理 UrlKey 字段, 否则后续无法和正常的商品信息比对
                productBean.setUrl_key(clearSpecialSymbol(productBean.getUrl_key()));
            }

            return productBeans;
        }

        private Map<String, List<ImageBean>> getLastImages() {

            $info("准备批量获取上次记录的 Image ( Full )");



            // 逻辑设定上, getLastImags 将和 getLastProducts 一样, 一次 Job 只查询一次. 数据的处理移交 Java 端执行
            // 所以吧必须的两列内容按固定格式取出
            String code = Feeds.getVal1(channel, FeedEnums.Name.item_code);
            String images = Feeds.getVal1(channel, FeedEnums.Name.images);

            // 拼装 full 表获取的专用条件
            String where = String.format("WHERE item_id in (select item_id from %s where %s)", imageTable,getWhereUpdateFlg());

            // 通过组装的 SQL 查询这次需要更新的商品,其上次的图片信息
            List<String> imageArrs = superFeedDao.selectAllfeedImage(
                    where,
                    String.format("DISTINCT CONCAT(%s, '<>', %s)", code, images), // 这里定义固定格式
                    String.format("%s_full %s", imageTable, "")); // 表部分

            return imageArrs
                    .stream()
                    .map(str -> str.split("<>"))
                    .collect(
                            toMap(arr -> arr[0], arr -> getImageBeans(arr[1])));
        }

        private List<ImageBean> getImageBeans(String imagePathsStr) {

            List<ImageBean> imageBeans = new ArrayList<>();

            String separator = Feeds.getVal1(channel, FeedEnums.Name.image_split);

            String[] imagePaths = imagePathsStr.split(separator);

            for (String imagePath : imagePaths) {

                ImageBean imageBean = new ImageBean();

                imageBean.setImage_type("1");
                imageBean.setImage_url(imagePath);
                imageBean.setImage_name(imagePath.substring(imagePath.lastIndexOf("/") + 1));
                imageBean.setDisplay_order("0");

                imageBeans.add(imageBean);
            }

            return imageBeans;
        }

        /**
         * 通过关联关系,组装更新参数
         */
        private ProductsFeedUpdate getWsdlParam(Relation relation) {

            Map<String, String> updateFields = relation.getUpdateFields();

            if (updateFields == null || updateFields.size() < 1)
                return null;

            ProductsFeedUpdate feedUpdate = new ProductsFeedUpdate();

            feedUpdate.setChannel_id(channel.getId());
            feedUpdate.setCode(relation.updating.getP_code());
            feedUpdate.setProduct_url_key(relation.updating.getUrl_key());
            feedUpdate.setUpdatefields(updateFields);

            return feedUpdate;
        }
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
        private ProductBean last;

        public Relation(ProductBean updating, List<ProductBean> lastProduct, Map<String, List<ImageBean>> codeImages) {

            this.updating = updating;

            // 这里因为 ProductBean 的 equals 重写, 可以通过 indexOf 查询到上次的数据
            this.last = lastProduct.get(lastProduct.indexOf(updating));
            // 使用 Code 关联查询上次的图片信息
            this.last.setImages(codeImages.get(this.last.getP_code()));
        }

        /**
         * 获取需要更新的字段
         */
        public Map<String, String> getUpdateFields() {

            if (last == null) return null;

            Map<String, String> updateFields = new HashMap<>();

            if (!updating.getP_msrp().equals(last.getP_msrp())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_MSRP, updating.getP_msrp());
            }

            if (!updating.getPs_price().equals(last.getPs_price())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_PRICE, updating.getPs_price());
            }

            if (!updating.getCps_cn_price().equals(last.getCps_cn_price())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE, updating.getCps_cn_price());
            }

            if (!updating.getCps_cn_price_rmb().equals(last.getCps_cn_price_rmb())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_RMB, updating.getCps_cn_price_rmb());
            }

            if (!updating.getPe_long_description().equals(last.getPe_long_description())) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_LONG_DESCRIPTION, updating.getPe_long_description());
            }

            String separator = CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_SPLIT;

            String images = updating.getImages().stream().map(ImageBean::getImage_url).filter(i -> i != null).collect(joining(separator));

            String lastImages = last.getImages().stream().map(ImageBean::getImage_url).filter(i -> i != null).collect(joining(separator));

            if (!images.equals(lastImages)) {
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_URL, images);
            }

            return updateFields;
        }
    }
}
