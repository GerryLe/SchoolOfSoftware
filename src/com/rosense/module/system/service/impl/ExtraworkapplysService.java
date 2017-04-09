package com.rosense.module.system.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.module.common.service.BaseService;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.ExtraworkapplysEntity;
import com.rosense.module.system.entity.HolidaysUsersEntity;
import com.rosense.module.system.entity.PersonEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IExtraworkapplysService;
import com.rosense.module.system.web.form.ExtraworkapplysForm;
import com.rosense.module.system.web.form.HolidaysUserForm;

import net.sf.json.JSONObject;

@Service("extraworkapplysService")
@Transactional
public class ExtraworkapplysService extends BaseService implements IExtraworkapplysService {
	private Logger logger = Logger.getLogger(RoleService.class);
	@Inject
	private IBaseDao<ExtraworkapplysEntity> eDao;
	@Inject
	private IBaseDao<RoleEntity> rDao;
	@Inject
	private IBaseDao<PersonEntity> pDao;
	@Inject
	private IBaseDao<UserEntity> uDao;
	@Inject
	private IBaseDao<HolidaysUsersEntity> huDao;


	@Override
	public Msg add(ExtraworkapplysForm form) {
		// TODO Auto-generated method stub
		try {
			final ExtraworkapplysEntity p = new ExtraworkapplysEntity();
			form.setExtworkapplyname(WebContextUtil.getCurrentUser().getUser().getName());
			form.setUserid(WebContextUtil.getCurrentUser().getUser().getUserId());
			BeanUtils.copyNotNullProperties(form, p);
			this.eDao.add(p);
			this.logService.add("申请加班", "账号：[" + form.getExtworkapplyname() + "]");
			this.logService.add("申请假期", "账号：[" + form.getExtworkapplyname() + "]");
			return new Msg(true, "申请成功！");
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("申请加班失败===>异常信息：", e);
			return new Msg(false, "申请失败！");
		}
	}

	@Override
	public Msg delete(ExtraworkapplysForm form) {
		// TODO Auto-generated method stub
try {
			
			String[] ids = form.getIds().split(",");
			for (String id : ids) {
				ExtraworkapplysEntity h = this.eDao.load(ExtraworkapplysEntity.class, id);
				if ("admin".equals(WebContextUtil.getCurrentUser().getUser().getAccount())) {
					h.setExtworkapplystatement(3);
					this.eDao.update(h);
					this.logService.add("撤销加班申请", "账号：[" + form.getExtworkapplyname() + "]");
					return new Msg(true, "撤销成功！");
				}
				if(!form.getExtworkapplyname().equals(WebContextUtil.getCurrentUser().getUser().getName())){
					return new Msg(true,"无权撤销!");
				}
				if(form.getExtworkapplystatement()==0){
					h.setExtworkapplystatement(3);
					this.eDao.update(h);
					this.logService.add("撤销加班申请", "账号：[" + form.getExtworkapplyname() + "]");
					return new Msg(true, "撤销成功！");
				}else{
					this.logService.add("撤销加班申请", "账号：[" + form.getExtworkapplyname() + "]");
					return new Msg(true, "已审批，不能撤销！");
				}
			}
			return new Msg(true, "撤销成功！");
		} catch (Exception e) {
			logger.error("撤销加班申请失败===>异常信息：", e);
			return new Msg(false, "撤销失败！");
		}
	}

	@Override
	public Msg update(ExtraworkapplysForm form) {
		// TODO Auto-generated method stub
		try {
			ExtraworkapplysEntity extraworkapplys =this.eDao.load(ExtraworkapplysEntity.class,form.getId());
			BeanUtils.copyNotNullProperties(form, extraworkapplys);
			this.eDao.update(extraworkapplys);
			this.logService.add("修改加班","名称：[" + extraworkapplys.getExtworkapplyname() + "]");
			return new Msg(true,"修改成功！");
	} catch (Exception e) {
		logger.error("修改加班信息失败===>异常信息：", e);
		return new Msg(false, "修改假期信息失败！");
	}
	}

