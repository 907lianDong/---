package com.briup.smart.env.client;

import java.awt.SecondaryLoop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.BackupImpl;
import com.briup.smart.env.util.Log;
/**
 * 作用：采集数据：对data-file文件中的数据进行处理 
 * @author fengliandong
 *
 */
public class GatherImpl implements Gather,ConfigurationAware,PropertiesAware{

	private Log log;
	
	private Backup backup;
	//保存备份文件的路径名
	String fileLengthName ;
		
	String dataFileName ;
		
	private int temperatureHumidityNum;
		
	private int illuminationNum;
		
	private int CO2Num;
	
	
	long length=0L;
	
	
	
	@Override
	public Collection<Environment> gather() throws Exception {
		File file=new File(dataFileName);
		BufferedReader br=new BufferedReader(new FileReader(file));
		
		//获取文件的字节数
		length=file.length();
		Object load = backup.load(fileLengthName, false);
		if(load!=null) {
			Long len=(Long) load+1;
			br.skip(len);
		}
		
		Environment evm ;
		Collection<Environment> list=new ArrayList<>();
		String rl;
		int n=0;
		while((rl=br.readLine())!=null) {
			evm=new Environment();
			String[] split = rl.split("[|]");
			evm.setSrcId(split[0]);
			evm.setDesId(split[1]);
			evm.setDevId(split[2]);
			evm.setSersorAddress(split[3]);
			evm.setCount(Integer.parseInt(split[4]));
			evm.setCmd(split[5]);
			evm.setStatus(Integer.parseInt(split[7]));
//			evm.setGather_date(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Double.parseDouble(split[8]))));
			evm.setGather_date(new Timestamp(Long.valueOf(split[8])));
			
			if(Integer.parseInt(split[3])==16){
				temperatureHumidityNum++;
				evm.setName("湿度");
				float data=(Integer.valueOf(split[6].substring(4, 8),16));
				evm.setData((data*0.00190735F)-6);
				
				Environment evm2=new Environment();
				evm2=new Environment();
				evm2.setSrcId(split[0]);
				evm2.setDesId(split[1]);
				evm2.setDevId(split[2]);
				evm2.setSersorAddress(split[3]);
				evm2.setCount(Integer.parseInt(split[4]));
				evm2.setCmd(split[5]);
				evm2.setName("温度");
				data=(Integer.valueOf(split[6].substring(0, 4),16));
				evm2.setData(data*(0.00268127F)-46.85F);
				evm2.setStatus(Integer.parseInt(split[7]));
//				evm2.setGather_date(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Double.parseDouble(split[8]))));
				evm2.setGather_date(new Timestamp(Long.valueOf(split[8])));
				list.add(evm2);
			}else if(Integer.parseInt(split[3])==256){
				illuminationNum++;
				evm.setName("光照强度");
				evm.setData((float)(Long.valueOf(split[6].substring(0, 4),16)));
			}else if(Integer.parseInt(split[3])==1280) {
				CO2Num++;
				evm.setName("二氧化碳");
				evm.setData((float)(Long.valueOf(split[6].substring(0, 4),16)));
			}
			list.add(evm);
		}
		log.info("采集模块采集数据");
		log.info("环境对象总条数:"+list.size());
		log.info("温度对象的数据条数："+temperatureHumidityNum);
		log.info("湿度对象的数据条数："+temperatureHumidityNum);
		log.info("光照强度对象的数据条数："+illuminationNum);
		log.info("CO2对象的数据条数"+CO2Num);
		//将文件的字节数保存到备份文件中
		backup.store(fileLengthName, length, false);
		return list;
	}



	@Override
	public void init(Properties properties) throws Exception {
		fileLengthName = properties.getProperty("fileLengthName");
		dataFileName = properties.getProperty("data-file-path");
		
	}



	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		backup = configuration.getBackup();
		log = configuration.getLogger();
		
	}
	
	

}
