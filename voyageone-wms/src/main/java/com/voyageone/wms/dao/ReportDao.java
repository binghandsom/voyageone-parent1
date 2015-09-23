package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.wms.formbean.FormReportBean;
import com.voyageone.wms.formbean.FormStocktake;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.dao]  
 * @ClassName    [ReportDao]
 * @Description  [Report持久层类]
 * @Author       [sky]   
 * @CreateDate   [20150720]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Repository
public class ReportDao extends BaseDao {

    /**
     * @Description 获得inventoryDetail报表的内容
     * @param formReportBean 对象
     * @return list FormReportBean对象集合
     */
    public List<FormReportBean> getInvDelList(FormReportBean formReportBean) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_report_getInvDelList", formReportBean);
    }
}
