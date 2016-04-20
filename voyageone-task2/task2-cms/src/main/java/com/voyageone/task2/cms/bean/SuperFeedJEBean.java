package com.voyageone.task2.cms.bean;

import com.voyageone.common.util.MD5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SuperFeedJEBean
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */

public class SuperFeedJEBean {
	private String AuctionTitle;
	private String InventoryNumber;
	private String Weight;
	private String ISBN;
	private String UPC;
	private String EAN;
	private String ASIN;
	private String MPN;
	private String ShortDescription;
	private String Description;
	private String Flag;
	private String FlagDescription;
	private String Blocked;
	private String BlockedComment;
	private String Manufacturer;
	private String Brand;
	private String Condition;
	private String Warranty;
	private String SellerCost;
	private String ProductMargin;
	private String BuyItNowPrice;
	private String RetailPrice;
	private String PictureURLs;
	private String TaxProductCode;
	private String SupplierCode;
	private String SupplierPO;
	private String WarehouseLocation;
	private String InventorySubtitle;
	private String RelationshipName;
	private String VariationParentSKU;
	private String Labels;
	private String Attribute1Name;
	private String Attribute1Value;
	private String Attribute2Name;
	private String Attribute2Value;
	private String Attribute3Name;
	private String Attribute3Value;
	private String Attribute4Name;
	private String Attribute4Value;
	private String Attribute5Name;
	private String Attribute5Value;
	private String Attribute6Name;
	private String Attribute6Value;
	private String Attribute7Name;
	private String Attribute7Value;
	private String Attribute8Name;
	private String Attribute8Value;
	private String Attribute9Name;
	private String Attribute9Value;
	private String Attribute10Name;
	private String Attribute10Value;
	private String Attribute11Name;
	private String Attribute11Value;
	private String Attribute12Name;
	private String Attribute12Value;
	private String Attribute13Name;
	private String Attribute13Value;
	private String Attribute14Name;
	private String Attribute14Value;
	private String Attribute15Name;
	private String Attribute15Value;
	private String Attribute16Name;
	private String Attribute16Value;
	private String Attribute17Name;
	private String Attribute17Value;
	private String Attribute18Name;
	private String Attribute18Value;
	private String Attribute19Name;
	private String Attribute19Value;
	private String Attribute20Name;
	private String Attribute20Value;
	private String Attribute21Name;
	private String Attribute21Value;
	private String Attribute22Name;
	private String Attribute22Value;
	private String Attribute23Name;
	private String Attribute23Value;
	private String Attribute24Name;
	private String Attribute24Value;
	private String Attribute25Name;
	private String Attribute25Value;
	private String Attribute26Name;
	private String Attribute26Value;
	private String Attribute27Name;
	private String Attribute27Value;
	private String Attribute28Name;
	private String Attribute28Value;
	private String Attribute29Name;
	private String Attribute29Value;
	private String Attribute30Name;
	private String Attribute30Value;
	private String Attribute31Name;
	private String Attribute31Value;
	private String Attribute32Name;
	private String Attribute32Value;
	private String Attribute33Name;
	private String Attribute33Value;
	private String Attribute34Name;
	private String Attribute34Value;
	private String Attribute35Name;
	private String Attribute35Value;
	private String Attribute36Name;
	private String Attribute36Value;
	private String Attribute37Name;
	private String Attribute37Value;
	private String Attribute38Name;
	private String Attribute38Value;
	private String Attribute39Name;
	private String Attribute39Value;
	private String Attribute40Name;
	private String Attribute40Value;
	private String Attribute41Name;
	private String Attribute41Value;
	private String Attribute42Name;
	private String Attribute42Value;
	private String Attribute43Name;
	private String Attribute43Value;
	private String Attribute44Name;
	private String Attribute44Value;
	private String Attribute47Name;
	private String Attribute47Value;
	private String Attribute48Name;
	private String Attribute48Value;
	private String Attribute49Name;
	private String Attribute49Value;
	private String Attribute50Name;
	private String Attribute50Value;
	private String Attribute51Name;
	private String Attribute51Value;
	private String Attribute52Name;
	private String Attribute52Value;
	private String Attribute53Name;
	private String Attribute53Value;
	private String Attribute54Name;
	private String Attribute54Value;
	private String Attribute55Name;
	private String Attribute55Value;
	private String Attribute56Name;
	private String Attribute56Value;
	private String Attribute57Name;
	private String Attribute57Value;
	private String Attribute58Name;
	private String Attribute58Value;
	private String Attribute59Name;
	private String Attribute59Value;
	private String Attribute60Name;
	private String Attribute60Value;
	private String Attribute61Name;
	private String Attribute61Value;
	private String Attribute62Name;
	private String Attribute62Value;
	private String Attribute63Name;
	private String Attribute63Value;
	private String Attribute64Name;
	private String Attribute64Value;
	private String Attribute65Name;
	private String Attribute65Value;
	private String Attribute66Name;
	private String Attribute66Value;
	private String Attribute67Name;
	private String Attribute67Value;
	private String Attribute68Name;
	private String Attribute68Value;
	private String Attribute69Name;
	private String Attribute69Value;
	private String Attribute70Name;
	private String Attribute70Value;
	private String Attribute71Name;
	private String Attribute71Value;
	private String Attribute72Name;
	private String Attribute72Value;
	private String Attribute73Name;
	private String Attribute73Value;
	private String Attribute74Name;
	private String Attribute74Value;
	private String Attribute75Name;
	private String Attribute75Value;
	private String Attribute76Name;
	private String Attribute76Value;
	private String Attribute77Name;
	private String Attribute77Value;
	private String Attribute78Name;
	private String Attribute78Value;
	private String Attribute79Name;
	private String Attribute79Value;
	private String Attribute80Name;
	private String Attribute80Value;
	private String Attribute81Name;
	private String Attribute81Value;
	private String Attribute82Name;
	private String Attribute82Value;
	private String Attribute83Name;
	private String Attribute83Value;
	private String Attribute84Name;
	private String Attribute84Value;
	private String Attribute85Name;
	private String Attribute85Value;
	private String Attribute86Name;
	private String Attribute86Value;
	private String Attribute87Name;
	private String Attribute87Value;
	private String Attribute88Name;
	private String Attribute88Value;
	private String Attribute89Name;
	private String Attribute89Value;
	private String Attribute90Name;
	private String Attribute90Value;
	private String Attribute91Name;
	private String Attribute91Value;
	private String Attribute92Name;
	private String Attribute92Value;
	private String Attribute93Name;
	private String Attribute93Value;
	private String Attribute94Name;
	private String Attribute94Value;
	private String HarmonizedCode;
	private String Height;
	private String Length;
	private String Width;
	private String DCCode;
	private String Classification;
	private String Attribute95Name;
	private String Attribute95Value;
	private String Attribute96Name;
	private String Attribute96Value;
	private String Attribute97Name;
	private String Attribute97Value;
	private String Attribute98Name;
	private String Attribute98Value;
	private String md5;

