package com.voyageone.components.gilt.bean;

import java.util.Date;
import java.util.List;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltSku {

    private Long id;//	A unique number to identify this sku.

    //Local language
    private GiltLocalLanguages locale;//	See Locale

    private String name;//	The name or title for this sku.

    private List<GiltImage> images;//	See Image

    private List<Object> attributes;//	See Attribute

    private GiltBrand brand;//	See Brand

    private String country_code;//	The country of origin in ISO3166 Alpha 3 Format

    private List<String> product_codes;//	The list of universal product codes (UPC) at Gilt - either assigned by Gilt or provided by the originating vendor. The first string will always be Gilt's identifier which can be seen as a sticker on the item's packaging.

    private Long product_id;//	The product id can be used to aggregate multiple skus under the same product.

    private Long product_look_id;//	Aggregates skus based upon Gilt's definition of a "look" (Style and Color)

    private String description;//	A word, or set of words, by which a product is described. This is more descriptive than the title and should include the primary material, as well as product dimensions and fit (if applicable).

    private GiltPrices prices;//	See Prices

    private List<GiltCategory> categories;//	See Category

    private Date timestamp; //time

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GiltLocalLanguages getLocale() {
        return locale;
    }

    public void setLocale(GiltLocalLanguages locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GiltImage> getImages() {
        return images;
    }

    public void setImages(List<GiltImage> images) {
        this.images = images;
    }

    public List<Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }

    public GiltBrand getBrand() {
        return brand;
    }

    public void setBrand(GiltBrand brand) {
        this.brand = brand;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public List<String> getProduct_codes() {
        return product_codes;
    }

    public void setProduct_codes(List<String> product_codes) {
        this.product_codes = product_codes;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getProduct_look_id() {
        return product_look_id;
    }

    public void setProduct_look_id(Long product_look_id) {
        this.product_look_id = product_look_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GiltPrices getPrices() {
        return prices;
    }

    public void setPrices(GiltPrices prices) {
        this.prices = prices;
    }

    public List<GiltCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<GiltCategory> categories) {
        this.categories = categories;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
