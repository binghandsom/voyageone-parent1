package com.voyageone.service.bean.cms.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by morse.lu on 16/4/20.
 */
public class SxData {
	private String channelId;
	private Integer cartId;
	private Long groupId;

	private CmsBtProductModel mainProduct; // 主商品
	private CmsBtFeedInfoModel cmsBtFeedInfoModel; // 主商品的feed信息
	private CmsBtProductGroupModel platform; // 平台信息(也是当前group信息)
	private List<CmsBtProductModel> productList; // 单个group中, 包含的所有product列表(product下所有sku都没有当前cartId，则去除)
	private List<BaseMongoMap<String, Object>> skuList;	// 只包含当前group，cart中,允许使用的sku信息,格式参照contents/cms/table/cms_bt_group.txt

    private Double maxPrice; // 当前productList的所有sku中，最大的那个价格（销售的价格）
	private String brandCode; // 主商品fields.Brand对应的BrandId(cms_mt_brands_mapping)

	private boolean hasSku; // 库存更新时，要选择商品上传时是否有SKU属性
	private boolean isDarwin; // 是不是达尔文
	private String styleCode;
    private String errorCode;     // 出错时回写到businessLog表的errorCode
    private String errorMessage;  // 出错时回写到businessLog表的errorMessage
	private boolean updateProductFlg; // 是否更新产品，默认false
	private Map<String, SxDarwinSkuProps> mapDarwinSkuProps; // Map<sku, SxDarwinSkuProps>

	public class SxDarwinSkuProps {
		private SxDarwinSkuProps() {}

		private String barcode; // 条形码
		private boolean allowUpdate; // 是否允许更新（表里设定的才是true，新增的规格在表里不用设定，所以是false）
		private String cspuId; // 规格id
		private boolean isErr; // 这个规格是否是审核失败的（allowUpdate=true才有用,因为不更新的话就不必去判断是否有错了）（审核成功的规格，只支持修改条形码、吊牌价以及产品规格主图）

		public String getBarcode() {
			return barcode;
		}

		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}

		public boolean isAllowUpdate() {
			return allowUpdate;
		}

		public void setAllowUpdate(boolean allowUpdate) {
			this.allowUpdate = allowUpdate;
		}

		public String getCspuId() {
			return cspuId;
		}

		public void setCspuId(String cspuId) {
			this.cspuId = cspuId;
		}

		public boolean isErr() {
			return isErr;
		}

		public void setErr(boolean err) {
			isErr = err;
		}
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public CmsBtProductModel getMainProduct() {
		return mainProduct;
	}

	public void setMainProduct(CmsBtProductModel mainProduct) {
		this.mainProduct = mainProduct;
	}

	public CmsBtFeedInfoModel getCmsBtFeedInfoModel() {
		return cmsBtFeedInfoModel;
	}

	public void setCmsBtFeedInfoModel(CmsBtFeedInfoModel cmsBtFeedInfoModel) {
		this.cmsBtFeedInfoModel = cmsBtFeedInfoModel;
	}

	public CmsBtProductGroupModel getPlatform() {
		return platform;
	}

	public void setPlatform(CmsBtProductGroupModel platform) {
		this.platform = platform;
	}

	public List<CmsBtProductModel> getProductList() {
		return productList;
	}

	public void setProductList(List<CmsBtProductModel> productList) {
		this.productList = productList;
	}

	public List<BaseMongoMap<String, Object>> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<BaseMongoMap<String, Object>> skuList) {
		this.skuList = skuList;
	}

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public boolean isHasSku() {
		return hasSku;
	}

	public void setHasSku(boolean hasSku) {
		this.hasSku = hasSku;
	}

	public boolean isDarwin() {
		return isDarwin;
	}

	public void setDarwin(boolean darwin) {
		isDarwin = darwin;
	}

	public String getStyleCode() {
		return styleCode;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

	public boolean isUpdateProductFlg() {
		return updateProductFlg;
	}

	public void setUpdateProductFlg(boolean updateProductFlg) {
		this.updateProductFlg = updateProductFlg;
	}

	public Map<String, SxDarwinSkuProps> getMapDarwinSkuProps() {
		return mapDarwinSkuProps;
	}

	public SxDarwinSkuProps getDarwinSkuProps(String skuCode, boolean isCreate) {
		if (mapDarwinSkuProps == null) {
			if (isCreate) {
				mapDarwinSkuProps = new HashMap<>();
			} else {
				return null;
			}
		}

		SxDarwinSkuProps sxDarwinSkuProps = mapDarwinSkuProps.get(skuCode);
		if (sxDarwinSkuProps == null && isCreate) {
			sxDarwinSkuProps = new SxDarwinSkuProps();
			mapDarwinSkuProps.put(skuCode, sxDarwinSkuProps);
		}

		return sxDarwinSkuProps;
	}
}
