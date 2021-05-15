package com.briup.smart.env.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;
import com.briup.smart.env.util.LogImpl;

public class GatherImplClient implements Client,ConfigurationAware,PropertiesAware{

	String ip;
	String pronum;
	ObjectOutputStream oos;
	
	private Log log;
	private String host;
	private int port;
	
	@Override
	public void send(Collection<Environment> c) throws Exception {
		LogImpl log=new LogImpl();
		
		try {
			if(c.size()==0) {
				log.warn("发送的数据为空，请重新发送数据");
				throw new Exception("发送数据为空，请重新发送");
			}
			Properties props=new Properties();
			InputStream is=new FileInputStream("src/main/resources/gather.properties");
			props.load(is);
			ip=props.getProperty("ip");
			pronum=props.getProperty("pronum");
			
			//构建客户端
			Socket client=new Socket(host,port);
//			System.out.println(client.getLocalPort()+"链接服务器");
			log.info(client.getLocalPort()+"链接服务器");
			//向服务器发送信息,创建对象流，进行序列化
			oos = new ObjectOutputStream(client.getOutputStream());

			//将集合序列化到输出流os中
			log.info("CLIENT:准备发送数据");
			oos.writeObject(c);
			oos.writeObject(null);
			log.info("CLIENT:成功发送"+c.size()+"条数据");
			oos.flush();
			if(oos!=null) {
				oos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void init(Properties properties) throws Exception {
		host = properties.getProperty("host");
		port = Integer.valueOf(properties.getProperty("port"));
		
	}

	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		log = configuration.getLogger();
		
	}
	

}
