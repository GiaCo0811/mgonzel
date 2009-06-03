package web.api.mvc.view;

import web.api.mvc.model.Model;

/**
 * Marker interface view
 * @author mgonzalez
 *
 */
public interface View {
	public void execute() throws Exception;
	public void setModel(Model model);
}
