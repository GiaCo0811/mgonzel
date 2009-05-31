package test.web.bid;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.api.mvc.controller.FrontEndControllerServlet;
import web.api.mvc.view.View;

public class BidControllerServlet extends FrontEndControllerServlet {

	@Override
	protected void executeView(HttpServletRequest req, HttpServletResponse res,
			HashMap<String, Object> requestAttributes) throws Exception {


		View view = new BidHtmlView(req,res,requestAttributes);

		view.execute();
	}

}
