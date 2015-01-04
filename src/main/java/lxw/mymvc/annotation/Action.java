package lxw.mymvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
	//映射地址
	String value();
	
	//form类
	String actionForm();
	
	//转发name
	String[] forwardName();
	
	//转发url
	String[] forwardUrl();
}
