package com.voyageone.service.bean.cms.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.service.model.cms.CmsTmpSxCnCodeModel;
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
	private String pBrandName; // 平台品牌名(cms_mt_platform_brands)

	private boolean hasSku; // 库存更新时，要选择商品上传时是否有SKU属性
	private boolean isDarwin; // 是不是达尔文
	private String styleCode;
    private String errorCode;     // 出错时回写到businessLog表的errorCode
    private String errorMessage;  // 出错时回写到businessLog表的errorMessage
	private boolean updateProductFlg; // 是否更新产品，默认false

	private Map<String, Field> updateItemFields; // Map<field_id, Field> 商品当前在平台上的信息，这样可以把有些不想改的属性设值回去

	private Map<String, SxDarwinSkuProps> mapDarwinSkuProps; // Map<sku, SxDarwinSkuProps>

	private Integer sizeChartId;

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

	// added by morse.lu 2017/01/05 start
	// 保存sku的各种上新用的扩展属性,方便在各个方法里直接拿到，以后有啥需要的，可以都加到这个里面
	private Map<String, SxSkuExInfo> mapSxSkuExInfo; // Map<skuCode, SxSkuExInfo>

	public class SxSkuExInfo {
		private SxSkuExInfo() {}

		private String scProductId; // 货品id

		public String getScProductId() {
			return scProductId;
		}

		public void setScProductId(String scProductId) {
			this.scProductId = scProductId;
		}
	}
	// added by morse.lu 2017/01/05 end

    // added by morse.lu 2016/10/19 start
    // 独立域名上新，临时用的，以后不看cms_tmp_sx_cn_code这张表且删了这张表之后，把这里的删掉
    private CmsTmpSxCnCodeModel tmpSxCnCode;

    public CmsTmpSxCnCodeModel getTmpSxCnCode() {
        return tmpSxCnCode;
    }

    public void setTmpSxCnCode(CmsTmpSxCnCodeModel tmpSxCnCode) {
        this.tmpSxCnCode = tmpSxCnCode;
    }
    // added by morse.lu 2016/10/19 end

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

	public String getpBrandName() {
		return pBrandName;
	}

	public void setpBrandName(String pBrandName) {
		this.pBrandName = pBrandName;
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

	public Map<String, Field> getUpdateItemFields() {
		return updateItemFields;
	}

	public void setUpdateItemFields(Map<String, Field> updateItemFields) {
		this.updateItemFields = updateItemFields;
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

	public Integer getSizeChartId() {
		return sizeChartId;
	}

	public void setSizeChartId(Integer sizeChartId) {
		this.sizeChartId = sizeChartId;
	}

	public Map<String, SxSkuExInfo> getMapSxSkuExInfo() {
		return mapSxSkuExInfo;
	}

	public SxSkuExInfo getSxSkuExInfo(String skuCode, boolean isCreate) {
		if (mapSxSkuExInfo == null) {
			if (isCreate) {
				mapSxSkuExInfo = new HashMap<>();
			} else {
				return null;
			}
		}

		SxSkuExInfo sxSkuExInfo = mapSxSkuExInfo.get(skuCode);
		if (sxSkuExInfo == null && isCreate) {
			sxSkuExInfo = new SxSkuExInfo();
			mapSxSkuExInfo.put(skuCode, sxSkuExInfo);
		}

		return sxSkuExInfo;
	}
}
