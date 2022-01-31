package com.vincent.Catalog.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vincent.Catalog.domain.Catalog;
import com.vincent.Catalog.service.CatalogService;
import com.vincent.File.domain.File;
import com.vincent.File.service.FileService;
import com.vincent.Util.Tool;
import com.vincent.user.domain.User;

import cn.itcast.servlet.BaseServlet;
import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class CatalogServlet extends BaseServlet {
    private CatalogService catalogService = new CatalogService();
    ThreadLocal<CatalogService> threadList = new ThreadLocal<CatalogService>();
    private FileService fileService = new FileService();
    ThreadLocal<FileService> fileServiceList = new ThreadLocal<FileService>();

    {
        threadList.set(catalogService);
        fileServiceList.set(fileService);
    }

    @SuppressWarnings("unchecked")
    public String myCatalog(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {
        catalogService = threadList.get() == null ? new CatalogService() : threadList
                .get();

        //面包屑的指引栏
        LinkedHashSet<Catalog> path = (LinkedHashSet<Catalog>) request
                .getSession().getAttribute("path");
        if (path == null) {
            path = new LinkedHashSet<Catalog>();
            request.getSession().setAttribute("path", path);
        }
        //直接进入Home page的时候进行验证
        String cid = request.getParameter("cid");
        if (cid == null || cid.isEmpty()) {
            User user = (User) request.getSession().getAttribute("user");
            cid = user.getcId();
        }
        Catalog c = catalogService.findByCidToCatalog(cid);
        // Catalog parent = c.getParent();
        /*
         * while (parent != null) { path.add(parent); parent =
         * parent.getParent(); }
         */
        if (!c.getcName().equals("root")) {
            path.add(c);
        } else {
            path.removeAll(path);
        }
        //首先将path变为一个迭代器
        Iterator<Catalog> iterator = path.iterator();
        //在创建一个新的面包屑指引栏set
        path = new LinkedHashSet<Catalog>();
        while (iterator.hasNext()) {
            Catalog next = iterator.next();
            path.add(next);
            //判断文件夹的名称是否相同，是的话就跳过不加入到面包屑
            if (c.getcName().equals(next.getcName())) {
                break;
            }
        }

        request.getSession().setAttribute("path", path);

        request.setAttribute("catalog", c);
        return "f:/home.jsp";
    }

    // 创建文件夹
    public String createCatalog(HttpServletRequest request,
                                HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        catalogService = threadList.get() == null ? new CatalogService() : threadList
                .get();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }

        String cid = request.getParameter("cid");// 拿到他的父cid
        String cName = request.getParameter("name");
        cName = java.net.URLDecoder.decode(cName, "UTF-8");

        Catalog parent = catalogService.findByCid(cid);

        Catalog c = new Catalog();
        // 开始封装文件夹的信息，然后存入到数据库
        String randomId = Tool.randomId();
        c.setcId(randomId);
        c.setcF(randomId);
        if (parent != null)
            c.setParent(parent);
        c.setcName(cName);
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
        String newTime = simp.format(new Date()).toString();
        c.setcDate(newTime);
        c.setIsShare("0");
        c.setuId(user.getuId());
        c.setcLevel(parent.getcLevel() + 1);//将准备要创建的文件夹深度+1
        catalogService.createCatalog(c);
        // 然后在转发到当前目录下
        /*
         * 这有2种方案，1 是用ajax创建文件夹 2是用servlet创建 先用2 ，
         */
        return "r:/CatalogServlet?method=myCatalog&cid=" + cid;
    }

    public String deleteCatalog(HttpServletRequest request,
                                HttpServletResponse response) throws ServletException, IOException {
        //线程安全地获取CatalogService
        catalogService = threadList.get() == null ? new CatalogService() : threadList
                .get();
        PrintWriter out = response.getWriter();
        String cid = request.getParameter("cid");
        String pid = request.getParameter("pid");
        String name = request.getParameter("name");
        if (name.equals("root")) {
            out.write("<script>alert('can not delete root');window.location.href='/mydisks/CatalogServlet?method=myCatalog&cid="
                    + cid + "'</script>");
            return null;
        }
        catalogService.deleteByCatalog(cid, this.getServletContext());
        return "r:/CatalogServlet?method=myCatalog&cid=" + pid;

    }

    public String findCatalogByPid(HttpServletRequest request,
                                   HttpServletResponse response) throws ServletException, IOException {
        catalogService = threadList.get() == null ? new CatalogService() : threadList
                .get();
        return null;
    }

    public String changeName(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException, IOException {
        catalogService = threadList.get() == null ? new CatalogService() : threadList
                .get();
        String cid = request.getParameter("cid");
        String pid = request.getParameter("pid");
        String name = request.getParameter("name");
        catalogService.updateNameByCid(cid, name);

        return "r:/CatalogServlet?method=myCatalog&cid=" + pid;
    }

    //移动文件
    public String moveCatalog(HttpServletRequest request,
                              HttpServletResponse response) throws ServletException, IOException {
        catalogService = threadList.get() == null ? new CatalogService() : threadList
                .get();
        String method = request.getParameter("do");
        String cid = null;
        String fid = null;
        boolean fileType = false;

        if (method.equals("get")) {
            if (request.getParameter("type").equals("folder")) {
                cid = request.getParameter("cid");
            } else if (request.getParameter("type").equals("file")) {
                fid = request.getParameter("fid");
                fileType = true;
            }
        } else if (method.equals("post")) {
            if (request.getParameter("type").equals("folder")) {
                cid = request.getParameter("cid");
            } else if (request.getParameter("type").equals("file")) {
                fid = request.getParameter("fid");
                fileType = true;
            }
        }

        HttpSession session = request.getSession();
        if (method.equals("get")) {
            String id = cid == null ? fid : cid;

            User user = (User) session.getAttribute("user");
            List<Catalog> userCatalogs = catalogService.getUserCatalog(user.getuId());

            if (!fileType) {
               /* userCatalogs = userCatalogs.stream().filter((e) -> {
                    return !id.equals(e.getcId());
                }).collect(Collectors.toList());*/
                Optional<Catalog> first = userCatalogs.stream().filter((e) -> {
                    return id.equals(e.getcId());
                }).findFirst();
                Catalog catalog = first.get();
                catalog.setHighlight(true);
                catalog.getParent().setHighlight(true);
                hightlightChildren(catalog.getChildren());
            }

            request.setAttribute("catalogList", userCatalogs);
            request.setAttribute("id", id);
            request.setAttribute("fileType", fileType);
            return "f:/FolderList.jsp";
        } else {
            String id = cid == null ? fid : cid;
            User user = (User) session.getAttribute("user");
            FileService fileService = fileServiceList.get() == null ? this.fileService : fileServiceList.get();
            fileServiceList.set(fileService);
            Catalog byCid = null;
            File byFid = null;
            if (request.getParameter("type").equals("folder")) {
                byCid = catalogService.findByCid(id);
            } else if (request.getParameter("type").equals("file")) {
                byFid = fileService.findByFid(id);
                fileType = true;
            }

            if (!fileType) {
                if (!byCid.getuId().equals(user.getuId())) {
                    return "r:/CatalogServlet?method=myCatalog";
                }
            } else {
                if (!byFid.getCatalog().getuId().equals(user.getuId())) {
                    return "r:/CatalogServlet?method=myCatalog";
                }
            }

            String mcid = request.getParameter("mcid");

            if (fileType && StringUtils.isNotEmpty(mcid)) {
                boolean move = catalogService.move(mcid, fid);
                return move ? "r:/CatalogServlet?method=myCatalog&cid=" + mcid : "r:/CatalogServlet?method=myCatalog";
            } else if (!fileType && StringUtils.isNotEmpty(mcid)) {
                Catalog mcidModel = catalogService.findByCid(mcid);
                boolean move = catalogService.moveFolder(mcid, cid, mcidModel.getcLevel() + 1, user.getuId());
                return move ? "r:/CatalogServlet?method=myCatalog&cid=" + mcid : "r:/CatalogServlet?method=myCatalog";
            }
            return "r:/CatalogServlet?method=myCatalog";
        }
    }

    private void hightlightChildren(List<Catalog> userCatalogs) {
        userCatalogs.forEach((e) -> {
            e.setHighlight(true);
            if (!e.getChildren().isEmpty()) {
                //pass current node id to do the recursive again.
                hightlightChildren(e.getChildren());
            }
        });
    }
}
