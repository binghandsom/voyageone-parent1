package com.voyageone.task2.cms.service.feed;

import com.overstock.mp.mpc.externalclient.api.ErrorDetails;
import com.overstock.mp.mpc.externalclient.api.Result;
import com.overstock.mp.mpc.externalclient.model.*;
import com.voyageone.common.components.issueLog.enums.*;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.*;
import com.voyageone.components.overstock.bean.OverstockMultipleRequest;
import com.voyageone.components.overstock.bean.product.OverstockProductOneQueryRequest;
import com.voyageone.components.overstock.service.OverstockEventService;
import com.voyageone.components.overstock.service.OverstockProductService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.SuperFeedOverStockBean;
import com.voyageone.task2.cms.dao.feed.OverStockFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoOverStockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.OverStock;
import static com.voyageone.ims.enums.CmsFieldEnum.CmsSkuEnum.sku;

/**
 * Created by gjl on 2016/6/20.
 */
@Service
public class OverStockAnalysisService extends BaseAnalysisService {
    @Autowired
    OverStockFeedDao overStockFeedDao;
    @Autowired
    private OverstockProductService overstockProductService;

    @Autowired
    private OverstockEventService overstockEventService;

    private Integer pageIndex = 0;

    private long lastExecuteTime = 0;



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
//    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
//
//        init();
//
//        zzWorkClear();
//        int cnt = 0;
//        if("1".equalsIgnoreCase(TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.feed_full_copy_temp))){
//            cnt = fullCopyTemp();
//        }else {
//            $info("产品信息插入开始");
//            cnt = superFeedImport();
//        }
//        $info("产品信息插入完成 共" + cnt + "条数据");
//        if (cnt > 0) {
//            if(!"1".equalsIgnoreCase(TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.feed_full_copy_temp))) {
//                transformer.new Context(channel, this).transform();
//            }
//            postNewProduct();
//        }
//    }
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        init();

        if(CacheHelper.getValueOperation().get("OverStockfeedPage") != null){
            pageIndex = (Integer) CacheHelper.getValueOperation().get("OverStockfeedPage");
        }

        if(CacheHelper.getValueOperation().get("OverStockFeedSynTime") != null){
            lastExecuteTime = (Long) CacheHelper.getValueOperation().get("OverStockFeedSynTime");
        }

