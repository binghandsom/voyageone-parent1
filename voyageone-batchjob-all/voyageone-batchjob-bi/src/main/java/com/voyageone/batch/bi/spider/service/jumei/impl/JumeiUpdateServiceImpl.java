package com.voyageone.batch.bi.spider.service.jumei.impl;

import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiDealBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiProductBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiRecordBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiSkuBean;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.service.base.JumeiUploadDataService;
import com.voyageone.batch.bi.spider.service.jumei.JumeiUpdateService;
import com.voyageone.batch.bi.util.FileUtils;
import com.voyageone.batch.configs.FileProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Kylin on 2015/7/16.
 */
@Service
public class JumeiUpdateServiceImpl implements JumeiUpdateService {

    private static final Log logger = LogFactory.getLog(JumeiUpdateServiceImpl.class);

    @Autowired
    private JumeiUploadDataService jumeiUploadDataService;

    @Override
    public void updateProduct(WebDriver driver, String strPID, JumeiProductBean jumeiProductBean, List<JumeiSkuBean> skuLst) throws Exception {

        String strJsonCheck = "";
        int index = 0;
        String strDataURL = "http://a.jumeiglobal.com/GlobalProduct/Edit?id=" + strPID;

        while (strJsonCheck.equals("")) {
            try {
                //运行查询URL
                driver.get(strDataURL);

                //获得返回数据
                strJsonCheck = driver.getPageSource();

//                try {
//                    // 产品类目——等级一
//                    Select selectLevel1 = new Select(driver.findElement(By.name("product[category_v3_1]")));
//                    selectLevel1.selectByVisibleText(jumeiProductBean.getJumei_category1());
//
//                    // 产品类目——等级二
//                    Select selectLevel2 = new Select(driver.findElement(By.name("product[category_v3_2]")));
//                    selectLevel2.selectByVisibleText(jumeiProductBean.getJumei_category2());
//
//                    // 产品类目——等级三
//                    Select selectLevel3 = new Select(driver.findElement(By.name("product[category_v3_3]")));
//                    selectLevel3.selectByVisibleText(jumeiProductBean.getJumei_category3());
//
//                    // 产品类目——等级四
//                    Select selectLevel4 = new Select(driver.findElement(By.name("product[category_v3_4]")));
//                    selectLevel4.selectByVisibleText(jumeiProductBean.getJumei_category4());
//                } catch (Exception e) {
//                    JumeiRecordBean jumeiRecord = doErrorRecordProduct(jumeiProductBean);
//                    jumeiRecord.setError_type_id(3);
//                    jumeiRecord.setError_message("上新失败:分类设置失败");
//                    jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
//                }

                // 品牌

//                boolean boolBrand = true;
//                try {
//                    JavascriptExecutor js;
//                    if (driver instanceof JavascriptExecutor) {
//                        js = (JavascriptExecutor) driver;
//                        js.executeScript("document.getElementById('friendly_name').style.display='block'");
//                    }
//                    Select brand = new Select(driver.findElement(By.id("friendly_name")));
//                    List<WebElement> myOptions = brand.getOptions();
//                    for (WebElement option : myOptions) {
//                        String strDescription = option.getAttribute("text");
//                        if (strDescription.indexOf(jumeiProductBean.getJumei_brand()) >= 0) {
//                            brand.selectByVisibleText(strDescription);
//                            boolBrand = false;
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//                    JumeiRecordBean jumeiRecord = doErrorRecordProduct(jumeiProductBean);
//                    jumeiRecord.setError_type_id(9);
//                    jumeiRecord.setError_message("上新失败:品牌设置失败");
//                    jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
//                    boolBrand = false;
//                }
//
//                if (boolBrand) {
//                    JumeiRecordBean jumeiRecord = doErrorRecordProduct(jumeiProductBean);
//                    jumeiRecord.setError_type_id(9);
//                    jumeiRecord.setError_message("上新失败:品牌设置失败");
//                    jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
//                    boolBrand = true;
//                }
//
//                // 产品名
//                String strProductNameCnReal = jumeiProductBean.getJumei_product_name_cn_real().replace("  ", " ");
//                driver.findElement(By.name("product[name]")).sendKeys(strProductNameCnReal);
//
//                // 外文名
//                driver.findElement(By.name("product[foreign_language_name]")).sendKeys(jumeiProductBean.getJumei_product_name_en_real());
//
//                //upload contorls
//                List<WebElement> imageControlList = driver.findElements(By.xpath("//input[@type='file']"));
//
//                //产品主图1
//                try {
//                    uploadImageFile(jumeiProductBean.getProduct_image1(), jumeiProductBean.getImage_url_from1(), imageControlList.get(0));
//                    uploadImageFile(jumeiProductBean.getProduct_image2(), jumeiProductBean.getImage_url_from2(), imageControlList.get(1));
//                    uploadImageFile(jumeiProductBean.getProduct_image3(), jumeiProductBean.getImage_url_from3(), imageControlList.get(2));
//                    uploadImageFile(jumeiProductBean.getProduct_image4(), jumeiProductBean.getImage_url_from4(), imageControlList.get(3));
//                    uploadImageFile(jumeiProductBean.getProduct_image5(), jumeiProductBean.getImage_url_from5(), imageControlList.get(4));
//                    uploadImageFile(jumeiProductBean.getProduct_image6(), jumeiProductBean.getImage_url_from6(), imageControlList.get(5));
//
//                } catch (Exception e) {
//                    JumeiRecordBean jumeiRecord = doErrorRecordProduct(jumeiProductBean);
//                    jumeiRecord.setError_type_id(3);
//                    jumeiRecord.setError_message("上新失败:图片获取失败");
//                    jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
//                }

                int existSkuCount = 0;
                List<WebElement> imageControlList = driver.findElements(By.xpath("//div[@class='sku-table']"));
                if (imageControlList != null) {
                    existSkuCount = imageControlList.size() + 1;
                }

                //子型号信息
                for (int i = 0; i < skuLst.size(); i++) {
                    driver.findElement(By.id("addSpu")).click();
                    JumeiSkuBean skuBean = skuLst.get(i);
                    //规格
                    String spu_property = "spu_" + (i + existSkuCount) + "[property]";
                    Select spu_property_select = new Select(driver.findElement(By.name(spu_property)));
                    spu_property_select.selectByVisibleText("其他");
                    //容量/尺码
                    String spu_size = "spu_" + (i + existSkuCount) + "[size]";
                    driver.findElement(By.name(spu_size)).sendKeys(skuBean.getJumei_size());
                    //型号/颜色
                    String spu_attributes = "spu_" + (i + existSkuCount) + "[attributes]";
                    driver.findElement(By.name(spu_attributes)).sendKeys(skuBean.getJumei_color());
                    //海外官网价
                    String spu_abroad_price = "spu_" + (i + existSkuCount) + "[abroad_price]";
                    String oversea_official_price = new DecimalFormat("#0.00").format(skuBean.getCms_oversea_official_price());
                    driver.findElement(By.name(spu_abroad_price)).sendKeys(oversea_official_price);
                    String spu_area_code = "spu_" + (i + existSkuCount) + "[area_code]";
                    Select spu_area_code_select = new Select(driver.findElement(By.name(spu_area_code)));
                    spu_area_code_select.selectByValue("19");
                }
                //提交审核
                driver.findElement(By.id("checkButton")).click();

                driver.findElement(By.id("dialog-reason")).sendKeys("添加SKU上传");
                WebElement sendControl = driver.findElement(By.xpath("//input[@value='确认送审']"));
                if (sendControl != null) {
                    sendControl.click();
                }

                String currentUrl = driver.getCurrentUrl();
                if (currentUrl.equals("http://a.jumeiglobal.com/GlobalProduct/List")) {
                    insertRecordLog(jumeiProductBean, 33, jumeiProductBean.getProduct_code(), "新SKU上传成功");
                } else {
                    WebElement centerTip = driver.findElement(By.className("centerTip"));
                    String resultHtml = centerTip.findElement(By.tagName("div")).getAttribute("innerHTML");
                    Document resultHtmlDoc = Jsoup.parse(resultHtml);
                    String msg = resultHtmlDoc.text();
                    insertRecordLog(jumeiProductBean, 11, jumeiProductBean.getProduct_code(), "message:=[" + msg + "]");
                }

            } catch (Exception e) {
                insertRecordLog(jumeiProductBean, 1, jumeiProductBean.getProduct_code(), "新SKU上传失败:message:=[" + e.getMessage() + "]");
                logger.info(jumeiProductBean.getProduct_code());
                logger.error(e.getMessage(), e);
            } finally {
                //删除下载的产品主图
//                deleteImageFile(jumeiProductBean.getProduct_image1(), jumeiProductBean.getImage_url_from1());
//                deleteImageFile(jumeiProductBean.getProduct_image2(), jumeiProductBean.getImage_url_from2());
//                deleteImageFile(jumeiProductBean.getProduct_image3(), jumeiProductBean.getImage_url_from3());
//                deleteImageFile(jumeiProductBean.getProduct_image4(), jumeiProductBean.getImage_url_from4());
//                deleteImageFile(jumeiProductBean.getProduct_image5(), jumeiProductBean.getImage_url_from5());
//                deleteImageFile(jumeiProductBean.getProduct_image6(), jumeiProductBean.getImage_url_from6());
            }

            index++;
            if (index > 3) {
                break;
            }
            Thread.sleep(DataSearchConstants.THREAD_SLEEP);
        }
    }


