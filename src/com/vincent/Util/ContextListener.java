package com.vincent.Util;

import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        InputStream resourceAsStream = servletContextEvent.getServletContext().getResourceAsStream("WEB-INF\\classes\\drive.sql");
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(resourceAsStream, "UTF-8");//使用系统默认的字符集
//            char[] cbuf = new char[20];
//            int len;
//            while ((len = isr.read(cbuf)) != -1) {
//                stringBuilder.append(new String(cbuf, 0, len));
//            }
//            System.out.println(stringBuilder);
            Connection connection = JdbcUtils.getConnection();
 /*           Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("CREATE TABLE IF NOT EXISTS `catalog` (")
                    .append(" `cId` varchar(50) NOT NULL,")
                    .append("  `pId` varchar(50) DEFAULT NULL,")
                    .append("  `cName` varchar(50) DEFAULT NULL,")
                    .append("  `cDate` varchar(50) DEFAULT NULL,")
                    .append("  `cF` varchar(50) DEFAULT NULL,")
                    .append("  `isShare` varchar(2) DEFAULT NULL,")
                    .append("  `cLevel` int(11) DEFAULT NULL,")
                    .append("  `uId` varchar(255) NOT NULL,")
                    .append("  PRIMARY KEY (`cId`)")
                    .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            stmt.execute(stringBuffer.toString());*/
            ScriptRunner scriptRunner = new ScriptRunner(connection, false, true);
            scriptRunner.runScript(isr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("MyServletContextListener Destoryed");
    }
}
