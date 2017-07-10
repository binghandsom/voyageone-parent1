package com.voyageone.service.impl.cms.feed;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsMtCategoryTreeAllBean;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.tools.common.CmsMasterBrandMappingService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * feed info Service
 *
 * @author JiangJusheng 2016/04/06
 * @version 2.0.0
 */
@Service
public class FeedInfoService extends BaseService {

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    private CmsMasterBrandMappingService cmsMasterBrandMappingService;

    /**
     * getList
     */
    public List<CmsBtFeedInfoModel> getList(String channelId, JongoQuery queryObject) {
        return cmsBtFeedInfoDao.select(queryObject, channelId);
    }

    /**
     * getCnt
     */
    public long getCnt(String channelId, Map<String, Object> searchValue) {
        String queryStr = getSearchQuery(channelId, searchValue);
        return cmsBtFeedInfoDao.countByQuery(queryStr, channelId);
    }

    public WriteResult updateFeedInfoSkuPrice(String channelId, String sku, Double price){
        return  cmsBtFeedInfoDao.updateFeedInfoSkuPrice(channelId, sku,price);
    }

    /**
     * getListForVendor
     */
    public List<CmsBtFeedInfoModel> getListForVendor(String channelId, JongoQuery queryObject) {
        return cmsBtFeedInfoDao.select(queryObject, channelId);
    }

    /**
     * getCntForVendor
     */
    public long getCntForVendor(String channelId, Map<String, Object> searchValue) {
        String queryStr = getSearchQueryForVendor(searchValue);
        return cmsBtFeedInfoDao.countByQuery(queryStr, channelId);
    }

    /**
     * getProductByCode
     */
    public CmsBtFeedInfoModel getProductByCode(String channelId, String productCode) {
        return cmsBtFeedInfoDao.selectProductByCode(channelId, productCode);
    }

    public CmsBtFeedInfoModel getProductBySku(String channelId, String sku) {
        return cmsBtFeedInfoDao.selectProductBySku(channelId, sku);
    }

    public List<CmsBtFeedInfoModel> getProductListBySku(String channelId, String sku) {
        return cmsBtFeedInfoDao.selectProductListBySku(channelId, sku);
    }

    public CmsBtFeedInfoModel getProductByClientSku(String channelId, String clientSku) {
        return cmsBtFeedInfoDao.selectProductByClientSku(channelId, clientSku);
    }

    public List<CmsBtFeedInfoModel> getProductByModel(String channelId, String model) {
        return cmsBtFeedInfoDao.selectProductByModel(channelId, model);
    }
    /**
     * 更新feed的产品信息
     *
     * @param cmsBtFeedInfoModel feed的产品信息
     * @return WriteResult
     */
    public WriteResult updateFeedInfo(CmsBtFeedInfoModel cmsBtFeedInfoModel) {
        return cmsBtFeedInfoDao.update(cmsBtFeedInfoModel);
    }

