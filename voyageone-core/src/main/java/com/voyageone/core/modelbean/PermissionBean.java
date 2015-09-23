package com.voyageone.core.modelbean;

import java.util.Map;
import java.util.ArrayList;

public class PermissionBean {

	private String propertyId;
	
	private String propertyName;

	private ArrayList<String> propertyRoles;

	private Map<String, Boolean> permissions;

	/**
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the permissions
	 */
	public Map<String, Boolean> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(Map<String, Boolean> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the propertyRoles
	 */
	public ArrayList<String> getPropertyRoles() {
		return propertyRoles;
	}

	/**
	 * @param propertyRoles the roles to set
	 */
	public void setPropertyRoles(ArrayList<String> propertyRoles) {
		this.propertyRoles = propertyRoles;
	}

}
