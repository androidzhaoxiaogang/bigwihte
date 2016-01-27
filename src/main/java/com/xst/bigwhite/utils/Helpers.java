package com.xst.bigwhite.utils;

import java.io.File;
import java.util.Random;

public class Helpers {

	public static String getDeviceNo(Long id){
		return Long.toString(840000 + id);
	}
	
	public static String random(int length) {
		Random random = new Random();
		String result = "";
		for (int i = 0; i < length; i++) {
			result += random.nextInt(10);
		}
		
		return result;
	}
	
    /* 
    **获得前运行文件在服务器上的绝对路径 
    */  
	public static String getBasePath(javax.servlet.http.HttpServletRequest request,String path) {  
    	//String path = request.getContextPath();  
        String basePath = request.getScheme() + "://" + request.getServerName()  
                + ":" + request.getServerPort() + path;  
        return basePath;  
    }  

    /**
     * 函数名：getPath 作 用：获取服务器路径 参 数：request 返回值：字符串 
     */  
    public static String getPath(javax.servlet.http.HttpServletRequest request,String basePath) {  
        //String basePath = "/WEB-INF/classes/";
    	//String basePath = getBasePath(request);
        String path = request.getSession().getServletContext().getRealPath(basePath);  
        path = path.replace("/", "\\");  
        return path + File.separator;  
    }  
	
	public static String getFileName(){
        return new TimeString().getTimeString();
    }
     
}
