package com.briup.smart.env.util;

import org.apache.log4j.Logger;

public class LogImpl implements Log{

	Logger log=Logger.getRootLogger();
	
	@Override
	public void debug(String message) {
		log.debug(message);
		
	}

	@Override
	public void info(String message) {
		log.info(message);
		
	}

	@Override
	public void warn(String message) {
		log.warn(message);
		
	}

	@Override
	public void error(String message) {
		log.error(message);
		
	}

	@Override
	public void fatal(String message) {
		log.fatal(message);
		
	}

}
