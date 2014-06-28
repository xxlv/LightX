package org.light.php;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class PHPModle {
	
	//php地址
	private String  PHPDir="";
	private static final String PHPCGI="php-cgi";
	public PHPModle(){}
	
	
//	public static InputStream runPHP(String phpPath){
//		InputStream in=null;
//		String [] cmd={"cmd","X:/web/","start php index.php"};
//		Runtime runtime = Runtime.getRuntime(); 
//		try {
//			Process process = runtime.exec(cmd);
//			in=process.getInputStream();
//			
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return in;
//	}
//	
	public static String runPHP(String phpPath,String queryString){
		String html="";
		String cmd=PHPCGI+" "+phpPath;
		System.out.println("查询字符串为"+queryString);
		cmd=cmd+" "+queryString;//空格不能少
		System.out.println(cmd);
		Runtime runtime = Runtime.getRuntime(); 
		try {
			//执行cgi程序获取php信息
			Process process = runtime.exec(cmd);
			InputStream fis=process.getInputStream();
			//用一个读输出流类去读      
             InputStreamReader isr=new InputStreamReader(fis);      
            //用缓冲器读行      
             BufferedReader br=new BufferedReader(isr);      
             String line=null;      
            //直到读完为止 
             String PHPPowered=br.readLine();//获取cgi发送的头
             String ContentType=br.readLine();//获取cgi发送的头
             //将cgi返回的html组装
            while((line=br.readLine())!=null)      
             {  
            	html+=line; 
             }     
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}
	
	

}
