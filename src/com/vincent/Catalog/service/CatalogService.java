package com.vincent.Catalog.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import com.vincent.Catalog.dao.CatalogDao;
import com.vincent.Catalog.domain.Catalog;


public class CatalogService {
    private CatalogDao cDao = new CatalogDao();

    // 拿cid去查找他下面的一级文件夹已经文件
    public Catalog findByCidToCatalog(String cid) {
        try {
            return cDao.findByCidToCatalog(cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // 通过cid 封装catalog
    public Catalog findByCid(String cid) {
        try {
            return cDao.findByCid(cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createCatalog(Catalog c) {
        try {
            cDao.createCatalog(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String cidTocf(String cid) {
        try {
            return cDao.cidTocf(cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void intoCf(String cid, String cf) {
        try {
            cDao.intoCf(cid, cf);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteByCatalog(String cid, ServletContext context) {
        try {
            cDao.deleteByCatalog(cid, context);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateNameByCid(String cid, String name) {
        try {
            cDao.updateNameByCid(cid, name);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Catalog> tree(List<Catalog> list, String fahterId, List<Catalog> returnList, boolean rootFlag) {

        for (Catalog catalog : list) {
            //root branch, insert the root node in to list first
            if (catalog.getParent() == null && !rootFlag) {
                rootFlag = true;
                if (catalog.getcId().equals(fahterId)) {
                    returnList.add(catalog);
                }
            }
            //non root branch, retrieve parent from current node, and compare current node is same with argument fahterId
            if (catalog.getParent() != null
                    && catalog.getParent().getcId().equals(fahterId)) {
                returnList.add(catalog);
                //find the father from children
                Optional<Catalog> any = catalog.getParent().getChildren().stream()
                        .filter((e) -> e.getcId().equals(catalog.getcId())).findAny();
                if (!any.isPresent()) {
                    catalog.getParent().getChildren().add(catalog);
                }
                //pass current node id and returnList to do the recursive again.
                tree(list, catalog.getcId(), returnList, rootFlag);
            }
        }

        return returnList;

    }

    public List<Catalog> getUserCatalog(String uId) {
        List<Catalog> userCatalog;
        try {
            Catalog root = cDao.getRoot(uId);
            userCatalog = cDao.findUserCatalog(uId);
            List<Catalog> returnList = new ArrayList<>();
            boolean rootFlag = false;
            List<Catalog> tree = tree(userCatalog, root.getcId(), returnList, rootFlag);
            return tree;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean move(String movePath, String fid) {
        try {
            return cDao.updatePath(movePath, fid) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean moveFolder(String movePath, String cid, int level, String uid) {
        try {
            boolean b = cDao.updateFolderPath(movePath, cid, level) > 0;
            List<Catalog> userCatalog = getUserCatalog(uid);
            Optional<Catalog> first = userCatalog.stream().filter(e -> cid.equals(e.getcId())).findFirst();
            if (first.isPresent()) {
                Catalog catalog = first.get();
                List<Catalog> children = catalog.getChildren();
                if (!children.isEmpty()) {
                    increaseChildrenLevel(cid, children);
                }
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void increaseChildrenLevel(String id, List<Catalog> userCatalogs) {
        userCatalogs.forEach((c) -> {
            try {
                cDao.updateLevel(c.getcId(), c.getParent().getcLevel() + 1);
                c.setcLevel(c.getParent().getcLevel() + 1);
                if (!c.getChildren().isEmpty()) {
                    increaseChildrenLevel(c.getcId(), c.getChildren());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        //find the node what I want
 /*       userCatalogs.forEach((e) -> {
            if (e.getcId().equals(id)) {
                //find the chirden of current node
                e.getChildren().forEach((c) -> {
                    try {
                        cDao.updateLevel(c.getcId(),c.getParent().getcLevel()+1);
                        c.setcLevel(c.getParent().getcLevel()+1);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (!c.getChildren().isEmpty()) {
                        //pass current node id to do the recursive again.
                        increaseChildrenLevel(c.getcId(), userCatalogs);
                    }
                });
            }
        });*/
    }
}
