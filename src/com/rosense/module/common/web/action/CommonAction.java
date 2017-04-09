package com.rosense.module.common.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rosense.basic.util.IOUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.module.cache.Caches;
import com.rosense.module.common.web.servlet.WebContextUtil;

@Controller
@RequestMapping("/common")
public class CommonAction extends BaseController {

	@RequestMapping("/photo")
	public @ResponseBody
	void photo(String id, HttpServletResponse response) {
		try {
			Map<String, String> map = Caches.getMap("USER", id);
			String photo = WebContextUtil.getRealPath("/template/images/user.png");
			if (map != null && !map.isEmpty()) {
				if (StringUtil.isNotEmpty(map.get("PHOTO")))
					photo = WebContextUtil.getRealPath(map.get("PHOTO"));
			}
			File file = new File(photo);
			if (!file.exists()) {
				file = new File(WebContextUtil.getRealPath("/template/images/user.png"));
			}
			response.setContentType("image/gif");
			response.setHeader("Content-Length", String.valueOf(file.length()));

			final OutputStream outputStream = response.getOutputStream();
			InputStream is = new FileInputStream(file);
			IOUtils.copyStream(is, outputStream);
			is.close();
		} catch (IOException e) {
		}
	}

}
