package com.rosense.module.system.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.BoardroomForm;
import com.rosense.module.system.web.form.LoginUser;

public interface IBoardroomService {

	/**
	 * 添加申请信息
	 */
	 public Msg addApply(BoardroomForm form);
	 
   /**
	 * 删除申信息
	 */
	public Msg delete(BoardroomForm form);

	/**
	 * 修改申请信息
	 */
	public Msg update(BoardroomForm form);

	/**
	 * 获取一个申请信息
	 */
	public BoardroomForm get(String id);
	
	/**
	 * 获取所有申请信息
	 */
	public DataGrid select(BoardroomForm form);
	
	/*
	 * 获取当前用户申请会议室的Id
	 * 
	 * */
	public  List<BoardroomForm> selectId(LoginUser currentUser);
	
	
	public String JSONString();
}
