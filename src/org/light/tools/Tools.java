package org.light.tools;

import org.light.http.HTTP;

public class Tools {

	//�����ļ���׺��
	public static String parseFileSuffix(String fileName){
		String suffix="";
		fileName=fileName.toLowerCase();
//		//���������ļ����ƺͲ������з������ ��Ϊ�������������ģ�?������ĺ��壬������Ҫת�塣���͸�������ʽ�����Ӧ����\?����ֱ���������?�����������棬������Ҫ����ת��
//
//		String[] fileAndParameter=fileName.split("\\?");
//		String realfileName=fileAndParameter[0];
//		String parameter=fileAndParameter[1];//����
//		
//		System.out.println("Tools file ��ô is"+realfileName);
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
	
	//����path
	public static String parsePath(String path){
		if(path.contains("/")){
			String []tmp=path.split("/");
			//������Ϊ·�������ļ�
			if(!tmp[tmp.length-1].contains(".")){
				path=path.concat("/index.html");
			}
		}		
		if(path.endsWith("/")){
			path=path.concat("index.html");
		}
		//���������.˵����һ��Ŀ¼

		return path;
	}
	//ͨ��״̬���ȡ��Ϣ
	
	public static String getMsgByHttpState(int httpState){
	String Msg=null;
	if(httpState==HTTP.HTTP_NOT_FOUND){
		Msg="<html><head><title>404 Not Found </title></head><body bgcolor=\"black\"><center><h1 style=\"color:red\">404 Not Found</h1></center><hr><center  style=\"color:red\">LightX </center></body></html>";	
	}
	return Msg;
	}
	
	
	/*
	 * ����һ���ַ��� ����һ����ѯ�Ĳ��� ��ȡ?����Ĳ���
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
		//���������ļ����ƺͲ������з������ ��Ϊ�������������ģ�?������ĺ��壬������Ҫת�塣
		//���͸�������ʽ�����Ӧ����\?����ֱ���������?�����������棬������Ҫ����ת��
		String[] fileAndParameter=path.split("\\?");
		if(fileAndParameter.length>=2){
		queryStr=fileAndParameter[1];//����
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
		filePath=fileAndParameter[0];//����
		return filePath;
	}

	
	
}
