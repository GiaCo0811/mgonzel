package web.api.mvc.controller;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.api.session.SessionValidation;

/**
 * Controller abstracto para las paginas estilo Front-end.
 * @author mgonzalez
 *
 */
public abstract class FrontEndControllerServlet extends AbstractControllerServlet {

	private static final long serialVersionUID = -8973262686871954774L;

	protected void execute(HttpServletRequest req, HttpServletResponse res) {
		
		//Atributos del request
		HashMap<String,Object> requestAttributes = new HashMap<String, Object>();
		
		HashMap<String,Object> parameters = loadRequestParameters(req);
		requestAttributes.put("parameters", parameters);
		
		//Contexto
		
		//carga de Cookies
		HashMap<String, String> cookies = loadCookies(req); 
		requestAttributes.put("cookies", cookies);

		//validacion de sesion
		if (getValidated(req)){
			boolean sessionValidated = validateSession(cookies);
			
			if (!sessionValidated){
				//showLoginView(req, res);
				return;
			}
		}
		
		try {
			executeView(req,res,requestAttributes);
		} catch (Exception e){
			showExceptionView(req,res, requestAttributes, e);
		}

	}
	
	private void showExceptionView(HttpServletRequest req,
			HttpServletResponse res, HashMap<String, Object> requestAttributes, Exception e) {
		try { 
			PrintWriter w = res.getWriter();
			w.println("Exception:");
			StackTraceElement []stacks = e.getStackTrace();
			for (StackTraceElement stack : stacks){
				w.println(stack);
			}
			e.printStackTrace();
		}catch (Exception we){
			System.out.println("Error fatal, no puede escribir en Response");
		}
		
	}

	protected abstract void executeView(HttpServletRequest req, HttpServletResponse res,
			HashMap<String, Object> requestAttributes) throws Exception;

	private boolean validateSession(HashMap<String, String> cookies) {
		String [] login = ((String)cookies.get("user")).split("-");
		int userId = Integer.parseInt(login[0]);
		long hash = Long.parseLong(login[1]);
		return SessionValidation.validateSession(userId,hash);
	}

	/**
	 * Segun el request, dice si debe validarse la sesion 
	 * @param req
	 * @return por default = false, o sobreescribir metodo.
	 */
	protected boolean getValidated(HttpServletRequest req){
		return false;
	}

}