	@Override
	public DataGrid select(ExtraworkapplysForm form) {
		// TODO Auto-generated method stub
		try {
			List<ExtraworkapplysForm> forms = new ArrayList<ExtraworkapplysForm>();
			Map<String, Object> alias = new HashMap<String, Object>();
			String name = WebContextUtil.getCurrentUser().getUser().getAccount();
			RoleEntity re=this.rDao.load(RoleEntity.class,WebContextUtil.getCurrentUser().getUser().getRole_ids());
			String sql = "select po.name positionname,e.* from extraworkapplys e ";
			sql += " left join simple_user u ON(e.userid=u.id) ";
			sql += " left join simple_person p ON(u.personId=p.id) ";
			sql += " left join simple_position po ON(p.positionId=po.id) ";
			if ("admin".equals(name)) {
				sql+="where 1=1";
			}else if(re.getDefaultRole().equals(3)){
				sql+="left join simple_user_roles ur on(ur.userId=u.id) left join simple_role r on(ur.roleId=r.id) where e.extworkdirectorsapproval not in(0) and r.defaultRole!='4' ";
				//sql+=" where e.extworkdirectorsapproval not in(0) ";
			}else if(re.getDefaultRole().equals(2)){
				sql+="where u.account='" + name + "' or po.pid=po.id";
			}else if(re.getDefaultRole().equals(5)){
				sql += "left join simple_user_roles ur on(ur.userId=u.id) left join simple_role r on(ur.roleId=r.id) where r.defaultRole='4' ";
			} else{
				sql += "where u.account='" + name + "'";
			}
			sql = addWhere(sql, form, alias);
			Pager<ExtraworkapplysForm> pager = this.eDao.findSQL(sql, alias, ExtraworkapplysForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (ExtraworkapplysForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载加班列表信息失败===>异常信息：", e);
			throw new ServiceException("加载加班列表信息异常：", e);
		}
	}

	@Override
	public ExtraworkapplysForm get(String id) {
		// TODO Auto-generated method stub
		try {
			String sql = "select e.* from extraworkapplys e ";

			sql += " where e.id=?";

			ExtraworkapplysForm form = (ExtraworkapplysForm) this.eDao.queryObjectSQL(sql, new Object[] { id }, ExtraworkapplysForm.class, false);
			return form;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载人员信息失败===>异常信息：", e);
			throw new ServiceException("加载用户信息异常：", e);
		}
	}
	
	private String addWhere(String sql, ExtraworkapplysForm form, Map<String, Object> params){
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if(key.equals("extworkapplyname")){
					form.setExtworkapplyname(jsonObject.get(key).toString());
				}
			}
		}
		if (StringUtil.isNotEmpty(form.getExtworkapplyname())) {
			try {
				params.put("extworkapplyname", "%%" + URLDecoder.decode(form.getExtworkapplyname(), "UTF-8") + "%%");
				sql += " and extworkapplyname like :extworkapplyname";
			} catch (Exception e) {
			}
		}
		return sql;
	}

	@Override
	public Msg approvaltrue(String id) {
		try {
			RoleEntity r=this.rDao.load(RoleEntity.class,WebContextUtil.getCurrentUser().getUser().getRole_ids() );
			if (StringUtil.isNotEmpty(id)) {
				ExtraworkapplysEntity h = this.eDao.load(ExtraworkapplysEntity.class, id);
				UserEntity u=this.uDao.load(UserEntity.class,h.getUserid());
				PersonEntity p=this.pDao.load(PersonEntity.class,u.getPersonId());
				HolidaysUsersEntity hu=this.huDao.load(HolidaysUsersEntity.class,u.getHolidaysId());
				if ("admin".equals(WebContextUtil.getCurrentUser().getUser().getAccount())) {
					h.setExtworkdirectorsapproval(1);
					h.setExtworkhrapproval(1);
					this.logService.add("加班申请审批", "账号：[" + WebContextUtil.getCurrentUser().getUser().getAccount() + "]");
					if(h.getExtworkdirectorsapproval()==1&&h.getExtworkhrapproval()==1){
						h.setExtworkapplystatement(1);
					}
					this.eDao.update(h);
					return new Msg(true, "加班申请已审批");
				}
				// 根据角色权限审批
				if (r.getDefaultRole()==2) {
					h.setExtworkdirectorsapproval(1);
					this.logService.add("加班申请审批", "账号：[" + WebContextUtil.getCurrentUser().getUser().getAccount() + "]");
					if(h.getExtworkdirectorsapproval()==1&&h.getExtworkhrapproval()==1){
						h.setExtworkapplystatement(1);
					}
					this.eDao.update(h);
					sendMail("审核通知", p.getName()+"  "+p.getChinaname()+"的加班申请已通过该部门主管审批！","disonyan@can-dao.com"//p.getEmail()
							, "disonyan@can-dao.com");
					return new Msg(true, "加班申请已审批");
				}
				if (r.getDefaultRole()==3) {
					h.setExtworkhrapproval(1);
					this.logService.add("加班申请审批", "账号：[" + WebContextUtil.getCurrentUser().getUser().getAccount() + "]");
					if(h.getExtworkdirectorsapproval()==1&&h.getExtworkhrapproval()==1){
						h.setExtworkapplystatement(1);
					}
					this.eDao.update(h);
					sendMail("审核通知",  p.getName()+"  "+p.getChinaname()+"的加班申请已通过HR审批！","disonyan@can-dao.com"//p.getEmail()
							,"");
					
					//更新假期列表
					HolidaysUserForm huf=new HolidaysUserForm();
					huf.setThisyearsdaysworkovertime(hu.getThisyearsdaysworkovertime()-Double.parseDouble(h.getExtworkapplydays()));
					huf.setTheremainingpaidleave(hu.getTheremainingpaidleave()+Double.parseDouble(h.getExtworkapplydays()));
					BeanUtils.copyNotNullProperties(huf, hu);
					this.huDao.update(hu);
					
					return new Msg(true, "加班申请已审批");
				}else{
					return new Msg(true, "您没有审批权限!");
				}
			}
		} catch (ServiceException e) {
			return new Msg(false, "程序发送错误！");
		}

		return new Msg(true, "出现错误！");
	}

