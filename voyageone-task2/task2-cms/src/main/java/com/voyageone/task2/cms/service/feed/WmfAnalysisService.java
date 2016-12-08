package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.SuperFeedWmfBean;
import com.voyageone.task2.cms.dao.feed.WmfFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoWmfModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.WMF;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/***/
@Service
public class WmfAnalysisService extends BaseAnalysisService {
    @Autowired
    WmfFeedDao wmfFeedDao;

    private static String urlKey = "http://www.wmf.com/en/";
    private static String mediaImage="https://www.wmf.com/media/catalog/product";
    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                wmfFeedDao.delFullBySku(strings);
                wmfFeedDao.insertFullBySku(strings);
                wmfFeedDao.updateFlagBySku(strings);
            });
        }
    }

    @Override
    protected void zzWorkClear() {
        wmfFeedDao.delete();
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        init();
        zzWorkClear();
        int cnt;
        if ("1".equalsIgnoreCase(TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.feed_full_copy_temp))) {
            cnt = fullCopyTemp();
        } else {
            $info("产品信息插入开始");
            cnt = superFeedImport();
        }
        $info("产品信息插入完成 共" + cnt + "条数据");
        if (cnt > 0) {
            if (!"1".equalsIgnoreCase(TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.feed_full_copy_temp))) {
                transformer.new Context(channel, this).transform();
            }
            postNewProduct();
        }
    }

    @Override
    protected int superFeedImport() {
        $info("WMF产品文件读入开始");
        List<SuperFeedWmfBean> superFeed = new ArrayList<>();
        int cnt = 0;
        //CsvReader reader;
        CsvReader reader;
        try {
            String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);
            String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);
            reader = new CsvReader(new FileInputStream(fileFullName), ',', Charset.forName(encode));
            // Body读入
            while (reader.readRecord()) {
                if(reader.getCurrentRecord()<3)continue;
                SuperFeedWmfBean wmfBean = new SuperFeedWmfBean();
                int i = 0;
                wmfBean.setEntityId(reader.get(i++));
                wmfBean.setSku(reader.get(i++));
                wmfBean.setStore(reader.get(i++));
                wmfBean.setType(reader.get(i++));
                wmfBean.setAttributeSet(reader.get(i++));
                wmfBean.setCategory(reader.get(i++));
                if(!StringUtil.isEmpty(wmfBean.getCategory())){
                    String category = wmfBean.getCategory().split("\\|")[0];
                    wmfBean.setCategory(category);
                }else {
                    wmfBean.setCategory("Root Catalog > Default Category >");
                }
                wmfBean.setCategoryValue(wmfBean.getCategory());
                wmfBean.setStatus(reader.get(i++));
                if("1".equals(wmfBean.getStatus())&&"4".equals(wmfBean.getVisibility()))continue;
                wmfBean.setVisibility(reader.get(i++));
                wmfBean.setTaxClassId(reader.get(i++));
                wmfBean.setImage(reader.get(i++));
                wmfBean.setSmallImage(reader.get(i++));
                wmfBean.setThumbnail(reader.get(i++));
                wmfBean.setMediaImage(reader.get(i++));
                wmfBean.setMediaLable(reader.get(i++));
                wmfBean.setMediaPosition(reader.get(i++));
                wmfBean.setMediaIsDisabled(reader.get(i++));
                wmfBean.setName(reader.get(i++));
                wmfBean.setMarke(reader.get(i++));
                wmfBean.setPrice(reader.get(i++));
                wmfBean.setSapStatus(reader.get(i++));
                wmfBean.setMetaTitle(reader.get(i++));
                wmfBean.setDescription(reader.get(i++));
                wmfBean.setProduktart(reader.get(i++));
                wmfBean.setMetaKeyword(reader.get(i++));
                wmfBean.setShortDescription(reader.get(i++));
                wmfBean.setProduktart2(reader.get(i++));
                wmfBean.setSpecialPrice(reader.get(i++));
                wmfBean.setMetaDescription(reader.get(i++));
                wmfBean.setKollektion(reader.get(i++));
                wmfBean.setSpecialFromDate(reader.get(i++));
                wmfBean.setPlShortText(reader.get(i++));
                wmfBean.setArtikelanzahl(reader.get(i++));
                wmfBean.setSpecialToDate(reader.get(i++));
                wmfBean.setLieferumfang(reader.get(i++));
                wmfBean.setEan(reader.get(i++));
                wmfBean.setMaterial(reader.get(i++));
                wmfBean.setUrlKey(reader.get(i++));
                wmfBean.setUrlKey(urlKey+wmfBean.getUrlKey());
                wmfBean.setMaterialeigenschaft(reader.get(i++));
                wmfBean.setNebenmaterial(reader.get(i++));
                wmfBean.setProdukteigenschaft(reader.get(i++));
                wmfBean.setMadeInGermany(reader.get(i++));
                wmfBean.setInduktionseignung(reader.get(i++));
                wmfBean.setPlImageOrientation(reader.get(i++));
                wmfBean.setLieferlandAusschluss(reader.get(i++));
                wmfBean.setHerdart(reader.get(i++));
                wmfBean.setSimpleMsrp(reader.get(i++));
                wmfBean.setCountryOfManufacture(reader.get(i++));
                wmfBean.setTemperatureignung(reader.get(i++));
                wmfBean.setSimpleMsrpFromDate(reader.get(i++));
                wmfBean.setHitzebestaendigkeit(reader.get(i++));
                wmfBean.setSimpleMsrpToDate(reader.get(i++));
                wmfBean.setHighlight1(reader.get(i++));
                wmfBean.setDeckelart(reader.get(i++));
                wmfBean.setHighlight2(reader.get(i++));
                wmfBean.setMesserart(reader.get(i++));
                wmfBean.setHighlight3(reader.get(i++));
                wmfBean.setEignungAnzTassenEier(reader.get(i++));
                wmfBean.setHighlight4(reader.get(i++));
                wmfBean.setMasseLaengeInCm(reader.get(i++));
                wmfBean.setHighlight5(reader.get(i++));
                wmfBean.setMasseBreiteInCm(reader.get(i++));
                wmfBean.setAsin(reader.get(i++));
                wmfBean.setMasseHoeheInCm(reader.get(i++));
                wmfBean.setMasseKlingenlaengeInCm(reader.get(i++));
                wmfBean.setMasseDmInCm(reader.get(i++));
                wmfBean.setMasseDmHerdplattengr(reader.get(i++));
                wmfBean.setMasseFassvermInL(reader.get(i++));
                wmfBean.setMasseFuellmengeInG(reader.get(i++));
                wmfBean.setMasseNettogewichtInG(reader.get(i++));
                wmfBean.setMasseBruttogewichtInG(reader.get(i++));
                wmfBean.setUmdrehungenProMinute(reader.get(i++));
                wmfBean.setLeistungInW(reader.get(i++));
                wmfBean.setLeistungInV(reader.get(i++));
                wmfBean.setLeistungInHz(reader.get(i++));
                wmfBean.setFarbbezeichnungDerKollektion(reader.get(i++));
                wmfBean.setFarbe(reader.get(i++));
                wmfBean.setGarantie(reader.get(i++));
                wmfBean.setPflege(reader.get(i++));
                wmfBean.setAltersgruppe(reader.get(i++));
                wmfBean.setDesigner(reader.get(i++));
                wmfBean.setThemenMotive(reader.get(i++));
                wmfBean.setDesignpreis(reader.get(i++));
                wmfBean.setTestauszeichnung(reader.get(i++));
                wmfBean.setKollektionstext(reader.get(i++));
                wmfBean.setVariantenzugehoerigkeit(reader.get(i++));
                wmfBean.setMasseVolumenInL(reader.get(i++));
                wmfBean.setMasseUmdrehungenInUMin(reader.get(i++));
                wmfBean.setMasseUmdrehungenInKmH(reader.get(i++));
                wmfBean.setMasseLeistungInW(reader.get(i++));
                wmfBean.setMasseLeistungInV(reader.get(i++));
                wmfBean.setMasseLeistungInHz(reader.get(i++));
                wmfBean.setVerpackungHoeheInCm(reader.get(i++));
                wmfBean.setVerpackungBreiteInCm(reader.get(i++));
                wmfBean.setVerpackungTiefeInCm(reader.get(i++));
                wmfBean.setNameTestauszeichnung(reader.get(i++));
                wmfBean.setInduktionsfaehigkeit(reader.get(i++));
                wmfBean.setTemperaturbestaendigkeitKorpu(reader.get(i++));
                wmfBean.setTemperaturbestaendigkeitDecke(reader.get(i++));
                wmfBean.setHitzebestaendigBis(reader.get(i++));
                wmfBean.setHerstellungshinweis(reader.get(i++));
                wmfBean.setAnzahlTeileDp(reader.get(i++));
                wmfBean.setMasseDurchmesserInCmDp(reader.get(i++));
                wmfBean.setMasseFassungsvermoegenLDp(reader.get(i++));
                wmfBean.setVideo1(reader.get(i++));
                wmfBean.setVideo2(reader.get(i++));
                wmfBean.setVideo3(reader.get(i++));
                wmfBean.setVideo4(reader.get(i++));
                StringBuffer sb = new StringBuffer();
                if(!StringUtil.isEmpty(wmfBean.getMasseLaengeInCm())){
                    sb.append("Length:").append(wmfBean.getMasseLaengeInCm()).append("-");
                }
                if(!StringUtil.isEmpty(wmfBean.getMasseBreiteInCm())){
                    sb.append("Width:").append(wmfBean.getMasseBreiteInCm()).append("-");
                }
                if(!StringUtil.isEmpty(wmfBean.getMasseHoeheInCm())){
                    sb.append("Height:").append(wmfBean.getMasseHoeheInCm()).append("-");
                }
                if(!StringUtil.isEmpty(wmfBean.getMasseDmInCm())){
                    sb.append("Diameter:").append(wmfBean.getMasseDmInCm());
                }
                if(sb.length()==0){
                    wmfBean.setItemISize("OneSize");
                }else{
                    wmfBean.setItemISize(sb.toString());
                }
                superFeed.add(wmfBean);
                $info(wmfBean.getSku());
                cnt++;
                if (superFeed.size() > 1000) {
                    transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                    superFeed.clear();
                }
            }
            if (superFeed.size() > 0) {
                transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                superFeed.clear();
            }
            $info("WMF产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("WMF产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("WMF产品文件读入失败");
            logIssue("cms 数据导入处理", "WMF产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }

    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedWmfBean> superFeedList) {
        superFeedList.stream()
                .filter(superFeed -> wmfFeedDao.insertSelective(superFeed) <= 0)
                .forEach(superFeed -> $info("WMF产品信息插入失败 sku = " + superFeed.getSku()));
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return WMF;
    }

    @Override
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath) {
        Map<String, Object> column = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = feedBeans.stream()
                .filter(feedConfig -> !StringUtil.isEmpty(feedConfig.getCfg_val1()))
                .map(FeedBean::getCfg_val1).collect(Collectors.toList());
        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, column.get("category").toString(),
                categorPath.replace("'", "\\\'"));

        column.put("keyword", where);
        column.put("tableName", table);
        if (attList.size() > 0) {
            column.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoWmfModel> vtmModelBeans = wmfFeedDao.selectSuperFeedModel(column);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoWmfModel vtmModelBean : vtmModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add(String.valueOf(temp.get(key)));
                attribute.put(key, values);
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);
            //取得图片开始
            List<String> imagesList = new ArrayList<>();
            if(!StringUtil.isEmpty(vtmModelBean.getMediaImage())){
                String images[] =vtmModelBean.getMediaImage().split("\\|");
                for(String img:images){
                    imagesList.add(mediaImage+img);
                }
                cmsBtFeedInfoModel.setImage(imagesList);
            }

            //设置重量开始
            List<CmsBtFeedInfoModel_Sku> skus = vtmModelBean.getSkus();
            for (CmsBtFeedInfoModel_Sku sku : skus) {
                String Weight = sku.getWeightOrg().trim();
                Pattern pattern = Pattern.compile("[^0-9.]");
                Matcher matcher = pattern.matcher(Weight);
                if (matcher.find()) {
                    int index = Weight.indexOf(matcher.group());
                    if (index != -1) {
                        String weightOrg = Weight.substring(0, index);
                        sku.setWeightOrg(weightOrg);
                    }
                }
                sku.setWeightOrgUnit(sku.getWeightOrgUnit());
            }
            cmsBtFeedInfoModel.setSkus(skus);
            //设置重量结束
            if (codeMap.containsKey(cmsBtFeedInfoModel.getCode())) {
                CmsBtFeedInfoModel beforeFeed = codeMap.get(cmsBtFeedInfoModel.getCode());
                beforeFeed.getSkus().addAll(cmsBtFeedInfoModel.getSkus());
                //sku取得图片
                beforeFeed.getImage().addAll(cmsBtFeedInfoModel.getImage());
                beforeFeed.setAttribute(attributeMerge(beforeFeed.getAttribute(), cmsBtFeedInfoModel.getAttribute()));
            } else {
                modelBeans.add(cmsBtFeedInfoModel);
                codeMap.put(cmsBtFeedInfoModel.getCode(), cmsBtFeedInfoModel);
            }
        }
        $info("取得 [ %s ] 的 Product 数 %s", categorPath, modelBeans.size());

        return modelBeans;
    }

    @Override
    protected String getTaskName() {
        return "CmsWmfAnalysisJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
