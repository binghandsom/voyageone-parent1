package com.voyageone.web2.cms.views.mapping.brand;

import com.jd.open.api.sdk.response.list.VenderBrandPubInfo;
import com.taobao.api.domain.Brand;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jd.service.JdCategoryService;
import com.voyageone.components.jumei.bean.JmBrandBean;
import com.voyageone.components.jumei.service.JumeiBrandService;
import com.voyageone.components.tmall.service.TbCategoryService;
import com.voyageone.ecerp.interfaces.third.koala.KoalaVenderService;
import com.voyageone.ecerp.interfaces.third.koala.beans.BrandInfo;
import com.voyageone.service.bean.cms.CmsBtBrandBean;
import com.voyageone.service.bean.cms.CmsBtBrandMappingBean;
import com.voyageone.service.impl.cms.BrandBtMappingService;
import com.voyageone.service.impl.cms.CmsMtPlatformBrandService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmMasterBrandService;
import com.voyageone.service.model.cms.CmsBtJmMasterBrandModel;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import com.voyageone.service.model.cms.CmsMtPlatformBrandsModel;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.enums.PlatformType;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.BrandMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 品牌映射服务
 * @author Wangtd 2016/07/25
 * @since 2.3.0
 */
@Service
public class BrandMappingService extends BaseViewService {
	
	@Autowired
	private BrandBtMappingService brandBtMappingService;

	@Autowired
	private JumeiBrandService jumeiBrandService;
	
	@Autowired
	private TbCategoryService tbCategoryService;
	
	@Autowired
	private JdCategoryService jdCategoryService;
	
	@Autowired
	private CmsBtJmMasterBrandService cmsBtJmMasterBrandService;
	
	@Autowired
	private CmsMtPlatformBrandService cmsMtPlatformBrandService;

	@Autowired
	private KoalaVenderService koalaVenderService;
	
	private static final Map<String, String> synchronizedTimes = new ConcurrentHashMap<String, String>();

