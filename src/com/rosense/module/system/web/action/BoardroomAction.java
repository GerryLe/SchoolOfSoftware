package com.rosense.module.system.web.action;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IBoardroomService;
import com.rosense.module.system.web.form.BoardroomForm;
import com.rosense.module.system.web.form.LoginSession;
import com.rosense.module.system.web.form.LoginUser;

@Controller
@RequestMapping("/admin/system/boardroom")
public class BoardroomAction extends BaseController {

	@Inject
	private IBoardroomService boardroomService;
	
	/*
	 * 添加申请信息
	 * */
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg addApply(BoardroomForm form) throws Exception{
		return this.boardroomService.addApply(form);
	}
	
	
	/*
	 * 更新申请信息
	 * */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(BoardroomForm form) throws Exception{
		return this.boardroomService.update(form);
	}
	
	
	/*
	 * 删除申请信息
	 * 
	 * */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(BoardroomForm form) throws Exception{
		return this.boardroomService.delete(form);
	}
	
	/*
	 * 获取单个信息
	 * 
	 * */
	@RequestMapping("/get.do")
	@ResponseBody
	public BoardroomForm get(BoardroomForm form) throws Exception {
		return this.boardroomService.get(form.getId());
	}
	
	/**
	 * 获取全部申请信息
	 * 
	 * */
	@RequestMapping("/select.do")
	@ResponseBody
	public DataGrid select (BoardroomForm form) throws Exception {
		return this.boardroomService.select(form);
   }
	
	/**
	 * 获取全部申请信息
	 * 
	 * */
	@RequestMapping("/selectId.do")
	@ResponseBody
	public  List<BoardroomForm> selectId (HttpSession session) throws Exception {
		LoginSession ls = (LoginSession) session.getAttribute(Const.USER_SESSION);
		return this.boardroomService.selectId(ls.getUser());
	}
	
	@RequestMapping("/JSONString.do")
	public @ResponseBody Object JSONString(){
		//返回的是拼装的JSON字符串，需将字符串转换为JSON对象
		return JSON.parse(boardroomService.JSONString());
	}
}
