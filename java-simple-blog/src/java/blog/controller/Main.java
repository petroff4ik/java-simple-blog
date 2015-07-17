/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blog.controller;

import blog.system.ControllerIntf;
import blog.system.exception.Exception404;
import blog.tools.ParseUrl;
import blog.tools.ErrorPage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author petroff
 */
@WebServlet(name = "Main", loadOnStartup = 1, urlPatterns = {"/"})
public class Main extends HttpServlet {

	protected ErrorPage errorPage;
	final String modelPrefix = "Controller";
	final String path = "blog.controller.";
	final String mainServlet = "Main";

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception404 {
		errorPage = new ErrorPage(request, response);

		String servletPath = request.getServletPath();
		ParseUrl parseUrl = new ParseUrl(servletPath);

		String class_name = parseUrl.getClassUrl();
		String class_name_path = path + class_name + modelPrefix;
		Class c;
		try {
			c = Class.forName(class_name_path);
		} catch (ClassNotFoundException cN) {
			throw new Exception404();
		}
		Object obj;
		try {
			obj = c.newInstance();
		} catch (InstantiationException | IllegalAccessException ie) {
			throw new Exception404();
		}

		ControllerIntf base = (ControllerIntf) obj;
		base.init(request, response, errorPage);

		if (parseUrl.getMethodUrl() != null) {
			try {
				Method method = c.getMethod(parseUrl.getMethodUrl(), parseUrl.getParamTypesUrl());
				method.invoke(obj, parseUrl.getParamsUrl());
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				throw new Exception404();
			}

		} else {
			base.index();
		}

	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (ServletException ie) {
			throw new ServletException();
		} catch (IOException il) {
			throw new IOException();
		} catch (Exception404 e) {
			errorPage.show404(e);
		}

	}

	/**
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (ServletException ie) {
			throw new ServletException();
		} catch (IOException il) {
			throw new IOException();
		} catch (Exception404 e) {
			errorPage.show404(e);
		}
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Main controller router";
	}// </editor-fold>
}
