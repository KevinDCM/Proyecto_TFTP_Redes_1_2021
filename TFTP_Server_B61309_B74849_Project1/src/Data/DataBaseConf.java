package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DataBaseConf {

    public final String driverame = "com.microsoft.sqlserver.jbdc.SQLServerDriver";
    public String sql_url = "jdbc:sqlserver://localhost:1440;databasename=B61309_B74849_Redes;user=kdcm;password=1234ABCD";
    //"jdbc:sqlserver://Server=163.178.107.10;Initial Catalog=B61309_B74849_Redes;User ID=laboratorios;Password=KmZpo.2796;Integrated Security=True;";
    //"Server=163.178.107.10;Initial Catalog=B61309_2021;User ID=laboratorios;Password=KmZpo.2796"


    public Connection getSQLConnection() throws ClassNotFoundException, SQLException {
        Connection conn = DriverManager.getConnection(sql_url);
        return conn;

    }

}