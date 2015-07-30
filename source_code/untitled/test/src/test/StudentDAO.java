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
    private String path;

    public StudentDAO(String path) {
        this.path = path;
    }

    public void getCon() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        c = DriverManager.getConnection("jdbc:hsqldb:file:" + path, "SA", "");
    }

    public void closeCon() throws SQLException {
        c.close();
        c = null;
    }

    public PreparedStatement getPS(String sql) throws Exception {
        getCon();
        return c.prepareStatement(sql);
    }

    public Statement getS() throws Exception {
        getCon();
        return c.createStatement();
    }

    public boolean updateStudent(long id, long s_id, String s_name, String s_surname, String s_middlename, Date s_birthdate, long s_group) {
        try {
            String sql = "UPDATE student SET s_id=?,s_name=?,s_surname=?,s_middlename=?,s_birthdate=?,s_group=? WHERE s_id=?";
            PreparedStatement preparedStatement = getPS(sql);
            preparedStatement.setLong(1, s_id);
            preparedStatement.setString(2, s_name);
            preparedStatement.setString(3, s_surname);
            preparedStatement.setString(4, s_middlename);
            preparedStatement.setDate(5, s_birthdate);
            preparedStatement.setLong(6, s_group);
            preparedStatement.setLong(7, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            closeCon();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertStudent(long s_id, String s_name, String s_surname, String s_middlename, Date s_birthdate, long s_group) {
        try {
            String sql = "INSERT INTO student (s_id,s_name,s_surname,s_middlename,s_birthdate,s_group) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = getPS(sql);
            preparedStatement.setLong(1, s_id);
            preparedStatement.setString(2, s_name);
            preparedStatement.setString(3, s_surname);
            preparedStatement.setString(4, s_middlename);
            preparedStatement.setDate(5, s_birthdate);
            preparedStatement.setLong(6, s_group);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            closeCon();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteStudent(long id) {
        try {
            Statement st = getS();
            st.executeUpdate("DELETE FROM student WHERE s_id=" + id);
            st.close();
            closeCon();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public JTable getTable(){

        JTable t1 = new JTable();
        DefaultTableModel dm = new DefaultTableModel();
        try {
            Statement st = getS();
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
            st.close();
            closeCon();
        } catch (Exception e) {
            e.printStackTrace();
        }
        t1.setModel(dm);
        return t1;
    }
}
