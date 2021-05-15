package com.briup.smart.env.main;

import java.util.ArrayList;
import java.util.Collection;

import com.briup.smart.env.ConfigAImpl;
import com.briup.smart.env.Configuration;
import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.client.GatherImpl;
import com.briup.smart.env.client.GatherImplClient;
import com.briup.smart.env.entity.Environment;

//客户端入口类
public class ClientMain {
	public static void main(String[] args) {
		
		Configuration config = ConfigAImpl.getInstance();
		
		
		try {
			Gather gather = config.getGather();
			Client client = config.getClient();
			
			Collection<Environment> list=gather.gather();
			client.send(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
