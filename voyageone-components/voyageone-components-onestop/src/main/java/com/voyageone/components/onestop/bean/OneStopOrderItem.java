package com.voyageone.components.onestop.bean;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: zhen.wang
 * @date: 2016/11/23 13:06
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OneStopOrderItem {

    /*
    * Upc: string
    * ModelName: string
    * ColorName: string
    * ColorSku: string
    * SizeName: string
    * SizeSku: string
    * ItemCost: 0
    * ItemDiscount: 0
    * ItemTax: [{"TaxAmount": 0,"Jurisdiction": "string","TaxCode": "string"}]
    * ItemTotal: 0
    * IsReturnable: true
    * ShipmentId: 0
    * FulfillmentType: string
    * FacilityId: 0
    * ProductId: 0
    * ItemStatus: string
    * DisplayStatus: string
    * IsShipping: true
    * IsRefunded: true
    * RefundId: 0
    * CustomData: {}
    * IsReturned: true
    * PendingRmaNumber: 0
    * RefundIds: [0]
    * */
    private String Upc;

    private String ModelName;

    private String ColorName;

    private String ColorSku;

    private String SizeName;

    private String SizeSku;

    private double ItemCost;

    private int ItemDiscount;

    private List<ItemTax> ItemTax;

    private double ItemTotal;

    private boolean IsReturnable;

    private int ShipmentId;

    private String FulfillmentType;

    private int FacilityId;

    private int ProductId;

    private String ItemStatus;

    private String DisplayStatus;

    private boolean IsShipping;

    private boolean IsRefunded;

    private int RefundId;

    private Map<String,String> CustomData;

    private boolean IsReturned;

    private int PendingRmaNumber;

    private List<Integer> RefundIds;

    public void setUpc(String Upc) {
        this.Upc = Upc;
    }

    public String getUpc() {
        return this.Upc;
    }

    public void setModelName(String ModelName) {
        this.ModelName = ModelName;
    }

    public String getModelName() {
        return this.ModelName;
    }

    public void setColorName(String ColorName) {
        this.ColorName = ColorName;
    }

    public String getColorName() {
        return this.ColorName;
    }

    public void setColorSku(String ColorSku) {
        this.ColorSku = ColorSku;
    }

    public String getColorSku() {
        return this.ColorSku;
    }

    public void setSizeName(String SizeName) {
        this.SizeName = SizeName;
    }

    public String getSizeName() {
        return this.SizeName;
    }

    public void setSizeSku(String SizeSku) {
        this.SizeSku = SizeSku;
    }

    public String getSizeSku() {
        return this.SizeSku;
    }

    public void setItemCost(double ItemCost) {
        this.ItemCost = ItemCost;
    }

    public double getItemCost() {
        return this.ItemCost;
    }

    public void setItemDiscount(int ItemDiscount) {
        this.ItemDiscount = ItemDiscount;
    }

    public int getItemDiscount() {
        return this.ItemDiscount;
    }

    public void setItemTax(List<ItemTax> ItemTax) {
        this.ItemTax = ItemTax;
    }

    public List<ItemTax> getItemTax() {
        return this.ItemTax;
    }

    public void setItemTotal(double ItemTotal) {
        this.ItemTotal = ItemTotal;
    }

    public double getItemTotal() {
        return this.ItemTotal;
    }

    public void setIsReturnable(boolean IsReturnable) {
        this.IsReturnable = IsReturnable;
    }

    public boolean getIsReturnable() {
        return this.IsReturnable;
    }

    public void setShipmentId(int ShipmentId) {
        this.ShipmentId = ShipmentId;
    }

    public int getShipmentId() {
        return this.ShipmentId;
    }

    public void setFulfillmentType(String FulfillmentType) {
        this.FulfillmentType = FulfillmentType;
    }

    public String getFulfillmentType() {
        return this.FulfillmentType;
    }

    public void setFacilityId(int FacilityId) {
        this.FacilityId = FacilityId;
    }

    public int getFacilityId() {
        return this.FacilityId;
    }

    public void setProductId(int ProductId) {
        this.ProductId = ProductId;
    }

    public int getProductId() {
        return this.ProductId;
    }

    public void setItemStatus(String ItemStatus) {
        this.ItemStatus = ItemStatus;
    }

    public String getItemStatus() {
        return this.ItemStatus;
    }

    public void setDisplayStatus(String DisplayStatus) {
        this.DisplayStatus = DisplayStatus;
    }

    public String getDisplayStatus() {
        return this.DisplayStatus;
    }

    public void setIsShipping(boolean IsShipping) {
        this.IsShipping = IsShipping;
    }

    public boolean getIsShipping() {
        return this.IsShipping;
    }

    public void setIsRefunded(boolean IsRefunded) {
        this.IsRefunded = IsRefunded;
    }

    public boolean getIsRefunded() {
        return this.IsRefunded;
    }

    public void setRefundId(int RefundId) {
        this.RefundId = RefundId;
    }

    public int getRefundId() {
        return this.RefundId;
    }

    public void setCustomData(Map<String,String> CustomData) {
        this.CustomData = CustomData;
    }

    public Map<String,String> getCustomData() {
        return this.CustomData;
    }

    public void setIsReturned(boolean IsReturned) {
        this.IsReturned = IsReturned;
    }

    public boolean getIsReturned() {
        return this.IsReturned;
    }

    public void setPendingRmaNumber(int PendingRmaNumber) {
        this.PendingRmaNumber = PendingRmaNumber;
    }

    public int getPendingRmaNumber() {
        return this.PendingRmaNumber;
    }

    public void setRefundIds(List<Integer> RefundIds) {
        this.RefundIds = RefundIds;
    }

    public List<Integer> getRefundIds() {
        return this.RefundIds;
    }

    public class ItemTax {
        /*
        * TaxAmount: 0
        * Jurisdiction: string
        * TaxCode: string
        * */
        private double TaxAmount;

        private String Jurisdiction;

        private String TaxCode;

        public double getTaxAmount() {
            return TaxAmount;
        }

        public void setTaxAmount(double taxAmount) {
            TaxAmount = taxAmount;
        }


        public void setJurisdiction(String Jurisdiction) {
            this.Jurisdiction = Jurisdiction;
        }

        public String getJurisdiction() {
            return this.Jurisdiction;
        }

        public void setTaxCode(String TaxCode) {
            this.TaxCode = TaxCode;
        }

        public String getTaxCode() {
            return this.TaxCode;
        }

    }



}
