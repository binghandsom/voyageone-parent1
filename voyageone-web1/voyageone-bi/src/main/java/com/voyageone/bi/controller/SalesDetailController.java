package com.voyageone.bi.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voyageone.bi.ajax.bean.AjaxSalesDetailBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.bean.UserMenuBean;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.DateTimeUtil;
import com.voyageone.bi.commonutils.SessionKey;
import com.voyageone.bi.task.SalesDetailTask;
import com.voyageone.bi.tranbean.UserInfoBean;

@Controller  
public class SalesDetailController {
	
	private static Log logger = LogFactory.getLog(SalesDetailController.class);
	
	// 按时间
	@Autowired
	private SalesDetailTask salesDetailTask;
	

	// 销售分析页面
    @RequestMapping(value = "/manage/goSalesDetail")
    public String goSalesDetail(HttpServletRequest request, Map<String, Object> map) throws BiException {
    	
    	HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		Map<String, List<UserMenuBean>> userMenuMap = userInfoBean.getUserMenuMap();
		if (userMenuMap != null) {
			Collection<List<UserMenuBean>>  coll = userMenuMap.values();
			for (List<UserMenuBean> col : coll) {
				for (UserMenuBean bean : col) {
					if (bean.getLink().indexOf("/manage/goSalesDetail")>=0) {
						bean.setSelect(true);
					} else {
						bean.setSelect(false);
					}
				}
			}
		}
		

        return "manage/salesdetail/main";    	
    }
	