	/**
	 * 检索品牌匹配数据
	 */
	public List<CmsBtBrandMappingBean> searchBrandsByPage(BrandMappingBean brandMapping) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mappingState", brandMapping.getMappingState());
		params.put("brandName", brandMapping.getBrandName());
		params.put("offset", brandMapping.getOffset());
		params.put("size", brandMapping.getSize());
		// 查询品牌匹配关系
		return brandBtMappingService.searchBrandsByPage(brandMapping.getChannelId(), brandMapping.getCartId(),
				brandMapping.getLangId(), params);
	}

	/**
	 * 检索品牌数量
	 */
	public Long searchBrandsCount(BrandMappingBean brandMapping) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mappingState", brandMapping.getMappingState());
		params.put("brandName", brandMapping.getBrandName());
		// 查询品牌匹配关系
		return brandBtMappingService.searchBrandsCount(brandMapping.getChannelId(), brandMapping.getCartId(),
				brandMapping.getLangId(), params);
	}

	/**
	 * 检索商户品牌数据
	 */
	public List<CmsBtBrandBean> searchCustBrands(BrandMappingBean brandMapping) {
		List<CmsBtBrandBean> brandList = null;
		if (CartEnums.Cart.JM.getId().equals(String.valueOf(brandMapping.getCartId()))) {
			// 聚美的品牌数据
			brandList = brandBtMappingService.searchJmBrands(brandMapping.getLangId(), brandMapping.getBrandName());
		} else {
			// 非聚美的品牌数据
			brandList = brandBtMappingService.searchMasterBrands(brandMapping.getChannelId(), brandMapping.getCartId(),
					brandMapping.getBrandName());
		}
		
		return brandList;
	}
	
	/**
	 * 检索品牌数量 
	 */
	public List<CmsBtBrandMappingBean> searchMatchedBrands(BrandMappingBean brandMapping) {
		return brandBtMappingService.searchMatchedBrands(brandMapping.getChannelId(), brandMapping.getCartId(),
				brandMapping.getLangId(), brandMapping.getBrandId());
	}

	/**
	 * 更新匹配的品牌数据 
	 */
	public boolean addNewBrandMapping(BrandMappingBean brandMapping, UserSessionBean userSession) {
		CmsMtBrandsMappingModel brandModel = new CmsMtBrandsMappingModel();
		// 设置品牌映射中的各项参数
		brandModel.setChannelId(brandMapping.getChannelId());
		brandModel.setCartId(brandMapping.getCartId());
		brandModel.setBrandId(brandMapping.getBrandId());
		brandModel.setCmsBrand(brandMapping.getCmsBrand());
		brandModel.setActive(true);
		Date now = new Date();
		brandModel.setCreated(now);
		brandModel.setCreater(userSession.getUserName());
		brandModel.setModified(now);
		brandModel.setModifier(userSession.getUserName());
		
		return brandBtMappingService.addNewBrandMapping(brandModel);
	}

	/**
	 * 添加或更新匹配的品牌数据 
	 */
	public boolean addOrUpdateBrandMapping(BrandMappingBean brandMapping, UserSessionBean userInfo) {
		CmsMtBrandsMappingModel brandModel = new CmsMtBrandsMappingModel();
		// 设置品牌映射中的各项参数
		brandModel.setChannelId(brandMapping.getChannelId());
		brandModel.setCartId(brandMapping.getCartId());
		brandModel.setBrandId(brandMapping.getBrandId());
		brandModel.setCmsBrand(brandMapping.getCmsBrand());
		brandModel.setActive(true);
		Date now = new Date();
		brandModel.setCreated(now);
		brandModel.setCreater(userInfo.getUserName());
		brandModel.setModified(now);
		brandModel.setModifier(userInfo.getUserName());
		
		return brandBtMappingService.addOrUpdateBrandMapping(brandModel);
	}

	/**
	 * 同步平台品牌数据
	 */
	public void synchronizePlatformBrands(BrandMappingBean brandMapping, UserSessionBean userInfo) {
		String cartId = String.valueOf(brandMapping.getCartId());
		String channelId = brandMapping.getChannelId();
		ShopBean shop = new ShopBean();
		shop.setOrder_channel_id(channelId);
		try {
			List<?> brands = null;
			// 取得各品牌的最新数据
			shop = Shops.getShop(channelId, cartId);
			Integer platformId = CartType.getPlatformIdById(Integer.valueOf(cartId));
			if (platformId == PlatformType.JM.getPlatformId()) {
				// 聚美品牌
				brands = jumeiBrandService.getBrands(shop);
			} else if (platformId == PlatformType.TMALL.getPlatformId()) {
				// 天猫品牌
				brands = tbCategoryService.getSellerCategoriesAuthorize(shop).getBrands();
			} else if (platformId == PlatformType.JD.getPlatformId()) {
				// 京东品牌
				brands = jdCategoryService.getCategoryBrandInfo(shop, "");
			} else if (platformId == PlatformType.NTES.getPlatformId()) {
				// 京东品牌
				brands = Arrays.asList(koalaVenderService.brandGet(Shops.getShopKoala(channelId, cartId)));
			} else {
				throw new BusinessException("不支持[channelId=" + channelId + ", cartId=" + cartId + "]的平台品牌同步功能");
			}
			// 更新数据库中的品牌数据
			deleteAndAddPlatformBrands(channelId, cartId, brands, userInfo, platformId);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		
		// 更新同步的执行时间
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		synchronizedTimes.put(brandMapping.getChannelId() + "_" + brandMapping.getCartId(), nowTime);
	}
	
	private void deleteAndAddPlatformBrands(String channelId, String cartId, List<?> brands, UserSessionBean userInfo, Integer platformId) {
		if (brands == null || brands.size() == 0) {
			logger.warn("[channelId={}, cartId={}]没有可以同步的品牌数据", channelId, cartId);
			return;
		}
		if (platformId == PlatformType.JM.getPlatformId()) {
			// 聚美品牌
			List<CmsBtJmMasterBrandModel> brandModels = new ArrayList<CmsBtJmMasterBrandModel>();
			Date now = new Date();
			for (int i = 0; i < brands.size(); i++) {
				JmBrandBean brand = (JmBrandBean) brands.get(i);
				CmsBtJmMasterBrandModel brandModel = new CmsBtJmMasterBrandModel();
				brandModel.setId(0);
				brandModel.setJmMasterBrandId(brand.getId());
				brandModel.setName(brand.getName());
				brandModel.setEnName(brand.getEnName());
				brandModel.setCreater(userInfo.getUserName());
				brandModel.setCreated(now);
				brandModel.setModifier(userInfo.getUserName());
				brandModel.setModified(now);
				brandModels.add(brandModel);
			}
			// 删除表中的数据
			cmsBtJmMasterBrandService.deleteAll();
			// 保存新的品牌数据
			cmsBtJmMasterBrandService.saveList(brandModels);
		} else {
			Date now = new Date();
			List<CmsMtPlatformBrandsModel> brandModels = new ArrayList<CmsMtPlatformBrandsModel>();
			if (platformId == PlatformType.TMALL.getPlatformId()) {
				// 天猫品牌
				for (int i = 0; i < brands.size(); i++) {
					Brand brand = (Brand) brands.get(i);
					CmsMtPlatformBrandsModel brandModel = new CmsMtPlatformBrandsModel();
					brandModel.setChannelId(channelId);
					brandModel.setCartId(Integer.valueOf(cartId));
					brandModel.setActive(true);
					brandModel.setBrandId(String.valueOf(brand.getVid()));
					brandModel.setBrandName(brand.getName());
					brandModel.setPropName(brand.getPropName());
					brandModel.setCreated(now);
					brandModel.setCreater(userInfo.getUserName());
					brandModel.setModified(now);
					brandModel.setModifier(userInfo.getUserName());
					brandModels.add(brandModel);
				}
			} else if (platformId == PlatformType.JD.getPlatformId()) {
				// 京东品牌
				for (int i = 0; i < brands.size(); i++) {
					VenderBrandPubInfo brand = (VenderBrandPubInfo) brands.get(i);
					CmsMtPlatformBrandsModel brandModel = new CmsMtPlatformBrandsModel();
					brandModel.setChannelId(channelId);
					brandModel.setCartId(Integer.valueOf(cartId));
					brandModel.setActive(true);
					brandModel.setBrandId(String.valueOf(brand.getErpBrandId()));
					brandModel.setBrandName(brand.getBrandName());
					brandModel.setPropName(brand.getBrandName());
					brandModel.setCreated(now);
					brandModel.setCreater(userInfo.getUserName());
					brandModel.setModified(now);
					brandModel.setModifier(userInfo.getUserName());
					brandModels.add(brandModel);
				}
			} else if (platformId == PlatformType.NTES.getPlatformId()) {
				// 京东品牌
				for (int i = 0; i < brands.size(); i++) {
					BrandInfo brand = (BrandInfo) brands.get(i);
					CmsMtPlatformBrandsModel brandModel = new CmsMtPlatformBrandsModel();
					brandModel.setChannelId(channelId);
					brandModel.setCartId(Integer.valueOf(cartId));
					brandModel.setActive(true);
					brandModel.setBrandId(String.valueOf(brand.getBrandId()));
					brandModel.setBrandName(brand.getBrand_name());
					brandModel.setPropName(brand.getBrand_name());
					brandModel.setCreated(now);
					brandModel.setCreater(userInfo.getUserName());
					brandModel.setModified(now);
					brandModel.setModifier(userInfo.getUserName());
					brandModels.add(brandModel);
				}
			}
			// 删除表中的数据
			cmsMtPlatformBrandService.deleteBrandsByChannelIdAndCartId(channelId, cartId);
			// 保存新的品牌数据
			cmsMtPlatformBrandService.saveList(brandModels);
		}
	}

	/**
	 * 取得平台品牌数据同步时间
	 */
	public String getSynchronizedTime(BrandMappingBean brandMapping) {
		return synchronizedTimes.get(brandMapping.getChannelId() + "_" + brandMapping.getCartId());
	}

}