        if ((Calendar.getInstance().getTimeInMillis() - lastExecuteTime) > (24 * 3 * 60 * 60 * 1000L)) {
            int sumCnt = 0;
            int cnt = 0;
            do {
                cnt = getEventProduct();
                sumCnt+=cnt;
            }while (cnt > 0);
            $info("产品信息插入开始");
//            superFeedImport();
        }else{
            $info("间隔时间未定不许要执行");
        }

    }
    @Override
    public int fullCopyTemp(){
        int cnt = overStockFeedDao.fullCopyTemp();
//        overStockFeedDao.updateMd5();
        overStockFeedDao.updateUpdateFlag();
        return cnt;
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
            CacheHelper.getValueOperation().set("OverStockfeedPage", pageIndex);
            $info("取得第"+(pageIndex+1)+"页的数据");
            request.setOffset((pageIndex) * 100);
            request.setLimit(100);
            String sku = "";
            try {
                pageIndex++;
                Result<ProductsType> result;
                try {
                    result = overstockProductService.queryForMultipleProducts(request);
                }catch (Exception e){
                    $error(e);
                    continue;
                }
                int statusCode = result.getStatusCode();
                if (statusCode == 200) {
                    ProductsType productsType = result.getEntity();
                    List<ProductType> productTypeList = productsType.getProduct();
                    if (productTypeList.size() == 0) {
                        $info("产品取得结束");
                        CacheHelper.getValueOperation().set("OverStockfeedPage", 0);
                        CacheHelper.getValueOperation().set("OverStockFeedSynTime", Calendar.getInstance().getTimeInMillis());
                        break;
                    } else {
                        zzWorkClear();
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
                                    sku = variationType.getFullSku();
                                    superFeedverStockBean.setClientsku(getValue(variationType.getSku()));
                                    superFeedverStockBean.setTitle(getValue(variationType.getProduct().getTitle()));
                                    superFeedverStockBean.setBrand(getValue(variationType.getProduct().getBrand()));
                                    superFeedverStockBean.setManufacturername(getValue(variationType.getProduct().getManufacturerName()));
                                    superFeedverStockBean.setShortdescription(getValue(variationType.getProduct().getShortDescription()));
                                    superFeedverStockBean.setLongdescription(getValue(variationType.getProduct().getLongDescription()));
                                    superFeedverStockBean.setLeadtime(getValue(String.valueOf(variationType.getProduct().getLeadTime())));
                                    superFeedverStockBean.setAdultcontent(getValue(String.valueOf(variationType.getProduct().isAdultContent())));
                                    superFeedverStockBean.setCountryoforigin(getValue(String.valueOf(variationType.getProduct().getCountryOfOrigin())));
                                    superFeedverStockBean.setProductactivestatus(getValue(String.valueOf(variationType.getProduct().getProductActiveStatus())));
                                    superFeedverStockBean.setShippingsitesale(getValue(String.valueOf(variationType.getProduct().getShippingSiteSale())));
                                    superFeedverStockBean.setDiscountsitesale(getValue(String.valueOf(variationType.getProduct().getDiscountSiteSale())));
                                    superFeedverStockBean.setCondition(getValue(String.valueOf(variationType.getProduct().getCondition())));
                                    superFeedverStockBean.setReturnpolicy(getValue(String.valueOf(variationType.getProduct().getReturnPolicy())));
                                    List<ProductCategoryType> categoryList = product.getCategories().getCategory();
                                    //category
                                    if (categoryList.size() > 0) {
                                        StringBuilder sb = new StringBuilder();
                                        for (ProductCategoryType categoryType : categoryList) {
                                            sb.append(categoryType.getRetailerCategoryName());
                                            if (categoryList.size() != 1) {
                                                sb.append("-");
                                            }
                                        }
                                        superFeedverStockBean.setCategory(sb.toString());
                                    }
                                    superFeedverStockBean.setDescription(getValue(variationType.getDescription()));
                                    superFeedverStockBean.setInventoryavailable(getValue(String.valueOf(variationType.getInventoryAvailable())));
                                    if (variationType.getSellingPrice() == null) {
                                        superFeedverStockBean.setSellingpriceAmount("");
                                        superFeedverStockBean.setSellingpriceCurrency("");
                                    } else {
                                        superFeedverStockBean.setSellingpriceAmount(String.valueOf(variationType.getSellingPrice().getAmount()));
                                        superFeedverStockBean.setSellingpriceCurrency(String.valueOf(variationType.getSellingPrice().getCurrency()));
                                    }
                                    if (variationType.getMapPrice() == null) {
                                        superFeedverStockBean.setMappriceCurrency("");
                                        superFeedverStockBean.setMappriceAmount("");
                                    } else {
                                        superFeedverStockBean.setMappriceCurrency(String.valueOf(variationType.getMapPrice().getCurrency()));
                                        superFeedverStockBean.setMappriceAmount(String.valueOf(variationType.getMapPrice().getAmount()));
                                    }
                                    if (variationType.getMsrpPrice() == null) {
                                        superFeedverStockBean.setMsrppriceAmount(String.valueOf(variationType.getSellingPrice().getAmount()));
                                        superFeedverStockBean.setMsrppriceCurrency(String.valueOf(variationType.getSellingPrice().getCurrency()));
                                    } else {
                                        superFeedverStockBean.setMsrppriceAmount(getValue(String.valueOf(variationType.getMsrpPrice().getAmount())));
                                        superFeedverStockBean.setMsrppriceCurrency(getValue(String.valueOf(variationType.getMsrpPrice().getCurrency())));
                                    }


                                    superFeedverStockBean.setMsrpexpirationdate(getValue(String.valueOf(variationType.getMsrpExpirationDate())));
                                    if (variationType.getCompareAtPrice() == null) {
                                        superFeedverStockBean.setCompareatpriceAmount("");
                                        superFeedverStockBean.setCompareatpriceCurrency("");
                                    } else {
                                        superFeedverStockBean.setCompareatpriceAmount(getValue(String.valueOf(variationType.getCompareAtPrice().getAmount())));
                                        superFeedverStockBean.setCompareatpriceCurrency(getValue(String.valueOf(variationType.getCompareAtPrice().getCurrency())));
                                    }
                                    superFeedverStockBean.setCompareatexpirationdate(getValue(String.valueOf(variationType.getCompareAtExpirationDate())));
                                    if (variationType.getPreviouslyAdvertisedPrice() == null) {
                                        superFeedverStockBean.setPreviouslyadvertisedpriceAmount("");
                                        superFeedverStockBean.setPreviouslyadvertisedpriceCurrency("");
                                    } else {
                                        superFeedverStockBean.setPreviouslyadvertisedpriceAmount(String.valueOf(variationType.getPreviouslyAdvertisedPrice().getAmount()));
                                        superFeedverStockBean.setPreviouslyadvertisedpriceCurrency(String.valueOf(variationType.getPreviouslyAdvertisedPrice().getCurrency()));
                                    }
                                    superFeedverStockBean.setShippingwidth(getValue(String.valueOf(variationType.getShippingWidth())));
                                    superFeedverStockBean.setShippingheight(getValue(String.valueOf(variationType.getShippingHeight())));
                                    superFeedverStockBean.setShippinglength(getValue(String.valueOf(variationType.getShippingLength())));
                                    superFeedverStockBean.setShippingweight(getValue(String.valueOf(variationType.getShippingWeight())));
                                    superFeedverStockBean.setUpc(getValue(variationType.getUpc()));
                                    superFeedverStockBean.setShipsvialtl(getValue(String.valueOf(variationType.isShipsViaLtl())));
                                    List<VariationAttributeType> variationAttributeTypeList = variationType.getAttributes().getAttribute();
                                    //attribute
                                    if (variationAttributeTypeList.size() > 0) {
                                        StringBuilder sb = new StringBuilder();
                                        for (VariationAttributeType variationAttributeType : variationAttributeTypeList) {

                                            List<String> valueList = variationAttributeType.getValues().getValue();
                                            StringBuilder sbValue = new StringBuilder();
                                            if (valueList.size() > 0) {
                                                for (String value : valueList) {
                                                    sbValue.append(value.replace(" ", "") + "-");
                                                }
                                                sbValue.deleteCharAt(sbValue.length() - 1);
                                            }
                                            sb.append(variationAttributeType.getName() + ":" + sbValue + "|");
                                        }
                                        superFeedverStockBean.setAttribute1(sb.deleteCharAt(sb.length() - 1).toString());
                                    }
                                    //attributeColor attributeMetal attributeSize
                                    if (variationAttributeTypeList.size() > 0) {
                                        //按照名称去排序
                                        variationAttributeTypeList.sort((a, b) -> a.getName().compareTo(b.getName()));
                                        //attributeColor
                                        StringBuilder sbColorValue = new StringBuilder();
                                        //attributeMetal
                                        StringBuilder sbMetalValue = new StringBuilder();
                                        //attributeSize
                                        StringBuilder sbSizeValue = new StringBuilder();
                                        for (VariationAttributeType variationAttributeType : variationAttributeTypeList) {
                                            List<String> valueList = variationAttributeType.getValues().getValue();
                                            String name = variationAttributeType.getName();
                                            //attributeColor
                                            if (name.contains("颜色") || name.contains("Color")) {
                                                if (valueList.size() > 0) {
                                                    for (String value : valueList) {
                                                        if (value.toString().equals("N/A")) {
                                                            continue;
                                                        }
                                                        sbColorValue.append(value.replace(" ", "") + "-");
                                                    }
                                                }
                                            }
                                            //attributeMetal
                                            if (name.equals("金属") || name.equals("Metal")) {
                                                if (valueList.size() > 0) {
                                                    for (String value : valueList) {
                                                        if (value.toString().equals("N/A")) {
                                                            continue;
                                                        }
                                                        sbMetalValue.append(value.replace(" ", "") + "-");
                                                    }

                                                }
                                            }
                                            //attributeSize
                                            if (name.contains("尺寸") || name.contains("Size")) {

                                                if (valueList.size() > 0) {
                                                    for (String value : valueList) {
                                                        if (value.toString().equals("N/A")) {
                                                            continue;
                                                        }
                                                        sbSizeValue.append(value.replace(" ", "") + "-");
                                                    }
                                                }
                                            }
                                        }
                                        //attributeColor
                                        if ("".equals(String.valueOf(sbColorValue))) {
                                            superFeedverStockBean.setAttributeColor("");
                                        } else {
                                            superFeedverStockBean.setAttributeColor(String.valueOf(sbColorValue.deleteCharAt(sbColorValue.length() - 1)));
                                        }
                                        //attributeMetal
                                        if ("".equals(String.valueOf(sbMetalValue))) {
                                            superFeedverStockBean.setAttributeMetal("");
                                        } else {
                                            superFeedverStockBean.setAttributeMetal(String.valueOf(sbMetalValue.deleteCharAt(sbMetalValue.length() - 1)));
                                        }
                                        //attributeSize
                                        if ("".equals(String.valueOf(sbSizeValue))) {
                                            superFeedverStockBean.setAttributeSize("");
                                        } else {
                                            superFeedverStockBean.setAttributeSize(String.valueOf(sbSizeValue.deleteCharAt(sbSizeValue.length() - 1)));
                                        }
                                    }
                                    //model_image
                                    List<ImageType> model_imageTypeList = product.getImages().getImage();
                                    if (model_imageTypeList.size() > 0) {
                                        StringBuilder sb = new StringBuilder();
                                        for (ImageType imageType : model_imageTypeList) {
                                            sb.append(imageType.getCdnPath() + ",");
                                        }
                                        superFeedverStockBean.setModelImage(sb.deleteCharAt(sb.length() - 1).toString());
                                    }
                                    //SKU_Image
                                    List<ImageType> imageTypeList = variationType.getImages().getImage();
                                    if (imageTypeList.size() > 0) {
                                        StringBuilder sb = new StringBuilder();
                                        for (ImageType imageType : imageTypeList) {
                                            sb.append(imageType.getCdnPath() + ",");
                                        }
                                        if(!StringUtils.isEmpty(superFeedverStockBean.getModelImage())){
                                            sb.append(superFeedverStockBean.getModelImage());
                                        }
                                        superFeedverStockBean.setImage(sb.toString());
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
                                    if (variationAttributeTypeList.size() > 0) {
                                        //按照名称去排序
                                        variationAttributeTypeList.sort((a, b) -> a.getName().compareTo(b.getName()));
                                        StringBuilder sb = new StringBuilder();
                                        for (VariationAttributeType variationAttributeType : variationAttributeTypeList) {
                                            List<String> valueList = variationAttributeType.getValues().getValue();
                                            StringBuilder sbValue = new StringBuilder();
                                            String name = variationAttributeType.getName();
                                            //attributeSize
                                            if (name.contains("尺寸") || name.contains("Size")) {
                                                continue;
                                            }
                                            if (valueList.size() > 0) {
                                                for (String value : valueList) {
                                                    if (value.toString().equals("N/A")) {
                                                        continue;
                                                    }
                                                    sbValue.append(value.replace(" ", "") + "-");
                                                }

                                                sb.append(sbValue);
                                            }
                                        }
                                        if ("".equals(String.valueOf(sb))) {
                                            superFeedverStockBean.setSalepoint("");
                                        } else {
                                            superFeedverStockBean.setSalepoint(String.valueOf(sb.deleteCharAt(sb.length() - 1)));
                                        }
                                    }
                                    //取得bean
                                    superfeed.add(superFeedverStockBean);
                                    count++;
                                    $info("SKU:" + count + "---" + sku);
//                                    if (superfeed.size() > 1000) {
//                                        transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
//                                        superfeed.clear();
//                                    }
                                }
                            }
                        }
                        transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
                        superfeed.clear();
                        transformer.new Context(channel, this).transform();
                        postNewProduct();

                    }
                } else {
                    $info("queryForMultipleProducts error; offset = " + offset + " statusCode = " + statusCode);
                    break;
                }
