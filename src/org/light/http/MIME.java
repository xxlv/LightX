package org.light.http;

public class MIME {
	public static  final String evy="application/envoy"; 
	public static  final String js="application/x-javascript"; 
	public static  final String swf="application/x-shockwave-flash"; 
	public static  final String mp3="audio/mpeg"; 
	public static  final String wav="audio/x-wav"; 
	public static  final String gif="image/gif"; 
	public static  final String jpe="image/jpeg"; 
	public static  final String jpeg="image/jpeg"; 
	public static  final String jpg="image/jpeg"; 
	public static  final String css="text/css"; 
	public static  final String htm="text/html"; 
	public static  final String html ="text/html";
	/*
	 * 通过后缀名称来获取MIME类型
	 * */
	public static String getMIMEBySuffix(String suffer){
		if(suffer.startsWith(".")){
			suffer=suffer.substring(1);
		}
		if("htm".equals(suffer.toLowerCase())){
			return htm;		
		}
		if("html".equals(suffer.toLowerCase())){
			return html;		
		}
		
		if("css".equals(suffer.toLowerCase())){
			return css;		
		}
		if("js".equals(suffer.toLowerCase())){
			return js;		
		}
		if("gif".equals(suffer.toLowerCase())){
			return gif;		
		}
		
		if("jpe".equals(suffer.toLowerCase())){
			return jpe;		
		}
		
		if("jpeg".equals(suffer.toLowerCase())){
			return jpeg;		
		}
		if("jpg".equals(suffer.toLowerCase())){
			return jpg;		
		}
		else{
		return null;
		}
	} 
	
	

}
