package com.rosense.module.system.service;



import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.HolidayApplysForm;

public interface IHolidayApplysService {
	/**
	 * 添加
	 */
	public Msg add(HolidayApplysForm form);
	
	/**
	 * 删除
	 */
	public Msg delete(String id);

	/**
	 * 修改
	 */
	public Msg update(HolidayApplysForm form);
	
	/**
	 * 查询
	 */
	public DataGrid holidayApplysdg(HolidayApplysForm form);
	
	/**
	 * 编辑加载
	 */
	public HolidayApplysForm get(String id);

	/**
	 * 批准
	 */
	public Msg approvaltrue(HolidayApplysForm form);
	

	/**
	 * 申请加载
	 */
	public HolidayApplysForm loading(String id);
	/**
	 * 判断是否有权限编辑，只有个人可以编辑申请信息
	 * @param id
	 * @return
	 */
	public Msg getLimit(String id);
	/**
	 * 判断是否有权限审核
	 * @param id
	 * @return
	 */
	public Msg getaudit(String id);



}
