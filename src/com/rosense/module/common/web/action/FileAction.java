package com.rosense.module.common.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.util.file.FileUtils;
import com.rosense.module.common.web.servlet.WebContextUtil;

/**
 * 公共的文件上传处理Action
 * 默认将文件上传到一个临时目录temp
 * 操作完之后再将文件拷贝到相应的目录
 *
 */
@Controller
@RequestMapping("/fileAction")
public class FileAction extends BaseController {

	@RequestMapping(value = "/no_Delete.do")
	public @ResponseBody
	void doNotNeedAuth_deleteFile(String path) {
		FileUtils.deleteFile(WebContextUtil.getRealPath(path));
	}

}
