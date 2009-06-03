package web.api.mvc.view;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.api.cache.HtmlCache;

public abstract class HtmlView extends WebView {


	public HtmlView(HttpServletRequest req, HttpServletResponse res,
			HashMap<String,Object> requestAttributes,ServletContext servletContext, HashMap<String,Object> requestParameters) throws Exception {
		super(req,res,requestAttributes,servletContext, requestParameters);
	}

	@Override
	protected void doHtmlFooter() {
		out.println(HtmlCache.getHtml("FooterDefault"));
	}

	@Override
	protected void doHtmlHeader() {
		out.println(HtmlCache.getHtml("HtmlHeaderDefault"));
	}

	@Override
	protected void doHtmlMenu() {
		out.println(HtmlCache.getHtml("MenuDefault"));
	}

}
