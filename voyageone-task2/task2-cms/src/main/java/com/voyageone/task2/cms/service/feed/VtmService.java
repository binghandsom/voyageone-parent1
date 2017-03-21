package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.StringUtils;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.SuperFeedVtmBean;
import com.voyageone.task2.cms.dao.SuperFeed2Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.LUCKY_VITAMIN;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/4
 */
@Service
public class VtmService extends BaseCronTaskService {

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
        // 插入数据库
        $info("维他命产品信息插入开始");
        int count = vtmSuperFeedImport();
        $info("维他命产品信息插入完成 共" + count + "条数据");
        if (count > 0) {
            $info("transform开始");
            transformer.new Context(LUCKY_VITAMIN, this).transform();
            $info("transform结束");
            insertService.new Context(LUCKY_VITAMIN).postNewProduct();
            // 更新完成，UpdateFlag结果：
            // UpdateFlag    cms_zz_worktable_vtm_superfeed
            // 0             已经加入的原有数据(未变化)
            // 1             途中OK待加入的数据(追加和变更的数据)
            // 2             本次导入error的数据
            // 3             本次导入完全导入成功的数据
            backupFeedFile(LUCKY_VITAMIN.getId(), FeedEnums.Name.file_id);
        }
    }

    /**
     * 维他命产品信息插入
     *
     * @return isSuccess
     */
    @Transactional
    public int insertSuperFeedVtm(List<SuperFeedVtmBean> superFeedList) {
        return superfeeddao.insertSuperfeedVtmInfo(superFeedList);
    }

    /**
     * 维他命产品文件读入
     */
    private int vtmSuperFeedImport() throws IOException {
        $info("维他命产品文件读入开始");

        List<SuperFeedVtmBean> superFeed = new ArrayList<>();
        int count = 0;
        String sku = "first";

        $info("维他命产品信息清表开始");
        superfeeddao.deleteTableInfo(Feeds.getVal1(ChannelConfigEnums.Channel.LUCKY_VITAMIN.getId(), FeedEnums.Name.table_id));
        $info("维他命产品信息清表结束");
        BufferedReader br = null;
        try {
            String fileName = Feeds.getVal1(ChannelConfigEnums.Channel.LUCKY_VITAMIN.getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(ChannelConfigEnums.Channel.LUCKY_VITAMIN.getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileFullName)));
            // InputStreamReader 是字节流通向字符流的桥梁,
            // Body读入
            String b;
            br.readLine();
            // 跳过第一行
            while ((b = br.readLine()) != null) {
                List<String> reader = new ArrayList(Arrays.asList(b.split("\t")));
                SuperFeedVtmBean superfeedvtmbean = new SuperFeedVtmBean();
                int i = 0;
                try {
                    superfeedvtmbean.setSKU(reader.get(i++));
                    sku = superfeedvtmbean.getSKU();
                    superfeedvtmbean.setUPC(reader.get(i++));
                    if (StringUtils.isEmpty(superfeedvtmbean.getUPC())) {
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
                    superfeedvtmbean.setTranslationName1(reader.get(i++));
                    superfeedvtmbean.setTranslationValue1(reader.get(i++));
                    superfeedvtmbean.setTranslationValue2(reader.get(i++));
                    superfeedvtmbean.setTranslationName2(reader.get(i++));
                } catch (IndexOutOfBoundsException e) {
                    $info(superfeedvtmbean.getSKU());
                    continue;
                }
                if (isErrData(superfeedvtmbean)) {
                    continue;
                }
                superFeed.add(superfeedvtmbean);
                sku = sku + " -------read over, next. ";
                $info("sku:" + sku);
                if (superFeed.size() > 100) {
                    count = count + insertSuperFeedVtm(superFeed);
                    superFeed.clear();
                }
            }
            if (superFeed.size() > 0) {
                count = count + insertSuperFeedVtm(superFeed);
            }
            $info("维他命产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("维他命产品文件不存在");
        } catch (Exception ex) {
            String message;
            if (superFeed.size() > 0) {
                message = "★★★★★从SKU=" + superFeed.get(0).getSKU() + " 开始未成功导入★★★★★";
            } else {
                message = "★★★★★从SKU=" + sku + " 开始未成功导入★★★★★";
            }
            $info(message);
            $error(ex);
            logIssue("cms 数据导入处理", "维他命产品文件读入失败. " + message + ex.getMessage());
        } finally {
            if (br != null) br.close();
        }
        return count;
    }

    /**
     * 错误数据把UpdateFlag设置成2
     * 备注：response的原因，替代transform的sql文 UPDATE voyageone_cms2.cms_zz_worktable_vtm_superfeed b SET b.UpdateFlag = 2 WHERE b.MerchantPrimaryCategory="" OR b.CNMSRP="" OR b.CNPrice="" OR b.`Image List`="";
     */
    private boolean isErrData(SuperFeedVtmBean superfeedvtmbean) {
        if (StringUtils.isEmpty(superfeedvtmbean.getMerchantPrimaryCategory())
                || StringUtils.isEmpty(superfeedvtmbean.getCNMSRP())
                || StringUtils.isEmpty(superfeedvtmbean.getCNPrice())
                || StringUtils.isEmpty(superfeedvtmbean.getImageList())
                ) {
            return true;
        }

        return false;
    }

    private boolean backupFeedFile(String channel_id, FeedEnums.Name name) {
        $info("备份处理文件开始");

        String filename = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feeds.getVal1(channel_id, name));
        String filename_backup = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + "discontinue" + "_"
                + StringUtils.null2Space(Feeds.getVal1(channel_id, name));
        File file = new File(filename);
        File file_backup = new File(filename_backup);

        if (!file.renameTo(file_backup)) {
            $info("UPC文件备份失败");
        }

        $info("备份处理文件结束");
        return true;
    }
}
