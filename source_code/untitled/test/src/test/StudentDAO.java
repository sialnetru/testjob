package test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * Created by admin on 28.07.2015.
 */
public class StudentDAO {
    private Connection c;
    private String table = "student";

    public StudentDAO(String path) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            c = DriverManager.getConnection("jdbc:hsqldb:file:"+path, "SA", "");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (java.sql.SQLException e2) {
            e2.printStackTrace();
        }
    }

    public void updateStudent(long id, long s_id, String s_name, String s_surname, String s_middlename, Date s_birthdate, long s_group) throws SQLException {
        String sql = "UPDATE student SET s_id=?,s_name=?,s_surname=?,s_middlename=?,s_birthdate=?,s_group=? WHERE s_id=?";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        preparedStatement.setLong(1, s_id);
        preparedStatement.setString(2, s_name);
        preparedStatement.setString(3, s_surname);
        preparedStatement.setString(4, s_middlename);
        preparedStatement.setDate(5, s_birthdate);
        preparedStatement.setLong(6, s_group);
        preparedStatement.setLong(7, id);
        preparedStatement.executeUpdate();
    }

    public void insertStudent(long s_id, String s_name, String s_surname, String s_middlename, Date s_birthdate, long s_group) throws SQLException {
        String sql = "INSERT INTO student (s_id,s_name,s_surname,s_middlename,s_birthdate,s_group) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        preparedStatement.setLong(1, s_id);
        preparedStatement.setString(2, s_name);
        preparedStatement.setString(3, s_surname);
        preparedStatement.setString(4, s_middlename);
        preparedStatement.setDate(5, s_birthdate);
        preparedStatement.setLong(6, s_group);
        preparedStatement.executeUpdate();
    }

    public void deleteStudent(long id) throws SQLException {
        Statement st = c.createStatement();
        st.executeUpdate("DELETE FROM student WHERE s_id=" + id);
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
}
