package com.voyageone.batch.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.SuperFeedJEBean;
import com.voyageone.batch.cms.dao.SuperFeed2Dao;
import com.voyageone.batch.cms.dao.SuperFeedDao;
import com.voyageone.batch.cms.dao.feed.SearsFeedDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.SearsService;
import com.voyageone.common.components.sears.bean.PaginationBean;
import com.voyageone.common.components.sears.bean.ProductResponse;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.JEWELRY;
import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.SEARS;

/**
 * Bcbg 的 Feed 数据分析服务
 * <p>
 * Created by Jonas on 10/10/15.
 */
@Service
public class JewelryAnalysisService extends BaseTaskService {

    @Autowired
    private SuperFeed2Dao superfeeddao;

    @Autowired
    private Transformer transformer;

    @Autowired
    private JewelryWsdlInsert insertService;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "CmsJEAnalySisJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 插入数据库
        List<SuperFeedJEBean> superFeedJEBean = jeSuperFeedImport();

        if(superFeedJEBean.size()>0){
            insertSuperFeedJE(superFeedJEBean);

            CheckSuperFeed(JEWELRY.getId());

            transformer.new Context(JEWELRY, this).transform();

            insertService.new Context(JEWELRY).postNewProduct();

            backupFeedFile(JEWELRY.getId());
        }

    }
    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeedJE(List<SuperFeedJEBean> superfeedlist) {
        boolean isSuccess = true;
        $info("JE产品信息插入开始");

        // 清表
        if (superfeeddao.deleteTableInfo(Feed.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(), FeedEnums.Name.table_id2)) >= 0) {
            for (SuperFeedJEBean superfeed : superfeedlist) {

                if (superfeeddao.insertSuperfeedJEInfo(superfeed) <= 0) {
                    $info("JE产品信息插入失败 InventoryNumber = " + superfeed.getInventoryNumber());
                }
            }
        } else {
            isSuccess = false;
            $info("JE产品信息插入失败 清TMP表失败");
        }
        $info("JE产品信息插入完成");
        return isSuccess;
    }

    /**
     * JE产品文件读入
     */
    public List<SuperFeedJEBean> jeSuperFeedImport() {
        $info("JE产品文件读入开始");

        List<SuperFeedJEBean> superfeed = new ArrayList<>();

        CsvReader reader;
        try {
            String fileName = Feed.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(), FeedEnums.Name.file_id);
            String filePath = Feed.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(), FeedEnums.Name.feed_ftp_localpath2);
            String fileFullName = String.format("%s/%s", filePath, fileName);

            String encode = Feed.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(), FeedEnums.Name.feed_ftp_file_coding);

            reader = new CsvReader(new FileInputStream(fileFullName), '\t', Charset.forName(encode));

            // Head读入
            reader.readHeaders();
            reader.getHeaders();

            // Body读入
            while (reader.readRecord()) {
                SuperFeedJEBean superfeedjebean = new SuperFeedJEBean();

                int i = 0;
                superfeedjebean.setAuctionTitle(reader.get(i++));
                superfeedjebean.setInventoryNumber(reader.get(i++));
                superfeedjebean.setWeight(reader.get(i++));
                superfeedjebean.setISBN(reader.get(i++));
                superfeedjebean.setUPC(reader.get(i++));
                superfeedjebean.setEAN(reader.get(i++));
                superfeedjebean.setASIN(reader.get(i++));
                superfeedjebean.setMPN(reader.get(i++));
                superfeedjebean.setShortDescription(reader.get(i++));
                superfeedjebean.setDescription(reader.get(i++));
                superfeedjebean.setFlag(reader.get(i++));
                superfeedjebean.setFlagDescription(reader.get(i++));
                superfeedjebean.setBlocked(reader.get(i++));
                superfeedjebean.setBlockedComment(reader.get(i++));
                superfeedjebean.setManufacturer(reader.get(i++));
                superfeedjebean.setBrand(reader.get(i++));
                superfeedjebean.setCondition(reader.get(i++));
                superfeedjebean.setWarranty(reader.get(i++));
                superfeedjebean.setSellerCost(reader.get(i++));
                superfeedjebean.setProductMargin(reader.get(i++));
                superfeedjebean.setBuyItNowPrice(reader.get(i++));
                superfeedjebean.setRetailPrice(reader.get(i++));
                superfeedjebean.setPictureURLs(reader.get(i++));
                superfeedjebean.setTaxProductCode(reader.get(i++));
                superfeedjebean.setSupplierCode(reader.get(i++));
                superfeedjebean.setSupplierPO(reader.get(i++));
                superfeedjebean.setWarehouseLocation(reader.get(i++));
                superfeedjebean.setInventorySubtitle(reader.get(i++));
                superfeedjebean.setRelationshipName(reader.get(i++));
                superfeedjebean.setVariationParentSKU(reader.get(i++));
                superfeedjebean.setLabels(reader.get(i++));
                superfeedjebean.setAttribute1Name(reader.get(i++));
                superfeedjebean.setAttribute1Value(reader.get(i++));
                superfeedjebean.setAttribute2Name(reader.get(i++));
                superfeedjebean.setAttribute2Value(reader.get(i++));
                superfeedjebean.setAttribute3Name(reader.get(i++));
                superfeedjebean.setAttribute3Value(reader.get(i++));
                superfeedjebean.setAttribute4Name(reader.get(i++));
                superfeedjebean.setAttribute4Value(reader.get(i++));
                superfeedjebean.setAttribute5Name(reader.get(i++));
                superfeedjebean.setAttribute5Value(reader.get(i++));
                superfeedjebean.setAttribute6Name(reader.get(i++));
                superfeedjebean.setAttribute6Value(reader.get(i++));
                superfeedjebean.setAttribute7Name(reader.get(i++));
                superfeedjebean.setAttribute7Value(reader.get(i++));
                superfeedjebean.setAttribute8Name(reader.get(i++));
                superfeedjebean.setAttribute8Value(reader.get(i++));
                superfeedjebean.setAttribute9Name(reader.get(i++));
                superfeedjebean.setAttribute9Value(reader.get(i++));
                superfeedjebean.setAttribute10Name(reader.get(i++));
                superfeedjebean.setAttribute10Value(reader.get(i++));
                superfeedjebean.setAttribute11Name(reader.get(i++));
                superfeedjebean.setAttribute11Value(reader.get(i++));
                superfeedjebean.setAttribute12Name(reader.get(i++));
                superfeedjebean.setAttribute12Value(reader.get(i++));
                superfeedjebean.setAttribute13Name(reader.get(i++));
                superfeedjebean.setAttribute13Value(reader.get(i++));
                superfeedjebean.setAttribute14Name(reader.get(i++));
                superfeedjebean.setAttribute14Value(reader.get(i++));
                superfeedjebean.setAttribute15Name(reader.get(i++));
                superfeedjebean.setAttribute15Value(reader.get(i++));
                superfeedjebean.setAttribute16Name(reader.get(i++));
                superfeedjebean.setAttribute16Value(reader.get(i++));
                superfeedjebean.setAttribute17Name(reader.get(i++));
                superfeedjebean.setAttribute17Value(reader.get(i++));
                superfeedjebean.setAttribute18Name(reader.get(i++));
                superfeedjebean.setAttribute18Value(reader.get(i++));
                superfeedjebean.setAttribute19Name(reader.get(i++));
                superfeedjebean.setAttribute19Value(reader.get(i++));
                superfeedjebean.setAttribute20Name(reader.get(i++));
                superfeedjebean.setAttribute20Value(reader.get(i++));
                superfeedjebean.setAttribute21Name(reader.get(i++));
                superfeedjebean.setAttribute21Value(reader.get(i++));
                superfeedjebean.setAttribute22Name(reader.get(i++));
                superfeedjebean.setAttribute22Value(reader.get(i++));
                superfeedjebean.setAttribute23Name(reader.get(i++));
                superfeedjebean.setAttribute23Value(reader.get(i++));
                superfeedjebean.setAttribute24Name(reader.get(i++));
                superfeedjebean.setAttribute24Value(reader.get(i++));
                superfeedjebean.setAttribute25Name(reader.get(i++));
                superfeedjebean.setAttribute25Value(reader.get(i++));
                superfeedjebean.setAttribute26Name(reader.get(i++));
                superfeedjebean.setAttribute26Value(reader.get(i++));
                superfeedjebean.setAttribute27Name(reader.get(i++));
                superfeedjebean.setAttribute27Value(reader.get(i++));
                superfeedjebean.setAttribute28Name(reader.get(i++));
                superfeedjebean.setAttribute28Value(reader.get(i++));
                superfeedjebean.setAttribute29Name(reader.get(i++));
                superfeedjebean.setAttribute29Value(reader.get(i++));
                superfeedjebean.setAttribute30Name(reader.get(i++));
                superfeedjebean.setAttribute30Value(reader.get(i++));
                superfeedjebean.setAttribute31Name(reader.get(i++));
                superfeedjebean.setAttribute31Value(reader.get(i++));
                superfeedjebean.setAttribute32Name(reader.get(i++));
                superfeedjebean.setAttribute32Value(reader.get(i++));
                superfeedjebean.setAttribute33Name(reader.get(i++));
                superfeedjebean.setAttribute33Value(reader.get(i++));
                superfeedjebean.setAttribute34Name(reader.get(i++));
                superfeedjebean.setAttribute34Value(reader.get(i++));
                superfeedjebean.setAttribute35Name(reader.get(i++));
                superfeedjebean.setAttribute35Value(reader.get(i++));
                superfeedjebean.setAttribute36Name(reader.get(i++));
                superfeedjebean.setAttribute36Value(reader.get(i++));
                superfeedjebean.setAttribute37Name(reader.get(i++));
                superfeedjebean.setAttribute37Value(reader.get(i++));
                superfeedjebean.setAttribute38Name(reader.get(i++));
                superfeedjebean.setAttribute38Value(reader.get(i++));
                superfeedjebean.setAttribute39Name(reader.get(i++));
                superfeedjebean.setAttribute39Value(reader.get(i++));
                superfeedjebean.setAttribute40Name(reader.get(i++));
                superfeedjebean.setAttribute40Value(reader.get(i++));
                superfeedjebean.setAttribute41Name(reader.get(i++));
                superfeedjebean.setAttribute41Value(reader.get(i++));
                superfeedjebean.setAttribute42Name(reader.get(i++));
                superfeedjebean.setAttribute42Value(reader.get(i++));
                superfeedjebean.setAttribute43Name(reader.get(i++));
                superfeedjebean.setAttribute43Value(reader.get(i++));
                superfeedjebean.setAttribute44Name(reader.get(i++));
                superfeedjebean.setAttribute44Value(reader.get(i++));
                superfeedjebean.setAttribute47Name(reader.get(i++));
                superfeedjebean.setAttribute47Value(reader.get(i++));
                superfeedjebean.setAttribute48Name(reader.get(i++));
                superfeedjebean.setAttribute48Value(reader.get(i++));
                superfeedjebean.setAttribute49Name(reader.get(i++));
                superfeedjebean.setAttribute49Value(reader.get(i++));
                superfeedjebean.setAttribute50Name(reader.get(i++));
                superfeedjebean.setAttribute50Value(reader.get(i++));
                superfeedjebean.setAttribute51Name(reader.get(i++));
                superfeedjebean.setAttribute51Value(reader.get(i++));
                superfeedjebean.setAttribute52Name(reader.get(i++));
                superfeedjebean.setAttribute52Value(reader.get(i++));
                superfeedjebean.setAttribute53Name(reader.get(i++));
                superfeedjebean.setAttribute53Value(reader.get(i++));
                superfeedjebean.setAttribute54Name(reader.get(i++));
                superfeedjebean.setAttribute54Value(reader.get(i++));
                superfeedjebean.setAttribute55Name(reader.get(i++));
                superfeedjebean.setAttribute55Value(reader.get(i++));
                superfeedjebean.setAttribute56Name(reader.get(i++));
                superfeedjebean.setAttribute56Value(reader.get(i++));
                superfeedjebean.setAttribute57Name(reader.get(i++));
                superfeedjebean.setAttribute57Value(reader.get(i++));
                superfeedjebean.setAttribute58Name(reader.get(i++));
                superfeedjebean.setAttribute58Value(reader.get(i++));
                superfeedjebean.setAttribute59Name(reader.get(i++));
                superfeedjebean.setAttribute59Value(reader.get(i++));
                superfeedjebean.setAttribute60Name(reader.get(i++));
                superfeedjebean.setAttribute60Value(reader.get(i++));
                superfeedjebean.setAttribute61Name(reader.get(i++));
                superfeedjebean.setAttribute61Value(reader.get(i++));
                superfeedjebean.setAttribute62Name(reader.get(i++));
                superfeedjebean.setAttribute62Value(reader.get(i++));
                superfeedjebean.setAttribute63Name(reader.get(i++));
                superfeedjebean.setAttribute63Value(reader.get(i++));
                superfeedjebean.setAttribute64Name(reader.get(i++));
                superfeedjebean.setAttribute64Value(reader.get(i++));
                superfeedjebean.setAttribute65Name(reader.get(i++));
                superfeedjebean.setAttribute65Value(reader.get(i++));
                superfeedjebean.setAttribute66Name(reader.get(i++));
                superfeedjebean.setAttribute66Value(reader.get(i++));
                superfeedjebean.setAttribute67Name(reader.get(i++));
                superfeedjebean.setAttribute67Value(reader.get(i++));
                superfeedjebean.setAttribute68Name(reader.get(i++));
                superfeedjebean.setAttribute68Value(reader.get(i++));
                superfeedjebean.setAttribute69Name(reader.get(i++));
                superfeedjebean.setAttribute69Value(reader.get(i++));
                superfeedjebean.setAttribute70Name(reader.get(i++));
                superfeedjebean.setAttribute70Value(reader.get(i++));
                superfeedjebean.setAttribute71Name(reader.get(i++));
                superfeedjebean.setAttribute71Value(reader.get(i++));
                superfeedjebean.setAttribute72Name(reader.get(i++));
                superfeedjebean.setAttribute72Value(reader.get(i++));
                superfeedjebean.setAttribute73Name(reader.get(i++));
                superfeedjebean.setAttribute73Value(reader.get(i++));
                superfeedjebean.setAttribute74Name(reader.get(i++));
                superfeedjebean.setAttribute74Value(reader.get(i++));
                superfeedjebean.setAttribute75Name(reader.get(i++));
                superfeedjebean.setAttribute75Value(reader.get(i++));
                superfeedjebean.setAttribute76Name(reader.get(i++));
                superfeedjebean.setAttribute76Value(reader.get(i++));
                superfeedjebean.setAttribute77Name(reader.get(i++));
                superfeedjebean.setAttribute77Value(reader.get(i++));
                superfeedjebean.setAttribute78Name(reader.get(i++));
                superfeedjebean.setAttribute78Value(reader.get(i++));
                superfeedjebean.setAttribute79Name(reader.get(i++));
                superfeedjebean.setAttribute79Value(reader.get(i++));
                superfeedjebean.setAttribute80Name(reader.get(i++));
                superfeedjebean.setAttribute80Value(reader.get(i++));
                superfeedjebean.setAttribute81Name(reader.get(i++));
                superfeedjebean.setAttribute81Value(reader.get(i++));
                superfeedjebean.setAttribute82Name(reader.get(i++));
                superfeedjebean.setAttribute82Value(reader.get(i++));
                superfeedjebean.setAttribute83Name(reader.get(i++));
                superfeedjebean.setAttribute83Value(reader.get(i++));
                superfeedjebean.setAttribute84Name(reader.get(i++));
                superfeedjebean.setAttribute84Value(reader.get(i++));
                superfeedjebean.setAttribute85Name(reader.get(i++));
                superfeedjebean.setAttribute85Value(reader.get(i++));
                superfeedjebean.setAttribute86Name(reader.get(i++));
                superfeedjebean.setAttribute86Value(reader.get(i++));
                superfeedjebean.setAttribute87Name(reader.get(i++));
                superfeedjebean.setAttribute87Value(reader.get(i++));
                superfeedjebean.setAttribute88Name(reader.get(i++));
                superfeedjebean.setAttribute88Value(reader.get(i++));
                superfeedjebean.setAttribute89Name(reader.get(i++));
                superfeedjebean.setAttribute89Value(reader.get(i++));
                superfeedjebean.setAttribute90Name(reader.get(i++));
                superfeedjebean.setAttribute90Value(reader.get(i++));
                superfeedjebean.setAttribute91Name(reader.get(i++));
                superfeedjebean.setAttribute91Value(reader.get(i++));
                superfeedjebean.setAttribute92Name(reader.get(i++));
                superfeedjebean.setAttribute92Value(reader.get(i++));
                superfeedjebean.setAttribute93Name(reader.get(i++));
                superfeedjebean.setAttribute93Value(reader.get(i++));
                superfeedjebean.setAttribute94Name(reader.get(i++));
                superfeedjebean.setAttribute94Value(reader.get(i++));
                superfeedjebean.setHarmonizedCode(reader.get(i++));
                superfeedjebean.setHeight(reader.get(i++));
                superfeedjebean.setLength(reader.get(i++));
                superfeedjebean.setWidth(reader.get(i++));
                superfeedjebean.setDCCode(reader.get(i++));
                superfeedjebean.setClassification(reader.get(i++));
                superfeedjebean.setAttribute95Name(reader.get(i++));
                superfeedjebean.setAttribute95Value(reader.get(i++));
                superfeedjebean.setAttribute96Name(reader.get(i++));
                superfeedjebean.setAttribute96Value(reader.get(i++));

                // 97 / 98 (2015-12-03 16:22:59 - jonas add)
                superfeedjebean.setAttribute97Name(reader.get(i++));
                superfeedjebean.setAttribute97Value(reader.get(i++));
                superfeedjebean.setAttribute98Name(reader.get(i++));
                superfeedjebean.setAttribute98Value(reader.get(i));

                superfeed.add(superfeedjebean);
            }

            reader.close();
            $info("JE产品文件读入完成");
        } catch (Exception ex) {
            $info("JE产品文件读入失败");
            logIssue("cms 数据导入处理", "JE产品文件读入失败 " + ex.getMessage());
        }
        return superfeed;
    }

    public boolean CheckSuperFeed(String channel_id) {
        boolean isSuccess = true;
        $info("异常数据清除开始");


        // 取得category异常数据
        List<String> err_categorylist = superfeeddao.selectErrData(Feed.getVal1(channel_id, FeedEnums.Name.feed_delete_category_err_sql2));

        // 异常数据-邮件提示
        String err_data_maill = "category异常:";
        // 删除异常数据
        String err_data = "";
        for (String anErr_categorylist : err_categorylist) {
            err_data = err_data + " '" + anErr_categorylist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feed.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feed.getVal1(channel_id, FeedEnums.Name.table_id2), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }

        // 取得model异常数据
        List<String> err_modellist = superfeeddao.selectErrData(Feed.getVal1(channel_id, FeedEnums.Name.feed_delete_model_sql2));
        err_data_maill = "model异常:";
        err_data = "";
        for (String anErr_modellist : err_modellist) {
            err_data = err_data + " '" + anErr_modellist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feed.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feed.getVal1(channel_id, FeedEnums.Name.table_id2), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }

        // 取得product异常数据
        List<String> err_productlist = superfeeddao.selectErrData(Feed.getVal1(channel_id, FeedEnums.Name.feed_delete_product_sql2));
        err_data = "";
        err_data_maill = "product异常:";
        for (String anErr_productlist : err_productlist) {
            err_data = err_data + " '" + anErr_productlist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feed.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feed.getVal1(channel_id, FeedEnums.Name.table_id2), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }

        // 取得有model无product异常数据
        List<String> err_model_noproductlist = superfeeddao.selectErrData(Feed.getVal1(channel_id, FeedEnums.Name.feed_delete_model_no_product_sql2));
        err_data = "";
        err_data_maill = "有model无product异常:";
        for (String anErr_model_noproductlist : err_model_noproductlist) {
            err_data = err_data + " '" + anErr_model_noproductlist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feed.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feed.getVal1(channel_id, FeedEnums.Name.table_id2), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }

        // 取得有prouduct无model异常数据
        List<String> err_product_nomodellist = superfeeddao.selectErrData(Feed.getVal1(channel_id, FeedEnums.Name.feed_delete_product_no_model_sql2));
        err_data = "";
        err_data_maill = "有prouduct无model异常:";
        for (String anErr_product_nomodellist : err_product_nomodellist) {
            err_data = err_data + " '" + anErr_product_nomodellist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feed.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feed.getVal1(channel_id, FeedEnums.Name.table_id2), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }
        $info("异常数据清除结束");

//        // UpdateFlag : 1:插入 2:更新 3:两者都有 0:不处理
//        $info("数据判定开始");
//        if (isSuccess) {
//            // insert data
//            List<String> insert_data = superfeeddao.inertSuperfeedInsertData(Feed.getVal1(channel_id, FeedEnums.Name.category_column),
//                    Feed.getVal1(channel_id, FeedEnums.Name.feed_model_key),
//                    Feed.getVal1(channel_id, FeedEnums.Name.feed_code_key),
//                    Feed.getVal1(channel_id, FeedEnums.Name.table_id2),
//                    Feed.getVal1(channel_id, FeedEnums.Name.table_id2) + "_full");
//
//            // update data
//            List<String> update_data = superfeeddao.inertSuperfeedUpdateData(Feed.getVal1(channel_id, FeedEnums.Name.feed_code_key),
//                    Feed.getVal1(channel_id, FeedEnums.Name.table_id2),
//                    Feed.getVal1(channel_id, FeedEnums.Name.table_id2) + "_full");
//
//            // 新数据_code
//            String str_code_insert = "";
//            // 更新数据_code
//            String str_code_update = "";
//            // 插入数据_code 存在
//            if (insert_data.size() > 0) {
//                for (String anInsert_data : insert_data) {
//                    str_code_insert = str_code_insert + "'" + anInsert_data + "',";
//                }
//                // 去掉最后一个“，”
//                str_code_insert = str_code_insert.substring(0, str_code_insert.lastIndexOf(","));
//
//                // 插入数据 更新UpdateFlag :1
//                int reslut_insert = superfeeddao.updateInsertData(Feed.getVal1(channel_id, FeedEnums.Name.table_id2),
//                        Feed.getVal1(channel_id, FeedEnums.Name.product_keyword),
//                        Feed.getVal1(channel_id, FeedEnums.Name.feed_code_key),
//                        str_code_insert);
//
//                if (reslut_insert <= 0) {
//                    logIssue("cms 数据导入处理", "更新UpdateFlag :1 ( for product ), 无受影响行数");
//                }
//
//                // 插入数据 补足没有model的数据
//                int reslut_insertmodel = superfeeddao.updateInsertModelData(Feed.getVal1(channel_id, FeedEnums.Name.table_id2), Feed.getVal1(channel_id, FeedEnums.Name.feed_model_key),
//                        Feed.getVal1(channel_id, FeedEnums.Name.feed_code_key), str_code_insert, Feed.getVal1(channel_id, FeedEnums.Name.feed_model_keyword));
//                if (reslut_insertmodel < 0) {
//                    logIssue("cms 数据导入处理", "更新UpdateFlag :1 ( for model ), 无受影响行数");
//                }
//            }
//
//            // 更新数据_code 存在
//            if (update_data.size() > 0) {
//                for (String anUpdate_data : update_data) {
//                    str_code_update = str_code_update + "'" + anUpdate_data + "',";
//                }
//                // 去掉最后一个“，”
//                str_code_update = str_code_update.substring(0, str_code_update.lastIndexOf(","));
//
//                // 更新数据 更新UpdateFlag :2，3
//                int reslut_update = superfeeddao.updateUpdateData(Feed.getVal1(channel_id, FeedEnums.Name.table_id2), Feed.getVal1(channel_id, FeedEnums.Name.feed_code_key), str_code_update);
//                if (reslut_update <= 0) {
//                    logIssue("cms 数据导入处理", "更新UpdateFlag :2，3 ( for all ), 无受影响行数");
//                }
//            }
//        }
        $info("数据判定结束");

        return isSuccess;
    }

    private boolean backupFeedFile(String channel_id) {
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath2) + "/" + StringUtils.null2Space(Feed.getVal1(channel_id, FeedEnums.Name.file_id));
        String filename_backup = Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath2) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feed.getVal1(channel_id, FeedEnums.Name.file_id));
        File file = new File(filename);
        File file_backup = new File(filename_backup);

        if (!file.renameTo(file_backup)) {
//            logger.error("产品文件备份失败");
            $info("产品文件备份失败");
        }

        $info("备份处理文件结束");
        return true;
    }
}
