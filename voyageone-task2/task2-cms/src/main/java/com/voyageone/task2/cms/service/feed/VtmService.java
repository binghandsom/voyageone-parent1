package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.task2.base.BaseTaskService;

import com.voyageone.task2.cms.bean.SuperFeedVtmBean;
import com.voyageone.task2.cms.dao.SuperFeed2Dao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.VITAMIN;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/4
 */
@Service
public class VtmService extends BaseTaskService {

    @Autowired
    private SuperFeed2Dao superfeeddao;

    @Autowired
    private Transformer transformer;

    @Autowired
    private VtmWsdlInsert insertService;

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
        return "CmsVtmJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 清表
        superfeeddao.deleteTableInfo(Feed.getVal1(ChannelConfigEnums.Channel.VITAMIN.getId(), FeedEnums.Name.table_id));

        // 插入数据库
        $info("维他命产品信息插入开始");
        int count = vtmSuperFeedImport();
        $info("维他命产品信息插入完成");

        // updateFlag变更，0：原有数据 1：新数据 2：Error数据（category,CNMSRP，CNPrice, Image List为空）
        // 1的sql文：
        // UPDATE voyageone_cms2.cms_zz_worktable_vtm_superfeed b LEFT JOIN voyageone_cms2.cms_zz_worktable_vtm_superfeed_full bf ON b.md5 = bf.md5 SET b.UpdateFlag = 1 WHERE bf.md5 IS NULL;
        // 2的sql文：
        // UPDATE voyageone_cms2.cms_zz_worktable_vtm_superfeed b SET b.UpdateFlag = 2 WHERE b.MerchantPrimaryCategory="" OR b.CNMSRP="" OR b.CNPrice="" OR b.`Image List`="";
        transformer.new Context(VITAMIN, this).transform();

