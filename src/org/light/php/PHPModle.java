package org.light.php;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class PHPModle {
	
	//php��ַ
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
		System.out.println("��ѯ�ַ���Ϊ"+queryString);
		cmd=cmd+" "+queryString;//�ո�����
		System.out.println(cmd);
		Runtime runtime = Runtime.getRuntime(); 
		try {
			//ִ��cgi�����ȡphp��Ϣ
			Process process = runtime.exec(cmd);
			InputStream fis=process.getInputStream();
			//��һ�����������ȥ��      
             InputStreamReader isr=new InputStreamReader(fis);      
            //�û���������      
             BufferedReader br=new BufferedReader(isr);      
             String line=null;      
            //ֱ������Ϊֹ 
             String PHPPowered=br.readLine();//��ȡcgi���͵�ͷ
             String ContentType=br.readLine();//��ȡcgi���͵�ͷ
             //��cgi���ص�html��װ
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