//                offset = offset + 100;
            } catch (Exception e) {
                $error("OverStock产品文件读入失败", e);
//                $info("OverStock产品文件读入失败"  + e.getMessage());
                logIssue("cms 数据导入处理", "OverStock产品文件读入失败 " + e.getMessage());
                break;
            }
        }
//        if (superfeed.size() > 0) {
//            transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
//            superfeed.clear();
//        }
        $info("OverStock产品api调用结束");
        $info("OverStock产品个数为:" + count);
        return count;
    }


    public Integer getEventProduct() {

        try {
            Result<EventsType> result = overstockEventService.queryingForNewVariation();
            int statusCode = result.getStatusCode();
            ErrorDetails errMsg = result.getErrorDetails();
            EventsType eventsType = result.getEntity();
            int count = 0;

            List<EventType> eventTypeList = eventsType.getEvent();

            // 返回正常的场合
            if (statusCode == 200) {
                if (eventTypeList.size() == 0) {
                    $info("价格没有变化");
                } else {
                    zzWorkClear();
                    List<EventType> eventTypeListPara = new ArrayList<EventType>();
                    for (int i = 0; i < eventTypeList.size(); i++) {
                        String sku = "";
                        // 仅有Event Id 信息
                        EventType eventType = eventTypeList.get(i);

                        $info("OverStock queryingForEventDetail event id =" + eventType.getId());
                        // Event 详细信息取得
                        Result<EventType> eventTypeDetail = overstockEventService.queryingForEventDetail(String.valueOf(eventType.getId()));
                        int statusCodeSub = eventTypeDetail.getStatusCode();
                        if(statusCodeSub != 200) continue;
                        ErrorDetails errMsgSub = eventTypeDetail.getErrorDetails();
                        EventType event = eventTypeDetail.getEntity();

                        OverstockProductOneQueryRequest overstockProductOneQueryRequest = new OverstockProductOneQueryRequest();
                        overstockProductOneQueryRequest.setProductId(event.getVariation().getProduct().getId());
                        Result<ProductType> eventProduct = overstockProductService.queryForOneProduct(overstockProductOneQueryRequest);
                        if(eventProduct.getStatusCode() == 200){
                            List<SuperFeedOverStockBean> superfeed = importZZWork(eventProduct.getEntity());
                            insertSuperFeed(superfeed);
                            eventTypeListPara.add(event);
                        }
                    }
                    transformer.new Context(channel, this).transform();
                    postNewProduct();
                    overstockEventService.updateHandledEvents(eventTypeListPara);
                }
                return eventTypeList.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private List<SuperFeedOverStockBean> importZZWork(ProductType product){
        List<VariationType> variationTypeList = product.getVariations().getVariation();
        List<SuperFeedOverStockBean> superfeed = new ArrayList<>();
        if (variationTypeList.size() > 0) {
            //循环variationTypeList取得对应的属性值
            for (VariationType variationType : variationTypeList) {
                SuperFeedOverStockBean superFeedverStockBean = new SuperFeedOverStockBean();
                superFeedverStockBean.setRetailerid(getValue(variationType.getProduct().getRetailerId()));
                superFeedverStockBean.setModel(getValue(variationType.getFullSku().split("-")[0]));
                superFeedverStockBean.setClientmodel(getValue(product.getSku()));
                superFeedverStockBean.setSku(getValue(variationType.getFullSku()));
                superFeedverStockBean.setClientsku(getValue(variationType.getSku()));
                superFeedverStockBean.setTitle(getValue(variationType.getProduct().getTitle()));
                superFeedverStockBean.setBrand(getValue(variationType.getProduct().getBrand()));
                superFeedverStockBean.setManufacturername(getValue(variationType.getProduct().getManufacturerName()));
                superFeedverStockBean.setShortdescription(getValue(variationType.getProduct().getShortDescription()));
                superFeedverStockBean.setLongdescription(getValue(variationType.getProduct().getLongDescription()));
                superFeedverStockBean.setLeadtime(getValue(String.valueOf(variationType.getProduct().getLeadTime())));
                superFeedverStockBean.setAdultcontent(getValue(String.valueOf(variationType.getProduct().isAdultContent())));
                superFeedverStockBean.setCountryoforigin(getValue(String.valueOf(variationType.getProduct().getCountryOfOrigin())));
                superFeedverStockBean.setProductactivestatus(getValue(String.valueOf(variationType.getProduct().getProductActiveStatus())));
                superFeedverStockBean.setShippingsitesale(getValue(String.valueOf(variationType.getProduct().getShippingSiteSale())));
                superFeedverStockBean.setDiscountsitesale(getValue(String.valueOf(variationType.getProduct().getDiscountSiteSale())));
                superFeedverStockBean.setCondition(getValue(String.valueOf(variationType.getProduct().getCondition())));
                superFeedverStockBean.setReturnpolicy(getValue(String.valueOf(variationType.getProduct().getReturnPolicy())));
                List<ProductCategoryType> categoryList = product.getCategories().getCategory();
                //category
                if (categoryList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (ProductCategoryType categoryType : categoryList) {
                        sb.append(categoryType.getRetailerCategoryName());
                        if (categoryList.size() != 1) {
                            sb.append("-");
                        }
                    }
                    superFeedverStockBean.setCategory(sb.toString());
                }
                superFeedverStockBean.setDescription(getValue(variationType.getDescription()));
                superFeedverStockBean.setInventoryavailable(getValue(String.valueOf(variationType.getInventoryAvailable())));
                if (variationType.getSellingPrice() == null) {
                    superFeedverStockBean.setSellingpriceAmount("");
                    superFeedverStockBean.setSellingpriceCurrency("");
                } else {
                    superFeedverStockBean.setSellingpriceAmount(String.valueOf(variationType.getSellingPrice().getAmount()));
                    superFeedverStockBean.setSellingpriceCurrency(String.valueOf(variationType.getSellingPrice().getCurrency()));
                }
                if (variationType.getMapPrice() == null) {
                    superFeedverStockBean.setMappriceCurrency("");
                    superFeedverStockBean.setMappriceAmount("");
                } else {
                    superFeedverStockBean.setMappriceCurrency(String.valueOf(variationType.getMapPrice().getCurrency()));
                    superFeedverStockBean.setMappriceAmount(String.valueOf(variationType.getMapPrice().getAmount()));
                }
                if (variationType.getMsrpPrice() == null) {
                    superFeedverStockBean.setMsrppriceAmount(String.valueOf(variationType.getSellingPrice().getAmount()));
                    superFeedverStockBean.setMsrppriceCurrency(String.valueOf(variationType.getSellingPrice().getCurrency()));
                } else {
                    superFeedverStockBean.setMsrppriceAmount(getValue(String.valueOf(variationType.getMsrpPrice().getAmount())));
                    superFeedverStockBean.setMsrppriceCurrency(getValue(String.valueOf(variationType.getMsrpPrice().getCurrency())));
                }


                superFeedverStockBean.setMsrpexpirationdate(getValue(String.valueOf(variationType.getMsrpExpirationDate())));
                if (variationType.getCompareAtPrice() == null) {
                    superFeedverStockBean.setCompareatpriceAmount("");
                    superFeedverStockBean.setCompareatpriceCurrency("");
                } else {
                    superFeedverStockBean.setCompareatpriceAmount(getValue(String.valueOf(variationType.getCompareAtPrice().getAmount())));
                    superFeedverStockBean.setCompareatpriceCurrency(getValue(String.valueOf(variationType.getCompareAtPrice().getCurrency())));
                }
                superFeedverStockBean.setCompareatexpirationdate(getValue(String.valueOf(variationType.getCompareAtExpirationDate())));
                if (variationType.getPreviouslyAdvertisedPrice() == null) {
                    superFeedverStockBean.setPreviouslyadvertisedpriceAmount("");
                    superFeedverStockBean.setPreviouslyadvertisedpriceCurrency("");
                } else {
                    superFeedverStockBean.setPreviouslyadvertisedpriceAmount(String.valueOf(variationType.getPreviouslyAdvertisedPrice().getAmount()));
                    superFeedverStockBean.setPreviouslyadvertisedpriceCurrency(String.valueOf(variationType.getPreviouslyAdvertisedPrice().getCurrency()));
                }
                superFeedverStockBean.setShippingwidth(getValue(String.valueOf(variationType.getShippingWidth())));
                superFeedverStockBean.setShippingheight(getValue(String.valueOf(variationType.getShippingHeight())));
                superFeedverStockBean.setShippinglength(getValue(String.valueOf(variationType.getShippingLength())));
                superFeedverStockBean.setShippingweight(getValue(String.valueOf(variationType.getShippingWeight())));
                superFeedverStockBean.setUpc(getValue(variationType.getUpc()));
                superFeedverStockBean.setShipsvialtl(getValue(String.valueOf(variationType.isShipsViaLtl())));
                List<VariationAttributeType> variationAttributeTypeList = variationType.getAttributes().getAttribute();
                //attribute
                if (variationAttributeTypeList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (VariationAttributeType variationAttributeType : variationAttributeTypeList) {

                        List<String> valueList = variationAttributeType.getValues().getValue();
                        StringBuilder sbValue = new StringBuilder();
                        if (valueList.size() > 0) {
                            for (String value : valueList) {
                                sbValue.append(value.replace(" ", "") + "-");
                            }
                            sbValue.deleteCharAt(sbValue.length() - 1);
                        }
                        sb.append(variationAttributeType.getName() + ":" + sbValue + "|");
                    }
                    superFeedverStockBean.setAttribute1(sb.deleteCharAt(sb.length() - 1).toString());
                }
                //attributeColor attributeMetal attributeSize
                if (variationAttributeTypeList.size() > 0) {
                    //按照名称去排序
                    variationAttributeTypeList.sort((a, b) -> a.getName().compareTo(b.getName()));
                    //attributeColor
                    StringBuilder sbColorValue = new StringBuilder();
                    //attributeMetal
                    StringBuilder sbMetalValue = new StringBuilder();
                    //attributeSize
                    StringBuilder sbSizeValue = new StringBuilder();
                    for (VariationAttributeType variationAttributeType : variationAttributeTypeList) {
                        List<String> valueList = variationAttributeType.getValues().getValue();
                        String name = variationAttributeType.getName();
                        //attributeColor
                        if (name.contains("颜色") || name.contains("Color")) {
                            if (valueList.size() > 0) {
                                for (String value : valueList) {
                                    if (value.toString().equals("N/A")) {
                                        continue;
                                    }
                                    sbColorValue.append(value.replace(" ", "") + "-");
                                }
                            }
                        }
                        //attributeMetal
                        if (name.equals("金属") || name.equals("Metal")) {
                            if (valueList.size() > 0) {
                                for (String value : valueList) {
                                    if (value.toString().equals("N/A")) {
                                        continue;
                                    }
                                    sbMetalValue.append(value.replace(" ", "") + "-");
                                }

                            }
                        }
                        //attributeSize
                        if (name.contains("尺寸") || name.contains("Size")) {

                            if (valueList.size() > 0) {
                                for (String value : valueList) {
                                    if (value.toString().equals("N/A")) {
                                        continue;
                                    }
                                    sbSizeValue.append(value.replace(" ", "") + "-");
                                }
                            }
                        }
                    }
                    //attributeColor
                    if ("".equals(String.valueOf(sbColorValue))) {
                        superFeedverStockBean.setAttributeColor("");
                    } else {
                        superFeedverStockBean.setAttributeColor(String.valueOf(sbColorValue.deleteCharAt(sbColorValue.length() - 1)));
                    }
                    //attributeMetal
                    if ("".equals(String.valueOf(sbMetalValue))) {
                        superFeedverStockBean.setAttributeMetal("");
                    } else {
                        superFeedverStockBean.setAttributeMetal(String.valueOf(sbMetalValue.deleteCharAt(sbMetalValue.length() - 1)));
                    }
                    //attributeSize
                    if ("".equals(String.valueOf(sbSizeValue))) {
                        superFeedverStockBean.setAttributeSize("");
                    } else {
                        superFeedverStockBean.setAttributeSize(String.valueOf(sbSizeValue.deleteCharAt(sbSizeValue.length() - 1)));
                    }
                }
                //model_image
                List<ImageType> model_imageTypeList = product.getImages().getImage();
                if (model_imageTypeList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (ImageType imageType : model_imageTypeList) {
                        sb.append(imageType.getCdnPath() + ",");
                    }
                    superFeedverStockBean.setModelImage(sb.deleteCharAt(sb.length() - 1).toString());
                }
                //SKU_Image
                List<ImageType> imageTypeList = variationType.getImages().getImage();
                if (imageTypeList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (ImageType imageType : imageTypeList) {
                        sb.append(imageType.getCdnPath() + ",");
                    }
                    if(!StringUtils.isEmpty(superFeedverStockBean.getModelImage())){
                        sb.append(superFeedverStockBean.getModelImage());
                    }
                    superFeedverStockBean.setImage(sb.toString());
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
                if (variationAttributeTypeList.size() > 0) {
                    //按照名称去排序
                    variationAttributeTypeList.sort((a, b) -> a.getName().compareTo(b.getName()));
                    StringBuilder sb = new StringBuilder();
                    for (VariationAttributeType variationAttributeType : variationAttributeTypeList) {
                        List<String> valueList = variationAttributeType.getValues().getValue();
                        StringBuilder sbValue = new StringBuilder();
                        String name = variationAttributeType.getName();
                        //attributeSize
                        if (name.contains("尺寸") || name.contains("Size")) {
                            continue;
                        }
                        if (valueList.size() > 0) {
                            for (String value : valueList) {
                                if (value.toString().equals("N/A")) {
                                    continue;
                                }
                                sbValue.append(value.replace(" ", "") + "-");
                            }

                            sb.append(sbValue);
                        }
                    }
                    if ("".equals(String.valueOf(sb))) {
                        superFeedverStockBean.setSalepoint("");
                    } else {
                        superFeedverStockBean.setSalepoint(String.valueOf(sb.deleteCharAt(sb.length() - 1)));
                    }
                }
                //取得bean
                superfeed.add(superFeedverStockBean);
                $info("SKU:" +  sku);
            }
        }
        return superfeed;
    }
    /**
     * 调用 WsdlProductService 提交新商品
     *
     * @throws Exception
     */
    @Override
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
                if(ListUtils.isNull(product)) break;
                $info("每棵树的信息取得结束");

                String categorySplit = Feeds.getVal1(channel, FeedEnums.Name.category_split);
                if (!StringUtils.isEmpty(categorySplit)) {
                    product.forEach(cmsBtFeedInfoModel -> {
                        List<String> categors = java.util.Arrays.asList(cmsBtFeedInfoModel.getCategory().split(categorySplit));
                        cmsBtFeedInfoModel.setCategory(categors.stream().map(s -> s.replace("-", "－")).collect(Collectors.joining("-")));
                    });
                }
                productAll.addAll(product);
//                if (productAll.size() > 0) {
//                    executeMongoDB(productAll, productSucceeList, productFailAllList);
//                }
            }catch (Exception e){
                e.printStackTrace();
                $error(e);
                issueLog.log(e, com.voyageone.common.components.issueLog.enums.ErrorType.BatchJob, SubSystem.CMS);
            }
        }
        executeMongoDB(productAll, productSucceeList, productFailAllList);

        $info("总共~ 失败的 Product: %s", productFailAllList.size());

    }

    /**
     * 空值转换
     *
     * @param value
     * @return value
     */
    public String getValue(String value) {
        if (value == null || value.equals("null")) {
            value = "";
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

            overStockFeedDao.deleteBySku(superfeed);
            if (overStockFeedDao.insertSelective(superfeed) <= 0) {
                $info("OverStock产品信息插入失败sku = " + superfeed.getSku());
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
        String where = String.format("WHERE %s AND %s = '%s'", INSERT_FLG, colums.get("category").toString(),
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
            cmsBtFeedInfoModel.setProductType(StringUtil.isEmpty(cmsBtFeedInfoModel.getProductType())?"":cmsBtFeedInfoModel.getProductType().trim());
            cmsBtFeedInfoModel.setSizeType(StringUtil.isEmpty(cmsBtFeedInfoModel.getSizeType())?"":cmsBtFeedInfoModel.getSizeType().trim());
            cmsBtFeedInfoModel.setAttribute(attribute);

            //设置重量
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
                //size转换开始
                if(sku.getSize().toLowerCase().contains(":")){
                    String attributeList [] =sku.getSize().split("\\|");
                    String size = "";
                    for(String attribute1 :attributeList){
                        if(attribute1.toLowerCase().contains("size")){
                            size=attribute1.split(":")[1];
                        }
                    }
                    if(!StringUtils.isEmpty(size)){
                        sku.setSize(size);
                    }else{
                        sku.setSize("OneSize");
                    }
                }
                //size转换结束
                sku.setWeightOrgUnit(sku.getWeightOrgUnit());
            }
            cmsBtFeedInfoModel.setSkus(skus);
            //设置重量结束

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
