package web.api.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller abstracto para las paginas del estilo back-end
 * @author mgonzalez
 *
 */
public abstract class BackEndControllerServlet extends
		AbstractControllerServlet {

	private static final long serialVersionUID = 3415759297132343885L;

	@Override
	protected void execute(HttpServletRequest req, HttpServletResponse res) {
		//Contexto
		//Cookies
		//sesion
		//validacion de sesion
		//permiso de administracion

	}

}
