package com.voyageone.batch.wms.modelbean;

import java.util.List;

/**
 * @author jack
 * 分配库存Bean
 */
public class AllotInventoryDetailBean {

    /**
     * 订单号
     */
    private long order_number;

    /**
     * 物品Items
     */
    private List<ReservationBean> lstReservation;

    /**
     * 分配错误
     */
    private boolean allot_error;

    /**
     * 存在再分配记录
     */
    private boolean allot_again;

    public long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(long order_number) {
        this.order_number = order_number;
    }

    public List<ReservationBean> getLstReservation() {
        return lstReservation;
    }

    public void setLstReservation(List<ReservationBean> lstReservation) {
        this.lstReservation = lstReservation;
    }

    public boolean isAllot_error() {
        return allot_error;
    }

    public void setAllot_error(boolean allot_error) {
        this.allot_error = allot_error;
    }

    public boolean isAllot_again() {
        return allot_again;
    }

    public void setAllot_again(boolean allot_again) {
        this.allot_again = allot_again;
    }
}
