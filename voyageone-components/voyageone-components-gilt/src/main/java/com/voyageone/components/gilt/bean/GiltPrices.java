package com.voyageone.components.gilt.bean;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltPrices {

    private GiltMoney retail;//	The manufacturer's suggested retail price for this item

    private GiltMoney sale;//	The recommended sale price for this item if different from retail. This price should be displayed to customers when purchasing this item.

    private GiltMoney cost;//	The actual cost of the item the partner will pay.

    public GiltMoney getRetail() {
        return retail;
    }

    public void setRetail(GiltMoney retail) {
        this.retail = retail;
    }

    public GiltMoney getSale() {
        return sale;
    }

    public void setSale(GiltMoney sale) {
        this.sale = sale;
    }

    public GiltMoney getCost() {
        return cost;
    }

    public void setCost(GiltMoney cost) {
        this.cost = cost;
    }
}
