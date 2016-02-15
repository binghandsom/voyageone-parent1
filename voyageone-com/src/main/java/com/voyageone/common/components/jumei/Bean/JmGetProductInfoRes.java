package com.voyageone.common.components.jumei.Bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author james.li on 2016/2/15.
 * @version 2.0.0
 */
public class JmGetProductInfoRes extends JmProductBean {
    private String hash_ids;

    public String getHash_ids() {
        return hash_ids;
    }

    public void setHash_ids(String hash_ids) {
        this.hash_ids = hash_ids;
    }
}
