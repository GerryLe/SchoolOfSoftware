package com.rosense.module.system.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.kahadb.page.Page;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.MD5Util;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.basic.util.date.DateUtils;
import com.rosense.module.cache.Caches;
import com.rosense.module.system.entity.BoardroomEntity;
import com.rosense.module.system.entity.HolidaysUsersEntity;
import com.rosense.module.system.entity.OrgEntity;
import com.rosense.module.system.entity.PersonEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IBoardroomService;
import com.rosense.module.system.web.form.BoardroomForm;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.LoginUser;
import com.rosense.module.system.web.form.OrgForm;

import net.sf.json.JSONObject;

/*
 * 会议室申请操作
 */

@Service("boardroomService")
@Transactional
public class BoardroomService implements IBoardroomService {
	private Logger logger = Logger.getLogger(BoardroomService.class);
	@Inject
	private IBaseDao<BoardroomEntity> boardroomDao;
	@Inject
	private IBaseDao<OrgEntity> basedaoOrg;

	@Override
	public Msg addApply(BoardroomForm form) {
		// TODO Auto-generated method stub
		try {
			form.setStatus(0);
			final BoardroomEntity p = new BoardroomEntity();
			BeanUtils.copyNotNullProperties(form, p);
			this.boardroomDao.add(p);
			return new Msg(true, "添加成功！");
		} catch (Throwable e) {
			e.printStackTrace();
			return new Msg(false, "添加失败！");
		}
	}

	@Override
	public Msg delete(BoardroomForm form) {
		// TODO Auto-generated method stub
		if (null != form.getId() && !"".equals(form.getId())) {
			this.boardroomDao.delete(BoardroomEntity.class, form.getId());
			return new Msg(true, "删除成功!");
		}
		return new Msg(false, "删除失败!");
	}

	@Override
	public Msg update(BoardroomForm form) {
		try {
			BoardroomEntity b = this.boardroomDao.load(BoardroomEntity.class, form.getId());
			BeanUtils.copyNotNullProperties(form, b);
			this.boardroomDao.update(b);
			return new Msg(true, "更新成功");
		} catch (Exception e) {
			return new Msg(false, "更新失败");
		}
	}

	@Override
	public BoardroomForm get(String id) {
		try {
			String sql = "select * from simple_boardroom b where b.id=?";
			BoardroomForm bform = (BoardroomForm) this.boardroomDao.queryObjectSQL(sql, new Object[] { id },
					BoardroomForm.class, false);
			return bform;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载信息失败", e);
			return null;
		}
	}

	@Override
	public DataGrid select(BoardroomForm form) {
		try {
			/*if (null == form.getApplyRoom()) {
				SystemContext.setSort("b.applyRoom");
				SystemContext.setOrder("asc");
			}
			if (null == form.getApplyDate()) {
				SystemContext.setSort("b.applyDate");
				SystemContext.setOrder("desc");
			}*/
			Map<String, Object> alias = new HashMap<String, Object>();
			List<BoardroomForm> boardforms = new ArrayList<BoardroomForm>();
			String sql = "select b.*,o.name orgName from (select * from simple_boardroom order by applyDate asc,applyTime asc ) b ";
			//String sql = "select b.*,o.name orgName from simple_boardroom b ";
			sql += "left join simple_org o ON(b.applyOrg=o.id)  ";
			//sql += "where b.status=0  ";
			sql+="where unix_timestamp(CONCAT(b.applyDate,' ',b.applyTime,':00')) >= unix_timestamp(CONCAT(CURDATE(),' ',CURTIME()))  order by  b.applyRoom desc, b.applyRoom asc, b.applyDate desc ,b.applyTime asc";
			sql = addWhere(sql, form, alias);
			Pager<BoardroomForm> pager = this.boardroomDao.findSQL(sql, alias, BoardroomForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (BoardroomForm pf : pager.getDataRows()) {
					boardforms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(boardforms);
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载信息异常", e);
			throw new ServiceException("加载信息异常");
		}
	}

	private String addWhere(String sql, BoardroomForm form, Map<String, Object> alias) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject json = JSONObject.fromObject(form.getFilter());
			for (Object key : json.keySet()) {
				if (key.equals("applyDate")) {
					form.setApplyDate(json.get(key).toString());
				}
				if (key.equals("applyTime")) {
					form.setApplyTime(json.get(key).toString());
				}
				if (key.equals("applyName")) {
					form.setApplyName(json.get(key).toString());
				}
			}
		}
		if (form.getApplyDate() != null) {
			sql += " and b.applyDate=:applyDate";
			alias.put("applyDate", form.getApplyDate());
		}
		if (form.getApplyTime() != null) {
			sql += " and b.applyTime=:applyTime";
			alias.put("applyTime", form.getApplyTime());
		}
		if (form.getApplyName() != null) {
			sql += " and b.applyName=:applyName";
			alias.put("applyName", form.getApplyName());
		}

		return sql;
	}

