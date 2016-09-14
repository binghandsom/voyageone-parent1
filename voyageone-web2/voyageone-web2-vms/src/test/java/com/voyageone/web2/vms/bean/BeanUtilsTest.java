package com.voyageone.web2.vms.bean;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.jumei.ProductImportBean;
import com.voyageone.service.bean.cms.jumei.SkuImportBean;
import com.voyageone.service.model.cms.CmsBtBrandBlockModel;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.bean.order.OrderSearchInfoBean;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.voyageone.web2.vms.VmsConstants.DEFAULT_PAGE_SIZE;

/**
 * Created by DELL on 2016/9/14.
 */
public class BeanUtilsTest {

    @Test
    public void testCmsBtBrandBlockModel() {
        CmsBtBrandBlockModel model  = new CmsBtBrandBlockModel();
        model.setId(1);
        model.setCartId(23);
        model.setChannelId("001");
        model.setCreated(new Date());
        Map<String, Object> map = BeanUtils.toMap(model);

        map = JacksonUtil.jsonToMap(JacksonUtil.bean2Json(model));

        CmsBtBrandBlockModel model1 = BeanUtils.toModel(map, CmsBtBrandBlockModel.class);

        System.out.println(JacksonUtil.bean2Json(model));
        System.out.println(JacksonUtil.bean2Json(model1));

    }

    @Test
    public void testProductImportBean() {
        List<ProductImportBean> list = new ArrayList<>();

        ProductImportBean model = new ProductImportBean();
        model.setProductCode("001");
        model.setAppId(123);
        model.setPcId(234);
        model.setLimit(345);
        model.setPromotionTag("456");
        model.setErrorMsg("789");
        model.setDiscount(911.02);

        list.add(model);

        model = new ProductImportBean();
        model.setProductCode("002");
        model.setAppId(1231);
        model.setPcId(2341);
        model.setLimit(3451);
        model.setPromotionTag("4561");
        model.setErrorMsg("7891");
        model.setDiscount(911.03);

        list.add(model);

        List<Map<String, Object>> mapList = BeanUtils.toMapList(list);

        mapList = JacksonUtil.jsonToMapList(JacksonUtil.bean2Json(list));

        List<ProductImportBean> list1 = BeanUtils.toModelList(mapList, ProductImportBean.class);

        System.out.println(JacksonUtil.bean2Json(list));
        System.out.println(JacksonUtil.bean2Json(list1));
    }

    @Test
    public void testSkuImportBean() {
        List<SkuImportBean> list = new ArrayList<>();

        SkuImportBean model = new SkuImportBean();
        model.setProductCode("001");
        model.setSkuCode("A123");
        model.setDealPrice(234.01);
        model.setMarketPrice(345.01);
        model.setErrorMsg("789");
        model.setDiscount(911.02);

        list.add(model);

        model = new SkuImportBean();
        model.setProductCode("002");
        model.setSkuCode("A124");
        model.setDealPrice(135.01);
        model.setMarketPrice(245.01);
        model.setErrorMsg("589");
        model.setDiscount(3411.02);

        list.add(model);

        List<Map<String, Object>> mapList = BeanUtils.toMapList(list);

        mapList = JacksonUtil.jsonToMapList(JacksonUtil.bean2Json(list));

        List<SkuImportBean> list1 = BeanUtils.toModelList(mapList, SkuImportBean.class);

        System.out.println(JacksonUtil.bean2Json(list));
        System.out.println(JacksonUtil.bean2Json(list1));
    }

    @Test
    public void testOrderSearchInfoBean() {
        OrderSearchInfoBean model = new OrderSearchInfoBean();
        model.setStatus(VmsConstants.STATUS_VALUE.PRODUCT_STATUS.OPEN);
        model.setConsolidationOrderId("234");
        model.setSku("sku");
        model.setSize(DEFAULT_PAGE_SIZE);
        model.setCurr(1);
        SortParamBean sortParamBean = new SortParamBean();
        sortParamBean.setColumnName("columnName");
        sortParamBean.setDirection(Order.Direction.ASC);
        model.setSortParamBean(sortParamBean);

        model.setOrderDateFrom(new Date());
        model.setOrderDateTo(new Date());

        System.out.println(JacksonUtil.bean2Json(model));

        Map<String, Object> map = BeanUtils.toMap(model);

        map = JacksonUtil.jsonToMap(JacksonUtil.bean2Json(model));

        OrderSearchInfoBean model1 = BeanUtils.toModel(map, OrderSearchInfoBean.class);

        System.out.println(JacksonUtil.bean2Json(model));
        System.out.println(JacksonUtil.bean2Json(model1));
    }


}
