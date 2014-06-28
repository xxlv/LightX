package org.light;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.light.agent.UserAgentThread;

/*
 *实现轻量级的服务器 可以解析HTML TEXT/HTML
 *这个类是主类 否则监听
 *@author xxlv 
 * */
public class Listener {
	//默认监听80端口
	private static final int PORT=8080;//端口号
	public static final int CONCURRENT_ACCESS = 100;//并发数
	public static final int RESPONSE_TIMEOUT=10;//最大延迟10秒
	private static final boolean isrunable=true;//服务是否启动
	public static final String WEB_ROOT="X:/web";//跟目录
	
	/*
	 * 服务启动
	 * */	
	public void runServer(){
			ExecutorService pool=Executors.newFixedThreadPool(CONCURRENT_ACCESS);
			ServerSocket ss = null;
			try {
				ss = new ServerSocket(PORT);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				while(isrunable){
				System.out.println("服务器已经成功启动， 正在监听"+PORT+" 端口");
				Socket clientSocket=ss.accept();//开始监听
				//为这个用户分配处理线程
				pool.submit(new UserAgentThread(clientSocket));
//				new Thread(new UserAgentThread(clientSocket)).start();
				
				
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	
	}
	public static void main(String [] args){
		//启动服务
		new Listener().runServer();
	}
}
