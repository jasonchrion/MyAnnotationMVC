package lxw.mymvc.servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lxw.mymvc.servlet.mvc.ActionForm;
import lxw.mymvc.servlet.mvc.Controller;
import lxw.mymvc.servlet.mvc.UrlMapping;

public class DispatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DispatchServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected final void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1:读取解析配置文件
		String path = request.getServletPath();
		@SuppressWarnings("unchecked")
		Map<String, UrlMapping> map = (Map<String, UrlMapping>) request
				.getServletContext().getAttribute("mymvc");
		UrlMapping mvcBean = map.get(path);
		ActionForm form = getBean(mvcBean.getEntityClass(), request);
		Controller action = null;
		try {
			Class<?> class1 = Class.forName(mvcBean.getActionClass());
			action = (Controller) class1.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String str = action.handleRequest(form);
		String url = mvcBean.getForward().get(str);
		
		request.getRequestDispatcher(url).forward(request, response);
	}
	
	private ActionForm getBean(String className,HttpServletRequest request) {
		ActionForm o = null;
		try {
			Class<?> clazz = Class.forName(className);
			o = (ActionForm) clazz.newInstance();
			Field[] fields = clazz.getFields();
			for (Field field : fields) {
				field.setAccessible(true);
				field.set(o, request.getParameter(field.getName()));
				field.setAccessible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o ;
	}
}
