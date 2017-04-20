package com.rosense.module.system.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.IpUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.device.BrowserUtils;
import com.rosense.basic.util.device.UserAgent;
import com.rosense.basic.util.device.UserAgentUtil;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.common.web.servlet.ValidCodeServlet;
import com.rosense.module.system.service.ILogService;
import com.rosense.module.system.service.IStudentService;
import com.rosense.module.system.service.ITeacherService;
import com.rosense.module.system.web.form.AuthForm;
import com.rosense.module.system.web.form.LoginLogForm;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.UserForm;

/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月19日 上午8:32:57
 *
 */
@Controller
@RequestMapping("/system/login")
public class LoginAction extends BaseController {
	@Inject
	private IStudentService stuService;
	@Inject
	private ITeacherService teaService;
	@Inject
	private ILogService logService;

	/**
	 * 学生或者管理员登录
	 */
	@RequestMapping("/login.do")
	@ResponseBody
	public Msg login(LoginUser form, HttpServletRequest request, HttpSession session) throws Exception {
		Msg msg = new Msg();
		Map<String, Object> maps = new HashMap<String, Object>();

		if (null == form || form.getAccount() == null || "".equals(form.getAccount().trim())) {
			return new Msg(false, "账号不能为空！");
		}
		Integer count = (Integer) session.getAttribute("login_error");
		if (null == count)
			count = 0;

		LoginUser user = this.stuService.loginCheck(form);
		if (null != user) {
			if (user.getStatus() == 1) {
				msg.setStatus(false);
				msg.setMsg("你的账号已被锁定，无法登陆系统，解锁请联系管理员！");
				msg.setParam1("1");
				return msg;
			}

			String ipAddr = IpUtil.getIpAddr(request);

			AuthForm auth = this.stuService.getAuth(user.getUserId());
			LoginSession loginSession = new LoginSession();
			user.setIp(ipAddr);
			loginSession.setUser(user);
			loginSession.setAuth(auth);
			session.setAttribute(Const.USER_SESSION, loginSession);

			//保存用户的设备信息
			UserAgent userAgent = UserAgentUtil.getUserAgent(BrowserUtils.getAgent(request));
			if (userAgent == null) {
				userAgent = new UserAgent();
			}
			LoginLogForm lf = new LoginLogForm();
			lf.setUserId(user.getUserId());
			lf.setIp(ipAddr);
			lf.setBrowserType(userAgent.getBrowserType());
			lf.setBrowserVersion(userAgent.getBrowserVersion());
			lf.setPlatformType(userAgent.getPlatformType());
			lf.setDetail(BrowserUtils.getAgent(request));
			this.logService.addLL(lf);

			session.removeAttribute(ValidCodeServlet.VALIDATE_CODE);
			session.removeAttribute("login_error");

			maps.put("photo", user.getPhoto());
			msg.setObj(maps);
			msg.setStatus(true);
			msg.setMsg("验证成功，正在访问...");
			return msg;
		} else {
			count = count + 1;
			session.setAttribute("login_error", count);

			if (count >= 3)
				maps.put("v", true);
			msg.setStatus(false);
			msg.setMsg("登陆失败！请检查账号密码");
			msg.setObj(maps);
			return msg;
		}
	}
	
	
	/**
	 * 教师登录
	 */
	@RequestMapping("/loginTeacher.do")
	@ResponseBody
	public Msg loginTeacher(LoginUser form, HttpServletRequest request, HttpSession session) throws Exception {
		Msg msg = new Msg();
		Map<String, Object> maps = new HashMap<String, Object>();
		if (null == form || form.getAccount() == null || "".equals(form.getAccount().trim())) {
			return new Msg(false, "账号不能为空！");
		}
		Integer count = (Integer) session.getAttribute("login_error");
		if (null == count)
			count = 0;

		LoginUser user = this.teaService.loginCheck(form);
		if (null != user) {
			if (user.getStatus() == 1) {
				msg.setStatus(false);
				msg.setMsg("你的账号已被锁定，无法登陆系统，解锁请联系管理员！");
				msg.setParam1("1");
				return msg;
			}

			String ipAddr = IpUtil.getIpAddr(request);

			AuthForm auth = this.stuService.getAuth(user.getUserId());
			LoginSession loginSession = new LoginSession();
			user.setIp(ipAddr);
			loginSession.setUser(user);
			loginSession.setAuth(auth);
			session.setAttribute(Const.USER_SESSION, loginSession);

			//保存用户的设备信息
			UserAgent userAgent = UserAgentUtil.getUserAgent(BrowserUtils.getAgent(request));
			if (userAgent == null) {
				userAgent = new UserAgent();
			}
			LoginLogForm lf = new LoginLogForm();
			lf.setUserId(user.getUserId());
			lf.setIp(ipAddr);
			lf.setBrowserType(userAgent.getBrowserType());
			lf.setBrowserVersion(userAgent.getBrowserVersion());
			lf.setPlatformType(userAgent.getPlatformType());
			lf.setDetail(BrowserUtils.getAgent(request));
			this.logService.addLL(lf);

			session.removeAttribute(ValidCodeServlet.VALIDATE_CODE);
			session.removeAttribute("login_error");

			maps.put("photo", user.getPhoto());
			msg.setObj(maps);
			msg.setStatus(true);
			msg.setMsg("验证成功，正在访问...");
			return msg;
		} else {
			count = count + 1;
			session.setAttribute("login_error", count);

			if (count >= 3)
				maps.put("v", true);
			msg.setStatus(false);
			msg.setMsg("登陆失败！请检查账号密码");
			msg.setObj(maps);
			return msg;
		}
	}

