package com.voyageone.bi.task;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.ajax.bean.AjaxSalesDetailBean;
import com.voyageone.bi.base.BiApplication;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.BiFileUtils;
import com.voyageone.bi.commonutils.ConditionUtil;
import com.voyageone.bi.commonutils.ConditionUtil.Dimension;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.dao.mondrian.MondrianSalesUtils;
import com.voyageone.bi.disbean.BaseKpiDisBean;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.task.sup.SalesBrandTask;
import com.voyageone.bi.task.sup.SalesBuyChannelTask;
import com.voyageone.bi.task.sup.SalesCategoryTask;
import com.voyageone.bi.task.sup.SalesColorTask;
import com.voyageone.bi.task.sup.SalesModelTask;
import com.voyageone.bi.task.sup.SalesProductTask;
import com.voyageone.bi.task.sup.SalesShopTask;
import com.voyageone.bi.task.sup.SalesSizeTask;
import com.voyageone.bi.task.sup.SalesSkuTask;
import com.voyageone.bi.task.sup.SalesSumTask;
import com.voyageone.bi.task.sup.SalesTimeTask;
import com.voyageone.bi.tranbean.UserInfoBean;

@Service
public class SalesDetailTask {
	private static Log logger = LogFactory.getLog(SalesHomeTask.class);
	
	// 按时间
	@Autowired
	private SalesTimeTask salesTimeTask;
	@Autowired
	private SalesSumTask salesSumTask;
	// 按销售渠道
	@Autowired
	private SalesBuyChannelTask salesBuyChannelTask;
	// 按销售门店
	@Autowired
	private SalesShopTask salesShopTask;
	// 按品牌
	@Autowired
	private SalesBrandTask salesBrandTask;
	// 按品类
	@Autowired
	private SalesCategoryTask salesCategoryTask;
	// 按品类
	@Autowired
	private SalesColorTask salesColorTask;
	// 按品类
	@Autowired
	private SalesSizeTask salesSizeTask;
	// 按款式
	@Autowired
	private SalesModelTask salesModelTask;
	// 按产品
	@Autowired
	private SalesProductTask salesProductTask;
	// 按SKU
	@Autowired
	private SalesSkuTask salesSkuTask;
	
