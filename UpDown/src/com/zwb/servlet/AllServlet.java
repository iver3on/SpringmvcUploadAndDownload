package com.zwb.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AllServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AllServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method.equals("upload")){
			this.upload(request,response);
		}
		if (method.equals("download")) {
			this.download(request,response);
		}
	}
	
	
	private void download(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		//得到工程的绝对路径  http://localhost:8080/UpDown
		String path = request.getServletContext().getRealPath("/")+"images\\";
		String filename = request.getParameter("filename");
		String path1 = request.getServletContext().getContextPath();
		String path2 = request.getServletPath();
		String path3 = request.getRequestURI();
		System.out.println("1:"+path+filename);
		System.out.println("2:"+path1);
		System.out.println("3:"+path2);
		System.out.println("4:"+path3);
		File file = new File(path+filename);
		/*------WebKitFormBoundary7ADSvXZzzYDvrrd0
		Content-Disposition: form-data; name="myfile"; filename="1.txt"
		Content-Type: text/plain

		hello
		------WebKitFormBoundary7ADSvXZzzYDvrrd0--*/
		if (file.exists()) {
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
			InputStream inputStream = new FileInputStream(file);
			OutputStream outputStream = response.getOutputStream();
			byte[] b = new byte[1024]; 
			int n ;
			while((n = inputStream.read(b))!=-1){
				outputStream.write(b, 0, n);
			}
			outputStream.close();
			inputStream.close();
		}else{
			request.setAttribute("errorResult", "文件不存在");
			request.getRequestDispatcher("jsp/01.jsp").forward(request, response);
		}
		
	}

	private void upload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//接收请求传过来的输入流
		InputStream inputStream = request.getInputStream();
		File file = new File("E:/tempFile");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		byte[] b = new byte[1024];
		int n =0;
		while((n=inputStream.read(b))!=-1){
			fileOutputStream.write(b, 0, n);
		}
		fileOutputStream.close();
		inputStream.close();
		
		//通过此类可以读取文件的某一行的数据，可以根据文件内容指针 定位文件起始位置  比较方便
		//只读r
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		randomAccessFile.readLine();
		String str = randomAccessFile.readLine();
		//记录文件最后一个反/  加1 开始位置   获取文件名
		int beginIndex = str.lastIndexOf("=")+2;
		int endIndex = str.lastIndexOf("\"");
		String filename = str.substring(beginIndex, endIndex);
		System.out.println(filename);
		//获取文件内容 开始位置 第一个回车符  最后一个换行符
		//传输流内容
		/*------WebKitFormBoundary7ADSvXZzzYDvrrd0
		Content-Disposition: form-data; name="myfile"; filename="1.txt"
		Content-Type: text/plain

		hello
		------WebKitFormBoundary7ADSvXZzzYDvrrd0--*/
		randomAccessFile.seek(0);
		long startPosition = 0;
		int i = 0;
		while((n=randomAccessFile.readByte())!=-1&&i<=3){
			if(n=='\n'){
				//the offset from the beginning of the file, in bytes, at which the next read or write occurs.
				startPosition = randomAccessFile.getFilePointer();
				i++;
			}
		}
		//startPosition = startPosition;
		
		//获取文件内容结束位置，从后往前读
		randomAccessFile.seek(randomAccessFile.length());
		long endPosition = randomAccessFile.getFilePointer();
		int j = 1;
		while(endPosition>=0&&j<=2){
			endPosition--;
			randomAccessFile.seek(endPosition);
			if (randomAccessFile.readByte()=='\n') {
				j++;
			}
		}
		endPosition = endPosition -1 ;
		System.out.println(startPosition+"xxx"+endPosition);
		//设置保存上传文件的路径
		String realPath = getServletContext().getRealPath("/") + "images";
		File fileupload = new File(realPath);
		if(!fileupload.exists()){
			fileupload.mkdir();
		}
		File saveFile = new File(realPath,filename);
		RandomAccessFile randomAccessFile1 = new RandomAccessFile(saveFile,"rw");
		//从临时文件当中读取文件内容（根据起止位置获取）
		randomAccessFile.seek(startPosition);
		while(startPosition < endPosition){
			randomAccessFile1.write(randomAccessFile.readByte());
			startPosition = randomAccessFile.getFilePointer();
		}
		//关闭输入输出流、删除临时文件
		randomAccessFile.close();
		randomAccessFile1.close();
		file.delete();
		
		request.setAttribute("result", "上传成功！");
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/01.jsp");
		dispatcher.forward(request, response);
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