	/**
	 * 获取登录后的可以访问的菜单
	 * @param session
	 * @return
	 */
	@RequestMapping("/getCurrentAuthMenu.do")
	public @ResponseBody
	Object getCurrentAuthMenu(HttpSession session) {
		LoginSession ls = (LoginSession) session.getAttribute(Const.USER_SESSION);
		if (null != ls) {
			LoginUser currentUser=ls.getUser();
			String name=currentUser.getName();
			if(name.equals("Myolie")||name.equals("Cathy")||name.equals("Emily")||name.equals("Rachel")||name.equals("admin")){
			messageUser();
			}
			AuthForm auth = ls.getAuth();
			//返回的是拼装的JSON字符串，需将字符串转换为JSON对象
			return JSON.parse("[" + auth.getAuthTree() + "]");
		} else {
			return "";
		}
	}

	@RequestMapping("/getUserCurrentAuthMenu.do")
	public @ResponseBody
	Object getUserCurrentAuthMenu(HttpSession session) {
		LoginSession ls = (LoginSession) session.getAttribute(Const.USER_SESSION);
		if (null != ls) {
			LoginUser currentUser=ls.getUser();
			String name=currentUser.getName();
			if(name.equals("Myolie")||name.equals("Cathy")||name.equals("Emily")||name.equals("Rachel")||name.equals("admin")){
				//每隔一个小时提醒一次
				 Timer timer = new Timer();
				  timer.schedule(new TimerTask() {
				          public void run() {
				        	  messageUser();
				          }
				  }, new Date() , 1000*60*60);
			}
			AuthForm auth = ls.getAuth();
			//返回的是拼装的JSON字符串，需将字符串转换为JSON对象
			return JSON.parse(auth.getAuthTree());
		} else {
			return "";
		}
	}
	
	/**
	 * 登录错误信息,防止恶意登录
	 */
	@RequestMapping("/login_error.do")
	@ResponseBody
	public Msg login_error(HttpServletRequest request, HttpSession session) throws Exception {
		Integer count = (Integer) session.getAttribute("login_error");
		if (null == count)
			count = 0;
		if (count >= 3) {
			return new Msg(true);
		} else {
			return new Msg(false);
		}
	}

	/**
	 * 注销
	 */
	@RequestMapping("/logout.do")
	@ResponseBody
	public Msg logout(HttpServletRequest request, HttpSession session) throws Exception {
		session.invalidate();
		return new Msg(true);
	}
	
	private void messageUser() {
		// TODO Auto-generated method stub
		try {
		String username="";
		String userdate="";
		List<UserForm> list=this.stuService.searchUsersData();
		if(null != list && list.size() > 0){
		for(UserForm user : list){
			username+=user.getName()+",";
			userdate=user.getBecomeStaffDate();
		}
		
			username.substring(0, username.length()-1);
			JOptionPane.showMessageDialog(null,"用户"+username+"合同期为"+userdate, "系统信息", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
