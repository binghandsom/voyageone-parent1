package com.voyageone.core.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.core.emum.UserEditEnum;
import com.voyageone.core.formbean.InFormUser;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDao extends BaseDao{

	
	public List<Map<String, Object>> getUserList(Map <String,Object> data){
		List<Map<String, Object>> userList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_getUserList",data);
		
		if (userList == null || userList.size() == 0) {
			userList = new ArrayList<>();
		}
		return userList;
	}
	
	public int addUser(InFormUser formUser)
	{
		int ret=0;
		formUser.setActive(true);
		ret=updateTemplate.insert(Constants.DAO_NAME_SPACE_CORE + "ct_user_insert_user", formUser);
		return ret;
	}
	
	public int updateUser(InFormUser formUser)
	{
		int ret=0;
		ret=updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "ct_user_update_user", formUser);
		return ret;
	}
	public int delUser(int id)
	{
		int ret=0;
		ret=updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "ct_user_del_user", id);
		return ret;
	}
	
	public List<Map<String, Object>> getUserRoleInfoById(int userId)
	{
		List<Map<String, Object>> userRoleInfo = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_get_UserRole",userId);
		
		if (userRoleInfo == null || userRoleInfo.size() == 0) {
			userRoleInfo = new ArrayList<>();
		}
		return userRoleInfo;
	}
	
	public List<Map<String, Object>> getUserPermissionInfoById(int userId)
	{
		List<Map<String, Object>> userPermissionInfo = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_get_UserPermission",userId);
		
		if (userPermissionInfo == null || userPermissionInfo.size() == 0) {
			userPermissionInfo = new ArrayList<>();
		}
		return userPermissionInfo;
	}
	
	/*-----------------------------------编辑ct_user_role_property和ct_user_permission---------------------------------------*/
	//共同的检索
	public List<Map<String, Object>> selectHandle(Map<String, Object> selectData,UserEditEnum type)
	{
		List<Map<String, Object>> userRoleInfo = 
				(List) updateTemplate.selectList(type.getSql_select(),selectData);	
		if (userRoleInfo == null || userRoleInfo.size() == 0) {
			userRoleInfo = new ArrayList<>();
		}
		return userRoleInfo;
	}
	
	//共同的更新数据 
	public int updateHandle(Map<String, Object> data,UserEditEnum type)
	{
		int ret=0;
		ret=updateTemplate.update(type.getSql_update(), data);
		return ret;
	}
	//共同的删除数据
	public int delHandle(int id,UserEditEnum type)
	{
		int ret=0;
		Map<String,Object> delData=new HashMap<String,Object>();
		delData.put("active", false);
		delData.put("id",id+"");
		
		ret=this.updateHandle(delData,type);
		return ret;
	}
	//共同的追加数据
	public int insertHandle(Map<String, Object> data,UserEditEnum type)
	{
		int ret=0;
//		data.put("id", 0);
		ret=updateTemplate.insert(type.getSql_insert(), data);
		return ret;
	}
	
	//取得ct_role的信息
	public List<Map<String, Object>> getRole(){
		
		List<Map<String, Object>>roleInfo=(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_select_all_role");
		if (roleInfo == null || roleInfo.size() == 0) {
			roleInfo = new ArrayList<Map<String, Object>>();
		}
		
		return roleInfo;
	}
	public Map<String, Object> getAllRolePermissionName(){
		Map<String, Object> data =new  HashMap<String, Object>();
		
		data.put("role", (List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_select_all_role"));
		data.put("property", (List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_select_all_property"));
		data.put("company", (List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_select_all_company"));
		
		List<Map<String,Object>> application=(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_select_application");
		List<Map<String,Object>> module=(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_select_module");
		List<Map<String,Object>> controller=(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_select_all_controller");
		List<Map<String,Object>> action=(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_select_all_action");
		
		appGroup(controller,action,"controller_id");
		appGroup(module,controller,"module_id");
		appGroup(application,module,"application_id");
		data.put("application", application);
		return data;
	}
	/**
	 * 最佳指定application下的所有权限
	 * @param data
	 * @return
	 */
	public int insertRoleAllPermission(Map<String,Object> data){
		int ret=0;
		ret=updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "ct_role_permission_insert_all", data);
		return ret;
	}
	
	public int insertUserAllPermission(Map<String, Object> data) {
		int ret=0;
		ret=updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "ct_user_insert_UserPermission_All", data);
		return ret;
	}	
	private void appGroup(List<Map<String,Object>> parent,List<Map<String,Object>> children,String key)
	{
		for (Map<String, Object> p : parent) {
			List<Map<String,Object>> item=new ArrayList<Map<String,Object>>();
			Iterator<Map<String, Object>> c=children.iterator();
			while(c.hasNext())
			{
				Map<String, Object>child=c.next();
				if(Integer.parseInt(p.get("id").toString()) == Integer.parseInt(child.get(key).toString()))
				{
					item.add(child);
					c.remove();;
				}
			}
			p.put("children", item);
		}
	}


	public int updateUserConfig(Map<String, Object> requestMap){
		
		return updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "update_ct_user_config", requestMap);
	}
	
	public int insertUserConfig(Map<String, Object> requestMap){
		return updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "insert_ct_user_config", requestMap);
	}
	

}
