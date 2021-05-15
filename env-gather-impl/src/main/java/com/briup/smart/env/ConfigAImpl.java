package com.briup.smart.env;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.Log;
/**
 * 配置模块：负责初始化和配置其他模块
 * 
 * 流程：
 * 1.加载xml文件进行解析（dom4j）
 * 2.得到类的全限定名（存储在子节点的属性中）
 * 3.通过类的全限定名得到Class对象（反射）
 * 4.通过反射创建对饮模块的对象
 * 5.将创建的对象保存（Map集合）
 * 6.得到类中的变量值（存储在子节点的文本值中）
 * 7.将变量值保存（保存在properties文件中）
 * @author fengliandong
 *
 */
public class ConfigAImpl implements Configuration{

	//保存各个模块的实例对象
	private static Map<String,Object> map=new HashMap<>();
	//保存各个模块中的变量值
	private static Properties properties=new Properties();
	
	private final static Configuration CONFIG=new ConfigAImpl();
	
	static {
		//创建解析器对象
		SAXReader sax=new SAXReader();
		
		try {
			//加载xml文件，获取文档对象
			Document document = sax.read(new File("src/main/resources/conf.xml"));
			//获取根节点
			Element rootElement = document.getRootElement();
			//获取子节点
			List<Element> elements = rootElement.elements();
			//遍历子节点
			for (Element element : elements) {
				//得到子节点的名字
				String name = element.getName();
				//获取对应节点的属性
				Attribute attribute = element.attribute("class");
				//获取属性值
				String attvalue = attribute.getValue();
				//通过全限定名获得Class对象
				Class<?> clazz = Class.forName(attvalue);
				//通过反射中的newIstance方法创建对象
				Object object = clazz.newInstance();
				//将各模块的实例对象保存到map集合中
				map.put(name, object);
				List<Element> childElements = element.elements();
				for (Element childElement : childElements) {
					String childName = childElement.getName();
					String chiledText = childElement.getText();
					properties.setProperty(childName, chiledText);
				}
			}
			initModel();
//			map.forEach((k,v)->{
//				System.out.println(k+v);
//			});
			properties.forEach((k,v)->{
				System.out.println(k+":"+v);
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * 为其它模块进行初始化，调用各个模块中的init方法和setConfiguration
	 */
	public static void initModel() {
		//得到各个模块的实例对象
		Collection<Object> objects = map.values();
		for (Object object : objects) {
//			object.
			//判断是否实现了ConfihurationAware
			if(object instanceof ConfigurationAware) {
				try {
					((ConfigurationAware) object).setConfiguration(CONFIG);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//判断是否实现了PropertiesAware接口
			if(object instanceof PropertiesAware) {
				try {
					((PropertiesAware) object).init(properties);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Configuration getInstance() {
		return CONFIG;
		
	}
	
	//获取日志模块的实例
	@Override
	public Log getLogger() throws Exception {
		return (Log) map.get("logger");
	}

	//获取服务器端的实例
	@Override
	public Server getServer() throws Exception {
		return (Server) map.get("server");
	}

	//获取客户端的实例
	@Override
	public Client getClient() throws Exception {
		return (Client) map.get("client");
	}

	//获取入库模块的实例
	@Override
	public DBStore getDbStore() throws Exception {
		return (DBStore) map.get("dbStore");
	}

	//获取备份模块的实例
	@Override
	public Gather getGather() throws Exception {
		return (Gather) map.get("gather");
	}

	//获取采集模块的实例
	@Override
	public Backup getBackup() throws Exception {
		return (Backup) map.get("backup");
	}

	

	

}
