package org.light;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.light.agent.UserAgentThread;

/*
 *ʵ���������ķ����� ���Խ���HTML TEXT/HTML
 *����������� �������
 *@author xxlv 
 * */
public class Listener {
	//Ĭ�ϼ���80�˿�
	private static final int PORT=8080;//�˿ں�
	public static final int CONCURRENT_ACCESS = 100;//������
	public static final int RESPONSE_TIMEOUT=10;//����ӳ�10��
	private static final boolean isrunable=true;//�����Ƿ�����
	public static final String WEB_ROOT="X:/web";//��Ŀ¼
	
	/*
	 * ��������
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
				System.out.println("�������Ѿ��ɹ������� ���ڼ���"+PORT+" �˿�");
				Socket clientSocket=ss.accept();//��ʼ����
				//Ϊ����û����䴦���߳�
				pool.submit(new UserAgentThread(clientSocket));
//				new Thread(new UserAgentThread(clientSocket)).start();
				
				
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	
	}
	public static void main(String [] args){
		//��������
		new Listener().runServer();
	}
}
