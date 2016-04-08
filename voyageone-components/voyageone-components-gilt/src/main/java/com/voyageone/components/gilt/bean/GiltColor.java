package com.voyageone.components.gilt.bean;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltColor {

    private String name;//	The customer-friendly name of this color.

    private Long nrf_code;//	The NRF standard color code - see https://nrf.com/resources/retail-library/standard-color-and-size-codes

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNrf_code() {
        return nrf_code;
    }

    public void setNrf_code(Long nrf_code) {
        this.nrf_code = nrf_code;
    }
}
