package lxw.mymvc.servlet.mvc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lxw.mymvc.annotation.Action;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class AnnotationHandlerMapping implements HandlerMapping {

	public static Map<String, UrlMapping> handleUrlMapping(String path) {
		String basePackage = "";
		SAXReader saxReader = new SAXReader();
		Map<String, UrlMapping> map = new HashMap<String, UrlMapping>();
		try {
			Document document = saxReader.read(new File(path));
			Element root = document.getRootElement();
			
			Iterator<?> scan = root.elementIterator("scan");
			while (scan.hasNext()) {
				Element pack = (Element) scan.next();
				basePackage = pack.attributeValue("base-package");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//扫描包下所有类
		if (basePackage != null && basePackage.length() > 0) {
			List<String> scanList = scanPackage(basePackage);
			for(String className : scanList){
				UrlMapping mvcBean = new UrlMapping();
				try {
					Class<?> clazz = Class.forName(className);
					if(clazz.isAnnotationPresent(Action.class)){
						Action action = clazz.getAnnotation(Action.class);
						String entityName = action.value();
						String actionClass = className;
						String actionPath = action.value();
						String entityClass = action.actionForm();
						mvcBean.setEntityName(entityName);
						mvcBean.setActionClass(actionClass);
						mvcBean.setActionPath(actionPath);
						mvcBean.setEntityClass(entityClass);
						
						String[] forwardName = action.forwardName(); 
						String[] forwardUrl = action.forwardUrl(); 
						Map<String, String> forwardMap = new HashMap<String, String>();
						for (int i = 0; i < forwardName.length; i++) {
							forwardMap.put(forwardName[i], forwardUrl[i]);
						}
						mvcBean.setForward(forwardMap);
						
						map.put(actionPath, mvcBean);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}	
		}
		return map;
	}

	private static List<String> scanPackage(String basePackage) {
		List<String> classesResult = new ArrayList<String>();
		//获得上下文的类加载器

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		String path = basePackage.replace(".", "/");
		//查找具有给定名称的资源,资源名称是以 '/' 分隔的标识资源的路径名称。
		URL url = classLoader.getResource(path); 
		
		//获得要扫描的路径
		String filepath = url.getPath();
		
		//获得要扫描的文件
		File file = new File(filepath);
		
		//获得子文件
		File[] files = file.listFiles();
	
		for(File childfile:files){
			//如果是文件夹递归调用
			if(childfile.isDirectory()){
				String childpackagepath = basePackage+"."+childfile.getName();

				classesResult.addAll(scanPackage(childpackagepath));
			}else{
				//获得类的全路径名
				classesResult.add(basePackage+"."+childfile.getName().replace(".class", ""));
			}
		}
		
		return classesResult;
	}
	
	public static void main(String[] args) {
		List<String> actionclass = scanPackage("lxw.mymvc.test");
		for(String classname:actionclass){
			System.out.println(classname);
		}
	}

}