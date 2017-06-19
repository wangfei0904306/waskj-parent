package com.waskj.base.consumer.core;

import com.waskj.base.consumer.core.result.JsonResult;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/uploadController")
public class UploadController {

	private static final Log log = Logs.get();

	@RequestMapping("/upload")
	@ResponseBody
	public JsonResult upload(DefaultMultipartHttpServletRequest multipartRequest, HttpSession session) {
		JsonResult json = new JsonResult();
		json.setSuccess(true);
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile file = multipartRequest.getFile((String) iterator.next());
				if (!file.isEmpty()) {
					log.info(file.getContentType());// 获取文件MIME类型

					log.info(file.getName());// 获取表单中文件组件的名字

					log.info(file.getOriginalFilename());// 获取上传文件的原名

					log.info(file.getSize());// 获取文件的字节大小，单位byte

					String fileName = UUID.randomUUID().toString().replaceAll("-", "")
							+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));// 保存后的文件名
					log.info(fileName);

					try {
						// 文件保存路径
						String filePath = session.getServletContext().getRealPath("/") + "attached" + File.separator;
						json.setMsg("上传成功，请查看【" + filePath + "】目录");
						File uploadFile = new File(filePath + fileName);
						uploadFile.mkdirs();
						file.transferTo(uploadFile);// 保存到一个目标文件中。
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("fileUrl", "/attached/" + fileName);
						json.setObj(m);
					} catch (Exception e) {
						json.setSuccess(false);
						json.setMsg(e.getLocalizedMessage());
						e.printStackTrace();
					}
				}
			}
		}
		return json;
	}

}
