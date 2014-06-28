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
 * ���û�������Դ���Ƿ� ����һ�������̣߳��������̼߳��뵽�̳߳���
 * �û������߳��� ����һ�η���������
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
		
			//��ȡ��ǰϵͳʱ��
			final long start=System.currentTimeMillis();
			try {
				//����ͷ���Զ�����HTTP��Ӧ
				autoParseRequestHeadAndResponse(this.in);
			} catch (Exception e) {
				sendErrorMsg(500,"�������ڲ�������Ϣ�Ѽ�¼");
				e.printStackTrace();
			}	

	}
	public boolean autoParseRequestHeadAndResponse(InputStream in) throws Exception{

		Vector<String> headers=new Vector<String>();
		boolean isOK=false;
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		/*��ȡHTTP����ͷ��Ϣ*/
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
		System.out.println("referer Ϊ"+ referer);
//		System.out.println(Thread.currentThread().getName());
//		String host=br.readLine();
//		String Connection=br.readLine();
//		String CacheControl=br.readLine();
//		String Accept=br.readLine();
//		String UserAgent=br.readLine();
//		String Referer=br.readLine();
//		String AcceptEncoding=br.readLine();
//		String AcceptLanguage=br.readLine();
		
		//һ�������ǰѴ˺��header�����һ�������У������������
		

//		HTTP.getReferer(headers);
		/*������һ������*/
		if((null ==headContent|| "".equals(headContent))){
			return false;
			}
		String headArr[]=headContent.split(" ");
		String RequestType=headArr[0];//����ͷ����  GET ��POST
		String DocumentPath=headArr[1];
		String HTTPVersion=headArr[2];
		//����ͷ���� 
		System.out.println("requestType: "+RequestType);
		System.out.println("DocumentPath: "+DocumentPath);
		System.out.println("HTTPVersion: "+HTTPVersion);
		if("GET".equals(RequestType.toUpperCase())){
			
			if(!referer.isEmpty()){
				int index=referer.indexOf("/",8);//   ������(http://)��λ�õ�/
				referer=referer.substring(index);//������Դreferer�ĵ�ַ  /test
//				String [] subPath=referer.split("/");
//				System.out.println("����������������������������������"+referer);
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
	
	
	
	//��������get���� ��ʱֻ�������µ�
	public void doGet(String path) throws Exception{
		
		//absPath�п��ܰ�������?test=1֮�����Ϣ
		//��ҳ��ҳ����Ӧ���͸��ͻ���ʱ�������ִ��html����ʱhtml�ְ��������󣬴�ʱ�����·��Ϊ���·��ʱ����ν�����
		String absPath=Tools.parsePath(Listener.WEB_ROOT+path);
		System.out.println("������Դ����·��========== "+absPath);
		String queryString =Tools.getQueryStringByPath(absPath);
		String filePath=Tools.getFilePath(absPath);//ȥ�����ܵ�?��Ĳ���
		File f=new File(filePath);
	
		String fileType=Tools.parseFileSuffix(filePath);//��ȡ�ļ�����
		System.out.println("�����ļ�����========"+fileType);
		if(!f.canRead()){
			System.out.println("can not read");
			sendResponse(HTTP.HTTP_NOT_FOUND,null,null,filePath+"can not read ,please check~");
			return;
		}
		if(!f.exists()){
			//û�з�����Դ
			System.out.println("û�з���Ŀ����Դ");
			sendResponse(HTTP.HTTP_NOT_FOUND,null,null,null);
			return;
	
		}
		else{
			if("php".equals(fileType)){
				//����php�ļ� ����������� �����Ժ���
				//php���������Ͳ�����html
				String html=PHPModle.runPHP(filePath,queryString);//��ȡ������ ��������������php���ͺ������
				sendResponsePHP(HTTP.HTTP_OK,html,null);	
				return;
			}
			else{
			//�����ȡ�ļ�������  ������Ҫ���ܶ��ж� ������վ֧�ֵ�����
			//��ȡĿ����Դ д��
			//ָ���ļ���������
			System.out.println("��ǰ�������"+MIME.getMIMEBySuffix(fileType));
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			sendResponse(HTTP.HTTP_OK,bis,fileType,null);	
			return;
			}
			
		}
		
		
		
	}
	//����post����
	public void doPost(String path) throws Exception{
		doGet(path);
	}
	
	/*
	 * ��Ӧ��Ϣ����  
	 * */
	public void sendResponse(int state,BufferedInputStream bis,String fileType,String extra) throws IOException{
		//����״̬ͷ
		
		String headers=HTTP.getHttpResponseHeader(state, MIME.getMIMEBySuffix(fileType));
		System.out.println("��Ӧͷ��ϢӦ���� "+headers);
		if(bis==null){state=HTTP.HTTP_NOT_FOUND;}
		if(state==HTTP.HTTP_OK){
			byte[] buff=new byte[8080];//ע������Ļ���̫С�Ļ��������
			out.println("HTTP/1.1 200 OK");
			out.println(headers);
			out.println();
			while ((bis.read(buff)) != -1) {
				out.write(buff);
				System.out.println("������Ӧ��Ϣ�� "+new String(buff));
				out.flush();
				}
			bis.close();
			out.close();	
		}
		else if(state==HTTP.HTTP_NOT_FOUND){
		System.out.println("����״̬404");
		out.println("HTTP/1.1 404 Not Found");
		out.println();
		out.write(Tools.getMsgByHttpState(state).getBytes());
		out.write((""+extra).getBytes());
		out.close();	
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//�������������Ӧ
	public void sendResponsePHP(int state,String html,String extra) throws IOException{
		//����״̬ͷ
		if(state==HTTP.HTTP_OK){
			out.println("HTTP/1.1 200 OK");
			out.println("Content-Type: text/html");
			out.println();
			out.write(html.getBytes());
			out.flush();

			out.close();	
		}
		else if(state==HTTP.HTTP_NOT_FOUND){
		System.out.println("û�з���Ŷ");
		out.println("HTTP/1.1 404 Not Found");
		out.println();
		out.write(Tools.getMsgByHttpState(state).getBytes());
		out.close();	
		}
	}
	
	
	//������Ӧ��Ϣ
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