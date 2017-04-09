package com.rosense.module.system.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.entity.StationeriesEntity;
import com.rosense.module.system.entity.StationeryEntity;
import com.rosense.module.system.web.form.OrgForm;
import com.rosense.module.system.web.form.StationeryForm;

public interface IStationeriesService {
	/**
	 * 添加一条文具申请记录
	 * @param form
	 * @return
	 */
	Msg addstationery(StationeryForm form);
	/**
	 * 拆除一条文具申请记录
	 * @param form
	 * @return
	 */
	Msg deletestationery(StationeryForm form);
	/**
	 * 修改一条文具申请记录
	 * @param form
	 * @return
	 */
	Msg updatestationery(StationeryForm form);
	/**
	 * 查看文具列表
	 * @param form
	 * @return
	 */
	DataGrid datagridstationeries(StationeryForm form);
	/**
	 * 新增种文具
	 * @param form
	 * @return
	 */
	Msg addstationeries(StationeryForm form);
	/**
	 * 根据id获取文具种类
	 * @param 
	 * @return
	 */
	StationeriesEntity getstationeries(StationeryForm form);
	/**
	 * 根据id获取文具申请记录
	 * @param 
	 * @return
	 */
	StationeryEntity getstationery(StationeryForm form);
	/**
	 * 删除文具种类
	 * @param form
	 * @return
	 */
	Msg deletestationeries(StationeryForm form);
	/**
	 * 更新文具种类
	 * @param form
	 * @return
	 */
	Msg updatestationeries(StationeryForm form);
	/**
	 * 用户获取申请文具信息
	 * @param userId
	 * @return
	 */
	DataGrid datagridstationery(StationeryForm form);
	/**
	 * 获取文具列表
	 * @return
	 */
	List<StationeriesEntity> getstationerylist();
	/**
	 * 根据部门，订单日期汇总采购单
	 * @return
	 */
	DataGrid datagridbuy(StationeryForm form);
	/**
	 * 获取文具采购日期
	 * @return
	 */
	List<StationeryEntity> tree();
	/**
	 * 批量导入文具
	 * @param request 
	 * @return
	 */
	Msg bulkImport(HttpServletRequest request);
}
