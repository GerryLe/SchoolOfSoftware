package com.rosense.module.system.web.action;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IClassService;
import com.rosense.module.system.service.ICourseService;
import com.rosense.module.system.web.form.ClassForm;
import com.rosense.module.system.web.form.CourseForm;

@Controller
@RequestMapping("/admin/system/course")
public class CourseAction extends BaseController {
	@Inject
	private ICourseService courseService;
    @Inject
    private IClassService classService;
	/**
	 * 添加课程
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(CourseForm form) throws Exception {
		return this.courseService.add(form);
	}

	/**
	 * 删除课程
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(CourseForm form) throws Exception {
		return this.courseService.delete(form);
	}

	
	/**
	 * 修改课程信息
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(CourseForm form) throws Exception {
		return this.courseService.update(form);
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public CourseForm get(CourseForm form) throws Exception {
		return this.courseService.get(form.getId());
	}


	/**
	 * 查询课程信息
	 */
	@RequestMapping("/datagridCourse.do")
	@ResponseBody
	public DataGrid datagridCourse(CourseForm form,String selectType,String searchKeyName) throws Exception {
		return this.courseService.datagridCourse(form,selectType,searchKeyName);
	}

	/**
	 * 获取班级信息
	 */
	@RequestMapping("datagridClass.do")
	@ResponseBody
	public DataGrid datagridClass(ClassForm form){
		return this.classService.datagridClass(form);
	}
	
	@RequestMapping("/tree.do")
	@ResponseBody
	public List<CourseForm> tree(CourseForm form) throws Exception {
		return this.courseService.tree();
	}
} 

