package com.zwb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("zwb")
public class DownloadController {

	@RequestMapping(value="/download",method=RequestMethod.GET)
	public String download(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		String filename = request.getParameter("filename");
		String path = request.getServletContext().getRealPath("/")+"images\\";
		File file = new File(path+filename);
		if (file.exists()) {
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
			InputStream inputStream = new FileInputStream(file);
			OutputStream outputStream = response.getOutputStream();
			byte[] b = new byte[2014];
			int n;
			while((n=inputStream.read(b))!=-1){
				outputStream.write(b, 0, n);
			}
			outputStream.close();
			inputStream.close();
		}else{
			request.setAttribute("errorResult", "文件不存在");
			request.getRequestDispatcher("springmvc").forward(request, response);
		}
		return "/success";
	}
}
