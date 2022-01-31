package com.vincent.File.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import com.vincent.File.dao.FileDao;
import com.vincent.File.domain.File;
import com.vincent.File.domain.ShareList;
import com.vincent.user.dao.UserDao;
import com.vincent.user.domain.User;

import cn.itcast.jdbc.JdbcUtils;


public class FileService {
    private FileDao fileDao = new FileDao();
    private UserDao userDao = new UserDao();

    public List<File> findByCf(String cf) {
        try {
            return fileDao.findByCf(cf);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public File findByFid(String fid) {
        try {
            return fileDao.findByFid(fid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void upLoadFile(File file, User user) {
        try {
            JdbcUtils.beginTransaction();
            fileDao.upLoadFile(file);
            user.setFileSize(file.getfSize() + "");
            userDao.updateSizeByUid(user, 0);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }

    public void writecf(String fid, String cf) {
        try {
            fileDao.writecf(fid, cf);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean deleteByFid(String fid, User user) {
        boolean result = false;
        try {
            JdbcUtils.beginTransaction();
            File file = fileDao.findByFid(fid);
            user.setFileSize(file.getfSize() + "");
            userDao.updateSizeByUid(user, 1);
            result = fileDao.deleteByFid(fid);
            JdbcUtils.commitTransaction();
        } catch (Exception e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        }
        return result;
    }

    public File findByHash(String hash) {
        try {
            return fileDao.findByHash(hash);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<File> findByPath(String path) {
        try {
            return fileDao.findByFpath(path);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 修改downcount
    public void editcount(String count, String fid) {
        try {
            fileDao.editFile(count, fid);
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public boolean updateNameByFid(String fId, String name) {
        try {
            int updateNameByFid = fileDao.updateNameByFid(fId, name);
            if (updateNameByFid > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upateHashByFid(String fId, String hash) {
        try {
            return fileDao.updateHashByFid(fId, hash) > 0 ? true : false;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public boolean createShareItem(ShareList shareList) {

        try {
            return fileDao.createShareItem(shareList) > 0 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
