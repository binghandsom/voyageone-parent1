package com.voyageone.service.impl.cms.usa;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.solr.bean.CommIdSearchModel;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.query.SimpleQueryBean;
import com.voyageone.components.solr.service.CmsProductSearchService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsSaleDataStatisticsMQMessageBody;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 美国CMS 自定义列动态时间段销售统计
 *
 * @Author rex.wu
 * @Create 2017-08-04 11:30
 */
@Service
public class UsaSaleDataStatisticsService extends BaseService {
    private final
    CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    private final CmsBtProductDao cmsBtProductDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private CmsProductSearchService cmsProductSearchService;

    private final static String CORE_NAME_PRODUCT = "cms_product";


    @Autowired
    public UsaSaleDataStatisticsService(CmsMtProdSalesHisDao cmsMtProdSalesHisDao, CmsBtProductDao cmsBtProductDao) {
        this.cmsMtProdSalesHisDao = cmsMtProdSalesHisDao;
        this.cmsBtProductDao = cmsBtProductDao;
    }

    public void SaleDataStatistics(CmsSaleDataStatisticsMQMessageBody messageBody) {
        $info(String.format("销量计算: cartId=%d, beginTime=%s, endTime=%s", messageBody.getCartId(), messageBody.getStartDate(), messageBody.getEndDate()));
        List<String> codes = new ArrayList<>();
        String username = messageBody.getSender();
        if (StringUtils.isBlank(username)) {
            username = getClass().getSimpleName();
        }
        $info("销量数据从solr中清空start");
        cleanBeforeSaleData(messageBody.getChannelId(), messageBody.getCartId());
        $info("销量数据从solr中清空中end");
        Criteria criteria = new Criteria("channel_id").is(messageBody.getChannelId()).and("date").gte(messageBody.getStartDate()).lte(messageBody.getEndDate());

        if(messageBody.getCartId() != null && messageBody.getCartId() != -1){
            criteria = criteria.and("cart_id").is(messageBody.getCartId());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("prodCode").sum("qty").as("sale"));

        AggregationResults<SaleMode> items = cmsMtProdSalesHisDao.aggregateToObj(aggregation, SaleMode.class);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (Iterator<SaleMode> iterator = items.iterator(); iterator.hasNext();) {
            SaleMode item = iterator.next();
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", item.get_id());
            codes.add(item.get_id());
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("sales.P" + messageBody.getCartId(), item.getSale());
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
            if(bulkList.size() == 500) {
                BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(messageBody.getChannelId(), bulkList, username, "$set");
                $info(bulkWriteResult.toString());
                bulkList.clear();
            }
            addSaleDataToSolr(messageBody.getChannelId(), item.get_id(), messageBody.getCartId(), item.getSale());
        }
        if(bulkList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(messageBody.getChannelId(), bulkList, username, "$set");
            $info(bulkWriteResult.toString());
            bulkList.clear();
        }
        CacheHelper.delete("P" + messageBody.getCartId() + "_customSale");
    }

    private void addSaleDataToSolr(String channelId, String code, Integer cartId, String sale){
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, code);
        if(cmsBtProductModel != null) {
            SolrUpdateBean update = new SolrUpdateBean("id", cmsBtProductModel.get_id());
            update.add("P" + cartId + "_customSale", sale);
            cmsProductSearchService.saveBean(CORE_NAME_PRODUCT, update);
        }

    }
    private void cleanBeforeSaleData(String channelId, Integer cartId){
        JongoUpdate jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{'sales.P"+cartId+"':{$ne:0}}");
        jongoUpdate.setUpdate("{$set:{'sales.P"+cartId+"':0}}");
        cmsBtProductDao.updateMulti(jongoUpdate,channelId);

        String queryString = String.format("productChannel:\"%s\" && -P%d_customSale:0", channelId, cartId);
        SimpleQueryBean query = new SimpleQueryBean(queryString);
        query.addSort(new Sort(Sort.Direction.DESC, "id"));
        query.addProjectionOnField("id");

        int pageNo = 0;
        int pageSize = 500;
        List<SolrUpdateBean> updates = new ArrayList<>(pageSize);
        //noinspection Duplicates
        while (true) {
            List<String> removeIdList = new ArrayList<>();

            query.setOffset(pageNo * pageSize);
            query.setRows(pageSize);
            Page<CommIdSearchModel> productSearchCursor = cmsProductSearchService.queryIdsForCursorNotLastVer(CORE_NAME_PRODUCT, query);
            if (!productSearchCursor.hasContent()) {
                break;
            }

            removeIdList.addAll(productSearchCursor.getContent().stream().filter(model -> model != null && model.getId() != null).map(CommIdSearchModel::getId).collect(Collectors.toList()));

            removeIdList.forEach(id->{
                SolrUpdateBean update = new SolrUpdateBean("id", id);
                update.add("P"+cartId+"_customSale", 0);
                updates.add(update);
            });
            cmsProductSearchService.saveBeans(CORE_NAME_PRODUCT, updates);
            updates.clear();
        }

    }
    class SaleMode{
        String _id;
        String sale;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getSale() {
            return sale;
        }

        public void setSale(String sale) {
            this.sale = sale;
        }

    }
}
