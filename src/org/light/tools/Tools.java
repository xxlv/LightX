package org.light.tools;

import org.light.http.HTTP;

public class Tools {

	//解析文件后缀名
	public static String parseFileSuffix(String fileName){
		String suffix="";
		fileName=fileName.toLowerCase();
//		//将真正的文件名称和参数进行分离操作 因为这个函数是正则的，?有特殊的含义，所以需要转义。发送给正则表达式引擎的应该是\?但是直接这样会把?发给正则引擎，所以需要两次转义
//
//		String[] fileAndParameter=fileName.split("\\?");
//		String realfileName=fileAndParameter[0];
//		String parameter=fileAndParameter[1];//参数
//		
//		System.out.println("Tools file 那么 is"+realfileName);
		if("".equals(fileName)||null==fileName){
			return null;
		}
		else if(fileName.endsWith(".php")){
			suffix="php";
		}
		else if(fileName.endsWith(".html")){
			suffix="html";
		}
		
		else if(fileName.endsWith(".css")){
			suffix="css";
		}
		else if(fileName.endsWith(".html")){
			suffix="html";
		}
		else if(fileName.endsWith(".js")){
			suffix="js";
		}
		else if(fileName.endsWith(".png")){
			suffix="png";
		}
		else if(fileName.endsWith(".jpg")){
			suffix="jpg";
		}
		else{
			suffix="FILE_TYPE_NOT_FOUND";	
		}
		
		return suffix;
	}
	
	//解析path
	public static String parsePath(String path){
		if(path.contains("/")){
			String []tmp=path.split("/");
			//若请求为路径而非文件
			if(!tmp[tmp.length-1].contains(".")){
				path=path.concat("/index.html");
			}
		}		
		if(path.endsWith("/")){
			path=path.concat("index.html");
		}
		//如果不包含.说明是一个目录

		return path;
	}
	//通过状态码获取信息
	
	public static String getMsgByHttpState(int httpState){
	String Msg=null;
	if(httpState==HTTP.HTTP_NOT_FOUND){
		Msg="<html><head><title>404 Not Found </title></head><body bgcolor=\"black\"><center><h1 style=\"color:red\">404 Not Found</h1></center><hr><center  style=\"color:red\">LightX </center></body></html>";	
	}
	return Msg;
	}
	
	
	/*
	 * 返回一个字符串 返回一个查询的参数 获取?后面的参数
	 * */
	public static String getQueryStringByPath(String path){
		String queryStr="";
		if(path.isEmpty()){
			return "";
		}
		if(path.endsWith(".php")){
			return "";
		}

		path=path.toLowerCase();
		//将真正的文件名称和参数进行分离操作 因为这个函数是正则的，?有特殊的含义，所以需要转义。
		//发送给正则表达式引擎的应该是\?但是直接这样会把?发给正则引擎，所以需要两次转义
		String[] fileAndParameter=path.split("\\?");
		if(fileAndParameter.length>=2){
		queryStr=fileAndParameter[1];//参数
		}
		return queryStr;
	}
	
	public static String getFilePath(String path){
		String filePath="";
		if(path.isEmpty()){
			return "";
		}
		if(path.endsWith(".php")){
			return path;
		}
		path=path.toLowerCase();
		String[] fileAndParameter=path.split("\\?");
		filePath=fileAndParameter[0];//参数
		return filePath;
	}

	
	
}
