package com.briup.smart.env.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.briup.smart.env.entity.Environment;

class GatherImplTest {

	@Test
	void test() {
		GatherImpl g=new GatherImpl();
		Collection<Environment> list = null;
		try {
			list = g.gather();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		list.forEach(System.out::println);
//		int i=0;
//		for (Environment e : list) {
//			i++;
//			System.out.print(i);
//			System.out.println(e);
//		}
	}

}
