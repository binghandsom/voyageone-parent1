/**
 * 
 */
package com.voyageone.components.tmall.bean;

/**
 * @author jacky
 *
 */
public class ItemSchema {

    private int result;
    private String itemResult;
    private Long cid;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getItemResult() {
        return itemResult;
    }

    public void setItemResult(String itemResult) {
        this.itemResult = itemResult;
    }
}
