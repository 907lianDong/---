package com.briup.smart.env.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class GetConn {
	//把数据库链接所需的信息保存到一个文件中  ss.properties
	//ss.properties 后缀为.properties的文件要求内部必须放置键值对  key=val
	//Map<k,v>-->properties就是一个map集合，直接将后缀为.properties的文件中的键值对读取到集合中
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	static {
		
		try {
			Properties props=new Properties();
			InputStream is=new FileInputStream("src/main/resources/gather.properties");
			props.load(is);
			//把集合中的数据获取到复制给变量
			driver=props.getProperty("driver");
			url=props.getProperty("url");
			user=props.getProperty("user");
			password=props.getProperty("password");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//一调用就返回conn对象
	public static Connection getConn() throws Exception {
		Class.forName(driver);
	 	Connection conn = DriverManager.getConnection(url, user, password);
	 	return conn;
	}
	//关闭链接对象
	public static void endConn(Connection conn) throws SQLException {
		conn.close();
	}

}
