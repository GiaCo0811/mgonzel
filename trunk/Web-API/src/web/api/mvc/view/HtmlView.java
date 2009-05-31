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
		// TODO Auto-generated method stub

	}

	@Override
	protected void doHtmlHeader() {

	}

	@Override
	protected void doHtmlMenu() {
		HtmlCache cache = HtmlCache.getInstance();
		
		out.println(cache.get(new HtmlCacheKey("MenuLaRematada")));

	}

}