	public String getAuctionTitle() {
		return AuctionTitle;
	}
	public void setAuctionTitle(String auctionTitle) {
		AuctionTitle = auctionTitle;
	}
	public String getInventoryNumber() {
		return InventoryNumber;
	}
	public void setInventoryNumber(String inventoryNumber) {
		InventoryNumber = inventoryNumber;
	}
	public String getWeight() {
		return Weight;
	}
	public void setWeight(String weight) {
		Weight = weight;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String getUPC() {
		return UPC;
	}
	public void setUPC(String uPC) {
		UPC = uPC;
	}
	public String getEAN() {
		return EAN;
	}
	public void setEAN(String eAN) {
		EAN = eAN;
	}
	public String getASIN() {
		return ASIN;
	}
	public void setASIN(String aSIN) {
		ASIN = aSIN;
	}
	public String getMPN() {
		return MPN;
	}
	public void setMPN(String mPN) {
		MPN = mPN;
	}
	public String getShortDescription() {
		return ShortDescription;
	}
	public void setShortDescription(String shortDescription) {
		ShortDescription = shortDescription;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public String getFlagDescription() {
		return FlagDescription;
	}
	public void setFlagDescription(String flagDescription) {
		FlagDescription = flagDescription;
	}
	public String getBlocked() {
		return Blocked;
	}
	public void setBlocked(String blocked) {
		Blocked = blocked;
	}
	public String getBlockedComment() {
		return BlockedComment;
	}
	public void setBlockedComment(String blockedComment) {
		BlockedComment = blockedComment;
	}
	public String getManufacturer() {
		return Manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		Manufacturer = manufacturer;
	}
	public String getBrand() {
		return Brand;
	}
	public void setBrand(String brand) {
		Brand = brand;
	}
	public String getCondition() {
		return Condition;
	}
	public void setCondition(String condition) {
		Condition = condition;
	}
	public String getWarranty() {
		return Warranty;
	}
	public void setWarranty(String warranty) {
		Warranty = warranty;
	}
	public String getSellerCost() {
		return SellerCost;
	}
	public void setSellerCost(String sellerCost) {
		SellerCost = sellerCost;
	}
	public String getProductMargin() {
		return ProductMargin;
	}
	public void setProductMargin(String productMargin) {
		ProductMargin = productMargin;
	}
	public String getBuyItNowPrice() {
		return BuyItNowPrice;
	}
	public void setBuyItNowPrice(String buyItNowPrice) {
		BuyItNowPrice = buyItNowPrice;
	}
	public String getRetailPrice() {
		return RetailPrice;
	}
	public void setRetailPrice(String retailPrice) {
		RetailPrice = retailPrice;
	}
	public String getPictureURLs() {
		return PictureURLs;
	}
	public void setPictureURLs(String pictureURLs) {
		PictureURLs = pictureURLs;
	}
	public String getTaxProductCode() {
		return TaxProductCode;
	}
	public void setTaxProductCode(String taxProductCode) {
		TaxProductCode = taxProductCode;
	}
	public String getSupplierCode() {
		return SupplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		SupplierCode = supplierCode;
	}
	public String getSupplierPO() {
		return SupplierPO;
	}
	public void setSupplierPO(String supplierPO) {
		SupplierPO = supplierPO;
	}
	public String getWarehouseLocation() {
		return WarehouseLocation;
	}
	public void setWarehouseLocation(String warehouseLocation) {
		WarehouseLocation = warehouseLocation;
	}
	public String getInventorySubtitle() {
		return InventorySubtitle;
	}
	public void setInventorySubtitle(String inventorySubtitle) {
		InventorySubtitle = inventorySubtitle;
	}
	public String getRelationshipName() {
		return RelationshipName;
	}
	public void setRelationshipName(String relationshipName) {
		RelationshipName = relationshipName;
	}
	public String getVariationParentSKU() {
		return VariationParentSKU;
	}
	public void setVariationParentSKU(String variationParentSKU) {
		VariationParentSKU = variationParentSKU;
	}
	public String getLabels() {
		return Labels;
	}
	public void setLabels(String labels) {
		Labels = labels;
	}
	public String getAttribute1Name() {
		return Attribute1Name;
	}
	public void setAttribute1Name(String attribute1Name) {
		Attribute1Name = attribute1Name;
	}
	public String getAttribute1Value() {
		return Attribute1Value;
	}
	public void setAttribute1Value(String attribute1Value) {
		Attribute1Value = attribute1Value;
	}
	public String getAttribute2Name() {
		return Attribute2Name;
	}
	public void setAttribute2Name(String attribute2Name) {
		Attribute2Name = attribute2Name;
	}
	public String getAttribute2Value() {
		return Attribute2Value;
	}
	public void setAttribute2Value(String attribute2Value) {
		Attribute2Value = attribute2Value;
	}
	public String getAttribute3Name() {
		return Attribute3Name;
	}
	public void setAttribute3Name(String attribute3Name) {
		Attribute3Name = attribute3Name;
	}
	public String getAttribute3Value() {
		return Attribute3Value;
	}
	public void setAttribute3Value(String attribute3Value) {
		Attribute3Value = attribute3Value;
	}
	public String getAttribute4Name() {
		return Attribute4Name;
	}
	public void setAttribute4Name(String attribute4Name) {
		Attribute4Name = attribute4Name;
	}
	public String getAttribute4Value() {
		return Attribute4Value;
	}
	public void setAttribute4Value(String attribute4Value) {
		Attribute4Value = attribute4Value;
	}
	public String getAttribute5Name() {
		return Attribute5Name;
	}
	public void setAttribute5Name(String attribute5Name) {
		Attribute5Name = attribute5Name;
	}
	public String getAttribute5Value() {
		return Attribute5Value;
	}
	public void setAttribute5Value(String attribute5Value) {
		Attribute5Value = attribute5Value;
	}
	public String getAttribute6Name() {
		return Attribute6Name;
	}
	public void setAttribute6Name(String attribute6Name) {
		Attribute6Name = attribute6Name;
	}
	public String getAttribute6Value() {
		return Attribute6Value;
	}
	public void setAttribute6Value(String attribute6Value) {
		Attribute6Value = attribute6Value;
	}
	public String getAttribute7Name() {
		return Attribute7Name;
	}
	public void setAttribute7Name(String attribute7Name) {
		Attribute7Name = attribute7Name;
	}
	public String getAttribute7Value() {
		return Attribute7Value;
	}
	public void setAttribute7Value(String attribute7Value) {
		Attribute7Value = attribute7Value;
	}
	public String getAttribute8Name() {
		return Attribute8Name;
	}
	public void setAttribute8Name(String attribute8Name) {
		Attribute8Name = attribute8Name;
	}
	public String getAttribute8Value() {
		return Attribute8Value;
	}
	public void setAttribute8Value(String attribute8Value) {
		Attribute8Value = attribute8Value;
	}
	public String getAttribute9Name() {
		return Attribute9Name;
	}
	public void setAttribute9Name(String attribute9Name) {
		Attribute9Name = attribute9Name;
	}
	public String getAttribute9Value() {
		return Attribute9Value;
	}
	public void setAttribute9Value(String attribute9Value) {
		Attribute9Value = attribute9Value;
	}
	public String getAttribute10Name() {
		return Attribute10Name;
	}
	public void setAttribute10Name(String attribute10Name) {
		Attribute10Name = attribute10Name;
	}
	public String getAttribute10Value() {
		return Attribute10Value;
	}
	public void setAttribute10Value(String attribute10Value) {
		Attribute10Value = attribute10Value;
	}
	public String getAttribute11Name() {
		return Attribute11Name;
	}
	public void setAttribute11Name(String attribute11Name) {
		Attribute11Name = attribute11Name;
	}
	public String getAttribute11Value() {
		return Attribute11Value;
	}
	public void setAttribute11Value(String attribute11Value) {
		Attribute11Value = attribute11Value;
	}
	public String getAttribute12Name() {
		return Attribute12Name;
	}
	public void setAttribute12Name(String attribute12Name) {
		Attribute12Name = attribute12Name;
	}
	public String getAttribute12Value() {
		return Attribute12Value;
	}
	public void setAttribute12Value(String attribute12Value) {
		Attribute12Value = attribute12Value;
	}
	public String getAttribute13Name() {
		return Attribute13Name;
	}
	public void setAttribute13Name(String attribute13Name) {
		Attribute13Name = attribute13Name;
	}
	public String getAttribute13Value() {
		return Attribute13Value;
	}
	public void setAttribute13Value(String attribute13Value) {
		Attribute13Value = attribute13Value;
	}
	public String getAttribute14Name() {
		return Attribute14Name;
	}
	public void setAttribute14Name(String attribute14Name) {
		Attribute14Name = attribute14Name;
	}
	public String getAttribute14Value() {
		return Attribute14Value;
	}
	public void setAttribute14Value(String attribute14Value) {
		Attribute14Value = attribute14Value;
	}
	public String getAttribute15Name() {
		return Attribute15Name;
	}
	public void setAttribute15Name(String attribute15Name) {
		Attribute15Name = attribute15Name;
	}
	public String getAttribute15Value() {
		return Attribute15Value;
	}
	public void setAttribute15Value(String attribute15Value) {
		Attribute15Value = attribute15Value;
	}
	public String getAttribute16Name() {
		return Attribute16Name;
	}
	public void setAttribute16Name(String attribute16Name) {
		Attribute16Name = attribute16Name;
	}
	public String getAttribute16Value() {
		return Attribute16Value;
	}
	public void setAttribute16Value(String attribute16Value) {
		Attribute16Value = attribute16Value;
	}
	public String getAttribute17Name() {
		return Attribute17Name;
	}
	public void setAttribute17Name(String attribute17Name) {
		Attribute17Name = attribute17Name;
	}
	public String getAttribute17Value() {
		return Attribute17Value;
	}
	public void setAttribute17Value(String attribute17Value) {
		Attribute17Value = attribute17Value;
	}
	public String getAttribute18Name() {
		return Attribute18Name;
	}
	public void setAttribute18Name(String attribute18Name) {
		Attribute18Name = attribute18Name;
	}
	public String getAttribute18Value() {
		return Attribute18Value;
	}
	public void setAttribute18Value(String attribute18Value) {
		Attribute18Value = attribute18Value;
	}
	public String getAttribute19Name() {
		return Attribute19Name;
	}
	public void setAttribute19Name(String attribute19Name) {
		Attribute19Name = attribute19Name;
	}
	public String getAttribute19Value() {
		return Attribute19Value;
	}
	public void setAttribute19Value(String attribute19Value) {
		Attribute19Value = attribute19Value;
	}
	public String getAttribute20Name() {
		return Attribute20Name;
	}
	public void setAttribute20Name(String attribute20Name) {
		Attribute20Name = attribute20Name;
	}
	public String getAttribute20Value() {
		return Attribute20Value;
	}
	public void setAttribute20Value(String attribute20Value) {
		Attribute20Value = attribute20Value;
	}
	public String getAttribute21Name() {
		return Attribute21Name;
	}
	public void setAttribute21Name(String attribute21Name) {
		Attribute21Name = attribute21Name;
	}
	public String getAttribute21Value() {
		return Attribute21Value;
	}
	public void setAttribute21Value(String attribute21Value) {
		Attribute21Value = attribute21Value;
	}
	public String getAttribute22Name() {
		return Attribute22Name;
	}
	public void setAttribute22Name(String attribute22Name) {
		Attribute22Name = attribute22Name;
	}
	public String getAttribute22Value() {
		return Attribute22Value;
	}
	public void setAttribute22Value(String attribute22Value) {
		Attribute22Value = attribute22Value;
	}
	public String getAttribute23Name() {
		return Attribute23Name;
	}
	public void setAttribute23Name(String attribute23Name) {
		Attribute23Name = attribute23Name;
	}
	public String getAttribute23Value() {
		return Attribute23Value;
	}
	public void setAttribute23Value(String attribute23Value) {
		Attribute23Value = attribute23Value;
	}
	public String getAttribute24Name() {
		return Attribute24Name;
	}
	public void setAttribute24Name(String attribute24Name) {
		Attribute24Name = attribute24Name;
	}
	public String getAttribute24Value() {
		return Attribute24Value;
	}
	public void setAttribute24Value(String attribute24Value) {
		Attribute24Value = attribute24Value;
	}
	public String getAttribute25Name() {
		return Attribute25Name;
	}
	public void setAttribute25Name(String attribute25Name) {
		Attribute25Name = attribute25Name;
	}
	public String getAttribute25Value() {
		return Attribute25Value;
	}
	public void setAttribute25Value(String attribute25Value) {
		Attribute25Value = attribute25Value;
	}
	public String getAttribute26Name() {
		return Attribute26Name;
	}
	public void setAttribute26Name(String attribute26Name) {
		Attribute26Name = attribute26Name;
	}
	public String getAttribute26Value() {
		return Attribute26Value;
	}
	public void setAttribute26Value(String attribute26Value) {
		Attribute26Value = attribute26Value;
	}
	public String getAttribute27Name() {
		return Attribute27Name;
	}
	public void setAttribute27Name(String attribute27Name) {
		Attribute27Name = attribute27Name;
	}
	public String getAttribute27Value() {
		return Attribute27Value;
	}
	public void setAttribute27Value(String attribute27Value) {
		Attribute27Value = attribute27Value;
	}
	public String getAttribute28Name() {
		return Attribute28Name;
	}
	public void setAttribute28Name(String attribute28Name) {
		Attribute28Name = attribute28Name;
	}
	public String getAttribute28Value() {
		return Attribute28Value;
	}
	public void setAttribute28Value(String attribute28Value) {
		Attribute28Value = attribute28Value;
	}
	public String getAttribute29Name() {
		return Attribute29Name;
	}
	public void setAttribute29Name(String attribute29Name) {
		Attribute29Name = attribute29Name;
	}
	public String getAttribute29Value() {
		return Attribute29Value;
	}
	public void setAttribute29Value(String attribute29Value) {
		Attribute29Value = attribute29Value;
	}
	public String getAttribute30Name() {
		return Attribute30Name;
	}
	public void setAttribute30Name(String attribute30Name) {
		Attribute30Name = attribute30Name;
	}
	public String getAttribute30Value() {
		return Attribute30Value;
	}
	public void setAttribute30Value(String attribute30Value) {
		Attribute30Value = attribute30Value;
	}
	public String getAttribute31Name() {
		return Attribute31Name;
	}
	public void setAttribute31Name(String attribute31Name) {
		Attribute31Name = attribute31Name;
	}
	public String getAttribute31Value() {
		return Attribute31Value;
	}
	public void setAttribute31Value(String attribute31Value) {
		Attribute31Value = attribute31Value;
	}
	public String getAttribute32Name() {
		return Attribute32Name;
	}
	public void setAttribute32Name(String attribute32Name) {
		Attribute32Name = attribute32Name;
	}
	public String getAttribute32Value() {
		return Attribute32Value;
	}
	public void setAttribute32Value(String attribute32Value) {
		Attribute32Value = attribute32Value;
	}
	public String getAttribute33Name() {
		return Attribute33Name;
	}
	public void setAttribute33Name(String attribute33Name) {
		Attribute33Name = attribute33Name;
	}
	public String getAttribute33Value() {
		return Attribute33Value;
	}
	public void setAttribute33Value(String attribute33Value) {
		Attribute33Value = attribute33Value;
	}
	public String getAttribute34Name() {
		return Attribute34Name;
	}
	public void setAttribute34Name(String attribute34Name) {
		Attribute34Name = attribute34Name;
	}
	public String getAttribute34Value() {
		return Attribute34Value;
	}
	public void setAttribute34Value(String attribute34Value) {
		Attribute34Value = attribute34Value;
	}
	public String getAttribute35Name() {
		return Attribute35Name;
	}
	public void setAttribute35Name(String attribute35Name) {
		Attribute35Name = attribute35Name;
	}
	public String getAttribute35Value() {
		return Attribute35Value;
	}
	public void setAttribute35Value(String attribute35Value) {
		Attribute35Value = attribute35Value;
	}
	public String getAttribute36Name() {
		return Attribute36Name;
	}
	public void setAttribute36Name(String attribute36Name) {
		Attribute36Name = attribute36Name;
	}
	public String getAttribute36Value() {
		return Attribute36Value;
	}
	public void setAttribute36Value(String attribute36Value) {
		Attribute36Value = attribute36Value;
	}
	public String getAttribute37Name() {
		return Attribute37Name;
	}
	public void setAttribute37Name(String attribute37Name) {
		Attribute37Name = attribute37Name;
	}
	public String getAttribute37Value() {
		return Attribute37Value;
	}
	public void setAttribute37Value(String attribute37Value) {
		Attribute37Value = attribute37Value;
	}
	public String getAttribute38Name() {
		return Attribute38Name;
	}
	public void setAttribute38Name(String attribute38Name) {
		Attribute38Name = attribute38Name;
	}
	public String getAttribute38Value() {
		return Attribute38Value;
	}
	public void setAttribute38Value(String attribute38Value) {
		Attribute38Value = attribute38Value;
	}
	public String getAttribute39Name() {
		return Attribute39Name;
	}
	public void setAttribute39Name(String attribute39Name) {
		Attribute39Name = attribute39Name;
	}
	public String getAttribute39Value() {
		return Attribute39Value;
	}
	public void setAttribute39Value(String attribute39Value) {
		Attribute39Value = attribute39Value;
	}
	public String getAttribute40Name() {
		return Attribute40Name;
	}
	public void setAttribute40Name(String attribute40Name) {
		Attribute40Name = attribute40Name;
	}
	public String getAttribute40Value() {
		return Attribute40Value;
	}
	public void setAttribute40Value(String attribute40Value) {
		Attribute40Value = attribute40Value;
	}
	public String getAttribute41Name() {
		return Attribute41Name;
	}
	public void setAttribute41Name(String attribute41Name) {
		Attribute41Name = attribute41Name;
	}
	public String getAttribute41Value() {
		return Attribute41Value;
	}
	public void setAttribute41Value(String attribute41Value) {
		Attribute41Value = attribute41Value;
	}
	public String getAttribute42Name() {
		return Attribute42Name;
	}
	public void setAttribute42Name(String attribute42Name) {
		Attribute42Name = attribute42Name;
	}
	public String getAttribute42Value() {
		return Attribute42Value;
	}
	public void setAttribute42Value(String attribute42Value) {
		Attribute42Value = attribute42Value;
	}
	public String getAttribute43Name() {
		return Attribute43Name;
	}
	public void setAttribute43Name(String attribute43Name) {
		Attribute43Name = attribute43Name;
	}
	public String getAttribute43Value() {
		return Attribute43Value;
	}
	public void setAttribute43Value(String attribute43Value) {
		Attribute43Value = attribute43Value;
	}
	public String getAttribute44Name() {
		return Attribute44Name;
	}
	public void setAttribute44Name(String attribute44Name) {
		Attribute44Name = attribute44Name;
	}
	public String getAttribute44Value() {
		return Attribute44Value;
	}
	public void setAttribute44Value(String attribute44Value) {
		Attribute44Value = attribute44Value;
	}
	public String getAttribute47Name() {
		return Attribute47Name;
	}
	public void setAttribute47Name(String attribute47Name) {
		Attribute47Name = attribute47Name;
	}
	public String getAttribute47Value() {
		return Attribute47Value;
	}
	public void setAttribute47Value(String attribute47Value) {
		Attribute47Value = attribute47Value;
	}
	public String getAttribute48Name() {
		return Attribute48Name;
	}
	public void setAttribute48Name(String attribute48Name) {
		Attribute48Name = attribute48Name;
	}
	public String getAttribute48Value() {
		return Attribute48Value;
	}
	public void setAttribute48Value(String attribute48Value) {
		Attribute48Value = attribute48Value;
	}
	public String getAttribute49Name() {
		return Attribute49Name;
	}
	public void setAttribute49Name(String attribute49Name) {
		Attribute49Name = attribute49Name;
	}
	public String getAttribute49Value() {
		return Attribute49Value;
	}
	public void setAttribute49Value(String attribute49Value) {
		Attribute49Value = attribute49Value;
	}
	public String getAttribute50Name() {
		return Attribute50Name;
	}
	public void setAttribute50Name(String attribute50Name) {
		Attribute50Name = attribute50Name;
	}
	public String getAttribute50Value() {
		return Attribute50Value;
	}
	public void setAttribute50Value(String attribute50Value) {
		Attribute50Value = attribute50Value;
	}
	public String getAttribute51Name() {
		return Attribute51Name;
	}
	public void setAttribute51Name(String attribute51Name) {
		Attribute51Name = attribute51Name;
	}
	public String getAttribute51Value() {
		return Attribute51Value;
	}
	public void setAttribute51Value(String attribute51Value) {
		Attribute51Value = attribute51Value;
	}
	public String getAttribute52Name() {
		return Attribute52Name;
	}
	public void setAttribute52Name(String attribute52Name) {
		Attribute52Name = attribute52Name;
	}
	public String getAttribute52Value() {
		return Attribute52Value;
	}
	public void setAttribute52Value(String attribute52Value) {
		Attribute52Value = attribute52Value;
	}
	public String getAttribute53Name() {
		return Attribute53Name;
	}
	public void setAttribute53Name(String attribute53Name) {
		Attribute53Name = attribute53Name;
	}
	public String getAttribute53Value() {
		return Attribute53Value;
	}
	public void setAttribute53Value(String attribute53Value) {
		Attribute53Value = attribute53Value;
	}
	public String getAttribute54Name() {
		return Attribute54Name;
	}
	public void setAttribute54Name(String attribute54Name) {
		Attribute54Name = attribute54Name;
	}
	public String getAttribute54Value() {
		return Attribute54Value;
	}
	public void setAttribute54Value(String attribute54Value) {
		Attribute54Value = attribute54Value;
	}
	public String getAttribute55Name() {
		return Attribute55Name;
	}
	public void setAttribute55Name(String attribute55Name) {
		Attribute55Name = attribute55Name;
	}
	public String getAttribute55Value() {
		return Attribute55Value;
	}
	public void setAttribute55Value(String attribute55Value) {
		Attribute55Value = attribute55Value;
	}
	public String getAttribute56Name() {
		return Attribute56Name;
	}
	public void setAttribute56Name(String attribute56Name) {
		Attribute56Name = attribute56Name;
	}
	public String getAttribute56Value() {
		return Attribute56Value;
	}
	public void setAttribute56Value(String attribute56Value) {
		Attribute56Value = attribute56Value;
	}
	public String getAttribute57Name() {
		return Attribute57Name;
	}
	public void setAttribute57Name(String attribute57Name) {
		Attribute57Name = attribute57Name;
	}
	public String getAttribute57Value() {
		return Attribute57Value;
	}
	public void setAttribute57Value(String attribute57Value) {
		Attribute57Value = attribute57Value;
	}
	public String getAttribute58Name() {
		return Attribute58Name;
	}
	public void setAttribute58Name(String attribute58Name) {
		Attribute58Name = attribute58Name;
	}
	public String getAttribute58Value() {
		return Attribute58Value;
	}
	public void setAttribute58Value(String attribute58Value) {
		Attribute58Value = attribute58Value;
	}
	public String getAttribute59Name() {
		return Attribute59Name;
	}
	public void setAttribute59Name(String attribute59Name) {
		Attribute59Name = attribute59Name;
	}
	public String getAttribute59Value() {
		return Attribute59Value;
	}
	public void setAttribute59Value(String attribute59Value) {
		Attribute59Value = attribute59Value;
	}
	public String getAttribute60Name() {
		return Attribute60Name;
	}
	public void setAttribute60Name(String attribute60Name) {
		Attribute60Name = attribute60Name;
	}
	public String getAttribute60Value() {
		return Attribute60Value;
	}
	public void setAttribute60Value(String attribute60Value) {
		Attribute60Value = attribute60Value;
	}
	public String getAttribute61Name() {
		return Attribute61Name;
	}
	public void setAttribute61Name(String attribute61Name) {
		Attribute61Name = attribute61Name;
	}
	public String getAttribute61Value() {
		return Attribute61Value;
	}
	public void setAttribute61Value(String attribute61Value) {
		Attribute61Value = attribute61Value;
	}
	public String getAttribute62Name() {
		return Attribute62Name;
	}
	public void setAttribute62Name(String attribute62Name) {
		Attribute62Name = attribute62Name;
	}
	public String getAttribute62Value() {
		return Attribute62Value;
	}
	public void setAttribute62Value(String attribute62Value) {
		Attribute62Value = attribute62Value;
	}
	public String getAttribute63Name() {
		return Attribute63Name;
	}
	public void setAttribute63Name(String attribute63Name) {
		Attribute63Name = attribute63Name;
	}
	public String getAttribute63Value() {
		return Attribute63Value;
	}
	public void setAttribute63Value(String attribute63Value) {
		Attribute63Value = attribute63Value;
	}
	public String getAttribute64Name() {
		return Attribute64Name;
	}
	public void setAttribute64Name(String attribute64Name) {
		Attribute64Name = attribute64Name;
	}
	public String getAttribute64Value() {
		return Attribute64Value;
	}
	public void setAttribute64Value(String attribute64Value) {
		Attribute64Value = attribute64Value;
	}
	public String getAttribute65Name() {
		return Attribute65Name;
	}
	public void setAttribute65Name(String attribute65Name) {
		Attribute65Name = attribute65Name;
	}
	public String getAttribute65Value() {
		return Attribute65Value;
	}
	public void setAttribute65Value(String attribute65Value) {
		Attribute65Value = attribute65Value;
	}
	public String getAttribute66Name() {
		return Attribute66Name;
	}
	public void setAttribute66Name(String attribute66Name) {
		Attribute66Name = attribute66Name;
	}
	public String getAttribute66Value() {
		return Attribute66Value;
	}
	public void setAttribute66Value(String attribute66Value) {
		Attribute66Value = attribute66Value;
	}
	public String getAttribute67Name() {
		return Attribute67Name;
	}
	public void setAttribute67Name(String attribute67Name) {
		Attribute67Name = attribute67Name;
	}
	public String getAttribute67Value() {
		return Attribute67Value;
	}
	public void setAttribute67Value(String attribute67Value) {
		Attribute67Value = attribute67Value;
	}
	public String getAttribute68Name() {
		return Attribute68Name;
	}
	public void setAttribute68Name(String attribute68Name) {
		Attribute68Name = attribute68Name;
	}
	public String getAttribute68Value() {
		return Attribute68Value;
	}
	public void setAttribute68Value(String attribute68Value) {
		Attribute68Value = attribute68Value;
	}
	public String getAttribute69Name() {
		return Attribute69Name;
	}
	public void setAttribute69Name(String attribute69Name) {
		Attribute69Name = attribute69Name;
	}
	public String getAttribute69Value() {
		return Attribute69Value;
	}
	public void setAttribute69Value(String attribute69Value) {
		Attribute69Value = attribute69Value;
	}
	public String getAttribute70Name() {
		return Attribute70Name;
	}
	public void setAttribute70Name(String attribute70Name) {
		Attribute70Name = attribute70Name;
	}
	public String getAttribute70Value() {
		return Attribute70Value;
	}
	public void setAttribute70Value(String attribute70Value) {
		Attribute70Value = attribute70Value;
	}
	public String getAttribute71Name() {
		return Attribute71Name;
	}
	public void setAttribute71Name(String attribute71Name) {
		Attribute71Name = attribute71Name;
	}
	public String getAttribute71Value() {
		return Attribute71Value;
	}
	public void setAttribute71Value(String attribute71Value) {
		Attribute71Value = attribute71Value;
	}
	public String getAttribute72Name() {
		return Attribute72Name;
	}
	public void setAttribute72Name(String attribute72Name) {
		Attribute72Name = attribute72Name;
	}
	public String getAttribute72Value() {
		return Attribute72Value;
	}
	public void setAttribute72Value(String attribute72Value) {
		Attribute72Value = attribute72Value;
	}
	public String getAttribute73Name() {
		return Attribute73Name;
	}
	public void setAttribute73Name(String attribute73Name) {
		Attribute73Name = attribute73Name;
	}
	public String getAttribute73Value() {
		return Attribute73Value;
	}
	public void setAttribute73Value(String attribute73Value) {
		Attribute73Value = attribute73Value;
	}
	public String getAttribute74Name() {
		return Attribute74Name;
	}
	public void setAttribute74Name(String attribute74Name) {
		Attribute74Name = attribute74Name;
	}
	public String getAttribute74Value() {
		return Attribute74Value;
	}
	public void setAttribute74Value(String attribute74Value) {
		Attribute74Value = attribute74Value;
	}
	public String getAttribute75Name() {
		return Attribute75Name;
	}
	public void setAttribute75Name(String attribute75Name) {
		Attribute75Name = attribute75Name;
	}
	public String getAttribute75Value() {
		return Attribute75Value;
	}
	public void setAttribute75Value(String attribute75Value) {
		Attribute75Value = attribute75Value;
	}
	public String getAttribute76Name() {
		return Attribute76Name;
	}
	public void setAttribute76Name(String attribute76Name) {
		Attribute76Name = attribute76Name;
	}
	public String getAttribute76Value() {
		return Attribute76Value;
	}
	public void setAttribute76Value(String attribute76Value) {
		Attribute76Value = attribute76Value;
	}
	public String getAttribute77Name() {
		return Attribute77Name;
	}
	public void setAttribute77Name(String attribute77Name) {
		Attribute77Name = attribute77Name;
	}
	public String getAttribute77Value() {
		return Attribute77Value;
	}
	public void setAttribute77Value(String attribute77Value) {
		Attribute77Value = attribute77Value;
	}
	public String getAttribute78Name() {
		return Attribute78Name;
	}
	public void setAttribute78Name(String attribute78Name) {
		Attribute78Name = attribute78Name;
	}
	public String getAttribute78Value() {
		return Attribute78Value;
	}
	public void setAttribute78Value(String attribute78Value) {
		Attribute78Value = attribute78Value;
	}
	public String getAttribute79Name() {
		return Attribute79Name;
	}
	public void setAttribute79Name(String attribute79Name) {
		Attribute79Name = attribute79Name;
	}
	public String getAttribute79Value() {
		return Attribute79Value;
	}
	public void setAttribute79Value(String attribute79Value) {
		Attribute79Value = attribute79Value;
	}
	public String getAttribute80Name() {
		return Attribute80Name;
	}
	public void setAttribute80Name(String attribute80Name) {
		Attribute80Name = attribute80Name;
	}
	public String getAttribute80Value() {
		return Attribute80Value;
	}
	public void setAttribute80Value(String attribute80Value) {
		Attribute80Value = attribute80Value;
	}
	public String getAttribute81Name() {
		return Attribute81Name;
	}
	public void setAttribute81Name(String attribute81Name) {
		Attribute81Name = attribute81Name;
	}
	public String getAttribute81Value() {
		return Attribute81Value;
	}
	public void setAttribute81Value(String attribute81Value) {
		Attribute81Value = attribute81Value;
	}
	public String getAttribute82Name() {
		return Attribute82Name;
	}
	public void setAttribute82Name(String attribute82Name) {
		Attribute82Name = attribute82Name;
	}
	public String getAttribute82Value() {
		return Attribute82Value;
	}
	public void setAttribute82Value(String attribute82Value) {
		Attribute82Value = attribute82Value;
	}
	public String getAttribute83Name() {
		return Attribute83Name;
	}
	public void setAttribute83Name(String attribute83Name) {
		Attribute83Name = attribute83Name;
	}
	public String getAttribute83Value() {
		return Attribute83Value;
	}
	public void setAttribute83Value(String attribute83Value) {
		Attribute83Value = attribute83Value;
	}
	public String getAttribute84Name() {
		return Attribute84Name;
	}
	public void setAttribute84Name(String attribute84Name) {
		Attribute84Name = attribute84Name;
	}
	public String getAttribute84Value() {
		return Attribute84Value;
	}
	public void setAttribute84Value(String attribute84Value) {
		Attribute84Value = attribute84Value;
	}
	public String getAttribute85Name() {
		return Attribute85Name;
	}
	public void setAttribute85Name(String attribute85Name) {
		Attribute85Name = attribute85Name;
	}
	public String getAttribute85Value() {
		return Attribute85Value;
	}
	public void setAttribute85Value(String attribute85Value) {
		Attribute85Value = attribute85Value;
	}
	public String getAttribute86Name() {
		return Attribute86Name;
	}
	public void setAttribute86Name(String attribute86Name) {
		Attribute86Name = attribute86Name;
	}
	public String getAttribute86Value() {
		return Attribute86Value;
	}
	public void setAttribute86Value(String attribute86Value) {
		Attribute86Value = attribute86Value;
	}
	public String getAttribute87Name() {
		return Attribute87Name;
	}
	public void setAttribute87Name(String attribute87Name) {
		Attribute87Name = attribute87Name;
	}
	public String getAttribute87Value() {
		return Attribute87Value;
	}
	public void setAttribute87Value(String attribute87Value) {
		Attribute87Value = attribute87Value;
	}
	public String getAttribute88Name() {
		return Attribute88Name;
	}
	public void setAttribute88Name(String attribute88Name) {
		Attribute88Name = attribute88Name;
	}
	public String getAttribute88Value() {
		return Attribute88Value;
	}
	public void setAttribute88Value(String attribute88Value) {
		Attribute88Value = attribute88Value;
	}
	public String getAttribute89Name() {
		return Attribute89Name;
	}
	public void setAttribute89Name(String attribute89Name) {
		Attribute89Name = attribute89Name;
	}
	public String getAttribute89Value() {
		return Attribute89Value;
	}
	public void setAttribute89Value(String attribute89Value) {
		Attribute89Value = attribute89Value;
	}
	public String getAttribute90Name() {
		return Attribute90Name;
	}
	public void setAttribute90Name(String attribute90Name) {
		Attribute90Name = attribute90Name;
	}
	public String getAttribute90Value() {
		return Attribute90Value;
	}
	public void setAttribute90Value(String attribute90Value) {
		Attribute90Value = attribute90Value;
	}
	public String getAttribute91Name() {
		return Attribute91Name;
	}
	public void setAttribute91Name(String attribute91Name) {
		Attribute91Name = attribute91Name;
	}
	public String getAttribute91Value() {
		return Attribute91Value;
	}
	public void setAttribute91Value(String attribute91Value) {
		Attribute91Value = attribute91Value;
	}
	public String getAttribute92Name() {
		return Attribute92Name;
	}
	public void setAttribute92Name(String attribute92Name) {
		Attribute92Name = attribute92Name;
	}
	public String getAttribute92Value() {
		return Attribute92Value;
	}
	public void setAttribute92Value(String attribute92Value) {
		Attribute92Value = attribute92Value;
	}
	public String getAttribute93Name() {
		return Attribute93Name;
	}
	public void setAttribute93Name(String attribute93Name) {
		Attribute93Name = attribute93Name;
	}
	public String getAttribute93Value() {
		return Attribute93Value;
	}
	public void setAttribute93Value(String attribute93Value) {
		Attribute93Value = attribute93Value;
	}
	public String getAttribute94Name() {
		return Attribute94Name;
	}
	public void setAttribute94Name(String attribute94Name) {
		Attribute94Name = attribute94Name;
	}
	public String getAttribute94Value() {
		return Attribute94Value;
	}
	public void setAttribute94Value(String attribute94Value) {
		Attribute94Value = attribute94Value;
	}
	public String getHarmonizedCode() {
		return HarmonizedCode;
	}
	public void setHarmonizedCode(String harmonizedCode) {
		HarmonizedCode = harmonizedCode;
	}
	public String getHeight() {
		return Height;
	}
	public void setHeight(String height) {
		Height = height;
	}
	public String getLength() {
		return Length;
	}
	public void setLength(String length) {
		Length = length;
	}
	public String getWidth() {
		return Width;
	}
	public void setWidth(String width) {
		Width = width;
	}
	public String getDCCode() {
		return DCCode;
	}
	public void setDCCode(String dCCode) {
		DCCode = dCCode;
	}
	public String getClassification() {
		return Classification;
	}
	public void setClassification(String classification) {
		Classification = classification;
	}

	public String getAttribute95Value() {
		return Attribute95Value;
	}

	public void setAttribute95Value(String attribute95Value) {
		Attribute95Value = attribute95Value;
	}

	public String getAttribute95Name() {
		return Attribute95Name;
	}

	public void setAttribute95Name(String attribute95Name) {
		Attribute95Name = attribute95Name;
	}

	public String getAttribute96Name() {
		return Attribute96Name;
	}

	public void setAttribute96Name(String attribute96Name) {
		Attribute96Name = attribute96Name;
	}

	public String getAttribute96Value() {
		return Attribute96Value;
	}

	public void setAttribute96Value(String attribute96Value) {
		Attribute96Value = attribute96Value;
	}

	public void setAttribute97Name(String attribute97Name) {
		this.Attribute97Name = attribute97Name;
	}

	public String getAttribute97Name() {
		return Attribute97Name;
	}

	public void setAttribute97Value(String attribute97Value) {
		this.Attribute97Value = attribute97Value;
	}

	public String getAttribute97Value() {
		return Attribute97Value;
	}

	public void setAttribute98Name(String attribute98Name) {
		this.Attribute98Name = attribute98Name;
	}

	public String getAttribute98Name() {
		return Attribute98Name;
	}

	public void setAttribute98Value(String attribute98Value) {
		this.Attribute98Value = attribute98Value;
	}

	public String getAttribute98Value() {
		return Attribute98Value;
	}

	public String getMd5() {
		StringBuffer temp = new StringBuffer();
		temp.append(this.AuctionTitle);
		temp.append(this.InventoryNumber);
		temp.append(this.Weight);
		temp.append(this.ISBN);
		temp.append(this.UPC);
		temp.append(this.EAN);
		temp.append(this.ASIN);
		temp.append(this.MPN);
		temp.append(this.ShortDescription);
		temp.append(this.Description);
		temp.append(this.Flag);
		temp.append(this.FlagDescription);
		temp.append(this.Blocked);
		temp.append(this.BlockedComment);
		temp.append(this.Manufacturer);
		temp.append(this.Brand);
		temp.append(this.Condition);
		temp.append(this.Warranty);
		temp.append(this.SellerCost);
		temp.append(this.ProductMargin);
		temp.append(this.BuyItNowPrice);
		temp.append(this.RetailPrice);
        List<String> images = Arrays.asList(this.PictureURLs.split(","));
        images.stream().map(s -> s.substring(s.lastIndexOf("/"))).sorted().collect(Collectors.toList()).forEach(temp::append);
		temp.append(this.TaxProductCode);
		temp.append(this.SupplierCode);
		temp.append(this.SupplierPO);
		temp.append(this.WarehouseLocation);
		temp.append(this.InventorySubtitle);
		temp.append(this.RelationshipName);
		temp.append(this.VariationParentSKU);
		temp.append(this.Labels);
		temp.append(this.Attribute1Name);
		temp.append(this.Attribute1Value);
		temp.append(this.Attribute2Name);
		temp.append(this.Attribute2Value);
		temp.append(this.Attribute3Name);
		temp.append(this.Attribute3Value);
		temp.append(this.Attribute4Name);
		temp.append(this.Attribute4Value);
		temp.append(this.Attribute5Name);
		temp.append(this.Attribute5Value);
		temp.append(this.Attribute6Name);
		temp.append(this.Attribute6Value);
		temp.append(this.Attribute7Name);
		temp.append(this.Attribute7Value);
		temp.append(this.Attribute8Name);
		temp.append(this.Attribute8Value);
		temp.append(this.Attribute9Name);
		temp.append(this.Attribute9Value);
		temp.append(this.Attribute10Name);
		temp.append(this.Attribute10Value);
		temp.append(this.Attribute11Name);
		temp.append(this.Attribute11Value);
		temp.append(this.Attribute12Name);
		temp.append(this.Attribute12Value);
		temp.append(this.Attribute13Name);
		temp.append(this.Attribute13Value);
		temp.append(this.Attribute14Name);
		temp.append(this.Attribute14Value);
		temp.append(this.Attribute15Name);
		temp.append(this.Attribute15Value);
		temp.append(this.Attribute16Name);
		temp.append(this.Attribute16Value);
		temp.append(this.Attribute17Name);
		temp.append(this.Attribute17Value);
		temp.append(this.Attribute18Name);
		temp.append(this.Attribute18Value);
		temp.append(this.Attribute19Name);
		temp.append(this.Attribute19Value);
		temp.append(this.Attribute20Name);
		temp.append(this.Attribute20Value);
		temp.append(this.Attribute21Name);
		temp.append(this.Attribute21Value);
		temp.append(this.Attribute22Name);
		temp.append(this.Attribute22Value);
		temp.append(this.Attribute23Name);
		temp.append(this.Attribute23Value);
		temp.append(this.Attribute24Name);
		temp.append(this.Attribute24Value);
		temp.append(this.Attribute25Name);
		temp.append(this.Attribute25Value);
		temp.append(this.Attribute26Name);
		temp.append(this.Attribute26Value);
		temp.append(this.Attribute27Name);
		temp.append(this.Attribute27Value);
		temp.append(this.Attribute28Name);
		temp.append(this.Attribute28Value);
		temp.append(this.Attribute29Name);
		temp.append(this.Attribute29Value);
		temp.append(this.Attribute30Name);
		temp.append(this.Attribute30Value);
		temp.append(this.Attribute31Name);
		temp.append(this.Attribute31Value);
		temp.append(this.Attribute32Name);
		temp.append(this.Attribute32Value);
		temp.append(this.Attribute33Name);
		temp.append(this.Attribute33Value);
		temp.append(this.Attribute34Name);
		temp.append(this.Attribute34Value);
		temp.append(this.Attribute35Name);
		temp.append(this.Attribute35Value);
		temp.append(this.Attribute36Name);
		temp.append(this.Attribute36Value);
		temp.append(this.Attribute37Name);
		temp.append(this.Attribute37Value);
		temp.append(this.Attribute38Name);
		temp.append(this.Attribute38Value);
		temp.append(this.Attribute39Name);
		temp.append(this.Attribute39Value);
		temp.append(this.Attribute40Name);
		temp.append(this.Attribute40Value);
		temp.append(this.Attribute41Name);
		temp.append(this.Attribute41Value);
		temp.append(this.Attribute42Name);
		temp.append(this.Attribute42Value);
		temp.append(this.Attribute43Name);
		temp.append(this.Attribute43Value);
		temp.append(this.Attribute44Name);
		temp.append(this.Attribute44Value);
		temp.append(this.Attribute47Name);
		temp.append(this.Attribute47Value);
		temp.append(this.Attribute48Name);
		temp.append(this.Attribute48Value);
		temp.append(this.Attribute49Name);
		temp.append(this.Attribute49Value);
		temp.append(this.Attribute50Name);
		temp.append(this.Attribute50Value);
		temp.append(this.Attribute51Name);
		temp.append(this.Attribute51Value);
		temp.append(this.Attribute52Name);
		temp.append(this.Attribute52Value);
		temp.append(this.Attribute53Name);
		temp.append(this.Attribute53Value);
		temp.append(this.Attribute54Name);
		temp.append(this.Attribute54Value);
		temp.append(this.Attribute55Name);
		temp.append(this.Attribute55Value);
		temp.append(this.Attribute56Name);
		temp.append(this.Attribute56Value);
		temp.append(this.Attribute57Name);
		temp.append(this.Attribute57Value);
		temp.append(this.Attribute58Name);
		temp.append(this.Attribute58Value);
		temp.append(this.Attribute59Name);
		temp.append(this.Attribute59Value);
		temp.append(this.Attribute60Name);
		temp.append(this.Attribute60Value);
		temp.append(this.Attribute61Name);
		temp.append(this.Attribute61Value);
		temp.append(this.Attribute62Name);
		temp.append(this.Attribute62Value);
		temp.append(this.Attribute63Name);
		temp.append(this.Attribute63Value);
		temp.append(this.Attribute64Name);
		temp.append(this.Attribute64Value);
		temp.append(this.Attribute65Name);
		temp.append(this.Attribute65Value);
		temp.append(this.Attribute66Name);
		temp.append(this.Attribute66Value);
		temp.append(this.Attribute67Name);
		temp.append(this.Attribute67Value);
		temp.append(this.Attribute68Name);
		temp.append(this.Attribute68Value);
		temp.append(this.Attribute69Name);
		temp.append(this.Attribute69Value);
		temp.append(this.Attribute70Name);
		temp.append(this.Attribute70Value);
		temp.append(this.Attribute71Name);
		temp.append(this.Attribute71Value);
		temp.append(this.Attribute72Name);
		temp.append(this.Attribute72Value);
		temp.append(this.Attribute73Name);
		temp.append(this.Attribute73Value);
		temp.append(this.Attribute74Name);
		temp.append(this.Attribute74Value);
		temp.append(this.Attribute75Name);
		temp.append(this.Attribute75Value);
		temp.append(this.Attribute76Name);
		temp.append(this.Attribute76Value);
		temp.append(this.Attribute77Name);
		temp.append(this.Attribute77Value);
		temp.append(this.Attribute78Name);
		temp.append(this.Attribute78Value);
		temp.append(this.Attribute79Name);
		temp.append(this.Attribute79Value);
		temp.append(this.Attribute80Name);
		temp.append(this.Attribute80Value);
		temp.append(this.Attribute81Name);
		temp.append(this.Attribute81Value);
		temp.append(this.Attribute82Name);
		temp.append(this.Attribute82Value);
		temp.append(this.Attribute83Name);
		temp.append(this.Attribute83Value);
		temp.append(this.Attribute84Name);
		temp.append(this.Attribute84Value);
		temp.append(this.Attribute85Name);
		temp.append(this.Attribute85Value);
		temp.append(this.Attribute86Name);
		temp.append(this.Attribute86Value);
		temp.append(this.Attribute87Name);
		temp.append(this.Attribute87Value);
		temp.append(this.Attribute88Name);
		temp.append(this.Attribute88Value);
		temp.append(this.Attribute89Name);
		temp.append(this.Attribute89Value);
		temp.append(this.Attribute90Name);
		temp.append(this.Attribute90Value);
		temp.append(this.Attribute91Name);
		temp.append(this.Attribute91Value);
		temp.append(this.Attribute92Name);
		temp.append(this.Attribute92Value);
		temp.append(this.Attribute93Name);
		temp.append(this.Attribute93Value);
		temp.append(this.Attribute94Name);
		temp.append(this.Attribute94Value);
		temp.append(this.HarmonizedCode);
		temp.append(this.Height);
		temp.append(this.Length);
		temp.append(this.Width);
		temp.append(this.DCCode);
		temp.append(this.Classification);
		temp.append(this.Attribute95Name);
		temp.append(this.Attribute95Value);
		temp.append(this.Attribute96Name);
		temp.append(this.Attribute96Value);
		temp.append(this.Attribute97Name);
		temp.append(this.Attribute97Value);
		temp.append(this.Attribute98Name);
		temp.append(this.Attribute98Value);
		return MD5.getMD5(temp.toString());
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
}
