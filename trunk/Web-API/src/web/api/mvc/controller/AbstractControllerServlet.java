package web.api.mvc.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller Abstracto. Debe ser stateless.
 * @author mgonzalez
 *
 */
public abstract class AbstractControllerServlet extends HttpServlet implements Controller {

	private static final long serialVersionUID = 7810846794288770467L;

	public static String COOKIES = "cookies";
	public static String PARAMETERS = "parameters";
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp);
	}
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	
	protected abstract void execute(HttpServletRequest req, HttpServletResponse res);
	
	/**
	 * Carga los parametros de la pagina, incluyendo los enviados por URL.
	 * @param req
	 * @return
	 */
	protected final HashMap<String, Object> loadRequestParameters(HttpServletRequest req) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		Map reqParameters = req.getParameterMap();
		Iterator paramIter = reqParameters.keySet().iterator(); 
		while (paramIter.hasNext()){
			String key = (String)paramIter.next();
			
			parameters.put(key, reqParameters.get(key));
		}
		
		return parameters;
	}
	
	/**
	 * Carga las cookies de la pagina.
	 * @param req
	 * @return
	 */
	protected final HashMap<String,String> loadCookies(HttpServletRequest req){
		HashMap<String, String> cookies = new HashMap<String, String>();
		
		Cookie [] arCookie = req.getCookies();
		
		for (Cookie cookie : arCookie){
			cookies.put(cookie.getName(), cookie.getValue());
		}
		return cookies;	
	}
	

}