        insertService.new Context(VITAMIN).postNewProduct();
        // 更新完成，UpdateFlag结果：
        // UpdateFlag    cms_zz_worktable_vtm_superfeed           cms_zz_worktable_vtm_superfeed_full
        // 0             已经加入的原有数据(未变化)               已经加入的原有数据(未变化)
        // 1             途中OK待加入的数据(追加和变更的数据)     本次导入追加和变更的数据
        // 2             本次导入error的数据                      /
        // 3             本次导入完全导入成功的数据               /

//        backupFeedFile(VITAMIN.getId());

    }

    /**
     * 维他命产品信息插入
     *
     * @return isSuccess
     */
    public int insertSuperFeedVtm(List<SuperFeedVtmBean> superfeedlist) {

        int count = 0;

        for (SuperFeedVtmBean superfeed : superfeedlist) {
            try {
                count = count + superfeeddao.insertSuperfeedVtmInfo(superfeed);
            } catch (Exception ex) {
                String message = "维他命产品文件插入失败, SKU= " + superfeed.getSKU() + "  " + ex.getMessage();
                $info(message);
                logger.error(ex.getMessage());
                logIssue("cms 数据导入处理", message);
            }
        }

        return count;
    }

    /**
     * 维他命产品文件读入
     */
    private int vtmSuperFeedImport() {
        $info("维他命产品文件读入开始");

        List<SuperFeedVtmBean> superfeed = new ArrayList<>();

        int count = 0;

        CsvReader reader;
        try {
            String fileName = Feed.getVal1(ChannelConfigEnums.Channel.VITAMIN.getId(), FeedEnums.Name.file_id);
            String filePath = Feed.getVal1(ChannelConfigEnums.Channel.VITAMIN.getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);

            String encode = Feed.getVal1(ChannelConfigEnums.Channel.VITAMIN.getId(), FeedEnums.Name.feed_ftp_file_coding);

            reader = new CsvReader(new FileInputStream(fileFullName), '\t', Charset.forName(encode));

            // Head读入
            reader.readHeaders();
            reader.getHeaders();

            // Body读入
            while (reader.readRecord()) {
                SuperFeedVtmBean superfeedvtmbean = new SuperFeedVtmBean();

                int i = 0;
                superfeedvtmbean.setSKU(reader.get(i++));
                superfeedvtmbean.setUPC(reader.get(i++));
                if(StringUtils.isEmpty(superfeedvtmbean.getUPC())) {
                    continue;
                }
                superfeedvtmbean.setEAN(reader.get(i++));
                superfeedvtmbean.setMPN(reader.get(i++));
                superfeedvtmbean.setDescription(reader.get(i++));
                superfeedvtmbean.setManufacturer(reader.get(i++));
                superfeedvtmbean.setBrand1(reader.get(i++));
                superfeedvtmbean.setShortTitle(reader.get(i++));
                superfeedvtmbean.setProductMargin(reader.get(i++));
                superfeedvtmbean.setBuyItNowPrice(reader.get(i++));
                superfeedvtmbean.setRetailPrice(reader.get(i++));
                superfeedvtmbean.setRelationshipName(reader.get(i++));
                superfeedvtmbean.setVariationParentSKU(reader.get(i++));
                superfeedvtmbean.setClassification(reader.get(i++));
                superfeedvtmbean.setAlternativeGroups(reader.get(i++));
                superfeedvtmbean.setAmazonUSASIN(reader.get(i++));
                superfeedvtmbean.setBaseSize(reader.get(i++));
                superfeedvtmbean.setBrand2(reader.get(i++));
                superfeedvtmbean.setCasinFree(reader.get(i++));
                superfeedvtmbean.setCNMSRP(reader.get(i++));
                superfeedvtmbean.setCNPrice(reader.get(i++));
                superfeedvtmbean.setCOLOUR(reader.get(i++));
                superfeedvtmbean.setCONDITION(reader.get(i++));
                superfeedvtmbean.setCountryofOrigin(reader.get(i++));
                superfeedvtmbean.setCountryISOCode(reader.get(i++));
                superfeedvtmbean.setDairyFree(reader.get(i++));
                superfeedvtmbean.setDosageSize(reader.get(i++));
                superfeedvtmbean.setDosageUnits(reader.get(i++));
                superfeedvtmbean.setDropship(reader.get(i++));
                superfeedvtmbean.setEcoFriendly(reader.get(i++));
                superfeedvtmbean.setEggFree(reader.get(i++));
                superfeedvtmbean.setFullTitle(reader.get(i++));
                superfeedvtmbean.setGenericColor(reader.get(i++));
                superfeedvtmbean.setGenericFlavor(reader.get(i++));
                superfeedvtmbean.setGlutenFree(reader.get(i++));
                superfeedvtmbean.setHTMLMarketingDescription(reader.get(i++));
                superfeedvtmbean.setHypoAllergenic(reader.get(i++));
                superfeedvtmbean.setImageList(reader.get(i++));
                superfeedvtmbean.setIngredientsText(reader.get(i++));
                superfeedvtmbean.setIsItemMAP(reader.get(i++));
                superfeedvtmbean.setItemCategBuyerGroup(reader.get(i++));
                superfeedvtmbean.setItemName(reader.get(i++));
                superfeedvtmbean.setKeywords(reader.get(i++));
                superfeedvtmbean.setKosher(reader.get(i++));
                superfeedvtmbean.setLactoseFree(reader.get(i++));
                superfeedvtmbean.setLowCarb(reader.get(i++));
                superfeedvtmbean.setLowestAvailablePrice(reader.get(i++));
                superfeedvtmbean.setMAPPrice(reader.get(i++));
                superfeedvtmbean.setMarketingDescription(reader.get(i++));
                superfeedvtmbean.setMerchantPrimaryCategory(reader.get(i++));
                superfeedvtmbean.setMSRP(reader.get(i++));
                superfeedvtmbean.setNoAnimalTesting(reader.get(i++));
                superfeedvtmbean.setNormalSellingPrice(reader.get(i++));
                superfeedvtmbean.setNutFree(reader.get(i++));
                superfeedvtmbean.setOrganic(reader.get(i++));
                superfeedvtmbean.setParabenFree(reader.get(i++));
                superfeedvtmbean.setPotencySize(reader.get(i++));
                superfeedvtmbean.setPotencyUnits(reader.get(i++));
                superfeedvtmbean.setPrimaryImage(reader.get(i++));
                superfeedvtmbean.setProductID(reader.get(i++));
                superfeedvtmbean.setProductMarginPercentage(reader.get(i++));
                superfeedvtmbean.setProductRating(reader.get(i++));
                superfeedvtmbean.setProductURL(reader.get(i++));
                superfeedvtmbean.setRestricted(reader.get(i++));
                superfeedvtmbean.setSalePrice(reader.get(i++));
                superfeedvtmbean.setScent(reader.get(i++));
                superfeedvtmbean.setSecondaryCategories(reader.get(i++));
                superfeedvtmbean.setSecondaryImages(reader.get(i++));
                superfeedvtmbean.setServings(reader.get(i++));
                superfeedvtmbean.setShippingSurcharge(reader.get(i++));
                superfeedvtmbean.setSize(reader.get(i++));
                superfeedvtmbean.setSizeUnits(reader.get(i++));
                superfeedvtmbean.setSoyFree(reader.get(i++));
                superfeedvtmbean.setSpecificColor(reader.get(i++));
                superfeedvtmbean.setSpecificFlavor(reader.get(i++));
                superfeedvtmbean.setSugarFree(reader.get(i++));
                superfeedvtmbean.setSuggestedUse(reader.get(i++));
                superfeedvtmbean.setTaxable(reader.get(i++));
                superfeedvtmbean.setVariantID(reader.get(i++));
                superfeedvtmbean.setVegetarian(reader.get(i++));
                superfeedvtmbean.setWarnings(reader.get(i++));
                superfeedvtmbean.setWheatFree(reader.get(i++));
                superfeedvtmbean.setHeight(reader.get(i++));
                superfeedvtmbean.setLength(reader.get(i++));
                superfeedvtmbean.setWidth(reader.get(i++));
                superfeedvtmbean.setSellerCost(reader.get(i++));
                superfeedvtmbean.setWeight(reader.get(i++));
                superfeedvtmbean.setIsParent(reader.get(i++));
                superfeedvtmbean.setVoyageOnePrice(reader.get(i++));
                superfeedvtmbean.setQuantity(reader.get(i++));
                superfeedvtmbean.setVoyageOneMSRP(reader.get(i++));

                superfeed.add(superfeedvtmbean);

                if (superfeed.size() > 100) {
                    count = count + insertSuperFeedVtm(superfeed);
                    superfeed.clear();
                }
            }

            count = count + insertSuperFeedVtm(superfeed);
            reader.close();
            $info("维他命产品文件读入完成");
        } catch (Exception ex) {
            $info("维他命产品文件读入失败");
            logIssue("cms 数据导入处理", "维他命产品文件读入失败 " + ex.getMessage());
        }
        return count;
    }

    private boolean backupFeedFile(String channel_id) {
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feed.getVal1(channel_id, FeedEnums.Name.file_id));
        String filename_backup = Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
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
