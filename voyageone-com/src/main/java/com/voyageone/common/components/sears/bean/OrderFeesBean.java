package com.voyageone.common.components.sears.bean;

/**
 * Created by james.li on 2015/11/23.
 */
public class OrderFeesBean {
    private double voCommission;
    private double alipayFee;
    private double tmallCommission;

    public double getVoCommission() {
        return voCommission;
    }

    public void setVoCommission(double voCommission) {
        this.voCommission = voCommission;
    }

    public double getAlipayFee() {
        return alipayFee;
    }

    public void setAlipayFee(double alipayFee) {
        this.alipayFee = alipayFee;
    }

    public double getTmallCommission() {
        return tmallCommission;
    }

    public void setTmallCommission(double tmallCommission) {
        this.tmallCommission = tmallCommission;
    }
}
