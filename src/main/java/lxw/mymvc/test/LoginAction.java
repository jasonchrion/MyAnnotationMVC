package lxw.mymvc.test;

import lxw.mymvc.servlet.mvc.ActionForm;
import lxw.mymvc.servlet.mvc.Controller;

public class LoginAction implements Controller {

	@Override
	public String handleRequest(ActionForm form) {
		return "failed";
	}
	
}
