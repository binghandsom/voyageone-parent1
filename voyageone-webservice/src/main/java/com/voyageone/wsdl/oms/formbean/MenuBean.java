/**
 * 菜单Bean
 */
package com.voyageone.wsdl.oms.formbean;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jacky
 *
 */
@XmlRootElement//必须
public class MenuBean {

	/**
	 * 菜单名称
	 */
	private String menuName;

	/**
	 * 菜单链接
	 */
	private String menuUrl;

	/**
	 * 子菜单
	 */
	private List<MenuBean> subMenuList;

	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * @return the menuUrl
	 */
	public String getMenuUrl() {
		return menuUrl;
	}

	/**
	 * @param menuUrl the menuUrl to set
	 */
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	/**
	 * @return the subMenuList
	 */
	public List<MenuBean> getSubMenuList() {
		return subMenuList;
	}

	/**
	 * @param subMenuList the subMenuList to set
	 */
	public void setSubMenuList(List<MenuBean> subMenuList) {
		this.subMenuList = subMenuList;
	}

}
