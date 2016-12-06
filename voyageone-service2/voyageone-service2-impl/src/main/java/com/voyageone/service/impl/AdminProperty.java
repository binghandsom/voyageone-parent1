package com.voyageone.service.impl;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/29
 */
public interface AdminProperty {
	
	interface Props {
		
		String ADMIN_SQL_PATH = "admin.sql.path";
		
	}
	
	interface Tpls {
		
		String PROC_NEW_SHOP_SQL = "proc_new_shop_sql.ftl";
		
	}

	public  enum RoleType {
		ADMIN ("Role_Admin", "管理员",  1),
		CS("Role_CS","客服", 2),
		CS_MANAGER("Role_CS_Manager", "客服主管", 3),
		WHS("Role_WHS","仓库", 4),
		OP("Role_OP", "运营",  5),
		VENDOR("Role_Vendor", "Vendor",  6),
		TRANSLATOR("Role_Translator", "翻译",  7),
		OTHER("Role_OTHER", "其他",  99);

		private String _typeName;
		private String _name;
		private int _id;


		RoleType(String typeName , String name, int id) {
			this._typeName = typeName;
			this._name = name;
			this._id = id;
		}

		public String getTypeName() {
			return this._typeName;
		}

		public String getName() {
			return this._name;
		}

		public int getId() {
			return this._id;
		}
	}

}
