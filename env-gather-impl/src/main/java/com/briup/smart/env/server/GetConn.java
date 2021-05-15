package com.briup.smart.env.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class GetConn {
	//�����ݿ������������Ϣ���浽һ���ļ���  ss.properties
	//ss.properties ��׺Ϊ.properties���ļ�Ҫ���ڲ�������ü�ֵ��  key=val
	//Map<k,v>-->properties����һ��map���ϣ�ֱ�ӽ���׺Ϊ.properties���ļ��еļ�ֵ�Զ�ȡ��������
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	static {
		
		try {
			Properties props=new Properties();
			InputStream is=new FileInputStream("src/main/resources/gather.properties");
			props.load(is);
			//�Ѽ����е����ݻ�ȡ�����Ƹ�����
			driver=props.getProperty("driver");
			url=props.getProperty("url");
			user=props.getProperty("user");
			password=props.getProperty("password");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//һ���þͷ���conn����
	public static Connection getConn() throws Exception {
		Class.forName(driver);
	 	Connection conn = DriverManager.getConnection(url, user, password);
	 	return conn;
	}
	//�ر����Ӷ���
	public static void endConn(Connection conn) throws SQLException {
		conn.close();
	}

}
