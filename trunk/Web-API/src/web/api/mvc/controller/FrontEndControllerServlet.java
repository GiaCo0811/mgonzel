package web.api.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller abstracto para las paginas estilo Front-end.
 * @author mgonzalez
 *
 */
public abstract class FrontEndControllerServlet extends AbstractControllerServlet {

	private static final long serialVersionUID = -8973262686871954774L;

	protected void execute(HttpServletRequest req, HttpServletResponse res){
		//contexto
		//Cookies
		//sesion
		//validacion de sesion / pagina
	}
}
