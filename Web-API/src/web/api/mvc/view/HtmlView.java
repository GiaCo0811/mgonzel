package web.api.mvc.view;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class HtmlView extends WebView {


	public HtmlView(HttpServletRequest req, HttpServletResponse res,
			HashMap<String,Object> requestAttributes) {
		super(req,res,requestAttributes);
	}

	@Override
	protected void doHtmlFooter() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doHtmlHeader() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doHtmlMenu() {
		// TODO Auto-generated method stub

	}

}
