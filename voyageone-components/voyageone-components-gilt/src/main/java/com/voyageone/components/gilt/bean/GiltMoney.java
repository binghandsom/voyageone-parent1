package com.voyageone.components.gilt.bean;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltMoney {

    private	 Double	value	;//	The amount of money.

    private	 String	currency	;//	The ISO 4217 currency code.

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
