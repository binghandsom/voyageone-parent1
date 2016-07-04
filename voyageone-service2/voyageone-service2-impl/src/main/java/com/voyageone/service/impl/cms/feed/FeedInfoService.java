package com.voyageone.service.impl.cms.feed;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
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

    /**
     * getList
     */
    public List<CmsBtFeedInfoModel> getList(String channelId, JomgoQuery queryObject) {
        return cmsBtFeedInfoDao.select(queryObject, channelId);
    }

    /**
     * getCnt
     */
    public long getCnt(String channelId, Map<String, Object> searchValue) {
        String queryStr = getSearchQuery(searchValue);
        return cmsBtFeedInfoDao.countByQuery(queryStr, channelId);
    }

    /**
     * getProductByCode
     */
    public CmsBtFeedInfoModel getProductByCode(String channelId, String productCode) {
        return cmsBtFeedInfoDao.selectProductByCode(channelId, productCode);
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

    /**
     * 更新feed的产品信息
     */
    public WriteResult updateFeedInfo(String channelId, Map paraMap, Map rsMap) {
        HashMap valueMap = new HashMap(1);
        valueMap.put("$set", rsMap);
        return cmsBtFeedInfoDao.update(channelId, paraMap, valueMap);
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     *
     * @param searchValue
     * @return
     */
    public String getSearchQuery(Map<String, Object> searchValue) {
        StringBuffer result = new StringBuffer();

        // 获取查询的价格类型
        String priceType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("priceType"));
        if (priceType != null) {
            // 获取查询的价格区间下限
            float priceSta = NumberUtils.toFloat(org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("priceValueSta")), -1);
            // 获取查询的价格区间上限
            float priceEnd = NumberUtils.toFloat(org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("priceValueEnd")), -1);
            if (priceSta > -1 && priceEnd > -1 && priceEnd < priceSta) {
                throw new BusinessException("设置的查询价格区间不正确");
            }
            if (priceSta > -1 || priceEnd > -1) {
                result.append("{\"skus." + priceType + "\":{");
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
        String category = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("category"));
        if (category != null) {
            result.append("{" + MongoUtils.splicingValue("category", category));
            result.append("},");
        }

        // 获取product name
        String prodName = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("name"));
        if (prodName != null) {
            result.append("{" + MongoUtils.splicingValue("name", prodName));
            result.append("},");
        }

        // 获取输入的模糊查询字符串,用于检索code,name,model,short_description,long_description
        String codesStr = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("codeList"));
        if (codesStr != null) {
            List<String> strList = new ArrayList(Arrays.asList(codesStr.split("\n")));
            if (strList != null && strList.size() > 0) {
                List<String> orSearch = new ArrayList<>();
                for (String fuzzyStr : strList) {
                    orSearch.add(MongoUtils.splicingValue("code", fuzzyStr));
                    orSearch.add(MongoUtils.splicingValue("model", fuzzyStr));
                    orSearch.add(MongoUtils.splicingValue("skus.sku", fuzzyStr));
                    orSearch.add(MongoUtils.splicingValue("skus.clientSku", fuzzyStr));
                }
                result.append("{" + MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
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
            result.append("{" + MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
            result.append("},");
        }

        // 获取brand
        if (searchValue.get("brand") != null) {
            List<String> brands = (List<String>) searchValue.get("brand");
            if (brands.size() > 0) {
                List<String> orSearch = new ArrayList<>();
                for (String brand : brands) {
                    orSearch.add(MongoUtils.splicingValue("brand", brand));
                }
                result.append("{" + MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
                result.append("},");
            }
        }

        // 获取color
        String color = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("color"));
        if (color != null) {
            result.append("{'color':{'$regex': '" + color + "','$options':'i'}");
            result.append("},");
        }

        // 获取product type
        String productType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("productType"));
        if (productType != null) {
            result.append("{" + MongoUtils.splicingValue("productType", productType));
            result.append("},");
        }

        // 获取size type
        String sizeType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("sizeType"));
        if (sizeType != null) {
            result.append("{" + MongoUtils.splicingValue("sizeType", sizeType));
            result.append("},");
        }

        // 获取inventory
        String compareType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("compareType"));
        String qtyStr = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("inventory"));
        if (compareType != null && qtyStr != null) {
            int inventory = NumberUtils.toInt(qtyStr);
            result.append("{" + MongoUtils.splicingValue("qty", inventory, compareType));
            result.append("},");
        }

        // 获取status
        String status = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("status"));
        if (status != null) {
            result.append("{" + MongoUtils.splicingValue("updFlg", NumberUtils.toInt(status, -1)));
            result.append("},");
        }

        //
        if (searchValue.get("ninStatus") != null) {
            List<Integer> ninList = (List<Integer>) searchValue.get("ninStatus");
            result.append("{updFlg:{$nin:[");
            result.append(ninList.stream().map(integer -> integer.toString()).collect(Collectors.joining(",")));
            result.append("]}},");
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
}
