package org.light.http;

import java.util.Vector;

public class HTTP {


//定义一些状态值信息		
public static final int HTTP_ACCEPTED =202;	
public static final int HTTP_BAD_GATEWAY =502;
public static final int HTTP_BAD_METHOD= 405;
public static final int HTTP_BAD_REQUEST=  400;
public static final int HTTP_CLIENT_TIMEOUT =408;
public static final int HTTP_CONFLICT=   409;
public static final int HTTP_CREATED = 201;
public static final int HTTP_ENTITY_TOO_LARGE=   413;
public static final int HTTP_FORBIDDEN =  403;
public static final int HTTP_GATEWAY_TIMEOUT = 504;
public static final int HTTP_GONE =  410;
public static final int HTTP_INTERNAL_ERROR =  500;
public static final int HTTP_LENGTH_REQUIRED =  411;
public static final int HTTP_MOVED_PERM  = 301;
public static final int HTTP_MOVED_TEMP  =  302;
public static final int HTTP_MULT_CHOICE = 300;
public static final int HTTP_NO_CONTENT =  204;
public static final int HTTP_NOT_ACCEPTABLE = 406;
public static final int HTTP_NOT_AUTHORITATIVE=  203;
public static final int HTTP_NOT_FOUND =  404;
public static final int HTTP_NOT_IMPLEMENTED=  501;
public static final int HTTP_NOT_MODIFIED = 304;
public static final int HTTP_OK = 200;
public static final int HTTP_PARTIAL=  206;
public static final int HTTP_PAYMENT_REQUIRED=  402;
public static final int HTTP_PRECON_FAILED=  412;
public static final int HTTP_PROXY_AUTH = 407;
public static final int HTTP_REQ_TOO_LONG = 414;
public static final int HTTP_RESET  = 205;
public static final int HTTP_SEE_OTHER=  303;
public static final int HTTP_UNAUTHORIZED=  401;
public static final int HTTP_UNAVAILABLE = 503;
public static final int HTTP_UNSUPPORTED_TYPE=  415;
public static final int HTTP_USE_PROXY = 305;
public static final int HTTP_VERSION = 505;



/*
 * 功能描述：通过状态值和数据类型构造http响应头
 * */
public static String getHttpResponseHeader(int state,String mimeType){
	String resp="";
	resp="Server:LightX\n";
	resp+="Connection:Keep-Alive\n";
	resp+="Content-Type: "+mimeType;
	return resp;
	
}
public  static String getReferer(Vector<String> headers){
	String referer="";
	if(headers.size()<=0){
		return "";
	}
	for(int i=0;i<headers.size();i++){

		if(headers.get(i).toLowerCase().indexOf("refere")!=-1){
			referer=headers.get(i).split(":",2)[1];//获取referer 因为"http:"存在:所以用2
		}
	}
	return referer;
	
}







	
	
}
