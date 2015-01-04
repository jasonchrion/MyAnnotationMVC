package lxw.mymvc.context;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lxw.mymvc.servlet.mvc.AnnotationHandlerMapping;
import lxw.mymvc.servlet.mvc.SimpleUrlHandlerMapping;
import lxw.mymvc.servlet.mvc.UrlMapping;


public class ContextLoaderListener implements ServletContextListener {

    public ContextLoaderListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
    	System.out.println("应用初始化：读取配置文件");
    	ServletContext context = sce.getServletContext();
		String xmlpath = context.getInitParameter("mvc-config");
		String tomcatpath =context.getRealPath("\\");
		Map<String, UrlMapping> urlMapping = SimpleUrlHandlerMapping.handleUrlMapping(tomcatpath+xmlpath);
		Map<String, UrlMapping> annotationMapping = AnnotationHandlerMapping.handleUrlMapping(tomcatpath+xmlpath);
		urlMapping.putAll(annotationMapping);
		context.setAttribute("mymvc", urlMapping);
		System.out.println("应用初始化完毕");
    }

    //应用
    public void contextDestroyed(ServletContextEvent sce) {
       
    }
	
}