	public List<BoardroomForm> selectId(LoginUser currentUser) {
		String sql = "select * from simple_boardroom where applyName='" + currentUser.getName() + "'";
		List<BoardroomForm> forms = this.boardroomDao.listSQL(sql, BoardroomForm.class, false);
		if (forms != null && forms.size() > 0) {
			return forms;
		}
		return null;
	}

	public String JSONString() {
		String result = "";
		String A = "{ image: '/template/resource/images/meet1.jpg' , heading: 'A区会议室' , description: '当前申请情况： null'},";
		String B = "{ image: '/template/resource/images/meet2.jpg' , heading: 'B区会议室' , description: '当前申请情况：null'},";
		String C = "{ image: '/template/resource/images/meet1.jpg' , heading: 'C区会议室' , description: '当前申请情况：null'},";
		String D = "{ image: '/template/resource/images/meet3.jpg' , heading: 'D区会议室' , description: '当前申请情况：null'},";
		String E = "{ image: '/template/resource/images/meet1.jpg' , heading: 'E区会议室' , description: '当前申请情况：null'},";
		String F = "{ image: '/template/resource/images/meet2.jpg' , heading: 'F区会议室' , description: '当前申请情况：null'},";
		String sql = "select b.* from (select * from simple_boardroom order by applyDate asc,applyTime asc ) b where unix_timestamp(CONCAT(b.applyDate,' ',b.applyTime,':00')) >= unix_timestamp(CONCAT(CURDATE(),' ',CURTIME())) GROUP BY applyRoom order by applyDate asc ,applyTime asc";
		List<BoardroomForm> forms = boardroomDao.listSQL(sql, BoardroomForm.class, false);
		result += "[";
		if (forms != null && forms.size() > 0) {
			for (BoardroomForm form : forms) {
				List<OrgEntity> list = this.basedaoOrg.list("from OrgEntity where id=?", form.getApplyOrg());
				OrgForm orgForm = new OrgForm();
				if (list != null && list.size() > 0) {
					BeanUtils.copyNotNullProperties(list.get(0), orgForm);
				}
				if (form.getApplyRoom().equals("A区会议室")) {
					A = "{ image: '/template/resource/images/meet1.jpg' , heading: '" + form.getApplyRoom()
							+ "', description: '当前申请情况：" + orgForm.getName() + ", " + form.getNumber() + "人, "
							+ form.getApplyDate() + "  " + form.getApplyTime() + "'},";
				}
				if (form.getApplyRoom().equals("B区会议室")) {
					B = "{ image: '/template/resource/images/meet2.jpg' , heading: '" + form.getApplyRoom()
							+ "', description: '当前申请情况：" + orgForm.getName() + ", " + form.getNumber() + "人, "
							+ form.getApplyDate() + "  " + form.getApplyTime() + "'},";
				}
				if (form.getApplyRoom().equals("C区会议室")) {
					C = "{ image: '/template/resource/images/meet1.jpg' , heading: '" + form.getApplyRoom()
							+ "', description: '当前申请情况：" + orgForm.getName() + ", " + form.getNumber() + "人, "
							+ form.getApplyDate() + "  " + form.getApplyTime() + "'},";
				}
				if (form.getApplyRoom().equals("D区会议室")) {
					D = "{ image: '/template/resource/images/meet3.jpg' , heading: '" + form.getApplyRoom()
							+ "', description: '当前申请情况：" + orgForm.getName() + ", " + form.getNumber() + "人, "
							+ form.getApplyDate() + "  " + form.getApplyTime() + "'},";
				}
				if (form.getApplyRoom().equals("E区会议室")) {
					E = "{ image: '/template/resource/images/meet1.jpg' , heading: '" + form.getApplyRoom()
							+ "', description: '当前申请情况：" + orgForm.getName() + ", " + form.getNumber() + "人, "
							+ form.getApplyDate() + "  " + form.getApplyTime() + "'},";
				}
				if (form.getApplyRoom().equals("F区会议室")) {
					F = "{ image: '/template/resource/images/meet2.jpg' , heading: '" + form.getApplyRoom()
							+ "', description: '当前申请情况：" + orgForm.getName() + ", " + form.getNumber() + "人, "
							+ form.getApplyDate() + "  " + form.getApplyTime() + "'},";
				}
			}

		} else {
		}
		result += A + B + C + D + E + F;
		result = result.substring(0, result.length() - 1);
		result += "]";
		return result;
	}

}