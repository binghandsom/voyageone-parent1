package com.voyageone.batch.bi.spider.jumei.task;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.bean.modelbean.ShopChannelEcommBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiDealBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiRecordBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiSkuBean;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.job.BaseSpiderService;
import com.voyageone.batch.bi.spider.service.base.JumeiUploadDataService;
import com.voyageone.batch.bi.spider.service.driver.FireFoxDriverService;
import com.voyageone.batch.bi.spider.service.jumei.JumeiUploadService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.batch.core.modelbean.TaskControlBean;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BiGlobalDealUploadTask extends BaseSpiderService {

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
                List<JumeiDealBean> productDealBeanLst = jumeiUploadDataService.getUploadJumeiDealList("", "BHFO003");
                for (JumeiDealBean deal : productDealBeanLst) {

                    JumeiRecordBean jumeiRecord = new JumeiRecordBean();

                    //运行查询URL
                    driver.get("http://a.jumeiglobal.com/GlobalProduct/List");

                    //Boolean allTab = true;
                    JavascriptExecutor js = null;
                    try {
                        if (driver instanceof JavascriptExecutor) {
                            js = (JavascriptExecutor) driver;
                            if (js.executeScript("allTab();") == null) {
                                //allTab = false;
                            }
                        }
                    } catch (Exception e) {
                        //allTab = false;
                    }


                    //System.out.println("aaa:"+strTitleLong);
                    String strTitleLong = deal.getTitle_long();
                    driver.findElement(By.name("search[name]")).sendKeys(strTitleLong + " " + deal.getProduct_code());
                    driver.findElement(By.xpath("//div/button[@onclick='submitFun();']")).click();
                    String strOrderLog = driver.findElement(By.className("tb-top")).getText();
                    boolean isFindProduct = true;
                    if (strOrderLog.contains("共0个商品")) {
                        driver.findElement(By.name("search[name]")).clear();
                        driver.findElement(By.name("search[name]")).sendKeys(deal.getProduct_code());
                        driver.findElement(By.xpath("//div/button[@onclick='submitFun();']")).click();
                        strOrderLog = driver.findElement(By.className("tb-top")).getText();
                        if (strOrderLog.contains("共0个商品")) {
                            logger.info("商品编号" + deal.getProduct_code() + "未上传");
                            jumeiRecord.setChannel_id(deal.getChannel_id());
                            jumeiRecord.setTask_id(deal.getTask_id());
                            jumeiRecord.setProduct_code(deal.getProduct_code());
                            jumeiRecord.setError_type_id(5);
                            String strTitle = deal.getTitle_long() + " " + deal.getProduct_code();
                            jumeiRecord.setJumei_product_name_cn_real(strTitle.replace("'", "\'"));
                            jumeiRecord.setError_message("Deal失败：未找到对应新品");
                            jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
                            isFindProduct = false;
                        }
                    }

                    if (isFindProduct) {
                        //SKU选择
                        List<WebElement> tds = driver.findElements(By.xpath("//tbody/tr/td"));
                        //获得PID
                        WebElement elementPID = tds.get(7);
                        String productID = elementPID.getText();
                        String status = tds.get(2).getText();
                        if (productID != null && !"未生成".equals(status.trim())) {
                            String strProductCode = deal.getProduct_code();
                            //需要修改：task_id
                            List<JumeiSkuBean> skuLst = jumeiUploadDataService.getUploadJumeiSkuList(strProductCode, "BHFO003");
                            //获得dID
                            List<WebElement> a_list = elementPID.findElements(By.xpath("div/div/a"));
                            String dID = null;
                            for (WebElement aTag : a_list) {
                            	if (aTag.getAttribute("innerHTML").indexOf("新建Deal")>=0) {
                            		String url = aTag.getAttribute("href");
                            		Pattern p=Pattern.compile("\\d+"); 
                        			Matcher m=p.matcher(url); 
                        			while(m.find()) { 
                        				dID = m.group();
                        				break;
                        			} 
                            	}
                            	if (dID != null) {
                            		break;
                            	}
                            }
                            //String pid = jumeiUploadDataService.getUploadJumeiID(productID, "");
                            if (dID == null) {
                                jumeiRecord.setChannel_id(deal.getChannel_id());
                                jumeiRecord.setTask_id(deal.getTask_id());
                                jumeiRecord.setProduct_code(deal.getProduct_code());
                                String strTitle = deal.getTitle_long() + " " + deal.getProduct_code();
                                jumeiRecord.setJumei_product_name_cn_real(strTitle.replace("'", "\'"));
                                jumeiRecord.setError_type_id(6);
                                jumeiRecord.setError_message("Deal失败：未找到对应新品");
                                jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
                            } else {

                                jumeiUploadService.uploadDeal(driver, dID, deal, skuLst);
                            }
                        } else {
                            jumeiRecord.setChannel_id(deal.getChannel_id());
                            jumeiRecord.setTask_id(deal.getTask_id());
                            jumeiRecord.setProduct_code(deal.getProduct_code());
                            String strTitle = deal.getTitle_long() + " " + deal.getProduct_code();
                            jumeiRecord.setJumei_product_name_cn_real(strTitle.replace("'", "\'"));
                            jumeiRecord.setError_type_id(15);
                            jumeiRecord.setError_message("Deal失败：对应新品未完成审核");
                            jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
                        }
                    }

                }

            }
        } catch (Exception e) {
            logger.error("Driver Initial execute error", e);
        }
    }

}
