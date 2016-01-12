package com.voyageone.bi.task;

import java.util.ArrayList;
import java.util.List;

import com.voyageone.bi.tranbean.UserChannelBean;
import com.voyageone.bi.tranbean.UserPortBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.ajax.bean.AjaxUserInfoBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.dao.BrandInfoDao;
import com.voyageone.bi.dao.CategoryInfoDao;
import com.voyageone.bi.dao.ColorInfoDao;
import com.voyageone.bi.dao.ProductInfoDao;
import com.voyageone.bi.dao.SizeInfoDao;
import com.voyageone.bi.dao.UserInfoDao;
import com.voyageone.bi.disbean.PageCmbBoxDisBean;
import com.voyageone.bi.tranbean.UserInfoBean;
import com.voyageone.bi.tranbean.UserShopBean;

@Service
public class UserInfoTask {
	@Autowired
	private UserInfoDao userInfoDao;
	
	@Autowired
	private BrandInfoDao brandInfoDao;

	@Autowired
	private CategoryInfoDao categoryInfoDao;
	
	@Autowired
	private ColorInfoDao colorInfoDao;
	
	@Autowired
	private SizeInfoDao sizeInfoDao;
	
	@Autowired
	private ProductInfoDao productInfoDao;
	
	
	//ajaxGetUserShopList
	public void ajaxGetUserShopList(AjaxUserInfoBean bean, UserInfoBean userInfoBean) throws BiException{
		if (!bean.checkInput()) {
			return;
		}
		
		// AJAX 返回值
		AjaxUserInfoBean.Result result = bean.getResponseBean();
		
		// 画面店铺显示数据
		List<PageCmbBoxDisBean> cmbShops = new ArrayList<PageCmbBoxDisBean>();
		List<UserShopBean> userShopList = userInfoDao.selectUserShopById(userInfoBean.getUid());
		for (UserShopBean userShopBean: userShopList) {
			PageCmbBoxDisBean disBean = new PageCmbBoxDisBean();
			disBean.setCode(String.valueOf(userShopBean.getId()));
			disBean.setName(userShopBean.getName());
			cmbShops.add(disBean);
		}
		// 画面店铺控件
		result.setCmbShops(cmbShops);
		result.setReqResult(Contants.AJAX_RESULT_OK);
	}

	//ajaxGetUserChannelList
	public void ajaxGetUserChannelList(AjaxUserInfoBean bean, UserInfoBean userInfoBean) throws BiException{
		if (!bean.checkInput()) {
			return;
		}

		// AJAX 返回值
		AjaxUserInfoBean.Result result = bean.getResponseBean();

		// 画面店铺显示数据
		List<PageCmbBoxDisBean> cmbChannel = new ArrayList<PageCmbBoxDisBean>();
		List<UserChannelBean> userChannelList = userInfoDao.selectUserChannelById(userInfoBean.getUid());
		for (UserChannelBean userChannelBean: userChannelList) {
			PageCmbBoxDisBean disBean = new PageCmbBoxDisBean();
			disBean.setCode(String.valueOf(userChannelBean.getCode()));
			disBean.setName(userChannelBean.getName());
			cmbChannel.add(disBean);
		}
		// 画面店铺控件
		result.setCmbChannels(cmbChannel);
		result.setReqResult(Contants.AJAX_RESULT_OK);
	}

	//ajaxGetUserChannelShopList
	public void ajaxGetUserChannelShopList(AjaxUserInfoBean bean, UserInfoBean userInfoBean) throws BiException{
		if (!bean.checkInput()) {
			return;
		}

		// AJAX 返回值
		AjaxUserInfoBean.Result result = bean.getResponseBean();

		// 画面店铺显示数据
		List<PageCmbBoxDisBean> cmbChannel = new ArrayList<PageCmbBoxDisBean>();
		List<UserChannelBean> userChannelList = userInfoDao.selectUserChannelById(userInfoBean.getUid());
		List<UserShopBean> userShopList = userInfoDao.selectUserShopById(userInfoBean.getUid());
		for (UserChannelBean userChannelBean: userChannelList) {
			PageCmbBoxDisBean disBean = new PageCmbBoxDisBean();
			disBean.setCode(userChannelBean.getCode());
			disBean.setName(userChannelBean.getName());
			// add channel shop
			List<PageCmbBoxDisBean> shopList = new ArrayList<>();
			for (UserShopBean userShopBean: userShopList) {
				if (userShopBean.getChannel_id().equals(String.valueOf(userChannelBean.getId()))) {
					PageCmbBoxDisBean shopBean = new PageCmbBoxDisBean();
					shopBean.setCode(String.valueOf(userShopBean.getId()));
					shopBean.setName(userShopBean.getName());
					shopList.add(shopBean);
				}
			}
			disBean.setChildren(shopList);
			cmbChannel.add(disBean);
		}
		// 画面店铺控件
		result.setCmbChannels(cmbChannel);
		result.setReqResult(Contants.AJAX_RESULT_OK);
	}

