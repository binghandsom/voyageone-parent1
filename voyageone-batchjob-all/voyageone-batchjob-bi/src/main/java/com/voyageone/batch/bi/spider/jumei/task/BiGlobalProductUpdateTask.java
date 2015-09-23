package com.voyageone.batch.bi.spider.jumei.task;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.voyageone.batch.bi.spider.service.jumei.JumeiUpdateService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.batch.core.modelbean.TaskControlBean;

@Service
public class BiGlobalProductUpdateTask extends BaseSpiderService {

    @Autowired
    private FireFoxDriverService fireFoxDriverService;
    @Autowired
    private JumeiUploadDataService jumeiUploadDataService;
    @Autowired
    private JumeiUpdateService jumeiUpdateService;

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
            user.setChannel_code("SN");
            if (UtilCheckData.checkUser(user)) {
                UtilCheckData.setLocalUser(user);

                DriverConfigBean driverConfigBean = new DriverConfigBean();
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
                List<JumeiProductBean> productBeanLst = jumeiUploadDataService.getUploadJumeiProductList("SN00104");
                for (JumeiProductBean product : productBeanLst) {
                	
                    String strProductCode = product.getProduct_code();
                    
                    //运行查询URL
                    driver.get("http://a.jumeiglobal.com/GlobalProduct/List");
                    
                  //Boolean allTab = true;
                    JavascriptExecutor js = null;
                    try {
                        if (driver instanceof JavascriptExecutor) {
                            js = (JavascriptExecutor) driver;
                            if(js.executeScript("allTab();") == null){
                               //allTab = false;
                            }
                        }
                    } catch (Exception e) {
                        //allTab = false;
                    }
                    
                    driver.findElement(By.name("search[name]")).sendKeys(product.getProduct_code());
                    driver.findElement(By.xpath("//div/button[@onclick='submitFun();']")).click();
                    String strOrderLog = driver.findElement(By.className("tb-top")).getText();
                    boolean isFindProduct = true;
                    if (strOrderLog.contains("共0个商品")) {
                        	logger.info("商品编号" + product.getProduct_code() + "未上传");
                        	insertRecordLog(product, 5, product.getProduct_code(), "ProductUpdate失败：未找到对应新品");
                            isFindProduct = false;
                    }
                
					if (isFindProduct) {
						// SKU选择
						List<WebElement> tds = driver.findElements(By.xpath("//tbody/tr/td"));
						// 获得PID
						WebElement elementPID = tds.get(2);
						String productID = elementPID.getText();
						if (productID != null && !"未生成".equals(productID.trim())) {
							// 需要修改：task_id
							List<JumeiSkuBean> skuLst = jumeiUploadDataService.getUploadJumeiSkuList(strProductCode, "SN00104");
							String pid = jumeiUploadDataService.getUploadJumeiID(productID, "");
							if (pid == null) {
	                        	insertRecordLog(product, 5, product.getProduct_code(), "ProductUpdate失败：未找到对应新品(pid not found)");
							} else {
								jumeiUpdateService.updateProduct(driver, pid, product, skuLst);
							}
						} else {
							insertRecordLog(product, 6, product.getProduct_code(), "ProductUpdate失败：对应新品未完成审核");
						}
					}
                    
                }

            }
        } catch (Exception e) {
            logger.error("Driver Initial execute error", e);
        }
    }
    
    private void insertRecordLog(JumeiProductBean product, int error_type_id, String name, String message) {
    	JumeiRecordBean jumeiRecord  = new JumeiRecordBean();
        jumeiRecord.setChannel_id(product.getChannel_id());
        jumeiRecord.setTask_id(product.getTask_id());
        jumeiRecord.setProduct_code(product.getProduct_code());
        jumeiRecord.setError_type_id(error_type_id);
        jumeiRecord.setJumei_product_name_cn_real(name.replace("'", "\'"));
        jumeiRecord.setError_message(message);
        jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
    }


}
