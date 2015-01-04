package lxw.mymvc.servlet.mvc;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SimpleUrlHandlerMapping implements HandlerMapping {

	public static Map<String, UrlMapping> handleUrlMapping(String path) {
		SAXReader saxReader = new SAXReader();
		Map<String, UrlMapping> map = new HashMap<String, UrlMapping>();
		try {
			Document document = saxReader.read(new File(path));
			Element root = document.getRootElement();

			Iterator<?> itAction = root.elementIterator("action");
			while (itAction.hasNext()) {
				UrlMapping mvcBean = new UrlMapping();
				Element actionEle = (Element) itAction.next();
				String entityName = actionEle.attributeValue("entityName");
				String actionClass = actionEle.attributeValue("class");
				String actionPath = actionEle.attributeValue("path");
				mvcBean.setEntityName(entityName);
				mvcBean.setActionClass(actionClass);
				mvcBean.setActionPath(actionPath);

				Map<String, String> forwardMap = new HashMap<String, String>();
				Iterator<?> itForward = actionEle.elementIterator("forward");
				while (itForward.hasNext()) {
					Element forwardEle = (Element) itForward.next();
					forwardMap.put(forwardEle.attributeValue("name"),
							forwardEle.attributeValue("value"));
				}
				mvcBean.setForward(forwardMap);

				Iterator<?> itEntitys = root.elementIterator("entitys");
				while (itEntitys.hasNext()) {
					Element entitysEle = (Element) itEntitys.next();
					Iterator<?> itEntity = entitysEle.elementIterator("entity");
					while (itEntity.hasNext()) {
						Element entityEle = (Element) itEntity.next();
						if (entityEle.attributeValue("name").equals(entityName)) {
							String entityClass = entityEle
									.attributeValue("class");
							mvcBean.setEntityClass(entityClass);
						}
					}
				}

				map.put(actionPath, mvcBean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
