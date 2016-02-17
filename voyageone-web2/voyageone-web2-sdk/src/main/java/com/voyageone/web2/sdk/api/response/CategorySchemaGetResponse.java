package com.voyageone.web2.sdk.api.response;

import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;


/**
 * /categorySchema/selectOne product get response
 * Created on 2015-1-28
 *
 * @author lewis
 * @version 2.0.0
 * @since. 2.0.0
 */
public class CategorySchemaGetResponse extends VoApiResponse {

	private String categoryId;

	private String categoryFullPath;

	private List<Field> masterFields;

	private Field skuFields;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryFullPath() {
		return categoryFullPath;
	}

	public void setCategoryFullPath(String categoryFullPath) {
		this.categoryFullPath = categoryFullPath;
	}

	public List<Field> getMasterFields() {
		return masterFields;
	}

	public void setMasterFields(List<Field> masterFields) {
		this.masterFields = masterFields;
	}

	public Field getSkuFields() {
		return skuFields;
	}

	public void setSkuFields(Field skuFields) {
		this.skuFields = skuFields;
	}

}
