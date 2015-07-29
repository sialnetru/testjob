package test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * Created by admin on 28.07.2015.
 */
public class GroupDAO {
    private Connection c;
    private String table = "sgroup";

    public GroupDAO(String path) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            c = DriverManager.getConnection("jdbc:hsqldb:file:"+path, "SA", "");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (java.sql.SQLException e2) {
            e2.printStackTrace();
        }
    }

    public void closeCon() {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JTable getTable() throws Exception {
        JTable t1 = new JTable();
        DefaultTableModel dm = new DefaultTableModel();
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery("select * from " + table);
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        String c[] = new String[cols];
        for (int i = 0; i < cols; i++) {
            c[i] = rsmd.getColumnName(i + 1);
            dm.addColumn(c[i]);
        }
        Object row[] = new Object[cols];
        while (rs.next()) {
            for (int i = 0; i < cols; i++) {
                row[i] = rs.getString(i + 1);
            }
            dm.addRow(row);
        }
        t1.setModel(dm);
        return t1;
    }

    public JTable getTable(String query) throws Exception {
        JTable t1 = new JTable();
        DefaultTableModel dm = new DefaultTableModel();
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        String c[] = new String[cols];
        for (int i = 0; i < cols; i++) {
            c[i] = rsmd.getColumnName(i + 1);
            dm.addColumn(c[i]);
        }
        Object row[] = new Object[cols];
        while (rs.next()) {
            for (int i = 0; i < cols; i++) {
                row[i] = rs.getString(i + 1);
            }
            dm.addRow(row);
        }
        t1.setModel(dm);
        return t1;
    }

    public void deleteGroup(long id) throws SQLException {
        Statement st = c.createStatement();
        st.executeUpdate("DELETE FROM sgroup WHERE g_number=" + id);
    }

    public void insertGroup(long id, String faculty) throws SQLException {
        String sql = "INSERT INTO sgroup (g_number,g_faculty) VALUES (?,?)";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, faculty);
        preparedStatement.executeUpdate();
    }

    public void updateGroup(long id, long new_id, String new_faculty) throws SQLException {
        String sql = "UPDATE sgroup SET g_number=?,g_faculty=? WHERE g_number=?";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        preparedStatement.setLong(1, new_id);
        preparedStatement.setString(2, new_faculty);
        preparedStatement.setLong(3, id);
        preparedStatement.executeUpdate();
    }

}
