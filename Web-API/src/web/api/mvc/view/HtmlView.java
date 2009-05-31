package web.api.mvc.view;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.api.cache.HtmlCache;
import web.api.cache.key.HtmlCacheKey;

public abstract class HtmlView extends WebView {


	public HtmlView(HttpServletRequest req, HttpServletResponse res,
			HashMap<String,Object> requestAttributes) throws Exception {
		super(req,res,requestAttributes);
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
