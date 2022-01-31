package com.vincent.Catalog.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletContext;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.vincent.Catalog.domain.Catalog;
import com.vincent.File.dao.FileDao;
import com.vincent.File.domain.File;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class CatalogDao {
    private QueryRunner qr = new TxQueryRunner();
    private FileDao fileDao = new FileDao();

    // 给我一个cid，就可以查找下面一级的目录和文件
    public Catalog findByCidToCatalog(String cid) throws SQLException {
        String sql = "select * from catalog where cId=?";
        Map<String, Object> beanMap = qr.query(sql, new MapHandler(), cid);
        Catalog catalog = CommonUtils.toBean(beanMap, Catalog.class);
        if (beanMap == null) {
            return null;
        }
        // 还有pid cF 这个属性就行映射
        if (beanMap.get("pId") != null) {
            // 这不是最顶层目录 需要封装他的父级目录 只有pid
            Catalog c = new Catalog();
            c.setcId((String) beanMap.get("pId"));
            catalog.setParent(c);
        }

        sql = "select * from catalog where pId=?";
        List<Map<String, Object>> beanMapList = qr.query(sql,
                new MapListHandler(), catalog.getcId());

        List<Catalog> cataList = this.listToBean(beanMapList);
        catalog.setChildren(cataList);
        // 开始封装catalog 的子文件

        String cf = (String) beanMap.get("cF");
        List<File> myfile = fileDao.findByCf(cf);
        catalog.setMyFile(myfile);

        return catalog;
    }

    // 给我一个list map 集合 我帮你转换成 list bean map-bean
    public List<Catalog> listToBean(List<Map<String, Object>> mapList) {
        List<Catalog> toList = new ArrayList<>();

        for (Map<String, Object> myMap : mapList) {
            Optional<Catalog> mapBean = toList.stream().filter((e) -> e.getcId().equals(myMap.get("cId"))).findFirst();
            boolean needAdd = true;
            if (mapBean.isPresent())
                needAdd = false;
            Catalog outsideCatalog = mapBean.orElseGet(() -> CommonUtils.toBean(myMap, Catalog.class));
            if (myMap.get("pId") != null) {
                //find the father from result list
                Optional<Catalog> pId = toList.stream().filter((e) -> e.getcId().equals(myMap.get("pId"))).findFirst();
                if (pId.isPresent()) {
                    Catalog catalog = pId.get();
                    outsideCatalog.setParent(catalog);
                } else {
                    //find the father from map
                    Optional<Map<String, Object>> first1 = mapList.stream()
                            .filter((e) -> e.get("cId").equals(myMap.get("pId"))).findFirst();
                    if (first1.isPresent()) {
                        Catalog father = CommonUtils.toBean(first1.get(), Catalog.class);
                        outsideCatalog.setParent(father);
                        toList.add(father);
                    }
                }
            }
            if (needAdd)
                toList.add(outsideCatalog);
        }
        return toList;
    }

    // 通过cid 你bean 一个对象
    public Catalog findByCid(String cid) throws SQLException {
        String sql = "select * from catalog where cId=?";
        return qr.query(sql, new BeanHandler<Catalog>(Catalog.class), cid);
    }

    public void createCatalog(Catalog c) throws SQLException {
        String sql = "insert into catalog (cId,pId,cName,cDate,isShare,cLevel,uId,cF) values(?,?,?,?,?,?,?,?)";
        Object[] para = {c.getcId(),
                c.getParent() == null ? null : c.getParent().getcId(),
                c.getcName(), c.getcDate(), c.getIsShare(), c.getcLevel(),
                c.getuId(), c.getcF()};
        qr.update(sql, para);
    }

    // 通过cid 找到cf值
    public String cidTocf(String cid) throws SQLException {
        String sql = "select cf from catalog where cId=?";
        return (String) qr.query(sql, new ScalarHandler(), cid);
    }

    // 写入cf值
    public void intoCf(String cid, String cf) throws SQLException {
        String sql = "update catalog set cF=? where cId=?";
        qr.update(sql, cf, cid);
    }

    /*
     * 给cid 删除他后面所有的文件和文件夹 需要遍历数 递归
     */
    public void deleteByCatalog(String cid, ServletContext context)
            throws SQLException {

        Catalog cata = this.findByCidToCatalog(cid);

        // 先删除该目录下的文件
        if (cata.getMyFile() != null) {

            System.out.println("开始删除纯文件");
            fileDao.removeAll(cata.getMyFile(), context);
            // 还要删除本地文件
        }

        String sql = "select cF from catalog where cId=?";
        String cf = (String) qr.query(sql, new ScalarHandler(), cata.getcId());
        sql = "delete from catalog_file where cf=?";
        qr.update(sql, cf);

        sql = "delete from catalog where cId=?";
        qr.update(sql, cata.getcId());
        List<Catalog> chileList = cata.getChildren();

        for (Catalog c : chileList) {
            deleteByCatalog(c.getcId(), context);
        }

        return;
    }

    public int updateNameByCid(String cid, String name) throws SQLException {
        String sql = "update catalog set cName=? where cId=?";
        return qr.update(sql, name, cid);
    }

    public List<Catalog> findUserCatalog(String uId) throws SQLException {
        String sql = "select * from catalog where uId =? order by cLevel";
        List<Map<String, Object>> beanMapList = qr.query(sql,
                new MapListHandler(), uId);
        List<Catalog> cataList = this.listToBean(beanMapList);
        //System.out.println(cataList);
        return cataList;
    }

    public Catalog getRoot(String uId) throws SQLException {
        String sql = "select * from catalog where uId =? and cName = ?";
        Map<String, Object> beanMap = qr.query(sql, new MapHandler(), uId,
                "root");
        Catalog catalog = CommonUtils.toBean(beanMap, Catalog.class);
        return catalog;
    }

    public void testDelete() throws Exception {

    }

    public int updatePath(String movePath, String file) throws Exception {
        String sql = "update catalog_file set cF=? where fId=?";
        return qr.update(sql, movePath, file);
    }

    public int updateFolderPath(String movePath, String folder, int level) throws Exception {
        String sql = "update catalog set pId=?, cLevel=? where cId=?";
        return qr.update(sql, movePath, level, folder);
    }

    public int updateLevel(String folder, int level) throws Exception {
        String sql = "update catalog set cLevel=? where cId=?";
        return qr.update(sql, level, folder);
    }
}
