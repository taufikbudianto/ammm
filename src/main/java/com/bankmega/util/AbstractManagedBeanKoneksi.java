package com.bankmega.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : taufik.budiyanto
 * @date : 14/09/2018  8:32
 */
@Component
public abstract class AbstractManagedBeanKoneksi implements Serializable {

    @Value("${mega.driver}")
    private String driverMysql;
    @Value("${mega.urldatabase}")
    private String urlMysql;
    @Value("${mega.userdatabase}")
    private String usernameMysql;
    @Value("${mega.passdatabase}")
    private String passMysql;
    private static final String url="jdbc:jtds:sqlserver://10.11.88.218:1433/stg_host";
    private static final String username="ams";
    private static final String pass="amsapp";
    private static final String driver="net.sourceforge.jtds.jdbc.Driver";

    protected Connection getKoneksiMysql(){
        Connection con =null;
        try {
            Class.forName(driverMysql);
            con= DriverManager.getConnection(urlMysql,usernameMysql,passMysql);
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            e.getMessage();
        }
        return con;
    }
    protected  Connection getKoneksi(){
        Connection con=null;
        try {
            Class.forName(driver);
            con=DriverManager.getConnection(url,username,pass);
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            e.getMessage();
        }
        return con;
    }

}
