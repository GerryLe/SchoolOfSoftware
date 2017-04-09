package com.rosense.module.system.web.action;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.Msg;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.common.web.servlet.AppConst;
import com.rosense.module.system.web.form.LoginSession;

@Controller
@RequestMapping("/admin/system/online")
public class OnlineUserAction extends BaseController {
	private Logger logger = Logger.getLogger(OnlineUserAction.class);

	@RequestMapping("/datagrid.do")
	@ResponseBody
	public Map<String, Object> datagrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> onlineUsers = new ArrayList<Map<String, Object>>();

		for (String id : AppConst.SESSION_MAP.keySet()) {
			HttpSession sess = AppConst.SESSION_MAP.get(id);
			LoginSession loginSession = (LoginSession) sess.getAttribute(Const.USER_SESSION);
			if (null != loginSession) {
				Map<String, Object> userMap = new HashMap<String, Object>();
				userMap.put("sessionId", id);
				userMap.put("loginTime", DateFormat.getTimeInstance().format(sess.getCreationTime()));
				userMap.put("userId", loginSession.getUser().getUserId());
				userMap.put("account", loginSession.getUser().getAccount());
				userMap.put("status", loginSession.getUser().getStatus());
				userMap.put("personId", loginSession.getUser().getPersonId());
				userMap.put("name", loginSession.getUser().getName());
				userMap.put("orgId", loginSession.getUser().getOrgId());
				userMap.put("orgName", loginSession.getUser().getOrgName());
				userMap.put("ip", loginSession.getUser().getIp());
				onlineUsers.add(userMap);
			}
		}
		map.put("rows", onlineUsers);
		map.put("curent_login_count", AppConst.CURRENT_LOGIN_COUNT);
		map.put("max_online_count", AppConst.MAX_ONLINE_COUNT);
		map.put("total_history_count", AppConst.TOTAL_HISTORY_COUNT);
		map.put("datetime", DateUtils.formatYYYYMMDD_HHMMSS(AppConst.MAX_ONLINE_COUNT_DATE));
		map.put("serverTime", DateUtils.formatYYYYMMDD_HHMMSS(AppConst.START_DATE));
		map.put("total", onlineUsers.size());
		return map;
	}

	@RequestMapping("/doNotNeedAuth_forceLogout.do")
	@ResponseBody
	public Msg doNotNeedAuth_forceLogout(String sessionIds, HttpSession session) {
		try {
			if (null == sessionIds || "".equals(sessionIds)) {
				for (String id : AppConst.SESSION_MAP.keySet()) {
					//当前用户排除在外
					if (!id.equals(session.getId())) {
						AppConst.SESSION_MAP.get(id).removeAttribute(Const.USER_SESSION);
						AppConst.SESSION_MAP.remove(id);
						AppConst.CURRENT_LOGIN_COUNT--;
						logger.info("全部强制注销用户#SessionId：" + id);
					}
				}
			} else {
				String[] split = sessionIds.split(",");
				for (int i = 0; i < split.length; i++) {
					//当前用户排除在外
					if (!split[i].equals(session.getId())) {
						if (AppConst.SESSION_MAP.keySet().contains(split[i])) {
							AppConst.SESSION_MAP.get(split[i]).removeAttribute(Const.USER_SESSION);
							AppConst.CURRENT_LOGIN_COUNT--;
							logger.info("强制注销用户#SessionId：" + split[i]);
						}
					}
				}
			}
		} catch (Exception e) {
			return new Msg(false, "强制用户退出发出错误！");
		}
		return new Msg(true);
	}

}