    private void insertRecordLog(JumeiProductBean product, int error_type_id, String name, String message) {
        JumeiRecordBean jumeiRecord = new JumeiRecordBean();
        jumeiRecord.setChannel_id(product.getChannel_id());
        jumeiRecord.setTask_id(product.getTask_id());
        jumeiRecord.setProduct_code(product.getProduct_code());
        jumeiRecord.setError_type_id(error_type_id);
        jumeiRecord.setJumei_product_name_cn_real(name.replace("'", "\'"));
        jumeiRecord.setError_message(message);
        jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
    }

    private boolean uploadImageFile(String product_image, String image_url_from, WebElement imageControl) throws InterruptedException {
        if (image_url_from != null && !"".equals(image_url_from.trim()) && !"0".equals(image_url_from.trim())) {
            String url = image_url_from;
            String fileName = product_image + ".jpg";
            String strSavePath = FileProperties.readValue("bi.jumei.save.path");
            FileUtils.deleteFile(strSavePath, fileName);
            logger.info("uploadImageFile start:" + fileName + ";" + url);
            int index=0;
        	while (index < 5) {
	            try {
	                if (FileUtils.downloadImageWithProxy(url, fileName, strSavePath)) {
	                    imageControl.sendKeys(strSavePath + "\\" + fileName);
	                    logger.info("uploadImageFile end:" + fileName + ";" + url);
	                    return true;
	                }
	            } catch (Exception e) {
	            }
	            index++;
            	Thread.sleep(1000);
        	}
            logger.info("uploadImageFile error:" + fileName + ";" + url);
        }

        return false;
    }