	// TimeLine
	public void ajaxGetSalesTimeLineData(AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesDetailBean.Result result = bean.getResponseBean();
		
		try{
			// TimeLine
			ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.TimeLine, userInfoBean);
			List<ChartGridDisBean> timeLineDisBean = salesTimeTask.getSalesTimeLineData(condition, userInfoBean);
			List<ChartGridDisBean> timeLineDisExtendBean = salesTimeTask.getSalesExtendTimeLineData(condition, userInfoBean);
			MondrianSalesUtils.mergeExtendBeanList(timeLineDisBean, timeLineDisExtendBean);
			result.setTimeLineDisBean(timeLineDisBean);
			
			// TimeLineSum
			condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.None, userInfoBean);
			ChartGridDisBean sumKpiBean = salesSumTask.getSalesSumData(condition, userInfoBean);
			result.setTimeLineSumDisBean(sumKpiBean);
			// TimeLine PC vs Mobile Sum
			result.setTimeLineSumPcMobileDisBean(salesBuyChannelTask.getSalesByBuyChannel(condition, userInfoBean));
			// TimeLine Shops Sum
			result.setTimeLineSumShopDisBean(salesShopTask.getSalesByShops(condition, userInfoBean));
			
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxGetSalesTimeLineData");
		}
	}
	
	//ajaxGetSalesTimeLineDataExcel KPI
	public String ajaxGetSalesTimeLineDataExcel(HttpServletRequest request, AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.TimeLine, userInfoBean);
		List<ChartGridDisBean> timeLineDisBeanList = salesTimeTask.getSalesTimeLineData(condition, userInfoBean);
		List<ChartGridDisBean> timeLineDisExtendBean = salesTimeTask.getSalesExtendTimeLineData(condition, userInfoBean);
		MondrianSalesUtils.mergeExtendBeanList(timeLineDisBeanList, timeLineDisExtendBean);
		
		String strExcelFileName = "";
		try{
			strExcelFileName = createExcelFile(request, timeLineDisBeanList, "bi_sales_detail_template_d", false);
		} catch(Exception e) {
			logger.error("ajaxGetSalesTimeLineDataExcel error", e);
		}
		return strExcelFileName;
	}	
	
	//ajaxGetSalesBrandData KPI
	public void ajaxGetSalesBrandData(AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesDetailBean.Result result = bean.getResponseBean();
		
		try{
			ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Brand, userInfoBean);
			result.setBrandDisBean(salesBrandTask.getSalesBrandInfo(condition, userInfoBean));
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxGetSalesBrandData");
		}
	}
	
	//ajaxGetSalesBrandDataExcel KPI
	public String ajaxGetSalesBrandDataExcel(HttpServletRequest request, AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Brand, userInfoBean);
		List<ChartGridDisBean> brandList = salesBrandTask.getSalesBrandInfo(condition, userInfoBean);
		
		String strExcelFileName = "";
		try{
			strExcelFileName = createExcelFile(request, brandList, "bi_sales_detail_template_d", false);
		} catch(Exception e) {
			logger.error("ajaxGetSalesBrandDataExcel error", e);
		}
		return strExcelFileName;
	}
	
	//ajaxGetSalesCategoryData KPI
	public void ajaxGetSalesCategoryData(AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesDetailBean.Result result = bean.getResponseBean();
		
		try{
			ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Category, userInfoBean);
			result.setCategoryDisBean(salesCategoryTask.getCategoryBeanLst(condition, userInfoBean));
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxGetSalesCategoryData");
		}
	}
	
	//ajaxGetSalesCategoryDataExcel KPI
	public String ajaxGetSalesCategoryDataExcel(HttpServletRequest request, AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Category, userInfoBean);
		List<ChartGridDisBean> categoryList = salesCategoryTask.getCategoryBeanLst(condition, userInfoBean);
		
		String strExcelFileName = "";
		try{
			strExcelFileName = createExcelFile(request, categoryList, "bi_sales_detail_template_d", false);
		} catch(Exception e) {
			logger.error("ajaxGetSalesCategoryDataExcel error", e);
		}
		return strExcelFileName;
	}
	
	//ajaxGetSalesColorData KPI
	public void ajaxGetSalesColorData(AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesDetailBean.Result result = bean.getResponseBean();
		
		try{
			ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Color, userInfoBean);
			result.setColorDisBean(salesColorTask.getSalesColorInfo(condition, userInfoBean));
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxGetSalesCategoryData");
		}
	}
	
	//ajaxGetSalesColorDataExcel KPI
	public String ajaxGetSalesColorDataExcel(HttpServletRequest request, AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Color, userInfoBean);
		List<ChartGridDisBean> colorList = salesColorTask.getSalesColorInfo(condition, userInfoBean);
		
		String strExcelFileName = "";
		try{
			strExcelFileName = createExcelFile(request, colorList, "bi_sales_detail_template_d", false);
		} catch(Exception e) {
			logger.error("ajaxGetSalesColorDataExcel error", e);
		}
		return strExcelFileName;
	}
	
	//ajaxGetSalesSizeData KPI
	public void ajaxGetSalesSizeData(AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesDetailBean.Result result = bean.getResponseBean();
		
		try{
			ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Size, userInfoBean);
			result.setSizeDisBean(salesSizeTask.getSalesSizeInfo(condition, userInfoBean));
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxGetSalesCategoryData");
		}
	}

	//ajaxGetSalesSizeDataExcel KPI
	public String ajaxGetSalesSizeDataExcel(HttpServletRequest request, AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Size, userInfoBean);
		List<ChartGridDisBean> sizeList = salesSizeTask.getSalesSizeInfo(condition, userInfoBean);
		
		String strExcelFileName = "";
		try{
			strExcelFileName = createExcelFile(request, sizeList, "bi_sales_detail_template_d", false);
		} catch(Exception e) {
			logger.error("ajaxGetSalesSizeDataExcel error", e);
		}
		return strExcelFileName;
	}
	
	//ajaxGetSalesModelData KPI
	public void ajaxGetSalesModelData(AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesDetailBean.Result result = bean.getResponseBean();
		
		try{
			ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Product, userInfoBean);
			result.setModelDisBean(salesModelTask.getTopSalesModelInfo(condition, userInfoBean, 100));
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxGetSalesModelData");
		}
	}
	
	//ajaxGetSalesModelDataExcel KPI
	public String ajaxGetSalesModelDataExcel(HttpServletRequest request, AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Product, userInfoBean);
		List<ChartGridDisBean> modelList = salesModelTask.getTopSalesModelInfo(condition, userInfoBean, 0);
		
		String strExcelFileName = "";
		try{
			strExcelFileName = createExcelFile(request, modelList, "bi_sales_detail_template_p", true);
		} catch(Exception e) {
			logger.error("ajaxGetSalesModelDataExcel error", e);
		}
		return strExcelFileName;
	}
	
	//ajaxGetSalesProductData KPI
	public void ajaxGetSalesProductData(AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesDetailBean.Result result = bean.getResponseBean();
		
		try{
			ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Product, userInfoBean);
			result.setProductDisBean(salesProductTask.getTopSalesProductInfo(condition, userInfoBean, 100));
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxGetSalesCategoryData");
		}
	}
	
	//ajaxGetSalesProductDataExcel KPI
	public String ajaxGetSalesProductDataExcel(HttpServletRequest request, AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.Product, userInfoBean);
		List<ChartGridDisBean> productList = salesProductTask.getTopSalesProductInfo(condition, userInfoBean, 0);
		
		String strExcelFileName = "";
		try{
			strExcelFileName = createExcelFile(request, productList, "bi_sales_detail_template_p", true);
		} catch(Exception e) {
			logger.error("ajaxGetSalesProductDataExcel error", e);
		}
		return strExcelFileName;
	}
	
	//ajaxGetSalesSkuData KPI
	public void ajaxGetSalesSkuData(AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesDetailBean.Result result = bean.getResponseBean();
		
		try{
			ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.SKU, userInfoBean);
			result.setProductDisBean(salesSkuTask.getTopSalesSkuByProduct(condition, userInfoBean, 100));
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxGetSalesCategoryData");
		}
	}
	
	//ajaxGetSalesSkuDataExcel KPI
	public String ajaxGetSalesSkuDataExcel(HttpServletRequest request, AjaxSalesDetailBean bean, UserInfoBean userInfoBean) throws BiException {
		ConditionBean condition = ConditionUtil.getSalesDetailCondition(bean, Dimension.SKU, userInfoBean);
		List<ChartGridDisBean> skuList = salesSkuTask.getTopSalesSkuByProduct(condition, userInfoBean, 0);

		String strExcelFileName = "";
		try{
			strExcelFileName = createExcelFile(request, skuList, "bi_sales_detail_template_p", true);
		} catch(Exception e) {
			logger.error("ajaxGetSalesSkuDataExcel error", e);
		}
		return strExcelFileName;
	}
	
	private String createExcelFile(HttpServletRequest request, List<ChartGridDisBean> rowList, String tmplateFile, boolean isExistId) throws Exception {
		//template file 绝对路径
		String strWebRootPath = BiFileUtils.getAbsolutRootPath(request);
		String strTemplateFile = strWebRootPath + BiApplication.readValue("finance_report_path") +"module/" + tmplateFile + ".xls";
		
		//excel file 绝对路径
		UUID uuid = UUID.randomUUID();
		String strExcelFileName = strWebRootPath + BiApplication.readValue("finance_report_path") + uuid.toString()+".xls";
				
		WritableWorkbook writableWorkbook = getTemplateWorkBook(strTemplateFile, strExcelFileName);
        if (writableWorkbook != null) {
        	if (rowList != null) {
        		exportProductListExcel(writableWorkbook, rowList, isExistId);
        	}
            writableWorkbook.write();
            writableWorkbook.close();
        }
        return strExcelFileName;
	}

	private WritableWorkbook getTemplateWorkBook(String strTemplateFile, String strExcelFileName) throws Exception {
        Workbook workbook = Workbook.getWorkbook(new File(strTemplateFile));
		return Workbook.createWorkbook(new File(strExcelFileName), workbook);
	}
	
	private void exportProductListExcel(WritableWorkbook workbook, List<ChartGridDisBean> beanList, boolean isExistId) throws Exception {
        // 生成一个表格
        WritableSheet writableSheet = workbook.getSheet(0);
        if (writableSheet != null) {
            Cell cell = writableSheet.findCell("$start_data$");
            int iColumn = cell.getColumn();
            int iRow = cell.getRow();
            Label label1 = new Label(iColumn, iRow, "");
            writableSheet.addCell(label1.copyTo(iColumn, iRow));
            
            for (int indexRow = 0; indexRow < beanList.size(); indexRow++) {
                int indexColumn = 0;
                ChartGridDisBean row = beanList.get(indexRow);
                if (isExistId) {
                    //id
                    if (row.getValue() != null) {
                        Label label = new Label(iColumn + indexColumn, iRow + indexRow, row.getValue());
                        writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                    }
                    indexColumn++;
                }

                //Name
                if (row.getTitle() != null) {
                    Label label = new Label(iColumn + indexColumn, iRow + indexRow, String.valueOf(row.getTitle()));
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;

                //quantity
                BaseKpiDisBean kpiBean = row.getQty_kpi();
                if (kpiBean.getValue() != null) {
                    Number label = new Number(iColumn + indexColumn, iRow + indexRow, Integer.parseInt(kpiBean.getValue()));
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                if (kpiBean.getValue_r_rate_up() != null) {
                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_r_rate_up())*100);
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                if (kpiBean.getValue_y_rate_up() != null) {
                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_y_rate_up())*100);
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                
                //Amount
                kpiBean = row.getAmt_kpi();
                if (kpiBean.getValue() != null) {
                    Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue()));
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                if (kpiBean.getValue_r_rate_up() != null) {
                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_r_rate_up())*100);
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                if (kpiBean.getValue_y_rate_up() != null) {
                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_y_rate_up())*100);
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                
                //Order
                kpiBean = row.getOrder_kpi();
                if (kpiBean.getValue() != null) {
                    Number label = new Number(iColumn + indexColumn, iRow + indexRow, Integer.parseInt(kpiBean.getValue()));
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                if (kpiBean.getValue_r_rate_up() != null) {
                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_r_rate_up())*100);
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                if (kpiBean.getValue_y_rate_up() != null) {
                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_y_rate_up())*100);
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                
                //ATV
                kpiBean = row.getAtv_kpi();
                if (kpiBean.getValue() != null) {
                    Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue()));
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                if (kpiBean.getValue_r_rate_up() != null) {
                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_r_rate_up())*100);
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                if (kpiBean.getValue_y_rate_up() != null) {
                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_y_rate_up())*100);
                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
                }
                indexColumn++;
                
                //UV
                if (!isExistId) {
	                kpiBean = row.getUv_kpi();
	                if (kpiBean.getValue() != null) {
	                    Number label = new Number(iColumn + indexColumn, iRow + indexRow, Integer.parseInt(kpiBean.getValue()));
	                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
	                }
	                indexColumn++;
	                if (kpiBean.getValue_r_rate_up() != null) {
	                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_r_rate_up())*100);
	                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
	                }
	                indexColumn++;
	                if (kpiBean.getValue_y_rate_up() != null) {
	                	Number label = new Number(iColumn + indexColumn, iRow + indexRow, Double.parseDouble(kpiBean.getValue_y_rate_up())*100);
	                    writableSheet.addCell(label.copyTo(iColumn + indexColumn, iRow + indexRow));
	                }
                }
                indexColumn++;
            }
        }

    }
}