	//ajaxGetUserChannelList
	public void ajaxGetUserPortList(AjaxUserInfoBean bean, UserInfoBean userInfoBean) throws BiException{
		if (!bean.checkInput()) {
			return;
		}

		// AJAX 返回值
		AjaxUserInfoBean.Result result = bean.getResponseBean();

		// 画面店铺显示数据
		List<PageCmbBoxDisBean> cmbPort = new ArrayList<PageCmbBoxDisBean>();
		List<UserPortBean> userPortList = userInfoDao.selectUserPortById(userInfoBean.getUid());
		for (UserPortBean userPortBean: userPortList) {
			PageCmbBoxDisBean disBean = new PageCmbBoxDisBean();
			disBean.setCode(String.valueOf(userPortBean.getCode()));
			disBean.setName(userPortBean.getName());
			cmbPort.add(disBean);
		}
		// 画面店铺控件
		result.setCmbPorts(cmbPort);
		result.setReqResult(Contants.AJAX_RESULT_OK);
	}
	
	/**
	 * ajaxGetUserBrandList (取得有销售数据的分类)
	 * @param bean
	 * @param userInfoBean
	 * @throws Exception 
	 */
	public void ajaxGetUserCategoryList(AjaxUserInfoBean bean, UserInfoBean userInfoBean)  {
		if (!bean.checkInput()) {
			return;
		}
		// AJAX 返回值
		AjaxUserInfoBean.Result result = bean.getResponseBean();
		List<PageCmbBoxDisBean> cmbCategorys = categoryInfoDao.selectAllCateogry(userInfoBean);
		result.setCmbCategorys(cmbCategorys);
		result.setReqResult(Contants.AJAX_RESULT_OK);
	}
	
	/**
	 * ajaxGetUserBrandList (取得有销售数据的品牌)
	 * @param bean
	 * @param userInfoBean
	 * @throws Exception 
	 */
	public void ajaxGetUserBrandList(AjaxUserInfoBean bean, UserInfoBean userInfoBean) {
		if (!bean.checkInput()) {
			return;
		}
		// AJAX 返回值
		AjaxUserInfoBean.Result result = bean.getResponseBean();
		List<PageCmbBoxDisBean> cmbCategorys = brandInfoDao.selectAllBrand(userInfoBean);
		result.setCmbBrands(cmbCategorys);
		result.setReqResult(Contants.AJAX_RESULT_OK);
	}
	
	/**
	 * ajaxGetUserColorList
	 * @param bean
	 * @param userInfoBean
	 * @throws Exception 
	 */
	public void ajaxGetUserColorList(AjaxUserInfoBean bean, UserInfoBean userInfoBean) {
		if (!bean.checkInput()) {
			return;
		}
		// AJAX 返回值
		AjaxUserInfoBean.Result result = bean.getResponseBean();
		List<PageCmbBoxDisBean> cmbColors = colorInfoDao.selectAllColor(userInfoBean);
		result.setCmbColors(cmbColors);
		result.setReqResult(Contants.AJAX_RESULT_OK);
	}
	
	/**
	 * ajaxGetUserSizeList
	 * @param bean
	 * @param userInfoBean
	 * @throws BiException
	 */
	public void ajaxGetUserSizeList(AjaxUserInfoBean bean, UserInfoBean userInfoBean) throws BiException {
		if (!bean.checkInput()) {
			return;
		}

		// AJAX 返回值
		AjaxUserInfoBean.Result result = bean.getResponseBean();
		List<PageCmbBoxDisBean> cmbSizes = sizeInfoDao.selectAllSize(userInfoBean);
		result.setCmbSizes(cmbSizes);
		result.setReqResult(Contants.AJAX_RESULT_OK);
	}
	
	/**
	 * ajaxGetUserProductList
	 * @param bean
	 * @param userInfoBean
	 * @throws BiException
	 */
	public void ajaxGetUserProductList(AjaxUserInfoBean bean, UserInfoBean userInfoBean) throws BiException {
		if (!bean.checkInput()) {
			return;
		}

		// AJAX 返回值
		AjaxUserInfoBean.Result result = bean.getResponseBean();
		List<PageCmbBoxDisBean> cmbProducts = productInfoDao.selectProductIDAndCodeByQuery(bean.getProductQueryStr(), 20, userInfoBean);
		result.setCmbProducts(cmbProducts);
		result.setReqResult(Contants.AJAX_RESULT_OK);
	}


}
