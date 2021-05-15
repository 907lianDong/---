package com.briup.smart.env.client;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.briup.smart.env.entity.Environment;

class GatherImplClientTest {

	@Test
	void test() {
		GatherImplClient clientTest = new GatherImplClient();
		Collection<Environment> list=new ArrayList<>();
		try {
			clientTest.send(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
