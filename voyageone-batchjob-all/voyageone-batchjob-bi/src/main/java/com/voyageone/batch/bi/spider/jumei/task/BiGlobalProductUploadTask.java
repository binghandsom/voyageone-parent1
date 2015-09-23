package com.voyageone.batch.bi.spider.jumei.task;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.bean.modelbean.ShopChannelEcommBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiProductBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiRecordBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiSkuBean;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.job.BaseSpiderService;
import com.voyageone.batch.bi.spider.service.base.JumeiUploadDataService;
import com.voyageone.batch.bi.spider.service.driver.FireFoxDriverService;
import com.voyageone.batch.bi.spider.service.jumei.JumeiUploadService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BiGlobalProductUploadTask extends BaseSpiderService {

    @Autowired
    private FireFoxDriverService fireFoxDriverService;
    @Autowired
    private JumeiUploadDataService jumeiUploadDataService;
    @Autowired
    private JumeiUploadService jumeiUploadService;

    @Override
    public String getTaskName() {
        return "FireFoxDriverInitialService";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        try {
            FormUser user = new FormUser();
            user.setUser_name("Voyageone");
            user.setUser_ps("voyage1@la&SH");
            //需要修改：data_base
            user.setDb_name("test");
            //需要修改：channel_id
            user.setChannel_code("BHFO");
            if (UtilCheckData.checkUser(user)) {
                UtilCheckData.setLocalUser(user);

                DriverConfigBean driverConfigBean = new DriverConfigBean();//DriverConfigs.getDriver(user.getShop_id());
                ShopChannelEcommBean shopBean = new ShopChannelEcommBean();
                driverConfigBean.setShopBean(shopBean);
                shopBean.setEcomm_id(DataSearchConstants.ECOMM_JM);
                shopBean.setLogin_url("https://v.jumei.com/v1/api/login?app_id=e27e3ecab118&request_uri=http://a.jumeiglobal.com/&tag=9");
                shopBean.setUser_name(user.getUser_name());
                shopBean.setUser_ps(user.getUser_ps());
                shopBean.setReflash_url("http://a.jumeiglobal.com/");

                fireFoxDriverService.initialLocalLoginFireFoxDriver(driverConfigBean);
                WebDriver driver = driverConfigBean.getInitial_driver();
                //需要修改：task_id
                List<JumeiProductBean> productBeanLst = jumeiUploadDataService.getUploadJumeiProductList("BHFO003");
                for (JumeiProductBean product : productBeanLst) {

                    JumeiRecordBean jumeiRecord = new JumeiRecordBean();
                    String strProductCode = product.getProduct_code();
                    //需要修改：task_id
                    List<JumeiSkuBean> skuLst = jumeiUploadDataService.getUploadJumeiSkuList(strProductCode, "BHFO003");
                    if(skuLst.size()>0){
                        jumeiUploadService.uploadProduct(driver, product, skuLst);
                    }else {
                        jumeiRecord.setChannel_id(product.getChannel_id());
                        jumeiRecord.setTask_id(product.getTask_id());
                        jumeiRecord.setProduct_code(product.getProduct_code());
                        String strTitle = product.getJumei_product_name_cn_real();
                        jumeiRecord.setJumei_product_name_cn_real(strTitle.replace("'", "\'"));
                        jumeiRecord.setError_type_id(11);
                        jumeiRecord.setError_message("Deal失败：对应新品没有sku数据");
                        jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Driver Initial execute error", e);
        }
    }


}
