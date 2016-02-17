package com.voyageone.batch.bi.spider.jumei.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.bean.modelbean.ShopChannelEcommBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiProductAddedBean;
import com.voyageone.batch.bi.mapper.JumeiMapper;
import com.voyageone.batch.bi.mapper.UserMapper;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.job.BaseSpiderService;
import com.voyageone.batch.bi.spider.service.driver.FireFoxDriverService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.batch.core.modelbean.TaskControlBean;

@Service
public class BiGlobalProductListTask extends BaseSpiderService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FireFoxDriverService fireFoxDriverService;
    @Autowired
    private JumeiMapper jumeiMapper;
    
    
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
        	user.setDb_name("test");
            if (UtilCheckData.checkUser(user)) {
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
                
                String pageSource = getListPageSource(driver, 1);
				pageSource = pageSource.replace("xmlns:a0=\"http://www.w3.org/1999/xhtml\" ", "");
				pageSource = pageSource.replace("a0:", "");
                Document doc = getDocument(pageSource);
                int pageSize = getPageSizeCount(doc);
                List<JumeiProductAddedBean> allRows = new ArrayList<JumeiProductAddedBean>();
                if (pageSize>0) {
                	List<JumeiProductAddedBean> rows = parseProductList(doc);
                	allRows.addAll(rows);
                	
                	//next page
                	for (int i=2; i<=pageSize; i++) {
                		rows = getProductList(driver, i);
                    	allRows.addAll(rows);
                    	System.out.println("GET ROWS:" + String.valueOf(allRows.size()));
                    }
                	System.out.println("GET ALL ROWS:" + String.valueOf(allRows.size()));
                	
                	for (int i=0; i<pageSize; i++) {
                		int fromIndex = i*20;
                		int toIndex = (i+1)*20;
                		if (toIndex>allRows.size()) {
                			toIndex = allRows.size();
                		}
                		System.out.println("fromIndex:"+String.valueOf(fromIndex)+";toIndex:"+String.valueOf(toIndex)+"");
                		rows = allRows.subList(fromIndex, toIndex);
                    	//setItemValue(driver, rows);
                    	for(JumeiProductAddedBean row : rows) {
                    		row.setNameEn("");
                    		System.out.println(row.toString());
                    	}
                    	saveProductListToDB(rows);
                    	System.out.println("GET ALL ROWS fromIndex:"+String.valueOf(fromIndex)+";toIndex:"+String.valueOf(toIndex)+"");
                	}
                }
            }
        } catch (Exception e) {
            logger.error("Driver Initial execute error", e);
        }
    }
    
    private String getListPageSource(WebDriver driver, int page) throws InterruptedException {
        String strJsonCheck = "";
        int index = 0;
        String strDataURL =  "http://a.jumeiglobal.com//GlobalProduct/List";
        while (strJsonCheck.equals("")) {
            try {
                //运行查询URL
            	if ( page == 1) {
                    driver.get(strDataURL);
                    JavascriptExecutor js = null;
                    try {
                        if (driver instanceof JavascriptExecutor) {
                            js = (JavascriptExecutor) driver;
                            js.executeScript("allTab();");
                        }
                    } catch (Exception e) {
                    }
            	} else {
                    List<WebElement> page_btns = driver.findElements(By.xpath("//a[@class=\"page_btn\"]"));
                    if (page_btns.size()>2) {
                    	WebElement nextPageBtn = page_btns.get(page_btns.size()-2);
                    	nextPageBtn.click();
                    }
            	}
                
                //获得返回数据
                strJsonCheck = driver.getPageSource();
                if (checkPageSource(strJsonCheck)) {
                    return strJsonCheck;
                } else {
                    strJsonCheck = "";
                }
            } catch (Exception e) {
                logger.info(driver.getPageSource());
                logger.error(e.getMessage(), e);
                strJsonCheck = "";
            }

            index++;
            if (index > 3) {
                break;
            }
            Thread.sleep(DataSearchConstants.THREAD_SLEEP);
        }
        return strJsonCheck;
    }
    
    private boolean checkPageSource(String pageSource) {
    	boolean result = false;
    	if (pageSource != null &&
    			(pageSource.indexOf("聚美国际产品库")>0 || pageSource.indexOf("产品信息")>0)
    		){
    		result = true;
    	}
    	return result;
    }
    
	private Document getDocument(String pageSource)  {
		return Jsoup.parse(pageSource);
	}
	
	private int getPageSizeCount(Document doc)  {
		int count = getProductCount(doc);
        int pageSize = count/20;
        if (count%20>0) {
        	pageSize++;
        }
        return pageSize;
	}
	
	private int getProductCount(Document doc)  {
		int result = 0;
		Elements tb_tops = doc.body().getElementsByClass("tb-top");
		if (tb_tops.size()>0) {
			Element tb_top = tb_tops.get(0);
			String text  = tb_top.text();
			Pattern p=Pattern.compile("\\d+"); 
			Matcher m=p.matcher(text); 
			while(m.find()) { 
			     result = Integer.parseInt(m.group());
			} 
		}
		return result;
	}
	
	private List<JumeiProductAddedBean> getProductList(WebDriver driver, int pageIndex) throws InterruptedException {
		String pageSource = getListPageSource(driver, pageIndex);
		pageSource = pageSource.replace("xmlns:a0=\"http://www.w3.org/1999/xhtml\" ", "");
		pageSource = pageSource.replace("a0:", "");
	    Document doc = getDocument(pageSource);
	    return parseProductList(doc);
	}
	
	private List<JumeiProductAddedBean> parseProductList(Document doc) throws InterruptedException {
		Thread.sleep(1000);
		List<JumeiProductAddedBean> productLst = new ArrayList<>();
		Elements tbodys = doc.body().getElementsByTag("tbody");
		if (tbodys.size()>0) {
			Element tbody = tbodys.get(0);
			Elements trs = tbody.getElementsByTag("tr");
			for (Element tr : trs) {
				Elements tds = tr.getElementsByTag("td");
				int index = 1;
				JumeiProductAddedBean row = new JumeiProductAddedBean();
				for (Element td : tds) {
					if (index == 2) {
						row.setName(td.text());
					} else if (index == 3) {
						row.setCode(td.text());
					} else if (index == 4) {
						row.setBrand(td.text());
					} else if (index == 5) {
						row.setCategory(td.text());
					} else if (index == 6) {
						//row.sub=td.text();
					} else if (index == 7) {
						String jumeiId = td.attr("id");
						if (jumeiId != null) {
							jumeiId = jumeiId.replaceAll("status_", "");
						}
						row.setJumeiId(jumeiId);
						row.setStatus(td.text());
					}
					index++;
				}
				if (row.getName() != null) {
					row.setCreateTime(new Date());
					row.setUpdateTime(new Date());
					productLst.add(row);
				}
			}
		}
		return productLst;
	}
	
	private void saveProductListToDB(List<JumeiProductAddedBean> list) {
		if (list != null && list.size()>0) {
	    	HashMap<String, List<JumeiProductAddedBean>> insertMap = new HashMap<String, List<JumeiProductAddedBean>>();
	    	insertMap.put("value", list);
			jumeiMapper.insert_ims_jumei_product_added(insertMap);
		}
	}
	
    @SuppressWarnings("unused")
	private void setItemValue(WebDriver driver, List<JumeiProductAddedBean> rows) throws InterruptedException {
        for (JumeiProductAddedBean row : rows) {
        	String pageSource = getItemPageSource(driver, row);
    	    Document doc = getDocument(pageSource);
    	    setItemPageValue(row, doc);
        }
    }
    
    private String getItemPageSource(WebDriver driver, JumeiProductAddedBean row) throws InterruptedException {
        String strJsonCheck = "";
        int index = 0;
        String strDataURL =  "http://a.jumeiglobal.com/GlobalProduct/View?id=";
        strDataURL  = strDataURL + row.getJumeiId();
        while (strJsonCheck.equals("")) {
            try {
                //运行查询URL
            	driver.get(strDataURL);
                //获得返回数据
                strJsonCheck = driver.getPageSource();
                if (checkPageSource(strJsonCheck)) {
                    return strJsonCheck;
                } else {
                    strJsonCheck = "";
                }
            } catch (Exception e) {
                logger.info(driver.getPageSource());
                logger.error(e.getMessage(), e);
                strJsonCheck = "";
            }

            index++;
            if (index > 3) {
                break;
            }
            Thread.sleep(DataSearchConstants.THREAD_SLEEP);
        }
        return strJsonCheck;
    }
    
	private void setItemPageValue(JumeiProductAddedBean row, Document doc) throws InterruptedException {
		Thread.sleep(500);
		Element basicForm = doc.body().getElementById("basicForm");
		if (basicForm != null) {
			Elements form_groups = basicForm.getElementsByClass("form-group");
			for (Element form_group : form_groups) {
				Elements labels = form_group.getElementsByTag("label");
				if (labels != null && labels.size()>0) {
					Element label = labels.get(0);
					if ("外文名".equals(label.text())) {
						Elements divs = form_group.getElementsByTag("div");
						if (divs != null && divs.size()>1) {
							Element div = divs.get(1);
							row.setNameEn(div.text());
						}
					}
				}
			}
		}
	}
}
