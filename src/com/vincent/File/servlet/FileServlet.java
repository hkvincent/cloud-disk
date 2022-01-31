package com.vincent.File.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vincent.File.domain.File;
import com.vincent.File.service.FileService;
import com.vincent.Util.Constant;
import com.vincent.user.domain.User;
import com.vincent.user.service.UserService;


import cn.itcast.servlet.BaseServlet;

public class FileServlet extends BaseServlet {

	private FileService fileService = new FileService();
	private UserService userService = new UserService();

	// 删除file ,暂时是单个文件 ，后面实现批量删除
	public String deleteFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String fid = request.getParameter("fid");
		// 从数据中删除记录，但是还有文件
		User user = (User) request.getSession().getAttribute("user");
		File f = fileService.findByFid(fid);
		String filePath = f.getfPath();
		if (("").equals(f.getfHash()) || f.getfHash().isEmpty()) {
			// 说明他是备份人家的 只删除数据库的信息
			fileService.deleteByFid(fid, user);
			response.getWriter().write("true");
			updateUserSession(request, user);

			return null;
		} else {
			List<File> fileList = fileService.findByPath(filePath);
			if (fileList.size() > 1) {// 存在其它用户使用这个文件，不能够物理删除 只删除数据库
				for (File myFile : fileList) {
					if (!f.getfId().equals(myFile.getfId())) {
						// 将文件hash值随便赋值给数据库相同的文件身上，那么下次再删除这个文件时候就不会出错
						fileService.upateHashByFid(myFile.getfId(),
								f.getfHash());
						// 从数据库中删除用户要删除的文件
						fileService.deleteByFid(fid, user);
						response.getWriter().write("true");
						updateUserSession(request, user);
						break;
					}
				}
				return null;
			} else {
				// 整个数据库只有这一份文件，那么就可以直接物理删除了
				try {
					java.io.File abf = new java.io.File(this
							.getServletContext().getRealPath(filePath));
					// 同时删除数据库上的记录
					fileService.deleteByFid(fid, user);
					abf.delete();
					updateUserSession(request, user);
				} catch (Exception e) {
					response.getWriter().write("false");
					return null;
				}
				response.getWriter().write("true");
				return null;
			}
		}
	}

	// 每次执行删除操作都需要更新用户还有多少空间
	private void updateUserSession(HttpServletRequest request, User user) {
		User sessionUser = userService.findUserByUid(user.getuId());
		double updateFileSize = Constant.updateFileSize(sessionUser);
		request.getSession().setAttribute("percentSpace", updateFileSize);
	}

	public String changeName(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fid = request.getParameter("fid");
		String name = request.getParameter("name");
		boolean result = fileService.updateNameByFid(fid, name);
		if (result) {
			response.getWriter().write("true");
			return null;
		} else {
			response.getWriter().write("false");
			return null;
		}

	}

}
