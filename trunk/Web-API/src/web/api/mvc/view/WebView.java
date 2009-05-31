package web.api.mvc.view;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.api.mvc.model.Model;

public abstract class WebView implements View {
	protected HttpServletRequest req;
	protected HttpServletResponse res;
	protected HashMap<String, Object> requestAttributes;

	protected Model model;

	protected void setModel(Model model){
		this.model = model;
	}

	public WebView(HttpServletRequest req, HttpServletResponse res,
			HashMap<String,Object> requestAttributes) {
		this.req = req;
		this.res = res;
		this.requestAttributes = requestAttributes;
	}

	protected void execute(){
		doHttpHeader();
		doHtmlHeader();
		doHtmlMenu();
		doHtmlBody();
		doHtmlFooter();
	}
	protected void doHttpHeader(){
		setContentType();
		setCacheControl();
	}
	protected abstract void doHtmlHeader();
	protected abstract void doHtmlMenu();
	protected abstract void doHtmlBody();
	protected abstract void doHtmlFooter();
	
	protected void setContentType(){
		res.setContentType("text/html");
	}
	protected void setCacheControl(){
		res.setHeader("Cache-control", "no-cache");
	}

}