    @SuppressWarnings("unused")
    private void deleteImageFile(String product_image, String image_url_from) {
        if (image_url_from != null && !"".equals(image_url_from.trim()) && !"0".equals(image_url_from.trim())) {
            String fileName = product_image + ".jpg";
            String strSavePath = FileProperties.readValue("bi.jumei.save.path");
            FileUtils.deleteFile(strSavePath, fileName);
        }
    }

    @Override
    public void updateDeal(WebDriver driver, String strDID, JumeiDealBean jumeiDealBean, List<JumeiSkuBean> skuLst) throws Exception {

        String strJsonCheck = "";
        int index = 0;
        String strDataURL = "";
        while (strJsonCheck.equals("")) {
            try {

                Boolean boolDo = false;
                //运行查询URL
                strDataURL = "http://a.jumeiglobal.com/GlobalDeal/Edit?edit=edit&did=" + strDID;
                driver.get(strDataURL);

                //获得返回数据
                strJsonCheck = driver.getPageSource();

                int existSkuCount = 0;
                List<WebElement> imageControlList = driver.findElements(By.xpath("//div[@class='sku-table']"));
                if (imageControlList != null) {
                    existSkuCount = imageControlList.size() + 1;
                }

                //临时子型号信息变更
                for (int k = 0; k < existSkuCount - 1; k++) {
                    //商家商品尺码
                    String size = driver.findElement(By.name("sku_" + k + "[size]")).getAttribute("value");
                    //商家商品编码
                    String strSku = driver.findElement(By.name("sku_" + k + "[businessman_num]")).getAttribute("value");

                    if (strSku.lastIndexOf("H") + 1 == strSku.length()) {
                        driver.findElement(By.name("sku_" + k + "[businessman_num]")).clear();
                        driver.findElement(By.name("sku_" + k + "[businessman_num]")).sendKeys(strSku.substring(0, strSku.lastIndexOf("H")) + "*5");
                        boolDo = true;
                    }
                }

                //既存子型号信息变更
                List<JumeiSkuBean> skuListGet = skuLst;
                for (int k = 0; k < existSkuCount - 1; k++) {
                    //商家商品编码
                    String size = driver.findElement(By.name("sku_" + k + "[size]")).getAttribute("value");

                    for (JumeiSkuBean skuGet : skuListGet) {
                        String strJumeiSize = skuGet.getJumei_size();
                        if (strJumeiSize.equals(size)) {
                            String strSku = skuGet.getSku().replace(".", "*");
                            driver.findElement(By.name("sku_" + k + "[businessman_num]")).clear();
                            driver.findElement(By.name("sku_" + k + "[businessman_num]")).sendKeys(strSku);

                            // 库存
                            driver.findElement(By.name("sku_" + k + "[stocks]")).sendKeys(Integer.toString(skuGet.getInventory()));

                            // 团购价
                            String trade_price = new DecimalFormat("#0.00").format(skuGet.getTrade_price());
                            driver.findElement(By.name("sku_" + k + "[deal_price]")).sendKeys(trade_price);

                            // 市场价
                            String market_price = new DecimalFormat("#0.00").format(skuGet.getMarket_price());
                            driver.findElement(By.name("sku_" + k + "[market_price]")).sendKeys(market_price);

                            skuListGet.remove(skuGet);
                            break;
                        }
                    }
                }

//                // 更新SKU
//                int addSkuCount = 0;
//                List<JumeiSkuBean> skuListGet = skuLst;
//                for (int j = 0; j < skuLst.size(); j++) {
//                    JumeiSkuBean sku = skuLst.get(j);
//                    boolean isExist = false;
//                    //添加SKU
//                    driver.findElement(By.id("addSKU")).click();
//
//                    //SKU选择
//                    WebElement spu_table = driver.findElement(By.id("spu_table"));
//                    List<WebElement> spu_table_trs = spu_table.findElements(By.xpath("tbody/tr"));
//                    int rowsSize = spu_table_trs.size();
//                    for (int i = 0; i < rowsSize; i++) {
//                        WebElement spu_table_tr = spu_table_trs.get(i);
//                        List<WebElement> spu_table_tds = spu_table_tr.findElements(By.tagName("td"));
//                        if (spu_table_tds.size() > 7) {
//                            String spu_size = spu_table_tds.get(7).getText();
//                            String strJumeiSize = sku.getJumei_size();
//                            if (strJumeiSize.equals(spu_size)) {
//                                isExist = true;
//                                spu_table_tds.get(0).findElement(By.tagName("input")).click();
//                                WebElement addSkuControl = driver.findElement(By.xpath("//input[@value='确定']"));
//                                if (addSkuControl != null) {
//                                    addSkuControl.click();
//                                    addSkuCount++;
//                                }
//                                break;
//                            }
//                        }
//                    }
//                    if (!isExist) {
//                        WebElement cancelSkuControl = driver.findElement(By.xpath("//input[@value='取消']"));
//                        if (cancelSkuControl != null) {
//                            cancelSkuControl.click();
//                        }
//                    }
//                }
//
//                //子型号信息
//                for (int j = 0; j < addSkuCount; j++) {
//                    //商家商品编码
//                    String size = driver.findElement(By.name("sku_" + (j + existSkuCount) + "[size]")).getAttribute("value");
//
//                    for (JumeiSkuBean skuGet : skuListGet) {
//                        String strJumeiSize = skuGet.getJumei_size();
//                        if (strJumeiSize.equals(size)) {
//                            String strSku = skuGet.getSku().replace(".", "*");
//                            driver.findElement(By.name("sku_" + (j + existSkuCount) + "[businessman_num]")).clear();
//                            driver.findElement(By.name("sku_" + (j + existSkuCount) + "[businessman_num]")).sendKeys(strSku);
//
//                            // 库存
//                            driver.findElement(By.name("sku_" + (j + existSkuCount) + "[stocks]")).sendKeys("0");
//
//                            // 团购价
//                            String trade_price = new DecimalFormat("#0.00").format(skuGet.getTrade_price());
//                            driver.findElement(By.name("sku_" + (j + existSkuCount) + "[deal_price]")).sendKeys(trade_price);
//
//                            // 市场价
//                            String market_price = new DecimalFormat("#0.00").format(skuGet.getMarket_price());
//                            driver.findElement(By.name("sku_" + (j + existSkuCount) + "[market_price]")).sendKeys(market_price);
//
//                            skuListGet.remove(skuGet);
//                            break;
//                        }
//                    }
//                }


//
//                // 售卖时间(开始)
//                driver.findElement(By.name("deal[start_time]")).clear();
//                driver.findElement(By.name("deal[start_time]")).sendKeys(jumeiDealBean.getOn_sale_time());
//
//                // 售卖时间(结束)
//                driver.findElement(By.name("deal[end_time]")).clear();
//                driver.findElement(By.name("deal[end_time]")).sendKeys(jumeiDealBean.getOff_list_time());
//
//                // 发货仓库
//                Select selectLevel3 = new Select(driver.findElement(By.name("deal[shipping_system_id]")));
//                selectLevel3.selectByVisibleText(jumeiDealBean.getDelivery());
//
//                // 每人限购
//                driver.findElement(By.name("deal[user_purchase_limit]")).clear();
//                driver.findElement(By.name("deal[user_purchase_limit]")).sendKeys(Integer.toString(jumeiDealBean.getLimit()));
//
//                // 长标题
//                driver.findElement(By.name("deal[product_long_name]")).clear();
//                driver.findElement(By.name("deal[product_long_name]")).sendKeys(jumeiDealBean.getTitle_long().replace("  ", " "));
//
//                // 中标题
//                driver.findElement(By.name("deal[product_medium_name]")).clear();
//                driver.findElement(By.name("deal[product_medium_name]")).sendKeys(jumeiDealBean.getTitle_middle().replace("  ", " "));
//
//                // 短标题
//                driver.findElement(By.name("deal[product_short_name]")).clear();
//                driver.findElement(By.name("deal[product_short_name]")).sendKeys(jumeiDealBean.getTitle_short().replace("  ", " "));
//
//                // 生产地区
//                driver.findElement(By.name("deal[address_of_produce]")).clear();
//                driver.findElement(By.name("deal[address_of_produce]")).sendKeys(jumeiDealBean.getJumei_production_place());
//
//                // 保质期限
//                driver.findElement(By.name("deal[before_date]")).clear();
//                driver.findElement(By.name("deal[before_date]")).sendKeys(jumeiDealBean.getQuality_period());
//
//                // 适用人群
//                driver.findElement(By.name("deal[suit_people]")).clear();
//                driver.findElement(By.name("deal[suit_people]")).sendKeys(jumeiDealBean.getApplicable_crowd());
//
//                // 特殊说明
//                driver.findElement(By.name("deal[special_explain]")).clear();
//                driver.findElement(By.name("deal[special_explain]")).sendKeys(jumeiDealBean.getSpecial_description());
//
//                // 自定义搜索词
//                driver.findElement(By.name("deal[search_meta_text_custom]")).clear();
//                driver.findElement(By.name("deal[search_meta_text_custom]")).sendKeys(jumeiDealBean.getCustom_sear_label());
//
//                // 获取编辑框
//                List<WebElement> editors = driver.findElements(By.xpath("//div[@class='ke-container ke-container-default']"));
//
//
//                // 本单详情
//                WebElement editorIframe = editors.get(0);
//                clearEditorContent(editorIframe);
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getDetail(), "ThisDeal", driver, editorIframe);

//                // 使用方法
//                editorIframe = editors.get(1);
//                clearEditorContent(editorIframe);
//                
//                // 获取编辑框
//                WebElement usage = editorIframe.findElement(By.xpath("div/iframe[@class='ke-edit-iframe']"));
//                usage.sendKeys(jumeiDealBean.getCms_long_description());
//                //使用方法图片
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getUsage(), "ThisUsage", driver, editorIframe);

//                // 商品实拍
//                editorIframe = editors.get(2);
//                clearEditorContent(editorIframe);
//
//                //商品实拍1
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getProduct_image_url_from1(), "ThisProduct1", driver, editorIframe);
//                //商品实拍2
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getProduct_image_url_from2(), "ThisProduct2", driver, editorIframe);
//                //商品实拍3
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getProduct_image_url_from3(), "ThisProduct3", driver, editorIframe);
//                //商品实拍4
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getProduct_image_url_from4(), "ThisProduct4", driver, editorIframe);
//                //商品实拍5
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getProduct_image_url_from5(), "ThisProduct5", driver, editorIframe);
//                //商品实拍6
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getProduct_image_url_from6(), "ThisProduct6", driver, editorIframe);
//
//                //购买流程1
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getBuy_process1(), "ThisBuyProcess1", driver, editorIframe);
//                //购买流程2
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getBuy_process2(), "ThisBuyProcess2", driver, editorIframe);
//                //购买流程3
//                uploadEditorImage(jumeiDealBean, jumeiDealBean.getBuy_process3(), "ThisBuyProcess3", driver, editorIframe);

                // 草稿生成
                //driver.findElement(By.id("saveButton")).click();

                //保存并送审
                if (boolDo) {
                    driver.findElement(By.id("checkButton")).click();
                    driver.findElement(By.id("dialog-reason")).sendKeys("修正Sku送审");
                    WebElement sendControl = driver.findElement(By.xpath("//input[@value='确认送审']"));
                    if (sendControl != null) {
                        sendControl.click();
                    }

                    WebElement centerTip = driver.findElement(By.className("centerTip"));
                    String resultHtml = centerTip.findElement(By.tagName("div")).getAttribute("innerHTML");
                    Document resultHtmlDoc = Jsoup.parse(resultHtml);
                    String msg = resultHtmlDoc.text();
                    JumeiRecordBean jumeiRecord = doErrorRecordDeal(jumeiDealBean);
                    if (msg.indexOf("保存送审成功") >= 0) {
                        jumeiRecord.setError_type_id(22);
                        jumeiRecord.setError_message("Deal保存并送审成功[修改Sku:H-->*5]");
                    } else {
                        jumeiRecord.setError_type_id(4);
                        jumeiRecord.setError_message("message:=[" + msg + "]");
                    }
                    jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
                }

            } catch (Exception e) {
                JumeiRecordBean jumeiRecord = doErrorRecordDeal(jumeiDealBean);
                jumeiRecord.setError_type_id(4);
                jumeiRecord.setError_message("Deal失败:" + e.getMessage());
                jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
                logger.info(jumeiDealBean.getProduct_code());
                logger.error(e.getMessage(), e);
            } finally {
//                //删除下载的本单详情
//                deleteImageFile("ThisDeal", jumeiDealBean.getDetail());
//                //删除下载的使用方法
//                deleteImageFile("ThisUsage", jumeiDealBean.getUsage());
//                //删除下载的商品实拍
//                deleteImageFile("ThisProduct1", jumeiDealBean.getProduct_image_url_from1());
//                deleteImageFile("ThisProduct2", jumeiDealBean.getProduct_image_url_from2());
//                deleteImageFile("ThisProduct3", jumeiDealBean.getProduct_image_url_from3());
//                deleteImageFile("ThisProduct4", jumeiDealBean.getProduct_image_url_from4());
//                deleteImageFile("ThisProduct5", jumeiDealBean.getProduct_image_url_from5());
//                deleteImageFile("ThisProduct6", jumeiDealBean.getProduct_image_url_from6());
//                deleteImageFile("ThisBuyProcess1", jumeiDealBean.getBuy_process1());
//                deleteImageFile("ThisBuyProcess2", jumeiDealBean.getBuy_process2());
//                deleteImageFile("ThisBuyProcess3", jumeiDealBean.getBuy_process3());
            }

            index++;
            if (index > 3) {
                break;
            }
            Thread.sleep(DataSearchConstants.THREAD_SLEEP);
        }
    }

    private boolean clearEditorContent(WebElement editorIframe) {
        try {
            WebElement iframe = editorIframe.findElement(By.xpath("div/iframe[@class='ke-edit-iframe']"));
            iframe.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            Thread.sleep(100);
            iframe.sendKeys(Keys.DELETE);
            Thread.sleep(100);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    private boolean uploadEditorImage(JumeiDealBean jumeiDealBean, String image, String fileName, WebDriver driver, WebElement editorIframe) {
        if (image != null && !"".equals(image.trim()) && !"0".equals(image.trim())) {
            try {
                editorIframe.findElement(By.xpath("div/span[@title='图片']")).click();
                Thread.sleep(500);
                if (image.toLowerCase().indexOf("jmstatic.com") > 0) {
                    driver.findElement(By.id("remoteUrl")).sendKeys(image);
                    driver.findElement(By.xpath("//span/input[@value='确定']")).click();
                } else {
                    //driver.findElement(By.xpath("//ul/li[@class='ke-tabs-li']")).click();
                    WebElement tabs_ul = driver.findElement(By.xpath("//ul[@class='ke-tabs-ul ke-clearfix']"));
                    List<WebElement> tabs = tabs_ul.findElements(By.tagName("li"));
                    if (tabs.size() > 1) {
                        tabs.get(1).click();
                    }
                    Thread.sleep(500);
                    if (uploadImageFile("ThisBuyProcess1", image, driver.findElement(By.xpath("//div/input[@type='file']")))) {
                        driver.findElement(By.xpath("//span/input[@value='确定']")).click();
                        Thread.sleep(500);
                    } else {
                        driver.findElement(By.xpath("//span/input[@value='取消']")).click();
                        Thread.sleep(500);
                    }
                }
                return true;
            } catch (Exception e) {
                JumeiRecordBean jumeiRecord = doErrorRecordDeal(jumeiDealBean);
                jumeiRecord.setError_type_id(7);
                jumeiRecord.setError_message("Deal失败：Deal图片" + image + "不符合要求");
                jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
            }
        }
        return false;
    }

    private JumeiRecordBean doErrorRecordDeal(JumeiDealBean jumeiDealBean) {

        JumeiRecordBean jumeiRecord = new JumeiRecordBean();

        jumeiRecord.setChannel_id(jumeiDealBean.getChannel_id());
        jumeiRecord.setTask_id(jumeiDealBean.getTask_id());
        jumeiRecord.setProduct_code(jumeiDealBean.getProduct_code());
        String strTitle = jumeiDealBean.getTitle_long() + " " + jumeiDealBean.getProduct_code();
        jumeiRecord.setJumei_product_name_cn_real(strTitle.replace("'", "\'"));

        return jumeiRecord;
    }
}
