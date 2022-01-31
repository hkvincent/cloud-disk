package com.vincent.File.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ProgressServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		//从session中获取上传进度信息
		fileUploadStatus status = (fileUploadStatus) session
				.getAttribute("status");
		try {
			response.reset();
			//输出到访问这个方法的前端方法
			response.getWriter().write(
					"{\"pBytesRead\":" + status.getPBytesRead()
							+ ",\"pContentLength\":"
							+ status.getPContentLength() + "}");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		this.doPost(request, response);

	}
}