    public WriteResult insertFeedInfo(CmsBtFeedInfoModel cmsBtFeedInfoModel){
        return cmsBtFeedInfoDao.insert(cmsBtFeedInfoModel);
    }
    public WriteResult delFeedInfo(CmsBtFeedInfoModel cmsBtFeedInfoModel){
        return cmsBtFeedInfoDao.deleteById(cmsBtFeedInfoModel.get_id(), cmsBtFeedInfoModel.getChannelId());
    }
    /**
     * 更新feed的产品信息
     */
    public WriteResult updateFeedInfo(String channelId, Map paraMap, Map rsMap) {
        Map<String, Object> valueMap = new HashMap<>(1);
        valueMap.put("$set", rsMap);
        return cmsBtFeedInfoDao.update(channelId, paraMap, valueMap);
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     */
    public String getSearchQueryForVendor(Map<String, Object> searchValue) {
        StringBuilder result = new StringBuilder();

        // 获取code/sku
        String code = (String) searchValue.get("code");
        if (!StringUtils.isEmpty(code)) {
            List<String> orSearch = new ArrayList<>();
            orSearch.add(MongoUtils.splicingValue("code", code));
            orSearch.add(MongoUtils.splicingValue("skus.clientSku", code));
            result.append("{").append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
            result.append("},");
        }

        // 获取product name
        String name = (String) searchValue.get("name");
        if (!StringUtils.isEmpty(name)) {
            result.append("{").append(MongoUtils.splicingValue("name", replaceRegexReservedChar(name), "$regex"));
            result.append("},");
        }

        // 获取category
        List<String> categorys = (List<String>) searchValue.get("searchCats");
        List<String> searchCategory = new ArrayList<>();
        for (String category : categorys) {
            String[] categoryArray = category.split("/");
            category = "";
            for (String categoryItem : categoryArray) {
                // 不等于空的情况下，去掉首尾空格，并替换半角横杠为全角横杠，重新组装一下
                if (!StringUtils.isEmpty(categoryItem)) {
                    category += categoryItem.trim().replaceAll("-", "－") + "-";
                }
            }
            // 去掉最后一个分隔符[-]
            if (!StringUtils.isEmpty(category)) {
                category = category.substring(0, category.length() - 1);
                searchCategory.add(MongoUtils.splicingValue("category", replaceRegexReservedChar(category), "$regex"));
            }
        }
        if (searchCategory.size() == 1) {
            result.append("{").append(searchCategory.get(0));
            result.append("},");
        } else if (searchCategory.size() > 1) {
            result.append("{").append(MongoUtils.splicingValue("", searchCategory.toArray(), "$or"));
            result.append("},");
        }

        // 获取价格
        Double priceSta = null;
        Double priceEnd =  null;
       if(searchValue.get("priceStart") != null){
            if (StringUtils.isNumeric(String.valueOf(searchValue.get("priceStart")))) {
                priceSta = Double.parseDouble(String.valueOf(searchValue.get("priceStart")));
            }
        }

        if(searchValue.get("priceEnd") != null){
            if (StringUtils.isNumeric(String.valueOf(searchValue.get("priceEnd")))) {
                priceEnd = Double.parseDouble(String.valueOf(searchValue.get("priceEnd")));
            }
        }

        if (priceSta != null || priceEnd != null) {
            result.append("{\"skus\":{$elemMatch:{\"priceNet\":{");
            if (priceSta != null) {
                result.append(MongoUtils.splicingValue("$gte", priceSta));
            }
            if (priceEnd != null) {
                if (priceSta != null) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", priceEnd));
            }
            result.append("}}}},");
        }

        // 获取库存
        Integer qtySta = null;
        Integer qtyEnd =  null;
        if(searchValue.get("qtyStart") != null){
            if (StringUtils.isNumeric(String.valueOf(searchValue.get("qtyStart")))) {
                qtySta = Integer.parseInt(String.valueOf(searchValue.get("qtyStart")));
            }
        }

        if(searchValue.get("qtyEnd") != null){
            if (StringUtils.isNumeric(String.valueOf(searchValue.get("qtyEnd")))) {
                qtyEnd = Integer.parseInt(String.valueOf(searchValue.get("qtyEnd")));
            }
        }

        if (qtySta != null || qtyEnd != null) {
            result.append("{\"skus\":{$elemMatch:{\"qty\":{");
            if (qtySta != null) {
                result.append(MongoUtils.splicingValue("$gte", qtySta));
            }
            if (qtyEnd != null) {
                if (qtySta != null) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", qtyEnd));
            }
            result.append("}}}},");
        }

        if (!StringUtils.isEmpty(result.toString())) {
            return "{$and:[" + result.toString().substring(0, result.toString().length() - 1) + "]}";
        } else {
            return "";
        }
    }

    /**
     * 转义正则表达式保留字
     */
    private String replaceRegexReservedChar(String regStr) {
        // 替换保留字   \ * . ? + $ ^ [ ] ( ) { } |
        char reservedChars[] = {'\\','*','.','?','+','$','^','[',']','(',')','{','}','|'};
        for (char reservedChar : reservedChars) {
            regStr = replace(regStr, reservedChar);
        }

        return regStr;
    }

    /**
     * 替换转义字符
     */
    private String replace(String regStr, char replaceChar) {
        if (regStr.indexOf(replaceChar) == -1) {
            return regStr;
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i< regStr.length(); i++) {
            char c = regStr.charAt(i);
            if (c == replaceChar) {
                if (c != '\\') {
                    sb.append("\\\\");
                } else {
                    sb.append("\\\\\\");
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     */
    public String getSearchQuery(String channelId, Map<String, Object> searchValue) {
        StringBuilder result = new StringBuilder();

        // 获取查询的价格类型
        String priceType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("priceType"));
        if (priceType != null) {
            // 获取查询的价格区间下限
            float priceSta = -1;
            float priceEnd = -1;
            if(searchValue.get("priceValueSta") != null){
                priceSta = Float.parseFloat(searchValue.get("priceValueSta").toString());
            }
            if(searchValue.get("priceValueEnd") != null){
                priceEnd = Float.parseFloat(searchValue.get("priceValueEnd").toString());
            }
            if (priceSta > -1 && priceEnd > -1 && priceEnd < priceSta) {
                throw new BusinessException("设置的查询价格区间不正确");
            }
            if (priceSta > -1 || priceEnd > -1) {
                result.append("{\"skus.").append(priceType).append("\":{");
                if (priceSta > -1) {
                    result.append(MongoUtils.splicingValue("$gte", priceSta));
                }
                if (priceEnd > -1) {
                    if (priceSta > -1) {
                        result.append(",");
                    }
                    result.append(MongoUtils.splicingValue("$lte", priceEnd));
                }
                result.append("}},");
            }
        }

        // 获取createdTime start
        String createTimeSta = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("createTimeSta"));
        // 获取createdTime End
        String createTimeEnd = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("createTimeEnd"));
        if (createTimeSta != null || createTimeEnd != null) {
            result.append("{\"created\":{");
            if (createTimeSta != null) {
                result.append(MongoUtils.splicingValue("$gte", createTimeSta + " 00.00.00"));
            }
            if (createTimeEnd != null) {
                if (createTimeSta != null) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", createTimeEnd + " 23.59.59"));
            }
            result.append("}},");
        }

        // 获取updateTime start
        String updateTimeSta = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("updateTimeSta"));
        // 获取updateTime End
        String updateTimeEnd = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("updateTimeEnd"));
        if (updateTimeSta != null || updateTimeEnd != null) {
            result.append("{\"modified\":{");
            if (updateTimeSta != null) {
                result.append(MongoUtils.splicingValue("$gte", updateTimeSta + " 00.00.00.000"));
            }
            if (updateTimeEnd != null) {
                if (updateTimeSta != null) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", updateTimeEnd + " 23.59.59.999"));
            }
            result.append("}},");
        }

        // 获取category
        if(searchValue.get("category") != null){

            List<String> categorys = (List<String>) searchValue.get("category");

            if (!categorys.isEmpty()) {
                StringBuffer categoryQuery = new StringBuffer();
                categorys.forEach(s -> categoryQuery.append("^" + s + "|"));
                result.append("{\"category\":{$regex: \"" + categoryQuery.toString().substring(0, categoryQuery.length() -1) + "\"}},");
            }
        }


        // 获取product name
        String prodName = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("name"));
        if (prodName != null) {
            result.append("{").append(MongoUtils.splicingValue("name", prodName));
            result.append("},");
        }