    /**
     * checkSalesDetailParam
     * @param response
     * @param request
     * @param bean
     * @throws BiException
     */
	@RequestMapping(value = "/manage/checkSalesDetailParam")
	public void doCheckSalesDetailParam(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesDetailBean bean) throws BiException {
		if (!bean.checkInput()) {
			bean.WriteTo(response);
		} else {
			AjaxResponseBean result = bean.getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_OK);
			bean.WriteTo(response);
		}
		return;
	}
	
	/**
	 * TimeLine数据取得
	 */
	@RequestMapping(value = "/manage/getSalesDetailTimelineData")
	public void getSalesTimeLineData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailTimelineData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		
		salesDetailTask.ajaxGetSalesTimeLineData(bean, userInfoBean);
		bean.WriteTo(response);
	}
	/**
	 * getSalesTimeLineDataExcel
	 */
	@RequestMapping(value = "/manage/getSalesTimeLineDataExcel")
	public void getSalesTimeLineDataExcel(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesTimeLineDataExcel download");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		try {
			String fileName = salesDetailTask.ajaxGetSalesTimeLineDataExcel(request, bean, userInfoBean);
			String outFileName = "bi_sales_detail_timeline_list_" + DateTimeUtil.getDate() + ".xls";
			responseExcel(fileName, response, outFileName);
        } catch (Exception ex) {
            logger.error("getSalesTimeLineDataExcel", ex);
        }
		bean.WriteTo(response);
	}
	
	/**
	 * getSalesDetailBrandData
	 */
	@RequestMapping(value = "/manage/getSalesDetailBrandData")
	public void getSalesDetailBrandData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailBrandData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		salesDetailTask.ajaxGetSalesBrandData(bean, userInfoBean);
		
		bean.WriteTo(response);
	}
	/**
	 * getSalesDetailBrandDataExcel
	 */
	@RequestMapping(value = "/manage/getSalesDetailBrandDataExcel")
	public void getSalesDetailBrandDataExcel(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailBrandDataExcel download");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		try {
			String fileName = salesDetailTask.ajaxGetSalesBrandDataExcel(request, bean, userInfoBean);
			String outFileName = "bi_sales_detail_brand_list_" + DateTimeUtil.getDate() + ".xls";
			responseExcel(fileName, response, outFileName);
        } catch (Exception ex) {
            logger.error("getSalesDetailBrandDataExcel", ex);
        }
		bean.WriteTo(response);
	}
	
	/**
	 * getSalesDetailCategoryData
	 */
	@RequestMapping(value = "/manage/getSalesDetailCategoryData")
	public void getSalesDetailCategoryData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailCategoryData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		salesDetailTask.ajaxGetSalesCategoryData(bean, userInfoBean);
		
		bean.WriteTo(response);
	}
	/**
	 * getSalesDetailCategoryDataExcel
	 */
	@RequestMapping(value = "/manage/getSalesDetailCategoryDataExcel")
	public void getSalesDetailCategoryDataExcel(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailCategoryDataExcel download");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		try {
			String fileName = salesDetailTask.ajaxGetSalesCategoryDataExcel(request, bean, userInfoBean);
			String outFileName = "bi_sales_detail_category_list_" + DateTimeUtil.getDate() + ".xls";
			responseExcel(fileName, response, outFileName);
        } catch (Exception ex) {
            logger.error("getSalesDetailCategoryDataExcel", ex);
        }
		bean.WriteTo(response);
	}
	
	/**
	 * getSalesDetailColorData
	 */
	@RequestMapping(value = "/manage/getSalesDetailColorData")
	public void getSalesDetailColorData(HttpServletResponse response,
			HttpServletRequest request,
			AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailColorData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		salesDetailTask.ajaxGetSalesColorData(bean, userInfoBean);
		
		bean.WriteTo(response);
	}
	/**
	 * getSalesDetailColorDataExcel
	 */
	@RequestMapping(value = "/manage/getSalesDetailColorDataExcel")
	public void getSalesDetailColorDataExcel(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailColorDataExcel download");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		try {
			String fileName = salesDetailTask.ajaxGetSalesColorDataExcel(request, bean, userInfoBean);
			String outFileName = "bi_sales_detail_color_list_" + DateTimeUtil.getDate() + ".xls";
			responseExcel(fileName, response, outFileName);
        } catch (Exception ex) {
            logger.error("getSalesDetailColorDataExcel", ex);
        }
		bean.WriteTo(response);
	}
	
	/**
	 * getSalesDetailSizeData
	 */
	@RequestMapping(value = "/manage/getSalesDetailSizeData")
	public void getSalesDetailSizeData(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailSizeData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		salesDetailTask.ajaxGetSalesSizeData(bean, userInfoBean);
		
		bean.WriteTo(response);
	}

	/**
	 * getSalesDetailSizeDataExcel
	 */
	@RequestMapping(value = "/manage/getSalesDetailSizeDataExcel")
	public void getSalesDetailSizeDataExcel(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailSizeDataExcel download");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		try {
			String fileName = salesDetailTask.ajaxGetSalesSizeDataExcel(request, bean, userInfoBean);
			String outFileName = "bi_sales_detail_size_list_" + DateTimeUtil.getDate() + ".xls";
			responseExcel(fileName, response, outFileName);
        } catch (Exception ex) {
            logger.error("getSalesDetailSizeDataExcel", ex);
        }
		bean.WriteTo(response);
	}

	/**
	 * getSalesDetailModelData
	 */
	@RequestMapping(value = "/manage/getSalesDetailModelData")
	public void getSalesDetailModelData(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailModelData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		salesDetailTask.ajaxGetSalesModelData(bean, userInfoBean);
		
		bean.WriteTo(response);
	}

	/**
	 * getSalesDetailModelDataExcel
	 */
	@RequestMapping(value = "/manage/getSalesDetailModelDataExcel")
	public void getSalesDetailModelDataExcel(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailModelDataExcel download");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		try {
			String fileName = salesDetailTask.ajaxGetSalesModelDataExcel(request, bean, userInfoBean);
			String outFileName = "bi_sales_detail_model_list_" + DateTimeUtil.getDate() + ".xls";
			responseExcel(fileName, response, outFileName);
        } catch (Exception ex) {
            logger.error("getSalesDetailModelDataExcel", ex);
        }
		bean.WriteTo(response);
	}	

	/**
	 * getSalesDetailProductData
	 */
	@RequestMapping(value = "/manage/getSalesDetailProductData")
	public void getSalesDetailProductData(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailProductData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		salesDetailTask.ajaxGetSalesProductData(bean, userInfoBean);
		
		bean.WriteTo(response);
	}

	/**
	 * getSalesDetailProductData
	 */
	@RequestMapping(value = "/manage/getSalesDetailProductDataExcel")
	public void getSalesDetailProductDataExcel(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailProductDataExcel download");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		try {
			String fileName = salesDetailTask.ajaxGetSalesProductDataExcel(request, bean, userInfoBean);
			String outFileName = "bi_sales_detail_product_list_" + DateTimeUtil.getDate() + ".xls";
			responseExcel(fileName, response, outFileName);
        } catch (Exception ex) {
            logger.error("getSalesDetailProductDataExcel", ex);
        }
		bean.WriteTo(response);
	}

	/**
	 * getSalesDetailProductData
	 */
	@RequestMapping(value = "/manage/getSalesDetailSkuData")
	public void getSalesDetailSkuData(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailSkuData数据取得");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);

		salesDetailTask.ajaxGetSalesSkuData(bean, userInfoBean);
		
		bean.WriteTo(response);
	}

	/**
	 * getSalesDetailProductData
	 */
	@RequestMapping(value = "/manage/getSalesDetailSkuDataExcel")
	public void getSalesDetailSkuDataExcel(HttpServletResponse response, HttpServletRequest request, AjaxSalesDetailBean bean) throws BiException {
		logger.info("getSalesDetailSkuDataExcel download");
		if (!bean.checkInput()) {
			bean.WriteTo(response);
			return;
		}
		
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(SessionKey.LOGIN_INFO);
		
		try {
			String fileName = salesDetailTask.ajaxGetSalesSkuDataExcel(request, bean, userInfoBean);
			String outFileName = "bi_sales_detail_sku_list_" + DateTimeUtil.getDate() + ".xls";
			responseExcel(fileName, response, outFileName);
        } catch (Exception ex) {
            logger.error("getSalesDetailSkuDataExcel", ex);
        }
		
		bean.WriteTo(response);
	}

	/**
	 * responseExcel
	 * @param fileName
	 * @param response
	 * @param outFileName
	 * @throws Exception
	 */
	private void responseExcel(String fileName, HttpServletResponse response, String outFileName) throws Exception {
        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename="+ outFileName);
        File file = new File(fileName);
        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/vnd.ms-excel;charset=gb2312");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
	}
	
}
