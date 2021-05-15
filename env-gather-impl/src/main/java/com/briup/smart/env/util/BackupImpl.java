package com.briup.smart.env.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * 备份模块：在需要数据进行备份的时候进行备份
 * 1.在入库时可能发生异常，需要进行备份
 * 2.模拟实时采集数据
 * @author fengliandong
 *
 */
public class BackupImpl implements Backup{

	/**
	 * 读取备份文件,返回集合对象
	 */
	@Override
	public Object load(String fileName, boolean del) throws Exception {
		File file = new File(fileName);
		//判断文件是否存在
		if(!file.exists()) {
			return null;
		}
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
		//从备份文件中读取数据
		Object object = ois.readObject();
		//先进行关闭流操作，防止先用导致文件不能删除
		if(ois!=null) {
			ois.close();
		}
		//将备份文件删除
		if(del) {
			file.delete();
		}
		
		return object;
	}

	/**
	 * 将需要备份的集合对象写入到备份文件
	 */
	@Override
	public void store(String fileName, Object obj, boolean append) throws Exception {
		File file =new File(fileName);
		if(!file.exists()) {
			file.createNewFile();
		}
		ObjectOutputStream obs=new ObjectOutputStream(new FileOutputStream(fileName,append));
		obs.writeObject(obj);
		obs.flush();
		if(obs!=null) {
			obs.close();
		}
		
		
	}

}
