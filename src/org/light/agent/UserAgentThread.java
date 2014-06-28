package org.light.agent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

import org.light.Listener;
import org.light.http.HTTP;
import org.light.http.MIME;
import org.light.php.PHPModle;
import org.light.tools.Tools;

/*
 * 当用户请求资源的是否 分配一个代理线程，并将改线程加入到线程池中
 * 用户代理线程类 处理一次服务器请求
 * 
 * */
public class UserAgentThread implements Runnable{
	
	public String resPath=null;
	public int i=0;
	public  int ResponseState;
	private InputStream in=null;
	private PrintStream out=null;
	public Socket	clientSocket=null;
	public UserAgentThread(Socket s){
		this.clientSocket=s;
		try {
			this.in=s.getInputStream();
			this.out=new PrintStream(s.getOutputStream());
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
			//获取当前系统时间
			final long start=System.currentTimeMillis();
			try {
				//解析头并自动回送HTTP响应
				autoParseRequestHeadAndResponse(this.in);
			} catch (Exception e) {
				sendErrorMsg(500,"服务器内部错误，信息已记录");
				e.printStackTrace();
			}	

	}
	public boolean autoParseRequestHeadAndResponse(InputStream in) throws Exception{

		Vector<String> headers=new Vector<String>();
		boolean isOK=false;
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		/*获取HTTP请求头信息*/
		String headContent = br.readLine();
		synchronized(br){
			String line="";
			while((line=br.readLine())!=null){
				if(line.isEmpty()){
					break;
					}
				headers.add(line.toString());
			}
		}
		String referer=HTTP.getReferer(headers);
		System.out.println("referer 为"+ referer);
//		System.out.println(Thread.currentThread().getName());
//		String host=br.readLine();
//		String Connection=br.readLine();
//		String CacheControl=br.readLine();
//		String Accept=br.readLine();
//		String UserAgent=br.readLine();
//		String Referer=br.readLine();
//		String AcceptEncoding=br.readLine();
//		String AcceptLanguage=br.readLine();
		
		//一个方法是把此后的header存放在一个数组中，解析这个数组
		

//		HTTP.getReferer(headers);
		/*分析第一行数据*/
		if((null ==headContent|| "".equals(headContent))){
			return false;
			}
		String headArr[]=headContent.split(" ");
		String RequestType=headArr[0];//请求头类型  GET 和POST
		String DocumentPath=headArr[1];
		String HTTPVersion=headArr[2];
		//解析头数据 
		System.out.println("requestType: "+RequestType);
		System.out.println("DocumentPath: "+DocumentPath);
		System.out.println("HTTPVersion: "+HTTPVersion);
		if("GET".equals(RequestType.toUpperCase())){
			
			if(!referer.isEmpty()){
				int index=referer.indexOf("/",8);//   不包括(http://)该位置的/
				referer=referer.substring(index);//返回资源referer的地址  /test
//				String [] subPath=referer.split("/");
//				System.out.println("》》》》》》》》》》》》》》》》》"+referer);
//				for(int i=0;i<subPath.length;i++){
//					System.out.println("sub===============" +subPath[i]);	
//				}				
				
				doGet(referer+DocumentPath);
			}
			else{
				doGet(DocumentPath);	
			}
			
		}
		else if("POST".equals(RequestType.toUpperCase())) {
			if(!referer.isEmpty()){
				doPost(referer+DocumentPath);
			}
			else{
				doPost(DocumentPath);
				
			}
		}
		else{
		}
		return isOK;
	}
	
	
	
	//处理所有get请求 暂时只处理本域下的
	public void doGet(String path) throws Exception{
		
		//absPath中可能包含参数?test=1之类的信息
		//当页面页面响应发送给客户端时候，浏览器执行html，此时html又包含了请求，此时请求的路径为相对路径时该如何解析？
		String absPath=Tools.parsePath(Listener.WEB_ROOT+path);
		System.out.println("请求资源绝对路径========== "+absPath);
		String queryString =Tools.getQueryStringByPath(absPath);
		String filePath=Tools.getFilePath(absPath);//去掉可能的?后的参数
		File f=new File(filePath);
	
		String fileType=Tools.parseFileSuffix(filePath);//获取文件类型
		System.out.println("请求文件类型========"+fileType);
		if(!f.canRead()){
			System.out.println("can not read");
			sendResponse(HTTP.HTTP_NOT_FOUND,null,null,filePath+"can not read ,please check~");
			return;
		}
		if(!f.exists()){
			//没有发现资源
			System.out.println("没有发现目标资源");
			sendResponse(HTTP.HTTP_NOT_FOUND,null,null,null);
			return;
	
		}
		else{
			if("php".equals(fileType)){
				//解析php文件 单独分离出来 方便以后处理
				//php解释器解释并返回html
				String html=PHPModle.runPHP(filePath,queryString);//获取输入流 这个流里面包含了php解释后的数据
				sendResponsePHP(HTTP.HTTP_OK,html,null);	
				return;
			}
			else{
			//这里获取文件的类型  这里需要做很多判断 具体网站支持的类型
			//读取目标资源 写回
			//指向文件的输入流
			System.out.println("当前请求的是"+MIME.getMIMEBySuffix(fileType));
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			sendResponse(HTTP.HTTP_OK,bis,fileType,null);	
			return;
			}
			
		}
		
		
		
	}
	//处理post请求
	public void doPost(String path) throws Exception{
		doGet(path);
	}
	
	/*
	 * 响应信息发送  
	 * */
	public void sendResponse(int state,BufferedInputStream bis,String fileType,String extra) throws IOException{
		//发送状态头
		
		String headers=HTTP.getHttpResponseHeader(state, MIME.getMIMEBySuffix(fileType));
		System.out.println("响应头信息应该是 "+headers);
		if(bis==null){state=HTTP.HTTP_NOT_FOUND;}
		if(state==HTTP.HTTP_OK){
			byte[] buff=new byte[8080];//注意这里的缓冲太小的话会出问题
			out.println("HTTP/1.1 200 OK");
			out.println(headers);
			out.println();
			while ((bis.read(buff)) != -1) {
				out.write(buff);
				System.out.println("这是响应信息咯 "+new String(buff));
				out.flush();
				}
			bis.close();
			out.close();	
		}
		else if(state==HTTP.HTTP_NOT_FOUND){
		System.out.println("发送状态404");
		out.println("HTTP/1.1 404 Not Found");
		out.println();
		out.write(Tools.getMsgByHttpState(state).getBytes());
		out.write((""+extra).getBytes());
		out.close();	
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//给浏览器发送响应
	public void sendResponsePHP(int state,String html,String extra) throws IOException{
		//发送状态头
		if(state==HTTP.HTTP_OK){
			out.println("HTTP/1.1 200 OK");
			out.println("Content-Type: text/html");
			out.println();
			out.write(html.getBytes());
			out.flush();

			out.close();	
		}
		else if(state==HTTP.HTTP_NOT_FOUND){
		System.out.println("没有发现哦");
		out.println("HTTP/1.1 404 Not Found");
		out.println();
		out.write(Tools.getMsgByHttpState(state).getBytes());
		out.close();	
		}
	}
	
	
	//发送响应信息
	public void sendErrorMsg(int state,String Msg)  {
		
		out.println("HTTP/1.1 500 Internal_Error");
		out.println("Content-Type: text/html");
		out.println();
		try {
			out.write(Msg.getBytes());
			out.write(("<br/><font color='red'>code:"+state+"</font>").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();	
		
		
		
	}

		
	}