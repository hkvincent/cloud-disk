package com.vincent.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String uri = request.getRequestURI();
		boolean flag = false;
		if (uri.contains(".css") || uri.contains(".js") || uri.contains(".png")
				|| uri.contains(".jpg")) {
			// 如果发现是css或者js文件，直接放行
			filterChain.doFilter(request, response);
			return;
		}
		if (!uri.contains("login") && !uri.contains("UserServlet")) {
			// get the session object if user's browser have sessionID of
			// cookies
			HttpSession session = request.getSession(false);
			// not null is saying login before
			if (session != null) {
				// because one session object can have many attributes
				// so we need to get specify one to validate.
				Object user = session.getAttribute("user");
				if (user != null) {
					filterChain.doFilter(request, response);
				} else {
					// if user is null,just say session object have
					// no this
					// attribute
					// this mean user has no login before this
					// login,but do some
					// action in out website before
					uri = "/login.jsp";
					request.getRequestDispatcher(uri)
							.forward(request, response);
				}
			} else {
				uri = "/login.jsp";
				request.getRequestDispatcher(uri).forward(request, response);
			}
		} else {
			filterChain.doFilter(request, response);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
