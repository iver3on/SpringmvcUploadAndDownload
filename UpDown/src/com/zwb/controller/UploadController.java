package com.zwb.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Controller
@RequestMapping("zwb")
public class UploadController {
	
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	public String upload(HttpServletRequest request,HttpServletResponse responce) throws IllegalStateException, IOException{
		System.out.println("进入action");
		//创建一个多用的多部分解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//判断repuest是否有文件上传，即多部分请求
		if (commonsMultipartResolver.isMultipart(request)) {
			//转换成多部分request
			MultipartHttpServletRequest mutirequest = (MultipartHttpServletRequest) request;
			//取得request的中的所有文件名
			Iterator<String> iter = mutirequest.getFileNames();
			while (iter.hasNext()) {
				//记录上传时间
				int preTime =(int) System.currentTimeMillis();
				//获取上传文件
				MultipartFile file = mutirequest.getFile(iter.next());
				if (file!=null) {
					String filename = file.getOriginalFilename();
					if (filename.trim()!="") {
						System.out.println("要上传文件的名字是："+filename);
						//String filechangename = "uploadFile"+filename;
						String realPath = request.getSession().getServletContext().getRealPath("/") + "images/"+filename;
						File file2 = new File(realPath);
						file.transferTo(file2);
					}
				}
				 //记录上传该文件后的时间  
                int finaltime = (int) System.currentTimeMillis();  
                System.out.println("上传文件花费总时间是："+(finaltime - preTime));  
			}
		}
		return "/success";
	}
}
