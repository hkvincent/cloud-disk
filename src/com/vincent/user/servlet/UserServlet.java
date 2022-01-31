package com.vincent.user.servlet;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vincent.Util.Constant;
import com.vincent.Util.Tool;
import com.vincent.user.domain.User;
import com.vincent.user.service.UserService;


import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	private String checkCode;

	public String login(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("userName");
		String password = request.getParameter("password");
		User user = userService.findUserByNameAndPassword(name, password);
		if (user != null) {
			request.getSession().setAttribute("user", user);
			double percentSpace = Constant.updateFileSize(user);
			request.getSession().setAttribute("percentSpace", percentSpace);
			return "f:/index.jsp";
		}
		request.setAttribute("msg", "username or password error");
		return "f:/login.jsp";
	}

	public String register(HttpServletRequest request,
			HttpServletResponse response) {
		String uid = Tool.randomId(10);
		String name = request.getParameter("userName");
		String password = request.getParameter("password");
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		String cId = Tool.randomId();
		String role = "normal";
		String fileSize = "0";
		User user = new User(uid, name, password, cId, dateString, role,
				fileSize);
		boolean createUser = userService.createUser(user);
		if (createUser) {
			request.getSession().setAttribute("user", user);
			double percentSpace = Constant.updateFileSize(user);
			request.getSession().setAttribute("percentSpace", percentSpace);
			return "r:/index.jsp";
		}
		return "r:/error.jsp";

	}

	public String quit(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("user");
		request.getSession().invalidate();
		return "r:/login.jsp";
	}

	public String check(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println(checkCode);
		String tip = "images/MsgError.png";
		HttpSession session = request.getSession();
		String code = (String) session.getAttribute("CHECKNUM");
		System.out.println("code " + code);
		this.checkCode = request.getParameter("checkCode");
		if (checkCode.equals(code)) {
			tip = "images/MsgSent.png";
		}

		// get response
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(tip);
		writer.flush();
		writer.close();
		return null;

	}
}