        // 获取输入的模糊查询字符串,用于检索code,name,model,short_description,long_description
        String codesStr = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("codeList"));
        if (codesStr != null) {
            List<String> strList = Arrays.asList(codesStr.split("\n"));
            if (!strList.isEmpty()) {
                List<String> orSearch = new ArrayList<>();
                for (String fuzzyStr : strList) {
                    orSearch.add(MongoUtils.splicingValue("code", fuzzyStr));
                    orSearch.add(MongoUtils.splicingValue("model", fuzzyStr));
                    orSearch.add(MongoUtils.splicingValue("skus.sku", fuzzyStr));
                    orSearch.add(MongoUtils.splicingValue("skus.clientSku", fuzzyStr));
                }
                result.append("{").append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
                result.append("},");
            }
        }
        String fuzzySearch = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("fuzzySearch"));
        if (fuzzySearch != null) {
            List<String> orSearch = new ArrayList<>();
            orSearch.add(MongoUtils.splicingValue("category", fuzzySearch, "$regex"));
            orSearch.add(MongoUtils.splicingValue("name", fuzzySearch, "$regex"));
            orSearch.add(MongoUtils.splicingValue("shortDescription", fuzzySearch, "$regex"));
            orSearch.add(MongoUtils.splicingValue("longDescription", fuzzySearch, "$regex"));
            result.append("{").append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
            result.append("},");
        }

        if(searchValue.get("masterBrand") != null){
            List<String> masterBrands = (List<String>) searchValue.get("masterBrand");
            if (!masterBrands.isEmpty()) {
                List<String> feedBrands = cmsMasterBrandMappingService.getFeedBrandByMasterBrand(channelId, masterBrands);
                List<String> brands;
                if (searchValue.get("brand") != null) {
                    brands = (List<String>) searchValue.get("brand");
                } else {
                    brands = new ArrayList<String>();
                }
                brands.addAll(feedBrands);
                brands = brands.stream().distinct().collect(Collectors.toList());
                searchValue.put("brand", brands);
            }
        }

        // 获取brand
        if (searchValue.get("brand") != null) {
            List<String> brands = (List<String>) searchValue.get("brand");
            if (!brands.isEmpty()) {
                List<String> orSearch = new ArrayList<>();
                for (String brand : brands) {
                    orSearch.add(MongoUtils.splicingValue("brand", brand));
                }
                result.append("{").append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
                result.append("},");
            }
        }

        // 获取color
        String color = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("color"));
        if (color != null) {
            result.append("{'color':{'$regex': '").append(color).append("','$options':'i'}");
            result.append("},");
        }

        // 获取product type
        String productType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("productType"));
        if (productType != null) {
            result.append("{").append(MongoUtils.splicingValue("productType", productType));
            result.append("},");
        }

        // 获取size type
        String sizeType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("sizeType"));
        if (sizeType != null) {
            result.append("{").append(MongoUtils.splicingValue("sizeType", sizeType));
            result.append("},");
        }

        // 获取inventory
        String compareType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("compareType"));
        String qtyStr = null;
        if(searchValue.get("inventory") != null){
            qtyStr = searchValue.get("inventory").toString();
        }
        if (compareType != null && qtyStr != null) {
            int inventory = NumberUtils.toInt(qtyStr);
            result.append("{").append(MongoUtils.splicingValue("qty", inventory, compareType));
            result.append("},");
        }

        // 获取status
