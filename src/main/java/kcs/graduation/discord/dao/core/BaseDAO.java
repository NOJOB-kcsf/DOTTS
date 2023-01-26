package kcs.graduation.discord.dao.core;

import kcs.graduation.discord.parameter.DAOParameters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDAO {
    public Connection con;

    protected void open() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DAOParameters.CONNECT_STRING.getParam(), DAOParameters.USER_ID.getParam(), DAOParameters.PASS_WORD.getParam());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected void close(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
