package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Andy Li
 */
public class MyDatabase {

    private Connection conn;
    private PreparedStatement stmt;

    public MyDatabase() {  // obtain database parameters from configuration file

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/serler", "root", "1234");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void closeConn() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception while closing: " + e);
        }
    }

    public PreparedStatement getStmt(String s) {
        try {
            stmt = conn.prepareStatement(s);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return stmt;
    }

    public String getAverage(String table, String id) throws SQLException {

        double sum = 0;
        int count = 0;
        DecimalFormat df = new DecimalFormat("####0.0");
        try {
            stmt = conn.prepareStatement("SELECT * FROM " + table + " WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                sum += Integer.parseInt(rs.getString("Rating"));
                count++;
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        if (sum == 0) {
            return "not avaliable";
        } else {
            return df.format(sum / count);
        }
    }
}
