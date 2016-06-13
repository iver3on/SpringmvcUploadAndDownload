# SpringmvcUploadAndDownload
Files upload and download based on SpringMVC
> 实现缩略图：使用Thunmnails库 或者使用AWT实现，前者较为简单。后者复杂。

文件上传service实现：
1. 获取上传文件的输入流  
2. 获取上穿后文件的绝对路径  
3. 通过2中的绝对路径将输出流指向这个文件  
4. 创建缓冲字节数组 1KB  
5. 将输入流数据读取到buffer缓冲数组中，并返回字节数组的长度len，如果len>0表明输入流中有数据.  
6. 将字节数组写入输出流  
7. 执行while  

