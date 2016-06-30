package com.voyageone.task2.cms.service.feed;

import com.overstock.mp.mpc.externalclient.api.Result;
import com.overstock.mp.mpc.externalclient.model.*;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.overstock.bean.OverstockMultipleRequest;
import com.voyageone.components.overstock.service.OverstockProductService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.SuperFeedOverStockBean;
import com.voyageone.task2.cms.dao.feed.OverStockFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoOverStockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.OverStock;

/**
 * Created by gjl on 2016/6/20.
 */
@Service
public class OverStockAnalysisService extends BaseAnalysisService {
    @Autowired
    OverStockFeedDao overStockFeedDao;
    @Autowired
    private OverstockProductService overstockProductService;

    @Override
    @Transactional
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                overStockFeedDao.delFullBySku(strings);
                overStockFeedDao.insertFullBySku(strings);
                overStockFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        overStockFeedDao.delete();
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        init();

        zzWorkClear();

        $info("产品信息插入开始");
        int cnt = superFeedImport();
        $info("产品信息插入完成 共" + cnt + "条数据");
        if (cnt > 0) {
            transformer.new Context(channel, this).transform();
            postNewProduct();
        }
    }
    /**
     * OverStock产品文件读入
     */
    protected int superFeedImport() {
        $info("OverStock产品api调用开始");
        OverstockMultipleRequest request = new OverstockMultipleRequest();
        int count = 0;
        int offset = 0;
        List<SuperFeedOverStockBean> superfeed = new ArrayList<>();
        while (true) {
            request.setOffset(offset);
            request.setLimit(200);
            String sku ="";
            try {
                Result<ProductsType> result = overstockProductService.queryForMultipleProducts(request);
                $info(JacksonUtil.bean2Json(result));
                int statusCode = result.getStatusCode();
                ProductsType productsType = result.getEntity();
                List<ProductType> productTypeList = productsType.getProduct();
                if (statusCode == 200) {
                    if (productTypeList.size() == 0) {
                        $info("产品取得结束");
                        break;
                    } else {
                        for (ProductType product : productTypeList) {
                            //variations
                            List<VariationType> variationTypeList = product.getVariations().getVariation();
                            if (variationTypeList.size() > 0) {
                                //循环variationTypeList取得对应的属性值
                                for (VariationType variationType : variationTypeList) {
                                    SuperFeedOverStockBean superFeedverStockBean = new SuperFeedOverStockBean();
                                    superFeedverStockBean.setRetailerid(getValue(variationType.getProduct().getRetailerId()));
                                    superFeedverStockBean.setModel(getValue(variationType.getFullSku().split("-")[0]));
                                    superFeedverStockBean.setClientmodel(getValue(product.getSku()));
                                    superFeedverStockBean.setSku(getValue(variationType.getFullSku()));
                                    sku=variationType.getFullSku();
                                    superFeedverStockBean.setClientsku(getValue(variationType.getSku()));
                                    superFeedverStockBean.setTitle(getValue(variationType.getProduct().getTitle()));
                                    superFeedverStockBean.setBrand(getValue(variationType.getProduct().getBrand()));
                                    superFeedverStockBean.setManufacturername(getValue(variationType.getProduct().getManufacturerName()));
                                    superFeedverStockBean.setShortdescription(getValue(variationType.getProduct().getShortDescription()));
                                    superFeedverStockBean.setLongdescription(getValue(variationType.getProduct().getShortDescription()));
                                    superFeedverStockBean.setLeadtime(getValue(String.valueOf(variationType.getProduct().getLeadTime())));
                                    superFeedverStockBean.setAdultcontent(getValue(String.valueOf(variationType.getProduct().isAdultContent())));
                                    superFeedverStockBean.setCountryoforigin(getValue(String.valueOf(variationType.getProduct().getCountryOfOrigin())));
                                    superFeedverStockBean.setProductactivestatus(getValue(String.valueOf(variationType.getProduct().getProductActiveStatus())));
                                    superFeedverStockBean.setShippingsitesale(getValue(String.valueOf(variationType.getProduct().getShippingSiteSale())));
                                    superFeedverStockBean.setDiscountsitesale(getValue(String.valueOf(variationType.getProduct().getDiscountSiteSale())));
                                    superFeedverStockBean.setCondition(getValue(String.valueOf(variationType.getProduct().getCondition())));
                                    superFeedverStockBean.setReturnpolicy(getValue(String.valueOf(variationType.getProduct().getReturnPolicy())));
                                    List<ProductCategoryType> categoryList=product.getCategories().getCategory();
                                    //category
                                    if(categoryList.size()>0){
                                        StringBuilder sb = new StringBuilder();
                                        for(ProductCategoryType categoryType :categoryList){
                                            sb.append(categoryType.getRetailerCategoryName());
                                            if(categoryList.size()!=1){
                                                sb.append("-");
                                            }
                                        }
                                        superFeedverStockBean.setCategory(sb.toString());
                                    }
                                    superFeedverStockBean.setDescription(getValue(variationType.getDescription()));
                                    superFeedverStockBean.setInventoryavailable(getValue(String.valueOf(variationType.getInventoryAvailable())));
                                    if(variationType.getSellingPrice()==null){
                                        superFeedverStockBean.setSellingpriceAmount("");
                                        superFeedverStockBean.setSellingpriceCurrency("");
                                    }else {
                                        superFeedverStockBean.setSellingpriceAmount(String.valueOf(variationType.getSellingPrice().getAmount()));
                                        superFeedverStockBean.setSellingpriceCurrency(String.valueOf(variationType.getSellingPrice().getCurrency()));
                                    }
                                    if(variationType.getMapPrice()==null){
                                        superFeedverStockBean.setMappriceCurrency("");
                                        superFeedverStockBean.setMappriceAmount("");
                                    }else{
                                        superFeedverStockBean.setMappriceCurrency(String.valueOf(variationType.getMapPrice().getCurrency()));
                                        superFeedverStockBean.setMappriceAmount(String.valueOf(variationType.getMapPrice().getAmount()));
                                    }
                                    if(variationType.getMsrpPrice()==null){
                                        superFeedverStockBean.setMsrppriceAmount("");
                                        superFeedverStockBean.setMsrppriceCurrency("");
                                    }else{
                                        superFeedverStockBean.setMsrppriceAmount(getValue(String.valueOf(variationType.getMsrpPrice().getAmount())));
                                        superFeedverStockBean.setMsrppriceCurrency(getValue(String.valueOf(variationType.getMsrpPrice().getCurrency())));
                                    }


                                    superFeedverStockBean.setMsrpexpirationdate(getValue(String.valueOf(variationType.getMsrpExpirationDate())));
                                    if(variationType.getCompareAtPrice()==null){
                                        superFeedverStockBean.setCompareatpriceAmount("");
                                        superFeedverStockBean.setCompareatpriceCurrency("");
                                    }else{
                                        superFeedverStockBean.setCompareatpriceAmount(getValue(String.valueOf(variationType.getCompareAtPrice().getAmount())));
                                        superFeedverStockBean.setCompareatpriceCurrency(getValue(String.valueOf(variationType.getCompareAtPrice().getCurrency())));
                                    }
                                    superFeedverStockBean.setCompareatexpirationdate(getValue(String.valueOf(variationType.getCompareAtExpirationDate())));
                                    if(variationType.getPreviouslyAdvertisedPrice()==null){
                                        superFeedverStockBean.setPreviouslyadvertisedpriceAmount("");
                                        superFeedverStockBean.setPreviouslyadvertisedpriceCurrency("");
                                    }else{
                                        superFeedverStockBean.setPreviouslyadvertisedpriceAmount(String.valueOf(variationType.getPreviouslyAdvertisedPrice().getAmount()));
                                        superFeedverStockBean.setPreviouslyadvertisedpriceCurrency(String.valueOf(variationType.getPreviouslyAdvertisedPrice().getCurrency()));
                                    }
                                    superFeedverStockBean.setShippingwidth(getValue(String.valueOf(variationType.getShippingWidth())));
                                    superFeedverStockBean.setShippingheight(getValue(String.valueOf(variationType.getShippingHeight())));
                                    superFeedverStockBean.setShippinglength(getValue(String.valueOf(variationType.getShippingLength())));
                                    superFeedverStockBean.setShippingweight(getValue(String.valueOf(variationType.getShippingWeight())));
                                    superFeedverStockBean.setUpc(getValue(variationType.getUpc()));
                                    superFeedverStockBean.setShipsvialtl(getValue(String.valueOf(variationType.isShipsViaLtl())));
                                    List<VariationAttributeType>  variationAttributeTypeList =variationType.getAttributes().getAttribute();
                                    //attribute
                                    if(variationAttributeTypeList.size()>0){
                                        StringBuilder sb = new StringBuilder();
                                        for(VariationAttributeType variationAttributeType:variationAttributeTypeList ){

                                            List<String> valueList= variationAttributeType.getValues().getValue();
                                            StringBuilder sbValue = new StringBuilder();
                                            if(valueList.size()>0){
                                                for(String value :valueList){
                                                    sbValue.append(value.replace(" ","")+"-");
                                                }
                                                sbValue.deleteCharAt(sbValue.length()-1);
                                            }
                                            sb.append(variationAttributeType.getName()+":"+sbValue+"|");
                                        }
                                        superFeedverStockBean.setAttribute(sb.deleteCharAt(sb.length() - 1).toString());
                                    }
                                    //SKU_Image
                                    List<ImageType>  imageTypeList =variationType.getImages().getImage();
                                    if(imageTypeList.size()>0){
                                        StringBuilder sb = new StringBuilder();
                                        for(ImageType imageType:imageTypeList ){
                                            sb.append(imageType.getCdnPath()+",");
                                        }
                                        superFeedverStockBean.setImage(sb.deleteCharAt(sb.length() - 1).toString());
                                    }
                                    //model_image
                                    List<ImageType>  model_imageTypeList = product.getImages().getImage();
                                    if(model_imageTypeList.size()>0){
                                        StringBuilder sb = new StringBuilder();
                                        for(ImageType imageType:model_imageTypeList ){
                                            sb.append(imageType.getCdnPath()+",");
                                        }
                                        superFeedverStockBean.setModelImage(sb.deleteCharAt(sb.length() - 1).toString());
                                    }
                                    superFeedverStockBean.setModelRetailerid(getValue(product.getRetailerId()));
                                    superFeedverStockBean.setModelTitle(getValue(product.getTitle()));
                                    superFeedverStockBean.setModelBrand(getValue(product.getBrand()));
                                    superFeedverStockBean.setModelManufacturername(getValue(product.getManufacturerName()));
                                    superFeedverStockBean.setModelShortdescription(getValue(product.getShortDescription()));
                                    superFeedverStockBean.setModelLongdescription(getValue(product.getLongDescription()));
                                    superFeedverStockBean.setModelLeadtime(getValue(String.valueOf(product.getLeadTime())));
                                    superFeedverStockBean.setModelAdultcontent(getValue(String.valueOf(product.isAdultContent())));
                                    superFeedverStockBean.setModelCountryoforigin(getValue(String.valueOf(product.getCountryOfOrigin())));
                                    superFeedverStockBean.setModelProductactivestatus(getValue(String.valueOf(product.getProductActiveStatus())));
                                    superFeedverStockBean.setModelShippingsitesale(getValue(String.valueOf(product.getShippingSiteSale())));
                                    superFeedverStockBean.setModelDiscountsitesale(getValue(String.valueOf(product.getDiscountSiteSale())));
                                    superFeedverStockBean.setModelCondition(getValue(String.valueOf(product.getCondition())));
                                    superFeedverStockBean.setModelReturnpolicy(getValue(String.valueOf(product.getReturnPolicy())));
                                    //按照名称去排序
                                    variationAttributeTypeList.sort((a, b) -> a.getName().compareTo(b.getName()));
                                    if(variationAttributeTypeList.size()>0){
                                        StringBuilder sb = new StringBuilder();
                                        for(VariationAttributeType variationAttributeType:variationAttributeTypeList ){
                                            List<String> valueList= variationAttributeType.getValues().getValue();
                                            StringBuilder sbValue = new StringBuilder();
                                            if(valueList.size()>0){
                                                for(String value :valueList){
                                                    sbValue.append(value.replace(" ","")+"-");
                                                }
                                                sb.append(sbValue);
                                            }
                                        }
                                        superFeedverStockBean.setSalepoint(sb.deleteCharAt(sb.length() - 1).toString());
                                    }
                                    //取得bean
                                    superfeed.add(superFeedverStockBean);
                                    count++;
                                    $info("SKU:"+count+"---"+sku);
                                    if (superfeed.size() > 1000) {
                                        transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
                                        superfeed.clear();
                                    }
                                }
                            }

                        }
                    }
                } else {
                    $info("queryForMultipleProducts error; offset = " + offset + " statusCode = " + statusCode);
                    break;
                }
                offset = offset + 200;
            } catch (Exception e) {
                $info("OverStock产品文件读入失败");
                logIssue("cms 数据导入处理", "OverStock产品文件读入失败 " + e.getMessage());
            }
        }
        if (superfeed.size() >0) {
            transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
            superfeed.clear();
        }
        $info("OverStock产品api调用结束");
        $info("OverStock产品个数为:" + count);
        return count;
    }

    /**
     * 空值转换
     * @param value
     * @return value
     */
    public String getValue(String value){
        if(value==null||value.equals("null")){
            value="";
        }
        return value;
    }
    /**
     * OverStock产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedOverStockBean> superfeedlist) {

        for (SuperFeedOverStockBean superfeed : superfeedlist) {

            if (overStockFeedDao.insertSelective(superfeed) <= 0) {
                $info("OverStock产品信息插入失败 InventoryNumber = " + "11111");
            }
        }
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return OverStock;
    }

    /**
     * 生成类目数据包含model product数据
     */
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath) {

        Map colums = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = new ArrayList<>();
        for (FeedBean feedConfig : feedBeans) {
            if (!StringUtil.isEmpty(feedConfig.getCfg_val1())) {
                attList.add(feedConfig.getCfg_val1());
            }
        }

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, colums.get("category").toString(),
                categorPath.replace("'", "\\\'"));

        colums.put("keyword", where);
        colums.put("tableName", table);
        if (attList.size() > 0) {
            colums.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoOverStockModel> vtmModelBeans = overStockFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoOverStockModel vtmModelBean : vtmModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);

            if (codeMap.containsKey(cmsBtFeedInfoModel.getCode())) {
                CmsBtFeedInfoModel beforeFeed = codeMap.get(cmsBtFeedInfoModel.getCode());
                beforeFeed.getSkus().addAll(cmsBtFeedInfoModel.getSkus());
                beforeFeed.getImage().addAll(cmsBtFeedInfoModel.getImage());
                beforeFeed.setImage(beforeFeed.getImage().stream().distinct().collect(Collectors.toList()));
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
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsOverStockAnalysisJob";
    }
}