//        String status = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("status"));
//        if (status != null) {
//            result.append("{").append(MongoUtils.splicingValue("updFlg", NumberUtils.toInt(status, -1)));
//            result.append("},");
//        }
        if (searchValue.get("status") != null) {
            List<Integer> statuss = (List<Integer>) searchValue.get("status");
            if (!statuss.isEmpty()) {
                List<String> orSearch = new ArrayList<>();
                for (Integer status : statuss) {
                    orSearch.add(MongoUtils.splicingValue("updFlg", status));
                }
                result.append("{").append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
                result.append("},");
            }
        }

        //
        if (searchValue.get("ninStatus") != null) {
            List<Integer> ninList = (List<Integer>) searchValue.get("ninStatus");
            result.append("{updFlg:{$nin:[");
            result.append(ninList.stream().map(Object::toString).collect(Collectors.joining(",")));
            result.append("]}},");
        }

        // 获取 master category
        if (searchValue.get("mCatPath") != null) {
            List<String>mCatPathList = (List<String>) searchValue.get("mCatPath");
            if("1".equals(searchValue.get("mCatPathType").toString())) {
                StringBuilder mCatPathStr = new StringBuilder("{$or:[");
                int idx = 0;
                List<String> parameters = new ArrayList<>();
                for (String mCatPath : mCatPathList) {
                    //fCatPath = StringUtils.replace(fCatPath, "'", "\\'");
                    if (idx == 0) {
                        mCatPathStr.append("{\"mainCategoryCn\":{\"$regex\":#}}");
                        idx++;
                    } else {
                        mCatPathStr.append(",{\"mainCategoryCn\":{\"$regex\":#}}");
                    }
                    parameters.add("^" + mCatPath);
                }
                mCatPathStr.append("]}");
                JongoQuery queryObject = new JongoQuery();
                queryObject.addQuery(mCatPathStr.toString());
                queryObject.addParameters(parameters.toArray());
                result.append(queryObject.getJongoQueryStr()+",");
            }else{
                StringBuilder mCatPathStr = new StringBuilder("{$or:[{$and:[");
                int idx = 0;
                List<String> parameters = new ArrayList<>();
                for (String mCatPath : mCatPathList) {
                    //fCatPath = StringUtils.replace(fCatPath, "'", "\\'");
                    if (idx == 0) {
                        mCatPathStr.append("{\"mainCategoryCn\":{\"$regex\":#}}");
                        idx++;
                    } else {
                        mCatPathStr.append(",{\"mainCategoryCn\":{\"$regex\":#}}");
                    }
                    parameters.add(String.format("^((?!%s).)*$",mCatPath));
                }
                mCatPathStr.append("]},{\"mainCategoryCn\":{\"$in\":[null,'']}}]}");
                JongoQuery queryObject = new JongoQuery();
                queryObject.addQuery(mCatPathStr.toString());
                queryObject.addParameters(parameters.toArray());
                result.append(queryObject.getJongoQueryStr()+",");
            }
        }

        if(searchValue.get("message") != null && !StringUtil.isEmpty((String) searchValue.get("message"))){
            JongoQuery queryObject = new JongoQuery();
            queryObject.addQuery("{\"updMessage\":{\"$regex\":#}}");
            queryObject.addParameters(searchValue.get("message"));
            result.append(queryObject.getJongoQueryStr()+",");
        }

        if (!StringUtils.isEmpty(result.toString())) {
            return "{$and:[" + result.toString().substring(0, result.toString().length() - 1) + "]}";
        } else {
            return "";
        }
    }

    public WriteResult updateAllUpdFlg(String selChannelId, String searchQuery, Integer status, String modifier) {
        return cmsBtFeedInfoDao.updateAllUpdFlg(selChannelId, searchQuery, status, modifier);
    }

    public boolean updateMainCategory(String channelId, String code, CmsMtCategoryTreeAllBean cmsMtCategory, String modifier){
        CmsBtFeedInfoModel cmsBtFeedInfo = getProductByCode(channelId, code);
        if(cmsBtFeedInfo != null){
            cmsBtFeedInfo.setMainCategoryCn(cmsMtCategory.getCatPath());
            cmsBtFeedInfo.setMainCategoryEn(cmsMtCategory.getCatPathEn());
            cmsBtFeedInfo.setCatConf("1");
            cmsBtFeedInfo.setModifier(modifier);
            cmsBtFeedInfo.setModified(DateTimeUtil.getNowTimeStamp());
            if (cmsBtFeedInfo.getUpdFlg() == CmsConstants.FeedUpdFlgStatus.Succeed || cmsBtFeedInfo.getUpdFlg() == CmsConstants.FeedUpdFlgStatus.Fail) {
                cmsBtFeedInfo.setUpdFlg(CmsConstants.FeedUpdFlgStatus.Pending);
            }
        }
        updateFeedInfo(cmsBtFeedInfo);
        return true;
    }
}
