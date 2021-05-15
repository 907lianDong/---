package com.briup.smart.env.client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.briup.smart.env.server.GatherImplServer;

class GatherImplServerTest {

	@Test
	void test() {
		GatherImplServer server = new GatherImplServer();
		try {
			server.reciver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
