package com.rosense.module.system.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.AuthForm;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.UserForm;

public interface IUserService {

	/**
	 * 添加用户
	 */
	public Msg addUser(UserForm form);

	/**
	 * 删除用户
	 */
	public Msg delete(UserForm form);

	/**
	 * 修改用户
	 */
	public Msg update(UserForm form);

	/**
	 * 获取一个用户对象
	 */
	public UserForm get(String id);

	/**
	 * 查询用户
	 */
	List<UserForm> searchUsers(String content);

	/**
	 * 用户查询
	 */
	public DataGrid datagrid(UserForm form,String selectType,String searchKeyName);

	/**
	 * 查询员工
	 */
	public DataGrid datagridperson(UserForm form,String selectType,String searchKeyName);
	
	//查询离职用户
	public DataGrid datagridleaveperson(UserForm form);

	public DataGrid datagrid_ref(UserForm form);

	/**
	 * 用户添加角色
	 */
	public Msg batchUserRole(UserForm form);

	/**
	 * 重设密码
	 */
	public Msg resetPwd(String id);

	/**
	 * 修改密码
	 */
	public Msg updatePwd(UserForm form);

	/**
	 * 锁定账号
	 */
	public Msg lockUser(String id);

	/**
	 * 用户登陆验证
	 */
	public LoginUser loginCheck(LoginUser form);

	/**
	 * 获取权限
	 */
	public AuthForm getAuth(String userId);

	/**
	 * 为用户添加角色
	 */
	Msg addRoleForUser(String roleId, String users);

	/**
	 * 删除用户角色
	 */
	Msg deleteRoleForUser(String roleId, String userId);

	/**
	 * 为用户添加部门
	 */
	Msg addOrgForUser(String orgId, String users);

	/**
	 * 删除用户部门
	 */
	Msg deleteOrgForUser(String userId);

	/**
	 * 为用户添加职位
	 */
	public Msg addPositionForUser(String orgId, String userIds);

	/**
	 * 删除用户职位
	 */
	public Msg deletePositionForUser(String userId);

	/**
	 * 修改头像
	 */
	public Msg updatePhoto(String photo);
	
	/**
	 * 导入用户信息
	 */
	public Msg importFile(List<UserForm> importUserList);
 
	public DataGrid orgdatagridperson(HttpSession session,UserForm form);
	
	
	public int equlasValAccount(String account);

	public List<UserForm> searchUsersData();
	
	/**
	 * 邮件通知全体员工
	 * @param id
	 * @return
	 */
	public Msg notice(String id);
	
	/**
	 * 查询员工
	 */
	public DataGrid commondatagridperson(UserForm form,String selectType,String searchKeyName);
	
	/**
	 * 查看个人信息
	 */
	public DataGrid datagridpersonal();
	
	/**
	 * 获取当前用户信息
	 */
	public UserForm selectCurUser();
	
	/**
	 * 获取当前用户的部门主管
	 * @return
	 */
	public List<UserForm> chargeTree();
}
