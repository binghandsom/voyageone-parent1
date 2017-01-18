package com.voyageone.service.daoext.report;

import com.voyageone.service.model.report.ShopSalesOfChannel010Model;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dell on 2017/1/16.
 */

@Repository
public interface BiReportSalesShop010DaoExt {
    List<ShopSalesOfChannel010Model> selectListByDate(Object Map);
    BigDecimal selectAmtDateToDate(Object Map);
}
