package com.vincent.File.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.vincent.Catalog.domain.Catalog;
import com.vincent.Catalog.service.CatalogService;
import com.vincent.File.service.FileService;
import com.vincent.Util.Constant;
import com.vincent.Util.Tool;
import com.vincent.user.domain.User;
import com.vincent.user.service.UserService;

public class UploadServlet extends HttpServlet {

	private FileService fileservice = new FileService();
	private CatalogService catalogservice = new CatalogService();
	private UserService userService = new UserService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 如果要实现不同用户不同文件夹，这里需要拿到user用户
		PrintWriter out = resp.getWriter();
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		User user = (User) req.getSession().getAttribute("user");

		// 插件工厂
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1024 * 1024 * 1000);// 50M
		upload.setHeaderEncoding("utf-8");
		myProgressListener getBarListener = new myProgressListener(req);
		// 不停调用监听器，更新监听器参数
		upload.setProgressListener(getBarListener);
		List<FileItem> fileList = null;
		try {
			fileList = upload.parseRequest(req);
		} catch (FileUploadException e2) {
			out.write("<script>alert('over size');"
					+ "window.history.back(-1);" + "</script>");
			e2.printStackTrace();
			try {
				throw e2;
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
			return;
		}

		// get hashcode of the file
		String hash = null;
		for (FileItem file : fileList) {
			if (file.getContentType() != null) {
				// FileItem file = fileList.get(0);
				User tempUser = userService.findUserByUid(user.getuId());
				long homeSpace = Constant.getRoleSize(user);
				long usedSpace = Long.parseLong(tempUser.getFileSize());
				if ((homeSpace - usedSpace) < file.getSize()) {
					out.write("<script>alert('no enough space');"
							+ "window.history.back(-1);" + "</script>");
					return;
				}
				String cid = fileList.get(fileList.size() - 1).getString(
						"utf-8");
				try {
					hash = Tool.getMD5Checksum(file.getInputStream());
					// 得到了hash 应该去数据库查询一下 如果已经存在就不上传了
					com.vincent.File.domain.File f = fileservice.findByHash(hash);
					if (f != null) {
						// 说明已经存在改文件了直接写入数据库中
						System.out.println("文件名:" + f.getfName());
						write2Database(user, cid, f);
						updateUserSession(req, user);
						out.write("<script>alert('upload done');window.location.href='/mydisks/CatalogServlet?method=myCatalog&cid="
								+ cid + "'</script>");
						return;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				String filename = file.getName();
				String filePrefix = null; // 文件名，不包含后缀
				String fileExt = null; // 后缀文件类型
				// 截图名字名 部分浏览器给的是绝对路径
				int indes = filename.lastIndexOf("\\");
				if (indes != -1) {
					filename = filename.substring(indes + 1);
				}
				int indes2 = filename.lastIndexOf(".");
				if (indes2 != -1) {
					filePrefix = filename.substring(0, indes2);
					fileExt = filename.substring(indes2 + 1);
				}
				// 开始上传文件 文件名重名问题需要解决 并且按用户分文件夹！
				String savePath = this.getServletContext().getRealPath(
						"/upload");
				// 看是否存在 不存在就要创建
				File path = new File(savePath);
				if (!path.exists()) {
					path.mkdirs();
				}
				String filename2 = filePrefix
						+ " "
						+ new SimpleDateFormat("hh-mm-ss").format(new Date())
								.toString() + "." + fileExt;

				File imgfile = new File(savePath, filename2); // 绝对路径的文件
				try {
					file.write(imgfile);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				write2Database(user, hash, file, cid, filename, fileExt,
						filename2);
				updateUserSession(req, user);
				// 数据表写完成了，
				// PrintWriter out=resp.getWriter();
				out.write("<script>alert('upload ok!');window.location.href='/mydisks/CatalogServlet?method=myCatalog&cid="
						+ cid + "'</script>");
			}
		}
	}

	/**
	 * the file is not exist before
	 * 
	 * @param user
	 * @param hash
	 * @param file
	 * @param cid
	 * @param filename
	 * @param fileExt
	 * @param filename2
	 */
	private void write2Database(User user, String hash, FileItem file,
			String cid, String filename, String fileExt, String filename2) {
		// 上传完成 开始写数据库保存
		com.vincent.File.domain.File myfile = new com.vincent.File.domain.File();
		myfile.setfPath("/upload/" + filename2);
		myfile.setfSize(file.getSize());
		myfile.setfType(fileExt);
		myfile.setfName(filename);
		myfile.setfHash(hash);
		myfile.setfDiskName(filename2);
		String fid = Tool.randomId(); // 文件id
		myfile.setfId(fid);
		myfile.setfDowncount("0");
		myfile.setfDesc("暂留");
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
		String date = simp.format(new Date()).toString();
		myfile.setfUploadtime(date);
		myfile.setIsShare("0");
		Catalog c = new Catalog();
		c.setcId(cid);
		myfile.setCatalog(c);
		fileservice.upLoadFile(myfile, user);// 文件表写好了还需要写2个表
		getCf(cid, fid);
	}

	/**
	 * the file is exist before
	 * 
	 * @param user
	 * @param cid
	 * @param f
	 */
	private void write2Database(User user, String cid,
			com.vincent.File.domain.File f) {
		com.vincent.File.domain.File myfile = new com.vincent.File.domain.File();
		myfile.setfPath(f.getfPath());
		myfile.setfSize(f.getfSize());
		myfile.setfType(f.getfType());
		myfile.setfName(f.getfName());
		myfile.setfHash("");
		myfile.setfDiskName(f.getfDiskName());
		String fid = Tool.randomId(); // 文件id
		myfile.setfId(fid);
		myfile.setfDowncount("0");
		myfile.setfDesc("暂留");
		SimpleDateFormat simp1 = new SimpleDateFormat("yyyy-MM-dd");
		String date = simp1.format(new Date()).toString();
		myfile.setfUploadtime(date);
		myfile.setIsShare("0");
		Catalog c1 = new Catalog();
		c1.setcId(cid);
		myfile.setCatalog(c1);
		fileservice.upLoadFile(myfile, user);
		getCf(cid, fid);
	}

	/**
	 * find the cf of the catalog
	 * 
	 * @param cid
	 * @param fid
	 */
	private void getCf(String cid, String fid) {
		String cf = catalogservice.cidTocf(cid);
		if (cf == null || cf.isEmpty() || "".equals(cf)) {
			// 说明文件夹下暂时没有文件
			String sCf = Tool.randomId();// 生成cf值
			catalogservice.intoCf(cid, sCf);
			// 开始写入fid值
			fileservice.writecf(fid, sCf);
		} else {
			fileservice.writecf(fid, cf);
		}
	}

	private void updateUserSession(HttpServletRequest request, User user) {
		User sessionUser = userService.findUserByUid(user.getuId());
		double updateFileSize = Constant.updateFileSize(sessionUser);
		request.getSession().setAttribute("percentSpace", updateFileSize);
	}

}
