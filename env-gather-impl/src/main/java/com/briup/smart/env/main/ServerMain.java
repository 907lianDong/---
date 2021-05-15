package com.briup.smart.env.main;

import java.net.Socket;

import com.briup.smart.env.ConfigAImpl;
import com.briup.smart.env.Configuration;
import com.briup.smart.env.server.GatherImplServer;
import com.briup.smart.env.server.Server;

//服务器入口类
public class ServerMain {
	
	public static void main(String[] args) {
		Configuration config = ConfigAImpl.getInstance();
		
		try {
			Server server = config.getServer();
			server.reciver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