	@Override
	public Msg approvalfalse(String id) {
		// TODO Auto-generated method stub
		try {
			RoleEntity r=this.rDao.load(RoleEntity.class,WebContextUtil.getCurrentUser().getUser().getRole_ids() );
			if (StringUtil.isNotEmpty(id)) {
				ExtraworkapplysEntity h = this.eDao.load(ExtraworkapplysEntity.class, id);
				UserEntity u=this.uDao.load(UserEntity.class,h.getUserid());
				PersonEntity p=this.pDao.load(PersonEntity.class,u.getPersonId());
				if ("admin".equals(WebContextUtil.getCurrentUser().getUser().getAccount())) {
					h.setExtworkdirectorsapproval(2);
					h.setExtworkhrapproval(2);
					this.logService.add("加班申请审批", "账号：[" + WebContextUtil.getCurrentUser().getUser().getAccount() + "]");
					if(h.getExtworkdirectorsapproval()==2&&h.getExtworkhrapproval()==2){
						h.setExtworkapplystatement(2);
					}
					this.eDao.update(h);
					return new Msg(true, "加班申请已审批");
				}
				// 根据角色权限审批
				if (r.getDefaultRole()==2) {
					h.setExtworkdirectorsapproval(2);
					this.logService.add("加班申请审批", "账号：[" + WebContextUtil.getCurrentUser().getUser().getAccount() + "]");
					if(h.getExtworkdirectorsapproval()==2&&h.getExtworkhrapproval()==2){
						h.setExtworkapplystatement(2);
					}
					this.eDao.update(h);
					sendMail("审核通知", p.getName()+"  "+p.getChinaname()+"的加班申请未能通过该部门主管审批！","disonyan@can-dao.com"//p.getEmail()
							, "disonyan@can-dao.com");
					return new Msg(true, "加班申请已审批");
				}
				if (r.getDefaultRole()==3) {
					h.setExtworkhrapproval(2);
					this.logService.add("加班申请审批", "账号：[" + WebContextUtil.getCurrentUser().getUser().getAccount() + "]");
					if(h.getExtworkdirectorsapproval()==2&&h.getExtworkhrapproval()==2){
						h.setExtworkapplystatement(2);
					}
					this.eDao.update(h);
					sendMail("审核通知",  p.getName()+"  "+p.getChinaname()+"的加班申请未能通过HR审批！","disonyan@can-dao.com"//p.getEmail()
							,"");
					
					return new Msg(true, "加班申请已审批");
				}else{
					return new Msg(true, "您没有审批权限!");
				}
			}
		} catch (ServiceException e) {
			return new Msg(false, "程序发送错误！");
		}

		return new Msg(true, "出现错误！");
	}
	
	public static void sendMail(String title, String content, String to, String to2) {
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		// 设定mail server
		senderImpl.setHost("smtp.mxhichina.com");
		senderImpl.setPort(25);
		senderImpl.setProtocol("smtp");
		// 建立邮件消息
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		// 设置收件人，寄件人 用数组发送多个邮件
		String[] array = new String[] { to, to2 };
		mailMessage.setTo(array);
		// mailMessage.setTo(to);
		mailMessage.setFrom("disonyan@can-dao.com");
		mailMessage.setSubject(title);
		mailMessage.setText(content);

		senderImpl.setUsername("disonyan@can-dao.com");
		senderImpl.setPassword("Yanzihao.");

		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
		prop.put("mail.smtp.timeout", "25000");
		senderImpl.setJavaMailProperties(prop);
		prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.setProperty("mail.smtp.port", "465");
		prop.setProperty("mail.smtp.socketFactory.port", "465");
		// 发送邮件
		senderImpl.send(mailMessage);

	}

}
