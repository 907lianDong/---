package com.briup.smart.env.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;


import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;
import com.briup.smart.env.util.LogImpl;

public class GatherImplServer implements Server,ConfigurationAware,PropertiesAware{
	//创建日志类对象
	private Log log ;
	private DBStore dbstore;
	private int serverPort;
	
	String pronum;
	Collection<Environment> c=new ArrayList<>();
	ObjectInputStream oi;
	
	@Override
	public void reciver() throws Exception {
		Properties props=new Properties();
		InputStream is=new FileInputStream("src/main/resources/gather.properties");
		props.load(is);
		pronum=props.getProperty("pronum");
		
		//构建服务器
		ServerSocket socket = new ServerSocket(serverPort);
		log.info("SERVER:服务器已经启动，端口号为"+serverPort);
		//开启服务器，等待客户端链接
		while(true) {
			log.info("服务器开启等待客户端链接");
			Socket client=socket.accept();
			log.info("已经有客户端"+client.getPort()+"链接");
			Thread t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						log.info("线程"+Thread.currentThread().getName()+"去处理");
						//构建缓存字符流，包装网络流，用来获取客户端发送的消息
						Environment s=null;
						oi = new ObjectInputStream(client.getInputStream());
						Object object=oi.readObject();
						if(object instanceof Collection) {
							c=(Collection<Environment>) object;
							log.info("SERVER:"+Thread.currentThread().getName()+"处理，成功接收数据"+c.size()+"条");
							dbstore.saveDB(c);
						}
						if(client!=null) {
							client.close();
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
	}

	@Override
	public void init(Properties properties) throws Exception {
		serverPort  = Integer.valueOf(properties.getProperty("server-port"));
		
	}

	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		log= configuration.getLogger();
		dbstore = configuration.getDbStore();
	}
	

}
