package com.rosense.module.system.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.ImageUtils;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.service.IRoleService;
import com.rosense.module.system.service.IUserService;
import com.rosense.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/user")
public class UserAction extends BaseController {
	@Inject
	private IUserService userService;
	@Inject
	private IRoleService roleService;
	
	@RequestMapping("/userrole_main_UI.do")
	public String userrole_main_UI(String id, Model model) {
		model.addAttribute("id", id);
		UserForm form = this.userService.get(id);
		if (form != null) {
			model.addAttribute("userroles", form.getRole_ids());
		}
		return Const.SYSTEM + "userrole_main_UI";
	}

	/**
	 * 添加用户
	 *//*
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(UserForm form) throws Exception {
		return this.userService.addUser(form);
	}*/

	/**
	 * 删除用户
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(UserForm form) throws Exception {
		return this.userService.delete(form);
	}

	/**
	* 锁定账号
	*/
	@RequestMapping("/lockUser.do")
	@ResponseBody
	public Msg lockUser(String id) {
		return this.userService.lockUser(id);
	}

	/**
	 * 重置密码
	 */
	@RequestMapping("/resetPwd.do")
	@ResponseBody
	public Msg resetPwd(String id) {
		return this.userService.resetPwd(id);
	}

	/**
	 * 修改账号
	 */
	/*@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(UserForm form) throws Exception {
		return this.userService.update(form);
	}*/

	@RequestMapping("/get.do")
	@ResponseBody
	public UserForm get(UserForm form) throws Exception {
		return this.userService.get(form.getId());
	}

	/**
	 * 查询账号
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(UserForm form,String selectType,String searchKeyName) throws Exception {
		return this.userService.datagrid(form,selectType,searchKeyName);
	}

	/**
	 * 查询用户信息
	 */
	/*@RequestMapping("/datagridperson.do")
	@ResponseBody
	public DataGrid datagridperson(UserForm form,String selectType,String searchKeyName) throws Exception {
		return this.userService.datagridperson(form,selectType,searchKeyName);
	}*/
	
	/*@RequestMapping("/commondatagridperson.do")
	@ResponseBody
	public DataGrid commondatagridperson(UserForm form,String selectType,String searchKeyName) throws Exception {
		return this.userService.commondatagridperson(form,selectType,searchKeyName);
	}
	*/
	/**
	 * 查询用户信息
	 */
	/*@RequestMapping("/datagridleaveperson.do")
	@ResponseBody
	public DataGrid datagridleaveperson(UserForm form) throws Exception {
		return this.userService.datagridleaveperson(form);
	}*/

	/**
	 * 关联查询一些部门，角色，职位相关 的用户
	 */
	/*@RequestMapping("/datagrid_ref.do")
	@ResponseBody
	public DataGrid datagrid_ref(UserForm form) throws Exception {
		if (StringUtil.isEmpty(form.getId())) {
			form.setId(StringUtil.toString(getSession().getAttribute("ref_id"), ""));
		} else {
			getSession().setAttribute("ref_id", form.getId());
		}
		return this.userService.datagrid_ref(form);
	}*/

	/**
	 * 查询用户
	 */
	/*@RequestMapping("/search.do")
	@ResponseBody
	public List<UserForm> search(String content) throws Exception {
		return this.userService.searchUsers(content);
	}*/

	/**
	 * 修改头像
	 */
	@RequestMapping("/updatephoto.do")
	@ResponseBody
	public Msg updatephoto() {
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String userId = WebContextUtil.getUserId();
			try {
				String savePath = WebContextUtil.getAttachedRootPath("photo");
				new File(savePath).mkdirs();
				String path = WebContextUtil.getAttachedPath("photo");
				List<MultipartFile> files = multipartRequest.getFiles(Const.uploadFieldName);
				if (files == null || files.size() < 1) {
					throw new RuntimeException("没有发生上传的文件！请检查");
				}
				if (files.size() > 0) {
					MultipartFile multipartFile = files.get(0);
					String orginalName = multipartFile.getOriginalFilename();
					String extension = FilenameUtils.getExtension(orginalName);
					String newName = userId + "." + extension;
					ImageUtils.thumbnail(multipartFile.getInputStream(), 100, 100, new FileOutputStream(savePath + newName));
					Msg msg = this.userService.updatePhoto(path + newName);
					msg.setObj(path + newName + "?t=" + System.currentTimeMillis());
					return msg;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Msg(false, "照片上传异常");
	}

	/**
	 * 修改密码
	 */
	@RequestMapping("/updatepwd.do")
	@ResponseBody
	public Msg updatepwd(UserForm form) {
		return this.userService.updatePwd(form);
	}

	/**
	 * 修改角色
	 */
	@RequestMapping("/updateRole.do")
	@ResponseBody
	public Msg batchRole(UserForm form) {
		return this.userService.batchUserRole(form);
	}

	/**
	 * 添加角色用户
	 */
	/*@RequestMapping("/adduserrole.do")
	@ResponseBody
	public Msg no_adduserrole(String roleId, String userIds) throws Exception {
		return this.userService.addRoleForUser(roleId, userIds);
	}*/

	/**
	 * 删除角色用户
	 */
	/*@RequestMapping("/deleteuserrole.do")
	@ResponseBody
	public Msg deleteuserrole(String roleId, String userId) throws Exception {
		return this.userService.deleteRoleForUser(roleId, userId);
	}*/
	
	/**
	 * 查询用户个人信息
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping("/datagridpersonal.do")
	@ResponseBody
	public DataGrid datagridpersonal() throws Exception {
		return this.userService.datagridpersonal();
	}*/
} 
