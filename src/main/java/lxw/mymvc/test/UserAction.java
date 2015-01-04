package lxw.mymvc.test;

import lxw.mymvc.annotation.Action;
import lxw.mymvc.servlet.mvc.ActionForm;
import lxw.mymvc.servlet.mvc.Controller;

@Action(value="/user", 
	actionForm="lxw.mymvc.test.User", 
	forwardName={"success", "failed"},
	forwardUrl={"/WEB-INF/view/success.jsp", "/WEB-INF/view/failed.jsp"}
		)
public class UserAction implements Controller {

	public String handleRequest(ActionForm form) {
		return "failed";
	}
	
}
