package com.voyageone.task2.cms.service.product.sales;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.sneakerhead.bean.CmsBtProductModel_SalesBean;
import com.voyageone.components.sneakerhead.service.SneakerheadApiService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by gjl on 2016/11/23.
 */
@Service
public class CmsSumSneakerHeadSalesService extends BaseCronTaskService {
    @Autowired
    private SneakerheadApiService sneakerheadApiService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    private static final String SNEAKER_HEAD_CHANNEL = "001";

    private static final String SNEAKER_HEAD_ACCESS_DOMAIN = "47.180.64.158:52233";

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // TODO: 2016/12/1 add get domain from taskcontrollist
        //DEFAULT_DOMAIN = taskControlList

        $info("取得cms_bt_product_c001表的所有code开始:");
        //取得所有的code
        JongoQuery query = new JongoQuery();
        query.setProjectionExt("common.fields.code");
        Iterator<CmsBtProductModel> it = cmsBtProductDao.selectCursor(query, SNEAKER_HEAD_CHANNEL);
        int index = 1;
        List<String> codeList = new ArrayList<>();
        while (it.hasNext()) {
            CmsBtProductModel cmsBtProductModel = it.next();
            codeList.add(cmsBtProductModel.getCommon().getFields().getCode());
            if (index % 3000 == 0) {
                $info("取得cms_bt_product_c001表的所有code的个数为:" + index);
                // 更新数据--调用sneakerHead接口取得各个code销售数量及计算总共的销售数量
                getCodeSalesAndSumSales(codeList);
                codeList.clear();
            }
            index++;
        }
        // 更新数据--调用sneakerHead接口取得各个code销售数量及计算总共的销售数量
        if (!codeList.isEmpty()) {
            getCodeSalesAndSumSales(codeList);
        }
        $info("取得cms_bt_product_c001表的所有code的结束:");
    }

    /**
     * 调用sneakerHead接口取得各个code销售数量及计算总共的销售数量
     */
    private void getCodeSalesAndSumSales(List<String> codeList) throws Exception {
        $info("调用sneakerHead接口取得各个code销售数量开始:");
        List<CmsBtProductModel_SalesBean> saleList = sneakerheadApiService.getUsSales(codeList, SNEAKER_HEAD_ACCESS_DOMAIN);
        $info("调用sneakerHead接口取得各个code销售数量:" + saleList.size());
        $info("调用sneakerHead接口取得各个code销售数量结束:");

        $info("cmsBtProductModel_Sales计算总共的销售数量开始");
        updateCmsBtProductModelSales(saleList);
        $info("cmsBtProductModel_Sales计算总共的销售数量结束");
    }

    @Override
    protected String getTaskName() {
        return "CmsSumSneakerHeadSalesHisInfoJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }


    /**
     * 计算销量3天7天全年和总销量
     */
    private void updateCmsBtProductModelSales(List<CmsBtProductModel_SalesBean> saleList) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        if (saleList.size() == 0) return;
        for (CmsBtProductModel_SalesBean salesBean : saleList) {
            Map<String, Object> salesMap = new HashMap<>();
            CmsBtProductModel cmsBtProductModel = cmsBtProductDao.selectByCode(salesBean.getCode(), SNEAKER_HEAD_CHANNEL);
            //取得7天的销量
            Map<String, Object> sum7Map = Optional.ofNullable(cmsBtProductModel)
                    .map(CmsBtProductModel::getSales)
                    .map(CmsBtProductModel_Sales::getCodeSum7)
                    .orElse(new HashMap<>());
            sum7Map.putAll(salesBean.getCodeSum7());
            //7天的总销量
            int cartIdSale7 = getCartIdSalesSum(sum7Map);
            sum7Map.put("cartId0", cartIdSale7);
            salesMap.put(CmsBtProductModel_Sales.CODE_SUM_7, sum7Map);

            //取得30天的销量
            Map<String, Object> sum30Map = Optional.ofNullable(cmsBtProductModel)
                    .map(CmsBtProductModel::getSales)
                    .map(CmsBtProductModel_Sales::getCodeSum30)
                    .orElse(new HashMap<>());
            sum30Map.putAll(salesBean.getCodeSum30());
            //计算30天的总销量
            int cartIdSale30 = getCartIdSalesSum(sum30Map);
            sum30Map.put("cartId0", cartIdSale30);
            salesMap.put(CmsBtProductModel_Sales.CODE_SUM_30, sum30Map);

            //取得年的销量
            Map<String, Object> sumYearMap = Optional.ofNullable(cmsBtProductModel)
                    .map(CmsBtProductModel::getSales)
                    .map(CmsBtProductModel_Sales::getCodeSumYear)
                    .orElse(new HashMap<>());
            sumYearMap.putAll(salesBean.getCodeSumYear());
            //取得年的总销量
            int cartIdSaleYears = getCartIdSalesSum(sumYearMap);
            sumYearMap.put("cartId0", cartIdSaleYears);
            salesMap.put(CmsBtProductModel_Sales.CODE_SUM_YEAR, sumYearMap);

            //取得总的销量
            Map<String, Object> sumAllMap = Optional.ofNullable(cmsBtProductModel)
                    .map(CmsBtProductModel::getSales)
                    .map(CmsBtProductModel_Sales::getCodeSumAll)
                    .orElse(new HashMap<>());
            sumAllMap.putAll(salesBean.getCodeSumAll());
            //取得年的总销量
            int cartIdSaleAll = getCartIdSalesSum(sumAllMap);
            sumAllMap.put("cartId0", cartIdSaleAll);
            salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, sumAllMap);

            //批量更新
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("sales", salesMap);
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", salesBean.getCode());
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
            if (bulkList.size() > 999) {
                $info("已更新cms_bt_product_c001表的code的个数为:" + bulkList.size());
                cmsBtProductDao.bulkUpdateWithMap(SNEAKER_HEAD_CHANNEL, bulkList, getTaskName(), "$set");
                bulkList.clear();
            }
        }
        //批量更新
        if (bulkList.size() > 0) {
            $info("已更新cms_bt_product_c001表的code的个数为:" + bulkList.size());
            cmsBtProductDao.bulkUpdateWithMap(SNEAKER_HEAD_CHANNEL, bulkList, getTaskName(), "$set");
            bulkList.clear();
        }
    }

    /**
     * 取得3天7天全年和总销量
     */
    private int getCartIdSalesSum(Map<String, Object> sumMap) {
        return sumMap.entrySet().stream()
                .filter(pro -> !"cartId0".equals(pro.getKey()))
                .mapToInt(cartId -> {
                    try {
                        return Integer.parseInt(String.valueOf(cartId.getValue()));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }).sum();
    }
}
