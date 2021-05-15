package com.briup.smart.env.client;


import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

public class LoggerTest {
	@Test
	public void loggerTest1() {
		//获取日志记录器
		Logger rootlogger = Logger.getRootLogger();
		//方式二
		Logger.getLogger(LoggerTest.class);
		//方式三
		Logger.getLogger(LoggerTest.class);
		
		rootlogger.debug("debug");
		rootlogger.info("info");
		rootlogger.warn("warn");
		rootlogger.error("error");
		rootlogger.fatal("fatal");
	}

}
