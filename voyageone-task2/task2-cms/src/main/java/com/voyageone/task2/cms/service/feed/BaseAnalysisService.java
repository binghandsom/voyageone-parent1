package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.SuperFeed2Dao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author james.li on 2016/4/28.
 * @version 2.0.0
 */
public abstract class BaseAnalysisService  extends BaseTaskService {

    protected ChannelConfigEnums.Channel channel;

    protected String table;

    protected static final String INSERT_FLG = " UpdateFlag = 1 ";
    @Autowired
    protected SuperFeed2Dao superFeedDao;
    @Autowired
    protected FeedToCmsService feedToCmsService;
    @Autowired
    protected Transformer transformer;
    @Autowired
    protected TransactionRunner transactionRunnerCms2;

    protected abstract  void updateFull(List<String> itemIds);

    /**
     * 清空zzwork表
     */
    protected abstract  void zzWorkClear();

    /**
     * 读入feed文件并插入zzwork表
     * @return
     */
    protected abstract  int superFeedImport();

    public abstract ChannelConfigEnums.Channel getChannel();


    /**
     * 根据类目获取该类目小的产品数据
     * @param categorPath
     * @return
     */
    protected abstract List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath);

    protected void init(){
        channel=getChannel();

        table = Feeds.getVal1(channel, FeedEnums.Name.table_id);
    }
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        init();

        zzWorkClear();

        $info("产品信息插入开始");
        int cnt = superFeedImport();
        $info("产品信息插入完成 共" + cnt + "条数据");
        if (cnt > 0) {
//
            transformer.new Context(channel, this).transform();

            postNewProduct();

            backupFeedFile(channel.getId());
        }
    }

    protected boolean backupFeedFile(String channel_id) {
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_id));
        String filename_backup = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_id));
        File file = new File(filename);
        File file_backup = new File(filename_backup);

        if (!file.renameTo(file_backup)) {
//            logger.error("产品文件备份失败");
            $info("产品文件备份失败");
        }

        $info("备份处理文件结束");
        return true;
    }

    protected HashMap<String, Object> getColumns() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("category", Feeds.getVal1(channel, FeedEnums.Name.category_column));
        map.put("channel_id", channel.getId());
        map.put("m_brand", Feeds.getVal1(channel, FeedEnums.Name.model_m_brand));
        map.put("m_model", Feeds.getVal1(channel, FeedEnums.Name.model_m_model));
        map.put("m_size_type", Feeds.getVal1(channel, FeedEnums.Name.model_m_size_type));
        map.put("m_weight", Feeds.getVal1(channel, FeedEnums.Name.model_m_weight));
        map.put("p_code", (Feeds.getVal1(channel, FeedEnums.Name.product_p_code)));
        map.put("p_name", (Feeds.getVal1(channel, FeedEnums.Name.product_p_name)));
        map.put("p_color", (Feeds.getVal1(channel, FeedEnums.Name.product_p_color)));
        map.put("p_made_in_country", (Feeds.getVal1(channel, FeedEnums.Name.product_p_made_in_country)));
        map.put("pe_short_description", (Feeds.getVal1(channel, FeedEnums.Name.product_pe_short_description)));
        map.put("pe_long_description", (Feeds.getVal1(channel, FeedEnums.Name.product_pe_long_description)));
        map.put("i_sku", (Feeds.getVal1(channel, FeedEnums.Name.item_i_sku)));
        map.put("i_itemcode", (Feeds.getVal1(channel, FeedEnums.Name.item_i_itemcode)));
        map.put("i_size", (Feeds.getVal1(channel, FeedEnums.Name.item_i_size)));
        map.put("i_barcode", (Feeds.getVal1(channel, FeedEnums.Name.item_i_barcode)));
        map.put("image", (Feeds.getVal1(channel, FeedEnums.Name.images)));
        map.put("i_client_sku", (Feeds.getVal1(channel, FeedEnums.Name.item_i_client_sku)));
        map.put("client_product_url", (Feeds.getVal1(channel, FeedEnums.Name.client_product_url)));
        map.put("product_type", (Feeds.getVal1(channel, FeedEnums.Name.product_type)));
        map.put("price_client_msrp", (Feeds.getVal1(channel, FeedEnums.Name.price_client_msrp)));
        map.put("price_client_retail", (Feeds.getVal1(channel, FeedEnums.Name.price_client_retail)));
        map.put("price_net", (Feeds.getVal1(channel, FeedEnums.Name.price_net)));
        map.put("price_current", (Feeds.getVal1(channel, FeedEnums.Name.price_current)));
        map.put("price_msrp", (Feeds.getVal1(channel, FeedEnums.Name.price_msrp)));
        return map;
    }

    /**
     * MongoDB插入更新
     *
     * @param productAll 全部更新对象
     * @param productSucceeList 接收成功更新对象
     * @param productFailAllList 全部更新失败对象
     */
    protected  void executeMongoDB(List<CmsBtFeedInfoModel> productAll, List<CmsBtFeedInfoModel> productSucceeList, List<CmsBtFeedInfoModel> productFailAllList) {
        try {
            $info("插入mongodb开始");
            Map response = feedToCmsService.updateProduct(channel.getId(), productAll, getTaskName());
            $info("插入mongodb结束");
            List<String> itemIds = new ArrayList<>();
            productSucceeList = (List<CmsBtFeedInfoModel>) response.get("succeed");
            productSucceeList.forEach(feedProductModel -> feedProductModel.getSkus().forEach(feedSkuModel -> itemIds.add(feedSkuModel.getClientSku())));
            updateFull(itemIds);
            productFailAllList.addAll((List<CmsBtFeedInfoModel>) response.get("fail"));
        } catch (Exception e) {
            $error(e);
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
        } finally {
            productSucceeList.clear();
            productAll.clear();
        }
    }

    /**
     * 调用 WsdlProductService 提交新商品
     *
     * @throws Exception
     */
    protected void postNewProduct() throws Exception {


        $info("准备 <构造> 类目树");
        List<String> categoriePaths = getCategories();

        List<CmsBtFeedInfoModel> productSucceeList = new ArrayList<>();
        // 准备接收失败内容
        List<CmsBtFeedInfoModel> productFailAllList = new ArrayList<>();
        List<CmsBtFeedInfoModel> productAll = new ArrayList<>();

        for (String categorPath : categoriePaths) {

            // 每棵树的信息取得
            $info("每棵树的信息取得开始" + categorPath);
            List<CmsBtFeedInfoModel> product;
            try{
                product = getFeedInfoByCategory(categorPath);

                $info("每棵树的信息取得结束");

                String categorySplit =  Feeds.getVal1(channel, FeedEnums.Name.category_split);
                if(!StringUtils.isEmpty(categorySplit)) {
                    product.forEach(cmsBtFeedInfoModel -> {
                        List<String> categors = java.util.Arrays.asList(cmsBtFeedInfoModel.getCategory().split(categorySplit));
                        cmsBtFeedInfoModel.setCategory(categors.stream().map(s -> s.replace("-", "－")).collect(Collectors.joining("-")));
                    });
                }
                productAll.addAll(product);
                if(productAll.size() > 500){
                    executeMongoDB(productAll, productSucceeList, productFailAllList);
                }
            }catch (Exception e){
                e.printStackTrace();
                issueLog.log(e,ErrorType.BatchJob,SubSystem.CMS);
            }
        }
        executeMongoDB(productAll, productSucceeList, productFailAllList);

        $info("总共~ 失败的 Product: %s", productFailAllList.size());

    }

    /**
     * 查询一共有多少类目
     *
     * @return 类目清单
     */
    protected List<String> getCategories() {

        // 先从数据表中获取所有商品的类目路径,经过去重复的
        // update flg 标记, 只获取哪些即将进行新增的商品的类目
        List<String> categoryPaths = superFeedDao.selectSuperfeedCategory(
                Feeds.getVal1(channel, FeedEnums.Name.category_column), table, " AND " + INSERT_FLG);
        $info("获取类目路径数 %s , 准备拆分继续处理", categoryPaths.size());

        return categoryPaths;
    }

    // 合并属性
    public static Map<String, List<String>> attributeMerge(Map<String, List<String>> attribute1, Map<String, List<String>> attribute2) {

        for (String key : attribute1.keySet()) {
            if (attribute2.containsKey(key)) {
                attribute2.put(key, Stream.concat(attribute1.get(key).stream(), attribute2.get(key).stream())
                        .map(String::trim)
                        .distinct()
                        .collect(toList()));
            } else {
                attribute2.put(key, attribute1.get(key));
            }
        }
        for(String key: attribute2.keySet()){
            attribute2.put(key, attribute2.get(key).stream().distinct().collect(Collectors.toList()));
        }
        return attribute2;
    }
}
