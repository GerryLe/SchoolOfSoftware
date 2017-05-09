package com.rosense.module.system.web.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.common.web.action.BaseController;
import com.rosense.module.system.service.IMessageService;
import com.rosense.module.system.web.form.MessageForm;

@Controller
@RequestMapping("/admin/system/message")
public class MessageAction extends BaseController {
	@Autowired
	private IMessageService messageService;

	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(MessageForm form) {
		return this.messageService.datagrid(form);
	}

	@RequestMapping("/list.do")
	@ResponseBody
	public List<MessageForm> list(MessageForm form) {
		return this.messageService.list(form);
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(String ids) {
		return this.messageService.delete(ids);
	}

	@RequestMapping("/no_update.do")
	@ResponseBody
	public Msg edit(MessageForm form) {
		if (StringUtil.isEmpty(form.getId()))
			return this.messageService.updateAll();
		else
			return this.messageService.update(form.getId());
	}

	@RequestMapping("/get.do")
	@ResponseBody
	public MessageForm get() {
		final MessageForm form = new MessageForm();
		final StringBuilder sb = new StringBuilder();
		long count = this.messageService.count();
		if (count > 0) {
			sb.append("有" + count + "条新消息<a onclick=\"open_message();\">点击查看</a>");
		}
		form.setContent(sb.toString());
		return form;
	}
}
