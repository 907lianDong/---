package com.briup.smart.env.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;


import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.BackupImpl;
import com.briup.smart.env.util.Log;
/**
 * 入库模块：将服务器接收到的集合对象保存到oracle数据中
 * @author fengliandong
 *
 */
public class GatherImplDBStore implements DBStore,ConfigurationAware,PropertiesAware{

	private Backup backup;
	private Log log ;
	//批处理条数
	private int batchNum;
	
	//备份文件的路径名
	private String fileName;
	@Override
	public void saveDB(Collection<Environment> c) throws Exception {
		
		
		PreparedStatement ps = null;
		//获取数据库连接对象
		Connection connection = GetConn.getConn();
		//设置不自动提交事务
		connection.setAutoCommit(false);
		//记录批处理的次数
		int count = 0;
		//记录前一天
		int yesterDay = 0;
		//记录总数据数
		
		Object load = backup.load(fileName, true);
		//判断备份文件是否存在，存在时先读取备份文件中的内容
		if(load!=null) {
			Collection<Environment> backupColl=(Collection<Environment>) load;
			//将backupColl添加到集合c中
			c.addAll(backupColl);
		}
		
		int sum = 0;
		BackupImpl bu=new BackupImpl();
		//遍历集合，得到每一个环境对象
		try {
			for (Environment e : c) {
				
				//获取采集时间对应的天数
				int day = e.getGather_date().toLocalDateTime().getDayOfMonth();
				if(yesterDay != day) {
//					System.out.println("sum:"+sum);
					log.info("sum:"+sum);
//					System.out.println("day:"+day);
					log.info("day:"+day);
					yesterDay = day;
					//处理前一天批处理后剩余的数据
					if(ps != null) {
						ps.executeBatch();
						ps.clearBatch();
						ps.close();
					}
					// 每一天创建一个对应PrepareStatement对象去处理
					String sql = "insert into e_detail_"+day+" values(?,?,?,?,?,?,?,?,?,?)";
					ps = connection.prepareStatement(sql);
				}
				//动态化设置参数
				ps.setString(1, e.getName());
				ps.setString(2, e.getSrcId());
				ps.setString(3, e.getDesId());
				ps.setString(4, e.getDevId());
				ps.setString(5, e.getSersorAddress());
				ps.setInt(6, e.getCount());
				ps.setString(7, e.getCmd());
				ps.setInt(8, e.getStatus());
				ps.setFloat(9, e.getData());
				ps.setTimestamp(10, e.getGather_date());
				//使用批处理
				ps.addBatch();
				sum++;
				
				if(sum==batchNum) {
//					throw new Exception("模拟入库时发生异常");
				}
				
				//设置批处理的条件
				if(sum % batchNum == 0) {
					count++;
					ps.executeBatch();
					ps.clearBatch();
				}
			}
			log.info("批处理了"+count+"次");
			//处理最后一天批处理剩余的数据
			ps.executeBatch();
			ps.clearBatch();
			//手动提交事务
			connection.commit();
			log.info("成功入库："+c.size()+"条数据");
				
		} catch (Exception e) {
//			connection.rollback();
			//发生异常的时候进行数据备份
			backup.store(fileName, c, false);
			e.printStackTrace();
		}finally {
			GetConn.endConn(connection);
		}
			
			
//		System.out.println("数据库插入成功");
		log.info("数据库插入成功");
	}
	@Override
	public void init(Properties properties) throws Exception {
		batchNum = Integer.valueOf(properties.getProperty("batchNum"));
		fileName = properties.getProperty("backFileName");
	}
	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		log=configuration.getLogger();
		backup=configuration.getBackup();
	}

}
